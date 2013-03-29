package net.minecraft.src;

public class BlockFence2 extends BlockFence
{
    public static boolean bigfences = false;
    public static boolean connect = true;

    public BlockFence2(int par1, String par2Str, Material par3Material)
    {
        super(par1, par2Str, par3Material);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        if (bigfences){
            return AxisAlignedBB.getAABBPool().getAABB(par2, par3, par4, par2 + 1, (float)par3 + 1.5F, par4 + 1);
        }
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (bigfences){
            setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
        }else{
            super.setBlockBoundsBasedOnState(par1IBlockAccess, par2, par3, par4);
        }
    }

    /**
     * Returns true if the specified block can be connected by a fence
     */
    @Override
    public boolean canConnectFenceTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (!connect){
            int i = par1IBlockAccess.getBlockId(par2, par3, par4);

            if (i == blockID || i == Block.fenceGate.blockID)
            {
                return true;
            }
            return false;
        }else{
            return super.canConnectFenceTo(par1IBlockAccess, par2, par3, par4);
        }
    }
}
