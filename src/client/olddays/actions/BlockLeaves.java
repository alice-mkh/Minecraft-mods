package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockLeaves extends BlockLeavesBase
{
    public static final String LEAF_TYPES[] =
    {
        "oak", "spruce", "birch", "jungle"
    };
    public static final String field_94396_b[][] =
    {
        {
            "leaves_oak", "leaves_spruce", "leaves_birch", "leaves_jungle"
        }, {
            "leaves_oak_opaque", "leaves_spruce_opaque", "leaves_birch_opaque", "leaves_jungle_opaque"
        }
    };

    /** 1 for fast graphic. 0 for fancy graphics. used in iconArray. */
    private int iconType;
    private Icon iconArray[][];
    int adjacentTreeBlocks[];

    public static boolean apples = true;

    protected BlockLeaves(int par1)
    {
        super(par1, Material.leaves, false);
        iconArray = new Icon[2][];
        setTickRandomly(true);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    public int getBlockColor()
    {
        double d = 0.5D;
        double d1 = 1.0D;
        return ColorizerFoliage.getFoliageColor(d, d1);
    }

    /**
     * Returns the color this block should be rendered. Used by leaves.
     */
    public int getRenderColor(int par1)
    {
        if ((par1 & 3) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }

        if ((par1 & 3) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }
        else
        {
            return ColorizerFoliage.getFoliageColorBasic();
        }
    }

    /**
     * Returns a integer with hex for 0xrrggbb with this color multiplied against the blocks color. Note only called
     * when first determining what to render.
     */
    public int colorMultiplier(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if ((i & 3) == 1)
        {
            return ColorizerFoliage.getFoliageColorPine();
        }

        if ((i & 3) == 2)
        {
            return ColorizerFoliage.getFoliageColorBirch();
        }

        int j = 0;
        int k = 0;
        int l = 0;

        for (int i1 = -1; i1 <= 1; i1++)
        {
            for (int j1 = -1; j1 <= 1; j1++)
            {
                int k1 = par1IBlockAccess.getBiomeGenForCoords(par2 + j1, par4 + i1).getBiomeFoliageColor();
                j += (k1 & 0xff0000) >> 16;
                k += (k1 & 0xff00) >> 8;
                l += k1 & 0xff;
            }
        }

        return (j / 9 & 0xff) << 16 | (k / 9 & 0xff) << 8 | l / 9 & 0xff;
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        int i = 1;
        int j = i + 1;

        if (par1World.checkChunksExist(par2 - j, par3 - j, par4 - j, par2 + j, par3 + j, par4 + j))
        {
            for (int k = -i; k <= i; k++)
            {
                for (int l = -i; l <= i; l++)
                {
                    for (int i1 = -i; i1 <= i; i1++)
                    {
                        int j1 = par1World.getBlockId(par2 + k, par3 + l, par4 + i1);

                        if (j1 == Block.leaves.blockID)
                        {
                            int k1 = par1World.getBlockMetadata(par2 + k, par3 + l, par4 + i1);
                            par1World.setBlockMetadataWithNotify(par2 + k, par3 + l, par4 + i1, k1 | 8, 4);
                        }
                    }
                }
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.isRemote)
        {
            return;
        }

        int i = par1World.getBlockMetadata(par2, par3, par4);

        if ((i & 8) != 0 && (i & 4) == 0)
        {
            byte byte0 = 4;
            int j = byte0 + 1;
            byte byte1 = 32;
            int k = byte1 * byte1;
            int l = byte1 / 2;

            if (adjacentTreeBlocks == null)
            {
                adjacentTreeBlocks = new int[byte1 * byte1 * byte1];
            }

            if (par1World.checkChunksExist(par2 - j, par3 - j, par4 - j, par2 + j, par3 + j, par4 + j))
            {
                for (int i1 = -byte0; i1 <= byte0; i1++)
                {
                    for (int l1 = -byte0; l1 <= byte0; l1++)
                    {
                        for (int j2 = -byte0; j2 <= byte0; j2++)
                        {
                            int l2 = par1World.getBlockId(par2 + i1, par3 + l1, par4 + j2);

                            if (l2 == Block.wood.blockID)
                            {
                                adjacentTreeBlocks[(i1 + l) * k + (l1 + l) * byte1 + (j2 + l)] = 0;
                                continue;
                            }

                            if (l2 == Block.leaves.blockID)
                            {
                                adjacentTreeBlocks[(i1 + l) * k + (l1 + l) * byte1 + (j2 + l)] = -2;
                            }
                            else
                            {
                                adjacentTreeBlocks[(i1 + l) * k + (l1 + l) * byte1 + (j2 + l)] = -1;
                            }
                        }
                    }
                }

                for (int j1 = 1; j1 <= 4; j1++)
                {
                    for (int i2 = -byte0; i2 <= byte0; i2++)
                    {
                        for (int k2 = -byte0; k2 <= byte0; k2++)
                        {
                            for (int i3 = -byte0; i3 <= byte0; i3++)
                            {
                                if (adjacentTreeBlocks[(i2 + l) * k + (k2 + l) * byte1 + (i3 + l)] != j1 - 1)
                                {
                                    continue;
                                }

                                if (adjacentTreeBlocks[((i2 + l) - 1) * k + (k2 + l) * byte1 + (i3 + l)] == -2)
                                {
                                    adjacentTreeBlocks[((i2 + l) - 1) * k + (k2 + l) * byte1 + (i3 + l)] = j1;
                                }

                                if (adjacentTreeBlocks[(i2 + l + 1) * k + (k2 + l) * byte1 + (i3 + l)] == -2)
                                {
                                    adjacentTreeBlocks[(i2 + l + 1) * k + (k2 + l) * byte1 + (i3 + l)] = j1;
                                }

                                if (adjacentTreeBlocks[(i2 + l) * k + ((k2 + l) - 1) * byte1 + (i3 + l)] == -2)
                                {
                                    adjacentTreeBlocks[(i2 + l) * k + ((k2 + l) - 1) * byte1 + (i3 + l)] = j1;
                                }

                                if (adjacentTreeBlocks[(i2 + l) * k + (k2 + l + 1) * byte1 + (i3 + l)] == -2)
                                {
                                    adjacentTreeBlocks[(i2 + l) * k + (k2 + l + 1) * byte1 + (i3 + l)] = j1;
                                }

                                if (adjacentTreeBlocks[(i2 + l) * k + (k2 + l) * byte1 + ((i3 + l) - 1)] == -2)
                                {
                                    adjacentTreeBlocks[(i2 + l) * k + (k2 + l) * byte1 + ((i3 + l) - 1)] = j1;
                                }

                                if (adjacentTreeBlocks[(i2 + l) * k + (k2 + l) * byte1 + (i3 + l + 1)] == -2)
                                {
                                    adjacentTreeBlocks[(i2 + l) * k + (k2 + l) * byte1 + (i3 + l + 1)] = j1;
                                }
                            }
                        }
                    }
                }
            }

            int k1 = adjacentTreeBlocks[l * k + l * byte1 + l];

            if (k1 >= 0)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i & -9, 4);
            }
            else
            {
                removeLeaves(par1World, par2, par3, par4);
            }
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par1World.canLightningStrikeAt(par2, par3 + 1, par4) && !par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && par5Random.nextInt(15) == 1)
        {
            double d = (float)par2 + par5Random.nextFloat();
            double d1 = (double)par3 - 0.050000000000000003D;
            double d2 = (float)par4 + par5Random.nextFloat();
            par1World.spawnParticle("dripWater", d, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }

    private void removeLeaves(World par1World, int par2, int par3, int par4)
    {
        dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
        par1World.setBlockToAir(par2, par3, par4);
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return par1Random.nextInt(20) != 0 ? 0 : 1;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Block.sapling.blockID;
    }

    /**
     * Drops the block items with a specified chance of dropping the specified items
     */
    public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7)
    {
        if (!par1World.isRemote)
        {
            int i = 20;

            if ((par5 & 3) == 3)
            {
                i = 40;
            }

            if (par7 > 0)
            {
                i -= 2 << par7;

                if (i < 10)
                {
                    i = 10;
                }
            }

            if (par1World.rand.nextInt(i) == 0)
            {
                int j = idDropped(par5, par1World.rand, par7);
                dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(j, 1, damageDropped(par5)));
            }

            i = 200;

            if (par7 > 0)
            {
                i -= 10 << par7;

                if (i < 40)
                {
                    i = 40;
                }
            }

            if ((par5 & 3) == 0 && par1World.rand.nextInt(i) == 0 && apples)
            {
                dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(Item.appleRed, 1, 0));
            }
        }
    }

    /**
     * Called when the player destroys a block with an item that can harvest it. (i, j, k) are the coordinates of the
     * block and l is the block's subtype/damage.
     */
    public void harvestBlock(World par1World, EntityPlayer par2EntityPlayer, int par3, int par4, int par5, int par6)
    {
        if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null && par2EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.itemID)
        {
            par2EntityPlayer.addStat(StatList.mineBlockStatArray[blockID], 1);
            dropBlockAsItem_do(par1World, par3, par4, par5, new ItemStack(Block.leaves.blockID, 1, par6 & 3));
        }
        else
        {
            super.harvestBlock(par1World, par2EntityPlayer, par3, par4, par5, par6);
        }
    }

    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    public int damageDropped(int par1)
    {
        return par1 & 3;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return !graphicsLevel;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        if ((par2 & 3) == 1)
        {
            return iconArray[iconType][1];
        }

        if ((par2 & 3) == 3)
        {
            return iconArray[iconType][3];
        }

        if ((par2 & 3) == 2)
        {
            return iconArray[iconType][2];
        }
        else
        {
            return iconArray[iconType][0];
        }
    }

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    public void setGraphicsLevel(boolean par1)
    {
        graphicsLevel = par1;
        iconType = par1 ? 0 : 1;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs, List par3List)
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
        return new ItemStack(blockID, 1, par1 & 3);
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        for (int i = 0; i < field_94396_b.length; i++)
        {
            iconArray[i] = new Icon[field_94396_b[i].length];

            for (int j = 0; j < field_94396_b[i].length; j++)
            {
                iconArray[i][j] = par1IconRegister.registerIcon(field_94396_b[i][j]);
            }
        }
    }
}
