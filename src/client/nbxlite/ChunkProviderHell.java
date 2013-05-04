package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.src.nbxlite.mapgens.OldMapGenCavesHell;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenFlowers;
import net.minecraft.src.nbxlite.noise.BetaNoiseGeneratorOctaves;

public class ChunkProviderHell implements IChunkProvider
{
    private Random hellRNG;

    /** A NoiseGeneratorOctaves used in generating nether terrain */
    private NoiseGeneratorOctaves netherNoiseGen1;
    private NoiseGeneratorOctaves netherNoiseGen2;
    private NoiseGeneratorOctaves netherNoiseGen3;

    /** Determines whether slowsand or gravel can be generated at a location */
    private NoiseGeneratorOctaves slowsandGravelNoiseGen;

    /**
     * Determines whether something other than nettherack can be generated at a location
     */
    private NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
    public NoiseGeneratorOctaves netherNoiseGen6;
    public NoiseGeneratorOctaves netherNoiseGen7;

    private BetaNoiseGeneratorOctaves field_4169_i;
    private BetaNoiseGeneratorOctaves field_4168_j;
    private BetaNoiseGeneratorOctaves field_4167_k;
    private BetaNoiseGeneratorOctaves field_4166_l;
    private BetaNoiseGeneratorOctaves field_4165_m;
    public BetaNoiseGeneratorOctaves field_4177_a;
    public BetaNoiseGeneratorOctaves field_4176_b;

    /** Is the world that the nether is getting generated. */
    private World worldObj;
    private double noiseField[];
    public MapGenNetherBridge genNetherBridge;
    private double slowsandNoise[];
    private double gravelNoise[];
    private double netherrackExclusivityNoise[];
    private MapGenBase netherCaveGenerator;
    private MapGenBase netherCaveGeneratorOld;
    double noiseData1[];
    double noiseData2[];
    double noiseData3[];
    double noiseData4[];
    double noiseData5[];
    private boolean generateStructures;

    public ChunkProviderHell(World par1World, long par2)
    {
        genNetherBridge = new MapGenNetherBridge();
        slowsandNoise = new double[256];
        gravelNoise = new double[256];
        netherrackExclusivityNoise = new double[256];
        netherCaveGenerator = new MapGenCavesHell();
        netherCaveGeneratorOld = new OldMapGenCavesHell();
        worldObj = par1World;
        hellRNG = new Random(par2);
        netherNoiseGen1 = new NoiseGeneratorOctaves(hellRNG, 16);
        netherNoiseGen2 = new NoiseGeneratorOctaves(hellRNG, 16);
        netherNoiseGen3 = new NoiseGeneratorOctaves(hellRNG, 8);
        slowsandGravelNoiseGen = new NoiseGeneratorOctaves(hellRNG, 4);
        netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(hellRNG, 4);
        netherNoiseGen6 = new NoiseGeneratorOctaves(hellRNG, 10);
        netherNoiseGen7 = new NoiseGeneratorOctaves(hellRNG, 16);

        field_4169_i = new BetaNoiseGeneratorOctaves(hellRNG, 16);
        field_4168_j = new BetaNoiseGeneratorOctaves(hellRNG, 16);
        field_4167_k = new BetaNoiseGeneratorOctaves(hellRNG, 8);
        field_4166_l = new BetaNoiseGeneratorOctaves(hellRNG, 4);
        field_4165_m = new BetaNoiseGeneratorOctaves(hellRNG, 4);
        field_4177_a = new BetaNoiseGeneratorOctaves(hellRNG, 10);
        field_4176_b = new BetaNoiseGeneratorOctaves(hellRNG, 16);

        generateStructures = par1World.getWorldInfo().isMapFeaturesEnabled() || (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES && ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181);
    }

