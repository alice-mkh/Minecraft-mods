package net.minecraft.src.backport;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.NBTTagString;
import net.minecraft.src.World;
import net.minecraft.src.ModLoader;

public class ItemWritableBook extends Item
{
    public ItemWritableBook(int par1)
    {
        super(par1);
        setMaxStackSize(1);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        ModLoader.getMinecraftInstance().displayGuiScreen(new GuiScreenBook(par3EntityPlayer, par1ItemStack, true));
        return par1ItemStack;
    }

    public boolean func_46056_k()
    {
        return true;
    }

    public static boolean func_56831_a(NBTTagCompound par0NBTTagCompound)
    {
        if (par0NBTTagCompound == null)
        {
            return false;
        }

        if (!par0NBTTagCompound.hasKey("pages"))
        {
            return false;
        }

        NBTTagList nbttaglist = (NBTTagList)par0NBTTagCompound.getTag("pages");

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagString nbttagstring = (NBTTagString)nbttaglist.tagAt(i);

            if (nbttagstring.data == null)
            {
                return false;
            }

            if (nbttagstring.data.length() > 256)
            {
                return false;
            }
        }

        return true;
    }
}