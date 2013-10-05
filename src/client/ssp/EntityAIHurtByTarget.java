package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityAIHurtByTarget extends EntityAITarget
{
    public static boolean pre15 = false;

    boolean entityCallsForHelp;
    private int field_142052_b;

    public EntityAIHurtByTarget(EntityCreature par1EntityCreature, boolean par2)
    {
        super(par1EntityCreature, false);
        entityCallsForHelp = par2;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        int i = taskOwner.func_142015_aE();
        return i != field_142052_b && isSuitableTarget(taskOwner.getAITarget(), false);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(taskOwner.getAITarget());
        field_142052_b = taskOwner.func_142015_aE();

        if (entityCallsForHelp)
        {
            double d = getTargetDistance();
            List list = taskOwner.worldObj.getEntitiesWithinAABB(taskOwner.getClass(), AxisAlignedBB.getAABBPool().getAABB(taskOwner.posX, taskOwner.posY, taskOwner.posZ, taskOwner.posX + 1.0D, taskOwner.posY + 1.0D, taskOwner.posZ + 1.0D).expand(d, pre15 ? 4D : 10D, d));
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                EntityCreature entitycreature = (EntityCreature)iterator.next();

                if (taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(taskOwner.getAITarget()))
                {
                    entitycreature.setAttackTarget(taskOwner.getAITarget());
                }
            }
            while (true);
        }

        super.startExecuting();
    }
}
