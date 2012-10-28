package net.minecraft.src;

import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;

public class CommandClientHelp extends CommandHelp
{
    protected List getSortedPossibleCommands(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().getIntegratedServer().getCommandManager().getPossibleCommands(par1ICommandSender);
    }

    protected Map getCommands()
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
