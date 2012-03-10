package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.indev.*;
import net.minecraft.src.nbxlite.indev.noise.IndevNoiseGeneratorOctaves;

public class ChunkProviderFinite
    implements IChunkProvider
{
    private World worldObj;
    public int xwidth;
    public int zwidth;
    private IndevNoiseGeneratorOctaves d;
    private Random rand;

    public ChunkProviderFinite(World world, long l, boolean flag)
    {
        worldObj = world;
        rand = new Random(l);
        xwidth = mod_noBiomesX.IndevWidthX/16;
        zwidth = mod_noBiomesX.IndevWidthZ/16;
        d = new IndevNoiseGeneratorOctaves(rand,8);
    }

    private void generateBoundaries(byte abyte0[])
    {
        int altitude = mod_noBiomesX.IndevHeight-32;
        int i = abyte0.length / 256;
        for (int j = 0; j < 16; j++)
        {
            for (int k = 0; k < 16; k++)
            {
                for (int l = 0; l < i; l++)
                {
                    int i1 = 0;
                    if (mod_noBiomesX.IndevMapType==1){
                        if (l < altitude-10){
                            i1 = Block.bedrock.blockID;
                        }else  if (l = altitude-10){
                            i1 = Block.dirt.blockID;
                        }else if (l < altitude){
                            if (mod_noBiomesX.MapTheme==1){
                                i1 = Block.lavaStill.blockID;
                            }else{
                                i1 = Block.waterStill.blockID;
                            }
                        }
                    }
                    if (mod_noBiomesX.IndevMapType==0 || mod_noBiomesX.IndevMapType==3){
                        if (l < altitude){
                            i1 = Block.bedrock.blockID;
                        }else if (l == altitude){
                            if (mod_noBiomesX.MapTheme==1){
                                i1 = Block.dirt.blockID;
                            }else{
                                i1 = Block.grass.blockID;
                            }
                        }
                    }
                    abyte0[j << 11 | k << 7 | l] = (byte)i1;
                }
            }
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        Chunk chunk;
        if (i>=0 && i<xwidth && j>=0 && j<zwidth){
            Converter c = new Converter(mod_noBiomesX.IndevWorld, mod_noBiomesX.IndevWidthX, mod_noBiomesX.IndevWidthZ, mod_noBiomesX.IndevHeight);
            chunk = new Chunk(worldObj, c.getChunkArray(i, j), i, j);
        }else{
            byte abyte0[] = new byte[32768];
            generateBoundaries(abyte0);
            chunk = new Chunk(worldObj, abyte0, i, j);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
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
        return "FlatLevelSource";
    }

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        return null;
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
    {
        return null;
    }
}
