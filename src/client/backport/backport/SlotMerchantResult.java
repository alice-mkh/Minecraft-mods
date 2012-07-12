package net.minecraft.src.backport;

import net.minecraft.src.*;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant field_57193_a;
    private EntityPlayer field_57191_f;
    private int field_57192_g;
    private final IMerchant field_57194_h;

    public SlotMerchantResult(EntityPlayer par1EntityPlayer, IMerchant par2IMerchant, InventoryMerchant par3InventoryMerchant, int par4, int par5, int par6)
    {
        super(par3InventoryMerchant, par4, par5, par6);
        field_57191_f = par1EntityPlayer;
        field_57194_h = par2IMerchant;
        field_57193_a = par3InventoryMerchant;
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    public boolean isItemValid(ItemStack par1ItemStack)
    {
        return false;
    }

    /**
     * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
     * stack.
     */
    public ItemStack decrStackSize(int par1)
    {
        if (getHasStack())
        {
            field_57192_g += Math.min(par1, getStack().stackSize);
        }

        return super.decrStackSize(par1);
    }

    protected void func_48435_a(ItemStack par1ItemStack, int par2)
    {
        field_57192_g += par2;
        func_48434_c(par1ItemStack);
    }

    protected void func_48434_c(ItemStack par1ItemStack)
    {
        par1ItemStack.onCrafting(field_57191_f.worldObj, field_57191_f, field_57192_g);
        field_57192_g = 0;
    }

    /**
     * Called when the player picks up an item from an inventory slot
     */
    public void onPickupFromSlot(ItemStack par1ItemStack)
    {
        func_48434_c(par1ItemStack);
        MerchantRecipe merchantrecipe = field_57193_a.func_56192_h();

        if (merchantrecipe != null)
        {
            ItemStack itemstack = field_57193_a.getStackInSlot(0);
            ItemStack itemstack1 = field_57193_a.getStackInSlot(1);

            if (func_57190_a(merchantrecipe, itemstack, itemstack1) || func_57190_a(merchantrecipe, itemstack1, itemstack))
            {
                if (itemstack != null && itemstack.stackSize <= 0)
                {
                    itemstack = null;
                }

                if (itemstack1 != null && itemstack1.stackSize <= 0)
                {
                    itemstack1 = null;
                }

                field_57193_a.setInventorySlotContents(0, itemstack);
                field_57193_a.setInventorySlotContents(1, itemstack1);
                field_57194_h.func_56219_a(merchantrecipe);
            }
        }
    }

    private boolean func_57190_a(MerchantRecipe par1MerchantRecipe, ItemStack par2ItemStack, ItemStack par3ItemStack)
    {
        ItemStack itemstack = par1MerchantRecipe.func_57067_a();
        ItemStack itemstack1 = par1MerchantRecipe.func_57065_b();

        if (par2ItemStack != null && par2ItemStack.itemID == itemstack.itemID)
        {
            if (itemstack1 != null && par3ItemStack != null && itemstack1.itemID == par3ItemStack.itemID)
            {
                par2ItemStack.stackSize -= itemstack.stackSize;
                par3ItemStack.stackSize -= itemstack1.stackSize;
                return true;
            }

            if (itemstack1 == null && par3ItemStack == null)
            {
                par2ItemStack.stackSize -= itemstack.stackSize;
                return true;
            }
        }

        return false;
    }
}
