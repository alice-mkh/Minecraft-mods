package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.noise.InfdevOldNoiseGeneratorOctaves;

public class ChunkProviderGenerateOldInfdev
    implements IChunkProvider
{
    private Random a;
    private InfdevOldNoiseGeneratorOctaves b;
    private InfdevOldNoiseGeneratorOctaves c;
    private InfdevOldNoiseGeneratorOctaves d;
    private InfdevOldNoiseGeneratorOctaves e;
    private InfdevOldNoiseGeneratorOctaves f;
    private InfdevOldNoiseGeneratorOctaves g;
    private World worldObj;

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

    public ChunkProviderGenerateOldInfdev(World world, long l, boolean flag)
    {
        worldObj = world;
        a = new Random(l);
        b = new InfdevOldNoiseGeneratorOctaves(a,16);
        c = new InfdevOldNoiseGeneratorOctaves(a,16);
        d = new InfdevOldNoiseGeneratorOctaves(a,8);
        e = new InfdevOldNoiseGeneratorOctaves(a,4);
        f = new InfdevOldNoiseGeneratorOctaves(a,4);
        g = new InfdevOldNoiseGeneratorOctaves(a,5);
        new InfdevOldNoiseGeneratorOctaves(a,3);
        new InfdevOldNoiseGeneratorOctaves(a,3);
        new InfdevOldNoiseGeneratorOctaves(a,3);
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i11, int j11)
    {
        a.setSeed((long)i11 * 0x4f9939f508L + (long)j11 * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        int i = i11 << 4;
        int g11 = j11 << 4;
        int j = 0;
        for(int k = i; k < i + 16; k++)
        {
            for(int l = g11; l < g11 + 16; l++)
            {
                int i1 = k / 1024;
                int j1 = l / 1024;
                float f1 = (float)(b.a((float)k / 0.03125F, 0.0D, (float)l / 0.03125F) - c.a((float)k / 0.015625F, 0.0D, (float)l / 0.015625F)) / 512F / 4F;
                float f2 = (float)f.func_806_a((float)k / 4F, (float)l / 4F);
                float f3 = (float)g.func_806_a((float)k / 8F, (float)l / 8F) / 8F;
                f2 = f2 <= 0.0F ? (float)(e.func_806_a((float)k * 0.2571428F, (float)l * 0.2571428F) * (double)f3) : (float)((d.func_806_a((float)k * 0.2571428F * 2.0F, (float)l * 0.2571428F * 2.0F) * (double)f3) / 4D);
                f1 = (int)(f1 + 64F + f2);
                if((float)f.func_806_a((double)k, (double)l) < 0.0F)
                {
                    f1 = (int)f1 / 2 << 1;
                    if((float)f.func_806_a((double)k / 5, (double)l / 5) < 0.0F)
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
                        if (mod_noBiomesX.MapTheme == 1){
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
                        if (mod_noBiomesX.MapTheme == 1){
                            l1 = Block.lavaStill.blockID;
                        }else{
                            l1 = Block.waterStill.blockID;
                        }
                    }
                    a.setSeed(i11 + j11 * 13871);
                    int i2 = (i11 << 10) + 128 + a.nextInt(512);
                    int j2 = (j11 << 10) + 128 + a.nextInt(512);
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

        Chunk chunk = new Chunk(worldObj, abyte0, i11, j11);
        chunk.generateSkylightMap();
        return chunk;
//         return abyte0;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider2, int i1, int j1)
    {
        if (mod_noBiomesX.MapTheme == 2){
            int l3 = (int)((d.func_806_a((double)i1 * 8D, (double)j1 * 8D) / 8D + a.nextDouble() * 4D + 4D) / 3D);
            if(l3 < 0)
            {
                l3 = 0;
            }
            if(a.nextInt(10) == 0)
            {
                l3++;
            }
            l3 += 20;
            Object obj = new WorldGenTrees(false);
            for(int i11 = 0; i11 < l3; i11++)
            {
                int l13 = i1 * 16 + a.nextInt(16) + 8;
                int k16 = j1 * 16 + a.nextInt(16) + 8;
                ((WorldGenerator)obj).setScale(1.0D, 1.0D, 1.0D);
                ((WorldGenerator)obj).generate(worldObj, a, l13, worldObj.getHeightValue(l13, k16), k16);
            }
        }
        if (mod_noBiomesX.UseNewSpawning || mod_noBiomesX.MobSpawning==2){
            BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt((i1 * 16) + 16, (j1 * 16) + 16);
            SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, (i1 * 16) + 8, (j1 * 16) + 8, 16, 16, a);
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
