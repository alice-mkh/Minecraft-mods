package net.minecraft.src.nbxlite;

import java.util.ArrayList;
import java.util.List;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class RenderBounds{
    private static ByteBuffer imageData;
    private static RenderEngine renderEngine;
    private static Minecraft mc;
    private static World worldObj;

    private static void renderGroundBounds(float f){
        Tessellator tessellator = Tessellator.instance;
        float f1 = mod_noBiomesX.SurrGroundHeight;
        int i1 = 128;
        if (i1 > mod_noBiomesX.IndevWidthX){
            i1 = mod_noBiomesX.IndevWidthX;
        }
        if (i1 > mod_noBiomesX.IndevWidthZ){
            i1 = mod_noBiomesX.IndevWidthZ;
        }
        Block block = Block.blocksList[mod_noBiomesX.SurrGroundType];
        if (block==Block.grass && (mod_noBiomesX.SurrGroundHeight <= mod_noBiomesX.SurrWaterHeight || mod_noBiomesX.SurrWaterType==Block.lavaStill.blockID)){
            block = Block.dirt;
        }
        if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_CLASSIC){
            block = Block.bedrock;
        }
//         int j = block.getBlockTextureFromSideAndMetadata(1, 0);
//         if (globalRenderBlocks.overrideBlockTexture >= 0){
//             j = globalRenderBlocks.overrideBlockTexture;
//         }
//         int k = (j & 0xf) << 4;
//         int l = j & 0xf0;
//         double dd = (float)k / 256F;
//         double dd1 = ((float)k + 15.999F) / 256F;
//         double dd2 = (float)l / 256F;
//         double dd3 = ((float)l + 15.999F) / 256F;
        double dd = 0D;
        double dd1 = (double)i1;
        double dd2 = 0D;
        double dd3 = (double)i1;
        int i = block.colorMultiplier(worldObj, 0, mod_noBiomesX.SurrWaterHeight, 0);
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
        tessellator.setColorOpaque_F(ff1, ff2, ff3);
        for (int i3 = -i1 * i2; i3 < mod_noBiomesX.IndevWidthX + i1 * i2; i3 += i1){
            for (int i5 = -i1 * i2; i5 < mod_noBiomesX.IndevWidthZ + i1 * i2; i5 += i1){
                if ((f1 < 0.0F) || (i3 < 0) || (i5 < 0) || (i3 >= mod_noBiomesX.IndevWidthX) || (i5 >= mod_noBiomesX.IndevWidthZ)){
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
        float f1 = mod_noBiomesX.SurrGroundHeight;
        int i1 = 128;
        if (i1 > mod_noBiomesX.IndevWidthX){
            i1 = mod_noBiomesX.IndevWidthX;
        }
        if (i1 > mod_noBiomesX.IndevWidthZ){
            i1 = mod_noBiomesX.IndevWidthZ;
        }
//         Block block = Block.bedrock;
//         int j = block.getBlockTextureFromSideAndMetadata(1, 0);
//         if (globalRenderBlocks.overrideBlockTexture >= 0){
//             j = globalRenderBlocks.overrideBlockTexture;
//         }
//         int k = (j & 0xf) << 4;
//         int l = j & 0xf0;
        double dd = (double)i1;
        double dd1 = 0D;
        double dd2 = 0D;
        double dd3 = (double)i1;
//         double dd = (float)k / 256F;
//         double dd1 = ((float)k + 15.999F) / 256F;
//         double dd2 = (float)l / 256F;
//         double dd3 = ((float)l + 15.999F) / 256F;
        tessellator.startDrawingQuads();
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        int i = mod_noBiomesX.SurrGroundHeight-i1;
        tessellator.setTranslation(-d, -d1, -d2);
        for (int i3 = 0; i3 < mod_noBiomesX.IndevWidthZ; i3 += i1){
            tessellator.addVertexWithUV(0, mod_noBiomesX.SurrGroundHeight, i3, dd1, dd2);
            tessellator.addVertexWithUV(0, mod_noBiomesX.SurrGroundHeight, i3 + i1, dd1, dd3);
            tessellator.addVertexWithUV(0, i, i3 + i1, dd, dd3);
            tessellator.addVertexWithUV(0, i, i3, dd, dd2);
        }
        for (int i3 = 0; i3 < mod_noBiomesX.IndevWidthX; i3 += i1){
            tessellator.addVertexWithUV(i3, mod_noBiomesX.SurrGroundHeight, mod_noBiomesX.IndevWidthZ, dd1, dd2);
            tessellator.addVertexWithUV(i3 + i1, mod_noBiomesX.SurrGroundHeight, mod_noBiomesX.IndevWidthZ, dd1, dd3);
            tessellator.addVertexWithUV(i3 + i1, i, mod_noBiomesX.IndevWidthZ, dd, dd3);
            tessellator.addVertexWithUV(i3, i, mod_noBiomesX.IndevWidthZ, dd, dd2);
        }
        dd2 = (double)i1;
        dd3 = 0D;
        for (int i3 = 0; i3 < mod_noBiomesX.IndevWidthZ; i3 += i1){
            tessellator.addVertexWithUV(mod_noBiomesX.IndevWidthX, i, i3, dd, dd2);
            tessellator.addVertexWithUV(mod_noBiomesX.IndevWidthX, i, i3 + i1, dd, dd3);
            tessellator.addVertexWithUV(mod_noBiomesX.IndevWidthX, mod_noBiomesX.SurrGroundHeight, i3 + i1, dd1, dd3);
            tessellator.addVertexWithUV(mod_noBiomesX.IndevWidthX, mod_noBiomesX.SurrGroundHeight, i3, dd1, dd2);
        }
        for (int i3 = 0; i3 < mod_noBiomesX.IndevWidthX; i3 += i1){
            tessellator.addVertexWithUV(i3, i, 0, dd, dd2);
            tessellator.addVertexWithUV(i3 + i1, i, 0, dd, dd3);
            tessellator.addVertexWithUV(i3 + i1, mod_noBiomesX.SurrGroundHeight, 0, dd1, dd3);
            tessellator.addVertexWithUV(i3, mod_noBiomesX.SurrGroundHeight, 0, dd1, dd2);
        }
        tessellator.draw();
    }

    private static void renderBottomBounds(float f){
        Tessellator tessellator = Tessellator.instance;
        float f1 = 0;
        int i1 = 128;
        if (i1 > mod_noBiomesX.IndevWidthX){
            i1 = mod_noBiomesX.IndevWidthX;
        }
        if (i1 > mod_noBiomesX.IndevWidthZ){
            i1 = mod_noBiomesX.IndevWidthZ;
        }
//         Block block = Block.bedrock;
//         int j = block.getBlockTextureFromSideAndMetadata(1, 0);
//         if (globalRenderBlocks.overrideBlockTexture >= 0){
//             j = globalRenderBlocks.overrideBlockTexture;
//         }
//         int k = (j & 0xf) << 4;
//         int l = j & 0xf0;
//         double dd = (float)k / 256F;
//         double dd1 = ((float)k + 15.999F) / 256F;
//         double dd2 = (float)l / 256F;
//         double dd3 = ((float)l + 15.999F) / 256F;
        double dd = 0D;
        double dd1 = (double)i1;
        double dd2 = 0D;
        double dd3 = (double)i1;
        tessellator.startDrawingQuads();
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        tessellator.setTranslation(-d, -d1, -d2);
        for (int i3 = 0; i3 < mod_noBiomesX.IndevWidthX; i3 += i1){
            for (int i5 = 0; i5 < mod_noBiomesX.IndevWidthZ; i5 += i1){
                tessellator.addVertexWithUV(i3, f1, i5 + i1, dd1, dd3);
                tessellator.addVertexWithUV(i3 + i1, f1, i5 + i1, dd1, dd2);
                tessellator.addVertexWithUV(i3 + i1, f1, i5, dd, dd2);
                tessellator.addVertexWithUV(i3, f1, i5, dd, dd3);
            }
        }
        tessellator.draw();
    }

    public static void renderBounds(Minecraft m, float f)
    {
        if (mod_noBiomesX.SurrGroundType<=0 || mod_noBiomesX.SurrWaterType<=0){
            return;
        }
        mc = m;
        renderEngine = mc.renderEngine;
        worldObj = mc.theWorld;
        if (imageData == null){
            try{
                imageData = ((java.nio.ByteBuffer)ModLoader.getPrivateValue(net.minecraft.src.RenderEngine.class, renderEngine, 5));
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
        int width = mod_noBiomesX.textureWidth;
        mod_noBiomesX.terrfx.anaglyphEnabled = mc.gameSettings.anaglyph;
        mod_noBiomesX.terrfx.onTick();
        mod_noBiomesX.bedrockfx.anaglyphEnabled = mc.gameSettings.anaglyph;
        mod_noBiomesX.bedrockfx.onTick();
        mod_noBiomesX.waterfx.anaglyphEnabled = mc.gameSettings.anaglyph;
        mod_noBiomesX.waterfx.onTick();
        mod_noBiomesX.lavafx.anaglyphEnabled = mc.gameSettings.anaglyph;
        mod_noBiomesX.lavafx.onTick();
        int id = mod_noBiomesX.SurrGroundType;
        if (mod_noBiomesX.SurrGroundHeight<=mod_noBiomesX.SurrWaterHeight || mod_noBiomesX.SurrWaterType==Block.lavaStill.blockID && mod_noBiomesX.SurrGroundType==Block.grass.blockID){
            id = Block.dirt.blockID;
        }
        if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_CLASSIC){
            id = Block.bedrock.blockID;
        }
        GL11.glPushMatrix();
        mc.entityRenderer.enableLightmap(f);
        int l = mod_noBiomesX.getLightInBounds(mod_noBiomesX.SurrGroundHeight);
        int i1 = l % 0x10000;
        int j1 = l / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1 / 1.0F, (float)j1 / 1.0F);
        boolean anim = mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV;
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glRotatef(90F, 0F, 0F, 1F);
        if (id==Block.grass.blockID && !mod_noBiomesX.FallbackColors){
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/nbxlite/textures/grasstop.png"));
        }else{
            imageData.clear();
            imageData.put(mod_noBiomesX.terrfx.imageData);
            imageData.position(0).limit(mod_noBiomesX.terrfx.imageData.length);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_noBiomesX.emptyImage);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, width, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        renderGroundBounds(f);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_noBiomesX.emptyImage);
        if (mod_noBiomesX.SurrGroundHeight>=0 || mod_noBiomesX.SurrWaterHeight>=0){
            imageData.clear();
            imageData.put(mod_noBiomesX.bedrockfx.imageData);
            imageData.position(0).limit(mod_noBiomesX.bedrockfx.imageData.length);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, width, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
            renderSideBounds(f);
            renderBottomBounds(f);
        }
        l = mod_noBiomesX.getLightInBounds(mod_noBiomesX.SurrWaterHeight);
        i1 = l % 0x10000;
        j1 = l / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)i1 / 1.0F, (float)j1 / 1.0F);
        String name = Block.blocksList[mod_noBiomesX.SurrWaterType].getBlockName().replace("tile.", "").replace("Still", "").replace("Moving", "");
        if (anim){
            TextureFX texturefx = null;
            try{
                List list = ((ArrayList)ModLoader.getPrivateValue(net.minecraft.src.RenderEngine.class, renderEngine, 6));
                for (int i = 0; i < list.size(); i++){
                    if (((TextureFX)list.get(i)).iconIndex==((name.startsWith("lava") ? 14 : 12) * 16 + 13)){
                        texturefx = ((TextureFX)list.get(i));
                        break;
                    }
                }
                imageData.clear();
                imageData.put(texturefx.imageData);
                imageData.position(0).limit(texturefx.imageData.length);;
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, width, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
            }catch(Exception ex){
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_noBiomesX.emptyImage);
                TextureFX fx = name.startsWith("lava") ? mod_noBiomesX.lavafx : mod_noBiomesX.waterfx;
                imageData.clear();
                imageData.put(fx.imageData);
                imageData.position(0).limit(fx.imageData.length);
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, width, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
//                 GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/nbxlite/textures/"+name+".png"));
//                 System.out.println(ex);
            }
        }else{
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_noBiomesX.emptyImage);
            TextureFX fx = name.startsWith("lava") ? mod_noBiomesX.lavafx : mod_noBiomesX.waterfx;
            imageData.clear();
            imageData.put(fx.imageData);
            imageData.position(0).limit(fx.imageData.length);
            GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, width, width, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
