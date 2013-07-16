package net.minecraft.src.ssp;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.Explosion;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.PlayerManager;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.Teleporter;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldSettings;

public class FakeWorldServer extends WorldServer
{
    private WorldSSP normalWorld;

    public FakeWorldServer(FakeServer par1MinecraftServer, WorldSSP world, WorldSettings par5WorldSettings)
    {
        super(par1MinecraftServer, world.getSaveHandler(), world.getWorldInfo().getWorldName(), world.provider.dimensionId, par5WorldSettings, world.theProfiler, par1MinecraftServer.getLogAgent());
        normalWorld = world;
    }

    /**
     * Runs a single tick for the world
     */
    @Override
    public void tick()
    {
        return;
    }

    /**
     * only spawns creatures allowed by the chunkProvider
     */
    @Override
    public SpawnListEntry spawnRandomCreature(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        return null;
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    @Override
    public void updateAllPlayersSleepingFlag()
    {
    }

    @Override
    protected void wakeAllPlayers()
    {
    }

    @Override
    public boolean areAllPlayersAsleep()
    {
        return false;
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    @Override
    public void setSpawnLocation()
    {
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    @Override
    protected void tickBlocksAndAmbiance()
    {
    }

    /**
     * Schedules a tick to a block with a delay (Most commonly the tick rate)
     */
    @Override
    public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5)
    {
    }

    @Override
    public void scheduleBlockUpdateWithPriority(int par1, int par2, int par3, int par4, int par5, int par6)
    {
    }

    /**
     * Schedules a block update from the saved information in a chunk. Called when the chunk is loaded.
     */
    @Override
    public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6)
    {
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    @Override
    public void updateEntities()
    {
    }

    @Override
    public void resetUpdateEntityTick()
    {
    }

    /**
     * Runs through the list of updates to run and ticks them
     */
    @Override
    public boolean tickUpdates(boolean par1)
    {
        return false;
    }

    @Override
    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
    {
        return null;
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     * Args: entity, forceUpdate
     */
    @Override
    public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2)
    {
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    @Override
    protected IChunkProvider createChunkProvider()
    {
         return null;
    }

    /**
     * pars: min x,y,z , max x,y,z
     */
    @Override
    public List getAllTileEntityInBox(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        ArrayList arraylist = new ArrayList();

        return arraylist;
    }

    /**
     * Called when checking if a certain block can be mined or not. The 'spawn safe zone' check is located here.
     */
    @Override
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int par4)
    {
        return normalWorld.canMineBlock(par1EntityPlayer, par2, par3, par4);
    }

    @Override
    protected void initialize(WorldSettings par1WorldSettings)
    {
    }

    /**
     * creates a spawn position at random within 256 blocks of 0,0
     */
    @Override
    protected void createSpawnPosition(WorldSettings par1WorldSettings)
    {
    }

    /**
     * Creates the bonus chest in the world.
     */
    @Override
    protected void createBonusChest()
    {
    }

    /**
     * Gets the hard-coded portal location to use when entering this dimension.
     */
    @Override
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return normalWorld.getEntrancePortalLocation();
    }

    /**
     * Saves all chunks to disk while updating progress bar.
     */
    @Override
    public void saveAllChunks(boolean par1, IProgressUpdate par2IProgressUpdate) throws MinecraftException
    {
    }

    /**
     * Saves the chunks to disk.
     */
    @Override
    protected void saveLevel() throws MinecraftException
    {
    }

    /**
     * Start the skin for this entity downloading, if necessary, and increment its reference counter
     */
    @Override
    protected void onEntityAdded(Entity par1Entity)
    {
    }

    /**
     * Decrement the reference counter for this entity's skin image data
     */
    @Override
    protected void onEntityRemoved(Entity par1Entity)
    {
    }

    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    @Override
    public Entity getEntityByID(int par1)
    {
        return normalWorld.getEntityByID(par1);
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    @Override
    public boolean addWeatherEffect(Entity par1Entity)
    {
        return false;
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    @Override
    public void setEntityState(Entity par1Entity, byte par2)
    {
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    @Override
    public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
    {
        return null;
    }

    /**
     * Adds a block event with the given Args to the blockEventCache. During the next tick(), the block specified will
     * have its onBlockEvent handler called with the given parameters. Args: X,Y,Z, BlockID, EventID, EventParameter
     */
    @Override
    public void addBlockEvent(int par1, int par2, int par3, int par4, int par5, int par6)
    {
    }

    /**
     * Syncs all changes to disk and wait for completion.
     */
    @Override
    public void flush()
    {
    }

    /**
     * Updates all weather states.
     */
    @Override
    protected void updateWeather()
    {
    }

    /**
     * Gets the EntityTracker
     */
    @Override
    public EntityTracker getEntityTracker()
    {
        return null;
    }

    @Override
    public PlayerManager getPlayerManager()
    {
        return null;
    }

    @Override
    public Teleporter getDefaultTeleporter()
    {
        return null;
    }

//  If NBXlite is installed, this overrides the one from WorldServer.class, otherwise this is just a method.
    public void turnOnOldSpawners()
    {
    }

    @Override
    public void addWorldAccess(IWorldAccess par1IWorldAccess)
    {
    }


    /**
     * On the client, re-renders the block. On the server, sends the block to the client (which will re-render it),
     * including the tile entity description packet if applicable. Args: x, y, z
     */
    @Override
    public void markBlockForUpdate(int par1, int par2, int par3)
    {
        normalWorld.markBlockForUpdate(par1, par2, par3);
    }

    /**
     * marks a vertical line of blocks as dirty
     */
    @Override
    public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4)
    {
        normalWorld.markBlocksDirtyVertical(par1, par2, par3, par4);
    }
}
