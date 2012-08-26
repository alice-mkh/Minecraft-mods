package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientTp extends CommandServerTp
{
    public CommandClientTp()
    {
    }

    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 3)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft.theWorld != null)
            {
                int i = par2ArrayOfStr.length - 3;
                int j = 0x1c9c380;
                int k = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                int l = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], 0, 256);
                int i1 = parseIntBounded(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                ((EntityPlayerSP)par1ICommandSender).setPositionAndUpdate((float)k + 0.5F, l, (float)i1 + 0.5F);
                notifyAdmins(par1ICommandSender, "commands.tp.coordinates", new Object[]
                        {
                            ((EntityPlayerSP)par1ICommandSender).username, Integer.valueOf(k), Integer.valueOf(l), Integer.valueOf(i1)
                        });
            }

            return;
        }
        else
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
    }

    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2)
        {
            return getListOfStringsMatchingLastWord(par2ArrayOfStr, null);
        }
        else
        {
            return null;
        }
    }
}
