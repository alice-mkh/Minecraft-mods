package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class OldMapGenCavesHell extends MapGenBase
{
    public OldMapGenCavesHell()
    {
    }

    /**
     * Generates a larger initial cave node than usual. Called 25% of the time.
     */
    protected void generateLargeCaveNode(int par1, int par2, byte par3ArrayOfByte[], double par4, double par6, double par8)
    {
        generateCaveNode(par1, par2, par3ArrayOfByte, par4, par6, par8, 1.0F + rand.nextFloat() * 6F, 0.0F, 0.0F, -1, -1, 0.5D);
    }

    /**
     * Generates a node in the current cave system recursion tree.
     */
    protected void generateCaveNode(int par1, int par2, byte par3ArrayOfByte[], double par4, double par6, double par8, float par10, float par11, float par12, int par13, int par14, double par15)
    {
        double d = par1 * 16 + 8;
        double d1 = par2 * 16 + 8;
        float f = 0.0F;
        float f1 = 0.0F;
        Random random = new Random(rand.nextLong());

        if (par14 <= 0)
        {
            int i = range * 16 - 16;
            par14 = i - random.nextInt(i / 4);
        }

        boolean flag = false;

        if (par13 == -1)
        {
            par13 = par14 / 2;
            flag = true;
        }

        int j = random.nextInt(par14 / 2) + par14 / 4;
        boolean flag1 = random.nextInt(6) == 0;

        for (; par13 < par14; par13++)
        {
            double d2 = 1.5D + (double)(MathHelper.sin(((float)par13 * (float)Math.PI) / (float)par14) * par10 * 1.0F);
            double d3 = d2 * par15;
            float f2 = MathHelper.cos(par12);
            float f3 = MathHelper.sin(par12);
            par4 += MathHelper.cos(par11) * f2;
            par6 += f3;
            par8 += MathHelper.sin(par11) * f2;

            if (flag1)
            {
                par12 *= 0.92F;
            }
            else
            {
                par12 *= 0.7F;
            }

            par12 += f1 * 0.1F;
            par11 += f * 0.1F;
            f1 *= 0.9F;
            f *= 0.75F;
            f1 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

            if (!flag && par13 == j && par10 > 1.0F)
            {
                generateCaveNode(par1, par2, par3ArrayOfByte, par4, par6, par8, random.nextFloat() * 0.5F + 0.5F, par11 - ((float)Math.PI / 2F), par12 / 3F, par13, par14, 1.0D);
                generateCaveNode(par1, par2, par3ArrayOfByte, par4, par6, par8, random.nextFloat() * 0.5F + 0.5F, par11 + ((float)Math.PI / 2F), par12 / 3F, par13, par14, 1.0D);
                return;
            }

            if (!flag && random.nextInt(4) == 0)
            {
                continue;
            }

            double d4 = par4 - d;
            double d5 = par8 - d1;
            double d6 = par14 - par13;
            double d7 = par10 + 2.0F + 16F;

            if ((d4 * d4 + d5 * d5) - d6 * d6 > d7 * d7)
            {
                return;
            }

            if (par4 < d - 16D - d2 * 2D || par8 < d1 - 16D - d2 * 2D || par4 > d + 16D + d2 * 2D || par8 > d1 + 16D + d2 * 2D)
            {
                continue;
            }

            d4 = MathHelper.floor_double(par4 - d2) - par1 * 16 - 1;
            int k = (MathHelper.floor_double(par4 + d2) - par1 * 16) + 1;
            d5 = MathHelper.floor_double(par6 - d3) - 1;
            int l = MathHelper.floor_double(par6 + d3) + 1;
            d6 = MathHelper.floor_double(par8 - d2) - par2 * 16 - 1;
            int i1 = (MathHelper.floor_double(par8 + d2) - par2 * 16) + 1;

            if (d4 < 0)
            {
                d4 = 0;
            }

            if (k > 16)
            {
                k = 16;
            }

            if (d5 < 1)
            {
                d5 = 1;
            }

            if (l > 120)
            {
                l = 120;
            }

            if (d6 < 0)
            {
                d6 = 0;
            }

            if (i1 > 16)
            {
                i1 = 16;
            }

            boolean flag2 = false;

            for (int j1 = (int) d4; !flag2 && j1 < k; j1++)
            {
                for (int l1 = (int) d6; !flag2 && l1 < i1; l1++)
                {
                    for (int i2 = l + 1; !flag2 && i2 >= d5 - 1; i2--)
                    {
                        int j2 = (j1 * 16 + l1) * 128 + i2;

                        if (i2 < 0 || i2 >= 128)
                        {
                            continue;
                        }

                        if (par3ArrayOfByte[j2] == Block.lavaMoving.blockID || par3ArrayOfByte[j2] == Block.lavaStill.blockID)
                        {
                            flag2 = true;
                        }

                        if (i2 != d5 - 1 && j1 != d4 && j1 != k - 1 && l1 != d6 && l1 != i1 - 1)
                        {
                            i2 = (int) d5;
                        }
                    }
                }
            }

            if (flag2)
            {
                continue;
            }

            for (int k1 = (int) d4; k1 < k; k1++)
            {
                double d8 = (((double)(k1 + par1 * 16) + 0.5D) - par4) / d2;

                for (int k2 = (int) d6; k2 < i1; k2++)
                {
                    double d9 = (((double)(k2 + par2 * 16) + 0.5D) - par8) / d2;
                    int l2 = (k1 * 16 + k2) * 128 + l;

                    for (int i3 = l - 1; i3 >= d5; i3--)
                    {
                        double d10 = (((double)i3 + 0.5D) - par6) / d3;

                        if (d10 > -0.69999999999999996D && d8 * d8 + d10 * d10 + d9 * d9 < 1.0D)
                        {
                            byte byte0 = par3ArrayOfByte[l2];

                            if (byte0 == Block.netherrack.blockID || byte0 == Block.dirt.blockID || byte0 == Block.grass.blockID)
                            {
                                par3ArrayOfByte[l2] = 0;
                            }
                        }

                        l2--;
                    }
                }
            }

            if (flag)
            {
                break;
            }
        }
    }

    /**
     * Recursively called by generate() (generate) and optionally by itself.
     */
    @Override
    protected void recursiveGenerate(World par1World, int par2, int par3, int par4, int par5, byte par6ArrayOfByte[])
    {
        int i = rand.nextInt(rand.nextInt(rand.nextInt(10) + 1) + 1);

        if (rand.nextInt(5) != 0)
        {
            i = 0;
        }

        for (int j = 0; j < i; j++)
        {
            double d = par2 * 16 + rand.nextInt(16);
            double d1 = rand.nextInt(128);
            double d2 = par3 * 16 + rand.nextInt(16);
            int k = 1;

            if (rand.nextInt(4) == 0)
            {
                generateLargeCaveNode(par4, par5, par6ArrayOfByte, d, d1, d2);
                k += rand.nextInt(4);
            }

            for (int l = 0; l < k; l++)
            {
                float f = rand.nextFloat() * (float)Math.PI * 2.0F;
                float f1 = ((rand.nextFloat() - 0.5F) * 2.0F) / 8F;
                float f2 = rand.nextFloat() * 2.0F + rand.nextFloat();
                generateCaveNode(par4, par5, par6ArrayOfByte, d, d1, d2, f2 * 2.0F, f, f1, 0, 0, 0.5D);
            }
        }
    }
}
