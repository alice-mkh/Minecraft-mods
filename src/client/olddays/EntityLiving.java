package net.minecraft.src;

import java.util.*;

public abstract class EntityLiving extends Entity
{
    public static boolean laddergaps = false;
    public static boolean newai = true;
    public static boolean infdevai = false;
    public static boolean rareloot = true;
    public static boolean oldloot = false;
    public static boolean jumpdelay = true;
    public static boolean survivaltest = false;
    public static boolean armorblocksall = false;
    public static int nonewmobs = 10;
    public static boolean toolbreakanim = true;
    public static boolean score = false;
    public static boolean oldswing = false;

    public boolean newai(){
        if (this instanceof EntityOcelot){
            return true;
        }
        if (this instanceof EntityIronGolem){
            return true;
        }
        if (this instanceof EntityWitch){
            return true;
        }
        if (this instanceof EntityBat){
            return true;
        }
        if (this instanceof EntityWither){
            return true;
        }
        return newai;
    }

    private boolean allow(String id, int dim){
        /*if (nonewmobs<9 && (id=="LavaSlime" || id=="Blaze") && dim==-1){
            return false;
        }*/
        if (dim!=0){
            return true;
        }
        if (nonewmobs<10 && id=="Ozelot"){
            return false;
        }
        if (nonewmobs<9 && (id=="MushroomCow" || id=="Villager")){
            return false;
        }
        if (nonewmobs<8 && id=="Enderman"){
            return false;
        }
        if (nonewmobs<7 && id=="Wolf"){
            return false;
        }
        if (nonewmobs<5 && id=="Squid"){
            return false;
        }
        if (nonewmobs<4 && id=="Chicken"){
            return false;
        }
        if (nonewmobs<2 && id=="Cow"){
            return false;
        }
        if (nonewmobs<1 && (id=="Pig" || id=="Sheep" || id=="Zombie" || id=="Skeleton" || id=="Spider" || id=="Creeper")){
            return false;
        }
        return true;
    }

    protected void dropFewItemsOld(boolean par1, int par2, int item)
    {
        int k = rand.nextInt(3);
        if (par2 > 0){
            k += rand.nextInt(par2 + 1);
        }
        for (int l = 0; l < k; l++){
            dropItem(item, 1);
        }
    }

    protected int getDefaultScoreValue(){
        if (!score){
            return 0;
        }
        if (this instanceof EntitySkeleton){
            return 120;
        }
        if (this instanceof EntityZombie){
            return 80;
        }
        if (this instanceof EntitySpider){
            return 105;
        }
        if (this instanceof EntityCreeper){
            return 200;
        }
        if (this instanceof EntityChicken){
            return 5;
        }
        if (this instanceof EntityAgeable || this instanceof EntityWaterMob){
            return 10;
        }
        if (this instanceof EntitySilverfish){
            return 20;
        }
        if (this instanceof EntityGiantZombie){
            return 500;
        }
        if (this instanceof EntityGhast || this instanceof EntityEnderman){
            return 400;
        }
        if (this instanceof EntityBlaze){
            return 500;
        }
        if (this instanceof EntityDragon){
            return 10000;
        }
        if (this instanceof EntitySlime){
            int size = ((EntitySlime)this).getSlimeSize() - 1;  //1; 2; 4 -> 0; 1; 3
            size *= 70; //0; 70; 210
            size += 10; //10; 80; 220
            if (this instanceof EntityMagmaCube){
                size *= 1.5; //15; 120; 330
            }
            return size;
        }
        return 0;
    }

    private static final float field_82177_b[] =
    {
        0.0F, 0.0F, 0.005F, 0.01F
    };
    private static final float field_82178_c[] =
    {
        0.0F, 0.0F, 0.05F, 0.1F
    };
    private static final float field_82176_d[] =
    {
        0.0F, 0.0F, 0.005F, 0.02F
    };
    public static final float field_82181_as[] =
    {
        0.0F, 0.01F, 0.07F, 0.2F
    };
    public int maxHurtResistantTime;
    public float field_70769_ao;
    public float field_70770_ap;
    public float renderYawOffset;
    public float prevRenderYawOffset;

    /** Entity head rotation yaw */
    public float rotationYawHead;

    /** Entity head rotation yaw at previous tick */
    public float prevRotationYawHead;
    protected float field_70768_au;
    protected float field_70766_av;
    protected float field_70764_aw;
    protected float field_70763_ax;
    protected boolean field_70753_ay;

    /** the path for the texture of this entityLiving */
    protected String texture;
    protected boolean field_70740_aA;
    protected float field_70741_aB;

    /**
     * a string holding the type of entity it is currently only implemented in entityPlayer(as 'humanoid')
     */
    protected String entityType;
    protected float field_70743_aD;

    /** The score value of the Mob, the amount of points the mob is worth. */
    protected int scoreValue;
    protected float field_70745_aF;

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
    public int livingSoundTime;

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
    public int field_70731_aW;
    public float field_70730_aX;
    public float prevLegYaw;
    public float legYaw;

    /**
     * Only relevant when legYaw is not 0(the entity is moving). Influences where in its swing legs and arms currently
     * are.
     */
    public float legSwing;

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
    private int field_70748_f;
    private EntityLookHelper lookHelper;
    private EntityMoveHelper moveHelper;

    /** Entity jumping helper */
    private EntityJumpHelper jumpHelper;
    private EntityBodyHelper bodyHelper;
    private PathNavigate navigator;
    protected final EntityAITasks tasks;
    protected final EntityAITasks targetTasks;

