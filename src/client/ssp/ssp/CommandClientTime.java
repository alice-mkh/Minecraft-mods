package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientTime extends CommandTime
{
    public CommandClientTime()
    {
    }

    protected void func_71552_a(ICommandSender par1ICommandSender, int par2)
    {
        ((WorldSSP)Minecraft.getMinecraft().theWorld).commandSetTime(par2, true);
    }

    protected void addTime(ICommandSender par1ICommandSender, int par2)
    {
        WorldSSP world = ((WorldSSP)Minecraft.getMinecraft().theWorld);
        ((WorldSSP)world).commandSetTime(world.getWorldTime() + (long)par2, false);
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
