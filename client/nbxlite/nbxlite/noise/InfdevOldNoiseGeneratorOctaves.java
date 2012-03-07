package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;

public class InfdevOldNoiseGeneratorOctaves extends NoiseGenerator
{
    private InfdevOldNoiseGeneratorPerlin generatorCollection[];
    private int octaves;

    public InfdevOldNoiseGeneratorOctaves(int i)
    {
        this(new Random(), i);
    }

    public InfdevOldNoiseGeneratorOctaves(Random random, int i)
    {
        octaves = i;
        generatorCollection = new InfdevOldNoiseGeneratorPerlin[i];
        for(int j = 0; j < i; j++)
        {
            generatorCollection[j] = new InfdevOldNoiseGeneratorPerlin(random);
        }

    }

    public double func_806_a(double d, double d1)
    {
        double d2 = 0.0D;
        double d3 = 1.0D;
        for(int i = 0; i < octaves; i++)
        {
            d2 += generatorCollection[i].a(d / d3, d1 / d3) * d3;
            d3 *= 2D;
        }

        return d2;
    }

    public double a(double d, double d1, double d2)
    {
        double d3 = 0.0D;
        double d4 = 1.0D;
        for(d1 = 0; d1 < octaves; d1++)
        {
            d3 += generatorCollection[(int)d1].a(d / d4, 0.0D / d4, d2 / d4) * d4;
            d4 *= 2D;
        }

        return d3;
    }
}
