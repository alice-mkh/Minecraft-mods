package net.minecraft.src.nbxlite.mapgens;

import net.minecraft.src.GenLayer;
import net.minecraft.src.IntCache;
import net.minecraft.src.BiomeGenBase;

public class GenLayerVillageLandscape18 extends GenLayer
{
    private BiomeGenBase allowedBiomes[];

    public GenLayerVillageLandscape18(long l, GenLayer genlayer)
    {
        super(l);
        allowedBiomes = (new BiomeGenBase[] {
            BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga
        });
        parent = genlayer;
    }

    @Override
    public int[] getInts(int i, int j, int k, int l)
    {
        int ai[] = parent.getInts(i, j, k, l);
        int ai1[] = IntCache.getIntCache(k * l);
        for(int i1 = 0; i1 < l; i1++)
        {
            for(int j1 = 0; j1 < k; j1++)
            {
                initChunkSeed(j1 + i, i1 + j);
                ai1[j1 + i1 * k] = ai[j1 + i1 * k] <= 0 ? 0 : allowedBiomes[nextInt(allowedBiomes.length)].biomeID;
            }

        }

        return ai1;
    }
}
