package net.minecraft.src.nbxlite;

import org.lwjgl.opengl.GL11;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.ModelWolf;
import net.minecraft.src.RenderWolf;
import net.minecraft.src.ResourceLocation;

public class RenderWolf2 extends RenderWolf
{
    private static final ResourceLocation field_110918_h = new ResourceLocation("textures/entity/wolf/wolf_collar.png");

    public RenderWolf2()
    {
        super(new ModelWolf(), new ModelWolf(), 0.5F);
    }

    protected int func_82447_a(EntityWolf par1EntityWolf, int par2, float par3)
    {
        if (par2 == 0 && par1EntityWolf.getWolfShaking())
        {
            return super.func_82447_a(par1EntityWolf, par2, par3);
        }

        if (par2 == 1 && par1EntityWolf.isTamed())
        {
            bindTexture(field_110918_h);
            float f1 = par1EntityWolf.getBrightness(par3);
            int i = par1EntityWolf.getCollarColor();
            GL11.glColor3f(f1 * EntitySheep.fleeceColorTable[i][0], f1 * EntitySheep.fleeceColorTable[i][1], f1 * EntitySheep.fleeceColorTable[i][2]);
            return 1;
        }
        else
        {
            return super.func_82447_a(par1EntityWolf, par2, par3);
        }
    }
}
