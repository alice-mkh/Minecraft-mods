package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderHell implements IChunkProvider
{
    private Random hellRNG;
    private NoiseGeneratorOctaves field_4169_i;
    private NoiseGeneratorOctaves field_4168_j;
    private NoiseGeneratorOctaves field_4167_k;
    private NoiseGeneratorOctaves field_4166_l;
    private NoiseGeneratorOctaves field_4165_m;
    public NoiseGeneratorOctaves field_4177_a;
    public NoiseGeneratorOctaves field_4176_b;

    /** Is the world that the nether is getting generated. */
    private World worldObj;
    private double field_4163_o[];
    public MapGenNetherBridge genNetherBridge;
    private double field_4162_p[];
    private double gravelNoise[];
    private double field_4160_r[];
    private MapGenBase netherCaveGenerator;
    double field_4175_c[];
    double field_4174_d[];
    double field_4173_e[];
    double field_4172_f[];
    double field_4171_g[];
    private boolean generateStructures;

    public ChunkProviderHell(World par1World, long par2)
    {
        genNetherBridge = new MapGenNetherBridge();
        field_4162_p = new double[256];
        gravelNoise = new double[256];
        field_4160_r = new double[256];
        netherCaveGenerator = new MapGenCavesHell();
        worldObj = par1World;
        hellRNG = new Random(par2);
        field_4169_i = new NoiseGeneratorOctaves(hellRNG, 16);
        field_4168_j = new NoiseGeneratorOctaves(hellRNG, 16);
        field_4167_k = new NoiseGeneratorOctaves(hellRNG, 8);
        field_4166_l = new NoiseGeneratorOctaves(hellRNG, 4);
        field_4165_m = new NoiseGeneratorOctaves(hellRNG, 4);
        field_4177_a = new NoiseGeneratorOctaves(hellRNG, 10);
        field_4176_b = new NoiseGeneratorOctaves(hellRNG, 16);
        generateStructures = par1World.getWorldInfo().isMapFeaturesEnabled() || (mod_noBiomesX.Generator==2 && mod_noBiomesX.MapFeatures!=0);
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
        field_4163_o = func_4057_a(field_4163_o, par1 * byte0, 0, par2 * byte0, i, byte2, j);

        for (int k = 0; k < byte0; k++)
        {
            for (int l = 0; l < byte0; l++)
            {
                for (int i1 = 0; i1 < 16; i1++)
                {
                    double d = 0.125D;
                    double d1 = field_4163_o[((k + 0) * j + (l + 0)) * byte2 + (i1 + 0)];
                    double d2 = field_4163_o[((k + 0) * j + (l + 1)) * byte2 + (i1 + 0)];
                    double d3 = field_4163_o[((k + 1) * j + (l + 0)) * byte2 + (i1 + 0)];
                    double d4 = field_4163_o[((k + 1) * j + (l + 1)) * byte2 + (i1 + 0)];
                    double d5 = (field_4163_o[((k + 0) * j + (l + 0)) * byte2 + (i1 + 1)] - d1) * d;
                    double d6 = (field_4163_o[((k + 0) * j + (l + 1)) * byte2 + (i1 + 1)] - d2) * d;
                    double d7 = (field_4163_o[((k + 1) * j + (l + 0)) * byte2 + (i1 + 1)] - d3) * d;
                    double d8 = (field_4163_o[((k + 1) * j + (l + 1)) * byte2 + (i1 + 1)] - d4) * d;

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

    public void func_4058_b(int par1, int par2, byte par3ArrayOfByte[])
    {
        byte byte0 = 64;
        double d = 0.03125D;
        field_4162_p = field_4166_l.generateNoiseOctaves(field_4162_p, par1 * 16, par2 * 16, 0, 16, 16, 1, d, d, 1.0D);
        gravelNoise = field_4166_l.generateNoiseOctaves(gravelNoise, par1 * 16, 109, par2 * 16, 16, 1, 16, d, 1.0D, d);
        field_4160_r = field_4165_m.generateNoiseOctaves(field_4160_r, par1 * 16, par2 * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);

        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                boolean flag = field_4162_p[i + j * 16] + hellRNG.nextDouble() * 0.2D > 0.0D;
                boolean flag1 = gravelNoise[i + j * 16] + hellRNG.nextDouble() * 0.2D > 0.0D;
                int k = (int)(field_4160_r[i + j * 16] / 3D + 3D + hellRNG.nextDouble() * 0.25D);
                int l = -1;
                byte byte1 = (byte)Block.netherrack.blockID;
                byte byte2 = (byte)Block.netherrack.blockID;

                for (int i1 = 127; i1 >= 0; i1--)
                {
                    int j1 = (j * 16 + i) * 128 + i1;

                    if (i1 >= 127 - hellRNG.nextInt(5))
                    {
                        par3ArrayOfByte[j1] = (byte)Block.bedrock.blockID;
                        continue;
                    }

                    if (i1 <= 0 + hellRNG.nextInt(5))
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
     * Creates an empty chunk ready to put data from the server in
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
        func_4058_b(par1, par2, abyte0);
        netherCaveGenerator.generate(this, worldObj, par1, par2, abyte0);
        if(generateStructures){
            genNetherBridge.generate(this, worldObj, par1, par2, abyte0);
        }
        Chunk chunk = new Chunk(worldObj, abyte0, par1, par2);
        chunk.func_48496_n();
        return chunk;
    }

    private double[] func_4057_a(double par1ArrayOfDouble[], int par2, int par3, int par4, int par5, int par6, int par7)
    {
        if (par1ArrayOfDouble == null)
        {
            par1ArrayOfDouble = new double[par5 * par6 * par7];
        }

        double d = 684.412D;
        double d1 = 2053.236D;
        field_4172_f = field_4177_a.generateNoiseOctaves(field_4172_f, par2, par3, par4, par5, 1, par7, 1.0D, 0.0D, 1.0D);
        field_4171_g = field_4176_b.generateNoiseOctaves(field_4171_g, par2, par3, par4, par5, 1, par7, 100D, 0.0D, 100D);
        field_4175_c = field_4167_k.generateNoiseOctaves(field_4175_c, par2, par3, par4, par5, par6, par7, d / 80D, d1 / 60D, d / 80D);
        field_4174_d = field_4169_i.generateNoiseOctaves(field_4174_d, par2, par3, par4, par5, par6, par7, d, d1, d);
        field_4173_e = field_4168_j.generateNoiseOctaves(field_4173_e, par2, par3, par4, par5, par6, par7, d, d1, d);
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
                double d3 = (field_4172_f[j] + 256D) / 512D;

                if (d3 > 1.0D)
                {
                    d3 = 1.0D;
                }

                double d4 = 0.0D;
                double d5 = field_4171_g[j] / 8000D;

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

                    d5 /= 1.4D;
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
                    double d8 = field_4174_d[i] / 512D;
                    double d9 = field_4173_e[i] / 512D;
                    double d10 = (field_4175_c[i] / 10D + 1.0D) / 2D;

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
        genNetherBridge.generateStructuresInChunk(worldObj, hellRNG, par2, par3);

        for (int k = 0; k < 8; k++)
        {
            int i1 = i + hellRNG.nextInt(16) + 8;
            int k2 = hellRNG.nextInt(120) + 4;
            int i4 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenHellLava(Block.lavaMoving.blockID)).generate(worldObj, hellRNG, i1, k2, i4);
        }

        int l = hellRNG.nextInt(hellRNG.nextInt(10) + 1) + 1;

        for (int j1 = 0; j1 < l; j1++)
        {
            int l2 = i + hellRNG.nextInt(16) + 8;
            int j4 = hellRNG.nextInt(120) + 4;
            int k5 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenFire()).generate(worldObj, hellRNG, l2, j4, k5);
        }

        l = hellRNG.nextInt(hellRNG.nextInt(10) + 1);

        for (int k1 = 0; k1 < l; k1++)
        {
            int i3 = i + hellRNG.nextInt(16) + 8;
            int k4 = hellRNG.nextInt(120) + 4;
            int l5 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone1()).generate(worldObj, hellRNG, i3, k4, l5);
        }

        for (int l1 = 0; l1 < 10; l1++)
        {
            int j3 = i + hellRNG.nextInt(16) + 8;
            int l4 = hellRNG.nextInt(128);
            int i6 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenGlowStone2()).generate(worldObj, hellRNG, j3, l4, i6);
        }

        if (hellRNG.nextInt(1) == 0)
        {
            int i2 = i + hellRNG.nextInt(16) + 8;
            int k3 = hellRNG.nextInt(128);
            int i5 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, hellRNG, i2, k3, i5);
        }

        if (hellRNG.nextInt(1) == 0)
        {
            int j2 = i + hellRNG.nextInt(16) + 8;
            int l3 = hellRNG.nextInt(128);
            int j5 = j + hellRNG.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, hellRNG, j2, l3, j5);
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

    /**
     * Unloads the 100 oldest chunks from memory, due to a bug with chunkSet.add() never being called it thinks the list
     * is always empty and will not remove any chunks.
     */
    public boolean unload100OldestChunks()
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
        if (par1EnumCreatureType == EnumCreatureType.monster && genNetherBridge.func_40483_a(par2, par3, par4))
        {
            return genNetherBridge.getSpawnList();
        }

        BiomeGenBase biomegenbase = worldObj.func_48454_a(par2, par4);

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
}
