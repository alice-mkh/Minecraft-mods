package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityOcelot extends EntityTameable
{
    public static boolean despawn = false;

    /**
     * The tempt AI task for this mob, used to prevent taming while it is fleeing.
     */
    private EntityAITempt aiTempt;

    public EntityOcelot(World par1World)
    {
        super(par1World);
        setSize(0.6F, 0.8F);
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(3, aiTempt = new EntityAITempt(this, 0.59999999999999998D, Item.fishRaw.itemID, true));
        tasks.addTask(4, new EntityAIAvoidEntity(this, net.minecraft.src.EntityPlayer.class, 16F, 0.80000000000000004D, 1.3300000000000001D));
        tasks.addTask(5, new EntityAIFollowOwner(this, 1.0D, 10F, 5F));
        tasks.addTask(6, new EntityAIOcelotSit(this, 1.3300000000000001D));
        tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        tasks.addTask(8, new EntityAIOcelotAttack(this));
        tasks.addTask(9, new EntityAIMate(this, 0.80000000000000004D));
        tasks.addTask(10, new EntityAIWander(this, 0.80000000000000004D));
        tasks.addTask(11, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 10F));
        targetTasks.addTask(1, new EntityAITargetNonTamed(this, net.minecraft.src.EntityChicken.class, 750, false));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(18, Byte.valueOf((byte)0));
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    public void updateAITick()
    {
        if (getMoveHelper().isUpdating())
        {
            double d = getMoveHelper().getSpeed();

            if (d == 0.59999999999999998D)
            {
                setSneaking(true);
                setSprinting(false);
            }
            else if (d == 1.3300000000000001D)
            {
                setSneaking(false);
                setSprinting(true);
            }
            else
            {
                setSneaking(false);
                setSprinting(false);
            }
        }
        else
        {
            setSneaking(false);
            setSprinting(false);
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return !isTamed() && (despawn || ticksExisted > 2400);
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(10D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.30000001192092896D);
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float f)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("CatType", getTameSkin());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setTameSkin(par1NBTTagCompound.getInteger("CatType"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        if (isTamed())
        {
            if (isInLove())
            {
                return "mob.cat.purr";
            }

            if (rand.nextInt(4) == 0)
            {
                return "mob.cat.purreow";
            }
            else
            {
                return "mob.cat.meow";
            }
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.cat.hitt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.cat.hitt";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.leather.itemID;
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3F);
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, float par2)
    {
        if (isEntityInvulnerable())
        {
            return false;
        }
        else
        {
            aiSit.setSitting(false);
            return super.attackEntityFrom(par1DamageSource, par2);
        }
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean flag, int i)
    {
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (isTamed())
        {
            if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(getOwnerName()) && !worldObj.isRemote && !isBreedingItem(itemstack))
            {
                aiSit.setSitting(!isSitting());
            }
        }
        else if (aiTempt.isRunning() && itemstack != null && itemstack.itemID == Item.fishRaw.itemID && par1EntityPlayer.getDistanceSqToEntity(this) < 9D)
        {
            if (!par1EntityPlayer.capabilities.isCreativeMode)
            {
                itemstack.stackSize--;
            }

            if (itemstack.stackSize <= 0)
            {
                par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
            }

            if (!worldObj.isRemote)
            {
                if (rand.nextInt(3) == 0)
                {
                    setTamed(true);
                    setTameSkin(1 + worldObj.rand.nextInt(3));
                    setOwner(par1EntityPlayer.getCommandSenderName());
                    playTameEffect(true);
                    aiSit.setSitting(true);
                    worldObj.setEntityState(this, (byte)7);
                }
                else
                {
                    playTameEffect(false);
                    worldObj.setEntityState(this, (byte)6);
                }
            }

            return true;
        }

        return super.interact(par1EntityPlayer);
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityOcelot spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        EntityOcelot entityocelot = new EntityOcelot(worldObj);

        if (isTamed())
        {
            entityocelot.setOwner(getOwnerName());
            entityocelot.setTamed(true);
            entityocelot.setTameSkin(getTameSkin());
        }

        return entityocelot;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
    {
        return par1ItemStack != null && par1ItemStack.itemID == Item.fishRaw.itemID;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    public boolean canMateWith(EntityAnimal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }

        if (!isTamed())
        {
            return false;
        }

        if (!(par1EntityAnimal instanceof EntityOcelot))
        {
            return false;
        }

        EntityOcelot entityocelot = (EntityOcelot)par1EntityAnimal;

        if (!entityocelot.isTamed())
        {
            return false;
        }
        else
        {
            return isInLove() && entityocelot.isInLove();
        }
    }

    public int getTameSkin()
    {
        return dataWatcher.getWatchableObjectByte(18);
    }

    public void setTameSkin(int par1)
    {
        dataWatcher.updateObject(18, Byte.valueOf((byte)par1));
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (worldObj.rand.nextInt(3) == 0)
        {
            return false;
        }

        if (worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox))
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(boundingBox.minY);
            int k = MathHelper.floor_double(posZ);

            if (j < 63)
            {
                return false;
            }

            int l = worldObj.getBlockId(i, j - 1, k);

            if (l == Block.grass.blockID || l == Block.leaves.blockID)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the username of the entity.
     */
    public String getEntityName()
    {
        if (hasCustomNameTag())
        {
            return getCustomNameTag();
        }

        if (isTamed())
        {
            return "entity.Cat.name";
        }
        else
        {
            return super.getEntityName();
        }
    }

    public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData)
    {
        par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);

        if (worldObj.rand.nextInt(7) == 0)
        {
            for (int i = 0; i < 2; i++)
            {
                EntityOcelot entityocelot = new EntityOcelot(worldObj);
                entityocelot.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                entityocelot.setGrowingAge(-24000);
                worldObj.spawnEntityInWorld(entityocelot);
            }
        }

        return par1EntityLivingData;
    }

    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return spawnBabyAnimal(par1EntityAgeable);
    }
}
