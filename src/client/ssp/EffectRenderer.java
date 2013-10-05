package net.minecraft.src;

import java.util.*;
import org.lwjgl.opengl.GL11;

public class EffectRenderer
{
    public static boolean fixParticles = true;

    private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");

    /** Reference to the World object. */
    protected World worldObj;
    private List fxLayers[];
    private TextureManager renderer;

    /** RNG. */
    private Random rand;

    public EffectRenderer(World par1World, TextureManager par2TextureManager)
    {
        fxLayers = new List[4];
        rand = new Random();

        if (par1World != null)
        {
            worldObj = par1World;
        }

        renderer = par2TextureManager;

        for (int i = 0; i < 4; i++)
        {
            fxLayers[i] = new ArrayList();
        }
    }

    public void addEffect(EntityFX par1EntityFX)
    {
        int i = par1EntityFX.getFXLayer();

        if (fxLayers[i].size() >= 4000)
        {
            fxLayers[i].remove(0);
        }

        fxLayers[i].add(par1EntityFX);
    }

    public void updateEffects()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < fxLayers[i].size(); j++)
            {
                EntityFX entityfx = (EntityFX)fxLayers[i].get(j);
                entityfx.onUpdate();

                if (entityfx.isDead)
                {
                    fxLayers[i].remove(j--);
                }
            }
        }
    }

    /**
     * Renders all current particles. Args player, partialTickTime
     */
    public void renderParticles(Entity par1Entity, float par2)
    {
        float f = ActiveRenderInfo.rotationX;
        float f1 = ActiveRenderInfo.rotationZ;
        float f2 = ActiveRenderInfo.rotationYZ;
        float f3 = ActiveRenderInfo.rotationXY;
        float f4 = ActiveRenderInfo.rotationXZ;
        EntityFX.interpPosX = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
        EntityFX.interpPosY = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
        EntityFX.interpPosZ = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;

        for (int i = 0; i < 3; i++)
        {
            if (fxLayers[i].isEmpty())
            {
                continue;
            }

            switch (i)
            {
                case 0:
                default:
                    renderer.bindTexture(particleTextures);
                    break;
                case 1:
                    renderer.bindTexture(TextureMap.locationBlocksTexture);
                    break;
                case 2:
                    renderer.bindTexture(TextureMap.locationItemsTexture);
                    break;
            }

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (!fixParticles){
                GL11.glDepthMask(false);
            }
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();

            for (int j = 0; j < fxLayers[i].size(); j++)
            {
                EntityFX entityfx = (EntityFX)fxLayers[i].get(j);
                tessellator.setBrightness(entityfx.getBrightnessForRender(par2));
                entityfx.renderParticle(tessellator, par2, f, f4, f1, f2, f3);
            }

            tessellator.draw();
            GL11.glDisable(GL11.GL_BLEND);
            if (!fixParticles){
                GL11.glDepthMask(true);
            }
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        }
    }

    public void renderLitParticles(Entity par1Entity, float par2)
    {
        float f = 0.01745329F;
        float f1 = MathHelper.cos(par1Entity.rotationYaw * 0.01745329F);
        float f2 = MathHelper.sin(par1Entity.rotationYaw * 0.01745329F);
        float f3 = -f2 * MathHelper.sin(par1Entity.rotationPitch * 0.01745329F);
        float f4 = f1 * MathHelper.sin(par1Entity.rotationPitch * 0.01745329F);
        float f5 = MathHelper.cos(par1Entity.rotationPitch * 0.01745329F);
        byte byte0 = 3;
        List list = fxLayers[byte0];

        if (list.isEmpty())
        {
            return;
        }

        Tessellator tessellator = Tessellator.instance;

        for (int i = 0; i < list.size(); i++)
        {
            EntityFX entityfx = (EntityFX)list.get(i);
            tessellator.setBrightness(entityfx.getBrightnessForRender(par2));
            entityfx.renderParticle(tessellator, par2, f1, f5, f2, f3, f4);
        }
    }

    public void clearEffects(World par1World)
    {
        worldObj = par1World;

        for (int i = 0; i < 4; i++)
        {
            fxLayers[i].clear();
        }
    }

    public void addBlockDestroyEffects(int par1, int par2, int par3, int par4, int par5)
    {
        if (par4 == 0)
        {
            return;
        }

        Block block = Block.blocksList[par4];
        int i = 4;

        for (int j = 0; j < i; j++)
        {
            for (int k = 0; k < i; k++)
            {
                for (int l = 0; l < i; l++)
                {
                    double d = (double)par1 + ((double)j + 0.5D) / (double)i;
                    double d1 = (double)par2 + ((double)k + 0.5D) / (double)i;
                    double d2 = (double)par3 + ((double)l + 0.5D) / (double)i;
                    addEffect((new EntityDiggingFX(worldObj, d, d1, d2, d - (double)par1 - 0.5D, d1 - (double)par2 - 0.5D, d2 - (double)par3 - 0.5D, block, par5)).applyColourMultiplier(par1, par2, par3));
                }
            }
        }
    }

    /**
     * Adds block hit particles for the specified block. Args: x, y, z, sideHit
     */
    public void addBlockHitEffects(int par1, int par2, int par3, int par4)
    {
        int i = worldObj.getBlockId(par1, par2, par3);

        if (i == 0)
        {
            return;
        }

        Block block = Block.blocksList[i];
        float f = 0.1F;
        double d = (double)par1 + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
        double d1 = (double)par2 + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
        double d2 = (double)par3 + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

        if (par4 == 0)
        {
            d1 = ((double)par2 + block.getBlockBoundsMinY()) - (double)f;
        }

        if (par4 == 1)
        {
            d1 = (double)par2 + block.getBlockBoundsMaxY() + (double)f;
        }

        if (par4 == 2)
        {
            d2 = ((double)par3 + block.getBlockBoundsMinZ()) - (double)f;
        }

        if (par4 == 3)
        {
            d2 = (double)par3 + block.getBlockBoundsMaxZ() + (double)f;
        }

        if (par4 == 4)
        {
            d = ((double)par1 + block.getBlockBoundsMinX()) - (double)f;
        }

        if (par4 == 5)
        {
            d = (double)par1 + block.getBlockBoundsMaxX() + (double)f;
        }

        addEffect((new EntityDiggingFX(worldObj, d, d1, d2, 0.0D, 0.0D, 0.0D, block, worldObj.getBlockMetadata(par1, par2, par3))).applyColourMultiplier(par1, par2, par3).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
    }

    public String getStatistics()
    {
        return (new StringBuilder()).append("").append(fxLayers[0].size() + fxLayers[1].size() + fxLayers[2].size()).toString();
    }
}
