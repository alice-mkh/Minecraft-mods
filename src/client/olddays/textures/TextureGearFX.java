package net.minecraft.src;

import java.io.IOException;

public class TextureGearFX extends TextureFX
{
    private int tickCounter;
    private int[] gear;
    private int[] gearmiddle;
    private int unknown;

    public TextureGearFX(int par2)
    {
        super("olddays_gear_" + par2);
        unknown = (par2 << 1) - 1;
        tickCounter = 2;
    }

    @Override
    protected void updateSize(){
        super.updateSize();
        gear = getImageData("olddays/gear.png", size * 2);
        gearmiddle = getImageData("olddays/gearmiddle.png", size);
    }

    @Override
    public void onTick()
    {
        int multiplier = size / 16;
        tickCounter = (tickCounter + unknown & 63);
        float f1 = MathHelper.cos(tickCounter / 64.0F * (float)Math.PI * 2.0F);
        float f2 = MathHelper.sin(tickCounter / 64.0F * (float)Math.PI * 2.0F);
        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++)
            {
                float f3 = (i / (float)(size - 1) - 0.5F) * (float)(size * 2 - 1);
                float f4 = (j / (float)(size - 1) - 0.5F) * (float)(size * 2 - 1);
                float f5 = f2 * f3 - f1 * f4;
                float f6 = f2 * f4 + f1 * f3;
                int m = (int)(f5 + size);
                int k = (int)(f6 + size);
                int n = 0;
                if ((m >= 0) && (k >= 0) && (m < size * 2) && (k < size * 2)){
                    n = gear[m + k * size * 2];
                    if ((gearmiddle[i + j * size] >>> 24) > 128){
                        n = gearmiddle[i + j * size];
                    }
                }
                imageData[i + j * size] = n;
            }
        }
    }
}