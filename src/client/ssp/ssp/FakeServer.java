package net.minecraft.src;

import java.io.*;
import java.util.logging.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public class FakeServer extends IntegratedServer
{
    /** The Minecraft instance. */
    private final Minecraft mc;
    private final WorldSettings field_71350_m;
    private boolean field_71348_o;
    private ThreadLanServerPing field_71345_q;
    private ICommandManager commandManager2;

    public FakeServer(Minecraft par1Minecraft, String par2Str, String par3Str, WorldSettings par4WorldSettings)
    {
        super(par1Minecraft, par2Str, par3Str, par4WorldSettings);
        field_71348_o = false;
        setServerOwner(par1Minecraft.session.username);
        setFolderName(par2Str);
        setWorldName(par3Str);
        setDemo(par1Minecraft.isDemo());
        canCreateBonusChest(par4WorldSettings.isBonusChestEnabled());
        setBuildLimit(256);
        setConfigurationManager(new FakeServerPlayerList(this));
        mc = par1Minecraft;
        field_71350_m = par4WorldSettings;
        commandManager2 = new ClientCommandManager();
        worldServers = new WorldServer[3];
//         for (int i = 0; i < worldServers.length; i++){
//             worldServers[i] = new FakeWorldServer(this, (WorldSSP)par1Minecraft.theWorld, par4WorldSettings);
//             worldServers[i].addWorldAccess(par1Minecraft.renderGlobal);
//         }
    }

    protected void loadAllDimensions(String par1Str, String par2Str, long par3, WorldType par5WorldType)
    {
    }

    /**
     * Initialises the server and starts it.
     */
    protected boolean startServer() throws IOException
    {
        return true;
    }

    /**
     * main function called by run() every loop
     */
    public void tick()
    {
    }

    public void updateTimeLightAndEntities(){}

    public boolean canStructuresSpawn()
    {
        return false;
    }

    public EnumGameType getGameType()
    {
        return field_71350_m.getGameType();
    }

    /**
     * defaults to "1" for the dedicated server
     */
    public int getDifficulty()
    {
        return mc.gameSettings.difficulty;
    }

    /**
     * defaults to false
     */
    public boolean isHardcore()
    {
        return field_71350_m.getHardcoreEnabled();
    }

    protected File getDataDirectory()
    {
        return mc.mcDataDir;
    }

    public boolean isDedicatedServer()
    {
        return false;
    }

    public IntegratedServerListenThread func_71343_a()
    {
        return null;
    }

    /**
     * called on exit from the main run loop
     */
    protected void finalTick(CrashReport par1CrashReport)
    {
        mc.crashed(par1CrashReport);
    }

    /**
     * iterates the worldServers and adds their info also
     */
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.func_85056_g().addCrashSectionCallable("Type", new CallableTypeFake(this));
        par1CrashReport.func_85056_g().addCrashSectionCallable("Is Modded", new CallableIsModded(this));
        return par1CrashReport;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(false));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(0));
        par1PlayerUsageSnooper.addData("players_current", Integer.valueOf(getCurrentPlayerCount()));
        par1PlayerUsageSnooper.addData("players_max", Integer.valueOf(getMaxPlayers()));
        par1PlayerUsageSnooper.addData("players_seen", Integer.valueOf(getConfigurationManager().getAvailablePlayerDat().length));
        par1PlayerUsageSnooper.addData("uses_auth", Boolean.valueOf(isServerInOnlineMode()));
        par1PlayerUsageSnooper.addData("gui_state", getGuiEnabled() ? "enabled" : "disabled");
        par1PlayerUsageSnooper.addData("avg_tick_ms", Integer.valueOf((int)(MathHelper.average(tickTimeArray) * 9.9999999999999995E-007D)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_count", Integer.valueOf((int)MathHelper.average(sentPacketCountArray)));
        par1PlayerUsageSnooper.addData("avg_sent_packet_size", Integer.valueOf((int)MathHelper.average(sentPacketSizeArray)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_count", Integer.valueOf((int)MathHelper.average(receivedPacketCountArray)));
        par1PlayerUsageSnooper.addData("avg_rec_packet_size", Integer.valueOf((int)MathHelper.average(receivedPacketSizeArray)));

        World world = Minecraft.getMinecraft().theWorld;
        if (world != null)
        {
            WorldInfo worldinfo = world.getWorldInfo();
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[dimension]").toString(), Integer.valueOf(world.provider.dimensionId));
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[mode]").toString(), worldinfo.getGameType());
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[difficulty]").toString(), Integer.valueOf(world.difficultySetting));
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[hardcore]").toString(), Boolean.valueOf(worldinfo.isHardcoreModeEnabled()));
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[generator_name]").toString(), worldinfo.getTerrainType().getWorldTypeName());
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[generator_version]").toString(), Integer.valueOf(worldinfo.getTerrainType().getGeneratorVersion()));
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[height]").toString(), Integer.valueOf(getBuildLimit()));
            par1PlayerUsageSnooper.addData((new StringBuilder()).append("world[chunks_loaded]").toString(), Integer.valueOf(world.getChunkProvider().getLoadedChunkCount()));
        }

        par1PlayerUsageSnooper.addData("worlds", Integer.valueOf(1));
        par1PlayerUsageSnooper.addData("snooper_partner", mc.getPlayerUsageSnooper().getUniqueID());
    }

    public boolean isServerInOnlineMode()
    {
        return false;
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return null;
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    public void stopServer()
    {
        if (field_71345_q != null)
        {
            field_71345_q.interrupt();
            field_71345_q = null;
        }
    }

    /**
     * sets serverRunning to false
     */
    public void setServerStopping()
    {
        if (field_71345_q != null)
        {
            field_71345_q.interrupt();
            field_71345_q = null;
        }
    }

    public boolean getPublic()
    {
        return false;
    }

    public NetworkListenThread getNetworkThread()
    {
        return func_71343_a();
    }

    protected void saveAllDimensions(boolean par1){
    }

    public void startServerThread()
    {
    }

    public int getPlayerListSize()
    {
        return 1;
    }

    public int getMaxPlayers()
    {
        return 1;
    }

    public String[] getAllUsernames()
    {
        return new String[]{mc.session.username};
    }

    public boolean isSinglePlayer()
    {
        return true;
    }

    public void setGameType(EnumGameType par1EnumGameType)
    {
        getConfigurationManager().setGameType(par1EnumGameType);
        mc.theWorld.getWorldInfo().setGameType(par1EnumGameType);
    }

    public ICommandManager getCommandManager()
    {
        return commandManager2;
    }
}
