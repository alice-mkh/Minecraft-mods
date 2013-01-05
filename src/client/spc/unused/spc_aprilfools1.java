package net.minecraft.src;

import java.util.Random;

public class spc_aprilfools1 extends SPCPlugin {
   private long trigger;
   private boolean triggered;
   private int count;

   public spc_aprilfools1() {
      trigger = System.currentTimeMillis() + (int)(Math.random() * 30000);
      count = 10;
   }

   @Override
   public String getVersion() {
      return "1.0";
   }

   @Override
   public String getName() {
      return "Patch";
   }

   @Override
   public void atUpdate() {
      /*if (System.currentTimeMillis() >= trigger && !triggered) {
         super.ph.sendMessage("\2474CRITICAL: An unrecoverable error has occurred.");
         triggered = true;
      }
      if (triggered && count > -1) {
         if (System.currentTimeMillis() >= trigger) {
            if (count > 0) {
               super.ph.sendMessage("\2474Your world will be permanently erased in " + count--);
               trigger = System.currentTimeMillis() + 1000;
            } else {
               super.ph.sendMessage("\247aApril fools! <3 simo_415");
               count--;
               trigger = System.currentTimeMillis() + 5000;
            }
         } 
      }
      if (triggered && count == -1 && System.currentTimeMillis() <= trigger) {
         for (int i = 0; i < 5; i++) {
            rainFish();
         }
      }*/
   }
   
   public void rainFish() {
      double x = ph.ep.posX;
      double y = ph.ep.posY;
      double z = ph.ep.posZ;
      if (ph.mc.theWorld.canBlockSeeTheSky((int)x, (int)y, (int)z)) {
         EntityItem e = new EntityItem(ph.mc.theWorld, x, y + 100, z, new ItemStack(Item.fishRaw));
         float f1 = 0.3F;
         e.motionX = Math.random() < 0.5D ? -Math.random() : Math.random();
         e.motionZ = Math.random() < 0.5D ? -Math.random() : Math.random();
         //e.motionY = Math.random() < 0.5D ? -Math.random() / 2 : Math.random() / 2;
         f1 = 0.02F;
         Random random = new Random();
         float f3 = random.nextFloat() * 3.141593F * 2.0F;
         f1 *= random.nextFloat();
         e.motionX += Math.cos(f3) * (double) f1;
         e.motionY += (random.nextFloat() - random.nextFloat()) * 0.1F;
         e.motionZ += Math.sin(f3) * (double) f1;
         e.age = 5900;
         e.delayBeforeCanPickup = 0;
         ph.mc.theWorld.spawnEntityInWorld(e);
      }
   }
}
