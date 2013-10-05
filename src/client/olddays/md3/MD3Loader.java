package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;

public class MD3Loader {

   public final MD3Model load(String var1) throws IOException {
      InputStream var5 = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(var1)).getInputStream();
      ByteArrayOutputStream var2 = new ByteArrayOutputStream();
      byte[] var3 = new byte[4096];

      int var4;
      while((var4 = var5.read(var3)) >= 0) {
         var2.write(var3, 0, var4);
      }

      var5.close();
      var2.close();
      ByteBuffer var6 = ByteBuffer.wrap(var2.toByteArray());
      return load(var6);
   }

   private MD3Model load(ByteBuffer var1) throws IOException {
      Vec3Pool vec3Pool = new Vec3Pool(300, 2000);
      var1.order(ByteOrder.LITTLE_ENDIAN);
      if(!readString(var1, 4).equals("IDP3")) {
         throw new IOException("Not a valid MD3 file (bad magic number)");
      } else {
         MD3Model model = new MD3Model();
         var1.getInt();
         readString(var1, 64);
         var1.getInt();
         int frames = var1.getInt();
         System.out.println(frames + " frames");
         int tags = var1.getInt();
         int var5 = var1.getInt();
         var1.getInt();
         int frameOffset = var1.getInt();
         var1.getInt();
         int surfaceOffset = var1.getInt();
         var1.getInt();
         model.animFrames = frames;
         model.frames = new MD3Frame[frames];
         model.tags = new HashMap();
         model.surfaces = new MD3Surface[var5];
         var1.position(frameOffset);

         for(int i = 0; i < frames; ++i) {
            MD3Frame var12 = new MD3Frame();
            var12.min = nextVec3(vec3Pool, var1);
            var12.max = nextVec3(vec3Pool, var1);
            var12.origin = nextVec3(vec3Pool, var1);
            var12.radius = var1.getFloat();
            var12.name = readString(var1, 16);
            model.frames[i] = var12;
         }

         MD3Tag[] var14 = new MD3Tag[tags];

         for(int i = 0; i < tags; ++i) {
            var14[i] = new MD3Tag(frames);
         }

         for(int var8 = 0; var8 < frames; ++var8) {
            for(int var9 = 0; var9 < tags; ++var9) {
               MD3Tag var11 = var14[var9];
               var11.name = readString(var1, 64);
               var11.coords[var8] = nextVec3(vec3Pool, var1);
               var11.c[var8] = nextVec3(vec3Pool, var1);
               var11.d[var8] = nextVec3(vec3Pool, var1);
               var11.e[var8] = nextVec3(vec3Pool, var1);
            }
         }

         for(int i = 0; i < tags; ++i) {
            model.tags.put(var14[i].name, var14[i]);
         }

         var1.position(surfaceOffset);

         for(int var8 = 0; var8 < var5; ++var8) {
            model.surfaces[var8] = loadSurface(var1);
         }

         return model;
      }
   }

   private MD3Surface loadSurface(ByteBuffer var1) throws IOException {
      int var2 = var1.position();
      if(!readString(var1, 4).equals("IDP3")) {
         throw new IOException("Not a valid MD3 file (bad surface magic number)");
      } else {
         String name = readString(var1, 64);
         System.out.println("Name: " + name);
         var1.getInt();
         int frames = var1.getInt();
         int var4 = var1.getInt();
         int verts = var1.getInt();
         int var6 = var1.getInt();
         MD3Surface surface = new MD3Surface(var6, verts, frames);
         int ofsTriangles = var1.getInt() + var2;
         int ofsShaders = var1.getInt() + var2;
         int ofsSt = var1.getInt() + var2;
         var2 += var1.getInt();
         var1.getInt();
         surface.verts = verts;
         surface.shaders = new MD3Shader[var4];
         System.out.println("Triangles: " + var6);
         System.out.println("OFS_SHADERS: " + ofsShaders + " (current location: " + var1.position() + ")");
         var1.position(ofsShaders);

         for(int i = 0; i < var4; ++i) {
           MD3Shader shader = new MD3Shader();
           readString(var1, 64);
           var1.getInt();
           surface.shaders[i] = shader;
         }

         System.out.println("OFS_TRIANGLES: " + ofsTriangles + " (current location: " + var1.position() + ")");
         var1.position(ofsTriangles);

         for(int i = 0; i < var6 * 3; ++i) {
            surface.triangles.put(var1.getInt());
         }

         System.out.println("OFS_ST: " + ofsSt + " (current location: " + var1.position() + ")");
         var1.position(ofsSt);

         for(int i = 0; i < verts << 1; ++i) {
            surface.d.put(var1.getFloat());
         }

         System.out.println("OFS_XYZ_NORMAL: " + var2 + " (current location: " + var1.position() + ")");
         var1.position(var2);

         for(int i = 0; i < verts * frames; ++i) {
            surface.vertices.put((float)var1.getShort() / 64.0F);
            surface.vertices.put((float)var1.getShort() / 64.0F);
            surface.vertices.put((float)var1.getShort() / 64.0F);
            double var15 = (double)(var1.get() & 255) * Math.PI * 2.0D / 255.0D;
            double var17 = (double)(var1.get() & 255) * Math.PI * 2.0D / 255.0D;
            float var19 = (float)(Math.cos(var17) * Math.sin(var15));
            float var21 = (float)(Math.sin(var17) * Math.sin(var15));
            float var22 = (float)Math.cos(var15);
            surface.normals.put(var19);
            surface.normals.put(var21);
            surface.normals.put(var22);
         }

         return surface;
      }
   }

   private static Vec3 nextVec3(Vec3Pool vec3Pool, ByteBuffer var0) {
      float var1 = var0.getFloat();
      float var2 = var0.getFloat();
      float var3 = var0.getFloat();
      return vec3Pool.getVecFromPool(var1, var2, var3);
   }

   private static String readString(ByteBuffer var0, int var1) {
      byte[] var3 = new byte[var1];
      var0.get(var3);

      for(int var2 = 0; var2 < var3.length; ++var2) {
         if(var3[var2] == 0) {
            return new String(var3, 0, var2);
         }
      }

      return new String(var3);
   }
}
