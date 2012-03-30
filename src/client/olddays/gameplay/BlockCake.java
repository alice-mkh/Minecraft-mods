package net.minecraft.src;

import java.util.Random;

public class BlockCake extends Block
{
    public static boolean heal = false;

    protected BlockCake(int par1, int par2)
    {
        super(par1, par2, Material.cake);
        setTickRandomly(true);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        float f = 0.0625F;
        float f1 = (float)(1 + i * 2) / 16F;
        float f2 = 0.5F;
        setBlockBounds(f1, 0.0F, f, 1.0F - f, f2, 1.0F - f);
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        float f = 0.0625F;
        float f1 = 0.5F;
        setBlockBounds(f, 0.0F, f, 1.0F - f, f1, 1.0F - f);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        float f = 0.0625F;
        float f1 = (float)(1 + i * 2) / 16F;
        float f2 = 0.5F;
        return AxisAlignedBB.getBoundingBoxFromPool((float)par2 + f1, par3, (float)par4 + f, (float)(par2 + 1) - f, ((float)par3 + f2) - f, (float)(par4 + 1) - f);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        float f = 0.0625F;
        float f1 = (float)(1 + i * 2) / 16F;
        float f2 = 0.5F;
        return AxisAlignedBB.getBoundingBoxFromPool((float)par2 + f1, par3, (float)par4 + f, (float)(par2 + 1) - f, (float)par3 + f2, (float)(par4 + 1) - f);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par1 == 1)
        {
            return blockIndexInTexture;
        }

        if (par1 == 0)
        {
            return blockIndexInTexture + 3;
        }

        if (par2 > 0 && par1 == 4)
        {
            return blockIndexInTexture + 2;
        }
        else
        {
            return blockIndexInTexture + 1;
        }
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int par1)
    {
        if (par1 == 1)
        {
            return blockIndexInTexture;
        }

        if (par1 == 0)
        {
            return blockIndexInTexture + 3;
        }
        else
        {
            return blockIndexInTexture + 1;
        }
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
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
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        eatCakeSlice(par1World, par2, par3, par4, par5EntityPlayer);
        return true;
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        eatCakeSlice(par1World, par2, par3, par4, par5EntityPlayer);
    }

    /**
     * Heals the player and removes a slice from the cake.
     */
    private void eatCakeSlice(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        if (heal){
            eatCakeSliceOld(par1World, par2, par3, par4, par5EntityPlayer);
        }else{
            eatCakeSliceNew(par1World, par2, par3, par4, par5EntityPlayer);
        }
    }

    private void eatCakeSliceOld(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        if(par5EntityPlayer.health < 20){
            par5EntityPlayer.heal(3);
            int i = par1World.getBlockMetadata(par2, par3, par4) + 1;

            if (i >= 6){
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
            else{
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
                par1World.markBlockAsNeedsUpdate(par2, par3, par4);
            }
        }
    }

    private void eatCakeSliceNew(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        if (par5EntityPlayer.canEat(false)){
            par5EntityPlayer.getFoodStats().addStats(2, 0.1F);
            int i = par1World.getBlockMetadata(par2, par3, par4) + 1;

            if (i >= 6){
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
            else{
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
                par1World.markBlockAsNeedsUpdate(par2, par3, par4);
            }
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        if (!super.canPlaceBlockAt(par1World, par2, par3, par4))
        {
            return false;
        }
        else
        {
            return canBlockStay(par1World, par2, par3, par4);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
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
        return par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid();
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }
}
