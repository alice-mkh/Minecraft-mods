package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EntityWolf extends EntityTameable
{
    public static boolean despawn = false;
    public static boolean fixai = false;

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
        dataWatcher.addObject(20, new Byte((byte)BlockCloth.getBlockFromDye(1)));
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        playSound("mob.wolf.step", 0.15F, 1.0F);
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
        par1NBTTagCompound.setByte("CollarColor", (byte)getCollarColor());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        setAngry(par1NBTTagCompound.getBoolean("Angry"));

        if (par1NBTTagCompound.hasKey("CollarColor"))
        {
            setCollarColor(par1NBTTagCompound.getByte("CollarColor"));
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn()
    {
        return despawn ? !isTamed() : isAngry() && !isTamed();
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
        if (fixai){
            onLivingUpdate_old();
            return;
        }
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
                playSound("mob.wolf.shake", getSoundVolume(), (rand.nextFloat() - rand.nextFloat()) * 0.2F + 1.0F);
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
        if (isEntityInvulnerable())
        {
            return false;
        }
        if (fixai){
            return attackEntityFrom_old(par1DamageSource, par2);
        }

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
        if (fixai){
            return super.attackEntityAsMob(par1Entity);
        }
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
            if (itemstack != null)
            {
                if (Item.itemsList[itemstack.itemID] instanceof ItemFood)
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
                else if (itemstack.itemID == Item.dyePowder.itemID)
                {
                    int i = BlockCloth.getBlockFromDye(itemstack.getItemDamage());

                    if (i != getCollarColor())
                    {
                        setCollarColor(i);

                        if (!par1EntityPlayer.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
                        {
                            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, null);
                        }

                        return true;
                    }
                }
            }

            if (par1EntityPlayer.username.equalsIgnoreCase(getOwnerName()) && !worldObj.isRemote && !isBreedingItem(itemstack))
            {
                if (fixai){
                    setSitting(!isSitting());
                }else{
                    aiSit.setSitting(!isSitting());
                }
                isJumping = false;
                setPathToEntity(null);
            }
        }
        else if (itemstack != null && itemstack.itemID == Item.bone.itemID && !isAngry())
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
                    if (fixai){
                        setSitting(true);
                    }else{
                        aiSit.setSitting(true);
                    }
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
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack par1ItemStack)
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
     * Return this wolf's collar color.
     */
    public int getCollarColor()
    {
        return dataWatcher.getWatchableObjectByte(20) & 0xf;
    }

    /**
     * Set this wolf's collar color.
     */
    public void setCollarColor(int par1)
    {
        dataWatcher.updateObject(20, Byte.valueOf((byte)(par1 & 0xf)));
    }

    /**
     * This function is used when two same-species animals in 'love mode' breed to generate the new baby animal.
     */
    public EntityWolf spawnBabyAnimal(EntityAgeable par1EntityAgeable)
    {
        EntityWolf entitywolf = new EntityWolf(worldObj);
        String s = getOwnerName();

        if (s != null && s.trim().length() > 0)
        {
            entitywolf.setOwner(s);
            entitywolf.setTamed(true);
        }

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

    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
        return spawnBabyAnimal(par1EntityAgeable);
    }

    @Override
    protected void updateEntityActionState()
    {
        super.updateEntityActionState();
        if (!fixai){
            return;
        }
        if (!hasAttacked && !hasPath() && isTamed() && ridingEntity == null)
        {
            EntityPlayer entityplayer = worldObj.getPlayerEntityByName(getOwnerName());
            if (entityplayer != null)
            {
                float f = entityplayer.getDistanceToEntity(this);
                if (f > 5F)
                {
                    getPathOrWalkableBlock(entityplayer, f);
                }
            }
            else if (!isInWater())
            {
                setSitting(true);
            }
        }
        else if (entityToAttack == null && !hasPath() && !isTamed() && worldObj.rand.nextInt(100) == 0)
        {
            List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntitySheep.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
            if (!list.isEmpty())
            {
                setTarget((Entity)list.get(worldObj.rand.nextInt(list.size())));
            }
        }
        if (isInWater())
        {
            setSitting(false);
        }
        if (!worldObj.isRemote)
        {
            dataWatcher.updateObject(18, Integer.valueOf(getHealth()));
        }
    }

    private void getPathOrWalkableBlock(Entity entity, float f)
    {
        PathEntity pathentity = worldObj.getPathEntityToEntity(this, entity, 16F, true, false, false, true);
        if (pathentity == null && f > 12F)
        {
            int i = MathHelper.floor_double(entity.posX) - 2;
            int j = MathHelper.floor_double(entity.posZ) - 2;
            int k = MathHelper.floor_double(entity.boundingBox.minY);
            for (int l = 0; l <= 4; l++)
            {
                for (int i1 = 0; i1 <= 4; i1++)
                {
                    if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && worldObj.isBlockNormalCube(i + l, k - 1, j + i1) && !worldObj.isBlockNormalCube(i + l, k, j + i1) && !worldObj.isBlockNormalCube(i + l, k + 1, j + i1))
                    {
                        setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, rotationYaw, rotationPitch);
                        return;
                    }
                }
            }
        }
        else
        {
            setPathToEntity(pathentity);
        }
    }

    @Override
    protected void attackEntity(Entity entity, float f){
        if (!fixai){
            super.attackEntity(entity, f);
            return;
        }
        if (f > 2.0F && f < 6F && rand.nextInt(10) == 0)
        {
            if (onGround)
            {
                double d = entity.posX - posX;
                double d1 = entity.posZ - posZ;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1);
                motionX = (d / (double)f1) * 0.5D * 0.80000001192092896D + motionX * 0.20000000298023224D;
                motionZ = (d1 / (double)f1) * 0.5D * 0.80000001192092896D + motionZ * 0.20000000298023224D;
                motionY = 0.40000000596046448D;
            }
        }
        else if ((double)f < 1.5D && entity.boundingBox.maxY > boundingBox.minY && entity.boundingBox.minY < boundingBox.maxY)
        {
            attackTime = 20;
            byte byte0 = 2;
            if (isTamed())
            {
                byte0 = 4;
            }
            entity.attackEntityFrom(DamageSource.causeMobDamage(this), byte0);
        }
    }

    @Override
    protected Entity findPlayerToAttack()
    {
        if (!fixai){
            return super.findPlayerToAttack();
        }
        if (isAngry())
        {
            return worldObj.getClosestPlayerToEntity(this, 16D);
        }
        else
        {
            return null;
        }
    }


    public boolean attackEntityFrom_old(DamageSource damagesource, int i)
    {
        Entity entity = damagesource.getEntity();
        setSitting(false);
        if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof EntityArrow))
        {
            i = (i + 1) / 2;
        }
        if (super.attackEntityFrom(damagesource, i))
        {
            if (!isTamed() && !isAngry())
            {
                if (entity instanceof EntityPlayer)
                {
                    setAngry(true);
                    entityToAttack = entity;
                }
                if ((entity instanceof EntityArrow) && ((EntityArrow)entity).shootingEntity != null)
                {
                    entity = ((EntityArrow)entity).shootingEntity;
                }
                if (entity instanceof EntityLiving)
                {
                    List list = worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityWolf.class, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1.0D, posY + 1.0D, posZ + 1.0D).expand(16D, 4D, 16D));
                    Iterator iterator = list.iterator();
                    do
                    {
                        if (!iterator.hasNext())
                        {
                            break;
                        }
                        Entity entity1 = (Entity)iterator.next();
                        EntityWolf entitywolf = (EntityWolf)entity1;
                        if (!entitywolf.isTamed() && entitywolf.entityToAttack == null)
                        {
                            entitywolf.entityToAttack = entity;
                            if (entity instanceof EntityPlayer)
                            {
                                entitywolf.setAngry(true);
                            }
                        }
                    }
                    while (true);
                }
            }
            else if (entity != this && entity != null)
            {
                if (isTamed() && (entity instanceof EntityPlayer) && ((EntityPlayer)entity).username.equalsIgnoreCase(getOwnerName()))
                {
                    return true;
                }
                entityToAttack = entity;
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected boolean isMovementCeased()
    {
        if (!fixai){
            return super.isMovementCeased();
        }
        return isSitting() || field_70928_h;
    }

    public void onLivingUpdate_old()
    {
        super.onLivingUpdate();
        func_70918_i(false);
        if (hasCurrentTarget() && !hasPath() && !isAngry())
        {
            Entity entity = getCurrentTarget();
            if (entity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entity;
                ItemStack itemstack = entityplayer.inventory.getCurrentItem();
                if (itemstack != null)
                {
                    if (!isTamed() && itemstack.itemID == Item.bone.itemID)
                    {
                        func_70918_i(true);
                    }
                    else if (isTamed() && (Item.itemsList[itemstack.itemID] instanceof ItemFood))
                    {
                        func_70918_i(((ItemFood)Item.itemsList[itemstack.itemID]).isWolfsFavoriteMeat());
                    }
                }
            }
        }
        if (!worldObj.isRemote && isShaking && !field_70928_h && !hasPath() && onGround)
        {
            field_70928_h = true;
            timeWolfIsShaking = 0.0F;
            prevTimeWolfIsShaking = 0.0F;
            worldObj.setEntityState(this, (byte)8);
        }
    }
}
