package net.minecraft.src;

import java.util.Random;

public class BlockGrass extends Block
{
    public int toptex;
    public int sidetex;

    protected BlockGrass(int par1)
    {
        super(par1, Material.grass);
        blockIndexInTexture = 3;
        setTickRandomly(true);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par1 == 1)
        {
            if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && !mod_noBiomesX.FallbackColors){
                return toptex;
            }
            return 0;
        }

        return par1 != 0 ? 3 : 2;
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par5 == 1)
        {
            if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS && !mod_noBiomesX.FallbackColors){
                return toptex;
            }
            return 0;
        }

        if (par5 == 0)
        {
            return 2;
        }

        Material material = par1IBlockAccess.getBlockMaterial(par2, par3 + 1, par4);
        if ((mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS || (mod_noBiomesX.GreenGrassSides && !mod_noBiomesX.NoGreenGrassSides)) && !mod_noBiomesX.FallbackColors){
            return material != Material.snow && material != Material.craftedSnow ? sidetex : 68;
        }
        return material != Material.snow && material != Material.craftedSnow ? 3 : 68;
    }

    public int getBlockColor()
    {
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS){
            if (!mod_noBiomesX.FallbackColors){
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
        return mod_noBiomesX.GetGrassColorAtCoords(par1IBlockAccess, par2, par3, par4, true, true);
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
            par1World.setBlockWithNotify(par2, par3, par4, Block.dirt.blockID);
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
                    par1World.setBlockWithNotify(j, k, l, Block.grass.blockID);
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
}
