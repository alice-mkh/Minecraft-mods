package net.minecraft.src.nbxlite.oldbiomes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.src.nbxlite.spawners.SpawnListEntryBeta;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.Block;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.WorldGenerator;
import net.minecraft.src.WorldGenBigTree;

public class OldBiomeGenBase
{
    public static final OldBiomeGenBase rainforest = (new OldBiomeGenRainforest()).setColor(0x8fa36).setBiomeName("Rainforest").func_4124_a(0x1ff458);
    public static final OldBiomeGenBase swampland = (new OldBiomeGenSwamp()).setColor(0x7f9b2).setBiomeName("Swampland").func_4124_a(0x8baf48);
    public static final OldBiomeGenBase seasonalForest = (new OldBiomeGenBase()).setColor(0x9be023).setBiomeName("Seasonal Forest");
    public static final OldBiomeGenBase forest = (new OldBiomeGenForest()).setColor(0x56621).setBiomeName("Forest").func_4124_a(0x4eba31);
    public static final OldBiomeGenBase savanna = (new OldBiomeGenDesert()).setColor(0xd9e023).setBiomeName("Savanna");
    public static final OldBiomeGenBase shrubland = (new OldBiomeGenBase()).setColor(0xa1ad20).setBiomeName("Shrubland");
    public static final OldBiomeGenBase taiga = (new OldBiomeGenTaiga()).setColor(0x2eb153).setBiomeName("Taiga").setEnableSnow().func_4124_a(0x7bb731);
    public static final OldBiomeGenBase desert = (new OldBiomeGenDesert()).setColor(0xfa9418).setBiomeName("Desert").setDisableRain();
    public static final OldBiomeGenBase plains = (new OldBiomeGenDesert()).setColor(0xffd910).setBiomeName("Plains");
    public static final OldBiomeGenBase iceDesert = (new OldBiomeGenDesert()).setColor(0xffed93).setBiomeName("Ice Desert").setEnableSnow().setDisableRain().func_4124_a(0xc4d339);
    public static final OldBiomeGenBase tundra = (new OldBiomeGenBase()).setColor(0x57ebf9).setBiomeName("Tundra").setEnableSnow().func_4124_a(0xc4d339);
    public static final OldBiomeGenBase hell = (new OldBiomeGenHell()).setColor(0xff0000).setBiomeName("Hell").setDisableRain();
    public static final OldBiomeGenBase sky = (new OldBiomeGenSky()).setColor(0x8080ff).setBiomeName("Sky").setDisableRain();
    public static final OldBiomeGenBase notABiome = (new OldBiomeGenNotBiome()).setColor(0xffffff).setBiomeName("Not actually a biome!").setDisableRain();
    public String biomeName;
    public int color;
    public byte topBlock;
    public byte fillerBlock;
    public int field_6502_q;
    protected List spawnableMonsterList;
    protected List spawnableCreatureList;
    protected List spawnableWaterCreatureList;
    private boolean enableSnow;
    private boolean enableRain;
    private static OldBiomeGenBase biomeLookupTable[] = new OldBiomeGenBase[4096];

    protected OldBiomeGenBase()
    {
        topBlock = (byte)Block.grass.blockID;
        fillerBlock = (byte)Block.dirt.blockID;
        field_6502_q = 0x4ee031;
        spawnableMonsterList = new ArrayList();
        spawnableCreatureList = new ArrayList();
        spawnableWaterCreatureList = new ArrayList();
        enableRain = true;
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

    private OldBiomeGenBase setDisableRain()
    {
        enableRain = false;
        return this;
    }

    public static void generateBiomeLookup()
    {
        for(int i = 0; i < 64; i++)
        {
            for(int j = 0; j < 64; j++)
            {
                biomeLookupTable[i + j * 64] = getBiome((float)i / 63F, (float)j / 63F);
            }

        }

        desert.topBlock = desert.fillerBlock = (byte)Block.sand.blockID;
        iceDesert.topBlock = iceDesert.fillerBlock = (byte)Block.sand.blockID;
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        if(random.nextInt(10) == 0)
        {
            return new WorldGenBigTree(false);
        } else
        {
            return new OldWorldGenTrees(false);
        }
    }

    protected OldBiomeGenBase setEnableSnow()
    {
        enableSnow = true;
        return this;
    }

    protected OldBiomeGenBase setBiomeName(String s)
    {
        biomeName = s;
        return this;
    }

    protected OldBiomeGenBase func_4124_a(int i)
    {
        field_6502_q = i;
        return this;
    }

    protected OldBiomeGenBase setColor(int i)
    {
        color = i;
        return this;
    }

    public static OldBiomeGenBase getBiomeFromLookup(double d, double d1)
    {
        int i = (int)(d * 63D);
        int j = (int)(d1 * 63D);
        return biomeLookupTable[i + j * 64];
    }

    public static OldBiomeGenBase getBiome(float f, float f1)
    {
        f1 *= f;
        if(f < 0.1F)
        {
            return tundra;
        }
        if(f1 < 0.2F)
        {
            if(f < 0.5F)
            {
                return tundra;
            }
            if(f < 0.95F)
            {
                return savanna;
            } else
            {
                return desert;
            }
        }
        if(f1 > 0.5F && f < 0.7F)
        {
            return swampland;
        }
        if(f < 0.5F)
        {
            return taiga;
        }
        if(f < 0.97F)
        {
            if(f1 < 0.35F)
            {
                return shrubland;
            } else
            {
                return forest;
            }
        }
        if(f1 < 0.45F)
        {
            return plains;
        }
        if(f1 < 0.9F)
        {
            return seasonalForest;
        } else
        {
            return rainforest;
        }
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

    public List getSpawnableList(EnumCreatureType enumcreaturetype)
    {
        if(enumcreaturetype == EnumCreatureType.monster)
        {
            return spawnableMonsterList;
        }
        if(enumcreaturetype == EnumCreatureType.creature)
        {
            return spawnableCreatureList;
        }
        if(enumcreaturetype == EnumCreatureType.waterCreature)
        {
            return spawnableWaterCreatureList;
        } else
        {
            return null;
        }
    }

    public boolean getEnableSnow()
    {
        return enableSnow;
    }

    public boolean canSpawnLightningBolt()
    {
        if(enableSnow)
        {
            return false;
        } else
        {
            return enableRain;
        }
    }

    static 
    {
        generateBiomeLookup();
    }
}
