package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

abstract class ComponentScatteredFeature extends StructureComponent
{
    protected final int field_56297_a;
    protected final int field_56295_b;
    protected final int field_56296_c;
    protected int field_56294_d;

    protected ComponentScatteredFeature(Random par1Random, int par2, int par3, int par4, int par5, int par6, int par7)
    {
        super(0);
        field_56294_d = -1;
        field_56297_a = par5;
        field_56295_b = par6;
        field_56296_c = par7;
        coordBaseMode = par1Random.nextInt(4);

        switch (coordBaseMode)
        {
            case 0:
            case 2:
                boundingBox = new StructureBoundingBox(par2, par3, par4, (par2 + par5) - 1, (par3 + par6) - 1, (par4 + par7) - 1);
                break;

            default:
                boundingBox = new StructureBoundingBox(par2, par3, par4, (par2 + par7) - 1, (par3 + par6) - 1, (par4 + par5) - 1);
                break;
        }
    }

    protected boolean func_56293_a(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3)
    {
        if (field_56294_d >= 0)
        {
            return true;
        }

        int i = 0;
        int j = 0;

        for (int k = boundingBox.minZ; k <= boundingBox.maxZ; k++)
        {
            for (int l = boundingBox.minX; l <= boundingBox.maxX; l++)
            {
                if (par2StructureBoundingBox.isVecInside(l, 64, k))
                {
                    i += Math.max(par1World.getTopSolidOrLiquidBlock(l, k), par1World.worldProvider.getAverageGroundLevel());
                    j++;
                }
            }
        }

        if (j == 0)
        {
            return false;
        }
        else
        {
            field_56294_d = i / j;
            boundingBox.offset(0, (field_56294_d - boundingBox.minY) + par3, 0);
            return true;
        }
    }
}
