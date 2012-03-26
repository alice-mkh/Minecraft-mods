package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block
{
    private int chanceToEncourageFire[];
    private int abilityToCatchFire[];

    protected BlockFire(int par1, int par2)
    {
        super(par1, par2, Material.fire);
        chanceToEncourageFire = new int[256];
        abilityToCatchFire = new int[256];
        setTickRandomly(true);
    }

    /**
     * This method is called on a block after all other blocks gets already created. You can use it to reference and
     * configure something on the block that needs the others ones.
     */
    public void initializeBlock()
    {
        setBurnRate(Block.planks.blockID, 5, 20);
        setBurnRate(Block.fence.blockID, 5, 20);
        setBurnRate(Block.stairCompactPlanks.blockID, 5, 20);
        setBurnRate(Block.wood.blockID, 5, 5);
        setBurnRate(Block.leaves.blockID, 30, 60);
        setBurnRate(Block.bookShelf.blockID, 30, 20);
        setBurnRate(Block.tnt.blockID, 15, 100);
        setBurnRate(Block.tallGrass.blockID, 60, 100);
        setBurnRate(Block.cloth.blockID, 30, 60);
        setBurnRate(Block.vine.blockID, 15, 100);
    }

    /**
     * Sets the burn rate for a block. The larger abilityToCatchFire the more easily it will catch. The larger
     * chanceToEncourageFire the faster it will burn and spread to other blocks. Args: blockID, chanceToEncourageFire,
     * abilityToCatchFire
     */
    private void setBurnRate(int par1, int par2, int par3)
    {
        chanceToEncourageFire[par1] = par2;
        abilityToCatchFire[par1] = par3;
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int i)
    {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 3;
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate()
    {
        if (mod_WTFActions.FastFire){
            return 40;
        }
        return 30;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        boolean flag = par1World.getBlockId(par2, par3 - 1, par4) == Block.netherrack.blockID;

        if ((par1World.worldProvider instanceof WorldProviderEnd) && par1World.getBlockId(par2, par3 - 1, par4) == Block.bedrock.blockID)
        {
            flag = true;
        }

        if (!canPlaceBlockAt(par1World, par2, par3, par4))
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }

        if (!flag && par1World.isRaining() && (par1World.canLightningStrikeAt(par2, par3, par4) || par1World.canLightningStrikeAt(par2 - 1, par3, par4) || par1World.canLightningStrikeAt(par2 + 1, par3, par4) || par1World.canLightningStrikeAt(par2, par3, par4 - 1) || par1World.canLightningStrikeAt(par2, par3, par4 + 1)))
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return;
        }

        int i = par1World.getBlockMetadata(par2, par3, par4);

        if (i < 15)
        {
            par1World.setBlockMetadata(par2, par3, par4, i + par5Random.nextInt(3) / 2);
        }

        par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate() + par5Random.nextInt(10));

        if (!flag && !canNeighborBurn(par1World, par2, par3, par4))
        {
            if (!par1World.isBlockNormalCube(par2, par3 - 1, par4) || i > 3)
            {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }

            return;
        }

        if (!flag && !canBlockCatchFire(par1World, par2, par3 - 1, par4) && i == 15 && par5Random.nextInt(4) == 0)
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return;
        }

        boolean flag1 = par1World.func_48455_z(par2, par3, par4);
        byte byte0 = 0;

        if (flag1)
        {
            byte0 = -50;
        }

        tryToCatchBlockOnFire(par1World, par2 + 1, par3, par4, 300 + byte0, par5Random, i);
        tryToCatchBlockOnFire(par1World, par2 - 1, par3, par4, 300 + byte0, par5Random, i);
        tryToCatchBlockOnFire(par1World, par2, par3 - 1, par4, 250 + byte0, par5Random, i);
        tryToCatchBlockOnFire(par1World, par2, par3 + 1, par4, 250 + byte0, par5Random, i);
        tryToCatchBlockOnFire(par1World, par2, par3, par4 - 1, 300 + byte0, par5Random, i);
        tryToCatchBlockOnFire(par1World, par2, par3, par4 + 1, 300 + byte0, par5Random, i);

        for (int j = par2 - 1; j <= par2 + 1; j++)
        {
            for (int k = par4 - 1; k <= par4 + 1; k++)
            {
                for (int l = par3 - 1; l <= par3 + 4; l++)
                {
                    if (j == par2 && l == par3 && k == par4)
                    {
                        continue;
                    }

                    int i1 = 100;

                    if (l > par3 + 1)
                    {
                        i1 += (l - (par3 + 1)) * 100;
                    }

                    int j1 = getChanceOfNeighborsEncouragingFire(par1World, j, l, k);

                    if (j1 <= 0)
                    {
                        continue;
                    }

                    int k1 = (j1 + 40) / (i + 30);

                    if (flag1)
                    {
                        k1 /= 2;
                    }

                    if (k1 <= 0 || par5Random.nextInt(i1) > k1 || par1World.isRaining() && par1World.canLightningStrikeAt(j, l, k) || par1World.canLightningStrikeAt(j - 1, l, par4) || par1World.canLightningStrikeAt(j + 1, l, k) || par1World.canLightningStrikeAt(j, l, k - 1) || par1World.canLightningStrikeAt(j, l, k + 1))
                    {
                        continue;
                    }

                    int l1 = i + par5Random.nextInt(5) / 4;

                    if (l1 > 15)
                    {
                        l1 = 15;
                    }

                    par1World.setBlockAndMetadataWithNotify(j, l, k, blockID, l1);
                }
            }
        }
    }

    private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7)
    {
        int i = abilityToCatchFire[par1World.getBlockId(par2, par3, par4)];

        if (par6Random.nextInt(par5) < i)
        {
            boolean flag = par1World.getBlockId(par2, par3, par4) == Block.tnt.blockID;

            if (par6Random.nextInt(par7 + 10) < 5 && !par1World.canLightningStrikeAt(par2, par3, par4))
            {
                int j = par7 + par6Random.nextInt(5) / 4;

                if (j > 15)
                {
                    j = 15;
                }

                par1World.setBlockAndMetadataWithNotify(par2, par3, par4, blockID, j);
            }
            else
            {
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }

            if (flag)
            {
                Block.tnt.onBlockDestroyedByPlayer(par1World, par2, par3, par4, 1);
            }
        }
    }

    /**
     * Returns true if at least one block next to this one can burn.
     */
    private boolean canNeighborBurn(World par1World, int par2, int par3, int par4)
    {
        if (canBlockCatchFire(par1World, par2 + 1, par3, par4))
        {
            return true;
        }

        if (canBlockCatchFire(par1World, par2 - 1, par3, par4))
        {
            return true;
        }

        if (canBlockCatchFire(par1World, par2, par3 - 1, par4))
        {
            return true;
        }

        if (canBlockCatchFire(par1World, par2, par3 + 1, par4))
        {
            return true;
        }

        if (canBlockCatchFire(par1World, par2, par3, par4 - 1))
        {
            return true;
        }

        return canBlockCatchFire(par1World, par2, par3, par4 + 1);
    }

    /**
     * Gets the highest chance of a neighbor block encouraging this block to catch fire
     */
    private int getChanceOfNeighborsEncouragingFire(World par1World, int par2, int par3, int par4)
    {
        int i = 0;

        if (!par1World.isAirBlock(par2, par3, par4))
        {
            return 0;
        }
        else
        {
            i = getChanceToEncourageFire(par1World, par2 + 1, par3, par4, i);
            i = getChanceToEncourageFire(par1World, par2 - 1, par3, par4, i);
            i = getChanceToEncourageFire(par1World, par2, par3 - 1, par4, i);
            i = getChanceToEncourageFire(par1World, par2, par3 + 1, par4, i);
            i = getChanceToEncourageFire(par1World, par2, par3, par4 - 1, i);
            i = getChanceToEncourageFire(par1World, par2, par3, par4 + 1, i);
            return i;
        }
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable()
    {
        return false;
    }

    /**
     * Checks the specified block coordinate to see if it can catch fire.  Args: blockAccess, x, y, z
     */
    public boolean canBlockCatchFire(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return chanceToEncourageFire[par1IBlockAccess.getBlockId(par2, par3, par4)] > 0;
    }

    /**
     * Retrieves a specified block's chance to encourage their neighbors to burn and if the number is greater than the
     * current number passed in it will return its number instead of the passed in one.  Args: world, x, y, z,
     * curChanceToEncourageFire
     */
    public int getChanceToEncourageFire(World par1World, int par2, int par3, int par4, int par5)
    {
        int i = chanceToEncourageFire[par1World.getBlockId(par2, par3, par4)];

        if (i > par5)
        {
            return i;
        }
        else
        {
            return par5;
        }
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return par1World.isBlockNormalCube(par2, par3 - 1, par4) || canNeighborBurn(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isBlockNormalCube(par2, par3 - 1, par4) && !canNeighborBurn(par1World, par2, par3, par4))
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return;
        }
        else
        {
            return;
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.worldProvider.worldType <= 0 && par1World.getBlockId(par2, par3 - 1, par4) == Block.obsidian.blockID && Block.portal.tryToCreatePortal(par1World, par2, par3, par4))
        {
            return;
        }

        if (!par1World.isBlockNormalCube(par2, par3 - 1, par4) && !canNeighborBurn(par1World, par2, par3, par4))
        {
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return;
        }
        else
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate() + par1World.rand.nextInt(10));
            return;
        }
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (par5Random.nextInt(24) == 0)
        {
            par1World.playSoundEffect((float)par2 + 0.5F, (float)par3 + 0.5F, (float)par4 + 0.5F, "fire.fire", 1.0F + par5Random.nextFloat(), par5Random.nextFloat() * 0.7F + 0.3F);
        }

        if (par1World.isBlockNormalCube(par2, par3 - 1, par4) || Block.fire.canBlockCatchFire(par1World, par2, par3 - 1, par4))
        {
            for (int i = 0; i < 3; i++)
            {
                float f = (float)par2 + par5Random.nextFloat();
                float f6 = (float)par3 + par5Random.nextFloat() * 0.5F + 0.5F;
                float f12 = (float)par4 + par5Random.nextFloat();
                par1World.spawnParticle("largesmoke", f, f6, f12, 0.0D, 0.0D, 0.0D);
            }
        }
        else
        {
            if (Block.fire.canBlockCatchFire(par1World, par2 - 1, par3, par4))
            {
                for (int j = 0; j < 2; j++)
                {
                    float f1 = (float)par2 + par5Random.nextFloat() * 0.1F;
                    float f7 = (float)par3 + par5Random.nextFloat();
                    float f13 = (float)par4 + par5Random.nextFloat();
                    par1World.spawnParticle("largesmoke", f1, f7, f13, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.fire.canBlockCatchFire(par1World, par2 + 1, par3, par4))
            {
                for (int k = 0; k < 2; k++)
                {
                    float f2 = (float)(par2 + 1) - par5Random.nextFloat() * 0.1F;
                    float f8 = (float)par3 + par5Random.nextFloat();
                    float f14 = (float)par4 + par5Random.nextFloat();
                    par1World.spawnParticle("largesmoke", f2, f8, f14, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.fire.canBlockCatchFire(par1World, par2, par3, par4 - 1))
            {
                for (int l = 0; l < 2; l++)
                {
                    float f3 = (float)par2 + par5Random.nextFloat();
                    float f9 = (float)par3 + par5Random.nextFloat();
                    float f15 = (float)par4 + par5Random.nextFloat() * 0.1F;
                    par1World.spawnParticle("largesmoke", f3, f9, f15, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.fire.canBlockCatchFire(par1World, par2, par3, par4 + 1))
            {
                for (int i1 = 0; i1 < 2; i1++)
                {
                    float f4 = (float)par2 + par5Random.nextFloat();
                    float f10 = (float)par3 + par5Random.nextFloat();
                    float f16 = (float)(par4 + 1) - par5Random.nextFloat() * 0.1F;
                    par1World.spawnParticle("largesmoke", f4, f10, f16, 0.0D, 0.0D, 0.0D);
                }
            }

            if (Block.fire.canBlockCatchFire(par1World, par2, par3 + 1, par4))
            {
                for (int j1 = 0; j1 < 2; j1++)
                {
                    float f5 = (float)par2 + par5Random.nextFloat();
                    float f11 = (float)(par3 + 1) - par5Random.nextFloat() * 0.1F;
                    float f17 = (float)par4 + par5Random.nextFloat();
                    par1World.spawnParticle("largesmoke", f5, f11, f17, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }
}
