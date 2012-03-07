package net.minecraft.src;

import java.util.Random;

import java.util.*;

public class BlockRedRock extends Block
{

    public BlockRedRock(int i, int j)
    {
        super(i, j, Material.rock);           
    }
    public int idDropped(int i, Random random)
    {
       return mod_RedRock.redRock.blockID;
    }
    public int quantityDropped(Random random)
    {
            return 1;
    }
}