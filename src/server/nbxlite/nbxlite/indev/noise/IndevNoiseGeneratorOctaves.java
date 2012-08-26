package net.minecraft.src.nbxlite.indev.noise;

import java.util.Random;

public final class IndevNoiseGeneratorOctaves extends IndevNoiseGenerator
{
    private IndevNoiseGeneratorPerlin a[];
    private int b;

    public IndevNoiseGeneratorOctaves(Random random, int i)
    {
        b = i;
        a = new IndevNoiseGeneratorPerlin[i];
        for(int j = 0; j < i; j++)
        {
            a[j] = new IndevNoiseGeneratorPerlin(random);
        }

    }

    public final double a(double d, double d1)
    {
        double d2 = 0.0D;
        double d3 = 1.0D;
        for(int i = 0; i < b; i++)
        {
            d2 += a[i].a(d / d3, d1 / d3) * d3;
            d3 *= 2D;
        }

        return d2;
    }
}
