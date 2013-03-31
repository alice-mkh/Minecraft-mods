package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class OldWorldGenForest extends WorldGenerator
{
    public OldWorldGenForest(boolean par1)
    {
        super(par1);
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        int i = par2Random.nextInt(3) + 5;
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

            for (int j1 = par3 - byte0; j1 <= par3 + byte0 && flag; j1++)
            {
                for (int i2 = par5 - byte0; i2 <= par5 + byte0 && flag; i2++)
                {
                    if (j >= 0 && j < 256)
                    {
                        int k2 = par1World.getBlockId(j1, j, i2);

                        if (k2 != 0 && k2 != Block.leaves.blockID)
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

        for (int l = (par4 - 3) + i; l <= par4 + i; l++)
        {
            int k1 = l - (par4 + i);
            int j2 = 1 - k1 / 2;

            for (int l2 = par3 - j2; l2 <= par3 + j2; l2++)
            {
                int i3 = l2 - par3;

                for (int j3 = par5 - j2; j3 <= par5 + j2; j3++)
                {
                    int k3 = j3 - par5;

                    if ((Math.abs(i3) != j2 || Math.abs(k3) != j2 || par2Random.nextInt(2) != 0 && k1 != 0) && !Block.opaqueCubeLookup[par1World.getBlockId(l2, l, j3)])
                    {
                        setBlockAndMetadata(par1World, l2, l, j3, Block.leaves.blockID, 2);
                    }
                }
            }
        }

        for (int i1 = 0; i1 < i; i1++)
        {
            int l1 = par1World.getBlockId(par3, par4 + i1, par5);

            if (l1 == 0 || l1 == Block.leaves.blockID)
            {
                setBlockAndMetadata(par1World, par3, par4 + i1, par5, Block.wood.blockID, 2);
            }
        }

        return true;
    }
}
