package net.minecraft.src;

public class BlockOreStorageOld extends BlockOreStorage
{
    public int sidetex;
    public int bottomtex;
    public static boolean oldtextures = true;

    public BlockOreStorageOld(int par1)
    {
        super(par1);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
/*
    @Override
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
    }*/
}
