package net.minecraft.src.nbxlite.indev.noise;

import java.util.Random;
import net.minecraft.src.MathHelper;

public final class IndevNoiseGeneratorPerlin extends IndevNoiseGenerator
{
    private int a[];

    public IndevNoiseGeneratorPerlin()
    {
        this(new Random());
    }

    public IndevNoiseGeneratorPerlin(Random random)
    {
        a = new int[512];
        for(int i = 0; i < 256; i++)
        {
            a[i] = i;
        }

        for(int j = 0; j < 256; j++)
        {
            int k = random.nextInt(256 - j) + j;
            int l = a[j];
            a[j] = a[k];
            a[k] = l;
            a[j + 256] = a[j];
        }

    }

    private static double a(double d)
    {
        return d * d * d * (d * (d * 6D - 15D) + 10D);
    }

    private static double a(double d, double d1, double d2)
    {
        return d1 + d * (d2 - d1);
    }

    private static double a(int i, double d, double d1, double d2)
    {
        double d3 = (i &= 0xf) >= 8 ? d1 : d;
        double d4 = i >= 4 ? i != 12 && i != 14 ? d2 : d : d1;
        return ((i & 1) != 0 ? -d3 : d3) + ((i & 2) != 0 ? -d4 : d4);
    }

    public final double a(double d, double d1)
    {
        double d4 = 0.0D;
        double d3 = d1;
        double d2 = d;
//         d = this;
        int i = MathHelper.floor_double(d2) & 0xff;
        d1 = MathHelper.floor_double(d3) & 0xff;
        int j = MathHelper.floor_double(0.0D) & 0xff;
        d2 -= MathHelper.floor_double(d2);
        d3 -= MathHelper.floor_double(d3);
        d4 = 0.0D - (double)MathHelper.floor_double(0.0D);
        double d5 = a(d2);
        double d6 = a(d3);
        double d7 = a(d4);
        int k = a[i] + (int)d1;
        int l = a[k] + j;
        k = a[k + 1] + j;
        i = a[i + 1] + (int)d1;
        d1 = a[i] + j;
        i = a[i + 1] + j;
        return a(d7, a(d6, a(d5, a(a[l], d2, d3, d4), a(a[(int)d1], d2 - 1.0D, d3, d4)), a(d5, a(a[k], d2, d3 - 1.0D, d4), a(a[i], d2 - 1.0D, d3 - 1.0D, d4))), a(d6, a(d5, a(a[l + 1], d2, d3, d4 - 1.0D), a(a[(int)d1 + 1], d2 - 1.0D, d3, d4 - 1.0D)), a(d5, a(a[k + 1], d2, d3 - 1.0D, d4 - 1.0D), a(a[i + 1], d2 - 1.0D, d3 - 1.0D, d4 - 1.0D))));
    }
}
