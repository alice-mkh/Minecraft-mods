package net.minecraft.src.backport;

import java.io.*;
import java.util.ArrayList;
import net.minecraft.src.*;

public class MerchantRecipeList extends ArrayList
{
    public MerchantRecipeList()
    {
    }

    public MerchantRecipeList(NBTTagCompound par1NBTTagCompound)
    {
        func_57495_a(par1NBTTagCompound);
    }

    public MerchantRecipe func_57493_a(ItemStack par1ItemStack, ItemStack par2ItemStack, int par3)
    {
        if (par3 > 0 && par3 < size())
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)get(par3);

            if (par1ItemStack.itemID == merchantrecipe.func_57067_a().itemID && (par2ItemStack == null && !merchantrecipe.func_57070_c() || merchantrecipe.func_57070_c() && par2ItemStack != null && merchantrecipe.func_57065_b().itemID == par2ItemStack.itemID))
            {
                if (par1ItemStack.stackSize >= merchantrecipe.func_57067_a().stackSize && (!merchantrecipe.func_57070_c() || par2ItemStack.stackSize >= merchantrecipe.func_57065_b().stackSize))
                {
                    return merchantrecipe;
                }
                else
                {
                    return null;
                }
            }
        }

        for (int i = 0; i < size(); i++)
        {
            MerchantRecipe merchantrecipe1 = (MerchantRecipe)get(i);

            if (par1ItemStack.itemID == merchantrecipe1.func_57067_a().itemID && par1ItemStack.stackSize >= merchantrecipe1.func_57067_a().stackSize && (!merchantrecipe1.func_57070_c() && par2ItemStack == null || merchantrecipe1.func_57070_c() && par2ItemStack != null && merchantrecipe1.func_57065_b().itemID == par2ItemStack.itemID && par2ItemStack.stackSize >= merchantrecipe1.func_57065_b().stackSize))
            {
                return merchantrecipe1;
            }
        }

        return null;
    }

    public void func_57494_a(MerchantRecipe par1MerchantRecipe)
    {
        for (int i = 0; i < size(); i++)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)get(i);

            if (par1MerchantRecipe.func_57064_a(merchantrecipe))
            {
                if (par1MerchantRecipe.func_57069_b(merchantrecipe))
                {
                    set(i, par1MerchantRecipe);
                }

                return;
            }
        }

        add(par1MerchantRecipe);
    }

    public void func_57491_a(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeByte((byte)(size() & 0xff));

        for (int i = 0; i < size(); i++)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)get(i);
            writeItemStack(merchantrecipe.func_57067_a(), par1DataOutputStream);
            writeItemStack(merchantrecipe.func_57071_d(), par1DataOutputStream);
            ItemStack itemstack = merchantrecipe.func_57065_b();
            par1DataOutputStream.writeBoolean(itemstack != null);

            if (itemstack != null)
            {
                writeItemStack(itemstack, par1DataOutputStream);
            }
        }
    }

    public static MerchantRecipeList func_57492_a(DataInputStream par0DataInputStream) throws IOException
    {
        MerchantRecipeList merchantrecipelist = new MerchantRecipeList();
        int i = par0DataInputStream.readByte() & 0xff;

        for (int j = 0; j < i; j++)
        {
            ItemStack itemstack = readItemStack(par0DataInputStream);
            ItemStack itemstack1 = readItemStack(par0DataInputStream);
            ItemStack itemstack2 = null;

            if (par0DataInputStream.readBoolean())
            {
                itemstack2 = readItemStack(par0DataInputStream);
            }

            merchantrecipelist.add(new MerchantRecipe(itemstack, itemstack2, itemstack1));
        }

        return merchantrecipelist;
    }

    public void func_57495_a(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Recipes");

        for (int i = 0; i < nbttaglist.tagCount(); i++)
        {
            NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
            add(new MerchantRecipe(nbttagcompound));
        }
    }

    public NBTTagCompound func_57496_a()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        NBTTagList nbttaglist = new NBTTagList("Recipes");

        for (int i = 0; i < size(); i++)
        {
            MerchantRecipe merchantrecipe = (MerchantRecipe)get(i);
            nbttaglist.appendTag(merchantrecipe.func_57066_e());
        }

        nbttagcompound.setTag("Recipes", nbttaglist);
        return nbttagcompound;
    }

    public static void writeItemStack(ItemStack par0ItemStack, DataOutputStream par1DataOutputStream) throws IOException
    {
        if (par0ItemStack == null)
        {
            par1DataOutputStream.writeShort(-1);
        }
        else
        {
            par1DataOutputStream.writeShort(par0ItemStack.itemID);
            par1DataOutputStream.writeByte(par0ItemStack.stackSize);
            par1DataOutputStream.writeShort(par0ItemStack.getItemDamage());
            NBTTagCompound nbttagcompound = null;

            if (par0ItemStack.getItem().isDamageable() || par0ItemStack.getItem().func_46056_k())
            {
                nbttagcompound = par0ItemStack.stackTagCompound;
            }

            writeNBTTagCompound(nbttagcompound, par1DataOutputStream);
        }
    }

    public static ItemStack readItemStack(DataInputStream par1DataInputStream) throws IOException
    {
        ItemStack itemstack = null;
        short word0 = par1DataInputStream.readShort();

        if (word0 >= 0)
        {
            byte byte0 = par1DataInputStream.readByte();
            short word1 = par1DataInputStream.readShort();
            itemstack = new ItemStack(word0, byte0, word1);

            if (Item.itemsList[word0].isDamageable() || Item.itemsList[word0].func_46056_k())
            {
                itemstack.stackTagCompound = readNBTTagCompound(par1DataInputStream);
            }
        }

        return itemstack;
    }

    public static void writeNBTTagCompound(NBTTagCompound par0NBTTagCompound, DataOutputStream par1DataOutputStream) throws IOException
    {
        if (par0NBTTagCompound == null)
        {
            par1DataOutputStream.writeShort(-1);
        }
        else
        {
            byte abyte0[] = CompressedStreamTools.compress(par0NBTTagCompound);
            par1DataOutputStream.writeShort((short)abyte0.length);
            par1DataOutputStream.write(abyte0);
        }
    }

    public static NBTTagCompound readNBTTagCompound(DataInputStream par1DataInputStream) throws IOException
    {
        short word0 = par1DataInputStream.readShort();

        if (word0 < 0)
        {
            return null;
        }
        else
        {
            byte abyte0[] = new byte[word0];
            par1DataInputStream.readFully(abyte0);
            return CompressedStreamTools.decompress(abyte0);
        }
    }
}
