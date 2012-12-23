package net.minecraft.src;

import java.util.*;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal theAnimal;
    World theWorld;
    private EntityAnimal targetMate;

    /**
     * Delay preventing a baby from spawning immediately when two mate-able animals find each other.
     */
    int spawnBabyDelay;

    /** The speed the creature moves at during mating behavior. */
    float moveSpeed;

    public EntityAIMate(EntityAnimal par1EntityAnimal, float par2)
    {
        spawnBabyDelay = 0;
        theAnimal = par1EntityAnimal;
        theWorld = par1EntityAnimal.worldObj;
        moveSpeed = par2;
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
            targetMate = getNearbyMate();
            return targetMate != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return targetMate.isEntityAlive() && targetMate.isInLove() && spawnBabyDelay < 60;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        targetMate = null;
        spawnBabyDelay = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        theAnimal.getLookHelper().setLookPositionWithEntity(targetMate, 10F, theAnimal.getVerticalFaceSpeed());
        theAnimal.getNavigator().tryMoveToEntityLiving(targetMate, moveSpeed);
        spawnBabyDelay++;

        if (spawnBabyDelay == 60)
        {
            spawnBaby();
        }
    }

    /**
     * Loops through nearby animals and finds another animal of the same type that can be mated with. Returns the first
     * valid mate found.
     */
    private EntityAnimal getNearbyMate()
    {
        float f = 8F;
        List list = theWorld.getEntitiesWithinAABB(theAnimal.getClass(), theAnimal.boundingBox.expand(f, f, f));

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            EntityAnimal entityanimal = (EntityAnimal)iterator.next();

            if (theAnimal.canMateWith(entityanimal))
            {
                return entityanimal;
            }
        }

        return null;
    }

    /**
     * Spawns a baby animal of the same type.
     */
    private void spawnBaby()
    {
        EntityAgeable entityageable = theAnimal.func_90011_a(targetMate);

        if (entityageable == null)
        {
            return;
        }

        theAnimal.setGrowingAge(6000);
        targetMate.setGrowingAge(6000);
        theAnimal.resetInLove();
        targetMate.resetInLove();
        entityageable.setGrowingAge(-24000);
        entityageable.setLocationAndAngles(theAnimal.posX, theAnimal.posY, theAnimal.posZ, 0.0F, 0.0F);
        theWorld.spawnEntityInWorld(entityageable);
        theAnimal.breeded = true;
        targetMate.breeded = true;
        if (entityageable instanceof EntityAnimal){
            ((EntityAnimal)entityageable).breeded = true;
        }
        Random random = theAnimal.getRNG();

        for (int i = 0; i < 7; i++)
        {
            double d = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            theWorld.spawnParticle("heart", (theAnimal.posX + (double)(random.nextFloat() * theAnimal.width * 2.0F)) - (double)theAnimal.width, theAnimal.posY + 0.5D + (double)(random.nextFloat() * theAnimal.height), (theAnimal.posZ + (double)(random.nextFloat() * theAnimal.width * 2.0F)) - (double)theAnimal.width, d, d1, d2);
        }

        theWorld.spawnEntityInWorld(new EntityXPOrb(theWorld, theAnimal.posX, theAnimal.posY, theAnimal.posZ, random.nextInt(7) + 1));
    }
}
