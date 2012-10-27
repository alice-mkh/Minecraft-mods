package net.minecraft.src.nbxlite.oldbiomes;

import java.util.List;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;

public class OldBiomeGenNotBiome extends OldBiomeGenBase
{

    public OldBiomeGenNotBiome()
    {
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        field_82914_M.clear();
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySheep.class, 12));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityPig.class, 10));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityChicken.class, 10));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityCow.class, 8));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityWolf.class, 1));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySpider.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityZombie.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySkeleton.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityCreeper.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySlime.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityEnderman.class, 1));
        field_82914_M.add(new SpawnListEntryBeta(net.minecraft.src.EntitySquid.class, 10));
    }
}
