package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class CommandClientClear extends CommandClearInventory
{
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        EntityPlayer entityplayer = getCommandSenderAsPlayer2(par1ICommandSender);
        int i = par2ArrayOfStr.length < 2 ? -1 : parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 1);
        int j = par2ArrayOfStr.length < 3 ? -1 : parseIntWithMin(par1ICommandSender, par2ArrayOfStr[2], 0);
        int k = entityplayer.inventory.clearInventory(i, j);
        entityplayer.inventoryContainer.updateCraftingResults();
        notifyAdmins(par1ICommandSender, "commands.clear.success", new Object[]
                {
                    entityplayer.getEntityName(), Integer.valueOf(k)
                });
    }

    protected String[] func_82369_d()
    {
        return new String[]{Minecraft.getMinecraft().thePlayer.username};
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }

    /**
     * Returns the given ICommandSender as a EntityPlayer or throw an exception.
     */
    public static EntityPlayer getCommandSenderAsPlayer2(ICommandSender par0ICommandSender)
    {
        if (par0ICommandSender instanceof EntityPlayer)
        {
            return (EntityPlayer)par0ICommandSender;
        }
        else
        {
            throw new PlayerNotFoundException("You must specify which player you wish to perform this action on.", new Object[0]);
        }
    }
}
