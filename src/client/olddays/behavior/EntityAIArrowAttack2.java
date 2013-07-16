package net.minecraft.src;

public class EntityAIArrowAttack2 extends EntityAIBase
{
    /** The entity the AI instance has been applied to */
    private final EntityLiving entityHost;

    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;

    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackTime.
     */
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int field_75318_f;
    private int field_96561_g;

    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float field_82642_h;

    public EntityAIArrowAttack2(IRangedAttackMob par1IRangedAttackMob, double par2, int par3, float par4)
    {
        this(par1IRangedAttackMob, par2, par3, par3, par4);
    }

    public EntityAIArrowAttack2(IRangedAttackMob par1IRangedAttackMob, double par2, int par3, int par4, float par5)
    {
        rangedAttackTime = -1;

        if (!(par1IRangedAttackMob instanceof EntityLivingBase))
        {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        else
        {
            rangedAttackEntityHost = par1IRangedAttackMob;
            entityHost = (EntityLiving)par1IRangedAttackMob;
            entityMoveSpeed = par2;
            field_96561_g = par3;
            maxRangedAttackTime = par4;
            field_96562_i = par5;
            field_82642_h = par5 * par5;
            setMutexBits(3);
            return;
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = entityHost.getAttackTarget();

        if (entitylivingbase == null)
        {
            return false;
        }
        else
        {
            attackTarget = entitylivingbase;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return shouldExecute() || !entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    @Override
    public void resetTask()
    {
        attackTarget = null;
        field_75318_f = 0;
        rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    @Override
    public void updateTask()
    {
        double d = entityHost.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
        boolean flag = entityHost.getEntitySenses().canSee(attackTarget);

        if (flag)
        {
            field_75318_f++;
        }
        else
        {
            field_75318_f = 0;
        }

        if (d > (double)field_82642_h || field_75318_f < 20)
        {
            entityHost.getNavigator().tryMoveToEntityLiving(attackTarget, entityMoveSpeed);
        }
        else
        {
            entityHost.getNavigator().clearPathEntity();
        }

        entityHost.getLookHelper().setLookPositionWithEntity(attackTarget, 30F, 30F);

        if (--rangedAttackTime == 0)
        {
            if (d > (double)field_82642_h || !flag)
            {
                return;
            }

            float f = MathHelper.sqrt_double(d) / field_96562_i;
            float f2 = f;

            if (f2 < 0.1F)
            {
                f2 = 0.1F;
            }

            if (f2 > 1.0F)
            {
                f2 = 1.0F;
            }

            rangedAttackEntityHost.attackEntityWithRangedAttack(attackTarget, f2);
            rangedAttackTime = MathHelper.floor_float(f * (float)(maxRangedAttackTime - field_96561_g) + (float)field_96561_g);
        }
        else if (rangedAttackTime < 0)
        {
            float f1 = MathHelper.sqrt_double(d) / field_96562_i;
            rangedAttackTime = MathHelper.floor_float(f1 * (float)(maxRangedAttackTime - field_96561_g) + (float)field_96561_g);
        }
    }
}
