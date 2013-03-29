package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class RenderItemFrame2 extends Render
{
    public static boolean oldrotation = false;

    private final RenderBlocks renderBlocksInstance = new RenderBlocks();
    private Icon field_94147_f;

    public RenderItemFrame2()
    {
    }

    public void updateIcons(IconRegister par1IconRegister)
    {
        field_94147_f = par1IconRegister.registerIcon("itemframe_back");
    }

    public void func_82404_a(EntityItemFrame par1EntityItemFrame, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        float f = (float)(par1EntityItemFrame.posX - par2) - 0.5F;
        float f1 = (float)(par1EntityItemFrame.posY - par4) - 0.5F;
        float f2 = (float)(par1EntityItemFrame.posZ - par6) - 0.5F;
        int i = par1EntityItemFrame.xPosition + Direction.offsetX[par1EntityItemFrame.hangingDirection];
        int j = par1EntityItemFrame.yPosition;
        int k = par1EntityItemFrame.zPosition + Direction.offsetZ[par1EntityItemFrame.hangingDirection];
        GL11.glTranslatef((float)i - f, (float)j - f1, (float)k - f2);
        renderFrameItemAsBlock(par1EntityItemFrame);
        func_82402_b(par1EntityItemFrame);
        GL11.glPopMatrix();
    }

    private void renderFrameItemAsBlock(EntityItemFrame par1EntityItemFrame)
    {
        float f6 = Minecraft.oldlighting ? par1EntityItemFrame.getBrightness(0) : 1.0F;
        GL11.glPushMatrix();
        renderManager.renderEngine.bindTexture("/terrain.png");
        GL11.glRotatef(par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
        if (oldrotation){
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
        }
        Block block = Block.planks;
        float f = 0.0625F;
        float f1 = 0.75F;
        float f2 = f1 / 2.0F;
        GL11.glPushMatrix();
        renderBlocksInstance.overrideBlockBounds(0.0D, (0.5F - f2) + 0.0625F, (0.5F - f2) + 0.0625F, f * 0.5F, (0.5F + f2) - 0.0625F, (0.5F + f2) - 0.0625F);
        renderBlocksInstance.setOverrideBlockTexture(field_94147_f);
        renderBlocksInstance.renderBlockAsItem(block, 0, f6);
        renderBlocksInstance.clearOverrideBlockTexture();
        renderBlocksInstance.unlockBlockBounds();
        GL11.glPopMatrix();
        renderBlocksInstance.setOverrideBlockTexture(Block.planks.getBlockTextureFromSideAndMetadata(1, 2));
        GL11.glPushMatrix();
        renderBlocksInstance.overrideBlockBounds(0.0D, 0.5F - f2, 0.5F - f2, f + 0.0001F, (f + 0.5F) - f2, 0.5F + f2);
        renderBlocksInstance.renderBlockAsItem(block, 0, f6);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        renderBlocksInstance.overrideBlockBounds(0.0D, (0.5F + f2) - f, 0.5F - f2, f + 0.0001F, 0.5F + f2, 0.5F + f2);
        renderBlocksInstance.renderBlockAsItem(block, 0, f6);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        renderBlocksInstance.overrideBlockBounds(0.0D, 0.5F - f2, 0.5F - f2, f, 0.5F + f2, (f + 0.5F) - f2);
        renderBlocksInstance.renderBlockAsItem(block, 0, f6);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        renderBlocksInstance.overrideBlockBounds(0.0D, 0.5F - f2, (0.5F + f2) - f, f, 0.5F + f2, 0.5F + f2);
        renderBlocksInstance.renderBlockAsItem(block, 0, f6);
        GL11.glPopMatrix();
        renderBlocksInstance.unlockBlockBounds();
        renderBlocksInstance.clearOverrideBlockTexture();
        GL11.glPopMatrix();
    }

    private void func_82402_b(EntityItemFrame par1EntityItemFrame)
    {
        ItemStack itemstack = par1EntityItemFrame.getDisplayedItem();

        if (itemstack == null)
        {
            return;
        }

        EntityItem entityitem = new EntityItem(par1EntityItemFrame.worldObj, 0.0D, 0.0D, 0.0D, itemstack);
        entityitem.getEntityItem().stackSize = 1;
        entityitem.hoverStart = 0.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef(-0.453125F * (float)Direction.offsetX[par1EntityItemFrame.hangingDirection], -0.18F, -0.453125F * (float)Direction.offsetZ[par1EntityItemFrame.hangingDirection]);
        GL11.glRotatef(180F + par1EntityItemFrame.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-90 * par1EntityItemFrame.getRotation(), 0.0F, 0.0F, 1.0F);

        switch (par1EntityItemFrame.getRotation())
        {
            case 1:
                GL11.glTranslatef(-0.16F, -0.16F, 0.0F);
                break;
            case 2:
                GL11.glTranslatef(0.0F, -0.32F, 0.0F);
                break;
            case 3:
                GL11.glTranslatef(0.16F, -0.16F, 0.0F);
                break;
        }

        if (entityitem.getEntityItem().getItem() == Item.map)
        {
            float f6 = Minecraft.oldlighting ? par1EntityItemFrame.getBrightness(0) : 1.0F;
            renderManager.renderEngine.bindTexture("/misc/mapbg.png");
            Tessellator tessellator = Tessellator.instance;
            GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            GL11.glScalef(0.00390625F, 0.00390625F, 0.00390625F);
            GL11.glTranslatef(-65F, -107F, -1F);
            GL11.glNormal3f(0.0F, 0.0F, -1F);
            GL11.glColor3f(f6, f6, f6);
            tessellator.startDrawingQuads();
            byte byte0 = 7;
            tessellator.addVertexWithUV(0 - byte0, 128 + byte0, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(128 + byte0, 128 + byte0, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(128 + byte0, 0 - byte0, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(0 - byte0, 0 - byte0, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            MapData mapdata = Item.map.getMapData(entityitem.getEntityItem(), par1EntityItemFrame.worldObj);

            if (mapdata != null)
            {
                renderManager.itemRenderer.mapItemRenderer.renderMap(null, renderManager.renderEngine, mapdata);
            }
        }
        else
        {
            if (entityitem.getEntityItem().getItem() == Item.compass)
            {
                TextureCompass texturecompass = TextureCompass.compassTexture;
                double d = texturecompass.currentAngle;
                double d1 = texturecompass.angleDelta;
                texturecompass.currentAngle = 0.0D;
                texturecompass.angleDelta = 0.0D;
                texturecompass.updateCompass(par1EntityItemFrame.worldObj, par1EntityItemFrame.posX, par1EntityItemFrame.posZ, MathHelper.wrapAngleTo180_float(180 + par1EntityItemFrame.hangingDirection * 90), false, true);
                texturecompass.currentAngle = d;
                texturecompass.angleDelta = d1;
            }

            float f = renderManager.playerViewY;
            renderManager.playerViewY = 180F;
            RenderItem.renderInFrame = true;
            RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
            RenderItem.renderInFrame = false;
            renderManager.playerViewY = f;
            GL11.glEnable(GL11.GL_LIGHTING);

            if (entityitem.getEntityItem().getItem() == Item.compass)
            {
                TextureCompass texturecompass1 = TextureCompass.compassTexture;
                texturecompass1.updateAnimation();
            }
        }

        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        func_82404_a((EntityItemFrame)par1Entity, par2, par4, par6, par8, par9);
    }
}
