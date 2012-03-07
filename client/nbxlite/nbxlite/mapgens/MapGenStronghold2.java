package net.minecraft.src.nbxlite.mapgens;

import java.io.PrintStream;
import java.util.*;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.ChunkCoordIntPair;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.MapGenStructure;
import net.minecraft.src.StructureStart;
import net.minecraft.src.ComponentStrongholdStairs2;

public class MapGenStronghold2 extends MapGenStructure
{
    private BiomeGenBase allowedBiomeGenBases[];
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair structureCoords[];

    public MapGenStronghold2()
    {
        allowedBiomeGenBases = (new BiomeGenBase[]
                {
                    BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains, BiomeGenBase.iceMountains, BiomeGenBase.desertHills, BiomeGenBase.forestHills, BiomeGenBase.extremeHillsEdge,
                    BiomeGenBase.field_48416_w, BiomeGenBase.field_48417_x
                });
        structureCoords = new ChunkCoordIntPair[3];
    }

    protected boolean canSpawnStructureAtCoords(int i, int j)
    {
        if(!ranBiomeCheck)
        {
            rand.setSeed(worldObj.getSeed());
            double d = rand.nextDouble() * 3.1415926535897931D * 2D;
            for(int l = 0; l < structureCoords.length; l++)
            {
                double d1 = (1.25D + rand.nextDouble()) * 32D;
                int j1 = (int)Math.round(Math.cos(d) * d1);
                int k1 = (int)Math.round(Math.sin(d) * d1);
                ArrayList arraylist = new ArrayList();
                BiomeGenBase abiomegenbase[] = allowedBiomeGenBases;
                int l1 = abiomegenbase.length;
                for(int i2 = 0; i2 < l1; i2++)
                {
                    BiomeGenBase biomegenbase = abiomegenbase[i2];
                    arraylist.add(biomegenbase);
                }

                ChunkPosition chunkposition;
                chunkposition = worldObj.getWorldChunkManager().func_35556_a_ignoreBiome((j1 << 4) + 8, (k1 << 4) + 8, 112, arraylist, rand);
                if(chunkposition != null)
                {
                    j1 = chunkposition.x >> 4;
                    k1 = chunkposition.z >> 4;
                } else
                {
                    System.out.println((new StringBuilder()).append("Placed stronghold in INVALID biome at (").append(j1).append(", ").append(k1).append(")").toString());
                }
                structureCoords[l] = new ChunkCoordIntPair(j1, k1);
                d += 6.2831853071795862D / (double)structureCoords.length;
            }

            ranBiomeCheck = true;
        }
        ChunkCoordIntPair achunkcoordintpair[] = structureCoords;
        int k = achunkcoordintpair.length;
        for(int i1 = 0; i1 < k; i1++)
        {
            ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[i1];
            if(i == chunkcoordintpair.chunkXPos && j == chunkcoordintpair.chunkZPos)
            {
                return true;
            }
        }

        return false;
    }

    protected List func_40482_a()
    {
        ArrayList arraylist = new ArrayList();
        ChunkCoordIntPair achunkcoordintpair[] = structureCoords;
        int i = achunkcoordintpair.length;
        for(int j = 0; j < i; j++)
        {
            ChunkCoordIntPair chunkcoordintpair = achunkcoordintpair[j];
            if(chunkcoordintpair != null)
            {
                arraylist.add(chunkcoordintpair.getChunkPosition(64));
            }
        }

        return arraylist;
    }

    protected StructureStart getStructureStart(int i, int j)
    {
        StructureStrongholdStart2 structurestrongholdstart;
        for(structurestrongholdstart = new StructureStrongholdStart2(worldObj, rand, i, j); structurestrongholdstart.getComponents().isEmpty() || ((ComponentStrongholdStairs2)structurestrongholdstart.getComponents().get(0)).field_40009_b == null; structurestrongholdstart = new StructureStrongholdStart2(worldObj, rand, i, j)) { }
        return structurestrongholdstart;
    }
}
