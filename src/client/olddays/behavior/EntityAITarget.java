package net.minecraft.src;

import java.util.Random;
import org.apache.commons.lang3.StringUtils;

public abstract class EntityAITarget extends EntityAIBase
{
    public static boolean oldai = false;

    /** The entity that this task belongs to */
    protected EntityCreature taskOwner;

    /**
     * If true, EntityAI targets must be able to be seen (cannot be blocked by walls) to be suitable targets.
     */
    protected boolean shouldCheckSight;

    /**
     * When true, only entities that can be reached with minimal effort will be targetted.
     */
    private boolean nearbyOnly;

    /**
     * When nearbyOnly is true: 0 -> No target, but OK to search; 1 -> Nearby target found; 2 -> Target too far.
     */
    private int targetSearchStatus;

    /**
     * When nearbyOnly is true, this throttles target searching to avoid excessive pathfinding.
     */
    private int targetSearchDelay;
    private int field_75298_g;

    public EntityAITarget(EntityCreature par1EntityCreature, boolean par2)
    {
        this(par1EntityCreature, par2, false);
    }

    public EntityAITarget(EntityCreature par1EntityCreature, boolean par2, boolean par3)
    {
        taskOwner = par1EntityCreature;
        shouldCheckSight = par2;
        nearbyOnly = par3;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLivingBase entitylivingbase = taskOwner.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }

        if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }

        double d = getTargetDistance();

        if (taskOwner.getDistanceSqToEntity(entitylivingbase) > d * d)
        {
            return false;
        }

        if (shouldCheckSight)
        {
            if (taskOwner.getEntitySenses().canSee(entitylivingbase))
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

    protected double getTargetDistance()
    {
        AttributeInstance attributeinstance = taskOwner.getEntityAttribute(SharedMonsterAttributes.followRange);
        return attributeinstance != null ? attributeinstance.getAttributeValue() : 16D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        targetSearchStatus = 0;
        targetSearchDelay = 0;
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
    protected boolean isSuitableTarget(EntityLivingBase par1EntityLivingBase, boolean par2)
    {
        if (par1EntityLivingBase == null)
        {
            return false;
        }

        if (par1EntityLivingBase == taskOwner)
        {
            return false;
        }

        if (oldai && (par1EntityLivingBase.boundingBox.maxY <= taskOwner.boundingBox.minY || par1EntityLivingBase.boundingBox.minY >= taskOwner.boundingBox.maxY))
        {
            return false;
        }

        if (!par1EntityLivingBase.isEntityAlive())
        {
            return false;
        }

        if (!taskOwner.canAttackClass(par1EntityLivingBase.getClass()))
        {
            return false;
        }

        if ((taskOwner instanceof EntityOwnable) && StringUtils.isNotEmpty(((EntityOwnable)taskOwner).getOwnerName()))
        {
            if ((par1EntityLivingBase instanceof EntityOwnable) && ((EntityOwnable)taskOwner).getOwnerName().equals(((EntityOwnable)par1EntityLivingBase).getOwnerName()))
            {
                return false;
            }

            if (par1EntityLivingBase == ((EntityOwnable)taskOwner).getOwner())
            {
                return false;
            }
        }
        else if ((par1EntityLivingBase instanceof EntityPlayer) && !par2 && ((EntityPlayer)par1EntityLivingBase).capabilities.disableDamage)
        {
            return false;
        }

        if (!taskOwner.func_110176_b(MathHelper.floor_double(par1EntityLivingBase.posX), MathHelper.floor_double(par1EntityLivingBase.posY), MathHelper.floor_double(par1EntityLivingBase.posZ)))
        {
            return false;
        }

        if (shouldCheckSight && !taskOwner.getEntitySenses().canSee(par1EntityLivingBase))
        {
            return false;
        }

        if (nearbyOnly)
        {
            if (--targetSearchDelay <= 0)
            {
                targetSearchStatus = 0;
            }

            if (targetSearchStatus == 0)
            {
                targetSearchStatus = canEasilyReach(par1EntityLivingBase) ? 1 : 2;
            }

            if (targetSearchStatus == 2)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks to see if this entity can find a short path to the given target.
     */
    private boolean canEasilyReach(EntityLivingBase par1EntityLivingBase)
    {
        targetSearchDelay = 10 + taskOwner.getRNG().nextInt(5);
        PathEntity pathentity = taskOwner.getNavigator().getPathToEntityLiving(par1EntityLivingBase);

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
            int i = pathpoint.xCoord - MathHelper.floor_double(par1EntityLivingBase.posX);
            int j = pathpoint.zCoord - MathHelper.floor_double(par1EntityLivingBase.posZ);
            return (double)(i * i + j * j) <= 2.25D;
        }
    }
}
