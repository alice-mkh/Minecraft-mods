package net.minecraft.src;

import java.util.*;
import net.minecraft.src.nbxlite.oldbiomes.*;

public class WorldChunkManagerHell extends WorldChunkManager
{
    /** this is the sole biome to utilize for this world */
    private BiomeGenBase biomeToUse;
    private float hellTemperature;

    /** The rainfall in the world */
    private float rainfall;
    private OldBiomeGenBase oldField_4201_e;

    public WorldChunkManagerHell(BiomeGenBase par1BiomeGenBase, float par2, float par3)
    {
        biomeToUse = par1BiomeGenBase;
        hellTemperature = par2;
        rainfall = par3;
        oldField_4201_e = OldBiomeGenBase.hell;
    }

    @Override
    public OldBiomeGenBase oldGetBiomeGenAt(int i, int j){
        return oldField_4201_e;
    }

    @Override
    public double[] getTemperatures_old(double ad[], int i, int j, int k, int l)
    {
        if(ad == null || ad.length < k * l)
        {
            ad = new double[k * l];
        }
        Arrays.fill(ad, 0, k * l, hellTemperature);
        return ad;
    }

    @Override
    public OldBiomeGenBase[] oldLoadBlockGeneratorData(OldBiomeGenBase aoldbiomegenbase[], int i, int j, int k, int l)
    {
        if(aoldbiomegenbase == null || aoldbiomegenbase.length < k * l)
        {
            aoldbiomegenbase = new OldBiomeGenBase[k * l];
        }
        if(temperature == null || temperature.length < k * l)
        {
            temperature = new double[k * l];
            humidity = new double[k * l];
        }
        Arrays.fill(aoldbiomegenbase, 0, k * l, oldField_4201_e);
        Arrays.fill(humidity, 0, k * l, rainfall);
        Arrays.fill(temperature, 0, k * l, hellTemperature);
        return aoldbiomegenbase;
    }

    public WorldChunkManagerHell(BiomeGenBase biomegenbase, float f, float f1, OldBiomeGenBase oldbiomegenbase)
    {
        biomeToUse = biomegenbase;
        hellTemperature = f;
        rainfall = f1;
        oldField_4201_e = oldbiomegenbase;
    }

    /**
     * Returns the BiomeGenBase related to the x, z position on the world.
     */
    public BiomeGenBase getBiomeGenAt(int par1, int par2)
    {
        return biomeToUse;
    }

    /**
     * Returns an array of biomes for the location input.
     */
    public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase par1ArrayOfBiomeGenBase[], int par2, int par3, int par4, int par5)
    {
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        Arrays.fill(par1ArrayOfBiomeGenBase, 0, par4 * par5, biomeToUse);
        return par1ArrayOfBiomeGenBase;
    }

    /**
     * Returns a list of temperatures to use for the specified blocks.  Args: listToReuse, x, y, width, length
     */
    public float[] getTemperatures(float par1ArrayOfFloat[], int par2, int par3, int par4, int par5)
    {
        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
        {
            par1ArrayOfFloat = new float[par4 * par5];
        }

        Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, hellTemperature);
        return par1ArrayOfFloat;
    }

    /**
     * Returns a list of rainfall values for the specified blocks. Args: listToReuse, x, z, width, length.
     */
    public float[] getRainfall(float par1ArrayOfFloat[], int par2, int par3, int par4, int par5)
    {
        if (par1ArrayOfFloat == null || par1ArrayOfFloat.length < par4 * par5)
        {
            par1ArrayOfFloat = new float[par4 * par5];
        }

        Arrays.fill(par1ArrayOfFloat, 0, par4 * par5, rainfall);
        return par1ArrayOfFloat;
    }

    /**
     * Returns biomes to use for the blocks and loads the other data like temperature and humidity onto the
     * WorldChunkManager Args: oldBiomeList, x, z, width, depth
     */
    public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase par1ArrayOfBiomeGenBase[], int par2, int par3, int par4, int par5)
    {
        if (par1ArrayOfBiomeGenBase == null || par1ArrayOfBiomeGenBase.length < par4 * par5)
        {
            par1ArrayOfBiomeGenBase = new BiomeGenBase[par4 * par5];
        }

        Arrays.fill(par1ArrayOfBiomeGenBase, 0, par4 * par5, biomeToUse);
        return par1ArrayOfBiomeGenBase;
    }

    /**
     * Return a list of biomes for the specified blocks. Args: listToReuse, x, y, width, length, cacheFlag (if false,
     * don't check biomeCache to avoid infinite loop in BiomeCacheBlock)
     */
    public BiomeGenBase[] getBiomeGenAt(BiomeGenBase par1ArrayOfBiomeGenBase[], int par2, int par3, int par4, int par5, boolean par6)
    {
        return loadBlockGeneratorData(par1ArrayOfBiomeGenBase, par2, par3, par4, par5);
    }

    /**
     * Finds a valid position within a range, that is in one of the listed biomes. Searches {par1,par2} +-par3 blocks.
     * Strongly favors positive y positions.
     */
    public ChunkPosition findBiomePosition(int par1, int par2, int par3, List par4List, Random par5Random)
    {
        if (par4List.contains(biomeToUse))
        {
            return new ChunkPosition((par1 - par3) + par5Random.nextInt(par3 * 2 + 1), 0, (par2 - par3) + par5Random.nextInt(par3 * 2 + 1));
        }
        else
        {
            return null;
        }
    }

    /**
     * checks given Chunk's Biomes against List of allowed ones
     */
    public boolean areBiomesViable(int par1, int par2, int par3, List par4List)
    {
        return par4List.contains(biomeToUse);
    }
}
