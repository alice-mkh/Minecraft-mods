package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientTime extends CommandTime
{
    public CommandClientTime()
    {
    }

    protected void func_71552_a(ICommandSender par1ICommandSender, int par2)
    {
        ((WorldSSP)Minecraft.getMinecraftInstance().field_71441_e).commandSetTime(par2);
    }

    protected void func_71553_b(ICommandSender par1ICommandSender, int par2)
    {
        WorldSSP world = ((WorldSSP)Minecraft.getMinecraftInstance().field_71441_e);
        ((WorldSSP)world).commandSetTime(world.getWorldTime() + (long)par2);
    }
}
