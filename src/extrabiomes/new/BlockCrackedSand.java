package net.minecraft.src;

import java.util.Random;

import java.util.*;

public class BlockCrackedSand extends Block
{

    public BlockCrackedSand(int i, int j)
    {
        super(i, j, Material.rock);           
    }
    public int idDropped(int i, Random random)
    {
       return mod_CrackedSand.crackedSand.blockID;
    }
    public int quantityDropped(Random random)
    {
            return 1;
    }
}