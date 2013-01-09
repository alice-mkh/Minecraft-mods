package net.minecraft.src;


public final class PathIndev {

   private PathPointIndev[] a = new PathPointIndev[1024];
   private int b = 0;


   public final PathPointIndev a(PathPointIndev var1) {
      if(var1.e >= 0) {
         throw new IllegalStateException("OW KNOWS!");
      } else {
         if(this.b == this.a.length) {
            PathPointIndev[] var2 = new PathPointIndev[this.b << 1];
            System.arraycopy(this.a, 0, var2, 0, this.b);
            this.a = var2;
         }

         this.a[this.b] = var1;
         var1.e = this.b;
         this.a(this.b++);
         return var1;
      }
   }

   public final void a() {
      this.b = 0;
   }

   public final PathPointIndev b() {
      PathPointIndev var1 = this.a[0];
      this.a[0] = this.a[--this.b];
      this.a[this.b] = null;
      if(this.b > 0) {
         this.b(0);
      }

      var1.e = -1;
      return var1;
   }

   public final void a(PathPointIndev var1, float var2) {
      float var3 = var1.h;
      var1.h = var2;
      if(var2 < var3) {
         this.a(var1.e);
      } else {
         this.b(var1.e);
      }
   }

   private void a(int var1) {
      PathPointIndev var2;
      int var4;
      for(float var3 = (var2 = this.a[var1]).h; var1 > 0; var1 = var4) {
         var4 = var1 - 1 >> 1;
         PathPointIndev var5 = this.a[var4];
         if(var3 >= var5.h) {
            break;
         }

         this.a[var1] = var5;
         var5.e = var1;
      }

      this.a[var1] = var2;
      var2.e = var1;
   }

   private void b(int var1) {
      PathPointIndev var2;
      float var3 = (var2 = this.a[var1]).h;

      while(true) {
         int var4;
         int var5 = (var4 = 1 + (var1 << 1)) + 1;
         if(var4 >= this.b) {
            break;
         }

         PathPointIndev var6;
         float var7 = (var6 = this.a[var4]).h;
         PathPointIndev var8;
         float var9;
         if(var5 >= this.b) {
            var8 = null;
            var9 = Float.POSITIVE_INFINITY;
         } else {
            var9 = (var8 = this.a[var5]).h;
         }

         if(var7 < var9) {
            if(var7 >= var3) {
               break;
            }

            this.a[var1] = var6;
            var6.e = var1;
            var1 = var4;
         } else {
            if(var9 >= var3) {
               break;
            }

            this.a[var1] = var8;
            var8.e = var1;
            var1 = var5;
         }
      }

      this.a[var1] = var2;
      var2.e = var1;
   }

   public final boolean c() {
      return this.b == 0;
   }
}
