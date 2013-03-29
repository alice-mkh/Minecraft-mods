package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.client.Minecraft;

public class RenderEnderman2 extends RenderLiving
{
    public static boolean greeneyes = false;

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
    public void renderEnderman2(EntityEnderman par1EntityEnderman, double par2, double par4, double par6, float par8, float par9)
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
            GL11.glScalef(f, -f, f);
            if (Minecraft.oldlighting){
                float ff = par1EntityEnderman.getBrightness(par2);
                GL11.glColor3f(ff, ff, ff);
                GL11.glColor3f(ff, ff, ff);
            }else{
                int i = par1EntityEnderman.getBrightnessForRender(par2);
                int j = i % 0x10000;
                int k = i / 0x10000;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            loadTexture("/terrain.png");
            renderBlocks.renderBlockAsItem(Block.blocksList[par1EntityEnderman.getCarried()], par1EntityEnderman.getCarryingData(), Minecraft.oldlighting ? par1EntityEnderman.getBrightness(par2) : 1.0F);
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
        else
        {
            if (greeneyes){
                loadTexture("/olddays/enderman_eyes.png");
            }else{
                loadTexture("/mob/enderman_eyes.png");
            }
            float f = 1.0F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_LIGHTING);
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
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return renderEyes((EntityEnderman)par1EntityLiving, par2, par3);
    }

    @Override
    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        renderCarrying((EntityEnderman)par1EntityLiving, par2);
    }

    @Override
    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        renderEnderman2((EntityEnderman)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        renderEnderman2((EntityEnderman)par1Entity, par2, par4, par6, par8, par9);
    }
}
