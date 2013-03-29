package net.minecraft.src.nbxlite.mapgens;

import java.io.PrintStream;
import java.util.Random;

public class MapGenSkyStronghold extends MapGenStronghold2
{
    public MapGenSkyStronghold()
    {
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int i, int j)
    {
        boolean flag = rand.nextInt(200) == 0 && rand.nextInt(120) < Math.max(Math.abs(i), Math.abs(j));
        if(flag)
        {
            System.out.printf("generating stronghold at chunk %d, %d!\n", new Object[] {
                Integer.valueOf(i * 16), Integer.valueOf(j * 16)
            });
        }
        return flag;
    }
}
