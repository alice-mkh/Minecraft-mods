package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

class StructureScatteredFeatureStones extends StructurePieceBlockSelector
{
    private StructureScatteredFeatureStones()
    {
    }

    /**
     * picks Block Ids and Metadata (Silverfish)
     */
    public void selectBlocks(Random par1Random, int par2, int par3, int par4, boolean par5)
    {
        if (par1Random.nextFloat() < 0.4F)
        {
            selectedBlockId = Block.cobblestone.blockID;
        }
        else
        {
            selectedBlockId = Block.cobblestoneMossy.blockID;
        }
    }

    StructureScatteredFeatureStones(ComponentScatteredFeaturePieces2 par1ComponentScatteredFeaturePieces2)
    {
        this();
    }
}
