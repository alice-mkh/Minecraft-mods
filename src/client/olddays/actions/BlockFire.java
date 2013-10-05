package net.minecraft.src;

import java.util.Random;

public class BlockFire extends Block
{
    private int chanceToEncourageFire[];
    private int abilityToCatchFire[];
    private Icon iconArray[];

    public static boolean oldFire = false;
    public static boolean infiniteBurn = false;
    public static boolean fixedDamage = false;

    protected BlockFire(int par1)
    {
        super(par1, Material.fire);
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
        setBurnRate(Block.woodDoubleSlab.blockID, 5, 20);
        setBurnRate(Block.woodSingleSlab.blockID, 5, 20);
        setBurnRate(Block.fence.blockID, 5, 20);
        setBurnRate(Block.stairsWoodOak.blockID, 5, 20);
        setBurnRate(Block.stairsWoodBirch.blockID, 5, 20);
        setBurnRate(Block.stairsWoodSpruce.blockID, 5, 20);
        setBurnRate(Block.stairsWoodJungle.blockID, 5, 20);
        setBurnRate(Block.wood.blockID, 5, 5);
        setBurnRate(Block.leaves.blockID, 30, 60);
        setBurnRate(Block.bookShelf.blockID, 30, 20);
        setBurnRate(Block.tnt.blockID, 15, 100);
        setBurnRate(Block.tallGrass.blockID, 60, 100);
        setBurnRate(Block.cloth.blockID, 30, 60);
        setBurnRate(Block.vine.blockID, 15, 100);
        setBurnRate(Block.coalBlock.blockID, 5, 5);
        setBurnRate(Block.hay.blockID, 60, 20);
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
    public int tickRate(World par1World)
    {
        return oldFire? 10 : 30;
    }

    public void updateTickOld(World world, int i, int j, int k, Random random)
    {
        boolean flag = world.getBlockId(i, j - 1, k) == Block.netherrack.blockID;
        if(!flag && world.isRaining() && (world.canLightningStrikeAt(i, j, k) || world.canLightningStrikeAt(i - 1, j, k) || world.canLightningStrikeAt(i + 1, j, k) || world.canLightningStrikeAt(i, j, k - 1) || world.canLightningStrikeAt(i, j, k + 1)))
        {
            world.setBlockToAir(i, j, k);
            return;
        }
        int l = world.getBlockMetadata(i, j, k);
        if(l < 15)
        {
            world.setBlockMetadataWithNotify(i, j, k, l + 1, 3);
            world.scheduleBlockUpdate(i, j, k, blockID, tickRate(world));
        }
        if(!flag && !canNeighborBurn(world, i, j, k))
        {
            if(!world.isBlockOpaqueCube(i, j - 1, k) || l > 3)
            {
                world.setBlockToAir(i, j, k);
            }
            return;
        }
        if(!flag && !canBlockCatchFire(world, i, j - 1, k) && l == 15 && random.nextInt(4) == 0)
        {
            world.setBlockToAir(i, j, k);
            return;
        }
        if(l % 2 == 0 && l > 2)
        {
            tryToCatchBlockOnFire_old(world, i + 1, j, k, 300, random);
            tryToCatchBlockOnFire_old(world, i - 1, j, k, 300, random);
            tryToCatchBlockOnFire_old(world, i, j - 1, k, 250, random);
            tryToCatchBlockOnFire_old(world, i, j + 1, k, 250, random);
            tryToCatchBlockOnFire_old(world, i, j, k - 1, 300, random);
            tryToCatchBlockOnFire_old(world, i, j, k + 1, 300, random);
            for(int i1 = i - 1; i1 <= i + 1; i1++)
            {
                for(int j1 = k - 1; j1 <= k + 1; j1++)
                {
                    for(int k1 = j - 1; k1 <= j + 4; k1++)
                    {
                        if(i1 == i && k1 == j && j1 == k)
                        {
                            continue;
                        }
                        int l1 = 100;
                        if(k1 > j + 1)
                        {
                            l1 += (k1 - (j + 1)) * 100;
                        }
                        int i2 = getChanceOfNeighborsEncouragingFire(world, i1, k1, j1);
                        if(i2 > 0 && random.nextInt(l1) <= i2 && (!world.isRaining() || !world.canLightningStrikeAt(i1, k1, j1)) && !world.canLightningStrikeAt(i1 - 1, k1, k) && !world.canLightningStrikeAt(i1 + 1, k1, j1) && !world.canLightningStrikeAt(i1, k1, j1 - 1) && !world.canLightningStrikeAt(i1, k1, j1 + 1))
                        {
                            world.setBlock(i1, k1, j1, blockID);
                        }
                    }

                }

            }

        }
        if(l == 15 && !infiniteBurn)
        {
            tryToCatchBlockOnFire_old(world, i + 1, j, k, 1, random);
            tryToCatchBlockOnFire_old(world, i - 1, j, k, 1, random);
            tryToCatchBlockOnFire_old(world, i, j - 1, k, 1, random);
            tryToCatchBlockOnFire_old(world, i, j + 1, k, 1, random);
            tryToCatchBlockOnFire_old(world, i, j, k - 1, 1, random);
            tryToCatchBlockOnFire_old(world, i, j, k + 1, 1, random);
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        if (!par1World.getGameRules().getGameRuleBooleanValue("doFireTick"))
        {
            return;
        }
        if (oldFire){
            updateTickOld(par1World, par2, par3, par4, par5Random);
            return;
        }

        boolean flag = par1World.getBlockId(par2, par3 - 1, par4) == Block.netherrack.blockID;

        if ((par1World.provider instanceof WorldProviderEnd) && par1World.getBlockId(par2, par3 - 1, par4) == Block.bedrock.blockID)
        {
            flag = true;
        }

        if (!canPlaceBlockAt(par1World, par2, par3, par4))
        {
            par1World.setBlockToAir(par2, par3, par4);
        }

        if (!flag && par1World.isRaining() && (par1World.canLightningStrikeAt(par2, par3, par4) || par1World.canLightningStrikeAt(par2 - 1, par3, par4) || par1World.canLightningStrikeAt(par2 + 1, par3, par4) || par1World.canLightningStrikeAt(par2, par3, par4 - 1) || par1World.canLightningStrikeAt(par2, par3, par4 + 1)))
        {
            par1World.setBlockToAir(par2, par3, par4);
            return;
        }

        int i = par1World.getBlockMetadata(par2, par3, par4);

        if (i < 15)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i + par5Random.nextInt(3) / 2, 4);
        }

        par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate(par1World) + par5Random.nextInt(10));

