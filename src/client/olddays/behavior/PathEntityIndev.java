package net.minecraft.src;

public final class PathEntityIndev {

   private final PathPointIndev[] a;
   private int b;


   public PathEntityIndev(PathPointIndev[] var1) {
      this.a = var1;
   }

   public final void a() {
      ++this.b;
   }

   public final boolean b() {
      return this.b >= this.a.length;
   }

   public final Vec3 a(Entity var1) {
      float var2 = (float)this.a[this.b].x + (float)((int)(var1.width + 1.0F)) * 0.5F;
      float var3 = (float)this.a[this.b].y;
      float var4 = (float)this.a[this.b].z + (float)((int)(var1.width + 1.0F)) * 0.5F;
      return var1.worldObj.getWorldVec3Pool().getVecFromPool(var2, var3, var4);
   }
}
