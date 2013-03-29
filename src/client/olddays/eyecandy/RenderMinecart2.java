package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class RenderMinecart2 extends RenderMinecart
{
    public static boolean shiftChest = false;
    public static boolean oldrotation = false;

    public RenderMinecart2()
    {
        super();
    }

    @Override
    protected void func_94144_a(EntityMinecart par1EntityMinecart, float par2, Block par3Block, int par4)
    {
        float f = par1EntityMinecart.getBrightness(par2);
        GL11.glPushMatrix();
        /*if (par1EntityMinecart.minecartType != 0)
        {
            loadTexture("/terrain.png");
            float f6 = 0.75F;
            GL11.glScalef(f6, f6, f6);

            if (par1EntityMinecart.minecartType == 1)
            {
                float ff = Minecraft.oldlighting ? par1EntityMinecart.getBrightness(par9) : 1.0F;
                if (shiftChest){
                    GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
                }else{
                    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                }
                if (oldrotation){
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                }
                (new RenderBlocks()).renderBlockAsItem(Block.blocksList[Block.chest.blockID], 0, par1EntityMinecart.getBrightness(par9));
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                if (shiftChest){
                    GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
                }else{
                    GL11.glTranslatef(0.5F, 0.0F, -0.5F);
                }
                GL11.glColor4f(ff, ff, ff, ff);
            }
            else if (par1EntityMinecart.minecartType == 2)
            {
                float ff = Minecraft.oldlighting ? par1EntityMinecart.getBrightness(par9) : 1.0F;
                GL11.glTranslatef(0.0F, 0.3125F, 0.0F);
                if (oldrotation){
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                }
                (new RenderBlocks()).renderBlockAsItem(Block.furnaceIdle, 0, par1EntityMinecart.getBrightness(par9));
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, -0.3125F, 0.0F);
                GL11.glColor4f(ff, ff, ff, ff);
            }

            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
        }*/
        field_94145_f.renderBlockAsItem(par3Block, par4, f);
        GL11.glPopMatrix();
    }
}
