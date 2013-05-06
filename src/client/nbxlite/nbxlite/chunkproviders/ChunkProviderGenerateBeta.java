package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.BetaNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.oldbiomes.*;
import net.minecraft.src.nbxlite.mapgens.MapGenScatteredFeature2;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.OldMapGenBase;
import net.minecraft.src.nbxlite.mapgens.OldMapGenCaves;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenClay;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenDeadBush;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenDungeons;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenFlowers;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenLakes;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenMinable;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenReed;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTallGrass;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.nbxlite.mapgens.SuperOldWorldGenMinable;

public class ChunkProviderGenerateBeta extends ChunkProviderBaseInfinite{
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
    public MapGenStronghold2 strongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;
    private MapGenBase ravineGenerator;
    private MapGenScatteredFeature2 templeGenerator;
    double noise3[];
    double noise1[];
    double noise2[];
    double noise5[];
    double noise6[];
    int unusedIntArray32x32[][];
    private double generatedTemperatures[];

    public ChunkProviderGenerateBeta(World world, long l, Boolean boolean1){
        super(world, l, boolean1, ODNBXlite.GEN_OLDBIOMES);
        fixLight = true;
        sandNoise = new double[256];
        gravelNoise = new double[256];
        stoneNoise = new double[256];
        caveGenerator = new OldMapGenCaves();
        if(boolean1)
        {
            ravineGenerator = new MapGenRavine();
            strongholdGenerator = new MapGenStronghold2();
            villageGenerator = new MapGenVillage();
            mineshaftGenerator = new MapGenMineshaft();
            templeGenerator = new MapGenScatteredFeature2();
        }
        unusedIntArray32x32 = new int[32][32];
        noiseGen1 = new BetaNoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new BetaNoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new BetaNoiseGeneratorOctaves(rand, 8);
        field_909_n = new BetaNoiseGeneratorOctaves(rand, 4);
        noiseGen4 = new BetaNoiseGeneratorOctaves(rand, 4);
        noiseGen5 = new BetaNoiseGeneratorOctaves(rand, 10);
        noiseGen6 = new BetaNoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new BetaNoiseGeneratorOctaves(rand, 8);
    }

