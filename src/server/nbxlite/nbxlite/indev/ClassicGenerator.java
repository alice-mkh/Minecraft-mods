package net.minecraft.src.nbxlite.indev;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.nbxlite.indev.noise.*;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.StatCollector;
import net.minecraft.src.mod_noBiomesX;
import net.minecraft.server.MinecraftServer; 

public final class ClassicGenerator
{
    private int b;
    private int c;
    private int d;
    private java.util.Random rand;
    private byte f[];
    private int g;
    private int h[];
    public int spawnX;
    public int spawnY;
    public int spawnZ;

    public ClassicGenerator(long seed)
    {
        rand = new Random(seed);
        h = new int[0x100000];
    }

    public final byte[] generateLevel(String s, int j, int k, int l)
    {
        b = j;
        c = k;
        d = l;
        g = d - 32;
        f = new byte[j * k << 6];
        MinecraftServer.logger.info("Raising..");
        java.lang.Object obj = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        IndevNoiseGenerator2 c1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        java.lang.Object obj1 = new IndevNoiseGeneratorOctaves(rand, 6);
        int ai[] = new int[this.b * this.c];
        float f3 = 1.3F;
        for(int i7 = 0; i7 < this.b; i7++)
        {
            this.a((i7 * 100) / (this.b - 1));
            for(int j3 = 0; j3 < this.c; j3++)
            {
                double d2 = ((IndevNoiseGenerator) (obj)).a((float)i7 * f3, (float)j3 * f3) / 6D + (double)-4;
                double d3 = c1.a((float)i7 * f3, (float)j3 * f3) / 5D + 10D + (double)-4;
                double d4;
                if((d4 = ((IndevNoiseGenerator) (obj1)).a(i7, j3) / 8D) > 0.0D)
                    d3 = d2;
                double d5;
                if((d5 = java.lang.Math.max(d2, d3) / 2D) < 0.0D)
                    d5 *= 0.80000000000000004D;
                ai[i7 + j3 * this.b] = (int)d5;
            }

        }

        MinecraftServer.logger.info("Eroding..");
        int arrayf1[] = ai;
        c1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        obj1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        for(int l1 = 0; l1 < this.b; l1++)
        {
            this.a((l1 * 100) / (this.b - 1));
            for(int intf3 = 0; intf3 < this.c; intf3++)
            {
                double d1 = c1.a(l1 << 1, intf3 << 1) / 8D;
                int k3 = ((IndevNoiseGenerator) (obj1)).a(l1 << 1, intf3 << 1) <= 0.0D ? 0 : 1;
                if(d1 > 2D)
                {
                    int i9;
                    i9 = (((i9 = arrayf1[l1 + intf3 * this.b]) - k3) / 2 << 1) + k3;
                    arrayf1[l1 + intf3 * this.b] = i9;
                }
            }

        }

        MinecraftServer.logger.info("Soiling..");
        arrayf1 = ai;
        int intf2 = b;
        int k1 = this.c;
        int i2 = this.d;
        IndevNoiseGeneratorOctaves b1 = new IndevNoiseGeneratorOctaves(rand, 8);
        for(int j5 = 0; j5 < intf2; j5++)
        {
            this.a((j5 * 100) / (this.b - 1));
            for(int j7 = 0; j7 < k1; j7++)
            {
                int l3 = (int)(b1.a(j5, j7) / 24D) - 4;
                int j9;
                int k10 = (j9 = arrayf1[j5 + j7 * intf2] + this.g) + l3;
                arrayf1[j5 + j7 * intf2] = java.lang.Math.max(j9, k10);
                if(arrayf1[j5 + j7 * intf2] > i2 - 2)
                    arrayf1[j5 + j7 * intf2] = i2 - 2;
                if(arrayf1[j5 + j7 * intf2] < 1)
                    arrayf1[j5 + j7 * intf2] = 1;
                for(int k11 = 0; k11 < i2; k11++)
                {
                    int i13 = (k11 * this.c + j7) * this.b + j5;
                    int j14 = 0;
                    if(k11 <= j9)
                        j14 = Block.dirt.blockID;
                    if(k11 <= k10)
                        j14 = Block.stone.blockID;
                    if(k11 == 0)
                        j14 = Block.lavaMoving.blockID;
                    this.f[i13] = (byte)j14;
                }

            }

        }

        MinecraftServer.logger.info("Carving..");
        float f2 = 4;
        float f1 = 0;
        int k5 = (this.b * this.c * this.d) / 256 / 64 << 1;
        for(int k7 = 0; k7 < k5; k7++)
        {
            this.a((k7 * 100) / (k5 - 1) / 4);
            float f4 = rand.nextFloat() * (float)this.b;
            float f6 = rand.nextFloat() * (float)this.d;
            float f7 = rand.nextFloat() * (float)this.c;
            int l11 = (int)((rand.nextFloat() + rand.nextFloat()) * 200F);
            float f8 = rand.nextFloat() * 3.141593F * 2.0F;
            float f9 = 0.0F;
            float f10 = rand.nextFloat() * 3.141593F * 2.0F;
            float f11 = 0.0F;
            float f12 = rand.nextFloat() * rand.nextFloat();
label0:
            for(int i1 = 0; i1 < l11; i1++)
            {
                f4 += MathHelper.sin(f8) * MathHelper.cos(f10);
                f7 += MathHelper.cos(f8) * MathHelper.cos(f10);
                f6 += MathHelper.sin(f10);
                f8 += f9 * 0.2F;
                f9 = (f9 *= 0.9F) + (rand.nextFloat() - rand.nextFloat());
                f10 = (f10 += f11 * 0.5F) * 0.5F;
                f11 = (f11 *= 0.75F) + (rand.nextFloat() - rand.nextFloat());
                if(rand.nextFloat() < 0.25F)
                    continue;
                f1 = f4 + (rand.nextFloat() * 4F - 2.0F) * 0.2F;
                f2 = f6 + (rand.nextFloat() * 4F - 2.0F) * 0.2F;
                float f5 = f7 + (rand.nextFloat() * 4F - 2.0F) * 0.2F;
                float f13 = ((float)this.d - f2) / (float)this.d;
                f13 = 1.2F + (f13 * 3.5F + 1.0F) * f12;
                f13 = MathHelper.sin(((float)i1 * 3.141593F) / (float)l11) * f13;
                int j16 = (int)(f1 - f13);
                do
                {
                    if(j16 > (int)(f1 + f13))
                        continue label0;
                    for(int k16 = (int)(f2 - f13); k16 <= (int)(f2 + f13); k16++)
                    {
                        for(int l16 = (int)(f5 - f13); l16 <= (int)(f5 + f13); l16++)
                        {
                            float f14 = (float)j16 - f1;
                            float f15 = (float)k16 - f2;
                            float f16 = (float)l16 - f5;
                            if((f14 = f14 * f14 + f15 * f15 * 2.0F + f16 * f16) >= f13 * f13 || j16 < 1 || k16 < 1 || l16 < 1 || j16 >= this.b - 1 || k16 >= this.d - 1 || l16 >= this.c - 1)
                                continue;
                            f14 = (k16 * this.c + l16) * this.b + j16;
                            if(this.f[(int)f14] == Block.stone.blockID)
                                this.f[(int)f14] = 0;
                        }

                    }

                    j16++;
                } while(true);
            }

        }

        a(Block.oreCoal.blockID, 90, 1, 4);
        a(Block.oreIron.blockID, 70, 2, 4);
        a(Block.oreGold.blockID, 50, 3, 4);
        MinecraftServer.logger.info("Watering..");
        i2 = Block.waterStill.blockID;
        this.a(0);
        for(int intb1 = 0; intb1 < this.b; intb1++)
        {
            fillWithLiquid(intb1, this.d / 2 - 1, 0, 0, i2);
            fillWithLiquid(intb1, this.d / 2 - 1, this.c - 1, 0, i2);
        }

        for(int intb1 = 0; intb1 < this.c; intb1++)
        {
            fillWithLiquid(0, this.d / 2 - 1, intb1, 0, i2);
            fillWithLiquid(this.b - 1, this.d / 2 - 1, intb1, 0, i2);
        }

        int intb1 = (this.b * this.c) / 8000;
        for(int l5 = 0; l5 < intb1; l5++)
        {
            if(l5 % 100 == 0)
                a((l5 * 100) / (intb1 - 1));
            int l7 = rand.nextInt(this.b);
            int i4 = this.g - 1 - rand.nextInt(2);
            int k9 = rand.nextInt(this.c);
            if(this.f[(i4 * this.c + k9) * this.b + l7] == 0)
                fillWithLiquid(l7, i4, k9, 0, i2);
        }

        this.a(100);
        MinecraftServer.logger.info("Melting..");
        int intf22 = (this.b * this.c * this.d) / 20000;
        for(int intk1 = 0; intk1 < intf22; intk1++)
        {
            if(intk1 % 100 == 0){
                this.a((intk1 * 100) / (intf22 - 1));
            }
            i2 = rand.nextInt(this.b);
            int j2 = (int)(rand.nextFloat() * rand.nextFloat() * (float)(this.g - 3));
            int i6 = rand.nextInt(this.c);
            if(this.f[(j2 * this.c + i6) * this.b + i2] == 0){
                fillWithLiquid(i2, j2, i6, 0, Block.lavaStill.blockID);
            }
        }

        this.a(100);
        MinecraftServer.logger.info("Growing..");
        arrayf1 = ai;
        IndevNoiseGeneratorOctaves j2 = new IndevNoiseGeneratorOctaves(rand, 8);
        IndevNoiseGeneratorOctaves b2 = new IndevNoiseGeneratorOctaves(rand, 8);
        for(int i8 = 0; i8 < this.b; i8++)
        {
            this.a((i8 * 100) / (this.b - 1));
            for(int j4 = 0; j4 < this.c; j4++)
            {
                boolean flag = j2.a(i8, j4) > 8D;
                boolean flag1 = b2.a(i8, j4) > 12D;
                int i12 = arrayf1[i8 + j4 * this.b];
                int j13 = (i12 * this.c + j4) * this.b + i8;
                int k14 = this.f[((i12+1) * this.c + j4) * this.b + i8] & 0xff;
                if((k14 == Block.waterMoving.blockID || k14 == Block.waterStill.blockID) && i12 <= this.d / 2 - 1 && flag1)
                    this.f[j13] = (byte)Block.gravel.blockID;
                if(k14 != 0){
                    continue;
                }
                int i15 = Block.grass.blockID;
                if(i12 <= this.d / 2 - 1 && flag)
                    i15 = Block.sand.blockID;
                this.f[j13] = (byte)i15;
            }

        }

        MinecraftServer.logger.info("Planting..");
        arrayf1 = ai;
        if (this.b==64 || this.c==64){
            k1 = (this.b * this.c) / 2000;
        }else{
            k1 = (this.b * this.c) / 3000;
        }
        for(i2 = 0; i2 < k1; i2++)
        {
            int k2 = rand.nextInt(2);
            this.a((i2 * 50) / (k1 - 1));
            int j6 = rand.nextInt(this.b);
            int j8 = rand.nextInt(this.c);
            for(int k4 = 0; k4 < 10; k4++)
            {
                int l9 = j6;
                int l10 = j8;
                for(int j12 = 0; j12 < 5; j12++)
                {
                    l9 += rand.nextInt(6) - rand.nextInt(6);
                    l10 += rand.nextInt(6) - rand.nextInt(6);
                    if(k2 >= 2 && rand.nextInt(4) != 0 || l9 < 0 || l10 < 0 || l9 >= this.b || l10 >= this.c)
                        continue;
                    int k13 = arrayf1[l9 + l10 * this.b] + 1;
                    boolean flag2;
                    if(!(flag2 = (this.f[(k13 * this.c + l10) * this.b + l9] & 0xff) == 0))
                        continue;
                    int j15 = (k13 * this.c + l10) * this.b + l9;
                    int l15;
                    if((l15 = this.f[((k13 - 1) * this.c + l10) * this.b + l9] & 0xff) != Block.grass.blockID)
                        continue;
                    if(k2 == 0)
                    {
                        this.f[j15] = (byte)Block.plantYellow.blockID;
                        continue;
                    }
                    if(k2 == 1)
                        this.f[j15] = (byte)Block.plantRed.blockID;
                }

            }

        }

        f1 = l;
        i2 = (this.b * this.c * this.d) / 2000;
        for(int l2 = 0; l2 < i2; l2++)
        {
            int k6 = rand.nextInt(2);
            this.a((l2 * 50) / (i2 - 1) + 50);
            int k8 = rand.nextInt(this.b);
            int l4 = rand.nextInt(this.d);
            int i10 = rand.nextInt(this.c);
            for(int i11 = 0; i11 < 20; i11++)
            {
                int k12 = k8;
                int l13 = l4;
                int l14 = i10;
                for(int k15 = 0; k15 < 5; k15++)
                {
                    k12 += rand.nextInt(6) - rand.nextInt(6);
                    l13 += rand.nextInt(2) - rand.nextInt(2);
                    l14 += rand.nextInt(6) - rand.nextInt(6);
                    boolean flag3;
                    if(k6 >= 2 && rand.nextInt(4) != 0 || k12 < 0 || l14 < 0 || l13 < 1 || k12 >= this.b || l14 >= this.c || l13 >= arrayf1[k12 + l14 * this.b] - 1 || !(flag3 = (this.f[(l13 * this.c + l14) * this.b + k12] & 0xff) == 0))
                        continue;
                    int i16 = (l13 * this.c + l14) * this.b + k12;
                    int j1;
                    if((j1 = this.f[((l13 - 1) * this.c + l14) * this.b + k12] & 0xff) != Block.stone.blockID)
                        continue;
                    if(k6 == 0)
                    {
                        this.f[i16] = (byte)Block.mushroomBrown.blockID;
                        continue;
                    }
                    if(k6 == 1)
                        this.f[i16] = (byte)Block.mushroomRed.blockID;
                }

            }

        }

        IndevLevel level = new IndevLevel();
        level.waterLevel = g;
//         level.setData(j, 64, k, f);
        findSpawn(level);
        level.createTime = java.lang.System.currentTimeMillis();
        level.creator = s;
        level.name = "A Nice World";
        arrayf1 = ai;
        if (this.b == 64 || this.c == 64){
            i2 = (this.b * this.c) / 2000;
        }else{
            i2 = (this.b * this.c) / 4000;
        }
        for(int i3 = 0; i3 < i2; i3++)
        {
            this.a((i3 * 50) / (i2 - 1) + 50);
            int l6 = rand.nextInt(this.b);
            int l8 = rand.nextInt(this.c);
            for(int i5 = 0; i5 < 20; i5++)
            {
                int j10 = l6;
                int j11 = l8;
                for(int l12 = 0; l12 < 20; l12++)
                {
                    j10 += rand.nextInt(6) - rand.nextInt(6);
                    j11 += rand.nextInt(6) - rand.nextInt(6);
                    if(j10 < 0 || j11 < 0 || j10 >= this.b || j11 >= this.c)
                        continue;
                    int i14 = arrayf1[j10 + j11 * this.b] + 1;
                    if(rand.nextInt(4) == 0)
                        maybeGrowTree(level, j10, i14, j11);
                }

            }

        }

//         return level;
        return this.f;
    }

