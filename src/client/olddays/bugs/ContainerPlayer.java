package net.minecraft.src;

import java.util.List;

public class ContainerPlayer extends Container
{
    public static boolean dropCrafting = true;

    /** The crafting matrix inventory. */
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;

    /** Determines if inventory manipulation should be handled. */
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;

    public ContainerPlayer(InventoryPlayer par1InventoryPlayer, boolean par2, EntityPlayer par3EntityPlayer)
    {
        craftMatrix = new InventoryCrafting(this, 2, 2);
        craftResult = new InventoryCraftResult();
        isLocalWorld = par2;
        thePlayer = par3EntityPlayer;
        addSlotToContainer(new SlotCrafting(par1InventoryPlayer.player, craftMatrix, craftResult, 0, 144, 36));

        for (int i = 0; i < 2; i++)
        {
            for (int i1 = 0; i1 < 2; i1++)
            {
                addSlotToContainer(new Slot(craftMatrix, i1 + i * 2, 88 + i1 * 18, 26 + i * 18));
            }
        }

        for (int j = 0; j < 4; j++)
        {
            int j1 = j;
            addSlotToContainer(new SlotArmor(this, par1InventoryPlayer, par1InventoryPlayer.getSizeInventory() - 1 - j, 8, 8 + j * 18, j1));
        }

        for (int k = 0; k < 3; k++)
        {
            for (int k1 = 0; k1 < 9; k1++)
            {
                addSlotToContainer(new Slot(par1InventoryPlayer, k1 + (k + 1) * 9, 8 + k1 * 18, 84 + k * 18));
            }
        }

        for (int l = 0; l < 9; l++)
        {
            addSlotToContainer(new Slot(par1InventoryPlayer, l, 8 + l * 18, 142));
        }

        onCraftMatrixChanged(craftMatrix);
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    public void onCraftMatrixChanged(IInventory par1IInventory)
    {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(craftMatrix, thePlayer.worldObj));
    }

    /**
     * Called when the container is closed.
     */
    public void onContainerClosed(EntityPlayer par1EntityPlayer)
    {
        super.onContainerClosed(par1EntityPlayer);

        if (dropCrafting){
            for (int i = 0; i < 4; i++)
            {
                ItemStack itemstack = craftMatrix.getStackInSlotOnClosing(i);

                if (itemstack != null)
                {
                    par1EntityPlayer.dropPlayerItem(itemstack);
                }
            }
        }

        craftResult.setInventorySlotContents(0, null);
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return true;
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 0)
            {
                if (!mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 >= 1 && par2 < 5)
            {
                if (!mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 5 && par2 < 9)
            {
                if (!mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if ((itemstack.getItem() instanceof ItemArmor) && !((Slot)inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack())
            {
                int i = 5 + ((ItemArmor)itemstack.getItem()).armorType;

                if (!mergeItemStack(itemstack1, i, i + 1, false))
                {
                    return null;
                }
            }
            else if (par2 >= 9 && par2 < 36)
            {
                if (!mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (par2 >= 36 && par2 < 45)
            {
                if (!mergeItemStack(itemstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 9, 45, false))
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

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }

    public boolean func_94530_a(ItemStack par1ItemStack, Slot par2Slot)
    {
        return par2Slot.inventory != craftResult && super.func_94530_a(par1ItemStack, par2Slot);
    }
}
