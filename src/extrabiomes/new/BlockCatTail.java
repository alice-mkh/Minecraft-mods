package net.minecraft.src;

import java.util.Random;

public class BlockCatTail extends Block
{
    protected BlockCatTail(int i, int j)
    {
        super(i, Material.plants);
        setTickRandomly(true);
        
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        int l = world.getBlockId(i, j - 1, k);
        if (l == blockID)
        {
            return true;
        }
        if (l != Block.grass.blockID && l != Block.dirt.blockID && l != Block.sand.blockID)
        {
            return false;
        }
        if (world.getBlockMaterial(i - 1, j - 1, k) == Material.water)
        {
            return true;
        }
        if (world.getBlockMaterial(i + 1, j - 1, k) == Material.water)
        {
            return true;
        }
        if (world.getBlockMaterial(i, j - 1, k - 1) == Material.water)
        {
            return true;
        }
        return world.getBlockMaterial(i, j - 1, k + 1) == Material.water;
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        checkBlockCoordValid(world, i, j, k);
    }

    protected final void checkBlockCoordValid(World world, int i, int j, int k)
    {
        if (!canBlockStay(world, i, j, k))
        {
            dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockWithNotify(i, j, k, 0);
        }
    }

    public boolean canBlockStay(World world, int i, int j, int k)
    {
        return canPlaceBlockAt(world, i, j, k);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
        
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 6;
    
    }
    
    public int idDropped(int i, Random random, int j)
    {
        return mod_CatTail.catTailItem.shiftedIndex;
    }
}
