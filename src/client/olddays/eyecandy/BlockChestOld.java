package net.minecraft.src;

import java.util.*;

public class BlockChestOld extends BlockChest
{
    public static boolean normalblock = false;

    protected BlockChestOld(int par1)
    {
        super(par1);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return normalblock ? 0 : 22;
    }
}
