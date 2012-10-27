package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.InfdevNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenBigTree;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.nbxlite.mapgens.SuperOldWorldGenMinable;

public class ChunkProviderGenerateInfdev extends ChunkProviderBaseInfinite{
    private InfdevNoiseGeneratorOctaves terrainAlt1Generator;
    private InfdevNoiseGeneratorOctaves terrainAlt2Generator;
    private InfdevNoiseGeneratorOctaves terrainGenerator;
    private InfdevNoiseGeneratorOctaves noiseSandGen;
    private InfdevNoiseGeneratorOctaves rockSandGen;
    private InfdevNoiseGeneratorOctaves treeGen;
    private double field_4180_q[];
    private double terrainMain[];
    private double terrainAlt1[];
    private double terrainAlt2[];
    public MapGenStronghold2 strongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;

    public ChunkProviderGenerateInfdev(World world, long l, boolean flag){
        super(world, l, flag);
        fixLight = true;
        terrainAlt1Generator = new InfdevNoiseGeneratorOctaves(rand, 16);
        terrainAlt2Generator = new InfdevNoiseGeneratorOctaves(rand, 16);
        terrainGenerator = new InfdevNoiseGeneratorOctaves(rand, 8);
        noiseSandGen = new InfdevNoiseGeneratorOctaves(rand, 4);
        rockSandGen = new InfdevNoiseGeneratorOctaves(rand, 4);
        new InfdevNoiseGeneratorOctaves(rand, 5);
        treeGen = new InfdevNoiseGeneratorOctaves(rand, 5);
        if(flag)
        {
            strongholdGenerator = new MapGenStronghold2();
            villageGenerator = new MapGenVillage();
            mineshaftGenerator = new MapGenMineshaft();
        }
        field_4180_q = new double[425];
    }

    protected void generateTerrain(int i, int j, byte abyte0[]){
        byte byte00 = 5;
        byte byte01 = 17;
        byte byte02 = 5;
        terrainMain = terrainGenerator.a(terrainMain, i << 2, 0, j << 2, byte00, byte01, byte02, 8.5551500000000011D, 4.2775750000000006D, 8.5551500000000011D);
        terrainAlt1 = terrainAlt1Generator.a(terrainAlt1, i << 2, 0, j << 2, byte00, byte01, byte02, 684.41200000000003D, 684.41200000000003D, 684.41200000000003D);
        terrainAlt2 = terrainAlt2Generator.a(terrainAlt2, i << 2, 0, j << 2, byte00, byte01, byte02, 684.41200000000003D, 684.41200000000003D, 684.41200000000003D);
        int k2 = 0;
        for(int j3 = 0; j3 < byte00; j3++)
        {
            for(int k3 = 0; k3 < byte02; k3++)
            {
                for(int l3 = 0; l3 < byte01; l3++)
                {
                    double d23 = ((double)l3 - 8.5D) * 12D;
                    if(d23 < 0.0D)
                    {
                        d23 *= 2D;
                    }
                    double d24 = terrainAlt1[k2] / 512D;
                    double d25 = terrainAlt2[k2] / 512D;
                    double d22;
                    double d26 = (terrainMain[k2] / 10D + 1.0D) / 2D;
                    if(d26 < 0.0D)
                    {
                        d22 = d24;
                    } else
                    if(d26 > 1.0D)
                    {
                        d22 = d25;
                    } else
                    {
                        d22 = d24 + (d25 - d24) * d26;
                    }
                    d22 -= d23;
                    field_4180_q[k2] = d22;
                    k2++;
                }

            }

        }

        for(int k1 = 0; k1 < 4; k1++)
        {
            for(int i2 = 0; i2 < 4; i2++)
            {
                for(int l2 = 0; l2 < 16; l2++)
                {
                    double d3 = field_4180_q[(k1 * byte00 + i2) * byte01 + l2];
                    double d5 = field_4180_q[(k1 * byte00 + (i2 + 1)) * byte01 + l2];
                    double d6 = field_4180_q[((k1 + 1) * byte00 + i2) * byte01 + l2];
                    double d7 = field_4180_q[((k1 + 1) * byte00 + (i2 + 1)) * byte01 + l2];
                    double d8 = field_4180_q[(k1 * byte00 + i2) * byte01 + (l2 + 1)];
                    double d9 = field_4180_q[(k1 * byte00 + (i2 + 1)) * byte01 + (l2 + 1)];
                    double d10 = field_4180_q[((k1 + 1) * byte00 + i2) * byte01 + (l2 + 1)];
                    double d11 = field_4180_q[((k1 + 1) * byte00 + (i2 + 1)) * byte01 + (l2 + 1)];
                    for(int k5 = 0; k5 < 8; k5++)
                    {
                        double d12 = (double)k5 / 8D;
                        double d13 = d3 + (d8 - d3) * d12;
                        double d14 = d5 + (d9 - d5) * d12;
                        double d15 = d6 + (d10 - d6) * d12;
                        double d16 = d7 + (d11 - d7) * d12;
                        for(int l5 = 0; l5 < 4; l5++)
                        {
                            double d17 = (double)l5 / 4D;
                            double d18 = d13 + (d15 - d13) * d17;
                            double d19 = d14 + (d16 - d14) * d17;
                            int i6 = l5 + (k1 << 2) << 11 | 0 + (i2 << 2) << 7 | (l2 << 3) + k5;
                            for(int j6 = 0; j6 < 4; j6++)
                            {
                                double d20 = (double)j6 / 4D;
                                double d21 = d18 + (d19 - d18) * d20;
                                int k6 = 0;
                                if((l2 << 3) + k5 < 64)
                                {
                                    if (ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
                                        k6 = Block.lavaStill.blockID;
                                    }else{
                                        k6 = Block.waterStill.blockID;
                                    }
                                }
                                if(d21 > 0.0D)
                                {
                                    k6 = Block.stone.blockID;
                                }
                                abyte0[i6] = (byte)k6;
                                i6 += 128;
                            }

                        }

                    }

                }

            }

        }
    }

