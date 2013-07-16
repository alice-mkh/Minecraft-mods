package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ComponentMineshaftCorridorOld extends StructureComponent
{
    private final boolean hasRails;
    private final boolean hasSpiders;
    private boolean spawnerPlaced;

    /**
     * A count of the different sections of this mine. The space between ceiling supports.
     */
    private int sectionCount;

    public ComponentMineshaftCorridorOld(int par1, Random par2Random, StructureBoundingBox par3StructureBoundingBox, int par4)
    {
        super(par1);
        coordBaseMode = par4;
        boundingBox = par3StructureBoundingBox;
        hasRails = par2Random.nextInt(3) == 0;
        hasSpiders = !hasRails && par2Random.nextInt(23) == 0;

        if (coordBaseMode == 2 || coordBaseMode == 0)
        {
            sectionCount = par3StructureBoundingBox.getZSize() / 5;
        }
        else
        {
            sectionCount = par3StructureBoundingBox.getXSize() / 5;
        }
    }

    public static StructureBoundingBox findValidPlacement(List par0List, Random par1Random, int par2, int par3, int par4, int par5)
    {
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(par2, par3, par4, par2, par3 + 2, par4);
        int i = par1Random.nextInt(3) + 2;

        do
        {
            if (i <= 0)
            {
                break;
            }

            int j = i * 5;

            switch (par5)
            {
                case 2:
                    structureboundingbox.maxX = par2 + 2;
                    structureboundingbox.minZ = par4 - (j - 1);
                    break;

                case 0:
                    structureboundingbox.maxX = par2 + 2;
                    structureboundingbox.maxZ = par4 + (j - 1);
                    break;

                case 1:
                    structureboundingbox.minX = par2 - (j - 1);
                    structureboundingbox.maxZ = par4 + 2;
                    break;

                case 3:
                    structureboundingbox.maxX = par2 + (j - 1);
                    structureboundingbox.maxZ = par4 + 2;
                    break;
            }

            if (StructureComponent.findIntersecting(par0List, structureboundingbox) == null)
            {
                break;
            }

            i--;
        }
        while (true);

        if (i > 0)
        {
            return structureboundingbox;
        }
        else
        {
            return null;
        }
    }

    /**
     * Initiates construction of the Structure Component picked, at the current Location of StructGen
     */
    @Override
    public void buildComponent(StructureComponent par1StructureComponent, List par2List, Random par3Random)
    {
        int i = getComponentType();
        int j = par3Random.nextInt(4);

        switch (coordBaseMode)
        {
            case 2:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ - 1, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX - 1, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ, 1, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.maxX + 1, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ, 3, i);
                }

                break;

            case 0:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.maxZ + 1, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX - 1, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.maxZ - 3, 1, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.maxX + 1, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.maxZ - 3, 3, i);
                }

                break;

            case 1:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX - 1, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ - 1, 2, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.maxZ + 1, 0, i);
                }

                break;

            case 3:
                if (j <= 1)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.maxX + 1, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ, coordBaseMode, i);
                }
                else if (j == 2)
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.maxX - 3, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.minZ - 1, 2, i);
                }
                else
                {
                    StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.maxX - 3, (boundingBox.minY - 1) + par3Random.nextInt(3), boundingBox.maxZ + 1, 0, i);
                }

                break;
        }

        if (i < (ODNBXlite.mineshaftSomeValue() ?  8 : 10))
        {
            if (coordBaseMode == 2 || coordBaseMode == 0)
            {
                for (int k = boundingBox.minZ + 3; k + 3 <= boundingBox.maxZ; k += 5)
                {
                    int i1 = par3Random.nextInt(5);

                    if (i1 == 0)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.minX - 1, boundingBox.minY, k, 1, i + 1);
                    }
                    else if (i1 == 1)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, boundingBox.maxX + 1, boundingBox.minY, k, 3, i + 1);
                    }
                }
            }
            else
            {
                for (int l = boundingBox.minX + 3; l + 3 <= boundingBox.maxX; l += 5)
                {
                    int j1 = par3Random.nextInt(5);

                    if (j1 == 0)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, l, boundingBox.minY, boundingBox.minZ - 1, 2, i + 1);
                        continue;
                    }

                    if (j1 == 1)
                    {
                        StructureMineshaftPieces.getNextComponent(par1StructureComponent, par2List, par3Random, l, boundingBox.minY, boundingBox.maxZ + 1, 0, i + 1);
                    }
                }
            }
        }
    }

    /**
     * second Part of Structure generating, this for example places Spiderwebs, Mob Spawners, it closes Mineshafts at
     * the end, it adds Fences...
     */
    @Override
    public boolean addComponentParts(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
    {
        if (isLiquidInStructureBoundingBox(par1World, par3StructureBoundingBox))
        {
            return false;
        }

        int i = sectionCount * 5 - 1;
        fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, 0, 2, 1, i, 0, 0, false);
        randomlyFillWithBlocks(par1World, par3StructureBoundingBox, par2Random, 0.8F, 0, 2, 0, 2, 2, i, 0, 0, false);

        if (hasSpiders)
        {
            randomlyFillWithBlocks(par1World, par3StructureBoundingBox, par2Random, 0.6F, 0, 0, 0, 2, 1, i, Block.web.blockID, 0, false);
        }

        for (int j = 0; j < sectionCount; j++)
        {
            int i1 = 2 + j * 5;
            if (ODNBXlite.mineshaftFences()){
                fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, i1, 0, 1, i1, Block.fence.blockID, 0, false);
                fillWithBlocks(par1World, par3StructureBoundingBox, 2, 0, i1, 2, 1, i1, Block.fence.blockID, 0, false);
            }else{
                fillWithBlocks(par1World, par3StructureBoundingBox, 0, 0, i1, 0, 1, i1, Block.planks.blockID, 0, false);
                fillWithBlocks(par1World, par3StructureBoundingBox, 2, 0, i1, 2, 1, i1, Block.planks.blockID, 0, false);
            }

            if (par2Random.nextInt(4) == 0)
            {
                fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, i1, 0, 2, i1, Block.planks.blockID, 0, false);
                fillWithBlocks(par1World, par3StructureBoundingBox, 2, 2, i1, 2, 2, i1, Block.planks.blockID, 0, false);
            }
            else
            {
                fillWithBlocks(par1World, par3StructureBoundingBox, 0, 2, i1, 2, 2, i1, Block.planks.blockID, 0, false);
            }

            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, i1 - 1, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, i1 - 1, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 0, 2, i1 + 1, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.1F, 2, 2, i1 + 1, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, i1 - 2, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, i1 - 2, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 0, 2, i1 + 2, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 2, 2, i1 + 2, Block.web.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, i1 - 1, Block.torchWood.blockID, 0);
            randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.05F, 1, 2, i1 + 1, Block.torchWood.blockID, 0);

            if (par2Random.nextInt(100) == 0)
            {
                generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 2, 0, i1 - 1, WeightedRandomChestContent.func_92080_a(StructureMineshaftPieces.func_78816_a(), new WeightedRandomChestContent[]
                        {
                            Item.enchantedBook.func_92114_b(par2Random)
                        }), 3 + par2Random.nextInt(4));
            }

            if (par2Random.nextInt(100) == 0)
            {
                generateStructureChestContents(par1World, par3StructureBoundingBox, par2Random, 0, 0, i1 + 1, WeightedRandomChestContent.func_92080_a(StructureMineshaftPieces.func_78816_a(), new WeightedRandomChestContent[]
                        {
                            Item.enchantedBook.func_92114_b(par2Random)
                        }), 3 + par2Random.nextInt(4));
            }

            if (!hasSpiders || spawnerPlaced)
            {
                continue;
            }

            int l1 = getYWithOffset(0);
            int j2 = (i1 - 1) + par2Random.nextInt(3);
            int k2 = getXWithOffset(1, j2);
            j2 = getZWithOffset(1, j2);

            if (!par3StructureBoundingBox.isVecInside(k2, l1, j2))
            {
                continue;
            }

            spawnerPlaced = true;
            par1World.setBlock(k2, l1, j2, Block.mobSpawner.blockID, 0, 2);
            TileEntityMobSpawner tileentitymobspawner = (TileEntityMobSpawner)par1World.getBlockTileEntity(k2, l1, j2);

            if (tileentitymobspawner != null)
            {
                tileentitymobspawner.getSpawnerLogic().setMobID("CaveSpider");
            }
        }

        if (ODNBXlite.mineshaftFloor()){
            for (int k = 0; k <= 2; k++)
            {
                for (int j1 = 0; j1 <= i; j1++)
                {
                    int i2 = getBlockIdAtCurrentPosition(par1World, k, -1, j1, par3StructureBoundingBox);

                    if (i2 == 0)
                    {
                        placeBlockAtCurrentPosition(par1World, Block.planks.blockID, 0, k, -1, j1, par3StructureBoundingBox);
                    }
                }
            }
        }

        if (hasRails)
        {
            for (int l = 0; l <= i; l++)
            {
                int k1 = getBlockIdAtCurrentPosition(par1World, 1, -1, l, par3StructureBoundingBox);

                if (k1 > 0 && Block.opaqueCubeLookup[k1])
                {
                    randomlyPlaceBlock(par1World, par3StructureBoundingBox, par2Random, 0.7F, 1, 0, l, Block.rail.blockID, getMetadataWithOffset(Block.rail.blockID, 0));
                }
            }
        }

        return true;
    }

    /**
     * Used to generate chests with items in it. ex: Temple Chests, Village Blacksmith Chests, Mineshaft Chests.
     */
    @Override
    protected boolean generateStructureChestContents(World par1World, StructureBoundingBox par2StructureBoundingBox, Random par3Random, int par4, int par5, int par6, WeightedRandomChestContent par7ArrayOfWeightedRandomChestContent[], int par8)
    {
        if (ODNBXlite.noCartsInMineshafts()){
            return super.generateStructureChestContents(par1World, par2StructureBoundingBox, par3Random, par4, par5, par6, par7ArrayOfWeightedRandomChestContent, par8);
        }
        int i = getXWithOffset(par4, par6);
        int j = getYWithOffset(par5);
        int k = getZWithOffset(par4, par6);

        if (par2StructureBoundingBox.isVecInside(i, j, k) && par1World.getBlockId(i, j, k) == 0)
        {
            par1World.setBlock(i, j, k, Block.rail.blockID, getMetadataWithOffset(Block.rail.blockID, par3Random.nextBoolean() ? 1 : 0), 2);
            EntityMinecartChest entityminecartchest = new EntityMinecartChest(par1World, (float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F);
            WeightedRandomChestContent.generateChestContents(par3Random, par7ArrayOfWeightedRandomChestContent, entityminecartchest, par8);
            par1World.spawnEntityInWorld(entityminecartchest);
            return true;
        }
        else
        {
            return false;
        }
    }
}
