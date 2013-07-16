package net.minecraft.src;

import java.util.Random;

public class BlockMushroom extends BlockFlower
{
    public static boolean spreading = true;

    protected BlockMushroom(int par1)
    {
        super(par1);
        float f = 0.2F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
        setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par5Random.nextInt(25) == 0 && spreading)
        {
            byte byte0 = 4;
            int i = 5;

            for (int j = par2 - byte0; j <= par2 + byte0; j++)
            {
                for (int l = par4 - byte0; l <= par4 + byte0; l++)
                {
                    for (int j1 = par3 - 1; j1 <= par3 + 1; j1++)
                    {
                        if (par1World.getBlockId(j, j1, l) == blockID && --i <= 0)
                        {
                            return;
                        }
                    }
                }
            }

            int k = (par2 + par5Random.nextInt(3)) - 1;
            int i1 = (par3 + par5Random.nextInt(2)) - par5Random.nextInt(2);
            int k1 = (par4 + par5Random.nextInt(3)) - 1;

            for (int l1 = 0; l1 < 4; l1++)
            {
                if (par1World.isAirBlock(k, i1, k1) && canBlockStay(par1World, k, i1, k1))
                {
                    par2 = k;
                    par3 = i1;
                    par4 = k1;
                }

                k = (par2 + par5Random.nextInt(3)) - 1;
                i1 = (par3 + par5Random.nextInt(2)) - par5Random.nextInt(2);
                k1 = (par4 + par5Random.nextInt(3)) - 1;
            }

            if (par1World.isAirBlock(k, i1, k1) && canBlockStay(par1World, k, i1, k1))
            {
                par1World.setBlock(k, i1, k1, blockID, 0, 2);
            }
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return super.canPlaceBlockAt(par1World, par2, par3, par4) && canBlockStay(par1World, par2, par3, par4);
    }

    /**
     * Gets passed in the blockID of the block below and supposed to return true if its allowed to grow on the type of
     * blockID passed in. Args: blockID
     */
    protected boolean canThisPlantGrowOnThisBlockID(int par1)
    {
        return Block.opaqueCubeLookup[par1];
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        if (par3 < 0 || par3 >= 256)
        {
            return false;
        }
        else
        {
            int i = par1World.getBlockId(par2, par3 - 1, par4);
            return i == Block.mycelium.blockID || par1World.getFullBlockLightValue(par2, par3, par4) < 13 && canThisPlantGrowOnThisBlockID(i);
        }
    }

    /**
     * Fertilize the mushroom.
     */
    public boolean fertilizeMushroom(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        par1World.setBlockToAir(par2, par3, par4);
        WorldGenBigMushroom worldgenbigmushroom = null;

        if (blockID == Block.mushroomBrown.blockID)
        {
            worldgenbigmushroom = new WorldGenBigMushroom(0);
        }
        else if (blockID == Block.mushroomRed.blockID)
        {
            worldgenbigmushroom = new WorldGenBigMushroom(1);
        }

        if (worldgenbigmushroom == null || !worldgenbigmushroom.generate(par1World, par5Random, par2, par3, par4))
        {
            par1World.setBlock(par2, par3, par4, blockID, i, 3);
            return false;
        }
        else
        {
            return true;
        }
    }
}
