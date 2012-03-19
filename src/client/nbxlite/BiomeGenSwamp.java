package net.minecraft.src;

import java.util.Random;

public class BiomeGenSwamp extends BiomeGenBase
{
    protected BiomeGenSwamp(int par1)
    {
        super(par1);
        biomeDecorator.treesPerChunk = 2;
        biomeDecorator.flowersPerChunk = -999;
        biomeDecorator.deadBushPerChunk = 1;
        biomeDecorator.mushroomsPerChunk = 8;
        biomeDecorator.reedsPerChunk = 10;
        biomeDecorator.clayPerChunk = 1;
        biomeDecorator.waterlilyPerChunk = 4;
        waterColorMultiplier = 0xe0ffae;
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
    {
        return worldGenSwamp;
    }

    public int func_48415_j()
    {
        double d = func_48411_i();
        double d1 = func_48414_h();
        if (mod_noBiomesX.ReleaseSwampColor!=0){
            return ((ColorizerGrass.getGrassColor(d, d1) & 0xfefefe) + 0x4e0e4e) / 2;
        }
        return ColorizerGrass.getGrassColor(d, d1);
    }

    public int func_48412_k()
    {
        double d = func_48411_i();
        double d1 = func_48414_h();
        if (mod_noBiomesX.ReleaseSwampColor!=0){
            return ((ColorizerFoliage.getFoliageColor(d, d1) & 0xfefefe) + 0x4e0e4e) / 2;
        }
        return ColorizerFoliage.getFoliageColor(d, d1);
    }
}
