package net.minecraft.src.nbxlite;

import org.lwjgl.opengl.GL11;
import net.minecraft.src.*;

public class RenderSheep2 extends RenderSheep
{
    private static final ResourceLocation field_110885_a = new ResourceLocation("textures/entity/sheep/sheep_fur.png");

    public RenderSheep2(ModelBase par1ModelBase, ModelBase par2ModelBase, float par3)
    {
        super(par1ModelBase, par2ModelBase, par3);
    }

    @Override
    protected int setWoolColorAndRender(EntitySheep par1EntitySheep, int par2, float par3)
    {
        if (par2 == 0 && !par1EntitySheep.getSheared())
        {
            bindTexture(field_110885_a);
            float f = ODNBXlite.oldLightEngine ? par1EntitySheep.getBrightness(par2) : 1.0F;
            int i = par1EntitySheep.getFleeceColor();
            GL11.glColor3f(f * EntitySheep.fleeceColorTable[i][0], f * EntitySheep.fleeceColorTable[i][1], f * EntitySheep.fleeceColorTable[i][2]);
            return 1;
        }
        else
        {
            return -1;
        }
    }
}
