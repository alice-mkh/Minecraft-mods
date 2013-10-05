package net.minecraft.src;

import java.util.*;

public class BlockChestOld extends BlockChest
{
    public static boolean normalblock = false;

    public Icon texSide;
    public Icon texFront;
    public Icon texTop;
    public Icon texFrontLeft;
    public Icon texFrontRight;
    public Icon texBackLeft;
    public Icon texBackRight;

    protected BlockChestOld(int par1, int par2)
    {
        super(par1, par2);
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return normalblock ? 0 : 22;
    }

    @Override
    public Icon getBlockTexture(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        if (!normalblock){
            return blockIcon;
        }
        if (par5 == 1)
        {
            return texTop;
        }

        if (par5 == 0)
        {
            return texTop;
        }

        int i = par1IBlockAccess.getBlockId(par2, par3, par4 - 1);
        int j = par1IBlockAccess.getBlockId(par2, par3, par4 + 1);
        int k = par1IBlockAccess.getBlockId(par2 - 1, par3, par4);
        int l = par1IBlockAccess.getBlockId(par2 + 1, par3, par4);

        if (i == blockID || j == blockID)
        {
            if (par5 == 2 || par5 == 3)
            {
                return texSide;
            }

            int i1 = 0;

            if (i == blockID)
            {
                i1 = -1;
            }

            int k1 = par1IBlockAccess.getBlockId(par2 - 1, par3, i != blockID ? par4 + 1 : par4 - 1);
            int i2 = par1IBlockAccess.getBlockId(par2 + 1, par3, i != blockID ? par4 + 1 : par4 - 1);

            if (par5 == 4)
            {
                i1 = -1 - i1;
            }

            byte byte1 = 5;

            if ((Block.opaqueCubeLookup[k] || Block.opaqueCubeLookup[k1]) && !Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[i2])
            {
                byte1 = 5;
            }

            if ((Block.opaqueCubeLookup[l] || Block.opaqueCubeLookup[i2]) && !Block.opaqueCubeLookup[k] && !Block.opaqueCubeLookup[k1])
            {
                byte1 = 4;
            }

            return par5 == byte1 ? (i1 != 0 ? texFrontLeft : texFrontRight) : (i1 != 0 ? texBackLeft : texBackRight);
        }

        if (k == blockID || l == blockID)
        {
            if (par5 == 4 || par5 == 5)
            {
                return texSide;
            }

            int j1 = 0;

            if (k == blockID)
            {
                j1 = -1;
            }

            int l1 = par1IBlockAccess.getBlockId(k != blockID ? par2 + 1 : par2 - 1, par3, par4 - 1);
            int j2 = par1IBlockAccess.getBlockId(k != blockID ? par2 + 1 : par2 - 1, par3, par4 + 1);

            if (par5 == 3)
            {
                j1 = -1 - j1;
            }

            byte byte2 = 3;

            if ((Block.opaqueCubeLookup[i] || Block.opaqueCubeLookup[l1]) && !Block.opaqueCubeLookup[j] && !Block.opaqueCubeLookup[j2])
            {
                byte2 = 3;
            }

            if ((Block.opaqueCubeLookup[j] || Block.opaqueCubeLookup[j2]) && !Block.opaqueCubeLookup[i] && !Block.opaqueCubeLookup[l1])
            {
                byte2 = 2;
            }
            return par5 == byte2 ? (j1 != 0 ? texFrontLeft : texFrontRight) : (j1 != 0 ? texBackLeft : texBackRight);
        }

        byte byte0 = 3;

        if (Block.opaqueCubeLookup[i] && !Block.opaqueCubeLookup[j])
        {
            byte0 = 3;
        }

        if (Block.opaqueCubeLookup[j] && !Block.opaqueCubeLookup[i])
        {
            byte0 = 2;
        }

        if (Block.opaqueCubeLookup[k] && !Block.opaqueCubeLookup[l])
        {
            byte0 = 5;
        }

        if (Block.opaqueCubeLookup[l] && !Block.opaqueCubeLookup[k])
        {
            byte0 = 4;
        }

        return par5 != byte0 ? texSide : texFront;
    }

    @Override
    public Icon getIcon(int par1, int par2)
    {
        if (!normalblock){
            return blockIcon;
        }
        if (par1 == 1)
        {
            return texTop;
        }

        if (par1 == 0)
        {
            return texTop;
        }

        if (par1 == 3)
        {
            return texFront;
        }
        else
        {
            return texSide;
        }
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        texTop = par1IconRegister.registerIcon("olddays_chest_top");
        texSide = par1IconRegister.registerIcon("olddays_chest_side");
        texBackLeft = par1IconRegister.registerIcon("olddays_chest_back_left");
        texBackRight = par1IconRegister.registerIcon("olddays_chest_back_right");
        if (chestType == 1){
            texFront = par1IconRegister.registerIcon("olddays_chest_front_trap");
            texFrontLeft = par1IconRegister.registerIcon("olddays_chest_front_left_trap");
            texFrontRight = par1IconRegister.registerIcon("olddays_chest_front_right_trap");
        }else{
            texFront = par1IconRegister.registerIcon("olddays_chest_front");
            texFrontLeft = par1IconRegister.registerIcon("olddays_chest_front_left");
            texFrontRight = par1IconRegister.registerIcon("olddays_chest_front_right");
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4){
        if (normalblock){
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
            return;
        }
        super.setBlockBoundsBasedOnState(par1IBlockAccess, par2, par3, par4);
    }
}
