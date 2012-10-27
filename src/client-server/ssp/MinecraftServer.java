package net.minecraft.server;

import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.src.AABBPool;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BehaviorArrowDispense;
import net.minecraft.src.BehaviorBucketEmptyDispense;
import net.minecraft.src.BehaviorBucketFullDispense;
import net.minecraft.src.BehaviorDispenseBoat;
import net.minecraft.src.BehaviorDispenseFireball;
import net.minecraft.src.BehaviorDispenseMinecart;
import net.minecraft.src.BehaviorEggDispense;
import net.minecraft.src.BehaviorExpBottleDispense;
import net.minecraft.src.BehaviorMobEggDispense;
import net.minecraft.src.BehaviorPotionDispense;
import net.minecraft.src.BehaviorSnowballDispense;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.CallableIsServerModded;
import net.minecraft.src.CallablePlayers;
import net.minecraft.src.CallableServerMemoryStats;
import net.minecraft.src.CallableServerProfiler;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ChunkProviderServer;
import net.minecraft.src.CommandBase;
import net.minecraft.src.ConvertingProgressUpdate;
import net.minecraft.src.CrashReport;
import net.minecraft.src.DemoWorldServer;
import net.minecraft.src.EntityTracker;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.IPlayerUsage;
import net.minecraft.src.IRegistry;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IUpdatePlayerListBox;
import net.minecraft.src.Item;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MinecraftException;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet4UpdateTime;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.RConConsoleSource;
import net.minecraft.src.ReportedException;
import net.minecraft.src.ServerCommandManager;
import net.minecraft.src.ServerConfigurationManager;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.StringUtils;
import net.minecraft.src.ThreadMinecraftServer;
import net.minecraft.src.Vec3Pool;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldManager;
import net.minecraft.src.WorldProvider;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldServerMulti;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

public abstract class MinecraftServer implements Runnable, IPlayerUsage, ICommandSender
{
    /** The logging system. */
    public static Logger logger = Logger.getLogger("Minecraft");

    /** Instance of Minecraft Server. */
    private static MinecraftServer mcServer;
    private final ISaveFormat anvilConverterForAnvilFile;

    /** The PlayerUsageSnooper instance. */
    private final PlayerUsageSnooper usageSnooper;
    private final File anvilFile;

    /** List of names of players who are online. */
    private final List playersOnline = new ArrayList();
    private final ICommandManager commandManager;
    public final Profiler theProfiler = new Profiler();

    /** The server's hostname. */
    private String hostname;

    /** The server's port. */
    private int serverPort;
    public WorldServer worldServers[];

    /** The ServerConfigurationManager instance. */
    private ServerConfigurationManager serverConfigManager;

    /**
     * Indicates whether the server is running or not. Set to false to initiate a shutdown.
     */
    private boolean serverRunning;

    /** Indicates to other classes that the server is safely stopped. */
    private boolean serverStopped;

    /** Incremented every tick. */
    private int tickCounter;

    /**
     * The task the server is currently working on(and will output on outputPercentRemaining).
     */
    public String currentTask;

    /** The percentage of the current task finished so far. */
    public int percentDone;

    /** True if the server is in online mode. */
    private boolean onlineMode;

    /** True if the server has animals turned on. */
    private boolean canSpawnAnimals;
    private boolean canSpawnNPCs;

    /** Indicates whether PvP is active on the server or not. */
    private boolean pvpEnabled;

    /** Determines if flight is allowed or not. */
    private boolean allowFlight;

    /** The server MOTD string. */
    private String motd;

    /** Maximum build height. */
    private int buildLimit;
    private long lastSentPacketID;
    private long lastSentPacketSize;
    private long lastReceivedID;
    private long lastReceivedSize;
    public final long sentPacketCountArray[] = new long[100];
    public final long sentPacketSizeArray[] = new long[100];
    public final long receivedPacketCountArray[] = new long[100];
    public final long receivedPacketSizeArray[] = new long[100];
    public final long tickTimeArray[] = new long[100];
    public long timeOfLastDimensionTick[][];
    private KeyPair serverKeyPair;

    /** Username of the server owner (for integrated servers) */
    private String serverOwner;
    private String folderName;
    private String worldName;
    private boolean isDemo;
    private boolean enableBonusChest;

