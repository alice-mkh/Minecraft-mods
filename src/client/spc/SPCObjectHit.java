package net.minecraft.src;


public class SPCObjectHit {
   public int blockx;
   public int blocky;
   public int blockz;
   public int sidehit;
   public SPCEntity entity;
   public MovingObjectPosition m;
   
   public SPCObjectHit(MovingObjectPosition m) {
      this.m = m;
      if (m == null) {
         blockx = -1;
         blocky = -1;
         blockz = -1;
         entity = null;
         sidehit = -1;
         return;
      }
      blockx = m.blockX;
      blocky = m.blockY;
      blockz = m.blockZ;
      sidehit = m.sideHit;
      if (m.entityHit != null) {
         entity = new SPCEntity(m.entityHit);
      }
   }
   
   public MovingObjectPosition getMOP() {
      return m;
   }
}
