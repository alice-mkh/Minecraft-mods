package net.minecraft.src;

import java.util.Random;

public class EntitySpider extends EntityMob
{
    public static boolean survivaltest = false;
    public static boolean canclimb = true;
    public static boolean oldhealth = false;
    public static boolean jockeys = true;

    public EntitySpider(World par1World)
    {
        super(par1World);
        setSize(1.4F, 0.9F);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, new Byte((byte)0));
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!worldObj.isRemote)
        {
            setBesideClimbableBlock(isCollidedHorizontally);
        }
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(survivaltest ? 10D : (oldhealth ? 20D : 16D));
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.80000001192092896D);
    }

    /**
     * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
     * (Animals, Spiders at day, peaceful PigZombies).
     */
    protected Entity findPlayerToAttack()
    {
        float f = getBrightness(1.0F);

        if (f < 0.5F || survivaltest)
        {
            double d = 16D;
            return worldObj.getClosestVulnerablePlayerToEntity(this, d);
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.spider.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.spider.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        playSound("mob.spider.step", 0.15F, 1.0F);
    }

    /**
     * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
     */
    protected void attackEntity(Entity par1Entity, float par2)
    {
        float f = getBrightness(1.0F);

        if (f > 0.5F && rand.nextInt(100) == 0 && !survivaltest)
        {
            entityToAttack = null;
            return;
        }

        if (par2 > 2.0F && par2 < 6F && rand.nextInt(10) == 0)
        {
            if (onGround)
            {
                if (survivaltest){
                    motionX = 0D;
                    motionZ = 0D;
                    moveFlying(0.0F, 1.0F, 0.6F);
                    motionY += 0.5D;
                }else{
                    double d = par1Entity.posX - posX;
                    double d1 = par1Entity.posZ - posZ;
                    float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                    motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                    motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                    motionY = 0.40000000596046448D;
                }
            }
        }
        else
        {
            super.attackEntity(par1Entity, par2);
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.silk.itemID;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        super.dropFewItems(par1, par2);

        if (par1 && (rand.nextInt(3) == 0 || rand.nextInt(1 + par2) > 0))
        {
            dropItem(Item.spiderEye.itemID, 1);
        }
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        return canclimb ? isBesideClimbableBlock() : super.isOnLadder();
    }

    /**
     * Sets the Entity inside a web block.
     */
    public void setInWeb()
    {
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        if (par1PotionEffect.getPotionID() == Potion.poison.id)
        {
            return false;
        }
        else
        {
            return super.isPotionApplicable(par1PotionEffect);
        }
    }

    /**
     * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
     * setBesideClimableBlock.
     */
    public boolean isBesideClimbableBlock()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
     * false.
     */
    public void setBesideClimbableBlock(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            byte0 |= 1;
        }
        else
        {
            byte0 &= 0xfe;
        }

        dataWatcher.updateObject(16, Byte.valueOf(byte0));
    }

    public EntityLivingData onSpawnWithEgg(EntityLivingData par1EntityLivingData)
    {
        par1EntityLivingData = super.onSpawnWithEgg(par1EntityLivingData);

        if (worldObj.rand.nextInt(100) == 0 && jockeys)
        {
            EntitySkeleton entityskeleton = new EntitySkeleton(worldObj);
            entityskeleton.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
            entityskeleton.onSpawnWithEgg(null);
            worldObj.spawnEntityInWorld(entityskeleton);
            entityskeleton.mountEntity(this);
        }

        if (par1EntityLivingData == null)
        {
            par1EntityLivingData = new SpiderEffectsGroupData();

            if (worldObj.difficultySetting > 2 && worldObj.rand.nextFloat() < 0.1F * worldObj.getLocationTensionFactor(posX, posY, posZ))
            {
                ((SpiderEffectsGroupData)par1EntityLivingData).func_111104_a(worldObj.rand);
            }
        }

        if (par1EntityLivingData instanceof SpiderEffectsGroupData)
        {
            int i = ((SpiderEffectsGroupData)par1EntityLivingData).field_111105_a;

            if (i > 0 && Potion.potionTypes[i] != null)
            {
                addPotionEffect(new PotionEffect(i, 0x7fffffff));
            }
        }

        return par1EntityLivingData;
    }
}
