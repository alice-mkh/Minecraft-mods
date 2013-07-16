package net.minecraft.src.ssp;

import net.minecraft.src.CommandTime;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;

public class CommandClientTime extends CommandTime
{
    @Override
    protected void setTime(ICommandSender par1ICommandSender, int par2)
    {
        ((WorldSSP)Minecraft.getMinecraft().theWorld).commandSetTime(par2, true);
    }

    @Override
    protected void addTime(ICommandSender par1ICommandSender, int par2)
    {
        WorldSSP world = ((WorldSSP)Minecraft.getMinecraft().theWorld);
        ((WorldSSP)world).commandSetTime(world.getTotalWorldTime() + (long)par2, false);
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
