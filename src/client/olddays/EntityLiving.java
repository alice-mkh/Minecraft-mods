package net.minecraft.src;

import java.util.*;

public abstract class EntityLiving extends EntityLivingBase
{
    public static boolean indevai = false;
    public static int nonewmobs = 13;
    public static boolean oldrange = false;
    public static boolean fastzombies = false;

    public double getRealMoveSpeed(){
        double base = func_110148_a(SharedMonsterAttributes.field_111263_d).func_111126_e();
        if (isAIEnabled() && !newai()){
            if (this instanceof EntityAnimal ||
                this instanceof EntityCreeper ||
                this instanceof EntitySkeleton){
                if (this instanceof EntityWolf){
                    base *= 3.6D;
                }else{
                    base *= 2.8D;
                }
            }
            if (this instanceof EntityZombie){
                if (this instanceof EntityPigZombie){
                    base *= 2D;
                }else{
                    base *= 2.17391304348D;
                }
            }
        }
        if (fastzombies && this instanceof EntityZombie && !(this instanceof EntityPigZombie)){
            base *= 2.0D;
        }
        return base;
    }

    public static boolean allow(String id, int dim){
        if (dim!=0){
            return true;
        }
        if (nonewmobs<13 && id=="Bat"){
            return false;
        }
        if (nonewmobs<12 && id=="Ozelot"){
            return false;
        }
        if (nonewmobs<11 && (id=="MushroomCow" || id=="Villager")){
            return false;
        }
        if (nonewmobs<10 && id=="Enderman"){
            return false;
        }
        if (nonewmobs<8 && id=="Wolf"){
            return false;
        }
        if (nonewmobs<7 && id=="Squid"){
            return false;
        }
        if (nonewmobs<6 && id=="Chicken"){
            return false;
        }
        if (nonewmobs<5 && id=="Slime"){
            return false;
        }
        if (nonewmobs<4 && id=="Cow"){
            return false;
        }
        if (nonewmobs<3 && (id=="Pig" || id=="Sheep" || id=="Zombie" || id=="Skeleton" || id=="Spider" || id=="Creeper")){
            return false;
        }
        if ((nonewmobs != 1 && id == "Rana") || (nonewmobs != 2 && id == "Steve")){
            return false;
        }
        return true;
    }

    /** Number of ticks since this EntityLiving last produced its sound */
    public int livingSoundTime;

    /** The experience points the Entity gives. */
    protected int experienceValue;
    private EntityLookHelper lookHelper;
    private EntityMoveHelper moveHelper;

    /** Entity jumping helper */
    private EntityJumpHelper jumpHelper;
    private EntityBodyHelper bodyHelper;
    private PathNavigate navigator;
    protected final EntityAITasks tasks;
    protected final EntityAITasks targetTasks;

    /** The active target the Task system uses for tracking */
    private EntityLivingBase attackTarget;
    private EntitySenses senses;
    private ItemStack equipment[];
    protected float equipmentDropChances[];

    /** Whether this entity can pick up items from the ground. */
    private boolean canPickUpLoot;

    /** Whether this entity should NOT despawn. */
    private boolean persistenceRequired;
    protected float defaultPitch;

    /** This entity's current target. */
    private Entity currentTarget;

    /** How long to keep a specific target entity */
    protected int numTicksToChaseTarget;
    private boolean field_110169_bv;
    private Entity field_110168_bw;
    private NBTTagCompound field_110170_bx;

    public EntityLiving(World par1World)
    {
        super(par1World);
        equipment = new ItemStack[5];
        equipmentDropChances = new float[5];
        tasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
        targetTasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
        lookHelper = new EntityLookHelper(this);
        moveHelper = new EntityMoveHelper(this);
        jumpHelper = new EntityJumpHelper(this);
        bodyHelper = new EntityBodyHelper(this);
        navigator = new PathNavigate(this, par1World);
        senses = new EntitySenses(this);

        for (int i = 0; i < equipmentDropChances.length; i++)
        {
            equipmentDropChances[i] = 0.085F;
        }
    }

