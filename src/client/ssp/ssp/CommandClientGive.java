package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientGive extends CommandGive
{
    public CommandClientGive()
    {
    }

    protected EntityPlayer func_71537_a(String par1Str)
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected String[] getPlayers()
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
