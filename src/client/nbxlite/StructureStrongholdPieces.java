package net.minecraft.src;

import java.util.*;

public class StructureStrongholdPieces
{
    private static final StructureStrongholdPieceWeight pieceWeightArray[];
    private static final StructureStrongholdPieceWeight pieceWeightArrayOld[];
    private static List structurePieceList;
    private static Class strongComponentType;
    static int totalWeight;
    private static final StructureStrongholdStones strongholdStones = new StructureStrongholdStones(null);

    public static void func_143046_a()
    {
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdChestCorridor.class, "SHCC");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdCorridor.class, "SHFC");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdCrossing.class, "SH5C");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdCrossingOld.class, "SH5COld");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdLeftTurn.class, "SHLT");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdLibrary.class, "SHLi");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdPortalRoom.class, "SHPR");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdPrison.class, "SHPH");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdRightTurn.class, "SHRT");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdRoomCrossing.class, "SHRC");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdStairs.class, "SHSD");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdStairs2.class, "SHStart");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdStraight.class, "SHS");
        MapGenStructureIO.func_143031_a(net.minecraft.src.ComponentStrongholdStairsStraight.class, "SHSSD");
    }

    /**
     * sets up Arrays with the Structure pieces and their weights
     */
    public static void prepareStructurePieces()
    {
        structurePieceList = new ArrayList();
        StructureStrongholdPieceWeight astructurestrongholdpieceweight[] = ODNBXlite.oldStrongholds() ? pieceWeightArrayOld : pieceWeightArray;
        int i = astructurestrongholdpieceweight.length;

        for (int j = 0; j < i; j++)
        {
            StructureStrongholdPieceWeight structurestrongholdpieceweight = astructurestrongholdpieceweight[j];
            structurestrongholdpieceweight.instancesSpawned = 0;
            structurePieceList.add(structurestrongholdpieceweight);
        }

        strongComponentType = null;
    }

    private static boolean canAddStructurePieces()
    {
        boolean flag = false;
        totalWeight = 0;

        for (Iterator iterator = structurePieceList.iterator(); iterator.hasNext();)
        {
            StructureStrongholdPieceWeight structurestrongholdpieceweight = (StructureStrongholdPieceWeight)iterator.next();

            if (structurestrongholdpieceweight.instancesLimit > 0 && structurestrongholdpieceweight.instancesSpawned < structurestrongholdpieceweight.instancesLimit)
            {
                flag = true;
            }

            totalWeight += structurestrongholdpieceweight.pieceWeight;
        }

        return flag;
    }

    /**
     * translates the PieceWeight class to the Component class
     */
    private static ComponentStronghold getStrongholdComponentFromWeightedPiece(Class par0Class, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        Object obj = null;

        if (par0Class == (net.minecraft.src.ComponentStrongholdStraight.class))
        {
            obj = ComponentStrongholdStraight.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdPrison.class))
        {
            obj = ComponentStrongholdPrison.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdLeftTurn.class))
        {
            obj = ComponentStrongholdLeftTurn.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdRightTurn.class))
        {
            obj = ComponentStrongholdRightTurn.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdRoomCrossing.class))
        {
            obj = ComponentStrongholdRoomCrossing.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdStairsStraight.class))
        {
            obj = ComponentStrongholdStairsStraight.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdStairs.class))
        {
            obj = ComponentStrongholdStairs.getStrongholdStairsComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdStairsOld.class))
        {
            obj = ComponentStrongholdStairsOld.getStrongholdStairsComponent(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdCrossing.class))
        {
            obj = ComponentStrongholdCrossing.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdCrossingOld.class))
        {
            obj = ComponentStrongholdCrossingOld.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdChestCorridor.class))
        {
            obj = ComponentStrongholdChestCorridor.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdLibrary.class))
        {
            obj = ComponentStrongholdLibrary.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }
        else if (par0Class == (net.minecraft.src.ComponentStrongholdPortalRoom.class))
        {
            obj = ComponentStrongholdPortalRoom.findValidPlacement(par1List, par2Random, par3, par4, par5, par6, par7);
        }

        return ((ComponentStronghold)(obj));
    }

    private static ComponentStronghold getNextComponent(ComponentStrongholdStairs2 par0ComponentStrongholdStairs2, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (!canAddStructurePieces())
        {
            return null;
        }
        else
        {
            if (strongComponentType != null && !ODNBXlite.oldStrongholds())
            {
                ComponentStronghold var8 = getStrongholdComponentFromWeightedPiece(strongComponentType, par1List, par2Random, par3, par4, par5, par6, par7);
                strongComponentType = null;

                if (var8 != null)
                {
                    return var8;
                }
            }

            int var13 = 0;

            while (var13 < 5)
            {
                ++var13;
                int var9 = par2Random.nextInt(totalWeight);
                Iterator var10 = structurePieceList.iterator();

                while (var10.hasNext())
                {
                    StructureStrongholdPieceWeight var11 = (StructureStrongholdPieceWeight)var10.next();
                    var9 -= var11.pieceWeight;

                    if (var9 < 0)
                    {
                        if (!var11.canSpawnMoreStructuresOfType(par7) || var11 == par0ComponentStrongholdStairs2.strongholdPieceWeight)
                        {
                            break;
                        }

                        ComponentStronghold var12 = null;
                        if (ODNBXlite.oldStrongholds()){
                            var12 = getStrongholdComponentFromWeightedPiece_old(var11, par1List, par2Random, par3, par4, par5, par6, par7);
                        }else{
                            var12 = getStrongholdComponentFromWeightedPiece(var11.pieceClass, par1List, par2Random, par3, par4, par5, par6, par7);
                        }

                        if (var12 != null)
                        {
                            ++var11.instancesSpawned;
                            par0ComponentStrongholdStairs2.strongholdPieceWeight = var11;

                            if (!var11.canSpawnMoreStructures())
                            {
                                structurePieceList.remove(var11);
                            }

                            return var12;
                        }
                    }
                }
            }

            StructureBoundingBox var14 = ComponentStrongholdCorridor.func_74992_a(par1List, par2Random, par3, par4, par5, par6);

            if (var14 != null && var14.minY > 1)
            {
                return new ComponentStrongholdCorridor(par7, par2Random, var14, par6);
            }
            else
            {
                return null;
            }
        }
    }

    private static ComponentStronghold getStrongholdComponentFromWeightedPiece_old(StructureStrongholdPieceWeight structurestrongholdpieceweight, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getStrongholdComponentFromWeightedPiece(structurestrongholdpieceweight.pieceClass, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    private static StructureComponent getNextValidComponent(ComponentStrongholdStairs2 par0ComponentStrongholdStairs2, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        if (par7 > 50)
        {
            return null;
        }

        if (Math.abs(par3 - par0ComponentStrongholdStairs2.getBoundingBox().minX) > 112 || Math.abs(par5 - par0ComponentStrongholdStairs2.getBoundingBox().minZ) > 112)
        {
            return null;
        }

        ComponentStronghold componentstronghold = getNextComponent(par0ComponentStrongholdStairs2, par1List, par2Random, par3, par4, par5, par6, par7 + 1);

        if (componentstronghold != null)
        {
            par1List.add(componentstronghold);
            par0ComponentStrongholdStairs2.field_75026_c.add(componentstronghold);
        }

        return componentstronghold;
    }

    static StructureComponent getNextValidComponentAccess(ComponentStrongholdStairs2 par0ComponentStrongholdStairs2, List par1List, Random par2Random, int par3, int par4, int par5, int par6, int par7)
    {
        return getNextValidComponent(par0ComponentStrongholdStairs2, par1List, par2Random, par3, par4, par5, par6, par7);
    }

    static Class setComponentType(Class par0Class)
    {
        return strongComponentType = par0Class;
    }

    static StructureStrongholdStones getStrongholdStones()
    {
        return strongholdStones;
    }

    static
    {
        pieceWeightArray = (new StructureStrongholdPieceWeight[]
                {
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdStraight.class, 40, 0),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdPrison.class, 5, 5),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdLeftTurn.class, 20, 0),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdRightTurn.class, 20, 0),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdRoomCrossing.class, 10, 6),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdStairsStraight.class, 5, 5),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdStairs.class, 5, 5),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdCrossing.class, 5, 4),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdChestCorridor.class, 5, 4),
                    new StructureStrongholdPieceWeight2(net.minecraft.src.ComponentStrongholdLibrary.class, 10, 2),
                    new StructureStrongholdPieceWeight3(net.minecraft.src.ComponentStrongholdPortalRoom.class, 20, 1)
                });
        pieceWeightArrayOld = (new StructureStrongholdPieceWeight[]
                {
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdStraight.class, 40, 0),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdPrison.class, 5, 5),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdLeftTurn.class, 20, 0),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdRightTurn.class, 20, 0),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdRoomCrossing.class, 10, 6),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdStairsStraight.class, 5, 10),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdStairsOld.class, 5, 10),
                    new StructureStrongholdPieceWeight(net.minecraft.src.ComponentStrongholdCrossingOld.class, 5, 4),
                    new StructureStrongholdPieceWeight2(net.minecraft.src.ComponentStrongholdLibrary.class, 10, 1)
                });
    }
}
