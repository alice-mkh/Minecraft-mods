package net.minecraft.src.nbxlite.indev;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import net.minecraft.src.nbxlite.indev.noise.*;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Block;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.IProgressUpdate;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.StatCollector;
import net.minecraft.src.Material;
import net.minecraft.src.mod_noBiomesX;

public final class IndevGenerator
{
    private IProgressUpdate progressupdate;
    private int width;
    private int length;
    private int height;
    private Random rand;
    public byte blocks[];
    private byte[] e;
    private int k;
    private int l;
    public boolean island;
    public boolean floating;
    public boolean flat;
    public int theme;
    private int m;
    private int n;
    private float o;
    private int p[];
    public int spawnX;
    public int spawnY;
    public int spawnZ;

    public IndevGenerator(IProgressUpdate iprogressupdate, long seed)
    {
        rand = new Random(seed);
        island = false;
        floating = false;
        flat = false;
        o = 0.0F;
        p = new int[0x100000];
        progressupdate = iprogressupdate;
    }

    public IndevGenerator(long seed)
    {
        this(new LoadingScreenRenderer(ModLoader.getMinecraftInstance()), seed);
    }

    public final byte[] generateLevel(String s, int i1, int j1, int k1)
    {
        int l1 = 1;
        if(floating)
        {
            l1 = (k1 - 64) / 48 + 1;
        }
        n = 13 + l1 * 4;
        progressupdate.displaySavingString(StatCollector.translateToLocal("menu.generatingLevel"));
        IndevLevel world = new IndevLevel();
        world.waterLevel = k;
        world.t = l;
        world.a = i1;
        world.b = j1;
        world.c = k1;
        this.width = i1;
        this.length = j1;
        this.height = k1;
        blocks = new byte[(i1 * j1 * k1)];
        for(int i2 = 0; i2 < l1; i2++)
        {
            k = k1 - 32 - i2 * 48;
            l = k - 2;
            int ai[];
            if(flat)
            {
                ai = new int[i1 * j1];
                for(int k3 = 0; k3 < ai.length; k3++)
                {
                    ai[k3] = 0;
                }

                nextPhase();
                nextPhase();
            } else
            {
                progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.raising"));
                nextPhase();
                IndevGenerator a1 = this;
                IndevNoiseGenerator2 d1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(a1.rand, 8), new IndevNoiseGeneratorOctaves(a1.rand, 8));
                IndevNoiseGenerator2 d2 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(a1.rand, 8), new IndevNoiseGeneratorOctaves(a1.rand, 8));
                Object obj = new IndevNoiseGeneratorOctaves(a1.rand, 6);
                IndevNoiseGeneratorOctaves c1 = new IndevNoiseGeneratorOctaves(a1.rand, 2);
                int ai3[] = new int[a1.width * a1.length];
                for(int j7 = 0; j7 < a1.width; j7++)
                {
                    double d5 = Math.abs(((double)j7 / ((double)a1.width - 1.0D) - 0.5D) * 2D);
                    a1.a(((float)j7 * 100F) / (float)(a1.width - 1));
                    for(int k8 = 0; k8 < a1.length; k8++)
                    {
                        double d7 = Math.abs(((double)k8 / ((double)a1.length - 1.0D) - 0.5D) * 2D);
                        double d9 = d1.a((float)j7 * 1.3F, (float)k8 * 1.3F) / 6D + -4D;
                        double d10 = d2.a((float)j7 * 1.3F, (float)k8 * 1.3F) / 5D + 10D + -4D;
                        double d11;
                        if((d11 = ((IndevNoiseGenerator) (obj)).a(j7, k8) / 8D) > 0.0D)
                        {
                            d10 = d9;
                        }
                        double d13 = Math.max(d9, d10) / 2D;
                        if(a1.island)
                        {
                            double d14 = Math.sqrt(d5 * d5 + d7 * d7) * 1.2000000476837158D;
                            double d15 = c1.a((float)j7 * 0.05F, (float)k8 * 0.05F) / 4D + 1.0D;
                            if((d14 = Math.max(d14 = Math.min(d14, d15), Math.max(d5, d7))) > 1.0D)
                            {
                                d14 = 1.0D;
                            }
                            if(d14 < 0.0D)
                            {
                                d14 = 0.0D;
                            }
                            d14 *= d14;
                            if((d13 = (d13 * (1.0D - d14) - d14 * 10D) + 5D) < 0.0D)
                            {
                                d13 -= d13 * d13 * 0.20000000298023224D;
                            }
                        } else
                        if(d13 < 0.0D)
                        {
                            d13 *= 0.80000000000000004D;
                        }
                        ai3[j7 + k8 * a1.width] = (int)d13;
                    }

                }

                ai = ai3;
                progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.eroding"));
                nextPhase();
                int ai1[] = ai;
                a1 = this;
                d2 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(a1.rand, 8), new IndevNoiseGeneratorOctaves(a1.rand, 8));
                obj = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(a1.rand, 8), new IndevNoiseGeneratorOctaves(a1.rand, 8));
                for(int l4 = 0; l4 < a1.width; l4++)
                {
                    a1.a(((float)l4 * 100F) / (float)(a1.width - 1));
                    for(int k5 = 0; k5 < a1.length; k5++)
                    {
                        double d3 = d2.a(l4 << 1, k5 << 1) / 8D;
                        int k7 = ((IndevNoiseGenerator) (obj)).a(l4 << 1, k5 << 1) <= 0.0D ? 0 : 1;
                        if(d3 > 2D)
                        {
                            int i8;
                            i8 = (((i8 = ai1[l4 + k5 * a1.width]) - k7) / 2 << 1) + k7;
                            ai1[l4 + k5 * a1.width] = i8;
                        }
                    }

                }

            }
            progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.soiling"));
            nextPhase();
            int ai2[] = ai;
            IndevGenerator a2 = this;
            int i4 = a2.width;
            int j4 = a2.length;
            int i5 = a2.height;
            IndevNoiseGeneratorOctaves c3 = new IndevNoiseGeneratorOctaves(a2.rand, 8);
            IndevNoiseGeneratorOctaves c4 = new IndevNoiseGeneratorOctaves(a2.rand, 8);
            for(int k6 = 0; k6 < i4; k6++)
            {
                double d4 = Math.abs(((double)k6 / ((double)i4 - 1.0D) - 0.5D) * 2D);
                a2.a(((float)k6 * 100F) / (float)(i4 - 1));
                for(int j8 = 0; j8 < j4; j8++)
                {
                    double d6 = Math.abs(((double)j8 / ((double)j4 - 1.0D) - 0.5D) * 2D);
                    double d8;
                    d8 = (d8 = Math.max(d4, d6)) * d8 * d8;
                    int i10 = (int)(c3.a(k6, j8) / 24D) - 4;
                    int j10;
                    int k10 = (j10 = ai2[k6 + j8 * i4] + a2.k) + i10;
                    ai2[k6 + j8 * i4] = Math.max(j10, k10);
                    if(ai2[k6 + j8 * i4] > i5 - 2)
                    {
                        ai2[k6 + j8 * i4] = i5 - 2;
                    }
                    if(ai2[k6 + j8 * i4] <= 0)
                    {
                        ai2[k6 + j8 * i4] = 1;
                    }
                    double d12;
                    int i11;
                    if((i11 = (int)((double)(i11 = (int)(Math.sqrt(Math.abs(d12 = c4.a((double)k6 * 2.2999999999999998D, (double)j8 * 2.2999999999999998D) / 24D)) * Math.signum(d12) * 20D) + a2.k) * (1.0D - d8) + d8 * (double)a2.height)) > a2.k)
                    {
                        i11 = a2.height;
                    }
                    for(int j11 = 0; j11 < i5; j11++)
                    {
                        int k11 = (j11 * a2.length + j8) * a2.width + k6;
                        int l11 = 0;
                        if(j11 <= j10)
                        {
                            l11 = Block.dirt.blockID;
                        }
                        if(j11 <= k10)
                        {
                            l11 = Block.stone.blockID;
                        }
                        if(a2.floating && j11 < i11)
                        {
                            l11 = 0;
                        }
                        if(a2.blocks[k11] == 0)
                        {
                            a2.blocks[k11] = (byte)l11;
                        }
                    }

                }

            }

            progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.growing"));
            nextPhase();
            ai2 = ai;
            a2 = this;
            i4 = a2.width;
            j4 = a2.length;
            IndevNoiseGeneratorOctaves c2 = new IndevNoiseGeneratorOctaves(a2.rand, 8);
            c3 = new IndevNoiseGeneratorOctaves(a2.rand, 8);
            int i6 = a2.k - 1;
            if(a2.theme == 3)
            {
                i6 += 2;
            }
            for(int l6 = 0; l6 < i4; l6++)
            {
                a2.a(((float)l6 * 100F) / (float)(i4 - 1));
                for(int l7 = 0; l7 < j4; l7++)
                {
                    boolean flag = c2.a(l6, l7) > 8D;
                    if(a2.island)
                    {
                        flag = c2.a(l6, l7) > -8D;
                    }
                    if(a2.theme == 3)
                    {
                        flag = c2.a(l6, l7) > -32D;
                    }
                    boolean flag1 = c3.a(l6, l7) > 12D;
                    if(a2.theme == 1 || a2.theme == 2)
                    {
                        flag = c2.a(l6, l7) > -8D;
                    }
                    int l8;
                    int j9 = ((l8 = ai2[l6 + l7 * i4]) * a2.length + l7) * a2.width + l6;
                    int k9;
                    if(((k9 = a2.blocks[((l8 + 1) * a2.length + l7) * a2.width + l6] & 0xff) == Block.waterMoving.blockID || k9 == Block.waterStill.blockID || k9 == 0) && l8 <= a2.k - 1 && flag1)
                    {
                        a2.blocks[j9] = (byte)Block.gravel.blockID;
                    }
                    if(k9 != 0)
                    {
                        continue;
                    }
                    int l9 = -1;
                    if(l8 <= i6 && flag)
                    {
                        l9 = Block.sand.blockID;
                        if(a2.theme == 1)
                        {
                            l9 = Block.grass.blockID;
                        }
                    }
                    if(a2.blocks[j9] != 0 && l9 > 0)
                    {
                        a2.blocks[j9] = (byte)l9;
                    }
                }

            }

        }

        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.carving"));
        nextPhase();
        IndevGenerator a3 = this;
        int k4 = a3.width;
        int j5 = a3.length;
        int l5 = a3.height;
        int j6 = (k4 * j5 * l5) / 256 / 64 << 1;
        for(int i7 = 0; i7 < j6; i7++)
        {
            a3.a(((float)i7 * 100F) / (float)(j6 - 1));
            float f4 = a3.rand.nextFloat() * (float)k4;
            float f5 = a3.rand.nextFloat() * (float)l5;
            float f6 = a3.rand.nextFloat() * (float)j5;
            int i9 = (int)((a3.rand.nextFloat() + a3.rand.nextFloat()) * 200F);
            float f7 = a3.rand.nextFloat() * 3.141593F * 2.0F;
            float f8 = 0.0F;
            float f9 = a3.rand.nextFloat() * 3.141593F * 2.0F;
            float f10 = 0.0F;
            float f11 = a3.rand.nextFloat() * a3.rand.nextFloat();
label0:
            for(int l10 = 0; l10 < i9; l10++)
            {
                f4 += MathHelper.sin(f7) * MathHelper.cos(f9);
                f6 += MathHelper.cos(f7) * MathHelper.cos(f9);
                f5 += MathHelper.sin(f9);
                f7 += f8 * 0.2F;
                f8 = (f8 *= 0.9F) + (a3.rand.nextFloat() - a3.rand.nextFloat());
                f9 = (f9 += f10 * 0.5F) * 0.5F;
                f10 = (f10 *= 0.75F) + (a3.rand.nextFloat() - a3.rand.nextFloat());
                if(a3.rand.nextFloat() < 0.25F)
                {
                    continue;
                }
                float f12 = f4 + (a3.rand.nextFloat() * 4F - 2.0F) * 0.2F;
                float f13 = f5 + (a3.rand.nextFloat() * 4F - 2.0F) * 0.2F;
                float f14 = f6 + (a3.rand.nextFloat() * 4F - 2.0F) * 0.2F;
                float f15 = ((float)a3.height - f13) / (float)a3.height;
                float f16 = 1.2F + (f15 * 3.5F + 1.0F) * f11;
                float f17 = MathHelper.sin(((float)l10 * 3.141593F) / (float)i9) * f16;
                l1 = (int)(f12 - f17);
                do
                {
                    if(l1 > (int)(f12 + f17))
                    {
                        continue label0;
                    }
                    for(int i12 = (int)(f13 - f17); i12 <= (int)(f13 + f17); i12++)
                    {
                        for(int j12 = (int)(f14 - f17); j12 <= (int)(f14 + f17); j12++)
                        {
                            float f1 = (float)l1 - f12;
                            float f2 = (float)i12 - f13;
                            float f3 = (float)j12 - f14;
                            if((f1 = f1 * f1 + f2 * f2 * 2.0F + f3 * f3) >= f17 * f17 || l1 <= 0 || i12 <= 0 || j12 <= 0 || l1 >= a3.width - 1 || i12 >= a3.height - 1 || j12 >= a3.length - 1)
                            {
                                continue;
                            }
                            f1 = (i12 * a3.length + j12) * a3.width + l1;
                            if(a3.blocks[(int)f1] == Block.stone.blockID)
                            {
                                a3.blocks[(int)f1] = 0;
                            }
                        }

                    }

                    l1++;
                } while(true);
            }

        }

        int j2 = a(Block.oreCoal.blockID, 1000, 10, (k1 << 2) / 5);
        int j3 = a(Block.oreIron.blockID, 800, 8, (k1 * 3) / 5);
        int l3 = a(Block.oreGold.blockID, 500, 6, (k1 << 1) / 5);
        l1 = a(Block.oreDiamond.blockID, 800, 2, k1 / 5);
        if (mod_noBiomesX.GenerateNewOres){
            int redstone = a(Block.oreRedstone.blockID, 800, 4, (k1 << 1) / 5);
            int lapis = a(Block.oreLapis.blockID, 600, 4, (k1 << 1) / 5);
            System.out.println("Coal: "+j2+", Iron: "+j3+", Lapis: "+lapis+", Gold: "+l3+", Redstone: "+redstone+", Diamond: "+l1);
        }else{
            System.out.println("Coal: "+j2+", Iron: "+j3+", Gold: "+l3+", Diamond: "+l1);
        }
        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.melting"));
        nextPhase();
        c();
        world.u = k1 + 2;
        if(floating)
        {
            l = -128;
            k = l + 1;
            world.u = -16;
        } else
        if(!island)
        {
            l = k + 1;
            k = l - 16;
        } else
        {
            l = k - 9;
        }
        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.watering"));
        nextPhase();
        a();
        if(!floating)
        {
            l1 = Block.waterStill.blockID;
            if(theme == 1)
            {
                l1 = Block.lavaStill.blockID;
            }
            for(int k2 = 0; k2 < i1; k2++)
            {
                fillWithLiquid(k2, k - 1, 0, 0, l1);
                fillWithLiquid(k2, k - 1, j1 - 1, 0, l1);
            }

            for(int l2 = 0; l2 < j1; l2++)
            {
                fillWithLiquid(i1 - 1, k - 1, l2, 0, l1);
                fillWithLiquid(0, k - 1, l2, 0, l1);
            }

        }
        if(theme == 0)
        {
            world.v = 0x99ccff;
            world.w = 0xffffff;
            world.x = 0xffffff;
        }
        if(theme == 1)
        {
            world.x = 0x210800;
            world.w = 0x100400;
            world.v = 0x100400;
            world.B = world.A = 7;
            world.m = Block.lavaStill.blockID;
            if(floating)
            {
                world.u = k1 + 2;
                k = -16;
            }
        }
        if(theme == 3)
        {
            world.v = 0xc6deff;
            world.w = 0xc6deff;
            world.x = 0xeeeeff;
            world.B = world.A = 15;
            world.A = 16;
            world.u = k1 + 64;
        }
        if(theme == 2)
        {
            world.v = 0x757d87;
            world.w = 0x4d5a5b;
            world.x = 0x4d5a5b;
            world.B = world.A = 12;
        }
        world.waterLevel = k;
        world.t = l;
        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.assembling"));
        nextPhase();
        a(0.0F);
        /*world.*/setData(world, i1, k1, j1, blocks, null);
        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.building"));
        nextPhase();
        a(0.0F);
        world.a();
        spawnHouse(world);
        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.planting"));
        nextPhase();
        if(theme != 1)
        {
            generateGrass(world);
        }
        nextPhase();
        generateTrees(world);
        if(theme == 2)
        {
            for(l1 = 0; l1 < 50; l1++)
            {
                generateTrees(world);
            }

        }
        l1 = 100;
        if(theme == 3)
        {
            l1 = 1000;
        }
        nextPhase();
        generateFlowers(world, Block.plantYellow, l1);
        nextPhase();
        generateFlowers(world, Block.plantRed, l1);
        nextPhase();
        generateFlowers(world, Block.mushroomBrown, 50);
        nextPhase();
        generateFlowers(world, Block.mushroomRed, 50);
        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.lighting"));
        nextPhase();
        for(int i3 = 0; i3 < 10000; i3++)
        {
            a((i3 * 100) / 10000);
            world.d();
        }

        progressupdate.displayLoadingString(mod_noBiomesX.lang.get("indev.spawning"));
        nextPhase();
