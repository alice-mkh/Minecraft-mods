package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderHuman extends RenderLiving{
    private static final ResourceLocation texture1 = new ResourceLocation("textures/entity/steve.png");
    private static final ResourceLocation texture2 = new ResourceLocation("char.png");

    public RenderHuman(ModelHuman modelhuman, float f){
        super(modelhuman, 0.5F);
    }

    @Override
    protected ResourceLocation func_110775_a(Entity par1Entity)
    {
        return mod_SpawnHuman.CustomTexture ? texture2 : texture1;
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        mainModel.onGround = renderSwingProgress(par1EntityLiving, par9);

        if (renderPassModel != null)
        {
            renderPassModel.onGround = mainModel.onGround;
        }

        mainModel.isRiding = par1EntityLiving.isRiding();

        if (renderPassModel != null)
        {
            renderPassModel.isRiding = mainModel.isRiding;
        }

        mainModel.isChild = par1EntityLiving.isChild();

        if (renderPassModel != null)
        {
            renderPassModel.isChild = mainModel.isChild;
        }

        try
        {
            float f = interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
            float f1 = interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
            float f2 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
            renderLivingAt(par1EntityLiving, par2, par4, par6);
            float f3 = handleRotationFloat(par1EntityLiving, par9);
            rotateCorpse(par1EntityLiving, f3, f, par9);
            float f4 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1F, -1F, 1.0F);
            preRenderCallback(par1EntityLiving, par9);
            float f5 = par1EntityLiving.prevLimbYaw + (par1EntityLiving.limbYaw - par1EntityLiving.prevLimbYaw) * par9;
            float f6 = par1EntityLiving.field_70763_ax + (par1EntityLiving.field_70764_aw - par1EntityLiving.field_70763_ax) * par9;
            float f32 = par1EntityLiving.field_70768_au + (par1EntityLiving.field_110154_aX - par1EntityLiving.field_70768_au) * par9;
            float bob = -Math.abs(MathHelper.cos(f6 * 0.6662F)) * 5F * f32 - 23F;
            GL11.glTranslatef(0.0F, bob * f4 - 0.0078125F, 0.0F);

            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            mainModel.setLivingAnimations(par1EntityLiving, f6, f5, par9);
            renderModel(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);
            GL11.glDepthMask(true);
            renderEquippedItems(par1EntityLiving, par9);
            float f7 = par1EntityLiving.getBrightness(par9);
            int k = getColorMultiplier(par1EntityLiving, f7, par9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((k >> 24 & 0xff) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0)
                {
                    GL11.glColor4f(f7, 0.0F, 0.0F, 0.4F);
                    mainModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);

                    for (int l = 0; l < 4; l++)
                    {
                        if (inheritRenderPass(par1EntityLiving, l, par9) >= 0)
                        {
                            GL11.glColor4f(f7, 0.0F, 0.0F, 0.4F);
                            renderPassModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);
                        }
                    }
                }

                if ((k >> 24 & 0xff) > 0)
                {
                    float f9 = (float)(k >> 16 & 0xff) / 255F;
                    float f11 = (float)(k >> 8 & 0xff) / 255F;
                    float f12 = (float)(k & 0xff) / 255F;
                    float f14 = (float)(k >> 24 & 0xff) / 255F;
                    GL11.glColor4f(f9, f11, f12, f14);
                    mainModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);

                    for (int j1 = 0; j1 < 4; j1++)
                    {
                        if (inheritRenderPass(par1EntityLiving, j1, par9) >= 0)
                        {
                            GL11.glColor4f(f9, f11, f12, f14);
                            renderPassModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);
                        }
                    }
                }

                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();
        passSpecialRender(par1EntityLiving, par2, par4, par6);
    }

    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    private float interpolateRotation(float par1, float par2, float par3)
    {
        float f;

        for (f = par2 - par1; f < -180F; f += 360F) { }

        for (; f >= 180F; f -= 360F) { }

        return par1 + par3 * f;
    }
}