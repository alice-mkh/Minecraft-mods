package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class CommandClientGive extends CommandGive
{
    protected EntityPlayer func_71537_a(String par1Str)
    {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected String[] getPlayers()
    {
        return (new String[]
                {
                    Minecraft.getMinecraft().thePlayer.username
                });
    }

    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length >= 2)
        {
            EntityPlayer entityplayer = func_71537_a(par2ArrayOfStr[0]);
            int i = parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 1);
            int j = 1;
            int k = 0;

            if (Item.itemsList[i] == null)
            {
                throw new NumberInvalidException("commands.give.notFound", new Object[]
                        {
                            Integer.valueOf(i)
                        });
            }

            if (par2ArrayOfStr.length >= 3)
            {
                j = parseIntBounded(par1ICommandSender, par2ArrayOfStr[2], 1, 64);
            }

            if (par2ArrayOfStr.length >= 4)
            {
                k = parseInt(par1ICommandSender, par2ArrayOfStr[3]);
            }

            ItemStack itemstack = new ItemStack(i, j, k);
            entityplayer.dropPlayerItem(itemstack);
            notifyAdmins(par1ICommandSender, "commands.give.success", new Object[]
                    {
                        Item.itemsList[i].func_77653_i(itemstack), Integer.valueOf(i), Integer.valueOf(j), entityplayer.getEntityName()
                    });
            return;
        }
        else
        {
            throw new WrongUsageException("commands.give.usage", new Object[0]);
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }
}
