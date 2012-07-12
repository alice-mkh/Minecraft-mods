package net.minecraft.src.backport;

import java.util.LinkedList;
import java.util.Random;
import net.minecraft.src.*;

public class StructureScatteredFeatureStart extends StructureStart
{
    public StructureScatteredFeatureStart(World par1World, Random par2Random, int par3, int par4)
    {
        if (par1World.getBiomeGenForCoords(par3 * 16 + 8, par4 * 16 + 8) == BiomeGenBase.jungle)
        {
            ComponentScatteredFeatureJunglePyramid componentscatteredfeaturejunglepyramid = new ComponentScatteredFeatureJunglePyramid(par2Random, par3 * 16, par4 * 16);
            components.add(componentscatteredfeaturejunglepyramid);
        }
        else
        {
            ComponentScatteredFeatureDesertPyramid componentscatteredfeaturedesertpyramid = new ComponentScatteredFeatureDesertPyramid(par2Random, par3 * 16, par4 * 16);
            components.add(componentscatteredfeaturedesertpyramid);
        }

        updateBoundingBox();
    }
}
