package net.minecraft.src;

public class BiomeGenMountainRidge extends BiomeGenBase
{
    public BiomeGenMountainRidge(int i)
    {
        super(i);
        spawnableCreatureList.clear();
        topBlock = (byte)mod_RedRock.redRock.blockID;
        fillerBlock = (byte)mod_RedRock.redRock.blockID;
        biomeDecorator.brownGrassPerChunk = 222;
        biomeDecorator.brownGrassShortPerChunk = 222;
    }
}
