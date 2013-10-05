package net.minecraft.src;

import java.io.IOException;
import org.lwjgl.opengl.GL11;

public class RenderMD3 extends RenderLiving {
   private MD3Renderer renderer;
   private ResourceLocation[] textures;

   public RenderMD3(boolean anim, String modelname, String... textures) {
      super(null, 0.5F);
      if (textures.length > 0){
         this.textures = new ResourceLocation[textures.length];
         for(int i = 0; i < textures.length; i++){
            this.textures[i] = new ResourceLocation(textures[i]);
         }
      }else{
         return;
      }
      try {
         renderer = new MD3Renderer((new MD3Loader()).load(modelname), anim);
         System.out.println("Animation frames: " + renderer.getAnimFrames());
      } catch (IOException var2) {
         var2.printStackTrace();
      }
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double d, double d1, double d2, float f, float f1){
        renderMD3(par1EntityLiving, (float)d, (float)d1, (float)d2, f, f1);
   }

   protected int getTextureIndex(Entity e){
        return e.hashCode();
   }

   @Override
   protected ResourceLocation getEntityTexture(Entity e){
        return null;//textures[getTextureIndex(e) % textures.length];
   }

   protected float getSpeedMultiplier(Entity e){
        return 1.0F;
   }

   public final void renderMD3(EntityLiving entity, float f, float f1, float f2, float f3, float f4)
   {
        f3 = f2;
        f2 = f1 - (float)entity.yOffset;
        f1 = f;
        GL11.glPushMatrix();
        float f5 = entity.prevRenderYawOffset + (entity.renderYawOffset - entity.prevRenderYawOffset) * f4;
        GL11.glTranslatef(f1, f2, f3);
        bindTexture(textures[getTextureIndex(entity) % textures.length]);
        GL11.glRotatef(-f5 + 180F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(0.02F, -0.02F, 0.02F);
        float rotation = handleRotationFloat(entity, f4) * getSpeedMultiplier(entity);
        try{
            int frame1 = (int)rotation % renderer.getAnimFrames();
            int frame2 = (frame1 + 1) % renderer.getAnimFrames();
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glEnable(GL11.GL_NORMALIZE);
            renderer.render(frame1, frame2, rotation - (int)rotation);
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
    @Override
    protected void passSpecialRender(EntityLivingBase par1EntityLivingBase, double par2, double par4, double par6)
    {
        par4 += 0.5D;
        super.passSpecialRender(par1EntityLivingBase, par2, par4, par6);
    }
}
