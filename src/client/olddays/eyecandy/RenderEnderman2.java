package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderEnderman2 extends RenderLiving
{
    public static boolean greeneyes = false;
    private static final ResourceLocation customEyes = new ResourceLocation("olddays/enderman_eyes.png");

    private static final ResourceLocation field_110840_a = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
    private static final ResourceLocation field_110839_f = new ResourceLocation("textures/entity/enderman/enderman.png");

    /** The model of the enderman */
    private ModelEnderman endermanModel;
    private Random rnd;

    public RenderEnderman2()
    {
        super(new ModelEnderman(), 0.5F);
        rnd = new Random();
        endermanModel = (ModelEnderman)super.mainModel;
        setRenderPassModel(endermanModel);
    }

    /**
     * Renders the enderman
     */
    public void renderEnderman(EntityEnderman par1EntityEnderman, double par2, double par4, double par6, float par8, float par9)
    {
        endermanModel.isCarrying = par1EntityEnderman.getCarried() > 0;
        endermanModel.isAttacking = par1EntityEnderman.isScreaming();

        if (par1EntityEnderman.isScreaming())
        {
            double d = 0.02D;
            par2 += rnd.nextGaussian() * d;
            par6 += rnd.nextGaussian() * d;
        }

        super.doRenderLiving(par1EntityEnderman, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation func_110838_a(EntityEnderman par1EntityEnderman)
    {
        return field_110839_f;
    }

    /**
     * Render the block an enderman is carrying
     */
    protected void renderCarrying(EntityEnderman par1EntityEnderman, float par2)
    {
        super.renderEquippedItems(par1EntityEnderman, par2);

        if (par1EntityEnderman.getCarried() > 0)
        {
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glPushMatrix();
            float f = 0.5F;
            GL11.glTranslatef(0.0F, 0.6875F, -0.75F);
            f *= 1.0F;
            GL11.glRotatef(20F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-f, -f, f);
            float ff = Minecraft.oldlighting ? par1EntityEnderman.getBrightness(par2) : 1.0F;
            if (!Minecraft.oldlighting){
                int i = par1EntityEnderman.getBrightnessForRender(par2);
                int j = i % 0x10000;
                int k = i / 0x10000;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            }
            GL11.glColor4f(ff, ff, ff, 1.0F);
            GL11.glColor4f(ff, ff, ff, 1.0F);
            bindTexture(TextureMap.locationBlocksTexture);
            renderBlocks.renderBlockAsItem(Block.blocksList[par1EntityEnderman.getCarried()], par1EntityEnderman.getCarryingData(), ff);
            GL11.glPopMatrix();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }
    }

    /**
     * Render the endermans eyes
     */
    protected int renderEyes(EntityEnderman par1EntityEnderman, int par2, float par3)
    {
        if (par2 != 0)
        {
            return -1;
        }

        bindTexture(greeneyes ? customEyes : field_110840_a);
        float f = 1.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_LIGHTING);

        if (par1EntityEnderman.isInvisible())
        {
            GL11.glDepthMask(false);
        }
        else
        {
            GL11.glDepthMask(true);
        }

        if (!Minecraft.oldlighting){
            int i = 61680;
            int j = i % 0x10000;
            int k = i / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, f);
        return 1;
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        renderEnderman((EntityEnderman)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase par1EntityLivingBase, int par2, float par3)
    {
        return renderEyes((EntityEnderman)par1EntityLivingBase, par2, par3);
    }

    protected void renderEquippedItems(EntityLivingBase par1EntityLivingBase, float par2)
    {
        renderCarrying((EntityEnderman)par1EntityLivingBase, par2);
    }

    public void renderPlayer(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6, float par8, float par9)
    {
        renderEnderman((EntityEnderman)par1EntityLivingBase, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return func_110838_a((EntityEnderman)par1Entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderEnderman((EntityEnderman)par1Entity, par2, par4, par6, par8, par9);
    }
}
