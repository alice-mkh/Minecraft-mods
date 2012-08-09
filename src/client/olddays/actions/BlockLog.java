package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockLog extends Block
{
    public static boolean rotate = true;

    public static final String field_72142_a[] =
    {
        "oak", "spruce", "birch", "jungle"
    };

    protected BlockLog(int par1)
    {
        super(par1, Material.wood);
        blockIndexInTexture = 20;
        func_71849_a(CreativeTabs.field_78030_b);
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 31;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.wood.blockID;
    }

    public void func_71852_a(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        byte byte0 = 4;
        int i = byte0 + 1;

        if (par1World.checkChunksExist(par2 - i, par3 - i, par4 - i, par2 + i, par3 + i, par4 + i))
        {
            for (int j = -byte0; j <= byte0; j++)
            {
                for (int k = -byte0; k <= byte0; k++)
                {
                    for (int l = -byte0; l <= byte0; l++)
                    {
                        int i1 = par1World.getBlockId(par2 + j, par3 + k, par4 + l);

                        if (i1 != Block.leaves.blockID)
                        {
                            continue;
                        }

                        int j1 = par1World.getBlockMetadata(par2 + j, par3 + k, par4 + l);

                        if ((j1 & 8) == 0)
                        {
                            par1World.setBlockMetadata(par2 + j, par3 + k, par4 + l, j1 | 8);
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4) & 3;
        int j = rotate ? BlockPistonBase.determineOrientation(par1World, par2, par3, par4, (EntityPlayer)par5EntityLiving) : 0;
        byte byte0 = 0;

        switch (j)
        {
            case 2:
            case 3:
                byte0 = 8;
                break;

            case 4:
            case 5:
                byte0 = 4;
                break;

            case 0:
            case 1:
                byte0 = 0;
                break;
        }

        par1World.setBlockMetadataWithNotify(par2, par3, par4, i | byte0);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        int i = par2 & 0xc;
        int j = par2 & 3;

        if (i == 0 && (par1 == 1 || par1 == 0))
        {
            return 21;
        }

        if (i == 4 && (par1 == 5 || par1 == 4))
        {
            return 21;
        }

        if (i == 8 && (par1 == 2 || par1 == 3))
        {
            return 21;
        }

        if (j == 1)
        {
            return 116;
        }

        if (j == 2)
        {
            return 117;
        }

        return j != 3 ? 20 : 153;
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    protected int damageDropped(int par1)
    {
        return par1 & 3;
    }

    public static int func_72141_e(int par0)
    {
        return par0 & 3;
    }

    public void func_71879_a(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        par3List.add(new ItemStack(par1, 1, 0));
        par3List.add(new ItemStack(par1, 1, 1));
        par3List.add(new ItemStack(par1, 1, 2));
        par3List.add(new ItemStack(par1, 1, 3));
    }

    /**
     * Returns an item stack containing a single instance of the current block type. 'i' is the block's subtype/damage
     * and is ignored for blocks which do not support subtypes. Blocks which cannot be harvested should return null.
     */
    protected ItemStack createStackedBlock(int par1)
    {
        return new ItemStack(blockID, 1, func_72141_e(par1));
    }
}
