package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;

public class AlphaNoiseGeneratorPerlin extends NoiseGenerator
{
    private int permutations[];
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public AlphaNoiseGeneratorPerlin()
    {
        this(new Random());
    }

    public AlphaNoiseGeneratorPerlin(Random random)
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

    public double generateNoise(double d, double d1, double d2)
    {
        double d3 = d + xCoord;
        double d4 = d1 + yCoord;
        double d5 = d2 + zCoord;
        int i = (int)d3;
        int j = (int)d4;
        int k = (int)d5;
        if(d3 < (double)i)
        {
            i--;
        }
        if(d4 < (double)j)
        {
            j--;
        }
        if(d5 < (double)k)
        {
            k--;
        }
        int l = i & 0xff;
        int i1 = j & 0xff;
        int j1 = k & 0xff;
        d3 -= i;
        d4 -= j;
        d5 -= k;
        double d6 = d3 * d3 * d3 * (d3 * (d3 * 6D - 15D) + 10D);
        double d7 = d4 * d4 * d4 * (d4 * (d4 * 6D - 15D) + 10D);
        double d8 = d5 * d5 * d5 * (d5 * (d5 * 6D - 15D) + 10D);
        int k1 = permutations[l] + i1;
        int l1 = permutations[k1] + j1;
        int i2 = permutations[k1 + 1] + j1;
        int j2 = permutations[l + 1] + i1;
        int k2 = permutations[j2] + j1;
        int l2 = permutations[j2 + 1] + j1;
        return lerp(d8, lerp(d7, lerp(d6, grad(permutations[l1], d3, d4, d5), grad(permutations[k2], d3 - 1.0D, d4, d5)), lerp(d6, grad(permutations[i2], d3, d4 - 1.0D, d5), grad(permutations[l2], d3 - 1.0D, d4 - 1.0D, d5))), lerp(d7, lerp(d6, grad(permutations[l1 + 1], d3, d4, d5 - 1.0D), grad(permutations[k2 + 1], d3 - 1.0D, d4, d5 - 1.0D)), lerp(d6, grad(permutations[i2 + 1], d3, d4 - 1.0D, d5 - 1.0D), grad(permutations[l2 + 1], d3 - 1.0D, d4 - 1.0D, d5 - 1.0D))));
    }

    public double lerp(double d, double d1, double d2)
    {
        return d1 + d * (d2 - d1);
    }

    public double grad(int i, double d, double d1, double d2)
    {
        int j = i & 0xf;
        double d3 = j < 8 ? d : d1;
        double d4 = j < 4 ? d1 : j == 12 || j == 14 ? d : d2;
        return ((j & 1) == 0 ? d3 : -d3) + ((j & 2) == 0 ? d4 : -d4);
    }

    public double func_801_a(double d, double d1)
    {
        return generateNoise(d, d1, 0.0D);
    }

    public void func_805_a(double ad[], double d, double d1, double d2, 
            int i, int j, int k, double d3, double d4, 
            double d5, double d6)
    {
        int l = 0;
        double d7 = 1.0D / d6;
        int i1 = -1;
        boolean flag = false;
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        boolean flag5 = false;
        double d8 = 0.0D;
        double d9 = 0.0D;
        double d10 = 0.0D;
        double d11 = 0.0D;
        for(int j1 = 0; j1 < i; j1++)
        {
            double d12 = (d + (double)j1) * d3 + xCoord;
            int k1 = (int)d12;
            if(d12 < (double)k1)
            {
                k1--;
            }
            int l1 = k1 & 0xff;
            d12 -= k1;
            double d13 = d12 * d12 * d12 * (d12 * (d12 * 6D - 15D) + 10D);
            for(int i2 = 0; i2 < k; i2++)
            {
                double d14 = (d2 + (double)i2) * d5 + zCoord;
                int j2 = (int)d14;
                if(d14 < (double)j2)
                {
                    j2--;
                }
                int k2 = j2 & 0xff;
                d14 -= j2;
                double d15 = d14 * d14 * d14 * (d14 * (d14 * 6D - 15D) + 10D);
                for(int l2 = 0; l2 < j; l2++)
                {
                    double d16 = (d1 + (double)l2) * d4 + yCoord;
                    int i3 = (int)d16;
                    if(d16 < (double)i3)
                    {
                        i3--;
                    }
                    int j3 = i3 & 0xff;
                    d16 -= i3;
                    double d17 = d16 * d16 * d16 * (d16 * (d16 * 6D - 15D) + 10D);
                    if(l2 == 0 || j3 != i1)
                    {
                        i1 = j3;
                        int k3 = permutations[l1] + j3;
                        int l3 = permutations[k3] + k2;
                        int i4 = permutations[k3 + 1] + k2;
                        int j4 = permutations[l1 + 1] + j3;
                        int k4 = permutations[j4] + k2;
                        int l4 = permutations[j4 + 1] + k2;
                        d8 = lerp(d13, grad(permutations[l3], d12, d16, d14), grad(permutations[k4], d12 - 1.0D, d16, d14));
                        d9 = lerp(d13, grad(permutations[i4], d12, d16 - 1.0D, d14), grad(permutations[l4], d12 - 1.0D, d16 - 1.0D, d14));
                        d10 = lerp(d13, grad(permutations[l3 + 1], d12, d16, d14 - 1.0D), grad(permutations[k4 + 1], d12 - 1.0D, d16, d14 - 1.0D));
                        d11 = lerp(d13, grad(permutations[i4 + 1], d12, d16 - 1.0D, d14 - 1.0D), grad(permutations[l4 + 1], d12 - 1.0D, d16 - 1.0D, d14 - 1.0D));
                    }
                    double d18 = lerp(d17, d8, d9);
                    double d19 = lerp(d17, d10, d11);
                    double d20 = lerp(d15, d18, d19);
                    ad[l++] += d20 * d7;
                }

            }

        }

    }
}
