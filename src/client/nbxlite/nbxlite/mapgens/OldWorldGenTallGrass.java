package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class OldWorldGenTallGrass extends WorldGenerator
{
    /** Stores ID for OldWorldGenTallGrass */
    private int tallGrassID;
    private int tallGrassMetadata;

    public OldWorldGenTallGrass(int par1, int par2)
    {
        tallGrassID = par1;
        tallGrassMetadata = par2;
    }

    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5)
    {
        for (int i = 0; ((i = par1World.getBlockId(par3, par4, par5)) == 0 || i == Block.leaves.blockID) && par4 > 0; par4--) { }

        for (int j = 0; j < 128; j++)
        {
            int k = (par3 + par2Random.nextInt(8)) - par2Random.nextInt(8);
            int l = (par4 + par2Random.nextInt(4)) - par2Random.nextInt(4);
            int i1 = (par5 + par2Random.nextInt(8)) - par2Random.nextInt(8);

            boolean old = ODNBXlite.MapFeatures < ODNBXlite.FEATURES_13 || ODNBXlite.Generator < ODNBXlite.GEN_NEWBIOMES;
            boolean oldc = ((BlockFlower)Block.blocksList[tallGrassID]).canBlockStay(par1World, k, l, i1);
            boolean newc = Block.blocksList[tallGrassID].canBlockStay(par1World, k, l, i1);
            if (par1World.isAirBlock(k, l, i1) && ((oldc && old) || (!old && newc)))
            {
                par1World.setBlockAndMetadata(k, l, i1, tallGrassID, tallGrassMetadata);
            }
        }

        return true;
    }
}
