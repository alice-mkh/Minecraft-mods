package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientTp extends CommandServerTp
{
    public CommandClientTp()
    {
    }

    public void func_71515_b(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 3)
        {
            Minecraft minecraft = Minecraft.getMinecraftInstance();
            if (minecraft.field_71441_e != null)
            {
                int i = par2ArrayOfStr.length - 3;
                int j = 0x1c9c380;
                int k = func_71532_a(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                int l = func_71532_a(par1ICommandSender, par2ArrayOfStr[i++], 0, 256);
                int i1 = func_71532_a(par1ICommandSender, par2ArrayOfStr[i++], -j, j);
                ((EntityPlayerSP)par1ICommandSender).setPositionAndUpdate((float)k + 0.5F, l, (float)i1 + 0.5F);
                func_71522_a(par1ICommandSender, "commands.tp.coordinates", new Object[]
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

    public List func_71516_a(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2)
        {
            return func_71530_a(par2ArrayOfStr, null);
        }
        else
        {
            return null;
        }
    }
}
