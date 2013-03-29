package net.minecraft.src.nbxlite;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;

public class BoundChunk extends Chunk
{
    public BoundChunk(World par1World, int par2, int par3)
    {
        super(par1World, par2, par3);
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    @Override
    public boolean isAtLocation(int par1, int par2)
    {
        return par1 == xPosition && par2 == zPosition;
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    @Override
    public int getHeightValue(int par1, int par2)
    {
        return 0;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    @Override
    public void generateHeightMap()
    {
    }

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    @Override
    public void generateSkylightMap()
    {
    }

    /**
     * Return the ID of a block in the chunk.
     */
    @Override
    public int getBlockID(int par1, int par2, int par3)
    {
        return ODNBXlite.getBlockIdInBounds(par2);
    }

    @Override
    public int getBlockLightOpacity(int par1, int par2, int par3)
    {
        return Block.lightOpacity[getBlockID(par1, par2, par3)];
    }

    /**
     * Sets a blockID of a position within a chunk with metadata. Args: x, y, z, blockID, metadata
     */
    @Override
    public boolean setBlockIDWithMetadata(int par1, int par2, int par3, int i, int j)
    {
        return false;
    }

    /**
     * Return the metadata corresponding to the given coordinates inside a chunk.
     */
    @Override
    public int getBlockMetadata(int par1, int par2, int par3)
    {
        return 0;
    }

    /**
     * Set the metadata of a block in the chunk
     */
    @Override
    public boolean setBlockMetadata(int par1, int par2, int par3, int i)
    {
        return false;
    }

    /**
     * Gets the amount of light saved in this block (doesn't adjust for daylight)
     */
    @Override
    public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int i)
    {
        if (par1EnumSkyBlock==EnumSkyBlock.Sky){
            return ODNBXlite.getSkyLightInBounds(par3);
        }
        return ODNBXlite.getBlockLightInBounds(par3);
    }

    /**
     * Sets the light value at the coordinate. If enumskyblock is set to sky it sets it in the skylightmap and if its a
     * block then into the blocklightmap. Args enumSkyBlock, x, y, z, lightValue
     */
    @Override
    public void setLightValue(EnumSkyBlock enumskyblock, int i, int j, int k, int l)
    {
    }

    /**
     * Gets the amount of light on a block taking into account sunlight
     */
    @Override
    public int getBlockLightValue(int par1, int par2, int par3, int i)
    {
        return Math.max(ODNBXlite.getSkyLightInBounds(par2), ODNBXlite.getBlockLightInBounds(par2));
    }

    /**
     * Returns whether is not a block above this one blocking sight to the sky (done via checking against the heightmap)
     */
    @Override
    public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return false;
    }

    /**
     * Gets the TileEntity for a given block in this chunk
     */
    @Override
    public TileEntity getChunkBlockTileEntity(int par1, int par2, int par3)
    {
        return null;
    }

    /**
     * Adds a TileEntity to a chunk
     */
    @Override
    public void addTileEntity(TileEntity tileentity)
    {
    }

    /**
     * Sets the TileEntity for a given block in this chunk
     */
    @Override
    public void setChunkBlockTileEntity(int i, int j, int k, TileEntity tileentity)
    {
    }

    /**
     * Removes the TileEntity for a given block in this chunk
     */
    @Override
    public void removeChunkBlockTileEntity(int i, int j, int k)
    {
    }

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    @Override
    public void onChunkLoad()
    {
    }

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    @Override
    public void onChunkUnload()
    {
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    @Override
    public void setChunkModified()
    {
    }

    /**
     * Returns true if this Chunk needs to be saved
     */
    @Override
    public boolean needsSaving(boolean par1)
    {
        return false;
    }

    @Override
    public Random getRandomWithSeed(long par1)
    {
        return new Random(worldObj.getSeed() + (long)(xPosition * xPosition * 0x4c1906) + (long)(xPosition * 0x5ac0db) + (long)(zPosition * zPosition) * 0x4307a7L + (long)(zPosition * 0x5f24f) ^ par1);
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    @Override
    public boolean getAreLevelsEmpty(int par1, int par2)
    {
        return true;
    }
}
