package net.minecraft.src;

import java.util.Random;

public class BlockPurpleFlower extends BlockFlower {
	protected BlockPurpleFlower(int i, int j) {
		super (i, j);
        blockIndexInTexture = j;
        setTickRandomly(true);
        float f = 0.2F;
        setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 3F, 0.5F + f);

	}
	
	public int idDropped(int i, Random random, int j)
    {
        return mod_ExtraBiomesFlowers.purpleFlowerItem.shiftedIndex;
    }
}