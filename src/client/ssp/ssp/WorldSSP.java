package net.minecraft.src.ssp;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFluid;
import net.minecraft.src.CrashReport;
import net.minecraft.src.CrashReportCategory;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.Explosion;
import net.minecraft.src.ExtendedBlockStorage;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IChunkLoader;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.IWorldAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Minecraft;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NextTickListEntry;
import net.minecraft.src.Profiler;
import net.minecraft.src.SpawnerAnimals;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.ThreadedFileIOBase;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.WeightedRandom;
import net.minecraft.src.WeightedRandomChestContent;
import net.minecraft.src.World;
import net.minecraft.src.WorldChunkManager;
import net.minecraft.src.WorldClient;
import net.minecraft.src.WorldGeneratorBonusChest;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldSettings;

public class WorldSSP extends WorldClient implements IBlockAccess
{
    /**
     * TreeSet of scheduled ticks which is used as a priority queue for the ticks
     */
    protected TreeSet scheduledTickTreeSet;

    /** Set of scheduled ticks (used for checking if a tick already exists) */
    protected Set scheduledTickSet;

    /** Entities marked for removal. */
    protected List entityRemoval;
    protected long cloudColour;

    /**
     * Contains a timestamp from when the World object was created. Is used in the session.lock file
     */
    public long lockTimestamp;
    protected int autosavePeriod;

    /**
     * Used to differentiate between a newly generated world and an already existing world.
     */
    public boolean isNewWorld;

    /**
     * A flag indicating whether or not all players in the world are sleeping.
     */
    protected boolean allPlayersSleeping;
    protected ArrayList collidingBoundingBoxes;
    protected boolean scanningTileEntities;

    /** number of ticks until the next random ambients play */
    protected int ambientTickCountdown;

    /**
     * entities within AxisAlignedBB excluding one, set and returned in getEntitiesWithinAABBExcludingEntity(Entity
     * var1, AxisAlignedBB var2)
     */
    protected List entitiesWithinAABBExcludingEntity;
    protected static final WeightedRandomChestContent bonusChestContent[];

    public double field_35467_J;
    public double field_35468_K;
    public double field_35465_L;

    /** true while the world is editing blocks */
    public boolean editingBlocks;

    protected final SpawnerAnimals animalSpawner = new SpawnerAnimals();

