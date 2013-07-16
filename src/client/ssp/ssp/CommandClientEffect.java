package net.minecraft.src.ssp;

import net.minecraft.src.CommandEffect;
import net.minecraft.src.CommandException;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NumberInvalidException;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.StatCollector;
import net.minecraft.src.WrongUsageException;

public class CommandClientEffect extends CommandEffect
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length >= 2)
        {
            EntityPlayerSP2 entityplayer = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
            int i = parseIntWithMin(par1ICommandSender, par2ArrayOfStr[1], 2);
            int j = 600;
            int k = 30;
            int l = 0;

            if (i < 0 || i >= Potion.potionTypes.length || Potion.potionTypes[i] == null)
            {
                throw new NumberInvalidException("commands.effect.notFound", new Object[]
                        {
                            Integer.valueOf(i)
                        });
            }

            if (par2ArrayOfStr.length >= 3)
            {
                k = parseIntBounded(par1ICommandSender, par2ArrayOfStr[2], 0, 0xf4240);

                if (Potion.potionTypes[i].isInstant())
                {
                    j = k;
                }
                else
                {
                    j = k * 20;
                }
            }
            else if (Potion.potionTypes[i].isInstant())
            {
                j = 1;
            }

            if (par2ArrayOfStr.length >= 4)
            {
                l = parseIntBounded(par1ICommandSender, par2ArrayOfStr[3], 0, 255);
            }

            if (k == 0)
            {
                if (entityplayer.isPotionActive(i))
                {
                    entityplayer.removePotionEffect(i);
                    notifyAdmins(par1ICommandSender, "commands.effect.success.removed", new Object[]
                            {
                                StatCollector.translateToLocal(Potion.potionTypes[i].getName()), entityplayer.getEntityName()
                            });
                }
                else
                {
                    throw new CommandException("commands.effect.failure.notActive", new Object[]
                            {
                                StatCollector.translateToLocal(Potion.potionTypes[i].getName()), entityplayer.getEntityName()
                            });
                }
            }
            else
            {
                PotionEffect potioneffect = new PotionEffect(i, j, l);
                entityplayer.addPotionEffect(potioneffect);
                notifyAdmins(par1ICommandSender, "commands.effect.success", new Object[]
                        {
                            StatCollector.translateToLocal(potioneffect.getEffectName()), Integer.valueOf(i), Integer.valueOf(l), entityplayer.getEntityName(), Integer.valueOf(k)
                        });
            }

            return;
        }
        else
        {
            throw new WrongUsageException("commands.effect.usage", new Object[0]);
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
