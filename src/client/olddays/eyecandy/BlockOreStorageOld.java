package net.minecraft.src;

public class BlockOreStorageOld extends BlockOreStorage
{
    public int sidetex;
    public int bottomtex;
    public static boolean oldtextures = true;

    public BlockOreStorageOld(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int par1)
    {
        if (oldtextures){
            if (par1 >= 2){
                return sidetex;
            }
            if (par1 == 0){
                return bottomtex;
            }
        }
        return blockIndexInTexture;
    }
}
