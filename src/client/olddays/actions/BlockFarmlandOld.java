package net.minecraft.src;

import java.util.Random;

public class BlockFarmlandOld extends BlockFarmland{
    public static boolean oldbreaking = false;

    protected BlockFarmlandOld(int par1)
    {
        super(par1);
    }

    /**
     * Block's chance to react to an entity falling on it.
     */
    @Override
    public void onFallenUpon(World par1World, int par2, int par3, int par4, Entity par5Entity, float par6)
    {
        if (oldbreaking){
            return;
        }
        super.onFallenUpon(par1World, par2, par3, par4, par5Entity, par6);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    @Override
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (oldbreaking && !par1World.isRemote && par1World.rand.nextInt(4) == 0)
        {
            par1World.setBlock(par2, par3, par4, Block.dirt.blockID);
        }
    }
}
