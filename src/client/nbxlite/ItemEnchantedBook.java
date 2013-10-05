package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ItemEnchantedBook extends Item
{
    public ItemEnchantedBook(int par1)
    {
        super(par1);
    }

    public boolean hasEffect(ItemStack par1ItemStack)
    {
        return true;
    }

    /**
     * Checks isDamagable and if it cannot be stacked
     */
    public boolean isItemTool(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * Return an item rarity from EnumRarity
     */
    public EnumRarity getRarity(ItemStack par1ItemStack)
    {
        if (func_92110_g(par1ItemStack).tagCount() > 0)
        {
            return EnumRarity.uncommon;
        }
        else
        {
            return super.getRarity(par1ItemStack);
        }
    }

    public NBTTagList func_92110_g(ItemStack par1ItemStack)
    {
        if (par1ItemStack.stackTagCompound == null || !par1ItemStack.stackTagCompound.hasKey("StoredEnchantments"))
        {
            return new NBTTagList();
        }
        else
        {
            return (NBTTagList)par1ItemStack.stackTagCompound.getTag("StoredEnchantments");
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
    {
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
        NBTTagList nbttaglist = func_92110_g(par1ItemStack);

        if (nbttaglist != null)
        {
            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                short word0 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("id");
                short word1 = ((NBTTagCompound)nbttaglist.tagAt(i)).getShort("lvl");

                if (Enchantment.enchantmentsList[word0] != null)
                {
                    par3List.add(Enchantment.enchantmentsList[word0].getTranslatedName(word1));
                }
            }
        }
    }

    /**
     * Adds an stored enchantment to an enchanted book ItemStack
     */
    public void addEnchantment(ItemStack par1ItemStack, EnchantmentData par2EnchantmentData)
    {
        NBTTagList nbttaglist = func_92110_g(par1ItemStack);
        boolean flag = true;
        int i = 0;

        do
        {
            if (i >= nbttaglist.tagCount())
            {
                break;
            }

            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);

            if (nbttagcompound1.getShort("id") == par2EnchantmentData.enchantmentobj.effectId)
            {
                if (nbttagcompound1.getShort("lvl") < par2EnchantmentData.enchantmentLevel)
                {
                    nbttagcompound1.setShort("lvl", (short)par2EnchantmentData.enchantmentLevel);
                }

                flag = false;
                break;
            }

            i++;
        }
        while (true);

        if (flag)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setShort("id", (short)par2EnchantmentData.enchantmentobj.effectId);
            nbttagcompound.setShort("lvl", (short)par2EnchantmentData.enchantmentLevel);
            nbttaglist.appendTag(nbttagcompound);
        }

        if (!par1ItemStack.hasTagCompound())
        {
            par1ItemStack.setTagCompound(new NBTTagCompound());
        }

        par1ItemStack.getTagCompound().setTag("StoredEnchantments", nbttaglist);
    }

    /**
     * Returns the ItemStack of an enchanted version of this item.
     */
    public ItemStack getEnchantedItemStack(EnchantmentData par1EnchantmentData)
    {
        ItemStack itemstack = new ItemStack(this);
        addEnchantment(itemstack, par1EnchantmentData);
        return itemstack;
    }

    public void func_92113_a(Enchantment par1Enchantment, List par2List)
    {
        for (int i = par1Enchantment.getMinLevel(); i <= par1Enchantment.getMaxLevel(); i++)
        {
            par2List.add(getEnchantedItemStack(new EnchantmentData(par1Enchantment, i)));
        }
    }

    public WeightedRandomChestContent func_92114_b(Random par1Random)
    {
        return func_92112_a(par1Random, 1, 1, 1);
    }

    public WeightedRandomChestContent func_92112_a(Random par1Random, int par2, int par3, int par4)
    {
        if (ODNBXlite.disableEnchantedBooks()){
            return new WeightedRandomChestContent(new ItemStack(itemID, 1, 0), 0, 0, 0);
        }
        Enchantment enchantment = Enchantment.enchantmentsBookList[par1Random.nextInt(Enchantment.enchantmentsBookList.length)];
        ItemStack itemstack = new ItemStack(itemID, 1, 0);
        int i = MathHelper.getRandomIntegerInRange(par1Random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        addEnchantment(itemstack, new EnchantmentData(enchantment, i));
        return new WeightedRandomChestContent(itemstack, par2, par3, par4);
    }

    public ItemStack func_92109_a(Random par1Random)
    {
        Enchantment enchantment = Enchantment.enchantmentsBookList[par1Random.nextInt(Enchantment.enchantmentsBookList.length)];
        ItemStack itemstack = new ItemStack(itemID, 1, 0);
        int i = MathHelper.getRandomIntegerInRange(par1Random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        addEnchantment(itemstack, new EnchantmentData(enchantment, i));
        return itemstack;
    }
}