    /**
     * If true, there is no need to save chunks or stop the server, because that is already being done.
     */
    private boolean worldIsBeingDeleted;
    private String texturePack;
    private boolean serverIsRunning;

    /**
     * Set when warned for "Can't keep up", which triggers again after 15 seconds.
     */
    private long timeOfLastWarning;
    private String userMessage;
    private boolean startProfiling;

    public MinecraftServer(File par1File)
    {
        serverPort = -1;
        serverRunning = true;
        serverStopped = false;
        tickCounter = 0;
        texturePack = "";
        serverIsRunning = false;
        mcServer = this;
        usageSnooper = new PlayerUsageSnooper("server", this);
        anvilFile = par1File;
        commandManager = new ServerCommandManager();
        anvilConverterForAnvilFile = new AnvilSaveConverter(par1File);
        func_82355_al();
    }

    private void func_82355_al()
    {
        BlockDispenser.field_82527_a.func_82595_a(Item.arrow, new BehaviorArrowDispense(this));
        BlockDispenser.field_82527_a.func_82595_a(Item.egg, new BehaviorEggDispense(this));
        BlockDispenser.field_82527_a.func_82595_a(Item.snowball, new BehaviorSnowballDispense(this));
        BlockDispenser.field_82527_a.func_82595_a(Item.expBottle, new BehaviorExpBottleDispense(this));
        BlockDispenser.field_82527_a.func_82595_a(Item.potion, new BehaviorPotionDispense(this));
        BlockDispenser.field_82527_a.func_82595_a(Item.monsterPlacer, new BehaviorMobEggDispense(this));
        BlockDispenser.field_82527_a.func_82595_a(Item.fireballCharge, new BehaviorDispenseFireball(this));
        BehaviorDispenseMinecart behaviordispenseminecart = new BehaviorDispenseMinecart(this);
        BlockDispenser.field_82527_a.func_82595_a(Item.minecartEmpty, behaviordispenseminecart);
        BlockDispenser.field_82527_a.func_82595_a(Item.minecartCrate, behaviordispenseminecart);
        BlockDispenser.field_82527_a.func_82595_a(Item.minecartPowered, behaviordispenseminecart);
        BlockDispenser.field_82527_a.func_82595_a(Item.boat, new BehaviorDispenseBoat(this));
        BehaviorBucketFullDispense behaviorbucketfulldispense = new BehaviorBucketFullDispense(this);
        BlockDispenser.field_82527_a.func_82595_a(Item.bucketLava, behaviorbucketfulldispense);
        BlockDispenser.field_82527_a.func_82595_a(Item.bucketWater, behaviorbucketfulldispense);
        BlockDispenser.field_82527_a.func_82595_a(Item.bucketEmpty, new BehaviorBucketEmptyDispense(this));
    }

    /**
     * Initialises the server and starts it.
     */
    protected abstract boolean startServer() throws IOException;

    protected void convertMapIfNeeded(String par1Str)
    {
        if (getActiveAnvilConverter().isOldMapFormat(par1Str))
        {
            logger.info("Converting map!");
            setUserMessage("menu.convertingLevel");
            getActiveAnvilConverter().convertMapFormat(par1Str, new ConvertingProgressUpdate(this));
        }
    }

    /**
     * Typically "menu.convertingLevel", "menu.loadingLevel" or others.
     */
    protected synchronized void setUserMessage(String par1Str)
    {
        userMessage = par1Str;
    }

    public synchronized String getUserMessage()
    {
        return userMessage;
    }

