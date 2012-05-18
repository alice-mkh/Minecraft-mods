package net.minecraft.src;

import java.util.Random;

public class BlockReed extends Block
{
    protected BlockReed(int par1, int par2)
    {
        super(par1, Material.plants);
        blockIndexInTexture = par2;
        float f = 0.375F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
        setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.isAirBlock(par2, par3 + 1, par4))
        {
            int i;

            for (i = 1; par1World.getBlockId(par2, par3 - i, par4) == blockID; i++) { }

            if (i < 3)
            {
                int j = par1World.getBlockMetadata(par2, par3, par4);

                if (j == 15)
                {
                    par1World.setBlockWithNotify(par2, par3 + 1, par4, blockID);
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, 0);
                }
                else
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, j + 1);
                }
            }
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockId(par2, par3 - 1, par4);

        if (i == blockID)
        {
            return true;
        }

        if (i != Block.grass.blockID && i != Block.dirt.blockID && i != Block.sand.blockID)
        {
            return false;
        }

        if (par1World.getBlockMaterial(par2 - 1, par3 - 1, par4) == Material.water)
        {
            return true;
        }

        if (par1World.getBlockMaterial(par2 + 1, par3 - 1, par4) == Material.water)
        {
            return true;
        }

        if (par1World.getBlockMaterial(par2, par3 - 1, par4 - 1) == Material.water)
        {
            return true;
        }

        return par1World.getBlockMaterial(par2, par3 - 1, par4 + 1) == Material.water;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        checkBlockCoordValid(par1World, par2, par3, par4);
    }

    /**
     * Checks if current block pos is valid, if not, breaks the block as dropable item. Used for reed and cactus.
     */
    protected final void checkBlockCoordValid(World par1World, int par2, int par3, int par4)
    {
        if (!canBlockStay(par1World, par2, par3, par4))
        {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        return canPlaceBlockAt(par1World, par2, par3, par4);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int i)
    {
        return null;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.reed.shiftedIndex;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 1;
    }
}
