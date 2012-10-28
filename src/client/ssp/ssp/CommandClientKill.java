package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientKill extends CommandKill
{
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayer entityplayer = getCommandSenderAsPlayer2(par1ICommandSender);
        entityplayer.attackEntityFrom(DamageSource.outOfWorld, 1000);
        par1ICommandSender.sendChatToPlayer("Ouch. That look like it hurt.");
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayer getCommandSenderAsPlayer2(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayer)
        {
            return (EntityPlayer)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }
}
