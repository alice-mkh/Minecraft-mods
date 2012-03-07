package net.minecraft.src.nbxlite.mapgens;

import net.minecraft.src.GenLayer;
import net.minecraft.src.IntCache;
import net.minecraft.src.BiomeGenBase;

public class GenLayerShore10 extends GenLayer
{
    public GenLayerShore10(long l, GenLayer genlayer)
    {
        super(l);
        parent = genlayer;
    }

    public int[] getInts(int i, int j, int k, int l)
    {
        int ai[] = parent.getInts(i - 1, j - 1, k + 2, l + 2);
        int ai1[] = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < l; i1++)
        {
            for (int j1 = 0; j1 < k; j1++)
            {
                initChunkSeed(j1 + i, i1 + j);
                int k1 = ai[j1 + 1 + (i1 + 1) * (k + 2)];
                if (k1 == BiomeGenBase.mushroomIsland.biomeID)
                {
                    int l1 = ai[j1 + 1 + ((i1 + 1) - 1) * (k + 2)];
                    int i2 = ai[j1 + 1 + 1 + (i1 + 1) * (k + 2)];
                    int j2 = ai[((j1 + 1) - 1) + (i1 + 1) * (k + 2)];
                    int k2 = ai[j1 + 1 + (i1 + 1 + 1) * (k + 2)];
                    if (l1 == BiomeGenBase.ocean.biomeID || i2 == BiomeGenBase.ocean.biomeID || j2 == BiomeGenBase.ocean.biomeID || k2 == BiomeGenBase.ocean.biomeID)
                    {
                        ai1[j1 + i1 * k] = BiomeGenBase.mushroomIslandShore.biomeID;
                    } else
                    {
                        ai1[j1 + i1 * k] = k1;
                    }
                } else
                {
                    ai1[j1 + i1 * k] = k1;
                }
            }
        }

        return ai1;
    }
}
