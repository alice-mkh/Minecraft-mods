package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;

public class AlphaNoiseGeneratorOctaves extends NoiseGenerator
{
    private AlphaNoiseGeneratorPerlin generatorCollection[];
    private int octaves;

    public AlphaNoiseGeneratorOctaves(Random random, int i)
    {
        octaves = i;
        generatorCollection = new AlphaNoiseGeneratorPerlin[i];
        for(int j = 0; j < i; j++)
        {
            generatorCollection[j] = new AlphaNoiseGeneratorPerlin(random);
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

    public double[] generateNoiseOctaves(double ad[], double d, double d1, double d2, 
            int i, int j, int k, double d3, double d4, 
            double d5)
    {
        if(ad == null)
        {
            ad = new double[i * j * k];
        } else
        {
            for(int l = 0; l < ad.length; l++)
            {
                ad[l] = 0.0D;
            }

        }
        double d6 = 1.0D;
        for(int i1 = 0; i1 < octaves; i1++)
        {
            generatorCollection[i1].func_805_a(ad, d, d1, d2, i, j, k, d3 * d6, d4 * d6, d5 * d6, d6);
            d6 /= 2D;
        }

        return ad;
    }

    public double[] generateNoiseOctaves(double ad[], int i, int j, int k, int l, double d, 
            double d1, double d2)
    {
        return generateNoiseOctaves(ad, i, 10D, j, k, 1, l, d, 1.0D, d1);
    }
}
