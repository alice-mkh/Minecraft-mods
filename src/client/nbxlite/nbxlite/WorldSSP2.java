package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.oldbiomes.*;
import net.minecraft.src.nbxlite.spawners.*;
import net.minecraft.src.nbxlite.indev.*;
import net.minecraft.src.nbxlite.chunkproviders.*;

public class WorldSSP2 extends WorldSSP
{
    protected OldSpawnerAnimals animalSpawner;
    protected OldSpawnerMonsters monsterSpawner;
    protected OldSpawnerAnimals waterMobSpawner;
    protected OldSpawnerAnimals ambientMobSpawner;

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
        if (ODNBXlite.Generator == ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures == ODNBXlite.FEATURES_INDEV && ODNBXlite.IndevSpawnY < ODNBXlite.IndevHeight){
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
                WeightedRandomChestContent.generateChestContents(rand, bonusChestContent, tileentitychest, 10);
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
        turnOnOldSpawners();
        calculateInitialSkylight();
        calculateInitialWeather();
        ODNBXlite.IndevWorld = null;
    }

    public WorldSSP2(WorldSSP par1World, WorldProvider par2WorldProvider, Profiler p)
    {
        super(par1World, par2WorldProvider, p);
//         ODNBXlite.SetGenerator(this, worldInfo.mapsGen, worldInfo.mapGenExtra, worldInfo.mapTheme, worldInfo.mapType, worldInfo.snowCovered, worldInfo.newOres);
        ODNBXlite.refreshProperties();
        turnOnOldSpawners();
        par2WorldProvider.registerWorld(this);
        calculateInitialSkylight();
        calculateInitialWeather();
        ODNBXlite.IndevWorld = null;
    }

    public WorldSSP2(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, Profiler p)
    {
        this(par1ISaveHandler, par2Str, par3WorldSettings, ((WorldProvider)(null)), p);
    }

    public WorldSSP2(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, WorldProvider par4WorldProvider, Profiler p)
    {
        super(par1ISaveHandler, par2Str, par3WorldSettings, par4WorldProvider, p);
        provider.registerWorld(this);

        if (isNewWorld)
        {
            generateSpawnPoint();
            if (par3WorldSettings.isBonusChestEnabled()){
                createBonusChest();
            }
        }
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
        ambientMobSpawner = new OldSpawnerAnimals(15, net.minecraft.src.EntityAmbientCreature.class, new Class[] {
            net.minecraft.src.EntityBat.class
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
            if (!ODNBXlite.Import && ODNBXlite.IndevSpawnY < ODNBXlite.IndevHeight){
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
                long l = worldInfo.getWorldTotalTime() + 24000L;
                worldInfo.setWorldTime(l - l % 24000L);
                func_82738_a(l - l % 24000L);
                wakeUpAllPlayers();
            }
        }

        theProfiler.startSection("mobSpawner");
        if (getGameRules().getGameRuleBooleanValue("doMobSpawning")){
            if (provider.dimensionId!=1){
                if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES || !ODNBXlite.OldSpawning){
                    SpawnerAnimals.performSpawningSP(this, spawnHostileMobs, spawnPeacefulMobs && worldInfo.getWorldTotalTime() % 400L == 0L);
                } else if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES || provider.dimensionId!=0){
                    SpawnerAnimalsBeta.performSpawning(this, spawnHostileMobs, spawnPeacefulMobs);
                } else if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
                    animalSpawner.func_1150_a(this);
                    monsterSpawner.func_1150_a(this);
                    waterMobSpawner.func_1150_a(this);
                    ambientMobSpawner.func_1150_a(this);
                }
            }else{
                SpawnerAnimals.performSpawningSP(this, spawnHostileMobs, spawnPeacefulMobs);
            }
        }
        theProfiler.endStartSection("chunkSource");
        chunkProvider.unload100OldestChunks();
        int i = calculateSkylightSubtracted(1.0F);

        if (i != skylightSubtracted)
        {
            skylightSubtracted = i;
            if (ODNBXlite.oldLightEngine){
                for(int j = 0; j < worldAccesses.size(); j++)
                {
                    ((RenderGlobal)worldAccesses.get(j)).updateAllRenderers(false);
                }
            }
        }

        long l1 = worldInfo.getWorldTotalTime() + 1L;

        if (l1 % (long)autosavePeriod == 0L)
        {
            theProfiler.endStartSection("save");
            saveWorld(false, null);
        }

        worldInfo.setWorldTime(l1);
        func_82738_a(getTotalWorldTime() + 1L);
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
            if (!ODNBXlite.oldLightEngine){
                func_48458_a(k, l, chunk);
            }
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
            if(rand.nextInt(4) == 0 && ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.SnowCovered && provider.dimensionId==0)
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

    /**
     * calls calculateCelestialAngle
     */
    public float getCelestialAngle(float par1)
    {
        if(ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_SKY && provider.dimensionId==0){
            return 0.0F;
        }
        if(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INFDEV0227){
            return 1.0F;
        }
        if(ODNBXlite.SkyBrightness == 16 || (ODNBXlite.SkyBrightness == -1 && ODNBXlite.getSkyBrightness(ODNBXlite.MapTheme) == 16)){
            return 1.0F;
        }
        return super.getCelestialAngle(par1);
    }

    /**
     * Returns how bright the block is shown as which is the block's light value looked up in a lookup table (light
     * values aren't linear for brightness). Args: x, y, z
     */
    public float getLightBrightness(int par1, int par2, int par3)
    {
        if (provider.dimensionId == 1){
            return 0.22F + super.getLightBrightness(par1, par2, par3) * 0.75F;
        }
        return super.getLightBrightness(par1, par2, par3);
    }

    public float getBrightness(int par1, int par2, int par3, int par4)
    {
        if (provider.dimensionId == 1){
            return 0.22F + super.getBrightness(par1, par2, par3, par4) * 0.75F;
        }
        return super.getBrightness(par1, par2, par3, par4);
    }
}
