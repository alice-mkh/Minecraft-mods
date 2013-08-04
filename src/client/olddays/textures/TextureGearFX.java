package net.minecraft.src;

import java.io.IOException;

public class TextureGearFX extends TextureFX
{
    private int tickCounter = 0;
    private int[] gear = new int[1024];
    private int[] gearmiddle = new int[1024];
    private int h;

    public TextureGearFX(int par2)
    {
        super("olddays_gear_" + par2);
        this.h = ((par2 << 1) - 1);
        tickCounter = 2;
        try{
            getImage("olddays/gear.png").getRGB(0, 0, 32, 32, gear, 0, 32);
            getImage("olddays/gearmiddle.png").getRGB(0, 0, 16, 16, gearmiddle, 0, 16);
        }catch (IOException localIOException){
            localIOException.printStackTrace();
        }
    }

    @Override
    public void onTick()
    {
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
                imageData[i + (j << 4)] = n;
            }
        }
    }
}