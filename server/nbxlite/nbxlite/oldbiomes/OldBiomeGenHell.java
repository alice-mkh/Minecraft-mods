package net.minecraft.src.nbxlite.oldbiomes;

import java.util.List;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;

public class OldBiomeGenHell extends OldBiomeGenBase
{
    public OldBiomeGenHell()
    {
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityGhast.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityPigZombie.class, 10));
    }
}
