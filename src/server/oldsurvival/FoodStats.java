package net.minecraft.src;

public class FoodStats
{
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

    public void addStats(int par1, float par2)
    {
        if(mod_OldSurvivalMode.DisableHunger){
            foodLevel = 20;
            foodSaturationLevel = 20;
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

            if(!mod_OldSurvivalMode.DisableHunger){
                if (foodTimer >= 80)
                {
                    par1EntityPlayer.heal(1);
                    foodTimer = 0;
                }
            }
        }
        else if (foodLevel <= 0)
        {
            foodTimer++;

            if (foodTimer >= 80)
            {
                if(!mod_OldSurvivalMode.DisableHunger){
                    if (par1EntityPlayer.getEntityHealth() > 10 || i >= 3 || par1EntityPlayer.getEntityHealth() > 1 && i >= 2)
                    {
                        par1EntityPlayer.attackEntityFrom(DamageSource.starve, 1);
                    }
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
            if(mod_OldSurvivalMode.DisableHunger){
                foodLevel = 20;
                foodTimer = 20;
                foodSaturationLevel = 20;
                foodExhaustionLevel = 20;
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
        if(mod_OldSurvivalMode.DisableHunger){
            par1NBTTagCompound.setInteger("foodLevel", 20);
            par1NBTTagCompound.setInteger("foodTickTimer", 20);
            par1NBTTagCompound.setFloat("foodSaturationLevel", 20);
            par1NBTTagCompound.setFloat("foodExhaustionLevel", 20);
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
        if(mod_OldSurvivalMode.DisableHunger){
            return 20;
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
        if(!mod_OldSurvivalMode.DisableHunger){
            foodExhaustionLevel = Math.min(foodExhaustionLevel + par1, 40F);
        }
    }

    /**
     * Get the player's food saturation level.
     */
    public float getSaturationLevel()
    {
        if(mod_OldSurvivalMode.DisableHunger){
            return 20;
        }
        return foodSaturationLevel;
    }
}
