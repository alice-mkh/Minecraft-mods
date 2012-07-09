package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.nbxlite.mapgens.BiomeDecorator2;
import net.minecraft.src.nbxlite.mapgens.BiomeGenEmeraldHills;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;

public abstract class BiomeGenBase
{
    public static final BiomeGenBase biomeList[] = new BiomeGenBase[256];
    public static final BiomeGenBase ocean = (new BiomeGenOcean(0)).setColor(112).setBiomeName("Ocean").setMinMaxHeight(-1F, 0.4F);
    public static final BiomeGenBase plains = (new BiomeGenPlains(1)).setColor(0x8db360).setBiomeName("Plains").setTemperatureRainfall(0.8F, 0.4F);
    public static final BiomeGenBase desert = (new BiomeGenDesert(2)).setColor(0xfa9418).setBiomeName("Desert").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.1F, 0.2F);
    public static final BiomeGenBase extremeHills = (new BiomeGenEmeraldHills(3)).setColor(0x606060).setBiomeName("Extreme Hills").setMinMaxHeight(0.2F, 1.3F).setTemperatureRainfall(0.2F, 0.3F);
    public static final BiomeGenBase forest = (new BiomeGenForest(4)).setColor(0x56621).setBiomeName("Forest").func_4124_a(0x4eba31).setTemperatureRainfall(0.7F, 0.8F);
    public static final BiomeGenBase taiga = (new BiomeGenTaiga(5)).setColor(0xb6659).setBiomeName("Taiga").func_4124_a(0x4eba31).setEnableSnow().setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.1F, 0.4F);
    public static final BiomeGenBase swampland = (new BiomeGenSwamp(6)).setColor(0x7f9b2).setBiomeName("Swampland").func_4124_a(0x8baf48).setMinMaxHeight(-0.2F, 0.1F).setTemperatureRainfall(0.8F, 0.9F);
    public static final BiomeGenBase river = (new BiomeGenRiver(7)).setColor(255).setBiomeName("River").setMinMaxHeight(-0.5F, 0.0F);
    public static final BiomeGenBase hell = (new BiomeGenHell(8)).setColor(0xff0000).setBiomeName("Hell").setDisableRain().setTemperatureRainfall(2.0F, 0.0F);

    /** Is the biome used for sky world. */
    public static final BiomeGenBase sky = (new BiomeGenEnd(9)).setColor(0x8080ff).setBiomeName("Sky").setDisableRain();
    public static final BiomeGenBase frozenOcean = (new BiomeGenOcean(10)).setColor(0x9090a0).setBiomeName("FrozenOcean").setEnableSnow().setMinMaxHeight(-1F, 0.5F).setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase frozenRiver = (new BiomeGenRiver(11)).setColor(0xa0a0ff).setBiomeName("FrozenRiver").setEnableSnow().setMinMaxHeight(-0.5F, 0.0F).setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase icePlains = (new BiomeGenSnow(12)).setColor(0xffffff).setBiomeName("Ice Plains").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase iceMountains = (new BiomeGenSnow(13)).setColor(0xa0a0a0).setBiomeName("Ice Mountains").setEnableSnow().setMinMaxHeight(0.2F, 1.2F).setTemperatureRainfall(0.0F, 0.5F);
    public static final BiomeGenBase mushroomIsland = (new BiomeGenMushroomIsland(14)).setColor(0xff00ff).setBiomeName("MushroomIsland").setTemperatureRainfall(0.9F, 1.0F).setMinMaxHeight(0.2F, 1.0F);
    public static final BiomeGenBase mushroomIslandShore = (new BiomeGenMushroomIsland(15)).setColor(0xa000ff).setBiomeName("MushroomIslandShore").setTemperatureRainfall(0.9F, 1.0F).setMinMaxHeight(-1F, 0.1F);

    /** Beach biome. */
    public static final BiomeGenBase beach = (new BiomeGenBeach(16)).setColor(0xfade55).setBiomeName("Beach").setTemperatureRainfall(0.8F, 0.4F).setMinMaxHeight(0.0F, 0.1F);

    /** Desert Hills biome. */
    public static final BiomeGenBase desertHills = (new BiomeGenDesert(17)).setColor(0xd25f12).setBiomeName("DesertHills").setDisableRain().setTemperatureRainfall(2.0F, 0.0F).setMinMaxHeight(0.2F, 0.7F);

    /** Forest Hills biome. */
    public static final BiomeGenBase forestHills = (new BiomeGenForest(18)).setColor(0x22551c).setBiomeName("ForestHills").func_4124_a(0x4eba31).setTemperatureRainfall(0.7F, 0.8F).setMinMaxHeight(0.2F, 0.6F);

    /** Taiga Hills biome. */
    public static final BiomeGenBase taigaHills = (new BiomeGenTaiga(19)).setColor(0x163933).setBiomeName("TaigaHills").setEnableSnow().func_4124_a(0x4eba31).setTemperatureRainfall(0.05F, 0.8F).setMinMaxHeight(0.2F, 0.7F);

    /** Extreme Hills Edge biome. */
    public static final BiomeGenBase extremeHillsEdge = (new BiomeGenEmeraldHills(20)).setColor(0x72789a).setBiomeName("Extreme Hills Edge").setMinMaxHeight(0.2F, 0.8F).setTemperatureRainfall(0.2F, 0.3F);

    /** Jungle biome identifier */
    public static final BiomeGenBase jungle = (new BiomeGenJungle(21)).setColor(0x537b09).setBiomeName("Jungle").func_4124_a(0x537b09).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(0.2F, 0.4F);
    public static final BiomeGenBase jungleHills = (new BiomeGenJungle(22)).setColor(0x2c4205).setBiomeName("JungleHills").func_4124_a(0x537b09).setTemperatureRainfall(1.2F, 0.9F).setMinMaxHeight(1.8F, 0.2F);
    public String biomeName;
    public int color;

    /** The block expected to be on the top of this biome */
    public byte topBlock;

    /** The block to fill spots in when not on the top */
    public byte fillerBlock;
    public int field_6502_q;

    /** The minimum height of this biome. Default 0.1. */
    public float minHeight;

    /** The maximum height of this biome. Default 0.3. */
    public float maxHeight;

    /** The temperature of this biome. */
    public float temperature;

    /** The rainfall in this biome. */
    public float rainfall;

    /** Color tint applied to water depending on biome */
    public int waterColorMultiplier;
    public BiomeDecorator biomeDecorator;

    /**
     * Holds the classes of IMobs (hostile mobs) that can be spawned in the biome.
     */
    protected List spawnableMonsterList;

    /**
     * Holds the classes of any creature that can be spawned in the biome as friendly creature.
     */
    protected List spawnableCreatureList;

    /**
     * Holds the classes of any aquatic creature that can be spawned in the water of the biome.
     */
    protected List spawnableWaterCreatureList;

    /** Set to true if snow is enabled for this biome. */
    private boolean enableSnow;

    /**
     * Is true (default) if the biome support rain (desert and nether can't have rain)
     */
    public boolean enableRain;

    /** The id number to this biome, and its index in the biomeList array. */
    public final int biomeID;
    protected WorldGenTrees worldGenTrees;
    public OldWorldGenTrees oldWorldGenTrees;
    protected WorldGenBigTree worldGenBigTree;
    protected WorldGenForest worldGenForest;
    protected WorldGenSwamp worldGenSwamp;

    public static final BiomeGenBase betaRainforest = (new BiomeGenJungle(30)).setColor(0x8fa36).setBiomeName("Beta Rainforest").setTemperatureRainfall(1F, 1F).func_4124_a(0x1ff458);
    public static final BiomeGenBase betaSwampland = (new BiomeGenPlains(31)).setColor(0x9be023).setBiomeName("Beta Swampland").setTemperatureRainfall(0.75F, 0.5F).func_4124_a(0x8baf48);
    public static final BiomeGenBase betaSeasonalForest = (new BiomeGenForest(32)).setColor(0x8fa36).setBiomeName("Beta Seasonal Forest").setTemperatureRainfall(1F, 0.8F);
    public static final BiomeGenBase betaForest = (new BiomeGenForest(33)).setColor(0x56621).setBiomeName("Beta Forest").setTemperatureRainfall(0.7F, 0.75F).func_4124_a(0x4eba31);
    public static final BiomeGenBase betaSavanna = (new BiomeGenPlains(34)).setColor(0xd9e023).setBiomeName("Beta Savanna").setTemperatureRainfall(0.8F, 0.1F);
    public static final BiomeGenBase betaShrubland = (new BiomeGenPlains(35)).setColor(0xa1ad20).setBiomeName("Beta Shrubland").setTemperatureRainfall(0.7F, 0.1F);
    public static final BiomeGenBase betaTaiga = (new BiomeGenDesert(36)).setColor(0x2eb153).setBiomeName("Beta Taiga").setTemperatureRainfall(0.2F, 0.5F).func_4124_a(0x7bb731);
    public static final BiomeGenBase betaDesert = (new BiomeGenDesert(37)).setColor(0xfa9418).setBiomeName("Beta Desert").setTemperatureRainfall(1F, 0.1F).setDisableRain();
    public static final BiomeGenBase betaPlains = (new BiomeGenPlains(38)).setColor(0xffd910).setBiomeName("Beta Plains").setTemperatureRainfall(0.5F, 0.25F);
    public static final BiomeGenBase betaIceDesert = (new BiomeGenDesert(39)).setColor(0xffed93).setBiomeName("Beta Ice Desert").setTemperatureRainfall(0.2F, 0.1F).func_4124_a(0xc4d339).setEnableSnow().setDisableRain();
    public static final BiomeGenBase betaTundra = (new BiomeGenSnow(40)).setColor(0x57ebf9).setBiomeName("Beta Tundra").setTemperatureRainfall(0.1F, 0.1F).func_4124_a(0xc4d339).setEnableSnow();
    public static final BiomeGenBase betaHell = (new BiomeGenHell(41)).setColor(0xff0000).setBiomeName("Beta Hell").setTemperatureRainfall(1F, 1F).setDisableRain();
    public static final BiomeGenBase betaSky = (new BiomeGenEnd(42)).setColor(0x8080ff).setBiomeName("Beta Sky").setTemperatureRainfall(1F, 1F).setDisableRain();
    public static final BiomeGenBase notABiome = (new BiomeGenPlains(43)).setColor(0xffffff).setBiomeName("No biomes").setTemperatureRainfall(1F, 1F).setDisableRain();
    public static final BiomeGenBase notABiomeSnow = (new BiomeGenSnow(44)).setColor(0xffffff).setBiomeName("No biomes (winter)").setTemperatureRainfall(0.1F, 1F).setEnableSnow().setDisableRain();

    private BiomeGenBase registerOldBiome(int id, float temp, float humid, boolean rain, BiomeGenBase biome){
        biome.temperature = temp;
        biome.rainfall = humid;
        biome.enableRain = rain;
//         try{
//             ModLoader.setPrivateValue(net.minecraft.src.BiomeGenBase.class, biome, 38, rain);
//         }catch(Exception ex){
//             System.out.println(ex);
//         }
        BiomeGenBase.biomeList[id] = biome;
        return biome;
    }

    protected BiomeGenBase(int par1)
    {
        topBlock = (byte)Block.grass.blockID;
        fillerBlock = (byte)Block.dirt.blockID;
        field_6502_q = 0x4ee031;
        minHeight = 0.1F;
        maxHeight = 0.3F;
        temperature = 0.5F;
        rainfall = 0.5F;
        waterColorMultiplier = 0xffffff;
        spawnableMonsterList = new ArrayList();
        spawnableCreatureList = new ArrayList();
        spawnableWaterCreatureList = new ArrayList();
        enableRain = true;
        worldGenTrees = new WorldGenTrees(false);
        oldWorldGenTrees = new OldWorldGenTrees(false);
        worldGenBigTree = new WorldGenBigTree(false);
        worldGenForest = new WorldGenForest(false);
        worldGenSwamp = new WorldGenSwamp();
        biomeID = par1;
        biomeList[par1] = this;
        biomeDecorator = createBiomeDecorator();
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySheep.class, 12, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityPig.class, 10, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityChicken.class, 10, 4, 4));
        spawnableCreatureList.add(new SpawnListEntry(net.minecraft.src.EntityCow.class, 8, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySpider.class, 10, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityZombie.class, 10, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySkeleton.class, 10, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityCreeper.class, 10, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntitySlime.class, 10, 4, 4));
        spawnableMonsterList.add(new SpawnListEntry(net.minecraft.src.EntityEnderman.class, 1, 1, 4));
        spawnableWaterCreatureList.add(new SpawnListEntry(net.minecraft.src.EntitySquid.class, 10, 4, 4));
    }

    /**
     * Allocate a new BiomeDecorator for this BiomeGenBase
     */
    protected BiomeDecorator createBiomeDecorator()
    {
        return new BiomeDecorator2(this);
    }

    /**
     * Sets the temperature and rainfall of this biome.
     */
    private BiomeGenBase setTemperatureRainfall(float par1, float par2)
    {
        if (par1 > 0.1F && par1 < 0.2F)
        {
            throw new IllegalArgumentException("Please avoid temperatures in the range 0.1 - 0.2 because of snow");
        }
        else
        {
            temperature = par1;
            rainfall = par2;
            return this;
        }
    }

    /**
     * Sets the minimum and maximum height of this biome. Seems to go from -2.0 to 2.0.
     */
    private BiomeGenBase setMinMaxHeight(float par1, float par2)
    {
        minHeight = par1;
        maxHeight = par2;
        return this;
    }

    /**
     * Disable the rain for the biome.
     */
    private BiomeGenBase setDisableRain()
    {
        enableRain = false;
        return this;
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
    {
        if (par1Random.nextInt(10) == 0)
        {
            return worldGenBigTree;
        }
        else
        {
            return worldGenTrees;
        }
    }

    public WorldGenerator func_48410_b(Random par1Random)
    {
        return new WorldGenTallGrass(Block.tallGrass.blockID, 1);
    }

    /**
     * sets enableSnow to true during biome initialization. returns BiomeGenBase.
     */
    protected BiomeGenBase setEnableSnow()
    {
        enableSnow = true;
        return this;
    }

    protected BiomeGenBase setBiomeName(String par1Str)
    {
        biomeName = par1Str;
        return this;
    }

    protected BiomeGenBase func_4124_a(int par1)
    {
        field_6502_q = par1;
        return this;
    }

    protected BiomeGenBase setColor(int par1)
    {
        color = par1;
        return this;
    }

    /**
     * takes temperature, returns color
     */
    public int getSkyColorByTemp(float par1)
    {
        par1 /= 3F;

        if (par1 < -1F)
        {
            par1 = -1F;
        }

        if (par1 > 1.0F)
        {
            par1 = 1.0F;
        }

        return java.awt.Color.getHSBColor(0.6222222F - par1 * 0.05F, 0.5F + par1 * 0.1F, 1.0F).getRGB();
    }

    /**
     * Returns the correspondent list of the EnumCreatureType informed.
     */
    public List getSpawnableList(EnumCreatureType par1EnumCreatureType)
    {
        if (par1EnumCreatureType == EnumCreatureType.monster)
        {
            return spawnableMonsterList;
        }

        if (par1EnumCreatureType == EnumCreatureType.creature)
        {
            return spawnableCreatureList;
        }

        if (par1EnumCreatureType == EnumCreatureType.waterCreature)
        {
            return spawnableWaterCreatureList;
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns true if the biome have snowfall instead a normal rain.
     */
    public boolean getEnableSnow()
    {
        return enableSnow;
    }

    /**
     * Return true if the biome supports lightning bolt spawn, either by have the bolts enabled and have rain enabled.
     */
    public boolean canSpawnLightningBolt()
    {
        if (enableSnow)
        {
            return false;
        }
        else
        {
            return enableRain;
        }
    }

    /**
     * Checks to see if the rainfall level of the biome is extremely high
     */
    public boolean isHighHumidity()
    {
        return rainfall > 0.85F;
    }

    /**
     * returns the chance a creature has to spawn.
     */
    public float getSpawningChance()
    {
        return 0.1F;
    }

    /**
     * Gets an integer representation of this biome's rainfall
     */
    public final int getIntRainfall()
    {
        return (int)(rainfall * 65536F);
    }

    /**
     * Gets an integer representation of this biome's temperature
     */
    public final int getIntTemperature()
    {
        return (int)(temperature * 65536F);
    }

    /**
     * Gets a floating point representation of this biome's rainfall
     */
    public final float getFloatRainfall()
    {
        return rainfall;
    }

    /**
     * Gets a floating point representation of this biome's temperature
     */
    public final float getFloatTemperature()
    {
        return temperature;
    }

    public void decorate(World par1World, Random par2Random, int par3, int par4)
    {
        biomeDecorator.decorate(par1World, par2Random, par3, par4);
    }

    /**
     * Provides the basic grass color based on the biome temperature and rainfall
     */
    public int getBiomeGrassColor()
    {
        double d = MathHelper.clamp_float(getFloatTemperature(), 0.0F, 1.0F);
        double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
        return ColorizerGrass.getGrassColor(d, d1);
    }

    public int getBiomeGrassColor2()
    {
        if (this==swampland && mod_noBiomesX.MapFeatures>=mod_noBiomesX.FEATURES_10){
            double d = getFloatTemperature();
            double d1 = getFloatRainfall();
            return ((ColorizerGrass.getGrassColor(d, d1) & 0xfefefe) + 0x4e0e4e) / 2;
        }
        double d = MathHelper.clamp_float(getFloatTemperature(), 0.0F, 1.0F);
        double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
        return ColorizerGrass.getGrassColor(d, d1);
    }

    /**
     * Provides the basic foliage color based on the biome temperature and rainfall
     */
    public int getBiomeFoliageColor()
    {
        double d = MathHelper.clamp_float(getFloatTemperature(), 0.0F, 1.0F);
        double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
        return ColorizerFoliage.getFoliageColor(d, d1);
    }

    public int getBiomeFoliageColor2()
    {
        if (this==swampland && mod_noBiomesX.MapFeatures>=mod_noBiomesX.FEATURES_10){
            double d = getFloatTemperature();
            double d1 = getFloatRainfall();
            return ((ColorizerFoliage.getFoliageColor(d, d1) & 0xfefefe) + 0x4e0e4e) / 2;
        }
        double d = MathHelper.clamp_float(getFloatTemperature(), 0.0F, 1.0F);
        double d1 = MathHelper.clamp_float(getFloatRainfall(), 0.0F, 1.0F);
        return ColorizerFoliage.getFoliageColor(d, d1);
    }
}
