package net.minecraft.src;

import java.util.*;
import net.minecraft.client.Minecraft;

public class WorldClient extends World
{
    /** The packets that need to be sent to the server. */
    private NetClientHandler sendQueue;

    /** The ChunkProviderClient instance */
    private ChunkProviderClient clientChunkProvider;

    /**
     * The hash set of entities handled by this client. Uses the entity's ID as the hash set's key.
     */
    private IntHashMap entityHashSet;

    /** Contains all entities for this client, both spawned and non-spawned. */
    private Set entityList;

    /**
     * Contains all entities for this client that were not spawned due to a non-present chunk. The game will attempt to
     * spawn up to 10 pending entities with each subsequent tick until the spawn queue is empty.
     */
    private Set entitySpawnQueue;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set previousActiveChunkSet = new HashSet();

    public WorldClient(NetClientHandler par1NetClientHandler, WorldSettings par2WorldSettings, int par3, int par4, Profiler par5Profiler)
    {
        super(new SaveHandlerMP(), "MpServer", WorldProvider.getProviderForDimension(par3), par2WorldSettings, par5Profiler);
        entityHashSet = new IntHashMap();
        entityList = new HashSet();
        entitySpawnQueue = new HashSet();
        sendQueue = par1NetClientHandler;
        difficultySetting = par4;
        setSpawnLocation(8, 64, 8);
        mapStorage = par1NetClientHandler.mapStorage;
    }

    public WorldClient(WorldProvider w, ISaveHandler i, WorldSettings s, String str, Profiler p)
    {
        super(i, str, s, w, p);
        sendQueue = new NetClientHandlerSP(Minecraft.getMinecraft());
        mapStorage = new MapStorage(i);
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        super.tick();
        setWorldTime(getWorldTime() + 1L);
        theProfiler.startSection("reEntryProcessing");

        for (int i = 0; i < 10 && !entitySpawnQueue.isEmpty(); i++)
        {
            Entity entity = (Entity)entitySpawnQueue.iterator().next();
            entitySpawnQueue.remove(entity);

            if (!loadedEntityList.contains(entity))
            {
                spawnEntityInWorld(entity);
            }
        }

        theProfiler.endStartSection("connection");
        sendQueue.processReadPackets();
        theProfiler.endStartSection("chunkCache");
        clientChunkProvider.unload100OldestChunks();
        theProfiler.endStartSection("tiles");
        tickBlocksAndAmbiance();
        theProfiler.endSection();
    }

