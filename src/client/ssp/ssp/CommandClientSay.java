package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientSay extends CommandServerSay
{
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0 && par2ArrayOfStr[0].length() > 0)
        {
            String s = func_82361_a(par1ICommandSender, par2ArrayOfStr, 0, true);
            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(String.format("[%s] %s", new Object[]
                    {
                        par1ICommandSender.getCommandSenderName(), s
                    }));
            return;
        }
        else
        {
            throw new WrongUsageException("commands.say.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length >= 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, Minecraft.getMinecraft().getAllUsernames());
        }
        else
        {
            return null;
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
