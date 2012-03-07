package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;

public class InfdevNoiseGeneratorOctaves extends NoiseGenerator
{
    private InfdevNoiseGeneratorPerlin generatorCollection[];
    private int octaves;

    public InfdevNoiseGeneratorOctaves(Random random, int i)
    {
        octaves = i;
        generatorCollection = new InfdevNoiseGeneratorPerlin[i];
        for(int j = 0; j < i; j++)
        {
            generatorCollection[j] = new InfdevNoiseGeneratorPerlin(random);
        }

    }

    public double func_806_a(double d, double d1)
    {
        double d2 = 0.0D;
        double d3 = 1.0D;
        for(int i = 0; i < octaves; i++)
        {
            d2 += generatorCollection[i].func_801_a(d * d3, d1 * d3) / d3;
            d3 /= 2D;
        }

        return d2;
    }

    public double a(double d, double d1, double d2)
    {
        double d3 = 0.0D;
        double d4 = 1.0D;
        for(int i = 0; i < octaves; i++)
        {
            d3 += generatorCollection[i].a(d * d4, d1 * d4, d2 * d4) / d4;
            d4 /= 2D;
        }

        return d3;
    }

    public double[] a(double ad[], int i, int j, int k, int l, int i1, int j1, 
            double d, double d1, double d2)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        } else
        {
            for(int k1 = 0; k1 < ad.length; k1++)
            {
                ad[k1] = 0.0D;
            }

        }
        double d3 = 1.0D;
        for(int l1 = 0; l1 < octaves; l1++)
        {
            generatorCollection[l1].func_805_a(ad, i, j, k, l, i1, j1, d * d3, d1 * d3, d2 * d3, d3);
            d3 /= 2D;
        }

        return ad;
    }
}
