package net.minecraft.src.nbxlite.chunkproviders;

import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.InfdevNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.mapgens.*;

public class ChunkProviderGenerateInfdev2 extends ChunkProviderBaseInfinite
{
    private Random rand;
    private InfdevNoiseGeneratorOctaves terrainAlt1Generator;
    private InfdevNoiseGeneratorOctaves terrainAlt2Generator;
    private InfdevNoiseGeneratorOctaves terrainGenerator;
    private InfdevNoiseGeneratorOctaves noiseSandGen;
    private InfdevNoiseGeneratorOctaves rockSandGen;
    private InfdevNoiseGeneratorOctaves detailGenerator;
    private InfdevNoiseGeneratorOctaves roughnessGenerator;
    private InfdevNoiseGeneratorOctaves mobSpawnerNoise;
    private World theWorld;
    private double field_4180_q[];
    private double terrainMain[];
    private double terrainAlt1[];
    private double terrainAlt2[];
    private double detail[];
    private double roughness[];
    public MapGenStronghold2 strongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;
    private MapGenBase ravineGenerator;

    public ChunkProviderGenerateInfdev2(World world, long l, boolean flag)
    {
        super(world, l, flag);
        fixLight = true;
        theWorld = world;
        rand = new Random(l);
        if(flag)
        {
            ravineGenerator = new MapGenRavine();
            strongholdGenerator = new MapGenStronghold2();
            villageGenerator = new MapGenVillage();
            mineshaftGenerator = new MapGenMineshaft();
        }
        terrainAlt1Generator = new InfdevNoiseGeneratorOctaves(rand, 16);
        terrainAlt2Generator = new InfdevNoiseGeneratorOctaves(rand, 16);
        terrainGenerator = new InfdevNoiseGeneratorOctaves(rand, 8);
        noiseSandGen = new InfdevNoiseGeneratorOctaves(rand, 4);
        rockSandGen = new InfdevNoiseGeneratorOctaves(rand, 4);
        detailGenerator = new InfdevNoiseGeneratorOctaves(rand, 10);
        roughnessGenerator = new InfdevNoiseGeneratorOctaves(rand, 16);
        mobSpawnerNoise = new InfdevNoiseGeneratorOctaves(rand, 8);
        field_4180_q = new double[425];
    }

