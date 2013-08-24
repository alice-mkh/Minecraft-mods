package net.minecraft.src.nbxlite.blocks;

import java.util.Random;
import net.minecraft.src.*;

public class BlockLeaves2 extends BlockLeaves
{
    public static boolean decay = true;

    private Icon biomelessFastIcon;
    private Icon biomelessFancyIcon;

    public BlockLeaves2(int par1)
    {
        super(par1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    @Override
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
                if (mod_OldDays.texman.hasIcons(false, "olddays_leaves_fast", "olddays_leaves_fancy")){
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
    @Override
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
            return ODNBXlite.GetFoliageColorAtCoords(par1IBlockAccess, par2, par3, par4, true, false, false);
        }
        return ODNBXlite.GetFoliageColorAtCoords(par1IBlockAccess, par2, par3, par4, true, true, false);
    }

    public void setDecay(boolean b){
        decay = b;
        setTickRandomly(b);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    @Override
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!decay){
            return;
        }
        super.updateTick(par1World, par2, par3, par4, par5Random);
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        if ((par2 & 3) == 0 && ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && mod_OldDays.texman.hasIcons(false, "olddays_leaves_fast", "olddays_leaves_fancy")){
            return graphicsLevel ? biomelessFancyIcon : biomelessFastIcon;
        }
        return super.getIcon(par1, par2);
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        biomelessFancyIcon = par1IconRegister.registerIcon("olddays_leaves_fancy");
        biomelessFastIcon = par1IconRegister.registerIcon("olddays_leaves_fast");
    }
}
