package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

public class ComponentScatteredFeatureDesertPyramid extends ComponentScatteredFeature
{
    private boolean field_56306_e[];
    private static final WeightedRandomChestContent field_56305_f[];

    public ComponentScatteredFeatureDesertPyramid(Random par1Random, int par2, int par3)
    {
        super(par1Random, par2, 64, par3, 21, 15, 21);
        field_56306_e = new boolean[4];
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        fillWithBlocks(par1World, par3StructureBoundingBox, 0, -4, 0, field_56297_a - 1, 0, field_56296_c - 1, Block.sandStone.blockID, Block.sandStone.blockID, false);

        for (int i = 1; i <= 9; i++)
        {
            fillWithBlocks(par1World, par3StructureBoundingBox, i, i, i, field_56297_a - 1 - i, i, field_56296_c - 1 - i, Block.sandStone.blockID, Block.sandStone.blockID, false);
            fillWithBlocks(par1World, par3StructureBoundingBox, i + 1, i, i + 1, field_56297_a - 2 - i, i, field_56296_c - 2 - i, 0, 0, false);
        }

        for (int j = 0; j < field_56297_a; j++)
        {
            for (int l = 0; l < field_56296_c; l++)
            {
                fillCurrentPositionBlocksDownwards(par1World, Block.sandStone.blockID, 0, j, -5, l, par3StructureBoundingBox);
            }
        }

        int k = 0;//getMetadataWithOffset(Block.field_56339_bQ.blockID, 3);
        int i1 = 0;//getMetadataWithOffset(Block.field_56339_bQ.blockID, 2);
        int j1 = 0;//getMetadataWithOffset(Block.field_56339_bQ.blockID, 0);
        int k1 = 0;//getMetadataWithOffset(Block.field_56339_bQ.blockID, 1);
        int l1 = 1;
        byte byte0 = 11;
        fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 9, 4, Block.sandStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 10, 1, 3, 10, 3, Block.sandStone.blockID, Block.sandStone.blockID, false);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k, 2, 10, 0, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, i1, 2, 10, 4, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, j1, 0, 10, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k1, 4, 10, 2, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 5, 0, 0, field_56297_a - 1, 9, 4, Block.sandStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 4, 10, 1, field_56297_a - 2, 10, 3, Block.sandStone.blockID, Block.sandStone.blockID, false);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k, field_56297_a - 3, 10, 0, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, i1, field_56297_a - 3, 10, 4, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, j1, field_56297_a - 5, 10, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k1, field_56297_a - 1, 10, 2, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, 8, 0, 0, 12, 4, 4, Block.sandStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 9, 1, 0, 11, 3, 4, 0, 0, false);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 9, 1, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 9, 2, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 9, 3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 10, 3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 11, 3, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 11, 2, 1, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 11, 1, 1, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 1, 8, 3, 3, Block.sandStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 4, 1, 2, 8, 2, 2, 0, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 12, 1, 1, 16, 3, 3, Block.sandStone.blockID, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 12, 1, 2, 16, 2, 2, 0, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 5, 4, 5, field_56297_a - 6, 4, field_56296_c - 6, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 9, 4, 9, 11, 4, 11, 0, 0, false);
        func_56290_a(par1World, par3StructureBoundingBox, 8, 1, 8, 8, 3, 8, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        func_56290_a(par1World, par3StructureBoundingBox, 12, 1, 8, 12, 3, 8, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        func_56290_a(par1World, par3StructureBoundingBox, 8, 1, 12, 8, 3, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        func_56290_a(par1World, par3StructureBoundingBox, 12, 1, 12, 12, 3, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 5, 4, 4, 11, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 5, 1, 5, field_56297_a - 2, 4, 11, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 6, 7, 9, 6, 7, 11, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 7, 7, 9, field_56297_a - 7, 7, 11, Block.sandStone.blockID, Block.sandStone.blockID, false);
        func_56290_a(par1World, par3StructureBoundingBox, 5, 5, 9, 5, 7, 11, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        func_56290_a(par1World, par3StructureBoundingBox, field_56297_a - 6, 5, 9, field_56297_a - 6, 7, 11, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        placeBlockAtCurrentPosition(par1World, 0, 0, 5, 5, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 5, 6, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 6, 6, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, field_56297_a - 6, 5, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, field_56297_a - 6, 6, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, field_56297_a - 7, 6, 10, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, 2, 4, 4, 2, 6, 4, 0, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 3, 4, 4, field_56297_a - 3, 6, 4, 0, 0, false);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k, 2, 4, 5, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k, 2, 3, 4, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k, field_56297_a - 3, 4, 5, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k, field_56297_a - 3, 3, 4, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 1, 3, 2, 2, 3, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 3, 1, 3, field_56297_a - 2, 2, 3, Block.sandStone.blockID, Block.sandStone.blockID, false);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, 0, 1, 1, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, 0, field_56297_a - 2, 1, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_55133_ak.blockID, 1, 1, 2, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_55133_ak.blockID, 1, field_56297_a - 2, 2, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, k1, 2, 1, 2, par3StructureBoundingBox);
//         placeBlockAtCurrentPosition(par1World, Block.field_56339_bQ.blockID, j1, field_56297_a - 3, 1, 2, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, 4, 3, 5, 4, 3, 18, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 5, 3, 5, field_56297_a - 5, 3, 17, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 3, 1, 5, 4, 2, 16, 0, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, field_56297_a - 6, 1, 5, field_56297_a - 5, 2, 16, 0, 0, false);

        for (int i2 = 5; i2 <= 17; i2 += 2)
        {
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 4, 1, i2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, 4, 2, i2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, field_56297_a - 5, 1, i2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, field_56297_a - 5, 2, i2, par3StructureBoundingBox);
        }

        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 10, 0, 7, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 10, 0, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 9, 0, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 11, 0, 9, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 8, 0, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 12, 0, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 7, 0, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 13, 0, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 9, 0, 11, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 11, 0, 11, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 10, 0, 12, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 10, 0, 13, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, byte0, 10, 0, 10, par3StructureBoundingBox);

        for (int j2 = 0; j2 <= field_56297_a - 1; j2 += field_56297_a - 1)
        {
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 2, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 2, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 2, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 3, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 3, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 3, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 4, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, j2, 4, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 4, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 5, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 5, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 5, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 6, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, j2, 6, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 6, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 7, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 7, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, j2, 7, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 8, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 8, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, j2, 8, 3, par3StructureBoundingBox);
        }

        for (int k2 = 2; k2 <= field_56297_a - 3; k2 += field_56297_a - 3 - 2)
        {
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 - 1, 2, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2, 2, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 + 1, 2, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 - 1, 3, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2, 3, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 + 1, 3, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2 - 1, 4, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, k2, 4, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2 + 1, 4, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 - 1, 5, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2, 5, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 + 1, 5, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2 - 1, 6, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, k2, 6, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2 + 1, 6, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2 - 1, 7, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2, 7, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, k2 + 1, 7, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 - 1, 8, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2, 8, 0, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, k2 + 1, 8, 0, par3StructureBoundingBox);
        }

        func_56290_a(par1World, par3StructureBoundingBox, 8, 4, 0, 12, 6, 0, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        placeBlockAtCurrentPosition(par1World, 0, 0, 8, 6, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 12, 6, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 9, 5, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, 10, 5, 0, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.cloth.blockID, l1, 11, 5, 0, par3StructureBoundingBox);
        func_56290_a(par1World, par3StructureBoundingBox, 8, -14, 8, 12, -11, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        func_56290_a(par1World, par3StructureBoundingBox, 8, -10, 8, 12, -10, 12, Block.sandStone.blockID, 1, Block.sandStone.blockID, 1, false);
        func_56290_a(par1World, par3StructureBoundingBox, 8, -9, 8, 12, -9, 12, Block.sandStone.blockID, 2, Block.sandStone.blockID, 2, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 8, -8, 8, 12, -1, 12, Block.sandStone.blockID, Block.sandStone.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 9, -11, 9, 11, -1, 11, 0, 0, false);
        placeBlockAtCurrentPosition(par1World, Block.pressurePlateStone.blockID, 0, 10, -11, 10, par3StructureBoundingBox);
        fillWithBlocks(par1World, par3StructureBoundingBox, 9, -13, 9, 11, -13, 11, Block.tnt.blockID, 0, false);
        placeBlockAtCurrentPosition(par1World, 0, 0, 8, -11, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 8, -10, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, 7, -10, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 7, -11, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 12, -11, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 12, -10, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, 13, -10, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 13, -11, 10, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 10, -11, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 10, -10, 8, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, 10, -10, 7, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 10, -11, 7, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 10, -11, 12, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, 0, 0, 10, -10, 12, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 1, 10, -10, 13, par3StructureBoundingBox);
        placeBlockAtCurrentPosition(par1World, Block.sandStone.blockID, 2, 10, -11, 13, par3StructureBoundingBox);

        for (int l2 = 0; l2 < 4; l2++)
        {
            if (!field_56306_e[l2])
            {
                int i3 = Direction.offsetX[l2] * 2;
                int j3 = Direction.offsetZ[l2] * 2;
                field_56306_e[l2] = func_56289_a(par1World, par3StructureBoundingBox, par2Random, 10 + i3, -11, 10 + j3, field_56305_f, 2 + par2Random.nextInt(5));
            }
        }

        return true;
    }

    protected boolean func_56289_a(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, WeightedRandomChestContent par7ArrayOfWeightedRandomChestContent[], int par8)
    {
        int i = getXWithOffset(par4, par6);
        int j = getYWithOffset(par5);
        int k = getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i, j, k) && par1World.getBlockId(i, j, k) != Block.chest.blockID)
        {
            par1World.setBlockWithNotify(i, j, k, Block.chest.blockID);
            TileEntityChest tileentitychest = (TileEntityChest)par1World.getBlockTileEntity(i, j, k);

            if (tileentitychest != null)
            {
                WeightedRandomChestContent.func_55216_a(par3Random, par7ArrayOfWeightedRandomChestContent, tileentitychest, par8);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    protected void func_56290_a(World par1World, StructureBoundingBox par2StructureBoundingBox, int par3, int par4, int par5, int par6, int par7, int par8, int par9, int par10, int par11, int par12, boolean par13)
    {
        for (int i = par4; i <= par7; i++)
        {
            for (int j = par3; j <= par6; j++)
            {
                for (int k = par5; k <= par8; k++)
                {
                    if (par13 && getBlockIdAtCurrentPosition(par1World, j, i, k, par2StructureBoundingBox) == 0)
                    {
                        continue;
                    }

                    if (i == par4 || i == par7 || j == par3 || j == par6 || k == par5 || k == par8)
                    {
                        placeBlockAtCurrentPosition(par1World, par9, par10, j, i, k, par2StructureBoundingBox);
                    }
                    else
                    {
                        placeBlockAtCurrentPosition(par1World, par11, par12, j, i, k, par2StructureBoundingBox);
                    }
                }
            }
        }
    }

    static
    {
        field_56305_f = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.diamond.shiftedIndex, 0, 1, 3, 3), new WeightedRandomChestContent(Item.ingotIron.shiftedIndex, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.shiftedIndex, 0, 2, 7, 15), new WeightedRandomChestContent(132 + 256, 0, 1, 3, 2), new WeightedRandomChestContent(Item.bone.shiftedIndex, 0, 4, 6, 20), new WeightedRandomChestContent(Item.rottenFlesh.shiftedIndex, 0, 3, 7, 16)
                });
    }
}
