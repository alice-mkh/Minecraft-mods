package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;

public class EntityAIHurtByTarget extends EntityAITarget
{
    boolean field_75312_a;

    /** The PathNavigate of our entity. */
    EntityLiving entityPathNavigate;

    public EntityAIHurtByTarget(EntityLiving par1EntityLiving, boolean par2)
    {
        super(par1EntityLiving, 16F, false);
        field_75312_a = par2;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return isSuitableTarget(taskOwner.getAITarget(), true);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (Minecraft.getMinecraft().enableSP){
            return super.continueExecuting();
        }
        return taskOwner.getAITarget() != null && taskOwner.getAITarget() != entityPathNavigate;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        taskOwner.setAttackTarget(taskOwner.getAITarget());
        entityPathNavigate = taskOwner.getAITarget();

        if (field_75312_a)
        {
            List list = taskOwner.worldObj.getEntitiesWithinAABB(taskOwner.getClass(), AxisAlignedBB.getAABBPool().getAABB(taskOwner.posX, taskOwner.posY, taskOwner.posZ, taskOwner.posX + 1.0D, taskOwner.posY + 1.0D, taskOwner.posZ + 1.0D).expand(targetDistance, 10D, targetDistance));
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                EntityLiving entityliving = (EntityLiving)iterator.next();

                if (taskOwner != entityliving && entityliving.getAttackTarget() == null)
                {
                    entityliving.setAttackTarget(taskOwner.getAITarget());
                }
            }
            while (true);
        }

        super.startExecuting();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        if (taskOwner.getAttackTarget() != null && (taskOwner.getAttackTarget() instanceof EntityPlayer) && ((EntityPlayer)taskOwner.getAttackTarget()).capabilities.disableDamage)
        {
            super.resetTask();
        }
    }
}
