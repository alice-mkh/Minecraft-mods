package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientGameMode extends CommandGameMode
{
    public CommandClientGameMode()
    {
    }

    protected EntityPlayer func_55243_a(String par1Str)
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected String[] func_55242_c()
    {
        return (new String[]
                {
                    Minecraft.getMinecraft().thePlayer.username
                });
    }

    public void func_71515_b(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            EnumGameType mode = getGameModeFromCommand(par1ICommandSender, par2ArrayOfStr[0]);
            Minecraft.getMinecraft().setController(mode);
            Minecraft.getMinecraft().setGameMode(mode);
            notifyAdmins(par1ICommandSender, 1, "commands.gamemode.success.self", new Object[]
                    {
                        par2ArrayOfStr[0]
                    });
        }
        else
        {
            throw new WrongUsageException("commands.gamemode.usage", new Object[0]);
        }
    }
}
