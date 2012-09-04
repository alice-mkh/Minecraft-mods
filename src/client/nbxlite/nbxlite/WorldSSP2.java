package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.src.nbxlite.oldbiomes.*;
import net.minecraft.src.nbxlite.spawners.*;
import net.minecraft.src.nbxlite.indev.*;
import net.minecraft.src.nbxlite.chunkproviders.*;

public class WorldSSP2 extends WorldSSP
{
    public boolean snowCovered;
    public int mapGen;
    public int mapGenExtra;
    public int mapTypeIndev;
    protected OldSpawnerAnimals animalSpawner;
    protected OldSpawnerMonsters monsterSpawner;
    protected OldSpawnerAnimals waterMobSpawner;

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
        if (ODNBXlite.Generator == ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures == ODNBXlite.FEATURES_INDEV){
            int j = worldInfo.getSpawnX();
            int k = worldInfo.getSpawnZ();
            int l = worldInfo.getSpawnY() + 2;
            int dir = rand.nextInt(3);
            if (dir == 0){
                j -= 2;
            }else if (dir == 1){
                j += 2;
            }else if (dir == 2){
                k += 2;
            }
            setBlockWithNotify(j, l, k, Block.chest.blockID);
            TileEntityChest tileentitychest = (TileEntityChest)getBlockTileEntity(j, l, k);
            if (tileentitychest != null && tileentitychest != null){
                WeightedRandomChestContent.func_76293_a(rand, bonusChestContent, tileentitychest, 10);
            }
            return;
        }
        WorldGeneratorBonusChest worldgeneratorbonuschest = new WorldGeneratorBonusChest(bonusChestContent, 10);
        int i = 0;
        do
        {
            if (i >= 10)
            {
                break;
            }
            int j = (worldInfo.getSpawnX() + rand.nextInt(6)) - rand.nextInt(6);
            int k = (worldInfo.getSpawnZ() + rand.nextInt(6)) - rand.nextInt(6);
            int l = getTopSolidOrLiquidBlock(j, k) + 1;

            if (worldgeneratorbonuschest.generate(this, rand, j, l, k))
            {
                break;
            }
            i++;
        }
        while (true);
    }

    public WorldSSP2(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler p)
    {
        super(par1ISaveHandler, par2Str, par3WorldProvider, par4WorldSettings, p);
        par3WorldProvider.registerWorld(this);
        ODNBXlite.SetGenerator(this, ODNBXlite.GEN_NEWBIOMES, ODNBXlite.FEATURES_12, ODNBXlite.THEME_NORMAL, ODNBXlite.TYPE_INLAND, false, false);
        ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
        ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
        ODNBXlite.setIndevBounds(ODNBXlite.IndevMapType, ODNBXlite.MapTheme);
        ODNBXlite.refreshProperties();
//         ODNBXlite NBX = new ODNBXlite();
//         NBX.RequestGeneratorInfo();
        turnOnOldSpawners();
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    public WorldSSP2(WorldSSP par1World, WorldProvider par2WorldProvider, Profiler p)
    {
        super(par1World, par2WorldProvider, p);
        mapGen = worldInfo.mapGen;
        mapGenExtra = worldInfo.mapGenExtra;
        snowCovered = worldInfo.snowCovered;
        ODNBXlite.SetGenerator(this, mapGen, mapGenExtra, worldInfo.mapTheme, worldInfo.mapType, snowCovered, worldInfo.newOres);
        ODNBXlite.refreshProperties();
        turnOnOldSpawners();
        par2WorldProvider.registerWorld(this);
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    public WorldSSP2(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, Profiler p)
    {
        this(par1ISaveHandler, par2Str, par3WorldSettings, ((WorldProvider)(null)), p);
    }

    public WorldSSP2(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, WorldProvider par4WorldProvider, Profiler p)
    {
        super(par1ISaveHandler, par2Str, par3WorldSettings, par4WorldProvider, p);
        worldInfo = par1ISaveHandler.loadWorldInfo();
        isNewWorld = worldInfo == null;

        boolean flag = false;

        if (worldInfo == null)
        {
            worldInfo = new WorldInfo(par3WorldSettings, par2Str);
            flag = true;
        }
        else
        {
            worldInfo.setWorldName(par2Str);
        }

        provider.registerWorld(this);
        chunkProvider = createChunkProvider();

        if (flag)
         {
            worldInfo.nbxlite = true;
            worldInfo.mapGen = ODNBXlite.Generator;
            worldInfo.mapGenExtra = ODNBXlite.MapFeatures;
            worldInfo.mapTheme = ODNBXlite.MapTheme;
            worldInfo.newOres = ODNBXlite.GenerateNewOres;
            mapGen=ODNBXlite.Generator;
            mapGenExtra=ODNBXlite.MapFeatures;
            if(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (ODNBXlite.MapTheme==ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS) && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_ALPHA11201)
            {
                if (!ODNBXlite.Import){
                    if(rand.nextInt(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS ? 2 : 4) == 0)
                    {
                        worldInfo.snowCovered = true;
                        snowCovered = true;
                        ODNBXlite.SnowCovered = true;
                    }else{
                        ODNBXlite.SnowCovered=false;
                    }
                }else{
                    snowCovered = worldInfo.snowCovered;
                    ODNBXlite.SnowCovered=worldInfo.snowCovered;
                }
            }else{
                ODNBXlite.SnowCovered=false;
            }
            ODNBXlite.SetGenerator(this, ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType, ODNBXlite.SnowCovered, ODNBXlite.GenerateNewOres);
            if (!(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV && ODNBXlite.Import)){
                worldInfo.cloudheight = ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
                worldInfo.skybrightness = ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
                worldInfo.skycolor = ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
                worldInfo.fogcolor = ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
                worldInfo.cloudcolor = ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
            }
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
                if (!ODNBXlite.Import){
                    ODNBXlite.generateIndevLevel(getSeed());
                    for (int x=-2; x<(ODNBXlite.IndevWidthX/16)+2; x++){
                        for (int z=-2; z<(ODNBXlite.IndevWidthZ/16)+2; z++){
                            chunkProvider.provideChunk(x,z);
                        }
                    }
                    ODNBXlite.IndevWorld = null;
                    ODNBXlite.setIndevBounds(ODNBXlite.IndevMapType, ODNBXlite.MapTheme);
                }else{
                    mod_OldDays.getMinecraft().loadingScreen.resetProgressAndMessage("Importing Indev level");
                    mod_OldDays.getMinecraft().loadingScreen.resetProgresAndWorkingMessage("Loading blocks..");
                    for (int x=-2; x<(ODNBXlite.IndevWidthX/16)+2; x++){
                        mod_OldDays.getMinecraft().loadingScreen.setLoadingProgress((x / ((ODNBXlite.IndevWidthX/16)+2)) * 100);
                        for (int z=-2; z<(ODNBXlite.IndevWidthZ/16)+2; z++){
                            chunkProvider.provideChunk(x,z);
                        }
                    }
                    worldInfo.setWorldTime(ODNBXlite.mclevelimporter.timeofday);
                    List tentlist = ODNBXlite.mclevelimporter.tileentities;
                    mod_OldDays.getMinecraft().loadingScreen.resetProgresAndWorkingMessage("Fixing blocks..");
                    for (int x = 0; x < ODNBXlite.IndevWidthX; x++){
                        mod_OldDays.getMinecraft().loadingScreen.setLoadingProgress((int)(((float)x / (float)ODNBXlite.IndevWidthX) * 100F));
                        for (int y = 0; y < ODNBXlite.IndevHeight; y++){
                            for (int z = 0; z < ODNBXlite.IndevWidthZ; z++){
                                int id = getBlockId(x, y, z);
                                int meta = ODNBXlite.mclevelimporter.data[IndexFinite(x, y, z)] >> 4;
                                if (ODNBXlite.mclevelimporter.needsFixing(id)){
                                    setBlockAndMetadata(x, y, z, ODNBXlite.mclevelimporter.getRightId(id), ODNBXlite.mclevelimporter.getRightMetadata(id));
                                }else if (id != 0 && meta != 0){
                                    setBlockMetadata(x, y, z, meta);
                                }
                                if (Block.lightValue[id]>0){
                                    updateAllLightTypes(x, y, z);
                                }
                                if (id > 0 && Block.blocksList[id].hasTileEntity()){
                                    for (int i=0; i < tentlist.size(); i++){
                                        TileEntity tent = ((TileEntity)tentlist.get(i));
                                        if (tent.xCoord == x && tent.yCoord == y && tent.zCoord == z){
                                            setBlockTileEntity(x, y, z, tent);
                                            tentlist.remove(i);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    mod_OldDays.getMinecraft().loadingScreen.resetProgresAndWorkingMessage("Loading entities..");
                    List entlist = ODNBXlite.mclevelimporter.entities;
                    for (int i = 0; i < entlist.size(); i++){
                        Entity entity = EntityList.createEntityFromNBT(((NBTTagCompound)entlist.get(i)), this);
                        spawnEntityInWorld(entity);
                    }
                    worldInfo.cloudheight = ODNBXlite.CloudHeight;
                    worldInfo.skybrightness = ODNBXlite.SkyBrightness;
                    worldInfo.skycolor = ODNBXlite.SkyColor;
                    worldInfo.fogcolor = ODNBXlite.FogColor;
                    worldInfo.cloudcolor = ODNBXlite.CloudColor;
                }
                mapTypeIndev=ODNBXlite.IndevMapType;
                worldInfo.mapType = ODNBXlite.IndevMapType;
                worldInfo.indevX = ODNBXlite.IndevWidthX;
                worldInfo.indevZ = ODNBXlite.IndevWidthZ;
                worldInfo.indevY = ODNBXlite.IndevHeight;
            }else if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC){
                ODNBXlite.generateClassicLevel(getSeed());
                for (int x=-2; x<(ODNBXlite.IndevWidthX/16)+2; x++){
                    for (int z=-2; z<(ODNBXlite.IndevWidthZ/16)+2; z++){
                        chunkProvider.provideChunk(x,z);
                    }
                }
                ODNBXlite.IndevWorld = null;
                mapTypeIndev=0;
                worldInfo.mapType = 0;
                worldInfo.indevX = ODNBXlite.IndevWidthX;
                worldInfo.indevZ = ODNBXlite.IndevWidthZ;
                worldInfo.indevY = ODNBXlite.IndevHeight;
                ODNBXlite.setIndevBounds(5, ODNBXlite.MapTheme);
            }else{
                mapTypeIndev=0;
                worldInfo.mapType = 0;
            }
            generateSpawnPoint();
            if (par3WorldSettings.isBonusChestEnabled()){
                createBonusChest();
            }
        } else
        {
            if (worldInfo.nbxlite){
                mapGen = worldInfo.mapGen;
                mapGenExtra = worldInfo.mapGenExtra;
                snowCovered = worldInfo.snowCovered;
                mapTypeIndev = worldInfo.mapType;
                ODNBXlite.IndevWidthX = worldInfo.indevX;
                ODNBXlite.IndevWidthZ = worldInfo.indevZ;
                ODNBXlite.IndevHeight = worldInfo.indevY;
                ODNBXlite.SurrWaterType = worldInfo.surrwatertype;
                ODNBXlite.SurrWaterHeight = worldInfo.surrwaterheight;
                ODNBXlite.SurrGroundType = worldInfo.surrgroundtype;
                ODNBXlite.SurrGroundHeight = worldInfo.surrgroundheight;
                ODNBXlite.CloudHeight = worldInfo.cloudheight;
                ODNBXlite.SkyBrightness = worldInfo.skybrightness;
                ODNBXlite.SkyColor = worldInfo.skycolor;
                ODNBXlite.FogColor = worldInfo.fogcolor;
                ODNBXlite.CloudColor = worldInfo.cloudcolor;
                ODNBXlite.SetGenerator(this, mapGen, mapGenExtra, worldInfo.mapTheme, mapTypeIndev, snowCovered, worldInfo.newOres);
            }else{
                ODNBXlite.SetGenerator(this, ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType, ODNBXlite.SnowCovered, ODNBXlite.GenerateNewOres);
                worldInfo.nbxlite = true;
                worldInfo.mapGen = ODNBXlite.Generator;
                worldInfo.mapGenExtra = ODNBXlite.MapFeatures;
                worldInfo.mapTheme = ODNBXlite.MapTheme;
                worldInfo.newOres = ODNBXlite.GenerateNewOres;
                worldInfo.mapType = ODNBXlite.IndevMapType;
                worldInfo.indevX = ODNBXlite.IndevWidthX;
                worldInfo.indevZ = ODNBXlite.IndevWidthZ;
                worldInfo.indevY = ODNBXlite.IndevHeight;
                ODNBXlite.setIndevBounds(ODNBXlite.IndevMapType, ODNBXlite.MapTheme);
                worldInfo.surrwatertype = ODNBXlite.SurrWaterType;
                worldInfo.surrwaterheight = ODNBXlite.SurrWaterHeight;
                worldInfo.surrgroundtype = ODNBXlite.SurrGroundType;
                worldInfo.surrgroundheight = ODNBXlite.SurrGroundHeight;
                worldInfo.cloudheight = ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
                worldInfo.skybrightness = ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
                worldInfo.skycolor = ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
                worldInfo.fogcolor = ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
                worldInfo.cloudcolor = ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
            }
            provider.registerWorld(this);
        }
        ODNBXlite.refreshProperties();
        ODNBXlite.setTextureFX();
        turnOnOldSpawners();
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    public void turnOnOldSpawners()
    {
        animalSpawner = new OldSpawnerAnimals(15, net.minecraft.src.EntityAnimal.class, new Class[] {
            net.minecraft.src.EntitySheep.class, net.minecraft.src.EntityPig.class, net.minecraft.src.EntityCow.class, net.minecraft.src.EntityChicken.class,
            net.minecraft.src.EntityWolf.class, net.minecraft.src.EntityOcelot.class
        });
        monsterSpawner = new OldSpawnerMonsters(200, net.minecraft.src.IMob.class, new Class[] {
            net.minecraft.src.EntityZombie.class, net.minecraft.src.EntitySkeleton.class, net.minecraft.src.EntityCreeper.class, net.minecraft.src.EntitySpider.class,
            net.minecraft.src.EntitySlime.class, net.minecraft.src.EntityEnderman.class
        });
        waterMobSpawner = new OldSpawnerAnimals(5, net.minecraft.src.EntityWaterMob.class, new Class[] {
            net.minecraft.src.EntitySquid.class
        });
    }

    /**
     * Finds an initial spawn location upon creating a new world
     */
    protected void generateSpawnPoint()
    {
        if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES){
            if (ODNBXlite.MapFeatures<ODNBXlite.FEATURES_11){
                findingSpawnPoint = true;
                WorldChunkManager worldchunkmanager = getWorldChunkManager();
                List list = worldchunkmanager.getBiomesToSpawnIn();
                Random random = new Random(getSeed());
                ChunkPosition chunkposition = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
                int i = 0;
                int j = 64;
                int k = 0;
                if(chunkposition != null)
                {
                    i = chunkposition.x;
                    k = chunkposition.z;
                } else
                {
                    System.out.println("Unable to find spawn biome");
                }
                int l = 0;
                do
                {
                    if(provider.canCoordinateBeSpawn(i, k))
                    {
                        break;
                    }
                    i += random.nextInt(64) - random.nextInt(64);
                    k += random.nextInt(64) - random.nextInt(64);
                } while(++l != 1000);
                worldInfo.setSpawnPosition(i, j, k);
                findingSpawnPoint = false;
            }else{
                if (!provider.canRespawnHere())
                {
                    worldInfo.setSpawnPosition(0, provider.getAverageGroundLevel(), 0);
                    return;
                }
                findingSpawnPoint = true;
                WorldChunkManager worldchunkmanager = getWorldChunkManager();
                List list = worldchunkmanager.getBiomesToSpawnIn();
                Random random = new Random(getSeed());
                ChunkPosition chunkposition = worldchunkmanager.findBiomePosition(0, 0, 256, list, random);
                int i = 0;
                int j = provider.getAverageGroundLevel();
                int k = 0;
                if (chunkposition != null)
                {
                    i = chunkposition.x;
                    k = chunkposition.z;
                }
                else
                {
                    System.out.println("Unable to find spawn biome");
                }
                int l = 0;
                do
                {
                    if (provider.canCoordinateBeSpawn(i, k))
                    {
                        break;
                    }
                    i += random.nextInt(64) - random.nextInt(64);
                    k += random.nextInt(64) - random.nextInt(64);
                }
                while (++l != 1000);
                worldInfo.setSpawnPosition(i, j, k);
                findingSpawnPoint = false;
            }
        }else if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
            findingSpawnPoint = true;
            worldInfo.setSpawnPosition(ODNBXlite.IndevSpawnX, ODNBXlite.IndevSpawnY, ODNBXlite.IndevSpawnZ);
            if (!ODNBXlite.Import){
                setBlockWithNotify(ODNBXlite.IndevSpawnX-2, ODNBXlite.IndevSpawnY+3, ODNBXlite.IndevSpawnZ, Block.torchWood.blockID);
                setBlockWithNotify(ODNBXlite.IndevSpawnX+2, ODNBXlite.IndevSpawnY+3, ODNBXlite.IndevSpawnZ, Block.torchWood.blockID);
            }
            findingSpawnPoint = false;
        }else if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC){
            findingSpawnPoint = true;
            worldInfo.setSpawnPosition(ODNBXlite.IndevSpawnX, ODNBXlite.IndevSpawnY, ODNBXlite.IndevSpawnZ);
            findingSpawnPoint = false;
        }else{
            findingSpawnPoint = true;
            int i = 0;
            byte byte0 = 64;
            int j;
            for(j = 0; !provider.canCoordinateBeSpawn(i, j); j += rand.nextInt(64) - rand.nextInt(64)){
                i += rand.nextInt(64) - rand.nextInt(64);
            }
            worldInfo.setSpawnPosition(i, byte0, j);
            findingSpawnPoint = false;
        }
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setSpawnLocation()
    {
        if (ODNBXlite.isFinite()){
            findingSpawnPoint = true;
            worldInfo.setSpawnX(worldInfo.getSpawnX());
            worldInfo.setSpawnY(worldInfo.getSpawnY());
            worldInfo.setSpawnZ(worldInfo.getSpawnZ());
            findingSpawnPoint = false;
        }else if (ODNBXlite.Generator!=ODNBXlite.GEN_NEWBIOMES){
            if(worldInfo.getSpawnY() <= 0)
            {
                worldInfo.setSpawnY(64);
            }
            int i = worldInfo.getSpawnX();
            int j;
            for(j = worldInfo.getSpawnZ(); getFirstUncoveredBlock(i, j) == 0; j += rand.nextInt(8) - rand.nextInt(8))
            {
                i += rand.nextInt(8) - rand.nextInt(8);
            }
            worldInfo.setSpawnX(i);
            worldInfo.setSpawnZ(j);
        }else{
            if(worldInfo.getSpawnY() <= 0)
            {
                worldInfo.setSpawnY(64);
            }
            int i = worldInfo.getSpawnX();
            int j = worldInfo.getSpawnZ();
            int k = 0;
            do
            {
                if(getFirstUncoveredBlock(i, j) != 0)
                {
                    break;
                }
                i += rand.nextInt(8) - rand.nextInt(8);
                j += rand.nextInt(8) - rand.nextInt(8);
            } while(++k != 10000);
            if (ODNBXlite.MapFeatures<ODNBXlite.FEATURES_11){
                for(j = worldInfo.getSpawnZ(); getFirstUncoveredBlock(i, j) == 0; j += rand.nextInt(8) - rand.nextInt(8))
                {
                    i += rand.nextInt(8) - rand.nextInt(8);
                }
            }
            worldInfo.setSpawnX(i);
            worldInfo.setSpawnZ(j);
        }
    }

    /**
     * spawns a player, load data from level.dat if needed and loads surrounding chunks
     */
    public void spawnPlayerWithLoadedChunks(EntityPlayer par1EntityPlayer)
    {
        try
        {
            NBTTagCompound nbttagcompound = worldInfo.getPlayerNBTTagCompound();
            if (ODNBXlite.isFinite() && ODNBXlite.Import){
                par1EntityPlayer.readFromNBT(ODNBXlite.mclevelimporter.localplayer);
                ODNBXlite.mclevelimporter = null;
            }
            ODNBXlite.Import = false;

            if (nbttagcompound != null)
            {
                par1EntityPlayer.readFromNBT(nbttagcompound);
                worldInfo.setPlayerNBTTagCompound(null);
            }

            if (chunkProvider instanceof ChunkProviderLoadOrGenerate)
            {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)chunkProvider;
                int i = MathHelper.floor_float((int)par1EntityPlayer.posX) >> 4;
                int j = MathHelper.floor_float((int)par1EntityPlayer.posZ) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
            }

            spawnEntityInWorld(par1EntityPlayer);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        field_35467_J = field_35468_K;
        field_35468_K += field_35465_L;
        field_35465_L *= 0.97999999999999998D;
        if (getWorldInfo().isHardcoreModeEnabled() && difficultySetting < 3)
        {
            difficultySetting = 3;
        }

        provider.worldChunkMgr.cleanupCache();
        if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES ||
           (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES &&
           (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA15 ||
            ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA173 ||
            ODNBXlite.MapFeatures==ODNBXlite.FEATURES_JUNGLE))){
            updateWeather();
        }

        if (isAllPlayersFullyAsleep())
        {
            boolean flag = false;

            if (spawnHostileMobs)
            {
                if (difficultySetting < 1);
            }

            if (!flag)
            {
                long l = worldInfo.getWorldTime() + 24000L;
                worldInfo.setWorldTime(l - l % 24000L);
                wakeUpAllPlayers();
            }
        }

        theProfiler.startSection("mobSpawner");
        if (provider.worldType!=1){
            if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES || !ODNBXlite.OldSpawning){
                SpawnerAnimals.performSpawningSP(this, spawnHostileMobs, spawnPeacefulMobs && worldInfo.getWorldTime() % 400L == 0L);
            } else if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES || provider.worldType!=0){
                SpawnerAnimalsBeta.performSpawning(this, spawnHostileMobs, spawnPeacefulMobs);
            } else if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
                animalSpawner.func_1150_a(this);
                monsterSpawner.func_1150_a(this);
                waterMobSpawner.func_1150_a(this);
            }
        }else{
            SpawnerAnimals.performSpawningSP(this, spawnHostileMobs, spawnPeacefulMobs);
        }
        theProfiler.endStartSection("chunkSource");
        chunkProvider.unload100OldestChunks();
        int i = calculateSkylightSubtracted(1.0F);

        if (i != skylightSubtracted)
        {
            skylightSubtracted = i;
        }

        long l1 = worldInfo.getWorldTime() + 1L;

        if (l1 % (long)autosavePeriod == 0L)
        {
            theProfiler.endStartSection("save");
            saveWorld(false, null);
        }

        worldInfo.setWorldTime(l1);
        theProfiler.endStartSection("tickPending");
        tickUpdates(false);
        theProfiler.endStartSection("tickTiles");
        tickBlocksAndAmbiance();
        theProfiler.endStartSection("village");
        villageCollectionObj.tick();
        villageSiegeObj.tick();
        theProfiler.endSection();
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    protected void tickBlocksAndAmbiance()
    {
        func_48461_r();
        int i = 0;
        int j = 0;

        for (Iterator iterator = activeChunkSet.iterator(); iterator.hasNext(); theProfiler.endSection())
        {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();
            int k = chunkcoordintpair.chunkXPos * 16;
            int l = chunkcoordintpair.chunkZPos * 16;
            theProfiler.startSection("getChunk");
            Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
            func_48458_a(k, l, chunk);
            theProfiler.endStartSection("thunder");

            if (rand.nextInt(0x186a0) == 0 && isRaining() && isThundering())
            {
                updateLCG = updateLCG * 3 + 0x3c6ef35f;
                int i1 = updateLCG >> 2;
                int k1 = k + (i1 & 0xf);
                int j2 = l + (i1 >> 8 & 0xf);
                int i3 = getPrecipitationHeight(k1, j2);

                if (canLightningStrikeAt(k1, i3, j2))
                {
                    addWeatherEffect(new EntityLightningBolt(this, k1, i3, j2));
                    lastLightningBolt = 2;
                }
            }

            theProfiler.endStartSection("iceandsnow");
            if(rand.nextInt(4) == 0 && ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && snowCovered && ODNBXlite.SnowCovered && provider.worldType==0)
            {
                updateLCG = updateLCG * 3 + 0x3c6ef35f;
                int l2 = updateLCG >> 2;
                int l3 = l2 & 0xf;
                int l4 = l2 >> 8 & 0xf;
                int l5 = getPrecipitationHeight(l3 + k, l4 + l);
                if(l5 >= 0 && l5 < 128 && chunk.getSavedLightValue(EnumSkyBlock.Block, l3, l5, l4) < 10)
                {
                    int k6 = chunk.getBlockID(l3, l5 - 1, l4);
                    int i7 = chunk.getBlockID(l3, l5, l4);
                    if(i7 == 0 && Block.snow.canPlaceBlockAt(this, l3 + k, l5, l4 + l) && k6 != 0 && k6 != Block.ice.blockID && Block.blocksList[k6].blockMaterial.isSolid())
                    {
                        setBlockWithNotify(l3 + k, l5, l4 + l, Block.snow.blockID);
                    }
                    if((k6 == Block.waterMoving.blockID || k6 == Block.waterStill.blockID) && chunk.getBlockMetadata(l3, l5 - 1, l4) == 0)
                    {
                        setBlockWithNotify(l3 + k, l5 - 1, l4 + l, Block.ice.blockID);
                    }
                }
            }else if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES){
                updateLCG = updateLCG * 3 + 0x3c6ef35f;
                int l7 = updateLCG >> 2;
                int l8 = l7 & 0xf;
                int l9 = l7 >> 8 & 0xf;
                int l10 = getPrecipitationHeight(l8 + k, l9 + l);
                if(isRaining() && isBlockFreezable(l8 + k, l10 - 1, l9 + l))
                {
                    setBlockWithNotify(l8 + k, l10 - 1, l9 + l, Block.ice.blockID);
                }
                if(isRaining() && canSnowAt(l8 + k, l10, l9 + l))
                {
                    setBlockWithNotify(l8 + k, l10, l9 + l, Block.snow.blockID);
                }
            }else{
                if(rand.nextInt(16) == 0 && (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA15 || ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA173))
                {
                    updateLCG = updateLCG * 3 + 0x3c6ef35f;
                    int l7 = updateLCG >> 2;
                    int l8 = l7 & 0xf;
                    int l9 = l7 >> 8 & 0xf;
                    int l10 = getPrecipitationHeight(l8 + k, l9 + l);
                    if(getWorldChunkManager().oldGetBiomeGenAt(l8 + k, l9 + l).getEnableSnow() && l10 >= 0 && l10 < 128 && chunk.getSavedLightValue(EnumSkyBlock.Block, l8, l10, l9) < 10)
                    {
                        int i66 = chunk.getBlockID(l8, l10 - 1, l9);
                        int k66 = chunk.getBlockID(l8, l10, l9);
                        if(isRaining() && k66 == 0 && Block.snow.canPlaceBlockAt(this, l8 + k, l10, l9 + l) && i66 != 0 && i66 != Block.ice.blockID && Block.blocksList[i66].blockMaterial.isSolid())
                        {
                            setBlockWithNotify(l8 + k, l10, l9 + l, Block.snow.blockID);
                        }
                        if(i66 == Block.waterStill.blockID && chunk.getBlockMetadata(l8, l10 - 1, l9) == 0)
                        {
                            setBlockWithNotify(l8 + k, l10 - 1, l9 + l, Block.ice.blockID);
                        }
                    }
                }
            }
            theProfiler.endStartSection("tickTiles");
            ExtendedBlockStorage aextendedblockstorage[] = chunk.getBlockStorageArray();
            int i2 = aextendedblockstorage.length;

            for (int l2 = 0; l2 < i2; l2++)
            {
                ExtendedBlockStorage extendedblockstorage = aextendedblockstorage[l2];

                if (extendedblockstorage == null || !extendedblockstorage.getNeedsRandomTick())
                {
                    continue;
                }

                for (int k3 = 0; k3 < 3; k3++)
                {
                    updateLCG = updateLCG * 3 + 0x3c6ef35f;
                    int l3 = updateLCG >> 2;
                    int i4 = l3 & 0xf;
                    int j4 = l3 >> 8 & 0xf;
                    int k4 = l3 >> 16 & 0xf;
                    int l4 = extendedblockstorage.getExtBlockID(i4, k4, j4);
                    j++;
                    Block block = Block.blocksList[l4];

                    if (block != null && block.getTickRandomly())
                    {
                        i++;
                        block.updateTick(this, i4 + k, k4 + extendedblockstorage.getYLocation(), j4 + l, rand);
                    }
                }
            }
        }
    }

    /**
     * Called from World constructor to set rainingStrength and thunderingStrength
     */
    private void calculateInitialWeather()
    {
        if (worldInfo.isRaining())
        {
            rainingStrength = 1.0F;

            if (worldInfo.isThundering())
            {
                thunderingStrength = 1.0F;
            }
        }
    }

    private int IndexFinite(int x, int y, int z){
        return x+(y*ODNBXlite.IndevWidthZ+z)*ODNBXlite.IndevWidthX;
    }
}
