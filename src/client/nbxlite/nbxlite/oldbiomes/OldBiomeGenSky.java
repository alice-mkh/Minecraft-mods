package net.minecraft.src.nbxlite.oldbiomes;

import java.util.List;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;

public class OldBiomeGenSky extends OldBiomeGenBase
{
    public OldBiomeGenSky()
    {
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        field_82914_M.clear();
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityChicken.class, 10));
    }

    public int getSkyColorByTemp(float f)
    {
        return 0xc0c0ff;
    }
}
