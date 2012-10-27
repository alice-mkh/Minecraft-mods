package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ItemRenderer
{
    public static boolean olddays = false;
    public static boolean sway = true;
    public static boolean items2d = false;
    public static int hand = 2;

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
        itemToRender = null;
        equippedProgress = 0.0F;
        prevEquippedProgress = 0.0F;
        renderBlocksInstance = new RenderBlocks();
        equippedItemSlot = -1;
        mc = par1Minecraft;
        mapItemRenderer = new MapItemRenderer(par1Minecraft.fontRenderer, par1Minecraft.gameSettings, par1Minecraft.renderEngine);
    }

    /**
     * Renders the item stack for being in an entity's hand Args: itemStack
     */
    public void renderItem(EntityLiving par1EntityLiving, ItemStack par2ItemStack, int par3)
    {
        GL11.glPushMatrix();
        Block block = null;

        if (par2ItemStack.itemID < Block.blocksList.length)
        {
            block = Block.blocksList[par2ItemStack.itemID];
        }

        if (block != null && RenderBlocks.renderItemIn3d(block.getRenderType()))
        {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
            renderBlocksInstance.renderBlockAsItem(block, par2ItemStack.getItemDamage(), Minecraft.oldlighting ? par1EntityLiving.getBrightness(1.0F) : 1.0F);
        }
        else
        {
            if (block != null)
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
            }
            else
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/items.png"));
            }

            Tessellator tessellator = Tessellator.instance;
            int i = par1EntityLiving.getItemIcon(par2ItemStack, par3);
            float f = ((float)((i % 16) * 16) + 0.0F) / 256F;
            float f1 = ((float)((i % 16) * 16) + 15.99F) / 256F;
            float f2 = ((float)((i / 16) * 16) + 0.0F) / 256F;
            float f3 = ((float)((i / 16) * 16) + 15.99F) / 256F;
            float f4 = 0.0F;
            float f5 = 0.3F;
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glTranslatef(-f4, -f5, 0.0F);
            float f6 = 1.5F;
            GL11.glScalef(f6, f6, f6);
            if (items2d){
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(360F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.5F, -0.0625F, 0.0F);
            }else{
                GL11.glRotatef(50F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(335F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(-0.9375F, -0.0625F, 0.0F);
            }
            renderItemIn2D(tessellator, f1, f2, f, f3);

            if (par2ItemStack != null && par2ItemStack.hasEffect() && par3 == 0)
            {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("%blur%/misc/glint.png"));
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
                renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(f8, f8, f8);
                f9 = ((float)(Minecraft.getSystemTime() % 4873L) / 4873F) * 8F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10F, 0.0F, 0.0F, 1.0F);
                renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F);
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
    private void renderItemIn2D(Tessellator par1Tessellator, float par2, float par3, float par4, float par5)
    {
        if (items2d){
            renderItemIn2D_old(par1Tessellator, par2, par3, par4, par5);
            return;
        }
        float f = 1.0F;
        float f1 = 0.0625F;
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, 1.0F);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, par2, par5);
        par1Tessellator.addVertexWithUV(f, 0.0D, 0.0D, par4, par5);
        par1Tessellator.addVertexWithUV(f, 1.0D, 0.0D, par4, par3);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, par2, par3);
        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 0.0F, -1F);
        par1Tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f1, par2, par3);
        par1Tessellator.addVertexWithUV(f, 1.0D, 0.0F - f1, par4, par3);
        par1Tessellator.addVertexWithUV(f, 0.0D, 0.0F - f1, par4, par5);
        par1Tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f1, par2, par5);
        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(-1F, 0.0F, 0.0F);

        for (int i = 0; i < 16; i++)
        {
            float f2 = (float)i / 16F;
            float f6 = (par2 + (par4 - par2) * f2) - 0.001953125F;
            float f10 = f * f2;
            par1Tessellator.addVertexWithUV(f10, 0.0D, 0.0F - f1, f6, par5);
            par1Tessellator.addVertexWithUV(f10, 0.0D, 0.0D, f6, par5);
            par1Tessellator.addVertexWithUV(f10, 1.0D, 0.0D, f6, par3);
            par1Tessellator.addVertexWithUV(f10, 1.0D, 0.0F - f1, f6, par3);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(1.0F, 0.0F, 0.0F);

        for (int j = 0; j < 16; j++)
        {
            float f3 = (float)j / 16F;
            float f7 = (par2 + (par4 - par2) * f3) - 0.001953125F;
            float f11 = f * f3 + 0.0625F;
            par1Tessellator.addVertexWithUV(f11, 1.0D, 0.0F - f1, f7, par3);
            par1Tessellator.addVertexWithUV(f11, 1.0D, 0.0D, f7, par3);
            par1Tessellator.addVertexWithUV(f11, 0.0D, 0.0D, f7, par5);
            par1Tessellator.addVertexWithUV(f11, 0.0D, 0.0F - f1, f7, par5);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, 1.0F, 0.0F);

        for (int k = 0; k < 16; k++)
        {
            float f4 = (float)k / 16F;
            float f8 = (par5 + (par3 - par5) * f4) - 0.001953125F;
            float f12 = f * f4 + 0.0625F;
            par1Tessellator.addVertexWithUV(0.0D, f12, 0.0D, par2, f8);
            par1Tessellator.addVertexWithUV(f, f12, 0.0D, par4, f8);
            par1Tessellator.addVertexWithUV(f, f12, 0.0F - f1, par4, f8);
            par1Tessellator.addVertexWithUV(0.0D, f12, 0.0F - f1, par2, f8);
        }

        par1Tessellator.draw();
        par1Tessellator.startDrawingQuads();
        par1Tessellator.setNormal(0.0F, -1F, 0.0F);

        for (int l = 0; l < 16; l++)
        {
            float f5 = (float)l / 16F;
            float f9 = (par5 + (par3 - par5) * f5) - 0.001953125F;
            float f13 = f * f5;
            par1Tessellator.addVertexWithUV(f, f13, 0.0D, par4, f9);
            par1Tessellator.addVertexWithUV(0.0D, f13, 0.0D, par2, f9);
            par1Tessellator.addVertexWithUV(0.0D, f13, 0.0F - f1, par2, f9);
            par1Tessellator.addVertexWithUV(f, f13, 0.0F - f1, par4, f9);
        }

        par1Tessellator.draw();
    }

    private void renderItemIn2D_old(Tessellator par1Tessellator, float par2, float par3, float par4, float par5){
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
        float f1 = ((EntityPlayer)(entityclientplayermp)).prevRotationPitch + (((EntityPlayer)(entityclientplayermp)).rotationPitch - ((EntityPlayer)(entityclientplayermp)).prevRotationPitch) * par1;
        GL11.glPushMatrix();
        GL11.glRotatef(f1, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(((EntityPlayer)(entityclientplayermp)).prevRotationYaw + (((EntityPlayer)(entityclientplayermp)).rotationYaw - ((EntityPlayer)(entityclientplayermp)).prevRotationYaw) * par1, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();

        if (entityclientplayermp instanceof EntityPlayerSP)
        {
            EntityPlayerSP entityplayersp = (EntityPlayerSP)entityclientplayermp;
            float f2 = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * par1;
            float f4 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * par1;
            if (sway){
                GL11.glRotatef((((EntityPlayer)(entityplayersp)).rotationPitch - f2) * 0.1F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef((((EntityPlayer)(entityplayersp)).rotationYaw - f4) * 0.1F, 0.0F, 1.0F, 0.0F);
            }
        }

        ItemStack itemstack = itemToRender;
        float f3 = mc.theWorld.getLightBrightness(MathHelper.floor_double(((EntityPlayer)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayer)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayer)(entityclientplayermp)).posZ));
        if (!Minecraft.oldlighting){
            f3 = 1.0F;
            int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(((EntityPlayer)(entityclientplayermp)).posX), MathHelper.floor_double(((EntityPlayer)(entityclientplayermp)).posY), MathHelper.floor_double(((EntityPlayer)(entityclientplayermp)).posZ), 0);
            int k = i % 0x10000;
            int l = i / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)l / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        if (itemstack != null)
        {
            int j = Item.itemsList[itemstack.itemID].func_82790_a(itemstack, 0);
            float f8 = (float)(j >> 16 & 0xff) / 255F;
            float f13 = (float)(j >> 8 & 0xff) / 255F;
            float f19 = (float)(j & 0xff) / 255F;
            GL11.glColor4f(f3 * f8, f3 * f13, f3 * f19, 1.0F);
        }
        else
        {
            GL11.glColor4f(f3, f3, f3, 1.0F);
        }

        if (itemstack != null && itemstack.itemID == Item.map.shiftedIndex)
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
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getTexture()));

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
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/misc/mapbg.png"));
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
                mapItemRenderer.renderMap(mc.thePlayer, mc.renderEngine, mapdata);
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
                renderItem(entityclientplayermp, itemstack, 0);
                int i1 = Item.itemsList[itemstack.itemID].func_82790_a(itemstack, 1);
                float f33 = (float)(i1 >> 16 & 0xff) / 255F;
                float f36 = (float)(i1 >> 8 & 0xff) / 255F;
                float f38 = (float)(i1 & 0xff) / 255F;
                GL11.glColor4f(f3 * f33, f3 * f36, f3 * f38, 1.0F);
                renderItem(entityclientplayermp, itemstack, 1);
            }
            else
            {
                renderItem(entityclientplayermp, itemstack, 0);
            }

            GL11.glPopMatrix();
        }
        else if (!entityclientplayermp.func_82150_aj())
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
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getTexture()));
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
            int i = mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, i);
            renderFireInFirstPerson(par1);
        }

        if (mc.thePlayer.isEntityInsideOpaqueBlock())
        {
            int j = MathHelper.floor_double(mc.thePlayer.posX);
            int l = MathHelper.floor_double(mc.thePlayer.posY);
            int i1 = MathHelper.floor_double(mc.thePlayer.posZ);
            int j1 = mc.renderEngine.getTexture("/terrain.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, j1);
            int k1 = mc.theWorld.getBlockId(j, l, i1);

            if (mc.theWorld.isBlockNormalCube(j, l, i1))
            {
                renderInsideOfBlock(par1, Block.blocksList[k1].getBlockTextureFromSide(2));
            }
            else
            {
                for (int l1 = 0; l1 < 8; l1++)
                {
                    float f = ((float)((l1 >> 0) % 2) - 0.5F) * mc.thePlayer.width * 0.9F;
                    float f1 = ((float)((l1 >> 1) % 2) - 0.5F) * mc.thePlayer.height * 0.2F;
                    float f2 = ((float)((l1 >> 2) % 2) - 0.5F) * mc.thePlayer.width * 0.9F;
                    int i2 = MathHelper.floor_float((float)j + f);
                    int j2 = MathHelper.floor_float((float)l + f1);
                    int k2 = MathHelper.floor_float((float)i1 + f2);

                    if (mc.theWorld.isBlockNormalCube(i2, j2, k2))
                    {
                        k1 = mc.theWorld.getBlockId(i2, j2, k2);
                    }
                }
            }

            if (Block.blocksList[k1] != null)
            {
                renderInsideOfBlock(par1, Block.blocksList[k1].getBlockTextureFromSide(2));
            }
        }

        if (mc.thePlayer.isInsideOfMaterial(Material.water))
        {
            int k = mc.renderEngine.getTexture("/misc/water.png");
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, k);
            renderWarpedTextureOverlay(par1);
        }

        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    /**
     * Renders the texture of the block the player is inside as an overlay. Args: partialTickTime, blockTextureIndex
     */
    private void renderInsideOfBlock(float par1, int par2)
    {
        Tessellator tessellator = Tessellator.instance;
        float f = mc.thePlayer.getBrightness(par1);
        f = 0.1F;
        GL11.glColor4f(f, f, f, 0.5F);
        GL11.glPushMatrix();
        float f1 = -1F;
        float f2 = 1.0F;
        float f3 = -1F;
        float f4 = 1.0F;
        float f5 = -0.5F;
        float f6 = 0.0078125F;
        float f7 = (float)(par2 % 16) / 256F - f6;
        float f8 = ((float)(par2 % 16) + 15.99F) / 256F + f6;
        float f9 = (float)(par2 / 16) / 256F - f6;
        float f10 = ((float)(par2 / 16) + 15.99F) / 256F + f6;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(f1, f3, f5, f8, f10);
        tessellator.addVertexWithUV(f2, f3, f5, f7, f10);
        tessellator.addVertexWithUV(f2, f4, f5, f7, f9);
        tessellator.addVertexWithUV(f1, f4, f5, f8, f9);
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
            int j = Block.fire.blockIndexInTexture + i * 16;
            int k = (j & 0xf) << 4;
            int l = j & 0xf0;
            float f1 = (float)k / 256F;
            float f2 = ((float)k + 15.99F) / 256F;
            float f3 = (float)l / 256F;
            float f4 = ((float)l + 15.99F) / 256F;
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

    public void func_78444_b()
    {
        equippedProgress = 0.0F;
    }

    public void func_78445_c()
    {
        equippedProgress = 0.0F;
    }

    public void drawFirstPersonHand(Render r, int h)
    {
        if (!olddays){
            ((RenderPlayer)r).func_82441_a(mc.thePlayer);
            return;
        }
        ModelBiped modelBipedMain = null;
        try{
            RenderPlayer r1 = (RenderPlayer)r;
            modelBipedMain = ((ModelBiped)mod_OldDays.getField(r1.getClass(), r1, 0));
        }catch(ClassCastException e){
            net.minecraft.src.RenderPlayer2 r1 = (net.minecraft.src.RenderPlayer2)r;
            modelBipedMain = ((ModelBiped)mod_OldDays.getField(r1.getClass(), r1, 1));
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
