package net.minecraft.src;

public class EntityDiggingFX extends EntityFX
{
    public static boolean oldparticles = false;

    private Block blockInstance;

    public EntityDiggingFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12, Block par14Block, int par15, int par16)
    {
        super(par1World, par2, par4, par6, par8, par10, par12);
        blockInstance = par14Block;
        setParticleTextureIndex(par14Block.getBlockTextureFromSideAndMetadata(oldparticles ? 2 : 0, par16));
        particleGravity = par14Block.blockParticleGravity;
        particleRed = particleGreen = particleBlue = 0.6F;
        particleScale /= 2.0F;
    }

    public EntityDiggingFX func_70596_a(int par1, int par2, int par3)
    {
        if (blockInstance == Block.grass)
        {
            return this;
        }
        else
        {
            int i = blockInstance.colorMultiplier(worldObj, par1, par2, par3);
            particleRed *= (float)(i >> 16 & 0xff) / 255F;
            particleGreen *= (float)(i >> 8 & 0xff) / 255F;
            particleBlue *= (float)(i & 0xff) / 255F;
            return this;
        }
    }

    public int getFXLayer()
    {
        return 1;
    }

    public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        float f = ((float)(getParticleTextureIndex() % 16) + particleTextureJitterX / 4F) / 16F;
        float f1 = f + 0.01560938F;
        float f2 = ((float)(getParticleTextureIndex() / 16) + particleTextureJitterY / 4F) / 16F;
        float f3 = f2 + 0.01560938F;
        float f4 = 0.1F * particleScale;
        float f5 = (float)((prevPosX + (posX - prevPosX) * (double)par2) - interpPosX);
        float f6 = (float)((prevPosY + (posY - prevPosY) * (double)par2) - interpPosY);
        float f7 = (float)((prevPosZ + (posZ - prevPosZ) * (double)par2) - interpPosZ);
        float f8 = 1.0F;
        par1Tessellator.setColorOpaque_F(f8 * particleRed, f8 * particleGreen, f8 * particleBlue);
        par1Tessellator.addVertexWithUV(f5 - par3 * f4 - par6 * f4, f6 - par4 * f4, f7 - par5 * f4 - par7 * f4, f, f3);
        par1Tessellator.addVertexWithUV((f5 - par3 * f4) + par6 * f4, f6 + par4 * f4, (f7 - par5 * f4) + par7 * f4, f, f2);
        par1Tessellator.addVertexWithUV(f5 + par3 * f4 + par6 * f4, f6 + par4 * f4, f7 + par5 * f4 + par7 * f4, f1, f2);
        par1Tessellator.addVertexWithUV((f5 + par3 * f4) - par6 * f4, f6 - par4 * f4, (f7 + par5 * f4) - par7 * f4, f1, f3);
    }
}
