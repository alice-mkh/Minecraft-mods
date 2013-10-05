package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.nbxlite.spawners.*;

public class WorldServer extends World
{
    private final MinecraftServer mcServer;
    private final EntityTracker theEntityTracker;
    private final PlayerManager thePlayerManager;
    private Set pendingTickListEntriesHashSet;

    /** All work to do in future ticks. */
    private TreeSet pendingTickListEntriesTreeSet;
    public ChunkProviderServer theChunkProviderServer;

    /** set by CommandServerSave{all,Off,On} */
    public boolean canNotSave;

    /** is false if there are no players */
    private boolean allPlayersSleeping;
    private int updateEntityTick;

    /**
     * the teleporter to use when the entity is being transferred into the dimension
     */
    private final Teleporter worldTeleporter = new Teleporter(this);
    private final SpawnerAnimals animalSpawner = new SpawnerAnimals();
    private ServerBlockEventList blockEventCache[] =
    {
        new ServerBlockEventList(null), new ServerBlockEventList(null)
    };

    /**
     * The index into the blockEventCache; either 0, or 1, toggled in sendBlockEventPackets  where all BlockEvent are
     * applied locally and send to clients.
     */
    private int blockEventCacheIndex;
    private static final WeightedRandomChestContent bonusChestContent[];
    private List pendingTickListEntriesThisTick;

    /** An IntHashMap of entity IDs (integers) to their Entity objects. */
    private IntHashMap entityIdMap;

    protected OldSpawnerAnimals animalSpawner2;
    protected OldSpawnerMonsters monsterSpawner;
    protected OldSpawnerAnimals waterMobSpawner;
    protected OldSpawnerAnimals ambientMobSpawner;

