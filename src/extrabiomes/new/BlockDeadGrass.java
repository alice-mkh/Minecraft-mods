package net.minecraft.src;

import java.util.Random;

public class BlockDeadGrass extends BlockFlower {
	protected BlockDeadGrass(int i, int j) {
		super(i, j);
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}

	protected boolean canThisPlantGrowOnThisBlockID(int i) {
		return i == mod_CrackedSand.crackedSand.blockID;
	}

	public int idDropped(int i, Random random, int j) {
		return -1;
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
}
