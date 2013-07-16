package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.CommandDifficulty;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.StatCollector;
import net.minecraft.src.WrongUsageException;

public class CommandClientDifficulty extends CommandDifficulty
{
    private static final String field_82365_a[] =
    {
        "options.difficulty.peaceful", "options.difficulty.easy", "options.difficulty.normal", "options.difficulty.hard"
    };

    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0)
        {
            int i = getDifficultyForName(par1ICommandSender, par2ArrayOfStr[0]);
            Minecraft.getMinecraft().forcedDifficulty = i;
            String s = StatCollector.translateToLocal(field_82365_a[i]);
            notifyAdmins(par1ICommandSender, 1, "commands.difficulty.success", new Object[]
                    {
                        s
                    });
            return;
        }
        else
        {
            throw new WrongUsageException("commands.difficulty.usage", new Object[0]);
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