    @Override
    protected void generateTerrain(int i1, int j1, byte abyte0[]){
        boolean flag = false;
        detail = detailGenerator.a(detail, i1 << 2, 0, j1 << 2, 5, 1, 5, 1.0D, 0.0D, 1.0D);
        roughness = roughnessGenerator.a(roughness, i1 << 2, 0, j1 << 2, 5, 1, 5, 100D, 0.0D, 100D);
        terrainMain = terrainGenerator.a(terrainMain, i1 << 2, 0, j1 << 2, 5, 17, 5, 8.5551500000000011D, 4.2775750000000006D, 8.5551500000000011D);
        terrainAlt1 = terrainAlt1Generator.a(terrainAlt1, i1 << 2, 0, j1 << 2, 5, 17, 5, 684.41200000000003D, 684.41200000000003D, 684.41200000000003D);
        terrainAlt2 = terrainAlt2Generator.a(terrainAlt2, i1 << 2, 0, j1 << 2, 5, 17, 5, 684.41200000000003D, 684.41200000000003D, 684.41200000000003D);
        int i3 = 0;
        int j6 = 0;
        for(int k6 = 0; k6 < 5; k6++)
        {
            for(int i7 = 0; i7 < 5; i7++)
            {
                double d23;
                if((d23 = (detail[j6] + 256D) / 512D) > 1.0D)
                    d23 = 1.0D;
                double d25;
                if((d25 = roughness[j6] / 8000D) < 0.0D)
                    d25 = -d25;
                if((d25 = d25 * 3D - 3D) < 0.0D)
                {
                    if((d25 /= 2D) < -1D)
                        d25 = -1D;
                    d25 = (d25 /= 1.3999999999999999D) / 2D;
                    d23 = 0.0D;
                } else
                {
                    if(d25 > 1.0D)
                        d25 = 1.0D;
                    d25 /= 6D;
                }
                d23 += 0.5D;
                d25 = (d25 * 17D) / 16D;
                double d26 = 8.5D + d25 * 4D;
                j6++;
                for(int i8 = 0; i8 < 17; i8++)
                {
                    double d28;
                    if((d28 = (((double)i8 - d26) * 12D) / d23) < 0.0D)
                        d28 *= 4D;
                    double d29 = terrainAlt1[i3] / 512D;
                    double d31 = terrainAlt2[i3] / 512D;
                    double d27;
                    double d33;
                    if((d33 = (terrainMain[i3] / 10D + 1.0D) / 2D) < 0.0D)
                        d27 = d29;
                    else
                    if(d33 > 1.0D)
                        d27 = d31;
                    else
                        d27 = d29 + (d31 - d29) * d33;
                    d27 -= d28;
                    field_4180_q[i3] = d27;
                    i3++;
                }

            }

        }

        for(int l1 = 0; l1 < 4; l1++)
        {
            for(int j2 = 0; j2 < 4; j2++)
            {
                for(int d1 = 0; d1 < 16; d1++)
                {
                    double d2 = field_4180_q[(l1 * 5 + j2) * 17 + d1];
                    double d4 = field_4180_q[(l1 * 5 + (j2 + 1)) * 17 + d1];
                    double d5 = field_4180_q[((l1 + 1) * 5 + j2) * 17 + d1];
                    double d6 = field_4180_q[((l1 + 1) * 5 + (j2 + 1)) * 17 + d1];
                    double d7 = field_4180_q[(l1 * 5 + j2) * 17 + (d1 + 1)];
                    double d8 = field_4180_q[(l1 * 5 + (j2 + 1)) * 17 + (d1 + 1)];
                    double d9 = field_4180_q[((l1 + 1) * 5 + j2) * 17 + (d1 + 1)];
                    double d10 = field_4180_q[((l1 + 1) * 5 + (j2 + 1)) * 17 + (d1 + 1)];
                    for(int i5 = 0; i5 < 8; i5++)
                    {
                        double d11 = (double)i5 / 8D;
                        double d12 = d2 + (d7 - d2) * d11;
                        double d13 = d4 + (d8 - d4) * d11;
                        double d14 = d5 + (d9 - d5) * d11;
                        double d15 = d6 + (d10 - d6) * d11;
                        for(int j5 = 0; j5 < 4; j5++)
                        {
                            double d16 = (double)j5 / 4D;
                            double d17 = d12 + (d14 - d12) * d16;
                            double d18 = d13 + (d15 - d13) * d16;
                            int k5 = j5 + (l1 << 2) << 11 | 0 + (j2 << 2) << 7 | (d1 << 3) + i5;
                            for(int l5 = 0; l5 < 4; l5++)
                            {
                                double d19 = (double)l5 / 4D;
                                double d20 = d17 + (d18 - d17) * d19;
                                int i6 = 0;
                                if((d1 << 3) + i5 < 64)
                                    i6 = ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.lavaStill.blockID : Block.waterStill.blockID;
                                if(d20 > 0.0D)
                                    i6 = Block.stone.blockID;
                                abyte0[k5] = (byte)i6;
                                k5 += 128;
                            }

                        }

                    }

                }
            }
        }
    }

