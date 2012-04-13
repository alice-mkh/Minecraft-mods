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

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    public int getBiomeGrassColor()
    {
        double d = getFloatTemperature();
        double d1 = getFloatRainfall();
        if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_BETA181){
            return ColorizerGrass.getGrassColor(d, d1);
        }
        return ((ColorizerGrass.getGrassColor(d, d1) & 0xfefefe) + 0x4e0e4e) / 2;
    }

    /**
     * Provides the basic foliage color based on the biome temperature and rainfall
     */
    public int getBiomeFoliageColor()
    {
        double d = getFloatTemperature();
        double d1 = getFloatRainfall();
        if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_BETA181){
            return ColorizerFoliage.getFoliageColor(d, d1);
        }
        return ((ColorizerFoliage.getFoliageColor(d, d1) & 0xfefefe) + 0x4e0e4e) / 2;
    }
}
