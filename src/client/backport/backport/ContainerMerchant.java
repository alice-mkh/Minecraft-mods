package net.minecraft.src.backport;

import java.util.List;
import net.minecraft.src.*;

public class ContainerMerchant extends Container
{
    private IMerchant field_56987_a;
    private InventoryMerchant field_56985_b;
    private final World field_56986_c;

    public ContainerMerchant(InventoryPlayer par1InventoryPlayer, IMerchant par2IMerchant, World par3World)
    {
        field_56987_a = par2IMerchant;
        field_56986_c = par3World;
        field_56985_b = new InventoryMerchant(par1InventoryPlayer.player, par2IMerchant);
        addSlot(new Slot(field_56985_b, 0, 36, 53));
        addSlot(new Slot(field_56985_b, 1, 62, 53));
        addSlot(new SlotMerchantResult(par1InventoryPlayer.player, par2IMerchant, field_56985_b, 2, 120, 53));

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlot(new Slot(par1InventoryPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlot(new Slot(par1InventoryPlayer, j, 8 + j * 18, 142));
        }
    }

    public InventoryMerchant func_56983_c()
    {
        return field_56985_b;
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        field_56985_b.func_56190_g();
        super.onCraftMatrixChanged(par1IInventory);
    }

    public void func_56984_c(int par1)
    {
        field_56985_b.func_56191_c(par1);
    }

    public void updateProgressBar(int i, int j)
    {
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return field_56987_a.func_56221_a() == par1EntityPlayer;
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par1 == 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.func_48433_a(itemstack1, itemstack);
            }
            else if (par1 == 0 || par1 == 1)
            {
                if (!mergeItemStack(itemstack1, 3, 39, false))
                {
                    return null;
                }
            }
            else if (par1 >= 3 && par1 < 30)
            {
                if (!mergeItemStack(itemstack1, 30, 39, false))
                {
                    return null;
                }
            }
            else if (par1 >= 30 && par1 < 39 && !mergeItemStack(itemstack1, 3, 30, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(itemstack1);
        }

        return itemstack;
    }

    /**
     * Callback for when the crafting gui is closed.
     */
    public void onCraftGuiClosed(EntityPlayer par1EntityPlayer)
    {
        super.onCraftGuiClosed(par1EntityPlayer);
        field_56987_a.func_56218_c_(null);
        super.onCraftGuiClosed(par1EntityPlayer);

        if (field_56986_c.isRemote)
        {
            return;
        }

        ItemStack itemstack = field_56985_b.getStackInSlotOnClosing(0);

        if (itemstack != null)
        {
            par1EntityPlayer.dropPlayerItem(itemstack);
        }

        itemstack = field_56985_b.getStackInSlotOnClosing(1);

        if (itemstack != null)
        {
            par1EntityPlayer.dropPlayerItem(itemstack);
        }
    }
}