    protected void replaceBlocks(int i, int j, byte abyte0[]){
        for(int l1 = 0; l1 < 16; l1++)
        {
            for(int j2 = 0; j2 < 16; j2++)
            {
                double d2 = (i << 4) + l1;
                double d4 = (j << 4) + j2;
                double asd = 0.0D;
                if(ODNBXlite.MapTheme==ODNBXlite.THEME_PARADISE)
                {
                    asd = -0.29999999999999999D;
                }
                boolean flag = noiseSandGen.a(d2 * 0.03125D, d4 * 0.03125D, 0.0D) + rand.nextDouble() * 0.20000000000000001D > asd;
                boolean flag1 = noiseSandGen.a(d4 * 0.03125D, 109.0134D, d2 * 0.03125D) + rand.nextDouble() * 0.20000000000000001D > 3D;
                int i4 = (int)(rockSandGen.func_806_a(d2 * 0.03125D * 2D, d4 * 0.03125D * 2D) / 3D + 3D + rand.nextDouble() * 0.25D);
                int j4 = l1 << 11 | j2 << 7 | 0x7f;
                int k4 = -1;
                int l4;
                if (ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
                    l4 = Block.dirt.blockID;
                }else{
                    l4 = Block.grass.blockID;
                }
                int i5 = Block.dirt.blockID;
                for(int j5 = 127; j5 >= 0; j5--)
                {
                    if(abyte0[j4] == 0)
                    {
                        k4 = -1;
                    } else
                    if(abyte0[j4] == Block.stone.blockID)
                    {
                        if(k4 == -1)
                        {
                            if(i4 <= 0)
                            {
                                l4 = 0;
                                i5 = (byte)Block.stone.blockID;
                            } else
                            if(j5 >= 60 && j5 <= 65)
                            {
                                if (ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
                                    l4 = Block.dirt.blockID;
                                    i5 = Block.dirt.blockID;
                                    if(flag1)
                                    {
                                        l4 = 0;
                                    }
                                    if(flag1)
                                    {
                                        i5 = Block.gravel.blockID;
                                    }
                                    if(flag)
                                    {
                                        l4 = Block.grass.blockID;
                                    }
                                    if(flag)
                                    {
                                        i5 = Block.sand.blockID;
                                    }
                                }else{
                                    l4 = Block.grass.blockID;
                                    i5 = Block.dirt.blockID;
                                    if(flag1)
                                    {
                                        l4 = 0;
                                    }
                                    if(flag1)
                                    {
                                        i5 = Block.gravel.blockID;
                                    }
                                    if(flag)
                                    {
                                        l4 = Block.sand.blockID;
                                    }
                                    if(flag)
                                    {
                                        i5 = Block.sand.blockID;
                                    }
                                }
                            }
                            if(j5 < 64 && l4 == 0)
                            {
                                if (ODNBXlite.MapTheme==ODNBXlite.THEME_HELL){
                                    l4 = Block.lavaStill.blockID;
                                }else{
                                    l4 = Block.waterStill.blockID;
                                }
                            }
                            k4 = i4;
                            if(j5 >= 63)
                            {
                                abyte0[j4] = (byte)l4;
                            } else
                            {
                                abyte0[j4] = (byte)i5;
                            }
                        } else
                        if(k4 > 0)
                        {
                            k4--;
                            abyte0[j4] = (byte)i5;
                        }
                    }
                    j4--;
                }

            }

        }
    }

