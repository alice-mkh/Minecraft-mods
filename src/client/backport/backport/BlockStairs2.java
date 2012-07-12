package net.minecraft.src.backport;

import java.util.*;
import net.minecraft.src.*;

public class BlockStairs2 extends Block
{
    private static final int field_56797_a[][] =
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
    private final int modelData;
    private boolean field_56796_c;
    private int field_56795_co;

    public BlockStairs2(int par1, Block par2Block, int par3)
    {
        super(par1, par2Block.blockIndexInTexture, par2Block.blockMaterial);
        field_56796_c = false;
        field_56795_co = 0;
        modelBlock = par2Block;
        modelData = par3;
        setHardness(par2Block.getHardness());
        setResistance(par2Block.getExplosionResistance(null) * 5F / 3F);
        setStepSound(par2Block.stepSound);
        setLightOpacity(255);
    }

    public BlockStairs2(int par1, Block par2Block)
    {
        this(par1, par2Block, 0);
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (field_56796_c)
        {
            setBlockBounds(0.5F * (float)(field_56795_co % 2), 0.5F * (float)((field_56795_co / 2) % 2), 0.5F * (float)((field_56795_co / 4) % 2), 0.5F + 0.5F * (float)(field_56795_co % 2), 0.5F + 0.5F * (float)((field_56795_co / 2) % 2), 0.5F + 0.5F * (float)((field_56795_co / 4) % 2));
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

    public void getCollidingBoundingBoxes(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, ArrayList par6List)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.5F;
        float f3 = 1.0F;

        if ((i & 4) != 0)
        {
            f = 0.5F;
            f1 = 1.0F;
            f2 = 0.0F;
            f3 = 0.5F;
        }

        setBlockBounds(0.0F, f, 0.0F, 1.0F, f1, 1.0F);
        super.getCollidingBoundingBoxes(par1World, par2, par3, par4, par5AxisAlignedBB, par6List);

        if (j == 0)
        {
            setBlockBounds(0.5F, f2, 0.0F, 1.0F, f3, 1.0F);
            super.getCollidingBoundingBoxes(par1World, par2, par3, par4, par5AxisAlignedBB, par6List);
        }
        else if (j == 1)
        {
            setBlockBounds(0.0F, f2, 0.0F, 0.5F, f3, 1.0F);
            super.getCollidingBoundingBoxes(par1World, par2, par3, par4, par5AxisAlignedBB, par6List);
        }
        else if (j == 2)
        {
            setBlockBounds(0.0F, f2, 0.5F, 1.0F, f3, 1.0F);
            super.getCollidingBoundingBoxes(par1World, par2, par3, par4, par5AxisAlignedBB, par6List);
        }
        else if (j == 3)
        {
            setBlockBounds(0.0F, f2, 0.0F, 1.0F, f3, 0.5F);
            super.getCollidingBoundingBoxes(par1World, par2, par3, par4, par5AxisAlignedBB, par6List);
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
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        return modelBlock.getBlockTextureFromSideAndMetadata(par1, modelData);
    }

    /**
     * Returns the block texture based on the side being looked at.  Args: side
     */
    public int getBlockTextureFromSide(int par1)
    {
        return modelBlock.getBlockTextureFromSideAndMetadata(par1, modelData);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate()
    {
        return modelBlock.tickRate();
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
    public void velocityToAddToEntity(World par1World, int par2, int par3, int par4, Entity par5Entity, Vec3D par6Vec3D)
    {
        modelBlock.velocityToAddToEntity(par1World, par2, par3, par4, par5Entity, par6Vec3D);
    }

    /**
     * Returns if this block is collidable (only used by Fire). Args: x, y, z
     */
    public boolean isCollidable()
    {
        return modelBlock.isCollidable();
    }

    /**
     * Returns whether this block is collideable based on the arguments passed in Args: blockMetaData, unknownFlag
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

    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
    {
        modelBlock.onBlockRemoval(par1World, par2, par3, par4);
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

    public boolean blockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer)
    {
        return modelBlock.blockActivated(par1World, par2, par3, par4, par5EntityPlayer);
    }

    /**
     * Called upon the block being destroyed by an explosion
     */
    public void onBlockDestroyedByExplosion(World par1World, int par2, int par3, int par4)
    {
        modelBlock.onBlockDestroyedByExplosion(par1World, par2, par3, par4);
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int i = MathHelper.floor_double((double)((par5EntityLiving.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        int j = par1World.getBlockMetadata(par2, par3, par4) & 4;

        if (i == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2 | j);
        }

        if (i == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 1 | j);
        }

        if (i == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3 | j);
        }

        if (i == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 0 | j);
        }
    }

    public void onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8)
    {
        if (par5 == 0)
        {
            int i = par1World.getBlockMetadata(par2, par3, par4);
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i | 4);
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit. Args: world,
     * x, y, z, startVec, endVec
     */
    public MovingObjectPosition collisionRayTrace(World par1World, int par2, int par3, int par4, Vec3D par5Vec3D, Vec3D par6Vec3D)
    {
        MovingObjectPosition amovingobjectposition[] = new MovingObjectPosition[8];
        int i = par1World.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        boolean flag = (i & 4) == 4;
        int ai[] = field_56797_a[j + (flag ? 4 : 0)];
        field_56796_c = true;

        for (int k = 0; k < 8; k++)
        {
            field_56795_co = k;
            int ai2[] = ai;
            int l = ai2.length;

            for (int j1 = 0; j1 < l; j1++)
            {
                int l1 = ai2[j1];

                if (l1 != k);
            }

            amovingobjectposition[k] = super.collisionRayTrace(par1World, par2, par3, par4, par5Vec3D, par6Vec3D);
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

            double d1 = movingobjectposition1.hitVec.squareDistanceTo(par6Vec3D);

            if (d1 > d)
            {
                movingobjectposition = movingobjectposition1;
                d = d1;
            }
        }

        return movingobjectposition;
    }
}
