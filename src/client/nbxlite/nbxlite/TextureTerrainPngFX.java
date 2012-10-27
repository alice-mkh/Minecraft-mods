package net.minecraft.src.nbxlite;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class TextureTerrainPngFX extends TextureFX
{
    /**
     * Holds the game instance to retrieve information like world provider and time.
     */
    private int spriteData[];
    private double field_4222_j;
    private double field_4221_k;
    public int currentIndex;
    private RenderEngine renderEngine;

    public TextureTerrainPngFX()
    {
        super(0);
        renderEngine = mod_OldDays.getMinecraft().renderEngine;
        changeIndex(255, false);
    }

    public void changeIndex(int index, boolean b){
        spriteData = new int[ODNBXlite.textureWidth * ODNBXlite.textureWidth];
        imageData = new byte[ODNBXlite.textureWidth * ODNBXlite.textureWidth * 4];
        currentIndex = index;
        try
        {
            TexturePackList packList = mod_OldDays.getMinecraft().texturePackList;
            ITexturePack texpack = ((ITexturePack)mod_OldDays.getField(net.minecraft.src.TexturePackList.class, packList, 6));
            BufferedImage bufferedimage = ImageIO.read(texpack.getResourceAsStream("/terrain.png"));
            if (b){
                bufferedimage = ImageIO.read((net.minecraft.client.Minecraft.class).getResource("/terrain.png"));
            }
            int i = (currentIndex % 16) * ODNBXlite.textureWidth;
            int j = (currentIndex / 16) * ODNBXlite.textureWidth;
            bufferedimage.getRGB(i, j, ODNBXlite.textureWidth, ODNBXlite.textureWidth, spriteData, 0, ODNBXlite.textureWidth);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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
