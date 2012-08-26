package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.*;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenDeadBush;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenFlowers;
import net.minecraft.src.nbxlite.mapgens.OldWorldGenTallGrass;

public class BiomeDecorator2 extends BiomeDecorator
{
    public BiomeDecorator2(BiomeGenBase par1BiomeGenBase)
    {
        super(par1BiomeGenBase);
        plantYellowGen = new OldWorldGenFlowers(Block.plantYellow.blockID);
        if (ODNBXlite.MapFeatures<=ODNBXlite.FEATURES_BETA181){
            plantRedGen = new OldWorldGenFlowers(Block.plantYellow.blockID);
        }else{
            plantRedGen = new OldWorldGenFlowers(Block.plantRed.blockID);
        }
        mushroomBrownGen = new OldWorldGenFlowers(Block.mushroomBrown.blockID);
        mushroomRedGen = new OldWorldGenFlowers(Block.mushroomRed.blockID);
    }

    /**
     * The method that does the work of actually decorating chunks
     */
    protected void decorate()
    {
        generateOres();

        for (int i = 0; i < sandPerChunk2; i++)
        {
            int i1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k5 = chunk_Z + randomGenerator.nextInt(16) + 8;
            sandGen.generate(currentWorld, randomGenerator, i1, currentWorld.getTopSolidOrLiquidBlock(i1, k5), k5);
        }

        for (int j = 0; j < clayPerChunk; j++)
        {
            int j1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l5 = chunk_Z + randomGenerator.nextInt(16) + 8;
            clayGen.generate(currentWorld, randomGenerator, j1, currentWorld.getTopSolidOrLiquidBlock(j1, l5), l5);
        }

        for (int k = 0; k < sandPerChunk; k++)
        {
            int k1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i6 = chunk_Z + randomGenerator.nextInt(16) + 8;
            sandGen.generate(currentWorld, randomGenerator, k1, currentWorld.getTopSolidOrLiquidBlock(k1, i6), i6);
        }

        int l = treesPerChunk;

        if (randomGenerator.nextInt(10) == 0)
        {
            l++;
        }

        for (int l1 = 0; l1 < l; l1++)
        {
            int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
            WorldGenerator worldgenerator = biome.getRandomWorldGenForTrees(randomGenerator);
            if (worldgenerator instanceof WorldGenTrees && ODNBXlite.MapFeatures<ODNBXlite.FEATURES_12){
                worldgenerator = biome.oldWorldGeneratorTrees;
            }
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(currentWorld, randomGenerator, j6, currentWorld.getHeightValue(j6, k10), k10);
        }

        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
            for (int i2 = 0; i2 < bigMushroomsPerChunk; i2++)
            {
                int k6 = chunk_X + randomGenerator.nextInt(16) + 8;
                int l10 = chunk_Z + randomGenerator.nextInt(16) + 8;
                bigMushroomGen.generate(currentWorld, randomGenerator, k6, currentWorld.getHeightValue(k6, l10), l10);
            }
        }

        for (int j2 = 0; j2 < flowersPerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            plantYellowGen.generate(currentWorld, randomGenerator, l6, i11, l14);

            if (randomGenerator.nextInt(4) == 0)
            {
                int i7 = chunk_X + randomGenerator.nextInt(16) + 8;
                int j11 = randomGenerator.nextInt(128);
                int i15 = chunk_Z + randomGenerator.nextInt(16) + 8;
                plantRedGen.generate(currentWorld, randomGenerator, i7, j11, i15);
            }
        }

        for (int k2 = 0; k2 < grassPerChunk; k2++)
        {
            int j7 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k11 = randomGenerator.nextInt(128);
            int j15 = chunk_Z + randomGenerator.nextInt(16) + 8;
            if (ODNBXlite.MapFeatures<ODNBXlite.FEATURES_12){
                (new OldWorldGenTallGrass(Block.tallGrass.blockID, 1)).generate(currentWorld, randomGenerator, j7, k11, j15);
            }else{
                WorldGenerator worldgenerator1 = biome.getRandomWorldGenForGrass(randomGenerator);
                worldgenerator1.generate(currentWorld, randomGenerator, j7, k11, j15);
            }
        }