    protected void generateStructures(int i, int j, byte abyte0[]){
        if(mapFeaturesEnabled){
            mineshaftGenerator.generate(this, worldObj, i, j, abyte0);
            villageGenerator.generate(this, worldObj, i, j, abyte0);
            strongholdGenerator.generate(this, worldObj, i, j, abyte0);
        }
    }

    public void populate(IChunkProvider ichunkprovider, int x, int z){
        rand.setSeed((long)x * 0x12f88dd3L + (long)z * 0x36d41eecL);
        int x1 = x << 4;
        int z1 = z << 4;
        if(mapFeaturesEnabled)
        {
            strongholdGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            villageGenerator.generateStructuresInChunk(worldObj, rand, x, z);
            mineshaftGenerator.generateStructuresInChunk(worldObj, rand, x, z);
        }
        for(int i = 0; i < 20; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(128);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreCoal.blockID,0)).generate_infdev(worldObj, rand, x2, y2, z2);
        }

        for(int i = 0; i < 10; i++)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(64);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreIron.blockID,0)).generate_infdev(worldObj, rand, x2, y2, z2);
        }

        if(rand.nextInt(2) == 0)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(32);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreGold.blockID,0)).generate_infdev(worldObj, rand, x2, y2, z2);
        }
        if(rand.nextInt(8) == 0)
        {
            int x2 = x1 + rand.nextInt(16);
            int y2 = rand.nextInt(16);
            int z2 = z1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreDiamond.blockID,0)).generate_infdev(worldObj, rand, x2, y2, z2);
        }
        if (ODNBXlite.GenerateNewOres){
            for(int i = 0; i < 8; i++)
            {
                int x2 = x1 + rand.nextInt(16);
                int y2 = rand.nextInt(16);
                int z2 = z1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreRedstone.blockID,0)).generate_infdev(worldObj, rand, x2, y2, z2);
            }
            for(int i = 0; i < 1; i++)
            {
                int x2 = x1 + rand.nextInt(16);
                int y2 = rand.nextInt(16) + rand.nextInt(16);
                int z2 = z1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreLapis.blockID,0)).generate_infdev(worldObj, rand, x2, y2, z2);
            }
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
        int trees = (int)(treeGen.func_806_a((double)x1 * 0.050000000000000003D, (double)z1 * 0.050000000000000003D) - rand.nextDouble());
        if(trees < 0)
        {
            trees = 0;
        }
        Object treegen = ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INFDEV0608 ? new OldWorldGenTrees(false) : new OldWorldGenBigTree();
        if(rand.nextInt(100) == 0)
        {
            trees++;
        }
        if(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS)
        {
            trees += 20;
        }
        for(int i = 0; i < trees; i++)
        {
            int x2 = x1 + rand.nextInt(16) + 8;
            int z2 = z1 + rand.nextInt(16) + 8;
            ((WorldGenerator)treegen).setScale(1.0D, 1.0D, 1.0D);
            ((WorldGenerator)treegen).generate(worldObj, rand, x2, worldObj.getHeightValue(x2, z2), z2);
        }
        spawnAnimals(x * 16, z * 16);
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
        if("Stronghold".equals(s) && strongholdGenerator != null){
            return strongholdGenerator.getNearestInstance(world, i, j, k);
        }else{
            return null;
        }
    }
}
