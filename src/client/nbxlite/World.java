package net.minecraft.src;

import java.util.*;

public abstract class World implements IBlockAccess
{
    /**
     * boolean; if true updates scheduled by scheduleBlockUpdate happen immediately
     */
    public boolean scheduledUpdatesAreImmediate;

    /** A list of all Entities in all currently-loaded chunks */
    public List loadedEntityList;
    protected List unloadedEntityList;

    /** A list of all TileEntities in all currently-loaded chunks */
    public List loadedTileEntityList;
    private List addedTileEntityList;

    /** Entities marked for removal. */
    private List entityRemoval;

    /** Array list of players in the world. */
    public List playerEntities;

    /** a list of all the lightning entities */
    public List weatherEffects;
    private long cloudColour;

    /** How much light is subtracted from full daylight */
    public int skylightSubtracted;

    /**
     * Contains the current Linear Congruential Generator seed for block updates. Used with an A value of 3 and a C
     * value of 0x3c6ef35f, producing a highly planar series of values ill-suited for choosing random blocks in a
     * 16x128x16 field.
     */
    protected int updateLCG;

    /**
     * magic number used to generate fast random numbers for 3d distribution within a chunk
     */
    protected final int DIST_HASH_MAGIC = 0x3c6ef35f;
    protected float prevRainingStrength;
    protected float rainingStrength;
    protected float prevThunderingStrength;
    protected float thunderingStrength;

    /**
     * Set to 2 whenever a lightning bolt is generated in SSP. Decrements if > 0 in updateWeather(). Value appears to be
     * unused.
     */
    protected int lastLightningBolt;

    /**
     * If > 0, the sky and skylight colors are illuminated by a lightning flash
     */
    public int lightningFlash;

    /** true while the world is editing blocks */
    public boolean editingBlocks;

    /** Option > Difficulty setting (0 - 3) */
    public int difficultySetting;

    /** RNG for World. */
    public Random rand;

    /** The WorldProvider instance that World uses. */
    public final WorldProvider worldProvider;
    protected List worldAccesses;

    /** Handles chunk operations and caching */
    protected IChunkProvider chunkProvider;
    protected final ISaveHandler saveHandler;

    /**
     * holds information about a world (size on disk, time, spawn point, seed, ...)
     */
    protected WorldInfo worldInfo;

    /** Boolean that is set to true when trying to find a spawn point */
    public boolean findingSpawnPoint;
    public MapStorage mapStorage;
    public final VillageCollection villageCollectionObj;
    protected final VillageSiege villageSiegeObj;
    public final Profiler field_72984_F;
    private ArrayList collidingBoundingBoxes;
    private boolean scanningTileEntities;

    /** indicates if enemies are spawned or not */
    protected boolean spawnHostileMobs;

    /** A flag indicating whether we should spawn peaceful mobs. */
    protected boolean spawnPeacefulMobs;

    /** Positions to update */
    protected Set activeChunkSet;

    /** number of ticks until the next random ambients play */
    private int ambientTickCountdown;
    int lightUpdateBlockList[];

    /**
     * entities within AxisAlignedBB excluding one, set and returned in getEntitiesWithinAABBExcludingEntity(Entity
     * var1, AxisAlignedBB var2)
     */
    private List entitiesWithinAABBExcludingEntity;

    /**
     * This is set to true when you are a client connected to a multiplayer world, false otherwise.
     */
    public boolean isRemote;

    /**
     * Gets the biome for a given set of x/z coordinates
     */
    public BiomeGenBase getBiomeGenForCoords(int par1, int par2)
    {
        if (blockExists(par1, 0, par2))
        {
            Chunk chunk = getChunkFromBlockCoords(par1, par2);

            if (chunk != null)
            {
                return chunk.getBiomeGenForWorldCoords(par1 & 0xf, par2 & 0xf, worldProvider.worldChunkMgr);
            }
        }

        return worldProvider.worldChunkMgr.getBiomeGenAt(par1, par2);
    }

    public WorldChunkManager getWorldChunkManager()
    {
        return worldProvider.worldChunkMgr;
    }

