package net.minecraft.src;

public class ChunkCache implements IBlockAccess
{
    private int chunkX;
    private int chunkZ;
    private Chunk chunkArray[][];
    private boolean field_48467_d;

    /** Reference to the World object. */
    private World worldObj;

    public ChunkCache(World par1World, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        worldObj = par1World;
        chunkX = par2 >> 4;
        chunkZ = par4 >> 4;
        int i = par5 >> 4;
        int j = par7 >> 4;
        chunkArray = new Chunk[(i - chunkX) + 1][(j - chunkZ) + 1];
        field_48467_d = true;

        for (int k = chunkX; k <= i; k++)
        {
            for (int l = chunkZ; l <= j; l++)
            {
                Chunk chunk = par1World.getChunkFromChunkCoords(k, l);

                if (chunk == null)
                {
                    continue;
                }

                chunkArray[k - chunkX][l - chunkZ] = chunk;

                if (!chunk.getAreLevelsEmpty(par3, par6))
                {
                    field_48467_d = false;
                }
            }
        }
    }

    public boolean func_48452_a()
    {
        return field_48467_d;
    }

    /**
     * Returns the block ID at coords x,y,z
     */
    public int getBlockId(int par1, int par2, int par3)
    {
        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            return 0;
        }

        int i = (par1 >> 4) - chunkX;
        int j = (par3 >> 4) - chunkZ;

        if (i < 0 || i >= chunkArray.length || j < 0 || j >= chunkArray[i].length)
        {
            return 0;
        }

        Chunk chunk = chunkArray[i][j];

