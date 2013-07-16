package net.minecraft.src.ssp;

import net.minecraft.src.CommandToggleDownfall;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;

public class CommandClientToggleDownfall extends CommandToggleDownfall
{
    @Override
    protected void toggleDownfall()
    {
        Minecraft.getMinecraft().theWorld.getWorldInfo().setRainTime(1);
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
