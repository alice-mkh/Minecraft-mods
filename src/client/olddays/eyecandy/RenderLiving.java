package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLiving extends Render
{
    public static boolean bobbing = false;
    public static boolean labels = false;
    public static boolean stick = false;
    public static boolean oldlabels = false;
    public static boolean oldHeadRotation = false;

    protected ModelBase mainModel;

    /** The model to be used during the render passes. */
    protected ModelBase renderPassModel;

    public RenderLiving(ModelBase par1ModelBase, float par2)
    {
        mainModel = par1ModelBase;
        shadowSize = par2;
    }

    /**
     * Sets the model to be used in the current render pass (the first render pass is done after the primary model is
     * rendered) Args: model
     */
    public void setRenderPassModel(ModelBase par1ModelBase)
    {
        renderPassModel = par1ModelBase;
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
            if (oldHeadRotation && par1EntityLiving == Minecraft.getMinecraft().thePlayer){
                f1 = interpolateRotation(par1EntityLiving.prevRotationYaw, par1EntityLiving.rotationYaw, par9);
            }
            float f2 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
            renderLivingAt(par1EntityLiving, par2, par4, par6);
            float f3 = handleRotationFloat(par1EntityLiving, par9);
            rotateCorpse(par1EntityLiving, f3, f, par9);
            float f4 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1F, -1F, 1.0F);
            preRenderCallback(par1EntityLiving, par9);
            if (!bobbing || par1EntityLiving.isChild()){
                GL11.glTranslatef(0.0F, -24F * f4 - 0.0078125F, 0.0F);
            }
            float f5 = par1EntityLiving.prevLegYaw + (par1EntityLiving.legYaw - par1EntityLiving.prevLegYaw) * par9;
            float f6;

            if (bobbing && !par1EntityLiving.isChild()){
                f6 = par1EntityLiving.field_70763_ax + (par1EntityLiving.field_70764_aw - par1EntityLiving.field_70763_ax) * par9;
                if (par1EntityLiving.isChild()){
                    f6 *= 3F;
                }
                float bobStrength = 0F;
                if (par1EntityLiving instanceof EntityZombie || 
                    par1EntityLiving instanceof EntitySkeleton || 
                    par1EntityLiving instanceof EntityCreeper || 
                    par1EntityLiving instanceof EntityPig || 
                    par1EntityLiving instanceof EntitySheep || 
                    par1EntityLiving instanceof EntityPlayer ||
                    par1EntityLiving instanceof EntityOtherPlayerMP){
                    bobStrength = 1.0F;
                }
                float f32 = par1EntityLiving.field_70768_au + (par1EntityLiving.field_70766_av - par1EntityLiving.field_70768_au) * par9;
                float bob = -Math.abs(MathHelper.cos(f6 * 0.6662F)) * 5F * f32 * bobStrength - 23F;
                GL11.glTranslatef(0.0F, bob * f4 - 0.0078125F, 0.0F);
            }else{
                f6 = par1EntityLiving.legSwing - par1EntityLiving.legYaw * (1.0F - par9);
                if (par1EntityLiving.isChild()){
                    f6 *= 3F;
                }
            }

            if (f5 > 1.0F)
            {
                f5 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            mainModel.setLivingAnimations(par1EntityLiving, f6, f5, par9);
            renderModel(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);

            for (int i = 0; i < 4; i++)
            {
                int j = shouldRenderPass(par1EntityLiving, i, par9);

                if (j <= 0)
                {
                    continue;
                }

                renderPassModel.setLivingAnimations(par1EntityLiving, f6, f5, par9);
                renderPassModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);

                if ((j & 0xf0) == 16)
                {
                    func_82408_c(par1EntityLiving, i, par9);
                    renderPassModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);
                }

                if ((j & 0xf) == 15)
                {
                    float f8 = (float)par1EntityLiving.ticksExisted + par9;
                    loadTexture("%blur%/misc/glint.png");
                    GL11.glEnable(GL11.GL_BLEND);
                    float f10 = 0.5F;
                    GL11.glColor4f(f10, f10, f10, 1.0F);
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDepthMask(false);

                    for (int i1 = 0; i1 < 2; i1++)
                    {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        float f13 = 0.76F;
                        GL11.glColor4f(0.5F * f13, 0.25F * f13, 0.8F * f13, 1.0F);
                        GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        float f15 = f8 * (0.001F + (float)i1 * 0.003F) * 20F;
                        float f16 = 0.3333333F;
                        GL11.glScalef(f16, f16, f16);
                        GL11.glRotatef(30F - (float)i1 * 60F, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(0.0F, f15, 0.0F);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        renderPassModel.render(par1EntityLiving, f6, f5, f3, f1 - f, f2, f4);
                    }

                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glDepthMask(true);
                    GL11.glLoadIdentity();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

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
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        if (!par1EntityLiving.getHasActivePotion())
        {
            loadDownloadableImageTexture(par1EntityLiving.skinUrl, par1EntityLiving.getTexture());
            mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
        }
        else
        {
            mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLiving);
        }
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
    }

    protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        GL11.glRotatef(180F - par3, 0.0F, 1.0F, 0.0F);

        if (par1EntityLiving.deathTime > 0)
        {
            float f = ((((float)par1EntityLiving.deathTime + par4) - 1.0F) / 20F) * 1.6F;
            f = MathHelper.sqrt_float(f);

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            GL11.glRotatef(f * getDeathMaxRotation(par1EntityLiving), 0.0F, 0.0F, 1.0F);
        }
    }

    protected float renderSwingProgress(EntityLiving par1EntityLiving, float par2)
    {
        return par1EntityLiving.getSwingProgress(par2);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2)
    {
        return (float)par1EntityLiving.ticksExisted + par2;
    }

    protected void renderEquippedItems(EntityLiving entityliving, float f)
    {
        if(stick)
        {
            renderArrowsStuckInEntity(entityliving, f);
        }
    }

    /**
     * renders arrows the Entity has been attacked with, attached to it
     */
    protected void renderArrowsStuckInEntity(EntityLiving par1EntityLiving, float par2)
    {
        int i = par1EntityLiving.getArrowCountInEntity();

        if (i > 0)
        {
            EntityArrow entityarrow = new EntityArrow(par1EntityLiving.worldObj, par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ);
            Random random = new Random(par1EntityLiving.entityId);
            RenderHelper.disableStandardItemLighting();

            for (int j = 0; j < i; j++)
            {
                GL11.glPushMatrix();
                ModelRenderer modelrenderer = mainModel.func_85181_a(random);
                ModelBox modelbox = (ModelBox)modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                modelrenderer.postRender(0.0625F);
                float f = random.nextFloat();
                float f1 = random.nextFloat();
                float f2 = random.nextFloat();
                float f3 = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * f) / 16F;
                float f4 = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * f1) / 16F;
                float f5 = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * f2) / 16F;
                GL11.glTranslatef(f3, f4, f5);
                f = f * 2.0F - 1.0F;
                f1 = f1 * 2.0F - 1.0F;
                f2 = f2 * 2.0F - 1.0F;
                f *= -1F;
                f1 *= -1F;
                f2 *= -1F;
                float f6 = MathHelper.sqrt_float(f * f + f2 * f2);
                entityarrow.prevRotationYaw = entityarrow.rotationYaw = (float)((Math.atan2(f, f2) * 180D) / Math.PI);
                entityarrow.prevRotationPitch = entityarrow.rotationPitch = (float)((Math.atan2(f1, f6) * 180D) / Math.PI);
                double d = 0.0D;
                double d1 = 0.0D;
                double d2 = 0.0D;
                float f7 = 0.0F;
                renderManager.renderEntityWithPosYaw(entityarrow, d, d1, d2, f7, par2);
                GL11.glPopMatrix();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return shouldRenderPass(par1EntityLiving, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return -1;
    }

    protected void func_82408_c(EntityLiving entityliving, int i, float f)
    {
    }

    protected float getDeathMaxRotation(EntityLiving par1EntityLiving)
    {
        return 90F;
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3)
    {
        return 0;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
    }

    /**
     * Passes the specialRender and renders it
     */
    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        if (Minecraft.isDebugInfoEnabled() && labels){
            if (par1EntityLiving instanceof EntityEnderman){
                renderLivingLabel(par1EntityLiving, Integer.toString(par1EntityLiving.entityId), par2, par4 + 1D, par6, 64);
            }else{
                renderLivingLabel(par1EntityLiving, Integer.toString(par1EntityLiving.entityId), par2, par4, par6, 64);
            }
        }
    }

    /**
     * Draws the debug or playername text above a living
     */
    protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
    {
        double d = par1EntityLiving.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d > (double)(par9 * par9) && !oldlabels)
        {
            return;
        }

        FontRenderer fontrenderer = getFontRendererFromRenderManager();
        float f = 1.6F;
        float f1 = 0.01666667F * f;
        if (oldlabels){
            f1 = (float)((double)f1 * (Math.sqrt(Math.sqrt(d)) / 2D));
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        byte byte0 = 0;

        if (par2Str.equals("deadmau5"))
        {
            byte0 = -10;
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int i = fontrenderer.getStringWidth(par2Str) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-i - 1, -1 + byte0, 0.0D);
        tessellator.addVertex(-i - 1, 8 + byte0, 0.0D);
        tessellator.addVertex(i + 1, 8 + byte0, 0.0D);
        tessellator.addVertex(i + 1, -1 + byte0, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, byte0, 0x20ffffff);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, byte0, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
    }
}
