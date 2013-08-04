package net.minecraft.src;

public class TextureWaterFX extends TextureFX
{
    protected float red[];
    protected float green[];
    protected float blue[];
    protected float alpha[];
    private int tickCounter;

    public TextureWaterFX()
    {
        super("water_still");
        red = new float[256];
        green = new float[256];
        blue = new float[256];
        alpha = new float[256];
        tickCounter = 0;
    }

    public void onTick()
    {
        tickCounter++;

        for (int i = 0; i < 16; i++)
        {
            for (int k = 0; k < 16; k++)
            {
                float f = 0.0F;

                for (int j1 = i - 1; j1 <= i + 1; j1++)
                {
                    int k1 = j1 & 0xf;
                    int i2 = k & 0xf;
                    f += red[k1 + i2 * 16];
                }

                green[i + k * 16] = f / 3.3F + blue[i + k * 16] * 0.8F;
            }
        }

        for (int j = 0; j < 16; j++)
        {
            for (int l = 0; l < 16; l++)
            {
                blue[j + l * 16] += alpha[j + l * 16] * 0.05F;

                if (blue[j + l * 16] < 0.0F)
                {
                    blue[j + l * 16] = 0.0F;
                }

                alpha[j + l * 16] -= 0.1F;

                if (Math.random() < 0.050000000000000003D)
                {
                    alpha[j + l * 16] = 0.5F;
                }
            }
        }

        float af[] = green;
        green = red;
        red = af;

        for (int i1 = 0; i1 < 256; i1++)
        {
            float f1 = red[i1];

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            if (f1 < 0.0F)
            {
                f1 = 0.0F;
            }

            float f2 = f1 * f1;
            int l1 = (int)(32F + f2 * 32F);
            int j2 = (int)(50F + f2 * 64F);
            int k2 = 255;
            int l2 = (int)(146F + f2 * 50F);

            if (anaglyphEnabled)
            {
                int i3 = (l1 * 30 + j2 * 59 + k2 * 11) / 100;
                int j3 = (l1 * 30 + j2 * 70) / 100;
                int k3 = (l1 * 30 + k2 * 70) / 100;
                l1 = i3;
                j2 = j3;
                k2 = k3;
            }

            imageData[i1] = l2 << 24 | l1 << 16 | j2 << 8 | k2;
        }
    }
}
