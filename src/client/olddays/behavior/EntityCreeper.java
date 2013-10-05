package net.minecraft.src;

import java.util.Random;

public class EntityCreeper extends EntityMob
{
    /**
     * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
     * weird)
     */
    private int lastActiveTime;

    /**
     * The amount of time since the creeper was close enough to the player to ignite
     */
    private int timeSinceIgnited;
    private int fuseTime;

    /** Explosion radius for this creeper. */
    private int explosionRadius;

    public static boolean fixai = false;
    public static boolean survivaltest = false;
    public static boolean dark = false;
    public static boolean oldrange = false;

    public EntityCreeper(World par1World)
    {
        super(par1World);
        fuseTime = 30;
        explosionRadius = 3;
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAICreeperSwell(this));
        tasks.addTask(3, new EntityAIAvoidEntity(this, net.minecraft.src.EntityOcelot.class, 6F, 1.0D, 1.2D));
        tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
        tasks.addTask(5, new EntityAIWander(this, 0.80000000000000004D));
        tasks.addTask(6, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityPlayer.class, 0, true));
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(survivaltest ? 10D : 20D);
        getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.25D);
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    @Override
    public int getBrightnessForRender(float f)
    {
        if (!dark){
            return super.getBrightnessForRender(f);
        }
        float f1 = (float)(10F - getHealth()) / 20F;
        float f2 = (MathHelper.cos((float)entityAge + f) * 0.5F + 0.5F);
        return (int)((f2 * f1 * 0.5F + 0.25F + f1 * 0.25F) * super.getBrightness(f) * 450F);
    }

    @Override
    protected void attackBlockedEntity(Entity entity, float f)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        if (!fixai){
            super.attackBlockedEntity(entity,f);
            return;
        }
        if (survivaltest){
            super.attackBlockedEntity(entity,f);
            return;
        }
        if (timeSinceIgnited > 0)
        {
            setCreeperState(-1);
            timeSinceIgnited--;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
        }
    }

    @Override
    protected void attackEntity(Entity entity, float f)
    {
        if (worldObj.isRemote)
        {
            return;
        }
        if (survivaltest){
            if (attackTime <= 0 && f < 2.0F && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
            {
                attackTime = 20;
                attackEntityAsMob(entity);
                this.attackEntityFrom(DamageSource.causeMobDamage(this), 2);
            }
            return;
        }
        if (!fixai){
            return;
        }
        int i = getCreeperState();
        if (i <= 0 && f < 3F || i > 0 && f < 7F)
        {
            if (timeSinceIgnited == 0)
            {
                playSound("random.fuse", 1.0F, 0.5F);
            }
            setCreeperState(1);
            timeSinceIgnited++;
            if (timeSinceIgnited >= fuseTime)
            {
                boolean flag = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");
                if (getPowered())
                {
                    worldObj.createExplosion(this, posX, posY, posZ, explosionRadius * 2, flag);
                }
                else
                {
                    worldObj.createExplosion(this, posX, posY, posZ, explosionRadius, flag);
                }
                setDead();
            }
            hasAttacked = true;
        }
        else
        {
            setCreeperState(-1);
            timeSinceIgnited--;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
        }
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return !survivaltest;
    }

    /**
     * The number of iterations PathFinder.getSafePoint will execute before giving up.
     */
    public int getMaxSafePointTries()
    {
        if (oldrange){
            return 4;
        }
        if (getAttackTarget() == null)
        {
            return 3;
        }
        else
        {
            return 3 + (int)(getHealth() - 1.0F);
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        super.fall(par1);
        timeSinceIgnited += par1 * 1.5F;

        if (timeSinceIgnited > fuseTime - 5)
        {
            timeSinceIgnited = fuseTime - 5;
        }
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte) - 1));
        dataWatcher.addObject(17, Byte.valueOf((byte)0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        if (dataWatcher.getWatchableObjectByte(17) == 1)
        {
            par1NBTTagCompound.setBoolean("powered", true);
        }

        par1NBTTagCompound.setShort("Fuse", (short)fuseTime);
        par1NBTTagCompound.setByte("ExplosionRadius", (byte)explosionRadius);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        dataWatcher.updateObject(17, Byte.valueOf((byte)(par1NBTTagCompound.getBoolean("powered") ? 1 : 0)));

        if (par1NBTTagCompound.hasKey("Fuse"))
        {
            fuseTime = par1NBTTagCompound.getShort("Fuse");
        }

        if (par1NBTTagCompound.hasKey("ExplosionRadius"))
        {
            explosionRadius = par1NBTTagCompound.getByte("ExplosionRadius");
        }
    }

    public void onUpdate_old()
    {
        lastActiveTime = timeSinceIgnited;
        if (worldObj.isRemote)
        {
            int i = getCreeperState();
            if (i > 0 && timeSinceIgnited == 0)
            {
                playSound("random.fuse", 1.0F, 0.5F);
            }
            timeSinceIgnited += i;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
            if (timeSinceIgnited >= fuseTime)
            {
                timeSinceIgnited = fuseTime;
            }
        }
        super.onUpdate();
        if (entityToAttack == null && timeSinceIgnited > 0)
        {
            setCreeperState(-1);
            timeSinceIgnited--;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (fixai){
            onUpdate_old();
            return;
        }
        if (isEntityAlive())
        {
            lastActiveTime = timeSinceIgnited;
            int i = getCreeperState();

            if (i > 0 && timeSinceIgnited == 0)
            {
                playSound("random.fuse", 1.0F, 0.5F);
            }

            timeSinceIgnited += i;

            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }

            if (timeSinceIgnited >= fuseTime)
            {
                timeSinceIgnited = fuseTime;

                if (!worldObj.isRemote)
                {
                    boolean flag = worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

                    if (getPowered())
                    {
                        worldObj.createExplosion(this, posX, posY, posZ, explosionRadius * 2, flag);
                    }
                    else
                    {
                        worldObj.createExplosion(this, posX, posY, posZ, explosionRadius, flag);
                    }

                    setDead();
                }
            }
        }

        super.onUpdate();
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.creeper.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.creeper.death";
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);
        if (!survivaltest){
            if (par1DamageSource.getEntity() instanceof EntitySkeleton)
            {
                int i = Item.record13.itemID + rand.nextInt((Item.recordWait.itemID - Item.record13.itemID) + 1);
                dropItem(i, 1);
            }
        }
    }

    @Override
    protected void onDeathUpdate(){
        if (survivaltest && deathTime >= 15){
            worldObj.createExplosion(this, posX, posY, posZ, 4F, worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            setDead();
        }else{
            super.onDeathUpdate();
        }
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        return survivaltest ? super.attackEntityAsMob(par1Entity) : true;
    }

    /**
     * Returns true if the creeper is powered by a lightning bolt.
     */
    public boolean getPowered()
    {
        return dataWatcher.getWatchableObjectByte(17) == 1;
    }

    /**
     * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
     */
    public float getCreeperFlashIntensity(float par1)
    {
        return ((float)lastActiveTime + (float)(timeSinceIgnited - lastActiveTime) * par1) / (float)(fuseTime - 2);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return survivaltest ? 0 : Item.gunpowder.itemID;
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getCreeperState()
    {
        return dataWatcher.getWatchableObjectByte(16);
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setCreeperState(int par1)
    {
        dataWatcher.updateObject(16, Byte.valueOf((byte)par1));
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt)
    {
        super.onStruckByLightning(par1EntityLightningBolt);
        dataWatcher.updateObject(17, Byte.valueOf((byte)1));
    }
}
