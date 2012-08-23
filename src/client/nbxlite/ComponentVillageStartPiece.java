package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentVillageStartPiece extends ComponentVillageWell
{
    public final WorldChunkManager worldChunkMngr;
    public final boolean field_74927_b;

    /** World terrain type, 0 for normal, 1 for flap map */
    public final int terrainType;
    public StructureVillagePieceWeight structVillagePieceWeight;

    /**
     * Contains List of all spawnable Structure Piece Weights. If no more Pieces of a type can be spawned, they are
     * removed from this list
     */
    public ArrayList structureVillageWeightedPieceList;
    public ArrayList field_74932_i;
    public ArrayList field_74930_j;

    public ComponentVillageStartPiece(WorldChunkManager par1WorldChunkManager, int par2, Random par3Random, int par4, int par5, ArrayList par6ArrayList, int par7)
    {
        super(null, 0, par3Random, par4, par5);
        field_74932_i = new ArrayList();
        field_74930_j = new ArrayList();
        worldChunkMngr = par1WorldChunkManager;
        structureVillageWeightedPieceList = par6ArrayList;
        terrainType = par7;
        BiomeGenBase biomegenbase = par1WorldChunkManager.getBiomeGenAt(par4, par5);
        field_74927_b = (biomegenbase == BiomeGenBase.desert || biomegenbase == BiomeGenBase.desertHills) && ODNBXlite.desertVillages();
        field_74897_k = this;
    }

    public WorldChunkManager getWorldChunkManager()
    {
        return worldChunkMngr;
    }
}
