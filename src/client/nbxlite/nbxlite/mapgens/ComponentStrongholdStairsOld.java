package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentStrongholdStairsOld extends ComponentStronghold
{
    private final boolean field_75024_a;
    private final EnumDoor doorType;

    public ComponentStrongholdStairsOld(int par1, Random par2Random, int par3, int par4)
    {
        super(par1);
        field_75024_a = true;
        coordBaseMode = par2Random.nextInt(4);
        doorType = EnumDoor.OPENING;

        switch (coordBaseMode)
        {
            case 0:
            case 2:
                boundingBox = new StructureBoundingBox(par3, 64, par4, (par3 + 5) - 1, 74, (par4 + 5) - 1);
                break;

            default:
                boundingBox = new StructureBoundingBox(par3, 64, par4, (par3 + 5) - 1, 74, (par4 + 5) - 1);
                break;
        }
    }

    public ComponentStrongholdStairsOld(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        field_75024_a = false;
        coordBaseMode = par4;
        doorType = getRandomDoor(par2Random);
        boundingBox = par3StructureBoundingBox;
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        if (field_75024_a && !ODNBXlite.oldStrongholds())
        {
            StructureStrongholdPieces.setComponentType(net.minecraft.src.ComponentStrongholdCrossing.class);
        }

        getNextComponentNormal((ComponentStrongholdStairs2)par1StructureComponent, par2List, par3Random, 1, 1);
    }

    /**
     * performs some checks, then gives out a fresh Stairs component
     */
    public static ComponentStrongholdStairsOld getStrongholdStairsComponent(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(par2, par3, par4, -1, -7, 0, 5, 11, 5, par5);

        if (!canStrongholdGoDeeper(structureboundingbox) || StructureComponent.findIntersecting(par0List, structureboundingbox) != null)
        {
            return null;
        }
        else
        {
            return new ComponentStrongholdStairsOld(par6, par1Random, structureboundingbox, par5);
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (isLiquidInStructureBoundingBox(par1World, par3StructureBoundingBox))
        {
            return false;
        }
        else
        {
            fillWithRandomizedBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 4, 10, 4, true, par2Random, StructureStrongholdPieces.getStrongholdStones());
            placeDoor(par1World, par2Random, par3StructureBoundingBox, doorType, 1, 7, 0);
            placeDoor(par1World, par2Random, par3StructureBoundingBox, EnumDoor.OPENING, 1, 1, 4);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 2, 6, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 1, 5, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 1, 6, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 1, 5, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 1, 4, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 1, 5, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 2, 4, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 3, 3, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 3, 4, 3, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 3, 3, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 3, 2, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 3, 3, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 2, 2, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 1, 1, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 1, 2, 1, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneBrick.blockID, 0, 1, 1, 2, par3StructureBoundingBox);
            placeBlockAtCurrentPosition(par1World, Block.stoneSingleSlab.blockID, 0, 1, 1, 3, par3StructureBoundingBox);
            return true;
        }
    }
}