    public WorldSSP(ISaveHandler par1ISaveHandler, String par2Str, WorldProvider par3WorldProvider, WorldSettings par4WorldSettings, Profiler p, ILogAgent log)
    {
        super(par3WorldProvider, par1ISaveHandler, par4WorldSettings, par2Str, p, log);
        editingBlocks = false;
        scheduledTickTreeSet = new TreeSet();
        scheduledTickSet = new HashSet();
        entityRemoval = new ArrayList();
        cloudColour = 0xffffffL;
        lockTimestamp = System.currentTimeMillis();
        autosavePeriod = 40;
        isNewWorld = false;
        collidingBoundingBoxes = new ArrayList();
        ambientTickCountdown = rand.nextInt(12000);
        entitiesWithinAABBExcludingEntity = new ArrayList();
        worldInfo = new WorldInfo(par4WorldSettings, par2Str);
        par3WorldProvider.registerWorld(this);
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    public WorldSSP(WorldSSP par1World, WorldProvider par2WorldProvider, Profiler p, ILogAgent log)
    {
        super(par2WorldProvider, par1World.saveHandler, new WorldSettings(par1World.getWorldInfo()), par1World.getWorldInfo().getWorldName(), p, log);
        editingBlocks = false;
        scheduledTickTreeSet = new TreeSet();
        scheduledTickSet = new HashSet();
        entityRemoval = new ArrayList();
        cloudColour = 0xffffffL;
        lockTimestamp = System.currentTimeMillis();
        autosavePeriod = 40;
        isNewWorld = false;
        collidingBoundingBoxes = new ArrayList();
        ambientTickCountdown = rand.nextInt(12000);
        entitiesWithinAABBExcludingEntity = new ArrayList();
        lockTimestamp = par1World.lockTimestamp;
        worldInfo = new WorldInfo(par1World.worldInfo);
        par2WorldProvider.registerWorld(this);
        calculateInitialSkylight();
        calculateInitialWeather();
    }

    public WorldSSP(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, Profiler p, ILogAgent log)
    {
        this(par1ISaveHandler, par2Str, par3WorldSettings, ((WorldProvider)(null)), p, log);
    }

    public WorldSSP(ISaveHandler par1ISaveHandler, String par2Str, WorldSettings par3WorldSettings, WorldProvider par4WorldProvider, Profiler p, ILogAgent log)
    {
        super(par4WorldProvider, par1ISaveHandler, par3WorldSettings, par2Str, p, log);
        scheduledTickTreeSet = new TreeSet();
        scheduledTickSet = new HashSet();
        entityRemoval = new ArrayList();
        cloudColour = 0xffffffL;
        lockTimestamp = System.currentTimeMillis();
        autosavePeriod = 40;
        isNewWorld = false;
        collidingBoundingBoxes = new ArrayList();
        ambientTickCountdown = rand.nextInt(12000);
        entitiesWithinAABBExcludingEntity = new ArrayList();
        isNewWorld = par1ISaveHandler.loadWorldInfo() == null;

        provider.registerWorld(this);

        if (isNewWorld)
        {
            if (getClass() == WorldSSP.class){
                generateSpawnPoint(par3WorldSettings);
            }
        }

        calculateInitialSkylight();
        calculateInitialWeather();
    }

    /**
     * Creates the bonus chest in the world.
     */
    protected void createBonusChest()
    {
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
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    @Override
    protected IChunkProvider createChunkProvider()
    {
        IChunkLoader ichunkloader = saveHandler.getChunkLoader(provider);
        return new ChunkProvider(this, ichunkloader, provider.createChunkGenerator());
    }

    /**
     * Finds an initial spawn location upon creating a new world
     */
    protected void generateSpawnPoint(WorldSettings par1WorldSettings)
    {
        if (!provider.canRespawnHere())
        {
            worldInfo.setSpawnPosition(0, provider.getAverageGroundLevel(), 0);
            return;
        }

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

        if (par1WorldSettings.isBonusChestEnabled())
        {
            createBonusChest();
        }
    }

    /**
     * Gets the hard-coded portal location to use when entering this dimension
     */
    public ChunkCoordinates getEntrancePortalLocation()
    {
        return provider.getEntrancePortalLocation();
    }

    /**
     * Sets a new spawn location by finding an uncovered block at a random (x,z) location in the chunk.
     */
    @Override
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
     * spawns a player, load data from level.dat if needed and loads surrounding chunks
     */
    public void spawnPlayerWithLoadedChunks(EntityPlayer par1EntityPlayer)
    {
        try
        {
            NBTTagCompound nbttagcompound = worldInfo.getPlayerNBTTagCompound();

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
     * Saves the data for this World. If passed true, then only save up to 2 chunks, otherwise, save all chunks.
     */
    public void saveWorld(boolean par1, IProgressUpdate par2IProgressUpdate)
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
     * Saves the global data associated with this World
     */
    protected void saveLevel()
    {
        try{
            checkSessionLock();
        }catch(MinecraftException ex){
            ex.printStackTrace();
        }
        saveHandler.saveWorldInfoAndPlayer(worldInfo, playerEntities);
        worldInfo.setSaveVersion(19133);
        mapStorage.saveAllData();
    }

    /**
     * Saves the world and all chunk data without displaying any progress message. If passed 0, then save player info
     * and metadata as well.
     */
    public boolean quickSaveWorld(int par1)
    {
        if (!chunkProvider.canSave())
        {
            return true;
        }

        if (par1 == 0)
        {
            saveLevel();
        }

        return chunkProvider.saveChunks(false, null);
    }

    public void commandSetTime(long par1, boolean reset)
    {
        long l = par1 - worldInfo.getWorldTotalTime();

        for (Iterator iterator = scheduledTickSet.iterator(); iterator.hasNext();)
        {
            NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();
            nextticklistentry.scheduledTime += l;
        }

        if (reset){
            field_35467_J = 0D;
            field_35468_K = 0D;
        }

        setWorldTime(par1);
        func_82738_a(par1);
    }

    /**
     * Returns true if the block at the specified coordinates is empty
     */
    @Override
    public boolean isAirBlock(int par1, int par2, int par3)
    {
        return getBlockId(par1, par2, par3) == 0;
    }

    /**
     * Returns whether a block exists at world coordinates x, y, z
     */
    @Override
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
    @Override
    public boolean doChunksNearChunkExist(int par1, int par2, int par3, int par4)
    {
        return checkChunksExist(par1 - par4, par2 - par4, par3 - par4, par1 + par4, par2 + par4, par3 + par4);
    }

    /**
     * Checks between a min and max all the chunks inbetween actually exist. Args: minX, minY, minZ, maxX, maxY, maxZ
     */
    @Override
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
    @Override
    protected boolean chunkExists(int par1, int par2)
    {
        return chunkProvider.chunkExists(par1, par2);
    }

    /**
     * Returns a chunk looked up by block coordinates. Args: x, z
     */
    @Override
    public Chunk getChunkFromBlockCoords(int par1, int par2)
    {
        return getChunkFromChunkCoords(par1 >> 4, par2 >> 4);
    }

    /**
     * Returns back a chunk looked up by chunk coordinates Args: x, y
     */
    @Override
    public Chunk getChunkFromChunkCoords(int par1, int par2)
    {
        return chunkProvider.provideChunk(par1, par2);
    }

    /**
     * Returns the block's material.
     */
    @Override
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
     * Marks the block as needing an update with the renderer. Args: x, y, z
     */
    @Override
    public void markBlockForUpdate(int par1, int par2, int par3)
    {
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).markBlockForUpdate(par1, par2, par3);
        }
    }

    /**
     * The block type change and need to notify other systems  Args: x, y, z, blockID
     */
    @Override
    public void notifyBlockChange(int par1, int par2, int par3, int par4)
    {
        markBlockForUpdate(par1, par2, par3);
        notifyBlocksOfNeighborChange(par1, par2, par3, par4);
    }

    /**
     * Notifies neighboring blocks that this specified block changed  Args: x, y, z, blockID
     */
    @Override
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
    @Override
    public void notifyBlockOfNeighborChange(int par1, int par2, int par3, int par4)
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
    @Override
    public boolean canBlockSeeTheSky(int par1, int par2, int par3)
    {
        return getChunkFromChunkCoords(par1 >> 4, par3 >> 4).canBlockSeeTheSky(par1 & 0xf, par2, par3 & 0xf);
    }

    /**
     * ray traces all blocks, including non-collideable ones
     */
    @Override
    public MovingObjectPosition clip(Vec3 par1Vec3, Vec3 par2Vec3)
    {
        return rayTraceBlocks_do_do(par1Vec3, par2Vec3, false, false);
    }

    @Override
    public MovingObjectPosition clip(Vec3 par1Vec3, Vec3 par2Vec3, boolean par3)
    {
        return rayTraceBlocks_do_do(par1Vec3, par2Vec3, par3, false);
    }

    @Override
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

            Vec3 vec3d = Vec3.createVectorHelper(par1Vec3.xCoord, par1Vec3.yCoord, par1Vec3.zCoord);
            l = (int)(vec3d.xCoord = MathHelper.floor_double(par1Vec3.xCoord));

            if (byte0 == 5)
            {
                l--;
                vec3d.xCoord++;
            }

            i1 = (int)(vec3d.yCoord = MathHelper.floor_double(par1Vec3.yCoord));

            if (byte0 == 1)
            {
                i1--;
                vec3d.yCoord++;
            }

            j1 = (int)(vec3d.zCoord = MathHelper.floor_double(par1Vec3.zCoord));

            if (byte0 == 3)
            {
                j1--;
                vec3d.zCoord++;
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
     * Plays a sound at the entity's position. Args: entity, sound, unknown1, volume (relative to 1.0)
     */
    @Override
    public void playSoundAtEntity(Entity par1Entity, String par2Str, float par3, float par4)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.enableSP){
            float f = 16F;

            if (par3 > 1.0F)
            {
                f *= par3;
            }

            if (mc.renderViewEntity == null){
                return;
            }
            if (mc.renderViewEntity.getDistanceSq(par1Entity.posX, par1Entity.posY - (double)par1Entity.yOffset, par1Entity.posZ) < (double)(f * f))
            {
                mc.sndManager.playSound(par2Str, (float)par1Entity.posX, (float)(par1Entity.posY - (double)par1Entity.yOffset), (float)par1Entity.posZ, par3, par4);
            }
            return;
        }
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playSound(par2Str, par1Entity.posX, par1Entity.posY - (double)par1Entity.yOffset, par1Entity.posZ, par3, par4);
        }
    }