    /**
     * Generates the shape of the terrain in the nether.
     */
    public void generateNetherTerrain(int par1, int par2, byte par3ArrayOfByte[])
    {
        byte byte0 = 4;
        byte byte1 = 32;
        int i = byte0 + 1;
        byte byte2 = 17;
        int j = byte0 + 1;
        if (ODNBXlite.Generator < ODNBXlite.GEN_NEWBIOMES){
            noiseField = func_4057_a(noiseField, par1 * byte0, 0, par2 * byte0, i, byte2, j);
        }else{
            noiseField = initializeNoiseField(noiseField, par1 * byte0, 0, par2 * byte0, i, byte2, j);
        }

        for (int k = 0; k < byte0; k++)
        {
            for (int l = 0; l < byte0; l++)
            {
                for (int i1 = 0; i1 < 16; i1++)
                {
                    double d = 0.125D;
                    double d1 = noiseField[((k + 0) * j + (l + 0)) * byte2 + (i1 + 0)];
                    double d2 = noiseField[((k + 0) * j + (l + 1)) * byte2 + (i1 + 0)];
                    double d3 = noiseField[((k + 1) * j + (l + 0)) * byte2 + (i1 + 0)];
                    double d4 = noiseField[((k + 1) * j + (l + 1)) * byte2 + (i1 + 0)];
                    double d5 = (noiseField[((k + 0) * j + (l + 0)) * byte2 + (i1 + 1)] - d1) * d;
                    double d6 = (noiseField[((k + 0) * j + (l + 1)) * byte2 + (i1 + 1)] - d2) * d;
                    double d7 = (noiseField[((k + 1) * j + (l + 0)) * byte2 + (i1 + 1)] - d3) * d;
                    double d8 = (noiseField[((k + 1) * j + (l + 1)) * byte2 + (i1 + 1)] - d4) * d;

                    for (int j1 = 0; j1 < 8; j1++)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;

                        for (int k1 = 0; k1 < 4; k1++)
                        {
                            int l1 = k1 + k * 4 << 11 | 0 + l * 4 << 7 | i1 * 8 + j1;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;

                            for (int i2 = 0; i2 < 4; i2++)
                            {
                                int j2 = 0;

                                if (i1 * 8 + j1 < byte1)
                                {
                                    j2 = Block.lavaStill.blockID;
                                }

                                if (d15 > 0.0D)
                                {
                                    j2 = Block.netherrack.blockID;
                                }

                                par3ArrayOfByte[l1] = (byte)j2;
                                l1 += c;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }
                }
            }
        }
    }

    /**
     * name based on ChunkProviderGenerate
     */
    public void replaceBlocksForBiome(int par1, int par2, byte par3ArrayOfByte[])
    {
        byte byte0 = 64;
        double d = 0.03125D;
        slowsandNoise = slowsandGravelNoiseGen.generateNoiseOctaves(slowsandNoise, par1 * 16, par2 * 16, 0, 16, 16, 1, d, d, 1.0D);
        gravelNoise = slowsandGravelNoiseGen.generateNoiseOctaves(gravelNoise, par1 * 16, 109, par2 * 16, 16, 1, 16, d, 1.0D, d);
        netherrackExclusivityNoise = netherrackExculsivityNoiseGen.generateNoiseOctaves(netherrackExclusivityNoise, par1 * 16, par2 * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                boolean flag = slowsandNoise[i + j * 16] + hellRNG.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = gravelNoise[i + j * 16] + hellRNG.nextDouble() * 0.20000000000000001D > 0.0D;
                int k = (int)(netherrackExclusivityNoise[i + j * 16] / 3D + 3D + hellRNG.nextDouble() * 0.25D);
                int l = -1;
                byte byte1 = (byte)Block.netherrack.blockID;
                byte byte2 = (byte)Block.netherrack.blockID;

                for (int i1 = 127; i1 >= 0; i1--)
                {
                    int j1 = (j * 16 + i) * 128 + i1;

                    if (i1 >= 127 - hellRNG.nextInt(5) || i1 <= 0 + hellRNG.nextInt(5))
                    {
                        par3ArrayOfByte[j1] = (byte)Block.bedrock.blockID;
                        continue;
                    }

                    byte byte3 = par3ArrayOfByte[j1];

                    if (byte3 == 0)
                    {
                        l = -1;
                        continue;
                    }

                    if (byte3 != Block.netherrack.blockID)
                    {
                        continue;
                    }

                    if (l == -1)
                    {
                        if (k <= 0)
                        {
                            byte1 = 0;
                            byte2 = (byte)Block.netherrack.blockID;
                        }
                        else if (i1 >= byte0 - 4 && i1 <= byte0 + 1)
                        {
                            byte1 = (byte)Block.netherrack.blockID;
                            byte2 = (byte)Block.netherrack.blockID;

                            if (flag1)
                            {
                                byte1 = (byte)Block.gravel.blockID;
                            }

                            if (flag1)
                            {
                                byte2 = (byte)Block.netherrack.blockID;
                            }

                            if (flag)
                            {
                                byte1 = (byte)Block.slowSand.blockID;
                            }

                            if (flag)
                            {
                                byte2 = (byte)Block.slowSand.blockID;
                            }
                        }

                        if (i1 < byte0 && byte1 == 0)
                        {
                            byte1 = (byte)Block.lavaStill.blockID;
                        }

                        l = k;

                        if (i1 >= byte0 - 1)
                        {
                            par3ArrayOfByte[j1] = byte1;
                        }
                        else
                        {
                            par3ArrayOfByte[j1] = byte2;
                        }

                        continue;
                    }

                    if (l > 0)
                    {
                        l--;
                        par3ArrayOfByte[j1] = byte2;
                    }
                }
            }
        }
    }