    @Override
    protected void generateTerrainForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[], double ad[]){
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

    @Override
    protected void replaceBlocksForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[]){
        byte byte0 = 64;
        double d = 0.03125D;
        if (!ODNBXlite.getFlag("fixbeaches")){
            sandNoise = field_909_n.generateNoiseOctaves(sandNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
            if (ODNBXlite.MapFeatures<ODNBXlite.FEATURES_BETA14){
                gravelNoise = field_909_n.generateNoiseOctaves(gravelNoise, j * 16, 109.0134D, i * 16, 16, 1, 16, d, 1.0D, d);
            }else{
                gravelNoise = field_909_n.generateNoiseOctaves(gravelNoise, i * 16, 109.0134D, j * 16, 16, 1, 16, d, 1.0D, d);
            }
            stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        }
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                boolean b = ODNBXlite.MapFeatures>ODNBXlite.FEATURES_ALPHA120 && (ODNBXlite.MapFeatures < ODNBXlite.FEATURES_BETA14 || !ODNBXlite.getFlag("fixbeaches"));
                OldBiomeGenBase oldbiomegenbase = aoldbiomegenbase[b ? k + l * 16 : k * 16 + l];
                boolean flag = false;
                boolean flag1 = false;
                int i1 = 0;
                if (ODNBXlite.getFlag("fixbeaches")){
                    double dd2 = (i << 4) + k;
                    double dd4 = (j << 4) + l;
                    flag = field_909_n.generateNoise(dd2 * d, dd4 * d, 0.0D) + rand.nextDouble() * 0.20000000000000001D > 0D;
                    flag1 = field_909_n.generateNoise(dd4 * d, d, dd2 * d) + rand.nextDouble() * 0.20000000000000001D > 3D;
                    i1 = (int)(noiseGen4.func_806_a(dd2 * d * 2D, dd4 * d * 2D) / 3D + 3D + rand.nextDouble() * 0.25D);
                }else{
                    flag = sandNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 0.0D;
                    flag1 = gravelNoise[k + l * 16] + rand.nextDouble() * 0.20000000000000001D > 3D;
                    i1 = (int)(stoneNoise[k + l * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                }
                int j1 = -1;
                byte byte1 = oldbiomegenbase.topBlock;
                byte byte2 = oldbiomegenbase.fillerBlock;
                for(int k1 = 127; k1 >= 0; k1--)
                {
                    int l1 = (l * 16 + k) * 128 + k1;
                    if (ODNBXlite.MapFeatures<ODNBXlite.FEATURES_BETA14 || ODNBXlite.getFlag("fixbeaches")){
                        l1 = (k * 16 + l) * 128 + k1;
                    }
                    if(k1 <= (ODNBXlite.MapFeatures<=ODNBXlite.FEATURES_ALPHA120 ? 0 + rand.nextInt(6) - 1 : 0 + rand.nextInt(5)))
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
                    if (ODNBXlite.MapFeatures<ODNBXlite.FEATURES_BETA14){
                        if(j1 > 0)
                        {
                            j1--;
                            abyte0[l1] = byte2;
                        }
                    }else{
                        if(j1 <= 0)
                        {
                            continue;
                        }
                        j1--;
                        abyte0[l1] = byte2;
                        if(j1 == 0 && byte2 == Block.sand.blockID)
                        {
                            j1 = rand.nextInt(4);
                            byte2 = (byte)Block.sandStone.blockID;
                        }
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
        if(mapFeaturesEnabled)
        {
            ravineGenerator.generate(this, worldObj, i, j, abyte0);
            mineshaftGenerator.generate(this, worldObj, i, j, abyte0);
            villageGenerator.generate(this, worldObj, i, j, abyte0);
            strongholdGenerator.generate(this, worldObj, i, j, abyte0);
            templeGenerator.generate(this, worldObj, i, j, abyte0);
        }
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

    @Override
    public void populate(IChunkProvider ichunkprovider, int x, int z){
        BlockSand.fallInstantly = true;
        int x1 = x * 16;
        int z1 = z * 16;
        OldBiomeGenBase oldbiomegenbase = worldObj.getWorldChunkManager().oldGetBiomeGenAt(x1 + 16, z1 + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)x * l1 + (long)z * l2 ^ worldObj.getSeed());
        boolean flag = false;
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            flag = villageGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            templeGenerator.generateStructuresInChunk(worldObj, rand, x, z);
        }
        double d = 0.25D;
        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_ALPHA120){
            if(!flag && rand.nextInt(4) == 0)
            {
                int x2 = x1 + rand.nextInt(16) + 8;
                int y2 = rand.nextInt(128);
                int z2 = z1 + rand.nextInt(16) + 8;
                (new OldWorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, x2, y2, z2);
            }
            if(!flag && rand.nextInt(8) == 0)
            {
                int x2 = x1 + rand.nextInt(16) + 8;
                int y2 = rand.nextInt(rand.nextInt(120) + 8);
                int z2 = z1 + rand.nextInt(16) + 8;
                if(y2 < 64 || rand.nextInt(10) == 0)
                {
                    (new OldWorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, x2, y2, z2);
                }
            }
        }
        for(int i = 0; i < 8; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new OldWorldGenDungeons()).generate(worldObj, rand, x2, y2, z2);
        }

        for(int i = 0; i < 10; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16);
            (new OldWorldGenClay(32)).generate(worldObj, rand, x2, y2, z2);
        }

        boolean bug = ODNBXlite.MapFeatures <= ODNBXlite.FEATURES_BETA15;

        for(int i = 0; i < 20; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.dirt.blockID, 32, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.dirt.blockID, 32)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        for(int i = 0; i < 10; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.gravel.blockID, 32, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.gravel.blockID, 32)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        for(int i = 0; i < 20; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.oreCoal.blockID, 16, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.oreCoal.blockID, 16)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        for(int i = 0; i < 20; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(64);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.oreIron.blockID, 8, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.oreIron.blockID, 8)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        for(int i = 0; i < 2; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(32);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.oreGold.blockID, 8, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.oreGold.blockID, 8)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        for(int i = 0; i < 8; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(16);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.oreRedstone.blockID, 7, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        for(int i = 0; i < 1; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(16);
            int z2 = z1 + rand.nextInt(16);
            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
                (new OldWorldGenMinable(Block.oreDiamond.blockID, 7, bug)).generate(worldObj, rand, x2, y2, z2);
            }else{
                (new SuperOldWorldGenMinable(Block.oreDiamond.blockID, 7)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10 || ODNBXlite.getFlag("newores"))
        {
            for(int i = 0; i < 1; i++)
            {
                int x2 = x1 + rand.nextInt(16);
                int y2 = rand.nextInt(16) + rand.nextInt(16);
                int z2 = z1 + rand.nextInt(16);
                if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_ALPHA120){
                   (new OldWorldGenMinable(Block.oreLapis.blockID, 6, bug)).generate(worldObj, rand, x2, y2, z2);
                }else{
                    (new SuperOldWorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, x2, y2, z2);
                }
            }

        }
        if (ODNBXlite.getFlag("newores")){
            int max = 0;
            detection: for(int i = x1; i < x1 + 16; i++){
                for(int j = z1; j < z1 + 16; j++){
                    int h = worldObj.getPrecipitationHeight(i, j);
                    if (max < h){
                        max = h;
                    }
                    if (max > 108){
                        break detection;
                    }
                }
            }
            if (max > 108){
                for (int i = 0; i < 3 + rand.nextInt(6); i++){
                    int x2 = x1 + rand.nextInt(16);
                    int y2 = rand.nextInt(28) + 4;
                    int z2 = z1 + rand.nextInt(16);
                    int id = worldObj.getBlockId(x2, y2, z2);
                    if (id == Block.stone.blockID){
                        worldObj.setBlock(x2, y2, z2, Block.oreEmerald.blockID);
                    }
                }
            }
        }

        d = 0.5D;
        int treenoise = (int)((mobSpawnerNoise.func_806_a((double)x1 * d, (double)z1 * d) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
        int trees = 0;
        if(rand.nextInt(10) == 0)
        {
            trees++;
        }
        if(oldbiomegenbase == OldBiomeGenBase.forest)
        {
            trees += treenoise + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest)
        {
            if (ODNBXlite.getFlag("jungle")){
                trees += treenoise + 50;
                trees *= 2;
            }else{
                trees += treenoise + 5;
            }
        }
        if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
        {
            trees += treenoise + 2;
        }
        if(oldbiomegenbase == OldBiomeGenBase.taiga)
        {
            trees += treenoise + 5;
        }
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            trees -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.tundra)
        {
            trees -= 20;
        }
        if(oldbiomegenbase == OldBiomeGenBase.plains)
        {
            trees -= 20;
        }
        if(ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA10){
            for(int i = 0; i < trees; i++)
            {
                int x2 = x1 + rand.nextInt(16) + 8;
                int z2 = z1 + rand.nextInt(16) + 8;
                WorldGenerator treegen = oldbiomegenbase.getRandomWorldGenForTrees(rand);
                treegen.setScale(1.0D, 1.0D, 1.0D);
                treegen.generate(worldObj, rand, x2, worldObj.getHeightValue(x2, z2), z2);
            }
        }else{
            Object treegen = new OldWorldGenTrees(false);
            if(rand.nextInt(10) == 0)
            {
                treegen = new WorldGenBigTree(false);
            }
            if(oldbiomegenbase == OldBiomeGenBase.rainforest && rand.nextInt(3) == 0)
            {
                treegen = new WorldGenBigTree(false);
            }
            for(int i = 0; i < trees; i++)
            {
                int x2 = x1 + rand.nextInt(16) + 8;
                int z2 = z1 + rand.nextInt(16) + 8;
                ((WorldGenerator)treegen).setScale(1.0D, 1.0D, 1.0D);
                ((WorldGenerator)treegen).generate(worldObj, rand, x2, worldObj.getHeightValue(x2, z2), z2);
            }
        }
        byte flowers = 2;
        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA12){
            flowers = 0;
            if(oldbiomegenbase == OldBiomeGenBase.forest)
            {
                flowers = 2;
            }
            if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
            {
                flowers = 4;
            }
            if(oldbiomegenbase == OldBiomeGenBase.taiga)
            {
                flowers = 2;
            }
            if(oldbiomegenbase == OldBiomeGenBase.plains)
            {
                flowers = 3;
            }
        }
        for(int i = 0; i < flowers; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantYellow.blockID)).generate(worldObj, rand, x2, y2, z2);
        }

        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA15){
            byte grass = 0;
            if(oldbiomegenbase == OldBiomeGenBase.forest)
            {
                grass = 2;
            }
            if(oldbiomegenbase == OldBiomeGenBase.rainforest)
            {
                grass = 10;
            }
            if(oldbiomegenbase == OldBiomeGenBase.seasonalForest)
            {
                grass = 2;
            }
            if(oldbiomegenbase == OldBiomeGenBase.taiga)
            {
                grass = 1;
            }
            if(oldbiomegenbase == OldBiomeGenBase.plains)
            {
                grass = 10;
            }
            for(int i = 0; i < grass; i++)
            {
                byte grasstype = 1;
                if(oldbiomegenbase == OldBiomeGenBase.rainforest && rand.nextInt(3) != 0)
                {
                    grasstype = 2;
                }
                int x2 = x1 + rand.nextInt(16) + 8;
                int y2 = rand.nextInt(128);
                int z2 = z1 + rand.nextInt(16) + 8;
                (new OldWorldGenTallGrass(Block.tallGrass.blockID, grasstype)).generate(worldObj, rand, x2, y2, z2);
            }

            grass = 0;
            if(oldbiomegenbase == OldBiomeGenBase.desert)
            {
                grass = 2;
            }
            for(int i = 0; i < grass; i++)
            {
                int x2 = x1 + rand.nextInt(16) + 8;
                int y2 = rand.nextInt(128);
                int z2 = z1 + rand.nextInt(16) + 8;
                (new OldWorldGenDeadBush(Block.deadBush.blockID)).generate(worldObj, rand, x2, y2, z2);
            }
        }

        if(rand.nextInt(2) == 0)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantRed.blockID)).generate(worldObj, rand, x2, y2, z2);
        }
        if(rand.nextInt(4) == 0)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomBrown.blockID)).generate(worldObj, rand, x2, y2, z2);
        }
        if(rand.nextInt(8) == 0)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomRed.blockID)).generate(worldObj, rand, x2, y2, z2);
        }
        for(int i = 0; i < 10; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new OldWorldGenReed()).generate(worldObj, rand, x2, y2, z2);
        }

        if(rand.nextInt(32) == 0)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(worldObj, rand, x2, y2, z2);
        }
        int cacti = 0;
        if(oldbiomegenbase == OldBiomeGenBase.desert)
        {
            cacti += 10;
        }
        for(int i = 0; i < cacti; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new WorldGenCactus()).generate(worldObj, rand, x2, y2, z2);
        }

        for(int i = 0; i < 50; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(rand.nextInt(120) + 8);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.waterMoving.blockID)).generate(worldObj, rand, x2, y2, z2);
        }

        for(int i = 0; i < 20; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int y2 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            int z2 = z1 + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(worldObj, rand, x2, y2, z2);
        }
        if(mapFeaturesEnabled && oldbiomegenbase == OldBiomeGenBase.desert && rand.nextInt(1000) == 0)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int z2 = z1 + rand.nextInt(16) + 8;
            WorldGenDesertWells worldgendesertwells = new WorldGenDesertWells();
            worldgendesertwells.generate(worldObj, rand, x2, worldObj.getHeightValue(x2, z2) + 1, z2);
        }
        if(oldbiomegenbase == OldBiomeGenBase.rainforest && ODNBXlite.getFlag("jungle")){
            WorldGenVines worldgenvines = new WorldGenVines();
            for (int i = 0; i < 50; i++)
            {
                int x2 = x1 + rand.nextInt(16) + 8;
                byte y2 = 64;
                int z2 = z1 + rand.nextInt(16) + 8;
                worldgenvines.generate(worldObj, rand, x2, y2, z2);
            }
        }

        generatedTemperatures = worldObj.getWorldChunkManager().getTemperatures_old(generatedTemperatures, x1 + 8, z1 + 8, 16, 16);
        for(int i = x1 + 8; i < x1 + 8 + 16; i++)
        {
            for(int j = z1 + 8; j < z1 + 8 + 16; j++)
            {
                int x2 = i - (x1 + 8);
                int z2 = j - (z1 + 8);
                int y2 = worldObj.getPrecipitationHeight(i, j);
                double d1 = generatedTemperatures[x2 * 16 + z2] - ((double)(y2 - 64) / 64D) * 0.29999999999999999D;
                if(d1 < 0.5D && y2 > 0 && y2 < 128 && worldObj.isAirBlock(i, y2, j) && worldObj.getBlockMaterial(i, y2 - 1, j).isSolid() && worldObj.getBlockMaterial(i, y2 - 1, j) != Material.ice)
                {
                    worldObj.setBlock(i, y2, j, Block.snow.blockID, 0, 2);
                }
            }
        }
        BlockSand.fallInstantly = false;
        spawnAnimals(x1, z1);
    }

    @Override
    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
        if("Stronghold".equals(s) && strongholdGenerator != null){
            return strongholdGenerator.getNearestInstance(world, i, j, k);
        }else{
            return null;
        }
    }
}
