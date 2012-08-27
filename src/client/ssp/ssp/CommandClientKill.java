package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientKill extends CommandKill
{
    public CommandClientKill()
    {
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
