package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientToggleDownfall extends CommandToggleDownfall
{
    protected void toggleDownfall()
    {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setRainTime(1);
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
