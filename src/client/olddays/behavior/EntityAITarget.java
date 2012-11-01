package net.minecraft.src;

import java.util.Random;

public abstract class EntityAITarget extends EntityAIBase
{
    public static boolean oldai = false;

    /** The entity that this task belongs to */
    protected EntityLiving taskOwner;
    protected float targetDistance;

    /**
     * If true, EntityAI targets must be able to be seen (cannot be blocked by walls) to be suitable targets.
     */
    protected boolean shouldCheckSight;
    private boolean field_75303_a;
    private int field_75301_b;
    private int field_75302_c;
    private int field_75298_g;

    public EntityAITarget(EntityLiving par1EntityLiving, float par2, boolean par3)
    {
        this(par1EntityLiving, par2, par3, false);
    }

    public EntityAITarget(EntityLiving par1EntityLiving, float par2, boolean par3, boolean par4)
    {
        field_75301_b = 0;
        field_75302_c = 0;
        field_75298_g = 0;
        taskOwner = par1EntityLiving;
        targetDistance = par2;
        shouldCheckSight = par3;
        field_75303_a = par4;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLiving entityliving = taskOwner.getAttackTarget();

        if (entityliving == null)
        {
            return false;
        }

        if (!entityliving.isEntityAlive())
        {
            return false;
        }

        if (taskOwner.getDistanceSqToEntity(entityliving) > (double)(targetDistance * targetDistance))
        {
            return false;
        }

        if (shouldCheckSight)
        {
            if (taskOwner.getEntitySenses().canSee(entityliving))
            {
                field_75298_g = 0;
            }
            else if (++field_75298_g > 60)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_75301_b = 0;
        field_75302_c = 0;
        field_75298_g = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        taskOwner.setAttackTarget(null);
    }

    /**
     * A method used to see if an entity is a suitable target through a number of checks.
     */
    protected boolean isSuitableTarget(EntityLiving par1EntityLiving, boolean par2)
    {
        if (par1EntityLiving == null)
        {
            return false;
        }

        if (par1EntityLiving == taskOwner)
        {
            return false;
        }

        if (!par1EntityLiving.isEntityAlive())
        {
            return false;
        }

        if (oldai && (par1EntityLiving.boundingBox.maxY <= taskOwner.boundingBox.minY || par1EntityLiving.boundingBox.minY >= taskOwner.boundingBox.maxY))
        {
            return false;
        }

        if (!taskOwner.isExplosiveMob(par1EntityLiving.getClass()))
        {
            return false;
        }

        if ((taskOwner instanceof EntityTameable) && ((EntityTameable)taskOwner).isTamed())
        {
            if ((par1EntityLiving instanceof EntityTameable) && ((EntityTameable)par1EntityLiving).isTamed())
            {
                return false;
            }

            if (par1EntityLiving == ((EntityTameable)taskOwner).getOwner())
            {
                return false;
            }
        }
        else if ((par1EntityLiving instanceof EntityPlayer) && !par2 && ((EntityPlayer)par1EntityLiving).capabilities.disableDamage)
        {
            return false;
        }

        if (!taskOwner.isWithinHomeDistance(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ)))
        {
            return false;
        }

        if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(par1EntityLiving))
        {
            return false;
        }

        if (field_75303_a)
        {
            if (--field_75302_c <= 0)
            {
                field_75301_b = 0;
            }

            if (field_75301_b == 0)
            {
                field_75301_b = func_75295_a(par1EntityLiving) ? 1 : 2;
            }

            if (field_75301_b == 2)
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_75295_a(EntityLiving par1EntityLiving)
    {
        field_75302_c = 10 + taskOwner.getRNG().nextInt(5);
        PathEntity pathentity = taskOwner.getNavigator().getPathToEntityLiving(par1EntityLiving);

        if (pathentity == null)
        {
            return false;
        }

        PathPoint pathpoint = pathentity.getFinalPathPoint();

        if (pathpoint == null)
        {
            return false;
        }
        else
        {
            int i = pathpoint.xCoord - MathHelper.floor_double(par1EntityLiving.posX);
            int j = pathpoint.zCoord - MathHelper.floor_double(par1EntityLiving.posZ);
            return (double)(i * i + j * j) <= 2.25D;
        }
    }
}
