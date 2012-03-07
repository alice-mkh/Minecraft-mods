package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.BetaNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.oldbiomes.*;
import net.minecraft.src.nbxlite.mapgens.MapGenSkyStronghold;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.OldMapGenBase;
import net.minecraft.src.nbxlite.mapgens.OldMapGenCaves;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenClay;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenMinable;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenReed;
import net.minecraft.src.nbxlite.mapgens.SuperOldWorldGenMinable;

public class ChunkProviderGenerateBeta
    implements IChunkProvider
{
    private Random rand;
    private BetaNoiseGeneratorOctaves noiseGen1;
    private BetaNoiseGeneratorOctaves noiseGen2;
    private BetaNoiseGeneratorOctaves noiseGen3;
    private BetaNoiseGeneratorOctaves field_909_n;
    private BetaNoiseGeneratorOctaves noiseGen4;
    public BetaNoiseGeneratorOctaves noiseGen5;
    public BetaNoiseGeneratorOctaves noiseGen6;
    public BetaNoiseGeneratorOctaves mobSpawnerNoise;
    private World worldObj;
    private final boolean mapFeaturesEnabled;
    private double field_4180_q[];
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];
    private OldMapGenBase caveGenerator;
    public MapGenStronghold2 strongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;
    private MapGenBase ravineGenerator;
    private OldBiomeGenBase biomesForGeneration[];
    double noise3[];
    double noise1[];
    double noise2[];
    double noise5[];
    double noise6[];
    int unusedIntArray32x32[][];
    private double generatedTemperatures[];

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        return null;
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
    {
        return null;
    }

    public ChunkProviderGenerateBeta(World world, long l, Boolean boolean1)
    {
        sandNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        caveGenerator = new OldMapGenCaves();
        if(boolean1)
        {
            ravineGenerator = new MapGenRavine();
            strongholdGenerator = new MapGenSkyStronghold();
            villageGenerator = new MapGenVillage(0);
            mineshaftGenerator = new MapGenMineshaft();
        }
        unusedIntArray32x32 = new int[32][32];
        worldObj = world;
        mapFeaturesEnabled = boolean1;
        rand = new Random(l);
        noiseGen1 = new BetaNoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new BetaNoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new BetaNoiseGeneratorOctaves(rand, 8);
        field_909_n = new BetaNoiseGeneratorOctaves(rand, 4);
        noiseGen4 = new BetaNoiseGeneratorOctaves(rand, 4);
        noiseGen5 = new BetaNoiseGeneratorOctaves(rand, 10);
        noiseGen6 = new BetaNoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new BetaNoiseGeneratorOctaves(rand, 8);
    }

    public void generateTerrain(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[], double ad[])
    {
        byte byte0 = 4;
        byte byte1 = 64;
        int k = byte0 + 1;
        byte byte2 = 17;
        int l = byte0 + 1;
        field_4180_q = initializeNoiseField(field_4180_q, i * byte0, 0, j * byte0, k, byte2, l);
        for(int i1 = 0; i1 < byte0; i1++)
        {
            for(int j1 = 0; j1 < byte0; j1++)
            {
                for(int k1 = 0; k1 < 16; k1++)
                {
                    double d = 0.125D;
                    double d1 = field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d2 = field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d3 = field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 0)];
                    double d4 = field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 0)];
                    double d5 = (field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte2 + (k1 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte2 + (k1 + 1)] - d4) * d;
                    for(int l1 = 0; l1 < 8; l1++)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int i2 = 0; i2 < 4; i2++)
                        {
                            int j2 = i2 + i1 * 4 << 11 | 0 + j1 * 4 << 7 | k1 * 8 + l1;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int k2 = 0; k2 < 4; k2++)
                            {
                                double d17 = ad[(i1 * 4 + i2) * 16 + (j1 * 4 + k2)];
                                int l2 = 0;
                                if(k1 * 8 + l1 < byte1)
                                {
                                    if(d17 < 0.5D && k1 * 8 + l1 >= byte1 - 1)
                                    {
                                        l2 = Block.ice.blockID;
                                    } else
                                    {
                                        l2 = Block.waterStill.blockID;
                                    }
                                }
                                if(d15 > 0.0D)
                                {
                                    l2 = Block.stone.blockID;
                                }
                                abyte0[j2] = (byte)l2;
                                j2 += c;
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

    public void replaceBlocksForBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[])
    {
        byte byte0 = 64;
        double d = 0.03125D;
        sandNoise = field_909_n.generateNoiseOctaves(sandNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        gravelNoise = field_909_n.generateNoiseOctaves(gravelNoise, i * 16, 109.0134D, j * 16, 16, 1, 16, d, 1.0D, d);
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                OldBiomeGenBase oldbiomegenbase = aoldbiomegenbase[k + l * 16];
                boolean flag = sandNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                boolean flag1 = gravelNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int i1 = (int)(stoneNoise[k + l * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte1 = oldbiomegenbase.topBlock;
                byte byte2 = oldbiomegenbase.fillerBlock;
                for(int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    if(k1 <= 0 + rand.nextInt(5))
                    {
                        abyte0[l1] = (byte)Block.bedrock.blockID;
                        continue;
                    }
                    byte byte3 = abyte0[l1];
                    if(byte3 == 0)
                    {
                        j1 = -1;
                        continue;
                    }
                    if(byte3 != Block.stone.blockID)
                    {
                        continue;
                    }
                    if(j1 == -1)
                    {
                        if(i1 <= 0)
                        {
                            byte1 = 0;
                            byte2 = (byte)Block.stone.blockID;
                        } else
                        if(k1 >= byte0 - 4 && k1 <= byte0 + 1)
                        {
                            byte1 = oldbiomegenbase.topBlock;
                            byte2 = oldbiomegenbase.fillerBlock;
                            if(flag1)
                            {
                                byte1 = 0;
                            }
                            if(flag1)
                            {
                                byte2 = (byte)Block.gravel.blockID;
                            }
                            if(flag)
                            {
                                byte1 = (byte)Block.sand.blockID;
                            }
                            if(flag)
                            {
                                byte2 = (byte)Block.sand.blockID;
                            }
                        }
                        if(k1 < byte0 && byte1 == 0)
                        {
                            byte1 = (byte)Block.waterStill.blockID;
                        }
                        j1 = i1;
                        if(k1 >= byte0 - 1)
                        {
                            abyte0[l1] = byte1;
                        } else
                        {
                            abyte0[l1] = byte2;
                        }
                        continue;
                    }
                    if(j1 <= 0)
                    {
                        continue;
                    }
                    j1--;
                    abyte0[l1] = byte2;
                    if(j1 != 0 || byte2 != Block.sand.blockID)
                    {
                        continue;
                    }
                    j1 = rand.nextInt(4);
                    if(mod_noBiomesX.MapFeatures>=2)
                    {
                        byte2 = (byte)Block.sandStone.blockID;
                    }
                }

            }

        }

    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        rand.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        biomesForGeneration = worldObj.getWorldChunkManager().oldLoadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
        double ad[] = worldObj.getWorldChunkManager().temperature;
        generateTerrain(i, j, abyte0, biomesForGeneration, ad);
        replaceBlocksForBiome(i, j, abyte0, biomesForGeneration);
        caveGenerator.generate(this, worldObj, i, j, abyte0);
        if(mapFeaturesEnabled)
        {
            ravineGenerator.generate(this, worldObj, i, j, abyte0);
            mineshaftGenerator.generate(this, worldObj, i, j, abyte0);
            villageGenerator.generate(this, worldObj, i, j, abyte0);
            strongholdGenerator.generate(this, worldObj, i, j, abyte0);
        }
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeNoiseField(double ad[], int i, int j, int k, int l, int i1, int j1)
    {
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double ad1[] = worldObj.getWorldChunkManager().temperature;
        double ad2[] = worldObj.getWorldChunkManager().humidity;
        noise5 = noiseGen5.func_4109_a(noise5, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        noise6 = noiseGen6.func_4109_a(noise6, i, k, l, j1, 200D, 200D, 0.5D);
        noise3 = noiseGen3.generateNoiseOctaves(noise3, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
        noise1 = noiseGen1.generateNoiseOctaves(noise1, i, j, k, l, i1, j1, d, d1, d);
        noise2 = noiseGen2.generateNoiseOctaves(noise2, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / l;
        for(int j2 = 0; j2 < l; j2++)
        {
            int k2 = j2 * i2 + i2 / 2;
            for(int l2 = 0; l2 < j1; l2++)
            {
                int i3 = l2 * i2 + i2 / 2;
                double d2 = ad1[k2 * 16 + i3];
                double d3 = ad2[k2 * 16 + i3] * d2;
                double d4 = 1.0D - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (noise5[l1] + 256D) / 512D;
                d5 *= d4;
                if(d5 > 1.0D)
                {
                    d5 = 1.0D;
                }
                double d6 = noise6[l1] / 8000D;
                if(d6 < 0.0D)
                {
                    d6 = -d6 * 0.29999999999999999D;
                }
                d6 = d6 * 3D - 2D;
                if(d6 < 0.0D)
                {
                    d6 /= 2D;
                    if(d6 < -1D)
                    {
                        d6 = -1D;
                    }
                    d6 /= 1.3999999999999999D;
                    d6 /= 2D;
                    d5 = 0.0D;
                } else
                {
                    if(d6 > 1.0D)
                    {
                        d6 = 1.0D;
                    }
                    d6 /= 8D;
                }
                if(d5 < 0.0D)
                {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double)i1) / 16D;
                double d7 = (double)i1 / 2D + d6 * 4D;
                l1++;
                for(int j3 = 0; j3 < i1; j3++)
                {
                    double d8 = 0.0D;
                    double d9 = (((double)j3 - d7) * 12D) / d5;
                    if(d9 < 0.0D)
                    {
                        d9 *= 4D;
                    }
                    double d10 = noise1[k1] / 512D;
                    double d11 = noise2[k1] / 512D;
                    double d12 = (noise3[k1] / 10D + 1.0D) / 2D;
                    if(d12 < 0.0D)
                    {
                        d8 = d10;
                    } else
                    if(d12 > 1.0D)
                    {
                        d8 = d11;
                    } else
                    {
                        d8 = d10 + (d11 - d10) * d12;
                    }
                    d8 -= d9;
                    if(j3 > i1 - 4)
                    {
                        double d13 = (float)(j3 - (i1 - 4)) / 3F;
                        d8 = d8 * (1.0D - d13) + -10D * d13;
                    }
                    ad[k1] = d8;
                    k1++;
                }

            }

        }

        return ad;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        int k = mod_noBiomesX.MapFeatures;
        if(k == 0)
        {
            populate_halloween(ichunkprovider, i, j);
            if (mod_noBiomesX.UseNewSpawning || mod_noBiomesX.MobSpawning==2){
                BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt((i * 16) + 16, (j * 16) + 16);
                SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, (i * 16) + 8, (j * 16) + 8, 16, 16, rand);
            }
            return;
        }
        if(k == 1)
        {
            populate_12(ichunkprovider, i, j);
            if (mod_noBiomesX.UseNewSpawning || mod_noBiomesX.MobSpawning==2){
                BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt((i * 16) + 16, (j * 16) + 16);
                SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, (i * 16) + 8, (j * 16) + 8, 16, 16, rand);
            }
            return;
        }
        if(k == 2 || k == 3)
        {
            populate_14(ichunkprovider, i, j);
            if (mod_noBiomesX.UseNewSpawning || mod_noBiomesX.MobSpawning==2){
                BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt((i * 16) + 16, (j * 16) + 16);
                SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, (i * 16) + 8, (j * 16) + 8, 16, 16, rand);
            }
            return;
        }
        BlockSand.fallInstantly = true;
        int l = i * 16;
        int i1 = j * 16;
        OldBiomeGenBase oldbiomegenbase = worldObj.getWorldChunkManager().oldGetBiomeGenAt(l + 16, i1 + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        boolean flag = false;
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            flag = villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }
        double d = 0.25D;
        if(!flag && rand.nextInt(4) == 0)
        {
            int j1 = l + rand.nextInt(16) + 8;
            int i5 = rand.nextInt(128);
            int j8 = i1 + rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, j1, i5, j8);
        }
        if(!flag && rand.nextInt(8) == 0)
        {
            int k1 = l + rand.nextInt(16) + 8;
            int j5 = rand.nextInt(rand.nextInt(120) + 8);
            int k8 = i1 + rand.nextInt(16) + 8;
            if(j5 < 64 || rand.nextInt(10) == 0)
            {
                (new WorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, k1, j5, k8);
            }
        }
        for(int i2 = 0; i2 < 8; i2++)
        {
            int k5 = l + rand.nextInt(16) + 8;
            int l8 = rand.nextInt(128);
            int k11 = i1 + rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, rand, k5, l8, k11);
        }

        for(int j2 = 0; j2 < 10; j2++)
        {
            int l5 = l + rand.nextInt(16);
            int i9 = rand.nextInt(128);
            int l11 = i1 + rand.nextInt(16);
            (new OldWorldGenClay(32)).generate(worldObj, rand, l5, i9, l11);
        }

        for(int k2 = 0; k2 < 20; k2++)
        {
            int i6 = l + rand.nextInt(16);
            int j9 = rand.nextInt(128);
            int i12 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, i6, j9, i12);
        }

        for(int i3 = 0; i3 < 10; i3++)
        {
            int j6 = l + rand.nextInt(16);
            int k9 = rand.nextInt(128);
            int j12 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.gravel.blockID, 32)).generate(worldObj, rand, j6, k9, j12);
        }

        for(int j3 = 0; j3 < 20; j3++)
        {
            int k6 = l + rand.nextInt(16);
            int l9 = rand.nextInt(128);
            int k12 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreCoal.blockID, 16)).generate(worldObj, rand, k6, l9, k12);
        }

        for(int k3 = 0; k3 < 20; k3++)
        {
            int l6 = l + rand.nextInt(16);
            int i10 = rand.nextInt(64);
            int l12 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreIron.blockID, 8)).generate(worldObj, rand, l6, i10, l12);
        }

        for(int l3 = 0; l3 < 2; l3++)
        {
            int i7 = l + rand.nextInt(16);
            int j10 = rand.nextInt(32);
            int i13 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreGold.blockID, 8)).generate(worldObj, rand, i7, j10, i13);
        }

        for(int i4 = 0; i4 < 8; i4++)
        {
            int j7 = l + rand.nextInt(16);
            int k10 = rand.nextInt(16);
            int j13 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, j7, k10, j13);
        }

        for(int j4 = 0; j4 < 1; j4++)
        {
            int k7 = l + rand.nextInt(16);
            int l10 = rand.nextInt(16);
            int k13 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreDiamond.blockID, 7)).generate(worldObj, rand, k7, l10, k13);
        }

        for(int k4 = 0; k4 < 1; k4++)
        {
            int l7 = l + rand.nextInt(16);
            int i11 = rand.nextInt(16) + rand.nextInt(16);
            int l13 = i1 + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, l7, i11, l13);
        }

        d = 0.5D;
        int l4 = (int)((mobSpawnerNoise.func_806_a((double)l * d, (double)i1 * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        int i8 = 0;
        if(rand.nextInt(10) == 0)
        {
            i8++;
        }
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            i8 += l4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest)
        {
            i8 += l4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            i8 += l4 + 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            i8 += l4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            i8 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.tundra)
        {
            i8 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            i8 -= 20;
        }
        for(int j11 = 0; j11 < i8; j11++)
        {
            int i14 = l + rand.nextInt(16) + 8;
            int k14 = i1 + rand.nextInt(16) + 8;
            WorldGenerator worldgenerator = oldbiomegenbase.getRandomWorldGenForTrees(rand);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(worldObj, rand, i14, worldObj.getHeightValue(i14, k14), k14);
        }

        byte byte0 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            byte0 = 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            byte0 = 4;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            byte0 = 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            byte0 = 3;
        }
        for(int j14 = 0; j14 < byte0; j14++)
        {
            int l14 = l + rand.nextInt(16) + 8;
            int i17 = rand.nextInt(128);
            int l19 = i1 + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, l14, i17, l19);
        }

        byte byte1 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            byte1 = 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest)
        {
            byte1 = 10;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            byte1 = 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            byte1 = 1;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            byte1 = 10;
        }
        for(int i15 = 0; i15 < byte1; i15++)
        {
            byte byte2 = 1;
            if(oldbiomegenbase == OldBiomeGenBase.rainforest && rand.nextInt(3) != 0)
            {
                byte2 = 2;
            }
            int i20 = l + rand.nextInt(16) + 8;
            int l22 = rand.nextInt(128);
            int k24 = i1 + rand.nextInt(16) + 8;
            (new WorldGenTallGrass(Block.tallGrass.blockID, byte2)).generate(worldObj, rand, i20, l22, k24);
        }

        byte1 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            byte1 = 2;
        }
        for(int j15 = 0; j15 < byte1; j15++)
        {
            int j17 = l + rand.nextInt(16) + 8;
            int j20 = rand.nextInt(128);
            int i23 = i1 + rand.nextInt(16) + 8;
            (new WorldGenDeadBush(Block.deadBush.blockID)).generate(worldObj, rand, j17, j20, i23);
        }

        if(rand.nextInt(2) == 0)
        {
            int k15 = l + rand.nextInt(16) + 8;
            int k17 = rand.nextInt(128);
            int k20 = i1 + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, k15, k17, k20);
        }
        if(rand.nextInt(4) == 0)
        {
            int l15 = l + rand.nextInt(16) + 8;
            int l17 = rand.nextInt(128);
            int l20 = i1 + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, l15, l17, l20);
        }
        if(rand.nextInt(8) == 0)
        {
            int i16 = l + rand.nextInt(16) + 8;
            int i18 = rand.nextInt(128);
            int i21 = i1 + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, i16, i18, i21);
        }
        for(int j16 = 0; j16 < 10; j16++)
        {
            int j18 = l + rand.nextInt(16) + 8;
            int j21 = rand.nextInt(128);
            int j23 = i1 + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, j18, j21, j23);
        }

        if(rand.nextInt(32) == 0)
        {
            int k16 = l + rand.nextInt(16) + 8;
            int k18 = rand.nextInt(128);
            int k21 = i1 + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, k16, k18, k21);
        }
        int l16 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            l16 += 10;
        }
        for(int l18 = 0; l18 < l16; l18++)
        {
            int l21 = l + rand.nextInt(16) + 8;
            int k23 = rand.nextInt(128);
            int l24 = i1 + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, l21, k23, l24);
        }

        for(int i19 = 0; i19 < 50; i19++)
        {
            int i22 = l + rand.nextInt(16) + 8;
            int l23 = rand.nextInt(rand.nextInt(120) + 8);
            int i25 = i1 + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, i22, l23, i25);
        }

        for(int j19 = 0; j19 < 20; j19++)
        {
            int j22 = l + rand.nextInt(16) + 8;
            int i24 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int j25 = i1 + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, j22, i24, j25);
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures_old(generatedTemperatures, l + 8, i1 + 8, 16, 16);
        for(int k19 = l + 8; k19 < l + 8 + 16; k19++)
        {
            for(int k22 = i1 + 8; k22 < i1 + 8 + 16; k22++)
            {
                int j24 = k19 - (l + 8);
                int k25 = k22 - (i1 + 8);
                int l25 = worldObj.getTopSolidOrLiquidBlock(k19, k22);
                double d1 = generatedTemperatures[j24 * 16 + k25] - ((double)(l25 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && l25 > 0 && l25 < 128 && worldObj.isAirBlock(k19, l25, k22) && worldObj.getBlockMaterial(k19, l25 - 1, k22).isSolid() && worldObj.getBlockMaterial(k19, l25 - 1, k22) != Material.ice)
                {
                    worldObj.setBlockWithNotify(k19, l25, k22, Block.snow.blockID);
                }
            }

        }

        BlockSand.fallInstantly = false;
        if (mod_noBiomesX.UseNewSpawning || mod_noBiomesX.MobSpawning==2){
            BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(l + 16, i1 + 16);
            SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, l + 8, i1 + 8, 16, 16, rand);
        }
    }

    public void populate_14(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        OldBiomeGenBase oldbiomegenbase = worldObj.getWorldChunkManager().oldGetBiomeGenAt(k + 16, l + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        boolean flag = false;
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            flag = villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }
        double d = 0.25D;
        if(!flag && rand.nextInt(4) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int l4 = rand.nextInt(128);
            int i8 = l + rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i1, l4, i8);
        }
        if(!flag && rand.nextInt(8) == 0)
        {
            int j1 = k + rand.nextInt(16) + 8;
            int i5 = rand.nextInt(rand.nextInt(120) + 8);
            int j8 = l + rand.nextInt(16) + 8;
            if(i5 < 64 || rand.nextInt(10) == 0)
            {
                (new WorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, j1, i5, j8);
            }
        }
        for(int k1 = 0; k1 < 8; k1++)
        {
            int j5 = k + rand.nextInt(16) + 8;
            int k8 = rand.nextInt(128);
            int j11 = l + rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, rand, j5, k8, j11);
        }

        for(int i2 = 0; i2 < 10; i2++)
        {
            int k5 = k + rand.nextInt(16);
            int l8 = rand.nextInt(128);
            int k11 = l + rand.nextInt(16);
            (new OldWorldGenClay(32)).generate(worldObj, rand, k5, l8, k11);
        }

        for(int j2 = 0; j2 < 20; j2++)
        {
            int l5 = k + rand.nextInt(16);
            int i9 = rand.nextInt(128);
            int l11 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, l5, i9, l11);
        }

        for(int k2 = 0; k2 < 10; k2++)
        {
            int i6 = k + rand.nextInt(16);
            int j9 = rand.nextInt(128);
            int i12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.gravel.blockID, 32)).generate(worldObj, rand, i6, j9, i12);
        }

        for(int i3 = 0; i3 < 20; i3++)
        {
            int j6 = k + rand.nextInt(16);
            int k9 = rand.nextInt(128);
            int j12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreCoal.blockID, 16)).generate(worldObj, rand, j6, k9, j12);
        }

        for(int j3 = 0; j3 < 20; j3++)
        {
            int k6 = k + rand.nextInt(16);
            int l9 = rand.nextInt(64);
            int k12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreIron.blockID, 8)).generate(worldObj, rand, k6, l9, k12);
        }

        for(int k3 = 0; k3 < 2; k3++)
        {
            int l6 = k + rand.nextInt(16);
            int i10 = rand.nextInt(32);
            int l12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreGold.blockID, 8)).generate(worldObj, rand, l6, i10, l12);
        }

        for(int l3 = 0; l3 < 8; l3++)
        {
            int i7 = k + rand.nextInt(16);
            int j10 = rand.nextInt(16);
            int i13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, i7, j10, i13);
        }

        for(int i4 = 0; i4 < 1; i4++)
        {
            int j7 = k + rand.nextInt(16);
            int k10 = rand.nextInt(16);
            int j13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreDiamond.blockID, 7)).generate(worldObj, rand, j7, k10, j13);
        }

        for(int j4 = 0; j4 < 1; j4++)
        {
            int k7 = k + rand.nextInt(16);
            int l10 = rand.nextInt(16) + rand.nextInt(16);
            int k13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, k7, l10, k13);
        }

        d = 0.5D;
        int k4 = (int)((mobSpawnerNoise.func_806_a((double)k * d, (double)l * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        int l7 = 0;
        if(rand.nextInt(10) == 0)
        {
            l7++;
        }
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            l7 += k4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest)
        {
            l7 += k4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            l7 += k4 + 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            l7 += k4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            l7 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.tundra)
        {
            l7 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            l7 -= 20;
        }
        for(int i11 = 0; i11 < l7; i11++)
        {
            int l13 = k + rand.nextInt(16) + 8;
            int l15 = l + rand.nextInt(16) + 8;
            WorldGenerator worldgenerator = oldbiomegenbase.getRandomWorldGenForTrees(rand);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(worldObj, rand, l13, worldObj.getHeightValue(l13, l15), l15);
        }

        byte byte0 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            byte0 = 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            byte0 = 4;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            byte0 = 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            byte0 = 3;
        }
        for(int i14 = 0; i14 < byte0; i14++)
        {
            int i16 = k + rand.nextInt(16) + 8;
            int k18 = rand.nextInt(128);
            int i21 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, i16, k18, i21);
        }

        if(rand.nextInt(2) == 0)
        {
            int j14 = k + rand.nextInt(16) + 8;
            int j16 = rand.nextInt(128);
            int l18 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, j14, j16, l18);
        }
        if(rand.nextInt(4) == 0)
        {
            int k14 = k + rand.nextInt(16) + 8;
            int k16 = rand.nextInt(128);
            int i19 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, k14, k16, i19);
        }
        if(rand.nextInt(8) == 0)
        {
            int l14 = k + rand.nextInt(16) + 8;
            int l16 = rand.nextInt(128);
            int j19 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, l14, l16, j19);
        }
        for(int i15 = 0; i15 < 10; i15++)
        {
            int i17 = k + rand.nextInt(16) + 8;
            int k19 = rand.nextInt(128);
            int j21 = l + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, i17, k19, j21);
        }

        if(rand.nextInt(32) == 0)
        {
            int j15 = k + rand.nextInt(16) + 8;
            int j17 = rand.nextInt(128);
            int l19 = l + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, j15, j17, l19);
        }
        int k15 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            k15 += 10;
        }
        for(int k17 = 0; k17 < k15; k17++)
        {
            int i20 = k + rand.nextInt(16) + 8;
            int k21 = rand.nextInt(128);
            int k22 = l + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, i20, k21, k22);
        }

        for(int l17 = 0; l17 < 50; l17++)
        {
            int j20 = k + rand.nextInt(16) + 8;
            int l21 = rand.nextInt(rand.nextInt(120) + 8);
            int l22 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, j20, l21, l22);
        }

        for(int i18 = 0; i18 < 20; i18++)
        {
            int k20 = k + rand.nextInt(16) + 8;
            int i22 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int i23 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, k20, i22, i23);
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures_old(generatedTemperatures, k + 8, l + 8, 16, 16);
        for(int j18 = k + 8; j18 < k + 8 + 16; j18++)
        {
            for(int l20 = l + 8; l20 < l + 8 + 16; l20++)
            {
                int j22 = j18 - (k + 8);
                int j23 = l20 - (l + 8);
                int k23 = worldObj.getTopSolidOrLiquidBlock(j18, l20);
                double d1 = generatedTemperatures[j22 * 16 + j23] - ((double)(k23 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && k23 > 0 && k23 < 128 && worldObj.isAirBlock(j18, k23, l20) && worldObj.getBlockMaterial(j18, k23 - 1, l20).isSolid() && worldObj.getBlockMaterial(j18, k23 - 1, l20) != Material.ice)
                {
                    worldObj.setBlockWithNotify(j18, k23, l20, Block.snow.blockID);
                }
            }

        }

        BlockSand.fallInstantly = false;
    }

    public void populate_12(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        OldBiomeGenBase oldbiomegenbase = worldObj.getWorldChunkManager().oldGetBiomeGenAt(k + 16, l + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        boolean flag = false;
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            flag = villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }
        double d = 0.25D;
        if(!flag && rand.nextInt(4) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int l4 = rand.nextInt(128);
            int i8 = l + rand.nextInt(16) + 8;
            (new WorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i1, l4, i8);
        }
        if(!flag && rand.nextInt(8) == 0)
        {
            int j1 = k + rand.nextInt(16) + 8;
            int i5 = rand.nextInt(rand.nextInt(120) + 8);
            int j8 = l + rand.nextInt(16) + 8;
            if(i5 < 64 || rand.nextInt(10) == 0)
            {
                (new WorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, j1, i5, j8);
            }
        }
        for(int k1 = 0; k1 < 8; k1++)
        {
            int j5 = k + rand.nextInt(16) + 8;
            int k8 = rand.nextInt(128);
            int j11 = l + rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, rand, j5, k8, j11);
        }

        for(int i2 = 0; i2 < 10; i2++)
        {
            int k5 = k + rand.nextInt(16);
            int l8 = rand.nextInt(128);
            int k11 = l + rand.nextInt(16);
            (new OldWorldGenClay(32)).generate(worldObj, rand, k5, l8, k11);
        }

        for(int j2 = 0; j2 < 20; j2++)
        {
            int l5 = k + rand.nextInt(16);
            int i9 = rand.nextInt(128);
            int l11 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, l5, i9, l11);
        }

        for(int k2 = 0; k2 < 10; k2++)
        {
            int i6 = k + rand.nextInt(16);
            int j9 = rand.nextInt(128);
            int i12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.gravel.blockID, 32)).generate(worldObj, rand, i6, j9, i12);
        }

        for(int i3 = 0; i3 < 20; i3++)
        {
            int j6 = k + rand.nextInt(16);
            int k9 = rand.nextInt(128);
            int j12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreCoal.blockID, 16)).generate(worldObj, rand, j6, k9, j12);
        }

        for(int j3 = 0; j3 < 20; j3++)
        {
            int k6 = k + rand.nextInt(16);
            int l9 = rand.nextInt(64);
            int k12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreIron.blockID, 8)).generate(worldObj, rand, k6, l9, k12);
        }

        for(int k3 = 0; k3 < 2; k3++)
        {
            int l6 = k + rand.nextInt(16);
            int i10 = rand.nextInt(32);
            int l12 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreGold.blockID, 8)).generate(worldObj, rand, l6, i10, l12);
        }

        for(int l3 = 0; l3 < 8; l3++)
        {
            int i7 = k + rand.nextInt(16);
            int j10 = rand.nextInt(16);
            int i13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, i7, j10, i13);
        }

        for(int i4 = 0; i4 < 1; i4++)
        {
            int j7 = k + rand.nextInt(16);
            int k10 = rand.nextInt(16);
            int j13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreDiamond.blockID, 7)).generate(worldObj, rand, j7, k10, j13);
        }

        for(int j4 = 0; j4 < 1; j4++)
        {
            int k7 = k + rand.nextInt(16);
            int l10 = rand.nextInt(16) + rand.nextInt(16);
            int k13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, k7, l10, k13);
        }

        d = 0.5D;
        int k4 = (int)((mobSpawnerNoise.func_806_a((double)k * d, (double)l * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        int l7 = 0;
        if(rand.nextInt(10) == 0)
        {
            l7++;
        }
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            l7 += k4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest)
        {
            l7 += k4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            l7 += k4 + 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            l7 += k4 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            l7 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.tundra)
        {
            l7 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            l7 -= 20;
        }
        for(int i11 = 0; i11 < l7; i11++)
        {
            int l13 = k + rand.nextInt(16) + 8;
            int l15 = l + rand.nextInt(16) + 8;
            WorldGenerator worldgenerator = oldbiomegenbase.getRandomWorldGenForTrees(rand);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(worldObj, rand, l13, worldObj.getHeightValue(l13, l15), l15);
        }
        byte byte0 = 2;
        for(int i14 = 0; i14 < byte0; i14++)
        {
            int i16 = k + rand.nextInt(16) + 8;
            int k18 = rand.nextInt(128);
            int i21 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, i16, k18, i21);
        }

        if(rand.nextInt(2) == 0)
        {
            int j14 = k + rand.nextInt(16) + 8;
            int j16 = rand.nextInt(128);
            int l18 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, j14, j16, l18);
        }
        if(rand.nextInt(4) == 0)
        {
            int k14 = k + rand.nextInt(16) + 8;
            int k16 = rand.nextInt(128);
            int i19 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, k14, k16, i19);
        }
        if(rand.nextInt(8) == 0)
        {
            int l14 = k + rand.nextInt(16) + 8;
            int l16 = rand.nextInt(128);
            int j19 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, l14, l16, j19);
        }
        for(int i15 = 0; i15 < 10; i15++)
        {
            int i17 = k + rand.nextInt(16) + 8;
            int k19 = rand.nextInt(128);
            int j21 = l + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, i17, k19, j21);
        }

        if(rand.nextInt(32) == 0)
        {
            int j15 = k + rand.nextInt(16) + 8;
            int j17 = rand.nextInt(128);
            int l19 = l + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, j15, j17, l19);
        }
        int k15 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            k15 += 10;
        }
        for(int k17 = 0; k17 < k15; k17++)
        {
            int i20 = k + rand.nextInt(16) + 8;
            int k21 = rand.nextInt(128);
            int k22 = l + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, i20, k21, k22);
        }

        for(int l17 = 0; l17 < 50; l17++)
        {
            int j20 = k + rand.nextInt(16) + 8;
            int l21 = rand.nextInt(rand.nextInt(120) + 8);
            int l22 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, j20, l21, l22);
        }

        for(int i18 = 0; i18 < 20; i18++)
        {
            int k20 = k + rand.nextInt(16) + 8;
            int i22 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int i23 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, k20, i22, i23);
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures_old(generatedTemperatures, k + 8, l + 8, 16, 16);
        for(int j18 = k + 8; j18 < k + 8 + 16; j18++)
        {
            for(int l20 = l + 8; l20 < l + 8 + 16; l20++)
            {
                int j22 = j18 - (k + 8);
                int j23 = l20 - (l + 8);
                int k23 = worldObj.getTopSolidOrLiquidBlock(j18, l20);
                double d1 = generatedTemperatures[j22 * 16 + j23] - ((double)(k23 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && k23 > 0 && k23 < 128 && worldObj.isAirBlock(j18, k23, l20) && worldObj.getBlockMaterial(j18, k23 - 1, l20).isSolid() && worldObj.getBlockMaterial(j18, k23 - 1, l20) != Material.ice)
                {
                    worldObj.setBlockWithNotify(j18, k23, l20, Block.snow.blockID);
                }
            }

        }

        BlockSand.fallInstantly = false;
    }

    public void populate_halloween(IChunkProvider ichunkprovider, int i, int j)
    {
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        OldBiomeGenBase oldbiomegenbase = worldObj.getWorldChunkManager().oldGetBiomeGenAt(k + 16, l + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        boolean flag = false;
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            flag = villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }
        double d = 0.25D;
        for(int i1 = 0; i1 < 8; i1++)
        {
            int i4 = k + rand.nextInt(16) + 8;
            int k6 = rand.nextInt(128);
            int l8 = l + rand.nextInt(16) + 8;
            (new WorldGenDungeons()).generate(worldObj, rand, i4, k6, l8);
        }

        for(int j1 = 0; j1 < 10; j1++)
        {
            int j4 = k + rand.nextInt(16);
            int l6 = rand.nextInt(128);
            int i9 = l + rand.nextInt(16);
            (new OldWorldGenClay(32)).generate(worldObj, rand, j4, l6, i9);
        }

        for(int k1 = 0; k1 < 20; k1++)
        {
            int k4 = k + rand.nextInt(16);
            int i7 = rand.nextInt(128);
            int j9 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, k4, i7, j9);
        }

        for(int i2 = 0; i2 < 10; i2++)
        {
            int l4 = k + rand.nextInt(16);
            int j7 = rand.nextInt(128);
            int k9 = l + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.gravel.blockID, 32)).generate(worldObj, rand, l4, j7, k9);
        }

        for(int j2 = 0; j2 < 20; j2++)
        {
            int i5 = k + rand.nextInt(16);
            int k7 = rand.nextInt(128);
            int l9 = l + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreCoal.blockID, 16)).generate(worldObj, rand, i5, k7, l9);
        }

        for(int k2 = 0; k2 < 20; k2++)
        {
            int j5 = k + rand.nextInt(16);
            int l7 = rand.nextInt(64);
            int i10 = l + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreIron.blockID, 8)).generate(worldObj, rand, j5, l7, i10);
        }

        for(int i3 = 0; i3 < 2; i3++)
        {
            int k5 = k + rand.nextInt(16);
            int i8 = rand.nextInt(32);
            int j10 = l + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreGold.blockID, 8)).generate(worldObj, rand, k5, i8, j10);
        }

        for(int j3 = 0; j3 < 8; j3++)
        {
            int l5 = k + rand.nextInt(16);
            int j8 = rand.nextInt(16);
            int k10 = l + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, l5, j8, k10);
        }

        for(int k3 = 0; k3 < 1; k3++)
        {
            int i6 = k + rand.nextInt(16);
            int k8 = rand.nextInt(16);
            int l10 = l + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreDiamond.blockID, 7)).generate(worldObj, rand, i6, k8, l10);
        }

        d = 0.5D;
        int l3 = (int)((mobSpawnerNoise.func_806_a((double)k * d, (double)l * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        int j6 = 0;
        if(rand.nextInt(10) == 0)
        {
            j6++;
        }
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            j6 += l3 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest)
        {
            j6 += l3 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            j6 += l3 + 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            j6 += l3 + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            j6 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.tundra)
        {
            j6 -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            j6 -= 20;
        }
        Object obj = new WorldGenTrees(false);
        if(rand.nextInt(10) == 0)
        {
            obj = new WorldGenBigTree(false);
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest && rand.nextInt(3) == 0)
        {
            obj = new WorldGenBigTree(false);
        }
        for(int i11 = 0; i11 < j6; i11++)
        {
            int i13 = k + rand.nextInt(16) + 8;
            int i16 = l + rand.nextInt(16) + 8;
            ((WorldGenerator)obj).setScale(1.0D, 1.0D, 1.0D);
            ((WorldGenerator)obj).generate(worldObj, rand, i13, worldObj.getHeightValue(i13, i16), i16);
        }

        for(int j11 = 0; j11 < 2; j11++)
        {
            int j13 = k + rand.nextInt(16) + 8;
            int j16 = rand.nextInt(128);
            int i19 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, j13, j16, i19);
        }

        if(rand.nextInt(2) == 0)
        {
            int k11 = k + rand.nextInt(16) + 8;
            int k13 = rand.nextInt(128);
            int k16 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, k11, k13, k16);
        }
        if(rand.nextInt(4) == 0)
        {
            int l11 = k + rand.nextInt(16) + 8;
            int l13 = rand.nextInt(128);
            int l16 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, l11, l13, l16);
        }
        if(rand.nextInt(8) == 0)
        {
            int i12 = k + rand.nextInt(16) + 8;
            int i14 = rand.nextInt(128);
            int i17 = l + rand.nextInt(16) + 8;
            (new WorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, i12, i14, i17);
        }
        for(int j12 = 0; j12 < 10; j12++)
        {
            int j14 = k + rand.nextInt(16) + 8;
            int j17 = rand.nextInt(128);
            int j19 = l + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, j14, j17, j19);
        }

        if(rand.nextInt(32) == 0)
        {
            int k12 = k + rand.nextInt(16) + 8;
            int k14 = rand.nextInt(128);
            int k17 = l + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, k12, k14, k17);
        }
        int l12 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            l12 += 10;
        }
        for(int l14 = 0; l14 < l12; l14++)
        {
            int l17 = k + rand.nextInt(16) + 8;
            int k19 = rand.nextInt(128);
            int l20 = l + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, l17, k19, l20);
        }

        for(int i15 = 0; i15 < 50; i15++)
        {
            int i18 = k + rand.nextInt(16) + 8;
            int l19 = rand.nextInt(rand.nextInt(120) + 8);
            int i21 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, i18, l19, i21);
        }

        for(int j15 = 0; j15 < 20; j15++)
        {
            int j18 = k + rand.nextInt(16) + 8;
            int i20 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int j21 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, j18, i20, j21);
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures_old(generatedTemperatures, k + 8, l + 8, 16, 16);
        for(int k15 = k + 8; k15 < k + 8 + 16; k15++)
        {
            for(int k18 = l + 8; k18 < l + 8 + 16; k18++)
            {
                int j20 = k15 - (k + 8);
                int k21 = k18 - (l + 8);
                int i22 = worldObj.getTopSolidOrLiquidBlock(k15, k18);
                double d1 = generatedTemperatures[j20 * 16 + k21] - ((double)(i22 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && i22 > 0 && i22 < 128 && worldObj.isAirBlock(k15, i22, k18) && worldObj.getBlockMaterial(k15, i22 - 1, k18).isSolid() && worldObj.getBlockMaterial(k15, i22 - 1, k18) != Material.ice)
                {
                    worldObj.setBlockWithNotify(k15, i22, k18, Block.snow.blockID);
                }
            }

        }

        if(mod_noBiomesX.GenerateLapis)
        {
            for(int l15 = 0; l15 < 1; l15++)
            {
                int l18 = k + rand.nextInt(16);
                int k20 = rand.nextInt(16) + rand.nextInt(16);
                int l21 = l + rand.nextInt(16);
                (new OldWorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, l18, k20, l21);
            }

        }
        BlockSand.fallInstantly = false;
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        return true;
    }

    public boolean unload100OldestChunks()
    {
        return false;
    }

    public boolean canSave()
    {
        return true;
    }

    public String makeString()
    {
        return "RandomLevelSource";
    }
}
