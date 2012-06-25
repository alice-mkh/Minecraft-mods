package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;
import net.minecraft.src.mod_noBiomesX;

public class TextureSpriteFX extends TextureFX
{
    /**
     * Holds the game instance to retrieve information like world provider and time.
     */
    public static int w = 16;
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
        index2 = i;
        sprite = name;
        sprite2 = origname;
        tileImage = origname == "/terrain.png" ? 0 : 1;
        swidth = sw;
        sheight = sh;
        enabled = true;
        renderEngine = ModLoader.getMinecraftInstance().renderEngine;
        changeIndex(j, true, false);
    }

    public void changeIndex(int index, boolean e, boolean b){
        enabled = e;
        spriteData = new int[w * w];
        imageData = new byte[w * w * 4];
        currentIndex = index;
        try
        {
            String spr = enabled ? sprite : sprite2;
            BufferedImage bufferedimage = ModLoader.loadImage(ModLoader.getMinecraftInstance().renderEngine, spr);
            if (b){
                bufferedimage = ImageIO.read((net.minecraft.client.Minecraft.class).getResource(spr));
            }
            int i = (enabled ? currentIndex : index2 % swidth) * w;
            int j = (enabled ? currentIndex : index2 / sheight) * w;
            bufferedimage.getRGB(i, j, w, w, spriteData, 0, w);
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }
    }

    public void refresh(boolean def){
        changeIndex(currentIndex, enabled, def);
    }

    public void onTick()
    {
        for (int i = 0; i < spriteData.length; i++)
        {
            int j = spriteData[i] >> 24 & 0xff;
            int k = spriteData[i] >> 16 & 0xff;
            int l = spriteData[i] >> 8 & 0xff;
            int i1 = spriteData[i] >> 0 & 0xff;
            if (anaglyphEnabled)
            {
                int j1 = (k * 30 + l * 59 + i1 * 11) / 100;
                int k1 = (k * 30 + l * 70) / 100;
                int l1 = (k * 30 + i1 * 70) / 100;
                k = j1;
                l = k1;
                i1 = l1;
            }

            imageData[i * 4 + 0] = (byte)k;
            imageData[i * 4 + 1] = (byte)l;
            imageData[i * 4 + 2] = (byte)i1;
            imageData[i * 4 + 3] = (byte)j;
        }
    }
}