    /**
     * loads or generates the chunk at the chunk location specified
     */
    public Chunk loadChunk(int par1, int par2)
    {
        return provideChunk(par1, par2);
    }

    /**
     * Will return back a chunk, if it doesn't exist and its not a MP client it will generates all the blocks for the
     * specified chunk from the map seed and chunk seed
     */
    public Chunk provideChunk(int par1, int par2)
    {
        hellRNG.setSeed((long)par1 * 0x4f9939f508L + (long)par2 * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        generateNetherTerrain(par1, par2, abyte0);
        replaceBlocksForBiome(par1, par2, abyte0);
        if (ODNBXlite.Generator < ODNBXlite.GEN_NEWBIOMES || ODNBXlite.MapFeatures < ODNBXlite.FEATURES_15){
            netherCaveGeneratorOld.generate(this, worldObj, par1, par2, abyte0);
        }else{
            netherCaveGenerator.generate(this, worldObj, par1, par2, abyte0);
        }
        if(generateStructures){
            genNetherBridge.generate(this, worldObj, par1, par2, abyte0);
        }
        Chunk chunk = new Chunk(worldObj, abyte0, par1, par2);
        BiomeGenBase abiomegenbase[] = worldObj.getWorldChunkManager().loadBlockGeneratorData(null, par1 * 16, par2 * 16, 16, 16);
        byte abyte1[] = chunk.getBiomeArray();

        for (int i = 0; i < abyte1.length; i++)
        {
            abyte1[i] = (byte)abiomegenbase[i].biomeID;
        }

        chunk.resetRelightChecks();
        return chunk;
    }

    private double[] func_4057_a(double ad[], int i, int j, int k, int l, int i1, int j1)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 2053.2359999999999D;
        noiseData4 = field_4177_a.generateNoiseOctaves(noiseData4, i, j, k, l, 1, j1, 1.0D, 0.0D, 1.0D);
        noiseData5 = field_4176_b.generateNoiseOctaves(noiseData5, i, j, k, l, 1, j1, 100D, 0.0D, 100D);
        noiseData1 = field_4167_k.generateNoiseOctaves(noiseData1, i, j, k, l, i1, j1, d / 80D, d1 / 60D, d / 80D);
        noiseData2 = field_4169_i.generateNoiseOctaves(noiseData2, i, j, k, l, i1, j1, d, d1, d);
        noiseData3 = field_4168_j.generateNoiseOctaves(noiseData3, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        double ad1[] = new double[i1];
        for(int i2 = 0; i2 < i1; i2++)
        {
            ad1[i2] = Math.cos(((double)i2 * 3.1415926535897931D * 6D) / (double)i1) * 2D;
            double d2 = i2;
            if(i2 > i1 / 2)
            {
                d2 = i1 - 1 - i2;
            }
            if(d2 < 4D)
            {
                d2 = 4D - d2;
                ad1[i2] -= d2 * d2 * d2 * 10D;
            }
        }

        for(int j2 = 0; j2 < l; j2++)
        {
            for(int k2 = 0; k2 < j1; k2++)
            {
                double d3 = (noiseData4[l1] + 256D) / 512D;
                if(d3 > 1.0D)
                {
                    d3 = 1.0D;
                }
                double d4 = 0.0D;
                double d5 = noiseData5[l1] / 8000D;
                if(d5 < 0.0D)
                {
                    d5 = -d5;
                }
                d5 = d5 * 3D - 3D;
                if(d5 < 0.0D)
                {
                    d5 /= 2D;
                    if(d5 < -1D)
                    {
                        d5 = -1D;
                    }
                    d5 /= 1.3999999999999999D;
                    d5 /= 2D;
                    d3 = 0.0D;
                } else
                {
                    if(d5 > 1.0D)
                    {
                        d5 = 1.0D;
                    }
                    d5 /= 6D;
                }
                d3 += 0.5D;
                d5 = (d5 * (double)i1) / 16D;
                l1++;
                for(int l2 = 0; l2 < i1; l2++)
                {
                    double d6 = 0.0D;
                    double d7 = ad1[l2];
                    double d8 = noiseData2[k1] / 512D;
                    double d9 = noiseData3[k1] / 512D;
                    double d10 = (noiseData1[k1] / 10D + 1.0D) / 2D;
                    if(d10 < 0.0D)
                    {
                        d6 = d8;
                    } else
                    if(d10 > 1.0D)
                    {
                        d6 = d9;
                    } else
                    {
                        d6 = d8 + (d9 - d8) * d10;
                    }
                    d6 -= d7;
                    if(l2 > i1 - 4)
                    {
                        double d11 = (float)(l2 - (i1 - 4)) / 3F;
                        d6 = d6 * (1.0D - d11) + -10D * d11;
                    }
                    if((double)l2 < d4)
                    {
                        double d12 = (d4 - (double)l2) / 4D;
                        if(d12 < 0.0D)
                        {
                            d12 = 0.0D;
                        }
                        if(d12 > 1.0D)
                        {
                            d12 = 1.0D;
                        }
                        d6 = d6 * (1.0D - d12) + -10D * d12;
                    }
                    ad[k1] = d6;
                    k1++;
                }

            }

        }

        return ad;
    }

    /**
     * generates a subset of the level's terrain data. Takes 7 arguments: the [empty] noise array, the position, and the
     * size.
     */
    private double[] initializeNoiseField(double par1ArrayOfDouble[], int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (par1ArrayOfDouble == null)
        {
            par1ArrayOfDouble = new double[par5 * par6 * par7];
        }

        double d = 684.41200000000003D;
        double d1 = 2053.2359999999999D;
        noiseData4 = netherNoiseGen6.generateNoiseOctaves(noiseData4, par2, par3, par4, par5, 1, par7, 1.0D, 0.0D, 1.0D);
        noiseData5 = netherNoiseGen7.generateNoiseOctaves(noiseData5, par2, par3, par4, par5, 1, par7, 100D, 0.0D, 100D);
        noiseData1 = netherNoiseGen3.generateNoiseOctaves(noiseData1, par2, par3, par4, par5, par6, par7, d / 80D, d1 / 60D, d / 80D);
        noiseData2 = netherNoiseGen1.generateNoiseOctaves(noiseData2, par2, par3, par4, par5, par6, par7, d, d1, d);
        noiseData3 = netherNoiseGen2.generateNoiseOctaves(noiseData3, par2, par3, par4, par5, par6, par7, d, d1, d);
        int i = 0;
        int j = 0;
        double ad[] = new double[par6];

        for (int k = 0; k < par6; k++)
        {
            ad[k] = Math.cos(((double)k * Math.PI * 6D) / (double)par6) * 2D;
            double d2 = k;

            if (k > par6 / 2)
            {
                d2 = par6 - 1 - k;
            }

            if (d2 < 4D)
            {
                d2 = 4D - d2;
                ad[k] -= d2 * d2 * d2 * 10D;
            }
        }

        for (int l = 0; l < par5; l++)
        {
            for (int i1 = 0; i1 < par7; i1++)
            {
                double d3 = (noiseData4[j] + 256D) / 512D;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                double d4 = 0.0D;
                double d5 = noiseData5[j] / 8000D;

                if (d5 < 0.0D)
                {
                    d5 = -d5;
                }

                d5 = d5 * 3D - 3D;

                if (d5 < 0.0D)
                {
                    d5 /= 2D;

                    if (d5 < -1D)
                    {
                        d5 = -1D;
                    }

                    d5 /= 1.3999999999999999D;
                    d5 /= 2D;
                    d3 = 0.0D;
                }
                else
                {
                    if (d5 > 1.0D)
                    {
                        d5 = 1.0D;
                    }

                    d5 /= 6D;
                }

                d3 += 0.5D;
                d5 = (d5 * (double)par6) / 16D;
                j++;

                for (int j1 = 0; j1 < par6; j1++)
                {
                    double d6 = 0.0D;
                    double d7 = ad[j1];
                    double d8 = noiseData2[i] / 512D;
                    double d9 = noiseData3[i] / 512D;
                    double d10 = (noiseData1[i] / 10D + 1.0D) / 2D;

                    if (d10 < 0.0D)
                    {
                        d6 = d8;
                    }
                    else if (d10 > 1.0D)
                    {
                        d6 = d9;
                    }
                    else
                    {
                        d6 = d8 + (d9 - d8) * d10;
                    }

                    d6 -= d7;

                    if (j1 > par6 - 4)
                    {
                        double d11 = (float)(j1 - (par6 - 4)) / 3F;
                        d6 = d6 * (1.0D - d11) + -10D * d11;
                    }

                    if ((double)j1 < d4)
                    {
                        double d12 = (d4 - (double)j1) / 4D;

                        if (d12 < 0.0D)
                        {
                            d12 = 0.0D;
                        }

                        if (d12 > 1.0D)
                        {
                            d12 = 1.0D;
                        }

                        d6 = d6 * (1.0D - d12) + -10D * d12;
                    }

                    par1ArrayOfDouble[i] = d6;
                    i++;
                }
            }
        }

        return par1ArrayOfDouble;
    }

    /**
     * Checks to see if a chunk exists at x, y
     */
    public boolean chunkExists(int par1, int par2)
    {
        return true;
    }

    /**
     * Populates chunk with ores etc etc
     */
    public void populate(IChunkProvider par1IChunkProvider, int par2, int par3)
    {
        BlockSand.fallInstantly = true;
        int i = par2 * 16;
        int j = par3 * 16;
        if(generateStructures){
            genNetherBridge.generateStructuresInChunk(worldObj, hellRNG, par2, par3);
        }

        for (int k = 0; k < 8; k++)
        {
            int i1 = i + hellRNG.nextInt(16) + 8;
            int k2 = hellRNG.nextInt(120) + 4;
            int k4 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenHellLava(Block.lavaMoving.blockID, ODNBXlite.Generator < ODNBXlite.GEN_NEWBIOMES || ODNBXlite.MapFeatures < ODNBXlite.FEATURES_15)).generate(worldObj, hellRNG, i1, k2, k4);
        }

        int l = hellRNG.nextInt(hellRNG.nextInt(10) + 1) + 1;

        for (int j1 = 0; j1 < l; j1++)
        {
            int l2 = i + hellRNG.nextInt(16) + 8;
            int l4 = hellRNG.nextInt(120) + 4;
            int k6 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenFire()).generate(worldObj, hellRNG, l2, l4, k6);
        }