    @Override
    protected void replaceBlocks(int i1, int j1, byte abyte0[]){
        for(int i2 = 0; i2 < 16; i2++)
        {
            for(int k2 = 0; k2 < 16; k2++)
            {
                double d1 = (i1 << 4) + i2;
                double d3 = (j1 << 4) + k2;
                double ddd = ODNBXlite.MapTheme==ODNBXlite.THEME_PARADISE ? -0.29999999999999999D : 0.0D;
                boolean flag1 = noiseSandGen.a(d1 * 0.03125D, d3 * 0.03125D, 0.0D) + rand.nextDouble() * 0.20000000000000001D > ddd;
                boolean flag2 = noiseSandGen.a(d3 * 0.03125D, 109.0134D, d1 * 0.03125D) + rand.nextDouble() * 0.20000000000000001D > 3D;
                int k3 = (int)(rockSandGen.func_806_a(d1 * 0.03125D * 2D, d3 * 0.03125D * 2D) / 3D + 3D + rand.nextDouble() * 0.25D);
                int l3 = i2 << 11 | k2 << 7 | 0x7f;
                int i4 = -1;
                int j4 = ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.dirt.blockID : Block.grass.blockID;
                int k4 = Block.dirt.blockID;
                for(int l4 = 127; l4 >= 0; l4--)
                {
                    if(l4 <= rand.nextInt(6) - 1)
                        abyte0[l3] = (byte)Block.bedrock.blockID;
                    else
                    if(abyte0[l3] == 0)
                        i4 = -1;
                    else
                    if(abyte0[l3] == Block.stone.blockID)
                        if(i4 == -1)
                        {
                            if(k3 <= 0)
                            {
                                j4 = 0;
                                k4 = (byte)Block.stone.blockID;
                            } else
                            if(l4 >= 60 && l4 <= 65)
                            {
                                j4 = ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.dirt.blockID : Block.grass.blockID;
                                k4 = Block.dirt.blockID;
                                if(flag2)
                                    j4 = 0;
                                if(flag2)
                                    k4 = Block.gravel.blockID;
                                if(flag1)
                                    j4 = ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.grass.blockID : Block.sand.blockID;
                                if(flag1)
                                    k4 = Block.sand.blockID;
                            }
                            if(l4 < 64 && j4 == 0)
                                j4 = ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.lavaStill.blockID : Block.waterStill.blockID;
                            i4 = k3;
                            if(l4 >= 63)
                                abyte0[l3] = (byte)j4;
                            else
                                abyte0[l3] = (byte)k4;
                        } else
                        if(i4 > 0)
                        {
                            i4--;
                            abyte0[l3] = (byte)k4;
                        }
                    l3--;
                }

            }

        }
    }

    @Override
    protected void generateStructures(int i1, int j1, byte abyte0[]){
        rand.setSeed(theWorld.getSeed());
        long l6 = (rand.nextLong() / 2L << 1) + 1L;
        long l7 = (rand.nextLong() / 2L << 1) + 1L;
        for(int i = i1 - 8; i <= i1 + 8; i++){
            for(int j = j1 - 8; j <= j1 + 8; j++)
            {
                rand.setSeed((long)i * l6 + (long)j * l7 ^ theWorld.getSeed());
                int k1 = rand.nextInt(rand.nextInt(rand.nextInt(40) + 1) + 1);
                if(rand.nextInt(10) != 0){
                    k1 = 0;
                }
                for(int j3 = 0; j3 < k1; j3++)
                {
                    double d21 = (i << 4) + rand.nextInt(16);
                    double d22 = rand.nextInt(rand.nextInt(120) + 8);
                    double d24 = (j << 4) + rand.nextInt(16);
                    int j7 = 1;
                    if(rand.nextInt(4) == 0)
                    {
                        generateCaves(i1, j1, abyte0, d21, d22, d24, 1.0F + rand.nextFloat() * 6F, 0.0F, 0.0F, -1, -1, 0.5D);
                        j7 = 1 + rand.nextInt(4);
                    }
                    for(int k7 = 0; k7 < j7; k7++)
                    {
                        float f1 = rand.nextFloat() * 3.141593F * 2.0F;
                        float f2 = ((rand.nextFloat() - 0.5F) * 2.0F) / 8F;
                        float f3 = rand.nextFloat() * 2.0F + rand.nextFloat();
                        generateCaves(i1, j1, abyte0, d21, d22, d24, f3, f1, f2, 0, 0, 1.0D);
                    }
                }
            }
        }
        if(mapFeaturesEnabled)
        {
            if (ODNBXlite.Structures[0]){
                ravineGenerator.generate(this, worldObj, i1, j1, abyte0);
            }
            if (ODNBXlite.Structures[3]){
                mineshaftGenerator.generate(this, worldObj, i1, j1, abyte0);
            }
            if (ODNBXlite.Structures[1]){
                villageGenerator.generate(this, worldObj, i1, j1, abyte0);
            }
            if (ODNBXlite.Structures[2]){
                strongholdGenerator.generate(this, worldObj, i1, j1, abyte0);
            }
        }
    }

