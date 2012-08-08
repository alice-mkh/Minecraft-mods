package net.minecraft.src.nbxlite.blocks;

import java.util.Random;
import net.minecraft.src.ODNBXlite;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

public class BlockGear extends Block
{
    public BlockGear(int par1, int par2)
    {
        super(par1, par2, Material.circuits);
        func_71849_a(CreativeTabs.field_78031_c);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int i){
        return null;
    }

    public boolean isOpaqueCube(){
        return false;
    }

    public boolean renderAsNormalBlock(){
        return false;
    }

    public int quantityDropped(Random paramRandom){
        return 1;
    }

    public int getRenderType(){
        return ODNBXlite.gearRenderID;
    }

    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5){
        return canBlockStay(par1World, par2, par3, par4);
    }

    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        if (!canBlockStay(par1World, par2, par3, par4)){
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    public boolean canBlockStay(World par1World, int par2, int par3, int par4)
    {
        if (par1World.isBlockNormalCube(par2, par3, par4 + 1)){
            return true;
        }
        if (par1World.isBlockNormalCube(par2, par3, par4 - 1)){
            return true;
        }
        if (par1World.isBlockNormalCube(par2 + 1, par3, par4)){
            return true;
        }
        return par1World.isBlockNormalCube(par2 - 1, par3, par4);
    }


    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        float thickness = 0.2F;
        float f1 = 0F;
        float f4 = 1F;
        float f = 0F;
        float f3 = 1F;
        float f2 = 0F;
        float f5 = 1F;
        boolean flag1 = par1IBlockAccess.isBlockNormalCube(par2 + 1, par3, par4);
        boolean flag2 = par1IBlockAccess.isBlockNormalCube(par2 - 1, par3, par4);
        boolean flag3 = par1IBlockAccess.isBlockNormalCube(par2, par3, par4 + 1);
        boolean flag4 = par1IBlockAccess.isBlockNormalCube(par2, par3, par4 - 1);
        if (!((flag1 && flag3) || (flag1 && flag4) || (flag2 && flag3) || (flag2 && flag4) || (flag1 && flag2) || (flag3 && flag4))){
            if (flag1){
                f = 1F - thickness;
                f3 = 1F;
            }else if (flag2){
                f = 0F;
                f3 = thickness;
            }
            if (flag3){
                f2 = 1F - thickness;
                f5 = 1F;
            }else if (flag4){
                f2 = 0F;
                f5 = thickness;
            }
        }
        setBlockBounds(f, f1, f2, f3, f4, f5);
    }

    public static boolean renderBlockGear(RenderBlocks r, IBlockAccess blockAccess, Block b, int i, int j, int k, int override){
        Tessellator tessellator = Tessellator.instance;
        int tex = b.getBlockTextureFromSide(0);
        if(override >= 0)
        {
            tex = override;
        }
        tessellator.setBrightness(b.getMixedBrightnessForBlock(blockAccess, i, j, k));
        float f5 = /*b.getBlockBrightness(blockAccess, i, j, k)*/1.0F;
        tessellator.setColorOpaque_F(f5, f5, f5);
        float f10 = ((tex & 0xf) << 4) + 16;
        float f11 = (tex & 0xf) << 4;
        int i2 = tex & 0xf0;
        if((i + j + k & 1) == 1)
        {
            f10 = (tex & 0xf) << 4;
            f11 = ((tex & 0xf) << 4) + 16;
        }
        double d11 = (float)f10 / 256F;
        double d15 = ((float)f10 + 15.99F) / 256F;
        double d18 = (float)i2 / 256F;
        double d21 = ((float)i2 + 15.99F) / 256F;
        double d23 = (float)f11 / 256F;
        double d27 = ((float)f11 + 15.99F) / 256F;
        double d30 = (float)i2 / 256F;
        double d33 = ((float)i2 + 15.99F) / 256F;
        if(blockAccess.isBlockNormalCube(i - 1, j, k)){
            tessellator.addVertexWithUV((float)i + 0.05F, (float)(j + 1) + 0.125F, (float)(k + 1) + 0.125F, d11, d18);
            tessellator.addVertexWithUV((float)i + 0.05F, (float)j - 0.125F, (float)(k + 1) + 0.125F, d11, d21);
            tessellator.addVertexWithUV((float)i + 0.05F, (float)j - 0.125F, (float)k - 0.125F, d15, d21);
            tessellator.addVertexWithUV((float)i + 0.05F, (float)(j + 1) + 0.125F, (float)k - 0.125F, d15, d18);
        }
        if(blockAccess.isBlockNormalCube(i + 1, j, k)){
            tessellator.addVertexWithUV((float)(i + 1) - 0.05F, (float)j - 0.125F, (float)(k + 1) + 0.125F, d15, d21);
            tessellator.addVertexWithUV((float)(i + 1) - 0.05F, (float)(j + 1) + 0.125F, (float)(k + 1) + 0.125F, d15, d18);
            tessellator.addVertexWithUV((float)(i + 1) - 0.05F, (float)(j + 1) + 0.125F, (float)k - 0.125F, d11, d18);
            tessellator.addVertexWithUV((float)(i + 1) - 0.05F, (float)j - 0.125F, (float)k - 0.125F, d11, d21);
        }
        if(blockAccess.isBlockNormalCube(i, j, k - 1)){
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)j - 0.125F, (float)k + 0.05F, d27, d33);
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)(j + 1) + 0.125F, (float)k + 0.05F, d27, d30);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)(j + 1) + 0.125F, (float)k + 0.05F, d23, d30);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)j - 0.125F, (float)k + 0.05F, d23, d33);
        }
        if(blockAccess.isBlockNormalCube(i, j, k + 1)){
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)(j + 1) + 0.125F, (float)(k + 1) - 0.05F, d23, d30);
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)j - 0.125F, (float)(k + 1) - 0.05F, d23, d33);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)j - 0.125F, (float)(k + 1) - 0.05F, d27, d33);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)(j + 1) + 0.125F, (float)(k + 1) - 0.05F, d27, d30);
        }
        return true;
    }
}
