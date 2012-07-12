package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.BlockOre;

public class BlockOre2 extends BlockOre{
    public BlockOre2(int par1, int par2){
        super(par1, par2);
    }

    public int idDropped(int par1, Random par2Random, int par3){
        if (blockID == 129)
        {
            return 132 + 256;
        }
        return super.idDropped(par1, par2Random, par3);
    }
}