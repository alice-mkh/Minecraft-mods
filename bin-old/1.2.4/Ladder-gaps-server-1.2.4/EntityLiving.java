package net.minecraft.src;

import java.util.*;

public abstract class EntityLiving extends Entity
{
    public int heartsHalvesLife;
    public float field_9098_aw;
    public float field_9096_ay;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float prevRotationYaw2;
    public float prevRotationYaw3;
    protected float field_9124_aB;
    protected float field_9123_aC;
    protected float field_9122_aD;
    protected float field_9121_aE;
    protected boolean field_9120_aF;

    /** the path for the texture of this entityLiving */
    protected String texture;
    protected boolean field_9118_aH;
    protected float field_9117_aI;

    /**
     * a string holding the type of entity it is currently only implemented in entityPlayer(as 'humanoid')
     */
    protected String entityType;
    protected float field_9115_aK;

    /** The score value of the Mob, the amount of points the mob is worth. */
    protected int scoreValue;
    protected float field_9113_aM;

    /**
     * A factor used to determine how far this entity will move each tick if it is walking on land. Adjusted by speed,
     * and slipperiness of the current block.
     */
    public float landMovementFactor;

    /**
     * A factor used to determine how far this entity will move each tick if it is jumping or falling.
     */
    public float jumpMovementFactor;
    public float prevSwingProgress;
    public float swingProgress;
    protected int health;
    public int prevHealth;

    /**
     * in each step in the damage calculations, this is set to the 'carryover' that would result if someone was damaged
     * .25 hearts (for example), and added to the damage in the next step
     */
    protected int carryoverDamage;

    /** Number of ticks since this EntityLiving last produced its sound */
    private int livingSoundTime;

    /**
     * The amount of time remaining this entity should act 'hurt'. (Visual appearance of red tint)
     */
    public int hurtTime;

    /** What the hurt time was max set to last. */
    public int maxHurtTime;

    /** The yaw at which this entity was last attacked from. */
    public float attackedAtYaw;

    /**
     * The amount of time remaining this entity should act 'dead', i.e. have a corpse in the world.
     */
    public int deathTime;
    public int attackTime;
    public float prevCameraPitch;
    public float cameraPitch;

    /**
     * This gets set on entity death, but never used. Looks like a duplicate of isDead
     */
    protected boolean dead;

    /** The experience points the Entity gives. */
    protected int experienceValue;
    public int field_9144_ba;
    public float field_9143_bb;
    public float field_9142_bc;
    public float field_9141_bd;
    public float field_386_ba;

    /** The most recent player that has attacked this entity */
    protected EntityPlayer attackingPlayer;

    /**
     * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity
     * should drop items on death.
     */
    protected int recentlyHit;

    /** is only being set, has no uses as of MC 1.1 */
    private EntityLiving entityLivingToAttack;
    private int revengeTimer;
    private EntityLiving lastAttackingEntity;

    /**
     * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity
     * should drop items on death.
     */
    public int arrowHitTempCounter;
    public int arrowHitTimer;
    protected HashMap activePotionsMap;

    /** Whether the DataWatcher needs to be updated with the active potions */
    private boolean potionsNeedUpdate;
    private int field_39003_c;
    private EntityLookHelper lookHelper;
    private EntityMoveHelper moveHelper;
    private EntityJumpHelper jumpHelper;
    private EntityBodyHelper bodyHelper;
    private PathNavigate navigator;
    protected EntityAITasks tasks;
    protected EntityAITasks targetTasks;

    /** The active target the Task system uses for tracking */
    private EntityLiving attackTarget;
    private EntitySenses field_48343_m;
    private float field_48340_n;
    private ChunkCoordinates homePosition;

    /** If -1 there is no maximum distance */
    private float maximumHomeDistance;

    /**
     * The number of updates over which the new position and rotation are to be applied to the entity.
     */
    protected int newPosRotationIncrements;

    /** The new X position to be applied to the entity. */
    protected double newPosX;

    /** The new Y position to be applied to the entity. */
    protected double newPosY;

    /** The new Z position to be applied to the entity. */
    protected double newPosZ;

    /** The new yaw rotation to be applied to the entity. */
    protected double newRotationYaw;

    /** The new yaw rotation to be applied to the entity. */
    protected double newRotationPitch;
    float field_9134_bl;

    /** intrinsic armor level for entity */
    protected int naturalArmorRating;

    /** The age of this EntityLiving (used to determine when it dies) */
    protected int entityAge;
    protected float moveStrafing;
    protected float moveForward;
    protected float randomYawVelocity;

    /** used to check whether entity is jumping. */
    protected boolean isJumping;
    protected float defaultPitch;
    protected float moveSpeed;

    /** Number of ticks since last jump */
    private int jumpTicks;

    /** this entities current target */
    private Entity currentTarget;

    /** how long to keep a specific target entity */
    protected int numTicksToChaseTarget;

