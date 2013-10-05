package net.minecraft.src;

import java.util.List;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class RendererLivingEntity extends Render
{
    public static boolean bobbing = false;
    public static boolean labels = false;
    public static boolean stick = false;
    public static boolean oldlabels = false;
    public static boolean oldHeadRotation = false;

    private float setBobbing(EntityLivingBase par1EntityLivingBase, float f5, float par9){
        if (par1EntityLivingBase.ridingEntity != null && par1EntityLivingBase.ridingEntity instanceof EntityLivingBase){
            return setBobbing((EntityLivingBase)par1EntityLivingBase.ridingEntity, f5, par9);
        }
        float bobStrength = 0F;
        if (bobbing && !par1EntityLivingBase.isChild() &&
            (par1EntityLivingBase instanceof EntityAgeable ||
             par1EntityLivingBase instanceof EntityMob ||
             par1EntityLivingBase instanceof EntityPlayer) &&
           !(par1EntityLivingBase instanceof EntitySpider) &&
           !(par1EntityLivingBase instanceof EntityHorse)){
            bobStrength = 1F;
        }else{
            GL11.glTranslatef(0.0F, -24F * f5 - 0.0078125F, 0.0F);
            float f7 = par1EntityLivingBase.limbSwing - par1EntityLivingBase.limbSwingAmount * (1.0F - par9);
            if (par1EntityLivingBase.isChild()){
                f7 *= 3F;
            }
            return f7;
        }
        float f7 = par1EntityLivingBase.field_70763_ax + (par1EntityLivingBase.field_70764_aw - par1EntityLivingBase.field_70763_ax) * par9;
        float f32 = par1EntityLivingBase.field_70768_au + (par1EntityLivingBase.field_110154_aX - par1EntityLivingBase.field_70768_au) * par9;
        float bob = -Math.abs(MathHelper.cos(f7 * 0.6662F)) * 5F * f32 * bobStrength - 23F;
        GL11.glTranslatef(0.0F, bob * f5 - 0.0078125F, 0.0F);
        return f7;
    }

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    protected ModelBase mainModel;

    /** The model to be used during the render passes. */
    protected ModelBase renderPassModel;

    public RendererLivingEntity(ModelBase par1ModelBase, float par2)
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

    public void doRenderLiving(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_CULL_FACE);
        mainModel.onGround = renderSwingProgress(par1EntityLivingBase, par9);

        if (renderPassModel != null)
        {
            renderPassModel.onGround = mainModel.onGround;
        }

        mainModel.isRiding = par1EntityLivingBase.isRiding();

        if (renderPassModel != null)
        {
            renderPassModel.isRiding = mainModel.isRiding;
        }

        mainModel.isChild = par1EntityLivingBase.isChild();

        if (renderPassModel != null)
        {
            renderPassModel.isChild = mainModel.isChild;
        }

        try
        {
            float f = interpolateRotation(par1EntityLivingBase.prevRenderYawOffset, par1EntityLivingBase.renderYawOffset, par9);
            float f1 = interpolateRotation(par1EntityLivingBase.prevRotationYawHead, par1EntityLivingBase.rotationYawHead, par9);
            if (oldHeadRotation && par1EntityLivingBase == Minecraft.getMinecraft().thePlayer){
                f1 = interpolateRotation(par1EntityLivingBase.prevRotationYaw, par1EntityLivingBase.rotationYaw, par9);
            }

            if (par1EntityLivingBase.isRiding() && (par1EntityLivingBase.ridingEntity instanceof EntityLivingBase))
            {
                EntityLivingBase entitylivingbase = (EntityLivingBase)par1EntityLivingBase.ridingEntity;
                f = interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, par9);
                float f3 = MathHelper.wrapAngleTo180_float(f1 - f);

                if (f3 < -85F)
                {
                    f3 = -85F;
                }

                if (f3 >= 85F)
                {
                    f3 = 85F;
                }

                f = f1 - f3;

                if (f3 * f3 > 2500F)
                {
                    f += f3 * 0.2F;
                }
            }

            float f2 = par1EntityLivingBase.prevRotationPitch + (par1EntityLivingBase.rotationPitch - par1EntityLivingBase.prevRotationPitch) * par9;
            renderLivingAt(par1EntityLivingBase, par2, par4, par6);
            float f4 = handleRotationFloat(par1EntityLivingBase, par9);
            rotateCorpse(par1EntityLivingBase, f4, f, par9);
            float f5 = 0.0625F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScalef(-1F, -1F, 1.0F);
            preRenderCallback(par1EntityLivingBase, par9);
            float f6 = par1EntityLivingBase.prevLimbSwingAmount + (par1EntityLivingBase.limbSwingAmount - par1EntityLivingBase.prevLimbSwingAmount) * par9;
            float f7 = setBobbing(par1EntityLivingBase, f5, par9);

            if (f6 > 1.0F)
            {
                f6 = 1.0F;
            }

            GL11.glEnable(GL11.GL_ALPHA_TEST);
            mainModel.setLivingAnimations(par1EntityLivingBase, f7, f6, par9);
            renderModel(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);

            for (int i = 0; i < 4; i++)
            {
                int j = shouldRenderPass(par1EntityLivingBase, i, par9);

                if (j <= 0)
                {
                    continue;
                }

                renderPassModel.setLivingAnimations(par1EntityLivingBase, f7, f6, par9);
                renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);

                if ((j & 0xf0) == 16)
                {
                    func_82408_c(par1EntityLivingBase, i, par9);
                    renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);
                }

                if ((j & 0xf) == 15)
                {
                    float f9 = (float)par1EntityLivingBase.ticksExisted + par9;
                    bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    float f11 = 0.5F;
                    GL11.glColor4f(f11, f11, f11, 1.0F);
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDepthMask(false);

                    for (int i1 = 0; i1 < 2; i1++)
                    {
                        GL11.glDisable(GL11.GL_LIGHTING);
                        float f14 = 0.76F;
                        GL11.glColor4f(0.5F * f14, 0.25F * f14, 0.8F * f14, 1.0F);
                        GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                        GL11.glMatrixMode(GL11.GL_TEXTURE);
                        GL11.glLoadIdentity();
                        float f16 = f9 * (0.001F + (float)i1 * 0.003F) * 20F;
                        float f17 = 0.3333333F;
                        GL11.glScalef(f17, f17, f17);
                        GL11.glRotatef(30F - (float)i1 * 60F, 0.0F, 0.0F, 1.0F);
                        GL11.glTranslatef(0.0F, f16, 0.0F);
                        GL11.glMatrixMode(GL11.GL_MODELVIEW);
                        renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);
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
            renderEquippedItems(par1EntityLivingBase, par9);
            float f8 = par1EntityLivingBase.getBrightness(par9);
            int k = getColorMultiplier(par1EntityLivingBase, f8, par9);
            OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);

            if ((k >> 24 & 0xff) > 0 || par1EntityLivingBase.hurtTime > 0 || par1EntityLivingBase.deathTime > 0)
            {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthFunc(GL11.GL_EQUAL);

                if (par1EntityLivingBase.hurtTime > 0 || par1EntityLivingBase.deathTime > 0)
                {
                    GL11.glColor4f(f8, 0.0F, 0.0F, 0.4F);
                    mainModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);

                    for (int l = 0; l < 4; l++)
                    {
                        if (inheritRenderPass(par1EntityLivingBase, l, par9) >= 0)
                        {
                            GL11.glColor4f(f8, 0.0F, 0.0F, 0.4F);
                            renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);
                        }
                    }
                }

                if ((k >> 24 & 0xff) > 0)
                {
                    float f10 = (float)(k >> 16 & 0xff) / 255F;
                    float f12 = (float)(k >> 8 & 0xff) / 255F;
                    float f13 = (float)(k & 0xff) / 255F;
                    float f15 = (float)(k >> 24 & 0xff) / 255F;
                    GL11.glColor4f(f10, f12, f13, f15);
                    mainModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);

                    for (int j1 = 0; j1 < 4; j1++)
                    {
                        if (inheritRenderPass(par1EntityLivingBase, j1, par9) >= 0)
                        {
                            GL11.glColor4f(f10, f12, f13, f15);
                            renderPassModel.render(par1EntityLivingBase, f7, f6, f4, f1 - f, f2, f5);
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
        passSpecialRender(par1EntityLivingBase, par2, par4, par6);
    }

    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4, float par5, float par6, float par7)
    {
        bindEntityTexture(par1EntityLivingBase);

        if (!par1EntityLivingBase.isInvisible())
        {
            mainModel.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
        }
        else if (!par1EntityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
        {
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            mainModel.render(par1EntityLivingBase, par2, par3, par4, par5, par6, par7);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glPopMatrix();
            GL11.glDepthMask(true);
        }
        else
        {
            mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLivingBase);
        }
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
    }

    protected void rotateCorpse(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4)
    {
        GL11.glRotatef(180F - par3, 0.0F, 1.0F, 0.0F);

        if (par1EntityLivingBase.deathTime > 0)
        {
            float f = ((((float)par1EntityLivingBase.deathTime + par4) - 1.0F) / 20F) * 1.6F;
            f = MathHelper.sqrt_float(f);

            if (f > 1.0F)
            {
                f = 1.0F;
            }

            GL11.glRotatef(f * getDeathMaxRotation(par1EntityLivingBase), 0.0F, 0.0F, 1.0F);
        }
        else
        {
            String s = EnumChatFormatting.func_110646_a(par1EntityLivingBase.getEntityName());

            if ((s.equals("Dinnerbone") || s.equals("Grumm")) && (!(par1EntityLivingBase instanceof EntityPlayer) || !((EntityPlayer)par1EntityLivingBase).getHideCape()))
            {
                GL11.glTranslatef(0.0F, par1EntityLivingBase.height + 0.1F, 0.0F);
                GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            }
        }
    }

    protected float renderSwingProgress(EntityLivingBase par1EntityLivingBase, float par2)
    {
        return par1EntityLivingBase.getSwingProgress(par2);
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase par1EntityLivingBase, float par2)
    {
        return (float)par1EntityLivingBase.ticksExisted + par2;
    }

    protected void renderEquippedItems(EntityLivingBase entitylivingbase, float f)
    {
        if(stick && !(this instanceof RenderPlayer) && !(this instanceof RenderPlayer2))
        {
            renderArrowsStuckInEntity(entitylivingbase, f);
        }
    }

    /**
     * renders arrows the Entity has been attacked with, attached to it
     */
    protected void renderArrowsStuckInEntity(EntityLivingBase par1EntityLivingBase, float par2)
    {
        int i = par1EntityLivingBase.getArrowCountInEntity();

        if (i > 0)
        {
            EntityArrow entityarrow = new EntityArrow(par1EntityLivingBase.worldObj, par1EntityLivingBase.posX, par1EntityLivingBase.posY, par1EntityLivingBase.posZ);
            Random random = new Random(par1EntityLivingBase.entityId);
            RenderHelper.disableStandardItemLighting();

            for (int j = 0; j < i; j++)
            {
                GL11.glPushMatrix();
                ModelRenderer modelrenderer = mainModel.getRandomModelBox(random);
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

    protected int inheritRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return shouldRenderPass(par1EntityLivingBase, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return -1;
    }

    protected void func_82408_c(EntityLivingBase entitylivingbase, int i, float f)
    {
    }

    protected float getDeathMaxRotation(EntityLivingBase par1EntityLivingBase)
    {
        return 90F;
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLivingBase par1EntityLivingBase, float par2, float par3)
    {
        return 0;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entitylivingbase, float f)
    {
    }

    /**
     * Passes the specialRender and renders it
     */
    protected void passSpecialRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        if (func_110813_b(par1EntityLivingBase))
        {
            float f = 1.6F;
            float f1 = 0.01666667F * f;
            double d = par1EntityLivingBase.getDistanceSqToEntity(renderManager.livingPlayer);
            float f2 = par1EntityLivingBase.isSneaking() ? 32F : 64F;

            if (d < (double)(f2 * f2))
            {
                String s = par1EntityLivingBase.getTranslatedEntityName();

                if (par1EntityLivingBase.isSneaking())
                {
                    FontRenderer fontrenderer = getFontRendererFromRenderManager();
                    GL11.glPushMatrix();
                    GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + par1EntityLivingBase.height + 0.5F, (float)par6);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GL11.glScalef(-f1, -f1, f1);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                    GL11.glDepthMask(false);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    Tessellator tessellator = Tessellator.instance;
                    GL11.glDisable(GL11.GL_TEXTURE_2D);
                    tessellator.startDrawingQuads();
                    int i = fontrenderer.getStringWidth(s) / 2;
                    tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    tessellator.addVertex(-i - 1, -1D, 0.0D);
                    tessellator.addVertex(-i - 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, 8D, 0.0D);
                    tessellator.addVertex(i + 1, -1D, 0.0D);
                    tessellator.draw();
                    GL11.glEnable(GL11.GL_TEXTURE_2D);
                    GL11.glDepthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, 0x20ffffff);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                    GL11.glPopMatrix();
                }
                else
                {
                    func_96449_a(par1EntityLivingBase, par2, par4, par6, s, f1, d);
                }
            }
        }
        if (Minecraft.getMinecraft().gameSettings.showDebugInfo && labels && par1EntityLivingBase instanceof EntityLiving){
            if (par1EntityLivingBase instanceof EntityEnderman){
                renderLivingLabel(par1EntityLivingBase, Integer.toString(par1EntityLivingBase.entityId), par2, par4 + 1D, par6, 64);
            }else{
                renderLivingLabel(par1EntityLivingBase, Integer.toString(par1EntityLivingBase.entityId), par2, par4, par6, 64);
            }
        }
    }

    protected boolean func_110813_b(EntityLivingBase par1EntityLivingBase)
    {
        return Minecraft.isGuiEnabled() && par1EntityLivingBase != renderManager.livingPlayer && !par1EntityLivingBase.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer) && par1EntityLivingBase.riddenByEntity == null;
    }

    protected void func_96449_a(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, String par8Str, float par9, double par10)
    {
        if (par1EntityLivingBase.isPlayerSleeping())
        {
            renderLivingLabel(par1EntityLivingBase, par8Str, par2, par4 - 1.5D, par6, 64);
        }
        else
        {
            renderLivingLabel(par1EntityLivingBase, par8Str, par2, par4, par6, 64);
        }
    }

    /**
     * Draws the debug or playername text above a living
     */
    protected void renderLivingLabel(EntityLivingBase par1EntityLivingBase, String par2Str, double par3, double par5, double par7, int par9)
    {
        double d = par1EntityLivingBase.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d > (double)(par9 * par9))
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
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + par1EntityLivingBase.height + 0.5F, (float)par7);
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
        doRenderLiving((EntityLivingBase)par1Entity, par2, par4, par6, par8, par9);
    }
}
