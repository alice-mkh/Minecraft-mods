package net.minecraft.src.nbxlite.mapgens;

import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.EntityChicken;

public class BiomeGenSky extends BiomeGenBase
{
    public BiomeGenSky(int i)
    {
        super(i);
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCaveCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityChicken.class, 10, 4, 4));
    }

    public int getSkyColorByTemp(float f)
    {
        return 0xc0c0ff;
    }
}
