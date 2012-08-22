package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerMP
{
    private final Minecraft field_78776_a;
    private final NetClientHandler netClientHandler;

    /** PosX of the current block being destroyed */
    private int currentBlockX;

    /** PosY of the current block being destroyed */
    private int currentBlockY;

    /** PosZ of the current block being destroyed */
    private int currentblockZ;

    /** Current block damage (MP) */
    private float curBlockDamageMP;

    /** Previous block damage (MP) */
    private float prevBlockDamageMP;

    /**
     * Tick counter, when it hits 4 it resets back to 0 and plays the step sound
     */
    private float stepSoundTickCounter;

    /**
     * Delays the first damage on the block after the first click on the block
     */
    private int blockHitDelay;

    /** Tells if the player is hitting a block */
    private boolean isHittingBlock;
    private EnumGameType field_78779_k;

    /** Index of the current item held by the player in the inventory hotbar */
    private int currentPlayerItem;

    public PlayerControllerMP(Minecraft par1Minecraft, NetClientHandler par2NetClientHandler)
    {
        currentBlockX = -1;
        currentBlockY = -1;
        currentblockZ = -1;
        curBlockDamageMP = 0.0F;
        prevBlockDamageMP = 0.0F;
        stepSoundTickCounter = 0.0F;
        blockHitDelay = 0;
        isHittingBlock = false;
        field_78779_k = EnumGameType.SURVIVAL;
        currentPlayerItem = 0;
        field_78776_a = par1Minecraft;
        netClientHandler = par2NetClientHandler;
    }

    public static void func_78744_a(Minecraft par0Minecraft, PlayerControllerMP par1PlayerControllerMP, int par2, int par3, int par4, int par5)
    {
        if (!par0Minecraft.field_71441_e.func_72886_a(par0Minecraft.field_71439_g, par2, par3, par4, par5))
        {
            par1PlayerControllerMP.onPlayerDestroyBlock(par2, par3, par4, par5);
        }
    }

    public void func_78748_a(EntityPlayer par1EntityPlayer)
    {
        field_78779_k.func_77147_a(par1EntityPlayer.capabilities);
    }

    public boolean func_78747_a()
    {
        return false;
    }

    public void func_78746_a(EnumGameType par1EnumGameType)
    {
        field_78779_k = par1EnumGameType;
        field_78779_k.func_77147_a(field_78776_a.field_71439_g.capabilities);
    }

    /**
     * Flips the player around. Args: player
     */
    public void flipPlayer(EntityPlayer par1EntityPlayer)
    {
        par1EntityPlayer.rotationYaw = -180F;
    }

    public boolean shouldDrawHUD()
    {
        return field_78779_k.func_77144_e();
    }

    /**
     * Called when a player completes the destruction of a block
     */
    public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4)
    {
        if (field_78779_k.func_77150_c())
        {
            return false;
        }

        World worldclient = (World)field_78776_a.field_71441_e;
        Block block = Block.blocksList[worldclient.getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }

        worldclient.playAuxSFX(2001, par1, par2, par3, block.blockID + (worldclient.getBlockMetadata(par1, par2, par3) << 12));
        int i = worldclient.getBlockMetadata(par1, par2, par3);
        boolean flag = worldclient.setBlockWithNotify(par1, par2, par3, 0);

        if (flag)
        {
            block.onBlockDestroyedByPlayer(worldclient, par1, par2, par3, i);
        }

        if (!field_78779_k.func_77145_d())
        {
            ItemStack itemstack = field_78776_a.field_71439_g.getCurrentEquippedItem();

            if (itemstack != null)
            {
                itemstack.func_77941_a(worldclient, block.blockID, par1, par2, par3, field_78776_a.field_71439_g);

                if (itemstack.stackSize == 0)
                {
                    field_78776_a.field_71439_g.destroyCurrentEquippedItem();
                }
            }
        }

        return flag;
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    public void clickBlock(int par1, int par2, int par3, int par4)
    {
        if (field_78779_k.func_77150_c())
        {
            return;
        }

        if (field_78779_k.func_77145_d())
        {
            netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
            func_78744_a(field_78776_a, this, par1, par2, par3, par4);
            blockHitDelay = 5;
        }
        else if (!isHittingBlock || par1 != currentBlockX || par2 != currentBlockY || par3 != currentblockZ)
        {
            netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
            int i = field_78776_a.field_71441_e.getBlockId(par1, par2, par3);

            if (i > 0 && curBlockDamageMP == 0.0F)
            {
                Block.blocksList[i].onBlockClicked(field_78776_a.field_71441_e, par1, par2, par3, field_78776_a.field_71439_g);
            }

            if (i > 0 && Block.blocksList[i].func_71908_a(field_78776_a.field_71439_g, field_78776_a.field_71439_g.worldObj, par1, par2, par3) >= 1.0F)
            {
                onPlayerDestroyBlock(par1, par2, par3, par4);
            }
            else
            {
                isHittingBlock = true;
                currentBlockX = par1;
                currentBlockY = par2;
                currentblockZ = par3;
                curBlockDamageMP = 0.0F;
                prevBlockDamageMP = 0.0F;
                stepSoundTickCounter = 0.0F;
                field_78776_a.field_71441_e.func_72888_f(field_78776_a.field_71439_g.entityId, currentBlockX, currentBlockY, currentblockZ, (int)(curBlockDamageMP * 10F) - 1);
            }
        }
    }

    /**
     * Resets current block damage and isHittingBlock
     */
    public void resetBlockRemoving()
    {
        if (isHittingBlock)
        {
            netClientHandler.addToSendQueue(new Packet14BlockDig(1, currentBlockX, currentBlockY, currentblockZ, -1));
        }

        isHittingBlock = false;
        curBlockDamageMP = 0.0F;
        field_78776_a.field_71441_e.func_72888_f(field_78776_a.field_71439_g.entityId, currentBlockX, currentBlockY, currentblockZ, -1);
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    public void onPlayerDamageBlock(int par1, int par2, int par3, int par4)
    {
        syncCurrentPlayItem();

        if (blockHitDelay > 0)
        {
            blockHitDelay--;
            return;
        }

        if (field_78779_k.func_77145_d())
        {
            blockHitDelay = 5;
            netClientHandler.addToSendQueue(new Packet14BlockDig(0, par1, par2, par3, par4));
            func_78744_a(field_78776_a, this, par1, par2, par3, par4);
            return;
        }

        if (par1 == currentBlockX && par2 == currentBlockY && par3 == currentblockZ)
        {
            int i = field_78776_a.field_71441_e.getBlockId(par1, par2, par3);

            if (i == 0)
            {
                isHittingBlock = false;
                return;
            }

            Block block = Block.blocksList[i];
            curBlockDamageMP += block.func_71908_a(field_78776_a.field_71439_g, field_78776_a.field_71439_g.worldObj, par1, par2, par3);

            if (stepSoundTickCounter % 4F == 0.0F && block != null)
            {
                field_78776_a.sndManager.playSound(block.stepSound.getStepSound(), (float)par1 + 0.5F, (float)par2 + 0.5F, (float)par3 + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
            }

            stepSoundTickCounter++;

            if (curBlockDamageMP >= 1.0F)
            {
                isHittingBlock = false;
                netClientHandler.addToSendQueue(new Packet14BlockDig(2, par1, par2, par3, par4));
                onPlayerDestroyBlock(par1, par2, par3, par4);
                curBlockDamageMP = 0.0F;
                prevBlockDamageMP = 0.0F;
                stepSoundTickCounter = 0.0F;
                blockHitDelay = 5;
            }

            field_78776_a.field_71441_e.func_72888_f(field_78776_a.field_71439_g.entityId, currentBlockX, currentBlockY, currentblockZ, (int)(curBlockDamageMP * 10F) - 1);
        }
        else
        {
            clickBlock(par1, par2, par3, par4);
        }
    }

    /**
     * player reach distance = 4F
     */
    public float getBlockReachDistance()
    {
        return !field_78779_k.func_77145_d() ? 4.5F : 5F;
    }

    public void updateController()
    {
        syncCurrentPlayItem();
        prevBlockDamageMP = curBlockDamageMP;
        field_78776_a.sndManager.playRandomMusicIfReady();
    }

    /**
     * Syncs the current player item with the server
     */
    private void syncCurrentPlayItem()
    {
        int i = field_78776_a.field_71439_g.inventory.currentItem;

        if (i != currentPlayerItem)
        {
            currentPlayerItem = i;
            netClientHandler.addToSendQueue(new Packet16BlockItemSwitch(currentPlayerItem));
        }
    }

    public boolean func_78760_a(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3)
    {
        syncCurrentPlayItem();
        float f = (float)par8Vec3.xCoord - (float)par4;
        float f1 = (float)par8Vec3.yCoord - (float)par5;
        float f2 = (float)par8Vec3.zCoord - (float)par6;
        boolean flag = false;
        int i = par2World.getBlockId(par4, par5, par6);

        if (i > 0 && Block.blocksList[i].func_71903_a(par2World, par4, par5, par6, par1EntityPlayer, par7, f, f1, f2))
        {
            flag = true;
        }

        if (!flag && par3ItemStack != null && (par3ItemStack.getItem() instanceof ItemBlock))
        {
            ItemBlock itemblock = (ItemBlock)par3ItemStack.getItem();

            if (!itemblock.func_77884_a(par2World, par4, par5, par6, par7, par1EntityPlayer, par3ItemStack))
            {
                return false;
            }
        }

        netClientHandler.addToSendQueue(new Packet15Place(par4, par5, par6, par7, par1EntityPlayer.inventory.getCurrentItem(), f, f1, f2));

        if (flag)
        {
            return true;
        }

        if (par3ItemStack == null)
        {
            return false;
        }

        if (field_78779_k.func_77145_d())
        {
            int j = par3ItemStack.getItemDamage();
            int k = par3ItemStack.stackSize;
            boolean flag1 = par3ItemStack.func_77943_a(par1EntityPlayer, par2World, par4, par5, par6, par7, f, f1, f2);
            par3ItemStack.setItemDamage(j);
            par3ItemStack.stackSize = k;
            return flag1;
        }
        else
        {
            return par3ItemStack.func_77943_a(par1EntityPlayer, par2World, par4, par5, par6, par7, f, f1, f2);
        }
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    public boolean sendUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet15Place(-1, -1, -1, 255, par1EntityPlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
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

    public EntityClientPlayerMP func_78754_a(World par1World)
    {
        return new EntityClientPlayerMP(field_78776_a, par1World, field_78776_a.session, netClientHandler);
    }

    /**
     * Attacks an entity
     */
    public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet7UseEntity(par1EntityPlayer.entityId, par2Entity.entityId, 1));
        par1EntityPlayer.attackTargetEntityWithCurrentItem(par2Entity);
    }

    public boolean func_78768_b(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet7UseEntity(par1EntityPlayer.entityId, par2Entity.entityId, 0));
        return par1EntityPlayer.func_70998_m(par2Entity);
    }

    public ItemStack windowClick(int par1, int par2, int par3, boolean par4, EntityPlayer par5EntityPlayer)
    {
        short word0 = par5EntityPlayer.craftingInventory.getNextTransactionID(par5EntityPlayer.inventory);
        ItemStack itemstack = par5EntityPlayer.craftingInventory.slotClick(par2, par3, par4, par5EntityPlayer);
        netClientHandler.addToSendQueue(new Packet102WindowClick(par1, par2, par3, par4, itemstack, word0));
        return itemstack;
    }

    /**
     * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to send a packet indicating the
     * enchantment action the player has taken.
     */
    public void sendEnchantPacket(int par1, int par2)
    {
        netClientHandler.addToSendQueue(new Packet108EnchantItem(par1, par2));
    }

    /**
     * Used in PlayerControllerMP to update the server with an ItemStack in a slot.
     */
    public void sendSlotPacket(ItemStack par1ItemStack, int par2)
    {
        if (field_78779_k.func_77145_d())
        {
            netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(par2, par1ItemStack));
        }
    }

    public void func_78752_a(ItemStack par1ItemStack)
    {
        if (field_78779_k.func_77145_d() && par1ItemStack != null)
        {
            netClientHandler.addToSendQueue(new Packet107CreativeSetSlot(-1, par1ItemStack));
        }
    }

    public void onStoppedUsingItem(EntityPlayer par1EntityPlayer)
    {
        syncCurrentPlayItem();
        netClientHandler.addToSendQueue(new Packet14BlockDig(5, 0, 0, 0, 255));
        par1EntityPlayer.stopUsingItem();
    }

    public boolean func_78763_f()
    {
        return true;
    }

    /**
     * Checks if the player is not creative, used for checking if it should break a block instantly
     */
    public boolean isNotCreative()
    {
        return !field_78779_k.func_77145_d();
    }

    /**
     * returns true if player is in creative mode
     */
    public boolean isInCreativeMode()
    {
        return field_78779_k.func_77145_d();
    }

    /**
     * true for hitting entities far away.
     */
    public boolean extendedReach()
    {
        return field_78779_k.func_77145_d();
    }
}
