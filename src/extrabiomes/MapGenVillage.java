package net.minecraft.src;

import java.util.*;

public class MapGenVillage extends MapGenStructure
{
    /** A list of all the biomes villages can spawn in. */
    public static List villageSpawnBiomes;

    /** World terrain type, 0 for normal, 1 for flat map */
    private final int terrainType;

    public MapGenVillage(int par1)
    {
        terrainType = par1;
    }

    protected boolean canSpawnStructureAtCoords(int par1, int par2)
    {
        byte byte0 = 32;
        byte byte1 = 8;
        int i = par1;
        int j = par2;

        if (par1 < 0)
        {
            par1 -= byte0 - 1;
        }

        if (par2 < 0)
        {
            par2 -= byte0 - 1;
        }

        int k = par1 / byte0;
        int l = par2 / byte0;
        Random random = worldObj.setRandomSeed(k, l, 0x9e7f70);
        k *= byte0;
        l *= byte0;
        k += random.nextInt(byte0 - byte1);
        l += random.nextInt(byte0 - byte1);
        par1 = i;
        par2 = j;

        if (par1 == k && par2 == l)
        {
            boolean flag = worldObj.getWorldChunkManager().areBiomesViable(par1 * 16 + 8, par2 * 16 + 8, 0, villageSpawnBiomes);

            if (flag)
            {
                return true;
            }
        }

        return false;
    }

    protected StructureStart getStructureStart(int par1, int par2)
    {
        return new StructureVillageStart(worldObj, rand, par1, par2, terrainType);
    }

    static
    {
        villageSpawnBiomes = Arrays.asList(new BiomeGenBase[]
                {
                    BiomeGenBase.plains, BiomeGenBase.desert, BiomeGenBase.autumnWoods, BiomeGenBase.birchForest, BiomeGenBase.greenHills, BiomeGenBase.meadow, BiomeGenBase.savanna, BiomeGenBase.shrubland, 
                    BiomeGenBase.snowForest, BiomeGenBase.woodlands
                });
    }
}