    public World(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler par5Profiler)
    {
        scheduledUpdatesAreImmediate = false;
        loadedEntityList = new ArrayList();
        unloadedEntityList = new ArrayList();
        loadedTileEntityList = new ArrayList();
        addedTileEntityList = new ArrayList();
        entityRemoval = new ArrayList();
        playerEntities = new ArrayList();
        weatherEffects = new ArrayList();
        cloudColour = 0xffffffL;
        skylightSubtracted = 0;
        updateLCG = (new Random()).nextInt();
        lastLightningBolt = 0;
        lightningFlash = 0;
        editingBlocks = false;
        rand = new Random();
        worldAccesses = new ArrayList();
        villageCollectionObj = new VillageCollection(this);
        villageSiegeObj = new VillageSiege(this);
        collidingBoundingBoxes = new ArrayList();
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        activeChunkSet = new HashSet();
        ambientTickCountdown = rand.nextInt(12000);
        lightUpdateBlockList = new int[32768];
        entitiesWithinAABBExcludingEntity = new ArrayList();
        isRemote = false;
        saveHandler = par1ISaveHandler;
        field_72984_F = par5Profiler;
        worldInfo = new WorldInfo(par4WorldSettings, par2Str);
        worldProvider = par3WorldProvider;
        mapStorage = new MapStorage(par1ISaveHandler);
        par3WorldProvider.registerWorld(this);
        chunkProvider = createChunkProvider();
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    public World(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, WorldProvider par4WorldProvider, Profiler par5Profiler)
    {
        scheduledUpdatesAreImmediate = false;
        loadedEntityList = new ArrayList();
        unloadedEntityList = new ArrayList();
        loadedTileEntityList = new ArrayList();
        addedTileEntityList = new ArrayList();
        entityRemoval = new ArrayList();
        playerEntities = new ArrayList();
        weatherEffects = new ArrayList();
        cloudColour = 0xffffffL;
        skylightSubtracted = 0;
        updateLCG = (new Random()).nextInt();
        lastLightningBolt = 0;
        lightningFlash = 0;
        editingBlocks = false;
        rand = new Random();
        worldAccesses = new ArrayList();
        villageCollectionObj = new VillageCollection(this);
        villageSiegeObj = new VillageSiege(this);
        collidingBoundingBoxes = new ArrayList();
        spawnHostileMobs = true;
        spawnPeacefulMobs = true;
        activeChunkSet = new HashSet();
        ambientTickCountdown = rand.nextInt(12000);
        lightUpdateBlockList = new int[32768];
        entitiesWithinAABBExcludingEntity = new ArrayList();
        isRemote = false;
        saveHandler = par1ISaveHandler;
        field_72984_F = par5Profiler;
        mapStorage = new MapStorage(par1ISaveHandler);
        worldInfo = par1ISaveHandler.loadWorldInfo();

        if (par4WorldProvider != null)
        {
            worldProvider = par4WorldProvider;
        }
        else if (worldInfo != null && worldInfo.getDimension() != 0)
        {
            worldProvider = WorldProvider.getProviderForDimension(worldInfo.getDimension());
        }
        else
        {
            worldProvider = WorldProvider.getProviderForDimension(0);
        }

        if (worldInfo == null)
        {
            worldInfo = new WorldInfo(par3WorldSettings, par2Str);
        }
        else
        {
            worldInfo.setWorldName(par2Str);
        }

        worldProvider.registerWorld(this);
        chunkProvider = createChunkProvider();

        if (!worldInfo.func_76070_v())
        {
            func_72963_a(par3WorldSettings);
            worldInfo.func_76091_d(true);
        }

        calculateInitialSkylight();
        calculateInitialWeather();
    }

    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    protected abstract IChunkProvider createChunkProvider();

    protected void func_72963_a(WorldSettings par1WorldSettings)
    {
        worldInfo.func_76091_d(true);
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    public void setSpawnLocation()
    {
        func_72950_A(8, 64, 8);
    }

    /**
     * Returns the block ID of the first block at this (x,z) location with air above it, searching from sea level
     * upwards.
     */
    public int getFirstUncoveredBlock(int par1, int par2)
    {
        int i;

        for (i = 63; !isAirBlock(par1, i + 1, par2); i++) { }

        return getBlockId(par1, i, par2);
    }

    /**
     * Returns the block ID at coords x,y,z
     */
    public int getBlockId(int par1, int par2, int par3)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return 0;
        }

        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            return 0;
        }
        else
        {
            return getChunkFromChunkCoords(par1 >> 4, par3 >> 4).getBlockID(par1 & 0xf, par2, par3 & 0xf);
        }
    }

    public int getBlockLightOpacity(int par1, int par2, int par3)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return 0;
        }

        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            return 0;
        }
        else
        {
            return getChunkFromChunkCoords(par1 >> 4, par3 >> 4).getBlockLightOpacity(par1 & 0xf, par2, par3 & 0xf);
        }
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    public boolean isAirBlock(int par1, int par2, int par3)
    {
        return getBlockId(par1, par2, par3) == 0;
    }

    public boolean func_72927_d(int par1, int par2, int par3)
    {
        int i = getBlockId(par1, par2, par3);
        return Block.blocksList[i] != null && Block.blocksList[i].hasTileEntity();
    }

    /**
     * Returns whether a block exists at world coordinates x, y, z
     */
    public boolean blockExists(int par1, int par2, int par3)
    {
        if (par2 < 0 || par2 >= 256)
        {
            return false;
        }
        else
        {
            return chunkExists(par1 >> 4, par3 >> 4);
        }
    }

    /**
     * Checks if any of the chunks within distance (argument 4) blocks of the given block exist
     */
    public boolean doChunksNearChunkExist(int par1, int par2, int par3, int par4)
    {
        return checkChunksExist(par1 - par4, par2 - par4, par3 - par4, par1 + par4, par2 + par4, par3 + par4);
    }

    /**
     * Checks between a min and max all the chunks inbetween actually exist. Args: minX, minY, minZ, maxX, maxY, maxZ
     */
    public boolean checkChunksExist(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        if (par5 < 0 || par2 >= 256)
        {
            return false;
        }

        par1 >>= 4;
        par3 >>= 4;
        par4 >>= 4;
        par6 >>= 4;

        for (int i = par1; i <= par4; i++)
        {
            for (int j = par3; j <= par6; j++)
            {
                if (!chunkExists(i, j))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Returns whether a chunk exists at chunk coordinates x, y
     */
    protected boolean chunkExists(int par1, int par2)
    {
        return chunkProvider.chunkExists(par1, par2);
    }

    /**
     * Returns a chunk looked up by block coordinates. Args: x, z
     */
    public Chunk getChunkFromBlockCoords(int par1, int par2)
    {
        return getChunkFromChunkCoords(par1 >> 4, par2 >> 4);
    }

    /**
     * Returns back a chunk looked up by chunk coordinates Args: x, y
     */
    public Chunk getChunkFromChunkCoords(int par1, int par2)
    {
        return chunkProvider.provideChunk(par1, par2);
    }

    /**
     * Sets the block ID and metadata of a block in global coordinates
     */
    public boolean setBlockAndMetadata(int par1, int par2, int par3, int par4, int par5)
    {
        return func_72930_a(par1, par2, par3, par4, par5, true);
    }

    public boolean func_72930_a(int par1, int par2, int par3, int par4, int par5, boolean par6)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return false;
        }

        if (par2 < 0)
        {
            return false;
        }

        if (par2 >= 256)
        {
            return false;
        }

        Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
        boolean flag = chunk.setBlockIDWithMetadata(par1 & 0xf, par2, par3 & 0xf, par4, par5);
        field_72984_F.startSection("checkLight");
        updateAllLightTypes(par1, par2, par3);
        field_72984_F.endSection();

        if (par6 && flag && (isRemote || chunk.deferRender))
        {
            markBlockNeedsUpdate(par1, par2, par3);
        }

        return flag;
    }

    /**
     * Sets the block to the specified blockID at the block coordinates Args x, y, z, blockID
     */
    public boolean setBlock(int par1, int par2, int par3, int par4)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return false;
        }

        if (par2 < 0)
        {
            return false;
        }

        if (par2 >= 256)
        {
            return false;
        }

        Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
        boolean flag = chunk.setBlockID(par1 & 0xf, par2, par3 & 0xf, par4);
        field_72984_F.startSection("checkLight");
        updateAllLightTypes(par1, par2, par3);
        field_72984_F.endSection();

        if (flag && (isRemote || chunk.deferRender))
        {
            markBlockNeedsUpdate(par1, par2, par3);
        }

        return flag;
    }

    /**
     * Returns the block's material.
     */
    public Material getBlockMaterial(int par1, int par2, int par3)
    {
        int i = getBlockId(par1, par2, par3);

        if (i == 0)
        {
            return Material.air;
        }
        else
        {
            return Block.blocksList[i].blockMaterial;
        }
    }

    /**
     * Returns the block metadata at coords x,y,z
     */
    public int getBlockMetadata(int par1, int par2, int par3)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return 0;
        }

        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            return 0;
        }
        else
        {
            Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
            par1 &= 0xf;
            par3 &= 0xf;
            return chunk.getBlockMetadata(par1, par2, par3);
        }
    }

    /**
     * Sets the blocks metadata and if set will then notify blocks that this block changed. Args: x, y, z, metadata
     */
    public void setBlockMetadataWithNotify(int par1, int par2, int par3, int par4)
    {
        if (setBlockMetadata(par1, par2, par3, par4))
        {
            notifyBlockChange(par1, par2, par3, getBlockId(par1, par2, par3));
        }
    }

    /**
     * Set the metadata of a block in global coordinates
     */
    public boolean setBlockMetadata(int par1, int par2, int par3, int par4)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return false;
        }

        if (par2 < 0)
        {
            return false;
        }

        if (par2 >= 256)
        {
            return false;
        }

        Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
        int i = par1 & 0xf;
        int j = par3 & 0xf;
        boolean flag = chunk.setBlockMetadata(i, par2, j, par4);

        if (flag && (isRemote || chunk.deferRender && Block.requiresSelfNotify[chunk.getBlockID(i, par2, j) & 0xfff]))
        {
            markBlockNeedsUpdate(par1, par2, par3);
        }

        return flag;
    }

    /**
     * Sets a block and notifies relevant systems with the block change  Args: x, y, z, blockID
     */
    public boolean setBlockWithNotify(int par1, int par2, int par3, int par4)
    {
        if (setBlock(par1, par2, par3, par4))
        {
            notifyBlockChange(par1, par2, par3, par4);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Sets the block ID and metadata, then notifies neighboring blocks of the change Params: x, y, z, BlockID, Metadata
     */
    public boolean setBlockAndMetadataWithNotify(int par1, int par2, int par3, int par4, int par5)
    {
        if (setBlockAndMetadata(par1, par2, par3, par4, par5))
        {
            notifyBlockChange(par1, par2, par3, par4);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Marks the block as needing an update with the renderer. Args: x, y, z
     */
    public void markBlockNeedsUpdate(int par1, int par2, int par3)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.markBlockNeedsUpdate(par1, par2, par3))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * The block type change and need to notify other systems  Args: x, y, z, blockID
     */
    public void notifyBlockChange(int par1, int par2, int par3, int par4)
    {
        notifyBlocksOfNeighborChange(par1, par2, par3, par4);
    }

    /**
     * marks a vertical line of blocks as dirty
     */
    public void markBlocksDirtyVertical(int par1, int par2, int par3, int par4)
    {
        if (par3 > par4)
        {
            int i = par4;
            par4 = par3;
            par3 = i;
        }

        if (!worldProvider.hasNoSky)
        {
            for (int j = par3; j <= par4; j++)
            {
                updateLightByType(EnumSkyBlock.Sky, par1, j, par2);
            }
        }

        markBlocksDirty(par1, par3, par2, par1, par4, par2);
    }

    /**
     * calls the 'MarkBlockAsNeedsUpdate' in all block accesses in this world
     */
    public void markBlockAsNeedsUpdate(int par1, int par2, int par3)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.markBlockRangeNeedsUpdate(par1, par2, par3, par1, par2, par3))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    public void markBlocksDirty(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.markBlockRangeNeedsUpdate(par1, par2, par3, par4, par5, par6))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * Notifies neighboring blocks that this specified block changed  Args: x, y, z, blockID
     */
    public void notifyBlocksOfNeighborChange(int par1, int par2, int par3, int par4)
    {
        notifyBlockOfNeighborChange(par1 - 1, par2, par3, par4);
        notifyBlockOfNeighborChange(par1 + 1, par2, par3, par4);
        notifyBlockOfNeighborChange(par1, par2 - 1, par3, par4);
        notifyBlockOfNeighborChange(par1, par2 + 1, par3, par4);
        notifyBlockOfNeighborChange(par1, par2, par3 - 1, par4);
        notifyBlockOfNeighborChange(par1, par2, par3 + 1, par4);
    }

    /**
     * Notifies a block that one of its neighbor change to the specified type Args: x, y, z, blockID
     */
    private void notifyBlockOfNeighborChange(int par1, int par2, int par3, int par4)
    {
        if (editingBlocks || isRemote)
        {
            return;
        }

        Block block = Block.blocksList[getBlockId(par1, par2, par3)];

        if (block != null)
        {
            block.onNeighborBlockChange(this, par1, par2, par3, par4);
        }
    }

    /**
     * Checks if the specified block is able to see the sky
     */
    public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return getChunkFromChunkCoords(par1 >> 4, par3 >> 4).canBlockSeeTheSky(par1 & 0xf, par2, par3 & 0xf);
    }

    /**
     * Does the same as getBlockLightValue_do but without checking if its not a normal block
     */
    public int getFullBlockLightValue(int par1, int par2, int par3)
    {
        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            par2 = 255;
        }

        return getChunkFromChunkCoords(par1 >> 4, par3 >> 4).getBlockLightValue(par1 & 0xf, par2, par3 & 0xf, 0);
    }

    /**
     * Gets the light value of a block location
     */
    public int getBlockLightValue(int par1, int par2, int par3)
    {
        return getBlockLightValue_do(par1, par2, par3, true);
    }

    /**
     * Gets the light value of a block location. This is the actual function that gets the value and has a bool flag
     * that indicates if its a half step block to get the maximum light value of a direct neighboring block (left,
     * right, forward, back, and up)
     */
    public int getBlockLightValue_do(int par1, int par2, int par3, boolean par4)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return 15;
        }

        if (par4)
        {
            int i = getBlockId(par1, par2, par3);

            if (i == Block.field_72079_ak.blockID || i == Block.field_72092_bO.blockID || i == Block.tilledField.blockID || i == Block.stairCompactCobblestone.blockID || i == Block.stairCompactPlanks.blockID)
            {
                int j = getBlockLightValue_do(par1, par2 + 1, par3, false);
                int k = getBlockLightValue_do(par1 + 1, par2, par3, false);
                int l = getBlockLightValue_do(par1 - 1, par2, par3, false);
                int i1 = getBlockLightValue_do(par1, par2, par3 + 1, false);
                int j1 = getBlockLightValue_do(par1, par2, par3 - 1, false);

                if (k > j)
                {
                    j = k;
                }

                if (l > j)
                {
                    j = l;
                }

                if (i1 > j)
                {
                    j = i1;
                }

                if (j1 > j)
                {
                    j = j1;
                }

                return j;
            }
        }

        if (par2 < 0)
        {
            return 0;
        }

        if (par2 >= 256)
        {
            par2 = 255;
        }

        Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);
        par1 &= 0xf;
        par3 &= 0xf;
        return chunk.getBlockLightValue(par1, par2, par3, skylightSubtracted);
    }

    /**
     * Returns the y coordinate with a block in it at this x, z coordinate
     */
    public int getHeightValue(int par1, int par2)
    {
        if (par1 < 0xfe363c80 || par2 < 0xfe363c80 || par1 >= 0x1c9c380 || par2 >= 0x1c9c380)
        {
            return 0;
        }

        if (!chunkExists(par1 >> 4, par2 >> 4))
        {
            return 0;
        }
        else
        {
            Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par2 >> 4);
            return chunk.getHeightValue(par1 & 0xf, par2 & 0xf);
        }
    }

    /**
     * Brightness for SkyBlock.Sky is clear white and (through color computing it is assumed) DEPENDENT ON DAYTIME.
     * Brightness for SkyBlock.Block is yellowish and independent.
     */
    public int getSkyBlockTypeBrightness(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        if (worldProvider.hasNoSky && par1EnumSkyBlock == EnumSkyBlock.Sky)
        {
            return 0;
        }

        if (par3 < 0)
        {
            par3 = 0;
        }

        if (par3 >= 256)
        {
            return par1EnumSkyBlock.defaultLightValue;
        }

        if (par2 < 0xfe363c80 || par4 < 0xfe363c80 || par2 >= 0x1c9c380 || par4 >= 0x1c9c380)
        {
            return par1EnumSkyBlock.defaultLightValue;
        }

        int i = par2 >> 4;
        int j = par4 >> 4;

        if (!chunkExists(i, j))
        {
            return par1EnumSkyBlock.defaultLightValue;
        }

        if (Block.useNeighborBrightness[getBlockId(par2, par3, par4)])
        {
            int k = getSavedLightValue(par1EnumSkyBlock, par2, par3 + 1, par4);
            int l = getSavedLightValue(par1EnumSkyBlock, par2 + 1, par3, par4);
            int i1 = getSavedLightValue(par1EnumSkyBlock, par2 - 1, par3, par4);
            int j1 = getSavedLightValue(par1EnumSkyBlock, par2, par3, par4 + 1);
            int k1 = getSavedLightValue(par1EnumSkyBlock, par2, par3, par4 - 1);

            if (l > k)
            {
                k = l;
            }

            if (i1 > k)
            {
                k = i1;
            }

            if (j1 > k)
            {
                k = j1;
            }

            if (k1 > k)
            {
                k = k1;
            }

            return k;
        }
        else
        {
            Chunk chunk = getChunkFromChunkCoords(i, j);
            return chunk.getSavedLightValue(par1EnumSkyBlock, par2 & 0xf, par3, par4 & 0xf);
        }
    }

    /**
     * Returns saved light value without taking into account the time of day.  Either looks in the sky light map or
     * block light map based on the enumSkyBlock arg.
     */
    public int getSavedLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        if (par3 < 0)
        {
            par3 = 0;
        }

        if (par3 >= 256)
        {
            par3 = 255;
        }

        if (par2 < 0xfe363c80 || par4 < 0xfe363c80 || par2 >= 0x1c9c380 || par4 >= 0x1c9c380)
        {
            return par1EnumSkyBlock.defaultLightValue;
        }

        int i = par2 >> 4;
        int j = par4 >> 4;

        if (!chunkExists(i, j))
        {
            return par1EnumSkyBlock.defaultLightValue;
        }
        else
        {
            Chunk chunk = getChunkFromChunkCoords(i, j);
            return chunk.getSavedLightValue(par1EnumSkyBlock, par2 & 0xf, par3, par4 & 0xf);
        }
    }

    /**
     * Sets the light value either into the sky map or block map depending on if enumSkyBlock is set to sky or block.
     * Args: enumSkyBlock, x, y, z, lightValue
     */
    public void setLightValue(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4, int par5)
    {
        if (par2 < 0xfe363c80 || par4 < 0xfe363c80 || par2 >= 0x1c9c380 || par4 >= 0x1c9c380)
        {
            return;
        }

        if (par3 < 0)
        {
            return;
        }

        if (par3 >= 256)
        {
            return;
        }

        if (!chunkExists(par2 >> 4, par4 >> 4))
        {
            return;
        }

        Chunk chunk = getChunkFromChunkCoords(par2 >> 4, par4 >> 4);
        chunk.setLightValue(par1EnumSkyBlock, par2 & 0xf, par3, par4 & 0xf, par5);
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.markBlockNeedsUpdate2(par2, par3, par4))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    public void func_72902_n(int par1, int par2, int par3)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.markBlockNeedsUpdate2(par1, par2, par3))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * Any Light rendered on a 1.8 Block goes through here
     */
    public int getLightBrightnessForSkyBlocks(int par1, int par2, int par3, int par4)
    {
        int i = getSkyBlockTypeBrightness(EnumSkyBlock.Sky, par1, par2, par3);
        int j = getSkyBlockTypeBrightness(EnumSkyBlock.Block, par1, par2, par3);

        if (j < par4)
        {
            j = par4;
        }

        return i << 20 | j << 4;
    }

    public float getBrightness(int par1, int par2, int par3, int par4)
    {
        int i = getBlockLightValue(par1, par2, par3);

        if (i < par4)
        {
            i = par4;
        }

        return worldProvider.lightBrightnessTable[i];
    }

    /**
     * Returns how bright the block is shown as which is the block's light value looked up in a lookup table (light
     * values aren't linear for brightness). Args: x, y, z
     */
    public float getLightBrightness(int par1, int par2, int par3)
    {
        return worldProvider.lightBrightnessTable[getBlockLightValue(par1, par2, par3)];
    }

    /**
     * Checks whether its daytime by seeing if the light subtracted from the skylight is less than 4
     */
    public boolean isDaytime()
    {
        return skylightSubtracted < 4;
    }

    /**
     * ray traces all blocks, including non-collideable ones
     */
    public MovingObjectPosition rayTraceBlocks(Vec3 par1Vec3, Vec3 par2Vec3)
    {
        return rayTraceBlocks_do_do(par1Vec3, par2Vec3, false, false);
    }

    public MovingObjectPosition rayTraceBlocks_do(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3)
    {
        return rayTraceBlocks_do_do(par1Vec3, par2Vec3, par3, false);
    }

    public MovingObjectPosition rayTraceBlocks_do_do(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3, boolean par4)
    {
        if (Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) || Double.isNaN(par1Vec3.zCoord))
        {
            return null;
        }

        if (Double.isNaN(par2Vec3.xCoord) || Double.isNaN(par2Vec3.yCoord) || Double.isNaN(par2Vec3.zCoord))
        {
            return null;
        }

        int i = MathHelper.floor_double(par2Vec3.xCoord);
        int j = MathHelper.floor_double(par2Vec3.yCoord);
        int k = MathHelper.floor_double(par2Vec3.zCoord);
        int l = MathHelper.floor_double(par1Vec3.xCoord);
        int i1 = MathHelper.floor_double(par1Vec3.yCoord);
        int j1 = MathHelper.floor_double(par1Vec3.zCoord);
        int k1 = getBlockId(l, i1, j1);
        int i2 = getBlockMetadata(l, i1, j1);
        Block block = Block.blocksList[k1];

        if ((!par4 || block == null || block.getCollisionBoundingBoxFromPool(this, l, i1, j1) != null) && k1 > 0 && block.canCollideCheck(i2, par3))
        {
            MovingObjectPosition movingobjectposition = block.collisionRayTrace(this, l, i1, j1, par1Vec3, par2Vec3);

            if (movingobjectposition != null)
            {
                return movingobjectposition;
            }
        }

        for (int l1 = 200; l1-- >= 0;)
        {
            if (Double.isNaN(par1Vec3.xCoord) || Double.isNaN(par1Vec3.yCoord) || Double.isNaN(par1Vec3.zCoord))
            {
                return null;
            }

            if (l == i && i1 == j && j1 == k)
            {
                return null;
            }

            boolean flag = true;
            boolean flag1 = true;
            boolean flag2 = true;
            double d = 999D;
            double d1 = 999D;
            double d2 = 999D;

            if (i > l)
            {
                d = (double)l + 1.0D;
            }
            else if (i < l)
            {
                d = (double)l + 0.0D;
            }
            else
            {
                flag = false;
            }

            if (j > i1)
            {
                d1 = (double)i1 + 1.0D;
            }
            else if (j < i1)
            {
                d1 = (double)i1 + 0.0D;
            }
            else
            {
                flag1 = false;
            }

            if (k > j1)
            {
                d2 = (double)j1 + 1.0D;
            }
            else if (k < j1)
            {
                d2 = (double)j1 + 0.0D;
            }
            else
            {
                flag2 = false;
            }

            double d3 = 999D;
            double d4 = 999D;
            double d5 = 999D;
            double d6 = par2Vec3.xCoord - par1Vec3.xCoord;
            double d7 = par2Vec3.yCoord - par1Vec3.yCoord;
            double d8 = par2Vec3.zCoord - par1Vec3.zCoord;

            if (flag)
            {
                d3 = (d - par1Vec3.xCoord) / d6;
            }

            if (flag1)
            {
                d4 = (d1 - par1Vec3.yCoord) / d7;
            }

            if (flag2)
            {
                d5 = (d2 - par1Vec3.zCoord) / d8;
            }

            byte byte0 = 0;

            if (d3 < d4 && d3 < d5)
            {
                if (i > l)
                {
                    byte0 = 4;
                }
                else
                {
                    byte0 = 5;
                }

                par1Vec3.xCoord = d;
                par1Vec3.yCoord += d7 * d3;
                par1Vec3.zCoord += d8 * d3;
            }
            else if (d4 < d5)
            {
                if (j > i1)
                {
                    byte0 = 0;
                }
                else
                {
                    byte0 = 1;
                }

                par1Vec3.xCoord += d6 * d4;
                par1Vec3.yCoord = d1;
                par1Vec3.zCoord += d8 * d4;
            }
            else
            {
                if (k > j1)
                {
                    byte0 = 2;
                }
                else
                {
                    byte0 = 3;
                }

                par1Vec3.xCoord += d6 * d5;
                par1Vec3.yCoord += d7 * d5;
                par1Vec3.zCoord = d2;
            }

            Vec3 vec3 = Vec3.func_72437_a().func_72345_a(par1Vec3.xCoord, par1Vec3.yCoord, par1Vec3.zCoord);
            l = (int)(vec3.xCoord = MathHelper.floor_double(par1Vec3.xCoord));

            if (byte0 == 5)
            {
                l--;
                vec3.xCoord++;
            }

            i1 = (int)(vec3.yCoord = MathHelper.floor_double(par1Vec3.yCoord));

            if (byte0 == 1)
            {
                i1--;
                vec3.yCoord++;
            }

            j1 = (int)(vec3.zCoord = MathHelper.floor_double(par1Vec3.zCoord));

            if (byte0 == 3)
            {
                j1--;
                vec3.zCoord++;
            }

            int j2 = getBlockId(l, i1, j1);
            int k2 = getBlockMetadata(l, i1, j1);
            Block block1 = Block.blocksList[j2];

            if ((!par4 || block1 == null || block1.getCollisionBoundingBoxFromPool(this, l, i1, j1) != null) && j2 > 0 && block1.canCollideCheck(k2, par3))
            {
                MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, l, i1, j1, par1Vec3, par2Vec3);

                if (movingobjectposition1 != null)
                {
                    return movingobjectposition1;
                }
            }
        }

        return null;
    }

    /**
     * Plays a sound at the entity's position. Args: entity, sound, volume (relative to 1.0), and frequency (or pitch,
     * also relative to 1.0).
     */
    public void playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4)
    {
        if (par1Entity == null || par2Str == null)
        {
            return;
        }

        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.playSound(par2Str, par1Entity.posX, par1Entity.posY - (double)par1Entity.yOffset, par1Entity.posZ, par3, par4))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * Play a sound effect. Many many parameters for this function. Not sure what they do, but a classic call is :
     * (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 'random.door_open', 1.0F, world.rand.nextFloat() * 0.1F +
     * 0.9F with i,j,k position of the block.
     */
    public void playSoundEffect(double par1, double par3, double par5, String par7Str, float par8, float par9)
    {
        if (par7Str == null)
        {
            return;
        }

        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.playSound(par7Str, par1, par3, par5, par8, par9))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    public void func_72980_b(double d, double d1, double d2, String s, float f, float f1)
    {
    }

    /**
     * Plays a record at the specified coordinates of the specified name. Args: recordName, x, y, z
     */
    public void playRecord(String par1Str, int par2, int par3, int par4)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.playRecord(par1Str, par2, par3, par4))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * Spawns a particle.  Args particleName, x, y, z, velX, velY, velZ
     */
    public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.spawnParticle(par1Str, par2, par4, par6, par8, par10, par12))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    public boolean addWeatherEffect(Entity par1Entity)
    {
        weatherEffects.add(par1Entity);
        return true;
    }

    /**
     * Called to place all entities as part of a world
     */
    public boolean spawnEntityInWorld(Entity par1Entity)
    {
        int i = MathHelper.floor_double(par1Entity.posX / 16D);
        int j = MathHelper.floor_double(par1Entity.posZ / 16D);
        boolean flag = false;

        if (par1Entity instanceof EntityPlayer)
        {
            flag = true;
        }

        if (flag || chunkExists(i, j))
        {
            if (par1Entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)par1Entity;
                playerEntities.add(entityplayer);
                updateAllPlayersSleepingFlag();
            }

            getChunkFromChunkCoords(i, j).addEntity(par1Entity);
            loadedEntityList.add(par1Entity);
            obtainEntitySkin(par1Entity);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Start the skin for this entity downloading, if necessary, and increment its reference counter
     */
    protected void obtainEntitySkin(Entity par1Entity)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.obtainEntitySkin(par1Entity))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * Decrement the reference counter for this entity's skin image data
     */
    protected void releaseEntitySkin(Entity par1Entity)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.releaseEntitySkin(par1Entity))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }

    /**
     * Dismounts the entity (and anything riding the entity), sets the dead flag, and removes the player entity from the
     * player entity list. Called by the playerLoggedOut function.
     */
    public void setEntityDead(Entity par1Entity)
    {
        if (par1Entity.riddenByEntity != null)
        {
            par1Entity.riddenByEntity.mountEntity(null);
        }

        if (par1Entity.ridingEntity != null)
        {
            par1Entity.mountEntity(null);
        }

        par1Entity.setDead();

        if (par1Entity instanceof EntityPlayer)
        {
            playerEntities.remove(par1Entity);
            updateAllPlayersSleepingFlag();
        }
    }

    public void func_72973_f(Entity par1Entity)
    {
        par1Entity.setDead();

        if (par1Entity instanceof EntityPlayer)
        {
            playerEntities.remove(par1Entity);
            updateAllPlayersSleepingFlag();
        }

        int i = par1Entity.chunkCoordX;
        int j = par1Entity.chunkCoordZ;

        if (par1Entity.addedToChunk && chunkExists(i, j))
        {
            getChunkFromChunkCoords(i, j).removeEntity(par1Entity);
        }

        loadedEntityList.remove(par1Entity);
        releaseEntitySkin(par1Entity);
    }

    /**
     * Adds a IWorldAccess to the list of worldAccesses
     */
    public void addWorldAccess(IWorldAccess par1IWorldAccess)
    {
        worldAccesses.add(par1IWorldAccess);
    }

    /**
     * Removes a worldAccess from the worldAccesses object
     */
    public void removeWorldAccess(IWorldAccess par1IWorldAccess)
    {
        worldAccesses.remove(par1IWorldAccess);
    }

    /**
     * Returns a list of bounding boxes that collide with aabb excluding the passed in entity's collision. Args: entity,
     * aabb
     */
    public List getCollidingBoundingBoxes(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB)
    {
        collidingBoundingBoxes.clear();
        int i = MathHelper.floor_double(par2AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par2AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par2AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par2AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par2AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par2AxisAlignedBB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = i1; l1 < j1; l1++)
            {
                if (!blockExists(k1, 64, l1))
                {
                    continue;
                }

                for (int i2 = k - 1; i2 < l; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, i2, l1)];

                    if (block != null)
                    {
                        block.func_71871_a(this, k1, i2, l1, par2AxisAlignedBB, collidingBoundingBoxes, par1Entity);
                    }
                }
            }
        }

        double d = 0.25D;
        List list = getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB.expand(d, d, d));
        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity = (Entity)iterator.next();
            AxisAlignedBB axisalignedbb = entity.getBoundingBox();

            if (axisalignedbb != null && axisalignedbb.intersectsWith(par2AxisAlignedBB))
            {
                collidingBoundingBoxes.add(axisalignedbb);
            }

            axisalignedbb = par1Entity.getCollisionBox(entity);

            if (axisalignedbb != null && axisalignedbb.intersectsWith(par2AxisAlignedBB))
            {
                collidingBoundingBoxes.add(axisalignedbb);
            }
        }
        while (true);

        return collidingBoundingBoxes;
    }

    public List func_72840_a(AxisAlignedBB par1AxisAlignedBB)
    {
        collidingBoundingBoxes.clear();
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = i1; l1 < j1; l1++)
            {
                if (!blockExists(k1, 64, l1))
                {
                    continue;
                }

                for (int i2 = k - 1; i2 < l; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, i2, l1)];

                    if (block != null)
                    {
                        block.func_71871_a(this, k1, i2, l1, par1AxisAlignedBB, collidingBoundingBoxes, null);
                    }
                }
            }
        }

        return collidingBoundingBoxes;
    }

    /**
     * Returns the amount of skylight subtracted for the current time
     */
    public int calculateSkylightSubtracted(float par1)
    {
        float f = getCelestialAngle(par1);
        float f1 = 1.0F - (MathHelper.cos(f * (float)Math.PI * 2.0F) * 2.0F + 0.5F);

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(getRainStrength(par1) * 5F) / 16D));
        f1 = (float)((double)f1 * (1.0D - (double)(getWeightedThunderStrength(par1) * 5F) / 16D));
        f1 = 1.0F - f1;
        return (int)(f1 * 11F);
    }

    public float func_72971_b(float par1)
    {
        float f = getCelestialAngle(par1);
        float f1 = 1.0F - (MathHelper.cos(f * (float)Math.PI * 2.0F) * 2.0F + 0.2F);

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        f1 = 1.0F - f1;
        f1 = (float)((double)f1 * (1.0D - (double)(getRainStrength(par1) * 5F) / 16D));
        f1 = (float)((double)f1 * (1.0D - (double)(getWeightedThunderStrength(par1) * 5F) / 16D));
        return f1 * 0.8F + 0.2F;
    }

    /**
     * Calculates the color for the skybox
     */
    public Vec3 getSkyColor(Entity par1Entity, float par2)
    {
        float f = getCelestialAngle(par2);
        float f1 = MathHelper.cos(f * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        int i = MathHelper.floor_double(par1Entity.posX);
        int j = MathHelper.floor_double(par1Entity.posZ);
        BiomeGenBase biomegenbase = getBiomeGenForCoords(i, j);
        float f2 = biomegenbase.getFloatTemperature();
        int k = biomegenbase.getSkyColorByTemp(f2);
        float f3 = (float)(k >> 16 & 0xff) / 255F;
        float f4 = (float)(k >> 8 & 0xff) / 255F;
        float f5 = (float)(k & 0xff) / 255F;
        f3 *= f1;
        f4 *= f1;
        f5 *= f1;
        float f6 = getRainStrength(par2);

        if (f6 > 0.0F)
        {
            float f7 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
            float f9 = 1.0F - f6 * 0.75F;
            f3 = f3 * f9 + f7 * (1.0F - f9);
            f4 = f4 * f9 + f7 * (1.0F - f9);
            f5 = f5 * f9 + f7 * (1.0F - f9);
        }

        float f8 = getWeightedThunderStrength(par2);

        if (f8 > 0.0F)
        {
            float f10 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
            float f12 = 1.0F - f8 * 0.75F;
            f3 = f3 * f12 + f10 * (1.0F - f12);
            f4 = f4 * f12 + f10 * (1.0F - f12);
            f5 = f5 * f12 + f10 * (1.0F - f12);
        }

        if (lightningFlash > 0)
        {
            float f11 = (float)lightningFlash - par2;

            if (f11 > 1.0F)
            {
                f11 = 1.0F;
            }

            f11 *= 0.45F;
            f3 = f3 * (1.0F - f11) + 0.8F * f11;
            f4 = f4 * (1.0F - f11) + 0.8F * f11;
            f5 = f5 * (1.0F - f11) + 1.0F * f11;
        }

        return Vec3.func_72437_a().func_72345_a(f3, f4, f5);
    }

    /**
     * calls calculateCelestialAngle
     */
    public float getCelestialAngle(float par1)
    {
        return worldProvider.calculateCelestialAngle(worldInfo.getWorldTime(), par1);
    }

    public int getMoonPhase(float par1)
    {
        return worldProvider.getMoonPhase(worldInfo.getWorldTime(), par1);
    }

    /**
     * Return getCelestialAngle()*2*PI
     */
    public float getCelestialAngleRadians(float par1)
    {
        float f = getCelestialAngle(par1);
        return f * (float)Math.PI * 2.0F;
    }

    public Vec3 drawClouds(float par1)
    {
        float f = getCelestialAngle(par1);
        float f1 = MathHelper.cos(f * (float)Math.PI * 2.0F) * 2.0F + 0.5F;

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        float f2 = (float)(cloudColour >> 16 & 255L) / 255F;
        float f3 = (float)(cloudColour >> 8 & 255L) / 255F;
        float f4 = (float)(cloudColour & 255L) / 255F;
        float f5 = getRainStrength(par1);

        if (f5 > 0.0F)
        {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f8 = 1.0F - f5 * 0.95F;
            f2 = f2 * f8 + f6 * (1.0F - f8);
            f3 = f3 * f8 + f6 * (1.0F - f8);
            f4 = f4 * f8 + f6 * (1.0F - f8);
        }

        f2 *= f1 * 0.9F + 0.1F;
        f3 *= f1 * 0.9F + 0.1F;
        f4 *= f1 * 0.85F + 0.15F;
        float f7 = getWeightedThunderStrength(par1);

        if (f7 > 0.0F)
        {
            float f9 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f10 = 1.0F - f7 * 0.95F;
            f2 = f2 * f10 + f9 * (1.0F - f10);
            f3 = f3 * f10 + f9 * (1.0F - f10);
            f4 = f4 * f10 + f9 * (1.0F - f10);
        }

        return Vec3.func_72437_a().func_72345_a(f2, f3, f4);
    }

    /**
     * Returns vector(ish) with R/G/B for fog
     */
    public Vec3 getFogColor(float par1)
    {
        float f = getCelestialAngle(par1);
        return worldProvider.getFogColor(f, par1);
    }

    /**
     * Gets the height to which rain/snow will fall. Calculates it if not already stored.
     */
    public int getPrecipitationHeight(int par1, int par2)
    {
        return getChunkFromBlockCoords(par1, par2).getPrecipitationHeight(par1 & 0xf, par2 & 0xf);
    }

    /**
     * Finds the highest block on the x, z coordinate that is solid and returns its y coord. Args x, z
     */
    public int getTopSolidOrLiquidBlock(int par1, int par2)
    {
        Chunk chunk = getChunkFromBlockCoords(par1, par2);
        int i = chunk.getTopFilledSegment() + 15;
        par1 &= 0xf;
        par2 &= 0xf;

        while (i > 0)
        {
            int j = chunk.getBlockID(par1, i, par2);

            if (j == 0 || !Block.blocksList[j].blockMaterial.blocksMovement() || Block.blocksList[j].blockMaterial == Material.leaves)
            {
                i--;
            }
            else
            {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * How bright are stars in the sky
     */
    public float getStarBrightness(float par1)
    {
        float f = getCelestialAngle(par1);
        float f1 = 1.0F - (MathHelper.cos(f * (float)Math.PI * 2.0F) * 2.0F + 0.25F);

        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }

        if (f1 > 1.0F)
        {
            f1 = 1.0F;
        }

        return f1 * f1 * 0.5F;
    }

    /**
     * Schedules a tick to a block with a delay (Most commonly the tick rate)
     */
    public void scheduleBlockUpdate(int i, int j, int k, int l, int i1)
    {
    }

    /**
     * Schedules a block update from the saved information in a chunk. Called when the chunk is loaded.
     */
    public void scheduleBlockUpdateFromLoad(int i, int j, int k, int l, int i1)
    {
    }

    /**
     * Updates (and cleans up) entities and tile entities
     */
    public void updateEntities()
    {
        field_72984_F.startSection("entities");
        field_72984_F.startSection("global");

        for (int i = 0; i < weatherEffects.size(); i++)
        {
            Entity entity = (Entity)weatherEffects.get(i);
            entity.onUpdate();

            if (entity.isDead)
            {
                weatherEffects.remove(i--);
            }
        }

        field_72984_F.endStartSection("remove");
        loadedEntityList.removeAll(unloadedEntityList);
        Iterator iterator = unloadedEntityList.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity1 = (Entity)iterator.next();
            int k = entity1.chunkCoordX;
            int i1 = entity1.chunkCoordZ;

            if (entity1.addedToChunk && chunkExists(k, i1))
            {
                getChunkFromChunkCoords(k, i1).removeEntity(entity1);
            }
        }
        while (true);

        Entity entity2;

        for (iterator = unloadedEntityList.iterator(); iterator.hasNext(); releaseEntitySkin(entity2))
        {
            entity2 = (Entity)iterator.next();
        }

        unloadedEntityList.clear();
        field_72984_F.endStartSection("regular");

        for (int j = 0; j < loadedEntityList.size(); j++)
        {
            Entity entity3 = (Entity)loadedEntityList.get(j);

            if (entity3.ridingEntity != null)
            {
                if (!entity3.ridingEntity.isDead && entity3.ridingEntity.riddenByEntity == entity3)
                {
                    continue;
                }

                entity3.ridingEntity.riddenByEntity = null;
                entity3.ridingEntity = null;
            }

            field_72984_F.startSection("tick");

            if (!entity3.isDead)
            {
                updateEntity(entity3);
            }

            field_72984_F.endSection();
            field_72984_F.startSection("remove");

            if (entity3.isDead)
            {
                int l = entity3.chunkCoordX;
                int j1 = entity3.chunkCoordZ;

                if (entity3.addedToChunk && chunkExists(l, j1))
                {
                    getChunkFromChunkCoords(l, j1).removeEntity(entity3);
                }

                loadedEntityList.remove(j--);
                releaseEntitySkin(entity3);
            }

            field_72984_F.endSection();
        }

        field_72984_F.endStartSection("tileEntities");
        scanningTileEntities = true;
        Iterator j = loadedTileEntityList.iterator();

        do
        {
            if (!j.hasNext())
            {
                break;
            }

            TileEntity tileentity = (TileEntity)j.next();

            if (!tileentity.isInvalid() && tileentity.func_70309_m() && blockExists(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord))
            {
                tileentity.updateEntity();
            }

            if (tileentity.isInvalid())
            {
                j.remove();

                if (chunkExists(tileentity.xCoord >> 4, tileentity.zCoord >> 4))
                {
                    Chunk chunk = getChunkFromChunkCoords(tileentity.xCoord >> 4, tileentity.zCoord >> 4);

                    if (chunk != null)
                    {
                        chunk.removeChunkBlockTileEntity(tileentity.xCoord & 0xf, tileentity.yCoord, tileentity.zCoord & 0xf);
                    }
                }
            }
        }
        while (true);

        scanningTileEntities = false;

        if (!entityRemoval.isEmpty())
        {
            loadedTileEntityList.removeAll(entityRemoval);
            entityRemoval.clear();
        }

        field_72984_F.endStartSection("pendingTileEntities");

        if (!addedTileEntityList.isEmpty())
        {
            Iterator iterator1 = addedTileEntityList.iterator();

            do
            {
                if (!iterator1.hasNext())
                {
                    break;
                }

                TileEntity tileentity1 = (TileEntity)iterator1.next();

                if (!tileentity1.isInvalid())
                {
                    if (!loadedTileEntityList.contains(tileentity1))
                    {
                        loadedTileEntityList.add(tileentity1);
                    }

                    if (chunkExists(tileentity1.xCoord >> 4, tileentity1.zCoord >> 4))
                    {
                        Chunk chunk1 = getChunkFromChunkCoords(tileentity1.xCoord >> 4, tileentity1.zCoord >> 4);

                        if (chunk1 != null)
                        {
                            chunk1.setChunkBlockTileEntity(tileentity1.xCoord & 0xf, tileentity1.yCoord, tileentity1.zCoord & 0xf, tileentity1);
                        }
                    }

                    markBlockNeedsUpdate(tileentity1.xCoord, tileentity1.yCoord, tileentity1.zCoord);
                }
            }
            while (true);

            addedTileEntityList.clear();
        }

        field_72984_F.endSection();
        field_72984_F.endSection();
    }

    public void addTileEntity(Collection par1Collection)
    {
        if (scanningTileEntities)
        {
            addedTileEntityList.addAll(par1Collection);
        }
        else
        {
            loadedTileEntityList.addAll(par1Collection);
        }
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is currently loaded. Args: entity
     */
    public void updateEntity(Entity par1Entity)
    {
        updateEntityWithOptionalForce(par1Entity, true);
    }

    /**
     * Will update the entity in the world if the chunk the entity is in is currently loaded or its forced to update.
     * Args: entity, forceUpdate
     */
    public void updateEntityWithOptionalForce(Entity par1Entity, boolean par2)
    {
        int i = MathHelper.floor_double(par1Entity.posX);
        int j = MathHelper.floor_double(par1Entity.posZ);
        byte byte0 = 32;

        if (par2 && !checkChunksExist(i - byte0, 0, j - byte0, i + byte0, 0, j + byte0))
        {
            return;
        }

        par1Entity.lastTickPosX = par1Entity.posX;
        par1Entity.lastTickPosY = par1Entity.posY;
        par1Entity.lastTickPosZ = par1Entity.posZ;
        par1Entity.prevRotationYaw = par1Entity.rotationYaw;
        par1Entity.prevRotationPitch = par1Entity.rotationPitch;

        if (par2 && par1Entity.addedToChunk)
        {
            if (par1Entity.ridingEntity != null)
            {
                par1Entity.updateRidden();
            }
            else
            {
                par1Entity.onUpdate();
            }
        }

        field_72984_F.startSection("chunkCheck");

        if (Double.isNaN(par1Entity.posX) || Double.isInfinite(par1Entity.posX))
        {
            par1Entity.posX = par1Entity.lastTickPosX;
        }

        if (Double.isNaN(par1Entity.posY) || Double.isInfinite(par1Entity.posY))
        {
            par1Entity.posY = par1Entity.lastTickPosY;
        }

        if (Double.isNaN(par1Entity.posZ) || Double.isInfinite(par1Entity.posZ))
        {
            par1Entity.posZ = par1Entity.lastTickPosZ;
        }

        if (Double.isNaN(par1Entity.rotationPitch) || Double.isInfinite(par1Entity.rotationPitch))
        {
            par1Entity.rotationPitch = par1Entity.prevRotationPitch;
        }

        if (Double.isNaN(par1Entity.rotationYaw) || Double.isInfinite(par1Entity.rotationYaw))
        {
            par1Entity.rotationYaw = par1Entity.prevRotationYaw;
        }

        int k = MathHelper.floor_double(par1Entity.posX / 16D);
        int l = MathHelper.floor_double(par1Entity.posY / 16D);
        int i1 = MathHelper.floor_double(par1Entity.posZ / 16D);

        if (!par1Entity.addedToChunk || par1Entity.chunkCoordX != k || par1Entity.chunkCoordY != l || par1Entity.chunkCoordZ != i1)
        {
            if (par1Entity.addedToChunk && chunkExists(par1Entity.chunkCoordX, par1Entity.chunkCoordZ))
            {
                getChunkFromChunkCoords(par1Entity.chunkCoordX, par1Entity.chunkCoordZ).removeEntityAtIndex(par1Entity, par1Entity.chunkCoordY);
            }

            if (chunkExists(k, i1))
            {
                par1Entity.addedToChunk = true;
                getChunkFromChunkCoords(k, i1).addEntity(par1Entity);
            }
            else
            {
                par1Entity.addedToChunk = false;
            }
        }

        field_72984_F.endSection();

        if (par2 && par1Entity.addedToChunk && par1Entity.riddenByEntity != null)
        {
            if (par1Entity.riddenByEntity.isDead || par1Entity.riddenByEntity.ridingEntity != par1Entity)
            {
                par1Entity.riddenByEntity.ridingEntity = null;
                par1Entity.riddenByEntity = null;
            }
            else
            {
                updateEntity(par1Entity.riddenByEntity);
            }
        }
    }

    /**
     * Returns true if there are no solid, live entities in the specified AxisAlignedBB
     */
    public boolean checkIfAABBIsClear(AxisAlignedBB par1AxisAlignedBB)
    {
        return func_72917_a(par1AxisAlignedBB, null);
    }

    public boolean func_72917_a(AxisAlignedBB par1AxisAlignedBB, Entity par2Entity)
    {
        List list = getEntitiesWithinAABBExcludingEntity(null, par1AxisAlignedBB);

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Entity entity = (Entity)iterator.next();

            if (!entity.isDead && entity.preventEntitySpawning && entity != par2Entity)
            {
                return false;
            }
        }

        return true;
    }

    public boolean func_72829_c(AxisAlignedBB par1AxisAlignedBB)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        if (par1AxisAlignedBB.minX < 0.0D)
        {
            i--;
        }

        if (par1AxisAlignedBB.minY < 0.0D)
        {
            k--;
        }

        if (par1AxisAlignedBB.minZ < 0.0D)
        {
            i1--;
        }

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];

                    if (block != null)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns if any of the blocks within the aabb are liquids. Args: aabb
     */
    public boolean isAnyLiquid(AxisAlignedBB par1AxisAlignedBB)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        if (par1AxisAlignedBB.minX < 0.0D)
        {
            i--;
        }

        if (par1AxisAlignedBB.minY < 0.0D)
        {
            k--;
        }

        if (par1AxisAlignedBB.minZ < 0.0D)
        {
            i1--;
        }

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];

                    if (block != null && block.blockMaterial.isLiquid())
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Returns whether or not the given bounding box is on fire or not
     */
    public boolean isBoundingBoxBurning(AxisAlignedBB par1AxisAlignedBB)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        if (checkChunksExist(i, k, i1, j, l, j1))
        {
            for (int k1 = i; k1 < j; k1++)
            {
                for (int l1 = k; l1 < l; l1++)
                {
                    for (int i2 = i1; i2 < j1; i2++)
                    {
                        int j2 = getBlockId(k1, l1, i2);

                        if (j2 == Block.fire.blockID || j2 == Block.lavaMoving.blockID || j2 == Block.lavaStill.blockID)
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * handles the acceleration of an object whilst in water. Not sure if it is used elsewhere.
     */
    public boolean handleMaterialAcceleration(AxisAlignedBB par1AxisAlignedBB, Material par2Material, Entity par3Entity)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        if (!checkChunksExist(i, k, i1, j, l, j1))
        {
            return false;
        }

        boolean flag = false;
        Vec3 vec3 = Vec3.func_72437_a().func_72345_a(0.0D, 0.0D, 0.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];

                    if (block == null || block.blockMaterial != par2Material)
                    {
                        continue;
                    }

                    double d1 = (float)(l1 + 1) - BlockFluid.getFluidHeightPercent(getBlockMetadata(k1, l1, i2));

                    if ((double)l >= d1)
                    {
                        flag = true;
                        block.velocityToAddToEntity(this, k1, l1, i2, par3Entity, vec3);
                    }
                }
            }
        }

        if (vec3.lengthVector() > 0.0D)
        {
            vec3 = vec3.normalize();
            double d = 0.014D;
            par3Entity.motionX += vec3.xCoord * d;
            par3Entity.motionY += vec3.yCoord * d;
            par3Entity.motionZ += vec3.zCoord * d;
        }

        return flag;
    }

    /**
     * Returns true if the given bounding box contains the given material
     */
    public boolean isMaterialInBB(AxisAlignedBB par1AxisAlignedBB, Material par2Material)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];

                    if (block != null && block.blockMaterial == par2Material)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * checks if the given AABB is in the material given. Used while swimming.
     */
    public boolean isAABBInMaterial(AxisAlignedBB par1AxisAlignedBB, Material par2Material)
    {
        int i = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int j = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
        int k = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int l = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
        int i1 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int j1 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);

        for (int k1 = i; k1 < j; k1++)
        {
            for (int l1 = k; l1 < l; l1++)
            {
                for (int i2 = i1; i2 < j1; i2++)
                {
                    Block block = Block.blocksList[getBlockId(k1, l1, i2)];

                    if (block == null || block.blockMaterial != par2Material)
                    {
                        continue;
                    }

                    int j2 = getBlockMetadata(k1, l1, i2);
                    double d = l1 + 1;

                    if (j2 < 8)
                    {
                        d = (double)(l1 + 1) - (double)j2 / 8D;
                    }

                    if (d >= par1AxisAlignedBB.minY)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Creates an explosion. Args: entity, x, y, z, strength
     */
    public Explosion createExplosion(Entity par1Entity, double par2, double par4, double par6, float par8)
    {
        return newExplosion(par1Entity, par2, par4, par6, par8, false);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9)
    {
        Explosion explosion = new Explosion(this, par1Entity, par2, par4, par6, par8);
        explosion.isFlaming = par9;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    /**
     * Gets the percentage of real blocks within within a bounding box, along a specified vector.
     */
    public float getBlockDensity(Vec3 par1Vec3, AxisAlignedBB par2AxisAlignedBB)
    {
        double d = 1.0D / ((par2AxisAlignedBB.maxX - par2AxisAlignedBB.minX) * 2D + 1.0D);
        double d1 = 1.0D / ((par2AxisAlignedBB.maxY - par2AxisAlignedBB.minY) * 2D + 1.0D);
        double d2 = 1.0D / ((par2AxisAlignedBB.maxZ - par2AxisAlignedBB.minZ) * 2D + 1.0D);
        int i = 0;
        int j = 0;

        for (float f = 0.0F; f <= 1.0F; f = (float)((double)f + d))
        {
            for (float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1))
            {
                for (float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2))
                {
                    double d3 = par2AxisAlignedBB.minX + (par2AxisAlignedBB.maxX - par2AxisAlignedBB.minX) * (double)f;
                    double d4 = par2AxisAlignedBB.minY + (par2AxisAlignedBB.maxY - par2AxisAlignedBB.minY) * (double)f1;
                    double d5 = par2AxisAlignedBB.minZ + (par2AxisAlignedBB.maxZ - par2AxisAlignedBB.minZ) * (double)f2;

                    if (rayTraceBlocks(Vec3.func_72437_a().func_72345_a(d3, d4, d5), par1Vec3) == null)
                    {
                        i++;
                    }

                    j++;
                }
            }
        }

        return (float)i / (float)j;
    }

    public boolean func_72886_a(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5)
    {
        if (par5 == 0)
        {
            par3--;
        }

        if (par5 == 1)
        {
            par3++;
        }

        if (par5 == 2)
        {
            par4--;
        }

        if (par5 == 3)
        {
            par4++;
        }

        if (par5 == 4)
        {
            par2--;
        }

        if (par5 == 5)
        {
            par2++;
        }

        if (getBlockId(par2, par3, par4) == Block.fire.blockID)
        {
            playAuxSFXAtEntity(par1EntityPlayer, 1004, par2, par3, par4, 0);
            setBlockWithNotify(par2, par3, par4, 0);
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * This string is 'All: (number of loaded entities)' Viewable by press ing F3
     */
    public String getDebugLoadedEntities()
    {
        return (new StringBuilder()).append("All: ").append(loadedEntityList.size()).toString();
    }

    /**
     * Returns the name of the current chunk provider, by calling chunkprovider.makeString()
     */
    public String getProviderName()
    {
        return chunkProvider.makeString();
    }

    /**
     * Returns the TileEntity associated with a given block in X,Y,Z coordinates, or null if no TileEntity exists
     */
    public TileEntity getBlockTileEntity(int par1, int par2, int par3)
    {
        label0:
        {
            TileEntity tileentity;
            label1:
            {
                if (par2 >= 256)
                {
                    return null;
                }

                Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);

                if (chunk == null)
                {
                    break label0;
                }

                tileentity = chunk.getChunkBlockTileEntity(par1 & 0xf, par2, par3 & 0xf);

                if (tileentity != null)
                {
                    break label1;
                }

                Iterator iterator = addedTileEntityList.iterator();
                TileEntity tileentity1;

                do
                {
                    if (!iterator.hasNext())
                    {
                        break label1;
                    }

                    tileentity1 = (TileEntity)iterator.next();
                }
                while (tileentity1.isInvalid() || tileentity1.xCoord != par1 || tileentity1.yCoord != par2 || tileentity1.zCoord != par3);

                tileentity = tileentity1;
            }
            return tileentity;
        }
        return null;
    }

    /**
     * Sets the TileEntity for a given block in X, Y, Z coordinates
     */
    public void setBlockTileEntity(int par1, int par2, int par3, TileEntity par4TileEntity)
    {
        if (par4TileEntity != null && !par4TileEntity.isInvalid())
        {
            if (scanningTileEntities)
            {
                par4TileEntity.xCoord = par1;
                par4TileEntity.yCoord = par2;
                par4TileEntity.zCoord = par3;
                addedTileEntityList.add(par4TileEntity);
            }
            else
            {
                loadedTileEntityList.add(par4TileEntity);
                Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);

                if (chunk != null)
                {
                    chunk.setChunkBlockTileEntity(par1 & 0xf, par2, par3 & 0xf, par4TileEntity);
                }
            }
        }
    }

    /**
     * Removes the TileEntity for a given block in X,Y,Z coordinates
     */
    public void removeBlockTileEntity(int par1, int par2, int par3)
    {
        TileEntity tileentity = getBlockTileEntity(par1, par2, par3);

        if (tileentity != null && scanningTileEntities)
        {
            tileentity.invalidate();
            addedTileEntityList.remove(tileentity);
        }
        else
        {
            if (tileentity != null)
            {
                addedTileEntityList.remove(tileentity);
                loadedTileEntityList.remove(tileentity);
            }

            Chunk chunk = getChunkFromChunkCoords(par1 >> 4, par3 >> 4);

            if (chunk != null)
            {
                chunk.removeChunkBlockTileEntity(par1 & 0xf, par2, par3 & 0xf);
            }
        }
    }

    /**
     * adds tile entity to despawn list (renamed from markEntityForDespawn)
     */
    public void markTileEntityForDespawn(TileEntity par1TileEntity)
    {
        entityRemoval.add(par1TileEntity);
    }

    /**
     * Returns true if the block at the specified coordinates is an opaque cube. Args: x, y, z
     */
    public boolean isBlockOpaqueCube(int par1, int par2, int par3)
    {
        Block block = Block.blocksList[getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }
        else
        {
            return block.isOpaqueCube();
        }
    }

    /**
     * Indicate if a material is a normal solid opaque cube.
     */
    public boolean isBlockNormalCube(int par1, int par2, int par3)
    {
        return Block.isNormalCube(getBlockId(par1, par2, par3));
    }

    public boolean func_72797_t(int par1, int par2, int par3)
    {
        Block block = Block.blocksList[getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }

        if (block.blockMaterial.isOpaque() && block.renderAsNormalBlock())
        {
            return true;
        }

        if (block instanceof BlockStairs)
        {
            return (getBlockMetadata(par1, par2, par3) & 4) == 4;
        }

        if (block instanceof BlockHalfSlab)
        {
            return (getBlockMetadata(par1, par2, par3) & 8) == 8;
        }
        else
        {
            return false;
        }
    }

    /**
     * Checks if the block is a solid, normal cube. If the chunk does not exist, or is not loaded, it returns the
     * boolean parameter.
     */
    public boolean isBlockNormalCubeDefault(int par1, int par2, int par3, boolean par4)
    {
        if (par1 < 0xfe363c80 || par3 < 0xfe363c80 || par1 >= 0x1c9c380 || par3 >= 0x1c9c380)
        {
            return par4;
        }

        Chunk chunk = chunkProvider.provideChunk(par1 >> 4, par3 >> 4);

        if (chunk == null || chunk.isEmpty())
        {
            return par4;
        }

        Block block = Block.blocksList[getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }
        else
        {
            return block.blockMaterial.isOpaque() && block.renderAsNormalBlock();
        }
    }

    /**
     * Called on construction of the World class to setup the initial skylight values
     */
    public void calculateInitialSkylight()
    {
        int i = calculateSkylightSubtracted(1.0F);

        if (i != skylightSubtracted)
        {
            skylightSubtracted = i;
        }
    }

    /**
     * Set which types of mobs are allowed to spawn (peaceful vs hostile).
     */
    public void setAllowedSpawnTypes(boolean par1, boolean par2)
    {
        spawnHostileMobs = par1;
        spawnPeacefulMobs = par2;
    }

    /**
     * Runs a single tick for the world
     */
    public void tick()
    {
        updateWeather();
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
     * Updates all weather states.
     */
    protected void updateWeather()
    {
        if (worldProvider.hasNoSky)
        {
            return;
        }

        if (lastLightningBolt > 0)
        {
            lastLightningBolt--;
        }

        int i = worldInfo.getThunderTime();

        if (i <= 0)
        {
            if (worldInfo.isThundering())
            {
                worldInfo.setThunderTime(rand.nextInt(12000) + 3600);
            }
            else
            {
                worldInfo.setThunderTime(rand.nextInt(0x29040) + 12000);
            }
        }
        else
        {
            i--;
            worldInfo.setThunderTime(i);

            if (i <= 0)
            {
                worldInfo.setThundering(!worldInfo.isThundering());
            }
        }

        int j = worldInfo.getRainTime();

        if (j <= 0)
        {
            if (worldInfo.isRaining())
            {
                worldInfo.setRainTime(rand.nextInt(12000) + 12000);
            }
            else
            {
                worldInfo.setRainTime(rand.nextInt(0x29040) + 12000);
            }
        }
        else
        {
            j--;
            worldInfo.setRainTime(j);

            if (j <= 0)
            {
                worldInfo.setRaining(!worldInfo.isRaining());
            }
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

    public void func_72913_w()
    {
        worldInfo.setRainTime(1);
    }

    protected void func_72903_x()
    {
        activeChunkSet.clear();
        field_72984_F.startSection("buildList");

        for (int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayer entityplayer = (EntityPlayer)playerEntities.get(i);
            int k = MathHelper.floor_double(entityplayer.posX / 16D);
            int i1 = MathHelper.floor_double(entityplayer.posZ / 16D);
            byte byte0 = 7;

            for (int l1 = -byte0; l1 <= byte0; l1++)
            {
                for (int i2 = -byte0; i2 <= byte0; i2++)
                {
                    activeChunkSet.add(new ChunkCoordIntPair(l1 + k, i2 + i1));
                }
            }
        }

        field_72984_F.endSection();

        if (ambientTickCountdown > 0)
        {
            ambientTickCountdown--;
        }

        field_72984_F.startSection("playerCheckLight");

        if (!playerEntities.isEmpty())
        {
            int j = rand.nextInt(playerEntities.size());
            EntityPlayer entityplayer1 = (EntityPlayer)playerEntities.get(j);
            int l = (MathHelper.floor_double(entityplayer1.posX) + rand.nextInt(11)) - 5;
            int j1 = (MathHelper.floor_double(entityplayer1.posY) + rand.nextInt(11)) - 5;
            int k1 = (MathHelper.floor_double(entityplayer1.posZ) + rand.nextInt(11)) - 5;
            updateAllLightTypes(l, j1, k1);
        }

        field_72984_F.endSection();
    }

    protected void func_72941_a(int par1, int par2, Chunk par3Chunk)
    {
        field_72984_F.endStartSection("moodSound");

        if (ambientTickCountdown == 0)
        {
            updateLCG = updateLCG * 3 + 0x3c6ef35f;
            int i = updateLCG >> 2;
            int j = i & 0xf;
            int k = i >> 8 & 0xf;
            int l = i >> 16 & 0x7f;
            int i1 = par3Chunk.getBlockID(j, l, k);
            j += par1;
            k += par2;

            if (i1 == 0 && getFullBlockLightValue(j, l, k) <= rand.nextInt(8) && getSavedLightValue(EnumSkyBlock.Sky, j, l, k) <= 0)
            {
                EntityPlayer entityplayer = getClosestPlayer((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D, 8D);

                if (entityplayer != null && entityplayer.getDistanceSq((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D) > 4D)
                {
                    playSoundEffect((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + rand.nextFloat() * 0.2F);
                    ambientTickCountdown = rand.nextInt(12000) + 6000;
                }
            }
        }

        field_72984_F.endStartSection("checkLight");
        par3Chunk.enqueueRelightChecks();
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    protected void tickBlocksAndAmbiance()
    {
        func_72903_x();
    }

    /**
     * checks to see if a given block is both water and is cold enough to freeze
     */
    public boolean isBlockFreezable(int par1, int par2, int par3)
    {
        return canBlockFreeze(par1, par2, par3, false);
    }

    /**
     * checks to see if a given block is both water and has at least one immediately adjacent non-water block
     */
    public boolean isBlockFreezableNaturally(int par1, int par2, int par3)
    {
        return canBlockFreeze(par1, par2, par3, true);
    }

    /**
     * checks to see if a given block is both water, and cold enough to freeze - if the par4 boolean is set, this will
     * only return true if there is a non-water block immediately adjacent to the specified block
     */
    public boolean canBlockFreeze(int par1, int par2, int par3, boolean par4)
    {
        BiomeGenBase biomegenbase = getBiomeGenForCoords(par1, par3);
        float f = biomegenbase.getFloatTemperature();

        if (f > 0.15F)
        {
            return false;
        }

        if (par2 >= 0 && par2 < 256 && getSavedLightValue(EnumSkyBlock.Block, par1, par2, par3) < 10)
        {
            int i = getBlockId(par1, par2, par3);

            if ((i == Block.waterStill.blockID || i == Block.waterMoving.blockID) && getBlockMetadata(par1, par2, par3) == 0)
            {
                if (!par4)
                {
                    return true;
                }

                boolean flag = true;

                if (flag && getBlockMaterial(par1 - 1, par2, par3) != Material.water)
                {
                    flag = false;
                }

                if (flag && getBlockMaterial(par1 + 1, par2, par3) != Material.water)
                {
                    flag = false;
                }

                if (flag && getBlockMaterial(par1, par2, par3 - 1) != Material.water)
                {
                    flag = false;
                }

                if (flag && getBlockMaterial(par1, par2, par3 + 1) != Material.water)
                {
                    flag = false;
                }

                if (!flag)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Tests whether or not snow can be placed at a given location
     */
    public boolean canSnowAt(int par1, int par2, int par3)
    {
        BiomeGenBase biomegenbase = getBiomeGenForCoords(par1, par3);
        float f = biomegenbase.getFloatTemperature();

        if (f > 0.15F)
        {
            return false;
        }

        if (par2 >= 0 && par2 < 256 && getSavedLightValue(EnumSkyBlock.Block, par1, par2, par3) < 10)
        {
            int i = getBlockId(par1, par2 - 1, par3);
            int j = getBlockId(par1, par2, par3);

            if (j == 0 && Block.snow.canPlaceBlockAt(this, par1, par2, par3) && i != 0 && i != Block.ice.blockID && Block.blocksList[i].blockMaterial.blocksMovement())
            {
                return true;
            }
        }

        return false;
    }

    public void updateAllLightTypes(int par1, int par2, int par3)
    {
        if (!worldProvider.hasNoSky)
        {
            updateLightByType(EnumSkyBlock.Sky, par1, par2, par3);
        }

        updateLightByType(EnumSkyBlock.Block, par1, par2, par3);
    }

    private int computeSkyLightValue(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int i = 0;

        if (canBlockSeeTheSky(par2, par3, par4))
        {
            i = 15;
        }
        else
        {
            if (par6 == 0)
            {
                par6 = 1;
            }

            int j = getSavedLightValue(EnumSkyBlock.Sky, par2 - 1, par3, par4) - par6;
            int k = getSavedLightValue(EnumSkyBlock.Sky, par2 + 1, par3, par4) - par6;
            int l = getSavedLightValue(EnumSkyBlock.Sky, par2, par3 - 1, par4) - par6;
            int i1 = getSavedLightValue(EnumSkyBlock.Sky, par2, par3 + 1, par4) - par6;
            int j1 = getSavedLightValue(EnumSkyBlock.Sky, par2, par3, par4 - 1) - par6;
            int k1 = getSavedLightValue(EnumSkyBlock.Sky, par2, par3, par4 + 1) - par6;

            if (j > i)
            {
                i = j;
            }

            if (k > i)
            {
                i = k;
            }

            if (l > i)
            {
                i = l;
            }

            if (i1 > i)
            {
                i = i1;
            }

            if (j1 > i)
            {
                i = j1;
            }

            if (k1 > i)
            {
                i = k1;
            }
        }

        return i;
    }

    private int computeBlockLightValue(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int i = Block.lightValue[par5];
        int j = getSavedLightValue(EnumSkyBlock.Block, par2 - 1, par3, par4) - par6;
        int k = getSavedLightValue(EnumSkyBlock.Block, par2 + 1, par3, par4) - par6;
        int l = getSavedLightValue(EnumSkyBlock.Block, par2, par3 - 1, par4) - par6;
        int i1 = getSavedLightValue(EnumSkyBlock.Block, par2, par3 + 1, par4) - par6;
        int j1 = getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4 - 1) - par6;
        int k1 = getSavedLightValue(EnumSkyBlock.Block, par2, par3, par4 + 1) - par6;

        if (j > i)
        {
            i = j;
        }

        if (k > i)
        {
            i = k;
        }

        if (l > i)
        {
            i = l;
        }

        if (i1 > i)
        {
            i = i1;
        }

        if (j1 > i)
        {
            i = j1;
        }

        if (k1 > i)
        {
            i = k1;
        }

        return i;
    }

    public void updateLightByType(EnumSkyBlock par1EnumSkyBlock, int par2, int par3, int par4)
    {
        if (!doChunksNearChunkExist(par2, par3, par4, 17))
        {
            return;
        }

        int i = 0;
        int j = 0;
        field_72984_F.startSection("getBrightness");
        int k = getSavedLightValue(par1EnumSkyBlock, par2, par3, par4);
        int l = 0;
        int i1 = getBlockId(par2, par3, par4);
        int l1 = getBlockLightOpacity(par2, par3, par4);

        if (l1 == 0)
        {
            l1 = 1;
        }

        int k2 = 0;

        if (par1EnumSkyBlock == EnumSkyBlock.Sky)
        {
            k2 = computeSkyLightValue(k, par2, par3, par4, i1, l1);
        }
        else
        {
            k2 = computeBlockLightValue(k, par2, par3, par4, i1, l1);
        }

        l = k2;

        if (l > k)
        {
            lightUpdateBlockList[j++] = 0x20820;
        }
        else if (l < k)
        {
            if (par1EnumSkyBlock == EnumSkyBlock.Block);

            lightUpdateBlockList[j++] = 0x20820 + (k << 18);

            do
            {
                if (i >= j)
                {
                    break;
                }

                int j1 = lightUpdateBlockList[i++];
                int i2 = ((j1 & 0x3f) - 32) + par2;
                int l2 = ((j1 >> 6 & 0x3f) - 32) + par3;
                int j3 = ((j1 >> 12 & 0x3f) - 32) + par4;
                int l3 = j1 >> 18 & 0xf;
                int j4 = getSavedLightValue(par1EnumSkyBlock, i2, l2, j3);

                if (j4 == l3)
                {
                    setLightValue(par1EnumSkyBlock, i2, l2, j3, 0);

                    if (l3 > 0)
                    {
                        int i5 = i2 - par2;
                        int k5 = l2 - par3;
                        int i6 = j3 - par4;

                        if (i5 < 0)
                        {
                            i5 = -i5;
                        }

                        if (k5 < 0)
                        {
                            k5 = -k5;
                        }

                        if (i6 < 0)
                        {
                            i6 = -i6;
                        }

                        if (i5 + k5 + i6 < 17)
                        {
                            int k6 = 0;

                            while (k6 < 6)
                            {
                                int i7 = (k6 % 2) * 2 - 1;
                                int k7 = i2 + (((k6 / 2) % 3) / 2) * i7;
                                int l7 = l2 + (((k6 / 2 + 1) % 3) / 2) * i7;
                                int i8 = j3 + (((k6 / 2 + 2) % 3) / 2) * i7;
                                int k4 = getSavedLightValue(par1EnumSkyBlock, k7, l7, i8);
                                int j8 = Block.lightOpacity[getBlockId(k7, l7, i8)];

                                if (j8 == 0)
                                {
                                    j8 = 1;
                                }

                                if (k4 == l3 - j8 && j < lightUpdateBlockList.length)
                                {
                                    lightUpdateBlockList[j++] = (k7 - par2) + 32 + ((l7 - par3) + 32 << 6) + ((i8 - par4) + 32 << 12) + (l3 - j8 << 18);
                                }

                                k6++;
                            }
                        }
                    }
                }
            }
            while (true);

            i = 0;
        }

        field_72984_F.endSection();
        field_72984_F.startSection("tcp < tcc");

        do
        {
            if (i >= j)
            {
                break;
            }

            int k1 = lightUpdateBlockList[i++];
            int j2 = ((k1 & 0x3f) - 32) + par2;
            int i3 = ((k1 >> 6 & 0x3f) - 32) + par3;
            int k3 = ((k1 >> 12 & 0x3f) - 32) + par4;
            int i4 = getSavedLightValue(par1EnumSkyBlock, j2, i3, k3);
            int l4 = getBlockId(j2, i3, k3);
            int j5 = Block.lightOpacity[l4];

            if (j5 == 0)
            {
                j5 = 1;
            }

            int l5 = 0;

            if (par1EnumSkyBlock == EnumSkyBlock.Sky)
            {
                l5 = computeSkyLightValue(i4, j2, i3, k3, l4, j5);
            }
            else
            {
                l5 = computeBlockLightValue(i4, j2, i3, k3, l4, j5);
            }

            if (l5 != i4)
            {
                setLightValue(par1EnumSkyBlock, j2, i3, k3, l5);

                if (l5 > i4)
                {
                    int j6 = j2 - par2;
                    int l6 = i3 - par3;
                    int j7 = k3 - par4;

                    if (j6 < 0)
                    {
                        j6 = -j6;
                    }

                    if (l6 < 0)
                    {
                        l6 = -l6;
                    }

                    if (j7 < 0)
                    {
                        j7 = -j7;
                    }

                    if (j6 + l6 + j7 < 17 && j < lightUpdateBlockList.length - 6)
                    {
                        if (getSavedLightValue(par1EnumSkyBlock, j2 - 1, i3, k3) < l5)
                        {
                            lightUpdateBlockList[j++] = (j2 - 1 - par2) + 32 + ((i3 - par3) + 32 << 6) + ((k3 - par4) + 32 << 12);
                        }

                        if (getSavedLightValue(par1EnumSkyBlock, j2 + 1, i3, k3) < l5)
                        {
                            lightUpdateBlockList[j++] = ((j2 + 1) - par2) + 32 + ((i3 - par3) + 32 << 6) + ((k3 - par4) + 32 << 12);
                        }

                        if (getSavedLightValue(par1EnumSkyBlock, j2, i3 - 1, k3) < l5)
                        {
                            lightUpdateBlockList[j++] = (j2 - par2) + 32 + ((i3 - 1 - par3) + 32 << 6) + ((k3 - par4) + 32 << 12);
                        }

                        if (getSavedLightValue(par1EnumSkyBlock, j2, i3 + 1, k3) < l5)
                        {
                            lightUpdateBlockList[j++] = (j2 - par2) + 32 + (((i3 + 1) - par3) + 32 << 6) + ((k3 - par4) + 32 << 12);
                        }

                        if (getSavedLightValue(par1EnumSkyBlock, j2, i3, k3 - 1) < l5)
                        {
                            lightUpdateBlockList[j++] = (j2 - par2) + 32 + ((i3 - par3) + 32 << 6) + ((k3 - 1 - par4) + 32 << 12);
                        }

                        if (getSavedLightValue(par1EnumSkyBlock, j2, i3, k3 + 1) < l5)
                        {
                            lightUpdateBlockList[j++] = (j2 - par2) + 32 + ((i3 - par3) + 32 << 6) + (((k3 + 1) - par4) + 32 << 12);
                        }
                    }
                }
            }
        }
        while (true);

        field_72984_F.endSection();
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
     * Will get all entities within the specified AABB excluding the one passed into it. Args: entityToExclude, aabb
     */
    public List getEntitiesWithinAABBExcludingEntity(Entity par1Entity, AxisAlignedBB par2AxisAlignedBB)
    {
        entitiesWithinAABBExcludingEntity.clear();
        int i = MathHelper.floor_double((par2AxisAlignedBB.minX - 2D) / 16D);
        int j = MathHelper.floor_double((par2AxisAlignedBB.maxX + 2D) / 16D);
        int k = MathHelper.floor_double((par2AxisAlignedBB.minZ - 2D) / 16D);
        int l = MathHelper.floor_double((par2AxisAlignedBB.maxZ + 2D) / 16D);

        for (int i1 = i; i1 <= j; i1++)
        {
            for (int j1 = k; j1 <= l; j1++)
            {
                if (chunkExists(i1, j1))
                {
                    getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(par1Entity, par2AxisAlignedBB, entitiesWithinAABBExcludingEntity);
                }
            }
        }

        return entitiesWithinAABBExcludingEntity;
    }

    /**
     * Returns all entities of the specified class type which intersect with the AABB. Args: entityClass, aabb
     */
    public List getEntitiesWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB)
    {
        int i = MathHelper.floor_double((par2AxisAlignedBB.minX - 2D) / 16D);
        int j = MathHelper.floor_double((par2AxisAlignedBB.maxX + 2D) / 16D);
        int k = MathHelper.floor_double((par2AxisAlignedBB.minZ - 2D) / 16D);
        int l = MathHelper.floor_double((par2AxisAlignedBB.maxZ + 2D) / 16D);
        ArrayList arraylist = new ArrayList();

        for (int i1 = i; i1 <= j; i1++)
        {
            for (int j1 = k; j1 <= l; j1++)
            {
                if (chunkExists(i1, j1))
                {
                    getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAAAB(par1Class, par2AxisAlignedBB, arraylist);
                }
            }
        }

        return arraylist;
    }

    public Entity findNearestEntityWithinAABB(Class par1Class, AxisAlignedBB par2AxisAlignedBB, Entity par3Entity)
    {
        List list = getEntitiesWithinAABB(par1Class, par2AxisAlignedBB);
        Entity entity = null;
        double d = Double.MAX_VALUE;
        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity1 = (Entity)iterator.next();

            if (entity1 != par3Entity)
            {
                double d1 = par3Entity.getDistanceSqToEntity(entity1);

                if (d1 <= d)
                {
                    entity = entity1;
                    d = d1;
                }
            }
        }
        while (true);

        return entity;
    }

    /**
     * Accessor for world Loaded Entity List
     */
    public List getLoadedEntityList()
    {
        return loadedEntityList;
    }

    /**
     * marks the chunk that contains this tilentity as modified and then calls worldAccesses.doNothingWithTileEntity
     */
    public void updateTileEntityChunkAndDoNothing(int par1, int par2, int par3, TileEntity par4TileEntity)
    {
        if (blockExists(par1, par2, par3))
        {
            getChunkFromBlockCoords(par1, par3).setChunkModified();
        }
    }

    /**
     * Counts how many entities of an entity class exist in the world. Args: entityClass
     */
    public int countEntities(Class par1Class)
    {
        int i = 0;

        for (int j = 0; j < loadedEntityList.size(); j++)
        {
            Entity entity = (Entity)loadedEntityList.get(j);

            if (par1Class.isAssignableFrom(entity.getClass()))
            {
                i++;
            }
        }

        return i;
    }

    /**
     * adds entities to the loaded entities list, and loads thier skins.
     */
    public void addLoadedEntities(List par1List)
    {
        loadedEntityList.addAll(par1List);

        for (int i = 0; i < par1List.size(); i++)
        {
            obtainEntitySkin((Entity)par1List.get(i));
        }
    }

    /**
     * Adds a list of entities to be unloaded on the next pass of World.updateEntities()
     */
    public void unloadEntities(List par1List)
    {
        unloadedEntityList.addAll(par1List);
    }

    public boolean func_72931_a(int par1, int par2, int par3, int par4, boolean par5, int par6, Entity par7Entity)
    {
        int i = getBlockId(par2, par3, par4);
        Block block = Block.blocksList[i];
        Block block1 = Block.blocksList[par1];
        AxisAlignedBB axisalignedbb = block1.getCollisionBoundingBoxFromPool(this, par2, par3, par4);

        if (par5)
        {
            axisalignedbb = null;
        }

        if (axisalignedbb != null && !func_72917_a(axisalignedbb, par7Entity))
        {
            return false;
        }

        if (block != null && (block == Block.waterMoving || block == Block.waterStill || block == Block.lavaMoving || block == Block.lavaStill || block == Block.fire || block.blockMaterial.isGroundCover()))
        {
            block = null;
        }

        return par1 > 0 && block == null && block1.canPlaceBlockOnSide(this, par2, par3, par4, par6);
    }

    public PathEntity getPathEntityToEntity(Entity par1Entity, Entity par2Entity, float par3, boolean par4, boolean par5, boolean par6, boolean par7)
    {
        field_72984_F.startSection("pathfind");
        int i = MathHelper.floor_double(par1Entity.posX);
        int j = MathHelper.floor_double(par1Entity.posY + 1.0D);
        int k = MathHelper.floor_double(par1Entity.posZ);
        int l = (int)(par3 + 16F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2);
        PathEntity pathentity = (new PathFinder(chunkcache, par4, par5, par6, par7)).createEntityPathTo(par1Entity, par2Entity, par3);
        field_72984_F.endSection();
        return pathentity;
    }

    public PathEntity getEntityPathToXYZ(Entity par1Entity, int par2, int par3, int par4, float par5, boolean par6, boolean par7, boolean par8, boolean par9)
    {
        field_72984_F.startSection("pathfind");
        int i = MathHelper.floor_double(par1Entity.posX);
        int j = MathHelper.floor_double(par1Entity.posY);
        int k = MathHelper.floor_double(par1Entity.posZ);
        int l = (int)(par5 + 8F);
        int i1 = i - l;
        int j1 = j - l;
        int k1 = k - l;
        int l1 = i + l;
        int i2 = j + l;
        int j2 = k + l;
        ChunkCache chunkcache = new ChunkCache(this, i1, j1, k1, l1, i2, j2);
        PathEntity pathentity = (new PathFinder(chunkcache, par6, par7, par8, par9)).createEntityPathTo(par1Entity, par2, par3, par4, par5);
        field_72984_F.endSection();
        return pathentity;
    }

    /**
     * Is this block powering in the specified direction Args: x, y, z, direction
     */
    public boolean isBlockProvidingPowerTo(int par1, int par2, int par3, int par4)
    {
        int i = getBlockId(par1, par2, par3);

        if (i == 0)
        {
            return false;
        }
        else
        {
            return Block.blocksList[i].isIndirectlyPoweringTo(this, par1, par2, par3, par4);
        }
    }

    /**
     * Whether one of the neighboring blocks is putting power into this block. Args: x, y, z
     */
    public boolean isBlockGettingPowered(int par1, int par2, int par3)
    {
        if (isBlockProvidingPowerTo(par1, par2 - 1, par3, 0))
        {
            return true;
        }

        if (isBlockProvidingPowerTo(par1, par2 + 1, par3, 1))
        {
            return true;
        }

        if (isBlockProvidingPowerTo(par1, par2, par3 - 1, 2))
        {
            return true;
        }

        if (isBlockProvidingPowerTo(par1, par2, par3 + 1, 3))
        {
            return true;
        }

        if (isBlockProvidingPowerTo(par1 - 1, par2, par3, 4))
        {
            return true;
        }

        return isBlockProvidingPowerTo(par1 + 1, par2, par3, 5);
    }

    /**
     * Is a block next to you getting powered (if its an attachable block) or is it providing power directly to you.
     * Args: x, y, z, direction
     */
    public boolean isBlockIndirectlyProvidingPowerTo(int par1, int par2, int par3, int par4)
    {
        if (isBlockNormalCube(par1, par2, par3))
        {
            return isBlockGettingPowered(par1, par2, par3);
        }

        int i = getBlockId(par1, par2, par3);

        if (i == 0)
        {
            return false;
        }
        else
        {
            return Block.blocksList[i].isPoweringTo(this, par1, par2, par3, par4);
        }
    }

    /**
     * Used to see if one of the blocks next to you or your block is getting power from a neighboring block. Used by
     * items like TNT or Doors so they don't have redstone going straight into them.  Args: x, y, z
     */
    public boolean isBlockIndirectlyGettingPowered(int par1, int par2, int par3)
    {
        if (isBlockIndirectlyProvidingPowerTo(par1, par2 - 1, par3, 0))
        {
            return true;
        }

        if (isBlockIndirectlyProvidingPowerTo(par1, par2 + 1, par3, 1))
        {
            return true;
        }

        if (isBlockIndirectlyProvidingPowerTo(par1, par2, par3 - 1, 2))
        {
            return true;
        }

        if (isBlockIndirectlyProvidingPowerTo(par1, par2, par3 + 1, 3))
        {
            return true;
        }

        if (isBlockIndirectlyProvidingPowerTo(par1 - 1, par2, par3, 4))
        {
            return true;
        }

        return isBlockIndirectlyProvidingPowerTo(par1 + 1, par2, par3, 5);
    }

    /**
     * Gets the closest player to the entity within the specified distance (if distance is less than 0 then ignored).
     * Args: entity, dist
     */
    public EntityPlayer getClosestPlayerToEntity(Entity par1Entity, double par2)
    {
        return getClosestPlayer(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par2);
    }

    /**
     * Gets the closest player to the point within the specified distance (distance can be set to less than 0 to not
     * limit the distance). Args: x, y, z, dist
     */
    public EntityPlayer getClosestPlayer(double par1, double par3, double par5, double par7)
    {
        double d = -1D;
        EntityPlayer entityplayer = null;

        for (int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)playerEntities.get(i);
            double d1 = entityplayer1.getDistanceSq(par1, par3, par5);

            if ((par7 < 0.0D || d1 < par7 * par7) && (d == -1D || d1 < d))
            {
                d = d1;
                entityplayer = entityplayer1;
            }
        }

        return entityplayer;
    }

    /**
     * Returns the closest vulnerable player to this entity within the given radius, or null if none is found
     */
    public EntityPlayer getClosestVulnerablePlayerToEntity(Entity par1Entity, double par2)
    {
        return getClosestVulnerablePlayer(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par2);
    }

    /**
     * Returns the closest vulnerable player within the given radius, or null if none is found.
     */
    public EntityPlayer getClosestVulnerablePlayer(double par1, double par3, double par5, double par7)
    {
        double d = -1D;
        EntityPlayer entityplayer = null;

        for (int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)playerEntities.get(i);

            if (entityplayer1.capabilities.disableDamage)
            {
                continue;
            }

            double d1 = entityplayer1.getDistanceSq(par1, par3, par5);

            if ((par7 < 0.0D || d1 < par7 * par7) && (d == -1D || d1 < d))
            {
                d = d1;
                entityplayer = entityplayer1;
            }
        }

        return entityplayer;
    }

    /**
     * Find a player by name in this world.
     */
    public EntityPlayer getPlayerEntityByName(String par1Str)
    {
        for (int i = 0; i < playerEntities.size(); i++)
        {
            if (par1Str.equals(((EntityPlayer)playerEntities.get(i)).username))
            {
                return (EntityPlayer)playerEntities.get(i);
            }
        }

        return null;
    }

    /**
     * If on MP, sends a quitting packet.
     */
    public void sendQuittingDisconnectingPacket()
    {
    }

    /**
     * Checks whether the session lock file was modified by another process
     */
    public void checkSessionLock() throws MinecraftException
    {
        saveHandler.checkSessionLock();
    }

    /**
     * Sets the world time.
     */
    public void setWorldTime(long par1)
    {
        worldInfo.setWorldTime(par1);
    }

    /**
     * Retrieve the world seed from level.dat
     */
    public long getSeed()
    {
        return worldInfo.getSeed();
    }

    public long getWorldTime()
    {
        return worldInfo.getWorldTime();
    }

    /**
     * Returns the coordinates of the spawn point
     */
    public ChunkCoordinates getSpawnPoint()
    {
        return new ChunkCoordinates(worldInfo.getSpawnX(), worldInfo.getSpawnY(), worldInfo.getSpawnZ());
    }

    public void func_72950_A(int par1, int par2, int par3)
    {
        worldInfo.setSpawnPosition(par1, par2, par3);
    }

    /**
     * spwans an entity and loads surrounding chunks
     */
    public void joinEntityInSurroundings(Entity par1Entity)
    {
        int i = MathHelper.floor_double(par1Entity.posX / 16D);
        int j = MathHelper.floor_double(par1Entity.posZ / 16D);
        byte byte0 = 2;

        for (int k = i - byte0; k <= i + byte0; k++)
        {
            for (int l = j - byte0; l <= j + byte0; l++)
            {
                getChunkFromChunkCoords(k, l);
            }
        }

        if (!loadedEntityList.contains(par1Entity))
        {
            loadedEntityList.add(par1Entity);
        }
    }

    /**
     * Called when checking if a certain block can be mined or not. The 'spawn safe zone' check is located here.
     */
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int i)
    {
        return true;
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    public void setEntityState(Entity entity, byte byte0)
    {
    }

    /**
     * gets the IChunkProvider this world uses.
     */
    public IChunkProvider getChunkProvider()
    {
        return chunkProvider;
    }

    /**
     * Calls receiveClientEvent of the tile entity at the given location on the server and all nearby clients with the
     * given event number and parameter. Args: X, Y, Z, event number, parameter
     */
    public void sendClientEvent(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        if (par4 > 0)
        {
            Block.blocksList[par4].receiveClientEvent(this, par1, par2, par3, par5, par6);
        }
    }

    /**
     * Returns this world's current save handler
     */
    public ISaveHandler getSaveHandler()
    {
        return saveHandler;
    }

    /**
     * Gets the World's WorldInfo instance
     */
    public WorldInfo getWorldInfo()
    {
        return worldInfo;
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    public void updateAllPlayersSleepingFlag()
    {
    }

    public float getWeightedThunderStrength(float par1)
    {
        return (prevThunderingStrength + (thunderingStrength - prevThunderingStrength) * par1) * getRainStrength(par1);
    }

    /**
     * Not sure about this actually. Reverting this one myself.
     */
    public float getRainStrength(float par1)
    {
        return prevRainingStrength + (rainingStrength - prevRainingStrength) * par1;
    }

    public void setRainStrength(float par1)
    {
        prevRainingStrength = par1;
        rainingStrength = par1;
    }

    /**
     * Returns true if the current thunder strength (weighted with the rain strength) is greater than 0.9
     */
    public boolean isThundering()
    {
        return (double)getWeightedThunderStrength(1.0F) > 0.90000000000000002D;
    }

    /**
     * Returns true if the current rain strength is greater than 0.2
     */
    public boolean isRaining()
    {
        return (double)getRainStrength(1.0F) > 0.20000000000000001D;
    }

    public boolean canLightningStrikeAt(int par1, int par2, int par3)
    {
        if (!isRaining())
        {
            return false;
        }

        if (!canBlockSeeTheSky(par1, par2, par3))
        {
            return false;
        }

        if (getPrecipitationHeight(par1, par3) > par2)
        {
            return false;
        }

        BiomeGenBase biomegenbase = getBiomeGenForCoords(par1, par3);

        if (biomegenbase.getEnableSnow())
        {
            return false;
        }
        else
        {
            return biomegenbase.canSpawnLightningBolt();
        }
    }

    /**
     * Checks to see if the biome rainfall values for a given x,y,z coordinate set are extremely high
     */
    public boolean isBlockHighHumidity(int par1, int par2, int par3)
    {
        BiomeGenBase biomegenbase = getBiomeGenForCoords(par1, par3);
        return biomegenbase.isHighHumidity();
    }

    /**
     * Assigns the given String id to the given MapDataBase using the MapStorage, removing any existing ones of the same
     * id.
     */
    public void setItemData(String par1Str, WorldSavedData par2WorldSavedData)
    {
        mapStorage.setData(par1Str, par2WorldSavedData);
    }

    /**
     * Loads an existing MapDataBase corresponding to the given String id from disk using the MapStorage, instantiating
     * the given Class, or returns null if none such file exists. args: Class to instantiate, String dataid
     */
    public WorldSavedData loadItemData(Class par1Class, String par2Str)
    {
        return mapStorage.loadData(par1Class, par2Str);
    }

    /**
     * Returns an unique new data id from the MapStorage for the given prefix and saves the idCounts map to the
     * 'idcounts' file.
     */
    public int getUniqueDataId(String par1Str)
    {
        return mapStorage.getUniqueDataId(par1Str);
    }

    /**
     * See description for playAuxSFX.
     */
    public void playAuxSFX(int par1, int par2, int par3, int par4, int par5)
    {
        playAuxSFXAtEntity(null, par1, par2, par3, par4, par5);
    }

    /**
     * See description for playAuxSFX.
     */
    public void playAuxSFXAtEntity(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6)
    {
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playAuxSFX(par1EntityPlayer, par2, par3, par4, par5, par6);
        }
    }

    /**
     * Returns current world height.
     */
    public int getHeight()
    {
        return 256;
    }

    public int func_72940_L()
    {
        return worldProvider.hasNoSky ? 128 : 256;
    }

    /**
     * puts the World Random seed to a specific state dependant on the inputs
     */
    public Random setRandomSeed(int par1, int par2, int par3)
    {
        long l = (long)par1 * 0x4f9939f508L + (long)par2 * 0x1ef1565bd5L + getWorldInfo().getSeed() + (long)par3;
        rand.setSeed(l);
        return rand;
    }

    /**
     * Updates lighting. Returns true if there are more lighting updates to update
     */
    public boolean updatingLighting()
    {
        return false;
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    public ChunkPosition findClosestStructure(String par1Str, int par2, int par3, int par4)
    {
        return getChunkProvider().findClosestStructure(this, par1Str, par2, par3, par4);
    }

    public boolean func_72806_N()
    {
        return false;
    }

    /**
     * Returns horizon height for use in rendering the sky.
     */
    public double getHorizon()
    {
        return worldInfo.getTerrainType() != WorldType.FLAT ? 63D : 0.0D;
    }

    public CrashReport func_72914_a(CrashReport par1CrashReport)
    {
        par1CrashReport.func_71500_a((new StringBuilder()).append("World ").append(worldInfo.getWorldName()).append(" Entities").toString(), new CallableLvl1(this));
        par1CrashReport.func_71500_a((new StringBuilder()).append("World ").append(worldInfo.getWorldName()).append(" Players").toString(), new CallableLvl2(this));
        par1CrashReport.func_71500_a((new StringBuilder()).append("World ").append(worldInfo.getWorldName()).append(" Chunk Stats").toString(), new CallableLvl3(this));
        return par1CrashReport;
    }

    public void func_72888_f(int par1, int par2, int par3, int par4, int par5)
    {
        IWorldAccess iworldaccess;

        for (Iterator iterator = worldAccesses.iterator(); iterator.hasNext(); iworldaccess.func_72705_a(par1, par2, par3, par4, par5))
        {
            iworldaccess = (IWorldAccess)iterator.next();
        }
    }
}
