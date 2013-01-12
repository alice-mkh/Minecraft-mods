package net.minecraft.src;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.server.MinecraftServer;

public class FakeWorldServer extends WorldServer
{
    private WorldSSP normalWorld;

    public FakeWorldServer(FakeServer par1MinecraftServer, WorldSSP world, WorldSettings par5WorldSettings)
    {
        super(par1MinecraftServer, world.getSaveHandler(), world.getWorldInfo().getWorldName(), world.provider.dimensionId, par5WorldSettings, world.theProfiler);
        normalWorld = world;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        return;
    }

    /**
     * only spawns creatures allowed by the chunkProvider
     */
    public SpawnListEntry spawnRandomCreature(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
    }

    protected void wakeAllPlayers()
    {
    }

    public boolean areAllPlayersAsleep()
    {
        return false;
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setSpawnLocation()
    {
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    protected void tickBlocksAndAmbiance()
    {
    }

    /**
     * Schedules a tick to a block with a delay (Most commonly the tick rate)
     */
    public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5)
    {
    }

    public void func_82740_a(int par1, int par2, int par3, int par4, int par5, int par6)
    {
    }

    /**
     * Schedules a block update from the saved information in a chunk. Called when the chunk is loaded.
     */
    public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5)
    {
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    public void updateEntities()
    {
    }

    public void func_82742_i()
    {
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    public boolean tickUpdates(boolean par1)
    {
        return false;
    }

    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
    {
        return null;
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     * Args: entity, forceUpdate
     */
    public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2)
    {
    }

    /**
     * direct call to super.updateEntityWithOptionalForce
     */
    public void uncheckedUpdateEntity(Entity par1Entity, boolean par2)
    {
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected IChunkProvider createChunkProvider()
    {
         return null;
    }

    /**
     * pars: min x,y,z , max x,y,z
     */
    public List getAllTileEntityInBox(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        ArrayList arraylist = new ArrayList();

        return arraylist;
    }

    /**
     * Called when checking if a certain block can be mined or not. The 'spawn safe zone' check is located here.
     */
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
    {
        return normalWorld.canMineBlock(par1EntityPlayer, par2, par3, par4);
    }

    protected void initialize(WorldSettings par1WorldSettings)
    {
    }

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    protected void createSpawnPosition(WorldSettings par1WorldSettings)
    {
    }

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
    }

    /**
     * Gets the hard-coded portal location to use when entering this dimension.
     */
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return normalWorld.getEntrancePortalLocation();
    }

    /**
     * Saves all chunks to disk while updating progress bar.
     */
    public void saveAllChunks(boolean par1, IProgressUpdate par2IProgressUpdate) throws MinecraftException
    {
    }

    /**
     * Saves the chunks to disk.
     */
    protected void saveLevel() throws MinecraftException
    {
    }

    /**
     * Start the skin for this entity downloading, if necessary, and increment its reference counter
     */
    protected void obtainEntitySkin(Entity par1Entity)
    {
    }

    /**
     * Decrement the reference counter for this entity's skin image data
     */
    protected void releaseEntitySkin(Entity par1Entity)
    {
    }

    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    public Entity getEntityByID(int par1)
    {
        return normalWorld.getEntityByID(par1);
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    public boolean addWeatherEffect(Entity par1Entity)
    {
        return false;
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity par1Entity, byte par2)
    {
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
    {
        return null;
    }

    /**
     * Adds a block event with the given Args to the blockEventCache. During the next tick(), the block specified will
     * have its onBlockEvent handler called with the given parameters. Args: X,Y,Z, BlockID, EventID, EventParameter
     */
    public void addBlockEvent(int par1, int par2, int par3, int par4, int par5, int par6)
    {
    }

    /**
     * Syncs all changes to disk and wait for completion.
     */
    public void flush()
    {
    }

    /**
     * Updates all weather states.
     */
    protected void updateWeather()
    {
    }

    /**
     * Gets the EntityTracker
     */
    public EntityTracker getEntityTracker()
    {
        return null;
    }

    public PlayerManager getPlayerManager()
    {
        return null;
    }

    public Teleporter func_85176_s()
    {
        return null;
    }

    public void turnOnOldSpawners()
    {
    }
}
