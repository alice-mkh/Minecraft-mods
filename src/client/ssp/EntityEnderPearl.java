package net.minecraft.src;

import java.util.Random;

public class EntityEnderPearl extends EntityThrowable
{
    public EntityEnderPearl(World par1World)
    {
        super(par1World);
    }

    public EntityEnderPearl(World par1World, EntityLivingBase par2EntityLivingBase)
    {
        super(par1World, par2EntityLivingBase);
    }

    public EntityEnderPearl(World par1World, double par2, double par4, double par6)
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

        for (int i = 0; i < 32; i++)
        {
            worldObj.spawnParticle("portal", posX, posY + rand.nextDouble() * 2D, posZ, rand.nextGaussian(), 0.0D, rand.nextGaussian());
        }

        if (!worldObj.isRemote)
        {
            if (getThrower() != null && Minecraft.getMinecraft().enableSP)
            {
                getThrower().setPositionAndUpdate(posX, posY, posZ);
                getThrower().fallDistance = 0.0F;
                getThrower().attackEntityFrom(DamageSource.fall, 5);
            }else
            if (getThrower() != null && (getThrower() instanceof EntityPlayerMP))
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)getThrower();

                if (!entityplayermp.playerNetServerHandler.connectionClosed && entityplayermp.worldObj == worldObj)
                {
                    if (getThrower().isRiding())
                    {
                        getThrower().mountEntity(null);
                    }

                    getThrower().setPositionAndUpdate(posX, posY, posZ);
                    getThrower().fallDistance = 0.0F;
                    getThrower().attackEntityFrom(DamageSource.fall, 5F);
                }
            }

            setDead();
        }
    }
}
