package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderItem extends Render
{
    public static boolean oldrotation = false;
    public static boolean oldrendering = false;

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private RenderBlocks itemRenderBlocks;

    /** The RNG used in RenderItem (for bobbing itemstacks on the ground) */
    private Random random;
    public boolean renderWithColor;

    /** Defines the zLevel of rendering of item on GUI. */
    public float zLevel;
    public static boolean renderInFrame;

    public RenderItem()
    {
        itemRenderBlocks = new RenderBlocks();
        random = new Random();
        renderWithColor = true;
        shadowSize = 0.15F;
        shadowOpaque = 0.75F;
    }

    /**
     * Renders the item
     */
    public void doRenderItem(EntityItem par1EntityItem, double par2, double par4, double par6, float par8, float par9)
    {
        bindEntityTexture(par1EntityItem);
        random.setSeed(187L);
        ItemStack itemstack = par1EntityItem.getEntityItem();

        if (itemstack.getItem() == null)
        {
            return;
        }

        GL11.glPushMatrix();
        float f = MathHelper.sin(((float)par1EntityItem.age + par9) / 10F + par1EntityItem.hoverStart) * 0.1F + 0.1F;
        float f1 = (((float)par1EntityItem.age + par9) / 20F + par1EntityItem.hoverStart) * (180F / (float)Math.PI);
        byte byte0 = 1;

        if (par1EntityItem.getEntityItem().stackSize > 1)
        {
            byte0 = 2;
        }

        if (par1EntityItem.getEntityItem().stackSize > 5)
        {
            byte0 = 3;
        }

        if (par1EntityItem.getEntityItem().stackSize > 20)
        {
            byte0 = 4;
        }

        if (par1EntityItem.getEntityItem().stackSize > 40)
        {
            byte0 = 5;
        }

        GL11.glTranslatef((float)par2, (float)par4 + f, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        if (itemstack.getItemSpriteNumber() == 0 && itemstack.itemID < Block.blocksList.length && Block.blocksList[itemstack.itemID] != null && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType()))
        {
            Block block = Block.blocksList[itemstack.itemID];
            GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);

            if (renderInFrame)
            {
                GL11.glScalef(1.25F, 1.25F, 1.25F);
                GL11.glTranslatef(0.0F, 0.05F, 0.0F);
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            }

            float f2 = 0.25F;
            int k = block.getRenderType();

            if (k == 1 || k == 19 || k == 12 || k == 2)
            {
                f2 = 0.5F;
            }

            GL11.glScalef(f2, f2, f2);

            for (int l = 0; l < byte0; l++)
            {
                GL11.glPushMatrix();

                if (l > 0)
                {
                    float f6 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f2;
                    float f10 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f2;
                    float f13 = ((random.nextFloat() * 2.0F - 1.0F) * 0.2F) / f2;
                    GL11.glTranslatef(f6, f10, f13);
                }

                float f7 = Minecraft.oldlighting && !renderInFrame ? par1EntityItem.getBrightness(par9) : 1.0F;
                itemRenderBlocks.renderBlockAsItem(block, itemstack.getItemDamage(), f7);
                GL11.glPopMatrix();
            }
        }
        else if (itemstack.getItemSpriteNumber() == 1 && itemstack.getItem().requiresMultipleRenderPasses())
        {
            if (renderInFrame)
            {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
            }
            else
            {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            for (int i = 0; i <= 1; i++)
            {
                random.setSeed(187L);
                Icon icon1 = itemstack.getItem().getIconFromDamageForRenderPass(itemstack.getItemDamage(), i);
                float f3 = Minecraft.oldlighting && !renderInFrame ? par1EntityItem.getBrightness(par9) : 1.0F;

                if (renderWithColor)
                {
                    int i1 = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, i);
                    float f8 = (float)(i1 >> 16 & 0xff) / 255F;
                    float f11 = (float)(i1 >> 8 & 0xff) / 255F;
                    float f14 = (float)(i1 & 0xff) / 255F;
                    GL11.glColor4f(f8 * f3, f11 * f3, f14 * f3, 1.0F);
                    renderDroppedItem(par1EntityItem, icon1, byte0, par9, f8 * f3, f11 * f3, f14 * f3);
                }
                else
                {
                    renderDroppedItem(par1EntityItem, icon1, byte0, par9, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        else
        {
            if (renderInFrame)
            {
                GL11.glScalef(0.5128205F, 0.5128205F, 0.5128205F);
                GL11.glTranslatef(0.0F, -0.05F, 0.0F);
            }
            else
            {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }

            Icon icon = itemstack.getIconIndex();

            if (renderWithColor)
            {
                int j = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
                float f4 = (float)(j >> 16 & 0xff) / 255F;
                float f5 = (float)(j >> 8 & 0xff) / 255F;
                float f9 = (float)(j & 0xff) / 255F;
                float f12 = Minecraft.oldlighting && !renderInFrame ? par1EntityItem.getBrightness(par9) : 1.0F;
                renderDroppedItem(par1EntityItem, icon, byte0, par9, f4 * f12, f5 * f12, f9 * f12);
            }
            else
            {
                renderDroppedItem(par1EntityItem, icon, byte0, par9, 1.0F, 1.0F, 1.0F);
            }
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    protected ResourceLocation func_110796_a(EntityItem par1EntityItem)
    {
        return renderManager.renderEngine.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
    }

    /**
     * Renders a dropped item
     */
    private void renderDroppedItem(EntityItem par1EntityItem, Icon par2Icon, int par3, float par4, float par5, float par6, float par7)
    {
        Tessellator tessellator = Tessellator.instance;

        if (par2Icon == null)
        {
            TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
            ResourceLocation resourcelocation = texturemanager.getResourceLocation(par1EntityItem.getEntityItem().getItemSpriteNumber());
            par2Icon = ((TextureMap)texturemanager.getTexture(resourcelocation)).getAtlasSprite("missingno");
        }

        float f = par2Icon.getMinU();
        float f1 = par2Icon.getMaxU();
        float f2 = par2Icon.getMinV();
        float f3 = par2Icon.getMaxV();
        float f4 = 1.0F;
        float f5 = 0.5F;
        float f6 = 0.25F;

        if (renderManager.options.fancyGraphics && !oldrendering)
        {
            GL11.glPushMatrix();

            if (renderInFrame)
            {
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                GL11.glRotatef((((float)par1EntityItem.age + par4) / 20F + par1EntityItem.hoverStart) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            }

            float f7 = 0.0625F;
            float f8 = 0.021875F;
            ItemStack itemstack = par1EntityItem.getEntityItem();
            int j = itemstack.stackSize;

            if (j < 2)
            {
                par3 = 1;
            }
            else if (j < 16)
            {
                par3 = 2;
            }
            else if (j < 32)
            {
                par3 = 3;
            }
            else
            {
                par3 = 4;
            }

            GL11.glTranslatef(-f5, -f6, -(((f7 + f8) * (float)par3) / 2.0F));

            for (int k = 0; k < par3; k++)
            {
                GL11.glTranslatef(0.0F, 0.0F, f7 + f8);

                if (itemstack.getItemSpriteNumber() == 0 && Block.blocksList[itemstack.itemID] != null)
                {
                    bindTexture(TextureMap.locationBlocksTexture);
                }
                else
                {
                    bindTexture(TextureMap.locationItemsTexture);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
                ItemRenderer.renderItemIn2D(tessellator, f1, f2, f, f3, par2Icon.getIconWidth(), par2Icon.getIconHeight(), f7);

                if (itemstack.hasEffect())
                {
                    GL11.glDepthFunc(GL11.GL_EQUAL);
                    GL11.glDisable(GL11.GL_LIGHTING);
                    renderManager.renderEngine.bindTexture(RES_ITEM_GLINT);
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                    float f12 = 0.76F;
                    GL11.glColor4f(0.5F * f12, 0.25F * f12, 0.8F * f12, 1.0F);
                    GL11.glMatrixMode(GL11.GL_TEXTURE);
                    GL11.glPushMatrix();
                    float f13 = 0.125F;
                    GL11.glScalef(f13, f13, f13);
                    float f14 = ((float)(Minecraft.getSystemTime() % 3000L) / 3000F) * 8F;
                    GL11.glTranslatef(f14, 0.0F, 0.0F);
                    GL11.glRotatef(-50F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f7);
                    GL11.glPopMatrix();
                    GL11.glPushMatrix();
                    GL11.glScalef(f13, f13, f13);
                    f14 = ((float)(Minecraft.getSystemTime() % 4873L) / 4873F) * 8F;
                    GL11.glTranslatef(-f14, 0.0F, 0.0F);
                    GL11.glRotatef(10F, 0.0F, 0.0F, 1.0F);
                    ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 255, 255, f7);
                    GL11.glPopMatrix();
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glDepthFunc(GL11.GL_LEQUAL);
                }
            }

            GL11.glPopMatrix();
        }
        else
        {
            for (int i = 0; i < par3; i++)
            {
                GL11.glPushMatrix();

                if (i > 0)
                {
                    float f9 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f10 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    float f11 = (random.nextFloat() * 2.0F - 1.0F) * 0.3F;
                    GL11.glTranslatef(f9, f10, f11);
                }

                if (!renderInFrame)
                {
                    GL11.glRotatef(180F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                }

                GL11.glColor4f(par5, par6, par7, 1.0F);
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
    }

    /**
     * Renders the item's icon or block into the UI at the specified position.
     */
    public void renderItemIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5)
    {
        int i = par3ItemStack.itemID;
        int j = par3ItemStack.getItemDamage();
        Object obj = par3ItemStack.getIconIndex();

        if (par3ItemStack.getItemSpriteNumber() == 0 && RenderBlocks.renderItemIn3d(Block.blocksList[i].getRenderType()))
        {
            par2TextureManager.bindTexture(TextureMap.locationBlocksTexture);
            Block block = Block.blocksList[i];
            GL11.glPushMatrix();
            GL11.glTranslatef(par4 - 2, par5 + 3, -3F + zLevel);
            GL11.glScalef(10F, 10F, 10F);
            GL11.glTranslatef(1.0F, 0.5F, 1.0F);
            if (!oldrotation){
                GL11.glScalef(1.0F, 1.0F, -1F);
            }
            GL11.glRotatef(210F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            int l = Item.itemsList[i].getColorFromItemStack(par3ItemStack, 0);
            float f = (float)(l >> 16 & 0xff) / 255F;
            float f2 = (float)(l >> 8 & 0xff) / 255F;
            float f5 = (float)(l & 0xff) / 255F;

            if (renderWithColor)
            {
                GL11.glColor4f(f, f2, f5, 1.0F);
            }

            if (!oldrotation){
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            }else{
                GL11.glScalef(1.0F, 1.0F, 1.0F);
            }
            itemRenderBlocks.useInventoryTint = renderWithColor;
            itemRenderBlocks.renderBlockAsItem(block, j, 1.0F);
            itemRenderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        }
        else if (Item.itemsList[i].requiresMultipleRenderPasses())
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            par2TextureManager.bindTexture(TextureMap.locationItemsTexture);

            for (int k = 0; k <= 1; k++)
            {
                Icon icon = Item.itemsList[i].getIconFromDamageForRenderPass(j, k);
                int j1 = Item.itemsList[i].getColorFromItemStack(par3ItemStack, k);
                float f3 = (float)(j1 >> 16 & 0xff) / 255F;
                float f6 = (float)(j1 >> 8 & 0xff) / 255F;
                float f8 = (float)(j1 & 0xff) / 255F;

                if (renderWithColor)
                {
                    GL11.glColor4f(f3, f6, f8, 1.0F);
                }

                renderIcon(par4, par5, icon, 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        }
        else
        {
            GL11.glDisable(GL11.GL_LIGHTING);
            ResourceLocation resourcelocation = par2TextureManager.getResourceLocation(par3ItemStack.getItemSpriteNumber());
            par2TextureManager.bindTexture(resourcelocation);

            if (obj == null)
            {
                obj = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(resourcelocation)).getAtlasSprite("missingno");
            }

            int i1 = Item.itemsList[i].getColorFromItemStack(par3ItemStack, 0);
            float f1 = (float)(i1 >> 16 & 0xff) / 255F;
            float f4 = (float)(i1 >> 8 & 0xff) / 255F;
            float f7 = (float)(i1 & 0xff) / 255F;

            if (renderWithColor)
            {
                GL11.glColor4f(f1, f4, f7, 1.0F);
            }

            renderIcon(par4, par5, ((Icon)(obj)), 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    /**
     * Render the item's icon or block into the GUI, including the glint effect.
     */
    public void renderItemAndEffectIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5)
    {
        if (par3ItemStack == null)
        {
            return;
        }

        renderItemIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5);

        if (par3ItemStack.hasEffect())
        {
            GL11.glDepthFunc(GL11.GL_GREATER);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            par2TextureManager.bindTexture(RES_ITEM_GLINT);
            zLevel -= 50F;
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
            GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
            renderGlint(par4 * 0x19b4ca14 + par5 * 0x1eafff1, par4 - 2, par5 - 2, 20, 20);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            zLevel += 50F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }
    }

    private void renderGlint(int par1, int par2, int par3, int par4, int par5)
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
    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5)
    {
        renderItemOverlayIntoGUI(par1FontRenderer, par2TextureManager, par3ItemStack, par4, par5, null);
    }

    public void renderItemOverlayIntoGUI(FontRenderer par1FontRenderer, TextureManager par2TextureManager, ItemStack par3ItemStack, int par4, int par5, String par6Str)
    {
        if (par3ItemStack == null)
        {
            return;
        }

        if (par3ItemStack.stackSize > 1 || par6Str != null)
        {
            String s = par6Str != null ? par6Str : String.valueOf(par3ItemStack.stackSize);
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

    public void renderIcon(int par1, int par2, Icon par3Icon, int par4, int par5)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par5, zLevel, par3Icon.getMinU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + par5, zLevel, par3Icon.getMaxU(), par3Icon.getMaxV());
        tessellator.addVertexWithUV(par1 + par4, par2 + 0, zLevel, par3Icon.getMaxU(), par3Icon.getMinV());
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, zLevel, par3Icon.getMinU(), par3Icon.getMinV());
        tessellator.draw();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity par1Entity)
    {
        return func_110796_a((EntityItem)par1Entity);
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