    /**
     * Play a sound effect. Many many parameters for this function. Not sure what they do, but a classic call is :
     * (double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D, 'random.door_open', 1.0F, world.rand.nextFloat() * 0.1F +
     * 0.9F with i,j,k position of the block.
     */
    @Override
    public void playSoundEffect(double par1, double par3, double par5, String par7Str, float par8, float par9)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.enableSP){
            float f = 16F;

            if (par8 > 1.0F)
            {
                f *= par8;
            }

            if (mc.renderViewEntity == null){
                return;
            }
            if (mc.renderViewEntity.getDistanceSq(par1, par3, par5) < (double)(f * f))
            {
                mc.sndManager.playSound(par7Str, (float)par1, (float)par3, (float)par5, par8, par9);
            }
            return;
        }
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playSound(par7Str, par1, par3, par5, par8, par9);
        }
    }

    /**
     * Plays a record at the specified coordinates of the specified name. Args: recordName, x, y, z
     */
    @Override
    public void playRecord(String par1Str, int par2, int par3, int par4)
    {
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playRecord(par1Str, par2, par3, par4);
        }
    }

    /**
     * Spawns a particle.  Args particleName, x, y, z, velX, velY, velZ
     */
    @Override
    public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).spawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
        }
    }

    /**
     * adds a lightning bolt to the list of lightning bolts in this world.
     */
    @Override
    public boolean addWeatherEffect(Entity par1Entity)
    {
        weatherEffects.add(par1Entity);
        return true;
    }

    /**
     * Called to place all entities as part of a world
     */
    @Override
    public boolean spawnEntityInWorld(Entity par1Entity)
    {
        int i = MathHelper.floor_double(par1Entity.posX / 16D);
        int j = MathHelper.floor_double(par1Entity.posZ / 16D);
        boolean flag = par1Entity.forceSpawn;

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
            onEntityAdded(par1Entity);
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
    @Override
    protected void onEntityAdded(Entity par1Entity)
    {
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).onEntityCreate(par1Entity);
        }
    }

    /**
     * Decrement the reference counter for this entity's skin image data
     */
    @Override
    protected void onEntityRemoved(Entity par1Entity)
    {
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).onEntityDestroy(par1Entity);
        }
    }

    /**
     * Dismounts the entity (and anything riding the entity), sets the dead flag, and removes the player entity from the
     * player entity list. Called by the playerLoggedOut function.
     */
    @Override
    public void removeEntity(Entity par1Entity)
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
            playerEntities.remove((EntityPlayer)par1Entity);
            updateAllPlayersSleepingFlag();
        }
    }

    /**
     * Adds a IWorldAccess to the list of worldAccesses
     */
    @Override
    public void addWorldAccess(IWorldAccess par1IWorldAccess)
    {
        worldAccesses.add(par1IWorldAccess);
    }

    /**
     * Removes a worldAccess from the worldAccesses object
     */
    @Override
    public void removeWorldAccess(IWorldAccess par1IWorldAccess)
    {
        worldAccesses.remove(par1IWorldAccess);
    }

    /**
     * Returns a list of bounding boxes that collide with aabb excluding the passed in entity's collision. Args: entity,
     * aabb
     */
    @Override
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
                        block.addCollisionBoxesToList(this, k1, i2, l1, par2AxisAlignedBB, collidingBoundingBoxes, par1Entity);
                    }
                }
            }
        }

        double d = 0.25D;
        List list = getEntitiesWithinAABBExcludingEntity(par1Entity, par2AxisAlignedBB.expand(d, d, d));

        for (int j2 = 0; j2 < list.size(); j2++)
        {
            AxisAlignedBB axisalignedbb = ((Entity)list.get(j2)).getBoundingBox();

            if (axisalignedbb != null && axisalignedbb.intersectsWith(par2AxisAlignedBB))
            {
                collidingBoundingBoxes.add(axisalignedbb);
            }

            axisalignedbb = par1Entity.getCollisionBox((Entity)list.get(j2));

            if (axisalignedbb != null && axisalignedbb.intersectsWith(par2AxisAlignedBB))
            {
                collidingBoundingBoxes.add(axisalignedbb);
            }
        }

        return collidingBoundingBoxes;
    }

    /**
     * calls calculateCelestialAngle
     */
    @Override
    public float getCelestialAngle(float par1)
    {
        if (Minecraft.timecontrol && provider.dimensionId == 0){
            return super.getCelestialAngle(par1) + (float)(field_35467_J + (field_35468_K - field_35467_J) * (double)par1);
        }
        return super.getCelestialAngle(par1);
    }

    @Override
    public int getMoonPhase()
    {
        return provider.getMoonPhase(worldInfo.getWorldTotalTime());
    }

    /**
     * Return getCelestialAngle()*2*PI
     */
    @Override
    public float getCelestialAngleRadians(float par1)
    {
        float f = getCelestialAngle(par1);
        return f * (float)Math.PI * 2.0F;
    }

    /**
     * Schedules a tick to a block with a delay (Most commonly the tick rate)
     */
    @Override
    public void scheduleBlockUpdate(int par1, int par2, int par3, int par4, int par5)
    {
        scheduleBlockUpdateWithPriority(par1, par2, par3, par4, par5, 0);
    }

    @Override
    public void scheduleBlockUpdateWithPriority(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);
        byte byte0 = 8;

        if (scheduledUpdatesAreImmediate)
        {
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

        if (checkChunksExist(par1 - byte0, par2 - byte0, par3 - byte0, par1 + byte0, par2 + byte0, par3 + byte0))
        {
            if (par4 > 0)
            {
                nextticklistentry.setScheduledTime((long)par5 + worldInfo.getWorldTotalTime());
                nextticklistentry.setPriority(par6);
            }

            if (!scheduledTickSet.contains(nextticklistentry))
            {
                scheduledTickSet.add(nextticklistentry);
                scheduledTickTreeSet.add(nextticklistentry);
            }
        }
    }

    /**
     * Schedules a block update from the saved information in a chunk. Called when the chunk is loaded.
     */
    @Override
    public void scheduleBlockUpdateFromLoad(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        NextTickListEntry nextticklistentry = new NextTickListEntry(par1, par2, par3, par4);

        if (par4 > 0)
        {
            nextticklistentry.setScheduledTime((long)par5 + worldInfo.getWorldTotalTime());
        }

        if (!scheduledTickSet.contains(nextticklistentry))
        {
            scheduledTickSet.add(nextticklistentry);
            scheduledTickTreeSet.add(nextticklistentry);
        }
    }

    /**
     * Returns true if there are no solid, live entities in the specified AxisAlignedBB
     */
    @Override
    public boolean checkNoEntityCollision(AxisAlignedBB par1AxisAlignedBB)
    {
        List list = getEntitiesWithinAABBExcludingEntity(null, par1AxisAlignedBB);

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);

            if (!entity.isDead && entity.preventEntitySpawning)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns if any of the blocks within the aabb are liquids. Args: aabb
     */
    @Override
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
    @Override
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
    @Override
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
        Vec3 vec3d = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);

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
                        block.velocityToAddToEntity(this, k1, l1, i2, par3Entity, vec3d);
                    }
                }
            }
        }

        if (vec3d.lengthVector() > 0.0D)
        {
            vec3d = vec3d.normalize();
            double d = 0.014D;
            par3Entity.motionX += vec3d.xCoord * d;
            par3Entity.motionY += vec3d.yCoord * d;
            par3Entity.motionZ += vec3d.zCoord * d;
        }

        return flag;
    }

    /**
     * Returns true if the given bounding box contains the given material
     */
    @Override
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
    @Override
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
    @Override
    public Explosion createExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9)
    {
        return newExplosion(par1Entity, par2, par4, par6, par8, false, par9);
    }

    /**
     * returns a new explosion. Does initiation (at time of writing Explosion is not finished)
     */
    @Override
    public Explosion newExplosion(Entity par1Entity, double par2, double par4, double par6, float par8, boolean par9, boolean par10)
    {
        Explosion explosion = new Explosion(this, par1Entity, par2, par4, par6, par8);
        explosion.isFlaming = par9;
        explosion.isSmoking = par10;
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    /**
     * Gets the percentage of real blocks within within a bounding box, along a specified vector.
     */
    @Override
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

                    if (clip(Vec3.createVectorHelper(d3, d4, d5), par1Vec3) == null)
                    {
                        i++;
                    }

                    j++;
                }
            }
        }

        return (float)i / (float)j;
    }

    @Override
    public boolean extinguishFire(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5)
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
            setBlockToAir(par2, par3, par4);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Entity func_4085_a(Class par1Class)
    {
        return null;
    }

    /**
     * This string is 'All: (number of loaded entities)' Viewable by press ing F3
     */
    @Override
    public String getDebugLoadedEntities()
    {
        return (new StringBuilder()).append("All: ").append(loadedEntityList.size()).toString();
    }

    /**
     * Returns the name of the current chunk provider, by calling chunkprovider.makeString()
     */
    @Override
    public String getProviderName()
    {
        return chunkProvider.makeString();
    }

    /**
     * adds tile entity to despawn list (renamed from markEntityForDespawn)
     */
    @Override
    public void markTileEntityForDespawn(TileEntity par1TileEntity)
    {
        entityRemoval.add(par1TileEntity);
    }

    /**
     * Returns true if the block at the specified coordinates is an opaque cube. Args: x, y, z
     */
    @Override
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
    @Override
    public boolean isBlockNormalCube(int par1, int par2, int par3)
    {
        return Block.isNormalCube(getBlockId(par1, par2, par3));
    }

    public void saveWorldIndirectly(IProgressUpdate par1IProgressUpdate)
    {
        saveWorld(true, par1IProgressUpdate);

        try
        {
            ThreadedFileIOBase.threadedIOInstance.waitForFinish();
        }
        catch (InterruptedException interruptedexception)
        {
            interruptedexception.printStackTrace();
        }
    }

    /**
     * Set which types of mobs are allowed to spawn (peaceful vs hostile).
     */
    @Override
    public void setAllowedSpawnTypes(boolean par1, boolean par2)
    {
        spawnHostileMobs = par1;
        spawnPeacefulMobs = par2;
    }

    /**
     * Runs a single tick for the world
     */
    @Override
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
        updateWeather();

        if (isAllPlayersFullyAsleep())
        {
            boolean flag = false;

            if (spawnHostileMobs)
            {
                if (difficultySetting < 1);
            }

            if (!flag)
            {
                if (getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
                {
                    long l = worldInfo.getWorldTotalTime() + 24000L;
                    worldInfo.setWorldTime(l - l % 24000L);
                    func_82738_a(l - l % 24000L);
                    field_35467_J = 0D;
                    field_35468_K = 0D;
                }
                wakeUpAllPlayers();
            }
        }

        theProfiler.startSection("mobSpawner");
        if (getGameRules().getGameRuleBooleanValue("doMobSpawning")){
            animalSpawner.performSpawningSP(this, spawnHostileMobs, spawnPeacefulMobs && worldInfo.getWorldTotalTime() % 400L == 0L);
        }
        theProfiler.endStartSection("chunkSource");
        chunkProvider.unloadQueuedChunks();
        int i = calculateSkylightSubtracted(1.0F);

        if (i != skylightSubtracted)
        {
            skylightSubtracted = i;
        }

        worldInfo.incrementTotalWorldTime(worldInfo.getWorldTotalTime() + 1L);

        long l1 = worldInfo.getWorldTotalTime() + 1L;

        if (l1 % (long)autosavePeriod == 0L)
        {
            theProfiler.endStartSection("save");
            saveWorld(false, null);
        }

        if (getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
        {
            worldInfo.setWorldTime(worldInfo.getWorldTime() + 1L);
        }
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
    @Override
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

    /**
     * Stops all weather effects.
     */
    private void clearWeather()
    {
        worldInfo.setRainTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThunderTime(0);
        worldInfo.setThundering(false);
    }

    protected void func_48461_r()
    {
        activeChunkSet.clear();
        theProfiler.startSection("buildList");

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

        theProfiler.endSection();

        if (ambientTickCountdown > 0)
        {
            ambientTickCountdown--;
        }

        theProfiler.startSection("playerCheckLight");

        if (!playerEntities.isEmpty())
        {
            int j = rand.nextInt(playerEntities.size());
            EntityPlayer entityplayer1 = (EntityPlayer)playerEntities.get(j);
            int l = (MathHelper.floor_double(entityplayer1.posX) + rand.nextInt(11)) - 5;
            int j1 = (MathHelper.floor_double(entityplayer1.posY) + rand.nextInt(11)) - 5;
            int k1 = (MathHelper.floor_double(entityplayer1.posZ) + rand.nextInt(11)) - 5;
            updateAllLightTypes(l, j1, k1);
        }

        theProfiler.endSection();
    }

    protected void func_48458_a(int par1, int par2, Chunk par3Chunk)
    {
        theProfiler.endStartSection("tickChunk");
        par3Chunk.updateSkylight();
        theProfiler.endStartSection("moodSound");

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

        theProfiler.endStartSection("checkLight");
        par3Chunk.enqueueRelightChecks();
    }

    /**
     * plays random cave ambient sounds and runs updateTick on random blocks within each chunk in the vacinity of a
     * player
     */
    @Override
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

            if (rand.nextInt(16) == 0)
            {
                updateLCG = updateLCG * 3 + 0x3c6ef35f;
                int j1 = updateLCG >> 2;
                int l1 = j1 & 0xf;
                int k2 = j1 >> 8 & 0xf;
                int j3 = getPrecipitationHeight(l1 + k, k2 + l);

                if (isBlockFreezableNaturally(l1 + k, j3 - 1, k2 + l))
                {
                    setBlock(l1 + k, j3 - 1, k2 + l, Block.ice.blockID);
                }

                if (isRaining() && canSnowAt(l1 + k, j3, k2 + l))
                {
                    setBlock(l1 + k, j3, k2 + l, Block.snow.blockID);
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
     * checks to see if a given block is both water and is cold enough to freeze
     */
    @Override
    public boolean isBlockFreezable(int par1, int par2, int par3)
    {
        return canBlockFreeze(par1, par2, par3, false);
    }

    /**
     * checks to see if a given block is both water and has at least one immediately adjacent non-water block
     */
    @Override
    public boolean isBlockFreezableNaturally(int par1, int par2, int par3)
    {
        return canBlockFreeze(par1, par2, par3, true);
    }

    /**
     * checks to see if a given block is both water, and cold enough to freeze - if the par4 boolean is set, this will
     * only return true if there is a non-water block immediately adjacent to the specified block
     */
    @Override
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
    @Override
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

    /**
     * Runs through the list of updates to run and ticks them
     */
    @Override
    public boolean tickUpdates(boolean par1)
    {
        int i = scheduledTickTreeSet.size();

        if (i != scheduledTickSet.size())
        {
            throw new IllegalStateException("TickNextTick list out of synch");
        }

        if (i > 1000)
        {
            i = 1000;
        }

        for (int j = 0; j < i; j++)
        {
            NextTickListEntry nextticklistentry = (NextTickListEntry)scheduledTickTreeSet.first();

            if (!par1 && nextticklistentry.scheduledTime > worldInfo.getWorldTotalTime())
            {
                break;
            }

            scheduledTickTreeSet.remove(nextticklistentry);
            scheduledTickSet.remove(nextticklistentry);
            byte byte0 = 8;

            if (!checkChunksExist(nextticklistentry.xCoord - byte0, nextticklistentry.yCoord - byte0, nextticklistentry.zCoord - byte0, nextticklistentry.xCoord + byte0, nextticklistentry.yCoord + byte0, nextticklistentry.zCoord + byte0))
            {
                continue;
            }

            int k = getBlockId(nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord);

            if (k == nextticklistentry.blockID && k > 0)
            {
                Block.blocksList[k].updateTick(this, nextticklistentry.xCoord, nextticklistentry.yCoord, nextticklistentry.zCoord, rand);
            }
        }

        return scheduledTickTreeSet.size() != 0;
    }

    @Override
    public List getPendingBlockUpdates(Chunk par1Chunk, boolean par2)
    {
        ArrayList arraylist = null;
        ChunkCoordIntPair chunkcoordintpair = par1Chunk.getChunkCoordIntPair();
        int i = chunkcoordintpair.chunkXPos << 4;
        int j = i + 16;
        int k = chunkcoordintpair.chunkZPos << 4;
        int l = k + 16;
        Iterator iterator = scheduledTickSet.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            NextTickListEntry nextticklistentry = (NextTickListEntry)iterator.next();

            if (nextticklistentry.xCoord >= i && nextticklistentry.xCoord < j && nextticklistentry.zCoord >= k && nextticklistentry.zCoord < l)
            {
                if (par2)
                {
                    scheduledTickTreeSet.remove(nextticklistentry);
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

        return arraylist;
    }

    /**
     * Randomly will call the random display update on a 1000 blocks within 16 units of the specified position. Args: x,
     * y, z
     */
    @Override
    public void doVoidFogParticles(int par1, int par2, int par3)
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
     * Does nothing while unloading 100 oldest chunks
     */
    public void dropOldChunks()
    {
        while (chunkProvider.unloadQueuedChunks()) ;
    }

    public EntityPlayer func_48456_a(double par1, double par3, double par5)
    {
        double d = -1D;
        EntityPlayer entityplayer = null;

        for (int i = 0; i < playerEntities.size(); i++)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)playerEntities.get(i);
            double d1 = entityplayer1.getDistanceSq(par1, entityplayer1.posY, par3);

            if ((par5 < 0.0D || d1 < par5 * par5) && (d == -1D || d1 < d))
            {
                d = d1;
                entityplayer = entityplayer1;
            }
        }

        return entityplayer;
    }

    /**
     * If on MP, sends a quitting packet.
     */
    @Override
    public void sendQuittingDisconnectingPacket()
    {
    }

    /**
     * Called when checking if a certain block can be mined or not. The 'spawn safe zone' check is located here.
     */
    @Override
    public boolean canMineBlock(EntityPlayer par1EntityPlayer, int par2, int par3, int i)
    {
        return true;
    }

    /**
     * sends a Packet 38 (Entity Status) to all tracked players of that entity
     */
    @Override
    public void setEntityState(Entity entity, byte byte0)
    {
    }

    public void updateEntityList()
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
            onEntityRemoved((Entity)unloadedEntityList.get(j));
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
            onEntityRemoved(entity1);
        }
    }

    /**
     * Updates the flag that indicates whether or not all players in the world are sleeping.
     */
    @Override
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

    /**
     * Wakes up all players in the world.
     */
    protected void wakeUpAllPlayers()
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

        clearWeather();
    }

    /**
     * Returns whether or not all players in the world are fully asleep.
     */
    public boolean isAllPlayersFullyAsleep()
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

    @Override
    public void playSound(double par1, double par3, double par5, String par7Str, float par8, float par9, boolean flag)
    {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.enableSP){
            float f = 16F;

            if (par8 > 1.0F)
            {
                f *= par8;
            }

            if (mc.renderViewEntity == null){
                return;
            }
            if (mc.renderViewEntity.getDistanceSq(par1, par3, par5) < (double)(f * f))
            {
                mc.sndManager.playSound(par7Str, (float)par1, (float)par3, (float)par5, par8, par9);
            }
            return;
        }
        for (int i = 0; i < worldAccesses.size(); i++)
        {
            ((IWorldAccess)worldAccesses.get(i)).playSound(par7Str, par1, par3, par5, par8, par9);
        }
    }

    /**
     * Gets a random mob for spawning in this world.
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
     * Invalidates an AABB region of blocks from the receive queue, in the event that the block has been modified
     * client-side in the intervening 80 receive ticks.
     */
    @Override
    public void invalidateBlockReceiveRegion(int i, int j, int k, int l, int i1, int j1)
    {
    }

    @Override
    public void doPreChunk(int par1, int par2, boolean par3)
    {
    }

    /**
     * Add an ID to Entity mapping to entityHashSet
     */
    @Override
    public void addEntityToWorld(int par1, Entity par2Entity)
    {
    }

    /**
     * Lookup and return an Entity based on its ID
     */
    @Override
    public Entity getEntityByID(int par1)
    {
        return null;
    }

    @Override
    public Entity removeEntityFromWorld(int par1)
    {
        return null;
    }

    @Override
    public boolean setBlockAndMetadataAndInvalidate(int par1, int par2, int par3, int par4, int par5)
    {
        return false;
    }

    @Override
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport par1CrashReport)
    {
        CrashReportCategory crashreportcategory = par1CrashReport.makeCategoryDepth("Affected level", 1);
        crashreportcategory.addCrashSection("Level name", worldInfo != null ? ((Object)(worldInfo.getWorldName())) : "????");
        crashreportcategory.addCrashSection("Chunk stats", chunkProvider.makeString());

        try
        {
            worldInfo.addToCrashReport(crashreportcategory);
        }
        catch (Throwable throwable)
        {
            crashreportcategory.addCrashSectionThrowable("Level Data Unobtainable", throwable);
        }

        return crashreportcategory;
    }

