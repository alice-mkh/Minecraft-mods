package net.minecraft.src;

import java.util.*;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal theAnimal;
    World theWorld;
    private EntityAnimal targetMate;
    int field_48201_b;
    float field_48202_c;

    public EntityAIMate(EntityAnimal par1EntityAnimal, float par2)
    {
        field_48201_b = 0;
        theAnimal = par1EntityAnimal;
        theWorld = par1EntityAnimal.worldObj;
        field_48202_c = par2;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!theAnimal.isInLove())
        {
            return false;
        }
        else
        {
            targetMate = func_48198_f();
            return targetMate != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return targetMate.isEntityAlive() && targetMate.isInLove() && field_48201_b < 60;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        targetMate = null;
        field_48201_b = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        theAnimal.getLookHelper().setLookPositionWithEntity(targetMate, 10F, theAnimal.getVerticalFaceSpeed());
        theAnimal.getNavigator().func_48652_a(targetMate, field_48202_c);
        field_48201_b++;

        if (field_48201_b == 60)
        {
            func_48197_i();
        }
    }

    private EntityAnimal func_48198_f()
    {
        float f = 8F;
        List list = theWorld.getEntitiesWithinAABB(theAnimal.getClass(), theAnimal.boundingBox.expand(f, f, f));

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Entity entity = (Entity)iterator.next();
            EntityAnimal entityanimal = (EntityAnimal)entity;

            if (theAnimal.func_48362_b(entityanimal))
            {
                return entityanimal;
            }
        }

        return null;
    }

    private void func_48197_i()
    {
        EntityAnimal entityanimal = theAnimal.spawnBabyAnimal(targetMate);

        if (entityanimal == null)
        {
            return;
        }

        theAnimal.setGrowingAge(6000);
        targetMate.setGrowingAge(6000);
        theAnimal.breeded = true;
        targetMate.breeded = true;
        entityanimal.breeded = true;
        theAnimal.resetInLove();
        targetMate.resetInLove();
        entityanimal.setGrowingAge(-24000);
        entityanimal.setLocationAndAngles(theAnimal.posX, theAnimal.posY, theAnimal.posZ, 0.0F, 0.0F);
        theWorld.spawnEntityInWorld(entityanimal);
        Random random = theAnimal.getRNG();

        for (int i = 0; i < 7; i++)
        {
            double d = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            theWorld.spawnParticle("heart", (theAnimal.posX + (double)(random.nextFloat() * theAnimal.width * 2.0F)) - (double)theAnimal.width, theAnimal.posY + 0.5D + (double)(random.nextFloat() * theAnimal.height), (theAnimal.posZ + (double)(random.nextFloat() * theAnimal.width * 2.0F)) - (double)theAnimal.width, d, d1, d2);
        }
    }
}
