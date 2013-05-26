package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientDefaultGameMode extends CommandDefaultGameMode
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 1)
        {
            EnumGameType mode = getGameModeFromCommand(par1ICommandSender, par2ArrayOfStr[0]);
            Minecraft.getMinecraft().theWorld.getWorldInfo().setGameType(mode);
            String s = StatCollector.translateToLocal((new StringBuilder()).append("gameMode.").append(mode.getName()).toString());
            notifyAdmins(par1ICommandSender, "commands.defaultgamemode.success", new Object[]
                    {
                        s
                    });
            return;
        }
        else
        {
            throw new WrongUsageException("commands.defaultgamemode.usage", new Object[0]);
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
