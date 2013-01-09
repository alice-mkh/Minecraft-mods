package net.minecraft.src;


public final class PathPointIndev {

   public final int x;
   public final int y;
   public final int z;
   public final int hash;
   int e = -1;
   float f;
   float g;
   float h;
   PathPointIndev i;
   public boolean j = false;


   public PathPointIndev(int var1, int var2, int var3) {
      x = var1;
      y = var2;
      z = var3;
      hash = var1 | var2 << 10 | var3 << 20;
   }

   public final float a(PathPointIndev var1) {
      float var2 = (float)(var1.x - x);
      float var3 = (float)(var1.y - y);
      float var4 = (float)(var1.z - z);
      return MathHelper.sqrt_double(var2 * var2 + var3 * var3 + var4 * var4);
   }

   public final boolean equals(Object var1) {
      return ((PathPointIndev)var1).hash == hash;
   }

   public final int hashCode() {
      return hash;
   }

   public final boolean a() {
      return this.e >= 0;
   }

   public final String toString() {
      return x + ", " + y + ", " + z;
   }
}
