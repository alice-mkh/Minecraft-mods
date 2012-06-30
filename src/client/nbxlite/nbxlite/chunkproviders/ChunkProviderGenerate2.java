package net.minecraft.src.nbxlite.chunkproviders;

import java.util.List;
import net.minecraft.src.*;

public class ChunkProviderGenerate2
    implements IChunkProvider
{
    public ChunkProviderBaseFinite indevGen;
    public ChunkProviderBaseFinite classicGen;
    public ChunkProviderBaseInfinite infdevGen;
    public ChunkProviderBaseInfinite oldInfdevGen;
    public ChunkProviderBaseInfinite alphaGen;
    public ChunkProviderBaseInfinite betaGen;
    public ChunkProviderBaseInfinite betaGenSky;
    public ChunkProviderBaseInfinite releaseGen;

    public ChunkProviderGenerate2(World world, long l, boolean flag)
    {
        infdevGen = new ChunkProviderGenerateInfdev(world, l, flag);
        oldInfdevGen = new ChunkProviderGenerateOldInfdev(world, l, flag);
        indevGen = new ChunkProviderIndev(world, l);
        classicGen = new ChunkProviderClassic(world, l);
        alphaGen = new ChunkProviderGenerateAlpha(world, l, flag);
        betaGen = new ChunkProviderGenerateBeta(world, l, flag);
        betaGenSky = new ChunkProviderSky(world, l, flag);
        releaseGen = new ChunkProviderGenerateRelease(world, l, flag);
    }

    private IChunkProvider getCurrentProvider(){
        if(mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS){
            if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_ALPHA11201){
                return alphaGen;
            }else if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0420 || mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0608){
                return infdevGen;
            }else if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INFDEV0227){
                return oldInfdevGen;
            }else if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_INDEV){
                return indevGen;
            }
            return classicGen;
        } else if(mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES){
            if (mod_noBiomesX.MapFeatures==mod_noBiomesX.FEATURES_SKY){
                return betaGenSky;
            }
            return betaGen;
        }
        return releaseGen;
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        return getCurrentProvider().provideChunk(i, j);
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        getCurrentProvider().populate(ichunkprovider, i, j);
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

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        return releaseGen.getPossibleCreatures(enumcreaturetype, i, j, k);
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
    {
        return getCurrentProvider().findClosestStructure(world, s, i, j, k);
    }
}