    /**
     * Invalidates an AABB region of blocks from the receive queue, in the event that the block has been modified
     * client-side in the intervening 80 receive ticks.
     */
    public void invalidateBlockReceiveRegion(int i, int j, int k, int l, int i1, int j1)
    {
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected IChunkProvider createChunkProvider()
    {
        clientChunkProvider = new ChunkProviderClient(this);
        return clientChunkProvider;
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    protected void tickBlocksAndAmbiance()
    {
        super.tickBlocksAndAmbiance();
        previousActiveChunkSet.retainAll(activeChunkSet);

        if (previousActiveChunkSet.size() == activeChunkSet.size())
        {
            previousActiveChunkSet.clear();
        }

        int i = 0;

        for (Iterator iterator = activeChunkSet.iterator(); iterator.hasNext();)
        {
            ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator.next();

            if (!previousActiveChunkSet.contains(chunkcoordintpair))
            {
                int j = chunkcoordintpair.chunkXPos * 16;
                int k = chunkcoordintpair.chunkZPos * 16;
                theProfiler.startSection("getChunk");
                Chunk chunk = getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
                moodSoundAndLightCheck(j, k, chunk);
                theProfiler.endSection();
                previousActiveChunkSet.add(chunkcoordintpair);

                if (++i >= 10)
                {
                    return;
                }
            }
        }
    }

    public void doPreChunk(int par1, int par2, boolean par3)
    {
        if (par3)
        {
            clientChunkProvider.loadChunk(par1, par2);
        }
        else
        {
            clientChunkProvider.unloadChunk(par1, par2);
        }

        if (!par3)
        {
            markBlocksDirty(par1 * 16, 0, par2 * 16, par1 * 16 + 15, 256, par2 * 16 + 15);
        }
    }

    /**
     * Called to place all entities as part of a world
     */
    public boolean spawnEntityInWorld(Entity par1Entity)
    {
        boolean flag = super.spawnEntityInWorld(par1Entity);
        entityList.add(par1Entity);

        if (!flag)
        {
            entitySpawnQueue.add(par1Entity);
        }

        return flag;
    }

    /**
     * Dismounts the entity (and anything riding the entity), sets the dead flag, and removes the player entity from the
     * player entity list. Called by the playerLoggedOut function.
     */
    public void setEntityDead(Entity par1Entity)
    {
        super.setEntityDead(par1Entity);
        entityList.remove(par1Entity);
    }

    /**
     * Start the skin for this entity downloading, if necessary, and increment its reference counter
     */
    protected void obtainEntitySkin(Entity par1Entity)
    {
        super.obtainEntitySkin(par1Entity);

        if (entitySpawnQueue.contains(par1Entity))
        {
            entitySpawnQueue.remove(par1Entity);
        }
    }

    /**
     * Decrement the reference counter for this entity's skin image data
     */
    protected void releaseEntitySkin(Entity par1Entity)
    {
        super.releaseEntitySkin(par1Entity);

        if (entityList.contains(par1Entity))
        {
            if (par1Entity.isEntityAlive())
            {
                entitySpawnQueue.add(par1Entity);
            }
            else
            {
                entityList.remove(par1Entity);
            }
        }
    }

    /**
     * Add an ID to Entity mapping to entityHashSet
     */
    public void addEntityToWorld(int par1, Entity par2Entity)
    {
        Entity entity = getEntityByID(par1);

        if (entity != null)
        {
            setEntityDead(entity);
        }

        entityList.add(par2Entity);
        par2Entity.entityId = par1;

        if (!spawnEntityInWorld(par2Entity))
        {
            entitySpawnQueue.add(par2Entity);
        }

        entityHashSet.addKey(par1, par2Entity);
    }

    /**
     * Lookup and return an Entity based on its ID
     */
    public Entity getEntityByID(int par1)
    {
        return (Entity)entityHashSet.lookup(par1);
    }

    public Entity removeEntityFromWorld(int par1)
    {
        Entity entity = (Entity)entityHashSet.removeObject(par1);

        if (entity != null)
        {
            entityList.remove(entity);
            setEntityDead(entity);
        }

        return entity;
    }

    public boolean setBlockAndMetadataAndInvalidate(int par1, int par2, int par3, int par4, int par5)
    {
        invalidateBlockReceiveRegion(par1, par2, par3, par1, par2, par3);
        return super.setBlockAndMetadataWithNotify(par1, par2, par3, par4, par5);
    }

    /**
     * If on MP, sends a quitting packet.
     */
    public void sendQuittingDisconnectingPacket()
    {
        sendQueue.quitWithPacket(new Packet255KickDisconnect("Quitting"));
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        if (provider.hasNoSky)
        {
            return;
        }

        if (lastLightningBolt > 0)
        {
            lastLightningBolt--;
        }

        prevRainingStrength = rainingStrength;

        if (worldInfo.isRaining())
        {
            rainingStrength += 0.01D;
        }
        else
        {
            rainingStrength -= 0.01D;
        }

        if (rainingStrength < 0.0F)
        {
            rainingStrength = 0.0F;
        }

        if (rainingStrength > 1.0F)
        {
            rainingStrength = 1.0F;
        }

        prevThunderingStrength = thunderingStrength;

        if (worldInfo.isThundering())
        {
            thunderingStrength += 0.01D;
        }
        else
        {
            thunderingStrength -= 0.01D;
        }

        if (thunderingStrength < 0.0F)
        {
            thunderingStrength = 0.0F;
        }

        if (thunderingStrength > 1.0F)
        {
            thunderingStrength = 1.0F;
        }
    }

    public void func_73029_E(int par1, int par2, int par3)
    {
        byte byte0 = 16;
        Random random = new Random();

        for (int i = 0; i < 1000; i++)
        {
            int j = (par1 + rand.nextInt(byte0)) - rand.nextInt(byte0);
            int k = (par2 + rand.nextInt(byte0)) - rand.nextInt(byte0);
            int l = (par3 + rand.nextInt(byte0)) - rand.nextInt(byte0);
            int i1 = getBlockId(j, k, l);

            if (i1 == 0 && rand.nextInt(8) > k && provider.getWorldHasVoidParticles())
            {
                spawnParticle("depthsuspend", (float)j + rand.nextFloat(), (float)k + rand.nextFloat(), (float)l + rand.nextFloat(), 0.0D, 0.0D, 0.0D);
                continue;
            }

            if (i1 > 0)
            {
                Block.blocksList[i1].randomDisplayTick(this, j, k, l, random);
            }
        }
    }

    /**
     * also releases skins.
     */
    public void removeAllEntities()
    {
        loadedEntityList.removeAll(unloadedEntityList);

        for (int i = 0; i < unloadedEntityList.size(); i++)
        {
            Entity entity = (Entity)unloadedEntityList.get(i);
            int l = entity.chunkCoordX;
            int j1 = entity.chunkCoordZ;

            if (entity.addedToChunk && chunkExists(l, j1))
            {
                getChunkFromChunkCoords(l, j1).removeEntity(entity);
            }
        }

        for (int j = 0; j < unloadedEntityList.size(); j++)
        {
            releaseEntitySkin((Entity)unloadedEntityList.get(j));
        }

        unloadedEntityList.clear();

        for (int k = 0; k < loadedEntityList.size(); k++)
        {
            Entity entity1 = (Entity)loadedEntityList.get(k);

            if (entity1.ridingEntity != null)
            {
                if (!entity1.ridingEntity.isDead && entity1.ridingEntity.riddenByEntity == entity1)
                {
                    continue;
                }

                entity1.ridingEntity.riddenByEntity = null;
                entity1.ridingEntity = null;
            }

            if (!entity1.isDead)
            {
                continue;
            }

            int i1 = entity1.chunkCoordX;
            int k1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && chunkExists(i1, k1))
            {
                getChunkFromChunkCoords(i1, k1).removeEntity(entity1);
            }

            loadedEntityList.remove(k--);
            releaseEntitySkin(entity1);
        }
    }

    /**
     * Adds some basic stats of the world to the given crash report.
     */
    public CrashReport addWorldInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addWorldInfoToCrashReport(par1CrashReport);
        par1CrashReport.addCrashSectionCallable("Forced Entities", new CallableMPL1(this));
        par1CrashReport.addCrashSectionCallable("Retry Entities", new CallableMPL2(this));
        return par1CrashReport;
    }

    /**
     * par8 is loudness, all pars passed to minecraftInstance.sndManager.playSound
     */
    public void playSound(double par1, double par3, double par5, String par7Str, float par8, float par9)
    {
        float f = 16F;

        if (par8 > 1.0F)
        {
            f *= par8;
        }

        if (mc.renderViewEntity.getDistanceSq(par1, par3, par5) < (double)(f * f))
        {
            mc.sndManager.playSound(par7Str, (float)par1, (float)par3, (float)par5, par8, par9);
        }
    }

    static Set getEntityList(WorldClient par0WorldClient)
    {
        return par0WorldClient.entityList;
    }

    static Set getEntitySpawnQueue(WorldClient par0WorldClient)
    {
        return par0WorldClient.entitySpawnQueue;
    }
}
