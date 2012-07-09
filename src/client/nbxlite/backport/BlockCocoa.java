package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.Direction;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import net.minecraft.src.mod_noBiomesX;

public class BlockCocoa extends BlockDirectional
{
    public BlockCocoa(int par1)
    {
        super(par1, 168, Material.plants);
        setTickRandomly(true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!canBlockStay(par1World, par2, par3, par4))
        {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
        else if (par1World.rand.nextInt(5) == 0)
        {
            int i = par1World.getBlockMetadata(par2, par3, par4);
            int j = func_56787_e(i);

            if (j < 2)
            {
                j++;
                par1World.setBlockMetadataWithNotify(par2, par3, par4, j << 2 | getDirection(i));
            }
        }
    }

    /**
     * Can this block stay at this position.  Similar to canPlaceBlockAt except gets checked often with plants.
     */
    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        int i = getDirection(par1World.getBlockMetadata(par2, par3, par4));
        par2 += Direction.offsetX[i];
        par4 += Direction.offsetZ[i];
        int j = par1World.getBlockId(par2, par3, par4);
        return j == Block.wood.blockID && par1World.getBlockMetadata(par2, par3, par4) == 3;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return mod_noBiomesX.cocoaRenderID;
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
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int j = getDirection(i);
        int k = func_56787_e(i);
        int l = 4 + k * 2;
        int i1 = 5 + k * 2;
        float f = (float)l / 2.0F;

        switch (j)
        {
            case 0:
                setBlockBounds((8F - f) / 16F, (12F - (float)i1) / 16F, (15F - (float)l) / 16F, (8F + f) / 16F, 0.75F, 0.9375F);
                break;

            case 2:
                setBlockBounds((8F - f) / 16F, (12F - (float)i1) / 16F, 0.0625F, (8F + f) / 16F, 0.75F, (1.0F + (float)l) / 16F);
                break;

            case 1:
                setBlockBounds(0.0625F, (12F - (float)i1) / 16F, (8F - f) / 16F, (1.0F + (float)l) / 16F, 0.75F, (8F + f) / 16F);
                break;

            case 3:
                setBlockBounds((15F - (float)l) / 16F, (12F - (float)i1) / 16F, (8F - f) / 16F, 0.9375F, 0.75F, (8F + f) / 16F);
                break;
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int i = ((MathHelper.floor_double((double)((par5EntityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3) + 0) % 4;
        par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
    }

    public void onBlockPlaced(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 == 1 || par5 == 0)
        {
            par5 = 2;
        }

        int i = Direction.footInvisibleFaceRemap[Direction.vineGrowth[par5]];
        par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
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

    public static int func_56787_e(int par0)
    {
        return (par0 & 0xc) >> 2;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        int i = func_56787_e(par5);
        byte byte0 = 1;

        if (i >= 2)
        {
            byte0 = 3;
        }

        for (int j = 0; j < byte0; j++)
        {
            dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.dyePowder, 1, 3));
        }
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.dyePowder.shiftedIndex;
    }

    public int quantityDropped(Random par1Random)
    {
        return 3;
    }

    public static boolean renderBlockCocoa(RenderBlocks r, IBlockAccess blockAccess, Block b, int par2, int par3, int par4){
        BlockCocoa par1BlockCocoa = (BlockCocoa)b;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(par1BlockCocoa.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = par1BlockCocoa.getBlockTextureFromSide(1);
        int k = BlockDirectional.getDirection(i);
        int l = BlockCocoa.func_56787_e(i);
        j = (j + 2) - l;
        int i1 = 4 + l * 2;
        int j1 = 5 + l * 2;
        int k1 = (j & 0xf) << 4;
        int l1 = j & 0xf0;
        double d = 15D - (double)i1;
        double d1 = 15D;
        double d2 = 4D;
        double d3 = 4D + (double)j1;
        double d4 = ((double)k1 + d) / 256D;
        double d5 = (((double)k1 + d1) - 0.01D) / 256D;
        double d6 = ((double)l1 + d2) / 256D;
        double d7 = (((double)l1 + d3) - 0.01D) / 256D;
        double d8 = 0.0D;
        double d9 = 0.0D;

        switch (k)
        {
            case 2:
                d8 = 8D - (double)(i1 / 2);
                d9 = 1.0D;
                break;

            case 0:
                d8 = 8D - (double)(i1 / 2);
                d9 = 15D - (double)i1;
                break;

            case 3:
                d8 = 15D - (double)i1;
                d9 = 8D - (double)(i1 / 2);
                break;

            case 1:
                d8 = 1.0D;
                d9 = 8D - (double)(i1 / 2);
                break;
        }

        double d10 = (double)par2 + d8 / 16D;
        double d11 = (double)par2 + (d8 + (double)i1) / 16D;
        double d12 = (double)par3 + (12D - (double)j1) / 16D;
        double d13 = (double)par3 + 0.75D;
        double d14 = (double)par4 + d9 / 16D;
        double d15 = (double)par4 + (d9 + (double)i1) / 16D;
        tessellator.addVertexWithUV(d10, d12, d14, d4, d7);
        tessellator.addVertexWithUV(d10, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d10, d13, d15, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d12, d14, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
        tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
        tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d15, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
        int i2 = i1;

        if (l >= 2)
        {
            i2--;
        }

        d4 = (float)(k1 + 0) / 256F;
        d5 = ((double)(k1 + i2) - 0.01D) / 256D;
        d6 = (float)(l1 + 0) / 256F;
        d7 = ((double)(l1 + i2) - 0.01D) / 256D;
        tessellator.addVertexWithUV(d10, d13, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d13, d15, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d10, d12, d14, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
        d4 = (float)(k1 + 12) / 256F;
        d5 = ((double)(k1 + 16) - 0.01D) / 256D;
        d6 = (float)(l1 + 0) / 256F;
        d7 = ((double)(l1 + 4) - 0.01D) / 256D;
        d8 = 8D;
        d9 = 0.0D;

        switch (k)
        {
            case 2:
                d8 = 8D;
                d9 = 0.0D;
                break;

            case 0:
                d8 = 8D;
                d9 = 12D;
                double d16 = d4;
                d4 = d5;
                d5 = d16;
                break;

            case 3:
                d8 = 12D;
                d9 = 8D;
                double d17 = d4;
                d4 = d5;
                d5 = d17;
                break;

            case 1:
                d8 = 0.0D;
                d9 = 8D;
                break;
        }

        d10 = (double)par2 + d8 / 16D;
        d11 = (double)par2 + (d8 + 4D) / 16D;
        d12 = (double)par3 + 0.75D;
        d13 = (double)par3 + 1.0D;
        d14 = (double)par4 + d9 / 16D;
        d15 = (double)par4 + (d9 + 4D) / 16D;

        if (k == 2 || k == 0)
        {
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
            tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
            tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
            tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
        }
        else if (k == 1 || k == 3)
        {
            tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
            tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
            tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
        }
        return true;
    }
}
