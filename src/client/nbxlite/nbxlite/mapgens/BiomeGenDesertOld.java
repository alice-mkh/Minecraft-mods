package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class BiomeGenDesertOld extends BiomeGenDesert
{
    public BiomeGenDesertOld(int par1)
    {
        super(par1);
    }

    public void decorate(World par1World, Random par2Random, int par3, int par4)
    {
        super.decorate(par1World, par2Random, par3, par4);

        if (par2Random.nextInt(1000) == 0 && ODNBXlite.MapFeatures >= ODNBXlite.FEATURES_12)
        {
            int i = par3 + par2Random.nextInt(16) + 8;
            int j = par4 + par2Random.nextInt(16) + 8;
            WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
            worldgendesertwells.generate(par1World, par2Random, i, par1World.getHeightValue(i, j) + 1, j);
        }
    }
}
