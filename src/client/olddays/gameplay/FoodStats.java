package net.minecraft.src;

public class FoodStats
{
    public static boolean disabled = false;
    public static int disabledLevel = 20;

    /** The player's food level. */
    private int foodLevel;

    /** The player's food saturation. */
    private float foodSaturationLevel;

    /** The player's food exhaustion. */
    private float foodExhaustionLevel;

    /** The player's food timer value. */
    private int foodTimer;
    private int prevFoodLevel;

    public FoodStats()
    {
        foodLevel = 20;
        prevFoodLevel = 20;
        foodSaturationLevel = 5F;
    }

    /**
     * Args: int foodLevel, float foodSaturationModifier
     */
    public void addStats(int par1, float par2)
    {
        if(disabled){
            foodLevel = disabledLevel;
        }else{
            foodLevel = Math.min(par1 + foodLevel, 20);
        }
        foodSaturationLevel = Math.min(foodSaturationLevel + (float)par1 * par2 * 2.0F, foodLevel);
    }

    /**
     * Eat some food.
     */
    public void addStats(ItemFood par1ItemFood)
    {
        addStats(par1ItemFood.getHealAmount(), par1ItemFood.getSaturationModifier());
    }

    /**
     * Handles the food game logic.
     */
    public void onUpdate(EntityPlayer par1EntityPlayer)
    {
        int i = par1EntityPlayer.worldObj.difficultySetting;
        prevFoodLevel = foodLevel;

        if (disabled){
            foodLevel = disabledLevel;
            return;
        }

        if (foodExhaustionLevel > 4F)
        {
            foodExhaustionLevel -= 4F;

            if (foodSaturationLevel > 0.0F)
            {
                foodSaturationLevel = Math.max(foodSaturationLevel - 1.0F, 0.0F);
            }
            else if (i > 0)
            {
                foodLevel = Math.max(foodLevel - 1, 0);
            }
        }

        if (par1EntityPlayer.worldObj.getGameRules().getGameRuleBooleanValue("naturalRegeneration") && foodLevel >= 18 && par1EntityPlayer.shouldHeal())
        {
            foodTimer++;

            if (foodTimer >= 80)
            {
                par1EntityPlayer.heal(1.0F);
                addExhaustion(3F);
                foodTimer = 0;
            }
        }
        else if (foodLevel <= 0)
        {
            foodTimer++;

            if (foodTimer >= 80)
            {
                if (par1EntityPlayer.getHealth() > 10F || i >= 3 || par1EntityPlayer.getHealth() > 1.0F && i >= 2)
                {
                    par1EntityPlayer.attackEntityFrom(DamageSource.starve, 1.0F);
                }

                foodTimer = 0;
            }
        }
        else
        {
            foodTimer = 0;
        }
    }

    /**
     * Reads food stats from an NBT object.
     */
    public void readNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (par1NBTTagCompound.hasKey("foodLevel"))
        {
            foodLevel = disabled ? disabledLevel : par1NBTTagCompound.getInteger("foodLevel");
            foodTimer = par1NBTTagCompound.getInteger("foodTickTimer");
            foodSaturationLevel = par1NBTTagCompound.getFloat("foodSaturationLevel");
            foodExhaustionLevel = par1NBTTagCompound.getFloat("foodExhaustionLevel");
        }
    }

    /**
     * Writes food stats to an NBT object.
     */
    public void writeNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setInteger("foodLevel", foodLevel);
        par1NBTTagCompound.setInteger("foodTickTimer", foodTimer);
        par1NBTTagCompound.setFloat("foodSaturationLevel", foodSaturationLevel);
        par1NBTTagCompound.setFloat("foodExhaustionLevel", foodExhaustionLevel);
    }

    /**
     * Get the player's food level.
     */
    public int getFoodLevel()
    {
        if(disabled){
            return disabledLevel;
        }
        return foodLevel;
    }

    public int getPrevFoodLevel()
    {
        return prevFoodLevel;
    }

    /**
     * If foodLevel is not max.
     */
    public boolean needFood()
    {
        return foodLevel < 20;
    }

    /**
     * adds input to foodExhaustionLevel to a max of 40
     */
    public void addExhaustion(float par1)
    {
        foodExhaustionLevel = Math.min(foodExhaustionLevel + par1, 40F);
    }

    /**
     * Get the player's food saturation level.
     */
    public float getSaturationLevel()
    {
        return foodSaturationLevel;
    }

    public void setFoodLevel(int par1)
    {
        foodLevel = disabled ? disabledLevel : par1;
    }

    public void setFoodSaturationLevel(float par1)
    {
        foodSaturationLevel = par1;
    }
}
