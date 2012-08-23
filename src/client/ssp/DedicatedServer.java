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
    private final List field_71341_l = Collections.synchronizedList(new ArrayList());
    private RConThreadQuery field_71342_m;
    private RConThreadMain field_71339_n;
    private PropertyManager field_71340_o;
    private boolean field_71338_p;
    private EnumGameType field_71337_q;
    private NetworkListenThread field_71336_r;
    private boolean field_71335_s;

    public DedicatedServer(File par1File)
    {
        super(par1File);
        field_71335_s = false;
        new DedicatedServerSleepThread(this);
    }

    protected boolean func_71197_b() throws IOException
    {
        DedicatedServerCommandThread dedicatedservercommandthread = new DedicatedServerCommandThread(this);
        dedicatedservercommandthread.setDaemon(true);
        dedicatedservercommandthread.start();
        ConsoleLogManager.func_73699_a();
        field_71306_a.info("Starting minecraft server version 1.3.1");

        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L)
        {
            field_71306_a.warning("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }

        field_71306_a.info("Loading properties");
        field_71340_o = new PropertyManager(new File("server.properties"));

        if (func_71264_H())
        {
            func_71189_e("127.0.0.1");
        }
        else
        {
            func_71229_d(field_71340_o.func_73670_a("online-mode", true));
            func_71189_e(field_71340_o.func_73671_a("server-ip", ""));
        }

        func_71251_e(field_71340_o.func_73670_a("spawn-animals", true));
        func_71257_f(field_71340_o.func_73670_a("spawn-npcs", true));
        func_71188_g(field_71340_o.func_73670_a("pvp", true));
        func_71245_h(field_71340_o.func_73670_a("allow-flight", false));
        func_71269_o(field_71340_o.func_73671_a("texture-pack", ""));
        func_71205_p(field_71340_o.func_73671_a("motd", "A Minecraft Server"));
        field_71338_p = field_71340_o.func_73670_a("generate-structures", true);
        int i = field_71340_o.func_73669_a("gamemode", EnumGameType.SURVIVAL.func_77148_a());
        field_71337_q = WorldSettings.func_77161_a(i);
        field_71306_a.info((new StringBuilder()).append("Default game type: ").append(field_71337_q).toString());
        InetAddress inetaddress = null;

        if (func_71211_k().length() > 0)
        {
            inetaddress = InetAddress.getByName(func_71211_k());
        }

        if (func_71215_F() < 0)
        {
            func_71208_b(field_71340_o.func_73669_a("server-port", 25565));
        }

        field_71306_a.info("Generating keypair");
        func_71253_a(CryptManager.func_75891_b());
        field_71306_a.info((new StringBuilder()).append("Starting Minecraft server on ").append(func_71211_k().length() != 0 ? func_71211_k() : "*").append(":").append(func_71215_F()).toString());

        try
        {
            field_71336_r = new DedicatedServerListenThread(this, inetaddress, func_71215_F());
        }
        catch (IOException ioexception)
        {
            field_71306_a.warning("**** FAILED TO BIND TO PORT!");
            field_71306_a.log(Level.WARNING, (new StringBuilder()).append("The exception was: ").append(ioexception.toString()).toString());
            field_71306_a.warning("Perhaps a server is already running on that port?");
            return false;
        }

        if (!func_71266_T())
        {
            field_71306_a.warning("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            field_71306_a.warning("The server will make no attempt to authenticate usernames. Beware.");
            field_71306_a.warning("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            field_71306_a.warning("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }

        func_71210_a(new DedicatedPlayerList(this));
        long l = System.nanoTime();

        if (func_71270_I() == null)
        {
            func_71261_m(field_71340_o.func_73671_a("level-name", "world"));
        }

        String s = field_71340_o.func_73671_a("level-seed", "");
        String s1 = field_71340_o.func_73671_a("level-type", "DEFAULT");
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

        func_71191_d(field_71340_o.func_73669_a("max-build-height", 256));
        func_71191_d(((func_71207_Z() + 8) / 16) * 16);
        func_71191_d(MathHelper.clamp_int(func_71207_Z(), 64, 256));
        field_71340_o.func_73667_a("max-build-height", Integer.valueOf(func_71207_Z()));
        field_71306_a.info((new StringBuilder()).append("Preparing level \"").append(func_71270_I()).append("\"").toString());
        func_71247_a(func_71270_I(), func_71270_I(), l1, worldtype);
        long l3 = System.nanoTime() - l;
        String s2 = String.format("%.3fs", new Object[]
                {
                    Double.valueOf((double)l3 / 1000000000D)
                });
        field_71306_a.info((new StringBuilder()).append("Done (").append(s2).append(")! For help, type \"help\" or \"?\"").toString());

        if (field_71340_o.func_73670_a("enable-query", false))
        {
            field_71306_a.info("Starting GS4 status listener");
            field_71342_m = new RConThreadQuery(this);
            field_71342_m.func_72602_a();
        }

        if (field_71340_o.func_73670_a("enable-rcon", false))
        {
            field_71306_a.info("Starting remote control listener");
            field_71339_n = new RConThreadMain(this);
            field_71339_n.func_72602_a();
        }

        return true;
    }

    public boolean func_71225_e()
    {
        return field_71338_p;
    }

    public EnumGameType func_71265_f()
    {
        return field_71337_q;
    }

    public int func_71232_g()
    {
        return field_71340_o.func_73669_a("difficulty", 1);
    }

    public boolean func_71199_h()
    {
        return field_71340_o.func_73670_a("hardcore", false);
    }

    protected void func_71228_a(CrashReport par1CrashReport)
    {
        while (func_71278_l())
        {
            func_71333_ah();

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

    public CrashReport func_71230_b(CrashReport par1CrashReport)
    {
        par1CrashReport.func_71500_a("Type", new CallableType(this));
        return super.func_71230_b(par1CrashReport);
    }

    protected void func_71240_o()
    {
        System.exit(0);
    }

    public void func_71190_q()
    {
        super.func_71190_q();
        func_71333_ah();
    }

    public boolean func_71255_r()
    {
        return field_71340_o.func_73670_a("allow-nether", true);
    }

    public boolean func_71193_K()
    {
        return field_71340_o.func_73670_a("spawn-monsters", true);
    }

    public void func_70000_a(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("whitelist_enabled", Boolean.valueOf(func_71334_ai().func_72383_n()));
        par1PlayerUsageSnooper.addData("whitelist_count", Integer.valueOf(func_71334_ai().func_72388_h().size()));
        super.func_70000_a(par1PlayerUsageSnooper);
    }

    public boolean func_70002_Q()
    {
        return field_71340_o.func_73670_a("snooper-enabled", true);
    }

    public void func_71331_a(String par1Str, ICommandSender par2ICommandSender)
    {
        field_71341_l.add(new ServerCommand(par1Str, par2ICommandSender));
    }

    public void func_71333_ah()
    {
        ServerCommand servercommand;

        for (; !field_71341_l.isEmpty(); func_71187_D().func_71556_a(servercommand.field_73701_b, servercommand.field_73702_a))
        {
            servercommand = (ServerCommand)field_71341_l.remove(0);
        }
    }

    public boolean func_71262_S()
    {
        return true;
    }

    public DedicatedPlayerList func_71334_ai()
    {
        return (DedicatedPlayerList)super.func_71203_ab();
    }

    public NetworkListenThread func_71212_ac()
    {
        return field_71336_r;
    }

    public int func_71327_a(String par1Str, int par2)
    {
        return field_71340_o.func_73669_a(par1Str, par2);
    }

    public String func_71330_a(String par1Str, String par2Str)
    {
        return field_71340_o.func_73671_a(par1Str, par2Str);
    }

    public boolean func_71332_a(String par1Str, boolean par2)
    {
        return field_71340_o.func_73670_a(par1Str, par2);
    }

    public void func_71328_a(String par1Str, Object par2Obj)
    {
        field_71340_o.func_73667_a(par1Str, par2Obj);
    }

    public void func_71326_a()
    {
        field_71340_o.func_73668_b();
    }

    public String func_71329_c()
    {
        File file = field_71340_o.func_73665_c();

        if (file != null)
        {
            return file.getAbsolutePath();
        }
        else
        {
            return "No settings file";
        }
    }

    public boolean func_71279_ae()
    {
        return field_71335_s;
    }

    public String func_71206_a(EnumGameType par1EnumGameType, boolean par2)
    {
        return "";
    }

    public ServerConfigurationManager func_71203_ab()
    {
        return func_71334_ai();
    }

    public void func_79001_aj()
    {
        ServerGUI.func_79003_a(this);
        field_71335_s = true;
    }
}
