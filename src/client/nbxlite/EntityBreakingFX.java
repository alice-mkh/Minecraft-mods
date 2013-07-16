package net.minecraft.src;

public class EntityBreakingFX extends EntityFX
{
    public EntityBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item)
    {
        this(par1World, par2, par4, par6, par8Item, 0);
    }

    public EntityBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item, int par9)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D);
        func_110125_a(par8Item.getIconFromDamage(par9));
        particleRed = particleGreen = particleBlue = 1.0F;
        particleGravity = Block.blockSnow.blockParticleGravity;
        particleScale /= 2.0F;
    }

    public EntityBreakingFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, Item par14Item, int par15)
    {
        this(par1World, par2, par4, par6, par14Item, par15);
        motionX *= 0.10000000149011612D;
        motionY *= 0.10000000149011612D;
        motionZ *= 0.10000000149011612D;
        motionX += par8;
        motionY += par10;
        motionZ += par12;
    }

    public int getFXLayer()
    {
        return 2;
    }

    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        float f = ((float)particleTextureIndexX + particleTextureJitterX / 4F) / 16F;
        float f1 = f + 0.01560938F;
        float f2 = ((float)particleTextureIndexY + particleTextureJitterY / 4F) / 16F;
        float f3 = f2 + 0.01560938F;
        float f4 = 0.1F * particleScale;

        if (particleIcon != null)
        {
            f = particleIcon.getInterpolatedU((particleTextureJitterX / 4F) * 16F);
            f1 = particleIcon.getInterpolatedU(((particleTextureJitterX + 1.0F) / 4F) * 16F);
            f2 = particleIcon.getInterpolatedV((particleTextureJitterY / 4F) * 16F);
            f3 = particleIcon.getInterpolatedV(((particleTextureJitterY + 1.0F) / 4F) * 16F);
        }

        float f5 = (float)((prevPosX + (posX - prevPosX) * (double)par2) - interpPosX);
        float f6 = (float)((prevPosY + (posY - prevPosY) * (double)par2) - interpPosY);
        float f7 = (float)((prevPosZ + (posZ - prevPosZ) * (double)par2) - interpPosZ);
        float f8 = Minecraft.oldlighting ? getBrightness(par2) : 1.0F;
        par1Tessellator.setColorOpaque_F(f8 * particleRed, f8 * particleGreen, f8 * particleBlue);
        par1Tessellator.addVertexWithUV(f5 - par3 * f4 - par6 * f4, f6 - par4 * f4, f7 - par5 * f4 - par7 * f4, f, f3);
        par1Tessellator.addVertexWithUV((f5 - par3 * f4) + par6 * f4, f6 + par4 * f4, (f7 - par5 * f4) + par7 * f4, f, f2);
        par1Tessellator.addVertexWithUV(f5 + par3 * f4 + par6 * f4, f6 + par4 * f4, f7 + par5 * f4 + par7 * f4, f1, f2);
        par1Tessellator.addVertexWithUV((f5 + par3 * f4) - par6 * f4, f6 - par4 * f4, (f7 + par5 * f4) - par7 * f4, f1, f3);
    }
}
