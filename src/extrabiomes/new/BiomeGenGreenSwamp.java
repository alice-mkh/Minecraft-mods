package net.minecraft.src;

import java.util.Random;

public class BiomeGenGreenSwamp extends BiomeGenBase
{

    protected BiomeGenGreenSwamp(int i)
    {
        super(i);
        biomeDecorator.treesPerChunk = 4;
        biomeDecorator.flowersPerChunk = -999;
        biomeDecorator.deadBushPerChunk = 1;
        biomeDecorator.mushroomsPerChunk = 8;
        biomeDecorator.reedsPerChunk = 10;
        biomeDecorator.clayPerChunk = 1;
        biomeDecorator.waterlilyPerChunk = 4;
        biomeDecorator.catTailPerChunk = 999;
        biomeDecorator.hydrangeaPerChunk = 1;
        biomeDecorator.coverPerChunk = 4;
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if (random.nextInt(5) == 0)
        {
            return worldGenSwamp;
        }
        else
        {
            return worldGenSwamp2;
        }
    }
}