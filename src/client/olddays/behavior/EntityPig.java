package net.minecraft.src;

import java.util.Random;

public class EntityPig extends EntityAnimal
{
    public static boolean survivaltest = false;
    public static boolean fixai = false;

    /** AI task for player control. */
    private final EntityAIControlledByPlayer aiControlledByPlayer;

    public EntityPig(World par1World)
    {
        super(par1World);
        setSize(0.9F, 0.9F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        tasks.addTask(2, aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
        tasks.addTask(3, new EntityAIMate(this, 1.0D));
        tasks.addTask(4, new EntityAITempt(this, 1.2D, Item.carrotOnAStick.itemID, false));
        tasks.addTask(4, new EntityAITempt(this, 1.2D, Item.carrot.itemID, false));
        tasks.addTask(5, new EntityAIFollowParent(this, 1.1000000000000001D));
        tasks.addTask(6, new EntityAIWander(this, 1.0D));
        tasks.addTask(7, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 6F));
        tasks.addTask(8, new EntityAILookIdle(this));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(survivaltest ? 6D : 10D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }

    protected void updateAITasks()
    {
        super.updateAITasks();
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
        ItemStack itemstack = ((EntityPlayer)riddenByEntity).getHeldItem();
        return itemstack != null && itemstack.itemID == Item.carrotOnAStick.itemID;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Saddle", getSaddled());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.pig.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.pig.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.pig.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        playSound("mob.pig.step", 0.15F, 1.0F);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        if (!super.interact(par1EntityPlayer))
        {
            if (getSaddled() && !worldObj.isRemote && (riddenByEntity == null || riddenByEntity == par1EntityPlayer))
            {
                par1EntityPlayer.mountEntity(this);
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return true;
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        if (isBurning())
        {
            return Item.porkCooked.itemID;
        }
        else
        {
            return Item.porkRaw.itemID;
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = rand.nextInt(3) + 1 + rand.nextInt(1 + par2);

        for (int j = 0; j < i; j++)
        {
            if (isBurning())
            {
                dropItem(Item.porkCooked.itemID, 1);
            }
            else
            {
                dropItem(Item.porkRaw.itemID, 1);
            }
        }

        if (getSaddled())
        {
            dropItem(Item.saddle.itemID, 1);
        }
    }

    /**
     * Returns true if the pig is saddled.
     */
    public boolean getSaddled()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Set or remove the saddle of the pig.
     */
    public void setSaddled(boolean par1)
    {
        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)1));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)0));
        }
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        else
        {
            EntityPigZombie entitypigzombie = new EntityPigZombie(worldObj);
            entitypigzombie.setLocationAndAngles(posX, posY, posZ, rotationYaw, rotationPitch);
            worldObj.spawnEntityInWorld(entitypigzombie);
            setDead();
            return;
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        super.fall(par1);

        if (par1 > 5F && (riddenByEntity instanceof EntityPlayer))
        {
            ((EntityPlayer)riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityPig spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        return new EntityPig(worldObj);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.itemID == Item.carrot.itemID;
    }

    /**
     * Return the AI task for player control.
     */
    public EntityAIControlledByPlayer getAIControlledByPlayer()
    {
        return aiControlledByPlayer;
    }

    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return spawnBabyAnimal(par1EntityAgeable);
    }

    @Override
    protected void updateEntityActionState(){
        if (fixai && aiControlledByPlayer.shouldExecute()){
            aiControlledByPlayer.updateTask();
        }else{
            super.updateEntityActionState();
        }
    }
}
