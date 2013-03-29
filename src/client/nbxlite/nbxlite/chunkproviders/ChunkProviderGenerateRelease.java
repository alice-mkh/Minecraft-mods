package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.MapGenScatteredFeature3;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenDungeons;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenLakes;

public class ChunkProviderGenerateRelease extends ChunkProviderBaseInfinite{
    private NoiseGeneratorOctaves noiseGen1;
    private NoiseGeneratorOctaves noiseGen2;
    private NoiseGeneratorOctaves noiseGen3;
    private NoiseGeneratorOctaves noiseGen4;
    public NoiseGeneratorOctaves noiseGen5;
    public NoiseGeneratorOctaves noiseGen6;
    public NoiseGeneratorOctaves mobSpawnerNoise;
    private double field_4180_q[];
    private double stoneNoise[];
    private MapGenBase caveGenerator;
    public MapGenStronghold2 strongholdGenerator;
    public MapGenStronghold newStrongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;
    private MapGenBase ravineGenerator;
    private MapGenScatteredFeature3 templeGenerator;
    private MapGenScatteredFeature scatteredFeatureGenerator;
    double noise3[];
    double noise1[];
    double noise2[];
    double noise5[];
    double noise6[];
    float field_35388_l[];
    int unusedIntArray32x32[][];

    public ChunkProviderGenerateRelease(World world, long l, Boolean flag){
        super(world, l, flag, ODNBXlite.GEN_NEWBIOMES);
        stoneNoise = new double[256];
        caveGenerator = new MapGenCaves();
        strongholdGenerator = new MapGenStronghold2();
        newStrongholdGenerator = new MapGenStronghold();
        villageGenerator = new MapGenVillage();
        mineshaftGenerator = new MapGenMineshaft();
        templeGenerator = new MapGenScatteredFeature3();
        scatteredFeatureGenerator = new MapGenScatteredFeature();
        ravineGenerator = new MapGenRavine();
        unusedIntArray32x32 = new int[32][32];
        noiseGen1 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen2 = new NoiseGeneratorOctaves(rand, 16);
        noiseGen3 = new NoiseGeneratorOctaves(rand, 8);
        noiseGen4 = new NoiseGeneratorOctaves(rand, 4);
        noiseGen5 = new NoiseGeneratorOctaves(rand, 10);
        noiseGen6 = new NoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new NoiseGeneratorOctaves(rand, 8);
    }

