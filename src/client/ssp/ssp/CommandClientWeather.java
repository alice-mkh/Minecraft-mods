package net.minecraft.src.ssp;

import java.util.List;
import java.util.Random;
import net.minecraft.src.CommandWeather;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WrongUsageException;

public class CommandClientWeather extends CommandWeather
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length < 1)
        {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }

        int i = (300 + (new Random()).nextInt(600)) * 20;

        if (par2ArrayOfStr.length >= 2)
        {
            i = parseIntBounded(par1ICommandSender, par2ArrayOfStr[1], 1, 0xf4240) * 20;
        }

        World world = Minecraft.getMinecraft().theWorld;
        WorldInfo worldinfo = world.getWorldInfo();
        worldinfo.setRainTime(i);
        worldinfo.setThunderTime(i);

        if ("clear".equalsIgnoreCase(par2ArrayOfStr[0]))
        {
            worldinfo.setRaining(false);
            worldinfo.setThundering(false);
            notifyAdmins(par1ICommandSender, "commands.weather.clear", new Object[0]);
        }
        else if ("rain".equalsIgnoreCase(par2ArrayOfStr[0]))
        {
            worldinfo.setRaining(true);
            worldinfo.setThundering(false);
            notifyAdmins(par1ICommandSender, "commands.weather.rain", new Object[0]);
        }
        else if ("thunder".equalsIgnoreCase(par2ArrayOfStr[0]))
        {
            worldinfo.setRaining(true);
            worldinfo.setThundering(true);
            notifyAdmins(par1ICommandSender, "commands.weather.thunder", new Object[0]);
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
