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
    public static boolean field_82407_g = false;

    public RenderItem2()
    {
        renderBlocks = new RenderBlocks();
        random = new Random();
        field_77024_a = true;
        zLevel = 0.0F;
        shadowSize = 0.15F;
        shadowOpaque = 0.75F;
    }

    /**
     * Renders the item
     */
    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9)
    {
        random.setSeed(187L);
        ItemStack itemstack = par1EntityItem.func_92059_d();
        GL11.glPushMatrix();
        float f = MathHelper.sin(((float)par1EntityItem.age + par9) / 10F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
        float f1 = (((float)par1EntityItem.age + par9) / 20F + par1EntityItem.hoverStart) * (180F / (float)Math.PI);
        byte byte0 = 1;

        if (par1EntityItem.func_92059_d().stackSize > 1)
        {
            byte0 = 2;
        }

        if (par1EntityItem.func_92059_d().stackSize > 5)
        {
            byte0 = 3;
        }

        if (par1EntityItem.func_92059_d().stackSize > 20)
        {
            byte0 = 4;
        }

        GL11.glTranslatef((float)par2, (float)par4 + f, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        Block block = Block.blocksList[itemstack.itemID];

        if (block != null && RenderBlocks.renderItemIn3d(block.getRenderType()))
        {
            GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);

            if (field_82407_g)
            {
                GL11.glScalef(1.25F, 1.25F, 1.25F);
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            }

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
            if (field_82407_g)
            {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            else
            {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            loadTexture("/gui/items.png");

            for (int i = 0; i <= 1; i++)
            {
                int l = itemstack.getItem().getIconFromDamageForRenderPass(itemstack.getItemDamage(), i);
                float f3 = Minecraft.oldlighting ? par1EntityItem.getBrightness(par9) : 1.0F;

                if (field_77024_a)
                {
                    int k1 = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, i);
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
            if (field_82407_g)
            {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            else
            {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

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
                int i1 = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
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
     * Renders the item's icon or block into the UI at the specified position.
     */
    public void renderItemIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        int i = par3ItemStack.itemID;
        int j = par3ItemStack.getItemDamage();
        int k = par3ItemStack.getIconIndex();

        if (i < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[i].getRenderType()))
        {
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/terrain.png"));
            Block block = Block.blocksList[i];
            GL11.glPushMatrix();
            GL11.glTranslatef(par4 - 2, par5 + 3, -3F + zLevel);
            GL11.glScalef(10F, 10F, 10F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            GL11.glScalef(1.0F, 1.0F, -1F);
            GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            int j1 = Item.itemsList[i].getColorFromItemStack(par3ItemStack, 0);
            float f1 = (float)(j1 >> 16 & 0xff) / 255F;
            float f3 = (float)(j1 >> 8 & 0xff) / 255F;
            float f6 = (float)(j1 & 0xff) / 255F;

            if (field_77024_a)
            {
                GL11.glColor4f(f1, f3, f6, 1.0F);
            }

            GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            renderBlocks.useInventoryTint = field_77024_a;
            renderBlocks.renderBlockAsItem(block, j, 1.0F);
            renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        }
        else if (Item.itemsList[i].requiresMultipleRenderPasses())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/gui/items.png"));

            for (int l = 0; l <= 1; l++)
            {
                int k1 = Item.itemsList[i].getIconFromDamageForRenderPass(j, l);
                int l1 = Item.itemsList[i].getColorFromItemStack(par3ItemStack, l);
                float f4 = (float)(l1 >> 16 & 0xff) / 255F;
                float f7 = (float)(l1 >> 8 & 0xff) / 255F;
                float f8 = (float)(l1 & 0xff) / 255F;

                if (field_77024_a)
                {
                    GL11.glColor4f(f4, f7, f8, 1.0F);
                }

                renderTexturedQuad(par4, par5, (k1 % 16) * 16, (k1 / 16) * 16, 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        }
        else if (k >= 0)
        {
            GL11.glDisable(GL11.GL_LIGHTING);

            if (i < 256)
            {
                par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/terrain.png"));
            }
            else
            {
                par2RenderEngine.bindTexture(par2RenderEngine.getTexture("/gui/items.png"));
            }

            int i1 = Item.itemsList[i].getColorFromItemStack(par3ItemStack, 0);
            float f = (float)(i1 >> 16 & 0xff) / 255F;
            float f2 = (float)(i1 >> 8 & 0xff) / 255F;
            float f5 = (float)(i1 & 0xff) / 255F;

            if (field_77024_a)
            {
                GL11.glColor4f(f, f2, f5, 1.0F);
            }

            renderTexturedQuad(par4, par5, (k % 16) * 16, (k / 16) * 16, 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void func_82406_b(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        if (par3ItemStack == null)
        {
            return;
        }

        renderItemIntoGUI(par1FontRenderer, par2RenderEngine, par3ItemStack, par4, par5);

        if (par3ItemStack != null && par3ItemStack.hasEffect())
        {
            GL11.glDepthFunc(GL11.GL_GREATER);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            par2RenderEngine.bindTexture(par2RenderEngine.getTexture("%blur%/misc/glint.png"));
            zLevel -= 50F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
            GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
            func_77018_a(par4 * 0x19b4ca14 + par5 * 0x1eafff1, par4 - 2, par5 - 2, 20, 20);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            zLevel += 50F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
    }

    private void func_77018_a(int par1, int par2, int par3, int par4, int par5)
    {
        for (int i = 0; i < 2; i++)
        {
            if (i == 0)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            if (i == 1)
            {
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            }

            float f = 0.00390625F;
            float f1 = 0.00390625F;
            float f2 = ((float)(Minecraft.getSystemTime() % (long)(3000 + i * 1873)) / (3000F + (float)(i * 1873))) * 256F;
            float f3 = 0.0F;
            Tessellator tessellator = Tessellator.instance;
            float f4 = 4F;

            if (i == 1)
            {
                f4 = -1F;
            }

            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(par2 + 0, par3 + par5, zLevel, (f2 + (float)par5 * f4) * f, (f3 + (float)par5) * f1);
            tessellator.addVertexWithUV(par2 + par4, par3 + par5, zLevel, (f2 + (float)par4 + (float)par5 * f4) * f, (f3 + (float)par5) * f1);
            tessellator.addVertexWithUV(par2 + par4, par3 + 0, zLevel, (f2 + (float)par4) * f, (f3 + 0.0F) * f1);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, zLevel, (f2 + 0.0F) * f, (f3 + 0.0F) * f1);
            tessellator.draw();
        }
    }

    /**
     * Renders the item's overlay information. Examples being stack count or damage on top of the item's image at the
     * specified position.
     */
    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, RenderEngine par2RenderEngine, ItemStack par3ItemStack, int par4, int par5)
    {
        if (par3ItemStack == null)
        {
            return;
        }

        if (par3ItemStack.stackSize > 1)
        {
            String s = (new StringBuilder()).append("").append(par3ItemStack.stackSize).toString();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            par1FontRenderer.drawStringWithShadow(s, (par4 + 19) - 2 - par1FontRenderer.getStringWidth(s), par5 + 6 + 3, 0xffffff);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (par3ItemStack.isItemDamaged())
        {
            int i = (int)Math.round(13D - ((double)par3ItemStack.getItemDamageForDisplay() * 13D) / (double)par3ItemStack.getMaxDamage());
            int j = (int)Math.round(255D - ((double)par3ItemStack.getItemDamageForDisplay() * 255D) / (double)par3ItemStack.getMaxDamage());
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator tessellator = Tessellator.instance;
            int k = 255 - j << 16 | j << 8;
            int l = (255 - j) / 4 << 16 | 0x3f00;
            renderQuad(tessellator, par4 + 2, par5 + 13, 13, 2, 0);
            renderQuad(tessellator, par4 + 2, par5 + 13, 12, 1, l);
            renderQuad(tessellator, par4 + 2, par5 + 13, i, 1, k);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Adds a quad to the tesselator at the specified position with the set width and height and color.  Args:
     * tessellator, x, y, width, height, color
     */
    private void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6)
    {
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setColorOpaque_I(par6);
        par1Tessellator.addVertex(par2 + 0, par3 + 0, 0.0D);
        par1Tessellator.addVertex(par2 + 0, par3 + par5, 0.0D);
        par1Tessellator.addVertex(par2 + par4, par3 + par5, 0.0D);
        par1Tessellator.addVertex(par2 + par4, par3 + 0, 0.0D);
        par1Tessellator.draw();
    }

    /**
     * Adds a textured quad to the tesselator at the specified position with the specified texture coords, width and
     * height.  Args: x, y, u, v, width, height
     */
    public void renderTexturedQuad(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, zLevel, (float)(par3 + 0) * f, (float)(par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, zLevel, (float)(par3 + par5) * f, (float)(par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, zLevel, (float)(par3 + par5) * f, (float)(par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, (float)(par3 + 0) * f, (float)(par4 + 0) * f1);
        tessellator.draw();
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