    @Override
    protected void generateTerrainForBiome(int i, int j, byte abyte0[], BiomeGenBase biomes[]){
        byte byte0 = 4;
        int k = 16;
        int l = 63;
        int i1 = byte0 + 1;
        int j1 = 17;
        int k1 = byte0 + 1;
        field_4180_q = initializeNoiseField(field_4180_q, i * byte0, 0, j * byte0, i1, j1, k1);
        for (int l1 = 0; l1 < byte0; l1++)
        {
            for (int i2 = 0; i2 < byte0; i2++)
            {
                for (int j2 = 0; j2 < k; j2++)
                {
                    double d = 0.125D;
                    double d1 = field_4180_q[((l1 + 0) * k1 + (i2 + 0)) * j1 + (j2 + 0)];
                    double d2 = field_4180_q[((l1 + 0) * k1 + (i2 + 1)) * j1 + (j2 + 0)];
                    double d3 = field_4180_q[((l1 + 1) * k1 + (i2 + 0)) * j1 + (j2 + 0)];
                    double d4 = field_4180_q[((l1 + 1) * k1 + (i2 + 1)) * j1 + (j2 + 0)];
                    double d5 = (field_4180_q[((l1 + 0) * k1 + (i2 + 0)) * j1 + (j2 + 1)] - d1) * d;
                    double d6 = (field_4180_q[((l1 + 0) * k1 + (i2 + 1)) * j1 + (j2 + 1)] - d2) * d;
                    double d7 = (field_4180_q[((l1 + 1) * k1 + (i2 + 0)) * j1 + (j2 + 1)] - d3) * d;
                    double d8 = (field_4180_q[((l1 + 1) * k1 + (i2 + 1)) * j1 + (j2 + 1)] - d4) * d;
                    for (int k2 = 0; k2 < 8; k2++)
                    {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for (int l2 = 0; l2 < 4; l2++)
                        {
                            int i3 = l2 + l1 * 4 << 11 | 0 + i2 * 4 << 7 | j2 * 8 + k2;
                            int j3 = 1 << 7;
                            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
                                i3 -= j3;
                            }
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
                                d15 -= d16;
                            }
                            for (int k3 = 0; k3 < 4; k3++)
                            {
                                if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA181){
                                    int k4 = 0;
                                    if (j2 * 8 + k2 < l)
                                    {
                                        k4 = Block.waterStill.blockID;
                                    }
                                    if (d15 > 0.0D)
                                    {
                                        k4 = Block.stone.blockID;
                                    }
                                    abyte0[i3] = (byte)k4;
                                    i3 += j3;
                                    d15 += d16;
                                }else{
                                    if ((d15 += d16) > 0.0D)
                                    {
                                        abyte0[i3 += j3] = (byte)Block.stone.blockID;
                                        continue;
                                    }
                                    if (j2 * 8 + k2 < l)
                                    {
                                        abyte0[i3 += j3] = (byte)Block.waterStill.blockID;
                                    }
                                    else
                                    {
                                        abyte0[i3 += j3] = 0;
                                    }
                                }
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
    protected void replaceBlocksForBiome(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[]){
        int k = 63;
        double d = 0.03125D;
        stoneNoise = noiseGen4.generateNoiseOctaves(stoneNoise, i * 16, j * 16, 0, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        float af[] = worldObj.getWorldChunkManager().initTemperatureCache(i * 16, j * 16, 16, 16);
        for (int l = 0; l < 16; l++)
        {
            for (int i1 = 0; i1 < 16; i1++)
            {
                float f = af[i1 + l * 16];
                BiomeGenBase biomegenbase = abiomegenbase[i1 + l * 16];
                int j1 = (int)(stoneNoise[l + i1 * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int k1 = -1;
                byte byte0 = biomegenbase.topBlock;
                byte byte1 = biomegenbase.fillerBlock;
                for (int l1 = 127; l1 >= 0; l1--)
                {
                    int i2 = (i1 * 16 + l) * 128 + l1;
                    if (l1 <= 0 + rand.nextInt(5))
                    {
                        abyte0[i2] = (byte)Block.bedrock.blockID;
                        continue;
                    }
                    byte byte2 = abyte0[i2];
                    if (byte2 == 0)
                    {
                        k1 = -1;
                        continue;
                    }
                    if (byte2 != Block.stone.blockID)
                    {
                        continue;
                    }
                    if (k1 == -1)
                    {
                        if (j1 <= 0)
                        {
                            byte0 = 0;
                            byte1 = (byte)Block.stone.blockID;
                        }
                        else if (l1 >= k - 4 && l1 <= k + 1)
                        {
                            byte0 = biomegenbase.topBlock;
                            byte1 = biomegenbase.fillerBlock;
                        }
                        if (l1 < k && byte0 == 0)
                        {
                            if (f < 0.15F && ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181)
                            {
                                byte0 = (byte)Block.ice.blockID;
                            }
                            else
                            {
                                byte0 = (byte)Block.waterStill.blockID;
                            }
                        }
                        k1 = j1;
                        if (l1 >= k - 1)
                        {
                            abyte0[i2] = byte0;
                        }
                        else
                        {
                            abyte0[i2] = byte1;
                        }
                        continue;
                    }
                    if (k1 <= 0)
                    {
                        continue;
                    }
                    k1--;
                    abyte0[i2] = byte1;
                    if (k1 == 0 && byte1 == Block.sand.blockID)
                    {
                        k1 = rand.nextInt(4);
                        byte1 = (byte)Block.sandStone.blockID;
                    }
                }
            }
        }
    }

    @Override
    protected void generateStructures(int i, int j, byte abyte0[]){
        caveGenerator.generate(this, worldObj, i, j, abyte0);
        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
            ravineGenerator.generate(this, worldObj, i, j, abyte0);
        }
        if (mapFeaturesEnabled)
        {
            if(ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA181){
                strongholdGenerator.generate(this, worldObj, i, j, abyte0);
            }
            mineshaftGenerator.generate(this, worldObj, i, j, abyte0);
            villageGenerator.generate(this, worldObj, i, j, abyte0);
            if(ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_14){
                newStrongholdGenerator.generate(this, worldObj, i, j, abyte0);
            }else if(ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
                strongholdGenerator.generate(this, worldObj, i, j, abyte0);
            }
            if (ODNBXlite.MapFeatures >= ODNBXlite.FEATURES_14){
                scatteredFeatureGenerator.generate(this, worldObj, i, j, abyte0);
            }else if (ODNBXlite.MapFeatures >= ODNBXlite.FEATURES_13){
                templeGenerator.generate(this, worldObj, i, j, abyte0);
            }
        }
        if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA181){
            ravineGenerator.generate(this, worldObj, i, j, abyte0);
        }
    }

    private double[] initializeNoiseField(double ad[], int i, int j, int k, int l, int i1, int j1){
        if (ad == null)
        {
            ad = new double[l * i1 * j1];
        }
        if (field_35388_l == null)
        {
            field_35388_l = new float[25];
            for (int k1 = -2; k1 <= 2; k1++)
            {
                for (int l1 = -2; l1 <= 2; l1++)
                {
                    float f = 10F / MathHelper.sqrt_float((float)(k1 * k1 + l1 * l1) + 0.2F);
                    field_35388_l[k1 + 2 + (l1 + 2) * 5] = f;
                }
            }
        }
        double d = 684.41200000000003D;
        double d1 = 684.41200000000003D;
        noise5 = noiseGen5.generateNoiseOctaves(noise5, i, k, l, j1, 1.121D, 1.121D, 0.5D);
        noise6 = noiseGen6.generateNoiseOctaves(noise6, i, k, l, j1, 200D, 200D, 0.5D);
        noise3 = noiseGen3.generateNoiseOctaves(noise3, i, j, k, l, i1, j1, d / 80D, d1 / 160D, d / 80D);
        noise1 = noiseGen1.generateNoiseOctaves(noise1, i, j, k, l, i1, j1, d, d1, d);
        noise2 = noiseGen2.generateNoiseOctaves(noise2, i, j, k, l, i1, j1, d, d1, d);
        i = k = 0;
        int i2 = 0;
        int j2 = 0;
        for (int k2 = 0; k2 < l; k2++)
        {
            for (int l2 = 0; l2 < j1; l2++)
            {
                float f1 = 0.0F;
                float f2 = 0.0F;
                float f3 = 0.0F;
                byte byte0 = 2;
                BiomeGenBase biomegenbase = biomesForGeneration[k2 + 2 + (l2 + 2) * (l + 5)];
                for (int i3 = -byte0; i3 <= byte0; i3++)
                {
                    for (int j3 = -byte0; j3 <= byte0; j3++)
                    {
                        BiomeGenBase biomegenbase1 = biomesForGeneration[k2 + i3 + 2 + (l2 + j3 + 2) * (l + 5)];
                        float f4 = field_35388_l[i3 + 2 + (j3 + 2) * 5] / (biomegenbase1.minHeight + 2.0F);
                        if (biomegenbase1.minHeight > biomegenbase.minHeight)
                        {
                            f4 /= 2.0F;
                        }
                        f1 += biomegenbase1.maxHeight * f4;
                        f2 += biomegenbase1.minHeight * f4;
                        f3 += f4;
                    }
                }

                f1 /= f3;
                f2 /= f3;
                f1 = f1 * 0.9F + 0.1F;
                f2 = (f2 * 4F - 1.0F) / 8F;
                double d2 = noise6[j2] / 8000D;
                if (d2 < 0.0D)
                {
                    d2 = -d2 * 0.29999999999999999D;
                }
                d2 = d2 * 3D - 2D;
                if (d2 < 0.0D)
                {
                    d2 /= 2D;
                    if (d2 < -1D)
                    {
                        d2 = -1D;
                    }
                    d2 /= 1.3999999999999999D;
                    d2 /= 2D;
                }
                else
                {
                    if (d2 > 1.0D)
                    {
                        d2 = 1.0D;
                    }
                    d2 /= 8D;
                }
                j2++;
                for (int k3 = 0; k3 < i1; k3++)
                {
                    double d3 = f2;
                    double d4 = f1;
                    d3 += d2 * 0.20000000000000001D;
                    d3 = (d3 * (double)i1) / 16D;
                    double d5 = (double)i1 / 2D + d3 * 4D;
                    double d6 = 0.0D;
                    double d7 = (((double)k3 - d5) * 12D * 128D) / (double)128 / d4;
                    if (d7 < 0.0D)
                    {
                        d7 *= 4D;
                    }
                    double d8 = noise1[i2] / 512D;
                    double d9 = noise2[i2] / 512D;
                    double d10 = (noise3[i2] / 10D + 1.0D) / 2D;
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
                    if (k3 > i1 - 4)
                    {
                        double d11 = (float)(k3 - (i1 - 4)) / 3F;
                        d6 = d6 * (1.0D - d11) + -10D * d11;
                    }
                    ad[i2] = d6;
                    i2++;
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
        BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(k + 16, l + 16);
        rand.setSeed(worldObj.getSeed());
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        long l2 = (rand.nextLong() / 2L) * 2L + 1L;
        rand.setSeed((long)i * l1 + (long)j * l2 ^ worldObj.getSeed());
        boolean flag = false;
        if (mapFeaturesEnabled)
        {
            if(ODNBXlite.MapFeatures==ODNBXlite.FEATURES_BETA181){
                strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            }
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            flag = villageGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            if(ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_14){
                newStrongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            }else if(ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
                strongholdGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            }
            if(ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_14){
                scatteredFeatureGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            }else if(ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_13){
                templeGenerator.generateStructuresInChunk(worldObj, rand, i, j);
            }
        }
        if (!flag && rand.nextInt(4) == 0)
        {
            int i1 = k + rand.nextInt(16) + 8;
            int j2 = rand.nextInt(128);
            int k3 = l + rand.nextInt(16) + 8;
            (new OldWorldGenLakes(Block.waterStill.blockID)).generate(worldObj, rand, i1, j2, k3);
        }
        if (!flag && rand.nextInt(8) == 0)
        {
            int j1 = k + rand.nextInt(16) + 8;
            int k2 = rand.nextInt(rand.nextInt(128 - 8) + 8);
            int l3 = l + rand.nextInt(16) + 8;
            if (k2 < 63 || rand.nextInt(10) == 0)
            {
                (new OldWorldGenLakes(Block.lavaStill.blockID)).generate(worldObj, rand, j1, k2, l3);
            }
        }
        for (int k1 = 0; k1 < 8; k1++)
        {
            int i3 = k + rand.nextInt(16) + 8;
            int i4 = rand.nextInt(128);
            int k4 = l + rand.nextInt(16) + 8;
            if (!(new OldWorldGenDungeons()).generate(worldObj, rand, i3, i4, k4));
        }
        biomegenbase.decorate(worldObj, rand, k, l);
        spawnAnimals(k, l);
        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
            k += 8;
            l += 8;
            for (int i2 = 0; i2 < 16; i2++)
            {
                for (int j3 = 0; j3 < 16; j3++)
                {
                    int j4 = worldObj.getPrecipitationHeight(k + i2, l + j3);
                    if (worldObj.isBlockFreezable(i2 + k, j4 - 1, j3 + l))
                    {
                        worldObj.setBlock(i2 + k, j4 - 1, j3 + l, Block.ice.blockID, 0, 2);
                    }
                    if (worldObj.canSnowAt(i2 + k, j4, j3 + l))
                    {
                        worldObj.setBlock(i2 + k, j4, j3 + l, Block.snow.blockID, 0, 2);
                    }
                }
            }
        }
        BlockSand.fallInstantly = false;
    }

    @Override
    protected void spawnAnimals(int i, int j){
        BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(i + 16, j + 16);
        SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, i + 8, j + 8, 16, 16, rand);
    }

    @Override
    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
        if (ODNBXlite.MapFeatures >= ODNBXlite.FEATURES_14 && "Stronghold".equals(s) && newStrongholdGenerator != null){
            return newStrongholdGenerator.getNearestInstance(world, i, j, k);
        }else if("Stronghold".equals(s) && strongholdGenerator != null){
            return strongholdGenerator.getNearestInstance(world, i, j, k);
        }else{
            return null;
        }
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k){
        WorldChunkManager worldchunkmanager = worldObj.getWorldChunkManager();
        if (worldchunkmanager == null)
        {
            return null;
        }
        BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAtChunkCoord(new ChunkCoordIntPair(i >> 4, k >> 4));
        if (biomegenbase == null)
        {
            return null;
        }
        if (biomegenbase == BiomeGenBase.swampland && enumcreaturetype == EnumCreatureType.monster && scatteredFeatureGenerator.hasStructureAt(i, j, k))
        {
            return scatteredFeatureGenerator.getScatteredFeatureSpawnList();
        }
        else
        {
            return biomegenbase.getSpawnableList(enumcreaturetype);
        }
    }
}
