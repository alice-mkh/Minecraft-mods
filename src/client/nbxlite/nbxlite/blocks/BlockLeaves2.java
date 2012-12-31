package net.minecraft.src.nbxlite.blocks;

import java.util.Random;
import net.minecraft.src.*;

public class BlockLeaves2 extends BlockLeaves
{
    public static boolean decay = true;

    public int fasttex;
    public int fancytex;

    /**
     * The base index in terrain.png corresponding to the fancy version of the leaf texture. This is stored so we can
     * switch the displayed version between fancy and fast graphics (fast is this index + 1).
     */
    private int baseIndexInPNG;

    public BlockLeaves2(int par1, int par2)
    {
        super(par1, par2);
        baseIndexInPNG = par2;
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int par1)
    {
        if ((par1 & 3) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }

        if ((par1 & 3) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        if ((par1 & 3) == 3)
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
                return ColorizerFoliage.getFoliageColor(1.0F, 1.0F);
            }
            return ColorizerFoliage.getFoliageColorBasic();
        }
        else
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
                if (mod_OldDays.texman.hasEntry("olddays/leavesfast.png", "olddays/leavesfancy.png")){
                    return 0xffffff;
                }
                return ColorizerFoliage.getFoliageColor(1.0F, 1.0F);
            }
            return ColorizerFoliage.getFoliageColorBasic();
        }
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if ((i & 3) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }

        if ((i & 3) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        if ((i & 3) == 3)
        {
            return ODNBXlite.GetFoliageColorAtCoords(par1IBlockAccess, par2, par3, par4, true, false);
        }
        return ODNBXlite.GetFoliageColorAtCoords(par1IBlockAccess, par2, par3, par4, true, true);
    }

    public void setDecay(boolean b){
        decay = b;
        setTickRandomly(b);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!decay){
            return;
        }
        super.updateTick(par1World, par2, par3, par4, par5Random);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if ((par2 & 3) == 1)
        {
            return blockIndexInTexture + 80;
        }

        if ((par2 & 3) == 3)
        {
            return blockIndexInTexture + 144;
        }
        else
        {
            if (ODNBXlite.Generator!=ODNBXlite.GEN_BIOMELESS || (par2 & 3) == 2 || !mod_OldDays.texman.hasEntry("olddays/leavesfast.png", "olddays/leavesfancy.png")){
                return blockIndexInTexture;
            }
            if (graphicsLevel){
                return fancytex;
            }
            return fasttex;
        }
    }
}
