package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;

public class BlockRedstoneWireOld extends BlockRedstoneWire
{
    public static boolean cross = false;

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

    public static boolean renderBlockRedstoneWire(RenderBlocks r, IBlockAccess blockAccess, Block par1Block, int par2, int par3, int par4, Icon override){
        Tessellator tessellator = Tessellator.instance;

        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        Icon icon = BlockRedstoneWire.func_94409_b("redstoneDust_cross");
        Icon icon1 = BlockRedstoneWire.func_94409_b("redstoneDust_line");
        Icon icon2 = BlockRedstoneWire.func_94409_b("redstoneDust_cross_overlay");
        Icon icon3 = BlockRedstoneWire.func_94409_b("redstoneDust_line_overlay");
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
