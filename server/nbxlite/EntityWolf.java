package net.minecraft.src;

import java.util.Random;

public class EntityWolf extends EntityTameable
{
    private boolean looksWithInterest;
    private float field_25038_b;
    private float field_25044_c;

    /** true is the wolf is wet else false */
    private boolean isShaking;
    private boolean field_25042_g;
    private float timeWolfIsShaking;
    private float prevTimeWolfIsShaking;

    public EntityWolf(World par1World)
    {
        super(par1World);
        looksWithInterest = false;
        texture = "/mob/wolf.png";
        setSize(0.6F, 0.8F);
        moveSpeed = 0.3F;
        func_48333_ak().func_48656_a(true);
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, field_48374_a);
        tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
        tasks.addTask(4, new EntityAIAttackOnCollide(this, moveSpeed, true));
        tasks.addTask(5, new EntityAIFollowOwner(this, moveSpeed, 10F, 2.0F));
        tasks.addTask(6, new EntityAIMate(this, moveSpeed));
        tasks.addTask(7, new EntityAIWander(this, moveSpeed));
        tasks.addTask(8, new EntityAIBeg(this, 8F));
        tasks.addTask(9, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(9, new EntityAILookIdle(this));
        field_48337_aM.addTask(1, new EntityAIOwnerHurtByTarget(this));
        field_48337_aM.addTask(2, new EntityAIOwnerHurtTarget(this));
        field_48337_aM.addTask(3, new EntityAIHurtByTarget(this, true));
        field_48337_aM.addTask(4, new EntityAITargetNonTamed(this, net.minecraft.src.EntitySheep.class, 16F, 200, false));
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    public void func_48327_b(EntityLiving par1EntityLiving)
    {
        super.func_48327_b(par1EntityLiving);

        if (par1EntityLiving instanceof EntityPlayer)
        {
            setAngry(true);
        }
    }

    protected void func_48326_g()
    {
        dataWatcher.updateObject(18, Integer.valueOf(getEntityHealth()));
    }

    public int getMaxHealth()
    {
        return !func_48373_u_() ? 8 : 20;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(18, new Integer(getEntityHealth()));
    }

    protected boolean canTriggerWalking()
    {
        return false;
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
        if (mod_noBiomesX.Generator==2 || mod_noBiomesX.UseNewSpawning){
            return isAngry();
        }
        return !func_48373_u_();
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
            if (func_48373_u_() && dataWatcher.getWatchableObjectInt(18) < 10)
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

        if (!worldObj.isRemote && isShaking && !field_25042_g && !hasPath() && onGround)
        {
            field_25042_g = true;
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
        field_25044_c = field_25038_b;

        if (looksWithInterest)
        {
            field_25038_b = field_25038_b + (1.0F - field_25038_b) * 0.4F;
        }
        else
        {
            field_25038_b = field_25038_b + (0.0F - field_25038_b) * 0.4F;
        }

        if (looksWithInterest)
        {
            numTicksToChaseTarget = 10;
        }

        if (isWet())
        {
            isShaking = true;
            field_25042_g = false;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
        }
        else if ((isShaking || field_25042_g) && field_25042_g)
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
                field_25042_g = false;
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

    public float getEyeHeight()
    {
        return height * 0.8F;
    }

    public int getVerticalFaceSpeed()
    {
        if (func_48371_v_())
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
        field_48374_a.func_48210_a(false);

        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            par2 = (par2 + 1) / 2;
        }

        return super.attackEntityFrom(par1DamageSource, par2);
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        byte byte0 = ((byte)(func_48373_u_() ? 4 : 2));
        return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (!func_48373_u_())
        {
            if (itemstack != null && itemstack.itemID == Item.bone.shiftedIndex && !isAngry())
            {
                itemstack.stackSize--;

                if (itemstack.stackSize <= 0)
                {
                    par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                }

                if (!worldObj.isRemote)
                {
                    if (rand.nextInt(3) == 0)
                    {
                        func_48366_b(true);
                        setPathToEntity(null);
                        func_48327_b(null);
                        field_48374_a.func_48210_a(true);
                        setEntityHealth(20);
                        func_48372_a(par1EntityPlayer.username);
                        func_48370_a(true);
                        worldObj.setEntityState(this, (byte)7);
                    }
                    else
                    {
                        func_48370_a(false);
                        worldObj.setEntityState(this, (byte)6);
                    }
                }

                return true;
            }
        }
        else
        {
            if (itemstack != null && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
            {
                ItemFood itemfood = (ItemFood)Item.itemsList[itemstack.itemID];

                if (itemfood.isWolfsFavoriteMeat() && dataWatcher.getWatchableObjectInt(18) < 20)
                {
                    itemstack.stackSize--;
                    heal(itemfood.getHealAmount());

                    if (itemstack.stackSize <= 0)
                    {
                        par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                    }

                    return true;
                }
            }

            if (par1EntityPlayer.username.equalsIgnoreCase(func_48367_A()) && !worldObj.isRemote && !isWheat(itemstack))
            {
                field_48374_a.func_48210_a(!func_48371_v_());
                isJumping = false;
                setPathToEntity(null);
            }
        }

        return super.interact(par1EntityPlayer);
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
     * gets if the wolf is angry
     */
    public boolean isAngry()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 2) != 0;
    }

    /**
     * sets if the wolf is angry or not
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
        entitywolf.func_48372_a(func_48367_A());
        entitywolf.func_48366_b(true);
        return entitywolf;
    }

    public void func_48378_e(boolean par1)
    {
        looksWithInterest = par1;
    }

    public boolean func_48362_b(EntityAnimal par1EntityAnimal)
    {
        if (par1EntityAnimal == this)
        {
            return false;
        }

        if (!func_48373_u_())
        {
            return false;
        }

        if (!(par1EntityAnimal instanceof EntityWolf))
        {
            return false;
        }

        EntityWolf entitywolf = (EntityWolf)par1EntityAnimal;

        if (!entitywolf.func_48373_u_())
        {
            return false;
        }

        if (entitywolf.func_48371_v_())
        {
            return false;
        }
        else
        {
            return func_48363_r_() && entitywolf.func_48363_r_();
        }
    }
}
