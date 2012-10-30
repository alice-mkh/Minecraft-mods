package net.minecraft.src;

import java.util.Calendar;
import java.util.Random;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob
{
    public static boolean fixai = false;
    public static boolean survivaltest = false;
    public static boolean fast = false;
    public static boolean custom = true;

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
        tasks.addTask(5, new EntityAIWander(this, moveSpeed));
        tasks.addTask(6, new EntityAIWatchClosest(this, net.minecraft.src.EntityPlayer.class, 8F));
        tasks.addTask(6, new EntityAILookIdle(this));
        targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, net.minecraft.src.EntityPlayer.class, 16F, 0, true));
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(13, new Byte((byte)0));
    }

    public boolean getCanSpawnHere()
    {
        return super.getCanSpawnHere() || survivaltest;
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
            entityarrow.setThrowableHeading(d, d2 + (double)f1, d1, 1.6F, 12F);
        }else if (f < 10F)
        {
            double d = entity.posX - posX;
            double d1 = entity.posZ - posZ;
            if (attackTime == 0)
            {
                EntityArrow entityarrow = new EntityArrow(worldObj, this, 1.0F);
                if (fast){
                    entityarrow.posY += 1.3999999761581421D;
                    double d2 = (entity.posY + (double)entity.getEyeHeight()) - 0.20000000298023224D - entityarrow.posY;
                    float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
                    worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
                    worldObj.spawnEntityInWorld(entityarrow);
                    entityarrow.setThrowableHeading(d, d2 + (double)f1, d1, /*0.6*/1.1F, 12F);
                    attackTime = 30;
                }else{
                    double d2 = (entity.posY + (double)entity.getEyeHeight()) - 0.69999998807907104D - entityarrow.posY;
                    float f1 = MathHelper.sqrt_double(d * d + d1 * d1) * 0.2F;
                    worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
                    worldObj.spawnEntityInWorld(entityarrow);
                    entityarrow.setThrowableHeading(d, d2 + (double)f1, d1, 1.6F, 12F);
                    attackTime = 60;
                }
            }
            rotationYaw = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
            hasAttacked = true;
        }
    }

    protected void onDeathUpdate(){
        if (survivaltest && deathTime >= 19){
            int i = (int)((Math.random() + Math.random()) * 3D + 4D);
            worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 0.8F));
            if (worldObj.func_82736_K().func_82766_b("doMobLoot")){
                for(int j = 0; j < i; j++){
                    EntityArrow arrow = new EntityArrow(worldObj, this, 0.4F);
                    arrow.canBePickedUp = 1;
                    arrow.posY = posY + 0.2F;
                    arrow.rotationYaw = (float)Math.random() * 360F;
                    arrow.rotationPitch = -(float)Math.random() * 60F;
                    arrow.motionX = -MathHelper.sin((arrow.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((arrow.rotationPitch / 180F) * (float)Math.PI);
                    arrow.motionZ = MathHelper.cos((arrow.rotationYaw / 180F) * (float)Math.PI) * MathHelper.cos((arrow.rotationPitch / 180F) * (float)Math.PI);
                    arrow.motionY = -MathHelper.sin((arrow.rotationPitch / 180F) * (float)Math.PI);
                    arrow.setThrowableHeading(arrow.motionX, arrow.motionY, arrow.motionZ, 0.4F, 1.0F);
                    worldObj.spawnEntityInWorld(arrow);
                }
            }
            for (int j = 0; j < 20; j++)
            {
                double d = rand.nextGaussian() * 0.02D;
                double d1 = rand.nextGaussian() * 0.02D;
                double d2 = rand.nextGaussian() * 0.02D;
                worldObj.spawnParticle("explode", (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
            }
//             onEntityDeath();
            setDead();
        }else{
            super.onDeathUpdate();
        }
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
        return survivaltest ? 10 : 20;
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound()
    {
        return "mob.skeleton.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.skeleton.hurt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.skeleton.death";
    }

    /**
     * Plays step sound at given x, y, z for the entity
     */
    protected void playStepSound(int par1, int par2, int par3, int par4)
    {
        worldObj.playSoundAtEntity(this, "mob.skeleton.step", 0.15F, 1.0F);
    }

    public boolean attackEntityAsMob(Entity par1Entity)
    {
        if (super.attackEntityAsMob(par1Entity))
        {
            if (func_82202_m() == 1 && (par1Entity instanceof EntityLiving))
            {
                ((EntityLiving)par1Entity).addPotionEffect(new PotionEffect(Potion.field_82731_v.id, 200));
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public int func_82193_c(Entity par1Entity)
    {
        if (func_82202_m() == 1)
        {
            ItemStack itemstack = getHeldItem();
            int i = 4;

            if (itemstack != null)
            {
                i += itemstack.getDamageVsEntity(this);
            }

            return i;
        }
        else
        {
            return super.func_82193_c(par1Entity);
        }
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

            if (f > 0.5F && rand.nextFloat() * 30F < (f - 0.4F) * 2.0F && worldObj.canBlockSeeTheSky(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)))
            {
                boolean flag = true;
                ItemStack itemstack = getCurrentItemOrArmor(4);

                if (itemstack != null)
                {
                    if (itemstack.isItemStackDamageable())
                    {
                        itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + rand.nextInt(2));

                        if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
                        {
                            renderBrokenItemStack(itemstack);
                            func_70062_b(4, null);
                        }
                    }

                    flag = false;
                }

                if (flag)
                {
                    setFire(8);
                }
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
        if (func_82202_m() == 1)
        {
            int i = rand.nextInt(3 + par2) - 1;

            for (int l = 0; l < i; l++)
            {
                dropItem(Item.coal.shiftedIndex, 1);
            }
        }
        else
        {
            int j = rand.nextInt(3 + par2);

            if (!survivaltest){
                for (int i1 = 0; i1 < j; i1++)
                {
                    dropItem(Item.arrow.shiftedIndex, 1);
                }
            }
        }

        int k = rand.nextInt(3 + par2);

        for (int j1 = 0; j1 < k; j1++)
        {
            dropItem(Item.bone.shiftedIndex, 1);
        }
    }

    protected void dropRareDrop(int par1)
    {
        if (func_82202_m() == 1)
        {
            entityDropItem(new ItemStack(Item.field_82799_bQ.shiftedIndex, 1, 1), 0.0F);
        }
    }

    protected void func_82164_bB()
    {
        super.func_82164_bB();
        if (getCurrentItemOrArmor(1) != null || getCurrentItemOrArmor(2) != null || getCurrentItemOrArmor(3) != null || getCurrentItemOrArmor(4) != null){
            helmet = false;
            armor = false;
        }
        func_70062_b(0, new ItemStack(Item.bow));
    }

    /**
     * Returns the texture's file path as a String.
     */
    public String getTexture()
    {
        if (func_82202_m() == 1)
        {
            return "/mob/skeleton_wither.png";
        }
        else
        {
            return super.getTexture();
        }
    }

    public void func_82163_bD()
    {
        if ((worldObj.provider instanceof WorldProviderHell) && getRNG().nextInt(5) > 0)
        {
            tasks.addTask(4, new EntityAIAttackOnCollide(this, net.minecraft.src.EntityPlayer.class, moveSpeed, false));
            func_82201_a(1);
            func_70062_b(0, new ItemStack(Item.swordStone));
        }
        else
        {
            tasks.addTask(4, new EntityAIArrowAttack(this, moveSpeed, 60, 10F));
            func_82164_bB();
            if (!custom){
                return;
            }
            func_82162_bC();
        }

        field_82172_bs = rand.nextFloat() < field_82181_as[worldObj.difficultySetting];

        if (getCurrentItemOrArmor(4) == null)
        {
            Calendar calendar = worldObj.func_83015_S();

            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && rand.nextFloat() < 0.25F)
            {
                func_70062_b(4, new ItemStack(rand.nextFloat() >= 0.1F ? Block.pumpkin : Block.pumpkinLantern));
                field_82174_bp[4] = 0.0F;
            }
        }
    }

    public void func_82196_d(EntityLiving par1EntityLiving)
    {
        EntityArrow entityarrow = new EntityArrow(worldObj, this, par1EntityLiving, 1.6F, 12F);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());

        if (i > 0)
        {
            entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
        }

        if (j > 0)
        {
            entityarrow.setKnockbackStrength(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0 || func_82202_m() == 1)
        {
            entityarrow.setFire(100);
        }

        worldObj.playSoundAtEntity(this, "random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
        worldObj.spawnEntityInWorld(entityarrow);
    }

    public int func_82202_m()
    {
        return dataWatcher.getWatchableObjectByte(13);
    }

    public void func_82201_a(int par1)
    {
        dataWatcher.updateObject(13, Byte.valueOf((byte)par1));
        isImmuneToFire = par1 == 1;

        if (par1 == 1)
        {
            setSize(0.72F, 2.16F);
        }
        else
        {
            setSize(0.6F, 1.8F);
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);

        if (par1NBTTagCompound.hasKey("SkeletonType"))
        {
            byte byte0 = par1NBTTagCompound.getByte("SkeletonType");
            func_82201_a(byte0);
        }

        if (func_82202_m() == 1)
        {
            tasks.addTask(4, new EntityAIAttackOnCollide(this, net.minecraft.src.EntityPlayer.class, moveSpeed, false));
        }
        else
        {
            tasks.addTask(4, new EntityAIArrowAttack(this, moveSpeed, 60, 10F));
        }
        helmet = par1NBTTagCompound.getBoolean("Helmet");
        armor = par1NBTTagCompound.getBoolean("Armor");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setByte("SkeletonType", (byte)func_82202_m());
        par1NBTTagCompound.setBoolean("Helmet", helmet);
        par1NBTTagCompound.setBoolean("Armor", armor);
    }
}
