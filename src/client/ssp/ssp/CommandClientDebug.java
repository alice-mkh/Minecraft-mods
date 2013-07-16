package net.minecraft.src.ssp;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.src.CommandDebug;
import net.minecraft.src.CommandException;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Profiler;
import net.minecraft.src.ProfilerResult;
import net.minecraft.src.WrongUsageException;

public class CommandClientDebug extends CommandDebug
{
    private long startTime;
    private int startTicks;

    public CommandClientDebug()
    {
        startTime = 0L;
        startTicks = 0;
    }

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            if (par2ArrayOfStr[0].equals("start"))
            {
                notifyAdmins(par1ICommandSender, "commands.debug.start", new Object[0]);
                Minecraft.getMinecraft().enableProfiling();
                startTime = System.currentTimeMillis();
                startTicks = Minecraft.getMinecraft().ticksRan;
                return;
            }

            if (par2ArrayOfStr[0].equals("stop"))
            {
                if (!Minecraft.getMinecraft().mcProfiler.profilingEnabled)
                {
                    throw new CommandException("commands.debug.notStarted", new Object[0]);
                }
                else
                {
                    long endTime = System.currentTimeMillis();
                    int endTicks = Minecraft.getMinecraft().ticksRan;
                    long time = endTime - startTime;
                    int ticks = endTicks - startTicks;
                    func_71548_a(time, ticks);
                    Minecraft.getMinecraft().disableProfiling();
                    notifyAdmins(par1ICommandSender, "commands.debug.stop", new Object[]
                            {
                                Float.valueOf((float)time / 1000F), Integer.valueOf(ticks)
                            });
                    return;
                }
            }
        }

        throw new WrongUsageException("commands.debug.usage", new Object[0]);
    }

    private void func_71548_a(long par1, int par3)
    {
        File file = new File(Minecraft.getMinecraft().getFile("debug"), (new StringBuilder()).append("profile-results-").append((new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date())).append(".txt").toString());
        file.getParentFile().mkdirs();

        try
        {
            FileWriter filewriter = new FileWriter(file);
            filewriter.write(func_71547_b(par1, par3));
            filewriter.close();
        }
        catch (Throwable throwable)
        {
            Logger.getLogger("Minecraft").log(Level.SEVERE, (new StringBuilder()).append("Could not save profiler results to ").append(file).toString(), throwable);
        }
    }

    private String func_71547_b(long par1, int par3)
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Profiler Results ----\n");
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time span: ").append(par1).append(" ms\n");
        stringbuilder.append("Tick span: ").append(par3).append(" ticks\n");
        stringbuilder.append("// This is approximately ").append(String.format("%.2f", new Object[]
                {
                    Float.valueOf((float)par3 / ((float)par1 / 1000F))
                })).append(" ticks per second. It should be ").append(20).append(" ticks per second\n\n");
        stringbuilder.append("--- BEGIN PROFILE DUMP ---\n\n");
        func_71546_a(0, "root", stringbuilder);
        stringbuilder.append("--- END PROFILE DUMP ---\n\n");
        return stringbuilder.toString();
    }

    private void func_71546_a(int par1, String par2Str, StringBuilder par3StringBuilder)
    {
        List list = Minecraft.getMinecraft().mcProfiler.getProfilingData(par2Str);

        if (list == null || list.size() < 3)
        {
            return;
        }

        for (int i = 1; i < list.size(); i++)
        {
            ProfilerResult profilerresult = (ProfilerResult)list.get(i);
            par3StringBuilder.append(String.format("[%02d] ", new Object[]
                    {
                        Integer.valueOf(par1)
                    }));

            for (int j = 0; j < par1; j++)
            {
                par3StringBuilder.append(" ");
            }

            par3StringBuilder.append(profilerresult.field_76331_c);
            par3StringBuilder.append(" - ");
            par3StringBuilder.append(String.format("%.2f", new Object[]
                    {
                        Double.valueOf(profilerresult.field_76332_a)
                    }));
            par3StringBuilder.append("%/");
            par3StringBuilder.append(String.format("%.2f", new Object[]
                    {
                        Double.valueOf(profilerresult.field_76330_b)
                    }));
            par3StringBuilder.append("%\n");

            if (profilerresult.field_76331_c.equals("unspecified"))
            {
                continue;
            }

            try
            {
                func_71546_a(par1 + 1, (new StringBuilder()).append(par2Str).append(".").append(profilerresult.field_76331_c).toString(), par3StringBuilder);
            }
            catch (Exception exception)
            {
                par3StringBuilder.append((new StringBuilder()).append("[[ EXCEPTION ").append(exception).append(" ]]").toString());
            }
        }
    }

    /**
     * Returns a random "witty" comment.
     */
    private static String getWittyComment()
    {
        String as[] =
        {
            "Shiny numbers!", "Am I not running fast enough? :(", "I'm working as hard as I can!", "Will I ever be good enough for you? :(", "Speedy. Zoooooom!", "Hello world", "40% better than a crash report.", "Now with extra numbers", "Now with less numbers", "Now with the same numbers",
            "You should add flames to things, it makes them go faster!", "Do you feel the need for... optimization?", "*cracks redstone whip*", "Maybe if you treated it better then it'll have more motivation to work faster! Poor server."
        };

        try
        {
            return as[(int)(System.nanoTime() % (long)as.length)];
        }
        catch (Throwable throwable)
        {
            return "Witty comment unavailable :(";
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}