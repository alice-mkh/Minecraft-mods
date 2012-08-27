package net.minecraft.src;

import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class CommandClientHelp extends CommandHelp
{
    public CommandClientHelp()
    {
    }

    protected List func_71534_d(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().getCommandManager().getPossibleCommands(par1ICommandSender);
    }

    protected Map func_71535_c()
    {
        return Minecraft.getMinecraft().getCommandManager().getCommands();
    }
}