    private void generateCaves(int i1, int j1, byte abyte0[], double d1, double d2, 
            double d3, float f1, float f2, float f3, int k1, int l1, 
            double d4)
    {
label0:
        do
        {
            double d5 = (i1 << 4) + 8;
            double d6 = (j1 << 4) + 8;
            float f4 = 0.0F;
            float f5 = 0.0F;
            Random random = new Random(rand.nextLong());
            if(l1 <= 0)
                l1 = 112 - random.nextInt(28);
            boolean flag = false;
            if(k1 == -1)
            {
                k1 = l1 / 2;
                flag = true;
            }
            int i2 = random.nextInt(l1 / 2) + l1 / 4;
            boolean flag1 = random.nextInt(6) == 0;
            for(; k1 < l1; k1++)
            {
                double d7;
                double d8 = (d7 = 1.5D + (double)(MathHelper.sin(((float)k1 * 3.141593F) / (float)l1) * f1)) * d4;
                float f6 = (float)MathHelper.cos(f3);
                float f7 = (float)MathHelper.sin(f3);
                d1 += MathHelper.cos(f2) * f6;
                d2 += f7;
                d3 += MathHelper.sin(f2) * f6;
                if(flag1)
                    f3 *= 0.92F;
                else
                    f3 *= 0.7F;
                f3 += f5 * 0.1F;
                f2 += f4 * 0.1F;
                f5 *= 0.9F;
                f4 *= 0.75F;
                f5 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
                f4 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;
                if(!flag && k1 == i2 && f1 > 1.0F)
                {
                    generateCaves(i1, j1, abyte0, d1, d2, d3, random.nextFloat() * 0.5F + 0.5F, f2 - 1.570796F, f3 / 3F, k1, l1, 1.0D);
                    d4 = 1.0D;
                    l1 = l1;
                    k1 = k1;
                    f3 = f3 / 3F;
                    f2 = f2 + 1.570796F;
                    f1 = random.nextFloat() * 0.5F + 0.5F;
                    d3 = d3;
                    d2 = d2;
                    d1 = d1;
                    abyte0 = abyte0;
                    j1 = j1;
                    i1 = i1;
                    continue label0;
                }
                if(!flag && random.nextInt(4) == 0)
                    continue;
                double d9 = d1 - d5;
                double d10 = d3 - d6;
                double d11 = l1 - k1;
                double d12 = f1 + 2.0F + 16F;
                if((d9 * d9 + d10 * d10) - d11 * d11 > d12 * d12)
                    return;
                if(d1 < d5 - 16D - d7 * 2D || d3 < d6 - 16D - d7 * 2D || d1 > d5 + 16D + d7 * 2D || d3 > d6 + 16D + d7 * 2D)
                    continue;
                d9 = MathHelper.floor_double(d1 - d7) - (i1 << 4) - 1;
                int i3 = (int)(MathHelper.floor_double(d1 + d7) - (i1 << 4)) + 1;
                d10 = MathHelper.floor_double(d2 - d8) - 1;
                int j3 = (int)(MathHelper.floor_double(d2 + d8)) + 1;
                d11 = MathHelper.floor_double(d3 - d7) - (j1 << 4) - 1;
                int k3 = (int)(MathHelper.floor_double(d3 + d7) - (j1 << 4)) + 1;
                if(d9 < 0)
                    d9 = 0;
                if(i3 > 16)
                    i3 = 16;
                if(d10 <= 0)
                    d10 = 1;
                if(j3 > 120)
                    j3 = 120;
                if(d11 < 0)
                    d11 = 0;
                if(k3 > 16)
                    k3 = 16;
                boolean flag3 = false;
                for(int i4 = (int)d9; !flag3 && i4 < i3; i4++)
                {
                    for(int k4 = (int)d11; !flag3 && k4 < k3; k4++)
                    {
                        for(int l4 = j3 + 1; !flag3 && l4 >= d10 - 1; l4--)
                        {
                            int j2 = ((i4 << 4) + k4 << 7) + l4;
                            if(l4 < 0 || l4 >= 128)
                                continue;
                            if(abyte0[j2] == Block.waterMoving.blockID || abyte0[j2] == Block.waterStill.blockID)
                                flag3 = true;
                            if(l4 != d10 - 1 && i4 != d9 && i4 != i3 - 1 && k4 != d11 && k4 != k3 - 1)
                                l4 = (int)d10;
                        }

                    }

                }

                if(flag3)
                    continue;
                for(int j4 = (int)d9; j4 < i3; j4++)
                {
                    double d13 = (((double)(j4 + (i1 << 4)) + 0.5D) - d1) / d7;
                    for(int k2 = (int)d11; k2 < k3; k2++)
                    {
                        double d14 = (((double)(k2 + (j1 << 4)) + 0.5D) - d3) / d7;
                        int l2 = ((j4 << 4) + k2 << 7) + j3;
                        boolean flag2 = false;
                        for(int l3 = j3 - 1; l3 >= d10; l3--)
                        {
                            double d15 = (((double)l3 + 0.5D) - d2) / d8;
                            if(d15 > -0.69999999999999996D && d13 * d13 + d15 * d15 + d14 * d14 < 1.0D)
                            {
                                byte byte0;
                                if((byte0 = abyte0[l2]) == Block.grass.blockID)
                                    flag2 = true;
                                if(byte0 == Block.stone.blockID || byte0 == Block.dirt.blockID || byte0 == Block.grass.blockID)
                                    if(l3 < 10)
                                    {
                                        abyte0[l2] = (byte)Block.lavaMoving.blockID;
                                    } else
                                    {
                                        abyte0[l2] = 0;
                                        if(flag2 && abyte0[l2 - 1] == Block.dirt.blockID)
                                            abyte0[l2 - 1] = (byte)(ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.dirt.blockID : Block.grass.blockID);
                                    }
                            }
                            l2--;
                        }

                    }

                }

                if(flag)
                    break;
            }

            return;
        } while(true);
    }

