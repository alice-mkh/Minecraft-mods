package net.minecraft.src.nbxlite.blocks;

import net.minecraft.src.ODNBXlite;
import net.minecraft.src.BlockVine;
import net.minecraft.src.IBlockAccess;

public class BlockVine2 extends BlockVine
{
    public BlockVine2(int i)
    {
        super(i);
    }

    public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k)
    {
        return ODNBXlite.GetFoliageColorAtCoords(iblockaccess, i, j, k, false, false);
    }
}
