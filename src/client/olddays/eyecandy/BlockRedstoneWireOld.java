package net.minecraft.src;

import java.util.*;

public class BlockRedstoneWireOld extends BlockRedstoneWire
{
    public static boolean cross = false;

    public BlockRedstoneWireOld(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return cross ? ODEyecandy.redstoneRenderID : super.getRenderType();
    }

    public static boolean renderBlockRedstoneWire(RenderBlocks r, IBlockAccess blockAccess, Block par1Block, int par2, int par3, int par4, int override){
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = par1Block.getBlockTextureFromSideAndMetadata(1, i);
        if (override >= 0){
            j = override;
        }
        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        float f = 1.0F;
        float f1 = (float)i / 15F;
        float f2 = f1 * 0.6F + 0.4F;
        if (i == 0){
            f2 = 0.3F;
        }
        float f3 = f1 * f1 * 0.7F - 0.5F;
        float f4 = f1 * f1 * 0.6F - 0.7F;
        if (f3 < 0.0F){
            f3 = 0.0F;
        }
        if (f4 < 0.0F){
            f4 = 0.0F;
        }
        tessellator.setColorOpaque_F(f2, f3, f4);
        int k = (j & 0xf) << 4;
        int l = j & 0xf0;
        double d = (float)k / 256F;
        double d2 = ((float)k + 15.99F) / 256F;
        double d4 = (float)l / 256F;
        double d6 = ((float)l + 15.99F) / 256F;
        boolean flag = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 - 1, par3, par4, 1) || !blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 - 1, par3 - 1, par4, -1);
        boolean flag1 = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 + 1, par3, par4, 3) || !blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 + 1, par3 - 1, par4, -1);
        boolean flag2 = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3, par4 - 1, 2) || !blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 - 1, -1);
        boolean flag3 = BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3, par4 + 1, 0) || !blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 + 1, -1);
        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4)){
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 - 1, par3 + 1, par4, -1)){
                flag = true;
            }
            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2 + 1, par3 + 1, par4, -1)){
                flag1 = true;
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 - 1, -1)){
                flag2 = true;
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWireOld.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 + 1, -1)){
                flag3 = true;
            }
        }
        float f5 = par2 + 0;
        float f6 = par2 + 1;
        float f7 = par4 + 0;
        float f8 = par4 + 1;
        byte byte0 = 0;
        if ((flag || flag1) && !flag2 && !flag3){
            byte0 = 1;
        }
        if ((flag2 || flag3) && !flag1 && !flag){
            byte0 = 2;
        }
        if (byte0 != 0){
            d = (float)(k + 16) / 256F;
            d2 = ((float)(k + 16) + 15.99F) / 256F;
            d4 = (float)l / 256F;
            d6 = ((float)l + 15.99F) / 256F;
        }
        if (byte0 == 0){
            if(flag1 || flag2 || flag3 || flag){
                if (!flag){
                    f5 += 0.3125F;
                }
                if (!flag){
                    d += 0.01953125D;
                }
                if (!flag1){
                    f6 -= 0.3125F;
                }
                if (!flag1){
                    d2 -= 0.01953125D;
                }
                if (!flag2){
                    f7 += 0.3125F;
                }
                if (!flag2){
                    d4 += 0.01953125D;
                }
                if (!flag3){
                    f8 -= 0.3125F;
                }
                if (!flag3){
                    d6 -= 0.01953125D;
                }
            }
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6 + 0.0625D);
        }else if (byte0 == 1){
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6 + 0.0625D);
        }else if (byte0 == 2){
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d, d6);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d2, d4);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d, d6 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d2, d4 + 0.0625D);
        }
        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4)){
            double d1 = (float)(k + 16) / 256F;
            double d3 = ((float)(k + 16) + 15.99F) / 256F;
            double d5 = (float)l / 256F;
            double d7 = ((float)l + 15.99F) / 256F;
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d5);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, d1, d5);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, d1, d7);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d7);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, d1, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d7 + 0.0625D);
            }
            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, d1, d7);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d7);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d5);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, d1, d5);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, d1, d5 + 0.0625D);
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, d1, d7);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d7);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d5);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, d1, d5);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, d1, d5 + 0.0625D);
            }
            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID){
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d5);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d5);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d7);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d7);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d7 + 0.0625D);
            }
        }
        return true;
    }
}
