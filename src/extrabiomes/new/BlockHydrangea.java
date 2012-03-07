package net.minecraft.src;

import java.util.Random;

public class BlockHydrangea extends BlockFlower {
	protected BlockHydrangea(int i, int j) {
		super(i, j);
		float f = 0.4F;
		setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.8F, 0.5F + f);

	}

	public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i,
			int j, int k) {
		boolean flag = iblockaccess.getBlockMetadata(i, j, k) == 1;
		float f = 1.0F;
		if (flag) {
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}

		else

		{
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		}
	}
	
	public int idDropped(int i, Random random, int j)
    {
        return mod_Hydrangea.hydrangeaItem.shiftedIndex;
    }
}