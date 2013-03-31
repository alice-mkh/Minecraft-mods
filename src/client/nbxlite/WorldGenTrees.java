package net.minecraft.src;

import java.util.Random;

public class WorldGenTrees extends WorldGenerator
{
    /** The minimum height of a generated tree. */
    private final int minTreeHeight;

    /** True if this tree should grow Vines. */
    private final boolean vinesGrow;

    /** The metadata value of the wood to use in tree generation. */
    private final int metaWood;

    /** The metadata value of the leaves to use in tree generation. */
    private final int metaLeaves;

    public WorldGenTrees(boolean par1)
    {
        this(par1, 4, 0, 0, false);
    }

    public WorldGenTrees(boolean par1, int par2, int par3, int par4, boolean par5)
    {
        super(par1);
        minTreeHeight = par2;
        metaWood = par3;
        metaLeaves = par4;
        vinesGrow = par5;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        int i = par2Random.nextInt(3) + minTreeHeight;
        boolean flag = true;

        if (par4 < 1 || par4 + i + 1 > 256)
        {
            return false;
        }

        for (int j = par4; j <= par4 + 1 + i; j++)
        {
            byte byte0 = 1;

            if (j == par4)
            {
                byte0 = 0;
            }

            if (j >= (par4 + 1 + i) - 2)
            {
                byte0 = 2;
            }

            for (int l = par3 - byte0; l <= par3 + byte0 && flag; l++)
            {
                for (int j1 = par5 - byte0; j1 <= par5 + byte0 && flag; j1++)
                {
                    if (j >= 0 && j < 256)
                    {
                        int k2 = par1World.getBlockId(l, j, j1);

                        if (k2 != 0 && k2 != Block.leaves.blockID && k2 != Block.grass.blockID && k2 != Block.dirt.blockID && k2 != Block.wood.blockID)
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = false;
                    }
                }
            }
        }

        if (!flag)
        {
            return false;
        }

        int k = par1World.getBlockId(par3, par4 - 1, par5);

        if (k != Block.grass.blockID && k != Block.dirt.blockID || par4 >= 256 - i - 1)
        {
            return false;
        }

        setBlock(par1World, par3, par4 - 1, par5, Block.dirt.blockID);
        byte byte1 = 3;
        int i1 = 0;

        for (int k1 = (par4 - byte1) + i; k1 <= par4 + i; k1++)
        {
            int l2 = k1 - (par4 + i);
            int l3 = (i1 + 1) - l2 / 2;

            for (int k4 = par3 - l3; k4 <= par3 + l3; k4++)
            {
                int i5 = k4 - par3;

                for (int k5 = par5 - l3; k5 <= par5 + l3; k5++)
                {
                    int l5 = k5 - par5;

                    if (Math.abs(i5) == l3 && Math.abs(l5) == l3 && (par2Random.nextInt(2) == 0 || l2 == 0))
                    {
                        continue;
                    }

                    boolean trees15 = ODNBXlite.MapFeatures >= ODNBXlite.FEATURES_15;
                    boolean old = ODNBXlite.MapFeatures < ODNBXlite.FEATURES_14;
                    if (((!par1World.isAirBlock(k4, k1, k5) || old) && (!old || Block.opaqueCubeLookup[par1World.getBlockId(k4, k1, k5)]))){
                        continue;
                    }

                    int i6 = par1World.getBlockId(k4, k1, k5);

                    if (i6 == 0 || (i6 == Block.leaves.blockID && trees15))
                    {
                        setBlockAndMetadata(par1World, k4, k1, k5, Block.leaves.blockID, metaLeaves);
                    }
                }
            }
        }

        for (int l1 = 0; l1 < i; l1++)
        {
            int i3 = par1World.getBlockId(par3, par4 + l1, par5);

            if (i3 != 0 && i3 != Block.leaves.blockID)
            {
                continue;
            }

            setBlockAndMetadata(par1World, par3, par4 + l1, par5, Block.wood.blockID, metaWood);

            if (!vinesGrow || l1 <= 0)
            {
                continue;
            }

            if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + l1, par5))
            {
                setBlockAndMetadata(par1World, par3 - 1, par4 + l1, par5, Block.vine.blockID, 8);
            }

            if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + l1, par5))
            {
                setBlockAndMetadata(par1World, par3 + 1, par4 + l1, par5, Block.vine.blockID, 2);
            }

            if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + l1, par5 - 1))
            {
                setBlockAndMetadata(par1World, par3, par4 + l1, par5 - 1, Block.vine.blockID, 1);
            }

            if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + l1, par5 + 1))
            {
                setBlockAndMetadata(par1World, par3, par4 + l1, par5 + 1, Block.vine.blockID, 4);
            }
        }

        if (vinesGrow)
        {
            for (int i2 = (par4 - 3) + i; i2 <= par4 + i; i2++)
            {
                int j3 = i2 - (par4 + i);
                int i4 = 2 - j3 / 2;

                for (int l4 = par3 - i4; l4 <= par3 + i4; l4++)
                {
                    for (int j5 = par5 - i4; j5 <= par5 + i4; j5++)
                    {
                        if (par1World.getBlockId(l4, i2, j5) != Block.leaves.blockID)
                        {
                            continue;
                        }

                        if (par2Random.nextInt(4) == 0 && par1World.getBlockId(l4 - 1, i2, j5) == 0)
                        {
                            growVines(par1World, l4 - 1, i2, j5, 8);
                        }

                        if (par2Random.nextInt(4) == 0 && par1World.getBlockId(l4 + 1, i2, j5) == 0)
                        {
                            growVines(par1World, l4 + 1, i2, j5, 2);
                        }

                        if (par2Random.nextInt(4) == 0 && par1World.getBlockId(l4, i2, j5 - 1) == 0)
                        {
                            growVines(par1World, l4, i2, j5 - 1, 1);
                        }

                        if (par2Random.nextInt(4) == 0 && par1World.getBlockId(l4, i2, j5 + 1) == 0)
                        {
                            growVines(par1World, l4, i2, j5 + 1, 4);
                        }
                    }
                }
            }

            if (par2Random.nextInt(5) == 0 && i > 5 && (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_12 || ODNBXlite.Generator!=ODNBXlite.GEN_NEWBIOMES))
            {
                for (int j2 = 0; j2 < 2; j2++)
                {
                    for (int k3 = 0; k3 < 4; k3++)
                    {
                        if (par2Random.nextInt(4 - j2) == 0)
                        {
                            int j4 = par2Random.nextInt(3);
                            setBlockAndMetadata(par1World, par3 + Direction.offsetX[Direction.footInvisibleFaceRemap[k3]], ((par4 + i) - 5) + j2, par5 + Direction.offsetZ[Direction.footInvisibleFaceRemap[k3]], Block.cocoaPlant.blockID, j4 << 2 | k3);
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Grows vines downward from the given block for a given length. Args: World, x, starty, z, vine-length
     */
    private void growVines(World par1World, int par2, int par3, int par4, int par5)
    {
        setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);

        for (int i = 4; par1World.getBlockId(par2, --par3, par4) == 0 && i > 0; i--)
        {
            setBlockAndMetadata(par1World, par2, par3, par4, Block.vine.blockID, par5);
        }
    }
}
