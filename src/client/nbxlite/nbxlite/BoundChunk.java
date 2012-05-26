package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BoundChunk extends Chunk
{
    public BoundChunk(World par1World, int par2, int par3)
    {
        super(par1World, par2, par3);
    }

    /**
     * Checks whether the chunk is at the X/Z location specified
     */
    public boolean isAtLocation(int par1, int par2)
    {
        return par1 == xPosition && par2 == zPosition;
    }

    /**
     * Returns the value in the height map at this x, z coordinate in the chunk
     */
    public int getHeightValue(int par1, int par2)
    {
        return 0;
    }

    /**
     * Generates the height map for a chunk from scratch
     */
    public void generateHeightMap()
    {
    }

    /**
     * Generates the initial skylight map for the chunk upon generation or load.
     */
    public void generateSkylightMap()
    {
    }

    public void func_4143_d()
    {
    }

    /**
     * Return the ID of a block in the chunk.
     */
    public int getBlockID(int par1, int par2, int par3)
    {
        if (par2<mod_noBiomesX.SurrGroundHeight-1){
            return Block.bedrock.blockID;
        }
        if (par2<mod_noBiomesX.SurrGroundHeight){
            if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_CLASSIC){
                return Block.bedrock.blockID;
            }
            if ((par2<mod_noBiomesX.SurrWaterHeight || mod_noBiomesX.SurrWaterType==Block.lavaStill.blockID) && mod_noBiomesX.SurrGroundType==Block.grass.blockID){
                return Block.dirt.blockID;
            }
            return mod_noBiomesX.SurrGroundType;
        }
        if (par2<mod_noBiomesX.SurrWaterHeight){
            return mod_noBiomesX.SurrWaterType;
        }
        return 0;
    }

    public int getBlockLightOpacity(int par1, int par2, int par3)
    {
        return Block.lightOpacity[getBlockID(par1, par2, par3)];
    }

    /**
     * Sets a blockID of a position within a chunk with metadata. Args: x, y, z, blockID, metadata
     */
    public boolean setBlockIDWithMetadata(int par1, int par2, int par3, int i, int j)
    {
        return false;
    }

    /**
     * Sets a blockID for a position in the chunk. Args: x, y, z, blockID
     */
    public boolean setBlockID(int par1, int par2, int par3, int i)
    {
        return false;
    }

    /**
     * Return the metadata corresponding to the given coordinates inside a chunk.
     */
    public int getBlockMetadata(int par1, int par2, int par3)
    {
        return 0;
    }

    /**
     * Set the metadata of a block in the chunk
     */
    public boolean setBlockMetadata(int par1, int par2, int par3, int i)
    {
        return false;
    }

    public static int getSkyLightInBounds(int par1, int par2, int par3){
        int sky = 15;
        if (par2<mod_noBiomesX.SurrWaterHeight){
            sky-=3*(mod_noBiomesX.SurrWaterHeight-par2);
        }
        if (sky<0){
            sky = 0;
        }
        return sky;
    }

    public static int getBlockLightInBounds(int par1, int par2, int par3){
        int block = 0;
        if (par2>=mod_noBiomesX.SurrGroundHeight){
            if (par2<=mod_noBiomesX.SurrWaterHeight){
                block = Block.lightValue[mod_noBiomesX.SurrWaterType];
            }else{
                block = Block.lightValue[mod_noBiomesX.SurrWaterType]-(par2-mod_noBiomesX.SurrWaterHeight)-1;
            }
        }
        if (block<0){
            block = 0;
        }
        return block;
    }

    /**
     * Gets the amount of light saved in this block (doesn't adjust for daylight)
     */
    public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int i)
    {
        if (par1EnumSkyBlock==EnumSkyBlock.Sky){
            return getSkyLightInBounds(par2, par3, i);
        }
        return getBlockLightInBounds(par2, par3, i);
    }

    /**
     * Sets the light value at the coordinate. If enumskyblock is set to sky it sets it in the skylightmap and if its a
     * block then into the blocklightmap. Args enumSkyBlock, x, y, z, lightValue
     */
    public void setLightValue(EnumSkyBlock enumskyblock, int i, int j, int k, int l)
    {
    }

    /**
     * Gets the amount of light on a block taking into account sunlight
     */
    public int getBlockLightValue(int par1, int par2, int par3, int i)
    {
        return Math.max(getSkyLightInBounds(par2, par3, i), getBlockLightInBounds(par2, par3, i));
    }

    /**
     * Adds an entity to the chunk. Args: entity
     */
    public void addEntity(Entity entity)
    {
    }

    /**
     * removes entity using its y chunk coordinate as its index
     */
    public void removeEntity(Entity entity)
    {
    }

    /**
     * Removes entity at the specified index from the entity array.
     */
    public void removeEntityAtIndex(Entity entity, int i)
    {
    }

    /**
     * Returns whether is not a block above this one blocking sight to the sky (done via checking against the heightmap)
     */
    public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return false;
    }

    /**
     * Gets the TileEntity for a given block in this chunk
     */
    public TileEntity getChunkBlockTileEntity(int par1, int par2, int par3)
    {
        return null;
    }

    /**
     * Adds a TileEntity to a chunk
     */
    public void addTileEntity(TileEntity tileentity)
    {
    }

    /**
     * Sets the TileEntity for a given block in this chunk
     */
    public void setChunkBlockTileEntity(int i, int j, int k, TileEntity tileentity)
    {
    }

    /**
     * Removes the TileEntity for a given block in this chunk
     */
    public void removeChunkBlockTileEntity(int i, int j, int k)
    {
    }

    /**
     * Called when this Chunk is loaded by the ChunkProvider
     */
    public void onChunkLoad()
    {
    }

    /**
     * Called when this Chunk is unloaded by the ChunkProvider
     */
    public void onChunkUnload()
    {
    }

    /**
     * Sets the isModified flag for this Chunk
     */
    public void setChunkModified()
    {
    }

    /**
     * Fills the given list of all entities that intersect within the given bounding box that aren't the passed entity
     * Args: entity, aabb, listToFill
     */
    public void getEntitiesWithinAABBForEntity(Entity entity, AxisAlignedBB axisalignedbb, List list)
    {
    }

    /**
     * Gets all entities that can be assigned to the specified class. Args: entityClass, aabb, listToFill
     */
    public void getEntitiesOfTypeWithinAAAB(Class class1, AxisAlignedBB axisalignedbb, List list)
    {
    }

    /**
     * Returns true if this Chunk needs to be saved
     */
    public boolean needsSaving(boolean par1)
    {
        return false;
    }

    public Random getRandomWithSeed(long par1)
    {
        return new Random(worldObj.getSeed() + (long)(xPosition * xPosition * 0x4c1906) + (long)(xPosition * 0x5ac0db) + (long)(zPosition * zPosition) * 0x4307a7L + (long)(zPosition * 0x5f24f) ^ par1);
    }

    public boolean isEmpty()
    {
        return true;
    }

    /**
     * Returns whether the ExtendedBlockStorages containing levels (in blocks) from arg 1 to arg 2 are fully empty
     * (true) or not (false).
     */
    public boolean getAreLevelsEmpty(int par1, int par2)
    {
        return true;
    }
}