/*
        net.minecraft.a.a.b b1 = new net.minecraft.a.a.b(world);
        for(i1 = 0; i1 < 1000; i1++)
        {
            a(((float)i1 * 100F) / 999F);
            b1.a();
        }
*/
        world.createTime = System.currentTimeMillis();
        world.creator = s;
        world.name = "A Nice World";
        if(m != n)
        {
            throw new IllegalStateException((new StringBuilder()+"Wrong number of phases! Wanted "+n+", got "+m).toString());
        } else
        {
//             return world;
            return this.blocks;
        }
    }
    
    public boolean isOpaque(int id){
        if (id == Block.leaves.blockID){
            return false;
        }
        return Block.opaqueCubeLookup[id];
    }

    public void spawnHouse(IndevLevel world)
    {
        int i1 = world.i;
        int j1 = world.j;
        int k1 = world.k;
        for(int l1 = i1 - 3; l1 <= i1 + 3; l1++)
        {
            for(int i2 = j1 - 2; i2 <= j1 + 2; i2++)
            {
                for(int j2 = k1 - 3; j2 <= k1 + 3; j2++)
                {
                    int k2 = i2 >= j1 - 1 ? 0 : Block.obsidian.blockID;
                    if(l1 == i1 - 3 || j2 == k1 - 3 || l1 == i1 + 3 || j2 == k1 + 3 || i2 == j1 - 2 || i2 == j1 + 2)
                    {
                        k2 = Block.stone.blockID;
                        if(i2 >= j1 - 1)
                        {
                            k2 = Block.planks.blockID;
                        }
                    }
                    if(j2 == k1 - 3 && l1 == i1 && i2 >= j1 - 1 && i2 <= j1)
                    {
                        k2 = 0;
                    }
                    setBlock(l1, i2, j2, k2);
                }

            }

        }

//         setBlock((i1 - 3) + 1, j1, k1, Block.torchWood.blockID);
//         setBlock((i1 + 3) - 1, j1, k1, Block.torchWood.blockID);
        spawnX = world.i;
        spawnY = world.j-3;
        spawnZ = world.k;
    }

    private void generateGrass(IndevLevel world)
    {
        for(int i1 = 0; i1 < width; i1++)
        {
            a(((float)i1 * 100F) / (float)(width - 1));
            for(int j1 = 0; j1 < height; j1++)
            {
                for(int k1 = 0; k1 < length; k1++)
                {
                    if(getBlockId(i1, j1, k1) == Block.dirt.blockID && getLightLevel(world, i1, j1 + 1, k1) >= 4 && getBlockId(i1, j1 + 1, k1) == 0)
                    {
                        setBlock(i1, j1, k1, Block.grass.blockID);
                    }
                }

            }

        }

    }

    private void generateTrees(IndevLevel world)
    {
        int i1 = (width * length * height) / 0x13880;
        for(int j1 = 0; j1 < i1; j1++)
        {
            if(j1 % 100 == 0)
            {
                a(((float)j1 * 100F) / (float)(i1 - 1));
            }
            int k1 = rand.nextInt(width);
            int l1 = rand.nextInt(height);
            int i2 = rand.nextInt(length);
            for(int j2 = 0; j2 < 25; j2++)
            {
                int k2 = k1;
                int l2 = l1;
                int i3 = i2;
                for(int j3 = 0; j3 < 20; j3++)
                {
                    k2 += rand.nextInt(12) - rand.nextInt(12);
                    l2 += rand.nextInt(3) - rand.nextInt(6);
                    i3 += rand.nextInt(12) - rand.nextInt(12);
                    if(k2 >= 0 && l2 >= 0 && i3 >= 0 && k2 < width && l2 < height && i3 < length)
                    {
                        maybeGrowTree(world, k2, l2, i3);
                    }
                }

            }

        }

    }

    private void generateFlowers(IndevLevel world, BlockFlower flower, int i1)
    {
        i1 = (int)(((long)width * (long)length * (long)height * (long)i1) / 0x186a00L);
        for(int j1 = 0; j1 < i1; j1++)
        {
            if(j1 % 100 == 0)
            {
                a(((float)j1 * 100F) / (float)(i1 - 1));
            }
            int k1 = rand.nextInt(width);
            int l1 = rand.nextInt(height);
            int i2 = rand.nextInt(length);
            for(int j2 = 0; j2 < 10; j2++)
            {
                int k2 = k1;
                int l2 = l1;
                int i3 = i2;
                for(int j3 = 0; j3 < 10; j3++)
                {
                    k2 += rand.nextInt(4) - rand.nextInt(4);
                    l2 += rand.nextInt(2) - rand.nextInt(2);
                    i3 += rand.nextInt(4) - rand.nextInt(4);
                    if(k2 >= 0 && i3 >= 0 && l2 > 0 && k2 < width && i3 < length && l2 < height && getBlockId(k2, l2, i3) == 0 && canFlowerStay(flower, world, k2, l2, i3))
                    {
                        setBlock(k2, l2, i3, flower.blockID);
                    }
                }

            }

        }

    }

    private int a(int i1, int j1, int k1, int l1)
    {
        int i2 = 0;
        i1 = (byte)i1;
        int j2 = width;
        int k2 = length;
        int l2 = height;
        j1 = (((j2 * k2 * l2) / 256 / 64) * j1) / 100;
label0:
        for(int i3 = 0; i3 < j1; i3++)
        {
            a(((float)i3 * 100F) / (float)(j1 - 1));
            float f1 = rand.nextFloat() * (float)j2;
            float f2 = rand.nextFloat() * (float)l2;
            float f3 = rand.nextFloat() * (float)k2;
            if(f2 > (float)l1)
            {
                continue;
            }
            int j3 = (int)(((rand.nextFloat() + rand.nextFloat()) * 75F * (float)k1) / 100F);
            float f4 = rand.nextFloat() * 3.141593F * 2.0F;
            float f5 = 0.0F;
            float f6 = rand.nextFloat() * 3.141593F * 2.0F;
            float f7 = 0.0F;
            int k3 = 0;
            do
            {
                if(k3 >= j3)
                {
                    continue label0;
                }
                f1 += MathHelper.sin(f4) * MathHelper.cos(f6);
                f3 += MathHelper.cos(f4) * MathHelper.cos(f6);
                f2 += MathHelper.sin(f6);
                f4 += f5 * 0.2F;
                f5 = (f5 *= 0.9F) + (rand.nextFloat() - rand.nextFloat());
                f6 = (f6 += f7 * 0.5F) * 0.5F;
                f7 = (f7 *= 0.9F) + (rand.nextFloat() - rand.nextFloat());
                float f8 = (MathHelper.sin(((float)k3 * 3.141593F) / (float)j3) * (float)k1) / 100F + 1.0F;
                for(int l3 = (int)(f1 - f8); l3 <= (int)(f1 + f8); l3++)
                {
                    for(int i4 = (int)(f2 - f8); i4 <= (int)(f2 + f8); i4++)
                    {
                        for(int j4 = (int)(f3 - f8); j4 <= (int)(f3 + f8); j4++)
                        {
                            float f9 = (float)l3 - f1;
                            float f10 = (float)i4 - f2;
                            float f11 = (float)j4 - f3;
                            if((f9 = f9 * f9 + f10 * f10 * 2.0F + f11 * f11) >= f8 * f8 || l3 <= 0 || i4 <= 0 || j4 <= 0 || l3 >= width - 1 || i4 >= height - 1 || j4 >= length - 1)
                            {
                                continue;
                            }
                            f9 = (i4 * length + j4) * width + l3;
                            if(blocks[(int)f9] == Block.stone.blockID)
                            {
                                blocks[(int)f9] = (byte)i1;
                                i2++;
                            }
                        }

                    }

                }

                k3++;
            } while(true);
        }

        return i2;
    }

    private void a()
    {
        int i1 = Block.waterStill.blockID;
        if(theme == 1)
        {
            i1 = Block.lavaStill.blockID;
        }
        int j1 = (width * length * height) / 1000;
        for(int k1 = 0; k1 < j1; k1++)
        {
            if(k1 % 100 == 0)
            {
                a(((float)k1 * 100F) / (float)(j1 - 1));
            }
            int l1 = rand.nextInt(width);
            int i2 = rand.nextInt(height);
            int j2 = rand.nextInt(length);
            if(blocks[(i2 * length + j2) * width + l1] != 0)
            {
                continue;
            }
            long l2;
            if((l2 = fillWithLiquid(l1, i2, j2, 0, 255)) > 0L && l2 < 640L)
            {
                fillWithLiquid(l1, i2, j2, 255, i1);
            } else
            {
                fillWithLiquid(l1, i2, j2, 255, 0);
            }
        }

        a(100F);
    }

    private void nextPhase()
    {
        m++;
        o = 0.0F;
        a(0.0F);
    }

    private void a(float f1)
    {
        if(f1 < 0.0F)
        {
            throw new IllegalStateException("Failed to set next phase!");
        } else
        {
            f1 = (int)((((float)(m - 1) + f1 / 100F) * 100F) / (float)n);
            progressupdate.setLoadingProgress((int)f1);
            return;
        }
    }

    private void c()
    {
        int i1 = (width * length * height) / 2000;
        int j1 = l;
        for(int k1 = 0; k1 < i1; k1++)
        {
            if(k1 % 100 == 0)
            {
                a(((float)k1 * 100F) / (float)(i1 - 1));
            }
            int l1 = rand.nextInt(width);
            int i2 = Math.min(Math.min(rand.nextInt(j1), rand.nextInt(j1)), Math.min(rand.nextInt(j1), rand.nextInt(j1)));
            int j2 = rand.nextInt(length);
            if(blocks[(i2 * length + j2) * width + l1] != 0)
            {
                continue;
            }
            long l2;
            if((l2 = fillWithLiquid(l1, i2, j2, 0, 255)) > 0L && l2 < 640L)
            {
                fillWithLiquid(l1, i2, j2, 255, Block.lavaStill.blockID);
            } else
            {
                fillWithLiquid(l1, i2, j2, 255, 0);
            }
        }

        a(100F);
    }

    private long fillWithLiquid(int i1, int j1, int k1, int l1, int i2)
    {
        byte byte0 = (byte)i2;
        l1 = (byte)l1;
        ArrayList arraylist = new ArrayList();
        int j2 = 0;
        int k2 = 1;
        int l2 = 1;
        for(; 1 << k2 < width; k2++) { }
        for(; 1 << l2 < length; l2++) { }
        int i3 = length - 1;
        int j3 = width - 1;
        j2++;
        p[0] = ((j1 << l2) + k1 << k2) + i1;
        long l3 = 0L;
        i1 = width * length;
        while(j2 > 0) 
        {
            j1 = p[--j2];
            if(j2 == 0 && arraylist.size() > 0)
            {
                p = (int[])arraylist.remove(arraylist.size() - 1);
                j2 = p.length;
            }
            k1 = j1 >> k2 & i3;
            int k3 = j1 >> k2 + l2;
            int i4;
            int j4 = i4 = j1 & j3;
            for(; i4 > 0 && blocks[j1 - 1] == l1; j1--)
            {
                i4--;
            }

            for(; j4 < width && blocks[(j1 + j4) - i4] == l1; j4++) { }
            int k4 = j1 >> k2 & i3;
            int l4 = j1 >> k2 + l2;
            if(i2 == 255 && (i4 == 0 || j4 == width - 1 || k3 == 0 || k3 == height - 1 || k1 == 0 || k1 == length - 1))
            {
                return -1L;
            }
            if(k4 != k1 || l4 != k3)
            {
                System.out.println("Diagonal flood!?");
            }
            boolean flag = false;
            boolean flag1 = false;
            boolean flag2 = false;
            l3 += j4 - i4;
            i4 = i4;
            while(i4 < j4) 
            {
                blocks[j1] = byte0;
                if(k1 > 0)
                {
                    boolean flag3;
                    if((flag3 = blocks[j1 - width] == l1) && !flag)
                    {
                        if(j2 == p.length)
                        {
                            arraylist.add(p);
                            p = new int[0x100000];
                            j2 = 0;
                        }
                        p[j2++] = j1 - width;
                    }
                    flag = flag3;
                }
                if(k1 < length - 1)
                {
                    boolean flag4;
                    if((flag4 = blocks[j1 + width] == l1) && !flag1)
                    {
                        if(j2 == p.length)
                        {
                            arraylist.add(p);
                            p = new int[0x100000];
                            j2 = 0;
                        }
                        p[j2++] = j1 + width;
                    }
                    flag1 = flag4;
                }
                if(k3 > 0)
                {
                    boolean bbyte1;
                    byte byte2 = blocks[j1 - i1];
                    if((byte0 == Block.lavaMoving.blockID || byte0 == Block.lavaStill.blockID) && (byte2 == Block.waterMoving.blockID || byte2 == Block.waterStill.blockID))
                    {
                        blocks[j1 - i1] = (byte)Block.stone.blockID;
                    }
                    bbyte1 = byte2 != l1 ? false : true;
                    if(bbyte1 && !flag2)
                    {
                        if(j2 == p.length)
                        {
                            arraylist.add(p);
                            p = new int[0x100000];
                            j2 = 0;
                        }
                        p[j2++] = j1 - i1;
                    }
                    flag2 = bbyte1;
                }
                j1++;
                i4++;
            }
        }
        return l3;
    }

    public final boolean maybeGrowTree(IndevLevel world, int i1, int j1, int k1)
    {
        int l1 = rand.nextInt(3) + 4;
        boolean flag = true;
        if(j1 <= 0 || j1 + l1 + 1 > world.c)
        {
            return false;
        }
        for(int j2 = j1; j2 <= j1 + 1 + l1; j2++)
        {
            byte byte0 = 1;
            if(j2 == j1)
            {
                byte0 = 0;
            }
            if(j2 >= (j1 + 1 + l1) - 2)
            {
                byte0 = 2;
            }
            for(int k3 = i1 - byte0; k3 <= i1 + byte0 && flag; k3++)
            {
                for(int i4 = k1 - byte0; i4 <= k1 + byte0 && flag; i4++)
                {
                    int k4;
                    if(k3 >= 0 && j2 >= 0 && i4 >= 0 && k3 < world.a && j2 < world.c && i4 < world.b)
                    {
                        if((k4 = this.blocks[(j2 * world.b + i4) * world.a + k3] & 0xff) != 0)
                        {
                            flag = false;
                        }
                    } else
                    {
                        flag = false;
                    }
                }

            }

        }

        if(!flag)
        {
            return false;
        }
        int k2;
        if((k2 = this.blocks[((j1 - 1) * world.b + k1) * world.a + i1] & 0xff) != Block.grass.blockID && k2 != Block.dirt.blockID || j1 >= world.c - l1 - 1)
        {
            return false;
        }
        setBlock(i1, j1 - 1, k1, Block.dirt.blockID);
        for(int i3 = (j1 - 3) + l1; i3 <= j1 + l1; i3++)
        {
            int l3 = i3 - (j1 + l1);
            int j4 = 1 - l3 / 2;
            for(int l4 = i1 - j4; l4 <= i1 + j4; l4++)
            {
                int i2 = l4 - i1;
                for(int l2 = k1 - j4; l2 <= k1 + j4; l2++)
                {
                    int i5 = l2 - k1;
                    if((Math.abs(i2) != j4 || Math.abs(i5) != j4 || rand.nextInt(2) != 0 && l3 != 0) && !isOpaque(getBlockId(l4, i3, l2)))
                    {
                        setBlock(l4, i3, l2, Block.leaves.blockID);
                    }
                }

            }

        }

        for(int j3 = 0; j3 < l1; j3++)
        {
            if(!isOpaque(getBlockId(i1, j1 + j3, k1)))
            {
                setBlock(i1, j1 + j3, k1, Block.wood.blockID);
            }
        }

        return true;
    }

    private void setBlock(int i, int j, int k, int id){
        int x = i;
        int y = j;
        int z = k;
        if ((x == 0) || (z == 0) || (x == width - 1) || (z == length - 1)){
            return;
        }
        int index = x+(y*length+z)*width;
        this.blocks[index]=(byte)id;
    }

    private boolean canFlowerStay(BlockFlower flower, IndevLevel world, int i, int j, int k){
        if (flower==Block.plantYellow || flower==Block.plantRed){
            return (((getLightLevel(world, i, j, k) >= 8) || ((getLightLevel(world, i, j, k) >= 4) && /*(world.l(i, j, k)))*/true)) && (canThisPlantGrowOnThisBlockID(flower, getBlockId(i, j - 1, k))));
        }
        return getLightLevel(world, i, j, k) < 13 && canThisPlantGrowOnThisBlockID(flower, getBlockId(i, j - 1, k));
    }

    private boolean canThisPlantGrowOnThisBlockID(BlockFlower flower, int par1)
    {
        if (flower==Block.plantYellow || flower==Block.plantRed){
            return par1 == Block.grass.blockID || par1 == Block.dirt.blockID || par1 == Block.tilledField.blockID;
        }
        return isOpaque(par1);
    }

    public final byte getLightLevel(IndevLevel world, int i1, int j1, int k1)
    {
        if (true){
            if (j1==getFirstUncoveredBlock(world, i1, k1, true)){
                return (byte)world.A;
            }
            return 0;
        }
        if(i1 < 0)
        {
            i1 = 0;
        } else
        if(i1 >= width)
        {
            i1 = width - 1;
        }
        if(j1 < 0)
        {
            j1 = 0;
        } else
        if(j1 >= height)
        {
            j1 = height - 1;
        }
        if(k1 < 0)
        {
            k1 = 0;
        } else
        if(k1 >= length)
        {
            k1 = length - 1;
        }
        if(blocks[(j1 * length + k1) * width + i1] == Block.stairSingle.blockID)
        {
            if(j1 < height - 1)
            {
                return (byte)(e[((j1 + 1) * length + k1) * width + i1] & 0xf);
            } else
            {
                return 15;
            }
        } else
        {
//             System.out.println((byte)(e[(j1 * length + k1) * width + i1] & 0xf));
            return (byte)(e[(j1 * length + k1) * width + i1] & 0xf);
        }
    }

  public final void setData(IndevLevel world, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    if ((paramArrayOfByte2 != null) && (paramArrayOfByte2.length == 0))
      paramArrayOfByte2 = null;
    world.a = paramInt1;
    world.b = paramInt3;
    world.c = paramInt2;
    world.d = paramArrayOfByte1;
    byte[] arrayOfByte;
    int i3;
    for (int j1 = 0; j1 < world.a; j1++){
        for (int j2 = 0; j2 < world.b; j2++){
            for (int j3 = 0; j3 < world.c; j3++){
                i3 = 0;
                if (j3 <= 1)
                {
                    if ((j3 < world.t - 1) && (paramArrayOfByte1[(((j3 + 1) * world.b + j2) * world.a + j1)] == 0))
                    {
                        i3 = Block.lavaStill.blockID;
                        break/* label235*/;
                    }
                }
                if (j3 < world.t - 1)
                {
                    i3 = Block.bedrock.blockID;
                }
                else
                {
                    if (j3 < world.t){
                        if ((world.t > world.waterLevel) && (world.m == Block.waterStill.blockID)){
                            i3 = Block.grass.blockID;
                        }else{
                            i3 = Block.dirt.blockID;
                        }
                    }else if (j3 < world.waterLevel){
                        i3 = world.m;
                    }
                }
                label235: paramArrayOfByte1[((j3 * world.b + j2) * world.a + j1)] = (byte)i3;
                if ((j3 != 1) || (j1 == 0) || (j2 == 0) || (j1 == world.a - 1) || (j2 == world.b - 1)){
                    continue;
                }
                j3 = world.c - 2;
            }
        }
    }
//     world.p = new int[paramInt1 * paramInt3];
//     Arrays.fill(world.p, world.c);
    if (paramArrayOfByte2 == null){
     /*   e = new byte[paramArrayOfByte1.length];
//           world.M = new h(world);
        int jj0 = 1;
        int jj1 = world.B;
        for (int j4 = 0; j4 < world.a; j4++){
            for (int j5 = 0; j5 < world.b; j5++)
            {
//                 for (int j6 = world.c - 1; (j6 > 0) && (Block.lightOpacity[getBlockId(j4, j6, j5)] == 0); j6--){
//                     world.p[(j4 + j5 * world.a)] = (j6 + 1);
//                 }
                for (int j7 = 0; j7 < world.c; j7++)
                {
                    int i1 = (j7 * world.b + j5) * world.a + j4;
                    int i4 = getFirstUncoveredBlock(world, j4, j5, false);
                    int i2 = j7 >= i4 ? jj1 : 0;
                    int i5 = world.d[i1];
                    if (i2 < Block.lightValue[i5]){
                        i2 = Block.lightValue[i5];
                    }
                    e[i1] = (byte)((e[i1] & 0xF0) + i2);
                }
            }
        }*/
//         world.M.a(0, 0, 0, world.a, world.c, world.b);
    }
    else
    {
        e = paramArrayOfByte2;
//         world.M = new h(world);
    }
//     for (int iparamInt2 = 0; iparamInt2 < world.n.size(); iparamInt2++){
//         ((d)world.n.get(iparamInt2)).a();
//     }
//     world.G.clear();
    b(world);
    world.a();
    System.gc();
  }

    public final void b(IndevLevel world){
        int x = 0;
        int y = 0;
        int z = 0;
        int c = 0;
        boolean fair = false;
        boolean fground = false;
        while(!(fair && fground)){
            x = rand.nextInt(world.a / 2) + world.a / 4;
            z = rand.nextInt(world.b / 2) + world.b / 4;
            y = getFirstUncoveredBlock(world, x, z, true) + 1;
            if (y<5 || y>world.c){
                y = world.c-29;
            }
            check: for (int i1 = x - 3; i1 <= x + 3; i1++){
                for (int i2 = y - 1; i2 <= y + 2; i2++){
                    for (int i3 = z - 3 - 2; i3 <= z + 3; i3++){
                        if (getBlockId(i1, i2, i3) != 0){
                            fair = false;
                            break check;
                        }
                    }
                }
                fair = true;
            }
            if (c<10000){
                check2: for (int i1 = x - 3; i1 <= x + 3; i1++){
                    for (int i3 = z - 3 - 2; i3 <= z + 3; i3++){
                        if (!getBlockMaterial(i1, y-2, i3).isSolid()){
                            fground = false;
                            break check2;
                        }
                    }
                    fground = true;
                }
            }else{
                check3: for (int i1 = x - 3; i1 <= x + 3; i1++){
                    for (int i3 = z - 3 - 2; i3 <= z - 1; i3++){
                        if (!getBlockMaterial(i1, y-2, i3).isSolid()){
                            fground = false;
                            break check3;
                        }
                    }
                    fground = true;
                }
            }
            if (c>20000){
                break;
            }
            c++;
        }
        world.i = x;
        world.j = y;
        world.k = z;
    }
    
    private byte getBlockId(int i, int j, int k){
        int x = i;
        int y = j;
        int z = k;
        int index = x+(y*length+z)*width;
        return this.blocks[index];
    }
    
    private Material getBlockMaterial(int i, int j, int k){
        if (getBlockId(i, j, k) == 0){
            return Material.air;
        }
        return Block.blocksList[getBlockId(i, j, k)].blockMaterial;
    }

    public int getFirstUncoveredBlock(IndevLevel world, int i1, int j1, boolean opaque)
    {
        int k1;
        for(k1 = world.c; (getBlockId(i1, k1 - 1, j1) == 0 || Block.blocksList[getBlockId(i1, k1 - 1, j1)].blockMaterial == Material.air || !isOpaque(getBlockId(i1, k1 - 1, j1))) && k1 > 1; k1--) { }
        return k1;
    }
}
