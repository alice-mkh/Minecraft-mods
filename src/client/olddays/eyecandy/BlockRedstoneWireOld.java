package net.minecraft.src;

import java.util.*;

public class BlockRedstoneWireOld extends BlockRedstoneWire
{
    public static boolean cross = false;

    public BlockRedstoneWireOld(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return cross ? ODEyecandy.redstoneRenderID : super.getRenderType();
    }
}