        if (chunk == null)
        {
            return 0;
        }
        else
        {
            return chunk.getBlockID(par1 & 0xf, par2, par3 & 0xf);
        }
    }

    /**
     * Returns the TileEntity associated with a given block in X,Y,Z coordinates, or null if no TileEntity exists
     */
    public TileEntity getBlockTileEntity(int par1, int par2, int par3)
    {
        int i = (par1 >> 4) - chunkX;
        int j = (par3 >> 4) - chunkZ;
        return chunkArray[i][j].getChunkBlockTileEntity(par1 & 0xf, par2, par3 & 0xf);
    }

    public float getBrightness(int par1, int par2, int par3, int par4)
    {
        int i = getLightValue(par1, par2, par3);

        if (i < par4)
        {
            i = par4;
        }

        return worldObj.worldProvider.lightBrightnessTable[i];
    }

    /**
     * Any Light rendered on a 1.8 Block goes through here
     */
    public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4)
    {
        int i = getSkyBlockTypeBrightness(EnumSkyBlock.Sky, par1, par2, par3);
        int j = getSkyBlockTypeBrightness(EnumSkyBlock.Block, par1, par2, par3);

        if (j < par4)
        {
            j = par4;
        }

        return i << 20 | j << 4;
    }

    /**
     * Returns how bright the block is shown as which is the block's light value looked up in a lookup table (light
     * values aren't linear for brightness). Args: x, y, z
     */
    public float getLightBrightness(int par1, int par2, int par3)
    {
        return worldObj.worldProvider.lightBrightnessTable[getLightValue(par1, par2, par3)];
    }

    /**
     * Gets the light value of the specified block coords. Args: x, y, z
     */
    public int getLightValue(int par1, int par2, int par3)
    {
        return getLightValueExt(par1, par2, par3, true);
    }

    /**
     * Get light value with flag
     */
    public int getLightValueExt(int par1, int par2, int par3, boolean par4)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 > 0x1c9c380)
        {
            return 15;
        }

        if (par4)
        {
            int i = getBlockId(par1, par2, par3);

            if (i == Block.stairSingle.blockID || i == Block.tilledField.blockID || i == Block.stairCompactPlanks.blockID || i == Block.stairCompactCobblestone.blockID)
            {
                int l = getLightValueExt(par1, par2 + 1, par3, false);
                int j1 = getLightValueExt(par1 + 1, par2, par3, false);
                int k1 = getLightValueExt(par1 - 1, par2, par3, false);
                int l1 = getLightValueExt(par1, par2, par3 + 1, false);
                int i2 = getLightValueExt(par1, par2, par3 - 1, false);

                if (j1 > l)
                {
                    l = j1;
                }

                if (k1 > l)
                {
                    l = k1;
                }

                if (l1 > l)
                {
                    l = l1;
                }

                if (i2 > l)
                {
                    l = i2;
                }

                return l;
            }
        }

        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            int j = 15 - worldObj.skylightSubtracted;

            if (j < 0)
            {
                j = 0;
            }

            return j;
        }
        else
        {
            int k = (par1 >> 4) - chunkX;
            int i1 = (par3 >> 4) - chunkZ;
            return chunkArray[k][i1].getBlockLightValue(par1 & 0xf, par2, par3 & 0xf, worldObj.skylightSubtracted);
        }
    }

    /**
     * Returns the block metadata at coords x,y,z
     */
    public int getBlockMetadata(int par1, int par2, int par3)
    {
        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            return 0;
        }
        else
        {
            int i = (par1 >> 4) - chunkX;
            int j = (par3 >> 4) - chunkZ;
            return chunkArray[i][j].getBlockMetadata(par1 & 0xf, par2, par3 & 0xf);
        }
    }

    /**
     * Returns the block's material.
     */
    public Material getBlockMaterial(int par1, int par2, int par3)
    {
        int i = getBlockId(par1, par2, par3);

        if (i == 0)
        {
            return Material.air;
        }
        else
        {
            return Block.blocksList[i].blockMaterial;
        }
    }

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    public BiomeGenBase getBiomeGenForCoords(int par1, int par2)
    {
        return worldObj.getBiomeGenForCoords(par1, par2);
    }

    /**
     * Returns true if the block at the specified coordinates is an opaque cube. Args: x, y, z
     */
    public boolean isBlockOpaqueCube(int par1, int par2, int par3)
    {
        Block block = Block.blocksList[getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }
        else
        {
            return block.isOpaqueCube();
        }
    }

    /**
     * Indicate if a material is a normal solid opaque cube.
     */
    public boolean isBlockNormalCube(int par1, int par2, int par3)
    {
        Block block = Block.blocksList[getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }
        else
        {
            return block.blockMaterial.blocksMovement() && block.renderAsNormalBlock();
        }
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    public boolean isAirBlock(int par1, int par2, int par3)
    {
        Block block = Block.blocksList[getBlockId(par1, par2, par3)];
        return block == null;
    }

    /**
     * Brightness for SkyBlock.Sky is clear white and (through color computing it is assumed) DEPENDENT ON DAYTIME.
     * Brightness for SkyBlock.Block is yellowish and independent.
     */
    public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        if (par3 < 0)
        {
            par3 = 0;
        }

        if (par3 >= 256)
        {
            par3 = 255;
        }

        if (par3 < 0 || par3 >= 256 || par2 < 0xfe363c80 || par4 < 0xfe363c80 || par2 >= 0x1c9c380 || par4 > 0x1c9c380)
        {
            return par1EnumSkyBlock.defaultLightValue;
        }

        if (Block.useNeighborBrightness[getBlockId(par2, par3, par4)])
        {
            int i = getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3 + 1, par4);
            int k = getSpecialBlockBrightness(par1EnumSkyBlock, par2 + 1, par3, par4);
            int i1 = getSpecialBlockBrightness(par1EnumSkyBlock, par2 - 1, par3, par4);
            int j1 = getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3, par4 + 1);
            int k1 = getSpecialBlockBrightness(par1EnumSkyBlock, par2, par3, par4 - 1);

            if (k > i)
            {
                i = k;
            }

            if (i1 > i)
            {
                i = i1;
            }

            if (j1 > i)
            {
                i = j1;
            }

            if (k1 > i)
            {
                i = k1;
            }

            return i;
        }
        else
        {
            int j = (par2 >> 4) - chunkX;
            int l = (par4 >> 4) - chunkZ;
            return chunkArray[j][l].getSavedLightValue(par1EnumSkyBlock, par2 & 0xf, par3, par4 & 0xf);
        }
    }

    /**
     * is only used on stairs and tilled fields
     */
    public int getSpecialBlockBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        if (par3 < 0)
        {
            par3 = 0;
        }

        if (par3 >= 256)
        {
            par3 = 255;
        }

        if (par3 < 0 || par3 >= 256 || par2 < 0xfe363c80 || par4 < 0xfe363c80 || par2 >= 0x1c9c380 || par4 > 0x1c9c380)
        {
            return par1EnumSkyBlock.defaultLightValue;
        }
        else
        {
            int i = (par2 >> 4) - chunkX;
            int j = (par4 >> 4) - chunkZ;
            return chunkArray[i][j].getSavedLightValue(par1EnumSkyBlock, par2 & 0xf, par3, par4 & 0xf);
        }
    }

    /**
     * Returns current world height.
     */
    public int getHeight()
    {
        return 256;
    }
}
