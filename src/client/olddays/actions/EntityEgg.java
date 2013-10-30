package net.minecraft.src;

import java.util.Random;

public class EntityEgg extends EntityThrowable
{
    public static boolean adult = false;

    public EntityEgg(World par1World)
    {
        super(par1World);
    }

    public EntityEgg(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }

    public EntityEgg(World par1World, double par2, double par4, double par6)
    {
        super(par1World, par2, par4, par6);
    }

    /**
     * Called when this EntityThrowable hits a block or entity.
     */
    protected void onImpact(MovingObjectPosition par1MovingObjectPosition)
    {
        if (par1MovingObjectPosition.entityHit != null)
        {
            par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 0.0F);
        }

        if (!worldObj.isRemote && rand.nextInt(8) == 0)
        {
            byte byte0 = 1;

            if (rand.nextInt(32) == 0)
            {
                byte0 = 4;
            }

            for (int j = 0; j < byte0; j++)
            {
                EntityChicken entitychicken = new EntityChicken(worldObj);
                if (!adult){
                    entitychicken.setGrowingAge(-24000);
                }
                entitychicken.setLocationAndAngles(posX, posY, posZ, rotationYaw, 0.0F);
                worldObj.spawnEntityInWorld(entitychicken);
            }
        }

        for (int i = 0; i < 8; i++)
        {
            worldObj.spawnParticle("snowballpoof", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
        }

        if (!worldObj.isRemote)
        {
            setDead();
        }
    }
}
