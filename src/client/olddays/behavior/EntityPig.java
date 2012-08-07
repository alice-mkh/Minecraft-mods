package net.minecraft.src;

import java.util.Random;

public class EntityPig extends EntityAnimal
{
    public static boolean survivaltest = false;

    public EntityPig(World par1World)
    {
        super(par1World);
        texture = "/mob/pig.png";
        setSize(0.9F, 0.9F);
        getNavigator().setAvoidsWater(true);
        float f = 0.25F;
        tasks.addTask(0, new EntityAISwimming(this));
        tasks.addTask(1, new EntityAIPanic(this, 0.38F));
        tasks.addTask(2, new EntityAIMate(this, f));
        tasks.addTask(3, new EntityAITempt(this, 0.25F, Item.wheat.shiftedIndex, false));
        tasks.addTask(4, new EntityAIFollowParent(this, 0.28F));
        tasks.addTask(5, new EntityAIWander(this, f));
        tasks.addTask(6, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 6F));
        tasks.addTask(7, new EntityAILookIdle(this));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    public int getMaxHealth()
    {
        return survivaltest ? 6 : 10;
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
        return "mob.pig";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.pig";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.pigdeath";
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
            return Item.porkCooked.shiftedIndex;
        }
        else
        {
            return Item.porkRaw.shiftedIndex;
        }
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = rand.nextInt(3) + 1 + rand.nextInt(1 + par2);

        for (int j = 0; j < i; j++)
        {
            if (isBurning())
            {
                dropItem(Item.porkCooked.shiftedIndex, 1);
            }
            else
            {
                dropItem(Item.porkRaw.shiftedIndex, 1);
            }
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
    public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal)
    {
        return new EntityPig(worldObj);
    }
}