        if (!flag && !canNeighborBurn(par1World, par2, par3, par4))
        {
            if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || i > 3)
            {
                par1World.setBlockToAir(par2, par3, par4);
            }

            return;
        }

        if (!flag && !canBlockCatchFire(par1World, par2, par3 - 1, par4) && i == 15 && par5Random.nextInt(4) == 0)
        {
            par1World.setBlockToAir(par2, par3, par4);
            return;
        }

        boolean flag1 = par1World.isBlockHighHumidity(par2, par3, par4);
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

                    int k1 = (j1 + 40 + (fixedDamage ? 0 : par1World.difficultySetting * 7)) / (i + 30);

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

                    par1World.setBlock(j, l, k, blockID, l1, 3);
                }
            }
        }
    }

    public boolean func_82506_l()
    {
        return false;
    }

    private void tryToCatchBlockOnFire_old(World world, int i, int j, int k, int l, Random random)
    {
        int i1 = abilityToCatchFire[world.getBlockId(i, j, k)];
        if(random.nextInt(l) < i1)
        {
            boolean flag = world.getBlockId(i, j, k) == Block.tnt.blockID;
            if(random.nextInt(2) == 0 && !world.canLightningStrikeAt(i, j, k))
            {
                world.setBlock(i, j, k, blockID);
            } else
            {
                world.setBlockToAir(i, j, k);
            }
            if(flag)
            {
                Block.tnt.onBlockDestroyedByPlayer(world, i, j, k, 0);
            }
        }
    }

    private void tryToCatchBlockOnFire(World par1World, int par2, int par3, int par4, int par5, Random par6Random, int par7)
    {
        if (oldFire){
            tryToCatchBlockOnFire_old(par1World, par2, par3, par4, par5, par6Random);
            return;
        }
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

                par1World.setBlock(par2, par3, par4, blockID, j, 3);
            }
            else
            {
                par1World.setBlockToAir(par2, par3, par4);
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
        return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || canNeighborBurn(par1World, par2, par3, par4);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !canNeighborBurn(par1World, par2, par3, par4))
        {
            par1World.setBlockToAir(par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (par1World.provider.dimensionId <= 0 && par1World.getBlockId(par2, par3 - 1, par4) == Block.obsidian.blockID && Block.portal.tryToCreatePortal(par1World, par2, par3, par4))
        {
            return;
        }

        if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) && !canNeighborBurn(par1World, par2, par3, par4))
        {
            par1World.setBlockToAir(par2, par3, par4);
            return;
        }
        else
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate(par1World) + par1World.rand.nextInt(10));
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
            par1World.playSound((float)par2 + 0.5F, (float)par3 + 0.5F, (float)par4 + 0.5F, "fire.fire", 1.0F + par5Random.nextFloat(), par5Random.nextFloat() * 0.7F + 0.3F, false);
        }

        if (par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || Block.fire.canBlockCatchFire(par1World, par2, par3 - 1, par4))
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

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        iconArray = (new Icon[]
                {
                    par1IconRegister.registerIcon((new StringBuilder()).append(getTextureName()).append("_layer_0").toString()), par1IconRegister.registerIcon((new StringBuilder()).append(getTextureName()).append("_layer_1").toString())
                });
    }

    public Icon getFireIcon(int par1)
    {
        return iconArray[par1];
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return iconArray[0];
    }
}
