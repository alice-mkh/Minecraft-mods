package net.minecraft.src.ssp;

import net.minecraft.src.CommandEnchant;
import net.minecraft.src.Enchantment;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NumberInvalidException;
import net.minecraft.src.WrongUsageException;

public class CommandClientEnchant extends CommandEnchant
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length >= 2)
        {
            EntityPlayerSP2 entityplayer = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
            int i = parseIntBounded(par1ICommandSender, par2ArrayOfStr[1], 0, Enchantment.enchantmentsList.length - 1);
            int j = 1;
            ItemStack itemstack = entityplayer.getCurrentEquippedItem();

            if (itemstack == null)
            {
                notifyAdmins(par1ICommandSender, "commands.enchant.noItem", new Object[0]);
                return;
            }

            Enchantment enchantment = Enchantment.enchantmentsList[i];

            if (enchantment == null)
            {
                throw new NumberInvalidException("commands.enchant.notFound", new Object[]
                        {
                            Integer.valueOf(i)
                        });
            }

            if (!enchantment.canApply(itemstack))
            {
                notifyAdmins(par1ICommandSender, "commands.enchant.cantEnchant", new Object[0]);
                return;
            }

            if (par2ArrayOfStr.length >= 3)
            {
                j = parseIntBounded(par1ICommandSender, par2ArrayOfStr[2], enchantment.getMinLevel(), enchantment.getMaxLevel());
            }

            if (itemstack.hasTagCompound())
            {
                NBTTagList nbttaglist = itemstack.getEnchantmentTagList();

                if (nbttaglist != null)
                {
                    for (int k = 0; k < nbttaglist.tagCount(); k++)
                    {
                        short word0 = ((NBTTagCompound)nbttaglist.tagAt(k)).getShort("id");

                        if (Enchantment.enchantmentsList[word0] == null)
                        {
                            continue;
                        }

                        Enchantment enchantment1 = Enchantment.enchantmentsList[word0];

                        if (!enchantment1.canApplyTogether(enchantment))
                        {
                            notifyAdmins(par1ICommandSender, "commands.enchant.cantCombine", new Object[]
                                    {
                                        enchantment.getTranslatedName(j), enchantment1.getTranslatedName(((NBTTagCompound)nbttaglist.tagAt(k)).getShort("lvl"))
                                    });
                            return;
                        }
                    }
                }
            }

            itemstack.addEnchantment(enchantment, j);
            notifyAdmins(par1ICommandSender, "commands.enchant.success", new Object[0]);
            return;
        }
        else
        {
            throw new WrongUsageException("commands.enchant.usage", new Object[0]);
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
