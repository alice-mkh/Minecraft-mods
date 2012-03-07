package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;

public class BetaNoiseGeneratorPerlin extends NoiseGenerator
{
    private int permutations[];
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public BetaNoiseGeneratorPerlin()
    {
        this(new Random());
    }

    public BetaNoiseGeneratorPerlin(Random random)
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

    public final double lerp(double d, double d1, double d2)
    {
        return d1 + d * (d2 - d1);
    }

    public final double func_4110_a(int i, double d, double d1)
    {
        int j = i & 0xf;
        double d2 = (double)(1 - ((j & 8) >> 3)) * d;
        double d3 = j < 4 ? 0.0D : j == 12 || j == 14 ? d : d1;
        return ((j & 1) == 0 ? d2 : -d2) + ((j & 2) == 0 ? d3 : -d3);
    }

    public final double grad(int i, double d, double d1, double d2)
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
        if(j == 1)
        {
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            double d8 = 0.0D;
            double d9 = 0.0D;
            int j1 = 0;
            double d10 = 1.0D / d6;
            for(int k1 = 0; k1 < i; k1++)
            {
                double d12 = (d + (double)k1) * d3 + xCoord;
                int l1 = (int)d12;
                if(d12 < (double)l1)
                {
                    l1--;
                }
                int i2 = l1 & 0xff;
                d12 -= l1;
                double d15 = d12 * d12 * d12 * (d12 * (d12 * 6D - 15D) + 10D);
                for(int j2 = 0; j2 < k; j2++)
                {
                    double d17 = (d2 + (double)j2) * d5 + zCoord;
                    int l2 = (int)d17;
                    if(d17 < (double)l2)
                    {
                        l2--;
                    }
                    int j3 = l2 & 0xff;
                    d17 -= l2;
                    double d19 = d17 * d17 * d17 * (d17 * (d17 * 6D - 15D) + 10D);
                    int l3 = permutations[i2] + 0;
                    int j4 = permutations[l3] + j3;
                    int k4 = permutations[i2 + 1] + 0;
                    int l4 = permutations[k4] + j3;
                    double d22 = lerp(d15, func_4110_a(permutations[j4], d12, d17), grad(permutations[l4], d12 - 1.0D, 0.0D, d17));
                    double d24 = lerp(d15, grad(permutations[j4 + 1], d12, 0.0D, d17 - 1.0D), grad(permutations[l4 + 1], d12 - 1.0D, 0.0D, d17 - 1.0D));
                    double d25 = lerp(d19, d22, d24);
                    ad[j1++] += d25 * d10;
                }

            }

            return;
        }
        int l = 0;
        double d7 = 1.0D / d6;
        int i1 = -1;
        boolean flag4 = false;
        boolean flag5 = false;
        boolean flag6 = false;
        boolean flag7 = false;
        boolean flag8 = false;
        boolean flag9 = false;
        double d11 = 0.0D;
        double d13 = 0.0D;
        double d14 = 0.0D;
        double d16 = 0.0D;
        for(int k2 = 0; k2 < i; k2++)
        {
            double d18 = (d + (double)k2) * d3 + xCoord;
            int i3 = (int)d18;
            if(d18 < (double)i3)
            {
                i3--;
            }
            int k3 = i3 & 0xff;
            d18 -= i3;
            double d20 = d18 * d18 * d18 * (d18 * (d18 * 6D - 15D) + 10D);
            for(int i4 = 0; i4 < k; i4++)
            {
                double d21 = (d2 + (double)i4) * d5 + zCoord;
                int i5 = (int)d21;
                if(d21 < (double)i5)
                {
                    i5--;
                }
                int j5 = i5 & 0xff;
                d21 -= i5;
                double d23 = d21 * d21 * d21 * (d21 * (d21 * 6D - 15D) + 10D);
                for(int k5 = 0; k5 < j; k5++)
                {
                    double d26 = (d1 + (double)k5) * d4 + yCoord;
                    int l5 = (int)d26;
                    if(d26 < (double)l5)
                    {
                        l5--;
                    }
                    int i6 = l5 & 0xff;
                    d26 -= l5;
                    double d27 = d26 * d26 * d26 * (d26 * (d26 * 6D - 15D) + 10D);
                    if(k5 == 0 || i6 != i1)
                    {
                        i1 = i6;
                        int j6 = permutations[k3] + i6;
                        int k6 = permutations[j6] + j5;
                        int l6 = permutations[j6 + 1] + j5;
                        int i7 = permutations[k3 + 1] + i6;
                        int j7 = permutations[i7] + j5;
                        int k7 = permutations[i7 + 1] + j5;
                        d11 = lerp(d20, grad(permutations[k6], d18, d26, d21), grad(permutations[j7], d18 - 1.0D, d26, d21));
                        d13 = lerp(d20, grad(permutations[l6], d18, d26 - 1.0D, d21), grad(permutations[k7], d18 - 1.0D, d26 - 1.0D, d21));
                        d14 = lerp(d20, grad(permutations[k6 + 1], d18, d26, d21 - 1.0D), grad(permutations[j7 + 1], d18 - 1.0D, d26, d21 - 1.0D));
                        d16 = lerp(d20, grad(permutations[l6 + 1], d18, d26 - 1.0D, d21 - 1.0D), grad(permutations[k7 + 1], d18 - 1.0D, d26 - 1.0D, d21 - 1.0D));
                    }
                    double d28 = lerp(d27, d11, d13);
                    double d29 = lerp(d27, d14, d16);
                    double d30 = lerp(d23, d28, d29);
                    ad[l++] += d30 * d7;
                }

            }

        }

    }
}
