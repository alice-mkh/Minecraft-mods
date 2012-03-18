package net.minecraft.src;

import java.util.*;

public class EntityAIMate extends EntityAIBase
{
    private EntityAnimal field_48259_d;
    World field_48263_a;
    private EntityAnimal field_48260_e;
    int field_48261_b;
    float field_48262_c;

    public EntityAIMate(EntityAnimal par1EntityAnimal, float par2)
    {
        field_48261_b = 0;
        field_48259_d = par1EntityAnimal;
        field_48263_a = par1EntityAnimal.worldObj;
        field_48262_c = par2;
        func_46079_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48259_d.func_48136_o_())
        {
            return false;
        }
        else
        {
            field_48260_e = func_48258_h();
            return field_48260_e != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48260_e.isEntityAlive() && field_48260_e.func_48136_o_() && field_48261_b < 60;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48260_e = null;
        field_48261_b = 0;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        field_48259_d.getLookHelper().setLookPositionWithEntity(field_48260_e, 10F, field_48259_d.getVerticalFaceSpeed());
        field_48259_d.func_48084_aL().func_48667_a(field_48260_e, field_48262_c);
        field_48261_b++;

        if (field_48261_b == 60)
        {
            func_48257_i();
        }
    }

    private EntityAnimal func_48258_h()
    {
        float f = 8F;
        List list = field_48263_a.getEntitiesWithinAABB(field_48259_d.getClass(), field_48259_d.boundingBox.expand(f, f, f));

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            Entity entity = (Entity)iterator.next();
            EntityAnimal entityanimal = (EntityAnimal)entity;

            if (field_48259_d.func_48135_b(entityanimal))
            {
                return entityanimal;
            }
        }

        return null;
    }

    private void func_48257_i()
    {
        EntityAnimal entityanimal = field_48259_d.spawnBabyAnimal(field_48260_e);

        if (entityanimal == null)
        {
            return;
        }

        field_48259_d.func_48122_d(6000);
        field_48260_e.func_48122_d(6000);
        field_48259_d.func_48134_p_();
        field_48260_e.func_48134_p_();
        entityanimal.func_48122_d(-24000);
        entityanimal.setLocationAndAngles(field_48259_d.posX, field_48259_d.posY, field_48259_d.posZ, 0.0F, 0.0F);
        field_48263_a.spawnEntityInWorld(entityanimal);
        field_48259_d.breeded = true;
        field_48260_e.breeded = true;
        entityanimal.breeded = true;
        Random random = field_48259_d.getRNG();

        for (int i = 0; i < 7; i++)
        {
            double d = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            field_48263_a.spawnParticle("heart", (field_48259_d.posX + (double)(random.nextFloat() * field_48259_d.width * 2.0F)) - (double)field_48259_d.width, field_48259_d.posY + 0.5D + (double)(random.nextFloat() * field_48259_d.height), (field_48259_d.posZ + (double)(random.nextFloat() * field_48259_d.width * 2.0F)) - (double)field_48259_d.width, d, d1, d2);
        }
    }
}