    public WorldServer(MinecraftServer par1MinecraftServer, ISaveHandler par2ISaveHandler, String par3Str, int par4, WorldSettings par5WorldSettings, Profiler par6Profiler, ILogAgent par7ILogAgent)
    {
        super(par2ISaveHandler, par3Str, par5WorldSettings, WorldProvider.getProviderForDimension(par4), par6Profiler, par7ILogAgent);
        pendingTickListEntriesThisTick = new ArrayList();
        mcServer = par1MinecraftServer;
        theEntityTracker = new EntityTracker(this);
        thePlayerManager = new PlayerManager(this, par1MinecraftServer.getConfigurationManager().getViewDistance());

        if (entityIdMap == null)
        {
            entityIdMap = new IntHashMap();
        }

        if (pendingTickListEntriesHashSet == null)
        {
            pendingTickListEntriesHashSet = new HashSet();
        }

        if (pendingTickListEntriesTreeSet == null)
        {
            pendingTickListEntriesTreeSet = new TreeSet();
        }

        worldScoreboard = new ServerScoreboard(par1MinecraftServer);
        ScoreboardSaveData scoreboardsavedata = (ScoreboardSaveData)mapStorage.loadData(net.minecraft.src.ScoreboardSaveData.class, "scoreboard");

        if (scoreboardsavedata == null)
        {
            scoreboardsavedata = new ScoreboardSaveData();
            mapStorage.setData("scoreboard", scoreboardsavedata);
        }

        scoreboardsavedata.func_96499_a(worldScoreboard);
        ((ServerScoreboard)worldScoreboard).func_96547_a(scoreboardsavedata);

        turnOnOldSpawners();
        ODNBXlite.IndevWorld = null;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();

        if (getWorldInfo().isHardcoreModeEnabled() && difficultySetting < 3)
        {
            difficultySetting = 3;
        }

        provider.worldChunkMgr.cleanupCache();

        if (areAllPlayersAsleep())
        {
            if (getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
            {
                long l = worldInfo.getWorldTime() + 24000L;
                worldInfo.setWorldTime(l - l % 24000L);
            }

            wakeAllPlayers();
        }

        theProfiler.startSection("mobSpawner");

        if (getGameRules().getGameRuleBooleanValue("doMobSpawning"))
        {
            if (provider.dimensionId!=1){
                    if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES || !ODNBXlite.OldSpawning){
                    animalSpawner.findChunksForSpawning(this, spawnHostileMobs, spawnPeacefulMobs, worldInfo.getWorldTotalTime() % 400L == 0L);
                } else if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES || provider.dimensionId!=0){
                    SpawnerAnimalsBeta.performSpawning(this, spawnHostileMobs, spawnPeacefulMobs);
                } else if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
                    if (spawnPeacefulMobs){
                        animalSpawner2.func_1150_a(this);
                        waterMobSpawner.func_1150_a(this);
                        ambientMobSpawner.func_1150_a(this);
                    }
                    if (spawnHostileMobs){
                        monsterSpawner.func_1150_a(this);
                    }
                }
            }else{
                animalSpawner.findChunksForSpawning(this, spawnHostileMobs, spawnPeacefulMobs, true);
            }
        }

        theProfiler.endStartSection("chunkSource");
        chunkProvider.unloadQueuedChunks();
        int i = calculateSkylightSubtracted(1.0F);

        if (i != skylightSubtracted)
        {
            skylightSubtracted = i;
        }

        worldInfo.incrementTotalWorldTime(worldInfo.getWorldTotalTime() + 1L);

        if (getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
        {
            worldInfo.setWorldTime(worldInfo.getWorldTime() + 1L);
        }

        theProfiler.endStartSection("tickPending");
        tickUpdates(false);
        theProfiler.endStartSection("tickTiles");
        tickBlocksAndAmbiance();
        theProfiler.endStartSection("chunkMap");
        thePlayerManager.updatePlayerInstances();
        theProfiler.endStartSection("village");
        villageCollectionObj.tick();
        villageSiegeObj.tick();
        theProfiler.endStartSection("portalForcer");
        worldTeleporter.removeStalePortalLocations(getTotalWorldTime());
        theProfiler.endSection();
        sendAndApplyBlockEvents();
    }

    /**
     * only spawns creatures allowed by the chunkProvider
     */
    public SpawnListEntry spawnRandomCreature(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        List list = getChunkProvider().getPossibleCreatures(par1EnumCreatureType, par2, par3, par4);

        if (list == null || list.isEmpty())
        {
            return null;
        }
        else
        {
            return (SpawnListEntry)WeightedRandom.getRandomItem(rand, list);
        }
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
        allPlayersSleeping = !playerEntities.isEmpty();
        Iterator iterator = playerEntities.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

            if (entityplayer.isPlayerSleeping())
            {
                continue;
            }

            allPlayersSleeping = false;
            break;
        }
        while (true);
    }

    protected void wakeAllPlayers()
    {
        allPlayersSleeping = false;
        Iterator iterator = playerEntities.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

            if (entityplayer.isPlayerSleeping())
            {
                entityplayer.wakeUpPlayer(false, false, true);
            }
        }
        while (true);

        resetRainAndThunder();
    }

    private void resetRainAndThunder()
    {
        worldInfo.setRainTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThunderTime(0);
        worldInfo.setThundering(false);
    }

    public boolean areAllPlayersAsleep()
    {
        if (allPlayersSleeping && !isRemote)
        {
            for (Iterator iterator = playerEntities.iterator(); iterator.hasNext();)
            {
                EntityPlayer entityplayer = (EntityPlayer)iterator.next();

                if (!entityplayer.isPlayerFullyAsleep())
                {
                    return false;
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setSpawnLocation()
    {
        if (worldInfo.getSpawnY() <= 0)
        {
            worldInfo.setSpawnY(64);
        }

        int i = worldInfo.getSpawnX();
        int j = worldInfo.getSpawnZ();
        int k = 0;

        do
        {
            if (getFirstUncoveredBlock(i, j) != 0)
            {
                break;
            }

            i += rand.nextInt(8) - rand.nextInt(8);
            j += rand.nextInt(8) - rand.nextInt(8);
        }
        while (++k != 10000);

        worldInfo.setSpawnX(i);
        worldInfo.setSpawnZ(j);
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    protected void tickBlocksAndAmbiance()
    {
        super.tickBlocksAndAmbiance();
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
                moodSoundAndLightCheck(k, l, chunk);
            }
            theProfiler.endStartSection("tickChunk");
            if (!ODNBXlite.oldLightEngine){
                chunk.updateSkylight();
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
                }
            }

            theProfiler.endStartSection("iceandsnow");
            if(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.SnowCovered && provider.dimensionId==0 && rand.nextInt(4) == 0){
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
                        setBlock(l3 + k, l5, l4 + l, Block.snow.blockID);
                    }
                    if((k6 == Block.waterMoving.blockID || k6 == Block.waterStill.blockID) && chunk.getBlockMetadata(l3, l5 - 1, l4) == 0)
                    {
                        setBlock(l3 + k, l5 - 1, l4 + l, Block.ice.blockID);
                    }
                }
            }else if (ODNBXlite.getFlag("weather") && rand.nextInt(16) == 0){
                updateLCG = updateLCG * 3 + 0x3c6ef35f;
                int l7 = updateLCG >> 2;
                int l8 = l7 & 0xf;
                int l9 = l7 >> 8 & 0xf;
                int l10 = getPrecipitationHeight(l8 + k, l9 + l);
                if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES){
                    if (isBlockFreezableNaturally(l8 + k, l10 - 1, l9 + l))
                    {
                        setBlock(l8 + k, l10 - 1, l9 + l, Block.ice.blockID);
                    }

                    if (isRaining() && canSnowAt(l8 + k, l10, l9 + l))
                    {
                        setBlock(l8 + k, l10, l9 + l, Block.snow.blockID);
                    }

                    if (isRaining())
                    {
                        BiomeGenBase biomegenbase = getBiomeGenForCoords(l8 + k, l9 + l);

                        if (biomegenbase.canSpawnLightningBolt())
                        {
                            int l3 = getBlockId(l8 + k, l10 - 1, l9 + l);

                            if (l3 != 0)
                            {
                                Block.blocksList[l3].fillWithRain(this, l8 + k, l10 - 1, l9 + l);
                            }
                        }
                    }
                }else{
                    if(getWorldChunkManager().oldGetBiomeGenAt(l8 + k, l9 + l).getEnableSnow() && l10 >= 0 && l10 < 128 && chunk.getSavedLightValue(EnumSkyBlock.Block, l8, l10, l9) < 10)
                    {
                        int i66 = chunk.getBlockID(l8, l10 - 1, l9);
                        int k66 = chunk.getBlockID(l8, l10, l9);
                        if(isRaining() && k66 == 0 && Block.snow.canPlaceBlockAt(this, l8 + k, l10, l9 + l) && i66 != 0 && i66 != Block.ice.blockID && Block.blocksList[i66].blockMaterial.isSolid())
                        {
                            setBlock(l8 + k, l10, l9 + l, Block.snow.blockID);
                        }
                        if(i66 == Block.waterStill.blockID && chunk.getBlockMetadata(l8, l10 - 1, l9) == 0)
                        {
                            setBlock(l8 + k, l10 - 1, l9 + l, Block.ice.blockID);
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
                    int i4 = updateLCG >> 2;
                    int j4 = i4 & 0xf;
                    int k4 = i4 >> 8 & 0xf;
                    int l4 = i4 >> 16 & 0xf;
                    int i5 = extendedblockstorage.getExtBlockID(j4, l4, k4);
                    j++;
                    Block block = Block.blocksList[i5];

                    if (block != null && block.getTickRandomly())
                    {
                        i++;
                        block.updateTick(this, j4 + k, l4 + extendedblockstorage.getYLocation(), k4 + l, rand);
                    }
                }
            }
        }
    }

    /**
     * Returns true if the given block will receive a scheduled tick in this tick. Args: X, Y, Z, blockID
     */
    public boolean isBlockTickScheduledThisTick(int par1, int par2, int par3, int par4)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        return pendingTickListEntriesThisTick.contains(nextticklistentry);
    }

    /**
     * Schedules a tick to a block with a delay (Most commonly the tick rate)
     */
    public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5)
    {
        scheduleBlockUpdateWithPriority(par1, par2, par3, par4, par5, 0);
    }

    public void scheduleBlockUpdateWithPriority(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        byte byte0 = 0;

        if (scheduledUpdatesAreImmediate && par4 > 0)
        {
            if (Block.blocksList[par4].func_82506_l())
            {
                byte0 = 8;

                if (checkChunksExist(nextticklistentry.xCoord - byte0, nextticklistentry.yCoord - byte0, nextticklistentry.zCoord - byte0, nextticklistentry.xCoord + byte0, nextticklistentry.yCoord + byte0, nextticklistentry.zCoord + byte0))
                {
                    int i = getBlockId(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);

                    if (i == nextticklistentry.blockID && i > 0)
                    {
                        Block.blocksList[i].updateTick(this, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, rand);
                    }
                }

                return;
            }

            par5 = 1;
        }

        if (checkChunksExist(par1 - byte0, par2 - byte0, par3 - byte0, par1 + byte0, par2 + byte0, par3 + byte0))
        {
            if (par4 > 0)
            {
                nextticklistentry.setScheduledTime((long)par5 + worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(par6);
            }

            if (!pendingTickListEntriesHashSet.contains(nextticklistentry))
            {
                pendingTickListEntriesHashSet.add(nextticklistentry);
                pendingTickListEntriesTreeSet.add(nextticklistentry);
            }
        }
    }

    /**
     * Schedules a block update from the saved information in a chunk. Called when the chunk is loaded.
     */
    public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        nextticklistentry.setPriority(par6);

        if (par4 > 0)
        {
            nextticklistentry.setScheduledTime((long)par5 + worldInfo.getWorldTotalTime());
        }

        if (!pendingTickListEntriesHashSet.contains(nextticklistentry))
        {
            pendingTickListEntriesHashSet.add(nextticklistentry);
            pendingTickListEntriesTreeSet.add(nextticklistentry);
        }
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    public void updateEntities()
    {
        if (playerEntities.isEmpty())
        {
            if (updateEntityTick++ >= 1200)
            {
                return;
            }
        }
        else
        {
            resetUpdateEntityTick();
        }

        super.updateEntities();
    }

    /**
     * Resets the updateEntityTick field to 0
     */
    public void resetUpdateEntityTick()
    {
        updateEntityTick = 0;
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    public boolean tickUpdates(boolean par1)
    {
        int i = pendingTickListEntriesTreeSet.size();

        if (i != pendingTickListEntriesHashSet.size())
        {
            throw new IllegalStateException("TickNextTick list out of synch");
        }

        if (i > 1000)
        {
            i = 1000;
        }

        theProfiler.startSection("cleaning");
        int j = 0;

        do
        {
            if (j >= i)
            {
                break;
            }

            NextTickListEntry nextticklistentry = (NextTickListEntry)pendingTickListEntriesTreeSet.first();

            if (!par1 && nextticklistentry.scheduledTime > worldInfo.getWorldTotalTime())
            {
                break;
            }

            pendingTickListEntriesTreeSet.remove(nextticklistentry);
            pendingTickListEntriesHashSet.remove(nextticklistentry);
            pendingTickListEntriesThisTick.add(nextticklistentry);
            j++;
        }
        while (true);

        theProfiler.endSection();
        theProfiler.startSection("ticking");
        Iterator iterator = pendingTickListEntriesThisTick.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            NextTickListEntry nextticklistentry1 = (NextTickListEntry)iterator.next();
            iterator.remove();
            int k = 0;

            if (checkChunksExist(nextticklistentry1.xCoord - k, nextticklistentry1.yCoord - k, nextticklistentry1.zCoord - k, nextticklistentry1.xCoord + k, nextticklistentry1.yCoord + k, nextticklistentry1.zCoord + k))
            {
                int l = getBlockId(nextticklistentry1.xCoord, nextticklistentry1.yCoord, nextticklistentry1.zCoord);

                if (l > 0 && Block.isAssociatedBlockID(l, nextticklistentry1.blockID))
                {
                    try
                    {
                        Block.blocksList[l].updateTick(this, nextticklistentry1.xCoord, nextticklistentry1.yCoord, nextticklistentry1.zCoord, rand);
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while ticking a block");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being ticked");
                        int i1;

                        try
                        {
                            i1 = getBlockMetadata(nextticklistentry1.xCoord, nextticklistentry1.yCoord, nextticklistentry1.zCoord);
                        }
                        catch (Throwable throwable1)
                        {
                            i1 = -1;
                        }

                        CrashReportCategory.addBlockCrashInfo(crashreportcategory, nextticklistentry1.xCoord, nextticklistentry1.yCoord, nextticklistentry1.zCoord, l, i1);
                        throw new ReportedException(crashreport);
                    }
                }
            }
            else
            {
                scheduleBlockUpdate(nextticklistentry1.xCoord, nextticklistentry1.yCoord, nextticklistentry1.zCoord, nextticklistentry1.blockID, 0);
            }
        }
        while (true);

        theProfiler.endSection();
        pendingTickListEntriesThisTick.clear();
        return !pendingTickListEntriesTreeSet.isEmpty();
    }

    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
    {
        ArrayList arraylist = null;
        ChunkCoordIntPair chunkcoordintpair = par1Chunk.getChunkCoordIntPair();
        int i = (chunkcoordintpair.chunkXPos << 4) - 2;
        int j = i + 16 + 2;
        int k = (chunkcoordintpair.chunkZPos << 4) - 2;
        int l = k + 16 + 2;
        label0:

        for (int i1 = 0; i1 < 2; i1++)
        {
            Iterator iterator;

            if (i1 == 0)
            {
                iterator = pendingTickListEntriesTreeSet.iterator();
            }
            else
            {
                iterator = pendingTickListEntriesThisTick.iterator();

                if (!pendingTickListEntriesThisTick.isEmpty())
                {
                    System.out.println(pendingTickListEntriesThisTick.size());
                }
            }

            do
            {
                if (!iterator.hasNext())
                {
                    continue label0;
                }

                NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();

                if (nextticklistentry.xCoord >= i && nextticklistentry.xCoord < j && nextticklistentry.zCoord >= k && nextticklistentry.zCoord < l)
                {
                    if (par2)
                    {
                        pendingTickListEntriesHashSet.remove(nextticklistentry);
                        iterator.remove();
                    }

                    if (arraylist == null)
                    {
                        arraylist = new ArrayList();
                    }

                    arraylist.add(nextticklistentry);
                }
            }
            while (true);
        }

        return arraylist;
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     * Args: entity, forceUpdate
     */
    public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2)
    {
        if (!mcServer.getCanSpawnAnimals() && ((par1Entity instanceof EntityAnimal) || (par1Entity instanceof EntityWaterMob)))
        {
            par1Entity.setDead();
        }

        if (!mcServer.getCanSpawnNPCs() && (par1Entity instanceof INpc))
        {
            par1Entity.setDead();
        }

        super.updateEntityWithOptionalForce(par1Entity, par2);
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected IChunkProvider createChunkProvider()
    {
        IChunkLoader ichunkloader = saveHandler.getChunkLoader(provider);
        theChunkProviderServer = new ChunkProviderServer(this, ichunkloader, provider.createChunkGenerator());
        return theChunkProviderServer;
    }

    /**
     * pars: min x,y,z , max x,y,z
     */
    public List getAllTileEntityInBox(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        ArrayList arraylist = new ArrayList();

        for (int i = 0; i < loadedTileEntityList.size(); i++)
        {
            TileEntity tileentity = (TileEntity)loadedTileEntityList.get(i);

            if (tileentity.xCoord >= par1 && tileentity.yCoord >= par2 && tileentity.zCoord >= par3 && tileentity.xCoord < par4 && tileentity.yCoord < par5 && tileentity.zCoord < par6)
            {
                arraylist.add(tileentity);
            }
        }

        return arraylist;
    }

    /**
     * Called when checking if a certain block can be mined or not. The 'spawn safe zone' check is located here.
     */
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
    {
        return !mcServer.isBlockProtected(this, par2, par3, par4, par1EntityPlayer);
    }

    protected void initialize(WorldSettings par1WorldSettings)
    {
        if (entityIdMap == null)
        {
            entityIdMap = new IntHashMap();
        }

        if (pendingTickListEntriesHashSet == null)
        {
            pendingTickListEntriesHashSet = new HashSet();
        }

        if (pendingTickListEntriesTreeSet == null)
        {
            pendingTickListEntriesTreeSet = new TreeSet();
        }

        createSpawnPosition(par1WorldSettings);
        super.initialize(par1WorldSettings);
    }

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    protected void createSpawnPosition(WorldSettings par1WorldSettings)
    {
        if (!provider.canRespawnHere())
        {
            worldInfo.setSpawnPosition(0, provider.getAverageGroundLevel(), 0);
            return;
        }

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
                findingSpawnPoint = true;
                WorldChunkManager worldchunkmanager = provider.worldChunkMgr;
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
                setBlock(ODNBXlite.IndevSpawnX-2, ODNBXlite.IndevSpawnY+3, ODNBXlite.IndevSpawnZ, Block.torchWood.blockID);
                setBlock(ODNBXlite.IndevSpawnX+2, ODNBXlite.IndevSpawnY+3, ODNBXlite.IndevSpawnZ, Block.torchWood.blockID);
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

        if (par1WorldSettings.isBonusChestEnabled())
        {
            createBonusChest();
        }
    }

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
            setBlock(j, l, k, Block.chest.blockID);
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

    /**
     * Gets the hard-coded portal location to use when entering this dimension.
     */
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return provider.getEntrancePortalLocation();
    }

    /**
     * Saves all chunks to disk while updating progress bar.
     */
    public void saveAllChunks(boolean par1, IProgressUpdate par2IProgressUpdate) throws MinecraftException
    {
        if (!chunkProvider.canSave())
        {
            return;
        }

        if (par2IProgressUpdate != null)
        {
            par2IProgressUpdate.displayProgressMessage("Saving level");
        }

        saveLevel();

        if (par2IProgressUpdate != null)
        {
            par2IProgressUpdate.resetProgresAndWorkingMessage("Saving chunks");
        }

        chunkProvider.saveChunks(par1, par2IProgressUpdate);
    }

    /**
     * saves chunk data - currently only called during execution of the Save All command
     */
    public void saveChunkData()
    {
        if (!chunkProvider.canSave())
        {
            return;
        }
        else
        {
            chunkProvider.saveExtraData();
            return;
        }
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
        checkSessionLock();
        saveHandler.saveWorldInfoWithPlayer(worldInfo, mcServer.getConfigurationManager().getHostPlayerData());
        mapStorage.saveAllData();
    }

    protected void onEntityAdded(Entity par1Entity)
    {
        super.onEntityAdded(par1Entity);
        entityIdMap.addKey(par1Entity.entityId, par1Entity);
        Entity aentity[] = par1Entity.getParts();

        if (aentity != null)
        {
            for (int i = 0; i < aentity.length; i++)
            {
                entityIdMap.addKey(aentity[i].entityId, aentity[i]);
            }
        }
    }

    protected void onEntityRemoved(Entity par1Entity)
    {
        super.onEntityRemoved(par1Entity);
        entityIdMap.removeObject(par1Entity.entityId);
        Entity aentity[] = par1Entity.getParts();

        if (aentity != null)
        {
            for (int i = 0; i < aentity.length; i++)
            {
                entityIdMap.removeObject(aentity[i].entityId);
            }
        }
    }

    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    public Entity getEntityByID(int par1)
    {
        return (Entity)entityIdMap.lookup(par1);
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    public boolean addWeatherEffect(Entity par1Entity)
    {
        if (super.addWeatherEffect(par1Entity))
        {
            mcServer.getConfigurationManager().sendToAllNear(par1Entity.posX, par1Entity.posY, par1Entity.posZ, 512D, provider.dimensionId, new Packet71Weather(par1Entity));
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity par1Entity, byte par2)
    {
        Packet38EntityStatus packet38entitystatus = new Packet38EntityStatus(par1Entity.entityId, par2);
        getEntityTracker().sendPacketToAllAssociatedPlayers(par1Entity, packet38entitystatus);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
    {
        Explosion explosion = new Explosion(this, par1Entity, par2, par4, par6, par8);
        explosion.isFlaming = par9;
        explosion.isSmoking = par10;
        explosion.doExplosionA();
        explosion.doExplosionB(false);

        if (!par10)
        {
            explosion.affectedBlockPositions.clear();
        }

        Iterator iterator = playerEntities.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            EntityPlayer entityplayer = (EntityPlayer)iterator.next();

            if (entityplayer.getDistanceSq(par2, par4, par6) < 4096D)
            {
                ((EntityPlayerMP)entityplayer).playerNetServerHandler.sendPacketToPlayer(new Packet60Explosion(par2, par4, par6, par8, explosion.affectedBlockPositions, (Vec3)explosion.func_77277_b().get(entityplayer)));
            }
        }
        while (true);

        return explosion;
    }

    /**
     * Adds a block event with the given Args to the blockEventCache. During the next tick(), the block specified will
     * have its onBlockEvent handler called with the given parameters. Args: X,Y,Z, BlockID, EventID, EventParameter
     */
    public void addBlockEvent(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        BlockEventData blockeventdata = new BlockEventData(par1, par2, par3, par4, par5, par6);

        for (Iterator iterator = blockEventCache[blockEventCacheIndex].iterator(); iterator.hasNext();)
        {
            BlockEventData blockeventdata1 = (BlockEventData)iterator.next();

            if (blockeventdata1.equals(blockeventdata))
            {
                return;
            }
        }

        blockEventCache[blockEventCacheIndex].add(blockeventdata);
    }

    /**
     * Send and apply locally all pending BlockEvents to each player with 64m radius of the event.
     */
    private void sendAndApplyBlockEvents()
    {
        int i;
        label0:

        for (; !blockEventCache[blockEventCacheIndex].isEmpty(); blockEventCache[i].clear())
        {
            i = blockEventCacheIndex;
            blockEventCacheIndex ^= 1;
            Iterator iterator = blockEventCache[i].iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    continue label0;
                }

                BlockEventData blockeventdata = (BlockEventData)iterator.next();

                if (onBlockEventReceived(blockeventdata))
                {
                    mcServer.getConfigurationManager().sendToAllNear(blockeventdata.getX(), blockeventdata.getY(), blockeventdata.getZ(), 64D, provider.dimensionId, new Packet54PlayNoteBlock(blockeventdata.getX(), blockeventdata.getY(), blockeventdata.getZ(), blockeventdata.getBlockID(), blockeventdata.getEventID(), blockeventdata.getEventParameter()));
                }
            }
            while (true);
        }
    }

    /**
     * Called to apply a pending BlockEvent to apply to the current world.
     */
    private boolean onBlockEventReceived(BlockEventData par1BlockEventData)
    {
        int i = getBlockId(par1BlockEventData.getX(), par1BlockEventData.getY(), par1BlockEventData.getZ());

        if (i == par1BlockEventData.getBlockID())
        {
            return Block.blocksList[i].onBlockEventReceived(this, par1BlockEventData.getX(), par1BlockEventData.getY(), par1BlockEventData.getZ(), par1BlockEventData.getEventID(), par1BlockEventData.getEventParameter());
        }
        else
        {
            return false;
        }
    }

    /**
     * Syncs all changes to disk and wait for completion.
     */
    public void flush()
    {
        saveHandler.flush();
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        boolean flag = isRaining();
        super.updateWeather();

        if (flag != isRaining())
        {
            if (flag)
            {
                mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(2, 0));
            }
            else
            {
                mcServer.getConfigurationManager().sendPacketToAllPlayers(new Packet70GameEvent(1, 0));
            }
        }
    }

    /**
     * Gets the MinecraftServer.
     */
    public MinecraftServer getMinecraftServer()
    {
        return mcServer;
    }

    /**
     * Gets the EntityTracker
     */
    public EntityTracker getEntityTracker()
    {
        return theEntityTracker;
    }

    public PlayerManager getPlayerManager()
    {
        return thePlayerManager;
    }

    public Teleporter getDefaultTeleporter()
    {
        return worldTeleporter;
    }

    public void turnOnOldSpawners()
    {
        animalSpawner2 = new OldSpawnerAnimals(15, EnumCreatureType.creature);
        monsterSpawner = new OldSpawnerMonsters(200, EnumCreatureType.monster);
        waterMobSpawner = new OldSpawnerAnimals(5, EnumCreatureType.waterCreature);
        ambientMobSpawner = new OldSpawnerAnimals(15, EnumCreatureType.ambient);
    }

    static
    {
        bonusChestContent = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.stick.itemID, 0, 1, 3, 10), new WeightedRandomChestContent(Block.planks.blockID, 0, 1, 3, 10), new WeightedRandomChestContent(Block.wood.blockID, 0, 1, 3, 10), new WeightedRandomChestContent(Item.axeStone.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.axeWood.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.pickaxeStone.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.pickaxeWood.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.appleRed.itemID, 0, 2, 3, 5), new WeightedRandomChestContent(Item.bread.itemID, 0, 2, 3, 3)
                });
    }
}
