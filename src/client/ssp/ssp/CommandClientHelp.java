package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientHelp extends CommandHelp
{
    public CommandClientHelp()
    {
    }

    protected List func_71534_d(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraftInstance().getCommandManager().func_71557_a(par1ICommandSender);
    }
}
