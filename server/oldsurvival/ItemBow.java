package net.minecraft.src;

import java.util.Random;

public class ItemBow extends Item
{
    public ItemBow(int par1)
    {
        super(par1);
        maxStackSize = 1;
        setMaxDamage(384);
    }

    public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4)
    {
        boolean flag = par3EntityPlayer.capabilities.depleteBuckets || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;

        if (flag || par3EntityPlayer.inventory.hasItem(Item.arrow.shiftedIndex))
        {
            int i = getMaxItemUseDuration(par1ItemStack) - par4;
            float f = (float)i / 20F;
            f = (f * f + f * 2.0F) / 3F;

            if ((double)f < 0.1D)
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
                entityarrow.arrowCritical = true;
            }

            int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);

            if (j > 0)
            {
                entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
            }

            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);

            if (k > 0)
            {
                entityarrow.func_46007_b(k);
            }

            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0)
            {
                entityarrow.setFire(100);
            }

            if (!mod_OldSurvivalMode.InfiniteBow){
                par1ItemStack.damageItem(1, par3EntityPlayer);
            }
            par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            if (!flag)
            {
                par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex);
            }
            else
            {
                entityarrow.doesArrowBelongToPlayer = false;
            }

            if (!par2World.isRemote)
            {
                par2World.spawnEntityInWorld(entityarrow);
            }
        }
    }

    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
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
        if(!mod_OldSurvivalMode.InstantBow){
            if(par3EntityPlayer.inventory.hasItem(Item.arrow.shiftedIndex))
            {
                par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
            }
        }else{
            if(par3EntityPlayer.capabilities.depleteBuckets || par3EntityPlayer.inventory.hasItem(Item.arrow.shiftedIndex))
            {
                par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 0.8F));
                if(!par2World.isRemote)
                {
                    par2World.spawnEntityInWorld(new EntityArrow(par2World, par3EntityPlayer, 1.0F));
                }
                if (!par3EntityPlayer.capabilities.depleteBuckets)
                {
                    par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex);
                }
                if (!mod_OldSurvivalMode.InfiniteBow){
                    par1ItemStack.damageItem(1, par3EntityPlayer);
                }
            }
        }

        return par1ItemStack;
    }

    public int getItemEnchantability()
    {
        return 1;
    }
}
