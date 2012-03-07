package net.minecraft.src.nbxlite.noise;

import java.util.Random;
import net.minecraft.src.NoiseGenerator;
import net.minecraft.src.MathHelper;

public class InfdevOldNoiseGeneratorPerlin extends NoiseGenerator
{
    private int permutations[];
    private double xCoord;
    private double yCoord;
    private double zCoord;

    public InfdevOldNoiseGeneratorPerlin()
    {
        this(new Random());
    }

    public InfdevOldNoiseGeneratorPerlin(Random random)
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
        d1 = MathHelper.floor_double(d4) & 0xff;
        double i = MathHelper.floor_double(d5) & 0xff;
        d2 =  MathHelper.floor_double(d6) & 0xff;
        d4 -=  MathHelper.floor_double(d4);
        d5 -=  MathHelper.floor_double(d5);
        d6 -=  MathHelper.floor_double(d6);
        double d7 = a(d4);
        double d8 = a(d5);
        double d9 = a(d6);
        double j = permutations[(int)d1] + i;
        d3 = permutations[(int)j] + d2;
        j = permutations[(int)j + 1] + d2;
        d1 = permutations[(int)d1 + 1] + i;
        i = permutations[(int)d1] + d2;
        d1 = permutations[(int)d1 + 1] + d2;
        return lerp(d9,
                    lerp(d8,
                        lerp(d7,
                            grad(permutations[(int)d3], d4, d5, d6),
                            grad(permutations[(int)i], d4 - 1.0D, d5, d6)),
                        lerp(d7,
                            grad(permutations[(int)j], d4, d5 - 1.0D, d6),
                            grad(permutations[(int)d1], d4 - 1.0D, d5 - 1.0D, d6))),
                    lerp(d8,
                        lerp(d7,
                            grad(permutations[(int)d3 + 1], d4, d5, d6 - 1.0D),
                            grad(permutations[(int)i + 1], d4 - 1.0D, d5, d6 - 1.0D)),
                        lerp(d7, 
                            grad(permutations[(int)j + 1], d4, d5 - 1.0D, d6 - 1.0D),
                            grad(permutations[(int)d1 + 1], d4 - 1.0D, d5 - 1.0D, d6 - 1.0D))));
    }

    private static double a(double d1)
    {
        return d1 * d1 * d1 * (d1 * (d1 * 6D - 15D) + 10D);
    }

    private static double lerp(double d1, double d2, double d3)
    {
        return d2 + d1 * (d3 - d2);
    }

    private static double grad(int i, double d1, double d2, double d3)
    {
        double d4 = (i &= 0xf) >= 8 ? d2 : d1;
        double d5 = i >= 4 ? i != 12 && i != 14 ? d3 : d1 : d2;
        return ((i & 1) != 0 ? -d4 : d4) + ((i & 2) != 0 ? -d5 : d5);
    }

    public double a(double d1, double d2)
    {
        return generateNoise(d1, d2, 0.0D);
    }

    public double a(double d1, double d2, double d3)
    {
        return generateNoise(d1, d2, d3);
    }
}
