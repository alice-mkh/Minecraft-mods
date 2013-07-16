package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderTntMinecart2 extends RenderMinecart2
{
    protected void func_94146_a(EntityMinecartTNT par1EntityMinecartTNT, float par2, Block par3Block, int par4)
    {
        int i = par1EntityMinecartTNT.func_94104_d();

        if (i > -1 && ((float)i - par2) + 1.0F < 10F)
        {
            float f = 1.0F - (((float)i - par2) + 1.0F) / 10F;

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
            float f1 = 1.0F + f * 0.3F;
            GL11.glScalef(f1, f1, f1);
        }

        super.renderBlockInMinecart(par1EntityMinecartTNT, par2, par3Block, par4);

        if (i > -1 && (i / 5) % 2 == 0)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_DST_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, (1.0F - (((float)i - par2) + 1.0F) / 100F) * 0.8F);
            GL11.glPushMatrix();
            field_94145_f.renderBlockAsItem(Block.tnt, 0, 1.0F);
            GL11.glPopMatrix();
            float ff = Minecraft.oldlighting ? par1EntityMinecartTNT.getBrightness(par2) : 1.0F;
            GL11.glColor4f(ff, ff, ff, 1.0F);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    @Override
    protected void renderBlockInMinecart(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4)
    {
        func_94146_a((EntityMinecartTNT)par1EntityMinecart, par2, par3Block, par4);
    }
}