    protected void func_110147_ax()
    {
        super.func_110147_ax();
        func_110140_aT().func_111150_b(SharedMonsterAttributes.field_111265_b).func_111128_a(16D);
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

    /**
     * Gets the active target the Task system uses for tracking
     */
    public EntityLivingBase getAttackTarget()
    {
        return attackTarget;
    }

    /**
     * Sets the active target the Task system uses for tracking
     */
    public void setAttackTarget(EntityLivingBase par1EntityLivingBase)
    {
        attackTarget = par1EntityLivingBase;
    }

    /**
     * Returns true if this entity can attack entities of the specified class.
     */
    public boolean canAttackClass(Class par1Class)
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

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(11, Byte.valueOf((byte)0));
        dataWatcher.addObject(10, "");
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
            playSound(s, getSoundVolume(), getSoundPitch());
        }
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        worldObj.theProfiler.startSection("mobBaseTick");

        if (isEntityAlive() && rand.nextInt(1000) < livingSoundTime++)
        {
            livingSoundTime = -getTalkInterval();
            playLivingSound();
        }

        worldObj.theProfiler.endSection();
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer par1EntityPlayer)
    {
        if (experienceValue > 0)
        {
            int i = experienceValue;
            ItemStack aitemstack[] = getLastActiveItems();

            for (int j = 0; j < aitemstack.length; j++)
            {
                if (aitemstack[j] != null && equipmentDropChances[j] <= 1.0F)
                {
                    i += 1 + rand.nextInt(3);
                }
            }

            return i;
        }
        else
        {
            return experienceValue;
        }
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
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        super.onUpdate();

        if (!worldObj.isRemote)
        {
            func_110159_bB();
        }
    }

    protected float func_110146_f(float par1, float par2)
    {
        if (isAIEnabled() && newai())
        {
            bodyHelper.func_75664_a();
            return par2;
        }
        else
        {
            return super.func_110146_f(par1, par2);
        }
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return null;
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return 0;
    }

    /**
     * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
     * par2 - Level of Looting used to kill this mob.
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
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("CanPickUpLoot", canPickUpLoot());
        par1NBTTagCompound.setBoolean("PersistenceRequired", persistenceRequired);
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < equipment.length; i++)
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            if (equipment[i] != null)
            {
                equipment[i].writeToNBT(nbttagcompound);
            }

            nbttaglist.appendTag(nbttagcompound);
        }

        par1NBTTagCompound.setTag("Equipment", nbttaglist);
        NBTTagList nbttaglist1 = new NBTTagList();

        for (int j = 0; j < equipmentDropChances.length; j++)
        {
            nbttaglist1.appendTag(new NBTTagFloat((new StringBuilder()).append(j).append("").toString(), equipmentDropChances[j]));
        }

        par1NBTTagCompound.setTag("DropChances", nbttaglist1);
        par1NBTTagCompound.setString("CustomName", getCustomNameTag());
        par1NBTTagCompound.setBoolean("CustomNameVisible", func_94062_bN());
        par1NBTTagCompound.setBoolean("Leashed", field_110169_bv);

        if (field_110168_bw != null)
        {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound("Leash");

            if (field_110168_bw instanceof EntityLivingBase)
            {
                nbttagcompound1.setLong("UUIDMost", field_110168_bw.func_110124_au().getMostSignificantBits());
                nbttagcompound1.setLong("UUIDLeast", field_110168_bw.func_110124_au().getLeastSignificantBits());
            }
            else if (field_110168_bw instanceof EntityHanging)
            {
                EntityHanging entityhanging = (EntityHanging)field_110168_bw;
                nbttagcompound1.setInteger("X", entityhanging.xPosition);
                nbttagcompound1.setInteger("Y", entityhanging.yPosition);
                nbttagcompound1.setInteger("Z", entityhanging.zPosition);
            }

            par1NBTTagCompound.setTag("Leash", nbttagcompound1);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setCanPickUpLoot(par1NBTTagCompound.getBoolean("CanPickUpLoot"));
        persistenceRequired = par1NBTTagCompound.getBoolean("PersistenceRequired");

        if (par1NBTTagCompound.hasKey("CustomName") && par1NBTTagCompound.getString("CustomName").length() > 0)
        {
            func_94058_c(par1NBTTagCompound.getString("CustomName"));
        }

        func_94061_f(par1NBTTagCompound.getBoolean("CustomNameVisible"));

        if (par1NBTTagCompound.hasKey("Equipment"))
        {
            NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Equipment");

            for (int i = 0; i < equipment.length; i++)
            {
                equipment[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound)nbttaglist.tagAt(i));
            }
        }

        if (par1NBTTagCompound.hasKey("DropChances"))
        {
            NBTTagList nbttaglist1 = par1NBTTagCompound.getTagList("DropChances");

            for (int j = 0; j < nbttaglist1.tagCount(); j++)
            {
                equipmentDropChances[j] = ((NBTTagFloat)nbttaglist1.tagAt(j)).data;
            }
        }

        field_110169_bv = par1NBTTagCompound.getBoolean("Leashed");

        if (field_110169_bv && par1NBTTagCompound.hasKey("Leash"))
        {
            field_110170_bx = par1NBTTagCompound.getCompoundTag("Leash");
        }
    }

    public void setMoveForward(float par1)
    {
        moveForward = par1;
    }

    /**
     * set the movespeed used for the new AI system
     */
    public void setAIMoveSpeed(float par1)
    {
        super.setAIMoveSpeed(par1);
        setMoveForward(par1);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        worldObj.theProfiler.startSection("looting");

        if (!worldObj.isRemote && canPickUpLoot() && !dead && worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
        {
            List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityItem.class, boundingBox.expand(1.0D, 0.0D, 1.0D));
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                EntityItem entityitem = (EntityItem)iterator.next();

                if (!entityitem.isDead && entityitem.getEntityItem() != null)
                {
                    ItemStack itemstack = entityitem.getEntityItem();
                    int i = getArmorPosition(itemstack);

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
                            if (itemstack1 != null && rand.nextFloat() - 0.1F < equipmentDropChances[i])
                            {
                                entityDropItem(itemstack1, 0.0F);
                            }

                            setCurrentItemOrArmor(i, itemstack);
                            equipmentDropChances[i] = 2.0F;
                            persistenceRequired = true;
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

    /**
     * Returns true if the newer Entity AI code should be run
     */
    protected boolean isAIEnabled()
    {
        return false;
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
        if (persistenceRequired)
        {
            entityAge = 0;
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

    protected void updateEntityActionStateIndev()
    {
        despawnEntity();
        if(rand.nextFloat() < 0.07F)
        {
            float moveSpeed = (float)getRealMoveSpeed();
            moveStrafing = (rand.nextFloat() - 0.5F) * moveSpeed;
            moveForward = rand.nextFloat() * moveSpeed;
        }
        isJumping = rand.nextFloat() < 0.01F;
        float f = 8F;
        if(rand.nextFloat() < 0.04F)
        {
            EntityPlayer entityplayer = worldObj.getClosestPlayerToEntity(this, f);

            if (entityplayer != null && false)
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
        if (indevai){
            updateEntityActionStateIndev();
            return;
        }
        super.updateEntityActionState();
        moveStrafing = 0.0F;
        moveForward = 0.0F;
        despawnEntity();
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
     * Changes pitch and yaw so that the entity calling the function is facing the entity provided as an argument.
     */
    public void faceEntity(Entity par1Entity, float par2, float par3)
    {
        double d = par1Entity.posX - posX;
        double d2 = par1Entity.posZ - posZ;
        double d1;

        if (par1Entity instanceof EntityLivingBase)
        {
            EntityLivingBase entitylivingbase = (EntityLivingBase)par1Entity;
            d1 = (entitylivingbase.posY + (double)entitylivingbase.getEyeHeight()) - (posY + (double)getEyeHeight());
        }
        else
        {
            d1 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2D - (posY + (double)getEyeHeight());
        }

        double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
        float f = (float)((Math.atan2(d2, d) * 180D) / Math.PI) - 90F;
        float f1 = (float)(-((Math.atan2(d1, d3) * 180D) / Math.PI));
        rotationPitch = updateRotation(rotationPitch, f1, par3);
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
        return worldObj.checkNoEntityCollision(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).isEmpty() && !worldObj.isAnyLiquid(boundingBox);
    }

    /**
     * Returns render size modifier
     */
    public float getRenderSizeModifier()
    {
        return 1.0F;
    }

    /**
     * Will return how many at most can spawn in a chunk at once.
     */
    public int getMaxSpawnedInChunk()
    {
        return 4;
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
        if (oldrange){
            return 4;
        }
        if (getAttackTarget() == null)
        {
            return 3;
        }

        int i = (int)(func_110143_aJ() - func_110138_aP() * 0.33F);
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
        return equipment[0];
    }

    /**
     * 0 = item, 1-n is armor
     */
    public ItemStack getCurrentItemOrArmor(int par1)
    {
        return equipment[par1];
    }

    public ItemStack func_130225_q(int i)
    {
        return equipment[i + 1];
    }

    /**
     * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
     */
    public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack)
    {
        equipment[par1] = par2ItemStack;
    }

    public ItemStack[] getLastActiveItems()
    {
        return equipment;
    }

    /**
     * Drop the equipment for this entity.
     */
    protected void dropEquipment(boolean par1, int par2)
    {
        for (int i = 0; i < getLastActiveItems().length; i++)
        {
            ItemStack itemstack = getCurrentItemOrArmor(i);
            boolean flag = equipmentDropChances[i] > 1.0F;

            if (itemstack == null || !par1 && !flag || rand.nextFloat() - (float)par2 * 0.01F >= equipmentDropChances[i])
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

    /**
     * Makes entity wear random armor based on difficulty
     */
    protected void addRandomArmor()
    {
        if (rand.nextFloat() < 0.15F * worldObj.func_110746_b(posX, posY, posZ))
        {
            int i = rand.nextInt(2);
            float f = worldObj.difficultySetting != 3 ? 0.25F : 0.1F;

            if (rand.nextFloat() < 0.095F)
            {
                i++;
            }

            if (rand.nextFloat() < 0.095F)
            {
                i++;
            }

            if (rand.nextFloat() < 0.095F)
            {
                i++;
            }

            for (int j = 3; j >= 0; j--)
            {
                ItemStack itemstack = func_130225_q(j);

                if (j < 3 && rand.nextFloat() < f)
                {
                    break;
                }

                if (itemstack != null)
                {
                    continue;
                }

                Item item = getArmorItemForSlot(j + 1, i);

                if (item != null)
                {
                    setCurrentItemOrArmor(j + 1, new ItemStack(item));
                }
            }
        }
    }

    public static int getArmorPosition(ItemStack par0ItemStack)
    {
        if (par0ItemStack.itemID == Block.pumpkin.blockID || par0ItemStack.itemID == Item.skull.itemID)
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

    /**
     * Params: Armor slot, Item tier
     */
    public static Item getArmorItemForSlot(int par0, int par1)
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
                    return Item.helmetIron;
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
                    return Item.plateIron;
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
                    return Item.legsIron;
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
                    return Item.bootsIron;
                }

                if (par1 == 4)
                {
                    return Item.bootsDiamond;
                }

            default:
                return null;
        }
    }

    /**
     * Enchants the entity's armor and held item based on difficulty
     */
    protected void enchantEquipment()
    {
        float f = worldObj.func_110746_b(posX, posY, posZ);

        if (getHeldItem() != null && rand.nextFloat() < 0.25F * f)
        {
            EnchantmentHelper.addRandomEnchantment(rand, getHeldItem(), (int)(5F + f * (float)rand.nextInt(18)));
        }

        for (int i = 0; i < 4; i++)
        {
            ItemStack itemstack = func_130225_q(i);

            if (itemstack != null && rand.nextFloat() < 0.5F * f)
            {
                EnchantmentHelper.addRandomEnchantment(rand, itemstack, (int)(5F + f * (float)rand.nextInt(18)));
            }
        }
    }

    public EntityLivingData func_110161_a(EntityLivingData par1EntityLivingData)
    {
        func_110148_a(SharedMonsterAttributes.field_111265_b).func_111121_a(new AttributeModifier("Random spawn bonus", rand.nextGaussian() * 0.050000000000000003D, 1));
        return par1EntityLivingData;
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered()
    {
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
        else
        {
            return super.getEntityName();
        }
    }

    public void func_110163_bv()
    {
        persistenceRequired = true;
    }

    public void func_94058_c(String par1Str)
    {
        dataWatcher.updateObject(10, par1Str);
    }

    public String getCustomNameTag()
    {
        return dataWatcher.getWatchableObjectString(10);
    }

    public boolean hasCustomNameTag()
    {
        return dataWatcher.getWatchableObjectString(10).length() > 0;
    }

    public void func_94061_f(boolean par1)
    {
        dataWatcher.updateObject(11, Byte.valueOf(((byte)(par1 ? 1 : 0))));
    }

    public boolean func_94062_bN()
    {
        return dataWatcher.getWatchableObjectByte(11) == 1;
    }

    public boolean getAlwaysRenderNameTagForRender()
    {
        return func_94062_bN();
    }

    public void setEquipmentDropChance(int par1, float par2)
    {
        equipmentDropChances[par1] = par2;
    }

    public boolean canPickUpLoot()
    {
        return canPickUpLoot;
    }

    public void setCanPickUpLoot(boolean par1)
    {
        canPickUpLoot = par1;
    }

    public boolean func_104002_bU()
    {
        return persistenceRequired;
    }

    public final boolean func_130002_c(EntityPlayer par1EntityPlayer)
    {
        if (func_110167_bD() && func_110166_bE() == par1EntityPlayer)
        {
            func_110160_i(true, !par1EntityPlayer.capabilities.isCreativeMode);
            return true;
        }

        ItemStack itemstack = par1EntityPlayer.inventory.getCurrentItem();

        if (itemstack != null && itemstack.itemID == Item.field_111214_ch.itemID && func_110164_bC())
        {
            if ((this instanceof EntityTameable) && ((EntityTameable)this).isTamed())
            {
                if (par1EntityPlayer.getCommandSenderName().equalsIgnoreCase(((EntityTameable)this).getOwnerName()))
                {
                    func_110162_b(par1EntityPlayer, true);
                    itemstack.stackSize--;
                    return true;
                }
            }
            else
            {
                func_110162_b(par1EntityPlayer, true);
                itemstack.stackSize--;
                return true;
            }
        }

        if (interact(par1EntityPlayer))
        {
            return true;
        }
        else
        {
            return super.func_130002_c(par1EntityPlayer);
        }
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    protected boolean interact(EntityPlayer par1EntityPlayer)
    {
        return false;
    }

    protected void func_110159_bB()
    {
        if (field_110170_bx != null)
        {
            func_110165_bF();
        }

        if (!field_110169_bv)
        {
            return;
        }

        if (field_110168_bw == null || field_110168_bw.isDead)
        {
            func_110160_i(true, true);
            return;
        }
        else
        {
            return;
        }
    }

    public void func_110160_i(boolean par1, boolean par2)
    {
        if (field_110169_bv)
        {
            field_110169_bv = false;
            field_110168_bw = null;

            if (!worldObj.isRemote && par2)
            {
                dropItem(Item.field_111214_ch.itemID, 1);
            }

            if (!worldObj.isRemote && par1 && (worldObj instanceof WorldServer))
            {
                ((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet39AttachEntity(1, this, null));
            }
        }
    }

    public boolean func_110164_bC()
    {
        return !func_110167_bD() && !(this instanceof IMob);
    }

    public boolean func_110167_bD()
    {
        return field_110169_bv;
    }

    public Entity func_110166_bE()
    {
        return field_110168_bw;
    }

    public void func_110162_b(Entity par1Entity, boolean par2)
    {
        field_110169_bv = true;
        field_110168_bw = par1Entity;

        if (!worldObj.isRemote && par2 && (worldObj instanceof WorldServer))
        {
            ((WorldServer)worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet39AttachEntity(1, this, field_110168_bw));
        }
    }

    private void func_110165_bF()
    {
        label0:
        {
            if (!field_110169_bv || field_110170_bx == null)
            {
                break label0;
            }

            if (field_110170_bx.hasKey("UUIDMost") && field_110170_bx.hasKey("UUIDLeast"))
            {
                UUID uuid = new UUID(field_110170_bx.getLong("UUIDMost"), field_110170_bx.getLong("UUIDLeast"));
                List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityLivingBase.class, boundingBox.expand(10D, 10D, 10D));
                Iterator iterator = list.iterator();
                EntityLivingBase entitylivingbase;

                do
                {
                    if (!iterator.hasNext())
                    {
                        break label0;
                    }

                    entitylivingbase = (EntityLivingBase)iterator.next();
                }
                while (!entitylivingbase.func_110124_au().equals(uuid));

                field_110168_bw = entitylivingbase;
            }
            else if (field_110170_bx.hasKey("X") && field_110170_bx.hasKey("Y") && field_110170_bx.hasKey("Z"))
            {
                int i = field_110170_bx.getInteger("X");
                int j = field_110170_bx.getInteger("Y");
                int k = field_110170_bx.getInteger("Z");
                EntityLeashKnot entityleashknot = EntityLeashKnot.func_110130_b(worldObj, i, j, k);

                if (entityleashknot == null)
                {
                    entityleashknot = EntityLeashKnot.func_110129_a(worldObj, i, j, k);
                }

                field_110168_bw = entityleashknot;
            }
            else
            {
                func_110160_i(false, true);
            }
        }
        field_110170_bx = null;
    }
}