    public EntityLiving(World par1World)
    {
        super(par1World);
        heartsHalvesLife = 20;
        renderYawOffset = 0.0F;
        prevRenderYawOffset = 0.0F;
        prevRotationYaw2 = 0.0F;
        prevRotationYaw3 = 0.0F;
        field_9120_aF = true;
        texture = "/mob/char.png";
        field_9118_aH = true;
        field_9117_aI = 0.0F;
        entityType = null;
        field_9115_aK = 1.0F;
        scoreValue = 0;
        field_9113_aM = 0.0F;
        landMovementFactor = 0.1F;
        jumpMovementFactor = 0.02F;
        attackedAtYaw = 0.0F;
        deathTime = 0;
        attackTime = 0;
        dead = false;
        field_9144_ba = -1;
        field_9143_bb = (float)(Math.random() * 0.89999997615814209D + 0.10000000149011612D);
        attackingPlayer = null;
        recentlyHit = 0;
        entityLivingToAttack = null;
        revengeTimer = 0;
        lastAttackingEntity = null;
        arrowHitTempCounter = 0;
        arrowHitTimer = 0;
        activePotionsMap = new HashMap();
        potionsNeedUpdate = true;
        tasks = new EntityAITasks();
        targetTasks = new EntityAITasks();
        homePosition = new ChunkCoordinates(0, 0, 0);
        maximumHomeDistance = -1F;
        field_9134_bl = 0.0F;
        naturalArmorRating = 0;
        entityAge = 0;
        isJumping = false;
        defaultPitch = 0.0F;
        moveSpeed = 0.7F;
        jumpTicks = 0;
        numTicksToChaseTarget = 0;
        health = getMaxHealth();
        preventEntitySpawning = true;
        lookHelper = new EntityLookHelper(this);
        moveHelper = new EntityMoveHelper(this);
        jumpHelper = new EntityJumpHelper(this);
        bodyHelper = new EntityBodyHelper(this);
        navigator = new PathNavigate(this, par1World, 16F);
        field_48343_m = new EntitySenses(this);
        field_9096_ay = (float)(Math.random() + 1.0D) * 0.01F;
        setPosition(posX, posY, posZ);
        field_9098_aw = (float)Math.random() * 12398F;
        rotationYaw = (float)(Math.random() * Math.PI * 2D);
        prevRotationYaw2 = rotationYaw;
        stepHeight = 0.5F;
    }

    public EntityLookHelper getLookHelper()
    {
        return lookHelper;
    }

    public EntityMoveHelper getMoveHelper()
    {
        return moveHelper;
    }

    public EntityJumpHelper getJumpHelper()
    {
        return jumpHelper;
    }

    public PathNavigate getNavigator()
    {
        return navigator;
    }

    public EntitySenses func_48318_al()
    {
        return field_48343_m;
    }

    public Random getRNG()
    {
        return rand;
    }

    public EntityLiving getAITarget()
    {
        return entityLivingToAttack;
    }

    public EntityLiving getLastAttackingEntity()
    {
        return lastAttackingEntity;
    }

    public void setLastAttackingEntity(Entity par1Entity)
    {
        if (par1Entity instanceof EntityLiving)
        {
            lastAttackingEntity = (EntityLiving)par1Entity;
        }
    }

    public int getAge()
    {
        return entityAge;
    }

    public float func_48314_aq()
    {
        return prevRotationYaw2;
    }

    public float func_48332_ar()
    {
        return field_48340_n;
    }

    public void func_48320_d(float par1)
    {
        field_48340_n = par1;
        setMoveForward(par1);
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        setLastAttackingEntity(par1Entity);
        return false;
    }

    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLiving getAttackTarget()
    {
        return attackTarget;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLiving par1EntityLiving)
    {
        attackTarget = par1EntityLiving;
    }

    public boolean func_48336_a(Class par1Class)
    {
        return (net.minecraft.src.EntityCreeper.class) != par1Class && (net.minecraft.src.EntityGhast.class) != par1Class;
    }

    /**
     * This function applies the benefits of growing back wool and faster growing up to the acting entity. (This
     * function is used in the AIEatGrass)
     */
    public void eatGrassBonus()
    {
    }

