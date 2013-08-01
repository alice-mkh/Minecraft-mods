package net.minecraft.src.nbxlite;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Frustrum;

public class Frustrum2 extends Frustrum{
    @Override
    public boolean isBoxInFrustum(double par1, double par3, double par5, double par7, double par9, double par11){
        return true;
    }

    @Override
    public boolean isBoundingBoxInFrustum(AxisAlignedBB par1AxisAlignedBB){
        return true;
    }
}
