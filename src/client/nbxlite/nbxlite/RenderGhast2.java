package net.minecraft.src.nbxlite;

import org.lwjgl.opengl.GL11;
import net.minecraft.src.mod_noBiomesX;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ModelGhast;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.RenderLiving;

public class RenderGhast2 extends RenderLiving
{
    public RenderGhast2()
    {
        super(new ModelGhast(), 0.5F);
    }

    protected void func_4014_a(EntityGhast entityghast, float f)
    {
        EntityGhast entityghast1 = entityghast;
        float f1 = ((float)entityghast1.prevAttackCounter + (float)(entityghast1.attackCounter - entityghast1.prevAttackCounter) * f) / 20F;
        if (f1 < 0.0F)
        {
            f1 = 0.0F;
        }
        f1 = 1.0F / (f1 * f1 * f1 * f1 * f1 * 2.0F + 1.0F);
        float f2 = (8F + f1) / 2.0F;
        float f3 = (8F + 1.0F / f1) / 2.0F;
        GL11.glScalef(f3, f2, f3);
        if (mod_noBiomesX.ClassicLight){
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)(61680 % 0x10000) / 1.0F, (float)(61680 / 0x10000) / 1.0F);
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void preRenderCallback(EntityLiving entityliving, float f)
    {
        func_4014_a((EntityGhast)entityliving, f);
    }
}
