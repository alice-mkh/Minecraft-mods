package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.oldbiomes.OldBiomeGenBase;

public class ChunkProviderBase implements IChunkProvider{
    protected Random rand;
    protected World worldObj;
    protected final boolean mapFeaturesEnabled;

    public ChunkProviderBase(World world, long l, boolean flag){
        rand = new Random(l);
        worldObj = world;
        mapFeaturesEnabled = flag;
    }

    protected void generateTerrain(int i, int j, byte abyte0[]){}

    protected void generateTerrainForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[], double ad[]){
        generateTerrain(i, j, abyte0);
    }

    protected void generateTerrainForBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[], double ad[]){
        generateTerrain(i, j, abyte0);
    }

    protected void replaceBlocks(int i, int j, byte abyte0[]){}

    protected void replaceBlocksForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[]){
        replaceBlocks(i, j, abyte0);
    }

    protected void replaceBlocksForBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[]){
        replaceBlocks(i, j, abyte0);
    }

    public Chunk provideChunk(int i, int j){
        rand.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        generateTerrain(i, j, abyte0);
        replaceBlocks(i, j, abyte0);
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[64 >> 4];
        if (extendedblockstorage == null){
            extendedblockstorage = chunk.getBlockStorageArray()[64 >> 4] = new ExtendedBlockStorage((64 >> 4) << 4);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(IChunkProvider ichunkprovider2, int x, int z){}

    public Chunk loadChunk(int i, int j){
        return provideChunk(i, j);
    }

    public boolean chunkExists(int i, int j){
        return true;
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate){
        return true;
    }

    public boolean unload100OldestChunks(){
        return false;
    }

    public boolean canSave(){
        return true;
    }

    public String makeString(){
        return "RandomLevelSource";
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k){
 /*       if("Stronghold".equals(s) && strongholdGenerator != null){
            return strongholdGenerator.getNearestInstance(world, i, j, k);
        }else{
            return null;
        }*/
        return null;
    }

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k){
        WorldChunkManager worldchunkmanager = worldObj.getWorldChunkManager();
        if (worldchunkmanager == null)
        {
            return null;
        }
        BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAtChunkCoord(new ChunkCoordIntPair(i >> 4, k >> 4));
        if (biomegenbase == null)
        {
            return null;
        }
        else
        {
            return biomegenbase.getSpawnableList(enumcreaturetype);
        }
    }
}
