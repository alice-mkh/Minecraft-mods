package net.minecraft.src.nbxlite;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class RenderMooshroom2 extends RenderLiving
{
    public RenderMooshroom2(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public void func_77114_a(EntityMooshroom par1EntityMooshroom, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(par1EntityMooshroom, par2, par4, par6, par8, par9);
    }

    protected void func_77115_a(EntityMooshroom par1EntityMooshroom, float par2)
    {
        super.renderEquippedItems(par1EntityMooshroom, par2);

        if (par1EntityMooshroom.isChild())
        {
            return;
        }
        else
        {
            loadTexture("/terrain.png");
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPushMatrix();
            GL11.glScalef(1.0F, -1F, 1.0F);
            GL11.glTranslatef(0.2F, 0.4F, 0.5F);
            GL11.glRotatef(42F, 0.0F, 1.0F, 0.0F);
            renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, Minecraft.oldlighting ? par1EntityMooshroom.getBrightness(par2) : 1.0F);
            GL11.glTranslatef(0.1F, 0.0F, -0.6F);
            GL11.glRotatef(42F, 0.0F, 1.0F, 0.0F);
            renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, Minecraft.oldlighting ? par1EntityMooshroom.getBrightness(par2) : 1.0F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            ((ModelQuadruped)mainModel).head.postRender(0.0625F);
            GL11.glScalef(1.0F, -1F, 1.0F);
            GL11.glTranslatef(0.0F, 0.75F, -0.2F);
            GL11.glRotatef(12F, 0.0F, 1.0F, 0.0F);
            renderBlocks.renderBlockAsItem(Block.mushroomRed, 0, Minecraft.oldlighting ? par1EntityMooshroom.getBrightness(par2) : 1.0F);
            GL11.glPopMatrix();
            GL11.glDisable(GL11.GL_CULL_FACE);
            return;
        }
    }

    protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2)
    {
        func_77115_a((EntityMooshroom)par1EntityLiving, par2);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        func_77114_a((EntityMooshroom)par1EntityLiving, par2, par4, par6, par8, par9);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        func_77114_a((EntityMooshroom)par1Entity, par2, par4, par6, par8, par9);
    }
}
