package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class DedicatedServer extends MinecraftServer implements IServer
{
    private final List pendingCommandList = Collections.synchronizedList(new ArrayList());
    private RConThreadQuery field_71342_m;
    private RConThreadMain field_71339_n;
    private PropertyManager settings;
    private boolean canSpawnStructures;
    private EnumGameType gameType;
    private NetworkListenThread networkThread;
    private boolean guiIsEnabled;

    public DedicatedServer(File par1File)
    {
        super(par1File);
        guiIsEnabled = false;
        new DedicatedServerSleepThread(this);
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        DedicatedServerCommandThread dedicatedservercommandthread = new DedicatedServerCommandThread(this);
        dedicatedservercommandthread.setDaemon(true);
        dedicatedservercommandthread.start();
        ConsoleLogManager.func_73699_a();
        logger.info("Starting minecraft server version 1.3.2");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            logger.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        logger.info("Loading properties");
        settings = new PropertyManager(new File("server.properties"));

        if (isSinglePlayer())
        {
            getHostName("127.0.0.1");
        }
        else
        {
            setOnlineMode(settings.getOrSetBoolProperty("online-mode", true));
            getHostName(settings.getOrSetProperty("server-ip", ""));
        }

        setSpawnAnimals(settings.getOrSetBoolProperty("spawn-animals", true));
        setSpawnNpcs(settings.getOrSetBoolProperty("spawn-npcs", true));
        setAllowPvp(settings.getOrSetBoolProperty("pvp", true));
        setAllowFlight(settings.getOrSetBoolProperty("allow-flight", false));
        setTexturePack(settings.getOrSetProperty("texture-pack", ""));
        setMOTD(settings.getOrSetProperty("motd", "A Minecraft Server"));
        canSpawnStructures = settings.getOrSetBoolProperty("generate-structures", true);
        int i = settings.getOrSetIntProperty("gamemode", EnumGameType.SURVIVAL.getID());
        gameType = WorldSettings.getGameTypeById(i);
        logger.info((new StringBuilder()).append("Default game type: ").append(gameType).toString());
        InetAddress inetaddress = null;

        if (getHostname().length() > 0)
        {
            inetaddress = InetAddress.getByName(getHostname());
        }

        if (getServerPort() < 0)
        {
            setServerPort(settings.getOrSetIntProperty("server-port", 25565));
        }

        logger.info("Generating keypair");
        setKeyPair(CryptManager.createNewKeyPair());
        logger.info((new StringBuilder()).append("Starting Minecraft server on ").append(getHostname().length() != 0 ? getHostname() : "*").append(":").append(getServerPort()).toString());

        try
        {
            networkThread = new DedicatedServerListenThread(this, inetaddress, getServerPort());
        }
        catch (IOException ioexception)
        {
            logger.warning("**** FAILED TO BIND TO PORT!");
            logger.log(Level.WARNING, (new StringBuilder()).append("The exception was: ").append(ioexception.toString()).toString());
            logger.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!isServerInOnlineMode())
        {
            logger.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            logger.warning("The server will make no attempt to authenticate usernames. Beware.");
            logger.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            logger.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        setConfigurationManager(new DedicatedPlayerList(this));
        long l = System.nanoTime();

        if (getFolderName() == null)
        {
            setFolderName(settings.getOrSetProperty("level-name", "world"));
        }

        String s = settings.getOrSetProperty("level-seed", "");
        String s1 = settings.getOrSetProperty("level-type", "DEFAULT");
        long l1 = (new Random()).nextLong();

        if (s.length() > 0)
        {
            try
            {
                long l2 = Long.parseLong(s);

                if (l2 != 0L)
                {
                    l1 = l2;
                }
            }
            catch (NumberFormatException numberformatexception)
            {
                l1 = s.hashCode();
            }
        }

        WorldType worldtype = WorldType.parseWorldType(s1);

        if (worldtype == null)
        {
            worldtype = WorldType.DEFAULT;
        }

        setBuildLimit(settings.getOrSetIntProperty("max-build-height", 256));
        setBuildLimit(((getBuildLimit() + 8) / 16) * 16);
        setBuildLimit(MathHelper.clamp_int(getBuildLimit(), 64, 256));
        settings.setArbitraryProperty("max-build-height", Integer.valueOf(getBuildLimit()));
        logger.info((new StringBuilder()).append("Preparing level \"").append(getFolderName()).append("\"").toString());
        loadAllDimensions(getFolderName(), getFolderName(), l1, worldtype);
        long l3 = System.nanoTime() - l;
        String s2 = String.format("%.3fs", new Object[]
                {
                    Double.valueOf((double)l3 / 1000000000D)
                });
        logger.info((new StringBuilder()).append("Done (").append(s2).append(")! For help, type \"help\" or \"?\"").toString());

        if (settings.getOrSetBoolProperty("enable-query", false))
        {
            logger.info("Starting GS4 status listener");
            field_71342_m = new RConThreadQuery(this);
            field_71342_m.func_72602_a();
        }

        if (settings.getOrSetBoolProperty("enable-rcon", false))
        {
            logger.info("Starting remote control listener");
            field_71339_n = new RConThreadMain(this);
            field_71339_n.func_72602_a();
        }

        return true;
    }

    public boolean canStructuresSpawn()
    {
        return canSpawnStructures;
    }

    public EnumGameType getGameType()
    {
        return gameType;
    }

    /**
     * defaults to "1" for the dedicated server
     */
    public int getDifficulty()
    {
        return settings.getOrSetIntProperty("difficulty", 1);
    }

    /**
     * defaults to false
     */
    public boolean isHardcore()
    {
        return settings.getOrSetBoolProperty("hardcore", false);
    }

    /**
     * called on exit from the main run loop
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        while (isServerRunning())
        {
            executePendingCommands();

            try
            {
                Thread.sleep(10L);
            }
            catch (InterruptedException interruptedexception)
            {
                interruptedexception.printStackTrace();
            }
        }
    }

    /**
     * iterates the worldServers and adds their info also
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.addCrashSectionCallable("Type", new CallableType(this));
        return par1CrashReport;
    }

    /**
     * directly calls system.exit, instantly killing the program
     */
    protected void systemExitNow()
    {
        System.exit(0);
    }

    public void updateTimeLightAndEntities()
    {
        super.updateTimeLightAndEntities();
        executePendingCommands();
    }

    public boolean getAllowNether()
    {
        return settings.getOrSetBoolProperty("allow-nether", true);
    }

    public boolean allowSpawnMonsters()
    {
        return settings.getOrSetBoolProperty("spawn-monsters", true);
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(getDedicatedPlayerList().isWhiteListEnabled()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(getDedicatedPlayerList().getIPWhiteList().size()));
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return settings.getOrSetBoolProperty("snooper-enabled", true);
    }

    public void addPendingCommand(String par1Str, ICommandSender par2ICommandSender)
    {
        pendingCommandList.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void executePendingCommands()
    {
        ServerCommand servercommand;

        for (; !pendingCommandList.isEmpty(); getCommandManager().executeCommand(servercommand.sender, servercommand.command))
        {
            servercommand = (ServerCommand)pendingCommandList.remove(0);
        }
    }

    public boolean isDedicatedServer()
    {
        return true;
    }

    public DedicatedPlayerList getDedicatedPlayerList()
    {
        return (DedicatedPlayerList)super.getConfigurationManager();
    }

    public NetworkListenThread getNetworkThread()
    {
        return networkThread;
    }

    public int getOrSetIntProperty(String par1Str, int par2)
    {
        return settings.getOrSetIntProperty(par1Str, par2);
    }

    public String getOrSetProperty(String par1Str, String par2Str)
    {
        return settings.getOrSetProperty(par1Str, par2Str);
    }

    public boolean getOrSetBoolProperty(String par1Str, boolean par2)
    {
        return settings.getOrSetBoolProperty(par1Str, par2);
    }

    public void setArbitraryProperty(String par1Str, Object par2Obj)
    {
        settings.setArbitraryProperty(par1Str, par2Obj);
    }

    public void saveSettingsToFile()
    {
        settings.saveSettingsToFile();
    }

    public String getSettingsFilePath()
    {
        File file = settings.getFile();

        if (file != null)
        {
            return file.getAbsolutePath();
        }
        else
        {
            return "No settings file";
        }
    }

    public boolean getGuiEnabled()
    {
        return guiIsEnabled;
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    public ServerConfigurationManager getConfigurationManager()
    {
        return getDedicatedPlayerList();
    }

    public void func_79001_aj()
    {
        ServerGUI.func_79003_a(this);
        guiIsEnabled = true;
    }
}