    /** The active target the Task system uses for tracking */
    private EntityLiving attackTarget;
    private EntitySenses senses;
    private float AIMoveSpeed;
    private ChunkCoordinates homePosition;

    /** If -1 there is no maximum distance */
    private float maximumHomeDistance;
    private ItemStack field_82182_bS[];
    protected float field_82174_bp[];
    private ItemStack field_82180_bT[];
    public boolean field_82175_bq;
    public int field_82173_br;
    protected boolean field_82172_bs;
    private boolean field_82179_bU;
    protected boolean field_83001_bt;

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
    float field_70706_bo;

    /** Amount of damage taken in last hit, in half-hearts */
    protected int lastDamage;

    /** Holds the living entity age, used to control the despawn. */
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

    /** This entity's current target. */
    private Entity currentTarget;

    /** How long to keep a specific target entity */
    protected int numTicksToChaseTarget;
    public int persistentId;

    public EntityLiving(World par1World)
    {
        super(par1World);
        persistentId = rand.nextInt(0x7fffffff);
        maxHurtResistantTime = 20;
        renderYawOffset = 0.0F;
        prevRenderYawOffset = 0.0F;
        rotationYawHead = 0.0F;
        prevRotationYawHead = 0.0F;
        field_70753_ay = true;
        texture = "/mob/char.png";
        field_70740_aA = true;
        field_70741_aB = 0.0F;
        entityType = null;
        field_70743_aD = 1.0F;
        scoreValue = getDefaultScoreValue();
        field_70745_aF = 0.0F;
        landMovementFactor = 0.1F;
        jumpMovementFactor = 0.02F;
        attackedAtYaw = 0.0F;
        deathTime = 0;
        attackTime = 0;
        dead = false;
        field_70731_aW = -1;
        field_70730_aX = (float)(Math.random() * 0.89999997615814209D + 0.10000000149011612D);
        attackingPlayer = null;
        recentlyHit = 0;
        entityLivingToAttack = null;
        revengeTimer = 0;
        lastAttackingEntity = null;
        arrowHitTempCounter = 0;
        arrowHitTimer = 0;
        activePotionsMap = new HashMap();
        potionsNeedUpdate = true;
        homePosition = new ChunkCoordinates(0, 0, 0);
        maximumHomeDistance = -1F;
        field_82182_bS = new ItemStack[5];
        field_82174_bp = new float[5];
        field_82180_bT = new ItemStack[5];
        field_82175_bq = false;
        field_82173_br = 0;
        field_82172_bs = false;
        field_82179_bU = false;
        field_83001_bt = false;
        field_70706_bo = 0.0F;
        lastDamage = 0;
        entityAge = 0;
        isJumping = false;
        defaultPitch = 0.0F;
        moveSpeed = 0.7F;
        jumpTicks = 0;
        numTicksToChaseTarget = 0;
        health = getMaxHealth();
        preventEntitySpawning = true;
        tasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
        targetTasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
        lookHelper = new EntityLookHelper(this);
        moveHelper = new EntityMoveHelper(this);
        jumpHelper = new EntityJumpHelper(this);
        bodyHelper = new EntityBodyHelper(this);
        navigator = new PathNavigate(this, par1World, 16F);
        senses = new EntitySenses(this);
        field_70770_ap = (float)(Math.random() + 1.0D) * 0.01F;
        setPosition(posX, posY, posZ);
        field_70769_ao = (float)Math.random() * 12398F;
        rotationYaw = (float)(Math.random() * Math.PI * 2D);
        rotationYawHead = rotationYaw;

        for (int i = 0; i < field_82174_bp.length; i++)
        {
            field_82174_bp[i] = 0.05F;
        }

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

    /**
     * returns the EntitySenses Object for the EntityLiving
     */
    public EntitySenses getEntitySenses()
    {
        return senses;
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

    public float func_70079_am()
    {
        return rotationYawHead;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setHeadRotationYaw(float par1)
    {
        rotationYawHead = par1;
    }

    /**
     * the movespeed used for the new AI system
     */
    public float getAIMoveSpeed()
    {
        return AIMoveSpeed;
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float par1)
    {
        AIMoveSpeed = par1;
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

    public boolean isExplosiveMob(Class par1Class)
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
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3)
    {
        if (par3 && fallDistance > 0.0F)
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int k = MathHelper.floor_double(posZ);
            int l = worldObj.getBlockId(i, j, k);

            if (l == 0 && worldObj.getBlockId(i, j - 1, k) == Block.fence.blockID)
            {
                l = worldObj.getBlockId(i, j - 1, k);
            }

            if (l > 0)
            {
                Block.blocksList[l].onFallenUpon(worldObj, i, j, k, this, fallDistance);
            }
        }

        super.updateFallState(par1, par3);
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
        dataWatcher.addObject(8, Integer.valueOf(field_70748_f));
        dataWatcher.addObject(9, Byte.valueOf((byte)0));
    }

    /**
     * returns true if the entity provided in the argument can be seen. (Raytrace)
     */
    public boolean canEntityBeSeen(Entity par1Entity)
    {
        return worldObj.rayTraceBlocks(worldObj.func_82732_R().getVecFromPool(posX, posY + (double)getEyeHeight(), posZ), worldObj.func_82732_R().getVecFromPool(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        return texture;
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
        worldObj.theProfiler.startSection("mobBaseTick");

        if (isEntityAlive() && rand.nextInt(1000) < livingSoundTime++)
        {
            livingSoundTime = -getTalkInterval();
            playLivingSound();
        }

        if (isEntityAlive() && isEntityInsideOpaqueBlock())
        {
            attackEntityFrom(DamageSource.inWall, 1);
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

        if (hurtResistantTime > 0)
        {
            hurtResistantTime--;
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
        field_70763_ax = field_70764_aw;
        prevRenderYawOffset = renderYawOffset;
        prevRotationYawHead = rotationYawHead;
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
        worldObj.theProfiler.endSection();
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
        int i = EnchantmentHelper.getRespiration(this);

        if (i > 0 && rand.nextInt(i + 1) > 0)
        {
            return par1;
        }
        else
        {
            return par1 - 1;
        }
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
        field_70768_au = field_70766_av;
        field_70766_av = 0.0F;
        fallDistance = 0.0F;
    }

    /**
     * Sets the position and rotation. Only difference from the other one is no bounding on the rotation. Args: posX,
     * posY, posZ, yaw, pitch
     */
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        yOffset = 0.0F;
        newPosX = par1;
        newPosY = par3;
        newPosZ = par5;
        newRotationYaw = par7;
        newRotationPitch = par8;
        newPosRotationIncrements = par9;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!worldObj.isRemote)
        {
            for (int i = 0; i < 5; i++)
            {
                ItemStack itemstack = getCurrentItemOrArmor(i);

                if (!ItemStack.areItemStacksEqual(itemstack, field_82180_bT[i]) && worldObj instanceof WorldServer)
                {
                    ((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(entityId, i, itemstack));
                    field_82180_bT[i] = itemstack != null ? itemstack.copy() : null;
                }
            }
        }

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
        float f = (float)(d * d + d1 * d1);
        float f1 = renderYawOffset;
        float f2 = 0.0F;
        field_70768_au = field_70766_av;
        float f3 = 0.0F;

        if (f > 0.0025F)
        {
            f3 = 1.0F;
            f2 = (float)Math.sqrt(f) * 3F;
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

        field_70766_av += (f3 - field_70766_av) * 0.3F;
        worldObj.theProfiler.startSection("headTurn");

        if (isAIEnabled() && newai())
        {
            bodyHelper.func_75664_a();
        }
        else
        {
            float f4 = MathHelper.wrapAngleTo180_float(f1 - renderYawOffset);
            renderYawOffset += f4 * 0.3F;
            float f5 = MathHelper.wrapAngleTo180_float(rotationYaw - renderYawOffset);
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

        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("rangeChecks");

        for (; rotationYaw - prevRotationYaw < -180F; prevRotationYaw -= 360F) { }

        for (; rotationYaw - prevRotationYaw >= 180F; prevRotationYaw += 360F) { }

        for (; renderYawOffset - prevRenderYawOffset < -180F; prevRenderYawOffset -= 360F) { }

        for (; renderYawOffset - prevRenderYawOffset >= 180F; prevRenderYawOffset += 360F) { }

        for (; rotationPitch - prevRotationPitch < -180F; prevRotationPitch -= 360F) { }

        for (; rotationPitch - prevRotationPitch >= 180F; prevRotationPitch += 360F) { }

        for (; rotationYawHead - prevRotationYawHead < -180F; prevRotationYawHead -= 360F) { }

        for (; rotationYawHead - prevRotationYawHead >= 180F; prevRotationYawHead += 360F) { }

        worldObj.theProfiler.endSection();
        field_70764_aw += f2;
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

        hurtResistantTime = maxHurtResistantTime / 2;
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

        if ((par1DamageSource == DamageSource.field_82728_o || par1DamageSource == DamageSource.field_82729_p) && getCurrentItemOrArmor(4) != null)
        {
            par2 = (int)((float)par2 * 0.55F);
        }

        legYaw = 1.5F;
        boolean flag = true;

        if ((float)hurtResistantTime > (float)maxHurtResistantTime / 2.0F)
        {
            if (par2 <= lastDamage)
            {
                return false;
            }

            damageEntity(par1DamageSource, par2 - lastDamage);
            lastDamage = par2;
            flag = false;
        }
        else
        {
            lastDamage = par2;
            prevHealth = health;
            hurtResistantTime = maxHurtResistantTime;
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

            if (par1DamageSource != DamageSource.drown && par1DamageSource != DamageSource.field_76375_l)
            {
                setBeenAttacked();
            }

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
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation()
    {
        hurtTime = maxHurtTime = 10;
        attackedAtYaw = 0.0F;
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    public int getTotalArmorValue()
    {
        int i = 0;
        ItemStack aitemstack[] = getLastActiveItems();
        int j = aitemstack.length;

        for (int k = 0; k < j; k++)
        {
            ItemStack itemstack = aitemstack[k];

            if (itemstack != null && (itemstack.getItem() instanceof ItemArmor))
            {
                int l = ((ItemArmor)itemstack.getItem()).damageReduceAmount;
                i += l;
            }
        }

        return i;
    }

    protected void damageArmor(int i)
    {
    }

    /**
     * Reduces damage, depending on armor
     */
    protected int applyArmorCalculations(DamageSource par1DamageSource, int par2)
    {
        if (!par1DamageSource.isUnblockable() || armorblocksall)
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
        if (field_83001_bt)
        {
            return;
        }
        else
        {
            par2 = applyArmorCalculations(par1DamageSource, par2);
            par2 = applyPotionDamageCalculations(par1DamageSource, par2);
            health -= par2;
            return;
        }
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
        return "damage.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "damage.hit";
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
                i = EnchantmentHelper.getLootingModifier((EntityLiving)entity);
            }

            if (!isChild() && worldObj.func_82736_K().func_82766_b("doMobLoot"))
            {
                if (survivaltest){
                    if (this instanceof EntitySheep || this instanceof EntityPig){
                        dropFewItemsOld(recentlyHit > 0, i, Block.mushroomBrown.blockID);
                    }
                }else if (oldloot){
                    if (this instanceof EntityPigZombie){
                        dropFewItemsOld(recentlyHit > 0, i, Item.porkCooked.shiftedIndex);
                    }else if (this instanceof EntityChicken || this instanceof EntityZombie){
                        dropFewItemsOld(recentlyHit > 0, i, Item.feather.shiftedIndex);
                    }else if (this instanceof EntityCow){
                        dropFewItemsOld(recentlyHit > 0, i, Item.leather.shiftedIndex);
                    }else if (this instanceof EntitySpider){
                        dropFewItemsOld(recentlyHit > 0, i, Item.silk.shiftedIndex);
                    }else if (this instanceof EntityPig){
                        dropFewItemsOld(recentlyHit > 0, i, getDropItemId());
                    }else{
                        dropFewItems(recentlyHit > 0, i);
                    }
                }else{
                    dropFewItems(recentlyHit > 0, i);
                }
                func_82160_b(recentlyHit > 0, i);

                if (recentlyHit > 0 && rareloot)
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
        int i = MathHelper.ceiling_float_int(par1 - 3F);

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
        if (isInWater() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying))
        {
            double d = posY;
            moveFlying(par1, par2, isAIEnabled() && newai() ? 0.04F : 0.02F);
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
        else if (handleLavaMovement() && (!(this instanceof EntityPlayer) || !((EntityPlayer)this).capabilities.isFlying))
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
                if (isAIEnabled() && newai())
                {
                    f2 = getAIMoveSpeed();
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

        prevLegYaw = legYaw;
        double d2 = posX - prevPosX;
        double d3 = posZ - prevPosZ;
        float f4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        legYaw += (f4 - legYaw) * 0.4F;
        legSwing += legYaw;
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
        if (laddergaps){
            int l1 = worldObj.getBlockId(i, j + 1, k);
            return l == Block.ladder.blockID || l == Block.vine.blockID || l1 == Block.ladder.blockID || l1 == Block.vine.blockID;
        }
        return l == Block.ladder.blockID || l == Block.vine.blockID;
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
        par1NBTTagCompound.setBoolean("CanPickUpLoot", field_82172_bs);
        par1NBTTagCompound.setBoolean("PersistenceRequired", field_82179_bU);
        par1NBTTagCompound.setBoolean("Invulnerable", field_83001_bt);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < field_82182_bS.length; i++)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if (field_82182_bS[i] != null)
            {
                field_82182_bS[i].writeToNBT(nbttagcompound);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        par1NBTTagCompound.setTag("Equipment", nbttaglist);

        if (!activePotionsMap.isEmpty())
        {
            NBTTagList nbttaglist1 = new NBTTagList();
            PotionEffect potioneffect;

            for (Iterator iterator = activePotionsMap.values().iterator(); iterator.hasNext(); nbttaglist1.appendTag(potioneffect.func_82719_a(new NBTTagCompound())))
            {
                potioneffect = (PotionEffect)iterator.next();
            }

            par1NBTTagCompound.setTag("ActiveEffects", nbttaglist1);
        }

        NBTTagList nbttaglist2 = new NBTTagList();

        for (int j = 0; j < field_82174_bp.length; j++)
        {
            nbttaglist2.appendTag(new NBTTagFloat((new StringBuilder()).append(j).append("").toString(), field_82174_bp[j]));
        }

        par1NBTTagCompound.setTag("DropChances", nbttaglist2);
        par1NBTTagCompound.setInteger("PersistentId", persistentId);
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
        field_82172_bs = par1NBTTagCompound.getBoolean("CanPickUpLoot");
        field_82179_bU = par1NBTTagCompound.getBoolean("PersistenceRequired");
        field_83001_bt = par1NBTTagCompound.getBoolean("Invulnerable");

        if (par1NBTTagCompound.hasKey("Equipment"))
        {
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Equipment");

            for (int i = 0; i < field_82182_bS.length; i++)
            {
                field_82182_bS[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbttaglist.tagAt(i));
            }
        }

        if (par1NBTTagCompound.hasKey("ActiveEffects"))
        {
            NBTTagList nbttaglist1 = par1NBTTagCompound.getTagList("ActiveEffects");

            for (int j = 0; j < nbttaglist1.tagCount(); j++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist1.tagAt(j);
                PotionEffect potioneffect = PotionEffect.func_82722_b(nbttagcompound);
                activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
            }
        }

        if (par1NBTTagCompound.hasKey("DropChances"))
        {
            NBTTagList nbttaglist2 = par1NBTTagCompound.getTagList("DropChances");

            for (int k = 0; k < nbttaglist2.tagCount(); k++)
            {
                field_82174_bp[k] = ((NBTTagFloat)nbttaglist2.tagAt(k)).data;
            }
        }
        persistentId = par1NBTTagCompound.getInteger("PersistentId");
        if(persistentId == 0){
            persistentId = rand.nextInt(0x7fffffff);
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
            double d3 = MathHelper.wrapAngleTo180_double(newRotationYaw - (double)rotationYaw);
            rotationYaw += d3 / (double)newPosRotationIncrements;
            rotationPitch += (newRotationPitch - (double)rotationPitch) / (double)newPosRotationIncrements;
            newPosRotationIncrements--;
            setPosition(d, d1, d2);
            setRotation(rotationYaw, rotationPitch);
        }

        if (Math.abs(motionX) < 0.0050000000000000001D)
        {
            motionX = 0.0D;
        }

        if (Math.abs(motionY) < 0.0050000000000000001D)
        {
            motionY = 0.0D;
        }

        if (Math.abs(motionZ) < 0.0050000000000000001D)
        {
            motionZ = 0.0D;
        }

        worldObj.theProfiler.startSection("ai");

        if (isMovementBlocked())
        {
            isJumping = false;
            moveStrafing = 0.0F;
            moveForward = 0.0F;
            randomYawVelocity = 0.0F;
        }
        else if (isClientWorld())
        {
            if (isAIEnabled() && newai())
            {
                worldObj.theProfiler.startSection("newAi");
                updateAITasks();
                worldObj.theProfiler.endSection();
            }
            else
            {
                worldObj.theProfiler.startSection("oldAi");
                updateEntityActionState();
                worldObj.theProfiler.endSection();
                rotationYawHead = rotationYaw;
            }
        }

        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("jump");

        if (isJumping)
        {
            if (isInWater() || handleLavaMovement())
            {
                motionY += 0.039999999105930328D;
            }
            else if (onGround && (jumpTicks == 0 || !jumpdelay))
            {
                jump();
                jumpTicks = 10;
            }
        }
        else
        {
            jumpTicks = 0;
        }

        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("travel");
        moveStrafing *= 0.98F;
        moveForward *= 0.98F;
        randomYawVelocity *= 0.9F;
        float f = landMovementFactor;
        landMovementFactor *= getSpeedModifier();
        moveEntityWithHeading(moveStrafing, moveForward);
        landMovementFactor = f;
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("push");

        if (!worldObj.isRemote)
        {
            List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

            if (list != null && !list.isEmpty())
            {
                Iterator iterator = list.iterator();

                do
                {
                    if (!iterator.hasNext())
                    {
                        break;
                    }

                    Entity entity = (Entity)iterator.next();

                    if (entity.canBePushed())
                    {
                        func_82167_n(entity);
                    }
                }
                while (true);
            }
        }

        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("looting");

        if (!worldObj.isRemote && field_82172_bs && worldObj.func_82736_K().func_82766_b("mobGriefing"))
        {
            List list1 = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, boundingBox.expand(1.0D, 0.0D, 1.0D));
            Iterator iterator1 = list1.iterator();

            do
            {
                if (!iterator1.hasNext())
                {
                    break;
                }

                EntityItem entityitem = (EntityItem)iterator1.next();

                if (!entityitem.isDead && entityitem.item != null)
                {
                    ItemStack itemstack = entityitem.item;
                    int i = func_82159_b(itemstack);

                    if (i > -1)
                    {
                        boolean flag = true;
                        ItemStack itemstack1 = getCurrentItemOrArmor(i);

                        if (itemstack1 != null)
                        {
                            if (i == 0)
                            {
                                if ((itemstack.getItem() instanceof ItemSword) && !(itemstack1.getItem() instanceof ItemSword))
                                {
                                    flag = true;
                                }
                                else if ((itemstack.getItem() instanceof ItemSword) && (itemstack1.getItem() instanceof ItemSword))
                                {
                                    ItemSword itemsword = (ItemSword)itemstack.getItem();
                                    ItemSword itemsword1 = (ItemSword)itemstack1.getItem();

                                    if (itemsword.func_82803_g() == itemsword1.func_82803_g())
                                    {
                                        flag = itemstack.getItemDamage() > itemstack1.getItemDamage() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                                    }
                                    else
                                    {
                                        flag = itemsword.func_82803_g() > itemsword1.func_82803_g();
                                    }
                                }
                                else
                                {
                                    flag = false;
                                }
                            }
                            else if ((itemstack.getItem() instanceof ItemArmor) && !(itemstack1.getItem() instanceof ItemArmor))
                            {
                                flag = true;
                            }
                            else if ((itemstack.getItem() instanceof ItemArmor) && (itemstack1.getItem() instanceof ItemArmor))
                            {
                                ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
                                ItemArmor itemarmor1 = (ItemArmor)itemstack1.getItem();

                                if (itemarmor.damageReduceAmount == itemarmor1.damageReduceAmount)
                                {
                                    flag = itemstack.getItemDamage() > itemstack1.getItemDamage() || itemstack.hasTagCompound() && !itemstack1.hasTagCompound();
                                }
                                else
                                {
                                    flag = itemarmor.damageReduceAmount > itemarmor1.damageReduceAmount;
                                }
                            }
                            else
                            {
                                flag = false;
                            }
                        }

                        if (flag)
                        {
                            if (itemstack1 != null && rand.nextFloat() - 0.1F < field_82174_bp[i])
                            {
                                entityDropItem(itemstack1, 0.0F);
                            }

                            func_70062_b(i, itemstack);
                            field_82174_bp[i] = 2.0F;
                            field_82179_bU = true;
                            onItemPickup(entityitem, 1);
                            entityitem.setDead();
                        }
                    }
                }
            }
            while (true);
        }

        worldObj.theProfiler.endSection();
    }

    protected void func_82167_n(Entity par1Entity)
    {
        par1Entity.applyEntityCollision(this);
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
     * Causes this entity to do an upwards motion (jumping).
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
        if (field_82179_bU)
        {
            return;
        }

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
        worldObj.theProfiler.startSection("checkDespawn");
        despawnEntity();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("sensing");
        senses.clearSensingCache();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("targetSelector");
        targetTasks.onUpdateTasks();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("goalSelector");
        tasks.onUpdateTasks();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("navigation");
        navigator.onUpdateNavigation();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("mob tick");
        updateAITick();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("controls");
        worldObj.theProfiler.startSection("move");
        moveHelper.onUpdateMoveHelper();
        worldObj.theProfiler.endStartSection("look");
        lookHelper.onUpdateLook();
        worldObj.theProfiler.endStartSection("jump");
        jumpHelper.doJump();
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.endSection();
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
    }

    protected void updateEntityActionStateInfdev()
    {
        entityAge++;
        despawnEntity();
        if(rand.nextFloat() < 0.07F)
        {
            moveStrafing = (rand.nextFloat() - 0.5F) * moveSpeed;
            moveForward = rand.nextFloat() * moveSpeed;
        }
        isJumping = rand.nextFloat() < 0.01F;
        float f = 8F;
        if(rand.nextFloat() < 0.04F)
        {
            EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, f);

            if (entityplayer != null)
            {
                currentTarget = entityplayer;
                numTicksToChaseTarget = 10 + rand.nextInt(20);
            }
            else
            {
                randomYawVelocity = (rand.nextFloat() - 0.5F) * 60F;
            }
        }
        rotationYaw += randomYawVelocity;
        rotationPitch = 0.0F;
        boolean flag = isInWater();
        boolean flag1 = handleLavaMovement();
        if(flag || flag1)
        {
            isJumping = rand.nextFloat() < 0.8F;
        }
    }

    protected void updateEntityActionState()
    {
        if (infdevai){
            updateEntityActionStateInfdev();
            return;
        }
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

    protected void func_82168_bl()
    {
        int i = func_82166_i();

        if (field_82175_bq)
        {
            field_82173_br++;

            if (field_82173_br >= i)
            {
                field_82173_br = 0;
                field_82175_bq = false;
            }
        }
        else
        {
            field_82173_br = 0;
        }

        swingProgress = (float)field_82173_br / (float)i;
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
     * Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
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
        float f = MathHelper.wrapAngleTo180_float(par2 - par1);

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
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        if (!allow(EntityList.getEntityString(this), worldObj.provider.dimensionId)){
            return false;
        }
        return worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox);
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
        attackEntityFrom(DamageSource.outOfWorld, 4);
    }

    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args: partialTickTime
     */
    public float getSwingProgress(float par1)
    {
        float f = swingProgress - prevSwingProgress;

        if (f < 0.0F)
        {
            f++;
        }

        return prevSwingProgress + f * par1;
    }

    /**
     * interpolated position vector
     */
    public Vec3 getPosition(float par1)
    {
        if (par1 == 1.0F)
        {
            return worldObj.func_82732_R().getVecFromPool(posX, posY, posZ);
        }
        else
        {
            double d = prevPosX + (posX - prevPosX) * (double)par1;
            double d1 = prevPosY + (posY - prevPosY) * (double)par1;
            double d2 = prevPosZ + (posZ - prevPosZ) * (double)par1;
            return worldObj.func_82732_R().getVecFromPool(d, d1, d2);
        }
    }

    /**
     * returns a (normalized) vector of where this entity is looking
     */
    public Vec3 getLookVec()
    {
        return getLook(1.0F);
    }

    /**
     * interpolated look vector
     */
    public Vec3 getLook(float par1)
    {
        if (par1 == 1.0F)
        {
            float f = MathHelper.cos(-rotationYaw * 0.01745329F - (float)Math.PI);
            float f2 = MathHelper.sin(-rotationYaw * 0.01745329F - (float)Math.PI);
            float f4 = -MathHelper.cos(-rotationPitch * 0.01745329F);
            float f6 = MathHelper.sin(-rotationPitch * 0.01745329F);
            return worldObj.func_82732_R().getVecFromPool(f2 * f4, f6, f * f4);
        }
        else
        {
            float f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * par1;
            float f3 = prevRotationYaw + (rotationYaw - prevRotationYaw) * par1;
            float f5 = MathHelper.cos(-f3 * 0.01745329F - (float)Math.PI);
            float f7 = MathHelper.sin(-f3 * 0.01745329F - (float)Math.PI);
            float f8 = -MathHelper.cos(-f1 * 0.01745329F);
            float f9 = MathHelper.sin(-f1 * 0.01745329F);
            return worldObj.func_82732_R().getVecFromPool(f7 * f8, f9, f5 * f8);
        }
    }

    /**
     * Returns render size modifier
     */
    public float getRenderSizeModifier()
    {
        return 1.0F;
    }

    /**
     * Performs a ray trace for the distance specified and using the partial tick time. Args: distance, partialTickTime
     */
    public MovingObjectPosition rayTrace(double par1, float par3)
    {
        Vec3 vec3 = getPosition(par3);
        Vec3 vec3_1 = getLook(par3);
        Vec3 vec3_2 = vec3.addVector(vec3_1.xCoord * par1, vec3_1.yCoord * par1, vec3_1.zCoord * par1);
        return worldObj.rayTraceBlocks(vec3, vec3_2);
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 4;
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 2)
        {
            legYaw = 1.5F;
            hurtResistantTime = maxHurtResistantTime;
            hurtTime = maxHurtTime = 10;
            attackedAtYaw = 0.0F;
            worldObj.playSoundAtEntity(this, getHurtSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            attackEntityFrom(DamageSource.generic, 0);
        }
        else if (par1 == 3)
        {
            worldObj.playSoundAtEntity(this, getDeathSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            health = 0;
            onDeath(DamageSource.generic);
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    /**
     * Returns whether player is sleeping or not
     */
    public boolean isPlayerSleeping()
    {
        return false;
    }

    /**
     * Gets the Icon Index of the item currently held
     */
    public int getItemIcon(ItemStack par1ItemStack, int par2)
    {
        return par1ItemStack.getIconIndex();
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
                if (activePotionsMap.isEmpty())
                {
                    dataWatcher.updateObject(9, Byte.valueOf((byte)0));
                    dataWatcher.updateObject(8, Integer.valueOf(0));
                    func_82142_c(false);
                }
                else
                {
                    int i = PotionHelper.calcPotionLiquidColor(activePotionsMap.values());
                    dataWatcher.updateObject(9, Byte.valueOf(((byte)(PotionHelper.func_82817_b(activePotionsMap.values()) ? 1 : 0))));
                    dataWatcher.updateObject(8, Integer.valueOf(i));
                    func_82142_c(func_82165_m(Potion.invisibility.id));
                }
            }

            potionsNeedUpdate = false;
        }

        int j = dataWatcher.getWatchableObjectInt(8);
        boolean flag = dataWatcher.getWatchableObjectByte(9) > 0;

        if (j > 0)
        {
            boolean flag1 = false;

            if (!func_82150_aj())
            {
                flag1 = rand.nextBoolean();
            }
            else
            {
                flag1 = rand.nextInt(15) == 0;
            }

            if (flag)
            {
                flag1 &= rand.nextInt(5) == 0;
            }

            if (flag1 && j > 0)
            {
                double d = (double)(j >> 16 & 0xff) / 255D;
                double d1 = (double)(j >> 8 & 0xff) / 255D;
                double d2 = (double)(j >> 0 & 0xff) / 255D;
                worldObj.spawnParticle(flag ? "mobSpellAmbient" : "mobSpell", posX + (rand.nextDouble() - 0.5D) * (double)width, (posY + rand.nextDouble() * (double)height) - (double)yOffset, posZ + (rand.nextDouble() - 0.5D) * (double)width, d, d1, d2);
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

    public boolean func_82165_m(int par1)
    {
        return activePotionsMap.containsKey(Integer.valueOf(par1));
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

    /**
     * input is the potion id to remove from the current active potion effects
     */
    public void removePotionEffect(int par1)
    {
        activePotionsMap.remove(Integer.valueOf(par1));
    }

    public void func_82170_o(int par1)
    {
        PotionEffect potioneffect = (PotionEffect)activePotionsMap.remove(Integer.valueOf(par1));

        if (potioneffect != null)
        {
            onFinishedPotionEffect(potioneffect);
        }
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
     * This method returns a value to be applied directly to entity speed, this factor is less than 1 when a slowdown
     * potion effect is applied, more than 1 when a haste potion effect is applied and 2 for fleeing entities.
     */
    public float getSpeedModifier()
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
     * Move the entity to the coordinates informed, but keep yaw/pitch values.
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

        if (toolbreakanim){
            for (int i = 0; i < 5; i++)
            {
                Vec3 vec3 = worldObj.func_82732_R().getVecFromPool(((double)rand.nextFloat() - 0.5D) * 0.10000000000000001D, Math.random() * 0.10000000000000001D + 0.10000000000000001D, 0.0D);
                vec3.rotateAroundX((-rotationPitch * (float)Math.PI) / 180F);
                vec3.rotateAroundY((-rotationYaw * (float)Math.PI) / 180F);
                Vec3 vec3_1 = worldObj.func_82732_R().getVecFromPool(((double)rand.nextFloat() - 0.5D) * 0.29999999999999999D, (double)(-rand.nextFloat()) * 0.59999999999999998D - 0.29999999999999999D, 0.59999999999999998D);
                vec3_1.rotateAroundX((-rotationPitch * (float)Math.PI) / 180F);
                vec3_1.rotateAroundY((-rotationYaw * (float)Math.PI) / 180F);
                vec3_1 = vec3_1.addVector(posX, posY + (double)getEyeHeight(), posZ);
                worldObj.spawnParticle((new StringBuilder()).append("iconcrack_").append(par1ItemStack.getItem().shiftedIndex).toString(), vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, vec3.xCoord, vec3.yCoord + 0.050000000000000003D, vec3.zCoord);
            }
        }
    }

    public boolean hasCurrentTarget()
    {
        return currentTarget != null;
    }

    public Entity getCurrentTarget()
    {
        return currentTarget;
    }

    public int func_82143_as()
    {
        if (getAttackTarget() == null)
        {
            return 3;
        }

        int i = (int)((float)health - (float)getMaxHealth() * 0.33F);
        i -= (3 - worldObj.difficultySetting) * 4;

        if (i < 0)
        {
            i = 0;
        }

        return i + 3;
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return field_82182_bS[0];
    }

    /**
     * 0 = item, 1-n is armor
     */
    public ItemStack getCurrentItemOrArmor(int par1)
    {
        return field_82182_bS[par1];
    }

    public ItemStack func_82169_q(int par1)
    {
        return field_82182_bS[par1 + 1];
    }

    public void func_70062_b(int par1, ItemStack par2ItemStack)
    {
        field_82182_bS[par1] = par2ItemStack;
    }

    public ItemStack[] getLastActiveItems()
    {
        return field_82182_bS;
    }

    protected void func_82160_b(boolean par1, int par2)
    {
        for (int i = 0; i < getLastActiveItems().length; i++)
        {
            ItemStack itemstack = getCurrentItemOrArmor(i);
            boolean flag = field_82174_bp[i] > 1.0F;

            if (itemstack == null || !par1 && !flag || rand.nextFloat() - (float)par2 * 0.01F >= field_82174_bp[i])
            {
                continue;
            }

            if (!flag && itemstack.isItemStackDamageable())
            {
                int j = Math.max(itemstack.getMaxDamage() - 25, 1);
                int k = itemstack.getMaxDamage() - rand.nextInt(rand.nextInt(j) + 1);

                if (k > j)
                {
                    k = j;
                }

                if (k < 1)
                {
                    k = 1;
                }

                itemstack.setItemDamage(k);
            }

            entityDropItem(itemstack, 0.0F);
        }
    }

    protected void func_82164_bB()
    {
        if (rand.nextFloat() < field_82176_d[worldObj.difficultySetting])
        {
            int i = rand.nextInt(2);
            float f = worldObj.difficultySetting != 3 ? 0.25F : 0.1F;

            if (rand.nextFloat() < 0.07F)
            {
                i++;
            }

            if (rand.nextFloat() < 0.07F)
            {
                i++;
            }

            if (rand.nextFloat() < 0.07F)
            {
                i++;
            }

            for (int j = 3; j >= 0; j--)
            {
                ItemStack itemstack = func_82169_q(j);

                if (j < 3 && rand.nextFloat() < f)
                {
                    break;
                }

                if (itemstack != null)
                {
                    continue;
                }

                Item item = func_82161_a(j + 1, i);

                if (item != null)
                {
                    func_70062_b(j + 1, new ItemStack(item));
                }
            }
        }
    }

    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    public void onItemPickup(Entity par1Entity, int par2)
    {
        if (!par1Entity.isDead && !worldObj.isRemote && worldObj instanceof WorldServer)
        {
            EntityTracker entitytracker = ((WorldServer)worldObj).getEntityTracker();

            if (par1Entity instanceof EntityItem)
            {
                entitytracker.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, entityId));
            }

            if (par1Entity instanceof EntityArrow)
            {
                entitytracker.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, entityId));
            }

            if (par1Entity instanceof EntityXPOrb)
            {
                entitytracker.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, entityId));
            }
        }
    }

    public static int func_82159_b(ItemStack par0ItemStack)
    {
        if (par0ItemStack.itemID == Block.pumpkin.blockID || par0ItemStack.itemID == Item.field_82799_bQ.shiftedIndex)
        {
            return 4;
        }

        if (par0ItemStack.getItem() instanceof ItemArmor)
        {
            switch (((ItemArmor)par0ItemStack.getItem()).armorType)
            {
                case 3:
                    return 1;
                case 2:
                    return 2;
                case 1:
                    return 3;
                case 0:
                    return 4;
            }
        }

        return 0;
    }

    public static Item func_82161_a(int par0, int par1)
    {
        switch (par0)
        {
            case 4:

                if (par1 == 0)
                {
                    return Item.helmetLeather;
                }

                if (par1 == 1)
                {
                    return Item.helmetGold;
                }

                if (par1 == 2)
                {
                    return Item.helmetChain;
                }

                if (par1 == 3)
                {
                    return Item.helmetSteel;
                }

                if (par1 == 4)
                {
                    return Item.helmetDiamond;
                }

            case 3:

                if (par1 == 0)
                {
                    return Item.plateLeather;
                }

                if (par1 == 1)
                {
                    return Item.plateGold;
                }

                if (par1 == 2)
                {
                    return Item.plateChain;
                }

                if (par1 == 3)
                {
                    return Item.plateSteel;
                }

                if (par1 == 4)
                {
                    return Item.plateDiamond;
                }

            case 2:

                if (par1 == 0)
                {
                    return Item.legsLeather;
                }

                if (par1 == 1)
                {
                    return Item.legsGold;
                }

                if (par1 == 2)
                {
                    return Item.legsChain;
                }

                if (par1 == 3)
                {
                    return Item.legsSteel;
                }

                if (par1 == 4)
                {
                    return Item.legsDiamond;
                }

            case 1:

                if (par1 == 0)
                {
                    return Item.bootsLeather;
                }

                if (par1 == 1)
                {
                    return Item.bootsGold;
                }

                if (par1 == 2)
                {
                    return Item.bootsChain;
                }

                if (par1 == 3)
                {
                    return Item.bootsSteel;
                }

                if (par1 == 4)
                {
                    return Item.bootsDiamond;
                }

            default:
                return null;
        }
    }

    protected void func_82162_bC()
    {
        if (getHeldItem() != null && rand.nextFloat() < field_82177_b[worldObj.difficultySetting])
        {
            EnchantmentHelper.addRandomEnchantment(rand, getHeldItem(), 5);
        }

        for (int i = 0; i < 4; i++)
        {
            ItemStack itemstack = func_82169_q(i);

            if (itemstack != null && rand.nextFloat() < field_82178_c[worldObj.difficultySetting])
            {
                EnchantmentHelper.addRandomEnchantment(rand, itemstack, 5);
            }
        }
    }

    public void func_82163_bD()
    {
    }

    private int func_82166_i()
    {
        if (isPotionActive(Potion.digSpeed))
        {
            return (oldswing ? 8 : 6) - (1 + getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1;
        }

        if (isPotionActive(Potion.digSlowdown))
        {
            return (oldswing ? 8 : 6) + (1 + getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2;
        }
        else
        {
            return (oldswing ? 8 : 6);
        }
    }

    /**
     * Swings the item the player is holding.
     */
    public void swingItem()
    {
        if (!field_82175_bq || field_82173_br >= func_82166_i() / 2 || field_82173_br < 0)
        {
            field_82173_br = -1;
            field_82175_bq = true;

            if (worldObj instanceof WorldServer)
            {
                ((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet18Animation(this, 1));
            }
        }
    }

    public boolean func_82171_bF()
    {
        return false;
    }
}
