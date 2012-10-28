package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientExperience extends CommandXP
{
    protected String[] getAllUsernames()
    {
        return (new String[]
                {
                    Minecraft.getMinecraft().thePlayer.username
                });
    }

    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0)
        {
            int i = parseIntBounded(par1ICommandSender, par2ArrayOfStr[0], 0, 5000);
            EntityPlayer entityplayer = getCommandSenderAsPlayer2(par1ICommandSender);

            entityplayer.addExperience(i);
            notifyAdmins(par1ICommandSender, "commands.xp.success", new Object[]
                    {
                        Integer.valueOf(i), entityplayer.getEntityName()
                    });
            return;
        }
        else
        {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
        }
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

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
