package net.minecraft.src.nbxlite.oldbiomes;

import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;

public class OldBiomeGenPlains extends OldBiomeGenBase
{
    public OldBiomeGenPlains()
    {
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityHorse.class, 5));
    }
}
