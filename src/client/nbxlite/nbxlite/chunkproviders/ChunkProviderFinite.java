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
    private Random rand;

    public ChunkProviderFinite(World world, long l, boolean flag)
    {
        worldObj = world;
        rand = new Random(l);
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
                        if (l == altitude-11){
                            i1 = Block.bedrock.blockID;
                        }else  if (l == altitude-10){
                            i1 = Block.dirt.blockID;
                        }else if (l < altitude && l > altitude-11){
                            if (mod_noBiomesX.MapTheme==1){
                                i1 = Block.lavaStill.blockID;
                            }else{
                                i1 = Block.waterStill.blockID;
                            }
                        }
                    }
                    if (mod_noBiomesX.IndevMapType==0 || mod_noBiomesX.IndevMapType==3){
                        if (l == altitude-1){
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
        if (i>=0 && i<mod_noBiomesX.IndevWidthX/16 && j>=0 && j<mod_noBiomesX.IndevWidthZ/16){
            if (mod_noBiomesX.IndevWorld==null){
                IndevGenerator gen2 = new IndevGenerator(ModLoader.getMinecraftInstance().loadingScreen, worldObj.getSeed());
                if (mod_noBiomesX.IndevMapType==1){
                    gen2.island=true;
                }
                if (mod_noBiomesX.IndevMapType==2){
                    gen2.floating=true;
                }
                if (mod_noBiomesX.IndevMapType==3){
                    gen2.flat=true;
                }
                gen2.theme=mod_noBiomesX.MapTheme;
                ModLoader.getMinecraftInstance().loadingScreen.printText(StatCollector.translateToLocal("menu.generatingLevel"));
                gen2.generateLevel("Created with NBXlite!", mod_noBiomesX.IndevWidthX, mod_noBiomesX.IndevWidthZ, mod_noBiomesX.IndevHeight);
                mod_noBiomesX.IndevWorld = gen2.blocks;
            }
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
