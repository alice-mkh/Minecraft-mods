package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientDefaultGameMode extends CommandDefaultGameMode
{
    public CommandClientDefaultGameMode()
    {
    }

    public void func_71515_b(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            EnumGameType mode = func_71539_b(par1ICommandSender, par2ArrayOfStr[0]);
            Minecraft.getMinecraftInstance().field_71441_e.getWorldInfo().func_76060_a(mode);
            func_71522_a(par1ICommandSender, "commands.defaultgamemode.success", new Object[]
                    {
                        par2ArrayOfStr[0]
                    });
            return;
        }
        else
        {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
        }
    }
}
