package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BiomeGenMiniJungle extends BiomeGenBase
{

    public BiomeGenMiniJungle(int i)
    {
        super(i);
        biomeDecorator.treesPerChunk = 15;
        biomeDecorator.grassPerChunk = 9;
        biomeDecorator.flowersPerChunk = 5;
        biomeDecorator.reedsPerChunk = 70;
        biomeDecorator.clayPerChunk = 3;
        biomeDecorator.mushroomsPerChunk = 2;
        biomeDecorator.waterlilyPerChunk = 12;
        waterColorMultiplier = 0x24b01c;
    }
    
    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(2) == 0)
        {
            return worldGenSwamp;
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