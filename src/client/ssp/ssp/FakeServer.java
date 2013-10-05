package net.minecraft.src.ssp;

import java.io.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;
// import net.minecraft.src.CallableModded;
import net.minecraft.src.CrashReport;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ILogAgent;
import net.minecraft.src.IntegratedServer;
import net.minecraft.src.IntegratedServerListenThread;
import net.minecraft.src.Minecraft;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NetworkListenThread;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.ThreadLanServerPing;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldServer;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;

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
        setServerOwner(par1Minecraft.getSession().getUsername());
        setFolderName(par2Str);
        setWorldName(par3Str);
        setDemo(par1Minecraft.isDemo());
        canCreateBonusChest(par4WorldSettings.isBonusChestEnabled());
        setBuildLimit(256);
        mc = par1Minecraft;
        field_71350_m = par4WorldSettings;
        worldServers = new WorldServer[3];
//         for (int i = 0; i < worldServers.length; i++){
//             worldServers[i] = new FakeWorldServer(this, (WorldSSP)par1Minecraft.theWorld, par4WorldSettings);
//             worldServers[i].addWorldAccess(par1Minecraft.renderGlobal);
//         }
    }

    @Override
    protected void loadAllWorlds(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str)
    {
    }

    /**
     * Initialises the server and starts it.
     */
    @Override
    protected boolean startServer() throws IOException
    {
        return true;
    }

    /**
     * main function called by run() every loop
     */
    @Override
    public void tick()
    {
    }

    @Override
    public void updateTimeLightAndEntities(){}

    @Override
    public boolean canStructuresSpawn()
    {
        return false;
    }

    @Override
    public EnumGameType getGameType()
    {
        return field_71350_m.getGameType();
    }

    /**
     * defaults to "1" for the dedicated server
     */
    @Override
    public int getDifficulty()
    {
        return mc.gameSettings.difficulty;
    }

    /**
     * defaults to false
     */
    @Override
    public boolean isHardcore()
    {
        return field_71350_m.getHardcoreEnabled();
    }

    @Override
    protected File getDataDirectory()
    {
        return mc.mcDataDir;
    }

    @Override
    public boolean isDedicatedServer()
    {
        return false;
    }

    @Override
    public IntegratedServerListenThread getServerListeningThread()
    {
        return null;
    }

    /**
     * called on exit from the main run loop
     */
    @Override
    protected void finalTick(CrashReport par1CrashReport)
    {
        mc.crashed(par1CrashReport);
    }

    /**
     * iterates the worldServers and adds their info also
     */
    @Override
    public CrashReport addServerInfoToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport = super.addServerInfoToCrashReport(par1CrashReport);
        par1CrashReport.getCategory().addCrashSectionCallable("Type", new CallableTypeFake(this));
//         par1CrashReport.getCategory().addCrashSectionCallable("Is Modded", new CallableModded(mc));
        return par1CrashReport;
    }

    @Override
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

    @Override
    public boolean isServerInOnlineMode()
    {
        return false;
    }

    /**
     * Returns whether snooping is enabled or not.
     */
    @Override
    public boolean isSnooperEnabled()
    {
        return Minecraft.getMinecraft().isSnooperEnabled();
    }

    /**
     * does nothing on dedicated. on integrated, sets commandsAllowedForAll and gameType and allows external connections
     */
    @Override
    public String shareToLAN(EnumGameType par1EnumGameType, boolean par2)
    {
        return null;
    }

    /**
     * Saves all necessary data as preparation for stopping the server.
     */
    @Override
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
    @Override
    public void initiateShutdown()
    {
        if (field_71345_q != null)
        {
            field_71345_q.interrupt();
            field_71345_q = null;
        }
    }

    @Override
    public boolean getPublic()
    {
        return false;
    }

    @Override
    public NetworkListenThread getNetworkThread()
    {
        return getServerListeningThread();
    }

    @Override
    protected void saveAllWorlds(boolean par1){
    }

    @Override
    public void startServerThread()
    {
    }

    @Override
    public int getCurrentPlayerCount()
    {
        return 1;
    }

    @Override
    public int getMaxPlayers()
    {
        return 1;
    }

    @Override
    public String[] getAllUsernames()
    {
        return new String[]{mc.getSession().getUsername()};
    }

    @Override
    public boolean isSinglePlayer()
    {
        return true;
    }

    @Override
    public void setGameType(EnumGameType par1EnumGameType)
    {
        getConfigurationManager().setGameType(par1EnumGameType);
        mc.theWorld.getWorldInfo().setGameType(par1EnumGameType);
    }

    @Override
    public ICommandManager getCommandManager()
    {
        return commandManager2;
    }

    public void setCommandManager(ICommandManager i)
    {
        commandManager2 = i;
    }

    @Override
    public ILogAgent getLogAgent(){
        return Minecraft.getMinecraft().getLogAgent();
    }
}
