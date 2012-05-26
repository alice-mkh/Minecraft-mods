package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.indev.*;

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

    private int indexIndev(int x, int y, int z){
        return x+(y*mod_noBiomesX.IndevWidthZ+z)*mod_noBiomesX.IndevWidthX;
    }

    private byte[] getChunkArray(int x1, int z1){
        byte[] result = new byte[32768];
        for (int x=0; x<16; x++){
            for (int z=0; z<16; z++){
                for (int y=0; y<Math.min(mod_noBiomesX.IndevHeight, 128); y++){
                    byte block = mod_noBiomesX.IndevWorld[indexIndev(x+(x1*16), y, z+(z1*16))];
                    if (block==0){
                        continue;
                    }
                    result[x << 11 | z << 7 | y]=block;
                }
            }
        }
        return result;
    }

    private void fixDeepMaps(Chunk chunk, int x1, int z1){
        for (int x=0; x<16; x++){
            for (int z=0; z<16; z++){
                for (int y=128; y<mod_noBiomesX.IndevHeight; y++){
                    byte block = mod_noBiomesX.IndevWorld[indexIndev(x+(x1*16), y, z+(z1*16))];
                    if (block==0){
                        continue;
                    }
                    ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[y >> 4];
                    if (extendedblockstorage == null)
                    {
                        extendedblockstorage = chunk.getBlockStorageArray()[y >> 4] = new ExtendedBlockStorage((y >> 4) << 4);
                    }
                    extendedblockstorage.setExtBlockID(x, y & 0xf, z, block);
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
        boolean tall = mod_noBiomesX.IndevHeight>128;
        boolean tall2 = mod_noBiomesX.IndevHeight>160;
        Chunk chunk;
        if (i>=0 && i<mod_noBiomesX.IndevWidthX/16 && j>=0 && j<mod_noBiomesX.IndevWidthZ/16){
            if (mod_noBiomesX.IndevWorld==null){
                if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV){
                    IndevGenerator gen2 = new IndevGenerator(worldObj.getSeed());
                    if (mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_ISLAND){
                        gen2.island=true;
                    }
                    if (mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_FLOATING){
                        gen2.floating=true;
                    }
                    if (mod_noBiomesX.IndevMapType==mod_noBiomesX.TYPE_FLAT){
                        gen2.flat=true;
                    }
                    gen2.theme=mod_noBiomesX.MapTheme;
                    mod_noBiomesX.IndevWorld = gen2.generateLevel("Created with NBXlite!", mod_noBiomesX.IndevWidthX, mod_noBiomesX.IndevWidthZ, mod_noBiomesX.IndevHeight);
                }else{
                    mod_noBiomesX.IndevHeight = 64;
                    ClassicGenerator gen2 = new ClassicGenerator(worldObj.getSeed());
                    mod_noBiomesX.IndevWorld = gen2.generateLevel("Created with NBXlite!", mod_noBiomesX.IndevWidthX, mod_noBiomesX.IndevWidthZ, mod_noBiomesX.IndevHeight);
                }
            }
            chunk = new Chunk(worldObj, getChunkArray(i, j), i, j);
            if (tall){
                fixDeepMaps(chunk, i, j);
            }
        }else{
            chunk = new BoundChunk(worldObj, i, j);
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
