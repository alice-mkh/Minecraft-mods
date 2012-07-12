package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

public class BlockEnderChest extends BlockContainer
{
    public BlockEnderChest(int par1)
    {
        super(par1, Material.rock);
        blockIndexInTexture = 37;
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
        return 22;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.obsidian.blockID;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 8;
    }

    /**
     * Return true if a player with SlikTouch can harvest this block directly, and not it's normal drops.
     */
    protected boolean canSilkHarvest()
    {
        return true;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        byte byte0 = 0;
        int i = MathHelper.floor_double((double)((par5EntityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (i == 0)
        {
            byte0 = 2;
        }

        if (i == 1)
        {
            byte0 = 5;
        }

        if (i == 2)
        {
            byte0 = 3;
        }

        if (i == 3)
        {
            byte0 = 4;
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0);
    }

    public boolean blockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        InventoryEnderChest inventoryenderchest = null;
        try{
            inventoryenderchest = ((EntityPlayerSP)par5EntityPlayer).func_56242_aN();
        }catch(Exception ex){
            System.out.println(ex);
            inventoryenderchest = new InventoryEnderChest();
        }
        TileEntityEnderChest tileentityenderchest = (TileEntityEnderChest)par1World.getBlockTileEntity(par2, par3, par4);

        if (inventoryenderchest == null || tileentityenderchest == null)
        {
            return true;
        }

        if (par1World.isBlockNormalCube(par2, par3 + 1, par4))
        {
            return true;
        }

        if (par1World.isRemote)
        {
            return true;
        }
        else
        {
            inventoryenderchest.func_56213_a(tileentityenderchest);
            par5EntityPlayer.displayGUIChest(inventoryenderchest);
            return true;
        }
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityEnderChest();
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        for (int i = 0; i < 3; i++)
        {
            double d = (float)par2 + par5Random.nextFloat();
            double d1 = (float)par3 + par5Random.nextFloat();
            double d2 = (float)par4 + par5Random.nextFloat();
            double d3 = 0.0D;
            double d4 = 0.0D;
            double d5 = 0.0D;
            int j = par5Random.nextInt(2) * 2 - 1;
            int k = par5Random.nextInt(2) * 2 - 1;
            d3 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
            d4 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
            d5 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
            d2 = (double)par4 + 0.5D + 0.25D * (double)k;
            d5 = par5Random.nextFloat() * 1.0F * (float)k;
            d = (double)par2 + 0.5D + 0.25D * (double)j;
            d3 = par5Random.nextFloat() * 1.0F * (float)j;
            par1World.spawnParticle("portal", d, d1, d2, d3, d4, d5);
        }
    }
}
