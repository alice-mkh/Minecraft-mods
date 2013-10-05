package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class BlockPistonBase extends Block
{
    public static boolean dupe = false;

    /** This pistons is the sticky one? */
    private final boolean isSticky;
    private boolean ignoreUpdates;

    /** Only visible when piston is extended */
    private Icon innerTopIcon;

    /** Bottom side texture */
    private Icon bottomIcon;

    /** Top icon of piston depends on (either sticky or normal) */
    private Icon topIcon;

    public BlockPistonBase(int par1, boolean par2)
    {
        super(par1, Material.piston);
        isSticky = par2;
        setStepSound(soundStoneFootstep);
        setHardness(0.5F);
        setCreativeTab(CreativeTabs.tabRedstone);
    }

    /**
     * Return the either 106 or 107 as the texture index depending on the isSticky flag. This will actually never get
     * called by TileEntityRendererPiston.renderPiston() because TileEntityPiston.shouldRenderHead() will always return
     * false.
     */
    public Icon getPistonExtensionTexture()
    {
        return topIcon;
    }

    public void func_96479_b(float par1, float par2, float par3, float par4, float par5, float par6)
    {
        setBlockBounds(par1, par2, par3, par4, par5, par6);
    }

    /**
     * From the specified side and block metadata retrieves the blocks texture. Args: side, metadata
     */
    public Icon getIcon(int par1, int par2)
    {
        int i = getOrientation(par2);

        if (i > 5)
        {
            return topIcon;
        }

        if (par1 == i)
        {
            if (isExtended(par2) || minX > 0.0D || minY > 0.0D || minZ > 0.0D || maxX < 1.0D || maxY < 1.0D || maxZ < 1.0D)
            {
                return innerTopIcon;
            }
            else
            {
                return topIcon;
            }
        }

        if (par1 == Facing.oppositeSide[i])
        {
            return bottomIcon;
        }
        else
        {
            return blockIcon;
        }
    }

    public static Icon getPistonBaseIcon(String par0Str)
    {
        if (par0Str == "piston_side")
        {
            return Block.pistonBase.blockIcon;
        }

        if (par0Str == "piston_top_normal")
        {
            return Block.pistonBase.topIcon;
        }

        if (par0Str == "piston_top_sticky")
        {
            return Block.pistonStickyBase.topIcon;
        }

        if (par0Str == "piston_inner")
        {
            return Block.pistonBase.innerTopIcon;
        }
        else
        {
            return null;
        }
    }

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerIcons(IconRegister par1IconRegister)
    {
        blockIcon = par1IconRegister.registerIcon("piston_side");
        topIcon = par1IconRegister.registerIcon(isSticky ? "piston_top_sticky" : "piston_top_normal");
        innerTopIcon = par1IconRegister.registerIcon("piston_inner");
        bottomIcon = par1IconRegister.registerIcon("piston_bottom");
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
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World par1World, int par2, int par3, int i, EntityPlayer entityplayer, int j, float f, float f1, float f2)
    {
        return false;
    }

    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int i = determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase);
        par1World.setBlockMetadataWithNotify(par2, par3, par4, i, 2);

        if (!par1World.isRemote)
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
        if (!par1World.isRemote)
        {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        if (!par1World.isRemote && par1World.getBlockTileEntity(par2, par3, par4) == null)
        {
            updatePistonState(par1World, par2, par3, par4);
        }
    }

    /**
     * handles attempts to extend or retract the piston.
     */
    private void updatePistonState(World par1World, int par2, int par3, int par4)
    {
        if (dupe){
            updatePistonStateOld(par1World, par2, par3, par4);
            return;
        }
        int i = par1World.getBlockMetadata(par2, par3, par4);
        int j = getOrientation(i);

        if (j == 7)
        {
            return;
        }

        boolean flag = isIndirectlyPowered(par1World, par2, par3, par4, j);

        if (flag && !isExtended(i))
        {
            if (canExtend(par1World, par2, par3, par4, j))
            {
                par1World.addBlockEvent(par2, par3, par4, blockID, 0, j);
            }
        }
        else if (!flag && isExtended(i))
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, j, 2);
            par1World.addBlockEvent(par2, par3, par4, blockID, 1, j);
        }
    }

    /**
     * handles attempts to extend or retract the piston.
     */
    private void updatePistonStateOld(World par1World, int par2, int par3, int par4)
    {
        if (ignoreUpdates){
            return;
        }
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
                par1World.addBlockEvent(par2, par3, par4, blockID, 0, j);
            }
        }
        else if (!flag && isExtended(i))
        {
            par1World.addBlockEvent(par2, par3, par4, blockID, 1, j);
        }
    }

    /**
     * checks the block to that side to see if it is indirectly powered.
     */
    private boolean isIndirectlyPowered(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 != 0 && par1World.getIndirectPowerOutput(par2, par3 - 1, par4, 0))
        {
            return true;
        }

        if (par5 != 1 && par1World.getIndirectPowerOutput(par2, par3 + 1, par4, 1))
        {
            return true;
        }

        if (par5 != 2 && par1World.getIndirectPowerOutput(par2, par3, par4 - 1, 2))
        {
            return true;
        }

        if (par5 != 3 && par1World.getIndirectPowerOutput(par2, par3, par4 + 1, 3))
        {
            return true;
        }

        if (par5 != 5 && par1World.getIndirectPowerOutput(par2 + 1, par3, par4, 5))
        {
            return true;
        }

        if (par5 != 4 && par1World.getIndirectPowerOutput(par2 - 1, par3, par4, 4))
        {
            return true;
        }

        if (par1World.getIndirectPowerOutput(par2, par3, par4, 0))
        {
            return true;
        }

        if (par1World.getIndirectPowerOutput(par2, par3 + 2, par4, 1))
        {
            return true;
        }

        if (par1World.getIndirectPowerOutput(par2, par3 + 1, par4 - 1, 2))
        {
            return true;
        }

        if (par1World.getIndirectPowerOutput(par2, par3 + 1, par4 + 1, 3))
        {
            return true;
        }

        if (par1World.getIndirectPowerOutput(par2 - 1, par3 + 1, par4, 4))
        {
            return true;
        }

        return par1World.getIndirectPowerOutput(par2 + 1, par3 + 1, par4, 5);
    }

    /**
     * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
     * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
     */
    public boolean onBlockEventReceived(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        if (dupe){
            powerBlock(par1World, par2, par3, par4, par5, par6);
            return false;
        }
        if (!par1World.isRemote)
        {
            boolean flag = isIndirectlyPowered(par1World, par2, par3, par4, par6);

            if (flag && par5 == 1)
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 2);
                return false;
            }

            if (!flag && par5 == 0)
            {
                return false;
            }
        }

        if (par5 == 0)
        {
            if (tryExtend(par1World, par2, par3, par4, par6))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, par6 | 8, 2);
                par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.out", 0.5F, par1World.rand.nextFloat() * 0.25F + 0.6F);
            }
            else
            {
                return false;
            }
        }
        else if (par5 == 1)
        {
            TileEntity tileentity = par1World.getBlockTileEntity(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);

            if (tileentity instanceof TileEntityPiston)
            {
                ((TileEntityPiston)tileentity).clearPistonTileEntity();
            }

            par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, par6, 3);
            par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(blockID, par6, par6, false, true));

            if (isSticky)
            {
                int i = par2 + Facing.offsetsXForSide[par6] * 2;
                int j = par3 + Facing.offsetsYForSide[par6] * 2;
                int k = par4 + Facing.offsetsZForSide[par6] * 2;
                int l = par1World.getBlockId(i, j, k);
                int i1 = par1World.getBlockMetadata(i, j, k);
                boolean flag1 = false;

                if (l == Block.pistonMoving.blockID)
                {
                    TileEntity tileentity1 = par1World.getBlockTileEntity(i, j, k);

                    if (tileentity1 instanceof TileEntityPiston)
                    {
                        TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity1;

                        if (tileentitypiston.getPistonOrientation() == par6 && tileentitypiston.isExtending())
                        {
                            tileentitypiston.clearPistonTileEntity();
                            l = tileentitypiston.getStoredBlockID();
                            i1 = tileentitypiston.getBlockMetadata();
                            flag1 = true;
                        }
                    }
                }

                if (!flag1 && l > 0 && canPushBlock(l, par1World, i, j, k, false) && (Block.blocksList[l].getMobilityFlag() == 0 || l == Block.pistonBase.blockID || l == Block.pistonStickyBase.blockID))
                {
                    par2 += Facing.offsetsXForSide[par6];
                    par3 += Facing.offsetsYForSide[par6];
                    par4 += Facing.offsetsZForSide[par6];
                    par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, i1, 3);
                    par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(l, i1, par6, false, false));
                    par1World.setBlockToAir(i, j, k);
                }
                else if (!flag1)
                {
                    par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);
                }
            }
            else
            {
                par1World.setBlockToAir(par2 + Facing.offsetsXForSide[par6], par3 + Facing.offsetsYForSide[par6], par4 + Facing.offsetsZForSide[par6]);
            }

            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.in", 0.5F, par1World.rand.nextFloat() * 0.15F + 0.6F);
        }

        return true;
    }

    public void powerBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        ignoreUpdates = true;
        int i = par6;

        if (par5 == 0)
        {
            if (tryExtend(par1World, par2, par3, par4, i))
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i | 8, 3);
                par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.5D, (double)par4 + 0.5D, "tile.piston.out", 0.5F, par1World.rand.nextFloat() * 0.25F + 0.6F);
            }
            else
            {
                par1World.setBlockMetadataWithNotify(par2, par3, par4, i, 0);
            }
        }
        else if (par5 == 1)
        {
            TileEntity tileentity = par1World.getBlockTileEntity(par2 + Facing.offsetsXForSide[i], par3 + Facing.offsetsYForSide[i], par4 + Facing.offsetsZForSide[i]);

            if (tileentity != null && (tileentity instanceof TileEntityPiston))
            {
                ((TileEntityPiston)tileentity).clearPistonTileEntity();
            }

            par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, i, 0);
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

                if (((i1 > 0 && (flag || canPushBlock(i1, par1World, j, k, l, false)) && dupe) || (!flag && i1 > 0 && canPushBlock(i1, par1World, j, k, l, false) && !dupe)) && (Block.blocksList[i1].getMobilityFlag() == 0 || i1 == Block.pistonBase.blockID || i1 == Block.pistonStickyBase.blockID))
                {
                    par2 += Facing.offsetsXForSide[i];
                    par3 += Facing.offsetsYForSide[i];
                    par4 += Facing.offsetsZForSide[i];
                    par1World.setBlock(par2, par3, par4, Block.pistonMoving.blockID, j1, 0);
                    par1World.setBlockTileEntity(par2, par3, par4, BlockPistonMoving.getTileEntity(i1, j1, i, false, false));
                    ignoreUpdates = false;
                    par1World.setBlockToAir(j, k, l);
                    ignoreUpdates = true;
                }
                else if (!flag)
                {
                    ignoreUpdates = false;
                    par1World.setBlockToAir(par2 + Facing.offsetsXForSide[i], par3 + Facing.offsetsYForSide[i], par4 + Facing.offsetsZForSide[i]);
                    ignoreUpdates = true;
                }
            }
            else
            {
                ignoreUpdates = false;
                par1World.setBlockToAir(par2 + Facing.offsetsXForSide[i], par3 + Facing.offsetsYForSide[i], par4 + Facing.offsetsZForSide[i]);
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
            float f = 0.25F;

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
     * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
     * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
     */
    public void addCollisionBoxesToList(World par1World, int par2, int par3, int par4, AxisAlignedBB par5AxisAlignedBB, List par6List, Entity par7Entity)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        super.addCollisionBoxesToList(par1World, par2, par3, par4, par5AxisAlignedBB, par6List, par7Entity);
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
    public static int determineOrientation(World par0World, int par1, int par2, int par3, EntityLivingBase par4EntityLivingBase)
    {
        if (MathHelper.abs((float)par4EntityLivingBase.posX - (float)par1) < 2.0F && MathHelper.abs((float)par4EntityLivingBase.posZ - (float)par3) < 2.0F)
        {
            double d = (par4EntityLivingBase.posY + 1.8200000000000001D) - (double)par4EntityLivingBase.yOffset;

            if (d - (double)par2 > 2D)
            {
                return 1;
            }

            if ((double)par2 - d > 0.0D)
            {
                return 0;
            }
        }

        int i = MathHelper.floor_double((double)((par4EntityLivingBase.rotationYaw * 4F) / 360F) + 0.5D) & 3;

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
            if (Block.blocksList[par0].getBlockHardness(par1World, par2, par3, par4) == -1F)
            {
                return false;
            }

            if (Block.blocksList[par0].getMobilityFlag() == 2)
            {
                return false;
            }

            if (Block.blocksList[par0].getMobilityFlag() == 1)
            {
                return par5;
            }
        }

        return !(Block.blocksList[par0] instanceof ITileEntityProvider);
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

            int i1 = par1World.getBlockId(i, j, k);

            if (i1 == 0)
            {
                break;
            }

            if (!canPushBlock(i1, par1World, i, j, k, true))
            {
                return false;
            }

            if (Block.blocksList[i1].getMobilityFlag() == 1)
            {
                Block.blocksList[i1].dropBlockAsItem(par1World, i, j, k, par1World.getBlockMetadata(i, j, k), 0);
                par1World.setBlockToAir(i, j, k);
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

        l = i;
        int j1 = j;
        int k1 = k;
        int l1 = 0;
        int ai[] = new int[13];
        int i3;

        for (; i != par2 || j != par3 || k != par4; k = i3)
        {
            int i2 = i - Facing.offsetsXForSide[par5];
            int k2 = j - Facing.offsetsYForSide[par5];
            i3 = k - Facing.offsetsZForSide[par5];
            int k3 = par1World.getBlockId(i2, k2, i3);
            int l3 = par1World.getBlockMetadata(i2, k2, i3);

            if (k3 == blockID && i2 == par2 && k2 == par3 && i3 == par4)
            {
                par1World.setBlock(i, j, k, Block.pistonMoving.blockID, par5 | (isSticky ? 8 : 0), 4);
                par1World.setBlockTileEntity(i, j, k, BlockPistonMoving.getTileEntity(Block.pistonExtension.blockID, par5 | (isSticky ? 8 : 0), par5, true, false));
            }
            else
            {
                par1World.setBlock(i, j, k, Block.pistonMoving.blockID, l3, 4);
                par1World.setBlockTileEntity(i, j, k, BlockPistonMoving.getTileEntity(k3, l3, par5, true, false));
            }

            ai[l1++] = k3;
            i = i2;
            j = k2;
        }

        i = l;
        j = j1;
        k = k1;
        l1 = 0;
        int j3;

        for (; i != par2 || j != par3 || k != par4; k = j3)
        {
            int j2 = i - Facing.offsetsXForSide[par5];
            int l2 = j - Facing.offsetsYForSide[par5];
            j3 = k - Facing.offsetsZForSide[par5];
            par1World.notifyBlocksOfNeighborChange(j2, l2, j3, ai[l1++]);
            i = j2;
            j = l2;
        }

        return true;
    }
}
