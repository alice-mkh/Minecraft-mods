package net.minecraft.src;

import java.util.*;

public abstract class EntityLivingBase extends Entity
{
    public static boolean laddergaps = false;
    public static boolean newai = true;
    public static boolean rareloot = true;
    public static boolean oldloot = false;
    public static boolean jumpdelay = true;
    public static boolean survivaltest = false;
    public static boolean armorblocksall = false;
    public static boolean toolbreakanim = true;
    public static boolean enablescore = false;
    public static boolean oldswing = false;

    public static boolean carryover = true;

    protected float carryoverDamage;

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
        if (this instanceof EntityHorse){
            return true;
        }
        return newai;
    }

    protected int getDefaultScoreValue(){
        if (!enablescore){
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

    private static final UUID sprintingSpeedBoostModifierUUID;
    private static final AttributeModifier sprintingSpeedBoostModifier;
    private BaseAttributeMap attributeMap;
    private final CombatTracker _combatTracker = new CombatTracker(this);
    private final HashMap activePotionsMap = new HashMap();
    private final ItemStack previousEquipment[] = new ItemStack[5];

    /** Whether an arm swing is currently in progress. */
    public boolean isSwingInProgress;
    public int swingProgressInt;
    public int arrowHitTimer;
    public float prevHealth;

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
    public float prevSwingProgress;
    public float swingProgress;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;

    /**
     * Only relevant when limbYaw is not 0(the entity is moving). Influences where in its swing legs and arms currently
     * are.
     */
    public float limbSwing;
    public int maxHurtResistantTime;
    public float prevCameraPitch;
    public float cameraPitch;
    public float field_70769_ao;
    public float field_70770_ap;
    public float renderYawOffset;
    public float prevRenderYawOffset;

    /** Entity head rotation yaw */
    public float rotationYawHead;

    /** Entity head rotation yaw at previous tick */
    public float prevRotationYawHead;

    /**
     * A factor used to determine how far this entity will move each tick if it is jumping or falling.
     */
    public float jumpMovementFactor;

    /** The most recent player that has attacked this entity */
    protected EntityPlayer attackingPlayer;

    /**
     * Set to 60 when hit by the player or the player's wolf, then decrements. Used to determine whether the entity
     * should drop items on death.
     */
    protected int recentlyHit;

    /**
     * This gets set on entity death, but never used. Looks like a duplicate of isDead
     */
    protected boolean dead;

    /** Holds the living entity age, used to control the despawn. */
    protected int entityAge;
    protected float field_70768_au;
    protected float field_110154_aX;
    protected float field_70764_aw;
    protected float field_70763_ax;
    protected float field_70741_aB;

    /** The score value of the Mob, the amount of points the mob is worth. */
    protected int scoreValue;

    /**
     * Damage taken in the last hit. Mobs are resistant to damage less than this for a short time after taking damage.
     */
    protected float lastDamage;

    /** used to check whether entity is jumping. */
    protected boolean isJumping;
    public float moveStrafing;
    public float moveForward;
    protected float randomYawVelocity;

    /**
     * The number of updates over which the new position and rotation are to be applied to the entity.
     */
    protected int newPosRotationIncrements;

    /** The new X position to be applied to the entity. */
    protected double newPosX;

    /** The new Y position to be applied to the entity. */
    protected double newPosY;
    protected double newPosZ;

    /** The new yaw rotation to be applied to the entity. */
    protected double newRotationYaw;

    /** The new yaw rotation to be applied to the entity. */
    protected double newRotationPitch;

    /** Whether the DataWatcher needs to be updated with the active potions */
    private boolean potionsNeedUpdate;

    /** is only being set, has no uses as of MC 1.1 */
    private EntityLivingBase entityLivingToAttack;
    private int revengeTimer;
    private EntityLivingBase lastAttacker;

    /** Holds the value of ticksExisted when setLastAttacker was last called. */
    private int lastAttackerTime;

    /**
     * A factor used to determine how far this entity will move each tick if it is walking on land. Adjusted by speed,
     * and slipperiness of the current block.
     */
    private float landMovementFactor;

    /** Number of ticks since last jump */
    private int jumpTicks;
    private float field_110151_bq;

    public EntityLivingBase(World par1World)
    {
        super(par1World);
        maxHurtResistantTime = 20;
        jumpMovementFactor = 0.02F;
        potionsNeedUpdate = true;
        applyEntityAttributes();
        setHealth(getMaxHealth());
        preventEntitySpawning = true;
        field_70770_ap = (float)(Math.random() + 1.0D) * 0.01F;
        setPosition(posX, posY, posZ);
        field_70769_ao = (float)Math.random() * 12398F;
        rotationYaw = (float)(Math.random() * Math.PI * 2D);
        rotationYawHead = rotationYaw;
        stepHeight = 0.5F;
        scoreValue = getDefaultScoreValue();
    }

    protected void entityInit()
    {
        dataWatcher.addObject(7, Integer.valueOf(0));
        dataWatcher.addObject(8, Byte.valueOf((byte)0));
        dataWatcher.addObject(9, Byte.valueOf((byte)0));
        dataWatcher.addObject(6, Float.valueOf(1.0F));
    }

    protected void applyEntityAttributes()
    {
        getAttributeMap().func_111150_b(SharedMonsterAttributes.maxHealth);
        getAttributeMap().func_111150_b(SharedMonsterAttributes.knockbackResistance);
        getAttributeMap().func_111150_b(SharedMonsterAttributes.movementSpeed);

        if (!isAIEnabled() || !newai())
        {
            getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(0.10000000149011612D);
        }
    }

    /**
     * Takes in the distance the entity has fallen this tick and whether its on the ground to update the fall distance
     * and deal fall damage if landing on the ground.  Args: distanceFallenThisTick, onGround
     */
    protected void updateFallState(double par1, boolean par3)
    {
        if (!isInWater())
        {
            handleWaterMovement();
        }

        if (par3 && fallDistance > 0.0F)
        {
            int i = MathHelper.floor_double(posX);
            int j = MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset);
            int k = MathHelper.floor_double(posZ);
            int l = worldObj.getBlockId(i, j, k);

            if (l == 0)
            {
                int i1 = worldObj.blockGetRenderType(i, j - 1, k);

                if (i1 == 11 || i1 == 32 || i1 == 21)
                {
                    l = worldObj.getBlockId(i, j - 1, k);
                }
            }

            if (l > 0)
            {
                Block.blocksList[l].onFallenUpon(worldObj, i, j, k, this, fallDistance);
            }
        }

        super.updateFallState(par1, par3);
    }

    public boolean canBreatheUnderwater()
    {
        return false;
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        prevSwingProgress = swingProgress;
        super.onEntityUpdate();
        worldObj.theProfiler.startSection("livingEntityBaseTick");

        if (isEntityAlive() && isEntityInsideOpaqueBlock())
        {
            attackEntityFrom(DamageSource.inWall, 1.0F);
        }

        if (isImmuneToFire() || worldObj.isRemote)
        {
            extinguish();
        }

        boolean flag = (this instanceof EntityPlayer) && ((EntityPlayer)this).capabilities.disableDamage;

        if (isEntityAlive() && isInsideOfMaterial(Material.water))
        {
            if (!canBreatheUnderwater() && !isPotionActive(Potion.waterBreathing.id) && !flag)
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

                    attackEntityFrom(DamageSource.drown, 2.0F);
                }
            }

            extinguish();

            if (!worldObj.isRemote && isRiding() && (ridingEntity instanceof EntityLivingBase))
            {
                mountEntity(null);
            }
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

        if (getHealth() <= 0.0F)
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

        if (lastAttacker != null && !lastAttacker.isEntityAlive())
        {
            lastAttacker = null;
        }

        if (entityLivingToAttack != null && !entityLivingToAttack.isEntityAlive())
        {
            setRevengeTarget(null);
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
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return false;
    }

    /**
     * handles entity death timer, experience orb and particle creation
     */
    protected void onDeathUpdate()
    {
        deathTime++;

        if (deathTime == 20)
        {
            if (!worldObj.isRemote && (recentlyHit > 0 || isPlayer()) && !isChild() && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
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
        return 0;
    }

    /**
     * Only use is to identify if class is an instance of player for experience dropping
     */
    protected boolean isPlayer()
    {
        return false;
    }

    public Random getRNG()
    {
        return rand;
    }

    public EntityLivingBase getAITarget()
    {
        return entityLivingToAttack;
    }

    public int func_142015_aE()
    {
        return revengeTimer;
    }

    public void setRevengeTarget(EntityLivingBase par1EntityLivingBase)
    {
        entityLivingToAttack = par1EntityLivingBase;
        revengeTimer = ticksExisted;
    }

    public EntityLivingBase getLastAttacker()
    {
        return lastAttacker;
    }

    public int getLastAttackerTime()
    {
        return lastAttackerTime;
    }

    public void setLastAttacker(Entity par1Entity)
    {
        if (par1Entity instanceof EntityLivingBase)
        {
            lastAttacker = (EntityLivingBase)par1Entity;
        }
        else
        {
            lastAttacker = null;
        }

        lastAttackerTime = ticksExisted;
    }

    public int getAge()
    {
        return entityAge;
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        par1NBTTagCompound.setFloat("HealF", getHealth());
        par1NBTTagCompound.setShort("Health", (short)(int)Math.ceil(getHealth()));
        par1NBTTagCompound.setShort("HurtTime", (short)hurtTime);
        par1NBTTagCompound.setShort("DeathTime", (short)deathTime);
        par1NBTTagCompound.setShort("AttackTime", (short)attackTime);
        par1NBTTagCompound.setFloat("AbsorptionAmount", getAbsorptionAmount());
        ItemStack aitemstack[] = getLastActiveItems();
        int i = aitemstack.length;

        for (int j = 0; j < i; j++)
        {
            ItemStack itemstack = aitemstack[j];

            if (itemstack != null)
            {
                attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
            }
        }

        par1NBTTagCompound.setTag("Attributes", SharedMonsterAttributes.func_111257_a(getAttributeMap()));
        aitemstack = getLastActiveItems();
        i = aitemstack.length;

        for (int k = 0; k < i; k++)
        {
            ItemStack itemstack1 = aitemstack[k];

            if (itemstack1 != null)
            {
                attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
            }
        }

        if (!activePotionsMap.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();
            PotionEffect potioneffect;

            for (Iterator iterator = activePotionsMap.values().iterator(); iterator.hasNext(); nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound())))
            {
                potioneffect = (PotionEffect)iterator.next();
            }

            par1NBTTagCompound.setTag("ActiveEffects", nbttaglist);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        setAbsorptionAmount(par1NBTTagCompound.getFloat("AbsorptionAmount"));

        if (par1NBTTagCompound.hasKey("Attributes") && worldObj != null && !worldObj.isRemote)
        {
            SharedMonsterAttributes.func_111260_a(getAttributeMap(), par1NBTTagCompound.getTagList("Attributes"), worldObj != null ? worldObj.getWorldLogAgent() : null);
        }

        if (par1NBTTagCompound.hasKey("ActiveEffects"))
        {
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("ActiveEffects");

            for (int i = 0; i < nbttaglist.tagCount(); i++)
            {
                NBTTagCompound nbttagcompound = (NBTTagCompound)nbttaglist.tagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
                activePotionsMap.put(Integer.valueOf(potioneffect.getPotionID()), potioneffect);
            }
        }

        if (par1NBTTagCompound.hasKey("HealF"))
        {
            setHealth(par1NBTTagCompound.getFloat("HealF"));
        }
        else
        {
            NBTBase nbtbase = par1NBTTagCompound.getTag("Health");

            if (nbtbase == null)
            {
                setHealth(getMaxHealth());
            }
            else if (nbtbase.getId() == 5)
            {
                setHealth(((NBTTagFloat)nbtbase).data);
            }
            else if (nbtbase.getId() == 2)
            {
                setHealth(((NBTTagShort)nbtbase).data);
            }
        }

        hurtTime = par1NBTTagCompound.getShort("HurtTime");
        deathTime = par1NBTTagCompound.getShort("DeathTime");
        attackTime = par1NBTTagCompound.getShort("AttackTime");
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

            if (!potioneffect.onUpdate(this))
            {
                if (!worldObj.isRemote)
                {
                    iterator.remove();
                    onFinishedPotionEffect(potioneffect);
                }
            }
            else if (potioneffect.getDuration() % 600 == 0)
            {
                onChangedPotionEffect(potioneffect, false);
            }
        }
        while (true);

        if (potionsNeedUpdate)
        {
            if (!worldObj.isRemote)
            {
                if (activePotionsMap.isEmpty())
                {
                    dataWatcher.updateObject(8, Byte.valueOf((byte)0));
                    dataWatcher.updateObject(7, Integer.valueOf(0));
                    setInvisible(false);
                }
                else
                {
                    int i = PotionHelper.calcPotionLiquidColor(activePotionsMap.values());
                    dataWatcher.updateObject(8, Byte.valueOf(((byte)(PotionHelper.func_82817_b(activePotionsMap.values()) ? 1 : 0))));
                    dataWatcher.updateObject(7, Integer.valueOf(i));
                    setInvisible(isPotionActive(Potion.invisibility.id));
                }
            }

            potionsNeedUpdate = false;
        }

        int j = dataWatcher.getWatchableObjectInt(7);
        boolean flag = dataWatcher.getWatchableObjectByte(8) > 0;

        if (j > 0)
        {
            boolean flag1 = false;

            if (!isInvisible())
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

    public boolean isPotionActive(int par1)
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
            onChangedPotionEffect((PotionEffect)activePotionsMap.get(Integer.valueOf(par1PotionEffect.getPotionID())), true);
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
     * Remove the speified potion effect from this entity.
     */
    public void removePotionEffectClient(int par1)
    {
        activePotionsMap.remove(Integer.valueOf(par1));
    }

    /**
     * Remove the specified potion effect from this entity.
     */
    public void removePotionEffect(int par1)
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

        if (!worldObj.isRemote)
        {
            Potion.potionTypes[par1PotionEffect.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), par1PotionEffect.getAmplifier());
        }
    }

    protected void onChangedPotionEffect(PotionEffect par1PotionEffect, boolean par2)
    {
        potionsNeedUpdate = true;

        if (par2 && !worldObj.isRemote)
        {
            Potion.potionTypes[par1PotionEffect.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), par1PotionEffect.getAmplifier());
            Potion.potionTypes[par1PotionEffect.getPotionID()].applyAttributesModifiersToEntity(this, getAttributeMap(), par1PotionEffect.getAmplifier());
        }
    }

    protected void onFinishedPotionEffect(PotionEffect par1PotionEffect)
    {
        potionsNeedUpdate = true;

        if (!worldObj.isRemote)
        {
            Potion.potionTypes[par1PotionEffect.getPotionID()].removeAttributesModifiersFromEntity(this, getAttributeMap(), par1PotionEffect.getAmplifier());
        }
    }

    /**
     * Heal living entity (param: amount of half-hearts)
     */
    public void heal(float par1)
    {
        float f = getHealth();

        if (f > 0.0F)
        {
            setHealth(f + par1);
        }
    }

    public final float getHealth()
    {
        return dataWatcher.getWatchableObjectFloat(6);
    }

    public void setHealth(float par1)
    {
        dataWatcher.updateObject(6, Float.valueOf(MathHelper.clamp_float(par1, 0.0F, getMaxHealth())));
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

        if (worldObj.isRemote)
        {
            return false;
        }

        entityAge = 0;

        if (getHealth() <= 0.0F)
        {
            return false;
        }

        if (par1DamageSource.isFireDamage() && isPotionActive(Potion.fireResistance))
        {
            return false;
        }

        if ((par1DamageSource == DamageSource.anvil || par1DamageSource == DamageSource.fallingBlock) && getCurrentItemOrArmor(4) != null)
        {
            getCurrentItemOrArmor(4).damageItem((int)(par2 * 4F + rand.nextFloat() * par2 * 2.0F), this);
            par2 *= 0.75F;
        }

        limbSwingAmount = 1.5F;
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
            prevHealth = getHealth();
            hurtResistantTime = maxHurtResistantTime;
            damageEntity(par1DamageSource, par2);
            hurtTime = maxHurtTime = 10;
        }

        attackedAtYaw = 0.0F;
        Entity entity = par1DamageSource.getEntity();

        if (entity != null)
        {
            if (entity instanceof EntityLivingBase)
            {
                setRevengeTarget((EntityLivingBase)entity);
            }

            if (entity instanceof EntityPlayer)
            {
                recentlyHit = 100;
                attackingPlayer = (EntityPlayer)entity;
            }
            else if (entity instanceof EntityWolf)
            {
                EntityWolf entitywolf = (EntityWolf)entity;

                if (entitywolf.isTamed())
                {
                    recentlyHit = 100;
                    attackingPlayer = null;
                }
            }
        }

        if (flag)
        {
            worldObj.setEntityState(this, (byte)2);

            if (par1DamageSource != DamageSource.drown)
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

        if (getHealth() <= 0.0F)
        {
            if (flag)
            {
                playSound(getDeathSound(), getSoundVolume(), getSoundPitch());
            }

            onDeath(par1DamageSource);
        }
        else if (flag)
        {
            playSound(getHurtSound(), getSoundVolume(), getSoundPitch());
        }

        return true;
    }

    /**
     * Renders broken item particles using the given ItemStack
     */
    public void renderBrokenItemStack(ItemStack par1ItemStack)
    {
        playSound("random.break", 0.8F, 0.8F + worldObj.rand.nextFloat() * 0.4F);

        if (!toolbreakanim){
            return;
        }

        for (int i = 0; i < 5; i++)
        {
            Vec3 vec3 = worldObj.getWorldVec3Pool().getVecFromPool(((double)rand.nextFloat() - 0.5D) * 0.10000000000000001D, Math.random() * 0.10000000000000001D + 0.10000000000000001D, 0.0D);
            vec3.rotateAroundX((-rotationPitch * (float)Math.PI) / 180F);
            vec3.rotateAroundY((-rotationYaw * (float)Math.PI) / 180F);
            Vec3 vec3_1 = worldObj.getWorldVec3Pool().getVecFromPool(((double)rand.nextFloat() - 0.5D) * 0.29999999999999999D, (double)(-rand.nextFloat()) * 0.59999999999999998D - 0.29999999999999999D, 0.59999999999999998D);
            vec3_1.rotateAroundX((-rotationPitch * (float)Math.PI) / 180F);
            vec3_1.rotateAroundY((-rotationYaw * (float)Math.PI) / 180F);
            vec3_1 = vec3_1.addVector(posX, posY + (double)getEyeHeight(), posZ);
            worldObj.spawnParticle((new StringBuilder()).append("iconcrack_").append(par1ItemStack.getItem().itemID).toString(), vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, vec3.xCoord, vec3.yCoord + 0.050000000000000003D, vec3.zCoord);
        }
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource)
    {
        Entity entity = par1DamageSource.getEntity();
        EntityLivingBase entitylivingbase = func_94060_bK();

        if (scoreValue >= 0 && entitylivingbase != null)
        {
            entitylivingbase.addToPlayerScore(this, scoreValue);
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
                i = EnchantmentHelper.getLootingModifier((EntityLivingBase)entity);
            }

            if (!isChild() && worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot"))
            {
                if (survivaltest){
                    if (this instanceof EntitySheep || this instanceof EntityPig){
                        dropFewItemsOld(recentlyHit > 0, i, Block.mushroomBrown.blockID);
                    }
                }else if (oldloot){
                    if (this instanceof EntityPigZombie){
                        dropFewItemsOld(recentlyHit > 0, i, Item.porkCooked.itemID);
                    }else if (this instanceof EntityChicken || this instanceof EntityZombie){
                        dropFewItemsOld(recentlyHit > 0, i, Item.feather.itemID);
                    }else if (this instanceof EntityCow){
                        dropFewItemsOld(recentlyHit > 0, i, Item.leather.itemID);
                    }else if (this instanceof EntitySpider){
                        dropFewItemsOld(recentlyHit > 0, i, Item.silk.itemID);
                    }else if (this instanceof EntityPig){
                        dropFewItemsOld(recentlyHit > 0, i, ((EntityLiving)this).getDropItemId());
                    }else{
                        dropFewItems(recentlyHit > 0, i);
                    }
                }else{
                    dropFewItems(recentlyHit > 0, i);
                }

                if (recentlyHit > 0 && rareloot)
                {
                    dropEquipment(recentlyHit > 0, i);
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

    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean flag, int i)
    {
    }

    /**
     * knocks back this entity
     */
    public void knockBack(Entity par1Entity, float par2, double par3, double par5)
    {
        if (rand.nextDouble() < getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue())
        {
            return;
        }

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

    protected void dropRareDrop(int i)
    {
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
     */
    protected void dropFewItems(boolean flag, int i)
    {
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
     * Checks whether target entity is alive.
     */
    public boolean isEntityAlive()
    {
        return !isDead && getHealth() > 0.0F;
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    protected void fall(float par1)
    {
        super.fall(par1);
        PotionEffect potioneffect = getActivePotionEffect(Potion.jump);
        float f = potioneffect == null ? 0.0F : potioneffect.getAmplifier() + 1;
        int i = MathHelper.ceiling_float_int(par1 - 3F - f);

        if (i > 0)
        {
            if (i > 4)
            {
                playSound("damage.fallbig", 1.0F, 1.0F);
            }
            else
            {
                playSound("damage.fallsmall", 1.0F, 1.0F);
            }

            attackEntityFrom(DamageSource.fall, i);
            int j = worldObj.getBlockId(MathHelper.floor_double(posX), MathHelper.floor_double(posY - 0.20000000298023224D - (double)yOffset), MathHelper.floor_double(posZ));

            if (j > 0)
            {
                StepSound stepsound = Block.blocksList[j].stepSound;
                playSound(stepsound.getStepSound(), stepsound.getVolume() * 0.5F, stepsound.getPitch() * 0.75F);
            }
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

    protected void damageArmor(float f)
    {
    }

    /**
     * Reduces damage, depending on armor
     */
    protected float applyArmorCalculations(DamageSource par1DamageSource, float par2)
    {
        if (!par1DamageSource.isUnblockable() || armorblocksall)
        {
            int i = 25 - getTotalArmorValue();
            float f = par2 * (float)i + carryoverDamage;
            damageArmor(par2);
            par2 = f / 25F;
            carryoverDamage = f % 25F;
        }

        return par2;
    }

    /**
     * Reduces damage, depending on potions
     */
    protected float applyPotionDamageCalculations(DamageSource par1DamageSource, float par2)
    {
        if (this instanceof EntityZombie)
        {
            par2 = par2;
        }

        if (isPotionActive(Potion.resistance) && par1DamageSource != DamageSource.outOfWorld)
        {
            int i = (getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
            int k = 25 - i;
            float f = par2 * (float)k + carryoverDamage;
            par2 = f / 25F;
            if (carryover){
                carryoverDamage = f % 25F;
            }
        }

        if (par2 <= 0.0F)
        {
            return 0.0F;
        }

        int j = EnchantmentHelper.getEnchantmentModifierDamage(getLastActiveItems(), par1DamageSource);

        if (j > 20)
        {
            j = 20;
        }

        if (j > 0 && j <= 20)
        {
            int l = 25 - j;
            float f1 = par2 * (float)l + carryoverDamage;
            par2 = f1 / 25F;
            if (carryover){
                carryoverDamage = f1 % 25;
            }
        }

        return par2;
    }

    /**
     * Deals damage to the entity. If its a EntityPlayer then will take damage from the armor first and then health
     * second with the reduced value. Args: damageAmount
     */
    protected void damageEntity(DamageSource par1DamageSource, float par2)
    {
        if (isEntityInvulnerable())
        {
            return;
        }

        par2 = applyArmorCalculations(par1DamageSource, par2);
        par2 = applyPotionDamageCalculations(par1DamageSource, par2);
        float f = par2;
        par2 = Math.max(par2 - getAbsorptionAmount(), 0.0F);
        setAbsorptionAmount(getAbsorptionAmount() - (f - par2));

        if (par2 == 0.0F)
        {
            return;
        }
        else
        {
            float f1 = getHealth();
            setHealth(f1 - par2);
            func_110142_aN().func_94547_a(par1DamageSource, f1, par2);
            setAbsorptionAmount(getAbsorptionAmount() - par2);
            return;
        }
    }

    public CombatTracker func_110142_aN()
    {
        return _combatTracker;
    }

    public EntityLivingBase func_94060_bK()
    {
        if (_combatTracker.func_94550_c() != null)
        {
            return _combatTracker.func_94550_c();
        }

        if (attackingPlayer != null)
        {
            return attackingPlayer;
        }

        if (entityLivingToAttack != null)
        {
            return entityLivingToAttack;
        }
        else
        {
            return null;
        }
    }

    public final float getMaxHealth()
    {
        return (float)getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
    }

    /**
     * counts the amount of arrows stuck in the entity. getting hit by arrows increases this, used in rendering
     */
    public final int getArrowCountInEntity()
    {
        return dataWatcher.getWatchableObjectByte(9);
    }

    /**
     * sets the amount of arrows stuck in the entity. used for rendering those
     */
    public final void setArrowCountInEntity(int par1)
    {
        dataWatcher.updateObject(9, Byte.valueOf((byte)par1));
    }

    /**
     * Returns an integer indicating the end point of the swing animation, used by {@link #swingProgress} to provide a
     * progress indicator. Takes dig speed enchantments into account.
     */
    private int getArmSwingAnimationEnd()
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
        if (!isSwingInProgress || swingProgressInt >= getArmSwingAnimationEnd() / 2 || swingProgressInt < 0)
        {
            swingProgressInt = -1;
            isSwingInProgress = true;

            if (worldObj instanceof WorldServer)
            {
                ((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet18Animation(this, 1));
            }
        }
    }

    public void handleHealthUpdate(byte par1)
    {
        if (par1 == 2)
        {
            limbSwingAmount = 1.5F;
            hurtResistantTime = maxHurtResistantTime;
            hurtTime = maxHurtTime = 10;
            attackedAtYaw = 0.0F;
            playSound(getHurtSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            attackEntityFrom(DamageSource.generic, 0.0F);
        }
        else if (par1 == 3)
        {
            playSound(getDeathSound(), getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
            setHealth(0.0F);
            onDeath(DamageSource.generic);
        }
        else
        {
            super.handleHealthUpdate(par1);
        }
    }

    /**
     * sets the dead flag. Used when you fall off the bottom of the world.
     */
    protected void kill()
    {
        attackEntityFrom(DamageSource.outOfWorld, 4F);
    }

    /**
     * Updates the arm swing progress counters and animation progress
     */
    protected void updateArmSwingProgress()
    {
        int i = getArmSwingAnimationEnd();

        if (isSwingInProgress)
        {
            swingProgressInt++;

            if (swingProgressInt >= i)
            {
                swingProgressInt = 0;
                isSwingInProgress = false;
            }
        }
        else
        {
            swingProgressInt = 0;
        }

        swingProgress = (float)swingProgressInt / (float)i;
    }

    public AttributeInstance getEntityAttribute(Attribute par1Attribute)
    {
        return getAttributeMap().getAttributeInstance(par1Attribute);
    }

    public BaseAttributeMap getAttributeMap()
    {
        if (attributeMap == null)
        {
            attributeMap = new ServersideAttributeMap();
        }

        return attributeMap;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public abstract ItemStack getHeldItem();

    /**
     * 0 = item, 1-n is armor
     */
    public abstract ItemStack getCurrentItemOrArmor(int i);

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public abstract void setCurrentItemOrArmor(int i, ItemStack itemstack);

    /**
     * Set sprinting switch for Entity.
     */
    public void setSprinting(boolean par1)
    {
        super.setSprinting(par1);
        AttributeInstance attributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);

        if (attributeinstance.getModifier(sprintingSpeedBoostModifierUUID) != null)
        {
            attributeinstance.removeModifier(sprintingSpeedBoostModifier);
        }

        if (par1)
        {
            attributeinstance.applyModifier(sprintingSpeedBoostModifier);
        }
    }

    public abstract ItemStack[] getLastActiveItems();

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 1.0F;
    }

    /**
     * Gets the pitch of living sounds in living entities.
     */
    protected float getSoundPitch()
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
     * Dead and sleeping entities cannot move
     */
    protected boolean isMovementBlocked()
    {
        return getHealth() <= 0.0F;
    }

    /**
     * Move the entity to the coordinates informed, but keep yaw/pitch values.
     */
    public void setPositionAndUpdate(double par1, double par3, double par5)
    {
        setLocationAndAngles(par1, par3, par5, rotationYaw, rotationPitch);
    }

    /**
     * Moves the entity to a position out of the way of its mount.
     */
    public void dismountEntity(Entity par1Entity)
    {
        double d = par1Entity.posX;
        double d1 = par1Entity.boundingBox.minY + (double)par1Entity.height;
        double d2 = par1Entity.posZ;

        for (double d3 = -1.5D; d3 < 2D; d3 += 1.5D)
        {
            for (double d4 = -1.5D; d4 < 2D; d4 += 1.5D)
            {
                if (d3 == 0.0D && d4 == 0.0D)
                {
                    continue;
                }

                int i = (int)(posX + d3);
                int j = (int)(posZ + d4);
                AxisAlignedBB axisalignedbb = boundingBox.getOffsetBoundingBox(d3, 1.0D, d4);

                if (!worldObj.getCollidingBlockBounds(axisalignedbb).isEmpty())
                {
                    continue;
                }

                if (worldObj.doesBlockHaveSolidTopSurface(i, (int)posY, j))
                {
                    setPositionAndUpdate(posX + d3, posY + 1.0D, posZ + d4);
                    return;
                }

                if (worldObj.doesBlockHaveSolidTopSurface(i, (int)posY - 1, j) || worldObj.getBlockMaterial(i, (int)posY - 1, j) == Material.water)
                {
                    d = posX + d3;
                    d1 = posY + 1.0D;
                    d2 = posZ + d4;
                }
            }
        }

        setPositionAndUpdate(d, d1, d2);
    }

    public boolean getAlwaysRenderNameTagForRender()
    {
        return false;
    }

    /**
     * Gets the Icon Index of the item currently held
     */
    public Icon getItemIcon(ItemStack par1ItemStack, int par2)
    {
        return par1ItemStack.getIconIndex();
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
                f2 = getAIMoveSpeed() * f1;
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

            if (!worldObj.isRemote || worldObj.blockExists((int)posX, 0, (int)posZ) && worldObj.getChunkFromBlockCoords((int)posX, (int)posZ).isChunkLoaded)
            {
                motionY -= 0.080000000000000002D;
            }
            else if (posY > 0.0D)
            {
                motionY = -0.10000000000000001D;
            }
            else
            {
                motionY = 0.0D;
            }

            motionY *= 0.98000001907348633D;
            motionX *= f;
            motionZ *= f;
        }

        prevLimbSwingAmount = limbSwingAmount;
        double d2 = posX - prevPosX;
        double d3 = posZ - prevPosZ;
        float f4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3) * 4F;

        if (f4 > 1.0F)
        {
            f4 = 1.0F;
        }

        limbSwingAmount += (f4 - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return false;
    }

    /**
     * the movespeed used for the new AI system
     */
    public float getAIMoveSpeed()
    {
        if (isAIEnabled() && newai())
        {
            return landMovementFactor;
        }
        else
        {
            return 0.1F;
        }
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float par1)
    {
        landMovementFactor = par1;
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        setLastAttacker(par1Entity);
        return false;
    }

    /**
     * Returns whether player is sleeping or not
     */
    public boolean isPlayerSleeping()
    {
        return false;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!worldObj.isRemote)
        {
            int i = getArrowCountInEntity();

            if (i > 0)
            {
                if (arrowHitTimer <= 0)
                {
                    arrowHitTimer = 20 * (30 - i);
                }

                arrowHitTimer--;

                if (arrowHitTimer <= 0)
                {
                    setArrowCountInEntity(i - 1);
                }
            }

            for (int j = 0; j < 5; j++)
            {
                ItemStack itemstack = previousEquipment[j];
                ItemStack itemstack1 = getCurrentItemOrArmor(j);

                if (ItemStack.areItemStacksEqual(itemstack1, itemstack))
                {
                    continue;
                }

                if (worldObj instanceof WorldServer){
                    ((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(entityId, j, itemstack1));
                }

                if (itemstack != null)
                {
                    attributeMap.removeAttributeModifiers(itemstack.getAttributeModifiers());
                }

                if (itemstack1 != null)
                {
                    attributeMap.applyAttributeModifiers(itemstack1.getAttributeModifiers());
                }

                previousEquipment[j] = itemstack1 != null ? itemstack1.copy() : null;
            }
        }

        onLivingUpdate();
        double d = posX - prevPosX;
        double d1 = posZ - prevPosZ;
        float f = (float)(d * d + d1 * d1);
        float f1 = renderYawOffset;
        float f2 = 0.0F;
        field_70768_au = field_110154_aX;
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

        field_110154_aX += (f3 - field_110154_aX) * 0.3F;
        worldObj.theProfiler.startSection("headTurn");
        f2 = func_110146_f(f1, f2);
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

    protected float func_110146_f(float par1, float par2)
    {
        float f = MathHelper.wrapAngleTo180_float(par1 - renderYawOffset);
        renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapAngleTo180_float(rotationYaw - renderYawOffset);
        boolean flag = f1 < -90F || f1 >= 90F;

        if (f1 < -75F)
        {
            f1 = -75F;
        }

        if (f1 >= 75F)
        {
            f1 = 75F;
        }

        renderYawOffset = rotationYaw - f1;

        if (f1 * f1 > 2500F)
        {
            renderYawOffset += f1 * 0.2F;
        }

        if (flag)
        {
            par2 *= -1F;
        }

        return par2;
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
        else if (!isClientWorld())
        {
            motionX *= 0.97999999999999998D;
            motionY *= 0.97999999999999998D;
            motionZ *= 0.97999999999999998D;
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
        moveEntityWithHeading(moveStrafing, moveForward);
        worldObj.theProfiler.endSection();
        worldObj.theProfiler.startSection("push");

        if (!worldObj.isRemote)
        {
            collideWithNearbyEntities();
        }

        worldObj.theProfiler.endSection();
    }

    protected void updateAITasks()
    {
    }

    protected void collideWithNearbyEntities()
    {
        List list = worldObj.getEntitiesWithinAABBExcludingEntity(this, boundingBox.expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

        if (list != null && !list.isEmpty())
        {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);

                if (entity.canBePushed())
                {
                    collideWithEntity(entity);
                }
            }
        }
    }

    protected void collideWithEntity(Entity par1Entity)
    {
        par1Entity.applyEntityCollision(this);
    }

    /**
     * Handles updating while being ridden by an entity
     */
    public void updateRidden()
    {
        super.updateRidden();
        field_70768_au = field_110154_aX;
        field_110154_aX = 0.0F;
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
     * main AI tick function, replaces updateEntityActionState
     */
    protected void updateAITick()
    {
    }

    protected void updateEntityActionState()
    {
        entityAge++;
    }

    public void setJumping(boolean par1)
    {
        isJumping = par1;
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

    /**
     * returns true if the entity provided in the argument can be seen. (Raytrace)
     */
    public boolean canEntityBeSeen(Entity par1Entity)
    {
        return worldObj.clip(worldObj.getWorldVec3Pool().getVecFromPool(posX, posY + (double)getEyeHeight(), posZ), worldObj.getWorldVec3Pool().getVecFromPool(par1Entity.posX, par1Entity.posY + (double)par1Entity.getEyeHeight(), par1Entity.posZ)) == null;
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
            return worldObj.getWorldVec3Pool().getVecFromPool(f2 * f4, f6, f * f4);
        }
        else
        {
            float f1 = prevRotationPitch + (rotationPitch - prevRotationPitch) * par1;
            float f3 = prevRotationYaw + (rotationYaw - prevRotationYaw) * par1;
            float f5 = MathHelper.cos(-f3 * 0.01745329F - (float)Math.PI);
            float f7 = MathHelper.sin(-f3 * 0.01745329F - (float)Math.PI);
            float f8 = -MathHelper.cos(-f1 * 0.01745329F);
            float f9 = MathHelper.sin(-f1 * 0.01745329F);
            return worldObj.getWorldVec3Pool().getVecFromPool(f7 * f8, f9, f5 * f8);
        }
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
            return worldObj.getWorldVec3Pool().getVecFromPool(posX, posY, posZ);
        }
        else
        {
            double d = prevPosX + (posX - prevPosX) * (double)par1;
            double d1 = prevPosY + (posY - prevPosY) * (double)par1;
            double d2 = prevPosZ + (posZ - prevPosZ) * (double)par1;
            return worldObj.getWorldVec3Pool().getVecFromPool(d, d1, d2);
        }
    }

    /**
     * Performs a ray trace for the distance specified and using the partial tick time. Args: distance, partialTickTime
     */
    public MovingObjectPosition rayTrace(double par1, float par3)
    {
        Vec3 vec3 = getPosition(par3);
        Vec3 vec3_1 = getLook(par3);
        Vec3 vec3_2 = vec3.addVector(vec3_1.xCoord * par1, vec3_1.yCoord * par1, vec3_1.zCoord * par1);
        return worldObj.clip(vec3, vec3_2);
    }

    /**
     * Returns whether the entity is in a local (client) world
     */
    public boolean isClientWorld()
    {
        return !worldObj.isRemote;
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
     * Sets that this entity has been attacked.
     */
    protected void setBeenAttacked()
    {
        velocityChanged = rand.nextDouble() >= getEntityAttribute(SharedMonsterAttributes.knockbackResistance).getAttributeValue();
    }

    public float getRotationYawHead()
    {
        return rotationYawHead;
    }

    /**
     * Sets the head's yaw rotation of the entity.
     */
    public void setRotationYawHead(float par1)
    {
        rotationYawHead = par1;
    }

    public float getAbsorptionAmount()
    {
        return field_110151_bq;
    }

    public void setAbsorptionAmount(float par1)
    {
        if (par1 < 0.0F)
        {
            par1 = 0.0F;
        }

        field_110151_bq = par1;
    }

    public Team getTeam()
    {
        return null;
    }

    public boolean isOnSameTeam(EntityLivingBase par1EntityLivingBase)
    {
        return isOnTeam(par1EntityLivingBase.getTeam());
    }

    /**
     * Returns true if the entity is on a specific team.
     */
    public boolean isOnTeam(Team par1Team)
    {
        if (getTeam() != null)
        {
            return getTeam().isSameTeam(par1Team);
        }
        else
        {
            return false;
        }
    }

    static
    {
        sprintingSpeedBoostModifierUUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
        sprintingSpeedBoostModifier = (new AttributeModifier(sprintingSpeedBoostModifierUUID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    }
}