    /**
     * Returns true if entity is within home distance from current position
     */
    public boolean isWithinHomeDistanceCurrentPosition()
    {
        return isWithinHomeDistance(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
    }

    public boolean isWithinHomeDistance(int par1, int par2, int par3)
    {
        if (maximumHomeDistance == -1F)
        {
            return true;
        }
        else
        {
            return homePosition.getDistanceSquared(par1, par2, par3) < maximumHomeDistance * maximumHomeDistance;
        }
    }

    public void setHomeArea(int par1, int par2, int par3, int par4)
    {
        homePosition.set(par1, par2, par3);
        maximumHomeDistance = par4;
    }

    public ChunkCoordinates getHomePosition()
    {
        return homePosition;
    }

    public float getMaximumHomeDistance()
    {
        return maximumHomeDistance;
    }

    public void detachHome()
    {
        maximumHomeDistance = -1F;
    }

    public boolean hasHome()
    {
        return maximumHomeDistance != -1F;
    }

    public void setRevengeTarget(EntityLiving par1EntityLiving)
    {
        entityLivingToAttack = par1EntityLiving;
        revengeTimer = entityLivingToAttack == null ? 0 : 60;
    }

    protected void entityInit()
    {
        dataWatcher.addObject(8, Integer.valueOf(field_39003_c));
    }

    /**
     * returns true if the entity provided in the argument can be seen. (Raytrace)
     */
    public boolean canEntityBeSeen(Entity par1Entity)
    {
        return worldObj.rayTraceBlocks(Vec3D.createVector(posX, posY + (double)getEyeHeight(), posZ), Vec3D.createVector(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return !isDead;
    }

    public float getEyeHeight()
    {
        return height * 0.85F;
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval()
    {
        return 80;
    }

    /**
     * Plays living's sound at its position
     */
    public void playLivingSound()
    {
        String s = getLivingSound();

        if (s != null)
        {
            worldObj.playSoundAtEntity(this, s, getSoundVolume(), getSoundPitch());
        }
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        prevSwingProgress = swingProgress;
        super.onEntityUpdate();
        Profiler.startSection("mobBaseTick");

        if (isEntityAlive() && rand.nextInt(1000) < livingSoundTime++)
        {
            livingSoundTime = -getTalkInterval();
            playLivingSound();
        }

        if (isEntityAlive() && isEntityInsideOpaqueBlock())
        {
            if (!attackEntityFrom(DamageSource.inWall, 1));
        }

        if (isImmuneToFire() || worldObj.isRemote)
        {
            extinguish();
        }

        if (isEntityAlive() && isInsideOfMaterial(Material.water) && !canBreatheUnderwater() && !activePotionsMap.containsKey(Integer.valueOf(Potion.waterBreathing.id)))
        {
            setAir(decreaseAirSupply(getAir()));

            if (getAir() == -20)
            {
                setAir(0);

                for (int i = 0; i < 8; i++)
                {
                    float f = rand.nextFloat() - rand.nextFloat();
                    float f1 = rand.nextFloat() - rand.nextFloat();
                    float f2 = rand.nextFloat() - rand.nextFloat();
                    worldObj.spawnParticle("bubble", posX + (double)f, posY + (double)f1, posZ + (double)f2, motionX, motionY, motionZ);
                }

                attackEntityFrom(DamageSource.drown, 2);
            }

            extinguish();
        }
        else
        {
            setAir(300);
        }

        prevCameraPitch = cameraPitch;

        if (attackTime > 0)
        {
            attackTime--;
        }

        if (hurtTime > 0)
        {
            hurtTime--;
        }

        if (heartsLife > 0)
        {
            heartsLife--;
        }

        if (health <= 0)
        {
            onDeathUpdate();
        }

        if (recentlyHit > 0)
        {
            recentlyHit--;
        }
        else
        {
            attackingPlayer = null;
        }

        if (lastAttackingEntity != null && !lastAttackingEntity.isEntityAlive())
        {
            lastAttackingEntity = null;
        }

        if (entityLivingToAttack != null)
        {
            if (!entityLivingToAttack.isEntityAlive())
            {
                setRevengeTarget(null);
            }
            else if (revengeTimer > 0)
            {
                revengeTimer--;
            }
            else
            {
                setRevengeTarget(null);
            }
        }

        updatePotionEffects();
        field_9121_aE = field_9122_aD;
        prevRenderYawOffset = renderYawOffset;
        prevRotationYaw3 = prevRotationYaw2;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        Profiler.endSection();
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        deathTime++;

        if (deathTime == 20)
        {
            if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && !isChild())
            {
                for (int i = getExperiencePoints(attackingPlayer); i > 0;)
                {
                    int k = EntityXPOrb.getXPSplit(i);
                    i -= k;
                    worldObj.spawnEntityInWorld(new EntityXPOrb(worldObj, posX, posY, posZ, k));
                }
            }

            onEntityDeath();
            setDead();

            for (int j = 0; j < 20; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
        }
    }

    /**
     * Decrements the entity's air supply when underwater
     */
    protected int decreaseAirSupply(int par1)
    {
        return par1 - 1;
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        return experienceValue;
    }

    /**
     * Only use is to identify if class is an instance of player for experience dropping
     */
    protected boolean isPlayer()
    {
        return false;
    }

    /**
     * Spawns an explosion particle around the Entity's location
     */
    public void spawnExplosionParticle()
    {
        for (int i = 0; i < 20; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            double d3 = 10D;
            worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - d * d3, (posY + (double)(rand.nextFloat() * height)) - d1 * d3, (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width - d2 * d3, d, d1, d2);
        }
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();
        field_9124_aB = field_9123_aC;
        field_9123_aC = 0.0F;
        fallDistance = 0.0F;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (arrowHitTempCounter > 0)
        {
            if (arrowHitTimer <= 0)
            {
                arrowHitTimer = 60;
            }

            arrowHitTimer--;

            if (arrowHitTimer <= 0)
            {
                arrowHitTempCounter--;
            }
        }

        onLivingUpdate();
        double d = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        float f1 = renderYawOffset;
        float f2 = 0.0F;
        field_9124_aB = field_9123_aC;
        float f3 = 0.0F;

        if (f > 0.05F)
        {
            f3 = 1.0F;
            f2 = f * 3F;
            f1 = ((float)Math.atan2(d1, d) * 180F) / (float)Math.PI - 90F;
        }

        if (swingProgress > 0.0F)
        {
            f1 = rotationYaw;
        }

        if (!onGround)
        {
            f3 = 0.0F;
        }

        field_9123_aC = field_9123_aC + (f3 - field_9123_aC) * 0.3F;

        if (isAIEnabled())
        {
            bodyHelper.func_48431_a();
        }
        else
        {
            float f4;

            for (f4 = f1 - renderYawOffset; f4 < -180F; f4 += 360F) { }

            for (; f4 >= 180F; f4 -= 360F) { }

            renderYawOffset += f4 * 0.3F;
            float f5;

            for (f5 = rotationYaw - renderYawOffset; f5 < -180F; f5 += 360F) { }

            for (; f5 >= 180F; f5 -= 360F) { }

            boolean flag = f5 < -90F || f5 >= 90F;

            if (f5 < -75F)
            {
                f5 = -75F;
            }

            if (f5 >= 75F)
            {
                f5 = 75F;
            }

            renderYawOffset = rotationYaw - f5;

            if (f5 * f5 > 2500F)
            {
                renderYawOffset += f5 * 0.2F;
            }

            if (flag)
            {
                f2 *= -1F;
            }
        }

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        for (; renderYawOffset - prevRenderYawOffset < -180F; prevRenderYawOffset -= 360F) { }

        for (; renderYawOffset - prevRenderYawOffset >= 180F; prevRenderYawOffset += 360F) { }

        for (; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; prevRotationYaw2 - prevRotationYaw3 < -180F; prevRotationYaw3 -= 360F) { }

        for (; prevRotationYaw2 - prevRotationYaw3 >= 180F; prevRotationYaw3 += 360F) { }

        field_9122_aD += f2;
    }

    /**
     * Sets the width and height of the entity. Args: width, height
     */
    protected void setSize(float par1, float par2)
    {
        super.setSize(par1, par2);
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(int par1)
    {
        if (health <= 0)
        {
            return;
        }

        health += par1;

        if (health > getMaxHealth())
        {
            health = getMaxHealth();
        }

        heartsLife = heartsHalvesLife / 2;
    }

    public abstract int getMaxHealth();

    public int getHealth()
    {
        return health;
    }

    public void setEntityHealth(int par1)
    {
        health = par1;

        if (par1 > getMaxHealth())
        {
            par1 = getMaxHealth();
        }
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2)
    {
        if (worldObj.isRemote)
        {
            return false;
        }

        entityAge = 0;

        if (health <= 0)
        {
            return false;
        }

        if (par1DamageSource.fireDamage() && isPotionActive(Potion.fireResistance))
        {
            return false;
        }

        field_9141_bd = 1.5F;
        boolean flag = true;

        if ((float)heartsLife > (float)heartsHalvesLife / 2.0F)
        {
            if (par2 <= naturalArmorRating)
            {
                return false;
            }

            damageEntity(par1DamageSource, par2 - naturalArmorRating);
            naturalArmorRating = par2;
            flag = false;
        }
        else
        {
            naturalArmorRating = par2;
            prevHealth = health;
            heartsLife = heartsHalvesLife;
            damageEntity(par1DamageSource, par2);
            hurtTime = maxHurtTime = 10;
        }

        attackedAtYaw = 0.0F;
        Entity entity = par1DamageSource.getEntity();

        if (entity != null)
        {
            if (entity instanceof EntityLiving)
            {
                setRevengeTarget((EntityLiving)entity);
            }

            if (entity instanceof EntityPlayer)
            {
                recentlyHit = 60;
                attackingPlayer = (EntityPlayer)entity;
            }
            else if (entity instanceof EntityWolf)
            {
                EntityWolf entitywolf = (EntityWolf)entity;

                if (entitywolf.isTamed())
                {
                    recentlyHit = 60;
                    attackingPlayer = null;
                }
            }
        }

        if (flag)
        {
            worldObj.setEntityState(this, (byte)2);
            setBeenAttacked();

            if (entity != null)
            {
                double d = entity.posX - posX;
                double d1;

                for (d1 = entity.posZ - posZ; d * d + d1 * d1 < 0.0001D; d1 = (Math.random() - Math.random()) * 0.01D)
                {
                    d = (Math.random() - Math.random()) * 0.01D;
                }

                attackedAtYaw = (float)((Math.atan2(d1, d) * 180D) / Math.PI) - rotationYaw;
                knockBack(entity, par2, d, d1);
            }
            else
            {
                attackedAtYaw = (int)(Math.random() * 2D) * 180;
            }
        }

        if (health <= 0)
        {
            if (flag)
            {
                worldObj.playSoundAtEntity(this, getDeathSound(), getSoundVolume(), getSoundPitch());
            }

            onDeath(par1DamageSource);
        }
        else if (flag)
        {
            worldObj.playSoundAtEntity(this, getHurtSound(), getSoundVolume(), getSoundPitch());
        }

        return true;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    private float getSoundPitch()
    {
        if (isChild())
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.5F;
        }
        else
        {
            return (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F;
        }
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        return 0;
    }

    protected void damageArmor(int i)
    {
    }

    /**
     * Reduces damage, depending on armor
     */
    protected int applyArmorCalculations(DamageSource par1DamageSource, int par2)
    {
        if (!par1DamageSource.isUnblockable())
        {
            int i = 25 - getTotalArmorValue();
            int j = par2 * i + carryoverDamage;
            damageArmor(par2);
            par2 = j / 25;
            carryoverDamage = j % 25;
        }

        return par2;
    }

    /**
     * Reduces damage, depending on potions
     */
    protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2)
    {
        if (isPotionActive(Potion.resistance))
        {
            int i = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            int j = 25 - i;
            int k = par2 * j + carryoverDamage;
            par2 = k / 25;
            carryoverDamage = k % 25;
        }

        return par2;
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource par1DamageSource, int par2)
    {
        par2 = applyArmorCalculations(par1DamageSource, par2);
        par2 = applyPotionDamageCalculations(par1DamageSource, par2);
        health -= par2;
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "damage.hurtflesh";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "damage.hurtflesh";
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity par1Entity, int par2, double par3, double par5)
    {
        isAirBorne = true;
        float f = MathHelper.sqrt_double(par3 * par3 + par5 * par5);
        float f1 = 0.4F;
        motionX /= 2D;
        motionY /= 2D;
        motionZ /= 2D;
        motionX -= (par3 / (double)f) * (double)f1;
        motionY += f1;
        motionZ -= (par5 / (double)f) * (double)f1;

        if (motionY > 0.40000000596046448D)
        {
            motionY = 0.40000000596046448D;
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource)
    {
        Entity entity = par1DamageSource.getEntity();

        if (scoreValue >= 0 && entity != null)
        {
            entity.addToPlayerScore(this, scoreValue);
        }

        if (entity != null)
        {
            entity.onKillEntity(this);
        }

        dead = true;

        if (!worldObj.isRemote)
        {
            int i = 0;

            if (entity instanceof EntityPlayer)
            {
                i = EnchantmentHelper.getLootingModifier(((EntityPlayer)entity).inventory);
            }

            if (!isChild())
            {
                dropFewItems(recentlyHit > 0, i);

                if (recentlyHit > 0)
                {
                    int j = rand.nextInt(200) - i;

                    if (j < 5)
                    {
                        dropRareDrop(j > 0 ? 0 : 1);
                    }
                }
            }
        }

        worldObj.setEntityState(this, (byte)3);
    }

    protected void dropRareDrop(int i)
    {
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = getDropItemId();

        if (i > 0)
        {
            int j = rand.nextInt(3);

            if (par2 > 0)
            {
                j += rand.nextInt(par2 + 1);
            }

            for (int k = 0; k < j; k++)
            {
                dropItem(i, 1);
            }
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return 0;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        super.fall(par1);
        int i = (int)Math.ceil(par1 - 3F);

        if (i > 0)
        {
            if (i > 4)
            {
                worldObj.playSoundAtEntity(this, "damage.fallbig", 1.0F, 1.0F);
            }
            else
            {
                worldObj.playSoundAtEntity(this, "damage.fallsmall", 1.0F, 1.0F);
            }

            attackEntityFrom(DamageSource.fall, i);
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));

            if (j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                worldObj.playSoundAtEntity(this, stepsound.getStepSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
        }
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    public void moveEntityWithHeading(float par1, float par2)
    {
        if (isInWater())
        {
            double d = posY;
            moveFlying(par1, par2, isAIEnabled() ? 0.04F : 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.80000001192092896D;
            motionY *= 0.80000001192092896D;
            motionZ *= 0.80000001192092896D;
            motionY -= 0.02D;

            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, ((motionY + 0.60000002384185791D) - posY) + d, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
        }
        else if (handleLavaMovement())
        {
            double d1 = posY;
            moveFlying(par1, par2, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
            motionY -= 0.02D;

            if (isCollidedHorizontally && isOffsetPositionInLiquid(motionX, ((motionY + 0.60000002384185791D) - posY) + d1, motionZ))
            {
                motionY = 0.30000001192092896D;
            }
        }
        else
        {
            float f = 0.91F;

            if (onGround)
            {
                f = 0.5460001F;
                int i = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

                if (i > 0)
                {
                    f = Block.blocksList[i].slipperiness * 0.91F;
                }
            }

            float f1 = 0.1627714F / (f * f * f);
            float f2;

            if (onGround)
            {
                if (isAIEnabled())
                {
                    f2 = func_48332_ar();
                }
                else
                {
                    f2 = landMovementFactor;
                }

                f2 *= f1;
            }
            else
            {
                f2 = jumpMovementFactor;
            }

            moveFlying(par1, par2, f2);
            f = 0.91F;

            if (onGround)
            {
                f = 0.5460001F;
                int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(boundingBox.minY) - 1, MathHelper.floor_double(posZ));

                if (j > 0)
                {
                    f = Block.blocksList[j].slipperiness * 0.91F;
                }
            }

            if (isOnLadder())
            {
                float f3 = 0.15F;

                if (motionX < (double)(-f3))
                {
                    motionX = -f3;
                }

                if (motionX > (double)f3)
                {
                    motionX = f3;
                }

                if (motionZ < (double)(-f3))
                {
                    motionZ = -f3;
                }

                if (motionZ > (double)f3)
                {
                    motionZ = f3;
                }

                fallDistance = 0.0F;

                if (motionY < -0.14999999999999999D)
                {
                    motionY = -0.14999999999999999D;
                }

                boolean flag = isSneaking() && (this instanceof EntityPlayer);

                if (flag && motionY < 0.0D)
                {
                    motionY = 0.0D;
                }
            }

            moveEntity(motionX, motionY, motionZ);

            if (isCollidedHorizontally && isOnLadder())
            {
                motionY = 0.20000000000000001D;
            }

            motionY -= 0.080000000000000002D;
            motionY *= 0.98000001907348633D;
            motionX *= f;
            motionZ *= f;
        }

        field_9142_bc = field_9141_bd;
        double d2 = posX - prevPosX;
        double d3 = posZ - prevPosZ;
        float f4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        field_9141_bd += (f4 - field_9141_bd) * 0.4F;
        field_386_ba += field_9141_bd;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    public boolean isOnLadder()
    {
        int i = MathHelper.floor_double(posX);
        int j = MathHelper.floor_double(boundingBox.minY);
        int k = MathHelper.floor_double(posZ);
        int l = worldObj.getBlockId(i, j, k);
        int l1 = worldObj.getBlockId(i, j + 1, k);
        return l == Block.ladder.blockID || l == Block.vine.blockID || l1 == Block.ladder.blockID || l1 == Block.vine.blockID;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setShort("Health", (short)health);
        par1NBTTagCompound.setShort("HurtTime", (short)hurtTime);
        par1NBTTagCompound.setShort("DeathTime", (short)deathTime);
        par1NBTTagCompound.setShort("AttackTime", (short)attackTime);

        if (!activePotionsMap.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();
            NBTTagCompound nbttagcompound;

            for (Iterator iterator = activePotionsMap.values().iterator(); iterator.hasNext(); nbttaglist.appendTag(nbttagcompound))
            {
                PotionEffect potioneffect = (PotionEffect)iterator.next();
                nbttagcompound = new NBTTagCompound();
                nbttagcompound.setByte("Id", (byte)potioneffect.getPotionID());
                nbttagcompound.setByte("Amplifier", (byte)potioneffect.getAmplifier());
                nbttagcompound.setInteger("Duration", potioneffect.getDuration());
            }

            par1NBTTagCompound.setTag("ActiveEffects", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        if (health < -32768)
        {
            health = -32768;
        }

        health = par1NBTTagCompound.getShort("Health");

        if (!par1NBTTagCompound.hasKey("Health"))
        {
            health = getMaxHealth();
        }

        hurtTime = par1NBTTagCompound.getShort("HurtTime");
        deathTime = par1NBTTagCompound.getShort("DeathTime");
        attackTime = par1NBTTagCompound.getShort("AttackTime");

        if (par1NBTTagCompound.hasKey("ActiveEffects"))
        {
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("ActiveEffects");

            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
                byte byte0 = nbttagcompound.getByte("Id");
                byte byte1 = nbttagcompound.getByte("Amplifier");
                int j = nbttagcompound.getInteger("Duration");
                activePotionsMap.put(Integer.valueOf(byte0), new PotionEffect(byte0, j, byte1));
            }
        }
    }

    /**
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive()
    {
        return !isDead && health > 0;
    }

    public boolean canBreatheUnderwater()
    {
        return false;
    }

    public void setMoveForward(float par1)
    {
        moveForward = par1;
    }

    public void setJumping(boolean par1)
    {
        isJumping = par1;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (jumpTicks > 0)
        {
            jumpTicks--;
        }

        if (newPosRotationIncrements > 0)
        {
            double d = posX + (newPosX - posX) / (double)newPosRotationIncrements;
            double d1 = posY + (newPosY - posY) / (double)newPosRotationIncrements;
            double d2 = posZ + (newPosZ - posZ) / (double)newPosRotationIncrements;
            double d3;

            for (d3 = newRotationYaw - (double)rotationYaw; d3 < -180D; d3 += 360D) { }

            for (; d3 >= 180D; d3 -= 360D) { }

            rotationYaw += d3 / (double)newPosRotationIncrements;
            rotationPitch += (newRotationPitch - (double)rotationPitch) / (double)newPosRotationIncrements;
            newPosRotationIncrements--;
            setPosition(d, d1, d2);
            setRotation(rotationYaw, rotationPitch);
            List list1 = worldObj.getCollidingBoundingBoxes(this, boundingBox.contract(0.03125D, 0.0D, 0.03125D));

            if (list1.size() > 0)
            {
                double d4 = 0.0D;

                for (int j = 0; j < list1.size(); j++)
                {
                    AxisAlignedBB axisalignedbb = (AxisAlignedBB)list1.get(j);

                    if (axisalignedbb.maxY > d4)
                    {
                        d4 = axisalignedbb.maxY;
                    }
                }

                d1 += d4 - boundingBox.minY;
                setPosition(d, d1, d2);
            }
        }

        Profiler.startSection("ai");

        if (isMovementBlocked())
        {
            isJumping = false;
            moveStrafing = 0.0F;
            moveForward = 0.0F;
            randomYawVelocity = 0.0F;
        }
        else if (isClientWorld())
        {
            if (isAIEnabled())
            {
                Profiler.startSection("newAi");
                updateAITasks();
                Profiler.endSection();
            }
            else
            {
                Profiler.startSection("oldAi");
                updateEntityActionState();
                Profiler.endSection();
                prevRotationYaw2 = rotationYaw;
            }
        }

        Profiler.endSection();
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();

        if (isJumping)
        {
            if (flag)
            {
                motionY += 0.039999999105930328D;
            }
            else if (flag1)
            {
                motionY += 0.039999999105930328D;
            }
            else if (onGround && jumpTicks == 0)
            {
                jump();
                jumpTicks = 10;
            }
        }
        else
        {
            jumpTicks = 0;
        }

        moveStrafing *= 0.98F;
        moveForward *= 0.98F;
        randomYawVelocity *= 0.9F;
        float f = landMovementFactor;
        landMovementFactor *= getSpeedModifier();
        moveEntityWithHeading(moveStrafing, moveForward);
        landMovementFactor = f;
        Profiler.startSection("push");
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && list.size() > 0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (entity.canBePushed())
                {
                    entity.applyEntityCollision(this);
                }
            }
        }

        Profiler.endSection();
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return false;
    }

    /**
     * Returns whether the entity is in a local (client) world
     */
    protected boolean isClientWorld()
    {
        return !worldObj.isRemote;
    }

    /**
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
        return health <= 0;
    }

    public boolean isBlocking()
    {
        return false;
    }

    /**
     * causes this entity to jump (or at least move upwards)
     */
    protected void jump()
    {
        motionY = 0.41999998688697815D;

        if (isPotionActive(Potion.jump))
        {
            motionY += (float)(getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
        }

        if (isSprinting())
        {
            float f = rotationYaw * 0.01745329F;
            motionX -= MathHelper.sin(f) * 0.2F;
            motionZ += MathHelper.cos(f) * 0.2F;
        }

        isAirBorne = true;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return true;
    }

    /**
     * Makes the entity despawn if requirements are reached
     */
    protected void despawnEntity()
    {
        EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, -1D);

        if (entityplayer != null)
        {
            double d = ((Entity)(entityplayer)).posX - posX;
            double d1 = ((Entity)(entityplayer)).posY - posY;
            double d2 = ((Entity)(entityplayer)).posZ - posZ;
            double d3 = d * d + d1 * d1 + d2 * d2;

            if (canDespawn() && d3 > 16384D)
            {
                setDead();
            }

            if (entityAge > 600 && rand.nextInt(800) == 0 && d3 > 1024D && canDespawn())
            {
                setDead();
            }
            else if (d3 < 1024D)
            {
                entityAge = 0;
            }
        }
    }

    protected void updateAITasks()
    {
        entityAge++;
        Profiler.startSection("checkDespawn");
        despawnEntity();
        Profiler.endSection();
        Profiler.startSection("sensing");
        field_48343_m.clearSensingCache();
        Profiler.endSection();
        Profiler.startSection("targetSelector");
        targetTasks.onUpdateTasks();
        Profiler.endSection();
        Profiler.startSection("goalSelector");
        tasks.onUpdateTasks();
        Profiler.endSection();
        Profiler.startSection("navigation");
        navigator.onUpdateNavigation();
        Profiler.endSection();
        Profiler.startSection("mob tick");
        updateAITick();
        Profiler.endSection();
        Profiler.startSection("controls");
        moveHelper.onUpdateMoveHelper();
        lookHelper.onUpdateLook();
        jumpHelper.doJump();
        Profiler.endSection();
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
    }

    protected void updateEntityActionState()
    {
        entityAge++;
        despawnEntity();
        moveStrafing = 0.0F;
        moveForward = 0.0F;
        float f = 8F;

        if (rand.nextFloat() < 0.02F)
        {
            EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, f);

            if (entityplayer != null)
            {
                currentTarget = entityplayer;
                numTicksToChaseTarget = 10 + rand.nextInt(20);
            }
            else
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }
        }

        if (currentTarget != null)
        {
            faceEntity(currentTarget, 10F, getVerticalFaceSpeed());

            if (numTicksToChaseTarget-- <= 0 || currentTarget.isDead || currentTarget.getDistanceSqToEntity(this) > (double)(f * f))
            {
                currentTarget = null;
            }
        }
        else
        {
            if (rand.nextFloat() < 0.05F)
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 20F;
            }

            rotationYaw += randomYawVelocity;
            rotationPitch = defaultPitch;
        }

        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();

        if (flag || flag1)
        {
            isJumping = rand.nextFloat() < 0.8F;
        }
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 40;
    }

    /**
     * changes pitch and yaw so that the entity calling the function is facing the entity provided as an argumen
     */
    public void faceEntity(Entity par1Entity, float par2, float par3)
    {
        double d = par1Entity.posX - posX;
        double d2 = par1Entity.posZ - posZ;
        double d1;

        if (par1Entity instanceof EntityLiving)
        {
            EntityLiving entityliving = (EntityLiving)par1Entity;
            d1 = (posY + (double)getEyeHeight()) - (entityliving.posY + (double)entityliving.getEyeHeight());
        }
        else
        {
            d1 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2D - (posY + (double)getEyeHeight());
        }

        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)((Math.atan2(d2, d) * 180D) / Math.PI) - 90F;
        float f1 = (float)(-((Math.atan2(d1, d3) * 180D) / Math.PI));
        rotationPitch = -updateRotation(rotationPitch, f1, par3);
        rotationYaw = updateRotation(rotationYaw, f, par2);
    }

    /**
     * Arguments: current rotation, intended rotation, max increment.
     */
    private float updateRotation(float par1, float par2, float par3)
    {
        float f;

        for (f = par2 - par1; f < -180F; f += 360F) { }

        for (; f >= 180F; f -= 360F) { }

        if (f > par3)
        {
            f = par3;
        }

        if (f < -par3)
        {
            f = -par3;
        }

        return par1 + f;
    }

    /**
     * Called when the entity vanishes after dies by damage (or other method that put health below or at zero).
     */
    public void onEntityDeath()
    {
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        return worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.isAnyLiquid(boundingBox);
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
        attackEntityFrom(DamageSource.outOfWorld, 4);
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3D getLookVec()
    {
        return getLook(1.0F);
    }

    /**
     * interpolated look vector
     */
    public Vec3D getLook(float par1)
    {
        if (par1 == 1.0F)
        {
            float f = MathHelper.cos(-rotationYaw * 0.01745329F - (float)Math.PI);
            float f2 = MathHelper.sin(-rotationYaw * 0.01745329F - (float)Math.PI);
            float f4 = -MathHelper.cos(-rotationPitch * 0.01745329F);
            float f6 = MathHelper.sin(-rotationPitch * 0.01745329F);
            return Vec3D.createVector(f2 * f4, f6, f * f4);
        }
        else
        {
            float f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * par1;
            float f3 = prevRotationYaw + (rotationYaw - prevRotationYaw) * par1;
            float f5 = MathHelper.cos(-f3 * 0.01745329F - (float)Math.PI);
            float f7 = MathHelper.sin(-f3 * 0.01745329F - (float)Math.PI);
            float f8 = -MathHelper.cos(-f1 * 0.01745329F);
            float f9 = MathHelper.sin(-f1 * 0.01745329F);
            return Vec3D.createVector(f7 * f8, f9, f5 * f8);
        }
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 4;
    }

    /**
     * Returns whether player is sleeping or not
     */
    public boolean isPlayerSleeping()
    {
        return false;
    }

    protected void updatePotionEffects()
    {
        Iterator iterator = activePotionsMap.keySet().iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Integer integer = (Integer)iterator.next();
            PotionEffect potioneffect = (PotionEffect)activePotionsMap.get(integer);

            if (!potioneffect.onUpdate(this) && !worldObj.isRemote)
            {
                iterator.remove();
                onFinishedPotionEffect(potioneffect);
            }
        }
        while (true);

        if (potionsNeedUpdate)
        {
            if (!worldObj.isRemote)
            {
                if (!activePotionsMap.isEmpty())
                {
                    int i = PotionHelper.func_40553_a(activePotionsMap.values());
                    dataWatcher.updateObject(8, Integer.valueOf(i));
                }
                else
                {
                    dataWatcher.updateObject(8, Integer.valueOf(0));
                }
            }

            potionsNeedUpdate = false;
        }

        if (rand.nextBoolean())
        {
            int j = dataWatcher.getWatchableObjectInt(8);

            if (j > 0)
            {
                double d = (double)(j >> 16 & 0xff) / 255D;
                double d1 = (double)(j >> 8 & 0xff) / 255D;
                double d2 = (double)(j >> 0 & 0xff) / 255D;
                worldObj.spawnParticle("mobSpell", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - (double)yOffset, posZ + (rand.nextDouble() - 0.5D) * (double)width, d, d1, d2);
            }
        }
    }

    public void clearActivePotions()
    {
        Iterator iterator = activePotionsMap.keySet().iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Integer integer = (Integer)iterator.next();
            PotionEffect potioneffect = (PotionEffect)activePotionsMap.get(integer);

            if (!worldObj.isRemote)
            {
                iterator.remove();
                onFinishedPotionEffect(potioneffect);
            }
        }
        while (true);
    }

    public Collection getActivePotionEffects()
    {
        return activePotionsMap.values();
    }

    public boolean isPotionActive(Potion par1Potion)
    {
        return activePotionsMap.containsKey(Integer.valueOf(par1Potion.id));
    }

    /**
     * returns the PotionEffect for the supplied Potion if it is active, null otherwise.
     */
    public PotionEffect getActivePotionEffect(Potion par1Potion)
    {
        return (PotionEffect)activePotionsMap.get(Integer.valueOf(par1Potion.id));
    }

    /**
     * adds a PotionEffect to the entity
     */
    public void addPotionEffect(PotionEffect par1PotionEffect)
    {
        if (!isPotionApplicable(par1PotionEffect))
        {
            return;
        }

        if (activePotionsMap.containsKey(Integer.valueOf(par1PotionEffect.getPotionID())))
        {
            ((PotionEffect)activePotionsMap.get(Integer.valueOf(par1PotionEffect.getPotionID()))).combine(par1PotionEffect);
            onChangedPotionEffect((PotionEffect)activePotionsMap.get(Integer.valueOf(par1PotionEffect.getPotionID())));
        }
        else
        {
            activePotionsMap.put(Integer.valueOf(par1PotionEffect.getPotionID()), par1PotionEffect);
            onNewPotionEffect(par1PotionEffect);
        }
    }

    public boolean isPotionApplicable(PotionEffect par1PotionEffect)
    {
        if (getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        {
            int i = par1PotionEffect.getPotionID();

            if (i == Potion.regeneration.id || i == Potion.poison.id)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns true if this entity is undead.
     */
    public boolean isEntityUndead()
    {
        return getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    protected void onNewPotionEffect(PotionEffect par1PotionEffect)
    {
        potionsNeedUpdate = true;
    }

    protected void onChangedPotionEffect(PotionEffect par1PotionEffect)
    {
        potionsNeedUpdate = true;
    }

    protected void onFinishedPotionEffect(PotionEffect par1PotionEffect)
    {
        potionsNeedUpdate = true;
    }

    /**
     * This method return a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    protected float getSpeedModifier()
    {
        float f = 1.0F;

        if (isPotionActive(Potion.moveSpeed))
        {
            f *= 1.0F + 0.2F * (float)(getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }

        if (isPotionActive(Potion.moveSlowdown))
        {
            f *= 1.0F - 0.15F * (float)(getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
        }

        return f;
    }

    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        setLocationAndAngles(par1, par3, par5, rotationYaw, rotationPitch);
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return false;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    /**
     * Renders broken item particles using the given ItemStack
     */
    public void renderBrokenItemStack(ItemStack par1ItemStack)
    {
        worldObj.playSoundAtEntity(this, "random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F);

        for (int i = 0; i < 5; i++)
        {
            Vec3D vec3d = Vec3D.createVector(((double)rand.nextFloat() - 0.5D) * 0.10000000000000001D, Math.random() * 0.10000000000000001D + 0.10000000000000001D, 0.0D);
            vec3d.rotateAroundX((-rotationPitch * (float)Math.PI) / 180F);
            vec3d.rotateAroundY((-rotationYaw * (float)Math.PI) / 180F);
            Vec3D vec3d1 = Vec3D.createVector(((double)rand.nextFloat() - 0.5D) * 0.29999999999999999D, (double)(-rand.nextFloat()) * 0.59999999999999998D - 0.29999999999999999D, 0.59999999999999998D);
            vec3d1.rotateAroundX((-rotationPitch * (float)Math.PI) / 180F);
            vec3d1.rotateAroundY((-rotationYaw * (float)Math.PI) / 180F);
            vec3d1 = vec3d1.addVector(posX, posY + (double)getEyeHeight(), posZ);
            worldObj.spawnParticle((new StringBuilder()).append("iconcrack_").append(par1ItemStack.getItem().shiftedIndex).toString(), vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, vec3d.xCoord, vec3d.yCoord + 0.050000000000000003D, vec3d.zCoord);
        }
    }
}
