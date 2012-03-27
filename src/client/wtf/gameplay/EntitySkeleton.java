package net.minecraft.src;

import java.util.Random;

public class EntitySkeleton extends EntityMob
{
    /** The ItemStack that any Skeleton holds (a bow). */
    private static final ItemStack defaultHeldItem;

    public EntitySkeleton(World par1World)
    {
        super(par1World);
        texture = "/mob/skeleton.png";
        moveSpeed = 0.25F;
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
        if (worldObj.isDaytime() && !worldObj.isRemote)
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

        for (int j = 0; j < i; j++)
        {
            dropItem(Item.arrow.shiftedIndex, 1);
        }

        i = rand.nextInt(3 + par2);

        for (int k = 0; k < i; k++)
        {
            dropItem(Item.bone.shiftedIndex, 1);
        }
    }

    protected void dropRareDrop(int par1)
    {
        if (mod_WTFGameplay.DisableRareLoot){
            return;
        }
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
