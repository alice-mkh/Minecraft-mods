package net.minecraft.src;

public class TextureFlamesFX extends TextureFX
{
    protected float field_76869_g[];
    protected float field_76870_h[];

    public TextureFlamesFX(int par1)
    {
        super("fire_layer_"+par1);
    }

    @Override
    protected void updateSize(){
        super.updateSize();
        field_76869_g = new float[size * size * 5 / 4];
        field_76870_h = new float[size * size * 5 / 4];
    }

    @Override
    public void onTick()
    {
        int size2 = size * 5 / 4;
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size2; j++)
            {
                int k = size * 9 / 8;
                float f = field_76869_g[i + ((j + 1) % size2) * size] * (float)k;

                for (int i1 = i - 1; i1 <= i + 1; i1++)
                {
                    for (int j1 = j; j1 <= j + 1; j1++)
                    {
                        if (i1 >= 0 && j1 >= 0 && i1 < size && j1 < size2)
                        {
                            f += field_76869_g[i1 + j1 * size];
                        }

                        k++;
                    }
                }

                field_76870_h[i + j * size] = f / ((float)k * (1.0F + 0.06F / size * 16.0F));

                if (j >= (size2 - 1))
                {
                    field_76870_h[i + j * size] = (float)(Math.random() * Math.random() * Math.random() * 4D + Math.random() * 0.10000000149011612D + 0.20000000298023224D);
                }
            }
        }

        float af[] = field_76870_h;
        field_76870_h = field_76869_g;
        field_76869_g = af;

        for (int l = 0; l < size * size; l++)
        {
            float f1 = field_76869_g[l] * 1.8F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F;
            }

            if (f1 < 0.0F)
            {
                f1 = 0.0F;
            }

            float f2 = f1;
            int k1 = (int)(f2 * 155F + 100F);
            int i2 = (int)(f2 * f2 * 255F);
            int k2 = (int)(f2 * f2 * f2 * f2 * f2 * f2 * f2 * f2 * f2 * f2 * 255F);
            char c = '\377';

            if (f2 < 0.5F)
            {
                c = '\0';
            }

            f2 = (f2 - 0.5F) * 2.0F;

            if (anaglyphEnabled)
            {
                int l2 = (k1 * 30 + i2 * 59 + k2 * 11) / 100;
                int i3 = (k1 * 30 + i2 * 70) / 100;
                int j3 = (k1 * 30 + k2 * 70) / 100;
                k1 = l2;
                i2 = i3;
                k2 = j3;
            }

            imageData[l] = c << 24 | k1 << 16 | i2 << 8 | k2;
        }
    }
}
