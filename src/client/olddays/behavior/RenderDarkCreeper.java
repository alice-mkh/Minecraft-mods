package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderDarkCreeper extends RenderLiving
{
    private ModelBase field_27008_a;

    public RenderDarkCreeper()
    {
        super(new ModelCreeper(), 0.5F);
        field_27008_a = new ModelCreeper(2.0F);
    }

    /**
     * Updates creeper scale in prerender callback
     */
    protected void updateCreeperScale(EntityCreeper par1EntityCreeper, float par2)
    {
        EntityCreeper entitycreeper = par1EntityCreeper;
        float f = entitycreeper.setCreeperFlashTime(par2);
        float f1 = 1.0F + MathHelper.sin(f * 100F) * f * 0.01F;

        if (f < 0.0F)
        {
            f = 0.0F;
        }

        if (f > 1.0F)
        {
            f = 1.0F;
        }

        f *= f;
        f *= f;
        float f2 = (1.0F + f * 0.4F) * f1;
        float f3 = (1.0F + f * 0.1F) / f1;
        GL11.glScalef(f2, f3, f2);
    }

    /**
     * Updates color multiplier based on creeper state called by getColorMultiplier
     */
    protected int updateCreeperColorMultiplier(EntityCreeper par1EntityCreeper, float par2, float par3)
    {
        EntityCreeper entitycreeper = par1EntityCreeper;
        float f = entitycreeper.setCreeperFlashTime(par3);

        if ((int)(f * 10F) % 2 == 0)
        {
            return 0;
        }

        int i = (int)(f * 0.2F * 255F);

        if (i < 0)
        {
            i = 0;
        }

        if (i > 255)
        {
            i = 255;
        }

        char c = '\377';
        char c1 = '\377';
        char c2 = '\377';
        return i << 24 | c << 16 | c1 << 8 | c2;
    }

    protected int func_27006_a(EntityCreeper par1EntityCreeper, int par2, float par3)
    {
        if (par1EntityCreeper.getPowered())
        {
            if (par2 == 1)
            {
                float f = (float)par1EntityCreeper.ticksExisted + par3;
                loadTexture("/armor/power.png");
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                float f1 = f * 0.01F;
                float f2 = f * 0.01F;
                GL11.glTranslatef(f1, f2, 0.0F);
                setRenderPassModel(field_27008_a);
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_BLEND);
                float f3 = 0.5F;
                GL11.glColor4f(f3, f3, f3, 1.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
                return 1;
            }

            if (par2 == 2)
            {
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glLoadIdentity();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }

        return -1;
    }

    protected int func_27007_b(EntityCreeper par1EntityCreeper, int par2, float par3)
    {
        return -1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLiving par1EntityLiving, float par2)
    {
        updateCreeperScale((EntityCreeper)par1EntityLiving, par2);
    }

    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3)
    {
        return updateCreeperColorMultiplier((EntityCreeper)par1EntityLiving, par2, par3);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return func_27006_a((EntityCreeper)par1EntityLiving, par2, par3);
    }

    protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3)
    {
        return func_27007_b((EntityCreeper)par1EntityLiving, par2, par3);
    }
}
