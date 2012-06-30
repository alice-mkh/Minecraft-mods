package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTrees;
import net.minecraft.src.nbxlite.noise.InfdevOldNoiseGeneratorOctaves;

public class ChunkProviderGenerateOldInfdev extends ChunkProviderBaseInfinite{
    private InfdevOldNoiseGeneratorOctaves terrainAlt1Generator;
    private InfdevOldNoiseGeneratorOctaves terrainAlt2Generator;
    private InfdevOldNoiseGeneratorOctaves terrainGenerator;
    private InfdevOldNoiseGeneratorOctaves noiseSandGen;
    private InfdevOldNoiseGeneratorOctaves rockSandGen;
    private InfdevOldNoiseGeneratorOctaves unknownGen;

    protected ChunkProviderGenerateOldInfdev(World world, long l, boolean flag){
        super(world, l, flag);
        terrainAlt1Generator = new InfdevOldNoiseGeneratorOctaves(rand, 16);
        terrainAlt2Generator = new InfdevOldNoiseGeneratorOctaves(rand, 16);
        terrainGenerator = new InfdevOldNoiseGeneratorOctaves(rand, 8);
        noiseSandGen = new InfdevOldNoiseGeneratorOctaves(rand, 4);
        rockSandGen = new InfdevOldNoiseGeneratorOctaves(rand, 4);
        unknownGen = new InfdevOldNoiseGeneratorOctaves(rand, 5);
        new InfdevOldNoiseGeneratorOctaves(rand, 3);
        new InfdevOldNoiseGeneratorOctaves(rand, 3);
        new InfdevOldNoiseGeneratorOctaves(rand, 3);
    }

    protected void generateTerrain(int i11, int j11, byte abyte0[]){
        int i = i11 << 4;
        int g11 = j11 << 4;
        int j = 0;
        for(int k = i; k < i + 16; k++)
        {
            for(int l = g11; l < g11 + 16; l++)
            {
                int i1 = k / 1024;
                int j1 = l / 1024;
                float f1 = (float)(terrainAlt1Generator.a((float)k / 0.03125F, 0.0D, (float)l / 0.03125F) - terrainAlt2Generator.a((float)k / 0.015625F, 0.0D, (float)l / 0.015625F)) / 512F / 4F;
                float f2 = (float)rockSandGen.func_806_a((float)k / 4F, (float)l / 4F);
                float f3 = (float)unknownGen.func_806_a((float)k / 8F, (float)l / 8F) / 8F;
                f2 = f2 <= 0.0F ? (float)(noiseSandGen.func_806_a((float)k * 0.2571428F, (float)l * 0.2571428F) * (double)f3) : (float)((terrainGenerator.func_806_a((float)k * 0.2571428F * 2.0F, (float)l * 0.2571428F * 2.0F) * (double)f3) / 4D);
                f1 = (int)(f1 + 64F + f2);
                if((float)rockSandGen.func_806_a((double)k, (double)l) < 0.0F)
                {
                    f1 = (int)f1 / 2 << 1;
                    if((float)rockSandGen.func_806_a((double)k / 5, (double)l / 5) < 0.0F)
                    {
                        f1++;
                    }
                }
                for(int k1 = 0; k1 < 128; k1++)
                {
                    int l1 = 0;
                    if(((k == 0 && i11 == 0) || (l == 0 && j11 == 0)) && k1 <= f1 + 2)
                    {
                        l1 = Block.obsidian.blockID;
                    } else
                    if(k1 == f1 + 1 && f1 >= 64 && Math.random() < 0.02D)
                    {
                        l1 = Block.plantYellow.blockID;
                    } else
                    if(k1 == f1 && f1 >= 64)
                    {
                        if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
                            l1 = Block.dirt.blockID;
                        }else{
                            l1 = Block.grass.blockID;
                        }
                    } else
                    if(k1 <= f1 - 2)
                    {
                        l1 = Block.stone.blockID;
                    } else
                    if(k1 <= f1)
                    {
                        l1 = Block.dirt.blockID;
                    } else
                    if(k1 <= 64)
                    {
                        if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_HELL){
                            l1 = Block.lavaStill.blockID;
                        }else{
                            l1 = Block.waterStill.blockID;
                        }
                    }
                    rand.setSeed(i11 + j11 * 13871);
                    int i2 = (i11 << 10) + 128 + rand.nextInt(512);
                    int j2 = (j11 << 10) + 128 + rand.nextInt(512);
                    i2 = k - i2;
                    j2 = l - j2;
                    if(i2 < 0)
                    {
                        i2 = -i2;
                    }
                    if(j2 < 0)
                    {
                        j2 = -j2;
                    }
                    if(j2 > i2)
                    {
                        i2 = j2;
                    }
                    if((i2 = 127 - i2) == 255)
                    {
                        i2 = 1;
                    }
                    if(i2 < f1)
                    {
                        i2 = (int)f1;
                    }
                    if(k1 <= i2 && (l1 == 0 || l1 == Block.waterStill.blockID || l1 == Block.lavaStill.blockID))
                    {
                        l1 = Block.brick.blockID;
                    }
                    if(l1 < 0)
                    {
                        l1 = 0;
                    }
                    abyte0[j++] = (byte)l1;
                }

            }

        }
    }

    public void populate(IChunkProvider ichunkprovider2, int i1, int j1){
        if (mod_noBiomesX.MapTheme==mod_noBiomesX.THEME_WOODS){
            int l3 = (int)((noiseSandGen.func_806_a((double)i1 * 8D, (double)j1 * 8D) / 8D + rand.nextDouble() * 4D + 4D) / 3D);
            if(l3 < 0)
            {
                l3 = 0;
            }
            if(rand.nextInt(10) == 0)
            {
                l3++;
            }
            l3 += 20;
            Object obj = new OldWorldGenTrees(false);
            for(int i11 = 0; i11 < l3; i11++)
            {
                int l13 = i1 * 16 + rand.nextInt(16) + 8;
                int k16 = j1 * 16 + rand.nextInt(16) + 8;
                ((WorldGenerator)obj).setScale(1.0D, 1.0D, 1.0D);
                ((WorldGenerator)obj).generate(worldObj, rand, l13, worldObj.getHeightValue(l13, k16), k16);
            }
        }
        spawnAnimals(i1 * 16, j1 * 16);
    }
}