//             GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/nbxlite/textures/"+name+".png"));
        }
        GL11.glEnable(GL11.GL_BLEND);
        renderLiquidBounds(f);
        GL11.glRotatef(-90F, 0F, 0F, 1F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_BLEND);
        mc.entityRenderer.disableLightmap(f);
        GL11.glPopMatrix();
    }

    private static void renderLiquidBounds(float f)
    {
        float f1 = (float)mod_noBiomesX.SurrWaterHeight;
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Tessellator tessellator = Tessellator.instance;
        int i1 = 128;
        if (i1 > mod_noBiomesX.IndevWidthX){
            i1 = mod_noBiomesX.IndevWidthX;
        }
        if (i1 > mod_noBiomesX.IndevWidthZ){
            i1 = mod_noBiomesX.IndevWidthZ;
        }
//         int j = Block.blocksList[mod_noBiomesX.SurrWaterType].getBlockTextureFromSideAndMetadata(1, 0);
//         if (globalRenderBlocks.overrideBlockTexture >= 0){
//             j = globalRenderBlocks.overrideBlockTexture;
//         }
//         int k = (j & 0xf) << 4;
//         int l = j & 0xf0;
//         double dd = (float)k / 256F;
//         double dd1 = ((float)k + 15.999F) / 256F;
//         double dd2 = (float)l / 256F;
//         double dd3 = ((float)l + 15.999F) / 256F;
        double dd = 0D;
        double dd1 = (double)i1;
        double dd2 = 0D;
        double dd3 = (double)i1;
        int i = Block.blocksList[mod_noBiomesX.SurrWaterType].colorMultiplier(worldObj, 0, mod_noBiomesX.SurrWaterHeight, 0);
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
        tessellator.setColorOpaque_F(ff1, ff2, ff3);
        int i2 = 2048 / i1;
        tessellator.startDrawingQuads();
        float f2 = (float)Block.waterStill.minX;
        float f3 = (float)Block.waterStill.minZ;
        double d = mc.renderViewEntity.lastTickPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.lastTickPosX) * (double)f;
        double d1 = mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)f;
        double d2 = mc.renderViewEntity.lastTickPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.lastTickPosZ) * (double)f;
        tessellator.setTranslation(-d, -d1, -d2);
        for (int i3 = -i1 * i2; i3 < mod_noBiomesX.IndevWidthX + i1 * i2; i3 += i1){
            for (int i5 = -i1 * i2; i5 < mod_noBiomesX.IndevWidthZ + i1 * i2; i5 += i1){
                float f4 = f1 + (float)Block.waterStill.maxY - 1.1121F;
                if ((f1 < 0.0F) || (i3 < 0) || (i5 < 0) || (i3 >= mod_noBiomesX.IndevWidthX) || (i5 >= mod_noBiomesX.IndevWidthZ)){
                    tessellator.addVertexWithUV(i3 + f2, f4, i5 + i1 + f3, dd1, dd3);
                    tessellator.addVertexWithUV(i3 + i1 + f2, f4, i5 + i1 + f3, dd1, dd2);
                    tessellator.addVertexWithUV(i3 + i1 + f2, f4, i5 + f3, dd, dd2);
                    tessellator.addVertexWithUV(i3 + f2, f4, i5 + f3, dd, dd3);
                    if (mod_noBiomesX.SurrWaterType==Block.waterStill.blockID || mod_noBiomesX.SurrWaterType==Block.waterMoving.blockID){
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
//         GL11.glDisable(GL11.GL_BLEND);
    }
}