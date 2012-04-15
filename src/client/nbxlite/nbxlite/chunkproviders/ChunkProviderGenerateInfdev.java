package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.InfdevNoiseGeneratorOctaves;
import net.minecraft.src.nbxlite.mapgens.MapGenSkyStronghold;
import net.minecraft.src.nbxlite.mapgens.MapGenStronghold2;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenBigTree;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.nbxlite.mapgens.SuperOldWorldGenMinable;

public class ChunkProviderGenerateInfdev
    implements IChunkProvider
{
    private Random rand;
    private InfdevNoiseGeneratorOctaves b;
    private InfdevNoiseGeneratorOctaves c;
    private InfdevNoiseGeneratorOctaves d;
    private InfdevNoiseGeneratorOctaves e;
    private InfdevNoiseGeneratorOctaves f;
    private InfdevNoiseGeneratorOctaves g;
    private World worldObj;
    private double i[];
    private double j[];
    private double k[];
    private double l[];
    private final boolean mapFeaturesEnabled;
    public MapGenStronghold2 strongholdGenerator;
    public MapGenVillage villageGenerator;
    public MapGenMineshaft mineshaftGenerator;

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        return null;
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
    {
 /*       if("Stronghold".equals(s) && strongholdGenerator != null)
        {
            return strongholdGenerator.getNearestInstance(world, i, j, k);
        } else
        {
            return null;
        }*/
        return null;
    }

    public ChunkProviderGenerateInfdev(World world, long l, boolean flag)
    {
        worldObj = world;
        rand = new Random(l);
        new Random(l);
        b = new InfdevNoiseGeneratorOctaves(rand, 16);
        c = new InfdevNoiseGeneratorOctaves(rand, 16);
        d = new InfdevNoiseGeneratorOctaves(rand, 8);
        e = new InfdevNoiseGeneratorOctaves(rand, 4);
        f = new InfdevNoiseGeneratorOctaves(rand, 4);
        new InfdevNoiseGeneratorOctaves(rand, 5);
        g = new InfdevNoiseGeneratorOctaves(rand, 5);
        if(flag)
        {
            strongholdGenerator = new MapGenSkyStronghold();
            villageGenerator = new MapGenVillage(0);
            mineshaftGenerator = new MapGenMineshaft();
        }
        mapFeaturesEnabled = flag;
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i1, int j1)
    {
        rand.setSeed((long)i1 * 0x4f9939f508L + (long)j1 * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        byte byte0 = 5;
        byte0 = 17;
        byte0 = 5;
        int i3 = j1 << 2;
        byte0 = 0;
        int k2 = i1 << 2;
        double ad[] = i;
        if(ad == null)
        {
            ad = new double[425];
        }
        j = d.a(j, k2, 0, i3, 5, 17, 5, 8.5551500000000011D, 4.2775750000000006D, 8.5551500000000011D);
        k = b.a(k, k2, 0, i3, 5, 17, 5, 684.41200000000003D, 684.41200000000003D, 684.41200000000003D);
        l = c.a(l, k2, 0, i3, 5, 17, 5, 684.41200000000003D, 684.41200000000003D, 684.41200000000003D);
        k2 = 0;
        for(int j3 = 0; j3 < 5; j3++)
        {
            for(int k3 = 0; k3 < 5; k3++)
            {
                for(int l3 = 0; l3 < 17; l3++)
                {
                    double d23;
                    if((d23 = ((double)l3 - 8.5D) * 12D) < 0.0D)
                    {
                        d23 *= 2D;
                    }
                    double d24 = k[k2] / 512D;
                    double d25 = l[k2] / 512D;
                    double d22;
                    double d26;
                    if((d26 = (j[k2] / 10D + 1.0D) / 2D) < 0.0D)
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
                    ad[k2] = d22;
                    k2++;
                }

            }

        }

        i = ad;
        for(int k1 = 0; k1 < 4; k1++)
        {
            for(int i2 = 0; i2 < 4; i2++)
            {
                for(int l2 = 0; l2 < 16; l2++)
                {
                    double d3 = i[(k1 * 5 + i2) * 17 + l2];
                    double d5 = i[(k1 * 5 + (i2 + 1)) * 17 + l2];
                    double d6 = i[((k1 + 1) * 5 + i2) * 17 + l2];
                    double d7 = i[((k1 + 1) * 5 + (i2 + 1)) * 17 + l2];
                    double d8 = i[(k1 * 5 + i2) * 17 + (l2 + 1)];
                    double d9 = i[(k1 * 5 + (i2 + 1)) * 17 + (l2 + 1)];
                    double d10 = i[((k1 + 1) * 5 + i2) * 17 + (l2 + 1)];
                    double d11 = i[((k1 + 1) * 5 + (i2 + 1)) * 17 + (l2 + 1)];
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
                                    if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
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

        for(int l1 = 0; l1 < 16; l1++)
        {
            for(int j2 = 0; j2 < 16; j2++)
            {
                double d2 = (i1 << 4) + l1;
                double d4 = (j1 << 4) + j2;
                double asd = 0.0D;
                if(mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_PARADISE)
                {
                    asd = -0.29999999999999999D;
                }
                boolean flag = e.a(d2 * 0.03125D, d4 * 0.03125D, 0.0D) + rand.nextDouble() * 0.20000000000000001D > asd;
                boolean flag1 = e.a(d4 * 0.03125D, 109.0134D, d2 * 0.03125D) + rand.nextDouble() * 0.20000000000000001D > 3D;
                int i4 = (int)(f.func_806_a(d2 * 0.03125D * 2D, d4 * 0.03125D * 2D) / 3D + 3D + rand.nextDouble() * 0.25D);
                int j4 = l1 << 11 | j2 << 7 | 0x7f;
                int k4 = -1;
                int l4;
                if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
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
                                if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
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
                                if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
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
        if(mapFeaturesEnabled)
        {
            mineshaftGenerator.generate(this, worldObj, i1, j1, abyte0);
            villageGenerator.generate(this, worldObj, i1, j1, abyte0);
            strongholdGenerator.generate(this, worldObj, i1, j1, abyte0);
        }
        Chunk chunk = new Chunk(worldObj, abyte0, i1, j1);
        chunk.generateSkylightMap();
        return chunk;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider2, int i1, int j1)
    {
        rand.setSeed((long)i1 * 0x12f88dd3L + (long)j1 * 0x36d41eecL);
        int ii1 = i1 << 4;
        i1 = j1 << 4;
        for(j1 = 0; j1 < 20; j1++)
        {
            int k1 = ii1 + rand.nextInt(16);
            int k2 = rand.nextInt(128);
            int l3 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreCoal.blockID,0)).generate_infdev(worldObj, rand, k1, k2, l3);
        }

        for(j1 = 0; j1 < 10; j1++)
        {
            int l1 = ii1 + rand.nextInt(16);
            int l2 = rand.nextInt(64);
            int i4 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreIron.blockID,0)).generate_infdev(worldObj, rand, l1, l2, i4);
        }

        if(rand.nextInt(2) == 0)
        {
            j1 = ii1 + rand.nextInt(16);
            int i2 = rand.nextInt(32);
            int i3 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreGold.blockID,0)).generate_infdev(worldObj, rand, j1, i2, i3);
        }
        if(rand.nextInt(8) == 0)
        {
            j1 = ii1 + rand.nextInt(16);
            int j2 = rand.nextInt(16);
            int j3 = i1 + rand.nextInt(16);
            (new SuperOldWorldGenMinable(Block.oreDiamond.blockID,0)).generate_infdev(worldObj, rand, j1, j2, j3);
        }
        if (mod_noBiomesX.GenerateNewOres){
            for(int j3 = 0; j3 < 8; j3++)
            {
                int l5 = ii1 + rand.nextInt(16);
                int j8 = rand.nextInt(16);
                int k10 = i1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreRedstone.blockID,0)).generate_infdev(worldObj, rand, l5, j8, k10);
            }
            for(int k13 = 0; k13 < 1; k13++)
            {
                int j16 = ii1 + rand.nextInt(16);
                int i19 = rand.nextInt(16) + rand.nextInt(16);
                int l20 = i1 + rand.nextInt(16);
                (new SuperOldWorldGenMinable(Block.oreLapis.blockID,0)).generate_infdev(worldObj, rand, j16, i19, l20);
            }
        }
        if((j1 = (int)(g.func_806_a((double)ii1 * 0.050000000000000003D, (double)i1 * 0.050000000000000003D) - rand.nextDouble())) < 0)
        {
            j1 = 0;
        }
        Object treegen = new OldWorldGenBigTree();
        if(mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0608)
        {
            treegen = new OldWorldGenTrees(false);
        }
        if(rand.nextInt(100) == 0)
        {
            j1++;
        }
        if(mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_WOODS)
        {
            j1 += 20;
        }
        for(int k3 = 0; k3 < j1; k3++)
        {
            int j4 = ii1 + rand.nextInt(16) + 8;
            int k4 = i1 + rand.nextInt(16) + 8;
            ((WorldGenerator)treegen).setScale(1.0D, 1.0D, 1.0D);
            ((WorldGenerator)treegen).generate(worldObj, rand, j4, worldObj.getHeightValue(j4, k4), k4);
        }
        if (mod_noBiomesX.UseNewSpawning){
            BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt((i1 * 16) + 16, (j1 * 16) + 16);
            SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, (i1 * 16) + 8, (j1 * 16) + 8, 16, 16, rand);
        }
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
