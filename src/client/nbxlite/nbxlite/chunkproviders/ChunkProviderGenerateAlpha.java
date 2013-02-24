package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.AlphaNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.OldMapGenBase;
import net.minecraft.src.nbxlite.mapgens.OldMapGenCaves;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenClay;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenDungeons;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenFlowers;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenReed;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.nbxlite.mapgens.SuperOldWorldGenMinable;

public class ChunkProviderGenerateAlpha extends ChunkProviderBaseInfinite{
    private AlphaNoiseGeneratorOctaves terrainAlt1Generator;
    private AlphaNoiseGeneratorOctaves terrainAlt2Generator;
    private AlphaNoiseGeneratorOctaves terrainGenerator;
    private AlphaNoiseGeneratorOctaves noiseSandGen;
    private AlphaNoiseGeneratorOctaves rockSandGen;
    public AlphaNoiseGeneratorOctaves detailGenerator;
    public AlphaNoiseGeneratorOctaves roughnessGenerator;
    public AlphaNoiseGeneratorOctaves mobSpawnerNoise;
    private double field_4180_q[];
    private double sandNoise[];
    private double gravelNoise[];
    private double stoneNoise[];
    private OldMapGenBase caveGenerator;
    public MapGenStronghold2 strongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;
    private MapGenBase ravineGenerator;
    double terrainMain[];
    double terrainAlt1[];
    double terrainAlt2[];
    double detail[];
    double roughness[];

    public ChunkProviderGenerateAlpha(World world, long l, boolean flag){
        super(world, l, flag);
        fixLight = true;
        stoneNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        caveGenerator = new OldMapGenCaves();
        if(flag)
        {
            ravineGenerator = new MapGenRavine();
            strongholdGenerator = new MapGenStronghold2();
            villageGenerator = new MapGenVillage();
            mineshaftGenerator = new MapGenMineshaft();
        }
        terrainAlt1Generator = new AlphaNoiseGeneratorOctaves(rand, 16);
        terrainAlt2Generator = new AlphaNoiseGeneratorOctaves(rand, 16);
        terrainGenerator = new AlphaNoiseGeneratorOctaves(rand, 8);
        noiseSandGen = new AlphaNoiseGeneratorOctaves(rand, 4);
        rockSandGen = new AlphaNoiseGeneratorOctaves(rand, 4);
        detailGenerator = new AlphaNoiseGeneratorOctaves(rand, 10);
        roughnessGenerator = new AlphaNoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new AlphaNoiseGeneratorOctaves(rand, 8);
    }

