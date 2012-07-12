package net.minecraft.src.backport;

import net.minecraft.src.*;

public class MerchantRecipe
{
    private ItemStack field_57074_a;
    private ItemStack field_57072_b;
    private ItemStack field_57073_c;
    private int field_58119_d;

    public MerchantRecipe(NBTTagCompound par1NBTTagCompound)
    {
        func_57068_a(par1NBTTagCompound);
    }

    public MerchantRecipe(ItemStack par1ItemStack, ItemStack par2ItemStack, ItemStack par3ItemStack)
    {
        field_57074_a = par1ItemStack;
        field_57072_b = par2ItemStack;
        field_57073_c = par3ItemStack;
    }

    public MerchantRecipe(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
        this(par1ItemStack, null, par2ItemStack);
    }

    public MerchantRecipe(ItemStack par1ItemStack, Item par2Item)
    {
        this(par1ItemStack, new ItemStack(par2Item));
    }

    public ItemStack func_57067_a()
    {
        return field_57074_a;
    }

    public ItemStack func_57065_b()
    {
        return field_57072_b;
    }

    public boolean func_57070_c()
    {
        return field_57072_b != null;
    }

    public ItemStack func_57071_d()
    {
        return field_57073_c;
    }

    public boolean func_57064_a(MerchantRecipe par1MerchantRecipe)
    {
        if (field_57074_a.itemID != par1MerchantRecipe.field_57074_a.itemID || field_57073_c.itemID != par1MerchantRecipe.field_57073_c.itemID)
        {
            return false;
        }
        else
        {
            return field_57072_b == null && par1MerchantRecipe.field_57072_b == null || field_57072_b != null && par1MerchantRecipe.field_57072_b != null && field_57072_b.itemID == par1MerchantRecipe.field_57072_b.itemID;
        }
    }

    public boolean func_57069_b(MerchantRecipe par1MerchantRecipe)
    {
        return func_57064_a(par1MerchantRecipe) && (field_57074_a.stackSize < par1MerchantRecipe.field_57074_a.stackSize || field_57072_b != null && field_57072_b.stackSize < par1MerchantRecipe.field_57072_b.stackSize);
    }

    public int func_58117_e()
    {
        return field_58119_d;
    }

    public void func_58118_f()
    {
        field_58119_d++;
    }

    public void func_57068_a(NBTTagCompound par1NBTTagCompound)
    {
        NBTTagCompound nbttagcompound = par1NBTTagCompound.getCompoundTag("buy");
        field_57074_a = ItemStack.loadItemStackFromNBT(nbttagcompound);
        NBTTagCompound nbttagcompound1 = par1NBTTagCompound.getCompoundTag("sell");
        field_57073_c = ItemStack.loadItemStackFromNBT(nbttagcompound1);

        if (par1NBTTagCompound.hasKey("buyB"))
        {
            field_57072_b = ItemStack.loadItemStackFromNBT(par1NBTTagCompound.getCompoundTag("buyB"));
        }

        if (par1NBTTagCompound.hasKey("uses"))
        {
            field_58119_d = par1NBTTagCompound.getInteger("uses");
        }
    }

    public NBTTagCompound func_57066_e()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        nbttagcompound.setCompoundTag("buy", field_57074_a.writeToNBT(new NBTTagCompound("buy")));
        nbttagcompound.setCompoundTag("sell", field_57073_c.writeToNBT(new NBTTagCompound("sell")));

        if (field_57072_b != null)
        {
            nbttagcompound.setCompoundTag("buyB", field_57072_b.writeToNBT(new NBTTagCompound("buyB")));
        }

        nbttagcompound.setInteger("uses", field_58119_d);
        return nbttagcompound;
    }
}
