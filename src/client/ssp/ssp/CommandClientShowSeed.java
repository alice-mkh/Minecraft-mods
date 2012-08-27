package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientShowSeed extends CommandShowSeed
{
    public CommandClientShowSeed()
    {
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayer entityplayer = getCommandSenderAsPlayer(par1ICommandSender);
        par1ICommandSender.sendChatToPlayer("Seed: "+entityplayer.worldObj.getSeed());
    }
}
