package net.minecraft.src;

public class FoodStats
{
    public static boolean disabled = false;
    private static int disabledLevel = 20;

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
        foodTimer = 0;
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
            foodSaturationLevel = disabledLevel;
        }else{
            foodLevel = Math.min(par1 + foodLevel, 20);
            foodSaturationLevel = Math.min(foodSaturationLevel + (float)par1 * par2 * 2.0F, foodLevel);
        }
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

        if (foodLevel >= 18 && par1EntityPlayer.shouldHeal())
        {
            foodTimer++;

            if (foodTimer >= 80 && !disabled)
            {
                par1EntityPlayer.heal(1);
                foodTimer = 0;
            }
        }
        else if (foodLevel <= 0)
        {
            foodTimer++;

            if (foodTimer >= 80)
            {
                if (par1EntityPlayer.getHealth() > 10 || i >= 3 || par1EntityPlayer.getHealth() > 1 && i >= 2 && !disabled)
                {
                    par1EntityPlayer.attackEntityFrom(DamageSource.starve, 1);
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
     * Reads the food data for the player.
     */
    public void readNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (par1NBTTagCompound.hasKey("foodLevel"))
        {
            if(disabled){
                foodLevel = disabledLevel;
                foodTimer = disabledLevel;
                foodSaturationLevel = disabledLevel;
                foodExhaustionLevel = disabledLevel;
            }else{
                foodLevel = par1NBTTagCompound.getInteger("foodLevel");
                foodTimer = par1NBTTagCompound.getInteger("foodTickTimer");
                foodSaturationLevel = par1NBTTagCompound.getFloat("foodSaturationLevel");
                foodExhaustionLevel = par1NBTTagCompound.getFloat("foodExhaustionLevel");
            }
        }
    }

    /**
     * Writes the food data for the player.
     */
    public void writeNBT(NBTTagCompound par1NBTTagCompound)
    {
        if(disabled){
            par1NBTTagCompound.setInteger("foodLevel", disabledLevel);
            par1NBTTagCompound.setInteger("foodTickTimer", disabledLevel);
            par1NBTTagCompound.setFloat("foodSaturationLevel", disabledLevel);
            par1NBTTagCompound.setFloat("foodExhaustionLevel", disabledLevel);
        }else{
            par1NBTTagCompound.setInteger("foodLevel", foodLevel);
            par1NBTTagCompound.setInteger("foodTickTimer", foodTimer);
            par1NBTTagCompound.setFloat("foodSaturationLevel", foodSaturationLevel);
            par1NBTTagCompound.setFloat("foodExhaustionLevel", foodExhaustionLevel);
        }
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

    /**
     * Get whether the player must eat food.
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
        if(!disabled){
            foodExhaustionLevel = Math.min(foodExhaustionLevel + par1, 40F);
        }
    }

    /**
     * Get the player's food saturation level.
     */
    public float getSaturationLevel()
    {
        if(disabled){
            return disabledLevel;
        }
        return foodSaturationLevel;
    }
}
