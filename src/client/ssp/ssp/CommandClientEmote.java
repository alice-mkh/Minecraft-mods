package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.ChatMessageComponent;
import net.minecraft.src.CommandServerEmote;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.WrongUsageException;

public class CommandClientEmote extends CommandServerEmote
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0)
        {
            String s = func_82360_a(par1ICommandSender, par2ArrayOfStr, 0);
            Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromTranslationWithSubstitutions("chat.type.emote", new Object[]
                    {
                        par1ICommandSender.getCommandSenderName(), s
                    }));
            return;
        }
        else
        {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    @Override
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        return getListOfStringsMatchingLastWord(par2ArrayOfStr, Minecraft.getMinecraft().getAllUsernames());
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }
}
