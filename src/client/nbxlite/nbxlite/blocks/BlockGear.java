package net.minecraft.src.nbxlite.blocks;

import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ODNBXlite;
import net.minecraft.src.mod_OldDays;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;

public class BlockGear extends Block
{
    private Icon blockIcon2;

    public BlockGear(int par1)
    {
        super(par1, Material.circuits);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int i){
        return null;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public int quantityDropped(Random paramRandom){
        return 1;
    }

    @Override
    public int getRenderType(){
        return ODNBXlite.gearRenderID;
    }

    @Override
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5){
        return canBlockStay(par1World, par2, par3, par4);
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        super.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        if (!canBlockStay(par1World, par2, par3, par4)){
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    @Override
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

    @Override
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

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = mod_OldDays.texman.registerCustomIcon(par1IconRegister, "olddays_gear_0", new TextureGearFX(0));
        blockIcon2 = mod_OldDays.texman.registerCustomIcon(par1IconRegister, "olddays_gear_1", new TextureGearFX(1));
    }

    public static boolean renderBlockGear(RenderBlocks r, IBlockAccess blockAccess, BlockGear b, int i, int j, int k, Icon override){
        Tessellator tessellator = Tessellator.instance;

        Icon icon = b.blockIcon;
        if((i + j + k & 1) == 1){
            icon = b.blockIcon2;
        }
        if (r.hasOverrideBlockTexture())
        {
            icon = override;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(b.getMixedBrightnessForBlock(blockAccess, i, j, k));
        }
        float f5 = Minecraft.oldlighting ? b.getBlockBrightness(blockAccess, i, j, k) : 1.0F;
        tessellator.setColorOpaque_F(f5, f5, f5);
        double d11 = icon.getMinU();
        double d15 = icon.getMaxU();
        double d18 = icon.getMinV();
        double d21 = icon.getMaxV();
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
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)j - 0.125F, (float)k + 0.05F, d15, d21);
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)(j + 1) + 0.125F, (float)k + 0.05F, d15, d18);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)(j + 1) + 0.125F, (float)k + 0.05F, d11, d18);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)j - 0.125F, (float)k + 0.05F, d11, d21);
        }
        if(blockAccess.isBlockNormalCube(i, j, k + 1)){
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)(j + 1) + 0.125F, (float)(k + 1) - 0.05F, d11, d18);
            tessellator.addVertexWithUV((float)(i + 1) + 0.125F, (float)j - 0.125F, (float)(k + 1) - 0.05F, d11, d21);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)j - 0.125F, (float)(k + 1) - 0.05F, d15, d21);
            tessellator.addVertexWithUV((float)i - 0.125F, (float)(j + 1) + 0.125F, (float)(k + 1) - 0.05F, d15, d18);
        }
        return true;
    }
}
