package net.minecraft.src.ssp;

import java.io.IOException;
import java.util.List;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCache;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.World;

public class ChunkProviderLoadOrGenerate implements IChunkProvider
{
    /**
     * A completely empty Chunk, used by ChunkProviderLoadOrGenerate when there's no ChunkProvider.
     */
    private Chunk blankChunk;

    /** The parent IChunkProvider for this ChunkProviderLoadOrGenerate. */
    private IChunkProvider chunkProvider;

    /** The IChunkLoader used by this ChunkProviderLoadOrGenerate. */
    private IChunkLoader chunkLoader;
    private Chunk chunks[];

    /** Reference to the World object. */
    private World worldObj;

    /** The last X position of a chunk that was returned from setRecordPlayingMessage */
    int lastQueriedChunkXPos;

    /** The last Z position of a chunk that was returned from setRecordPlayingMessage */
    int lastQueriedChunkZPosition;

    /** The last Chunk that was returned from setRecordPlayingMessage */
    private Chunk lastQueriedChunk;

    /** The current chunk the player is over */
    private int curChunkX;

    /** The current chunk the player is over */
    private int curChunkY;

    /**
     * This is the chunk that the player is currently standing over. Args: chunkX, chunkZ
     */
    public void setCurrentChunkOver(int par1, int par2)
    {
        curChunkX = par1;
        curChunkY = par2;
    }

    /**
     * Checks if the chunk coordinate could actually be stored within the chunk cache. Args: chunkX, chunkZ
     */
    public boolean canChunkExist(int par1, int par2)
    {
        byte byte0 = 15;
        return par1 >= curChunkX - byte0 && par2 >= curChunkY - byte0 && par1 <= curChunkX + byte0 && par2 <= curChunkY + byte0;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    @Override
    public boolean chunkExists(int par1, int par2)
    {
        if (!canChunkExist(par1, par2))
        {
            return false;
        }

        if (par1 == lastQueriedChunkXPos && par2 == lastQueriedChunkZPosition && lastQueriedChunk != null)
        {
            return true;
        }
        else
        {
            int i = par1 & 0x1f;
            int j = par2 & 0x1f;
            int k = i + j * 32;
            return chunks[k] != null && (chunks[k] == blankChunk || chunks[k].isAtLocation(par1, par2));
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    @Override
    public Chunk loadChunk(int par1, int par2)
    {
        return provideChunk(par1, par2);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(int par1, int par2)
    {
        if (par1 == lastQueriedChunkXPos && par2 == lastQueriedChunkZPosition && lastQueriedChunk != null)
        {
            return lastQueriedChunk;
        }

        if (!worldObj.findingSpawnPoint && !canChunkExist(par1, par2))
        {
            return blankChunk;
        }

        int i = par1 & 0x1f;
        int j = par2 & 0x1f;
        int k = i + j * 32;

        if (!chunkExists(par1, par2))
        {
            if (chunks[k] != null)
            {
                chunks[k].onChunkUnload();
                saveChunk(chunks[k]);
                saveExtraChunkData(chunks[k]);
            }

            Chunk chunk = func_542_c(par1, par2);

            if (chunk == null)
            {
                if (chunkProvider == null)
                {
                    chunk = blankChunk;
                }
                else
                {
                    chunk = chunkProvider.provideChunk(par1, par2);
                }
            }

            chunks[k] = chunk;

            if (chunks[k] != null)
            {
                chunks[k].onChunkLoad();
            }

            chunks[k].populateChunk(this, this, par1, par2);
        }

        lastQueriedChunkXPos = par1;
        lastQueriedChunkZPosition = par2;
        lastQueriedChunk = chunks[k];
        return chunks[k];
    }

    private Chunk func_542_c(int par1, int par2)
    {
        if (chunkLoader == null)
        {
            return blankChunk;
        }

        try
        {
            Chunk chunk = chunkLoader.loadChunk(worldObj, par1, par2);

            if (chunk != null)
            {
                chunk.lastSaveTime = worldObj.getTotalWorldTime();

                if (chunkProvider != null)
                {
                    chunkProvider.recreateStructures(par1, par2);
                }
            }

            return chunk;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return blankChunk;
    }

    /**
     * Save extra data associated with this Chunk not normally saved during autosave, only during chunk unload.
     * Currently unused.
     */
    private void saveExtraChunkData(Chunk par1Chunk)
    {
        if (chunkLoader == null)
        {
            return;
        }

        try
        {
            chunkLoader.saveExtraChunkData(worldObj, par1Chunk);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Save a given Chunk, recording the time in lastSaveTime
     */
    private void saveChunk(Chunk par1Chunk)
    {
        if (chunkLoader == null)
        {
            return;
        }

        try
        {
            par1Chunk.lastSaveTime = worldObj.getTotalWorldTime();
            chunkLoader.saveChunk(worldObj, par1Chunk);
        }
        catch (Exception ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    /**
     * Populates chunk with ores etc etc
     */
    @Override
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {
        Chunk chunk = provideChunk(par2, par3);

        if (!chunk.isTerrainPopulated)
        {
            chunk.isTerrainPopulated = true;

            if (chunkProvider != null)
            {
                chunkProvider.populate(par1IChunkProvider, par2, par3);
                chunk.setChunkModified();
            }
        }
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    @Override
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        int i = 0;
        int j = 0;

        if (par2IProgressUpdate != null)
        {
            for (int k = 0; k < chunks.length; k++)
            {
                if (chunks[k] != null && chunks[k].needsSaving(par1))
                {
                    j++;
                }
            }
        }

        int l = 0;

        for (int i1 = 0; i1 < chunks.length; i1++)
        {
            if (chunks[i1] == null)
            {
                continue;
            }

            if (par1)
            {
                saveExtraChunkData(chunks[i1]);
            }

            if (!chunks[i1].needsSaving(par1))
            {
                continue;
            }

            saveChunk(chunks[i1]);
            chunks[i1].isModified = false;

            if (++i == 2 && !par1)
            {
                return false;
            }

            if (par2IProgressUpdate != null && ++l % 10 == 0)
            {
                par2IProgressUpdate.setLoadingProgress((l * 100) / j);
            }
        }

        if (par1)
        {
            if (chunkLoader == null)
            {
                return true;
            }

            chunkLoader.saveExtraData();
        }

        return true;
    }

    /**
     * Unloads the 100 oldest chunks from memory, due to a bug with chunkSet.add() never being called it thinks the list
     * is always empty and will not remove any chunks.
     */
    @Override
    public boolean unloadQueuedChunks()
    {
        if (chunkLoader != null)
        {
            chunkLoader.chunkTick();
        }

        return chunkProvider.unloadQueuedChunks();
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    @Override
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    @Override
    public String makeString()
    {
        return (new StringBuilder()).append("ChunkCache: ").append(chunks.length).toString();
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    @Override
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        return chunkProvider.getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    @Override
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int par4, int par5)
    {
        return chunkProvider.findClosestStructure(par1World, par2Str, par3, par4, par5);
    }

    @Override
    public int getLoadedChunkCount()
    {
        return 0;
    }

    @Override
    public void recreateStructures(int par1, int par2)
    {
    }

    @Override
    public void saveExtraData()
    {
    }
}
