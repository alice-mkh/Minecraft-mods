package net.minecraft.src.ssp;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.src.CommandHelp;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;

public class CommandClientHelp extends CommandHelp
{
    @Override
    protected List getSortedPossibleCommands(ICommandSender par1ICommandSender)
    {
        List list = Minecraft.getMinecraft().getIntegratedServer().getCommandManager().getPossibleCommands(par1ICommandSender);
        Collections.sort(list);
        return list;
    }

    @Override
    protected Map getCommands()
    {
        return Minecraft.getMinecraft().getIntegratedServer().getCommandManager().getCommands();
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }
}
