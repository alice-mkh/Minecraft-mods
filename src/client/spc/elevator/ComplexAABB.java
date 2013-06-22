package net.minecraft.src;

import java.util.ArrayList;

public class ComplexAABB extends AxisAlignedBB{
    private ArrayList<AxisAlignedBB> boxes;

    public ComplexAABB(){
        super(0D, 0D, 0D, 0D, 0D, 0D);
        boxes = new ArrayList<AxisAlignedBB>();
    }

    @Override
    public boolean intersectsWith(AxisAlignedBB par1AxisAlignedBB){
        for (AxisAlignedBB box : boxes){
            if (box.intersectsWith(par1AxisAlignedBB)){
                return true;
            }
        }
        return false;
    }

    public void add(AxisAlignedBB box){
        boxes.add(box);
    }

    @Override
    public AxisAlignedBB offset(double x, double y, double z){
        for (AxisAlignedBB box : boxes){
            box.minX += x;
            box.maxX += x;
            box.minY += y;
            box.maxY += y;
            box.minZ += z;
            box.maxZ += z;
        }
        return this;
    }
}