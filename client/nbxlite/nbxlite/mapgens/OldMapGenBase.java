package net.minecraft.src.nbxlite.mapgens;

import java.util.Random;
import net.minecraft.src.IChunkProvider;
import net.minecraft.src.World;

public class OldMapGenBase
{

    protected int range;
    protected Random rand;

    public OldMapGenBase()
    {
        range = 8;
        rand = new Random();
    }

    public void generate(IChunkProvider ichunkprovider, World world, int i, int j, byte abyte0[])
    {
        int k = range;
        rand.setSeed(world.getSeed());
        long l = (rand.nextLong() / 2L) * 2L + 1L;
        long l1 = (rand.nextLong() / 2L) * 2L + 1L;
        for(int i1 = i - k; i1 <= i + k; i1++)
        {
            for(int j1 = j - k; j1 <= j + k; j1++)
            {
                rand.setSeed((long)i1 * l + (long)j1 * l1 ^ world.getSeed());
                recursiveGenerate(world, i1, j1, i, j, abyte0);
            }

        }

    }

    protected void recursiveGenerate(World world, int i, int j, int k, int l, byte abyte0[])
    {
    }
}
