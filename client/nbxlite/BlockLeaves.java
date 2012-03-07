package net.minecraft.src;

import java.util.Random;

public class BlockLeaves extends BlockLeavesBase
{
    public int fasttex;
    public int fancytex;

    /**
     * The base index in terrain.png corresponding to the fancy version of the leaf texture. This is stored so we can
     * switch the displayed version between fancy and fast graphics (fast is this index + 1).
     */
    private int baseIndexInPNG;
    int adjacentTreeBlocks[];

    protected BlockLeaves(int par1, int par2)
    {
        super(par1, par2, Material.leaves, false);
        baseIndexInPNG = par2;
        if (mod_noBiomesX.LeavesDecay){
            setTickRandomly(true);
        }
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
        if ((par1 & 3) == 3)
        {
            if (mod_noBiomesX.Generator==0){
                return 0x5fff3f;
            }
            return ColorizerFoliage.getFoliageColorBasic();
        }
        else
        {
            if (mod_noBiomesX.Generator==0){
                if (mod_noBiomesX.FallbackColors){
                    return 0x5fff3f;
                }
                return 0xffffff;
            }
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
        if ((i & 3) == 3)
        {
            return mod_noBiomesX.GetFoliageColorAtCoords(par1IBlockAccess, par2, par3, par4, true, false);
        }
        return mod_noBiomesX.GetFoliageColorAtCoords(par1IBlockAccess, par2, par3, par4, true, true);
    }

    /**
     * Called whenever the block is removed.
     */
    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
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
                            par1World.setBlockMetadata(par2 + k, par3 + l, par4 + i1, k1 | 8);
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
        if (!mod_noBiomesX.LeavesDecay){
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
                par1World.setBlockMetadata(par2, par3, par4, i & -9);
            }
            else
            {
                removeLeaves(par1World, par2, par3, par4);
            }
        }
    }

    private void removeLeaves(World par1World, int par2, int par3, int par4)
    {
        dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
        par1World.setBlockWithNotify(par2, par3, par4, 0);
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
            byte byte0 = 20;

            if ((par5 & 3) == 3)
            {
                byte0 = 40;
            }

            if (par1World.rand.nextInt(byte0) == 0)
            {
                int i = idDropped(par5, par1World.rand, par7);
                dropBlockAsItem_do(par1World, par2, par3, par4, new ItemStack(i, 1, damageDropped(par5)));
            }

            if ((par5 & 3) == 0 && par1World.rand.nextInt(200) == 0)
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
        if (!par1World.isRemote && par2EntityPlayer.getCurrentEquippedItem() != null && par2EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex)
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
    protected int damageDropped(int par1)
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
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        if ((par2 & 3) == 1)
        {
            return blockIndexInTexture + 80;
        }

        if ((par2 & 3) == 3)
        {
            return blockIndexInTexture + 144;
        }
        else
        {
            if (mod_noBiomesX.Generator!=0 || (par2 & 3) == 2 || mod_noBiomesX.FallbackColors){
                return blockIndexInTexture;
            }
            if (graphicsLevel){
                return fancytex;
            }
            return fasttex;
        }
    }

    /**
     * Pass true to draw this block using fancy graphics, or false for fast graphics.
     */
    public void setGraphicsLevel(boolean par1)
    {
        graphicsLevel = par1;
        blockIndexInTexture = baseIndexInPNG + (par1 ? 0 : 1);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        super.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }
}
