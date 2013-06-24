package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderGroup extends Render{
    @Override
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Group.GroupBlock b = (Group.GroupBlock)par1Entity;
        if (b.list < 0){
            b.list = GLAllocation.generateDisplayLists(1);
        }
        if (b.update){
            RenderBlocks rb = new RenderBlocks(b.group);
            GL11.glNewList(b.list, GL11.GL_COMPILE);
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            loadTexture("/terrain.png");
            Tessellator tessellator1 = Tessellator.instance;
            tessellator1.startDrawingQuads();
            tessellator1.setTranslation(-b.x, -b.y, -b.z);
            Block block = Block.blocksList[b.id];
            rb.setRenderBoundsFromBlock(block);
            rb.renderBlockAllFaces(block, b.x, b.y, b.z);
            tessellator1.setTranslation(0.0D, 0.0D, 0.0D);
            tessellator1.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
            GL11.glEndList();
            b.update = false;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(par2, par4, par6);
        float yaw = par1Entity.prevRotationYaw + (par1Entity.rotationYaw - par1Entity.prevRotationYaw) * par9;
//         float pitch = par1Entity.prevRotationPitch + (par1Entity.rotationPitch - par1Entity.prevRotationPitch) * par9;
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
//         GL11.glRotatef(pitch, (float)Math.sin(yaw * Math.PI / 180), 0.0F, (float)Math.cos(yaw * Math.PI / 180));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glCallList(b.list);
        GL11.glPopMatrix();
    }
}