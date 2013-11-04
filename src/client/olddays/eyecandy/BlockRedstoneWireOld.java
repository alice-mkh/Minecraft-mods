package net.minecraft.src;

import java.util.*;

public class BlockRedstoneWireOld extends BlockRedstoneWire
{
    public static boolean cross = false;
    public static boolean gradient = true;
    public static boolean fallback = false;

    public Icon oldCross;
    public Icon oldCrossPowered;
    public Icon oldLine;
    public Icon oldLinePowered;

    public BlockRedstoneWireOld(int par1)
    {
        super(par1);
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return cross ? ODEyecandy.redstoneRenderID : super.getRenderType();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    @Override
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return (gradient || fallback) ? super.colorMultiplier(par1IBlockAccess, par2, par3, par4) : 0xffffff;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        oldCross = par1IconRegister.registerIcon("olddays_redstone_dust_cross");
        oldCrossPowered = par1IconRegister.registerIcon("olddays_redstone_dust_cross_powered");
        oldLine = par1IconRegister.registerIcon("olddays_redstone_dust_line");
        oldLinePowered = par1IconRegister.registerIcon("olddays_redstone_dust_line_powered");
        super.registerIcons(par1IconRegister);
    }

    public static Icon getRedstoneWireIcon(String par0Str)
    {
        if (par0Str.equals("old_cross")){
            return ((BlockRedstoneWireOld)Block.redstoneWire).oldCross;
        }
        if (par0Str.equals("old_line")){
            return ((BlockRedstoneWireOld)Block.redstoneWire).oldLine;
        }
        if (par0Str.equals("old_cross_powered")){
            return ((BlockRedstoneWireOld)Block.redstoneWire).oldCrossPowered;
        }
        if (par0Str.equals("old_line_powered")){
            return ((BlockRedstoneWireOld)Block.redstoneWire).oldLinePowered;
        }
        return BlockRedstoneWire.getRedstoneWireIcon(par0Str);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    @Override
    public Icon getIcon(int par1, int par2)
    {
        return (gradient || fallback) ? super.getIcon(par1, par2) : oldCross;
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    @Override
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (gradient){
            super.randomDisplayTick(par1World, par2, par3, par4, par5Random);
            return;
        }
        if(par1World.getBlockMetadata(par2, par3, par4) > 0)
        {
            double d = (double)par2 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.20000000000000001D;
            double d1 = (float)par3 + 0.0625F;
            double d2 = (double)par4 + 0.5D + ((double)par5Random.nextFloat() - 0.5D) * 0.20000000000000001D;
            par1World.spawnParticle("reddust", d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    public static boolean renderBlockRedstoneWire(RenderBlocks r, IBlockAccess blockAccess, Block par1Block, int par2, int par3, int par4, Icon override){
        Tessellator tessellator = Tessellator.instance;

        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        Icon icon = getRedstoneWireIcon("cross");
        Icon icon1 = getRedstoneWireIcon("line");
        Icon icon2 = getRedstoneWireIcon("cross_overlay");
        Icon icon3 = getRedstoneWireIcon("line_overlay");
        if (!gradient){
            boolean powered = i > 0;
            if (fallback){
                i = powered ? 15 : 0;
            }else{
                icon = getRedstoneWireIcon("old_cross" + (powered ? "_powered" : ""));
                icon1 = getRedstoneWireIcon("old_line" + (powered ? "_powered" : ""));
            }
        }
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        float f1 = (float)i / 15F;
        float f2 = f1 * 0.6F + 0.4F;

        if (i == 0)
        {
            f2 = 0.3F;
        }

        float f3 = f1 * f1 * 0.7F - 0.5F;
        float f4 = f1 * f1 * 0.6F - 0.7F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        if (!gradient && !fallback){
            f2 = 1.0F;
            f3 = 1.0F;
            f4 = 1.0F;
        }

        tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
        boolean flag = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 - 1, par3, par4, 1) || !blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 - 1, par3 - 1, par4, -1);
        boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 + 1, par3, par4, 3) || !blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 + 1, par3 - 1, par4, -1);
        boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3, par4 - 1, 2) || !blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 - 1, -1);
        boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3, par4 + 1, 0) || !blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 + 1, -1);

        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 - 1, par3 + 1, par4, -1))
            {
                flag = true;
            }

            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 + 1, par3 + 1, par4, -1))
            {
                flag1 = true;
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 - 1, -1))
            {
                flag2 = true;
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 + 1, -1))
            {
                flag3 = true;
            }
        }

        float f5 = par2 + 0;
        float f6 = par2 + 1;
        float f7 = par4 + 0;
        float f8 = par4 + 1;
        byte byte0 = 0;

        if ((flag || flag1) && !flag2 && !flag3)
        {
            byte0 = 1;
        }

        if ((flag2 || flag3) && !flag1 && !flag)
        {
            byte0 = 2;
        }

        if (byte0 == 0)
        {
            int j = 0;
            int k = 0;
            int l = 16;
            int i1 = 16;

            if (flag1 || flag2 || flag3 || flag){
                if (!flag)
                {
                    f5 += 0.3125F;
                }

                if (!flag)
                {
                    j += 5;
                }

                if (!flag1)
                {
                    f6 -= 0.3125F;
                }

                if (!flag1)
                {
                    l -= 5;
                }

                if (!flag2)
                {
                    f7 += 0.3125F;
                }

                if (!flag2)
                {
                    k += 5;
                }

                if (!flag3)
                {
                    f8 -= 0.3125F;
                }

                if (!flag3)
                {
                    i1 -= 5;
                }
            }

            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon.getInterpolatedU(l), icon.getInterpolatedV(i1));
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon.getInterpolatedU(l), icon.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon.getInterpolatedU(j), icon.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon.getInterpolatedU(j), icon.getInterpolatedV(i1));
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon2.getInterpolatedU(l), icon2.getInterpolatedV(i1));
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon2.getInterpolatedU(l), icon2.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon2.getInterpolatedU(j), icon2.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon2.getInterpolatedU(j), icon2.getInterpolatedV(i1));
        }
        else if (byte0 == 1)
        {
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon1.getMaxU(), icon1.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon1.getMaxU(), icon1.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon1.getMinU(), icon1.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon1.getMinU(), icon1.getMaxV());
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon3.getMaxU(), icon3.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon3.getMaxU(), icon3.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon3.getMinU(), icon3.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon3.getMinU(), icon3.getMaxV());
        }
        else
        {
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon1.getMaxU(), icon1.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon1.getMinU(), icon1.getMaxV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon1.getMinU(), icon1.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon1.getMaxU(), icon1.getMinV());
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon3.getMaxU(), icon3.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon3.getMinU(), icon3.getMaxV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon3.getMinU(), icon3.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon3.getMaxU(), icon3.getMinV());
        }

        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, icon1.getMinU(), icon1.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon1.getMaxU(), icon1.getMaxV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, icon3.getMinU(), icon3.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon3.getMaxU(), icon3.getMaxV());
            }

            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon1.getMaxU(), icon1.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, icon1.getMinU(), icon1.getMinV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon3.getMaxU(), icon3.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, icon3.getMinU(), icon3.getMinV());
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon1.getMaxU(), icon1.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, icon1.getMinU(), icon1.getMinV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon3.getMaxU(), icon3.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, icon3.getMinU(), icon3.getMinV());
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, icon1.getMinU(), icon1.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon1.getMaxU(), icon1.getMaxV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, icon3.getMinU(), icon3.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon3.getMaxU(), icon3.getMaxV());
            }
        }

        return true;
    }
}
