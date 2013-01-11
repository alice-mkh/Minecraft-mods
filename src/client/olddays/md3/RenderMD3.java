package net.minecraft.src;

import java.io.IOException;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public final class RenderMD3 extends Render {
   public static boolean labels = false;
   public static boolean oldlabels = false;

   private MD3Renderer renderer;
   private String[] textures;

   public RenderMD3(String modelname, String... textures) {
      shadowSize = 0.5F;
      if (textures.length > 0){
         this.textures = textures;
      }else{
         return;
      }
      try {
         renderer = new MD3Renderer((new MD3Loader()).load(modelname));
         System.out.println("Animation frames: " + renderer.getAnimFrames());
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1){
        renderMD3((EntityLiving)entity, (float)d, (float)d1, (float)d2, f, f1);
   }

   protected String getTextureName(EntityLiving e){
      int index = e.hashCode();
      return textures[index % textures.length];
   }

   public final void renderMD3(EntityLiving entity, float f, float f1, float f2, float f3, float f4)
   {
        f3 = f2;
        f2 = f1;
        f1 = f;
        GL11.glPushMatrix();
        float f5 = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * f4;
        GL11.glTranslatef(f1, f2, f3);
        loadTexture(getTextureName(entity));
        GL11.glRotatef(-f5 + 180F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(0.02F, -0.02F, 0.02F);
        float wtf = ((float)entity.entityAge2 + f4) * entity.field_70730_aX;
        try{
            int frame1 = (int)wtf % renderer.getAnimFrames();
            int frame2 = (frame1 + 1) % renderer.getAnimFrames();
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glEnable(GL11.GL_NORMALIZE);
            renderer.renderFrame(frame1, frame2, wtf - (int)wtf);
            GL11.glDisable(GL11.GL_NORMALIZE);
        }catch(Exception e){
            e.printStackTrace();
        }
        GL11.glPopMatrix();
        passSpecialRender(entity, f1, f2, f3);
    }

    /**
     * Passes the specialRender and renders it
     */
    protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6)
    {
        if (Minecraft.isDebugInfoEnabled() && labels){
            renderLivingLabel(par1EntityLiving, Integer.toString(par1EntityLiving.entityId), par2, par4 + 0.5D, par6, 64);
        }
    }

    /**
     * Draws the debug or playername text above a living
     */
    protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9)
    {
        double d = par1EntityLiving.getDistanceSqToEntity(renderManager.livingPlayer);

        if (d > (double)(par9 * par9) && !oldlabels)
        {
            return;
        }

        FontRenderer fontrenderer = getFontRendererFromRenderManager();
        float f = 1.6F;
        float f1 = 0.01666667F * f;
        if (oldlabels){
            f1 = (float)((double)f1 * (Math.sqrt(Math.sqrt(d)) / 2D));
        }
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + 2.3F, (float)par7);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f1, -f1, f1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        byte byte0 = 0;

        if (par2Str.equals("deadmau5"))
        {
            byte0 = -10;
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        int i = fontrenderer.getStringWidth(par2Str) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex(-i - 1, -1 + byte0, 0.0D);
        tessellator.addVertex(-i - 1, 8 + byte0, 0.0D);
        tessellator.addVertex(i + 1, 8 + byte0, 0.0D);
        tessellator.addVertex(i + 1, -1 + byte0, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, byte0, 0x20ffffff);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        fontrenderer.drawString(par2Str, -fontrenderer.getStringWidth(par2Str) / 2, byte0, -1);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }
}
