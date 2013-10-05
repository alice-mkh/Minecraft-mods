package net.minecraft.src;

import java.util.Random;

public class ItemBow extends Item
{
    public static boolean nocharging = false;
    public static boolean nodurability = false;

    public static final String bowPullIconNameArray[] =
    {
        "pulling_0", "pulling_1", "pulling_2"
    };
    private Icon iconArray[];

    public ItemBow(int par1)
    {
        super(par1);
        maxStackSize = 1;
        setMaxDamage(384);
        setCreativeTab(CreativeTabs.tabCombat);
    }

    /**
     * called when the player releases the use item button. Args: itemstack, world, entityplayer, itemInUseCount
     */
    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
        boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

        if (flag || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID))
        {
            int i = getMaxItemUseDuration(par1ItemStack) - par4;
            float f = (float)i / 20F;
            f = (f * f + f * 2.0F) / 3F;

            if ((double)f < 0.10000000000000001D)
            {
                return;
            }

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            EntityArrow entityarrow = new EntityArrow(par2World, par3EntityPlayer, f * 2.0F);

            if (f == 1.0F)
            {
                entityarrow.setIsCritical(true);
            }

            int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

            if (j > 0)
            {
                entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

            if (k > 0)
            {
                entityarrow.setKnockbackStrength(k);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
            {
                entityarrow.setFire(100);
            }

            if (!nodurability){
                par1ItemStack.damageItem(1, par3EntityPlayer);
            }
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (flag)
            {
                entityarrow.canBePickedUp = 2;
            }
            else
            {
                par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.itemID);
            }

            if (!par2World.isRemote)
            {
                par2World.spawnEntityInWorld(entityarrow);
            }
        }
    }

    public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        return par1ItemStack;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 0x11940;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.bow;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        boolean flag = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
        if(!nocharging){
            if(flag || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID)){
                par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
            }
        }else if(flag || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID)){
            if (!nodurability){
                par1ItemStack.damageItem(1, par3EntityPlayer);
            }
            EntityArrow entityarrow = new EntityArrow(par2World, par3EntityPlayer, 1.0F);
            /*int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
            if (j > 0)
            {
                entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            }*/
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);
            if (k > 0)
            {
                entityarrow.setKnockbackStrength(k);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
            {
                entityarrow.setFire(100);
            }
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
            if (!flag){
                par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.itemID);
            }else{
                entityarrow.canBePickedUp = 2;
            }
            if(!par2World.isRemote){
                par2World.spawnEntityInWorld(entityarrow);
            }
        }
        return par1ItemStack;
    }

    /**
     * Return the enchantability factor of the item, most of the time is based on material.
     */
    public int getItemEnchantability()
    {
        return 1;
    }

    public void registerIcons(IconRegister par1IconRegister)
    {
        itemIcon = par1IconRegister.registerIcon((new StringBuilder()).append(getIconString()).append("_standby").toString());
        iconArray = new Icon[bowPullIconNameArray.length];

        for (int i = 0; i < iconArray.length; i++)
        {
            iconArray[i] = par1IconRegister.registerIcon((new StringBuilder()).append(getIconString()).append("_").append(bowPullIconNameArray[i]).toString());
        }
    }

    /**
     * used to cycle through icons based on their used duration, i.e. for the bow
     */
    public Icon getItemIconForUseDuration(int par1)
    {
        return iconArray[par1];
    }
}
