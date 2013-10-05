package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockStairs extends Block
{
    public static boolean oldstairs = true;
    public static boolean upsidedown = true;
    public static boolean corner = true;

    private static final int field_72159_a[][] =
    {
        {
            2, 6
        }, {
            3, 7
        }, {
            2, 3
        }, {
            6, 7
        }, {
            0, 4
        }, {
            1, 5
        }, {
            0, 1
        }, {
            4, 5
        }
    };

    /** The block that is used as model for the stair. */
    private final Block modelBlock;
    private final int modelBlockMetadata;
    private boolean field_72156_cr;
    private int field_72160_cs;

    protected BlockStairs(int par1, Block par2Block, int par3)
    {
        super(par1, par2Block.blockMaterial);
        modelBlock = par2Block;
        modelBlockMetadata = par3;
        setHardness(par2Block.blockHardness);
        setResistance(par2Block.blockResistance / 3F);
        setStepSound(par2Block.stepSound);
        setLightOpacity(255);
        setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (field_72156_cr)
        {
            setBlockBounds(0.5F * (float)(field_72160_cs % 2), 0.5F * (float)((field_72160_cs / 2) % 2), 0.5F * (float)((field_72160_cs / 4) % 2), 0.5F + 0.5F * (float)(field_72160_cs % 2), 0.5F + 0.5F * (float)((field_72160_cs / 2) % 2), 0.5F + 0.5F * (float)((field_72160_cs / 4) % 2));
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
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
        return 10;
    }

    public void func_82541_d(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if ((i & 4) != 0)
        {
            setBlockBounds(0.0F, 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
    }

    /**
     * Checks if supplied ID is one of a BlockStairs
     */
    public static boolean isBlockStairsID(int par0)
    {
        return par0 > 0 && (Block.blocksList[par0] instanceof BlockStairs);
    }

    /**
     * returns true if the given block is a stairs block and is in the given direction of par5.  Parameters are
     * IBlockAccess, x, y, z, direction
     */
    private boolean isBlockStairsDirection(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        int i = par1IBlockAccess.getBlockId(par2, par3, par4);
        return isBlockStairsID(i) && par1IBlockAccess.getBlockMetadata(par2, par3, par4) == par5;
    }

    public boolean func_82542_g(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        float f = 0.5F;
        float f1 = 1.0F;

        if ((i & 4) != 0)
        {
            f = 0.0F;
            f1 = 0.5F;
        }

        float f2 = 0.0F;
        float f3 = 1.0F;
        float f4 = 0.0F;
        float f5 = 0.5F;

        if (!corner){
            if (j == 0){
                f2 = 0.5F;
                f5 = 1.0F;
            }else if (j == 1){
                f3 = 0.5F;
                f5 = 1.0F;
            }else if (j == 2){
                f4 = 0.5F;
                f5 = 1.0F;
            }
            setBlockBounds(f2, f, f4, f3, f1, f5);
            return true;
        }

        boolean flag = true;

        if (j == 0)
        {
            f2 = 0.5F;
            f5 = 1.0F;
            int k = par1IBlockAccess.getBlockId(par2 + 1, par3, par4);
            int k1 = par1IBlockAccess.getBlockMetadata(par2 + 1, par3, par4);

            if (isBlockStairsID(k) && (i & 4) == (k1 & 4))
            {
                int k2 = k1 & 3;

                if (k2 == 3 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 + 1, i))
                {
                    f5 = 0.5F;
                    flag = false;
                }
                else if (k2 == 2 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 - 1, i))
                {
                    f4 = 0.5F;
                    flag = false;
                }
            }
        }
        else if (j == 1)
        {
            f3 = 0.5F;
            f5 = 1.0F;
            int l = par1IBlockAccess.getBlockId(par2 - 1, par3, par4);
            int l1 = par1IBlockAccess.getBlockMetadata(par2 - 1, par3, par4);

            if (isBlockStairsID(l) && (i & 4) == (l1 & 4))
            {
                int l2 = l1 & 3;

                if (l2 == 3 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 + 1, i))
                {
                    f5 = 0.5F;
                    flag = false;
                }
                else if (l2 == 2 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 - 1, i))
                {
                    f4 = 0.5F;
                    flag = false;
                }
            }
        }
        else if (j == 2)
        {
            f4 = 0.5F;
            f5 = 1.0F;
            int i1 = par1IBlockAccess.getBlockId(par2, par3, par4 + 1);
            int i2 = par1IBlockAccess.getBlockMetadata(par2, par3, par4 + 1);

            if (isBlockStairsID(i1) && (i & 4) == (i2 & 4))
            {
                int i3 = i2 & 3;

                if (i3 == 1 && !isBlockStairsDirection(par1IBlockAccess, par2 + 1, par3, par4, i))
                {
                    f3 = 0.5F;
                    flag = false;
                }
                else if (i3 == 0 && !isBlockStairsDirection(par1IBlockAccess, par2 - 1, par3, par4, i))
                {
                    f2 = 0.5F;
                    flag = false;
                }
            }
        }
        else if (j == 3)
        {
            int j1 = par1IBlockAccess.getBlockId(par2, par3, par4 - 1);
            int j2 = par1IBlockAccess.getBlockMetadata(par2, par3, par4 - 1);

            if (isBlockStairsID(j1) && (i & 4) == (j2 & 4))
            {
                int j3 = j2 & 3;

                if (j3 == 1 && !isBlockStairsDirection(par1IBlockAccess, par2 + 1, par3, par4, i))
                {
                    f3 = 0.5F;
                    flag = false;
                }
                else if (j3 == 0 && !isBlockStairsDirection(par1IBlockAccess, par2 - 1, par3, par4, i))
                {
                    f2 = 0.5F;
                    flag = false;
                }
            }
        }

        setBlockBounds(f2, f, f4, f3, f1, f5);
        return flag;
    }

    public boolean func_82544_h(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        float f = 0.5F;
        float f1 = 1.0F;

        if ((i & 4) != 0)
        {
            f = 0.0F;
            f1 = 0.5F;
        }

        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = 0.5F;
        float f5 = 1.0F;

        if (!corner){
            return false;
        }

        boolean flag = false;

        if (j == 0)
        {
            int k = par1IBlockAccess.getBlockId(par2 - 1, par3, par4);
            int k1 = par1IBlockAccess.getBlockMetadata(par2 - 1, par3, par4);

            if (isBlockStairsID(k) && (i & 4) == (k1 & 4))
            {
                int k2 = k1 & 3;

                if (k2 == 3 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 - 1, i))
                {
                    f4 = 0.0F;
                    f5 = 0.5F;
                    flag = true;
                }
                else if (k2 == 2 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 + 1, i))
                {
                    f4 = 0.5F;
                    f5 = 1.0F;
                    flag = true;
                }
            }
        }
        else if (j == 1)
        {
            int l = par1IBlockAccess.getBlockId(par2 + 1, par3, par4);
            int l1 = par1IBlockAccess.getBlockMetadata(par2 + 1, par3, par4);

            if (isBlockStairsID(l) && (i & 4) == (l1 & 4))
            {
                f2 = 0.5F;
                f3 = 1.0F;
                int l2 = l1 & 3;

                if (l2 == 3 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 - 1, i))
                {
                    f4 = 0.0F;
                    f5 = 0.5F;
                    flag = true;
                }
                else if (l2 == 2 && !isBlockStairsDirection(par1IBlockAccess, par2, par3, par4 + 1, i))
                {
                    f4 = 0.5F;
                    f5 = 1.0F;
                    flag = true;
                }
            }
        }
        else if (j == 2)
        {
            int i1 = par1IBlockAccess.getBlockId(par2, par3, par4 - 1);
            int i2 = par1IBlockAccess.getBlockMetadata(par2, par3, par4 - 1);

            if (isBlockStairsID(i1) && (i & 4) == (i2 & 4))
            {
                f4 = 0.0F;
                f5 = 0.5F;
                int i3 = i2 & 3;

                if (i3 == 1 && !isBlockStairsDirection(par1IBlockAccess, par2 - 1, par3, par4, i))
                {
                    flag = true;
                }
                else if (i3 == 0 && !isBlockStairsDirection(par1IBlockAccess, par2 + 1, par3, par4, i))
                {
                    f2 = 0.5F;
                    f3 = 1.0F;
                    flag = true;
                }
            }
        }
        else if (j == 3)
        {
            int j1 = par1IBlockAccess.getBlockId(par2, par3, par4 + 1);
            int j2 = par1IBlockAccess.getBlockMetadata(par2, par3, par4 + 1);

            if (isBlockStairsID(j1) && (i & 4) == (j2 & 4))
            {
                int j3 = j2 & 3;

                if (j3 == 1 && !isBlockStairsDirection(par1IBlockAccess, par2 - 1, par3, par4, i))
                {
                    flag = true;
                }
                else if (j3 == 0 && !isBlockStairsDirection(par1IBlockAccess, par2 + 1, par3, par4, i))
                {
                    f2 = 0.5F;
                    f3 = 1.0F;
                    flag = true;
                }
            }
        }

        if (flag)
        {
            setBlockBounds(f2, f, f4, f3, f1, f5);
        }

        return flag;
    }

    /**
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        func_82541_d(par1World, par2, par3, par4);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        boolean flag = func_82542_g(par1World, par2, par3, par4);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);

        if (flag && func_82544_h(par1World, par2, par3, par4))
        {
            super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
        }

        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * A randomly called display update to be able to add particles or other items for display
     */
    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        modelBlock.randomDisplayTick(par1World, par2, par3, par4, par5Random);
    }

    /**
     * Called when the block is clicked by a player. Args: x, y, z, entityPlayer
     */
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        modelBlock.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
    }

    /**
     * Called right before the block is destroyed by a player.  Args: world, x, y, z, metaData
     */
    public void onBlockDestroyedByPlayer(World par1World, int par2, int par3, int par4, int par5)
    {
        modelBlock.onBlockDestroyedByPlayer(par1World, par2, par3, par4, par5);
    }

    /**
     * Goes straight to getLightBrightnessForSkyBlocks for Blocks, does some fancy computing for Fluids
     */
    public int getMixedBrightnessForBlock(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return modelBlock.getMixedBrightnessForBlock(par1IBlockAccess, par2, par3, par4);
    }

    /**
     * How bright to render this block based on the light its receiving. Args: iBlockAccess, x, y, z
     */
    public float getBlockBrightness(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return modelBlock.getBlockBrightness(par1IBlockAccess, par2, par3, par4);
    }

    /**
     * Returns how much this block can resist explosions from the passed in entity.
     */
    public float getExplosionResistance(Entity par1Entity)
    {
        return modelBlock.getExplosionResistance(par1Entity);
    }

    /**
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return modelBlock.getRenderBlockPass();
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        return modelBlock.getIcon(par1, modelBlockMetadata);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate(World par1World)
    {
        return modelBlock.tickRate(par1World);
    }

    /**
     * Returns the bounding box of the wired rectangular prism to render.
     */
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return modelBlock.getSelectedBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * Can add to the passed in vector for a movement vector to be applied to the entity. Args: x, y, z, entity, vec3d
     */
    public void velocityToAddToEntity(World par1World, int par2, int par3, int par4, Entity par5Entity, Vec3 par6Vec3)
    {
        modelBlock.velocityToAddToEntity(par1World, par2, par3, par4, par5Entity, par6Vec3);
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable()
    {
        return modelBlock.isCollidable();
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in \n@param par1 block metaData \n@param
     * par2 whether the player right-clicked while holding a boat
     */
    public boolean canCollideCheck(int par1, boolean par2)
    {
        return modelBlock.canCollideCheck(par1, par2);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        return modelBlock.canPlaceBlockAt(par1World, par2, par3, par4);
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        onNeighborBlockChange(par1World, par2, par3, par4, 0);
        modelBlock.onBlockAdded(par1World, par2, par3, par4);
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        modelBlock.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    /**
     * Called whenever an entity is walking on top of this block. Args: world, x, y, z, entity
     */
    public void onEntityWalking(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        modelBlock.onEntityWalking(par1World, par2, par3, par4, par5Entity);
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        modelBlock.updateTick(par1World, par2, par3, par4, par5Random);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9)
    {
        return modelBlock.onBlockActivated(par1World, par2, par3, par4, par5EntityPlayer, 0, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4, Explosion par5Explosion)
    {
        modelBlock.onBlockDestroyedByExplosion(par1World, par2, par3, par4, par5Explosion);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int i = MathHelper.floor_double((double)((par5EntityLivingBase.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        int j = par1World.getBlockMetadata(par2, par3, par4) & 4;
        if (oldstairs){
            setOldMetadata(par1World, par2, par3, par4);
            return;
        }

        if (i == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | j, 2);
        }

        if (i == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | j, 2);
        }

        if (i == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | j, 2);
        }

        if (i == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | j, 2);
        }
    }

    /**
     * Called when a block is placed using its ItemBlock. Args: World, X, Y, Z, side, hitX, hitY, hitZ, block metadata
     */
    public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9)
    {
        if (upsidedown && (par5 == 0 || par5 != 1 && (double)par7 > 0.5D))
        {
            return par9 | 4;
        }
        else
        {
            return par9;
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3 par5Vec3, Vec3 par6Vec3)
    {
        MovingObjectPosition amovingobjectposition[] = new MovingObjectPosition[8];
        int i = par1World.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        boolean flag = (i & 4) == 4;
        int ai[] = field_72159_a[j + (flag ? 4 : 0)];
        field_72156_cr = true;

        for (int k = 0; k < 8; k++)
        {
            field_72160_cs = k;
            int ai2[] = ai;
            int l = ai2.length;

            for (int j1 = 0; j1 < l; j1++)
            {
                int l1 = ai2[j1];

                if (l1 != k)
                {
                    ;
                }
            }

            amovingobjectposition[k] = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3, par6Vec3);
        }

        int ai1[] = ai;
        double d = ai1.length;

        for (int i1 = 0; i1 < d; i1++)
        {
            int k1 = ai1[i1];
            amovingobjectposition[k1] = null;
        }

        MovingObjectPosition movingobjectposition = null;
        d = 0.0D;
        MovingObjectPosition amovingobjectposition1[] = amovingobjectposition;
        int i2 = amovingobjectposition1.length;

        for (int j2 = 0; j2 < i2; j2++)
        {
            MovingObjectPosition movingobjectposition1 = amovingobjectposition1[j2];

            if (movingobjectposition1 == null)
            {
                continue;
            }

            double d1 = movingobjectposition1.hitVec.squareDistanceTo(par6Vec3);

            if (d1 > d)
            {
                movingobjectposition = movingobjectposition1;
                d = d1;
            }
        }

        return movingobjectposition;
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister iconregister)
    {
    }

    private boolean isStair(World world, int i, int j, int k)
    {
        int id = world.getBlockId(i, j, k);
        return id == 0 ? false : Block.blocksList[id].getRenderType() == 10;
    }

    private boolean isSolid(World world, int i, int j, int k)
    {
        return world.getBlockMaterial(i, j, k).isSolid();
    }

    private void setOldMetadata(World world, int i, int j, int k)
    {
        if (isStair(world, i, j, k))
        {
            byte data = -1;

            if (isStair(world, i + 1, j + 1, k))
            {
                data = 0;
            }

            if (isStair(world, i - 1, j + 1, k))
            {
                data = 1;
            }

            if (isStair(world, i, j + 1, k + 1))
            {
                data = 2;
            }

            if (isStair(world, i, j + 1, k - 1))
            {
                data = 3;
            }

            if (data < 0)
            {
                if (isSolid(world, i + 1, j, k) && !isSolid(world, i - 1, j, k))
                {
                    data = 0;
                }

                if (isSolid(world, i - 1, j, k) && !isSolid(world, i + 1, j, k))
                {
                    data = 1;
                }

                if (isSolid(world, i, j, k + 1) && !isSolid(world, i, j, k - 1))
                {
                    data = 2;
                }

                if (isSolid(world, i, j, k - 1) && !isSolid(world, i, j, k + 1))
                {
                    data = 3;
                }
            }

            if (data < 0)
            {
                if (isStair(world, i - 1, j - 1, k))
                {
                    data = 0;
                }

                if (isStair(world, i + 1, j - 1, k))
                {
                    data = 1;
                }

                if (isStair(world, i, j - 1, k - 1))
                {
                    data = 2;
                }

                if (isStair(world, i, j - 1, k + 1))
                {
                    data = 3;
                }
            }
            if (data < 0){
                data = 0;
            }
            world.setBlockMetadataWithNotify(i, j, k, data | 0, 2);
        }
    }

    @Override
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote && oldstairs)
        {
            if (par1World.getBlockMaterial(par2, par3 + 1, par4).isSolid())
            {
                par1World.setBlock(par2, par3, par4, modelBlock.blockID, modelBlockMetadata, 2);
            }
            else
            {
                setOldMetadata(par1World, par2, par3, par4);
                setOldMetadata(par1World, par2 + 1, par3 - 1, par4);
                setOldMetadata(par1World, par2 - 1, par3 - 1, par4);
                setOldMetadata(par1World, par2, par3 - 1, par4 - 1);
                setOldMetadata(par1World, par2, par3 - 1, par4 + 1);
                setOldMetadata(par1World, par2 + 1, par3 + 1, par4);
                setOldMetadata(par1World, par2 - 1, par3 + 1, par4);
                setOldMetadata(par1World, par2, par3 + 1, par4 - 1);
                setOldMetadata(par1World, par2, par3 + 1, par4 + 1);
            }

            modelBlock.onNeighborBlockChange(par1World, par2, par3, par4, par5);
        }
    }
}