        for (int l2 = 0; l2 < deadBushPerChunk; l2++)
        {
            int k7 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l11 = randomGenerator.nextInt(128);
            int k15 = chunk_Z + randomGenerator.nextInt(16) + 8;
            (new OldWorldGenDeadBush(Block.deadBush.blockID)).generate(currentWorld, randomGenerator, k7, l11, k15);
        }

        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
            for (int i3 = 0; i3 < waterlilyPerChunk; i3++)
            {
                int l7 = chunk_X + randomGenerator.nextInt(16) + 8;
                int i12 = chunk_Z + randomGenerator.nextInt(16) + 8;
                int l15;

                for (l15 = randomGenerator.nextInt(128); l15 > 0 && currentWorld.getBlockId(l7, l15 - 1, i12) == 0; l15--) { }

                waterlilyGen.generate(currentWorld, randomGenerator, l7, l15, i12);
            }
        }

        for (int j3 = 0; j3 < mushroomsPerChunk; j3++)
        {
            if (randomGenerator.nextInt(4) == 0)
            {
                int i8 = chunk_X + randomGenerator.nextInt(16) + 8;
                int j12 = chunk_Z + randomGenerator.nextInt(16) + 8;
                int i16 = currentWorld.getHeightValue(i8, j12);
                mushroomBrownGen.generate(currentWorld, randomGenerator, i8, i16, j12);
            }

            if (randomGenerator.nextInt(8) == 0)
            {
                int j8 = chunk_X + randomGenerator.nextInt(16) + 8;
                int k12 = chunk_Z + randomGenerator.nextInt(16) + 8;
                int j16 = randomGenerator.nextInt(128);
                mushroomRedGen.generate(currentWorld, randomGenerator, j8, j16, k12);
            }
        }

        if (randomGenerator.nextInt(4) == 0)
        {
            int k3 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k8 = randomGenerator.nextInt(128);
            int l12 = chunk_Z + randomGenerator.nextInt(16) + 8;
            mushroomBrownGen.generate(currentWorld, randomGenerator, k3, k8, l12);
        }

        if (randomGenerator.nextInt(8) == 0)
        {
            int l3 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l8 = randomGenerator.nextInt(128);
            int i13 = chunk_Z + randomGenerator.nextInt(16) + 8;
            mushroomRedGen.generate(currentWorld, randomGenerator, l3, l8, i13);
        }

        for (int i4 = 0; i4 < reedsPerChunk; i4++)
        {
            int i9 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j13 = chunk_Z + randomGenerator.nextInt(16) + 8;
            int k16 = randomGenerator.nextInt(128);
            reedGen.generate(currentWorld, randomGenerator, i9, k16, j13);
        }

        for (int j4 = 0; j4 < 10; j4++)
        {
            int j9 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k13 = randomGenerator.nextInt(128);
            int l16 = chunk_Z + randomGenerator.nextInt(16) + 8;
            reedGen.generate(currentWorld, randomGenerator, j9, k13, l16);
        }

        if (randomGenerator.nextInt(32) == 0)
        {
            int k4 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k9 = randomGenerator.nextInt(128);
            int l13 = chunk_Z + randomGenerator.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(currentWorld, randomGenerator, k4, k9, l13);
        }

        for (int l4 = 0; l4 < cactiPerChunk; l4++)
        {
            int l9 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i14 = randomGenerator.nextInt(128);
            int i17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            cactusGen.generate(currentWorld, randomGenerator, l9, i14, i17);
        }

        if (ODNBXlite.MapFeatures>ODNBXlite.FEATURES_BETA181){
            if (generateLakes)
            {
                for (int i5 = 0; i5 < 50; i5++)
                {
                    int i10 = chunk_X + randomGenerator.nextInt(16) + 8;
                    int j14 = randomGenerator.nextInt(randomGenerator.nextInt(120) + 8);
                    int j17 = chunk_Z + randomGenerator.nextInt(16) + 8;
                    (new WorldGenLiquids(Block.waterMoving.blockID)).generate(currentWorld, randomGenerator, i10, j14, j17);
                }

                for (int j5 = 0; j5 < 20; j5++)
                {
                    int j10 = chunk_X + randomGenerator.nextInt(16) + 8;
                    int k14 = randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(112) + 8) + 8);
                    int k17 = chunk_Z + randomGenerator.nextInt(16) + 8;
                    (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(currentWorld, randomGenerator, j10, k14, k17);
                }
            }
        }else{
            for(int j5 = 0; j5 < 20; j5++)
            {
                int j10 = chunk_X + randomGenerator.nextInt(16) + 8;
                int k14 = randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(112) + 8) + 8);
                int k17 = chunk_Z + randomGenerator.nextInt(16) + 8;
                (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(currentWorld, randomGenerator, j10, k14, k17);
            }
        }
    }
}
