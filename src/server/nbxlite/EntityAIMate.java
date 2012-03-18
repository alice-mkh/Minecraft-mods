package net.minecraft.src;

import java.util.*;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal field_48199_d;
    World field_48203_a;
    private EntityAnimal field_48200_e;
    int field_48201_b;
    float field_48202_c;

    public EntityAIMate(EntityAnimal par1EntityAnimal, float par2)
    {
        field_48201_b = 0;
        field_48199_d = par1EntityAnimal;
        field_48203_a = par1EntityAnimal.worldObj;
        field_48202_c = par2;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48199_d.func_48363_r_())
        {
            return false;
        }
        else
        {
            field_48200_e = func_48198_f();
            return field_48200_e != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48200_e.isEntityAlive() && field_48200_e.func_48363_r_() && field_48201_b < 60;
    }

    public void resetTask()
    {
        field_48200_e = null;
        field_48201_b = 0;
    }

    public void updateTask()
    {
        field_48199_d.getLookHelper().setLookPositionWithEntity(field_48200_e, 10F, field_48199_d.getVerticalFaceSpeed());
        field_48199_d.func_48333_ak().func_48652_a(field_48200_e, field_48202_c);
        field_48201_b++;

        if (field_48201_b == 60)
        {
            func_48197_i();
        }
    }

    private EntityAnimal func_48198_f()
    {
        float f = 8F;
        List list = field_48203_a.getEntitiesWithinAABB(field_48199_d.getClass(), field_48199_d.boundingBox.expand(f, f, f));

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Entity entity = (Entity)iterator.next();
            EntityAnimal entityanimal = (EntityAnimal)entity;

            if (field_48199_d.func_48362_b(entityanimal))
            {
                return entityanimal;
            }
        }

        return null;
    }

    private void func_48197_i()
    {
        EntityAnimal entityanimal = field_48199_d.spawnBabyAnimal(field_48200_e);

        if (entityanimal == null)
        {
            return;
        }

        field_48199_d.func_48350_c(6000);
        field_48200_e.func_48350_c(6000);
        field_48199_d.breeded = false;
        field_48200_e.breeded = false;
        entityanimal.breeded = false;
        field_48199_d.func_48364_s_();
        field_48200_e.func_48364_s_();
        entityanimal.func_48350_c(-24000);
        entityanimal.setLocationAndAngles(field_48199_d.posX, field_48199_d.posY, field_48199_d.posZ, 0.0F, 0.0F);
        field_48203_a.spawnEntityInWorld(entityanimal);
        Random random = field_48199_d.getRNG();

        for (int i = 0; i < 7; i++)
        {
            double d = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            field_48203_a.spawnParticle("heart", (field_48199_d.posX + (double)(random.nextFloat() * field_48199_d.width * 2.0F)) - (double)field_48199_d.width, field_48199_d.posY + 0.5D + (double)(random.nextFloat() * field_48199_d.height), (field_48199_d.posZ + (double)(random.nextFloat() * field_48199_d.width * 2.0F)) - (double)field_48199_d.width, d, d1, d2);
        }
    }
}
