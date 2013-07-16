package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.CommandClearInventory;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;

public class CommandClientClear extends CommandClearInventory
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayerSP2 entityplayer = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr.length == 3 ? par2ArrayOfStr[0] : null);
        int i = par2ArrayOfStr.length < 2 ? -1 : parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 1);
        int j = par2ArrayOfStr.length < 3 ? -1 : parseIntWithMin(par1ICommandSender, par2ArrayOfStr[2], 0);
        int k = entityplayer.inventory.clearInventory(i, j);
        entityplayer.inventoryContainer.detectAndSendChanges();
        notifyAdmins(par1ICommandSender, "commands.clear.success", new Object[]
                {
                    entityplayer.getEntityName(), Integer.valueOf(k)
                });
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
