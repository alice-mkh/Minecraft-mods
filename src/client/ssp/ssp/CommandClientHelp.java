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
        return Minecraft.getMinecraft().getIntegratedServer().getCommandManager().getPossibleCommands(par1ICommandSender);
    }

    protected Map func_71535_c()
    {
        return Minecraft.getMinecraft().getIntegratedServer().getCommandManager().getCommands();
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }
}
