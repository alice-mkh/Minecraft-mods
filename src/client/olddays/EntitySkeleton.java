package net.minecraft.src;

import java.util.Random;

public class EntitySkeleton extends EntityMob
{
    public static boolean fixai = false;
    public static boolean survivaltest = false;

    /** The ItemStack that any Skeleton holds (a bow). */
    private static final ItemStack defaultHeldItem;

    public boolean helmet;
    public boolean armor;

    public EntitySkeleton(World par1World)
    {
        super(par1World);
        texture = "/mob/skeleton.png";
        moveSpeed = 0.25F;
        helmet = Math.random() < 0.20000000298023224D;
        armor = Math.random() < 0.20000000298023224D;
        tasks.addTask(1, new EntityAISwimming(this));
        tasks.addTask(2, new EntityAIRestrictSun(this));
        tasks.addTask(3, new EntityAIFleeSun(this, moveSpeed));
        tasks.addTask(4, new EntityAIArrowAttack(this, moveSpeed, 1, 60));
        tasks.addTask(5, new EntityAIWander(this, moveSpeed));
        tasks.addTask(6, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityPlayer.class, 16F, 0, true));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setBoolean("Helmet", helmet);
        par1NBTTagCompound.setBoolean("Armor", armor);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        helmet = par1NBTTagCompound.getBoolean("Helmet");
        armor = par1NBTTagCompound.getBoolean("Armor");
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    public boolean isAIEnabled()
    {
        return true;
    }

    public int getMaxHealth()
    {
        return 20;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.skeleton";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.skeletonhurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.skeletonhurt";
    }

    /**
     * Returns the item that this EntityLiving is holding, if any.
     */
    public ItemStack getHeldItem()
    {
        return defaultHeldItem;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        if (worldObj.isDaytime() && !worldObj.isRemote && !survivaltest)
        {
            float f = getBrightness(1.0F);

            if (f > 0.5F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F)
            {
                setFire(8);
            }
        }

        super.onLivingUpdate();
    }

    /**
     * Called when the mob's health reaches 0.
     */
    public void onDeath(DamageSource par1DamageSource)
    {
        super.onDeath(par1DamageSource);

        if ((par1DamageSource.getSourceOfDamage() instanceof EntityArrow) && (par1DamageSource.getEntity() instanceof EntityPlayer))
        {
            EntityPlayer entityplayer = (EntityPlayer)par1DamageSource.getEntity();
            double d = entityplayer.posX - posX;
            double d1 = entityplayer.posZ - posZ;

            if (d * d + d1 * d1 >= 2500D)
            {
                entityplayer.triggerAchievement(AchievementList.snipeSkeleton);
            }
        }
    }

    protected void onDeathUpdate(){
        if (survivaltest && deathTime >= 19){
            int i = (int)((Math.random() + Math.random()) * 3D + 4D);
            worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            for(int j = 0; j < i; j++){
                EntityArrow arrow = new EntityArrow(worldObj, this, 0.4F);
                arrow.doesArrowBelongToPlayer = true;
                arrow.posY = posY + 0.2F;
                arrow.rotationYaw = (float)Math.random() * 360F;
                arrow.rotationPitch = -(float)Math.random() * 60F;
                arrow.motionX = -MathHelper.sin((arrow.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((arrow.rotationPitch / 180F) * (float)Math.PI);
                arrow.motionZ = MathHelper.cos((arrow.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((arrow.rotationPitch / 180F) * (float)Math.PI);
                arrow.motionY = -MathHelper.sin((arrow.rotationPitch / 180F) * (float)Math.PI);
                arrow.setArrowHeading(arrow.motionX, arrow.motionY, arrow.motionZ, 0.4F, 1.0F);
                worldObj.spawnEntityInWorld(arrow);
            }
            for (int j = 0; j < 20; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
            onEntityDeath();
            setDead();
        }else{
            super.onDeathUpdate();
        }
    }

    protected void attackEntity(Entity entity, float f)
    {
        if (!fixai && !survivaltest){
            super.attackEntity(entity, f);
        }
        if(rand.nextInt(30) == 0 && entity != null && survivaltest){
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            EntityArrow entityarrow = new EntityArrow(worldObj, this, 1.0F);
            double d2 = (entity.posY + (double)entity.getEyeHeight()) - 0.69999998807907104D - entityarrow.posY;
            float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
            worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            worldObj.spawnEntityInWorld(entityarrow);
            entityarrow.setArrowHeading(d, d2 + (double)f1, d1, 1.6F, 12F);
        }else if (f < 10F)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            if (attackTime == 0)
            {
                EntityArrow entityarrow = new EntityArrow(worldObj, this, 1.0F);
                double d2 = (entity.posY + (double)entity.getEyeHeight()) - 0.69999998807907104D - entityarrow.posY;
                float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
                worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
                worldObj.spawnEntityInWorld(entityarrow);
                entityarrow.setArrowHeading(d, d2 + (double)f1, d1, 1.6F, 12F);
                attackTime = 60;
            }
            rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
            hasAttacked = true;
        }
    }

    /**
     * Returns the item ID for the item the mob drops on death.
     */
    protected int getDropItemId()
    {
        return Item.arrow.shiftedIndex;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean par1, int par2)
    {
        int i = rand.nextInt(3 + par2);

        if (!survivaltest){
            for (int j = 0; j < i; j++)
            {
                dropItem(Item.arrow.shiftedIndex, 1);
            }
        }

        i = rand.nextInt(3 + par2);

        for (int k = 0; k < i; k++)
        {
            dropItem(Item.bone.shiftedIndex, 1);
        }
    }

    protected void dropRareDrop(int par1)
    {
        if (par1 > 0)
        {
            ItemStack itemstack = new ItemStack(Item.bow);
            EnchantmentHelper.func_48441_a(rand, itemstack, 5);
            entityDropItem(itemstack, 0.0F);
        }
        else
        {
            dropItem(Item.bow.shiftedIndex, 1);
        }
    }

    static
    {
        defaultHeldItem = new ItemStack(Item.bow, 1);
    }
}
