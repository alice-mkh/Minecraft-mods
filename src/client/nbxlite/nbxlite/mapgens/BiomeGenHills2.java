package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;

public class BiomeGenHills2 extends BiomeGenHills
{
    private WorldGenerator field_82915_S;

    public BiomeGenHills2(int par1)
    {
        super(par1);
        field_82915_S = new WorldGenMinable(Block.silverfish.blockID, 8);
    }

    @Override
    public void decorate(World par1World, Random par2Random, int par3, int par4)
    {
        super.decorate(par1World, par2Random, par3, par4);
        if (ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_13 || ODNBXlite.GenerateNewOres){
            int i = 3 + par2Random.nextInt(6);

            for (int j = 0; j < i; j++)
            {
                int k = par3 + par2Random.nextInt(16);
                int l = par2Random.nextInt(28) + 4;
                int i1 = par4 + par2Random.nextInt(16);
                int j1 = par1World.getBlockId(k, l, i1);

                if (j1 == Block.stone.blockID)
                {
                    par1World.setBlock(k, l, i1, Block.oreEmerald.blockID);
                }
            }
        }
        if (ODNBXlite.MapFeatures>=ODNBXlite.FEATURES_14 || ODNBXlite.GenerateNewOres){
            for (int j = 0; j < 7; j++)
            {
                int l = par3 + par2Random.nextInt(16);
                int j1 = par2Random.nextInt(64);
                int l1 = par4 + par2Random.nextInt(16);
                field_82915_S.generate(par1World, par2Random, l, j1, l1);
            }
        }
    }
}
