package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockPistonBase extends Block
{
    /** This pistons is the sticky one? */
    private boolean isSticky;
    private boolean ignoreUpdates;

    public BlockPistonBase(int par1, int par2, boolean par3)
    {
        super(par1, par2, Material.piston);
        isSticky = par3;
        setStepSound(soundStoneFootstep);
        setHardness(0.5F);
    }

    /**
     * Return the either 106 or 107 as the texture index depending on the isSticky flag. This will actually never get
     * called by TileEntityRendererPiston.renderPiston() because TileEntityPiston.shouldRenderHead() will always return
     * false.
     */
    public int getPistonExtensionTexture()
    {
        return !isSticky ? 107 : 106;
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public int getBlockTextureFromSideAndMetadata(int par1, int par2)
    {
        int i = getOrientation(par2);

        if (i > 5)
        {
            return blockIndexInTexture;
        }

        if (par1 == i)
        {
            if (isExtended(par2) || minX > 0.0D || minY > 0.0D || minZ > 0.0D || maxX < 1.0D || maxY < 1.0D || maxZ < 1.0D)
            {
                return 110;
            }
            else
            {
                return blockIndexInTexture;
            }
        }

        return par1 != Facing.faceToSide[i] ? 108 : 109;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return 16;
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
     * Called upon block activation (left or right click on the block.). The three integers represent x,y,z of the
     * block.
     */
    public boolean blockActivated(World par1World, int par2, int par3, int i, EntityPlayer entityplayer)
    {
        return false;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving)
    {
        int i = determineOrientation(par1World, par2, par3, par4, (EntityPlayer)par5EntityLiving);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, i);

        if (!par1World.isRemote && !ignoreUpdates)
        {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (!par1World.isRemote && !ignoreUpdates)
        {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null && !ignoreUpdates)
        {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * handles attempts to extend or retract the piston.
     */
    private void updatePistonState(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        int j = getOrientation(i);
        boolean flag = isIndirectlyPowered(par1World, par2, par3, par4, j);

        if (i == 7)
        {
            return;
        }

        if (flag && !isExtended(i))
        {
            if (canExtend(par1World, par2, par3, par4, j))
            {
                par1World.setBlockMetadata(par2, par3, par4, j | 8);
                par1World.playNoteAt(par2, par3, par4, 0, j);
            }
        }
        else if (!flag && isExtended(i))
        {
            par1World.setBlockMetadata(par2, par3, par4, j);
            par1World.playNoteAt(par2, par3, par4, 1, j);
        }
    }

    /**
     * checks the block to that side to see if it is indirectly powered.
     */
    private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 != 0 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3 - 1, par4, 0))
        {
            return true;
        }

        if (par5 != 1 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3 + 1, par4, 1))
        {
            return true;
        }

        if (par5 != 2 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3, par4 - 1, 2))
        {
            return true;
        }

        if (par5 != 3 && par1World.isBlockIndirectlyProvidingPowerTo(par2, par3, par4 + 1, 3))
        {
            return true;
        }

        if (par5 != 5 && par1World.isBlockIndirectlyProvidingPowerTo(par2 + 1, par3, par4, 5))
        {
            return true;
        }

        if (par5 != 4 && par1World.isBlockIndirectlyProvidingPowerTo(par2 - 1, par3, par4, 4))
        {
            return true;
        }

        if (par1World.isBlockIndirectlyProvidingPowerTo(par2, par3, par4, 0))
        {
            return true;
        }

        if (par1World.isBlockIndirectlyProvidingPowerTo(par2, par3 + 2, par4, 1))
        {
            return true;
        }

        if (par1World.isBlockIndirectlyProvidingPowerTo(par2, par3 + 1, par4 - 1, 2))
        {
            return true;
        }

        if (par1World.isBlockIndirectlyProvidingPowerTo(par2, par3 + 1, par4 + 1, 3))
        {
            return true;
        }

        if (par1World.isBlockIndirectlyProvidingPowerTo(par2 - 1, par3 + 1, par4, 4))
        {
            return true;
        }

        return par1World.isBlockIndirectlyProvidingPowerTo(par2 + 1, par3 + 1, par4, 5);
    }

    public void powerBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        ignoreUpdates = true;
        int i = par6;

        if (par5 == 0)
        {
            if (tryExtend(par1World, par2, par3, par4, i))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i | 8);
                par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.out", 0.5F, par1World.rand.nextFloat() * 0.25F + 0.6F);
            }
            else
            {
                par1World.setBlockMetadata(par2, par3, par4, i);
            }
        }
        else if (par5 == 1)
        {
            TileEntity tileentity = par1World.getBlockTileEntity(par2 + Facing.offsetsXForSide[i], par3 + Facing.offsetsYForSide[i], par4 + Facing.offsetsZForSide[i]);

            if (tileentity != null && (tileentity instanceof TileEntityPiston))
            {
                ((TileEntityPiston)tileentity).clearPistonTileEntity();
            }

            par1World.setBlockAndMetadata(par2, par3, par4, Block.pistonMoving.blockID, i);
            par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(blockID, i, i, false, true));

            if (isSticky)
            {
                int j = par2 + Facing.offsetsXForSide[i] * 2;
                int k = par3 + Facing.offsetsYForSide[i] * 2;
                int l = par4 + Facing.offsetsZForSide[i] * 2;
                int i1 = par1World.getBlockId(j, k, l);
                int j1 = par1World.getBlockMetadata(j, k, l);
                boolean flag = false;

                if (i1 == Block.pistonMoving.blockID)
                {
                    TileEntity tileentity1 = par1World.getBlockTileEntity(j, k, l);

                    if (tileentity1 != null && (tileentity1 instanceof TileEntityPiston))
                    {
                        TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity1;

                        if (tileentitypiston.getPistonOrientation() == i && tileentitypiston.isExtending())
                        {
                            tileentitypiston.clearPistonTileEntity();
                            i1 = tileentitypiston.getStoredBlockID();
                            j1 = tileentitypiston.getBlockMetadata();
                            flag = true;
                        }
                    }
                }

                if (i1 > 0 && (flag || canPushBlock(i1, par1World, j, k, l, false)) && (Block.blocksList[i1].getMobilityFlag() == 0 || i1 == Block.pistonBase.blockID || i1 == Block.pistonStickyBase.blockID))
                {
                    par2 += Facing.offsetsXForSide[i];
                    par3 += Facing.offsetsYForSide[i];
                    par4 += Facing.offsetsZForSide[i];
                    par1World.setBlockAndMetadata(par2, par3, par4, Block.pistonMoving.blockID, j1);
                    par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(i1, j1, i, false, false));
                    ignoreUpdates = false;
                    par1World.setBlockWithNotify(j, k, l, 0);
                    ignoreUpdates = true;
                }
                else if (!flag)
                {
                    ignoreUpdates = false;
                    par1World.setBlockWithNotify(par2 + Facing.offsetsXForSide[i], par3 + Facing.offsetsYForSide[i], par4 + Facing.offsetsZForSide[i], 0);
                    ignoreUpdates = true;
                }
            }
            else
            {
                ignoreUpdates = false;
                par1World.setBlockWithNotify(par2 + Facing.offsetsXForSide[i], par3 + Facing.offsetsYForSide[i], par4 + Facing.offsetsZForSide[i], 0);
                ignoreUpdates = true;
            }

            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.in", 0.5F, par1World.rand.nextFloat() * 0.15F + 0.6F);
        }

        ignoreUpdates = false;
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);

        if (isExtended(i))
        {
            switch (getOrientation(i))
            {
                case 0:
                    setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 1:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                    break;

                case 2:
                    setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                    break;

                case 3:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                    break;

                case 4:
                    setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 5:
                    setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
                    break;
            }
        }
        else
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender()
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Adds to the supplied array any colliding bounding boxes with the passed in bounding box. Args: world, x, y, z,
     * axisAlignedBB, arrayList
     */
    public void getCollidingBoundingBoxes(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, ArrayList par6ArrayList)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.getCollidingBoundingBoxes(par1World, par2, par3, par4, par5AxisAlignedBB, par6ArrayList);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
     * cleared to be reused)
     */
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        setBlockBoundsBasedOnState(par1World, par2, par3, par4);
        return super.getCollisionBoundingBoxFromPool(par1World, par2, par3, par4);
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * returns an int which describes the direction the piston faces
     */
    public static int getOrientation(int par0)
    {
        return par0 & 7;
    }

    /**
     * Determine if the metadata is related to something powered.
     */
    public static boolean isExtended(int par0)
    {
        return (par0 & 8) != 0;
    }

    /**
     * gets the way this piston should face for that entity that placed it.
     */
    private static int determineOrientation(World par0World, int par1, int par2, int par3, EntityPlayer par4EntityPlayer)
    {
        if (MathHelper.abs((float)par4EntityPlayer.posX - (float)par1) < 2.0F && MathHelper.abs((float)par4EntityPlayer.posZ - (float)par3) < 2.0F)
        {
            double d = (par4EntityPlayer.posY + 1.8200000000000001D) - (double)par4EntityPlayer.yOffset;

            if (d - (double)par2 > 2D)
            {
                return 1;
            }

            if ((double)par2 - d > 0.0D)
            {
                return 0;
            }
        }

        int i = MathHelper.floor_double((double)((par4EntityPlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (i == 0)
        {
            return 2;
        }

        if (i == 1)
        {
            return 5;
        }

        if (i == 2)
        {
            return 3;
        }

        return i != 3 ? 0 : 4;
    }

    /**
     * returns true if the piston can push the specified block
     */
    private static boolean canPushBlock(int par0, World par1World, int par2, int par3, int par4, boolean par5)
    {
        if (par0 == Block.obsidian.blockID)
        {
            return false;
        }

        if (par0 == Block.pistonBase.blockID || par0 == Block.pistonStickyBase.blockID)
        {
            if (isExtended(par1World.getBlockMetadata(par2, par3, par4)))
            {
                return false;
            }
        }
        else
        {
            if (Block.blocksList[par0].getHardness() == -1F)
            {
                return false;
            }

            if (Block.blocksList[par0].getMobilityFlag() == 2)
            {
                return false;
            }

            if (!par5 && Block.blocksList[par0].getMobilityFlag() == 1)
            {
                return false;
            }
        }

        return !(Block.blocksList[par0] instanceof BlockContainer);
    }

    /**
     * checks to see if this piston could push the blocks in front of it.
     */
    private static boolean canExtend(World par0World, int par1, int par2, int par3, int par4)
    {
        int i = par1 + Facing.offsetsXForSide[par4];
        int j = par2 + Facing.offsetsYForSide[par4];
        int k = par3 + Facing.offsetsZForSide[par4];
        int l = 0;

        do
        {
            if (l >= 13)
            {
                break;
            }

            if (j <= 0 || j >= 255)
            {
                return false;
            }

            int i1 = par0World.getBlockId(i, j, k);

            if (i1 == 0)
            {
                break;
            }

            if (!canPushBlock(i1, par0World, i, j, k, true))
            {
                return false;
            }

            if (Block.blocksList[i1].getMobilityFlag() == 1)
            {
                break;
            }

            if (l == 12)
            {
                return false;
            }

            i += Facing.offsetsXForSide[par4];
            j += Facing.offsetsYForSide[par4];
            k += Facing.offsetsZForSide[par4];
            l++;
        }
        while (true);

        return true;
    }

    /**
     * attempts to extend the piston. returns false if impossible.
     */
    private boolean tryExtend(World par1World, int par2, int par3, int par4, int par5)
    {
        int i = par2 + Facing.offsetsXForSide[par5];
        int j = par3 + Facing.offsetsYForSide[par5];
        int k = par4 + Facing.offsetsZForSide[par5];
        int l = 0;

        do
        {
            if (l >= 13)
            {
                break;
            }

            if (j <= 0 || j >= 255)
            {
                return false;
            }

            int j1 = par1World.getBlockId(i, j, k);

            if (j1 == 0)
            {
                break;
            }

            if (!canPushBlock(j1, par1World, i, j, k, true))
            {
                return false;
            }

            if (Block.blocksList[j1].getMobilityFlag() == 1)
            {
                Block.blocksList[j1].dropBlockAsItem(par1World, i, j, k, par1World.getBlockMetadata(i, j, k), 0);
                par1World.setBlockWithNotify(i, j, k, 0);
                break;
            }

            if (l == 12)
            {
                return false;
            }

            i += Facing.offsetsXForSide[par5];
            j += Facing.offsetsYForSide[par5];
            k += Facing.offsetsZForSide[par5];
            l++;
        }
        while (true);

        int l1;

        for (; i != par2 || j != par3 || k != par4; k = l1)
        {
            int i1 = i - Facing.offsetsXForSide[par5];
            int k1 = j - Facing.offsetsYForSide[par5];
            l1 = k - Facing.offsetsZForSide[par5];
            int i2 = par1World.getBlockId(i1, k1, l1);
            int j2 = par1World.getBlockMetadata(i1, k1, l1);

            if (i2 == blockID && i1 == par2 && k1 == par3 && l1 == par4)
            {
                par1World.setBlockAndMetadata(i, j, k, Block.pistonMoving.blockID, par5 | (isSticky ? 8 : 0));
                par1World.setBlockTileEntity(i, j, k, BlockPistonMoving.getTileEntity(Block.pistonExtension.blockID, par5 | (isSticky ? 8 : 0), par5, true, false));
            }
            else
            {
                par1World.setBlockAndMetadata(i, j, k, Block.pistonMoving.blockID, j2);
                par1World.setBlockTileEntity(i, j, k, BlockPistonMoving.getTileEntity(i2, j2, par5, true, false));
            }

            i = i1;
            j = k1;
        }

        return true;
    }
}
