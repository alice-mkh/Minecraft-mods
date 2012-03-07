package net.minecraft.src;

import java.util.Random;

public class BiomeGenMeadow extends BiomeGenBase
{

    protected BiomeGenMeadow(int i)
    {
        super(i);
        biomeDecorator.flowersPerChunk = 9;
        biomeDecorator.grassPerChunk = 12;
    }
    
    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(9001) == 0)
        {
            return worldGenForest;
        }
        if(random.nextInt(1) == 0)
        {
            return worldGenBigTree;
        } else
        {
            return worldGenTrees;
        }
    }
}