package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.oldbiomes.OldBiomeGenBase;

public class ChunkProviderBaseInfinite implements IChunkProvider{
    protected Random rand;
    protected World worldObj;
    protected final boolean mapFeaturesEnabled;
    protected int biomes;
    protected BiomeGenBase biomesForGeneration[];
    protected OldBiomeGenBase oldBiomesForGeneration[];
    protected boolean fixLight;

    public ChunkProviderBaseInfinite(World world, long l, boolean flag, int b){
        rand = new Random(l);
        worldObj = world;
        mapFeaturesEnabled = flag;
        biomes = b;
        fixLight = false;
    }

    public ChunkProviderBaseInfinite(World world, long l, boolean flag){
        this(world, l, flag, ODNBXlite.GEN_BIOMELESS);
    }

    protected void generateTerrain(int i, int j, byte abyte0[]){}

    protected void generateTerrainForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[], double ad[]){
        generateTerrain(i, j, abyte0);
    }

    protected void generateTerrainForBiome(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[]){
        generateTerrain(i, j, abyte0);
    }

    protected void replaceBlocks(int i, int j, byte abyte0[]){}

    protected void replaceBlocksForOldBiome(int i, int j, byte abyte0[], OldBiomeGenBase aoldbiomegenbase[]){
        replaceBlocks(i, j, abyte0);
    }

    protected void replaceBlocksForBiome(int i, int j, byte abyte0[], BiomeGenBase abiomegenbase[]){
        replaceBlocks(i, j, abyte0);
    }

    protected void generateStructures(int i, int j, byte abyte0[]){}

    public Chunk provideChunk(int i, int j){
        rand.setSeed((long)i * 0x4f9939f508L + (long)j * 0x1ef1565bd5L);
        byte abyte0[] = new byte[32768];
        if (biomes==ODNBXlite.GEN_NEWBIOMES){
            biomesForGeneration = worldObj.getWorldChunkManager().getBiomesForGeneration(biomesForGeneration, i * 4 - 2, j * 4 - 2, 10, 10);
            generateTerrainForBiome(i, j, abyte0, biomesForGeneration);
            biomesForGeneration = worldObj.getWorldChunkManager().loadBlockGeneratorData(biomesForGeneration, i * 16, j * 16, 16, 16);
            replaceBlocksForBiome(i, j, abyte0, biomesForGeneration);
        }else if (biomes==ODNBXlite.GEN_OLDBIOMES){
            oldBiomesForGeneration = worldObj.getWorldChunkManager().oldLoadBlockGeneratorData(oldBiomesForGeneration, i * 16, j * 16, 16, 16);
            generateTerrainForOldBiome(i, j, abyte0, oldBiomesForGeneration, worldObj.getWorldChunkManager().temperature);
            replaceBlocksForOldBiome(i, j, abyte0, oldBiomesForGeneration);
        }else{
            generateTerrain(i, j, abyte0);
            replaceBlocks(i, j, abyte0);
        }
        generateStructures(i, j, abyte0);
        Chunk chunk = new Chunk(worldObj, abyte0, i, j);
        if (fixLight){
            ExtendedBlockStorage extendedblockstorage = chunk.getBlockStorageArray()[64 >> 4];
            if (extendedblockstorage == null){
                extendedblockstorage = chunk.getBlockStorageArray()[64 >> 4] = new ExtendedBlockStorage((64 >> 4) << 4);
            }
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    public void populate(IChunkProvider ichunkprovider2, int x, int z){}

    protected void spawnAnimals(int i, int j){
        if (!ODNBXlite.OldSpawning){
            BiomeGenBase biomegenbase = worldObj.getWorldChunkManager().getBiomeGenAt(i + 16, j + 16);
            SpawnerAnimals.performWorldGenSpawning(worldObj, biomegenbase, i + 8, j + 8, 16, 16, rand);
        }
    }

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

    public int func_73152_e()
    {
        return 0;
    }
}
