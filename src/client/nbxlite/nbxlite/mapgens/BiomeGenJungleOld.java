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
    public WorldGenerator getRandomWorldGenForTrees(Random par1Random)
    {
        if (par1Random.nextInt(10) == 0)
        {
            return worldGeneratorBigTree;
        }

        if (par1Random.nextInt(2) == 0)
        {
            return new WorldGenShrub(3, 0);
        }

        if (par1Random.nextInt(3) == 0)
        {
            return new OldWorldGenHugeTrees(false, 10 + par1Random.nextInt(20), 3, 3);
        }
        else
        {
            return new WorldGenTrees(false, 4 + par1Random.nextInt(7), 3, 3, true);
        }
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
