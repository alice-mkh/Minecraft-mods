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
        setConfigurationManager(new IntegratedPlayerList(this));
        mc = par1Minecraft;
        field_71350_m = par4WorldSettings;
        commandManager2 = new ClientCommandManager();
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
        par1CrashReport.addCrashSectionCallable("Type", new CallableTypeFake(this));
        par1CrashReport.addCrashSectionCallable("Is Modded", new CallableIsModded(this));
        return par1CrashReport;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        super.addServerStatsToSnooper(par1PlayerUsageSnooper);
        par1PlayerUsageSnooper.addData("snooper_partner", mc.getPlayerUsageSnooper().func_80006_f());
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

    public boolean func_71344_c()
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
