package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.BetaNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.oldbiomes.*;
import net.minecraft.src.nbxlite.mapgens.OldMapGenBase;
import net.minecraft.src.nbxlite.mapgens.OldMapGenCaves;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenClay;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenDungeons;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenFlowers;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenLakes;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenMinable;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenReed;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.MapGenSkyStronghold;

public class ChunkProviderSky extends ChunkProviderBaseInfinite{
    private BetaNoiseGeneratorOctaves noiseGen1;
    private BetaNoiseGeneratorOctaves noiseGen2;
    private BetaNoiseGeneratorOctaves noiseGen3;
    private BetaNoiseGeneratorOctaves field_909_n;
    private BetaNoiseGeneratorOctaves noiseGen4;
    public BetaNoiseGeneratorOctaves noiseGen5;
    public BetaNoiseGeneratorOctaves noiseGen6;
    public BetaNoiseGeneratorOctaves mobSpawnerNoise;
    private double field_4180_q[];
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];
    private OldMapGenBase caveGenerator;
    private MapGenStronghold2 strongholdGen;
    double noise3[];
    double noise1[];
    double noise2[];
    double noise5[];
    double noise6[];
    int unusedIntArray32x32[][];
    private double generatedTemperatures[];

    public ChunkProviderSky(World world, long l, boolean boolean1){
        super(world, l, boolean1, ODNBXlite.GEN_OLDBIOMES);
        sandNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        caveGenerator = new OldMapGenCaves();
        strongholdGen = new MapGenSkyStronghold();
        unusedIntArray32x32 = new int[32][32];
        noiseGen1 = new BetaNoiseGeneratorOctaves(rand, 16, true);
        noiseGen2 = new BetaNoiseGeneratorOctaves(rand, 16, true);
        noiseGen3 = new BetaNoiseGeneratorOctaves(rand, 8, true);
        field_909_n = new BetaNoiseGeneratorOctaves(rand, 4, true);
        noiseGen4 = new BetaNoiseGeneratorOctaves(rand, 4, true);
        noiseGen5 = new BetaNoiseGeneratorOctaves(rand, 10, true);
        noiseGen6 = new BetaNoiseGeneratorOctaves(rand, 16, true);
        mobSpawnerNoise = new BetaNoiseGeneratorOctaves(rand, 8, true);
    }

    @Override
    protected void generateTerrainForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[], double ad[]){
        byte byte0 = 2;
        int k = byte0 + 1;
        byte byte1 = 33;
        int l = byte0 + 1;
        field_4180_q = initializeNoiseField(field_4180_q, i * byte0, 0, j * byte0, k, byte1, l);
        for(int i1 = 0; i1 < byte0; i1++)
        {
            for(int j1 = 0; j1 < byte0; j1++)
            {
                for(int k1 = 0; k1 < 32; k1++)
                {
                    double d = 0.25D;
                    double d1 = field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte1 + (k1 + 0)];
                    double d2 = field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte1 + (k1 + 0)];
                    double d3 = field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte1 + (k1 + 0)];
                    double d4 = field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte1 + (k1 + 0)];
                    double d5 = (field_4180_q[((i1 + 0) * l + (j1 + 0)) * byte1 + (k1 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((i1 + 0) * l + (j1 + 1)) * byte1 + (k1 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((i1 + 1) * l + (j1 + 0)) * byte1 + (k1 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((i1 + 1) * l + (j1 + 1)) * byte1 + (k1 + 1)] - d4) * d;
                    for(int l1 = 0; l1 < 4; l1++)
                    {
                        double d9 = 0.125D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int i2 = 0; i2 < 8; i2++)
                        {
                            int j2 = i2 + i1 * 8 << 11 | 0 + j1 * 8 << 7 | k1 * 4 + l1;
                            char c = '\200';
                            double d14 = 0.125D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int k2 = 0; k2 < 8; k2++)
                            {
                                int l2 = 0;
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

    @Override
    protected void replaceBlocksForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[]){
        double d = 0.03125D;
        sandNoise = field_909_n.generateNoiseOctaves(sandNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        gravelNoise = field_909_n.generateNoiseOctaves(gravelNoise, i * 16, 109.0134D, j * 16, 16, 1, 16, d, 1.0D, d);
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                OldBiomeGenBase oldbiomegenbase = aoldbiomegenbase[k + l * 16];
                int i1 = (int)(stoneNoise[k + l * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int j1 = -1;
                byte byte0 = oldbiomegenbase.topBlock;
                byte byte1 = oldbiomegenbase.fillerBlock;
                for(int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    byte byte2 = abyte0[l1];
                    if(byte2 == 0)
                    {
                        j1 = -1;
                        continue;
                    }
                    if(byte2 != Block.stone.blockID)
                    {
                        continue;
                    }
                    if(j1 == -1)
                    {
                        if(i1 <= 0)
                        {
                            byte0 = 0;
                            byte1 = (byte)Block.stone.blockID;
                        }
                        j1 = i1;
                        if(k1 >= 0)
                        {
                            abyte0[l1] = byte0;
                        } else
                        {
                            abyte0[l1] = byte1;
                        }
                        continue;
                    }
                    if(j1 <= 0)
                    {
                        continue;
                    }
                    j1--;
                    abyte0[l1] = byte1;
                    if(j1 == 0 && byte1 == Block.sand.blockID)
                    {
                        j1 = rand.nextInt(4);
                        byte1 = (byte)Block.sandStone.blockID;
                    }
                }

            }

        }

    }

    @Override
    protected void generateStructures(int i, int j, byte abyte0[]){
        if (abyte0 != null){
            caveGenerator.generate(this, worldObj, i, j, abyte0);
        }
        if (mapFeaturesEnabled){
            strongholdGen.generate(this, worldObj, i, j, abyte0);
        }
    }

    @Override
    public Chunk provideChunk(int i, int j){
        rand.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        oldBiomesForGeneration = worldObj.getWorldChunkManager().oldLoadBlockGeneratorData(oldBiomesForGeneration, i * 16, j * 16, 16, 16);
        double ad[] = worldObj.getWorldChunkManager().temperature;
        generateTerrainForOldBiome(i, j, abyte0, oldBiomesForGeneration, ad);
        replaceBlocksForOldBiome(i, j, abyte0, oldBiomesForGeneration);
        generateStructures(i, j, abyte0);
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        chunk.generateSkylightMap();
        return chunk;
    }

    private double[] initializeNoiseField(double ad[], int i, int j, int k, int l, int i1, int j1){
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        double ad1[] = worldObj.getWorldChunkManager().temperature;
        double ad2[] = worldObj.getWorldChunkManager().humidity;
        noise5 = noiseGen5.generateNoiseOctaves(noise5, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        noise6 = noiseGen6.generateNoiseOctaves(noise6, i, k, l, j1, 200D, 200D, 0.5D);
        d *= 2D;
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
                if(d6 > 1.0D)
                {
                    d6 = 1.0D;
                }
                d6 /= 8D;
                d6 = 0.0D;
                if(d5 < 0.0D)
                {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double)i1) / 16D;
                l1++;
                double d7 = (double)i1 / 2D;
                for(int j3 = 0; j3 < i1; j3++)
                {
                    double d8 = 0.0D;
                    double d9 = (((double)j3 - d7) * 8D) / d5;
                    if(d9 < 0.0D)
                    {
                        d9 *= -1D;
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
                    d8 -= 8D;
                    int k3 = 32;
                    if(j3 > i1 - k3)
                    {
                        double d13 = (float)(j3 - (i1 - k3)) / ((float)k3 - 1.0F);
                        d8 = d8 * (1.0D - d13) + -30D * d13;
                    }
                    k3 = 8;
                    if(j3 < k3)
                    {
                        double d14 = (float)(k3 - j3) / ((float)k3 - 1.0F);
                        d8 = d8 * (1.0D - d14) + -30D * d14;
                    }
                    ad[k1] = d8;
                    k1++;
                }

            }

        }

        return ad;
    }

    @Override
    public void populate(IChunkProvider ichunkprovider, int i, int j){
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        OldBiomeGenBase oldbiomegenbase = worldObj.getWorldChunkManager().oldGetBiomeGenAt(k + 16, l + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        double d = 0.25D;
        if(rand.nextInt(4) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int l4 = rand.nextInt(128);
            int i8 = l + rand.nextInt(16) + 8;
            (new OldWorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i1, l4, i8);
        }
        if(rand.nextInt(8) == 0)
        {
            int j1 = k + rand.nextInt(16) + 8;
            int i5 = rand.nextInt(rand.nextInt(120) + 8);
            int j8 = l + rand.nextInt(16) + 8;
            if(i5 < 64 || rand.nextInt(10) == 0)
            {
                (new OldWorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, j1, i5, j8);
            }
        }
        for(int k1 = 0; k1 < 8; k1++)
        {
            int j5 = k + rand.nextInt(16) + 8;
            int k8 = rand.nextInt(128);
            int i13 = l + rand.nextInt(16) + 8;
            (new OldWorldGenDungeons()).generate(worldObj, rand, j5, k8, i13);
        }

        for(int i2 = 0; i2 < 10; i2++)
        {
            int k5 = k + rand.nextInt(16);
            int l8 = rand.nextInt(128);
            int j13 = l + rand.nextInt(16);
            (new OldWorldGenClay(32)).generate(worldObj, rand, k5, l8, j13);
        }

        for(int j2 = 0; j2 < 20; j2++)
        {
            int l5 = k + rand.nextInt(16);
            int i9 = rand.nextInt(128);
            int k13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.dirt.blockID, 32, false)).generate(worldObj, rand, l5, i9, k13);
        }

        for(int k2 = 0; k2 < 10; k2++)
        {
            int i6 = k + rand.nextInt(16);
            int j9 = rand.nextInt(128);
            int l13 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.gravel.blockID, 32, false)).generate(worldObj, rand, i6, j9, l13);
        }

        for(int i3 = 0; i3 < 20; i3++)
        {
            int j6 = k + rand.nextInt(16);
            int k9 = rand.nextInt(128);
            int i14 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreCoal.blockID, 16, false)).generate(worldObj, rand, j6, k9, i14);
        }

        for(int j3 = 0; j3 < 20; j3++)
        {
            int k6 = k + rand.nextInt(16);
            int l9 = rand.nextInt(64);
            int j14 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreIron.blockID, 8, false)).generate(worldObj, rand, k6, l9, j14);
        }

        for(int k3 = 0; k3 < 2; k3++)
        {
            int l6 = k + rand.nextInt(16);
            int i10 = rand.nextInt(32);
            int k14 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreGold.blockID, 8, false)).generate(worldObj, rand, l6, i10, k14);
        }

        for(int l3 = 0; l3 < 8; l3++)
        {
            int i7 = k + rand.nextInt(16);
            int j10 = rand.nextInt(16);
            int l14 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreRedstone.blockID, 7, false)).generate(worldObj, rand, i7, j10, l14);
        }

        for(int i4 = 0; i4 < 1; i4++)
        {
            int j7 = k + rand.nextInt(16);
            int k10 = rand.nextInt(16);
            int i15 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreDiamond.blockID, 7, false)).generate(worldObj, rand, j7, k10, i15);
        }

        for(int j4 = 0; j4 < 1; j4++)
        {
            int k7 = k + rand.nextInt(16);
            int l10 = rand.nextInt(16) + rand.nextInt(16);
            int j15 = l + rand.nextInt(16);
            (new OldWorldGenMinable(Block.oreLapis.blockID, 6, false)).generate(worldObj, rand, k7, l10, j15);
        }
        if (ODNBXlite.getFlag("newores")){
            int max = 0;
            detection: for(int iii = k; iii < k + 16; iii++){
                for(int jjj = l; jjj < l + 16; jjj++){
                    int h = worldObj.getPrecipitationHeight(iii, jjj);
                    if (max < h){
                        max = h;
                    }
                    if (max > 108){
                        break detection;
                    }
                }
            }
            if (max > 108){
                for (int iiii = 0; iiii < 3 + rand.nextInt(6); iiii++){
                    int x2 = k + rand.nextInt(16);
                    int y2 = rand.nextInt(28) + 4;
                    int z2 = l + rand.nextInt(16);
                    int id = worldObj.getBlockId(x2, y2, z2);
                    if (id == Block.stone.blockID){
                        worldObj.setBlock(x2, y2, z2, Block.oreEmerald.blockID);
                    }
                }
            }
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
            int k15 = k + rand.nextInt(16) + 8;
            int j18 = l + rand.nextInt(16) + 8;
            WorldGenerator worldgenerator = oldbiomegenbase.getRandomWorldGenForTrees(rand);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(worldObj, rand, k15, worldObj.getHeightValue(k15, j18), j18);
        }

        for(int j11 = 0; j11 < 2; j11++)
        {
            int l15 = k + rand.nextInt(16) + 8;
            int k18 = rand.nextInt(128);
            int i21 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, l15, k18, i21);
        }

        if(rand.nextInt(2) == 0)
        {
            int k11 = k + rand.nextInt(16) + 8;
            int i16 = rand.nextInt(128);
            int l18 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, k11, i16, l18);
        }
        if(rand.nextInt(4) == 0)
        {
            int l11 = k + rand.nextInt(16) + 8;
            int j16 = rand.nextInt(128);
            int i19 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, l11, j16, i19);
        }
        if(rand.nextInt(8) == 0)
        {
            int i12 = k + rand.nextInt(16) + 8;
            int k16 = rand.nextInt(128);
            int j19 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, i12, k16, j19);
        }
        for(int j12 = 0; j12 < 10; j12++)
        {
            int l16 = k + rand.nextInt(16) + 8;
            int k19 = rand.nextInt(128);
            int j21 = l + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, l16, k19, j21);
        }

        if(rand.nextInt(32) == 0)
        {
            int k12 = k + rand.nextInt(16) + 8;
            int i17 = rand.nextInt(128);
            int l19 = l + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, k12, i17, l19);
        }
        int l12 = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            l12 += 10;
        }
        for(int j17 = 0; j17 < l12; j17++)
        {
            int i20 = k + rand.nextInt(16) + 8;
            int k21 = rand.nextInt(128);
            int k22 = l + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, i20, k21, k22);
        }

        for(int k17 = 0; k17 < 50; k17++)
        {
            int j20 = k + rand.nextInt(16) + 8;
            int l21 = rand.nextInt(rand.nextInt(120) + 8);
            int l22 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, j20, l21, l22);
        }

        for(int l17 = 0; l17 < 20; l17++)
        {
            int k20 = k + rand.nextInt(16) + 8;
            int i22 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int i23 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, k20, i22, i23);
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures_old(generatedTemperatures, k + 8, l + 8, 16, 16);
        for(int i18 = k + 8; i18 < k + 8 + 16; i18++)
        {
            for(int l20 = l + 8; l20 < l + 8 + 16; l20++)
            {
                int j22 = i18 - (k + 8);
                int j23 = l20 - (l + 8);
                int k23 = worldObj.getPrecipitationHeight(i18, l20);
                double d1 = generatedTemperatures[j22 * 16 + j23] - ((double)(k23 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && k23 > 0 && k23 < 128 && worldObj.isAirBlock(i18, k23, l20) && worldObj.getBlockMaterial(i18, k23 - 1, l20).isSolid() && worldObj.getBlockMaterial(i18, k23 - 1, l20) != Material.ice)
                {
                    worldObj.setBlock(i18, k23, l20, Block.snow.blockID, 0, 2);
                }
            }

        }

        BlockSand.fallInstantly = false;
    }

    @Override
    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
        if("Stronghold".equals(s) && strongholdGen != null){
            return strongholdGen.getNearestInstance(world, i, j, k);
        }else{
            return null;
        }
    }
}