        l = hellRNG.nextInt(hellRNG.nextInt(10) + 1);

        for (int k1 = 0; k1 < l; k1++)
        {
            int i3 = i + hellRNG.nextInt(16) + 8;
            int i5 = hellRNG.nextInt(120) + 4;
            int l6 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone1()).generate(worldObj, hellRNG, i3, i5, l6);
        }

        for (int l1 = 0; l1 < 10; l1++)
        {
            int j3 = i + hellRNG.nextInt(16) + 8;
            int j5 = hellRNG.nextInt(128);
            int i7 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone2()).generate(worldObj, hellRNG, j3, j5, i7);
        }

        if (hellRNG.nextInt(1) == 0)
        {
            int i2 = i + hellRNG.nextInt(16) + 8;
            int k3 = hellRNG.nextInt(128);
            int k5 = j + hellRNG.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, hellRNG, i2, k3, k5);
        }

        if (hellRNG.nextInt(1) == 0)
        {
            int j2 = i + hellRNG.nextInt(16) + 8;
            int l3 = hellRNG.nextInt(128);
            int l5 = j + hellRNG.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, hellRNG, j2, l3, l5);
        }

        if ((ODNBXlite.Generator == ODNBXlite.GEN_NEWBIOMES && ODNBXlite.MapFeatures >= ODNBXlite.FEATURES_15) || ODNBXlite.GenerateNewOres){
            WorldGenMinable worldgenminable = new WorldGenMinable(Block.oreNetherQuartz.blockID, 13, Block.netherrack.blockID);

            for (int i4 = 0; i4 < 16; i4++)
            {
                int i6 = i + hellRNG.nextInt(16);
                int j7 = hellRNG.nextInt(108) + 10;
                int l7 = j + hellRNG.nextInt(16);
                worldgenminable.generate(worldObj, hellRNG, i6, j7, l7);
            }

            for (int j4 = 0; j4 < 16; j4++)
            {
                int j6 = i + hellRNG.nextInt(16);
                int k7 = hellRNG.nextInt(108) + 10;
                int i8 = j + hellRNG.nextInt(16);
                (new WorldGenHellLava(Block.lavaMoving.blockID, true)).generate(worldObj, hellRNG, j6, k7, i8);
            }
        }

        BlockSand.fallInstantly = false;
    }

    /**
     * Two modes of operation: if passed true, save all Chunks in one go.  If passed false, save up to two chunks.
     * Return true if all chunks have been saved.
     */
    public boolean saveChunks(boolean par1, IProgressUpdate par2IProgressUpdate)
    {
        return true;
    }

    public void func_104112_b()
    {
    }

    /**
     * Unloads chunks that are marked to be unloaded. This is not guaranteed to unload every such chunk.
     */
    public boolean unloadQueuedChunks()
    {
        return false;
    }

    /**
     * Returns if the IChunkProvider supports saving.
     */
    public boolean canSave()
    {
        return true;
    }

    /**
     * Converts the instance data to a readable string.
     */
    public String makeString()
    {
        return "HellRandomLevelSource";
    }

    /**
     * Returns a list of creatures of the specified type that can spawn at the given location.
     */
    public List getPossibleCreatures(EnumCreatureType par1EnumCreatureType, int par2, int par3, int par4)
    {
        if (par1EnumCreatureType == EnumCreatureType.monster && genNetherBridge.hasStructureAt(par2, par3, par4))
        {
            return genNetherBridge.getSpawnList();
        }

        BiomeGenBase biomegenbase = worldObj.getBiomeGenForCoords(par2, par4);

        if (biomegenbase == null)
        {
            return null;
        }
        else
        {
            return biomegenbase.getSpawnableList(par1EnumCreatureType);
        }
    }

    /**
     * Returns the location of the closest structure of the specified type. If not found returns null.
     */
    public ChunkPosition findClosestStructure(World par1World, String par2Str, int par3, int i, int j)
    {
        return null;
    }

    public int getLoadedChunkCount()
    {
        return 0;
    }

    public void recreateStructures(int par1, int par2)
    {
        genNetherBridge.generate(this, worldObj, par1, par2, null);
    }
}
