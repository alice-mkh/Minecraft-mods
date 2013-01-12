package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public final class PathFinderIndev {

   public static int width = 0;
   public static int length = 0;
   public static int height = 0;

   public World worldObj;
   private PathIndev b = new PathIndev();
   private Map c = new HashMap();
   private PathPointIndev[] d = new PathPointIndev[32];


   public PathFinderIndev(World var1) {
      worldObj = var1;
   }

   public final PathEntityIndev a(Entity var1, Entity var2, float var3) {
      return this.a(var1, (float)var2.posX, (float)var2.boundingBox.minY, (float)var2.posZ, 16.0F);
   }

   public final PathEntityIndev a(Entity var1, int var2, int var3, int var4, float var5) {
      return this.a(var1, (float)var2 + 0.5F, (float)var3 + 0.5F, (float)var4 + 0.5F, 16.0F);
   }

   private PathEntityIndev a(Entity var1, double var2, double var3, double var4, float var5) {
      this.b.a();
      this.c.clear();
      PathPointIndev var6 = this.a((int)var1.boundingBox.minX, (int)var1.boundingBox.minY, (int)var1.boundingBox.minZ);
      PathPointIndev var21 = this.a((int)(var2 - var1.width / 2.0F), (int)var3, (int)(var4 - var1.width / 2.0F));
      PathPointIndev var23 = new PathPointIndev((int)(var1.width + 1.0F), (int)(var1.height + 1.0F), (int)(var1.width + 1.0F));
      float var26 = var5;
      PathPointIndev var25 = var23;
      PathPointIndev var24 = var21;
      Entity var22 = var1;
      PathFinderIndev var20 = this;
      var6.f = 0.0F;
      var6.g = var6.a(var21);
      var6.h = var6.g;
      this.b.a();
      this.b.a(var6);
      PathPointIndev var7 = var6;

      PathEntityIndev var10000;
      while(true) {
         if(var20.b.c()) {
            var10000 = var7 == var6?null:a(var7);
            break;
         }

         PathPointIndev var8 = var20.b.b();
         if(var8.hash == var24.hash) {
            var10000 = a(var24);
            break;
         }

         if(var8.a(var24) < var7.a(var24)) {
            var7 = var8;
         }

         var8.j = true;
         int var15 = 0;
         byte var16 = 0;
         if(var20.a(var8.x, var8.y + 1, var8.z, var25) > 0) {
            var16 = 1;
         }

         PathPointIndev var17 = var20.a(var22, var8.x, var8.y, var8.z + 1, var25, var16);
         PathPointIndev var18 = var20.a(var22, var8.x - 1, var8.y, var8.z, var25, var16);
         PathPointIndev var19 = var20.a(var22, var8.x + 1, var8.y, var8.z, var25, var16);
         PathPointIndev var10 = var20.a(var22, var8.x, var8.y, var8.z - 1, var25, var16);
         if(var17 != null && !var17.j && var17.a(var24) < var26) {
            ++var15;
            var20.d[0] = var17;
         }

         if(var18 != null && !var18.j && var18.a(var24) < var26) {
            var20.d[var15++] = var18;
         }

         if(var19 != null && !var19.j && var19.a(var24) < var26) {
            var20.d[var15++] = var19;
         }

         if(var10 != null && !var10.j && var10.a(var24) < var26) {
            var20.d[var15++] = var10;
         }

         int var9 = var15;

         for(int var27 = 0; var27 < var9; ++var27) {
            PathPointIndev var11 = var20.d[var27];
            float var12 = var8.f + var8.a(var11);
            if(!var11.a() || var12 < var11.f) {
               var11.i = var8;
               var11.f = var12;
               var11.g = var11.a(var24);
               if(var11.a()) {
                  var20.b.a(var11, var11.f + var11.g);
               } else {
                  var11.h = var11.f + var11.g;
                  var20.b.a(var11);
               }
            }
         }
      }

      return var10000;
   }

   private PathPointIndev a(Entity var1, int var2, int var3, int var4, PathPointIndev var5, int var6) {
      PathPointIndev var8 = null;
      if(this.a(var2, var3, var4, var5) > 0) {
         var8 = this.a(var2, var3, var4);
      }

      if(var8 == null && this.a(var2, var3 + var6, var4, var5) > 0) {
         var8 = this.a(var2, var3 + var6, var4);
      }

      if(var8 != null) {
         int var7;
         for(var6 = 0; var3 > 0 && (var7 = this.a(var2, var3 - 1, var4, var5)) > 0; var8 = this.a(var2, var3, var4)) {
            if(var7 < 0) {
               return null;
            }

            ++var6;
            if(var6 >= 4) {
               return null;
            }

            --var3;
         }

         Material var9 = worldObj.getBlockMaterial(var2, var3 - 1, var4);
         if(var9 == Material.water || var9 == Material.lava) {
            return null;
         }
      }

      return var8;
   }

   private final PathPointIndev a(int var1, int var2, int var3) {
      int var4 = var1 | var2 << 10 | var3 << 20;
      PathPointIndev var5;
      if((var5 = (PathPointIndev)this.c.get(Integer.valueOf(var4))) == null) {
         var5 = new PathPointIndev(var1, var2, var3);
         this.c.put(Integer.valueOf(var4), var5);
      }

      return var5;
   }

   private int a(int var1, int var2, int var3, PathPointIndev var4) {
      for(int var5 = var1; var5 < var1 + var4.x; ++var5) {
         if((var5 < 0 || var5 >= width) && width > 0) {
            return 0;
         }

         for(int var6 = var2; var6 < var2 + var4.y; ++var6) {
            if((var6 < 0 || var6 >= height) && height > 0) {
               return 0;
            }

            int var7 = var3;

            while(var7 < var3 + var4.z) {
               if((var7 >= 0 && var7 < length) || length <= 0) {
                  Material var8 = worldObj.getBlockMaterial(var1, var2, var3);
                  if(var8.blocksMovement()) {
                     return 0;
                  }

                  if(var8 != Material.water && var8 != Material.lava) {
                     ++var7;
                     continue;
                  }

                  return -1;
               }

               return 0;
            }
         }
      }

      return 1;
   }

   private static PathEntityIndev a(PathPointIndev var0) {
      int var1 = 1;

      PathPointIndev var2;
      for(var2 = var0; var2.i != null; var2 = var2.i) {
         ++var1;
      }

      PathPointIndev[] var3 = new PathPointIndev[var1];
      var2 = var0;
      --var1;

      for(var3[var1] = var0; var2.i != null; var3[var1] = var2) {
         var2 = var2.i;
         --var1;
      }

      return new PathEntityIndev(var3);
   }
}
