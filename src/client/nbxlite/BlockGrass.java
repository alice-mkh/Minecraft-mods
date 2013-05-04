package net.minecraft.src;

import java.util.Random;

public class BlockGrass extends Block
{
    private Icon iconGrassTop;
    private Icon iconSnowSide;
    private Icon iconGrassSideOverlay;
    private Icon iconGrassTopBiomeless;
    private Icon iconGrassSideBiomeless;

    protected BlockGrass(int par1)
    {
        super(par1, Material.grass);
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        if (par1 == 1)
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && mod_OldDays.texman.hasIcons(false, "olddays_grass_top", "olddays_grass_side")){
                return iconGrassTopBiomeless;
            }
            return iconGrassTop;
        }

        if (par1 == 0)
        {
            return Block.dirt.getBlockTextureFromSide(par1);
        }
        else
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && mod_OldDays.texman.hasIcons(false, "olddays_grass_top", "olddays_grass_side")){
                return iconGrassSideBiomeless;
            }
            return blockIcon;
        }
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par5 == 1)
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && mod_OldDays.texman.hasIcons(false, "olddays_grass_top", "olddays_grass_side")){
                return iconGrassTopBiomeless;
            }
            return iconGrassTop;
        }

        if (par5 == 0)
        {
            return Block.dirt.getBlockTextureFromSide(par5);
        }

        Material material = par1IBlockAccess.getBlockMaterial(par2, par3 + 1, par4);

        if (material == Material.snow || material == Material.craftedSnow)
        {
            return iconSnowSide;
        }
        else
        {
            if ((ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS || (ODNBXlite.GreenGrassSides && !ODNBXlite.NoGreenGrassSides)) && mod_OldDays.texman.hasIcons(false, "olddays_grass_top", "olddays_grass_side")){
                return iconGrassSideBiomeless;
            }
            return blockIcon;
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon("grass_side");
        iconGrassTop = par1IconRegister.registerIcon("grass_top");
        iconSnowSide = par1IconRegister.registerIcon("snow_side");
        iconGrassSideOverlay = par1IconRegister.registerIcon("grass_side_overlay");
        iconGrassTopBiomeless = par1IconRegister.registerIcon("olddays_grass_top");
        iconGrassSideBiomeless = par1IconRegister.registerIcon("olddays_grass_side");
    }

    public int getBlockColor()
    {
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
            if (mod_OldDays.texman.hasIcons(false, "olddays_grass_top", "olddays_grass_side")){
                return 0xffffff;
            }
            ColorizerGrass.getGrassColor(1.0F, 1.0F);
        }
        double d = 0.5D;
        double d1 = 1.0D;
        return ColorizerGrass.getGrassColor(d, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int par1)
    {
        return getBlockColor();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return ODNBXlite.GetGrassColorAtCoords(par1IBlockAccess, par2, par3, par4, true, true);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.isRemote)
        {
            return;
        }

        if (par1World.getBlockLightValue(par2, par3 + 1, par4) < 4 && Block.lightOpacity[par1World.getBlockId(par2, par3 + 1, par4)] > 2)
        {
            par1World.setBlock(par2, par3, par4, Block.dirt.blockID);
        }
        else if (par1World.getBlockLightValue(par2, par3 + 1, par4) >= 9)
        {
            for (int i = 0; i < 4; i++)
            {
                int j = (par2 + par5Random.nextInt(3)) - 1;
                int k = (par3 + par5Random.nextInt(5)) - 3;
                int l = (par4 + par5Random.nextInt(3)) - 1;
                int i1 = par1World.getBlockId(j, k + 1, l);

                if (par1World.getBlockId(j, k, l) == Block.dirt.blockID && par1World.getBlockLightValue(j, k + 1, l) >= 4 && Block.lightOpacity[i1] <= 2)
                {
                    par1World.setBlock(j, k, l, Block.grass.blockID);
                }
            }
        }
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.dirt.idDropped(0, par2Random, par3);
    }

    public static Icon getIconSideOverlay()
    {
        return Block.grass.iconGrassSideOverlay;
    }
}
