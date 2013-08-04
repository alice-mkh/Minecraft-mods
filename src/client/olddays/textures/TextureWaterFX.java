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
        tickCounter = 0;
    }

    @Override
    protected void updateSize(){
        super.updateSize();
        red = new float[size * size];
        green = new float[size * size];
        blue = new float[size * size];
        alpha = new float[size * size];
    }

    @Override
    public void onTick()
    {
        tickCounter++;

        for (int i = 0; i < size; i++)
        {
            for (int k = 0; k < size; k++)
            {
                float f = 0.0F;

                for (int j1 = i - 1; j1 <= i + 1; j1++)
                {
                    int k1 = j1 & (size - 1);
                    int i2 = k & (size - 1);
                    f += red[k1 + i2 * size];
                }

                green[i + k * size] = f / 3.3F + blue[i + k * size] * 0.8F;
            }
        }

        for (int j = 0; j < size; j++)
        {
            for (int l = 0; l < size; l++)
            {
                blue[j + l * size] += alpha[j + l * size] * 0.05F;

                if (blue[j + l * size] < 0.0F)
                {
                    blue[j + l * size] = 0.0F;
                }

                alpha[j + l * size] -= 0.1F;

                if (Math.random() < 0.050000000000000003D)
                {
                    alpha[j + l * size] = 0.5F;
                }
            }
        }

        float af[] = green;
        green = red;
        red = af;

        for (int i1 = 0; i1 < size * size; i1++)
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
