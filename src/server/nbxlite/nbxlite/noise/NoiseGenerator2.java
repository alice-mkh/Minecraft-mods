package net.minecraft.src.nbxlite.noise;

import java.util.Random;

public class NoiseGenerator2
{
    private static int field_4296_d[][] = {
        {
            1, 1, 0
        }, {
            -1, 1, 0
        }, {
            1, -1, 0
        }, {
            -1, -1, 0
        }, {
            1, 0, 1
        }, {
            -1, 0, 1
        }, {
            1, 0, -1
        }, {
            -1, 0, -1
        }, {
            0, 1, 1
        }, {
            0, -1, 1
        }, {
            0, 1, -1
        }, {
            0, -1, -1
        }
    };
    private int field_4295_e[];
    public double field_4292_a;
    public double field_4291_b;
    public double field_4297_c;
    private static final double field_4294_f = 0.5D * (Math.sqrt(3D) - 1.0D);
    private static final double field_4293_g = (3D - Math.sqrt(3D)) / 6D;

    public NoiseGenerator2()
    {
        this(new Random());
    }

    public NoiseGenerator2(Random random)
    {
        field_4295_e = new int[512];
        field_4292_a = random.nextDouble() * 256D;
        field_4291_b = random.nextDouble() * 256D;
        field_4297_c = random.nextDouble() * 256D;
        for(int i = 0; i < 256; i++)
        {
            field_4295_e[i] = i;
        }

        for(int j = 0; j < 256; j++)
        {
            int k = random.nextInt(256 - j) + j;
            int l = field_4295_e[j];
            field_4295_e[j] = field_4295_e[k];
            field_4295_e[k] = l;
            field_4295_e[j + 256] = field_4295_e[j];
        }

    }

    private static int wrap(double d)
    {
        return d > 0.0D ? (int)d : (int)d - 1;
    }

    private static double func_4156_a(int ai[], double d, double d1)
    {
        return (double)ai[0] * d + (double)ai[1] * d1;
    }

    public void func_4157_a(double ad[], double d, double d1, int i, int j, 
            double d2, double d3, double d4)
    {
        int k = 0;
        for(int l = 0; l < i; l++)
        {
            double d5 = (d + (double)l) * d2 + field_4292_a;
            for(int i1 = 0; i1 < j; i1++)
            {
                double d6 = (d1 + (double)i1) * d3 + field_4291_b;
                double d7 = (d5 + d6) * field_4294_f;
                int j1 = wrap(d5 + d7);
                int k1 = wrap(d6 + d7);
                double d8 = (double)(j1 + k1) * field_4293_g;
                double d9 = (double)j1 - d8;
                double d10 = (double)k1 - d8;
                double d11 = d5 - d9;
                double d12 = d6 - d10;
                int l1;
                int i2;
                if(d11 > d12)
                {
                    l1 = 1;
                    i2 = 0;
                } else
                {
                    l1 = 0;
                    i2 = 1;
                }
                double d13 = (d11 - (double)l1) + field_4293_g;
                double d14 = (d12 - (double)i2) + field_4293_g;
                double d15 = (d11 - 1.0D) + 2D * field_4293_g;
                double d16 = (d12 - 1.0D) + 2D * field_4293_g;
                int j2 = j1 & 0xff;
                int k2 = k1 & 0xff;
                int l2 = field_4295_e[j2 + field_4295_e[k2]] % 12;
                int i3 = field_4295_e[j2 + l1 + field_4295_e[k2 + i2]] % 12;
                int j3 = field_4295_e[j2 + 1 + field_4295_e[k2 + 1]] % 12;
                double d17 = 0.5D - d11 * d11 - d12 * d12;
                double d18;
                if(d17 < 0.0D)
                {
                    d18 = 0.0D;
                } else
                {
                    d17 *= d17;
                    d18 = d17 * d17 * func_4156_a(field_4296_d[l2], d11, d12);
                }
                double d19 = 0.5D - d13 * d13 - d14 * d14;
                double d20;
                if(d19 < 0.0D)
                {
                    d20 = 0.0D;
                } else
                {
                    d19 *= d19;
                    d20 = d19 * d19 * func_4156_a(field_4296_d[i3], d13, d14);
                }
                double d21 = 0.5D - d15 * d15 - d16 * d16;
                double d22;
                if(d21 < 0.0D)
                {
                    d22 = 0.0D;
                } else
                {
                    d21 *= d21;
                    d22 = d21 * d21 * func_4156_a(field_4296_d[j3], d15, d16);
                }
                ad[k++] += 70D * (d18 + d20 + d22) * d4;
            }

        }

    }

}
