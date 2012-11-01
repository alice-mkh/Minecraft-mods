package net.minecraft.src.nbxlite.blocks;

import net.minecraft.src.*;

public class BlockGrass2 extends BlockGrass
{
    public int toptex;
    public int sidetex;

    public BlockGrass2(int par1)
    {
        super(par1);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (par1 == 1)
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && mod_OldDays.texman.hasEntry("olddays/grasstop.png", "olddays/grassside.png")){
                return toptex;
            }
            return 0;
        }

        return super.getBlockTextureFromSideAndMetadata(par1, par2);
    }

    /**
     * Retrieves the block texture to use based on the display side. Args: iBlockAccess, x, y, z, side
     */
    public int getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (par5 == 1)
        {
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && mod_OldDays.texman.hasEntry("olddays/grasstop.png", "olddays/grassside.png")){
                return toptex;
            }
            return 0;
        }

        if (par5 == 0)
        {
            return 2;
        }

        Material material = par1IBlockAccess.getBlockMaterial(par2, par3 + 1, par4);
        if ((ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS || (ODNBXlite.GreenGrassSides && !ODNBXlite.NoGreenGrassSides)) && mod_OldDays.texman.hasEntry("olddays/grasstop.png", "olddays/grassside.png")){
            return material != Material.snow && material != Material.craftedSnow ? sidetex : 68;
        }
        return material != Material.snow && material != Material.craftedSnow ? 3 : 68;
    }

    public int getBlockColor()
    {
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
            if (mod_OldDays.texman.hasEntry("olddays/grasstop.png", "olddays/grassside.png")){
                return 0xffffff;
            }
        }
        return super.getBlockColor();
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return ODNBXlite.GetGrassColorAtCoords(par1IBlockAccess, par2, par3, par4, true, true);
    }
}
