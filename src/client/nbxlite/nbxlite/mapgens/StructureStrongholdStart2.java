package net.minecraft.src.nbxlite.mapgens;

import java.util.*;
import net.minecraft.src.ComponentStrongholdStairs2;
import net.minecraft.src.StructureComponent;
import net.minecraft.src.StructureStart;
import net.minecraft.src.StructureStrongholdPieces;
import net.minecraft.src.World;

class StructureStrongholdStart2 extends StructureStart
{
    public StructureStrongholdStart2(World world, Random random, int i, int j)
    {
        StructureStrongholdPieces.prepareStructurePieces();
        ComponentStrongholdStairs2 componentstrongholdstairs2 = new ComponentStrongholdStairs2(0, random, (i << 4) + 2, (j << 4) + 2);
        components.add(componentstrongholdstairs2);
        componentstrongholdstairs2.buildComponent(componentstrongholdstairs2, components, random);
        StructureComponent structurecomponent;
        for (List list = componentstrongholdstairs2.field_75026_c; !list.isEmpty(); structurecomponent.buildComponent(componentstrongholdstairs2, components, random))
        {
            int k = random.nextInt(list.size());
            structurecomponent = (StructureComponent)list.remove(k);
        }

        updateBoundingBox();
        markAvailableHeight(world, random, 10);
    }
}
