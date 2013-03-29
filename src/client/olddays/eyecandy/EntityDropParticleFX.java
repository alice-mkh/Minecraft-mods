package net.minecraft.src;

public class EntityDropParticleFX extends EntityFX
{
    public static boolean allow = true;

    /** the material type for dropped items/blocks */
    private Material materialType;

    /** The height of the current bob */
    private int bobTimer;

    public EntityDropParticleFX(World par1World, double par2, double par4, double par6, Material par8Material)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
        motionX = motionY = motionZ = 0.0D;

        if (par8Material == Material.water)
        {
            particleRed = 0.0F;
            particleGreen = 0.0F;
            particleBlue = 1.0F;
        }
        else
        {
            particleRed = 1.0F;
            particleGreen = 0.0F;
            particleBlue = 0.0F;
        }

        setParticleTextureIndex(113);
        setSize(0.01F, 0.01F);
        particleGravity = 0.06F;
        materialType = par8Material;
        bobTimer = 40;
        particleMaxAge = (int)(64D / (Math.random() * 0.80000000000000004D + 0.20000000000000001D));
        motionX = motionY = motionZ = 0.0D;
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

    public int getBrightnessForRender(float par1)
    {
        if (materialType == Material.water)
        {
            return super.getBrightnessForRender(par1);
        }
        else
        {
            return 257;
        }
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float par1)
    {
        if (materialType == Material.water)
        {
            return super.getBrightness(par1);
        }
        else
        {
            return 1.0F;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        if (materialType == Material.water)
        {
            particleRed = 0.2F;
            particleGreen = 0.3F;
            particleBlue = 1.0F;
        }
        else
        {
            particleRed = 1.0F;
            particleGreen = 16F / (float)((40 - bobTimer) + 16);
            particleBlue = 4F / (float)((40 - bobTimer) + 8);
        }

        motionY -= particleGravity;

        if (bobTimer-- > 0)
        {
            motionX *= 0.02D;
            motionY *= 0.02D;
            motionZ *= 0.02D;
            setParticleTextureIndex(113);
        }
        else
        {
            setParticleTextureIndex(112);
        }

        moveEntity(motionX, motionY, motionZ);
        motionX *= 0.98000001907348633D;
        motionY *= 0.98000001907348633D;
        motionZ *= 0.98000001907348633D;

        if (particleMaxAge-- <= 0)
        {
            setDead();
        }

        if (onGround)
        {
            if (materialType == Material.water)
            {
                setDead();
                worldObj.spawnParticle("splash", posX, posY, posZ, 0.0D, 0.0D, 0.0D);
            }
            else
            {
                setParticleTextureIndex(114);
            }

            motionX *= 0.69999998807907104D;
            motionZ *= 0.69999998807907104D;
        }

        Material material = worldObj.getBlockMaterial(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));

        if (material.isLiquid() || material.isSolid())
        {
            double d = (float)(MathHelper.floor_double(posY) + 1) - BlockFluid.getFluidHeightPercent(worldObj.getBlockMetadata(MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ)));

            if (posY < d)
            {
                setDead();
            }
        }
    }
}
