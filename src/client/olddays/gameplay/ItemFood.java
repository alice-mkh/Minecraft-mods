package net.minecraft.src;

import java.util.Random;

public class ItemFood extends Item
{
    public static boolean heal = false;
    public static boolean instant = false;
    public static boolean stacks = true;

    public final int field_35430_a = 32;

    /** The amount this food item heals the player. */
    private final int healAmount;
    private final float saturationModifier;

    /** Whether wolves like this food (true for raw and cooked porkchop). */
    private final boolean isWolfsFavoriteMeat;

    /**
     * If this field is true, the food can be consumed even if the player don't need to eat.
     */
    private boolean alwaysEdible;

    /**
     * represents the potion effect that will occurr upon eating this food. Set by setPotionEffect
     */
    private int potionId;

    /** set by setPotionEffect */
    private int potionDuration;

    /** set by setPotionEffect */
    private int potionAmplifier;

    /** probably of the set potion effect occurring */
    private float potionEffectProbability;

    public ItemFood(int par1, int par2, float par3, boolean par4)
    {
        super(par1);
        healAmount = par2;
        isWolfsFavoriteMeat = par4;
        saturationModifier = par3;
    }

    public ItemFood(int par1, int par2, boolean par3)
    {
        this(par1, par2, 0.6F, par3);
    }

    public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        par1ItemStack.stackSize--;
        if(!heal){
            par3EntityPlayer.getFoodStats().addStats(this);
        }
        par2World.playSoundAtEntity(par3EntityPlayer, "random.burp", 0.5F, par2World.rand.nextFloat() * 0.1F + 0.9F);

        if (!par2World.isRemote){
            if(heal){
                if (this.shiftedIndex==Item.appleGold.shiftedIndex){
                    par3EntityPlayer.heal(20);
                }else if (this.shiftedIndex==Item.spiderEye.shiftedIndex || this.shiftedIndex==Item.rottenFlesh.shiftedIndex){
                    par3EntityPlayer.heal(-4);
                }else{
                    par3EntityPlayer.heal(healAmount);
                }
            }else{
                if (potionId > 0 && par2World.rand.nextFloat() < potionEffectProbability)
                {
                    par3EntityPlayer.addPotionEffect(new PotionEffect(potionId, potionDuration * 20, potionAmplifier));
                }
            }
        }

        return par1ItemStack;
    }

    public int getItemStackLimit(){
        if (!stacks){
            if (this.shiftedIndex==Item.cookie.shiftedIndex || this.shiftedIndex==Item.melon.shiftedIndex){
                return 8;
            }else{
                return 1;
            }
        }else{
            return maxStackSize;
        }
    }

    public Item setMaxStackSize(int i){
        maxStackSize = i;
        return this;
    }

    /**
     * How long it takes to use or consume an item
     */
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 32;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.eat;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
    {
        if (par3EntityPlayer.canEat(alwaysEdible))
        {
            par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
        }

        return par1ItemStack;
    }

    public int getHealAmount()
    {
        return healAmount;
    }

    /**
     * gets the saturationModifier of the ItemFood
     */
    public float getSaturationModifier()
    {
        return saturationModifier;
    }

    /**
     * Whether wolves like this food (true for raw and cooked porkchop).
     */
    public boolean isWolfsFavoriteMeat()
    {
        return isWolfsFavoriteMeat;
    }

    /**
     * sets a potion effect on the item. Args: int potionId, int duration (will be multiplied by 20), int amplifier,
     * float probability of effect happening
     */
    public ItemFood setPotionEffect(int par1, int par2, int par3, float par4)
    {
        potionId = par1;
        potionDuration = par2;
        potionAmplifier = par3;
        potionEffectProbability = par4;
        return this;
    }

    /**
     * Set the field 'alwaysEdible' to true, and make the food edible even if the player don't need to eat.
     */
    public ItemFood setAlwaysEdible()
    {
        alwaysEdible = true;
        return this;
    }

    /**
     * set name of item from language file
     */
    public Item setItemName(String par1Str)
    {
        return super.setItemName(par1Str);
    }
}
