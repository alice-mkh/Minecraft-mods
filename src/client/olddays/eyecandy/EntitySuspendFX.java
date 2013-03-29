package net.minecraft.src;

import java.util.Random;

public class EntitySuspendFX extends EntityFX
{
    public static boolean allow = true;

    public EntitySuspendFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        super(par1World, par2, par4 - 0.125D, par6, par8, par10, par12);
        particleRed = 0.4F;
        particleGreen = 0.4F;
        particleBlue = 0.7F;
        setParticleTextureIndex(0);
        setSize(0.01F, 0.01F);
        particleScale *= rand.nextFloat() * 0.6F + 0.2F;
        motionX = par8 * 0.0D;
        motionY = par10 * 0.0D;
        motionZ = par12 * 0.0D;
        particleMaxAge = (int)(16D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
    }

    @Override
    protected void entityInit()
    {
        if (!allow){
            setDead();
            return;
        }
        super.entityInit();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        moveEntity(motionX, motionY, motionZ);

        if (worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)) != Material.water)
        {
            setDead();
        }

        if (particleMaxAge-- <= 0)
        {
            setDead();
        }
    }
}