    protected void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str)
    {
        convertMapIfNeeded(par1Str);
        setUserMessage("menu.loadingLevel");
        worldServers = new WorldServer[3];
        timeOfLastDimensionTick = new long[worldServers.length][100];
        ISaveHandler isavehandler = anvilConverterForAnvilFile.getSaveLoader(par1Str, true);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();
        WorldSettings worldsettings;

        if (worldinfo == null)
        {
            worldsettings = new WorldSettings(par3, getGameType(), canStructuresSpawn(), isHardcore(), par5WorldType);
            worldsettings.func_82750_a(par6Str);
        }
        else
        {
            worldsettings = new WorldSettings(worldinfo);
        }

        if (enableBonusChest)
        {
            worldsettings.enableBonusChest();
        }

        for (int i = 0; i < worldServers.length; i++)
        {
            byte byte0 = 0;

            if (i == 1)
            {
                byte0 = -1;
            }

            if (i == 2)
            {
                byte0 = 1;
            }

            if (i == 0)
            {
                if (isDemo())
                {
                    worldServers[i] = new DemoWorldServer(this, isavehandler, par2Str, byte0, theProfiler);
                }
                else
                {
                    worldServers[i] = new WorldServer(this, isavehandler, par2Str, byte0, worldsettings, theProfiler);
                }
            }
            else
            {
                worldServers[i] = new WorldServerMulti(this, isavehandler, par2Str, byte0, worldsettings, worldServers[0], theProfiler);
            }

            worldServers[i].addWorldAccess(new WorldManager(this, worldServers[i]));

            if (!isSinglePlayer())
            {
                worldServers[i].getWorldInfo().setGameType(getGameType());
            }

            serverConfigManager.setPlayerManager(worldServers);
        }

        setDifficultyForAllWorlds(getDifficulty());
        initialWorldChunkLoad();
    }

    protected void initialWorldChunkLoad()
    {
        int i = 0;
        setUserMessage("menu.generatingTerrain");
        int j = 0;
        logger.info((new StringBuilder()).append("Preparing start region for level ").append(j).toString());
        WorldServer worldserver = worldServers[j];
        ChunkCoordinates chunkcoordinates = worldserver.getSpawnPoint();
        long l = System.currentTimeMillis();

        for (int k = -192; k <= 192 && isServerRunning(); k += 16)
        {
            for (int i1 = -192; i1 <= 192 && isServerRunning(); i1 += 16)
            {
                long l1 = System.currentTimeMillis();

                if (l1 - l > 1000L)
                {
                    outputPercentRemaining("Preparing spawn area", (i * 100) / 625);
                    l = l1;
                }

                i++;
                worldserver.theChunkProviderServer.loadChunk(chunkcoordinates.posX + k >> 4, chunkcoordinates.posZ + i1 >> 4);
            }
        }

        clearCurrentTask();
    }

    public abstract boolean canStructuresSpawn();

    public abstract EnumGameType getGameType();

    /**
     * Defaults to "1" (Easy) for the dedicated server, defaults to "2" (Normal) on the client.
     */
    public abstract int getDifficulty();

    /**
     * Defaults to false.
     */
    public abstract boolean isHardcore();

    /**
     * Used to display a percent remaining given text and the percentage.
     */
    protected void outputPercentRemaining(String par1Str, int par2)
    {
        currentTask = par1Str;
        percentDone = par2;
        logger.info((new StringBuilder()).append(par1Str).append(": ").append(par2).append("%").toString());
    }

    /**
     * Set current task to null and set its percentage to 0.
     */
    protected void clearCurrentTask()
    {
        currentTask = null;
        percentDone = 0;
    }

    /**
     * par1 indicates if a log message should be output.
     */
    protected void saveAllWorlds(boolean par1)
    {
        if (worldIsBeingDeleted)
        {
            return;
        }

        WorldServer aworldserver[] = worldServers;
        int i = aworldserver.length;

        for (int j = 0; j < i; j++)
        {
            WorldServer worldserver = aworldserver[j];

            if (worldserver == null)
            {
                continue;
            }

            if (!par1)
            {
                logger.info((new StringBuilder()).append("Saving chunks for level '").append(worldserver.getWorldInfo().getWorldName()).append("'/").append(worldserver.provider.getDimensionName()).toString());
            }

            try
            {
                worldserver.saveAllChunks(true, null);
            }
            catch (MinecraftException minecraftexception)
            {
                logger.warning(minecraftexception.getMessage());
            }
        }
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        if (worldIsBeingDeleted)
        {
            return;
        }

        logger.info("Stopping server");

        if (getNetworkThread() != null)
        {
            getNetworkThread().stopListening();
        }

        if (serverConfigManager != null)
        {
            logger.info("Saving players");
            serverConfigManager.saveAllPlayerData();
            serverConfigManager.removeAllPlayers();
        }

        logger.info("Saving worlds");
        saveAllWorlds(false);
        WorldServer aworldserver[] = worldServers;
        int i = aworldserver.length;

        for (int j = 0; j < i; j++)
        {
            WorldServer worldserver = aworldserver[j];
            worldserver.flush();
        }

        if (usageSnooper != null && usageSnooper.isSnooperRunning())
        {
            usageSnooper.stopSnooper();
        }
    }

    /**
     * "getHostname" is already taken, but both return the hostname.
     */
    public String getServerHostname()
    {
        return hostname;
    }

    public void setHostname(String par1Str)
    {
        hostname = par1Str;
    }

    public boolean isServerRunning()
    {
        return serverRunning;
    }

    /**
     * Sets the serverRunning variable to false, in order to get the server to shut down.
     */
    public void initiateShutdown()
    {
        serverRunning = false;
    }

    public void run()
    {
        try
        {
            if (this.startServer())
            {
                long var1 = System.currentTimeMillis();

                for (long var50 = 0L; this.serverRunning; this.serverIsRunning = true)
                {
                    long var5 = System.currentTimeMillis();
                    long var7 = var5 - var1;

                    if (var7 > 2000L && var1 - this.timeOfLastWarning >= 15000L)
                    {
                        logger.warning("Can\'t keep up! Did the system time change, or is the server overloaded?");
                        var7 = 2000L;
                        this.timeOfLastWarning = var1;
                    }

                    if (var7 < 0L)
                    {
                        logger.warning("Time ran backwards! Did the system time change?");
                        var7 = 0L;
                    }

                    var50 += var7;
                    var1 = var5;

                    if (this.worldServers[0].areAllPlayersAsleep())
                    {
                        this.tick();
                        var50 = 0L;
                    }
                    else
                    {
                        while (var50 > 50L)
                        {
                            var50 -= 50L;
                            this.tick();
                        }
                    }

                    Thread.sleep(1L);
                }
            }
            else
            {
                this.finalTick((CrashReport)null);
            }
        }
        catch (Throwable var48)
        {
            var48.printStackTrace();
            logger.log(Level.SEVERE, "Encountered an unexpected exception " + var48.getClass().getSimpleName(), var48);
            CrashReport var2 = null;

            if (var48 instanceof ReportedException)
            {
                var2 = this.addServerInfoToCrashReport(((ReportedException)var48).getTheReportedExceptionCrashReport());
            }
            else
            {
                var2 = this.addServerInfoToCrashReport(new CrashReport("Exception in server tick loop", var48));
            }

            File var3 = new File(new File(this.getDataDirectory(), "crash-reports"), "crash-" + (new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date()) + "-server.txt");

            if (var2.saveToFile(var3))
            {
                logger.severe("This crash report has been saved to: " + var3.getAbsolutePath());
            }
            else
            {
                logger.severe("We were unable to save this crash report to disk.");
            }

            this.finalTick(var2);
        }
        finally
        {
            try
            {
                this.stopServer();
                this.serverStopped = true;
            }
            catch (Throwable var46)
            {
                var46.printStackTrace();
            }
            finally
            {
                this.systemExitNow();
            }
        }
    }

    protected File getDataDirectory()
    {
        return new File(".");
    }

    /**
     * Called on exit from the main run() loop.
     */
    protected void finalTick(CrashReport crashreport)
    {
    }

    /**
     * Directly calls System.exit(0), instantly killing the program.
     */
    protected void systemExitNow()
    {
    }

    /**
     * Main function called by run() every loop.
     */
    public void tick()
    {
        long l = System.nanoTime();
        AxisAlignedBB.getAABBPool().cleanPool();
        tickCounter++;

        if (startProfiling)
        {
            startProfiling = false;
            theProfiler.profilingEnabled = true;
            theProfiler.clearProfiling();
        }

        theProfiler.startSection("root");
        updateTimeLightAndEntities();

        if (tickCounter % 900 == 0)
        {
            theProfiler.startSection("save");
            serverConfigManager.saveAllPlayerData();
            saveAllWorlds(true);
            theProfiler.endSection();
        }

        theProfiler.startSection("tallying");
        tickTimeArray[tickCounter % 100] = System.nanoTime() - l;
        sentPacketCountArray[tickCounter % 100] = Packet.sentID - lastSentPacketID;
        lastSentPacketID = Packet.sentID;
        sentPacketSizeArray[tickCounter % 100] = Packet.sentSize - lastSentPacketSize;
        lastSentPacketSize = Packet.sentSize;
        receivedPacketCountArray[tickCounter % 100] = Packet.receivedID - lastReceivedID;
        lastReceivedID = Packet.receivedID;
        receivedPacketSizeArray[tickCounter % 100] = Packet.receivedSize - lastReceivedSize;
        lastReceivedSize = Packet.receivedSize;
        theProfiler.endSection();
        theProfiler.startSection("snooper");

        if (!usageSnooper.isSnooperRunning() && tickCounter > 100)
        {
            usageSnooper.startSnooper();
        }

        if (tickCounter % 6000 == 0)
        {
            usageSnooper.addMemoryStatsToSnooper();
        }

        theProfiler.endSection();
        theProfiler.endSection();
    }

    public void updateTimeLightAndEntities()
    {
        theProfiler.startSection("levels");

        for (int i = 0; i < worldServers.length; i++)
        {
            long l = System.nanoTime();

            if (i == 0 || getAllowNether())
            {
                WorldServer worldserver = worldServers[i];
                theProfiler.startSection(worldserver.getWorldInfo().getWorldName());
                theProfiler.startSection("pools");
                worldserver.func_82732_R().clear();
                theProfiler.endSection();

                if (tickCounter % 20 == 0)
                {
                    theProfiler.startSection("timeSync");
                    serverConfigManager.sendPacketToAllPlayersInDimension(new Packet4UpdateTime(worldserver.func_82737_E(), worldserver.getWorldTime()), worldserver.provider.dimensionId);
                    theProfiler.endSection();
                }

                theProfiler.startSection("tick");
                worldserver.tick();
                worldserver.updateEntities();
                theProfiler.endSection();
                theProfiler.startSection("tracker");
                worldserver.getEntityTracker().updateTrackedEntities();
                theProfiler.endSection();
                theProfiler.endSection();
            }

            timeOfLastDimensionTick[i][tickCounter % 100] = System.nanoTime() - l;
        }

        theProfiler.endStartSection("connection");
        getNetworkThread().networkTick();
        theProfiler.endStartSection("players");
        serverConfigManager.sendPlayerInfoToAllPlayers();
        theProfiler.endStartSection("tickables");
        IUpdatePlayerListBox iupdateplayerlistbox;

        for (Iterator iterator = playersOnline.iterator(); iterator.hasNext(); iupdateplayerlistbox.update())
        {
            iupdateplayerlistbox = (IUpdatePlayerListBox)iterator.next();
        }

        theProfiler.endSection();
    }

    public boolean getAllowNether()
    {
        return true;
    }

    public void startServerThread()
    {
        (new ThreadMinecraftServer(this, "Server thread")).start();
    }

    /**
     * Returns a File object from the specified string.
     */
    public File getFile(String par1Str)
    {
        return new File(getDataDirectory(), par1Str);
    }

    /**
     * Logs the message with a level of INFO.
     */
    public void logInfo(String par1Str)
    {
        logger.info(par1Str);
    }

    /**
     * Logs the message with a level of WARN.
     */
    public void logWarning(String par1Str)
    {
        logger.warning(par1Str);
    }

    /**
     * Gets the worldServer by the given dimension.
     */
    public WorldServer worldServerForDimension(int par1)
    {
        if (par1 == -1)
        {
            return worldServers[1];
        }

        if (par1 == 1)
        {
            return worldServers[2];
        }
        else
        {
            return worldServers[0];
        }
    }

    /**
     * Returns the server's hostname.
     */
    public String getHostname()
    {
        return hostname;
    }

    /**
     * Never used, but "getServerPort" is already taken.
     */
    public int getPort()
    {
        return serverPort;
    }

    /**
     * minecraftServer.getMOTD is used in 2 places instead (it is a non-virtual function which returns the same thing)
     */
    public String getServerMOTD()
    {
        return motd;
    }

    /**
     * Returns the server's Minecraft version as string.
     */
    public String getMinecraftVersion()
    {
        return "1.4.2";
    }

    /**
     * Returns the number of players currently on the server.
     */
    public int getCurrentPlayerCount()
    {
        return serverConfigManager.getCurrentPlayerCount();
    }

    /**
     * Returns the maximum number of players allowed on the server.
     */
    public int getMaxPlayers()
    {
        return serverConfigManager.getMaxPlayers();
    }

    /**
     * Returns an array of the usernames of all the connected players.
     */
    public String[] getAllUsernames()
    {
        return serverConfigManager.getAllUsernames();
    }

    /**
     * Used by RCon's Query in the form of "MajorServerMod 1.2.3: MyPlugin 1.3; AnotherPlugin 2.1; AndSoForth 1.0".
     */
    public String getPlugins()
    {
        return "";
    }

    public String executeCommand(String par1Str)
    {
        RConConsoleSource.consoleBuffer.resetLog();
        commandManager.executeCommand(RConConsoleSource.consoleBuffer, par1Str);
        return RConConsoleSource.consoleBuffer.getChatBuffer();
    }

    /**
     * Returns true if debugging is enabled, false otherwise.
     */
    public boolean isDebuggingEnabled()
    {
        return false;
    }

    /**
     * Logs the error message with a level of SEVERE.
     */
    public void logSevere(String par1Str)
    {
        logger.log(Level.SEVERE, par1Str);
    }

    /**
     * If isDebuggingEnabled(), logs the message with a level of INFO.
     */
    public void logDebug(String par1Str)
    {
        if (isDebuggingEnabled())
        {
            logger.log(Level.INFO, par1Str);
        }
    }

    public String getServerModName()
    {
        return "vanilla";
    }

    /**
     * Adds the server info, including from theWorldServer, to the crash report.
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport.addCrashSectionCallable("Is Modded", new CallableIsServerModded(this));
        par1CrashReport.addCrashSectionCallable("Profiler Position", new CallableServerProfiler(this));

        if (worldServers != null && worldServers.length > 0 && worldServers[0] != null)
        {
            par1CrashReport.addCrashSectionCallable("Vec3 Pool Size", new CallableServerMemoryStats(this));
        }

        if (serverConfigManager != null)
        {
            par1CrashReport.addCrashSectionCallable("Player Count", new CallablePlayers(this));
        }

        if (worldServers != null)
        {
            WorldServer aworldserver[] = worldServers;
            int i = aworldserver.length;

            for (int j = 0; j < i; j++)
            {
                WorldServer worldserver = aworldserver[j];

                if (worldserver != null)
                {
                    worldserver.addWorldInfoToCrashReport(par1CrashReport);
                }
            }
        }

        return par1CrashReport;
    }

    /**
     * If par2Str begins with /, then it searches for commands, otherwise it returns players.
     */
    public List getPossibleCompletions(ICommandSender par1ICommandSender, String par2Str)
    {
        ArrayList arraylist = new ArrayList();

        if (par2Str.startsWith("/"))
        {
            par2Str = par2Str.substring(1);
            boolean flag = !par2Str.contains(" ");
            List list = commandManager.getPossibleCommands(par1ICommandSender, par2Str);

            if (list != null)
            {
                for (Iterator iterator = list.iterator(); iterator.hasNext();)
                {
                    String s1 = (String)iterator.next();

                    if (flag)
                    {
                        arraylist.add((new StringBuilder()).append("/").append(s1).toString());
                    }
                    else
                    {
                        arraylist.add(s1);
                    }
                }
            }

            return arraylist;
        }

        String as[] = par2Str.split(" ", -1);
        String s = as[as.length - 1];
        String as1[] = serverConfigManager.getAllUsernames();
        int i = as1.length;

        for (int j = 0; j < i; j++)
        {
            String s2 = as1[j];

            if (CommandBase.doesStringStartWith(s, s2))
            {
                arraylist.add(s2);
            }
        }

        return arraylist;
    }

    /**
     * Gets mcServer.
     */
    public static MinecraftServer getServer()
    {
        return mcServer;
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    public String getCommandSenderName()
    {
        return "Server";
    }

    public void sendChatToPlayer(String par1Str)
    {
        logger.info(StringUtils.stripControlCodes(par1Str));
    }

    /**
     * Returns true if the command sender is allowed to use the given command.
     */
    public boolean canCommandSenderUseCommand(int par1, String par2Str)
    {
        return true;
    }

    /**
     * Translates and formats the given string key with the given arguments.
     */
    public String translateString(String par1Str, Object par2ArrayOfObj[])
    {
        return StringTranslate.getInstance().translateKeyFormat(par1Str, par2ArrayOfObj);
    }

    public ICommandManager getCommandManager()
    {
        return commandManager;
    }

    /**
     * Gets KeyPair instanced in MinecraftServer.
     */
    public KeyPair getKeyPair()
    {
        return serverKeyPair;
    }

    /**
     * Gets serverPort.
     */
    public int getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(int par1)
    {
        serverPort = par1;
    }

    /**
     * Returns the username of the server owner (for integrated servers)
     */
    public String getServerOwner()
    {
        return serverOwner;
    }

    /**
     * Sets the username of the owner of this server (in the case of an integrated server)
     */
    public void setServerOwner(String par1Str)
    {
        serverOwner = par1Str;
    }

    public boolean isSinglePlayer()
    {
        return serverOwner != null;
    }

    public String getFolderName()
    {
        return folderName;
    }

    public void setFolderName(String par1Str)
    {
        folderName = par1Str;
    }

    public void setWorldName(String par1Str)
    {
        worldName = par1Str;
    }

    public String getWorldName()
    {
        return worldName;
    }

    public void setKeyPair(KeyPair par1KeyPair)
    {
        serverKeyPair = par1KeyPair;
    }

    public void setDifficultyForAllWorlds(int par1)
    {
        for (int i = 0; i < worldServers.length; i++)
        {
            WorldServer worldserver = worldServers[i];

            if (worldserver == null)
            {
                continue;
            }

            if (worldserver.getWorldInfo().isHardcoreModeEnabled())
            {
                worldserver.difficultySetting = 3;
                worldserver.setAllowedSpawnTypes(true, true);
                continue;
            }

            if (isSinglePlayer())
            {
                worldserver.difficultySetting = par1;
                worldserver.setAllowedSpawnTypes(((World)(worldserver)).difficultySetting > 0, true);
            }
            else
            {
                worldserver.difficultySetting = par1;
                worldserver.setAllowedSpawnTypes(allowSpawnMonsters(), canSpawnAnimals);
            }
        }
    }

    protected boolean allowSpawnMonsters()
    {
        return true;
    }

    /**
     * Gets whether this is a demo or not.
     */
    public boolean isDemo()
    {
        return isDemo;
    }

    /**
     * Sets whether this is a demo or not.
     */
    public void setDemo(boolean par1)
    {
        isDemo = par1;
    }

    public void canCreateBonusChest(boolean par1)
    {
        enableBonusChest = par1;
    }

    public ISaveFormat getActiveAnvilConverter()
    {
        return anvilConverterForAnvilFile;
    }

    /**
     * WARNING : directly calls
     * getActiveAnvilConverter().deleteWorldDirectory(theWorldServer[0].getSaveHandler().getSaveDirectoryName());
     */
    public void deleteWorldAndStopServer()
    {
        worldIsBeingDeleted = true;
        getActiveAnvilConverter().flushCache();

        for (int i = 0; i < worldServers.length; i++)
        {
            WorldServer worldserver = worldServers[i];

            if (worldserver != null)
            {
                worldserver.flush();
            }
        }

        getActiveAnvilConverter().deleteWorldDirectory(worldServers[0].getSaveHandler().getSaveDirectoryName());
        initiateShutdown();
    }

    public String getTexturePack()
    {
        return texturePack;
    }

    public void setTexturePack(String par1Str)
    {
        texturePack = par1Str;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(false));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(0));
        par1PlayerUsageSnooper.addData("players_current", Integer.valueOf(getCurrentPlayerCount()));
        par1PlayerUsageSnooper.addData("players_max", Integer.valueOf(getMaxPlayers()));
        par1PlayerUsageSnooper.addData("players_seen", Integer.valueOf(serverConfigManager.getAvailablePlayerDat().length));
        par1PlayerUsageSnooper.addData("uses_auth", Boolean.valueOf(onlineMode));
        par1PlayerUsageSnooper.addData("gui_state", getGuiEnabled() ? "enabled" : "disabled");
        par1PlayerUsageSnooper.addData("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(tickTimeArray) * 9.9999999999999995E-007D)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_count", Integer.valueOf((int)MathHelper.average(sentPacketCountArray)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_size", Integer.valueOf((int)MathHelper.average(sentPacketSizeArray)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_count", Integer.valueOf((int)MathHelper.average(receivedPacketCountArray)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_size", Integer.valueOf((int)MathHelper.average(receivedPacketSizeArray)));
        int i = 0;

        for (int j = 0; j < worldServers.length; j++)
        {
            if (worldServers[j] != null)
            {
                WorldServer worldserver = worldServers[j];
                WorldInfo worldinfo = worldserver.getWorldInfo();
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][dimension]").toString(), Integer.valueOf(worldserver.provider.dimensionId));
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][mode]").toString(), worldinfo.getGameType());
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][difficulty]").toString(), Integer.valueOf(worldserver.difficultySetting));
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][hardcore]").toString(), Boolean.valueOf(worldinfo.isHardcoreModeEnabled()));
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][generator_name]").toString(), worldinfo.getTerrainType().getWorldTypeName());
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][generator_version]").toString(), Integer.valueOf(worldinfo.getTerrainType().getGeneratorVersion()));
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][height]").toString(), Integer.valueOf(buildLimit));
                par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[").append(i).append("][chunks_loaded]").toString(), Integer.valueOf(worldserver.getChunkProvider().getLoadedChunkCount()));
                i++;
            }
        }

        par1PlayerUsageSnooper.addData("worlds", Integer.valueOf(i));
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("singleplayer", Boolean.valueOf(isSinglePlayer()));
        par1PlayerUsageSnooper.addData("server_brand", getServerModName());
        par1PlayerUsageSnooper.addData("gui_supported", java.awt.GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        par1PlayerUsageSnooper.addData("dedicated", Boolean.valueOf(isDedicatedServer()));
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return true;
    }

    /**
     * This is checked to be 16 upon receiving the packet, otherwise the packet is ignored.
     */
    public int textureSize()
    {
        return 16;
    }

    public abstract boolean isDedicatedServer();

    public boolean isServerInOnlineMode()
    {
        return onlineMode;
    }

    public void setOnlineMode(boolean par1)
    {
        onlineMode = par1;
    }

    public boolean getCanSpawnAnimals()
    {
        return canSpawnAnimals;
    }

    public void setCanSpawnAnimals(boolean par1)
    {
        canSpawnAnimals = par1;
    }

    public boolean getCanSpawnNPCs()
    {
        return canSpawnNPCs;
    }

    public void setCanSpawnNPCs(boolean par1)
    {
        canSpawnNPCs = par1;
    }

    public boolean isPVPEnabled()
    {
        return pvpEnabled;
    }

    public void setAllowPvp(boolean par1)
    {
        pvpEnabled = par1;
    }

    public boolean isFlightAllowed()
    {
        return allowFlight;
    }

    public void setAllowFlight(boolean par1)
    {
        allowFlight = par1;
    }

    public abstract boolean func_82356_Z();

    public String getMOTD()
    {
        return motd;
    }

    public void setMOTD(String par1Str)
    {
        motd = par1Str;
    }

    public int getBuildLimit()
    {
        return buildLimit;
    }

    public void setBuildLimit(int par1)
    {
        buildLimit = par1;
    }

    public boolean isServerStopped()
    {
        return serverStopped;
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return serverConfigManager;
    }

    public void setConfigurationManager(ServerConfigurationManager par1ServerConfigurationManager)
    {
        serverConfigManager = par1ServerConfigurationManager;
    }

    /**
     * Sets the game type for all worlds.
     */
    public void setGameType(EnumGameType par1EnumGameType)
    {
        for (int i = 0; i < worldServers.length; i++)
        {
            getServer().worldServers[i].getWorldInfo().setGameType(par1EnumGameType);
        }
    }

    public abstract NetworkListenThread getNetworkThread();

    public boolean serverIsInRunLoop()
    {
        return serverIsRunning;
    }

    public boolean getGuiEnabled()
    {
        return false;
    }

    /**
     * On dedicated does nothing. On integrated, sets commandsAllowedForAll, gameType and allows external connections.
     */
    public abstract String shareToLAN(EnumGameType enumgametype, boolean flag);

    public int getTickCounter()
    {
        return tickCounter;
    }

    public void enableProfiling()
    {
        startProfiling = true;
    }

    public PlayerUsageSnooper getPlayerUsageSnooper()
    {
        return usageSnooper;
    }

    public ChunkCoordinates func_82114_b()
    {
        return new ChunkCoordinates(0, 0, 0);
    }

    public int func_82357_ak()
    {
        return 16;
    }

    /**
     * Gets the current player count, maximum player count, and player entity list.
     */
    public static ServerConfigurationManager getServerConfigurationManager(MinecraftServer par0MinecraftServer)
    {
        return par0MinecraftServer.serverConfigManager;
    }

    public void setUserMessage2(String str){
        setUserMessage(str);
    }
}
