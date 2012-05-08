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
    private int width;
    private int length;
    private int height;
    private Random rand;
    private byte blocks[];
    private int sealevel;
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
        width = j;
        length = k;
        height = l;
        sealevel = height - 32;
        blocks = new byte[j * k << 6];
        MinecraftServer.logger.info("Raising..");
        java.lang.Object obj = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        IndevNoiseGenerator2 c1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        java.lang.Object obj1 = new IndevNoiseGeneratorOctaves(rand, 6);
        int ai[] = new int[width * length];
        float f3 = 1.3F;
        for(int i7 = 0; i7 < width; i7++)
        {
            a((i7 * 100) / (width - 1));
            for(int j3 = 0; j3 < length; j3++)
            {
                double d2 = ((IndevNoiseGenerator) (obj)).a((float)i7 * f3, (float)j3 * f3) / 6D + (double)-4;
                double d3 = c1.a((float)i7 * f3, (float)j3 * f3) / 5D + 10D + (double)-4;
                double d4;
                if((d4 = ((IndevNoiseGenerator) (obj1)).a(i7, j3) / 8D) > 0.0D)
                    d3 = d2;
                double d5;
                if((d5 = java.lang.Math.max(d2, d3) / 2D) < 0.0D)
                    d5 *= 0.80000000000000004D;
                ai[i7 + j3 * width] = (int)d5;
            }

        }

        MinecraftServer.logger.info("Eroding..");
        c1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        obj1 = new IndevNoiseGenerator2(new IndevNoiseGeneratorOctaves(rand, 8), new IndevNoiseGeneratorOctaves(rand, 8));
        for(int l1 = 0; l1 < width; l1++)
        {
            a((l1 * 100) / (width - 1));
            for(int intf3 = 0; intf3 < length; intf3++)
            {
                double d1 = c1.a(l1 << 1, intf3 << 1) / 8D;
                int k3 = ((IndevNoiseGenerator) (obj1)).a(l1 << 1, intf3 << 1) <= 0.0D ? 0 : 1;
                if(d1 > 2D)
                {
                    int i9;
                    i9 = (((i9 = ai[l1 + intf3 * width]) - k3) / 2 << 1) + k3;
                    ai[l1 + intf3 * width] = i9;
                }
            }

        }

        MinecraftServer.logger.info("Soiling..");
        IndevNoiseGeneratorOctaves b1 = new IndevNoiseGeneratorOctaves(rand, 8);
        for(int j5 = 0; j5 < width; j5++)
        {
            a((j5 * 100) / (width - 1));
            for(int j7 = 0; j7 < length; j7++)
            {
                int l3 = (int)(b1.a(j5, j7) / 24D) - 4;
                int j9;
                int length0 = (j9 = ai[j5 + j7 * width] + sealevel) + l3;
                ai[j5 + j7 * width] = java.lang.Math.max(j9, length0);
                if(ai[j5 + j7 * width] > height - 2)
                    ai[j5 + j7 * width] = height - 2;
                if(ai[j5 + j7 * width] < 1)
                    ai[j5 + j7 * width] = 1;
                for(int length1 = 0; length1 < height; length1++)
                {
                    int i13 = (length1 * length + j7) * width + j5;
                    int j14 = 0;
                    if(length1 <= j9)
                        j14 = Block.dirt.blockID;
                    if(length1 <= length0)
                        j14 = Block.stone.blockID;
                    if(length1 == 0)
                        j14 = Block.lavaStill.blockID;
                    blocks[i13] = (byte)j14;
                }

            }

        }

        MinecraftServer.logger.info("Carving..");
        float f2 = 4;
        float f1 = 0;
        int k5 = (width * length * height) / 256 / 64 << 1;
        for(int k7 = 0; k7 < k5; k7++)
        {
            a((k7 * 100) / (k5 - 1) / 4);
            float f4 = rand.nextFloat() * (float)width;
            float f6 = rand.nextFloat() * (float)height;
            float f7 = rand.nextFloat() * (float)length;
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
                float f13 = ((float)height - f2) / (float)height;
                f13 = 1.2F + (f13 * 3.5F + 1.0F) * f12;
                f13 = MathHelper.sin(((float)i1 * 3.141593F) / (float)l11) * f13;
                int j16 = (int)(f1 - f13);
                do
                {
                    if(j16 > (int)(f1 + f13))
                        continue label0;
                    for(int length6 = (int)(f2 - f13); length6 <= (int)(f2 + f13); length6++)
                    {
                        for(int l16 = (int)(f5 - f13); l16 <= (int)(f5 + f13); l16++)
                        {
                            float f14 = (float)j16 - f1;
                            float f15 = (float)length6 - f2;
                            float f16 = (float)l16 - f5;
                            if((f14 = f14 * f14 + f15 * f15 * 2.0F + f16 * f16) >= f13 * f13 || j16 < 1 || length6 < 1 || l16 < 1 || j16 >= width - 1 || length6 >= height - 1 || l16 >= length - 1)
                                continue;
                            f14 = (length6 * length + l16) * width + j16;
                            if(blocks[(int)f14] == Block.stone.blockID)
                                blocks[(int)f14] = 0;
                        }

                    }

                    j16++;
                } while(true);
            }

        }

        generateOre(Block.oreCoal.blockID, 90, 1, 4);
        generateOre(Block.oreIron.blockID, 70, 2, 4);
        generateOre(Block.oreGold.blockID, 50, 3, 4);
        if (mod_noBiomesX.GenerateNewOres){
            generateOre(Block.oreLapis.blockID, 30, 4, 4);
            generateOre(Block.oreRedstone.blockID, 40, 5, 4);
            generateOre(Block.oreDiamond.blockID, 30, 6, 4);
        }
        MinecraftServer.logger.info("Watering..");
        height = Block.waterStill.blockID;
        if(mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL)
            {
            height = Block.lavaStill.blockID;
        }
        a(0);
        for(int intb1 = 0; intb1 < width; intb1++)
        {
            fillWithLiquid(intb1, height / 2 - 1, 0, 0, height);
            fillWithLiquid(intb1, height / 2 - 1, length - 1, 0, height);
        }

        for(int intb1 = 0; intb1 < length; intb1++)
        {
            fillWithLiquid(0, height / 2 - 1, intb1, 0, height);
            fillWithLiquid(width - 1, height / 2 - 1, intb1, 0, height);
        }

        int intb1 = (width * length) / 8000;
        for(int l5 = 0; l5 < intb1; l5++)
        {
            if(l5 % 100 == 0)
                a((l5 * 100) / (intb1 - 1));
            int l7 = rand.nextInt(width);
            int i4 = sealevel - 1 - rand.nextInt(2);
            int k9 = rand.nextInt(length);
            if(blocks[(i4 * length + k9) * width + l7] == 0)
                fillWithLiquid(l7, i4, k9, 0, height);
        }

        a(100);
        MinecraftServer.logger.info("Melting..");
        int width2 = (width * length * height) / 20000;
        for(int intlength = 0; intlength < width2; intlength++)
        {
            if(intlength % 100 == 0){
                a((intlength * 100) / (width2 - 1));
            }
            height = rand.nextInt(width);
            int j2 = (int)(rand.nextFloat() * rand.nextFloat() * (float)(sealevel - 3));
            int i6 = rand.nextInt(length);
            if(blocks[(j2 * length + i6) * width + height] == 0){
                fillWithLiquid(height, j2, i6, 0, Block.lavaStill.blockID);
            }
        }

        a(100);
        MinecraftServer.logger.info("Growing..");
        IndevNoiseGeneratorOctaves j2 = new IndevNoiseGeneratorOctaves(rand, 8);
        IndevNoiseGeneratorOctaves b2 = new IndevNoiseGeneratorOctaves(rand, 8);
        for(int i8 = 0; i8 < width; i8++)
        {
            a((i8 * 100) / (width - 1));
            for(int j4 = 0; j4 < length; j4++)
            {
                boolean flag = j2.a(i8, j4) > 8D;
                boolean flag1 = b2.a(i8, j4) > 12D;
                int i12 = ai[i8 + j4 * width];
                int j13 = (i12 * length + j4) * width + i8;
                int length4 = blocks[((i12+1) * length + j4) * width + i8] & 0xff;
                if((length4 == Block.waterMoving.blockID || length4 == Block.waterStill.blockID) && i12 <= height / 2 - 1 && flag1)
                    blocks[j13] = (byte)Block.gravel.blockID;
                if(length4 != 0){
                    continue;
                }
                int i15 = Block.grass.blockID;
                if(mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL)
                {
                    i15 = Block.dirt.blockID;
                }
                if(i12 <= height / 2 - 1 && flag){
                    i15 = Block.sand.blockID;
                    if(mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL)
                    {
                        i15 = Block.grass.blockID;
                    }
                }
                blocks[j13] = (byte)i15;
            }

        }

        MinecraftServer.logger.info("Planting..");
        if (width==64 || length==64){
            length = (width * length) / 2000;
        }else{
            length = (width * length) / 3000;
        }
        for(height = 0; height < length; height++)
        {
            int k2 = rand.nextInt(2);
            a((height * 50) / (length - 1));
            int j6 = rand.nextInt(width);
            int j8 = rand.nextInt(length);
            for(int k4 = 0; k4 < 10; k4++)
            {
                int l9 = j6;
                int l10 = j8;
                for(int j12 = 0; j12 < 5; j12++)
                {
                    l9 += rand.nextInt(6) - rand.nextInt(6);
                    l10 += rand.nextInt(6) - rand.nextInt(6);
                    if(k2 >= 2 && rand.nextInt(4) != 0 || l9 < 0 || l10 < 0 || l9 >= width || l10 >= length)
                        continue;
                    int length3 = ai[l9 + l10 * width] + 1;
                    boolean flag2;
                    if(!(flag2 = (blocks[(length3 * length + l10) * width + l9] & 0xff) == 0))
                        continue;
                    int j15 = (length3 * length + l10) * width + l9;
                    int l15;
                    if((l15 = blocks[((length3 - 1) * length + l10) * width + l9] & 0xff) != Block.grass.blockID)
                        continue;
                    if(k2 == 0)
                    {
                        blocks[j15] = (byte)Block.plantYellow.blockID;
                        continue;
                    }
                    if(k2 == 1)
                        blocks[j15] = (byte)Block.plantRed.blockID;
                }

            }

        }

        f1 = l;
        height = (width * length * height) / 2000;
        for(int l2 = 0; l2 < height; l2++)
        {
            int k6 = rand.nextInt(2);
            a((l2 * 50) / (height - 1) + 50);
            int k8 = rand.nextInt(width);
            int l4 = rand.nextInt(height);
            int i10 = rand.nextInt(length);
            for(int i11 = 0; i11 < 20; i11++)
            {
                int length2 = k8;
                int l13 = l4;
                int l14 = i10;
                for(int length5 = 0; length5 < 5; length5++)
                {
                    length2 += rand.nextInt(6) - rand.nextInt(6);
                    l13 += rand.nextInt(2) - rand.nextInt(2);
                    l14 += rand.nextInt(6) - rand.nextInt(6);
                    boolean flag3;
                    if(k6 >= 2 && rand.nextInt(4) != 0 || length2 < 0 || l14 < 0 || l13 < 1 || length2 >= width || l14 >= length || l13 >= ai[length2 + l14 * width] - 1 || !(flag3 = (blocks[(l13 * length + l14) * width + length2] & 0xff) == 0))
                        continue;
                    int i16 = (l13 * length + l14) * width + length2;
                    int j1;
                    if((j1 = blocks[((l13 - 1) * length + l14) * width + length2] & 0xff) != Block.stone.blockID)
                        continue;
                    if(k6 == 0)
                    {
                        blocks[i16] = (byte)Block.mushroomBrown.blockID;
                        continue;
                    }
                    if(k6 == 1)
                        blocks[i16] = (byte)Block.mushroomRed.blockID;
                }

            }

        }

        IndevLevel level = new IndevLevel();
        level.waterLevel = sealevel;
