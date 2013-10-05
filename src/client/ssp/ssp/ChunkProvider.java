package net.minecraft.src.ssp;

import java.io.IOException;
import java.util.*;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.EmptyChunk;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.LongHashMap;
import net.minecraft.src.Minecraft;
import net.minecraft.src.World;

public class ChunkProvider implements IChunkProvider
{
    /** A set of dropped chunks. Currently not used in single player. */
    private Set droppedChunksSet;
    private Chunk emptyChunk;

    /** The parent IChunkProvider for this ChunkProvider. */
    private IChunkProvider chunkProvider;

    /** The IChunkLoader used by this ChunkProvider */
    private IChunkLoader chunkLoader;

    /**
     * A map of all the currently loaded chunks, uses the chunk id as the key.
     */
    private LongHashMap chunkMap;

    /** A list of all the currently loaded chunks. */
    private List chunkList;

    /** The World object which this ChunkProvider was constructed with */
    private WorldSSP worldObj;
    private int field_35392_h;

    public ChunkProvider(WorldSSP par1World, IChunkLoader par2IChunkLoader, IChunkProvider par3IChunkProvider)
    {
        droppedChunksSet = new HashSet();
        chunkMap = new LongHashMap();
        chunkList = new ArrayList();
        emptyChunk = new EmptyChunk(par1World, 0, 0);
        worldObj = par1World;
        chunkLoader = par2IChunkLoader;
        chunkProvider = par3IChunkProvider;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    @Override
    public boolean chunkExists(int par1, int par2)
    {
        return chunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
    }

    /**
     * Drops the specified chunk.
     */
    public void dropChunk(int par1, int par2)
    {
        ChunkCoordinates chunkcoordinates = worldObj.getSpawnPoint();
        int i = (par1 * 16 + 8) - chunkcoordinates.posX;
        int j = (par2 * 16 + 8) - chunkcoordinates.posZ;
        char c = '\200';

        if (i < -c || i > c || j < -c || j > c)
        {
            droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(par1, par2)));
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    @Override
    public Chunk loadChunk(int par1, int par2)
    {
        long l = ChunkCoordIntPair.chunkXZ2Int(par1, par2);
        droppedChunksSet.remove(Long.valueOf(l));
        Chunk chunk = (Chunk)chunkMap.getValueByKey(l);

        if (chunk == null)
        {
            int i = 0x1c9c3c;

            if (par1 < -i || par2 < -i || par1 >= i || par2 >= i)
            {
                return emptyChunk;
            }

            chunk = loadChunkFromFile(par1, par2);

            if (chunk == null)
            {
                if (chunkProvider == null)
                {
                    chunk = emptyChunk;
                }
                else
                {
                    chunk = chunkProvider.provideChunk(par1, par2);
                }
            }

            chunkMap.add(l, chunk);
            chunkList.add(chunk);

            if (chunk != null)
            {
                chunk.onChunkLoad();
            }

            chunk.populateChunk(this, this, par1, par2);
        }

        return chunk;
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    @Override
    public Chunk provideChunk(int par1, int par2)
    {
        Chunk chunk = (Chunk)chunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(par1, par2));
        return chunk != null ? chunk : loadChunk(par1, par2);
    }

    /**
     * Attemps to load the chunk from the save file, returns null if the chunk is not available.
     */
    private Chunk loadChunkFromFile(int par1, int par2)
    {
        if (chunkLoader == null)
        {
            return null;
        }

        try
        {
            Chunk chunk = chunkLoader.loadChunk(worldObj, par1, par2);

            if (chunk != null)
            {
                chunk.lastSaveTime = worldObj.getTotalWorldTime();
            }

            return chunk;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        return null;
    }

    private void saveChunkExtraData(Chunk par1Chunk)
    {
        if (chunkLoader != null)
        {
            try
            {
                chunkLoader.saveExtraChunkData(worldObj, par1Chunk);
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
    }

    private void saveChunkData(Chunk par1Chunk)
    {
        if (chunkLoader != null)
        {
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
                Minecraft.invokeModMethod("ModLoader", "populateChunk",
                                          new Class[]{IChunkProvider.class, Integer.TYPE, Integer.TYPE, World.class},
                                          chunkProvider, par2, par3, worldObj);
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

        for (int j = 0; j < chunkList.size(); j++)
        {
            Chunk chunk = (Chunk)chunkList.get(j);

            if (par1)
            {
                saveChunkExtraData(chunk);
            }

            if (chunk.needsSaving(par1))
            {
                saveChunkData(chunk);
                chunk.isModified = false;

                if (++i == 24 && !par1)
                {
                    return false;
                }
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
        for (int i = 0; i < 100; i++)
        {
            if (!droppedChunksSet.isEmpty())
            {
                Long long1 = (Long)droppedChunksSet.iterator().next();
                Chunk chunk1 = (Chunk)chunkMap.getValueByKey(long1.longValue());
                chunk1.onChunkUnload();
                saveChunkData(chunk1);
                saveChunkExtraData(chunk1);
                droppedChunksSet.remove(long1);
                chunkMap.remove(long1.longValue());
                chunkList.remove(chunk1);
            }
        }

        for (int j = 0; j < 10; j++)
        {
            if (field_35392_h >= chunkList.size())
            {
                field_35392_h = 0;
                break;
            }

            Chunk chunk = (Chunk)chunkList.get(field_35392_h++);
            EntityPlayer entityplayer = worldObj.func_48456_a((double)(chunk.xPosition << 4) + 8D, (double)(chunk.zPosition << 4) + 8D, 288D);

            if (entityplayer == null)
            {
                dropChunk(chunk.xPosition, chunk.zPosition);
            }
        }

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
        return (new StringBuilder("ServerChunkCache: ")).append(chunkMap.getNumHashElements()).append(" Drop: ").append(droppedChunksSet.size()).toString();
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
        return chunkList.size();
    }

    @Override
    public void recreateStructures(int par1, int par2)
    {
    }

    public IChunkProvider getChunkProvider(){
        return chunkProvider;
    }

    @Override
    public void saveExtraData()
    {
    }
}
