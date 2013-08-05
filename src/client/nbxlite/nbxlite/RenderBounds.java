package net.minecraft.src.nbxlite;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.src.*;

public class RenderBounds{
    private static ResourceLocation classicWater = new ResourceLocation("olddays/classic_water.png");
    private static ResourceLocation classicLava = new ResourceLocation("olddays/classic_lava.png");

    private static TextureManager renderEngine;
    private static Minecraft mc;
    private static World worldObj;
    private static DynamicTexture temp;
    private static IntBuffer sheetData;
    private static int width;
    private static int height;

    private static void renderGroundBounds(float f, float ff){
        Tessellator tessellator = Tessellator.instance;
        float f1 = ODNBXlite.SurrGroundHeight - 1;
        int i1 = 128;
        if (i1 > ODNBXlite.IndevWidthX){
            i1 = ODNBXlite.IndevWidthX;
        }
        if (i1 > ODNBXlite.IndevWidthZ){
            i1 = ODNBXlite.IndevWidthZ;
        }
        Block block = Block.blocksList[ODNBXlite.SurrGroundType];
        if (block==Block.grass && (ODNBXlite.SurrGroundHeight <= ODNBXlite.SurrWaterHeight || ODNBXlite.SurrWaterType==Block.lavaStill.blockID)){
            block = Block.dirt;
        }
        if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC){
            block = Block.bedrock;
        }
        f1 += (float)block.getBlockBoundsMaxY();
        double dd = 0D;
        double dd1 = (double)i1;
        double dd2 = 0D;
        double dd3 = (double)i1;
        int i = block.colorMultiplier(worldObj, 0, ODNBXlite.SurrWaterHeight, 0);
        float ff1 = (float)(i >> 16 & 0xff) / 255F;
        float ff2 = (float)(i >> 8 & 0xff) / 255F;
        float ff3 = (float)(i & 0xff) / 255F;
        if (EntityRenderer.anaglyphEnable)
        {
            float ff4 = (ff1 * 30F + ff2 * 59F + ff3 * 11F) / 100F;
            float ff5 = (ff1 * 30F + ff2 * 70F) / 100F;
            float ff6 = (ff1 * 30F + ff3 * 70F) / 100F;
            ff1 = ff4;
            ff2 = ff5;
            ff3 = ff6;
        }
        int i2 = 2048 / i1;
        tessellator.startDrawingQuads();
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        tessellator.setTranslation(-d, -d1, -d2);
        GL11.glColor3f(ff * ff1, ff * ff2, ff * ff3);
        for (int i3 = -i1 * i2; i3 < ODNBXlite.IndevWidthX + i1 * i2; i3 += i1){
            for (int i5 = -i1 * i2; i5 < ODNBXlite.IndevWidthZ + i1 * i2; i5 += i1){
                if ((f1 < 0.0F) || (i3 < 0) || (i5 < 0) || (i3 >= ODNBXlite.IndevWidthX) || (i5 >= ODNBXlite.IndevWidthZ)){
                    tessellator.addVertexWithUV(i3, f1, i5 + i1, dd1, dd3);
                    tessellator.addVertexWithUV(i3 + i1, f1, i5 + i1, dd1, dd2);
                    tessellator.addVertexWithUV(i3 + i1, f1, i5, dd, dd2);
                    tessellator.addVertexWithUV(i3, f1, i5, dd, dd3);
                }
            }
        }
        tessellator.draw();
    }

    private static void renderSideBounds(float f){
        Tessellator tessellator = Tessellator.instance;
        Block block = Block.blocksList[ODNBXlite.SurrGroundType];
        if (block==Block.grass && (ODNBXlite.SurrGroundHeight <= ODNBXlite.SurrWaterHeight || ODNBXlite.SurrWaterType==Block.lavaStill.blockID)){
            block = Block.dirt;
        }
        if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC){
            block = Block.bedrock;
        }
        float f1 = (float)block.getBlockBoundsMaxY();
        int i1 = 128;
        if (i1 > ODNBXlite.IndevWidthX){
            i1 = ODNBXlite.IndevWidthX;
        }
        if (i1 > ODNBXlite.IndevWidthZ){
            i1 = ODNBXlite.IndevWidthZ;
        }
        double dd = (double)i1;
        double dd1 = 0D;
        double dd2 = 0D;
        double dd3 = (double)i1;
        tessellator.startDrawingQuads();
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        float i = ODNBXlite.SurrGroundHeight - i1 + f1 - 1;
        tessellator.setTranslation(-d, -d1, -d2);
        for (int i3 = 0; i3 < ODNBXlite.IndevWidthZ; i3 += i1){
            tessellator.addVertexWithUV(0, ODNBXlite.SurrGroundHeight, i3, dd1, dd2);
            tessellator.addVertexWithUV(0, ODNBXlite.SurrGroundHeight, i3 + i1, dd1, dd3);
            tessellator.addVertexWithUV(0, i, i3 + i1, dd, dd3);
            tessellator.addVertexWithUV(0, i, i3, dd, dd2);
        }
        for (int i3 = 0; i3 < ODNBXlite.IndevWidthX; i3 += i1){
            tessellator.addVertexWithUV(i3, ODNBXlite.SurrGroundHeight, ODNBXlite.IndevWidthZ, dd1, dd2);
            tessellator.addVertexWithUV(i3 + i1, ODNBXlite.SurrGroundHeight, ODNBXlite.IndevWidthZ, dd1, dd3);
            tessellator.addVertexWithUV(i3 + i1, i, ODNBXlite.IndevWidthZ, dd, dd3);
            tessellator.addVertexWithUV(i3, i, ODNBXlite.IndevWidthZ, dd, dd2);
        }
        dd2 = (double)i1;
        dd3 = 0D;
        for (int i3 = 0; i3 < ODNBXlite.IndevWidthZ; i3 += i1){
            tessellator.addVertexWithUV(ODNBXlite.IndevWidthX, i, i3, dd, dd2);
            tessellator.addVertexWithUV(ODNBXlite.IndevWidthX, i, i3 + i1, dd, dd3);
            tessellator.addVertexWithUV(ODNBXlite.IndevWidthX, ODNBXlite.SurrGroundHeight, i3 + i1, dd1, dd3);
            tessellator.addVertexWithUV(ODNBXlite.IndevWidthX, ODNBXlite.SurrGroundHeight, i3, dd1, dd2);
        }
        for (int i3 = 0; i3 < ODNBXlite.IndevWidthX; i3 += i1){
            tessellator.addVertexWithUV(i3, i, 0, dd, dd2);
            tessellator.addVertexWithUV(i3 + i1, i, 0, dd, dd3);
            tessellator.addVertexWithUV(i3 + i1, ODNBXlite.SurrGroundHeight, 0, dd1, dd3);
            tessellator.addVertexWithUV(i3, ODNBXlite.SurrGroundHeight, 0, dd1, dd2);
        }
        tessellator.draw();
    }

    private static void renderBottomBounds(float f){
        Tessellator tessellator = Tessellator.instance;
        float f1 = 0;
        int i1 = 128;
        if (i1 > ODNBXlite.IndevWidthX){
            i1 = ODNBXlite.IndevWidthX;
        }
        if (i1 > ODNBXlite.IndevWidthZ){
            i1 = ODNBXlite.IndevWidthZ;
        }
        double dd = 0D;
        double dd1 = (double)i1;
        double dd2 = 0D;
        double dd3 = (double)i1;
        tessellator.startDrawingQuads();
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        tessellator.setTranslation(-d, -d1, -d2);
        for (int i3 = 0; i3 < ODNBXlite.IndevWidthX; i3 += i1){
            for (int i5 = 0; i5 < ODNBXlite.IndevWidthZ; i5 += i1){
                tessellator.addVertexWithUV(i3, f1, i5 + i1, dd1, dd3);
                tessellator.addVertexWithUV(i3 + i1, f1, i5 + i1, dd1, dd2);
                tessellator.addVertexWithUV(i3 + i1, f1, i5, dd, dd2);
                tessellator.addVertexWithUV(i3, f1, i5, dd, dd3);
            }
        }
        tessellator.draw();
    }

    public static void renderBounds(Minecraft m, float f, boolean liquid)
    {
        if (ODNBXlite.SurrGroundType<=0 || ODNBXlite.SurrWaterType<=0){
            return;
        }
        mc = m;
        renderEngine = mc.func_110434_K();
        worldObj = mc.theWorld;
        int id = ODNBXlite.SurrGroundType;
        if (ODNBXlite.SurrGroundHeight<=ODNBXlite.SurrWaterHeight || ODNBXlite.SurrWaterType==Block.lavaStill.blockID && ODNBXlite.SurrGroundType==Block.grass.blockID){
            id = Block.dirt.blockID;
        }
        if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC){
            id = Block.bedrock.blockID;
        }
        GL11.glPushMatrix();
        float ff = 1.0F;
        if (!Minecraft.oldlighting){
            mc.entityRenderer.enableLightmap(f);
            int l = ODNBXlite.getLightInBounds(ODNBXlite.SurrGroundHeight, liquid);
            int i1 = l % 0x10000;
            int j1 = l / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1 / 1.0F, (float)j1 / 1.0F);
        }else{
            ff = ODNBXlite.getLightFloat(ODNBXlite.SurrGroundHeight, liquid);
        }
        boolean anim = ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV;
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glRotatef(90F, 0F, 0F, 1F);
        bindTexture(Block.blocksList[id], false, true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        renderGroundBounds(f, ff);
        if (ODNBXlite.MapFeatures == ODNBXlite.FEATURES_CLASSIC && (ODNBXlite.SurrGroundHeight>=0 || ODNBXlite.SurrWaterHeight>=0)){
            bindTexture(Block.bedrock, true, true);
            renderSideBounds(f);
            bindTexture(Block.bedrock, false, true);
            renderBottomBounds(f);
        }
        if (!Minecraft.oldlighting){
            int l = ODNBXlite.getLightInBounds(ODNBXlite.SurrWaterHeight, liquid);
            int i1 = l % 0x10000;
            int j1 = l / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1 / 1.0F, (float)j1 / 1.0F);
        }else{
            ff = ODNBXlite.getLightFloat(ODNBXlite.SurrWaterHeight, liquid);
        }
        bindTexture(Block.blocksList[ODNBXlite.SurrWaterType], false, anim);
        GL11.glEnable(GL11.GL_BLEND);
        if (liquid){
            renderLiquidBounds(f, ff);
        }else{
            Tessellator tessellator = Tessellator.instance;
            tessellator.setTranslation(0, 0, 0);
        }
        GL11.glRotatef(-90F, 0F, 0F, 1F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_BLEND);
        if (!Minecraft.oldlighting){
            mc.entityRenderer.disableLightmap(f);
        }
        GL11.glPopMatrix();
    }

    private static void renderLiquidBounds(float f, float ff)
    {
        float f1 = (float)ODNBXlite.SurrWaterHeight;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        int i1 = 128;
        if (i1 > ODNBXlite.IndevWidthX){
            i1 = ODNBXlite.IndevWidthX;
        }
        if (i1 > ODNBXlite.IndevWidthZ){
            i1 = ODNBXlite.IndevWidthZ;
        }
        double dd = 0D;
        double dd1 = (double)i1;
        double dd2 = 0D;
        double dd3 = (double)i1;
        int i = Block.blocksList[ODNBXlite.SurrWaterType].colorMultiplier(worldObj, 0, ODNBXlite.SurrWaterHeight, 0);
        float ff1 = (float)(i >> 16 & 0xff) / 255F;
        float ff2 = (float)(i >> 8 & 0xff) / 255F;
        float ff3 = (float)(i & 0xff) / 255F;
        if (EntityRenderer.anaglyphEnable)
        {
            float ff4 = (ff1 * 30F + ff2 * 59F + ff3 * 11F) / 100F;
            float ff5 = (ff1 * 30F + ff2 * 70F) / 100F;
            float ff6 = (ff1 * 30F + ff3 * 70F) / 100F;
            ff1 = ff4;
            ff2 = ff5;
            ff3 = ff6;
        }
        GL11.glColor3f(ff * ff1, ff * ff2, ff * ff3);
        int i2 = 2048 / i1;
        tessellator.startDrawingQuads();
        float f2 = (float)Block.waterStill.getBlockBoundsMinX();
        float f3 = (float)Block.waterStill.getBlockBoundsMinZ();
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        tessellator.setTranslation(-d, -d1, -d2);
        for (int i3 = -i1 * i2; i3 < ODNBXlite.IndevWidthX + i1 * i2; i3 += i1){
            for (int i5 = -i1 * i2; i5 < ODNBXlite.IndevWidthZ + i1 * i2; i5 += i1){
                float f4 = f1 + (float)Block.waterStill.getBlockBoundsMaxY() - 1.1121F;
                if ((f1 < 0.0F) || (i3 < 0) || (i5 < 0) || (i3 >= ODNBXlite.IndevWidthX) || (i5 >= ODNBXlite.IndevWidthZ)){
                    tessellator.addVertexWithUV(i3 + f2, f4, i5 + i1 + f3, dd1, dd3);
                    tessellator.addVertexWithUV(i3 + i1 + f2, f4, i5 + i1 + f3, dd1, dd2);
                    tessellator.addVertexWithUV(i3 + i1 + f2, f4, i5 + f3, dd, dd2);
                    tessellator.addVertexWithUV(i3 + f2, f4, i5 + f3, dd, dd3);
                    if (ODNBXlite.SurrWaterType==Block.waterStill.blockID || ODNBXlite.SurrWaterType==Block.waterMoving.blockID){
                        tessellator.addVertexWithUV(i3 + f2, f4, i5 + f3, dd, dd3);
                        tessellator.addVertexWithUV(i3 + i1 + f2, f4, i5 + f3, dd, dd2);
                        tessellator.addVertexWithUV(i3 + i1 + f2, f4, i5 + i1 + f3, dd1, dd2);
                        tessellator.addVertexWithUV(i3 + f2, f4, i5 + i1 + f3, dd1, dd3);
                    }
                }
            }
        }
        tessellator.draw();
        tessellator.setTranslation(0, 0, 0);
    }

    private static void bindTexture(Block block, boolean side, boolean anim){
        if (!anim){
            if (block == Block.waterStill || block == Block.waterMoving){
                renderEngine.func_110577_a(classicWater);
                return;
            }else if (block == Block.lavaStill || block == Block.lavaMoving){
                renderEngine.func_110577_a(classicLava);
                return;
            }
        }
        TextureAtlasSprite icon = (TextureAtlasSprite)(block.getBlockTextureFromSide(side ? 2 : 1));
        int prevWidth = width;
        int prevHeight = height;
        width = icon.getOriginX();
        height = icon.getOriginY();
        double u = icon.getMaxU() - icon.getMinU();
        double v = icon.getMaxV() - icon.getMinV();
        int sheetWidth = (int)((double)width / u);
        int sheetHeight = (int)((double)height / v);
        if (temp == null || prevWidth != width || prevHeight != height){
            temp = new DynamicTexture(width, height);
            System.gc();
        }
        if (sheetData == null || prevWidth != width || prevHeight != height){
            sheetData = BufferUtils.createIntBuffer(sheetWidth * sheetHeight);
            System.gc();
        }
        renderEngine.func_110577_a(TextureMap.field_110575_b);
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, sheetData);
        sheetData.clear();
        int[] data = temp.func_110565_c();
        for (int i = 0; i < height; i++){
            int start = (((icon.func_110967_i() + i) * sheetWidth) + icon.func_130010_a());
            sheetData.position(start).limit(start + width);
            for (int j = 0; j < width; j++){
                data[j + i * width] = sheetData.get();
            }
            sheetData.clear();
        }
        temp.func_110564_a();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, temp.func_110552_b());
    }
}