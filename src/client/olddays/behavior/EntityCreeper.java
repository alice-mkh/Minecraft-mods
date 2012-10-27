package net.minecraft.src;

import java.util.Random;

public class EntityCreeper extends EntityMob
{
    public static boolean fixai = false;
    public static boolean survivaltest = false;
    public static boolean dark = false;

    /**
     * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
     * weird)
     */
    private int lastActiveTime;

    /**
     * The amount of time since the creeper was close enough to the player to ignite
     */
    private int timeSinceIgnited;
    private int field_82225_f;
    private int field_82226_g;

    public EntityCreeper(World par1World)
    {
        super(par1World);
        field_82225_f = 30;
        field_82226_g = 3;
        texture = "/mob/creeper.png";
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAICreeperSwell(this));
        tasks.addTask(3, new EntityAIAvoidEntity(this, net.minecraft.src.EntityOcelot.class, 6F, 0.25F, 0.3F));
        tasks.addTask(4, new EntityAIAttackOnCollide(this, 0.25F, false));
        tasks.addTask(5, new EntityAIWander(this, 0.2F));
        tasks.addTask(6, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityPlayer.class, 16F, 0, true));
        targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
    }

    public int getBrightnessForRender(float f)
    {
        if (!dark){
            return super.getBrightnessForRender(f);
        }
        float f1 = (float)(getMaxHealth() - health) / (getMaxHealth() * 2F);
        float f2 = (MathHelper.cos((float)entityAge + f) * 0.5F + 0.5F);
        return (int)((f2 * f1 * 0.5F + 0.25F + f1 * 0.25F) * super.getBrightness(f) * 450F);
    }

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
                worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }
            setCreeperState(1);
            timeSinceIgnited++;
            if (timeSinceIgnited >= field_82225_f)
            {
                boolean flag = worldObj.func_82736_K().func_82766_b("mobGriefing");
                if (getPowered())
                {
                    worldObj.createExplosion(this, posX, posY, posZ, field_82226_g * 2, flag);
                }
                else
                {
                    worldObj.createExplosion(this, posX, posY, posZ, field_82226_g, flag);
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

    public int func_82143_as()
    {
        if (getAttackTarget() == null)
        {
            return 3;
        }
        else
        {
            return 3 + (health - 1);
        }
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        super.fall(par1);
        timeSinceIgnited += par1 * 1.5F;

        if (timeSinceIgnited > field_82225_f - 5)
        {
            timeSinceIgnited = field_82225_f - 5;
        }
    }

    public int getMaxHealth()
    {
        return survivaltest ? 10 : 20;
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

        par1NBTTagCompound.setShort("Fuse", (short)field_82225_f);
        par1NBTTagCompound.setByte("ExplosionRadius", (byte)field_82226_g);
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
            field_82225_f = par1NBTTagCompound.getShort("Fuse");
        }

        if (par1NBTTagCompound.hasKey("ExplosionRadius"))
        {
            field_82226_g = par1NBTTagCompound.getByte("ExplosionRadius");
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
                worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }
            timeSinceIgnited += i;
            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }
            if (timeSinceIgnited >= field_82225_f)
            {
                timeSinceIgnited = field_82225_f;
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
                worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 0.5F);
            }

            timeSinceIgnited += i;

            if (timeSinceIgnited < 0)
            {
                timeSinceIgnited = 0;
            }

            if (timeSinceIgnited >= field_82225_f)
            {
                timeSinceIgnited = field_82225_f;

                if (!worldObj.isRemote)
                {
                    boolean flag = worldObj.func_82736_K().func_82766_b("mobGriefing");

                    if (getPowered())
                    {
                        worldObj.createExplosion(this, posX, posY, posZ, field_82226_g * 2, flag);
                    }
                    else
                    {
                        worldObj.createExplosion(this, posX, posY, posZ, field_82226_g, flag);
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
                dropItem(Item.record13.shiftedIndex + rand.nextInt(10), 1);
            }
        }
    }

    protected void onDeathUpdate(){
        if (survivaltest && deathTime >= 15){
            worldObj.createExplosion(this, posX, posY, posZ, 4F, worldObj.func_82736_K().func_82766_b("mobGriefing"));
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
     * Connects the the creeper flashes to the creeper's color multiplier
     */
    public float setCreeperFlashTime(float par1)
    {
        return ((float)lastActiveTime + (float)(timeSinceIgnited - lastActiveTime) * par1) / (float)(field_82225_f - 2);
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return survivaltest ? 0 : Item.gunpowder.shiftedIndex;
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

    public int func_82193_c(Entity par1Entity)
    {
        return 2;
    }
}