//This method is very important! Without it you will be able to place blocks in yourself.
    @Override
    public boolean canPlaceEntityOnSide(int par1, int par2, int par3, int par4, boolean par5, int par6, Entity par7Entity, ItemStack par8ItemStack)
    {
        int i = getBlockId(par2, par3, par4);
        Block block = Block.blocksList[i];
        Block block1 = Block.blocksList[par1];
        AxisAlignedBB axisalignedbb = block1.getCollisionBoundingBoxFromPool(this, par2, par3, par4);

        if (par5)
        {
            axisalignedbb = null;
        }

        if (axisalignedbb != null && !checkNoEntityCollision(axisalignedbb))
        {
            return false;
        }

        if (block != null && (block == Block.waterMoving || block == Block.waterStill || block == Block.lavaMoving || block == Block.lavaStill || block == Block.fire || block.blockMaterial.isReplaceable()))
        {
            block = null;
        }

        return par1 > 0 && block == null && block1.canPlaceBlockOnSide(this, par2, par3, par4, par6);
    }

    static
    {
        bonusChestContent = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.stick.itemID, 0, 1, 3, 10), new WeightedRandomChestContent(Block.planks.blockID, 0, 1, 3, 10), new WeightedRandomChestContent(Block.wood.blockID, 0, 1, 3, 10), new WeightedRandomChestContent(Item.axeStone.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.axeWood.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.pickaxeStone.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.pickaxeWood.itemID, 0, 1, 1, 5), new WeightedRandomChestContent(Item.appleRed.itemID, 0, 2, 3, 5), new WeightedRandomChestContent(Item.bread.itemID, 0, 2, 3, 3)
                });
    }
}
