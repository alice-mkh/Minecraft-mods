package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;

public class TextureSpriteFX extends TextureFX
{
    /**
     * Holds the game instance to retrieve information like world provider and time.
     */
    public static int w = 16;
    public int ww;
    private int spriteData[];
    private double field_4222_j;
    private double field_4221_k;
    public int currentIndex;
    private RenderEngine renderEngine;
    public String sprite;
    public String sprite2;
    public int swidth;
    public int sheight;
    public boolean enabled;
    public int index2;

    public TextureSpriteFX(String origname, String name, int sw, int sh, int i, int j)
    {
        super(i);
        ww = 16;
        index2 = i;
        sprite = name;
        sprite2 = origname;
        tileImage = origname == "/terrain.png" ? 0 : 1;
        swidth = sw;
        sheight = sh;
        enabled = true;
        renderEngine = mod_OldDays.getMinecraftInstance().renderEngine;
        changeIndex(j, true, false);
    }

    public void changeIndex(int index, boolean e, boolean b){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture(sprite));
        ww = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / swidth;
        if (ww <= 0){
            ww = 16;
        }
        enabled = e;
        int www = enabled ? ww : w;
        spriteData = new int[www * www];
        imageData = new byte[w * w * 4];
        currentIndex = index;
        try
        {
            String spr = enabled ? sprite : sprite2;
            TexturePackList packList = mod_OldDays.getMinecraftInstance().texturePackList;
            TexturePackBase texpack = ((TexturePackBase)mod_OldDays.getField(net.minecraft.src.TexturePackList.class, packList, 6));
            BufferedImage bufferedimage = ImageIO.read(texpack.getResourceAsStream(spr));
            if (b){
                bufferedimage = ImageIO.read((net.minecraft.client.Minecraft.class).getResource(spr));
            }
            int i = ((enabled ? currentIndex : index2) % swidth) * www;
            int j = ((enabled ? currentIndex : index2) / sheight) * www;
            bufferedimage.getRGB(i, j, www, www, spriteData, 0, www);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void refresh(boolean def){
        changeIndex(currentIndex, enabled, def);
    }

    private int indexGlobal(int x, int y){
        return x + y * w;
    }

    public void onTick()
    {
        int www = enabled ? ww : w;
        int n = w / www;
        if (imageData.length <= (((www - 1) * n) + n - 1) * (w + 1)){
            refresh(false);
        }
        for (int x = 0; x < www; x++){
            for (int y = 0; y < www; y++){
                int index1 = x + y * www;
                int j = spriteData[index1] >> 24 & 0xff;
                int k = spriteData[index1] >> 16 & 0xff;
                int l = spriteData[index1] >> 8 & 0xff;
                int i1 = spriteData[index1] >> 0 & 0xff;
                if (anaglyphEnabled){
                    int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                    int k1 = (k * 30 + l * 70) / 100;
                    int l1 = (k * 30 + i1 * 70) / 100;
                    k = j1;
                    l = k1;
                    i1 = l1;
                }
                for (int xx = 0; xx < n; xx++){
                    for (int yy = 0; yy < n; yy++){
                        int index2 = ((x * n) + xx) + ((y * n) + yy) * w;
                        imageData[index2 * 4 + 0] = (byte)k;
                        imageData[index2 * 4 + 1] = (byte)l;
                        imageData[index2 * 4 + 2] = (byte)i1;
                        imageData[index2 * 4 + 3] = (byte)j;
                    }
                }
            }
        }
    }

    public void bindImage(RenderEngine par1RenderEngine)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture(sprite2));
    }
}
