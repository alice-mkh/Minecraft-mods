package net.minecraft.src.ssp;

import net.minecraft.src.CommandXP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.WrongUsageException;

public class CommandClientExperience extends CommandXP
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length > 0)
        {
            String s = par2ArrayOfStr[0];
            boolean flag = s.endsWith("l") || s.endsWith("L");

            if (flag && s.length() > 1)
            {
                s = s.substring(0, s.length() - 1);
            }

            int i = parseInt(par1ICommandSender, s);
            boolean flag1 = i < 0;

            if (flag1)
            {
                i *= -1;
            }

            EntityPlayer entityplayer = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr.length > 1 ? par2ArrayOfStr[1] : null);

            if (flag)
            {
                if (flag1)
                {
                    entityplayer.addExperienceLevel(-i);
                    notifyAdmins(par1ICommandSender, "commands.xp.success.negative.levels", new Object[]
                            {
                                Integer.valueOf(i), entityplayer.getEntityName()
                            });
                }
                else
                {
                    entityplayer.addExperienceLevel(i);
                    notifyAdmins(par1ICommandSender, "commands.xp.success.levels", new Object[]
                            {
                                Integer.valueOf(i), entityplayer.getEntityName()
                            });
                }
            }
            else
            {
                if (flag1)
                {
                    throw new WrongUsageException("commands.xp.failure.widthdrawXp", new Object[0]);
                }

                entityplayer.addExperience(i);
                notifyAdmins(par1ICommandSender, "commands.xp.success", new Object[]
                        {
                            Integer.valueOf(i), entityplayer.getEntityName()
                        });
            }

            return;
        }
        else
        {
            throw new WrongUsageException("commands.xp.usage", new Object[0]);
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
