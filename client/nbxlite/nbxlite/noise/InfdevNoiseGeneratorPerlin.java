package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;

public class InfdevNoiseGeneratorPerlin extends NoiseGenerator
{
    private int permutations[];
    private double xCoord;
    private double yCoord;
    private double zCoord;

    public InfdevNoiseGeneratorPerlin()
    {
        this(new Random());
    }

    public InfdevNoiseGeneratorPerlin(Random random)
    {
        permutations = new int[512];
        xCoord = random.nextDouble() * 256D;
        yCoord = random.nextDouble() * 256D;
        zCoord = random.nextDouble() * 256D;
        for(int i = 0; i < 256; i++)
        {
            permutations[i] = i;
        }

        for(int j = 0; j < 256; j++)
        {
            int k = random.nextInt(256 - j) + j;
            int l = permutations[j];
            permutations[j] = permutations[k];
            permutations[k] = l;
            permutations[j + 256] = permutations[j];
        }

    }

    private double generateNoise(double d1, double d2, double d3)
    {
        double d4 = d1 + xCoord;
        double d5 = d2 + yCoord;
        double d6 = d3 + zCoord;
        d1 = (int)d4;
        int i = (int)d5;
        d2 = (int)d6;
        if(d4 < (double)d1)
        {
            d1--;
        }
        if(d5 < (double)i)
        {
            i--;
        }
        if(d6 < (double)d2)
        {
            d2--;
        }
        int j = (int)d1 & 0xff;
        d3 = i & 0xff;
        int k = (int)d2 & 0xff;
        d4 -= d1;
        d5 -= i;
        d6 -= d2;
        double d7 = d4 * d4 * d4 * (d4 * (d4 * 6D - 15D) + 10D);
        double d8 = d5 * d5 * d5 * (d5 * (d5 * 6D - 15D) + 10D);
        double d9 = d6 * d6 * d6 * (d6 * (d6 * 6D - 15D) + 10D);
        d1 = permutations[j] + d3;
        i = permutations[(int)d1] + k;
        d1 = permutations[(int)d1 + 1] + k;
        d2 = permutations[j + 1] + d3;
        j = permutations[(int)d2] + k;
        d2 = permutations[(int)d2 + 1] + k;
        return lerp(d9,
                    lerp(d8, 
                        lerp(d7,
                            grad(permutations[i], d4, d5, d6),
                            grad(permutations[j], d4 - 1.0D, d5, d6)),
                        lerp(d7,
                            grad(permutations[(int)d1], d4, d5 - 1.0D, d6),
                            grad(permutations[(int)d2], d4 - 1.0D, d5 - 1.0D, d6))),
                        lerp(d8,
                            lerp(d7,
                                grad(permutations[i + 1], d4, d5, d6 - 1.0D),
                                grad(permutations[j + 1], d4 - 1.0D, d5, d6 - 1.0D)),
                            lerp(d7,
                                grad(permutations[(int)d1 + 1], d4, d5 - 1.0D, d6 - 1.0D),
                                grad(permutations[(int)d2 + 1], d4 - 1.0D, d5 - 1.0D, d6 - 1.0D))));
    }

    private double lerp(double d1, double d2, double d3)
    {
        return d2 + d1 * (d3 - d2);
    }

    private double grad(int i, double d1, double d2, double d3)
    {
        double d4 = (i &= 0xf) >= 8 ? d2 : d1;
        double d5 = i >= 4 ? i != 12 && i != 14 ? d3 : d1 : d2;
        return ((i & 1) != 0 ? -d4 : d4) + ((i & 2) != 0 ? -d5 : d5);
    }

    public double func_801_a(double d1, double d2)
    {
        return generateNoise(d1, d2, 0.0D);
    }

    public double a(double d1, double d2, double d3)
    {
        return generateNoise(d1, d2, d3);
    }

    public void func_805_a(double ad[], int i, int j, int k, int l, int i1, int j1, 
            double d1, double d2, double d3, double d4)
    {
        int l1 = 0;
        double d5 = 1.0D / d4;
        d4 = -1;
        double d6 = 0.0D;
        double d7 = 0.0D;
        double d8 = 0.0D;
        double d9 = 0.0D;
        for(int l2 = 0; l2 < l; l2++)
        {
            double d10;
            int k1 = (int)(d10 = (double)(i + l2) * d1 + xCoord);
            if(d10 < (double)k1)
            {
                k1--;
            }
            int i3 = k1 & 0xff;
            double d11 = (d10 -= k1) * d10 * d10 * (d10 * (d10 * 6D - 15D) + 10D);
            for(int j3 = 0; j3 < j1; j3++)
            {
                double d12;
                k1 = (int)(d12 = (double)(k + j3) * d3 + zCoord);
                if(d12 < (double)k1)
                {
                    k1--;
                }
                int k3 = k1 & 0xff;
                double d13 = (d12 -= k1) * d12 * d12 * (d12 * (d12 * 6D - 15D) + 10D);
                for(int l3 = 0; l3 < i1; l3++)
                {
                    double d14;
                    k1 = (int)(d14 = (double)(j + l3) * d2 + yCoord);
                    if(d14 < (double)k1)
                    {
                        k1--;
                    }
                    int j2 = k1 & 0xff;
                    double d15 = (d14 -= k1) * d14 * d14 * (d14 * (d14 * 6D - 15D) + 10D);
                    if(l3 == 0 || j2 != d4)
                    {
                        d4 = j2;
                        k1 = permutations[i3] + j2;
                        int i2 = permutations[k1] + k3;
                        k1 = permutations[k1 + 1] + k3;
                        j2 = permutations[i3 + 1] + j2;
                        int k2 = permutations[j2] + k3;
                        j2 = permutations[j2 + 1] + k3;
                        d6 = lerp(d11, grad(permutations[i2], d10, d14, d12), grad(permutations[k2], d10 - 1.0D, d14, d12));
                        d7 = lerp(d11, grad(permutations[k1], d10, d14 - 1.0D, d12), grad(permutations[j2], d10 - 1.0D, d14 - 1.0D, d12));
                        d8 = lerp(d11, grad(permutations[i2 + 1], d10, d14, d12 - 1.0D), grad(permutations[k2 + 1], d10 - 1.0D, d14, d12 - 1.0D));
                        d9 = lerp(d11, grad(permutations[k1 + 1], d10, d14 - 1.0D, d12 - 1.0D), grad(permutations[j2 + 1], d10 - 1.0D, d14 - 1.0D, d12 - 1.0D));
                    }
                    double d16 = lerp(d15, d6, d7);
                    double d17 = lerp(d15, d8, d9);
                    double d18 = lerp(d13, d16, d17);
                    ad[l1++] += d18 * d5;
                }

            }

        }

    }
}
