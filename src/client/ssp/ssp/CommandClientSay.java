package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.CommandServerSay;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.WrongUsageException;

public class CommandClientSay extends CommandServerSay
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0 && par2ArrayOfStr[0].length() > 0)
        {
            String s = func_82361_a(par1ICommandSender, par2ArrayOfStr, 0, true);
            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.announcement", new Object[]
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
    @Override
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
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