    public final boolean maybeGrowTree(IndevLevel world, int i1, int j1, int k1)
    {
        int l1 = rand.nextInt(3) + 4;
        boolean flag = true;
        if(j1 <= 0 || j1 + l1 + 1 > d)
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
                    if(k3 >= 0 && j2 >= 0 && i4 >= 0 && k3 < b && j2 < d && i4 < c)
                    {
                        if((k4 = this.f[(j2 * c + i4) * b + k3] & 0xff) != 0)
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
        if((k2 = this.f[((j1 - 1) * c + k1) * b + i1] & 0xff) != Block.grass.blockID && k2 != Block.dirt.blockID || j1 >= d - l1 - 1)
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
        int index = x+(y*c+z)*b;
        this.f[index]=(byte)id;
    }

    private byte getBlockId(int i, int j, int k){
        int x = i;
        int y = j;
        int z = k;
        int index = x+(y*c+z)*b;
        return this.f[index];
    }

    public boolean isOpaque(int id){
        if (id == Block.leaves.blockID){
            return false;
        }
        return Block.opaqueCubeLookup[id];
    }

    private void a(int j, int k, int l, int i1)
    {
        i1 = b;
        int j1 = c;
        int k1 = d;
        int l1 = (((i1 * j1 * k1) / 256 / 64) * k) / 100;
        for(int i2 = 0; i2 < l1; i2++)
        {
            a((i2 * 100) / (l1 - 1) / 4 + (l * 100) / 4);
            float f1 = rand.nextFloat() * (float)i1;
            float f2 = rand.nextFloat() * (float)k1;
            float f3 = rand.nextFloat() * (float)j1;
            int j2 = (int)(((rand.nextFloat() + rand.nextFloat()) * 75F * (float)k) / 100F);
            float f4 = rand.nextFloat() * 3.141593F * 2.0F;
            float f5 = 0.0F;
            float f6 = rand.nextFloat() * 3.141593F * 2.0F;
            float f7 = 0.0F;
            for(int k2 = 0; k2 < j2; k2++)
            {
                f1 += MathHelper.sin(f4) * MathHelper.cos(f6);
                f3 += MathHelper.cos(f4) * MathHelper.cos(f6);
                f2 += MathHelper.sin(f6);
                f4 += f5 * 0.2F;
                f5 = (f5 *= 0.9F) + (rand.nextFloat() - rand.nextFloat());
                f6 = (f6 += f7 * 0.5F) * 0.5F;
                f7 = (f7 *= 0.9F) + (rand.nextFloat() - rand.nextFloat());
                float f8 = (MathHelper.sin(((float)k2 * 3.141593F) / (float)j2) * (float)k) / 100F + 1.0F;
                for(int l2 = (int)(f1 - f8); l2 <= (int)(f1 + f8); l2++)
                {
                    for(int i3 = (int)(f2 - f8); i3 <= (int)(f2 + f8); i3++)
                    {
                        for(int j3 = (int)(f3 - f8); j3 <= (int)(f3 + f8); j3++)
                        {
                            float f9 = (float)l2 - f1;
                            float f10 = (float)i3 - f2;
                            float f11 = (float)j3 - f3;
                            if((f9 = f9 * f9 + f10 * f10 * 2.0F + f11 * f11) >= f8 * f8 || l2 < 1 || i3 < 1 || j3 < 1 || l2 >= b - 1 || i3 >= d - 1 || j3 >= c - 1)
                                continue;
                            f9 = (i3 * c + j3) * b + l2;
                            if(f[(int)f9] == Block.stone.blockID)
                                f[(int)f9] = (byte)j;
                        }

                    }

                }

            }

        }

    }

    private void a(int j)
    {
    }

    private long fillWithLiquid(int i1, int j1, int k1, int l1, int i2)
    {
        byte byte0 = (byte)i2;
        l1 = (byte)l1;
        ArrayList arraylist = new ArrayList();
        int j2 = 0;
        int k2 = 1;
        int l2 = 1;
        for(; 1 << k2 < b; k2++) { }
        for(; 1 << l2 < c; l2++) { }
        int i3 = c - 1;
        int j3 = b - 1;
        j2++;
        h[0] = ((j1 << l2) + k1 << k2) + i1;
        long l3 = 0L;
        i1 = b * c;
        while(j2 > 0) 
        {
            j1 = h[--j2];
            if(j2 == 0 && arraylist.size() > 0)
            {
                h = (int[])arraylist.remove(arraylist.size() - 1);
                j2 = h.length;
            }
            k1 = j1 >> k2 & i3;
            int k3 = j1 >> k2 + l2;
            int i4;
            int j4 = i4 = j1 & j3;
            for(; i4 > 0 && f[j1 - 1] == l1; j1--)
            {
                i4--;
            }

            for(; j4 < b && f[(j1 + j4) - i4] == l1; j4++) { }
            int k4 = j1 >> k2 & i3;
            int l4 = j1 >> k2 + l2;
            if(i2 == 255 && (i4 == 0 || j4 == b - 1 || k3 == 0 || k3 == d - 1 || k1 == 0 || k1 == c - 1))
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
                f[j1] = byte0;
                if(k1 > 0)
                {
                    boolean flag3;
                    if((flag3 = f[j1 - b] == l1) && !flag)
                    {
                        if(j2 == h.length)
                        {
                            arraylist.add(h);
                            h = new int[0x100000];
                            j2 = 0;
                        }
                        h[j2++] = j1 - b;
                    }
                    flag = flag3;
                }
                if(k1 < c - 1)
                {
                    boolean flag4;
                    if((flag4 = f[j1 + b] == l1) && !flag1)
                    {
                        if(j2 == h.length)
                        {
                            arraylist.add(h);
                            h = new int[0x100000];
                            j2 = 0;
                        }
                        h[j2++] = j1 + b;
                    }
                    flag1 = flag4;
                }
                if(k3 > 0)
                {
                    boolean bbyte1;
                    byte byte2 = f[j1 - i1];
                    if((byte0 == Block.lavaMoving.blockID || byte0 == Block.lavaStill.blockID) && (byte2 == Block.waterMoving.blockID || byte2 == Block.waterStill.blockID))
                    {
                        f[j1 - i1] = (byte)Block.stone.blockID;
                    }
                    bbyte1 = byte2 != l1 ? false : true;
                    if(bbyte1 && !flag2)
                    {
                        if(j2 == h.length)
                        {
                            arraylist.add(h);
                            h = new int[0x100000];
                            j2 = 0;
                        }
                        h[j2++] = j1 - i1;
                    }
                    flag2 = bbyte1;
                }
                j1++;
                i4++;
            }
        }
        return l3;
    }

    public int getFirstUncoveredBlock(IndevLevel world, int i1, int j1, boolean opaque)
    {
        int k1;
        for(k1 = d; (getBlockId(i1, k1 - 1, j1) == 0 || !isOpaque(getBlockId(i1, k1 - 1, j1))) && k1 > 1; k1--) { }
        return k1;
    }

    public void findSpawn(IndevLevel level)
    {
        int j = 0;
        int i1;
        int j1;
        int k1;
        do
        {
            j++;
            i1 = rand.nextInt(b / 2) + b / 4;
            j1 = rand.nextInt(d / 2) + d / 4;
            k1 = getFirstUncoveredBlock(level, i1, j1, true) + 1;
            if(j == 10000)
            {
                level.i = i1;
                level.j = -100;
                level.k = j1;
                spawnX = i1;
                spawnY = -100;
                spawnZ = j1;
                return;
            }
        } while(k1 <= g);
        level.i = i1;
        level.j = k1;
        level.k = j1;
        spawnX = i1;
        spawnY = k1;
        spawnZ = j1;
    }
}
