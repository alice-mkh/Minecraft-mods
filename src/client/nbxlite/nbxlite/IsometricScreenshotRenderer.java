package net.minecraft.src.nbxlite;

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
import net.minecraft.src.*;

public class IsometricScreenshotRenderer{
    public static int SCALE = 16;

    private IProgressUpdate progressupdate;
    private Minecraft mc;
    private WorldClient worldObj;
    private RenderGlobal renderGlobal;
    private DecimalFormat decimalFormat;
    private int width;
    private int length;
    private int height;
    private float maxCloudHeight;
    private ByteBuffer byteBuffer;
    private FloatBuffer floatBuffer;

    public IsometricScreenshotRenderer(Minecraft m){
        progressupdate = m.loadingScreen;
        mc = m;
        worldObj = mc.theWorld;
        renderGlobal = mc.renderGlobal;
        decimalFormat = new DecimalFormat("0000");
        if (ODNBXlite.isFinite()){
            width = ODNBXlite.IndevWidthX;
            length = ODNBXlite.IndevWidthZ;
            height = ODNBXlite.IndevHeight;
            maxCloudHeight = height;
        }else{
            width = (64 << (3 - mc.gameSettings.renderDistance)) + 16;
            length = width;
            height = 256;
            maxCloudHeight = 108.0F;
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
        renderGlobal.isTakingIsometricScreenshot = true;
        boolean finite = ODNBXlite.isFinite();
        try{
            int i1 = (width * SCALE) + (length * SCALE);
            int i3 = (height * SCALE) + i1 / 2;
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
                    mc.entityRenderer.updateFogColorPublic(0.0F);
                    GL11.glClear(16640);
                    GL11.glEnable(GL11.GL_CULL_FACE);
                    GL11.glMatrixMode(GL11.GL_PROJECTION);
                    GL11.glLoadIdentity();
                    GL11.glOrtho(0.0D, dWidth, 0.0D, dHeight, 10.0D, 10000.0D);
                    GL11.glMatrixMode(GL11.GL_MODELVIEW);
                    GL11.glLoadIdentity();
                    GL11.glTranslatef(-i10, -i12, -5000.0F);
                    GL11.glScalef(SCALE, -SCALE, -SCALE);
                    floatBuffer.clear();
                    floatBuffer.put(1.0F).put(-0.5F).put(0.0F).put(0.0F);
                    floatBuffer.put(0.0F).put(1.0F).put(-1.0F).put(0.0F);
                    floatBuffer.put(1.0F).put(0.5F).put(0.0F).put(0.0F);
                    floatBuffer.put(0.0F).put(0.0F).put(0.0F).put(1.0F);
                    floatBuffer.flip();
                    GL11.glMultMatrix(floatBuffer);
                    GL11.glRotatef(0.0F, 0.0F, 1.0F, 0.0F);
                    if (finite){
                        GL11.glTranslated(-width / 2.0D, -height / 2.0D, -length / 2.0D);
                    }else{
                        GL11.glTranslated(-mc.renderViewEntity.lastTickPosX, -height / 2.0D, -mc.renderViewEntity.lastTickPosZ);
                    }
                    Frustrum frustrum = new Frustrum();
                    GL11.glTranslated(mc.renderViewEntity.lastTickPosX, mc.renderViewEntity.lastTickPosY, mc.renderViewEntity.lastTickPosZ);
                    renderGlobal.updateRenderers(mc.renderViewEntity, false);
                    mc.entityRenderer.setupFogPublic();
                    GL11.glEnable(GL11.GL_FOG);
                    GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
                    float f3 = height * 8.0F;
                    GL11.glFogf(GL11.GL_FOG_START, 5000.0F - f3);
                    GL11.glFogf(GL11.GL_FOG_END, 5000.0F + f3 * 8.0F);
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
                    renderGlobal.renderSky(0.0F);
                    if (finite){
                        GL11.glTranslated(width / 2.0D, height / 2.0D, length / 2.0D);
                        GL11.glTranslated(-mc.renderViewEntity.lastTickPosX, -mc.renderViewEntity.lastTickPosY, -mc.renderViewEntity.lastTickPosZ);
                    }else{
                        GL11.glTranslated(0, -mc.renderViewEntity.lastTickPosY + height / 2.0D, 0);
                    }
                    if (worldObj.provider.getCloudHeight() < maxCloudHeight && mc.gameSettings.clouds){
                        GL11.glPushMatrix();
                        double temp = mc.renderViewEntity.lastTickPosY;
                        mc.renderViewEntity.lastTickPosY = worldObj.provider.getCloudHeight() + 20;
                        renderGlobal.renderClouds(0.0F);
                        mc.renderViewEntity.lastTickPosY = temp;
                        GL11.glPopMatrix();
                    }
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glColorMask(false, false, false, false);
                    if (finite){
                        GL11.glTranslated(mc.renderViewEntity.lastTickPosX, mc.renderViewEntity.lastTickPosY, mc.renderViewEntity.lastTickPosZ);
                    }else{
                        GL11.glTranslated(0, mc.renderViewEntity.lastTickPosY - height / 2.0D, 0);
                    }
                    if (mc.gameSettings.ambientOcclusion != 0){
                        GL11.glShadeModel(GL11.GL_SMOOTH);
                    }
                    mc.func_110434_K().func_110577_a(TextureMap.field_110575_b);
                    int i11 = renderGlobal.sortAndRender(mc.renderViewEntity, 1, 0.5F);
                    GL11.glShadeModel(GL11.GL_FLAT);
                    GL11.glColorMask(true, true, true, true);
                    if (finite){
                        GL11.glTranslated(-width / 2.0D, -height / 2.0D, -length / 2.0D);
                    }
                    if (i11 > 0){
                        renderGlobal.renderAllRenderLists(1, 0.0F);
                    }
                    if (finite && ODNBXlite.SurrGroundHeight >= 0 && worldObj.provider.dimensionId == 0){
                        net.minecraft.src.nbxlite.RenderBounds.renderBounds(mc, 0.0F);
                    }
                    GL11.glDepthMask(true);
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glDisable(GL11.GL_FOG);
                    byteBuffer.clear();
                    GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
                    GL11.glReadPixels(0, 0, dWidth, dHeight, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, byteBuffer);
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
        }catch (OutOfMemoryError e){
            String str = "Out of memory. Reduce render distance and try again.";
            if (finite){
                str = "Out of memory. The world is too large.";
            }
            mc.ingameGUI.getChatGUI().printChatMessage(str);
        }catch (Throwable t){
            t.printStackTrace();
        }
        renderGlobal.isTakingIsometricScreenshot = false;
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