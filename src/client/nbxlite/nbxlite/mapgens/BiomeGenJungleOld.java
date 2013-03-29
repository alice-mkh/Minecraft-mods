package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class BiomeGenJungleOld extends BiomeGenJungle
{
    public BiomeGenJungleOld(int par1)
    {
        super(par1);
    }

    /**
     * Gets a WorldGen appropriate for this biome.
     */
    @Override
    public WorldGenerator getRandomWorldGenForGrass(Random par1Random)
    {
        if (par1Random.nextInt(4) == 0)
        {
            return new OldWorldGenTallGrass(Block.tallGrass.blockID, 2);
        }
        else
        {
            return new OldWorldGenTallGrass(Block.tallGrass.blockID, 1);
        }
    }
}
