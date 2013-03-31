package net.minecraft.src.nbxlite.oldbiomes;

import java.util.List;
import java.util.Random;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenForest;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.WorldGenBigTree;
import net.minecraft.src.WorldGenerator;

public class OldBiomeGenForest extends OldBiomeGenBase
{
    public OldBiomeGenForest()
    {
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityWolf.class, 2));
    }

    @Override
    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(5) == 0)
        {
            return new OldWorldGenForest(false);
        }
        if(random.nextInt(3) == 0)
        {
            return new WorldGenBigTree(false);
        } else
        {
            return new OldWorldGenTrees(false);
        }
    }
}