//         level.setData(j, 64, k, f);
        findSpawn(level);
        level.createTime = java.lang.System.currentTimeMillis();
        level.creator = s;
        level.name = "A Nice World";
        if (width == 64 || length == 64){
            height = (width * length) / 2000;
        }else{
            height = (width * length) / 4000;
        }
        int iii = 1;
        if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_WOODS){
            iii = 51;
        }
        for (int ii = 0; ii<iii; ii++){
            for(int i3 = 0; i3 < height; i3++)
            {
                a((i3 * 50) / (height - 1) + 50);
                int l6 = rand.nextInt(width);
                int l8 = rand.nextInt(length);
                for(int i5 = 0; i5 < 20; i5++)
                {
                    int j10 = l6;
                    int j11 = l8;
                    for(int l12 = 0; l12 < 20; l12++)
                    {
                        j10 += rand.nextInt(6) - rand.nextInt(6);
                        j11 += rand.nextInt(6) - rand.nextInt(6);
                        if(j10 < 0 || j11 < 0 || j10 >= width || j11 >= length)
                            continue;
                        int i14 = ai[j10 + j11 * width] + 1;
                        if(rand.nextInt(4) == 0)
                            maybeGrowTree(level, j10, i14, j11);
                    }

                }

            }
        }

//         return level;
        return blocks;
    }

    public final boolean maybeGrowTree(IndevLevel world, int i1, int j1, int k1)
    {
        int l1 = rand.nextInt(3) + 4;
        boolean flag = true;
        if(j1 <= 0 || j1 + l1 + 1 > height)
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
                    if(k3 >= 0 && j2 >= 0 && i4 >= 0 && k3 < width && j2 < height && i4 < length)
                    {
                        if((k4 = blocks[(j2 * length + i4) * width + k3] & 0xff) != 0)
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
        if((k2 = blocks[((j1 - 1) * length + k1) * width + i1] & 0xff) != Block.grass.blockID && k2 != Block.dirt.blockID || j1 >= height - l1 - 1)
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
        int index = x+(y*length+z)*width;
        blocks[index]=(byte)id;
    }

    private byte getBlockId(int i, int j, int k){
        int x = i;
        int y = j;
        int z = k;
        int index = x+(y*length+z)*width;
        return blocks[index];
    }

    public boolean isOpaque(int id){
        if (id == Block.leaves.blockID){
            return false;
        }
        return Block.opaqueCubeLookup[id];
    }

    private void generateOre(int j, int k, int l, int i1)
    {
        i1 = width;
        int j1 = length;
        int k1 = height;
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
                            if((f9 = f9 * f9 + f10 * f10 * 2.0F + f11 * f11) >= f8 * f8 || l2 < 1 || i3 < 1 || j3 < 1 || l2 >= width - 1 || i3 >= height - 1 || j3 >= length - 1)
                                continue;
                            f9 = (i3 * length + j3) * width + l2;
                            if(blocks[(int)f9] == Block.stone.blockID)
                                blocks[(int)f9] = (byte)j;
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
        for(; 1 << k2 < width; k2++) { }
        for(; 1 << l2 < length; l2++) { }
        int i3 = length - 1;
        int j3 = width - 1;
        j2++;
        h[0] = ((j1 << l2) + k1 << k2) + i1;
        long l3 = 0L;
        i1 = width * length;
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
                        if(j2 == h.length)
                        {
                            arraylist.add(h);
                            h = new int[0x100000];
                            j2 = 0;
                        }
                        h[j2++] = j1 - width;
                    }
                    flag = flag3;
                }
                if(k1 < length - 1)
                {
                    boolean flag4;
                    if((flag4 = blocks[j1 + width] == l1) && !flag1)
                    {
                        if(j2 == h.length)
                        {
                            arraylist.add(h);
                            h = new int[0x100000];
                            j2 = 0;
                        }
                        h[j2++] = j1 + width;
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
        for(k1 = height; (getBlockId(i1, k1 - 1, j1) == 0 || !isOpaque(getBlockId(i1, k1 - 1, j1))) && k1 > 1; k1--) { }
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
            i1 = rand.nextInt(width / 2) + width / 4;
            j1 = rand.nextInt(length / 2) + length / 4;
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
        } while(k1 <= sealevel);
        level.i = i1;
        level.j = k1;
        level.k = j1;
        spawnX = i1;
        spawnY = k1;
        spawnZ = j1;
    }
}
