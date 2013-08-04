package net.minecraft.src;

public class TextureLavaFlowFX extends TextureFX
{
    protected float field_76871_g[];
    protected float field_76874_h[];
    protected float field_76875_i[];
    protected float field_76872_j[];
    int field_76873_k;

    public TextureLavaFlowFX()
    {
        super("lava_flow");
        field_76873_k = 0;
        tile = 2;
    }

    @Override
    protected void updateSize(){
        super.updateSize();
        field_76871_g = new float[size * size];
        field_76874_h = new float[size * size];
        field_76875_i = new float[size * size];
        field_76872_j = new float[size * size];
    }

    @Override
    public void onTick()
    {
        field_76873_k++;

        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                float f = 0.0F;
                int l = (int)(MathHelper.sin(((float)j * (float)Math.PI * 2.0F) / (float)size) * 1.2F);
                int i1 = (int)(MathHelper.sin(((float)i * (float)Math.PI * 2.0F) / (float)size) * 1.2F);

                for (int k1 = i - 1; k1 <= i + 1; k1++)
                {
                    for (int i2 = j - 1; i2 <= j + 1; i2++)
                    {
                        int k2 = k1 + l & (size - 1);
                        int i3 = i2 + i1 & (size - 1);
                        f += field_76871_g[k2 + i3 * size];
                    }
                }

                field_76874_h[i + j * size] = f / 10F + ((field_76875_i[(i + 0 & (size - 1)) + (j + 0 & (size - 1)) * size] + field_76875_i[(i + 1 & (size - 1)) + (j + 0 & (size - 1)) * size] + field_76875_i[(i + 1 & (size - 1)) + (j + 1 & (size - 1)) * size] + field_76875_i[(i + 0 & (size - 1)) + (j + 1 & (size - 1)) * size]) / 4F) * 0.8F;
                field_76875_i[i + j * size] += field_76872_j[i + j * size] * 0.01F;

                if (field_76875_i[i + j * size] < 0.0F)
                {
                    field_76875_i[i + j * size] = 0.0F;
                }

                field_76872_j[i + j * size] -= 0.06F;

                if (Math.random() < 0.0050000000000000001D)
                {
                    field_76872_j[i + j * size] = 1.5F;
                }
            }
        }

        float af[] = field_76874_h;
        field_76874_h = field_76871_g;
        field_76871_g = af;

        for (int k = 0; k < size * size; k++)
        {
            float f1 = field_76871_g[k - (field_76873_k / 3) * size & ((size * size) - 1)] * 2.0F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            if (f1 < 0.0F)
            {
                f1 = 0.0F;
            }

            float f2 = f1;
            int j1 = (int)(f2 * 100F + 155F);
            int l1 = (int)(f2 * f2 * 255F);
            int j2 = (int)(f2 * f2 * f2 * f2 * 128F);

            if (anaglyphEnabled)
            {
                int l2 = (j1 * 30 + l1 * 59 + j2 * 11) / 100;
                int j3 = (j1 * 30 + l1 * 70) / 100;
                int k3 = (j1 * 30 + j2 * 70) / 100;
                j1 = l2;
                l1 = j3;
                j2 = k3;
            }

            imageData[k] = -1 << 24 | j1 << 16 | l1 << 8 | j2;
        }
    }
}
