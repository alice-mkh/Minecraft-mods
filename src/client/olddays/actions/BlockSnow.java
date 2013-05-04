package net.minecraft.src;

import java.util.Random;

public class BlockSnow extends Block
{
    public static boolean snowOnGlass = false;

    protected BlockSnow(int par1)
    {
        super(par1, Material.snow);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabDecorations);
        setBlockBoundsForSnowDepth(0);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon("snow");
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4) & 7;
        float f = 0.125F;
        return AxisAlignedBB.getAABBPool().getAABB((double)par2 + minX, (double)par3 + minY, (double)par4 + minZ, (double)par2 + maxX, (float)par3 + (float)i * f, (double)par4 + maxZ);
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
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        setBlockBoundsForSnowDepth(0);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        setBlockBoundsForSnowDepth(par1IBlockAccess.getBlockMetadata(par2, par3, par4));
    }

    /**
     * calls setBlockBounds based on the depth of the snow. Int is any values 0x0-0x7, usually this blocks metadata.
     */
    protected void setBlockBoundsForSnowDepth(int par1)
    {
        int i = par1 & 7;
        float f = (float)(2 * (1 + i)) / 16F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockId(par2, par3 - 1, par4);

        if (i == 0)
        {
            return false;
        }

        if (i == blockID && (par1World.getBlockMetadata(par2, par3 - 1, par4) & 7) == 7)
        {
            return true;
        }

        if (i != Block.leaves.blockID && (i != Block.glass.blockID || !snowOnGlass) && !Block.blocksList[i].isOpaqueCube())
        {
            return false;
        }
        else
        {
            return par1World.getBlockMaterial(par2, par3 - 1, par4).blocksMovement();
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        canSnowStay(par1World, par2, par3, par4);
    }

    /**
     * Checks if this snow block can stay at this location.
     */
    private boolean canSnowStay(World par1World, int par2, int par3, int par4)
    {
        if (!canPlaceBlockAt(par1World, par2, par3, par4))
        {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        int i = Item.snowball.itemID;
        int j = par6 & 7;
        dropBlockAsItem_do(par1World, par3, par4, par5, new ItemStack(i, j + 1, 0));
        par1World.setBlockToAir(par3, par4, par5);
        par2EntityPlayer.addStat(StatList.mineBlockStatArray[blockID], 1);
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.snowball.itemID;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4) > 11)
        {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Returns true if the given side of this block type should be rendered, if the adjacent block is at the given
     * coordinates.  Args: blockAccess, x, y, z, side
     */
    public boolean shouldSideBeRendered(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par5 == 1)
        {
            return true;
        }
        else
        {
            return super.shouldSideBeRendered(par1IBlockAccess, par2, par3, par4, par5);
        }
    }
}