    protected void generateTerrain(int i, int j, byte abyte0[]){
        byte byte0 = 4;
        byte byte1 = 64;
        int k = byte0 + 1;
        byte byte2 = 17;
        int l = byte0 + 1;
        field_4180_q = initializeNoiseField(field_4180_q, i * byte0, 0, j * byte0, k, byte2, l);
        for(int j1 = 0; j1 < byte0; j1++)
        {
            for(int k1 = 0; k1 < byte0; k1++)
            {
                for(int l1 = 0; l1 < 16; l1++)
                {
                    double d = 0.125D;
                    double d1 = field_4180_q[((j1 + 0) * l + (k1 + 0)) * byte2 + (l1 + 0)];
                    double d2 = field_4180_q[((j1 + 0) * l + (k1 + 1)) * byte2 + (l1 + 0)];
                    double d3 = field_4180_q[((j1 + 1) * l + (k1 + 0)) * byte2 + (l1 + 0)];
                    double d4 = field_4180_q[((j1 + 1) * l + (k1 + 1)) * byte2 + (l1 + 0)];
                    double d5 = (field_4180_q[((j1 + 0) * l + (k1 + 0)) * byte2 + (l1 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((j1 + 0) * l + (k1 + 1)) * byte2 + (l1 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((j1 + 1) * l + (k1 + 0)) * byte2 + (l1 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((j1 + 1) * l + (k1 + 1)) * byte2 + (l1 + 1)] - d4) * d;
                    for(int i2 = 0; i2 < 8; i2++)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int j2 = 0; j2 < 4; j2++)
                        {
                            int k2 = j2 + j1 * 4 << 11 | 0 + k1 * 4 << 7 | l1 * 8 + i2;
                            char c = '\200';
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int l2 = 0; l2 < 4; l2++)
                            {
                                int i3 = 0;
                                if(l1 * 8 + i2 < byte1)
                                {
                                    if(ODNBXlite.SnowCovered && l1 * 8 + i2 >= byte1 - 1 && ODNBXlite.MapTheme != ODNBXlite.THEME_HELL)
                                    {
                                        i3 = Block.ice.blockID;
                                    } else
                                    if(ODNBXlite.MapTheme==ODNBXlite.THEME_HELL)
                                    {
                                        i3 = Block.lavaStill.blockID;
                                    } else
                                    {
                                        i3 = Block.waterStill.blockID;
                                    }
                                }
                                if(d15 > 0.0D)
                                {
                                    i3 = Block.stone.blockID;
                                }
                                abyte0[k2] = (byte)i3;
                                k2 += c;
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

    protected void replaceBlocks(int i, int j, byte abyte0[]){
        byte byte0 = 64;
        double d = 0.03125D;
        sandNoise = noiseSandGen.generateNoiseOctaves(sandNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        gravelNoise = noiseSandGen.generateNoiseOctaves(gravelNoise, j * 16, 109.0134D, i * 16, 16, 1, 16, d, 1.0D, d);
        stoneNoise = rockSandGen.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int l = 0; l < 16; l++)
        {
            for(int i1 = 0; i1 < 16; i1++)
            {
                double d1 = 0.0D;
                if(ODNBXlite.MapTheme==ODNBXlite.THEME_PARADISE)
                {
                    d1 = -0.29999999999999999D;
                }
                boolean flag = sandNoise[l + i1 * 16] + rand.nextDouble() * 0.20000000000000001D > d1;
                boolean flag1 = gravelNoise[l + i1 * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                int j1 = (int)(stoneNoise[l + i1 * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int k1 = -1;
                byte byte1;
                if(ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
                    byte1 = (byte)Block.dirt.blockID;
                }else{
                    byte1 = (byte)Block.grass.blockID;
                }
                byte byte2 = (byte)Block.dirt.blockID;
                for(int l1 = 127; l1 >= 0; l1--)
                {
                    int i2 = (l * 16 + i1) * 128 + l1;
                    if(l1 <= (0 + rand.nextInt(6)) - 1)
                    {
                        abyte0[i2] = (byte)Block.bedrock.blockID;
                        continue;
                    }
                    byte byte3 = abyte0[i2];
                    if(byte3 == 0)
                    {
                        k1 = -1;
                        continue;
                    }
                    if(byte3 != Block.stone.blockID)
                    {
                        continue;
                    }
                    if(k1 == -1)
                    {
                        if(j1 <= 0)
                        {
                            byte1 = 0;
                            byte2 = (byte)Block.stone.blockID;
                        } else
                        if(l1 >= byte0 - 4 && l1 <= byte0 + 1)
                        {
                            if (ODNBXlite.MapTheme!=ODNBXlite.THEME_HELL){
                                byte1 = (byte)Block.grass.blockID;
                                byte2 = (byte)Block.dirt.blockID;
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
                            }else{
                                byte1 = (byte)Block.dirt.blockID;
                                byte2 = (byte)Block.dirt.blockID;
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
                                    byte1 = (byte)Block.grass.blockID;
                                }
                                if(flag)
                                {
                                    byte2 = (byte)Block.sand.blockID;
                                }
                            }
                        }
                        if(l1 < byte0 && byte1 == 0)
                        {
                            if(ODNBXlite.SnowCovered && l1 >= byte0 - 1 && ODNBXlite.MapTheme != ODNBXlite.THEME_HELL)
                            {
                                byte1 = (byte)Block.ice.blockID;
                            } else if(ODNBXlite.MapTheme==ODNBXlite.THEME_HELL)
                            {
                                byte1 = (byte)Block.lavaStill.blockID;
                            } else
                            {
                                byte1 = (byte)Block.waterStill.blockID;
                            }
                        }
                        k1 = j1;
                        if(l1 >= byte0 - 1)
                        {
                            abyte0[i2] = byte1;
                        } else
                        {
                            abyte0[i2] = byte2;
                        }
                        continue;
                    }
                    if(k1 > 0)
                    {
                        k1--;
                        abyte0[i2] = byte2;
                    }
                }

            }

        }

    }

    protected void generateStructures(int i, int j, byte abyte0[]){
        caveGenerator.generate(this, worldObj, i, j, abyte0);
        if(mapFeaturesEnabled)
        {
            ravineGenerator.generate(this, worldObj, i, j, abyte0);
            mineshaftGenerator.generate(this, worldObj, i, j, abyte0);
            villageGenerator.generate(this, worldObj, i, j, abyte0);
            strongholdGenerator.generate(this, worldObj, i, j, abyte0);
        }
    }

    private double[] initializeNoiseField(double ad[], int i, int j, int k, int l, int i1, int j1){
        if(ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        detail = detailGenerator.generateNoiseOctaves(detail, i, j, k, l, 1, j1, 1.0D, 0.0D, 1.0D);
        roughness = roughnessGenerator.generateNoiseOctaves(roughness, i, j, k, l, 1, j1, 100D, 0.0D, 100D);
        terrainMain = terrainGenerator.generateNoiseOctaves(terrainMain, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
        terrainAlt1 = terrainAlt1Generator.generateNoiseOctaves(terrainAlt1, i, j, k, l, i1, j1, d, d1, d);
        terrainAlt2 = terrainAlt2Generator.generateNoiseOctaves(terrainAlt2, i, j, k, l, i1, j1, d, d1, d);
        int k1 = 0;
        int l1 = 0;
        for(int i2 = 0; i2 < l; i2++)
        {
            for(int j2 = 0; j2 < j1; j2++)
            {
                double d2 = (detail[l1] + 256D) / 512D;
                if(d2 > 1.0D)
                {
                    d2 = 1.0D;
                }
                double d3 = 0.0D;
                double d4 = roughness[l1] / 8000D;
                if(d4 < 0.0D)
                {
                    d4 = -d4;
                }
                d4 = d4 * 3D - 3D;
                if(d4 < 0.0D)
                {
                    d4 /= 2D;
                    if(d4 < -1D)
                    {
                        d4 = -1D;
                    }
                    d4 /= 1.3999999999999999D;
                    d4 /= 2D;
                    d2 = 0.0D;
                } else
                {
                    if(d4 > 1.0D)
                    {
                        d4 = 1.0D;
                    }
                    d4 /= 6D;
                }
                d2 += 0.5D;
                d4 = (d4 * (double)i1) / 16D;
                double d5 = (double)i1 / 2D + d4 * 4D;
                l1++;
                for(int k2 = 0; k2 < i1; k2++)
                {
                    double d6 = 0.0D;
                    double d7 = (((double)k2 - d5) * 12D) / d2;
                    if(d7 < 0.0D)
                    {
                        d7 *= 4D;
                    }
                    double d8 = terrainAlt1[k1] / 512D;
                    double d9 = terrainAlt2[k1] / 512D;
                    double d10 = (terrainMain[k1] / 10D + 1.0D) / 2D;
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
                    if(k2 > i1 - 4)
                    {
                        double d11 = (float)(k2 - (i1 - 4)) / 3F;
                        d6 = d6 * (1.0D - d11) + -10D * d11;
                    }
                    if((double)k2 < d3)
                    {
                        double d12 = (d3 - (double)k2) / 4D;
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

    public void populate(IChunkProvider ichunkprovider, int i, int j){
        BlockSand.fallInstantly = true;
        int k = i * 16;
        int l = j * 16;
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
        }
        double d = 0.25D;
        for(int i1 = 0; i1 < 8; i1++)
        {
            int i4 = k + rand.nextInt(16) + 8;
            int k6 = rand.nextInt(128);
            int l8 = l + rand.nextInt(16) + 8;
            (new OldWorldGenDungeons()).generate(worldObj, rand, i4, k6, l8);
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
            (new SuperOldWorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, k4, i7, j9);
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
        if(ODNBXlite.GenerateNewOres)
        {
            for(int k13 = 0; k13 < 1; k13++)
            {
                int j16 = k + rand.nextInt(16);
                int i19 = rand.nextInt(16) + rand.nextInt(16);
                int l20 = l + rand.nextInt(16);
                (new WorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, j16, i19, l20);
            }
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
        int l3 = (int)((mobSpawnerNoise.func_806_a((double)k * d, (double)l * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        if(l3 < 0)
        {
            l3 = 0;
        }
        if(rand.nextInt(10) == 0)
        {
            l3++;
        }
        if(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS)
        {
            l3 += 20;
        }
        Object obj = new OldWorldGenTrees(false);
        if(rand.nextInt(10) == 0)
        {
            obj = new WorldGenBigTree(false);
        }
        for(int i11 = 0; i11 < l3; i11++)
        {
            int l13 = k + rand.nextInt(16) + 8;
            int k16 = l + rand.nextInt(16) + 8;
            ((WorldGenerator)obj).setScale(1.0D, 1.0D, 1.0D);
            ((WorldGenerator)obj).generate(worldObj, rand, l13, worldObj.getHeightValue(l13, k16), k16);
        }

        for(int j11 = 0; j11 < 2; j11++)
        {
            int i14 = k + rand.nextInt(16) + 8;
            int l16 = rand.nextInt(128);
            int j19 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, i14, l16, j19);
        }

        if(rand.nextInt(2) == 0)
        {
            int k11 = k + rand.nextInt(16) + 8;
            int j14 = rand.nextInt(128);
            int i17 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, k11, j14, i17);
        }

        if(rand.nextInt(4) == 0)
        {
            int l11 = k + rand.nextInt(16) + 8;
            int k14 = rand.nextInt(128);
            int j17 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, l11, k14, j17);
        }
        if(rand.nextInt(8) == 0)
        {
            int i12 = k + rand.nextInt(16) + 8;
            int l14 = rand.nextInt(128);
            int k17 = l + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, i12, l14, k17);
        }
        for(int j12 = 0; j12 < 10; j12++)
        {
            int i15 = k + rand.nextInt(16) + 8;
            int l17 = rand.nextInt(128);
            int k19 = l + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, i15, l17, k19);
        }

        for(int k12 = 0; k12 < 1; k12++)
        {
            int j15 = k + rand.nextInt(16) + 8;
            int i18 = rand.nextInt(128);
            int l19 = l + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, j15, i18, l19);
        }

        for(int l12 = 0; l12 < 50; l12++)
        {
            int k15 = k + rand.nextInt(16) + 8;
            int j18 = rand.nextInt(rand.nextInt(120) + 8);
            int i20 = l + rand.nextInt(16) + 8;
            if (ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
                (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, k15, j18, i20);
            }else{
                (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, k15, j18, i20);
            }
        }

        for(int i13 = 0; i13 < 20; i13++)
        {
            int l15 = k + rand.nextInt(16) + 8;
            int k18 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int j20 = l + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, l15, k18, j20);
        }
        spawnAnimals(k, l);

        for(int j13 = k + 8; j13 < k + 8 + 16; j13++)
        {
            for(int i16 = l + 8; i16 < l + 8 + 16; i16++)
            {
                int l18 = j13 - (k + 8);
                int k20 = i16 - (l + 8);
                int i21 = worldObj.getPrecipitationHeight(j13, i16);
                if(ODNBXlite.SnowCovered && i21 > 0 && i21 < 128 && worldObj.isAirBlock(j13, i21, i16) && worldObj.getBlockMaterial(j13, i21 - 1, i16).isSolid() && worldObj.getBlockMaterial(j13, i21 - 1, i16) != Material.ice)
                {
                    worldObj.setBlockWithNotify(j13, i21, i16, Block.snow.blockID);
                }
            }
        }
        BlockSand.fallInstantly = false;
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
        if("Stronghold".equals(s) && strongholdGenerator != null){
            return strongholdGenerator.getNearestInstance(world, i, j, k);
        }else{
            return null;
        }
    }
}
