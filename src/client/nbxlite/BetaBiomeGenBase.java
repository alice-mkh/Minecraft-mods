package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;

public class BetaBiomeGenBase extends BiomeGenBase
{
    public BetaBiomeGenBase(int par1)
    {
        super(par1);
        spawnableMonsterList.clear();
        spawnableCreatureList.clear();
        spawnableWaterCreatureList.clear();
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySheep.class, 12));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityPig.class, 10));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityChicken.class, 10));
        spawnableCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntityCow.class, 8));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySpider.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityZombie.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySkeleton.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityCreeper.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySlime.class, 10));
        spawnableMonsterList.add(new SpawnListEntryBeta(net.minecraft.src.EntityEnderman.class, 1));
        spawnableWaterCreatureList.add(new SpawnListEntryBeta(net.minecraft.src.EntitySquid.class, 10));
    }

    public int getSkyColorByTemp(float f)
    {
        f /= 3F;
        if(f < -1F)
        {
            f = -1F;
        }
        if(f > 1.0F)
        {
            f = 1.0F;
        }
        return java.awt.Color.getHSBColor(0.6222222F - f * 0.05F, 0.5F + f * 0.1F, 1.0F).getRGB();
    }
}

