package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

public class BlockTripWireSource extends Block
{
    public BlockTripWireSource(int par1)
    {
        super(par1, 172, Material.circuits);
        setTickRandomly(true);
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
        return mod_noBiomesX.hookRenderID;
    }

    /**
     * How many world ticks before ticking
     */
    public int tickRate()
    {
        return 10;
    }

    /**
     * checks to see if you can place this block can be placed on that side of a block: BlockLever overrides
     */
    public boolean canPlaceBlockOnSide(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 == 2 && par1World.isBlockNormalCube(par2, par3, par4 + 1))
        {
            return true;
        }

        if (par5 == 3 && par1World.isBlockNormalCube(par2, par3, par4 - 1))
        {
            return true;
        }

        if (par5 == 4 && par1World.isBlockNormalCube(par2 + 1, par3, par4))
        {
            return true;
        }

        return par5 == 5 && par1World.isBlockNormalCube(par2 - 1, par3, par4);
    }

    /**
     * Checks to see if its valid to put this block at the specified coordinates. Args: world, x, y, z
     */
    public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4)
    {
        if (par1World.isBlockNormalCube(par2 - 1, par3, par4))
        {
            return true;
        }

        if (par1World.isBlockNormalCube(par2 + 1, par3, par4))
        {
            return true;
        }

        if (par1World.isBlockNormalCube(par2, par3, par4 - 1))
        {
            return true;
        }

        return par1World.isBlockNormalCube(par2, par3, par4 + 1);
    }

    public void onBlockPlaced(World par1World, int par2, int par3, int par4, int par5)
    {
        byte byte0 = 0;

        if (par5 == 2 && par1World.isBlockNormalCubeDefault(par2, par3, par4 + 1, true))
        {
            byte0 = 2;
        }

        if (par5 == 3 && par1World.isBlockNormalCubeDefault(par2, par3, par4 - 1, true))
        {
            byte0 = 0;
        }

        if (par5 == 4 && par1World.isBlockNormalCubeDefault(par2 + 1, par3, par4, true))
        {
            byte0 = 1;
        }

        if (par5 == 5 && par1World.isBlockNormalCubeDefault(par2 - 1, par3, par4, true))
        {
            byte0 = 3;
        }

        func_56799_a(par1World, par2, par3, par4, blockID, byte0, false, -1, 0);
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor blockID
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5)
    {
        if (par5 == blockID)
        {
            return;
        }

        if (func_56800_j(par1World, par2, par3, par4))
        {
            int i = par1World.getBlockMetadata(par2, par3, par4);
            int j = i & 3;
            boolean flag = false;

            if (!par1World.isBlockNormalCube(par2 - 1, par3, par4) && j == 3)
            {
                flag = true;
            }

            if (!par1World.isBlockNormalCube(par2 + 1, par3, par4) && j == 1)
            {
                flag = true;
            }

            if (!par1World.isBlockNormalCube(par2, par3, par4 - 1) && j == 0)
            {
                flag = true;
            }

            if (!par1World.isBlockNormalCube(par2, par3, par4 + 1) && j == 2)
            {
                flag = true;
            }

            if (flag)
            {
                dropBlockAsItem(par1World, par2, par3, par4, i, 0);
                par1World.setBlockWithNotify(par2, par3, par4, 0);
            }
        }
    }

    public void func_56799_a(World par1World, int par2, int par3, int par4, int par5, int par6, boolean par7, int par8, int par9)
    {
        int i = par6 & 3;
        boolean flag = (par6 & 4) == 4;
        boolean flag1 = (par6 & 8) == 8;
        boolean flag2 = par5 == Block.blocksList[131].blockID;
        boolean flag3 = false;
        boolean flag4 = !func_58081_i(par2, par3 - 1, par4, par1World);
        int j = Direction.offsetX[i];
        int k = Direction.offsetZ[i];
        int l = 0;
        int ai[] = new int[42];

        for (int i1 = 1; i1 < 42; i1++)
        {
            int k1 = par2 + j * i1;
            int j2 = par4 + k * i1;
            int i3 = par1World.getBlockId(k1, par3, j2);

            if (i3 == 131)
            {
                int l3 = par1World.getBlockMetadata(k1, par3, j2);

                if ((l3 & 3) == Direction.footInvisibleFaceRemap[i])
                {
                    l = i1;
                }

                break;
            }

            if (i3 == 132 || i1 == par8)
            {
                int i4 = i1 != par8 ? par1World.getBlockMetadata(k1, par3, j2) : par9;
                boolean flag5 = (i4 & 8) != 8;
                boolean flag6 = (i4 & 1) == 1;
                boolean flag7 = (i4 & 2) == 2;
                flag2 &= flag7 == flag4;
                flag3 |= flag5 && flag6;
                ai[i1] = i4;

                if (i1 == par8)
                {
                    par1World.scheduleBlockUpdate(par2, par3, par4, par5, tickRate());
                    flag2 &= flag5;
                }
            }
            else
            {
                ai[i1] = -1;
                flag2 = false;
            }
        }

        flag2 &= l > 1;
        flag3 &= flag2;
        int j1 = (flag2 ? 4 : 0) | (flag3 ? 8 : 0);
        par6 = i | j1;

        if (l > 0)
        {
            int l1 = par2 + j * l;
            int k2 = par4 + k * l;
            int j3 = Direction.footInvisibleFaceRemap[i];
            par1World.setBlockMetadataWithNotify(l1, par3, k2, j3 | j1);
            func_56801_e(par1World, l1, par3, k2, j3);
            func_56802_a(par1World, l1, par3, k2, flag2, flag3, flag, flag1);
        }

        func_56802_a(par1World, par2, par3, par4, flag2, flag3, flag, flag1);

        if (par5 > 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, par6);

            if (par7)
            {
                func_56801_e(par1World, par2, par3, par4, i);
            }
        }

        if (flag != flag2)
        {
            for (int i2 = 1; i2 < l; i2++)
            {
                int l2 = par2 + j * i2;
                int k3 = par4 + k * i2;
                int j4 = ai[i2];

                if (j4 < 0)
                {
                    continue;
                }

                if (flag2)
                {
                    j4 |= 4;
                }
                else
                {
                    j4 &= -5;
                }

                par1World.setBlockMetadataWithNotify(l2, par3, k3, j4);
            }
        }
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        func_56799_a(par1World, par2, par3, par4, blockID, par1World.getBlockMetadata(par2, par3, par4), true, -1, 0);
    }

    private void func_56802_a(World par1World, int par2, int par3, int par4, boolean par5, boolean par6, boolean par7, boolean par8)
    {
        if (par6 && !par8)
        {
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.10000000000000001D, (double)par4 + 0.5D, "random.click", 0.4F, 0.6F);
        }
        else if (!par6 && par8)
        {
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.10000000000000001D, (double)par4 + 0.5D, "random.click", 0.4F, 0.5F);
        }
        else if (par5 && !par7)
        {
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.10000000000000001D, (double)par4 + 0.5D, "random.click", 0.4F, 0.7F);
        }
        else if (!par5 && par7)
        {
            par1World.playSoundEffect((double)par2 + 0.5D, (double)par3 + 0.10000000000000001D, (double)par4 + 0.5D, "random.bowhit", 0.4F, 1.2F / (par1World.rand.nextFloat() * 0.2F + 0.9F));
        }
    }

    private void func_56801_e(World par1World, int par2, int par3, int par4, int par5)
    {
        par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);

        if (par5 == 3)
        {
            par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, blockID);
        }
        else if (par5 == 1)
        {
            par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, blockID);
        }
        else if (par5 == 0)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, blockID);
        }
        else if (par5 == 2)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, blockID);
        }
    }

    private boolean func_56800_j(World par1World, int par2, int par3, int par4)
    {
        if (!canPlaceBlockAt(par1World, par2, par3, par4))
        {
            dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockWithNotify(par2, par3, par4, 0);
            return false;
        }
        else
        {
            return true;
        }
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        int i = par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 3;
        float f = 0.1875F;

        if (i == 3)
        {
            setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
        }
        else if (i == 1)
        {
            setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
        }
        else if (i == 0)
        {
            setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
        }
        else if (i == 2)
        {
            setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
        }
    }

    public void onBlockRemoval(World par1World, int par2, int par3, int par4)
    {
        int par6 = par1World.getBlockMetadata(par2, par3, par4);
        boolean flag = (par6 & 4) == 4;
        boolean flag1 = (par6 & 8) == 8;

        if (flag || flag1)
        {
            func_56799_a(par1World, par2, par3, par4, 0, par6, false, -1, 0);
        }

        if (flag1)
        {
            par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
            int i = par6 & 3;

            if (i == 3)
            {
                par1World.notifyBlocksOfNeighborChange(par2 - 1, par3, par4, blockID);
            }
            else if (i == 1)
            {
                par1World.notifyBlocksOfNeighborChange(par2 + 1, par3, par4, blockID);
            }
            else if (i == 0)
            {
                par1World.notifyBlocksOfNeighborChange(par2, par3, par4 - 1, blockID);
            }
            else if (i == 2)
            {
                par1World.notifyBlocksOfNeighborChange(par2, par3, par4 + 1, blockID);
            }
        }

        super.onBlockRemoval(par1World, par2, par3, par4);
    }

    /**
     * Is this block powering the block on the specified side
     */
    public boolean isPoweringTo(IBlockAccess par1IBlockAccess, int par2, int par3, int par4, int par5)
    {
        return (par1IBlockAccess.getBlockMetadata(par2, par3, par4) & 8) == 8;
    }

    /**
     * Is this block indirectly powering the block on the specified side
     */
    public boolean isIndirectlyPoweringTo(World par1World, int par2, int par3, int par4, int par5)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);

        if ((i & 8) != 8)
        {
            return false;
        }

        int j = i & 3;

        if (j == 2 && par5 == 2)
        {
            return true;
        }

        if (j == 0 && par5 == 3)
        {
            return true;
        }

        if (j == 1 && par5 == 4)
        {
            return true;
        }

        return j == 3 && par5 == 5;
    }

    /**
     * Can this block provide power. Only wire currently seems to have this change based on its state.
     */
    public boolean canProvidePower()
    {
        return true;
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

    public static boolean renderBlockWireHook(RenderBlocks r, IBlockAccess blockAccess, Block b, int par2, int par3, int par4)
    {
        Block par1Block = b;
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        boolean flag = (i & 4) == 4;
        boolean flag1 = (i & 8) == 8;
        boolean flag2 = func_58081_i(par2, par3 - 1, par4, ModLoader.getMinecraftInstance().theWorld);
        boolean flag3 = r.overrideBlockTexture >= 0;

        if (!flag3)
        {
            r.overrideBlockTexture = Block.planks.blockIndexInTexture;
        }

        float f = 0.25F;
        float f1 = 0.125F;
        float f2 = 0.125F;
        float f3 = 0.3F - f;
        float f4 = 0.3F + f;

        if (j == 2)
        {
            par1Block.setBlockBounds(0.5F - f1, f3, 1.0F - f2, 0.5F + f1, f4, 1.0F);
        }
        else if (j == 0)
        {
            par1Block.setBlockBounds(0.5F - f1, f3, 0.0F, 0.5F + f1, f4, f2);
        }
        else if (j == 1)
        {
            par1Block.setBlockBounds(1.0F - f2, f3, 0.5F - f1, 1.0F, f4, 0.5F + f1);
        }
        else if (j == 3)
        {
            par1Block.setBlockBounds(0.0F, f3, 0.5F - f1, f2, f4, 0.5F + f1);
        }

        r.renderStandardBlock(par1Block, par2, par3, par4);

        if (!flag3)
        {
            r.overrideBlockTexture = -1;
        }

        tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        float f5 = 1.0F;

        if (Block.lightValue[par1Block.blockID] > 0)
        {
            f5 = 1.0F;
        }

        tessellator.setColorOpaque_F(f5, f5, f5);
        int k = par1Block.getBlockTextureFromSide(0);

        if (r.overrideBlockTexture >= 0)
        {
            k = r.overrideBlockTexture;
        }

        int l = (k & 0xf) << 4;
        int i1 = k & 0xf0;
        float f7 = (float)l / 256F;
        float f8 = ((float)l + 15.99F) / 256F;
        float f9 = (float)i1 / 256F;
        float f10 = ((float)i1 + 15.99F) / 256F;
        Vec3D avec3[] = new Vec3D[8];
        float f11 = 0.046875F;
        float f12 = 0.046875F;
        float f13 = 0.3125F;
        avec3[0] = Vec3D.createVector(-f11, 0.0D, -f12);
        avec3[1] = Vec3D.createVector(f11, 0.0D, -f12);
        avec3[2] = Vec3D.createVector(f11, 0.0D, f12);
        avec3[3] = Vec3D.createVector(-f11, 0.0D, f12);
        avec3[4] = Vec3D.createVector(-f11, f13, -f12);
        avec3[5] = Vec3D.createVector(f11, f13, -f12);
        avec3[6] = Vec3D.createVector(f11, f13, f12);
        avec3[7] = Vec3D.createVector(-f11, f13, f12);

        for (int j1 = 0; j1 < 8; j1++)
        {
            avec3[j1].zCoord += 0.0625D;

            if (flag1)
            {
                avec3[j1].rotateAroundX(0.5235988F);
                avec3[j1].yCoord -= 0.4375D;
            }
            else if (flag)
            {
                avec3[j1].rotateAroundX(0.08726647F);
                avec3[j1].yCoord -= 0.4375D;
            }
            else
            {
                avec3[j1].rotateAroundX(-((float)Math.PI * 2F / 9F));
                avec3[j1].yCoord -= 0.375D;
            }

            avec3[j1].rotateAroundX(((float)Math.PI / 2F));

            if (j == 2)
            {
                avec3[j1].rotateAroundY(0.0F);
            }

            if (j == 0)
            {
                avec3[j1].rotateAroundY((float)Math.PI);
            }

            if (j == 1)
            {
                avec3[j1].rotateAroundY(((float)Math.PI / 2F));
            }

            if (j == 3)
            {
                avec3[j1].rotateAroundY(-((float)Math.PI / 2F));
            }

            avec3[j1].xCoord += (double)par2 + 0.5D;
            avec3[j1].yCoord += (float)par3 + 0.3125F;
            avec3[j1].zCoord += (double)par4 + 0.5D;
        }

        Vec3D vec3 = null;
        Vec3D vec3_1 = null;
        Vec3D vec3_2 = null;
        Vec3D vec3_3 = null;
        byte byte0 = 7;
        byte byte1 = 9;
        byte byte2 = 9;
        byte byte3 = 16;

        for (int k1 = 0; k1 < 6; k1++)
        {
            if (k1 == 0)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[1];
                vec3_2 = avec3[2];
                vec3_3 = avec3[3];
                f7 = (float)(l + byte0) / 256F;
                f8 = (float)(l + byte1) / 256F;
                f9 = (float)(i1 + byte2) / 256F;
                f10 = (float)(i1 + byte2 + 2) / 256F;
            }
            else if (k1 == 1)
            {
                vec3 = avec3[7];
                vec3_1 = avec3[6];
                vec3_2 = avec3[5];
                vec3_3 = avec3[4];
            }
            else if (k1 == 2)
            {
                vec3 = avec3[1];
                vec3_1 = avec3[0];
                vec3_2 = avec3[4];
                vec3_3 = avec3[5];
                f7 = (float)(l + byte0) / 256F;
                f8 = (float)(l + byte1) / 256F;
                f9 = (float)(i1 + byte2) / 256F;
                f10 = (float)(i1 + byte3) / 256F;
            }
            else if (k1 == 3)
            {
                vec3 = avec3[2];
                vec3_1 = avec3[1];
                vec3_2 = avec3[5];
                vec3_3 = avec3[6];
            }
            else if (k1 == 4)
            {
                vec3 = avec3[3];
                vec3_1 = avec3[2];
                vec3_2 = avec3[6];
                vec3_3 = avec3[7];
            }
            else if (k1 == 5)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[3];
                vec3_2 = avec3[7];
                vec3_3 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, f7, f10);
            tessellator.addVertexWithUV(vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, f8, f10);
            tessellator.addVertexWithUV(vec3_2.xCoord, vec3_2.yCoord, vec3_2.zCoord, f8, f9);
            tessellator.addVertexWithUV(vec3_3.xCoord, vec3_3.yCoord, vec3_3.zCoord, f7, f9);
        }

        float f14 = 0.09375F;
        float f15 = 0.09375F;
        float f16 = 0.03125F;
        avec3[0] = Vec3D.createVector(-f14, 0.0D, -f15);
        avec3[1] = Vec3D.createVector(f14, 0.0D, -f15);
        avec3[2] = Vec3D.createVector(f14, 0.0D, f15);
        avec3[3] = Vec3D.createVector(-f14, 0.0D, f15);
        avec3[4] = Vec3D.createVector(-f14, f16, -f15);
        avec3[5] = Vec3D.createVector(f14, f16, -f15);
        avec3[6] = Vec3D.createVector(f14, f16, f15);
        avec3[7] = Vec3D.createVector(-f14, f16, f15);

        for (int l1 = 0; l1 < 8; l1++)
        {
            avec3[l1].zCoord += 0.21875D;

            if (flag1)
            {
                avec3[l1].yCoord -= 0.09375D;
                avec3[l1].zCoord -= 0.16250000000000001D;
                avec3[l1].rotateAroundX(0.0F);
            }
            else if (flag)
            {
                avec3[l1].yCoord += 0.015625D;
                avec3[l1].zCoord -= 0.171875D;
                avec3[l1].rotateAroundX(0.1745329F);
            }
            else
            {
                avec3[l1].rotateAroundX(0.8726646F);
            }

            if (j == 2)
            {
                avec3[l1].rotateAroundY(0.0F);
            }

            if (j == 0)
            {
                avec3[l1].rotateAroundY((float)Math.PI);
            }

            if (j == 1)
            {
                avec3[l1].rotateAroundY(((float)Math.PI / 2F));
            }

            if (j == 3)
            {
                avec3[l1].rotateAroundY(-((float)Math.PI / 2F));
            }

            avec3[l1].xCoord += (double)par2 + 0.5D;
            avec3[l1].yCoord += (float)par3 + 0.3125F;
            avec3[l1].zCoord += (double)par4 + 0.5D;
        }

        byte byte4 = 5;
        byte byte5 = 11;
        byte byte6 = 3;
        byte byte7 = 9;

        for (int i2 = 0; i2 < 6; i2++)
        {
            if (i2 == 0)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[1];
                vec3_2 = avec3[2];
                vec3_3 = avec3[3];
                f7 = (float)(l + byte4) / 256F;
                f8 = (float)(l + byte5) / 256F;
                f9 = (float)(i1 + byte6) / 256F;
                f10 = (float)(i1 + byte7) / 256F;
            }
            else if (i2 == 1)
            {
                vec3 = avec3[7];
                vec3_1 = avec3[6];
                vec3_2 = avec3[5];
                vec3_3 = avec3[4];
            }
            else if (i2 == 2)
            {
                vec3 = avec3[1];
                vec3_1 = avec3[0];
                vec3_2 = avec3[4];
                vec3_3 = avec3[5];
                f7 = (float)(l + byte4) / 256F;
                f8 = (float)(l + byte5) / 256F;
                f9 = (float)(i1 + byte6) / 256F;
                f10 = (float)(i1 + byte6 + 2) / 256F;
            }
            else if (i2 == 3)
            {
                vec3 = avec3[2];
                vec3_1 = avec3[1];
                vec3_2 = avec3[5];
                vec3_3 = avec3[6];
            }
            else if (i2 == 4)
            {
                vec3 = avec3[3];
                vec3_1 = avec3[2];
                vec3_2 = avec3[6];
                vec3_3 = avec3[7];
            }
            else if (i2 == 5)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[3];
                vec3_2 = avec3[7];
                vec3_3 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, f7, f10);
            tessellator.addVertexWithUV(vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, f8, f10);
            tessellator.addVertexWithUV(vec3_2.xCoord, vec3_2.yCoord, vec3_2.zCoord, f8, f9);
            tessellator.addVertexWithUV(vec3_3.xCoord, vec3_3.yCoord, vec3_3.zCoord, f7, f9);
        }

        if (flag)
        {
            double d = avec3[0].yCoord;
            float f17 = 0.03125F;
            float f18 = 0.5F - f17 / 2.0F;
            float f19 = f18 + f17;
            int j2 = (Block.blocksList[132].blockIndexInTexture & 0xf) << 4;
            int k2 = Block.blocksList[132].blockIndexInTexture & 0xf0;
            double d1 = (float)j2 / 256F;
            double d2 = (float)(j2 + 16) / 256F;
            double d3 = (float)(k2 + (flag ? 2 : 0)) / 256F;
            double d4 = (float)(k2 + (flag ? 4 : 2)) / 256F;
            double d5 = (double)(flag2 ? 3.5F : 1.5F) / 16D;
            float f6 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4) * 0.75F;
            tessellator.setColorOpaque_F(f6, f6, f6);

            if (j == 2)
            {
                tessellator.addVertexWithUV((float)par2 + f18, (double)par3 + d5, (double)par4 + 0.25D, d1, d3);
                tessellator.addVertexWithUV((float)par2 + f19, (double)par3 + d5, (double)par4 + 0.25D, d1, d4);
                tessellator.addVertexWithUV((float)par2 + f19, (double)par3 + d5, par4, d2, d4);
                tessellator.addVertexWithUV((float)par2 + f18, (double)par3 + d5, par4, d2, d3);
                tessellator.addVertexWithUV((float)par2 + f18, d, (double)par4 + 0.5D, d1, d3);
                tessellator.addVertexWithUV((float)par2 + f19, d, (double)par4 + 0.5D, d1, d4);
                tessellator.addVertexWithUV((float)par2 + f19, (double)par3 + d5, (double)par4 + 0.25D, d2, d4);
                tessellator.addVertexWithUV((float)par2 + f18, (double)par3 + d5, (double)par4 + 0.25D, d2, d3);
            }
            else if (j == 0)
            {
                tessellator.addVertexWithUV((float)par2 + f18, (double)par3 + d5, (double)par4 + 0.75D, d1, d3);
                tessellator.addVertexWithUV((float)par2 + f19, (double)par3 + d5, (double)par4 + 0.75D, d1, d4);
                tessellator.addVertexWithUV((float)par2 + f19, d, (double)par4 + 0.5D, d2, d4);
                tessellator.addVertexWithUV((float)par2 + f18, d, (double)par4 + 0.5D, d2, d3);
                tessellator.addVertexWithUV((float)par2 + f18, (double)par3 + d5, par4 + 1, d1, d3);
                tessellator.addVertexWithUV((float)par2 + f19, (double)par3 + d5, par4 + 1, d1, d4);
                tessellator.addVertexWithUV((float)par2 + f19, (double)par3 + d5, (double)par4 + 0.75D, d2, d4);
                tessellator.addVertexWithUV((float)par2 + f18, (double)par3 + d5, (double)par4 + 0.75D, d2, d3);
            }
            else if (j == 1)
            {
                tessellator.addVertexWithUV(par2, (double)par3 + d5, (float)par4 + f19, d1, d4);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d5, (float)par4 + f19, d2, d4);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d5, (float)par4 + f18, d2, d3);
                tessellator.addVertexWithUV(par2, (double)par3 + d5, (float)par4 + f18, d1, d3);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d5, (float)par4 + f19, d1, d4);
                tessellator.addVertexWithUV((double)par2 + 0.5D, d, (float)par4 + f19, d2, d4);
                tessellator.addVertexWithUV((double)par2 + 0.5D, d, (float)par4 + f18, d2, d3);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d5, (float)par4 + f18, d1, d3);
            }
            else
            {
                tessellator.addVertexWithUV((double)par2 + 0.5D, d, (float)par4 + f19, d1, d4);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d5, (float)par4 + f19, d2, d4);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d5, (float)par4 + f18, d2, d3);
                tessellator.addVertexWithUV((double)par2 + 0.5D, d, (float)par4 + f18, d1, d3);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d5, (float)par4 + f19, d1, d4);
                tessellator.addVertexWithUV(par2 + 1, (double)par3 + d5, (float)par4 + f19, d2, d4);
                tessellator.addVertexWithUV(par2 + 1, (double)par3 + d5, (float)par4 + f18, d2, d3);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d5, (float)par4 + f18, d1, d3);
            }
        }

        return true;
    }
}
