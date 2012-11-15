package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentVillageField2 extends ComponentVillage
{
    private int averageGroundLevel;
    private int field_82675_b;
    private int field_82676_c;

    public ComponentVillageField2(ComponentVillageStartPiece par1ComponentVillageStartPiece, int par2, Random par3Random, StructureBoundingBox par4StructureBoundingBox, int par5)
    {
        super(par1ComponentVillageStartPiece, par2);
        averageGroundLevel = -1;
        coordBaseMode = par5;
        boundingBox = par4StructureBoundingBox;
        field_82675_b = pickRandomCrop(par3Random);
        field_82676_c = pickRandomCrop(par3Random);
    }

    /**
     * Returns a crop type to be planted on this field.
     */
    private int pickRandomCrop(Random par1Random)
    {
        if (ODNBXlite.noNewCrops()){
            return Block.crops.blockID;
        }
        switch (par1Random.nextInt(5))
        {
            default:
                return Block.crops.blockID;
            case 0:
                return Block.carrot.blockID;
            case 1:
                return Block.potatoe.blockID;
        }
    }

    public static ComponentVillageField2 func_74902_a(ComponentVillageStartPiece par0ComponentVillageStartPiece, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par3, par4, par5, 0, 0, 0, 7, 4, 9, par6);

        if (!canVillageGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(par1List, structureboundingbox) != null)
        {
            return null;
        }
        else
        {
            return new ComponentVillageField2(par0ComponentVillageStartPiece, par7, par2Random, structureboundingbox, par6);
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

        fillWithBlocks(par1World, par3StructureBoundingBox, 0, 1, 0, 6, 4, 8, 0, 0, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 1, 2, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 4, 0, 1, 5, 0, 7, Block.tilledField.blockID, Block.tilledField.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 0, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 6, 0, 0, 6, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 0, 5, 0, 0, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 1, 0, 8, 5, 0, 8, Block.wood.blockID, Block.wood.blockID, false);
        fillWithBlocks(par1World, par3StructureBoundingBox, 3, 0, 1, 3, 0, 7, Block.waterMoving.blockID, Block.waterMoving.blockID, false);

        for (int i = 1; i <= 7; i++)
        {
            placeBlockAtCurrentPosition(par1World, field_82675_b, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 1, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82675_b, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 2, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82676_c, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 4, 1, i, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, field_82676_c, MathHelper.getRandomIntegerInRange(par2Random, 2, 7), 5, 1, i, par3StructureBoundingBox);
        }

        for (int j = 0; j < 9; j++)
        {
            for (int k = 0; k < 7; k++)
            {
                clearCurrentPositionBlocksUpwards(par1World, k, 4, j, par3StructureBoundingBox);
                fillCurrentPositionBlocksDownwards(par1World, Block.dirt.blockID, 0, k, -1, j, par3StructureBoundingBox);
            }
        }

        return true;
    }
}
