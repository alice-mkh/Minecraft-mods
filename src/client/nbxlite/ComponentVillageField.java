package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageField extends ComponentVillage
{
    private int averageGroundLevel;
    private int field_82679_b;
    private int field_82680_c;
    private int field_82678_d;
    private int field_82681_h;

    public ComponentVillageField(ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        averageGroundLevel = -1;
        coordBaseMode = par5;
        boundingBox = par4StructureBoundingBox;
        field_82679_b = func_82677_a(par3Random);
        field_82680_c = func_82677_a(par3Random);
        field_82678_d = func_82677_a(par3Random);
        field_82681_h = func_82677_a(par3Random);
    }

    private int func_82677_a(Random par1Random)
    {
        if (ODNBXlite.noNewCrops()){
            return Block.crops.blockID;
        }
        switch (par1Random.nextInt(5))
        {
            default:
                return Block.crops.blockID;
            case 0:
                return Block.field_82513_cg.blockID;
            case 1:
                return Block.field_82514_ch.blockID;
        }
    }

    public static ComponentVillageField func_74900_a(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 13, 4, 9, par6);

        if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(par1List, structureboundingbox) != null)
        {
            return null;
        }
        else
        {
            return new ComponentVillageField(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6);
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (averageGroundLevel < 0)
        {
            averageGroundLevel = getAverageGroundLevel(par1World, par3StructureBoundingBox);

            if (averageGroundLevel < 0)
            {
                return true;
            }

            boundingBox.offset(0, ((averageGroundLevel - boundingBox.maxY) + 4) - 1, 0);
        }

        fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 12, 4, 8, 0, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 7, 0, 1, 8, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 10, 0, 1, 11, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 12, 0, 0, 12, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 11, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 11, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 9, 0, 1, 9, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);

        for (int i = 1; i <= 7; i++)
        {
            placeBlockAtCurrentPosition(par1World, field_82679_b, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 1, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82679_b, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 2, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82680_c, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 4, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82680_c, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 5, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82678_d, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 7, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82678_d, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 8, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82681_h, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 10, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82681_h, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 11, 1, i, par3StructureBoundingBox);
        }

        for (int j = 0; j < 9; j++)
        {
            for (int k = 0; k < 13; k++)
            {
                clearCurrentPositionBlocksUpwards(par1World, k, 4, j, par3StructureBoundingBox);
                fillCurrentPositionBlocksDownwards(par1World, Block.dirt.blockID, 0, k, -1, j, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
