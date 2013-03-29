package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientGameMode extends CommandGameMode
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2)
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

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
