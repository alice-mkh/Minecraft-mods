package net.minecraft.src.ssp;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.Session;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class PlayerController extends PlayerControllerMP
{
    public static boolean oldreach = false;

    /** A reference to the Minecraft object. */
    protected final Minecraft mc;
    public boolean isInTestMode;

    public PlayerController(Minecraft par1Minecraft)
    {
        super(par1Minecraft, null);
        isInTestMode = false;
        mc = par1Minecraft;
    }

    @Override
    public boolean func_78763_f()
    {
        return true;
    }

    @Override
    public void setGameType(EnumGameType par1EnumGameType)
    {
    }

    /**
     * GuiEnchantment uses this during multiplayer to tell PlayerControllerMP to send a packet indicating the
     * enchantment action the player has taken.
     */
    @Override
    public void sendEnchantPacket(int par1, int par2)
    {
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    @Override
    public void clickBlock(int i, int j, int k, int l)
    {
    }

    /**
     * Called when a player completes the destruction of a block
     */
    @Override
    public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4)
    {
        World world = mc.theWorld;
        Block block = Block.blocksList[world.getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }

        world.playAuxSFX(2001, par1, par2, par3, block.blockID + (world.getBlockMetadata(par1, par2, par3) << 12));
        int i = world.getBlockMetadata(par1, par2, par3);
        boolean flag = world.setBlock(par1, par2, par3, 0, 0, 3);

        if (flag)
        {
            block.onBlockDestroyedByPlayer(world, par1, par2, par3, i);
        }

        return flag;
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    @Override
    public void onPlayerDamageBlock(int i, int j, int k, int l)
    {
    }

    /**
     * Resets current block damage and isHittingBlock
     */
    @Override
    public void resetBlockRemoving()
    {
    }

    /**
     * player reach distance = 4F
     */
    @Override
    public float getBlockReachDistance(){
        return oldreach ? 4F : 5F;
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    @Override
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

    @Override
    public void func_78752_a(ItemStack par1ItemStack)
    {
    }

    /**
     * Flips the player around. Args: player
     */
    @Override
    public void flipPlayer(EntityPlayer entityplayer)
    {
    }

    @Override
    public void updateController()
    {
    }

    @Override
    public boolean shouldDrawHUD(){
        return true;
    }

    @Override
    public void setPlayerCapabilities(EntityPlayer par1EntityPlayer)
    {
        mc.setGameMode(EnumGameType.SURVIVAL);
    }

    /**
     * Handles a players right click
     */
    @Override
    public boolean onPlayerRightClick(EntityPlayer entityplayer, World world, ItemStack itemstack, int i, int j, int k, int l, Vec3 v)
    {
        return false;
    }

    @Override
    public EntityClientPlayerMP func_78754_a(World par1World)
    {
        try{
            Object o = mc.playerClass.getDeclaredConstructor(new Class[]{Minecraft.class, World.class, Session.class, Integer.TYPE}).
                       newInstance(new Object[]{mc, par1World, mc.getSession(), par1World.provider.dimensionId});
            return (EntityPlayerSP2)o;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return null;
//         return new EntityPlayerSP2(mc, par1World, mc.getSession(), par1World.provider.dimensionId);
    }

    /**
     * Interacts with an entity
     */
    @Override
    public boolean func_78768_b(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        return par1EntityPlayer.interactWith(par2Entity);
    }

    /**
     * Attacks an entity
     */
    @Override
    public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        par1EntityPlayer.attackTargetEntityWithCurrentItem(par2Entity);
    }

    @Override
    public ItemStack windowClick(int par1, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        return par5EntityPlayer.openContainer.slotClick(par2, par3, par4, par5EntityPlayer);
    }

    public boolean func_35643_e()
    {
        return false;
    }

    @Override
    public void onStoppedUsingItem(EntityPlayer par1EntityPlayer)
    {
        par1EntityPlayer.stopUsingItem();
    }

    /**
     * Checks if the player is not creative, used for checking if it should break a block instantly
     */
    @Override
    public boolean isNotCreative()
    {
        return true;
    }

    /**
     * returns true if player is in creative mode
     */
    @Override
    public boolean isInCreativeMode()
    {
        return false;
    }

    /**
     * true for hitting entities far away.
     */
    @Override
    public boolean extendedReach()
    {
        return false;
    }

    /**
     * Used in PlayerControllerMP to update the server with an ItemStack in a slot.
     */
    @Override
    public void sendSlotPacket(ItemStack itemstack, int i)
    {
        EntityPlayer playerEntity = Minecraft.getMinecraft().thePlayer;
        if (Minecraft.getMinecraft().playerController.isInCreativeMode())
        {
            boolean flag = itemstack == null || itemstack.itemID < Item.itemsList.length && itemstack.itemID >= 0 && Item.itemsList[itemstack.itemID] != null;
            boolean flag1 = itemstack == null || itemstack.getItemDamage() >= 0 && itemstack.getItemDamage() >= 0 && itemstack.stackSize <= 64 && itemstack.stackSize > 0;

            if (i >= 1 && i < 36 + InventoryPlayer.getHotbarSize() && flag && flag1)
            {
                if (itemstack == null)
                {
                    playerEntity.inventoryContainer.putStackInSlot(i, null);
                }
                else
                {
                    playerEntity.inventoryContainer.putStackInSlot(i, itemstack);
                }

                playerEntity.inventoryContainer.setPlayerIsPresent(playerEntity, true);
            }
            else if (i < 0 && flag && flag1)
            {
                EntityItem entityitem = playerEntity.dropPlayerItem(itemstack);

                if (entityitem != null)
                {
                    entityitem.setAgeToCreativeDespawnTime();
                }
            }
        }
    }

    public static void clickBlockCreative(Minecraft par0Minecraft, PlayerControllerMP par1PlayerControllerMP, int par2, int par3, int par4, int par5)
    {
    }

}
