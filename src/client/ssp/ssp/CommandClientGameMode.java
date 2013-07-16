package net.minecraft.src.ssp;

import net.minecraft.src.CommandGameMode;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.StatCollector;
import net.minecraft.src.WrongUsageException;

public class CommandClientGameMode extends CommandGameMode
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0)
        {
            EnumGameType mode = getGameModeFromCommand(par1ICommandSender, par2ArrayOfStr[0]);
            EntityPlayerSP2 p = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr.length > 1 ? par2ArrayOfStr[1] : null);
            p.fallDistance = 0.0F;
            Minecraft.getMinecraft().setController(mode);
            Minecraft.getMinecraft().setGameMode(mode);
            notifyAdmins(par1ICommandSender, 1, "commands.gamemode.success.self", new Object[]
                    {
                        StatCollector.translateToLocal("gameMode." + mode.getName())
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
