package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientGameRule extends CommandGameRule
{
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 2)
        {
            String s = par2ArrayOfStr[0];
            String s2 = par2ArrayOfStr[1];
            GameRules gamerules2 = func_82366_d();

            if (gamerules2.func_82765_e(s))
            {
                gamerules2.func_82764_b(s, s2);
                notifyAdmins(par1ICommandSender, "commands.gamerule.success", new Object[0]);
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[]
                        {
                            s
                        });
            }

            return;
        }

        if (par2ArrayOfStr.length == 1)
        {
            String s1 = par2ArrayOfStr[0];
            GameRules gamerules1 = func_82366_d();

            if (gamerules1.func_82765_e(s1))
            {
                String s3 = gamerules1.func_82767_a(s1);
                par1ICommandSender.sendChatToPlayer((new StringBuilder()).append(s1).append(" = ").append(s3).toString());
            }
            else
            {
                notifyAdmins(par1ICommandSender, "commands.gamerule.norule", new Object[]
                        {
                            s1
                        });
            }

            return;
        }

        if (par2ArrayOfStr.length == 0)
        {
            GameRules gamerules = func_82366_d();
            par1ICommandSender.sendChatToPlayer(joinNiceString(gamerules.func_82763_b()));
            return;
        }
        else
        {
            throw new WrongUsageException("commands.gamerule.usage", new Object[0]);
        }
    }

    private GameRules func_82366_d()
    {
        return Minecraft.getMinecraft().theWorld.func_82736_K();
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, func_82366_d().func_82763_b());
        }

        if (par2ArrayOfStr.length == 2)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, new String[]
                    {
                        "true", "false"
                    });
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
