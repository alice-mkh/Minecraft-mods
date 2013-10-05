package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderer
{
    public static boolean olddays = false;
    public static boolean sway = true;
    public static boolean items2d = false;
    public static int hand = 2;

    private static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");
    private static final ResourceLocation RES_UNDERWATER_OVERLAY = new ResourceLocation("textures/misc/underwater.png");

    /** A reference to the Minecraft object. */
    private Minecraft mc;
    private ItemStack itemToRender;

    /**
     * How far the current item has been equipped (0 disequipped and 1 fully up)
     */
    private float equippedProgress;
    private float prevEquippedProgress;

    /** Instance of RenderBlocks. */
    private RenderBlocks renderBlocksInstance;
    public final MapItemRenderer mapItemRenderer;

    /** The index of the currently held item (0-8, or -1 if not yet updated) */
    private int equippedItemSlot;

    public ItemRenderer(Minecraft par1Minecraft)
    {
        renderBlocksInstance = new RenderBlocks();
        equippedItemSlot = -1;
        mc = par1Minecraft;
        mapItemRenderer = new MapItemRenderer(par1Minecraft.gameSettings, par1Minecraft.getTextureManager());
    }

    public void renderItem(EntityLivingBase par1EntityLivingBase, ItemStack par2ItemStack, int par3){
        renderItem(par1EntityLivingBase, par2ItemStack, par3, false);
    }

    /**
     * Renders the item stack for being in an entity's hand Args: itemStack
     */
    public void renderItem(EntityLivingBase par1EntityLivingBase, ItemStack par2ItemStack, int par3, boolean b)
    {
        GL11.glPushMatrix();
        TextureManager texturemanager = mc.getTextureManager();

        if (par2ItemStack.getItemSpriteNumber() == 0 && par2ItemStack.itemID < Block.blocksList.length && Block.blocksList[par2ItemStack.itemID] != null && RenderBlocks.renderItemIn3d(Block.blocksList[par2ItemStack.itemID].getRenderType()))
        {
            texturemanager.bindTexture(texturemanager.getResourceLocation(0));
            renderBlocksInstance.renderBlockAsItem(Block.blocksList[par2ItemStack.itemID], par2ItemStack.getItemDamage(), Minecraft.oldlighting ? par1EntityLivingBase.getBrightness(1.0F) : 1.0F);
        }
        else
        {
            Icon icon = par1EntityLivingBase.getItemIcon(par2ItemStack, par3);

            if (icon == null)
            {
                GL11.glPopMatrix();
                return;
            }

            texturemanager.bindTexture(texturemanager.getResourceLocation(par2ItemStack.getItemSpriteNumber()));
            Tessellator tessellator = Tessellator.instance;
            float f = icon.getMinU();
            float f1 = icon.getMaxU();
            float f2 = icon.getMinV();
            float f3 = icon.getMaxV();
            float f4 = 0.0F;
            float f5 = 0.3F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(-f4, -f5, 0.0F);
            float f6 = 1.5F;
            GL11.glScalef(f6, f6, f6);
            if (items2d && b){
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(360F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.5F, -0.0625F, 0.0F);
            }else{
                GL11.glRotatef(50F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(335F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            }
            renderItemIn2D_old(tessellator, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 0.0625F, b);

            if (par2ItemStack.hasEffect() && par3 == 0)
            {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                texturemanager.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
                float f7 = 0.76F;
                GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float f8 = 0.125F;
                GL11.glScalef(f8, f8, f8);
                float f9 = ((float)(Minecraft.getSystemTime() % 3000L) / 3000F) * 8F;
                GL11.glTranslatef(f9, 0.0F, 0.0F);
                GL11.glRotatef(-50F, 0.0F, 0.0F, 1.0F);
                renderItemIn2D_old(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F, b);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f8, f8, f8);
                f9 = ((float)(Minecraft.getSystemTime() % 4873L) / 4873F) * 8F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10F, 0.0F, 0.0F, 1.0F);
                renderItemIn2D_old(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F, b);
                GL11.glPopMatrix();
                GL11.glMatrixMode(GL11.GL_MODELVIEW);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        GL11.glPopMatrix();
    }

    /**
     * Renders an item held in hand as a 2D texture with thickness
     */
    public static void renderItemIn2D(Tessellator par0Tessellator, float par1, float par2, float par3, float par4, int par5, int par6, float par7)
    {
        par0Tessellator.startDrawingQuads();
        par0Tessellator.setNormal(0.0F, 0.0F, 1.0F);
        par0Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, par1, par4);
        par0Tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, par3, par4);
        par0Tessellator.addVertexWithUV(1.0D, 1.0D, 0.0D, par3, par2);
        par0Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, par1, par2);
        par0Tessellator.draw();
        par0Tessellator.startDrawingQuads();
        par0Tessellator.setNormal(0.0F, 0.0F, -1F);
        par0Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - par7, par1, par2);
        par0Tessellator.addVertexWithUV(1.0D, 1.0D, 0.0F - par7, par3, par2);
        par0Tessellator.addVertexWithUV(1.0D, 0.0D, 0.0F - par7, par3, par4);
        par0Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - par7, par1, par4);
        par0Tessellator.draw();
        float f = (0.5F * (par1 - par3)) / (float)par5;
        float f1 = (0.5F * (par4 - par2)) / (float)par6;
        par0Tessellator.startDrawingQuads();
        par0Tessellator.setNormal(-1F, 0.0F, 0.0F);

        for (int i = 0; i < par5; i++)
        {
            float f2 = (float)i / (float)par5;
            float f6 = (par1 + (par3 - par1) * f2) - f;
            float f10 = f2;
            par0Tessellator.addVertexWithUV(f10, 0.0D, 0.0F - par7, f6, par4);
            par0Tessellator.addVertexWithUV(f10, 0.0D, 0.0D, f6, par4);
            par0Tessellator.addVertexWithUV(f10, 1.0D, 0.0D, f6, par2);
            par0Tessellator.addVertexWithUV(f10, 1.0D, 0.0F - par7, f6, par2);
        }

        par0Tessellator.draw();
        par0Tessellator.startDrawingQuads();
        par0Tessellator.setNormal(1.0F, 0.0F, 0.0F);

        for (int j = 0; j < par5; j++)
        {
            float f3 = (float)j / (float)par5;
            float f7 = (par1 + (par3 - par1) * f3) - f;
            float f11 = f3 + 1.0F / (float)par5;
            par0Tessellator.addVertexWithUV(f11, 1.0D, 0.0F - par7, f7, par2);
            par0Tessellator.addVertexWithUV(f11, 1.0D, 0.0D, f7, par2);
            par0Tessellator.addVertexWithUV(f11, 0.0D, 0.0D, f7, par4);
            par0Tessellator.addVertexWithUV(f11, 0.0D, 0.0F - par7, f7, par4);
        }

        par0Tessellator.draw();
        par0Tessellator.startDrawingQuads();
        par0Tessellator.setNormal(0.0F, 1.0F, 0.0F);

        for (int k = 0; k < par6; k++)
        {
            float f4 = (float)k / (float)par6;
            float f8 = (par4 + (par2 - par4) * f4) - f1;
            float f12 = f4 + 1.0F / (float)par6;
            par0Tessellator.addVertexWithUV(0.0D, f12, 0.0D, par1, f8);
            par0Tessellator.addVertexWithUV(1.0D, f12, 0.0D, par3, f8);
            par0Tessellator.addVertexWithUV(1.0D, f12, 0.0F - par7, par3, f8);
            par0Tessellator.addVertexWithUV(0.0D, f12, 0.0F - par7, par1, f8);
        }

        par0Tessellator.draw();
        par0Tessellator.startDrawingQuads();
        par0Tessellator.setNormal(0.0F, -1F, 0.0F);

        for (int l = 0; l < par6; l++)
        {
            float f5 = (float)l / (float)par6;
            float f9 = (par4 + (par2 - par4) * f5) - f1;
            float f13 = f5;
            par0Tessellator.addVertexWithUV(1.0D, f13, 0.0D, par3, f9);
            par0Tessellator.addVertexWithUV(0.0D, f13, 0.0D, par1, f9);
            par0Tessellator.addVertexWithUV(0.0D, f13, 0.0F - par7, par1, f9);
            par0Tessellator.addVertexWithUV(1.0D, f13, 0.0F - par7, par3, f9);
        }

        par0Tessellator.draw();
    }

    private static void renderItemIn2D_old(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, int par6, int par7, float par8, boolean b){
        if (!items2d || !b){
            renderItemIn2D(par1Tessellator, par2, par3, par4, par5, par6, par7, par8);
            return;
        }
        float f = 1.0F;
        float f1 = 0.0625F;
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, -1F);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f1, par2, par3);
        par1Tessellator.addVertexWithUV(f, 1.0D, 0.0F - f1, par4, par3);
        par1Tessellator.addVertexWithUV(f, 0.0D, 0.0F - f1, par4, par5);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f1, par2, par5);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f1, par2, par5);
        par1Tessellator.addVertexWithUV(f, 0.0D, 0.0F - f1, par4, par5);
        par1Tessellator.addVertexWithUV(f, 1.0D, 0.0F - f1, par4, par3);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f1, par2, par3);
        par1Tessellator.draw();
    }

    /**
     * Renders the active item in the player's hand when in first person mode. Args: partialTickTime
     */
    public void renderItemInFirstPerson(float par1)
    {
        float f = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * par1;
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
        float f1 = ((AbstractClientPlayer)(entityclientplayermp)).prevRotationPitch + (((AbstractClientPlayer)(entityclientplayermp)).rotationPitch - ((AbstractClientPlayer)(entityclientplayermp)).prevRotationPitch) * par1;
        GL11.glPushMatrix();
        GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(((AbstractClientPlayer)(entityclientplayermp)).prevRotationYaw + (((AbstractClientPlayer)(entityclientplayermp)).rotationYaw - ((AbstractClientPlayer)(entityclientplayermp)).prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        EntityPlayerSP entityplayersp = (EntityPlayerSP)entityclientplayermp;
        float f2 = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * par1;
        float f3 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * par1;
        if (sway){
            GL11.glRotatef((((AbstractClientPlayer)(entityclientplayermp)).rotationPitch - f2) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((((AbstractClientPlayer)(entityclientplayermp)).rotationYaw - f3) * 0.1F, 0.0F, 1.0F, 0.0F);
        }
        ItemStack itemstack = itemToRender;
        float f4 = mc.theWorld.getLightBrightness(MathHelper.floor_double(((AbstractClientPlayer)(entityclientplayermp)).posX), MathHelper.floor_double(((AbstractClientPlayer)(entityclientplayermp)).posY), MathHelper.floor_double(((AbstractClientPlayer)(entityclientplayermp)).posZ));
        if (!Minecraft.oldlighting){
            f4 = 1.0F;
            int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(((AbstractClientPlayer)(entityclientplayermp)).posX), MathHelper.floor_double(((AbstractClientPlayer)(entityclientplayermp)).posY), MathHelper.floor_double(((AbstractClientPlayer)(entityclientplayermp)).posZ), 0);
            int k = i % 0x10000;
            int l = i / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
        }
        GL11.glColor4f(f4, f4, f4, 1.0F);//NOT SURE ABOUT THIS

        if (itemstack != null)
        {
            int j = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 0);
            float f8 = (float)(j >> 16 & 0xff) / 255F;
            float f13 = (float)(j >> 8 & 0xff) / 255F;
            float f19 = (float)(j & 0xff) / 255F;
            GL11.glColor4f(f4 * f8, f4 * f13, f4 * f19, 1.0F);
        }
        else
        {
            GL11.glColor4f(f4, f4, f4, 1.0F);
        }

        if (itemstack != null && itemstack.itemID == Item.map.itemID)
        {
            GL11.glPushMatrix();
            float f5 = 0.8F;
            float f9 = entityclientplayermp.getSwingProgress(par1);
            float f14 = MathHelper.sin(f9 * (float)Math.PI);
            float f20 = MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI);
            GL11.glTranslatef(-f20 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f9) * (float)Math.PI * 2.0F) * 0.2F, -f14 * 0.2F);
            f9 = (1.0F - f1 / 45F) + 0.1F;

            if (f9 < 0.0F)
            {
                f9 = 0.0F;
            }

            if (f9 > 1.0F)
            {
                f9 = 1.0F;
            }

            f9 = -MathHelper.cos(f9 * (float)Math.PI) * 0.5F + 0.5F;
            GL11.glTranslatef(0.0F, (0.0F * f5 - (1.0F - f) * 1.2F - f9 * 0.5F) + 0.04F, -0.9F * f5);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f9 * -85F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());

            for (f14 = 0; f14 < 2; f14++)
            {
                f20 = f14 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0F, -0.6F, 1.1F * (float)f20);
                GL11.glRotatef(-45 * f20, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * f20, 0.0F, 1.0F, 0.0F);
                Render render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
                float f30 = 1.0F;
                GL11.glScalef(f30, f30, f30);
                drawFirstPersonHand(render, 2);
                GL11.glPopMatrix();
            }

            f14 = entityclientplayermp.getSwingProgress(par1);
            f20 = MathHelper.sin(f14 * f14 * (float)Math.PI);
            float f25 = MathHelper.sin(MathHelper.sqrt_float(f14) * (float)Math.PI);
            GL11.glRotatef(-f20 * 20F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f25 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f25 * 80F, 1.0F, 0.0F, 0.0F);
            float f28 = 0.38F;
            GL11.glScalef(f28, f28, f28);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-1F, -1F, 0.0F);
            float f31 = 0.015625F;
            GL11.glScalef(f31, f31, f31);
            mc.getTextureManager().bindTexture(RES_MAP_BACKGROUND);
            Tessellator tessellator = Tessellator.instance;
            GL11.glNormal3f(0.0F, 0.0F, -1F);
            tessellator.startDrawingQuads();
            byte byte0 = 7;
            tessellator.addVertexWithUV(0 - byte0, 128 + byte0, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(128 + byte0, 128 + byte0, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(128 + byte0, 0 - byte0, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(0 - byte0, 0 - byte0, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            MapData mapdata = Item.map.getMapData(itemstack, mc.theWorld);

            if (mapdata != null)
            {
                mapItemRenderer.renderMap(mc.thePlayer, mc.getTextureManager(), mapdata);
            }

            GL11.glPopMatrix();
        }
        else if (itemstack != null)
        {
            GL11.glPushMatrix();
            float f6 = 0.8F;

            if (entityclientplayermp.getItemInUseCount() > 0)
            {
                EnumAction enumaction = itemstack.getItemUseAction();

                if (enumaction == EnumAction.eat || enumaction == EnumAction.drink)
                {
                    float f15 = ((float)entityclientplayermp.getItemInUseCount() - par1) + 1.0F;
                    float f21 = 1.0F - f15 / (float)itemstack.getMaxItemUseDuration();
                    float f26 = 1.0F - f21;
                    f26 = f26 * f26 * f26;
                    f26 = f26 * f26 * f26;
                    f26 = f26 * f26 * f26;
                    float f29 = 1.0F - f26;
                    GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos((f15 / 4F) * (float)Math.PI) * 0.1F) * (float)((double)f21 <= 0.20000000000000001D ? 0 : 1), 0.0F);
                    GL11.glTranslatef(f29 * 0.6F, -f29 * 0.5F, 0.0F);
                    GL11.glRotatef(f29 * 90F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(f29 * 10F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(f29 * 30F, 0.0F, 0.0F, 1.0F);
                }
            }
            else
            {
                float f10 = entityclientplayermp.getSwingProgress(par1);
                float f16 = MathHelper.sin(f10 * (float)Math.PI);
                float f22 = MathHelper.sin(MathHelper.sqrt_float(f10) * (float)Math.PI);
                GL11.glTranslatef(-f22 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * (float)Math.PI * 2.0F) * 0.2F, -f16 * 0.2F);
            }

            GL11.glTranslatef(0.7F * f6, -0.65F * f6 - (1.0F - f) * 0.6F, -0.9F * f6);
            if (!items2d || itemstack.itemID<256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())){
                GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            }
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            float f11 = entityclientplayermp.getSwingProgress(par1);
            float f17 = MathHelper.sin(f11 * f11 * (float)Math.PI);
            float f23 = MathHelper.sin(MathHelper.sqrt_float(f11) * (float)Math.PI);
            if (!items2d || itemstack.itemID<256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())){
                GL11.glRotatef(-f17 * 20F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-f23 * 20F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-f23 * 80F, 1.0F, 0.0F, 0.0F);
            }
            float f27 = 0.4F;
            GL11.glScalef(f27, f27, f27);

            if (entityclientplayermp.getItemInUseCount() > 0)
            {
                EnumAction enumaction1 = itemstack.getItemUseAction();

                if (enumaction1 == EnumAction.block)
                {
                    GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
                    if (!items2d || itemstack.itemID<256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())){
                        GL11.glRotatef(30F, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(-80F, 1.0F, 0.0F, 0.0F);
                        GL11.glRotatef(60F, 0.0F, 1.0F, 0.0F);
                    }
                }
                else if (enumaction1 == EnumAction.bow)
                {
                    if (!items2d || itemstack.itemID<256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())){
                        GL11.glRotatef(-18F, 0.0F, 0.0F, 1.0F);
                        GL11.glRotatef(-12F, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(-8F, 1.0F, 0.0F, 0.0F);
                        GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
                    }
                    float f32 = (float)itemstack.getMaxItemUseDuration() - (((float)entityclientplayermp.getItemInUseCount() - par1) + 1.0F);
                    float f35 = f32 / 20F;
                    f35 = (f35 * f35 + f35 * 2.0F) / 3F;

                    if (f35 > 1.0F)
                    {
                        f35 = 1.0F;
                    }

                    if (f35 > 0.1F)
                    {
                        GL11.glTranslatef(0.0F, MathHelper.sin((f32 - 0.1F) * 1.3F) * 0.01F * (f35 - 0.1F), 0.0F);
                    }

                    GL11.glTranslatef(0.0F, 0.0F, f35 * 0.1F);
                    if (!items2d || itemstack.itemID<256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())){
                        GL11.glRotatef(-335F, 0.0F, 0.0F, 1.0F);
                        GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
                    }
                    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                    float f37 = 1.0F + f35 * 0.2F;
                    GL11.glScalef(1.0F, 1.0F, f37);
                    GL11.glTranslatef(0.0F, -0.5F, 0.0F);
                    if (!items2d|| itemstack.itemID<256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemstack.itemID].getRenderType())){
                        GL11.glRotatef(50F, 0.0F, 1.0F, 0.0F);
                        GL11.glRotatef(335F, 0.0F, 0.0F, 1.0F);
                    }
                }
            }

            if (itemstack.getItem().shouldRotateAroundWhenRendering())
            {
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            }

            if (itemstack.getItem().requiresMultipleRenderPasses())
            {
                renderItem(entityclientplayermp, itemstack, 0, true);
                int i1 = Item.itemsList[itemstack.itemID].getColorFromItemStack(itemstack, 1);
                float f33 = (float)(i1 >> 16 & 0xff) / 255F;
                float f36 = (float)(i1 >> 8 & 0xff) / 255F;
                float f38 = (float)(i1 & 0xff) / 255F;
                GL11.glColor4f(f4 * f33, f4 * f36, f4 * f38, 1.0F);
                renderItem(entityclientplayermp, itemstack, 1, true);
            }
            else
            {
                renderItem(entityclientplayermp, itemstack, 0, true);
            }

            GL11.glPopMatrix();
        }
        else if (!entityclientplayermp.isInvisible())
        {
            GL11.glPushMatrix();
            float f7 = 0.8F;
            float f12 = entityclientplayermp.getSwingProgress(par1);
            float f18 = MathHelper.sin(f12 * (float)Math.PI);
            float f24 = MathHelper.sin(MathHelper.sqrt_float(f12) * (float)Math.PI);
            GL11.glTranslatef(-f24 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f12) * (float)Math.PI * 2.0F) * 0.4F, -f18 * 0.4F);
            GL11.glTranslatef(0.8F * f7, -0.75F * f7 - (1.0F - f) * 0.6F, -0.9F * f7);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            f12 = entityclientplayermp.getSwingProgress(par1);
            f18 = MathHelper.sin(f12 * f12 * (float)Math.PI);
            f24 = MathHelper.sin(MathHelper.sqrt_float(f12) * (float)Math.PI);
            GL11.glRotatef(f24 * 70F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f18 * 20F, 0.0F, 0.0F, 1.0F);
            mc.getTextureManager().bindTexture(entityclientplayermp.getLocationSkin());
            GL11.glTranslatef(-1F, 3.6F, 3.5F);
            GL11.glRotatef(120F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            Render render1 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
            float f34 = 1.0F;
            GL11.glScalef(f34, f34, f34);
            if (hand==0){
                GL11.glRotatef(22F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-35F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(20F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0.08F, 0.21F, 0.12F);
            }
            drawFirstPersonHand(render1, hand);
            GL11.glPopMatrix();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        RenderHelper.disableStandardItemLighting();
    }

    /**
     * Renders all the overlays that are in first person mode. Args: partialTickTime
     */
    public void renderOverlays(float par1)
    {
        GL11.glDisable(GL11.GL_ALPHA_TEST);

        if (mc.thePlayer.isBurning())
        {
            renderFireInFirstPerson(par1);
        }

        if (mc.thePlayer.isEntityInsideOpaqueBlock())
        {
            int i = MathHelper.floor_double(mc.thePlayer.posX);
            int j = MathHelper.floor_double(mc.thePlayer.posY);
            int k = MathHelper.floor_double(mc.thePlayer.posZ);
            int l = mc.theWorld.getBlockId(i, j, k);

            if (mc.theWorld.isBlockNormalCube(i, j, k))
            {
                renderInsideOfBlock(par1, Block.blocksList[l].getBlockTextureFromSide(2));
            }
            else
            {
                for (int i1 = 0; i1 < 8; i1++)
                {
                    float f = ((float)((i1 >> 0) % 2) - 0.5F) * mc.thePlayer.width * 0.9F;
                    float f1 = ((float)((i1 >> 1) % 2) - 0.5F) * mc.thePlayer.height * 0.2F;
                    float f2 = ((float)((i1 >> 2) % 2) - 0.5F) * mc.thePlayer.width * 0.9F;
                    int j1 = MathHelper.floor_float((float)i + f);
                    int k1 = MathHelper.floor_float((float)j + f1);
                    int l1 = MathHelper.floor_float((float)k + f2);

                    if (mc.theWorld.isBlockNormalCube(j1, k1, l1))
                    {
                        l = mc.theWorld.getBlockId(j1, k1, l1);
                    }
                }
            }

            if (Block.blocksList[l] != null)
            {
                renderInsideOfBlock(par1, Block.blocksList[l].getBlockTextureFromSide(2));
            }
        }

        if (mc.thePlayer.isInsideOfMaterial(Material.water))
        {
            renderWarpedTextureOverlay(par1);
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    /**
     * Renders the texture of the block the player is inside as an overlay. Args: partialTickTime, blockTextureIndex
     */
    private void renderInsideOfBlock(float par1, Icon par2Icon)
    {
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        Tessellator tessellator = Tessellator.instance;
        float f = 0.1F;
        GL11.glColor4f(f, f, f, 0.5F);
        GL11.glPushMatrix();
        float f1 = -1F;
        float f2 = 1.0F;
        float f3 = -1F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = par2Icon.getMinU();
        float f7 = par2Icon.getMaxU();
        float f8 = par2Icon.getMinV();
        float f9 = par2Icon.getMaxV();
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f1, f3, f5, f7, f9);
        tessellator.addVertexWithUV(f2, f3, f5, f6, f9);
        tessellator.addVertexWithUV(f2, f4, f5, f6, f8);
        tessellator.addVertexWithUV(f1, f4, f5, f7, f8);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders a texture that warps around based on the direction the player is looking. Texture needs to be bound
     * before being called. Used for the water overlay. Args: parialTickTime
     */
    private void renderWarpedTextureOverlay(float par1)
    {
        mc.getTextureManager().bindTexture(RES_UNDERWATER_OVERLAY);
        Tessellator tessellator = Tessellator.instance;
        float f = mc.thePlayer.getBrightness(par1);
        GL11.glColor4f(f, f, f, 0.5F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glPushMatrix();
        float f1 = 4F;
        float f2 = -1F;
        float f3 = 1.0F;
        float f4 = -1F;
        float f5 = 1.0F;
        float f6 = -0.5F;
        float f7 = -mc.thePlayer.rotationYaw / 64F;
        float f8 = mc.thePlayer.rotationPitch / 64F;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f2, f4, f6, f1 + f7, f1 + f8);
        tessellator.addVertexWithUV(f3, f4, f6, 0.0F + f7, f1 + f8);
        tessellator.addVertexWithUV(f3, f5, f6, 0.0F + f7, 0.0F + f8);
        tessellator.addVertexWithUV(f2, f5, f6, f1 + f7, 0.0F + f8);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    /**
     * Renders the fire on the screen for first person mode. Arg: partialTickTime
     */
    private void renderFireInFirstPerson(float par1)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        float f = 1.0F;

        for (int i = 0; i < 2; i++)
        {
            GL11.glPushMatrix();
            Icon icon = Block.fire.getFireIcon(1);
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f1 = icon.getMinU();
            float f2 = icon.getMaxU();
            float f3 = icon.getMinV();
            float f4 = icon.getMaxV();
            float f5 = (0.0F - f) / 2.0F;
            float f6 = f5 + f;
            float f7 = 0.0F - f / 2.0F;
            float f8 = f7 + f;
            float f9 = -0.5F;
            GL11.glTranslatef((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GL11.glRotatef((float)(i * 2 - 1) * 10F, 0.0F, 1.0F, 0.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(f5, f7, f9, f2, f4);
            tessellator.addVertexWithUV(f6, f7, f9, f1, f4);
            tessellator.addVertexWithUV(f6, f8, f9, f1, f3);
            tessellator.addVertexWithUV(f5, f8, f9, f2, f3);
            tessellator.draw();
            GL11.glPopMatrix();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void updateEquippedItem()
    {
        prevEquippedProgress = equippedProgress;
        EntityClientPlayerMP entityclientplayermp = mc.thePlayer;
        ItemStack itemstack = ((EntityPlayer)(entityclientplayermp)).inventory.getCurrentItem();
        boolean flag = equippedItemSlot == ((EntityPlayer)(entityclientplayermp)).inventory.currentItem && itemstack == itemToRender;

        if (itemToRender == null && itemstack == null)
        {
            flag = true;
        }

        if (itemstack != null && itemToRender != null && itemstack != itemToRender && itemstack.itemID == itemToRender.itemID && itemstack.getItemDamage() == itemToRender.getItemDamage())
        {
            itemToRender = itemstack;
            flag = true;
        }

        float f = 0.4F;
        float f1 = flag ? 1.0F : 0.0F;
        float f2 = f1 - equippedProgress;

        if (f2 < -f)
        {
            f2 = -f;
        }

        if (f2 > f)
        {
            f2 = f;
        }

        equippedProgress += f2;

        if (equippedProgress < 0.1F)
        {
            itemToRender = itemstack;
            equippedItemSlot = ((EntityPlayer)(entityclientplayermp)).inventory.currentItem;
        }
    }

    /**
     * Resets equippedProgress
     */
    public void resetEquippedProgress()
    {
        equippedProgress = 0.0F;
    }

    /**
     * Resets equippedProgress
     */
    public void resetEquippedProgress2()
    {
        equippedProgress = 0.0F;
    }

    public void drawFirstPersonHand(Render r, int h)
    {
        if (!olddays){
            ((RenderPlayer)r).renderFirstPersonArm(mc.thePlayer);
            return;
        }
        ModelBiped modelBipedMain = null;
        try{
            RenderPlayer r1 = (RenderPlayer)r;
            modelBipedMain = ((ModelBiped)mod_OldDays.getField(r1.getClass(), r1, 1));
        }catch(ClassCastException e){
            try{
                RenderPlayer2 r1 = (RenderPlayer2)r;
                modelBipedMain = ((ModelBiped)mod_OldDays.getField(r1.getClass(), r1, 2));
            }catch(ClassCastException e2){
                return;
            }
        }
        if (h >= 2){
            modelBipedMain.onGround = 0.0F;
            modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mc.thePlayer);
            modelBipedMain.bipedRightArm.render(0.0625F);
            return;
        }
        if (h!=1){
            modelBipedMain.onGround = 0.0F;
            modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mc.thePlayer);
        }
        modelBipedMain.bipedRightArm.render(0.0625F);
    }
}
