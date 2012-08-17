package net.minecraft.src;

import net.minecraft.client.Minecraft;

public abstract class PlayerController
{
    /** A reference to the Minecraft object. */
    protected final Minecraft mc;
    public boolean isInTestMode;

    public PlayerController(Minecraft par1Minecraft)
    {
        isInTestMode = false;
        mc = par1Minecraft;
    }

    /**
     * Called on world change with the new World as the only parameter.
     */
    public void onWorldChange(World world)
    {
    }

    public boolean func_78763_f()
    {
        return true;
    }

    public void func_78746_a(EnumGameType par1EnumGameType)
    {
    }

    /**
     * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to send a packet indicating the
     * enchantment action the player has taken.
     */
    public void sendEnchantPacket(int par1, int par2)
    {
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    public abstract void clickBlock(int i, int j, int k, int l);

    /**
     * Called when a player completes the destruction of a block
     */
    public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4)
    {
        World world = mc.field_71441_e;
        Block block = Block.blocksList[world.getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }

        world.playAuxSFX(2001, par1, par2, par3, block.blockID + (world.getBlockMetadata(par1, par2, par3) << 12));
        int i = world.getBlockMetadata(par1, par2, par3);
        boolean flag = world.setBlockWithNotify(par1, par2, par3, 0);

        if (flag)
        {
            block.onBlockDestroyedByPlayer(world, par1, par2, par3, i);
        }

        return flag;
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    public abstract void onPlayerDamageBlock(int i, int j, int k, int l);

    /**
     * Resets current block damage and isHittingBlock
     */
    public abstract void resetBlockRemoving();

    public void setPartialTime(float f)
    {
    }

    /**
     * player reach distance = 4F
     */
    public abstract float getBlockReachDistance();

    /**
     * Notifies the server of things like consuming food, etc...
     */
    public boolean sendUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack)
    {
        int i = par3ItemStack.stackSize;
        ItemStack itemstack = par3ItemStack.useItemRightClick(par2World, par1EntityPlayer);

        if (itemstack != par3ItemStack || itemstack != null && itemstack.stackSize != i)
        {
            par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = itemstack;

            if (itemstack.stackSize == 0)
            {
                par1EntityPlayer.inventory.mainInventory[par1EntityPlayer.inventory.currentItem] = null;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public void func_78752_a(ItemStack par1ItemStack)
    {
    }

    /**
     * Flips the player around. Args: player
     */
    public void flipPlayer(EntityPlayer entityplayer)
    {
    }

    public void updateController()
    {
    }

    public abstract boolean shouldDrawHUD();

    public void func_6473_b(EntityPlayer par1EntityPlayer)
    {
        PlayerControllerCreative.disableAbilities(par1EntityPlayer);
        PlayerControllerAdventure.enableAbilities(par1EntityPlayer);
    }

    /**
     * Handles a players right click
     */
    public abstract boolean func_78760_a(EntityPlayer entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l, Vec3 v);

    public EntityPlayer func_78754_a(World par1World)
    {
        return new EntityPlayerSP(mc, par1World, mc.session, par1World.worldProvider.worldType);
    }

    /**
     * Interacts with an entity
     */
    public boolean func_78768_b(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        return par1EntityPlayer.func_70998_m(par2Entity);
    }

    public boolean func_78747_a()
    {
        return false;
    }

    /**
     * Attacks an entity
     */
    public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        par1EntityPlayer.attackTargetEntityWithCurrentItem(par2Entity);
    }

    public ItemStack windowClick(int par1, int par2, int par3, boolean par4, EntityPlayer par5EntityPlayer)
    {
        return par5EntityPlayer.craftingInventory.slotClick(par2, par3, par4, par5EntityPlayer);
    }

    public void func_20086_a(int par1, EntityPlayer par2EntityPlayer)
    {
        par2EntityPlayer.craftingInventory.onCraftGuiClosed(par2EntityPlayer);
        par2EntityPlayer.craftingInventory = par2EntityPlayer.inventorySlots;
    }

    public void func_40593_a(int i, int j)
    {
    }

    public boolean func_35643_e()
    {
        return false;
    }

    public void onStoppedUsingItem(EntityPlayer par1EntityPlayer)
    {
        par1EntityPlayer.stopUsingItem();
    }

    public boolean func_35642_f()
    {
        return false;
    }

    /**
     * Checks if the player is not creative, used for checking if it should break a block instantly
     */
    public boolean isNotCreative()
    {
        return true;
    }

    /**
     * returns true if player is in creative mode
     */
    public boolean isInCreativeMode()
    {
        return false;
    }

    /**
     * true for hitting entities far away.
     */
    public boolean extendedReach()
    {
        return false;
    }

    /**
     * Used in PlayerControllerMP to update the server with an ItemStack in a slot.
     */
    public void sendSlotPacket(ItemStack itemstack, int i)
    {
    }

    public void func_35639_a(ItemStack itemstack)
    {
    }

    public void func_78748_a(EntityPlayer par1EntityPlayer)
    {
//         field_78779_k.func_77147_a(par1EntityPlayer.capabilities);
    }
}
