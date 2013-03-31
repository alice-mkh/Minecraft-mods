package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class OldWorldGenHugeTrees extends WorldGenerator
{
    /** The base height of the tree */
    private final int baseHeight;

    /** Sets the metadata for the wood blocks used */
    private final int woodMetadata;

    /** Sets the metadata for the leaves used in huge trees */
    private final int leavesMetadata;

    public OldWorldGenHugeTrees(boolean par1, int par2, int par3, int par4)
    {
        super(par1);
        baseHeight = par2;
        woodMetadata = par3;
        leavesMetadata = par4;
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        int i = par2Random.nextInt(3) + baseHeight;
        boolean flag = true;

        if (par4 < 1 || par4 + i + 1 > 256)
        {
            return false;
        }

        for (int j = par4; j <= par4 + 1 + i; j++)
        {
            byte byte0 = 2;

            if (j == par4)
            {
                byte0 = 1;
            }

            if (j >= (par4 + 1 + i) - 2)
            {
                byte0 = 2;
            }

            for (int i1 = par3 - byte0; i1 <= par3 + byte0 && flag; i1++)
            {
                for (int k1 = par5 - byte0; k1 <= par5 + byte0 && flag; k1++)
                {
                    if (j >= 0 && j < 256)
                    {
                        int k2 = par1World.getBlockId(i1, j, k1);

                        if (k2 != 0 && k2 != Block.leaves.blockID && k2 != Block.grass.blockID && k2 != Block.dirt.blockID && k2 != Block.wood.blockID && k2 != Block.sapling.blockID)
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

        par1World.setBlock(par3, par4 - 1, par5, Block.dirt.blockID);
        par1World.setBlock(par3 + 1, par4 - 1, par5, Block.dirt.blockID);
        par1World.setBlock(par3, par4 - 1, par5 + 1, Block.dirt.blockID);
        par1World.setBlock(par3 + 1, par4 - 1, par5 + 1, Block.dirt.blockID);
        growLeaves(par1World, par3, par5, par4 + i, 2, par2Random);

        for (int l = (par4 + i) - 2 - par2Random.nextInt(4); l > par4 + i / 2; l -= 2 + par2Random.nextInt(4))
        {
            float f = par2Random.nextFloat() * (float)Math.PI * 2.0F;
            int l1 = par3 + (int)(0.5F + MathHelper.cos(f) * 4F);
            int l2 = par5 + (int)(0.5F + MathHelper.sin(f) * 4F);
            growLeaves(par1World, l1, l2, l, 0, par2Random);

            for (int j3 = 0; j3 < 5; j3++)
            {
                int i2 = par3 + (int)(1.5F + MathHelper.cos(f) * (float)j3);
                int i3 = par5 + (int)(1.5F + MathHelper.sin(f) * (float)j3);
                setBlockAndMetadata(par1World, i2, (l - 3) + j3 / 2, i3, Block.wood.blockID, woodMetadata);
            }
        }

        for (int j1 = 0; j1 < i; j1++)
        {
            int j2 = par1World.getBlockId(par3, par4 + j1, par5);

            if (j2 == 0 || j2 == Block.leaves.blockID)
            {
                setBlockAndMetadata(par1World, par3, par4 + j1, par5, Block.wood.blockID, woodMetadata);

                if (j1 > 0)
                {
                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + j1, par5))
                    {
                        setBlockAndMetadata(par1World, par3 - 1, par4 + j1, par5, Block.vine.blockID, 8);
                    }

                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 - 1))
                    {
                        setBlockAndMetadata(par1World, par3, par4 + j1, par5 - 1, Block.vine.blockID, 1);
                    }
                }
            }

            if (j1 >= i - 1)
            {
                continue;
            }

            j2 = par1World.getBlockId(par3 + 1, par4 + j1, par5);

            if (j2 == 0 || j2 == Block.leaves.blockID)
            {
                setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5, Block.wood.blockID, woodMetadata);

                if (j1 > 0)
                {
                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 2, par4 + j1, par5))
                    {
                        setBlockAndMetadata(par1World, par3 + 2, par4 + j1, par5, Block.vine.blockID, 2);
                    }

                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + j1, par5 - 1))
                    {
                        setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5 - 1, Block.vine.blockID, 1);
                    }
                }
            }

            j2 = par1World.getBlockId(par3 + 1, par4 + j1, par5 + 1);

            if (j2 == 0 || j2 == Block.leaves.blockID)
            {
                setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5 + 1, Block.wood.blockID, woodMetadata);

                if (j1 > 0)
                {
                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 2, par4 + j1, par5 + 1))
                    {
                        setBlockAndMetadata(par1World, par3 + 2, par4 + j1, par5 + 1, Block.vine.blockID, 2);
                    }

                    if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 + 1, par4 + j1, par5 + 2))
                    {
                        setBlockAndMetadata(par1World, par3 + 1, par4 + j1, par5 + 2, Block.vine.blockID, 4);
                    }
                }
            }

            j2 = par1World.getBlockId(par3, par4 + j1, par5 + 1);

            if (j2 != 0 && j2 != Block.leaves.blockID)
            {
                continue;
            }

            setBlockAndMetadata(par1World, par3, par4 + j1, par5 + 1, Block.wood.blockID, woodMetadata);

            if (j1 <= 0)
            {
                continue;
            }

            if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3 - 1, par4 + j1, par5 + 1))
            {
                setBlockAndMetadata(par1World, par3 - 1, par4 + j1, par5 + 1, Block.vine.blockID, 8);
            }

            if (par2Random.nextInt(3) > 0 && par1World.isAirBlock(par3, par4 + j1, par5 + 2))
            {
                setBlockAndMetadata(par1World, par3, par4 + j1, par5 + 2, Block.vine.blockID, 4);
            }
        }

        return true;
    }

    private void growLeaves(World par1World, int par2, int par3, int par4, int par5, Random par6Random)
    {
        byte byte0 = 2;

        for (int i = par4 - byte0; i <= par4; i++)
        {
            int j = i - par4;
            int k = (par5 + 1) - j;

            for (int l = par2 - k; l <= par2 + k + 1; l++)
            {
                int i1 = l - par2;

                for (int j1 = par3 - k; j1 <= par3 + k + 1; j1++)
                {
                    int k1 = j1 - par3;

                    if ((i1 >= 0 || k1 >= 0 || i1 * i1 + k1 * k1 <= k * k) && (i1 <= 0 && k1 <= 0 || i1 * i1 + k1 * k1 <= (k + 1) * (k + 1)) && (par6Random.nextInt(4) != 0 || i1 * i1 + k1 * k1 <= (k - 1) * (k - 1)) && !Block.opaqueCubeLookup[par1World.getBlockId(l, i, j1)])
                    {
                        setBlockAndMetadata(par1World, l, i, j1, Block.leaves.blockID, leavesMetadata);
                    }
                }
            }
        }
    }
}
