package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class SPCEntityCamera extends EntityPlayerSP {

   public SPCEntityCamera(Minecraft mc, World world, Session s, int i) {
      super(mc, world, s, i);
      yOffset = 1.62F;
   }

   @Override
   public boolean canBePushed() {
      return false;
   }
   
   @Override
   public void onEntityUpdate() {
   }
   
   @Override
   public void onUpdate() {
      
   }
   
   @Override
   public void onDeath(DamageSource source) {
   }
   
   @Override
   public boolean isEntityAlive() {
      return true;
   }
   
   public void setCamera(double x, double y, double z, float yaw, float pitch) {
      lastTickPosX = prevPosX = posX;
      lastTickPosY = prevPosY = posY;
      lastTickPosZ = prevPosZ = posZ;
      posX += x;
      posY += y;
      posZ += z;
      prevRotationYaw = rotationYaw;
      prevRotationPitch = rotationPitch;
      rotationYaw = yaw;
      rotationPitch = pitch;
   }
}
