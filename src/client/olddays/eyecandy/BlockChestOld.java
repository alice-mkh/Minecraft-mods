package net.minecraft.src;

import java.util.*;

public class BlockChestOld extends BlockChest
{
    public static boolean normalblock = false;

    public Icon sidetex;
    public Icon fronttex;
    public Icon toptex;
    public Icon texfrontleft;
    public Icon texfrontright;
    public Icon texbackleft;
    public Icon texbackright;

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
            return toptex;
        }

        if (par5 == 0)
        {
            return toptex;
        }

        int i = par1IBlockAccess.getBlockId(par2, par3, par4 - 1);
        int j = par1IBlockAccess.getBlockId(par2, par3, par4 + 1);
        int k = par1IBlockAccess.getBlockId(par2 - 1, par3, par4);
        int l = par1IBlockAccess.getBlockId(par2 + 1, par3, par4);

        if (i == blockID || j == blockID)
        {
            if (par5 == 2 || par5 == 3)
            {
                return sidetex;
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

            return par5 == byte1 ? (i1 != 0 ? texfrontleft : texfrontright) : (i1 != 0 ? texbackleft : texbackright);
        }

        if (k == blockID || l == blockID)
        {
            if (par5 == 4 || par5 == 5)
            {
                return sidetex;
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
            return par5 == byte2 ? (j1 != 0 ? texfrontleft : texfrontright) : (j1 != 0 ? texbackleft : texbackright);
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

        return par5 != byte0 ? sidetex : fronttex;
    }

    @Override
    public Icon getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if (!normalblock){
            return blockIcon;
        }
        if (par1 == 1)
        {
            return toptex;
        }

        if (par1 == 0)
        {
            return toptex;
        }

        if (par1 == 3)
        {
            return fronttex;
        }
        else
        {
            return sidetex;
        }
    }

    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        super.registerIcons(par1IconRegister);
        toptex = par1IconRegister.registerIcon("olddays_chest_top");
        fronttex = par1IconRegister.registerIcon("olddays_chest_front");
        sidetex = par1IconRegister.registerIcon("olddays_chest_side");
        texfrontleft = par1IconRegister.registerIcon("olddays_chest_front_left");
        texfrontright = par1IconRegister.registerIcon("olddays_chest_front_right");
        texbackleft = par1IconRegister.registerIcon("olddays_chest_back_left");
        texbackright = par1IconRegister.registerIcon("olddays_chest_back_right");
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
