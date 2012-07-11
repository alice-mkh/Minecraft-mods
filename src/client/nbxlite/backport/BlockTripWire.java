package net.minecraft.src.backport;

import java.util.List;
import java.util.Random;
import net.minecraft.src.*;

public class BlockTripWire extends Block
{
    public BlockTripWire(int par1)
    {
        super(par1, 173, Material.circuits);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.15625F, 1.0F);
        setTickRandomly(true);
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate()
    {
        return 10;
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
     * Returns which pass should this block be rendered on. 0 for solids and 1 for alpha
     */
    public int getRenderBlockPass()
    {
        return 1;
    }

    /**
     * The type of render function that is called for this block
     */
    public int getRenderType()
    {
        return mod_noBiomesX.wireRenderID;
    }

    /**
     * Returns the ID of the items to drop on destruction.
     */
    public int idDropped(int par1, Random par2Random, int par3)
    {
        return Item.silk.shiftedIndex;
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (i & 2) == 2;
        boolean flag1 = !func_58081_i(par2, par3 - 1, par4, par1World);

        if (flag != flag1)
        {
            dropBlockAsItem(par1World, par2, par3, par4, i, 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
        }
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = (i & 4) == 4;
        boolean flag1 = (i & 2) == 2;

        if (!flag1)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.09375F, 1.0F);
        }
        else if (!flag)
        {
            setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
        }
        else
        {
            setBlockBounds(0.0F, 0.0625F, 0.0F, 1.0F, 0.15625F, 1.0F);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        byte byte0 = ((byte)(func_58081_i(par2, par3 - 1, par4, par1World) ? 0 : 2));
        par1World.setBlockMetadataWithNotify(par2, par3, par4, byte0);
        func_56790_e(par1World, par2, par3, par4, byte0);
    }

    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
    {
        int par6 = par1World.getBlockMetadata(par2, par3, par4);
        func_56790_e(par1World, par2, par3, par4, par6 | 1);
    }

    public boolean blockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par6EntityPlayer)
    {
        int par5 = par1World.getBlockMetadata(par2, par3, par4);
        if (par1World.isRemote)
        {
            return false;
        }

        if (par6EntityPlayer.getCurrentEquippedItem() != null && par6EntityPlayer.getCurrentEquippedItem().itemID == Item.shears.shiftedIndex)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par5 | 8);
            return true;
        }
        return super.blockActivated(par1World, par2, par3, par4, par6EntityPlayer);
    }

    private void func_56790_e(World par1World, int par2, int par3, int par4, int par5)
    {
        for (int i = 0; i < 2; i++)
        {
            for (int j = 1; j < 42; j++)
            {
                int k = par2 + Direction.offsetX[i] * j;
                int l = par4 + Direction.offsetZ[i] * j;
                int i1 = par1World.getBlockId(k, par3, l);

                if (i1 == 131)
                {
                    ((BlockTripWireSource)Block.blocksList[131]).func_56799_a(par1World, k, par3, l, i1, par1World.getBlockMetadata(k, par3, l), true, j, par5);
                    continue;
                }

                if (i1 != 132)
                {
                    break;
                }
            }
        }
    }

    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        if (par1World.isRemote)
        {
            return;
        }

        if ((par1World.getBlockMetadata(par2, par3, par4) & 1) == 1)
        {
            return;
        }
        else
        {
            func_56789_j(par1World, par2, par3, par4);
            return;
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

        if ((par1World.getBlockMetadata(par2, par3, par4) & 1) != 1)
        {
            return;
        }
        else
        {
            func_56789_j(par1World, par2, par3, par4);
            return;
        }
    }

    private void func_56789_j(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (i & 1) == 1;
        boolean flag1 = false;
        List list = par1World.getEntitiesWithinAABBExcludingEntity(null, AxisAlignedBB.getBoundingBoxFromPool((double)par2 + minX, (double)par3 + minY, (double)par4 + minZ, (double)par2 + maxX, (double)par3 + maxY, (double)par4 + maxZ));

        if (!list.isEmpty())
        {
            flag1 = true;
        }

        if (flag1 && !flag)
        {
            i |= 1;
        }

        if (!flag1 && flag)
        {
            i &= -2;
        }

        if (flag1 != flag)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
            func_56790_e(par1World, par2, par3, par4, i);
        }

        if (flag1)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate());
        }
    }

    public static boolean func_56788_a(IBlockAccess par0IBlockAccess, int par1, int par2, int par3, int par4, int par5)
    {
        int i = par1 + Direction.offsetX[par5];
        int j = par2;
        int k = par3 + Direction.offsetZ[par5];
        int l = par0IBlockAccess.getBlockId(i, j, k);
        boolean flag = (par4 & 2) == 2;

        if (l == Block.blocksList[131].blockID)
        {
            int i1 = par0IBlockAccess.getBlockMetadata(i, j, k);
            int k1 = i1 & 3;
            return k1 == Direction.footInvisibleFaceRemap[par5];
        }

        if (l == Block.blocksList[132].blockID)
        {
            int j1 = par0IBlockAccess.getBlockMetadata(i, j, k);
            boolean flag1 = (j1 & 2) == 2;
            return flag == flag1;
        }
        else
        {
            return false;
        }
    }

    public static boolean func_58081_i(int par1, int par2, int par3, World par4World)
    {
        Block block = Block.blocksList[par4World.getBlockId(par1, par2, par3)];

        if (block == null)
        {
            return false;
        }

        if (block.blockMaterial.isOpaque() && block.renderAsNormalBlock())
        {
            return true;
        }

        if (block instanceof BlockStairs)
        {
            return (par4World.getBlockMetadata(par1, par2, par3) & 4) == 4;
        }

        if (block instanceof BlockStep)
        {
            return (par4World.getBlockMetadata(par1, par2, par3) & 8) == 8;
        }
        else
        {
            return false;
        }
    }

    public static boolean renderBlockWire(RenderBlocks r, IBlockAccess blockAccess, Block b, int par2, int par3, int par4)
    {
        Block par1Block = b;
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSide(0);
        int j = blockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = (j & 4) == 4;
        boolean flag1 = (j & 2) == 2;

        if (r.overrideBlockTexture >= 0)
        {
            i = r.overrideBlockTexture;
        }

        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        float f = par1Block.getBlockBrightness(blockAccess, par2, par3, par4) * 0.75F;
        tessellator.setColorOpaque_F(f, f, f);
        int k = (i & 0xf) << 4;
        int l = i & 0xf0;
        double d = (float)k / 256F;
        double d1 = (float)(k + 16) / 256F;
        double d2 = (float)(l + (flag ? 2 : 0)) / 256F;
        double d3 = (float)(l + (flag ? 4 : 2)) / 256F;
        double d4 = (double)(flag1 ? 3.5F : 1.5F) / 16D;
        boolean flag2 = func_56788_a(blockAccess, par2, par3, par4, j, 1);
        boolean flag3 = func_56788_a(blockAccess, par2, par3, par4, j, 3);
        boolean flag4 = func_56788_a(blockAccess, par2, par3, par4, j, 2);
        boolean flag5 = func_56788_a(blockAccess, par2, par3, par4, j, 0);
        float f1 = 0.03125F;
        float f2 = 0.5F - f1 / 2.0F;
        float f3 = f2 + f1;

        if (!flag4 && !flag3 && !flag5 && !flag2)
        {
            flag4 = true;
            flag5 = true;
        }

        if (flag4)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d, d2);
        }

        if (flag4 || flag5 && !flag3 && !flag2)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d, d2);
        }

        if (flag5 || flag4 && !flag3 && !flag2)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d, d2);
        }

        if (flag5)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4 + 1, d, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4 + 1, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d1, d2);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d1, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4 + 1, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4 + 1, d, d2);
        }

        if (flag2)
        {
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        if (flag2 || flag3 && !flag4 && !flag5)
        {
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        if (flag3 || flag2 && !flag4 && !flag5)
        {
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        if (flag3)
        {
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d, d2);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f2, d1, d2);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f3, d1, d3);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        return true;
    }
}