    public void populate(IChunkProvider ichunkprovider2, int i1, int j1)
    {
        rand.setSeed((long)i1 * 0x12f88dd3L + (long)j1 * 0x36d41eecL);
        if(mapFeaturesEnabled)
        {
            if (ODNBXlite.Structures[2]){
                strongholdGenerator.generateStructuresInChunk(worldObj, rand, i1, j1);
            }
            if (ODNBXlite.Structures[1]){
                villageGenerator.generateStructuresInChunk(worldObj, rand, i1, j1);
            }
            if (ODNBXlite.Structures[3]){
                mineshaftGenerator.generateStructuresInChunk(worldObj, rand, i1, j1);
            }
        }
        int k = i1 << 4;
        i1 = j1 << 4;
        for(j1 = 0; j1 < 20; j1++)
        {
            int k1 = k + rand.nextInt(16);
            int i3 = rand.nextInt(128);
            int j6 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.dirt.blockID, 32)).generate(theWorld, rand, k1, i3, j6);
        }

        for(j1 = 0; j1 < 10; j1++)
        {
            int l1 = k + rand.nextInt(16);
            int j3 = rand.nextInt(128);
            int k6 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.gravel.blockID, 32)).generate(theWorld, rand, l1, j3, k6);
        }

        for(j1 = 0; j1 < 20; j1++)
        {
            int i2 = k + rand.nextInt(16);
            int k3 = rand.nextInt(128);
            int l6 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreCoal.blockID, 16)).generate(theWorld, rand, i2, k3, l6);
        }

        for(j1 = 0; j1 < 20; j1++)
        {
            int j2 = k + rand.nextInt(16);
            int l3 = rand.nextInt(64);
            int i7 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreIron.blockID, 8)).generate(theWorld, rand, j2, l3, i7);
        }

        if(rand.nextInt(1) == 0)
        {
            j1 = k + rand.nextInt(16);
            int k2 = rand.nextInt(32);
            int i4 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreGold.blockID, 8)).generate(theWorld, rand, j1, k2, i4);
        }
        if(rand.nextInt(4) == 0)
        {
            j1 = k + rand.nextInt(16);
            int l2 = rand.nextInt(16);
            int j4 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreDiamond.blockID, 8)).generate(theWorld, rand, j1, l2, j4);
        }
        if (ODNBXlite.getFlag("newores")){
            for(int i = 0; i < 8; i++)
            {
                int x2 = k + rand.nextInt(16);
                int y2 = rand.nextInt(16);
                int z2 = i1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreRedstone.blockID, 7)).generate(worldObj, rand, x2, y2, z2);
            }
            for(int i = 0; i < 1; i++)
            {
                int x2 = k + rand.nextInt(16);
                int y2 = rand.nextInt(16) + rand.nextInt(16);
                int z2 = i1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreLapis.blockID, 6)).generate(worldObj, rand, x2, y2, z2);
            }
            int max = 0;
            detection: for(int i = k; i < k + 16; i++){
                for(int j = i1; j < i1 + 16; j++){
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
                    int x2 = k + rand.nextInt(16);
                    int y2 = rand.nextInt(28) + 4;
                    int z2 = i1 + rand.nextInt(16);
                    int id = worldObj.getBlockId(x2, y2, z2);
                    if (id == Block.stone.blockID){
                        worldObj.setBlock(x2, y2, z2, Block.oreEmerald.blockID);
                    }
                }
            }
        }

        int trees = (int)(mobSpawnerNoise.func_806_a((double)k * 0.5D, (double)i1 * 0.5D) / 8D + rand.nextDouble() * 4D + 4D);
        if(trees < 0){
            trees = 0;
        }
        if(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS){
            trees += 20;
        }
        OldWorldGenTrees treegen = new OldWorldGenTrees(false);
        if(rand.nextInt(10) == 0)
        {
            trees++;
        }
        for(int k4 = 0; k4 < trees; k4++)
        {
            int j7 = k + rand.nextInt(16) + 8;
            int i9 = i1 + rand.nextInt(16) + 8;
            ((WorldGenerator)treegen).setScale(1.0D, 1.0D, 1.0D);
            treegen.generate(theWorld, rand, j7, theWorld.getHeightValue(j7, i9), i9);
        }

        for(int l4 = 0; l4 < 2; l4++)
        {
            int k7 = k + rand.nextInt(16) + 8;
            int j9 = rand.nextInt(128);
            j1 = i1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantYellow.blockID)).generate(theWorld, rand, k7, j9, j1);
        }

        if(rand.nextInt(2) == 0)
        {
            int i5 = k + rand.nextInt(16) + 8;
            int l7 = rand.nextInt(128);
            int k9 = i1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.plantRed.blockID)).generate(theWorld, rand, i5, l7, k9);
        }
        if(rand.nextInt(4) == 0)
        {
            int j5 = k + rand.nextInt(16) + 8;
            int i8 = rand.nextInt(128);
            int l9 = i1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomBrown.blockID)).generate(theWorld, rand, j5, i8, l9);
        }
        if(rand.nextInt(8) == 0)
        {
            int k5 = k + rand.nextInt(16) + 8;
            int j8 = rand.nextInt(128);
            int i10 = i1 + rand.nextInt(16) + 8;
            (new OldWorldGenFlowers(Block.mushroomRed.blockID)).generate(theWorld, rand, k5, j8, i10);
        }
        for(int l5 = 0; l5 < 50; l5++)
        {
            int k8 = k + rand.nextInt(16) + 8;
            int j10 = rand.nextInt(rand.nextInt(120) + 8);
            j1 = i1 + rand.nextInt(16) + 8;
            (new WorldGenLiquids(ODNBXlite.MapTheme == ODNBXlite.THEME_HELL ? Block.lavaMoving.blockID : Block.waterMoving.blockID)).generate(theWorld, rand, k8, j10, j1);
        }

        for(int i6 = 0; i6 < 20; i6++)
        {
            int l8 = k + rand.nextInt(16) + 8;
            int k10 = rand.nextInt(rand.nextInt(rand.nextInt(112) + 8) + 8);
            j1 = i1 + rand.nextInt(16) + 8;
            (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(theWorld, rand, l8, k10, j1);
        }
    }
}
