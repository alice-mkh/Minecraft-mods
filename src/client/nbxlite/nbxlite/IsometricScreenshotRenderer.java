package net.minecraft.src;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.text.DecimalFormat;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class IsometricScreenshotRenderer{
    public static boolean oldFog = true;

    private IProgressUpdate progressupdate;
    private Minecraft mc;
    private WorldClient worldObj;
    private RenderGlobal renderGlobal;
    private DecimalFormat decimalFormat;
    private int width;
    private int length;
    private int height;
    private ByteBuffer byteBuffer;
    private FloatBuffer floatBuffer;

    public IsometricScreenshotRenderer(IProgressUpdate p, Minecraft m){
        progressupdate = p;
        mc = m;
        worldObj = mc.theWorld;
        renderGlobal = mc.renderGlobal;
        decimalFormat = new DecimalFormat("0000");
        if (ODNBXlite.isFinite()){
            width = ODNBXlite.IndevWidthX;
            length = ODNBXlite.IndevWidthZ;
            height = ODNBXlite.IndevHeight;
        }else{
            width = 64;
            length = 64;
            height = 256;
        }
        floatBuffer = BufferUtils.createFloatBuffer(16);
    }

    private File getOutputFile(){
        File file = null;
        int scrNumber = 0;
        do{
            file = new File(mc.mcDataDir, "mc_map_" + decimalFormat.format(scrNumber++) + ".png");
        }while (file.exists());
        return file.getAbsoluteFile();
    }

    public void doRender(){
        progressupdate.displayProgressMessage("Grabbing large screenshot");
        File outputFile = getOutputFile();
        progressupdate.resetProgresAndWorkingMessage("Rendering");
        progressupdate.setLoadingProgress(0);
        try{
            int i1 = (width << 4) + (length << 4);
            int i3 = (height << 4) + i1 / 2;
            BufferedImage image = new BufferedImage(i1, i3, 1);
            Graphics graphics = image.getGraphics();
            int dWidth = mc.displayWidth;
            int dHeight = mc.displayHeight;
            int total = (i1 / dWidth + 1) * (i3 / dHeight + 1);
            int progress = 0;

            for (int i8 = 0; i8 < i1; i8 += dWidth){
                for (int i9 = 0; i9 < i3; i9 += dHeight){
                    progressupdate.setLoadingProgress(++progress * 100 / total);

                    float f1 = 0.0F;
                    int i12 = i9 - i3 / 2;
                    int i10 = i8 - i1 / 2;
                    if (byteBuffer == null){
                        byteBuffer = BufferUtils.createByteBuffer(dWidth * dHeight << 2);
                    }
                    GL11.glViewport(0, 0, dWidth, dHeight);
//                     mc.entityRenderer.updateFogColorPublic(0.0F);
                    GL11.glClear(16640);
                    GL11.glEnable(2884);
//                     int farPlaneDistance = 256 >> mc.gameSettings.renderDistance;
//                     if (oldFog){
//                         farPlaneDistance = (512 >> (mc.gameSettings.renderDistance << 1));
//                     }
                    GL11.glMatrixMode(5889);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0.0D, dWidth, 0.0D, dHeight, 10.0D, 10000.0D);
                    GL11.glMatrixMode(5888);
                    GL11.glLoadIdentity();
                    GL11.glTranslatef(-i10, -i12, -5000.0F);
                    GL11.glScalef(16.0F, -16.0F, -16.0F);
                    floatBuffer.clear();
                    floatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
                    floatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
                    floatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
                    floatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
                    floatBuffer.flip();
                    GL11.glMultMatrix(floatBuffer);
                    GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(-width / 2.0F, -height / 2.0F, -length / 2.0F);
                    Frustrum frustrum = new Frustrum();
//                     renderGlobal.clipRenderersByFrustum(frustrum, 0.0F);
                    GL11.glTranslated(mc.renderViewEntity.lastTickPosX, mc.renderViewEntity.lastTickPosY, mc.renderViewEntity.lastTickPosZ);
                    renderGlobal.updateRenderers(mc.renderViewEntity, false);
                    mc.entityRenderer.setupFogPublic();
                    GL11.glEnable(2912);
                    GL11.glFogi(2917, 9729);
                    float f3 = height * 8.0F;
                    GL11.glFogf(2915, 5000.0F - f3);
                    GL11.glFogf(2916, 5000.0F + f3 * 8.0F);
                    RenderHelper.enableStandardItemLighting();
                    renderGlobal.renderEntities(mc.renderViewEntity.getPosition(0.0F), frustrum, 0.0F);
                    mc.entityRenderer.renderRainSnowPublic();
                    RenderHelper.disableStandardItemLighting();
                    mc.func_110434_K().func_110577_a(TextureMap.field_110575_b);
                    if (mc.gameSettings.ambientOcclusion != 0){
                        GL11.glShadeModel(GL11.GL_SMOOTH);
                    }
                    renderGlobal.sortAndRender(mc.renderViewEntity, 0, 0.0F);
                    GL11.glShadeModel(GL11.GL_FLAT);
//                     renderGlobal.f();
                    GL11.glTranslatef(width / 2.0F, height / 2.0F, length / 2.0F);
                    GL11.glTranslated(-mc.renderViewEntity.lastTickPosX, -mc.renderViewEntity.lastTickPosY, -mc.renderViewEntity.lastTickPosZ);
                    if (worldObj.provider.getCloudHeight() < height){
                        renderGlobal.renderClouds(0.0F);
                    }
                    GL11.glEnable(3042);
                    GL11.glBlendFunc(770, 771);
                    GL11.glColorMask(false, false, false, false);
                    GL11.glTranslated(mc.renderViewEntity.lastTickPosX, mc.renderViewEntity.lastTickPosY, mc.renderViewEntity.lastTickPosZ);
                    if (mc.gameSettings.ambientOcclusion != 0){
                        GL11.glShadeModel(GL11.GL_SMOOTH);
                    }
                    int i11 = renderGlobal.sortAndRender(mc.renderViewEntity, 1, 0.0F);
                    GL11.glShadeModel(GL11.GL_FLAT);
                    GL11.glTranslated(-mc.renderViewEntity.lastTickPosX, -mc.renderViewEntity.lastTickPosY, -mc.renderViewEntity.lastTickPosZ);
                    GL11.glColorMask(true, true, true, true);
                    if (i11 > 0){
                        renderGlobal.renderSky(0.0F);
                    }
                    GL11.glTranslatef(-width / 2.0F, -height / 2.0F, -length / 2.0F);
                    GL11.glTranslated(mc.renderViewEntity.lastTickPosX, mc.renderViewEntity.lastTickPosY, mc.renderViewEntity.lastTickPosZ);
                    if (ODNBXlite.SurrGroundHeight >= 0 && worldObj.provider.dimensionId == 0){
                        net.minecraft.src.nbxlite.RenderBounds.renderBounds(mc, 0.0F);
                    }
                    GL11.glDepthMask(true);
                    GL11.glDisable(3042);
                    GL11.glDisable(2912);
                    byteBuffer.clear();
                    GL11.glPixelStorei(3333, 1);
                    GL11.glReadPixels(0, 0, dWidth, dHeight, 6407, 5121, byteBuffer);
                    BufferedImage image2 = getImageFromByteBuffer(dWidth, dHeight);
                    graphics.drawImage(image2, i8, i9, null);

                }
            }
            graphics.dispose();
            progressupdate.resetProgresAndWorkingMessage("Saving as " + outputFile.toString());
            progressupdate.setLoadingProgress(100);
            FileOutputStream stream = new FileOutputStream(outputFile);
            ImageIO.write(image, "png", stream);
            stream.close();
            return;
        }catch (Throwable t){
            t.printStackTrace();
        }
    }

    private BufferedImage getImageFromByteBuffer(int width, int height){
        byteBuffer.position(0).limit(width * height << 2);
        BufferedImage image = new BufferedImage(width, height, 1);
        int[] arrayOfInt = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        for (int i = 0; i < width * height; i++){
            int r = byteBuffer.get(i * 3) & 0xFF;
            int g = byteBuffer.get(i * 3 + 1) & 0xFF;
            int b = byteBuffer.get(i * 3 + 2) & 0xFF;
            arrayOfInt[i] = (r << 16 | g << 8 | b);
        }
        return image;
    }
}