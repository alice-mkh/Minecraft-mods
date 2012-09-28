package net.minecraft.src.nbxlite;

import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.src.*;

public class RenderItem2 extends Render
{
    private RenderBlocks renderBlocks;

    /** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
    private Random random;
    public boolean field_77024_a;

    /** Defines the zLevel of rendering of item on GUI. */
    public float zLevel;

    public RenderItem2()
    {
        super();
        renderBlocks = new RenderBlocks();
        random = new Random();
    }

    /**
     * Renders the item
     */
    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9)
    {
        random.setSeed(187L);
        ItemStack itemstack = par1EntityItem.item;
        GL11.glPushMatrix();
        float f = MathHelper.sin(((float)par1EntityItem.age + par9) / 10F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
        float f1 = (((float)par1EntityItem.age + par9) / 20F + par1EntityItem.hoverStart) * (180F / (float)Math.PI);
        byte byte0 = 1;

        if (par1EntityItem.item.stackSize > 1)
        {
            byte0 = 2;
        }

        if (par1EntityItem.item.stackSize > 5)
        {
            byte0 = 3;
        }

        if (par1EntityItem.item.stackSize > 20)
        {
            byte0 = 4;
        }

        GL11.glTranslatef((float)par2, (float)par4 + f, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        Block block = Block.blocksList[itemstack.itemID];

        if (block != null && RenderBlocks.renderItemIn3d(block.getRenderType()))
        {
            GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
            loadTexture("/terrain.png");
            float f2 = 0.25F;
            int k = block.getRenderType();

            if (k == 1 || k == 19 || k == 12 || k == 2)
            {
                f2 = 0.5F;
            }

            GL11.glScalef(f2, f2, f2);

            for (int j1 = 0; j1 < byte0; j1++)
            {
                GL11.glPushMatrix();

                if (j1 > 0)
                {
                    float f5 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f2;
                    float f8 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f2;
                    float f11 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f2;
                    GL11.glTranslatef(f5, f8, f11);
                }

                float f6 = Minecraft.oldlighting ? par1EntityItem.getBrightness(par9) : 1.0F;
                renderBlocks.renderBlockAsItem(block, itemstack.getItemDamage(), f6);
                GL11.glPopMatrix();
            }
        }
        else if (itemstack.getItem().requiresMultipleRenderPasses())
        {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            loadTexture("/gui/items.png");

            for (int i = 0; i <= 1; i++)
            {
                int l = itemstack.getItem().getIconFromDamageForRenderPass(itemstack.getItemDamage(), i);
                float f3 = Minecraft.oldlighting ? par1EntityItem.getBrightness(par9) : 1.0F;

                if (field_77024_a)
                {
                    int k1 = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage(), i);
                    float f9 = (float)(k1 >> 16 & 0xff) / 255F;
                    float f12 = (float)(k1 >> 8 & 0xff) / 255F;
                    float f14 = (float)(k1 & 0xff) / 255F;
                    GL11.glColor4f(f9 * f3, f12 * f3, f14 * f3, 1.0F);
                }

                func_77020_a(l, byte0);
            }
        }
        else
        {
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            int j = itemstack.getIconIndex();

            if (block != null)
            {
                loadTexture("/terrain.png");
            }
            else
            {
                loadTexture("/gui/items.png");
            }

            if (field_77024_a)
            {
                int i1 = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage(), 0);
                float f4 = (float)(i1 >> 16 & 0xff) / 255F;
                float f7 = (float)(i1 >> 8 & 0xff) / 255F;
                float f10 = (float)(i1 & 0xff) / 255F;
                float f13 = Minecraft.oldlighting ? par1EntityItem.getBrightness(par9) : 1.0F;
                GL11.glColor4f(f4 * f13, f7 * f13, f10 * f13, 1.0F);
            }

            func_77020_a(j, byte0);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    private void func_77020_a(int par1, int par2)
    {
        Tessellator tessellator = Tessellator.instance;
        float f = (float)((par1 % 16) * 16 + 0) / 256F;
        float f1 = (float)((par1 % 16) * 16 + 16) / 256F;
        float f2 = (float)((par1 / 16) * 16 + 0) / 256F;
        float f3 = (float)((par1 / 16) * 16 + 16) / 256F;
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;

        for (int i = 0; i < par2; i++)
        {
            GL11.glPushMatrix();

            if (i > 0)
            {
                float f7 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float f8 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                GL11.glTranslatef(f7, f8, f9);
            }

            GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV(0.0F - f5, 0.0F - f6, 0.0D, f, f3);
            tessellator.addVertexWithUV(f4 - f5, 0.0F - f6, 0.0D, f1, f3);
            tessellator.addVertexWithUV(f4 - f5, 1.0F - f6, 0.0D, f1, f2);
            tessellator.addVertexWithUV(0.0F - f5, 1.0F - f6, 0.0D, f, f2);
            tessellator.draw();
            GL11.glPopMatrix();
        }
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        doRenderItem((EntityItem)par1Entity, par2, par4, par6, par8, par9);
    }
}
