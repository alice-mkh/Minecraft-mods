package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class StructureMineshaftPieces
{
    private static final WeightedRandomChestContent mineshaftChestContents[];
    private static final WeightedRandomChestContent mineshaftChestContents_old[];
    private static final WeightedRandomChestContent mineshaftChestContents_older[];

    public static void func_143048_a()
    {
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentMineshaftCorridor.class, "MSCorridor");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentMineshaftCorridorOld.class, "MSCorridorOld");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentMineshaftCross.class, "MSCrossing");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentMineshaftRoom.class, "MSRoom");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentMineshaftStairs.class, "MSStairs");
    }

    private static StructureComponent getRandomComponent(List par0List, Random par1Random, int par2, int par3, int par4, int par5, int par6)
    {
        int i = par1Random.nextInt(100);

        if (i >= 80)
        {
            StructureBoundingBox structureboundingbox = ComponentMineshaftCross.findValidPlacement(par0List, par1Random, par2, par3, par4, par5);

            if (structureboundingbox != null)
            {
                return new ComponentMineshaftCross(par6, par1Random, structureboundingbox, par5);
            }
        }
        else if (i >= 70)
        {
            StructureBoundingBox structureboundingbox1 = ComponentMineshaftStairs.findValidPlacement(par0List, par1Random, par2, par3, par4, par5);

            if (structureboundingbox1 != null)
            {
                return new ComponentMineshaftStairs(par6, par1Random, structureboundingbox1, par5);
            }
        }
        else
        {
            StructureBoundingBox structureboundingbox2 = ComponentMineshaftCorridorOld.findValidPlacement(par0List, par1Random, par2, par3, par4, par5);

            if (structureboundingbox2 != null)
            {
                return new ComponentMineshaftCorridorOld(par6, par1Random, structureboundingbox2, par5);
            }
        }

        return null;
    }

    private static StructureComponent getNextMineShaftComponent(StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > (ODNBXlite.mineshaftSomeValue() ?  8 : 10))
        {
            return null;
        }

        if (Math.abs(par3 - par0StructureComponent.getBoundingBox().minX) > 80 || Math.abs(par5 - par0StructureComponent.getBoundingBox().minZ) > 80)
        {
            return null;
        }

        StructureComponent structurecomponent = getRandomComponent(par1List, par2Random, par3, par4, par5, par6, par7 + 1);

        if (structurecomponent != null)
        {
            par1List.add(structurecomponent);
            structurecomponent.buildComponent(par0StructureComponent, par1List, par2Random);
        }

        return structurecomponent;
    }

    static StructureComponent getNextComponent(StructureComponent par0StructureComponent, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextMineShaftComponent(par0StructureComponent, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static WeightedRandomChestContent[] func_78816_a()
    {
        if (ODNBXlite.Generator < ODNBXlite.GEN_NEWBIOMES || ODNBXlite.MapFeatures > ODNBXlite.FEATURES_15){
            return mineshaftChestContents;
        }
        return ODNBXlite.mineshaftSomeValue() ? mineshaftChestContents_old : mineshaftChestContents_older;
    }

    static
    {
        mineshaftChestContents = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5), new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5), new WeightedRandomChestContent(Item.dyePowder.itemID, 4, 4, 9, 5), new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 2, 3), new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10), new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1), new WeightedRandomChestContent(Block.rail.blockID, 0, 4, 8, 1), new WeightedRandomChestContent(Item.melonSeeds.itemID, 0, 2, 4, 10),
                    new WeightedRandomChestContent(Item.pumpkinSeeds.itemID, 0, 2, 4, 10), new WeightedRandomChestContent(Item.saddle.itemID, 0, 1, 1, 3), new WeightedRandomChestContent(Item.horseArmorIron.itemID, 0, 1, 1, 1)
                });
        mineshaftChestContents_old = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5), new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5), new WeightedRandomChestContent(Item.dyePowder.itemID, 4, 4, 9, 5), new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 2, 3), new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10), new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1), new WeightedRandomChestContent(Block.rail.blockID, 0, 4, 8, 1), new WeightedRandomChestContent(Item.melonSeeds.itemID, 0, 2, 4, 10),
                    new WeightedRandomChestContent(Item.pumpkinSeeds.itemID, 0, 2, 4, 10)
                });
        mineshaftChestContents_older = (new WeightedRandomChestContent[]
                {
                    new WeightedRandomChestContent(Item.ingotIron.itemID, 0, 1, 5, 10), new WeightedRandomChestContent(Item.ingotGold.itemID, 0, 1, 3, 5), new WeightedRandomChestContent(Item.redstone.itemID, 0, 4, 9, 5), new WeightedRandomChestContent(Item.dyePowder.itemID, 4, 4, 9, 5), new WeightedRandomChestContent(Item.diamond.itemID, 0, 1, 2, 3), new WeightedRandomChestContent(Item.coal.itemID, 0, 3, 8, 10), new WeightedRandomChestContent(Item.bread.itemID, 0, 1, 3, 15), new WeightedRandomChestContent(Item.pickaxeIron.itemID, 0, 1, 1, 1), new WeightedRandomChestContent(Block.rail.blockID, 0, 4, 8, 1), new WeightedRandomChestContent(Item.melonSeeds.itemID, 0, 2, 4, 10)
                });
    }
}
