package net.minecraft.src.nbxlite.oldbiomes;

import java.util.List;
import java.util.Random;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.WorldGenBigTree;

public class OldBiomeGenRainforest extends OldBiomeGenBase
{

    public OldBiomeGenRainforest()
    {
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityOcelot.class, 2));
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(3) == 0)
        {
            return new WorldGenBigTree(false);
        } else
        {
            return new OldWorldGenTrees(false);
        }
    }
}
