package net.minecraft.src.nbxlite.spawners;

import java.util.Random;
import net.minecraft.src.Chunk;
import net.minecraft.src.ChunkPosition;
import net.minecraft.src.World;

public class OldSpawnerMonsters extends OldSpawnerAnimals
{
    public OldSpawnerMonsters(int i, Class class1, Class aclass[])
    {
        super(i, class1, aclass);
    }

    protected ChunkPosition func_1151_a(World world, int i, int j)
    {
        int k = i + world.rand.nextInt(16);
        int l = world.rand.nextInt(world.rand.nextInt(getWorldHeight(world, i, j) - 8) + 8);
        int i1 = j + world.rand.nextInt(16);
        return new ChunkPosition(k, l, i1);
    }
}
