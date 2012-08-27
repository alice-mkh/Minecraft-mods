package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientExperience extends CommandXP
{
    public CommandClientExperience()
    {
    }

    protected EntityPlayer func_71543_a(String par1Str)
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected String[] func_71542_c()
    {
        return (new String[]
                {
                    Minecraft.getMinecraft().thePlayer.username
                });
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
