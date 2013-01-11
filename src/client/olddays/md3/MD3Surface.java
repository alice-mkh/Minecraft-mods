package net.minecraft.src;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;

public final class MD3Surface {

   public int verts;
   private int frames;
   public MD3Shader[] shaders;
   public IntBuffer triangles;
   public FloatBuffer d;
   public FloatBuffer vertices;
   public FloatBuffer normals;
   private float[] h;
   private float[] i;


   public MD3Surface(int var1, int var2, int var3) {
      verts = var2;
      frames = var3;
      triangles = BufferUtils.createIntBuffer(var1 * 3);
      this.d = BufferUtils.createFloatBuffer(var2 << 1);
      vertices = BufferUtils.createFloatBuffer(var2 * (var3 + 2) * 3);
      normals = BufferUtils.createFloatBuffer(var2 * (var3 + 2) * 3);
      this.h = new float[var2 * 3];
      this.i = new float[var2 * 3];
   }

   public final void setFrame(int var1, int var2, float var3) {
      triangles.position(0).limit(triangles.capacity());
      this.d.position(0).limit(this.d.capacity());
      int var4 = var1;
      if(var3 != 0.0F) {
         this.interpolate(vertices, var1, var2, var3);
         this.interpolate(normals, var1, var2, var3);
         var4 = frames;
      }

      vertices.clear().position(var4 * verts * 3).limit((var4 + 1) * verts * 3);
      normals.clear().position(var4 * verts * 3).limit((var4 + 1) * verts * 3);
   }

   private void interpolate(FloatBuffer var1, int var2, int var3, float var4) {
      var1.clear().position(var2 * verts * 3).limit((var2 + 1) * verts * 3);
      var1.get(this.h);
      var1.clear().position(var3 * verts * 3).limit((var3 + 1) * verts * 3);
      var1.get(this.i);

      for(int var6 = 0; var6 < verts * 3; ++var6) {
         this.h[var6] += (this.i[var6] - this.h[var6]) * var4;
      }

      var2 = frames;
      var1.clear().position(var2 * verts * 3);
      var1.put(this.h);
   }
}