package net.minecraft.src.nbxlite.blocks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Texture;
import net.minecraft.src.TextureStitched;
import net.minecraft.src.ODNBXlite;

public class TextureGearFX extends TextureStitched
{
    private int tickCounter = 0;
    private int[] gear = new int[1024];
    private int[] gearmiddle = new int[1024];
    private int h;
    private ByteBuffer textureData;

    public TextureGearFX(int par2)
    {
        super("olddays_gear_" + par2);
        this.h = ((par2 << 1) - 1);
        tickCounter = 2;
        try{
            ImageIO.read(getClass().getResource("/olddays/gear.png")).getRGB(0, 0, 32, 32, gear, 0, 32);
            ImageIO.read(getClass().getResource("/olddays/gearmiddle.png")).getRGB(0, 0, 16, 16, gearmiddle, 0, 16);
            return;
        }catch (IOException localIOException){
            localIOException.printStackTrace();
        }
    }

    private byte[] getImageData(){
        byte[] imageData = new byte[1024];
        tickCounter = (tickCounter + this.h & 0x3F);
        float f1 = MathHelper.cos(tickCounter / 64.0F * 3.141593F * 2.0F);
        float f2 = MathHelper.sin(tickCounter / 64.0F * 3.141593F * 2.0F);
        for (int i = 0; i < 16; i++){
            for (int j = 0; j < 16; j++)
            {
                float f3 = (i / 15.0F - 0.5F) * 31.0F;
                float f4 = (j / 15.0F - 0.5F) * 31.0F;
                float f5 = f2 * f3 - f1 * f4;
                float f6 = f2 * f4 + f1 * f3;
                int m = (int)(f5 + 16.0F);
                int k = (int)(f6 + 16.0F);
                int n = 0;
                if ((m >= 0) && (k >= 0) && (m < 32) && (k < 32)){
                    n = gear[(m + (k << 5))];
                    if (gearmiddle[(i + (j << 4))] >>> 24 > 128){
                        n = gearmiddle[(i + (j << 4))];
                    }
                }
                int k1 = n >> 16 & 0xFF;
                int m1 = n >> 8 & 0xFF;
                int i1 = n & 0xFF;
                int n1 = n >>> 24 > 128 ? 255 : 0;
                int i2 = i + (j << 4);
                imageData[(i2 << 2)] = (byte)k1;
                imageData[((i2 << 2) + 1)] = (byte)m1;
                imageData[((i2 << 2) + 2)] = (byte)i1;
                imageData[((i2 << 2) + 3)] = (byte)n1;
            }
        }
        return imageData;
    }

    @Override
    public void updateAnimation(){
        textureData = ((Texture)textureList.get(0)).getTextureData();
        textureData.clear();
        textureData.put(getImageData());
        textureData.position(0).limit(1024);
//         textureData.flip();
        textureSheet.copyFrom(originX, originY, (Texture)textureList.get(0), rotated);
    }
}