package net.minecraft.src;

import java.util.Random;

public class EntityWolf extends EntityTameable
{
    public static boolean despawn = false;

    private float field_70926_e;
    private float field_70924_f;

    /** true is the wolf is wet else false */
    private boolean isShaking;
    private boolean field_70928_h;

    /**
     * This time increases while wolf is shaking and emitting water particles.
     */
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public EntityWolf(World par1World)
    {
        super(par1World);
        texture = "/mob/wolf.png";
        setSize(0.6F, 0.8F);
        moveSpeed = 0.3F;
        getNavigator().setAvoidsWater(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, aiSit);
        tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        tasks.addTask(4, new EntityAIAttackOnCollide(this, moveSpeed, true));
        tasks.addTask(5, new EntityAIFollowOwner(this, moveSpeed, 10F, 2.0F));
        tasks.addTask(6, new EntityAIMate(this, moveSpeed));
        tasks.addTask(7, new EntityAIWander(this, moveSpeed));
        tasks.addTask(8, new EntityAIBeg(this, 8F));
        tasks.addTask(9, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(9, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
        targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
        targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
        targetTasks.addTask(4, new EntityAITargetNonTamed(this, net.minecraft.src.EntitySheep.class, 16F, 200, false));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLiving par1EntityLiving)
    {
        super.setAttackTarget(par1EntityLiving);

        if (par1EntityLiving instanceof EntityPlayer)
        {
            setAngry(true);
        }
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
        dataWatcher.updateObject(18, Integer.valueOf(getHealth()));
    }

    public int getMaxHealth()
    {
        return !isTamed() ? 8 : 20;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(18, new Integer(getHealth()));
        dataWatcher.addObject(19, new Byte((byte)0));
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking()
    {
        return false;
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        if (isTamed())
        {
            return "/mob/wolf_tame.png";
        }

        if (isAngry())
        {
            return "/mob/wolf_angry.png";
        }
        else
        {
            return super.getTexture();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Angry", isAngry());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setAngry(par1NBTTagCompound.getBoolean("Angry"));
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return despawn ? !isTamed() : isAngry();
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        if (isAngry())
        {
            return "mob.wolf.growl";
        }

        if (rand.nextInt(3) == 0)
        {
            if (isTamed() && dataWatcher.getWatchableObjectInt(18) < 10)
            {
                return "mob.wolf.whine";
            }
            else
            {
                return "mob.wolf.panting";
            }
        }
        else
        {
            return "mob.wolf.bark";
        }
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.wolf.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.wolf.death";
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
        return -1;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (!worldObj.isRemote && isShaking && !field_70928_h && !hasPath() && onGround)
        {
            field_70928_h = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
            worldObj.setEntityState(this, (byte)8);
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();
        field_70924_f = field_70926_e;

        if (func_70922_bv())
        {
            field_70926_e += (1.0F - field_70926_e) * 0.4F;
        }
        else
        {
            field_70926_e += (0.0F - field_70926_e) * 0.4F;
        }

        if (func_70922_bv())
        {
            numTicksToChaseTarget = 10;
        }

        if (isWet())
        {
            isShaking = true;
            field_70928_h = false;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else if ((isShaking || field_70928_h) && field_70928_h)
        {
            if (timeWolfIsShaking == 0.0F)
            {
                worldObj.playSoundAtEntity(this, "mob.wolf.shake", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            }

            prevTimeWolfIsShaking = timeWolfIsShaking;
            timeWolfIsShaking += 0.05F;

            if (prevTimeWolfIsShaking >= 2.0F)
            {
                isShaking = false;
                field_70928_h = false;
                prevTimeWolfIsShaking = 0.0F;
                timeWolfIsShaking = 0.0F;
            }

            if (timeWolfIsShaking > 0.4F)
            {
                float f = (float)boundingBox.minY;
                int i = (int)(MathHelper.sin((timeWolfIsShaking - 0.4F) * (float)Math.PI) * 7F);

                for (int j = 0; j < i; j++)
                {
                    float f1 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    float f2 = (rand.nextFloat() * 2.0F - 1.0F) * width * 0.5F;
                    worldObj.spawnParticle("splash", posX + (double)f1, f + 0.8F, posZ + (double)f2, motionX, motionY, motionZ);
                }
            }
        }
    }

    public boolean getWolfShaking()
    {
        return isShaking;
    }

    /**
     * Used when calculating the amount of shading to apply while the wolf is shaking.
     */
    public float getShadingWhileShaking(float par1)
    {
        return 0.75F + ((prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * par1) / 2.0F) * 0.25F;
    }

    public float getShakeAngle(float par1, float par2)
    {
        float f = (prevTimeWolfIsShaking + (timeWolfIsShaking - prevTimeWolfIsShaking) * par1 + par2) / 1.8F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }
        else if (f > 1.0F)
        {
            f = 1.0F;
        }

        return MathHelper.sin(f * (float)Math.PI) * MathHelper.sin(f * (float)Math.PI * 11F) * 0.15F * (float)Math.PI;
    }

    public float getInterestedAngle(float par1)
    {
        return (field_70924_f + (field_70926_e - field_70924_f) * par1) * 0.15F * (float)Math.PI;
    }

    public float getEyeHeight()
    {
        return height * 0.8F;
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        if (isSitting())
        {
            return 20;
        }
        else
        {
            return super.getVerticalFaceSpeed();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        Entity entity = par1DamageSource.getEntity();
        aiSit.setSitting(false);

        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            par2 = (par2 + 1) / 2;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        byte byte0 = ((byte)(isTamed() ? 4 : 2));
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (isTamed())
        {
            if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
            {
                ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];

                if (itemfood.isWolfsFavoriteMeat() && dataWatcher.getWatchableObjectInt(18) < 20)
                {
                    if (!par1EntityPlayer.capabilities.isCreativeMode)
                    {
                        itemstack.stackSize--;
                    }

                    heal(itemfood.getHealAmount());

                    if (itemstack.stackSize <= 0)
                    {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                    }

                    return true;
                }
            }

            if (par1EntityPlayer.username.equalsIgnoreCase(getOwnerName()) && !worldObj.isRemote && !isWheat(itemstack))
            {
                aiSit.setSitting(!isSitting());
                isJumping = false;
                setPathToEntity(null);
            }
        }
        else if (itemstack != null && itemstack.itemID == Item.bone.shiftedIndex && !isAngry())
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
                    setPathToEntity(null);
                    setAttackTarget(null);
                    aiSit.setSitting(true);
                    setEntityHealth(20);
                    setOwner(par1EntityPlayer.username);
                    playTameEffect(true);
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

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 8)
        {
            field_70928_h = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    public float getTailRotation()
    {
        if (isAngry())
        {
            return 1.53938F;
        }

        if (isTamed())
        {
            return (0.55F - (float)(20 - dataWatcher.getWatchableObjectInt(18)) * 0.02F) * (float)Math.PI;
        }
        else
        {
            return ((float)Math.PI / 5F);
        }
    }

    /**
     * Checks if the parameter is an wheat item.
     */
    public boolean isWheat(ItemStack par1ItemStack)
    {
        if (par1ItemStack == null)
        {
            return false;
        }

        if (!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood))
        {
            return false;
        }
        else
        {
            return ((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat();
        }
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 8;
    }

    /**
     * Determines whether this wolf is angry or not.
     */
    public boolean isAngry()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * Sets whether this wolf is angry or not.
     */
    public void setAngry(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 2)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -3)));
        }
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityAnimal spawnBabyAnimal(EntityAnimal par1EntityAnimal)
    {
        EntityWolf entitywolf = new EntityWolf(worldObj);
        entitywolf.setOwner(getOwnerName());
        entitywolf.setTamed(true);
        return entitywolf;
    }

    public void func_70918_i(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(19);

        if (par1)
        {
            dataWatcher.updateObject(19, Byte.valueOf((byte)1));
        }
        else
        {
            dataWatcher.updateObject(19, Byte.valueOf((byte)0));
        }
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

        if (!(par1EntityAnimal instanceof EntityWolf))
        {
            return false;
        }

        EntityWolf entitywolf = (EntityWolf)par1EntityAnimal;

        if (!entitywolf.isTamed())
        {
            return false;
        }

        if (entitywolf.isSitting())
        {
            return false;
        }
        else
        {
            return isInLove() && entitywolf.isInLove();
        }
    }

    public boolean func_70922_bv()
    {
        return dataWatcher.getWatchableObjectByte(19) == 1;
    }
}
