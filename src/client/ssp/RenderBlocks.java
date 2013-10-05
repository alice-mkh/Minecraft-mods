package net.minecraft.src;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderBlocks
{
    public static boolean oldrotation = false;

    /** The IBlockAccess used by this instance of RenderBlocks */
    public IBlockAccess blockAccess;

    /**
     * If set to >=0, all block faces will be rendered using this texture index
     */
    public Icon overrideBlockTexture;

    /**
     * Set to true if the texture should be flipped horizontally during render*Face
     */
    public boolean flipTexture;

    /**
     * If true, renders all faces on all blocks rather than using the logic in Block.shouldSideBeRendered.  Unused.
     */
    public boolean renderAllFaces;

    /** Fancy grass side matching biome */
    public static boolean fancyGrass = true;
    public static boolean cfgGrassFix = true;
    public boolean useInventoryTint;

    /** The minimum X value for rendering (default 0.0). */
    public double renderMinX;

    /** The maximum X value for rendering (default 1.0). */
    public double renderMaxX;

    /** The minimum Y value for rendering (default 0.0). */
    public double renderMinY;

    /** The maximum Y value for rendering (default 1.0). */
    public double renderMaxY;

    /** The minimum Z value for rendering (default 0.0). */
    public double renderMinZ;

    /** The maximum Z value for rendering (default 1.0). */
    public double renderMaxZ;

    /**
     * Set by overrideBlockBounds, to keep this class from changing the visual bounding box.
     */
    public boolean lockBlockBounds;
    public boolean partialRenderBounds;
    public final Minecraft minecraftRB;
    public int uvRotateEast;
    public int uvRotateWest;
    public int uvRotateSouth;
    public int uvRotateNorth;
    public int uvRotateTop;
    public int uvRotateBottom;

    /** Whether ambient occlusion is enabled or not */
    public boolean enableAO;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/east corner.
     */
    public float aoLightValueScratchXYZNNN;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
     */
    public float aoLightValueScratchXYNN;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/west corner.
     */
    public float aoLightValueScratchXYZNNP;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the east face.
     */
    public float aoLightValueScratchYZNN;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the west face.
     */
    public float aoLightValueScratchYZNP;

    /**
     * Used as a scratch variable for ambient occlusion on the south/bottom/east corner.
     */
    public float aoLightValueScratchXYZPNN;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the south face.
     */
    public float aoLightValueScratchXYPN;

    /**
     * Used as a scratch variable for ambient occlusion on the south/bottom/west corner.
     */
    public float aoLightValueScratchXYZPNP;

    /**
     * Used as a scratch variable for ambient occlusion on the north/top/east corner.
     */
    public float aoLightValueScratchXYZNPN;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the north face.
     */
    public float aoLightValueScratchXYNP;

    /**
     * Used as a scratch variable for ambient occlusion on the north/top/west corner.
     */
    public float aoLightValueScratchXYZNPP;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the east face.
     */
    public float aoLightValueScratchYZPN;

    /**
     * Used as a scratch variable for ambient occlusion on the south/top/east corner.
     */
    public float aoLightValueScratchXYZPPN;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the south face.
     */
    public float aoLightValueScratchXYPP;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the west face.
     */
    public float aoLightValueScratchYZPP;

    /**
     * Used as a scratch variable for ambient occlusion on the south/top/west corner.
     */
    public float aoLightValueScratchXYZPPP;

    /**
     * Used as a scratch variable for ambient occlusion between the north face and the east face.
     */
    public float aoLightValueScratchXZNN;

    /**
     * Used as a scratch variable for ambient occlusion between the south face and the east face.
     */
    public float aoLightValueScratchXZPN;

    /**
     * Used as a scratch variable for ambient occlusion between the north face and the west face.
     */
    public float aoLightValueScratchXZNP;

    /**
     * Used as a scratch variable for ambient occlusion between the south face and the west face.
     */
    public float aoLightValueScratchXZPP;

    /** Ambient occlusion brightness XYZNNN */
    public int aoBrightnessXYZNNN;

    /** Ambient occlusion brightness XYNN */
    public int aoBrightnessXYNN;

    /** Ambient occlusion brightness XYZNNP */
    public int aoBrightnessXYZNNP;

    /** Ambient occlusion brightness YZNN */
    public int aoBrightnessYZNN;

    /** Ambient occlusion brightness YZNP */
    public int aoBrightnessYZNP;

    /** Ambient occlusion brightness XYZPNN */
    public int aoBrightnessXYZPNN;

    /** Ambient occlusion brightness XYPN */
    public int aoBrightnessXYPN;

    /** Ambient occlusion brightness XYZPNP */
    public int aoBrightnessXYZPNP;

    /** Ambient occlusion brightness XYZNPN */
    public int aoBrightnessXYZNPN;

    /** Ambient occlusion brightness XYNP */
    public int aoBrightnessXYNP;

    /** Ambient occlusion brightness XYZNPP */
    public int aoBrightnessXYZNPP;

    /** Ambient occlusion brightness YZPN */
    public int aoBrightnessYZPN;

    /** Ambient occlusion brightness XYZPPN */
    public int aoBrightnessXYZPPN;

    /** Ambient occlusion brightness XYPP */
    public int aoBrightnessXYPP;

    /** Ambient occlusion brightness YZPP */
    public int aoBrightnessYZPP;

    /** Ambient occlusion brightness XYZPPP */
    public int aoBrightnessXYZPPP;

    /** Ambient occlusion brightness XZNN */
    public int aoBrightnessXZNN;

    /** Ambient occlusion brightness XZPN */
    public int aoBrightnessXZPN;

    /** Ambient occlusion brightness XZNP */
    public int aoBrightnessXZNP;

    /** Ambient occlusion brightness XZPP */
    public int aoBrightnessXZPP;

    /** Brightness top left */
    public int brightnessTopLeft;

    /** Brightness bottom left */
    public int brightnessBottomLeft;

    /** Brightness bottom right */
    public int brightnessBottomRight;

    /** Brightness top right */
    public int brightnessTopRight;

    /** Red color value for the top left corner */
    public float colorRedTopLeft;

    /** Red color value for the bottom left corner */
    public float colorRedBottomLeft;

    /** Red color value for the bottom right corner */
    public float colorRedBottomRight;

    /** Red color value for the top right corner */
    public float colorRedTopRight;

    /** Green color value for the top left corner */
    public float colorGreenTopLeft;

    /** Green color value for the bottom left corner */
    public float colorGreenBottomLeft;

    /** Green color value for the bottom right corner */
    public float colorGreenBottomRight;

    /** Green color value for the top right corner */
    public float colorGreenTopRight;

    /** Blue color value for the top left corner */
    public float colorBlueTopLeft;

    /** Blue color value for the bottom left corner */
    public float colorBlueBottomLeft;

    /** Blue color value for the bottom right corner */
    public float colorBlueBottomRight;

    /** Blue color value for the top right corner */
    public float colorBlueTopRight;

    public RenderBlocks(IBlockAccess par1IBlockAccess)
    {
        useInventoryTint = true;
        blockAccess = par1IBlockAccess;
        minecraftRB = Minecraft.getMinecraft();
    }

    public RenderBlocks()
    {
        useInventoryTint = true;
        minecraftRB = Minecraft.getMinecraft();
    }

    /**
     * Sets overrideBlockTexture
     */
    public void setOverrideBlockTexture(Icon par1Icon)
    {
        overrideBlockTexture = par1Icon;
    }

    /**
     * Clear override block texture
     */
    public void clearOverrideBlockTexture()
    {
        overrideBlockTexture = null;
    }

    public boolean hasOverrideBlockTexture()
    {
        return overrideBlockTexture != null;
    }

    /**
     * Sets the bounding box for the block to draw in, e.g. 0.25-0.75 on all axes for a half-size, centered block.
     */
    public void setRenderBounds(double par1, double par3, double par5, double par7, double par9, double par11)
    {
        if (!lockBlockBounds)
        {
            renderMinX = par1;
            renderMaxX = par7;
            renderMinY = par3;
            renderMaxY = par9;
            renderMinZ = par5;
            renderMaxZ = par11;
            partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0.0D || renderMaxX < 1.0D || renderMinY > 0.0D || renderMaxY < 1.0D || renderMinZ > 0.0D || renderMaxZ < 1.0D);
        }
    }

    /**
     * Like setRenderBounds, but automatically pulling the bounds from the given Block.
     */
    public void setRenderBoundsFromBlock(Block par1Block)
    {
        if (!lockBlockBounds)
        {
            renderMinX = par1Block.getBlockBoundsMinX();
            renderMaxX = par1Block.getBlockBoundsMaxX();
            renderMinY = par1Block.getBlockBoundsMinY();
            renderMaxY = par1Block.getBlockBoundsMaxY();
            renderMinZ = par1Block.getBlockBoundsMinZ();
            renderMaxZ = par1Block.getBlockBoundsMaxZ();
            partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0.0D || renderMaxX < 1.0D || renderMinY > 0.0D || renderMaxY < 1.0D || renderMinZ > 0.0D || renderMaxZ < 1.0D);
        }
    }

    /**
     * Like setRenderBounds, but locks the values so that RenderBlocks won't change them.  If you use this, you must
     * call unlockBlockBounds after you finish rendering!
     */
    public void overrideBlockBounds(double par1, double par3, double par5, double par7, double par9, double par11)
    {
        renderMinX = par1;
        renderMaxX = par7;
        renderMinY = par3;
        renderMaxY = par9;
        renderMinZ = par5;
        renderMaxZ = par11;
        lockBlockBounds = true;
        partialRenderBounds = minecraftRB.gameSettings.ambientOcclusion >= 2 && (renderMinX > 0.0D || renderMaxX < 1.0D || renderMinY > 0.0D || renderMaxY < 1.0D || renderMinZ > 0.0D || renderMaxZ < 1.0D);
    }

    /**
     * Unlocks the visual bounding box so that RenderBlocks can change it again.
     */
    public void unlockBlockBounds()
    {
        lockBlockBounds = false;
    }

    /**
     * Renders a block using the given texture instead of the block's own default texture
     */
    public void renderBlockUsingTexture(Block par1Block, int par2, int par3, int par4, Icon par5Icon)
    {
        setOverrideBlockTexture(par5Icon);
        renderBlockByRenderType(par1Block, par2, par3, par4);
        clearOverrideBlockTexture();
    }

    /**
     * Render all faces of a block
     */
    public void renderBlockAllFaces(Block par1Block, int par2, int par3, int par4)
    {
        renderAllFaces = true;
        renderBlockByRenderType(par1Block, par2, par3, par4);
        renderAllFaces = false;
    }

    /**
     * Renders the block at the given coordinates using the block's rendering type
     */
    public boolean renderBlockByRenderType(Block par1Block, int par2, int par3, int par4)
    {
        int i = par1Block.getRenderType();

        if (i == -1)
        {
            return false;
        }

        par1Block.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);
        setRenderBoundsFromBlock(par1Block);

        if (i == 0)
        {
            return renderStandardBlock(par1Block, par2, par3, par4);
        }

        if (i == 4)
        {
            return renderBlockFluids(par1Block, par2, par3, par4);
        }

        if (i == 31)
        {
            return renderBlockLog(par1Block, par2, par3, par4);
        }

        if (i == 1)
        {
            return renderCrossedSquares(par1Block, par2, par3, par4);
        }

        if (i == 2)
        {
            return renderBlockTorch(par1Block, par2, par3, par4);
        }

        if (i == 20)
        {
            return renderBlockVine(par1Block, par2, par3, par4);
        }

        if (i == 11)
        {
            return renderBlockFence((BlockFence)par1Block, par2, par3, par4);
        }

        if (i == 39)
        {
            return renderBlockQuartz(par1Block, par2, par3, par4);
        }

        if (i == 5)
        {
            return renderBlockRedstoneWire(par1Block, par2, par3, par4);
        }

        if (i == 13)
        {
            return renderBlockCactus(par1Block, par2, par3, par4);
        }

        if (i == 9)
        {
            return renderBlockMinecartTrack((BlockRailBase)par1Block, par2, par3, par4);
        }

        if (i == 19)
        {
            return renderBlockStem(par1Block, par2, par3, par4);
        }

        if (i == 23)
        {
            return renderBlockLilyPad(par1Block, par2, par3, par4);
        }

        if (i == 6)
        {
            return renderBlockCrops(par1Block, par2, par3, par4);
        }

        if (i == 3)
        {
            return renderBlockFire((BlockFire)par1Block, par2, par3, par4);
        }

        if (i == 8)
        {
            return renderBlockLadder(par1Block, par2, par3, par4);
        }

        if (i == 7)
        {
            return renderBlockDoor(par1Block, par2, par3, par4);
        }

        if (i == 10)
        {
            return renderBlockStairs((BlockStairs)par1Block, par2, par3, par4);
        }

        if (i == 27)
        {
            return renderBlockDragonEgg((BlockDragonEgg)par1Block, par2, par3, par4);
        }

        if (i == 32)
        {
            return renderBlockWall((BlockWall)par1Block, par2, par3, par4);
        }

        if (i == 12)
        {
            return renderBlockLever(par1Block, par2, par3, par4);
        }

        if (i == 29)
        {
            return renderBlockTripWireSource(par1Block, par2, par3, par4);
        }

        if (i == 30)
        {
            return renderBlockTripWire(par1Block, par2, par3, par4);
        }

        if (i == 14)
        {
            return renderBlockBed(par1Block, par2, par3, par4);
        }

        if (i == 15)
        {
            return renderBlockRepeater((BlockRedstoneRepeater)par1Block, par2, par3, par4);
        }

        if (i == 36)
        {
            return renderBlockRedstoneLogic((BlockRedstoneLogic)par1Block, par2, par3, par4);
        }

        if (i == 37)
        {
            return renderBlockComparator((BlockComparator)par1Block, par2, par3, par4);
        }

        if (i == 16)
        {
            return renderPistonBase(par1Block, par2, par3, par4, false);
        }

        if (i == 17)
        {
            return renderPistonExtension(par1Block, par2, par3, par4, true);
        }

        if (i == 18)
        {
            return renderBlockPane((BlockPane)par1Block, par2, par3, par4);
        }

        if (i == 21)
        {
            return renderBlockFenceGate((BlockFenceGate)par1Block, par2, par3, par4);
        }

        if (i == 24)
        {
            return renderBlockCauldron((BlockCauldron)par1Block, par2, par3, par4);
        }

        if (i == 33)
        {
            return renderBlockFlowerpot((BlockFlowerPot)par1Block, par2, par3, par4);
        }

        if (i == 35)
        {
            return renderBlockAnvil((BlockAnvil)par1Block, par2, par3, par4);
        }

        if (i == 25)
        {
            return renderBlockBrewingStand((BlockBrewingStand)par1Block, par2, par3, par4);
        }

        if (i == 26)
        {
            return renderBlockEndPortalFrame((BlockEndPortalFrame)par1Block, par2, par3, par4);
        }

        if (i == 28)
        {
            return renderBlockCocoa((BlockCocoa)par1Block, par2, par3, par4);
        }

        if (i == 34)
        {
            return renderBlockBeacon((BlockBeacon)par1Block, par2, par3, par4);
        }

        if (i == 38)
        {
            return renderBlockHopper((BlockHopper)par1Block, par2, par3, par4);
        }
        else
        {
            return Minecraft.getMinecraft().renderBlocksMod(this, blockAccess, par1Block, par2, par3, par4, i, overrideBlockTexture);
        }
    }

    /**
     * Render BlockEndPortalFrame
     */
    public boolean renderBlockEndPortalFrame(BlockEndPortalFrame par1BlockEndPortalFrame, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;

        if (j == 0)
        {
            uvRotateTop = 3;
        }
        else if (j == 3)
        {
            uvRotateTop = 1;
        }
        else if (j == 1)
        {
            uvRotateTop = 2;
        }

        if (!BlockEndPortalFrame.isEnderEyeInserted(i))
        {
            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
            renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
            uvRotateTop = 0;
            return true;
        }
        else
        {
            renderAllFaces = true;
            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.8125D, 1.0D);
            renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
            setOverrideBlockTexture(par1BlockEndPortalFrame.func_94398_p());
            setRenderBounds(0.25D, 0.8125D, 0.25D, 0.75D, 1.0D, 0.75D);
            renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
            renderAllFaces = false;
            clearOverrideBlockTexture();
            uvRotateTop = 0;
            return true;
        }
    }

    /**
     * render a bed at the given coordinates
     */
    public boolean renderBlockBed(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = BlockBed.getDirection(i);
        boolean flag = BlockBed.isBlockHeadOfBed(i);
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        float f4 = f1;
        float f5 = f1;
        float f6 = f1;
        float f7 = f;
        float f8 = f2;
        float f9 = f3;
        float f10 = f;
        float f11 = f2;
        float f12 = f3;
        float f13 = f;
        float f14 = f2;
        float f15 = f3;
        int k = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        if (Minecraft.oldlighting){
            float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff * f7, ff * f10, ff * f13);
        }else{
            tessellator.setBrightness(k);
            tessellator.setColorOpaque_F(f7, f10, f13);
        }
        Icon icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0);
        double d = icon.getMinU();
        double d1 = icon.getMaxU();
        double d2 = icon.getMinV();
        double d3 = icon.getMaxV();
        double d4 = (double)par2 + renderMinX;
        double d5 = (double)par2 + renderMaxX;
        double d6 = (double)par3 + renderMinY + 0.1875D;
        double d7 = (double)par4 + renderMinZ;
        double d8 = (double)par4 + renderMaxZ;
        tessellator.addVertexWithUV(d4, d6, d8, d, d3);
        tessellator.addVertexWithUV(d4, d6, d7, d, d2);
        tessellator.addVertexWithUV(d5, d6, d7, d1, d2);
        tessellator.addVertexWithUV(d5, d6, d8, d1, d3);
        if (Minecraft.oldlighting){
            float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
            tessellator.setColorOpaque_F(f4 * ff, f5 * ff, f6 * ff);
        }else{
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4));
            tessellator.setColorOpaque_F(f4, f5, f6);
        }
        icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 1);
        d = icon.getMinU();
        d1 = icon.getMaxU();
        d2 = icon.getMinV();
        d3 = icon.getMaxV();
        d4 = d;
        d5 = d1;
        d6 = d2;
        d7 = d2;
        d8 = d;
        double d9 = d1;
        double d10 = d3;
        double d11 = d3;

        if (j == 0)
        {
            d5 = d;
            d6 = d3;
            d8 = d1;
            d11 = d2;
        }
        else if (j == 2)
        {
            d4 = d1;
            d7 = d3;
            d9 = d;
            d10 = d2;
        }
        else if (j == 3)
        {
            d4 = d1;
            d7 = d3;
            d9 = d;
            d10 = d2;
            d5 = d;
            d6 = d3;
            d8 = d1;
            d11 = d2;
        }

        double d12 = (double)par2 + renderMinX;
        double d13 = (double)par2 + renderMaxX;
        double d14 = (double)par3 + renderMaxY;
        double d15 = (double)par4 + renderMinZ;
        double d16 = (double)par4 + renderMaxZ;
        tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
        tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
        tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
        tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
        int l = Direction.directionToFacing[j];

        if (flag)
        {
            l = Direction.directionToFacing[Direction.rotateOpposite[j]];
        }

        byte byte0 = 4;

        switch (j)
        {
            case 0:
                byte0 = 5;
                break;
            case 3:
                byte0 = 2;
                break;
            case 1:
                byte0 = 3;
                break;
        }

        if (l != 2 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                tessellator.setColorOpaque_F(f8 * ff, f11 * ff, f14 * ff);
            }else{
                tessellator.setBrightness(renderMinZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : k);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }
            flipTexture = byte0 == 2;
            renderFaceZNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 2));
        }

        if (l != 3 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                tessellator.setColorOpaque_F(f8 * ff, f11 * ff, f14 * ff);
            }else{
                tessellator.setBrightness(renderMaxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : k);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }
            flipTexture = byte0 == 3;
            renderFaceZPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3));
        }

        if (l != 4 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                tessellator.setColorOpaque_F(f9 * ff, f12 * ff, f15 * ff);
            }else{
                tessellator.setBrightness(renderMinZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : k);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }
            flipTexture = byte0 == 4;
            renderFaceXNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 4));
        }

        if (l != 5 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                tessellator.setColorOpaque_F(f9 * ff, f12 * ff, f15 * ff);
            }else{
                tessellator.setBrightness(renderMaxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : k);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }
            flipTexture = byte0 == 5;
            renderFaceXPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 5));
        }

        flipTexture = false;
        return true;
    }

    /**
     * Render BlockBrewingStand
     */
    public boolean renderBlockBrewingStand(BlockBrewingStand par1BlockBrewingStand, int par2, int par3, int par4)
    {
        setRenderBounds(0.4375D, 0.0D, 0.4375D, 0.5625D, 0.875D, 0.5625D);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        setOverrideBlockTexture(par1BlockBrewingStand.getBrewingStandIcon());
        renderAllFaces = true;
        setRenderBounds(0.5625D, 0.0D, 0.3125D, 0.9375D, 0.125D, 0.6875D);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        setRenderBounds(0.125D, 0.0D, 0.0625D, 0.5D, 0.125D, 0.4375D);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        setRenderBounds(0.125D, 0.0D, 0.5625D, 0.5D, 0.125D, 0.9375D);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        renderAllFaces = false;
        clearOverrideBlockTexture();
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockBrewingStand.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1BlockBrewingStand.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int i = par1BlockBrewingStand.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f2 = (float)(i >> 8 & 0xff) / 255F;
        float f3 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        Icon icon = getBlockIconFromSideAndMetadata(par1BlockBrewingStand, 0, 0);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinV();
        double d1 = icon.getMaxV();
        int j = blockAccess.getBlockMetadata(par2, par3, par4);

        for (int k = 0; k < 3; k++)
        {
            double d2 = ((double)k * Math.PI * 2D) / 3D + (Math.PI / 2D);
            double d3 = icon.getInterpolatedU(8D);
            double d4 = icon.getMaxU();

            if ((j & 1 << k) != 0)
            {
                d4 = icon.getMinU();
            }

            double d5 = (double)par2 + 0.5D;
            double d6 = (double)par2 + 0.5D + (Math.sin(d2) * 8D) / 16D;
            double d7 = (double)par4 + 0.5D;
            double d8 = (double)par4 + 0.5D + (Math.cos(d2) * 8D) / 16D;
            tessellator.addVertexWithUV(d5, par3 + 1, d7, d3, d);
            tessellator.addVertexWithUV(d5, par3 + 0, d7, d3, d1);
            tessellator.addVertexWithUV(d6, par3 + 0, d8, d4, d1);
            tessellator.addVertexWithUV(d6, par3 + 1, d8, d4, d);
            tessellator.addVertexWithUV(d6, par3 + 1, d8, d4, d);
            tessellator.addVertexWithUV(d6, par3 + 0, d8, d4, d1);
            tessellator.addVertexWithUV(d5, par3 + 0, d7, d3, d1);
            tessellator.addVertexWithUV(d5, par3 + 1, d7, d3, d);
        }

        par1BlockBrewingStand.setBlockBoundsForItemRender();
        return true;
    }

    /**
     * Render block cauldron
     */
    public boolean renderBlockCauldron(BlockCauldron par1BlockCauldron, int par2, int par3, int par4)
    {
        renderStandardBlock(par1BlockCauldron, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockCauldron.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1BlockCauldron.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int i = par1BlockCauldron.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f2 = (float)(i >> 8 & 0xff) / 255F;
        float f3 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f7 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f7;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        Icon icon = par1BlockCauldron.getBlockTextureFromSide(2);
        float f6 = 0.125F;
        renderFaceXPos(par1BlockCauldron, ((float)par2 - 1.0F) + f6, par3, par4, icon);
        renderFaceXNeg(par1BlockCauldron, ((float)par2 + 1.0F) - f6, par3, par4, icon);
        renderFaceZPos(par1BlockCauldron, par2, par3, ((float)par4 - 1.0F) + f6, icon);
        renderFaceZNeg(par1BlockCauldron, par2, par3, ((float)par4 + 1.0F) - f6, icon);
        Icon icon1 = BlockCauldron.getCauldronIcon("inner");
        renderFaceYPos(par1BlockCauldron, par2, ((float)par3 - 1.0F) + 0.25F, par4, icon1);
        renderFaceYNeg(par1BlockCauldron, par2, ((float)par3 + 1.0F) - 0.75F, par4, icon1);
        int j = blockAccess.getBlockMetadata(par2, par3, par4);

        if (j > 0)
        {
            Icon icon2 = BlockFluid.getFluidIcon("water_still");

            if (j > 3)
            {
                j = 3;
            }

            renderFaceYPos(par1BlockCauldron, par2, ((float)par3 - 1.0F) + (6F + (float)j * 3F) / 16F, par4, icon2);
        }

        return true;
    }

    /**
     * Renders flower pot
     */
    public boolean renderBlockFlowerpot(BlockFlowerPot par1BlockFlowerPot, int par2, int par3, int par4)
    {
        renderStandardBlock(par1BlockFlowerPot, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(par1BlockFlowerPot.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        float f = Minecraft.oldlighting ? par1BlockFlowerPot.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int i = par1BlockFlowerPot.colorMultiplier(blockAccess, par2, par3, par4);
        Icon icon = getBlockIconFromSide(par1BlockFlowerPot, 0);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f3 = (float)(i >> 8 & 0xff) / 255F;
        float f5 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f7 = (f1 * 30F + f3 * 59F + f5 * 11F) / 100F;
            float f9 = (f1 * 30F + f3 * 70F) / 100F;
            float f10 = (f1 * 30F + f5 * 70F) / 100F;
            f1 = f7;
            f3 = f9;
            f5 = f10;
        }

        tessellator.setColorOpaque_F(f * f1, f * f3, f * f5);
        float f8 = 0.1865F;
        renderFaceXPos(par1BlockFlowerPot, ((float)par2 - 0.5F) + f8, par3, par4, icon);
        renderFaceXNeg(par1BlockFlowerPot, ((float)par2 + 0.5F) - f8, par3, par4, icon);
        renderFaceZPos(par1BlockFlowerPot, par2, par3, ((float)par4 - 0.5F) + f8, icon);
        renderFaceZNeg(par1BlockFlowerPot, par2, par3, ((float)par4 + 0.5F) - f8, icon);
        renderFaceYPos(par1BlockFlowerPot, par2, ((float)par3 - 0.5F) + f8 + 0.1875F, par4, getBlockIcon(Block.dirt));
        int k = blockAccess.getBlockMetadata(par2, par3, par4);

        if (k != 0)
        {
            float f11 = 0.0F;
            float f12 = 4F;
            float f13 = 0.0F;
            BlockFlower blockflower = null;

            switch (k)
            {
                case 1:
                    blockflower = Block.plantRed;
                    break;
                case 2:
                    blockflower = Block.plantYellow;
                    break;
                case 8:
                    blockflower = Block.mushroomBrown;
                    break;
                case 7:
                    blockflower = Block.mushroomRed;
                    break;
            }

            tessellator.addTranslation(f11 / 16F, f12 / 16F, f13 / 16F);

            if (blockflower != null)
            {
                renderBlockByRenderType(blockflower, par2, par3, par4);
            }
            else if (k == 9)
            {
                renderAllFaces = true;
                float f14 = 0.125F;
                setRenderBounds(0.5F - f14, 0.0D, 0.5F - f14, 0.5F + f14, 0.25D, 0.5F + f14);
                renderStandardBlock(Block.cactus, par2, par3, par4);
                setRenderBounds(0.5F - f14, 0.25D, 0.5F - f14, 0.5F + f14, 0.5D, 0.5F + f14);
                renderStandardBlock(Block.cactus, par2, par3, par4);
                setRenderBounds(0.5F - f14, 0.5D, 0.5F - f14, 0.5F + f14, 0.75D, 0.5F + f14);
                renderStandardBlock(Block.cactus, par2, par3, par4);
                renderAllFaces = false;
                setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            }
            else if (k == 3)
            {
                drawCrossedSquares(Block.sapling, 0, par2, par3, par4, 0.75F);
            }
            else if (k == 5)
            {
                drawCrossedSquares(Block.sapling, 2, par2, par3, par4, 0.75F);
            }
            else if (k == 4)
            {
                drawCrossedSquares(Block.sapling, 1, par2, par3, par4, 0.75F);
            }
            else if (k == 6)
            {
                drawCrossedSquares(Block.sapling, 3, par2, par3, par4, 0.75F);
            }
            else if (k == 11)
            {
                int j = Block.tallGrass.colorMultiplier(blockAccess, par2, par3, par4);
                float f2 = (float)(j >> 16 & 0xff) / 255F;
                float f4 = (float)(j >> 8 & 0xff) / 255F;
                float f6 = (float)(j & 0xff) / 255F;
                tessellator.setColorOpaque_F(f * f2, f * f4, f * f6);
                drawCrossedSquares(Block.tallGrass, 2, par2, par3, par4, 0.75F);
            }
            else if (k == 10)
            {
                drawCrossedSquares(Block.deadBush, 2, par2, par3, par4, 0.75F);
            }

            tessellator.addTranslation(-f11 / 16F, -f12 / 16F, -f13 / 16F);
        }

        return true;
    }

    /**
     * Renders anvil
     */
    public boolean renderBlockAnvil(BlockAnvil par1BlockAnvil, int par2, int par3, int par4)
    {
        return renderBlockAnvilMetadata(par1BlockAnvil, par2, par3, par4, blockAccess.getBlockMetadata(par2, par3, par4));
    }

    /**
     * Renders anvil block with metadata
     */
    public boolean renderBlockAnvilMetadata(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(par1BlockAnvil.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        float f = 1.0F;
        int i = par1BlockAnvil.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f2 = (float)(i >> 8 & 0xff) / 255F;
        float f3 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        return renderBlockAnvilOrient(par1BlockAnvil, par2, par3, par4, par5, false);
    }

    /**
     * Renders anvil block with orientation
     */
    public boolean renderBlockAnvilOrient(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5, boolean par6)
    {
        int i = par6 ? 0 : par5 & 3;
        boolean flag = false;
        float f = 0.0F;

        switch (i)
        {
            case 2:
                uvRotateSouth = 1;
                uvRotateNorth = 2;
                break;
            case 0:
                uvRotateSouth = 2;
                uvRotateNorth = 1;
                uvRotateTop = 3;
                uvRotateBottom = 3;
                break;
            case 1:
                uvRotateEast = 1;
                uvRotateWest = 2;
                uvRotateTop = 2;
                uvRotateBottom = 1;
                flag = true;
                break;
            case 3:
                uvRotateEast = 2;
                uvRotateWest = 1;
                uvRotateTop = 1;
                uvRotateBottom = 2;
                flag = true;
                break;
        }

        f = renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 0, f, 0.75F, 0.25F, 0.75F, flag, par6, par5);
        f = renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 1, f, 0.5F, 0.0625F, 0.625F, flag, par6, par5);
        f = renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 2, f, 0.25F, 0.3125F, 0.5F, flag, par6, par5);
        f = renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 3, f, 0.625F, 0.375F, 1.0F, flag, par6, par5);
        setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        return true;
    }

    /**
     * Renders anvil block with rotation
     */
    public float renderBlockAnvilRotate(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5, float par6, float par7, float par8, float par9, boolean par10, boolean par11, int par12)
    {
        if (par10)
        {
            float f = par7;
            par7 = par9;
            par9 = f;
        }

        par7 /= 2.0F;
        par9 /= 2.0F;
        par1BlockAnvil.field_82521_b = par5;
        setRenderBounds(0.5F - par7, par6, 0.5F - par9, 0.5F + par7, par6 + par8, 0.5F + par9);

        if (par11)
        {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderFaceYNeg(par1BlockAnvil, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockAnvil, 0, par12));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(par1BlockAnvil, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockAnvil, 1, par12));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderFaceZNeg(par1BlockAnvil, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockAnvil, 2, par12));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderFaceZPos(par1BlockAnvil, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockAnvil, 3, par12));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderFaceXNeg(par1BlockAnvil, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockAnvil, 4, par12));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderFaceXPos(par1BlockAnvil, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockAnvil, 5, par12));
            tessellator.draw();
        }
        else
        {
            renderStandardBlock(par1BlockAnvil, par2, par3, par4);
        }

        return par6 + par8;
    }

    /**
     * Renders a torch block at the given coordinates
     */
    public boolean renderBlockTorch(Block par1Block, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float f = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f = 1.0F;
            }
            tessellator.setColorOpaque_F(f, f, f);
        }
        double d = 0.40000000596046448D;
        double d1 = 0.5D - d;
        double d2 = 0.20000000298023224D;

        if (i == 1)
        {
            renderTorchAtAngle(par1Block, (double)par2 - d1, (double)par3 + d2, par4, -d, 0.0D, 0);
        }
        else if (i == 2)
        {
            renderTorchAtAngle(par1Block, (double)par2 + d1, (double)par3 + d2, par4, d, 0.0D, 0);
        }
        else if (i == 3)
        {
            renderTorchAtAngle(par1Block, par2, (double)par3 + d2, (double)par4 - d1, 0.0D, -d, 0);
        }
        else if (i == 4)
        {
            renderTorchAtAngle(par1Block, par2, (double)par3 + d2, (double)par4 + d1, 0.0D, d, 0);
        }
        else
        {
            renderTorchAtAngle(par1Block, par2, par3, par4, 0.0D, 0.0D, 0);
        }

        return true;
    }

    /**
     * render a redstone repeater at the given coordinates
     */
    public boolean renderBlockRepeater(BlockRedstoneRepeater par1BlockRedstoneRepeater, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        int k = (i & 0xc) >> 2;
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockRedstoneRepeater.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float ff = par1BlockRedstoneRepeater.getBlockBrightness(blockAccess, par2, par3, par4);
            if(par1BlockRedstoneRepeater.lightValue[par1BlockRedstoneRepeater.blockID] > 0)
            {
                ff = (ff + 1.0F) * 0.5F;
            }
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        double d = -0.1875D;
        boolean flag = par1BlockRedstoneRepeater.func_94476_e(blockAccess, par2, par3, par4, i);
        double d1 = 0.0D;
        double d2 = 0.0D;
        double d3 = 0.0D;
        double d4 = 0.0D;

        switch (j)
        {
            case 0:
                d4 = -0.3125D;
                d2 = BlockRedstoneRepeater.repeaterTorchOffset[k];
                break;
            case 2:
                d4 = 0.3125D;
                d2 = -BlockRedstoneRepeater.repeaterTorchOffset[k];
                break;
            case 3:
                d3 = -0.3125D;
                d1 = BlockRedstoneRepeater.repeaterTorchOffset[k];
                break;
            case 1:
                d3 = 0.3125D;
                d1 = -BlockRedstoneRepeater.repeaterTorchOffset[k];
                break;
        }

        if (!flag)
        {
            renderTorchAtAngle(par1BlockRedstoneRepeater, (double)par2 + d1, (double)par3 + d, (double)par4 + d2, 0.0D, 0.0D, 0);
        }
        else
        {
            Icon icon = getBlockIcon(Block.bedrock);
            setOverrideBlockTexture(icon);
            float f = 2.0F;
            float f1 = 14F;
            float f2 = 7F;
            float f3 = 9F;

            switch (j)
            {
                case 1:
                case 3:
                    f = 7F;
                    f1 = 9F;
                    f2 = 2.0F;
                    f3 = 14F;
                case 0:
                case 2:
                default:
                    setRenderBounds(f / 16F + (float)d1, 0.125D, f2 / 16F + (float)d2, f1 / 16F + (float)d1, 0.25D, f3 / 16F + (float)d2);
                    double d5 = icon.getInterpolatedU(f);
                    double d6 = icon.getInterpolatedV(f2);
                    double d7 = icon.getInterpolatedU(f1);
                    double d8 = icon.getInterpolatedV(f3);
                    tessellator.addVertexWithUV((double)((float)par2 + f / 16F) + d1, (float)par3 + 0.25F, (double)((float)par4 + f2 / 16F) + d2, d5, d6);
                    tessellator.addVertexWithUV((double)((float)par2 + f / 16F) + d1, (float)par3 + 0.25F, (double)((float)par4 + f3 / 16F) + d2, d5, d8);
                    tessellator.addVertexWithUV((double)((float)par2 + f1 / 16F) + d1, (float)par3 + 0.25F, (double)((float)par4 + f3 / 16F) + d2, d7, d8);
                    tessellator.addVertexWithUV((double)((float)par2 + f1 / 16F) + d1, (float)par3 + 0.25F, (double)((float)par4 + f2 / 16F) + d2, d7, d6);
                    renderStandardBlock(par1BlockRedstoneRepeater, par2, par3, par4);
                    setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
                    clearOverrideBlockTexture();
                    break;
            }
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockRedstoneRepeater.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float ff = par1BlockRedstoneRepeater.getBlockBrightness(blockAccess, par2, par3, par4);
            if(par1BlockRedstoneRepeater.lightValue[par1BlockRedstoneRepeater.blockID] > 0)
            {
                ff = (ff + 1.0F) * 0.5F;
            }
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        renderTorchAtAngle(par1BlockRedstoneRepeater, (double)par2 + d3, (double)par3 + d, (double)par4 + d4, 0.0D, 0.0D, 0);
        renderBlockRedstoneLogic(par1BlockRedstoneRepeater, par2, par3, par4);
        return true;
    }

    public boolean renderBlockComparator(BlockComparator par1BlockComparator, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.setBrightness(par1BlockComparator.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }else{
            float ff = par1BlockComparator.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        double d = 0.0D;
        double d1 = -0.1875D;
        double d2 = 0.0D;
        double d3 = 0.0D;
        double d4 = 0.0D;
        Icon icon;

        if (par1BlockComparator.func_94490_c(i))
        {
            icon = Block.torchRedstoneActive.getBlockTextureFromSide(0);
        }
        else
        {
            d1 -= 0.1875D;
            icon = Block.torchRedstoneIdle.getBlockTextureFromSide(0);
        }

        switch (j)
        {
            case 0:
                d2 = -0.3125D;
                d4 = 1.0D;
                break;
            case 2:
                d2 = 0.3125D;
                d4 = -1D;
                break;
            case 3:
                d = -0.3125D;
                d3 = 1.0D;
                break;
            case 1:
                d = 0.3125D;
                d3 = -1D;
                break;
        }

        renderTorchAtAngle(par1BlockComparator, (double)par2 + 0.25D * d3 + 0.1875D * d4, (float)par3 - 0.1875F, (double)par4 + 0.25D * d4 + 0.1875D * d3, 0.0D, 0.0D, i);
        renderTorchAtAngle(par1BlockComparator, (double)par2 + 0.25D * d3 + -0.1875D * d4, (float)par3 - 0.1875F, (double)par4 + 0.25D * d4 + -0.1875D * d3, 0.0D, 0.0D, i);
        setOverrideBlockTexture(icon);
        renderTorchAtAngle(par1BlockComparator, (double)par2 + d, (double)par3 + d1, (double)par4 + d2, 0.0D, 0.0D, i);
        clearOverrideBlockTexture();
        renderBlockRedstoneLogicMetadata(par1BlockComparator, par2, par3, par4, j);
        return true;
    }

    public boolean renderBlockRedstoneLogic(BlockRedstoneLogic par1BlockRedstoneLogic, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        renderBlockRedstoneLogicMetadata(par1BlockRedstoneLogic, par2, par3, par4, blockAccess.getBlockMetadata(par2, par3, par4) & 3);
        return true;
    }

    public void renderBlockRedstoneLogicMetadata(BlockRedstoneLogic par1BlockRedstoneLogic, int par2, int par3, int par4, int par5)
    {
        renderStandardBlock(par1BlockRedstoneLogic, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.setBrightness(par1BlockRedstoneLogic.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }else{
            float ff = par1BlockRedstoneLogic.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        Icon icon = getBlockIconFromSideAndMetadata(par1BlockRedstoneLogic, 1, i);
        double d = icon.getMinU();
        double d1 = icon.getMaxU();
        double d2 = icon.getMinV();
        double d3 = icon.getMaxV();
        double d4 = 0.125D;
        double d5 = par2 + 1;
        double d6 = par2 + 1;
        double d7 = par2 + 0;
        double d8 = par2 + 0;
        double d9 = par4 + 0;
        double d10 = par4 + 1;
        double d11 = par4 + 1;
        double d12 = par4 + 0;
        double d13 = (double)par3 + d4;

        if (par5 == 2)
        {
            d5 = d6 = par2 + 0;
            d7 = d8 = par2 + 1;
            d9 = d12 = par4 + 1;
            d10 = d11 = par4 + 0;
        }
        else if (par5 == 3)
        {
            d5 = d8 = par2 + 0;
            d6 = d7 = par2 + 1;
            d9 = d10 = par4 + 0;
            d11 = d12 = par4 + 1;
        }
        else if (par5 == 1)
        {
            d5 = d8 = par2 + 1;
            d6 = d7 = par2 + 0;
            d9 = d10 = par4 + 1;
            d11 = d12 = par4 + 0;
        }

        tessellator.addVertexWithUV(d8, d13, d12, d, d2);
        tessellator.addVertexWithUV(d7, d13, d11, d, d3);
        tessellator.addVertexWithUV(d6, d13, d10, d1, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
    }

    /**
     * Render all faces of the piston base
     */
    public void renderPistonBaseAllFaces(Block par1Block, int par2, int par3, int par4)
    {
        renderAllFaces = true;
        renderPistonBase(par1Block, par2, par3, par4, true);
        renderAllFaces = false;
    }

    /**
     * renders a block as a piston base
     */
    public boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = par5 || (i & 8) != 0;
        int j = BlockPistonBase.getOrientation(i);
        float f = 0.25F;

        if (flag)
        {
            switch (j)
            {
                case 0:
                    uvRotateEast = 3;
                    uvRotateWest = 3;
                    uvRotateSouth = 3;
                    uvRotateNorth = 3;
                    setRenderBounds(0.0D, 0.25D, 0.0D, 1.0D, 1.0D, 1.0D);
                    break;
                case 1:
                    setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.75D, 1.0D);
                    break;
                case 2:
                    uvRotateSouth = 1;
                    uvRotateNorth = 2;
                    setRenderBounds(0.0D, 0.0D, 0.25D, 1.0D, 1.0D, 1.0D);
                    break;
                case 3:
                    uvRotateSouth = 2;
                    uvRotateNorth = 1;
                    uvRotateTop = 3;
                    uvRotateBottom = 3;
                    setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.75D);
                    break;
                case 4:
                    uvRotateEast = 1;
                    uvRotateWest = 2;
                    uvRotateTop = 2;
                    uvRotateBottom = 1;
                    setRenderBounds(0.25D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                    break;
                case 5:
                    uvRotateEast = 2;
                    uvRotateWest = 1;
                    uvRotateTop = 1;
                    uvRotateBottom = 2;
                    setRenderBounds(0.0D, 0.0D, 0.0D, 0.75D, 1.0D, 1.0D);
                    break;
            }

            ((BlockPistonBase)par1Block).func_96479_b((float)renderMinX, (float)renderMinY, (float)renderMinZ, (float)renderMaxX, (float)renderMaxY, (float)renderMaxZ);
            renderStandardBlock(par1Block, par2, par3, par4);
            uvRotateEast = 0;
            uvRotateWest = 0;
            uvRotateSouth = 0;
            uvRotateNorth = 0;
            uvRotateTop = 0;
            uvRotateBottom = 0;
            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            ((BlockPistonBase)par1Block).func_96479_b((float)renderMinX, (float)renderMinY, (float)renderMinZ, (float)renderMaxX, (float)renderMaxY, (float)renderMaxZ);
        }
        else
        {
            switch (j)
            {
                case 0:
                    uvRotateEast = 3;
                    uvRotateWest = 3;
                    uvRotateSouth = 3;
                    uvRotateNorth = 3;
                    break;
                case 2:
                    uvRotateSouth = 1;
                    uvRotateNorth = 2;
                    break;
                case 3:
                    uvRotateSouth = 2;
                    uvRotateNorth = 1;
                    uvRotateTop = 3;
                    uvRotateBottom = 3;
                    break;
                case 4:
                    uvRotateEast = 1;
                    uvRotateWest = 2;
                    uvRotateTop = 2;
                    uvRotateBottom = 1;
                    break;
                case 5:
                    uvRotateEast = 2;
                    uvRotateWest = 1;
                    uvRotateTop = 1;
                    uvRotateBottom = 2;
                    break;
            }

            renderStandardBlock(par1Block, par2, par3, par4);
            uvRotateEast = 0;
            uvRotateWest = 0;
            uvRotateSouth = 0;
            uvRotateNorth = 0;
            uvRotateTop = 0;
            uvRotateBottom = 0;
        }

        return true;
    }

    /**
     * Render piston rod up/down
     */
    public void renderPistonRodUD(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14)
    {
        Icon icon = BlockPistonBase.getPistonBaseIcon("piston_side");

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        Tessellator tessellator = Tessellator.instance;
        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getInterpolatedU(par14);
        double d3 = icon.getInterpolatedV(4D);
        tessellator.setColorOpaque_F(par13, par13, par13);
        tessellator.addVertexWithUV(par1, par7, par9, d2, d1);
        tessellator.addVertexWithUV(par1, par5, par9, d, d1);
        tessellator.addVertexWithUV(par3, par5, par11, d, d3);
        tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
    }

    /**
     * Render piston rod south/north
     */
    public void renderPistonRodSN(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14)
    {
        Icon icon = BlockPistonBase.getPistonBaseIcon("piston_side");

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        Tessellator tessellator = Tessellator.instance;
        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getInterpolatedU(par14);
        double d3 = icon.getInterpolatedV(4D);
        tessellator.setColorOpaque_F(par13, par13, par13);
        tessellator.addVertexWithUV(par1, par5, par11, d2, d1);
        tessellator.addVertexWithUV(par1, par5, par9, d, d1);
        tessellator.addVertexWithUV(par3, par7, par9, d, d3);
        tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
    }

    /**
     * Render piston rod east/west
     */
    public void renderPistonRodEW(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14)
    {
        Icon icon = BlockPistonBase.getPistonBaseIcon("piston_side");

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        Tessellator tessellator = Tessellator.instance;
        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getInterpolatedU(par14);
        double d3 = icon.getInterpolatedV(4D);
        tessellator.setColorOpaque_F(par13, par13, par13);
        tessellator.addVertexWithUV(par3, par5, par9, d2, d1);
        tessellator.addVertexWithUV(par1, par5, par9, d, d1);
        tessellator.addVertexWithUV(par1, par7, par11, d, d3);
        tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
    }

    /**
     * Render all faces of the piston extension
     */
    public void renderPistonExtensionAllFaces(Block par1Block, int par2, int par3, int par4, boolean par5)
    {
        renderAllFaces = true;
        renderPistonExtension(par1Block, par2, par3, par4, par5);
        renderAllFaces = false;
    }

    /**
     * renders the pushing part of a piston
     */
    public boolean renderPistonExtension(Block par1Block, int par2, int par3, int par4, boolean par5)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = BlockPistonExtension.getDirectionMeta(i);
        float f = 0.25F;
        float f1 = 0.375F;
        float f2 = 0.625F;
        float f3 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
        float f4 = par5 ? 1.0F : 0.5F;
        double d = par5 ? 16D : 8D;

        switch (j)
        {
            case 0:
                uvRotateEast = 3;
                uvRotateWest = 3;
                uvRotateSouth = 3;
                uvRotateNorth = 3;
                setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 0.25D, 1.0D);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.625F, (float)par3 + 0.25F, (float)par3 + 0.25F + f4, (float)par4 + 0.625F, (float)par4 + 0.625F, f3 * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.375F, (float)par3 + 0.25F, (float)par3 + 0.25F + f4, (float)par4 + 0.375F, (float)par4 + 0.375F, f3 * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.375F, (float)par3 + 0.25F, (float)par3 + 0.25F + f4, (float)par4 + 0.375F, (float)par4 + 0.625F, f3 * 0.6F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.625F, (float)par3 + 0.25F, (float)par3 + 0.25F + f4, (float)par4 + 0.625F, (float)par4 + 0.375F, f3 * 0.6F, d);
                break;
            case 1:
                setRenderBounds(0.0D, 0.75D, 0.0D, 1.0D, 1.0D, 1.0D);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.625F, (((float)par3 - 0.25F) + 1.0F) - f4, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.625F, (float)par4 + 0.625F, f3 * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.375F, (((float)par3 - 0.25F) + 1.0F) - f4, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.375F, (float)par4 + 0.375F, f3 * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.375F, (((float)par3 - 0.25F) + 1.0F) - f4, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.375F, (float)par4 + 0.625F, f3 * 0.6F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.625F, (((float)par3 - 0.25F) + 1.0F) - f4, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.625F, (float)par4 + 0.375F, f3 * 0.6F, d);
                break;
            case 2:
                uvRotateSouth = 1;
                uvRotateNorth = 2;
                setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.375F, (float)par4 + 0.25F, (float)par4 + 0.25F + f4, f3 * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.625F, (float)par4 + 0.25F, (float)par4 + 0.25F + f4, f3 * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.375F, (float)par4 + 0.25F, (float)par4 + 0.25F + f4, f3 * 0.5F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.625F, (float)par4 + 0.25F, (float)par4 + 0.25F + f4, f3, d);
                break;
            case 3:
                uvRotateSouth = 2;
                uvRotateNorth = 1;
                uvRotateTop = 3;
                uvRotateBottom = 3;
                setRenderBounds(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.375F, (((float)par4 - 0.25F) + 1.0F) - f4, ((float)par4 - 0.25F) + 1.0F, f3 * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.625F, (((float)par4 - 0.25F) + 1.0F) - f4, ((float)par4 - 0.25F) + 1.0F, f3 * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.375F, (((float)par4 - 0.25F) + 1.0F) - f4, ((float)par4 - 0.25F) + 1.0F, f3 * 0.5F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.625F, (((float)par4 - 0.25F) + 1.0F) - f4, ((float)par4 - 0.25F) + 1.0F, f3, d);
                break;
            case 4:
                uvRotateEast = 1;
                uvRotateWest = 2;
                uvRotateTop = 2;
                uvRotateBottom = 1;
                setRenderBounds(0.0D, 0.0D, 0.0D, 0.25D, 1.0D, 1.0D);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f4, (float)par3 + 0.375F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.375F, f3 * 0.5F, d);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f4, (float)par3 + 0.625F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.625F, f3, d);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f4, (float)par3 + 0.375F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.375F, f3 * 0.6F, d);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f4, (float)par3 + 0.625F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.625F, f3 * 0.6F, d);
                break;
            case 5:
                uvRotateEast = 2;
                uvRotateWest = 1;
                uvRotateTop = 1;
                uvRotateBottom = 2;
                setRenderBounds(0.75D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f4, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.375F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.375F, f3 * 0.5F, d);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f4, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.625F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.625F, f3, d);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f4, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.375F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.375F, f3 * 0.6F, d);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f4, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.625F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.625F, f3 * 0.6F, d);
                break;
        }

        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        return true;
    }

    /**
     * Renders a lever block at the given coordinates
     */
    public boolean renderBlockLever(Block par1Block, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 7;
        boolean flag = (i & 8) > 0;
        Tessellator tessellator = Tessellator.instance;
        boolean flag1 = hasOverrideBlockTexture();

        if (!flag1)
        {
            setOverrideBlockTexture(getBlockIcon(Block.cobblestone));
        }

        float f = 0.25F;
        float f1 = 0.1875F;
        float f2 = 0.1875F;

        if (j == 5)
        {
            setRenderBounds(0.5F - f1, 0.0D, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        }
        else if (j == 6)
        {
            setRenderBounds(0.5F - f, 0.0D, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
        }
        else if (j == 4)
        {
            setRenderBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0D);
        }
        else if (j == 3)
        {
            setRenderBounds(0.5F - f1, 0.5F - f, 0.0D, 0.5F + f1, 0.5F + f, f2);
        }
        else if (j == 2)
        {
            setRenderBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0D, 0.5F + f, 0.5F + f1);
        }
        else if (j == 1)
        {
            setRenderBounds(0.0D, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
        }
        else if (j == 0)
        {
            setRenderBounds(0.5F - f, 1.0F - f2, 0.5F - f1, 0.5F + f, 1.0D, 0.5F + f1);
        }
        else if (j == 7)
        {
            setRenderBounds(0.5F - f1, 1.0F - f2, 0.5F - f, 0.5F + f1, 1.0D, 0.5F + f);
        }

        renderStandardBlock(par1Block, par2, par3, par4);

        if (!flag1)
        {
            clearOverrideBlockTexture();
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f3 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;

        if (Block.lightValue[par1Block.blockID] > 0)
        {
            f3 = 1.0F;
        }

        tessellator.setColorOpaque_F(f3, f3, f3);
        Icon icon = getBlockIconFromSide(par1Block, 0);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        Vec3 avec3[] = new Vec3[8];
        float f4 = 0.0625F;
        float f5 = 0.0625F;
        float f6 = 0.625F;
        avec3[0] = blockAccess.getWorldVec3Pool().getVecFromPool(-f4, 0.0D, -f5);
        avec3[1] = blockAccess.getWorldVec3Pool().getVecFromPool(f4, 0.0D, -f5);
        avec3[2] = blockAccess.getWorldVec3Pool().getVecFromPool(f4, 0.0D, f5);
        avec3[3] = blockAccess.getWorldVec3Pool().getVecFromPool(-f4, 0.0D, f5);
        avec3[4] = blockAccess.getWorldVec3Pool().getVecFromPool(-f4, f6, -f5);
        avec3[5] = blockAccess.getWorldVec3Pool().getVecFromPool(f4, f6, -f5);
        avec3[6] = blockAccess.getWorldVec3Pool().getVecFromPool(f4, f6, f5);
        avec3[7] = blockAccess.getWorldVec3Pool().getVecFromPool(-f4, f6, f5);

        for (int k = 0; k < 8; k++)
        {
            if (flag)
            {
                avec3[k].zCoord -= 0.0625D;
                avec3[k].rotateAroundX(((float)Math.PI * 2F / 9F));
            }
            else
            {
                avec3[k].zCoord += 0.0625D;
                avec3[k].rotateAroundX(-((float)Math.PI * 2F / 9F));
            }

            if (j == 0 || j == 7)
            {
                avec3[k].rotateAroundZ((float)Math.PI);
            }

            if (j == 6 || j == 0)
            {
                avec3[k].rotateAroundY(((float)Math.PI / 2F));
            }

            if (j > 0 && j < 5)
            {
                avec3[k].yCoord -= 0.375D;
                avec3[k].rotateAroundX(((float)Math.PI / 2F));

                if (j == 4)
                {
                    avec3[k].rotateAroundY(0.0F);
                }

                if (j == 3)
                {
                    avec3[k].rotateAroundY((float)Math.PI);
                }

                if (j == 2)
                {
                    avec3[k].rotateAroundY(((float)Math.PI / 2F));
                }

                if (j == 1)
                {
                    avec3[k].rotateAroundY(-((float)Math.PI / 2F));
                }

                avec3[k].xCoord += (double)par2 + 0.5D;
                avec3[k].yCoord += (float)par3 + 0.5F;
                avec3[k].zCoord += (double)par4 + 0.5D;
                continue;
            }

            if (j == 0 || j == 7)
            {
                avec3[k].xCoord += (double)par2 + 0.5D;
                avec3[k].yCoord += (float)par3 + 0.875F;
                avec3[k].zCoord += (double)par4 + 0.5D;
            }
            else
            {
                avec3[k].xCoord += (double)par2 + 0.5D;
                avec3[k].yCoord += (float)par3 + 0.125F;
                avec3[k].zCoord += (double)par4 + 0.5D;
            }
        }

        Vec3 vec3 = null;
        Vec3 vec3_1 = null;
        Vec3 vec3_2 = null;
        Vec3 vec3_3 = null;

        for (int l = 0; l < 6; l++)
        {
            if (l == 0)
            {
                d = icon.getInterpolatedU(7D);
                d1 = icon.getInterpolatedV(6D);
                d2 = icon.getInterpolatedU(9D);
                d3 = icon.getInterpolatedV(8D);
            }
            else if (l == 2)
            {
                d = icon.getInterpolatedU(7D);
                d1 = icon.getInterpolatedV(6D);
                d2 = icon.getInterpolatedU(9D);
                d3 = icon.getMaxV();
            }

            if (l == 0)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[1];
                vec3_2 = avec3[2];
                vec3_3 = avec3[3];
            }
            else if (l == 1)
            {
                vec3 = avec3[7];
                vec3_1 = avec3[6];
                vec3_2 = avec3[5];
                vec3_3 = avec3[4];
            }
            else if (l == 2)
            {
                vec3 = avec3[1];
                vec3_1 = avec3[0];
                vec3_2 = avec3[4];
                vec3_3 = avec3[5];
            }
            else if (l == 3)
            {
                vec3 = avec3[2];
                vec3_1 = avec3[1];
                vec3_2 = avec3[5];
                vec3_3 = avec3[6];
            }
            else if (l == 4)
            {
                vec3 = avec3[3];
                vec3_1 = avec3[2];
                vec3_2 = avec3[6];
                vec3_3 = avec3[7];
            }
            else if (l == 5)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[3];
                vec3_2 = avec3[7];
                vec3_3 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d, d3);
            tessellator.addVertexWithUV(vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, d2, d3);
            tessellator.addVertexWithUV(vec3_2.xCoord, vec3_2.yCoord, vec3_2.zCoord, d2, d1);
            tessellator.addVertexWithUV(vec3_3.xCoord, vec3_3.yCoord, vec3_3.zCoord, d, d1);
        }

        return true;
    }

    /**
     * Renders a trip wire source block at the given coordinates
     */
    public boolean renderBlockTripWireSource(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        boolean flag = (i & 4) == 4;
        boolean flag1 = (i & 8) == 8;
        boolean flag2 = !blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
        boolean flag3 = hasOverrideBlockTexture();

        if (!flag3)
        {
            setOverrideBlockTexture(getBlockIcon(Block.planks));
        }

        float f = 0.25F;
        float f1 = 0.125F;
        float f2 = 0.125F;
        float f3 = 0.3F - f;
        float f4 = 0.3F + f;

        if (j == 2)
        {
            setRenderBounds(0.5F - f1, f3, 1.0F - f2, 0.5F + f1, f4, 1.0D);
        }
        else if (j == 0)
        {
            setRenderBounds(0.5F - f1, f3, 0.0D, 0.5F + f1, f4, f2);
        }
        else if (j == 1)
        {
            setRenderBounds(1.0F - f2, f3, 0.5F - f1, 1.0D, f4, 0.5F + f1);
        }
        else if (j == 3)
        {
            setRenderBounds(0.0D, f3, 0.5F - f1, f2, f4, 0.5F + f1);
        }

        renderStandardBlock(par1Block, par2, par3, par4);

        if (!flag3)
        {
            clearOverrideBlockTexture();
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;

        if (Block.lightValue[par1Block.blockID] > 0)
        {
            f5 = 1.0F;
        }

        tessellator.setColorOpaque_F(f5, f5, f5);
        Icon icon = getBlockIconFromSide(par1Block, 0);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        Vec3 avec3[] = new Vec3[8];
        float f7 = 0.046875F;
        float f8 = 0.046875F;
        float f9 = 0.3125F;
        avec3[0] = blockAccess.getWorldVec3Pool().getVecFromPool(-f7, 0.0D, -f8);
        avec3[1] = blockAccess.getWorldVec3Pool().getVecFromPool(f7, 0.0D, -f8);
        avec3[2] = blockAccess.getWorldVec3Pool().getVecFromPool(f7, 0.0D, f8);
        avec3[3] = blockAccess.getWorldVec3Pool().getVecFromPool(-f7, 0.0D, f8);
        avec3[4] = blockAccess.getWorldVec3Pool().getVecFromPool(-f7, f9, -f8);
        avec3[5] = blockAccess.getWorldVec3Pool().getVecFromPool(f7, f9, -f8);
        avec3[6] = blockAccess.getWorldVec3Pool().getVecFromPool(f7, f9, f8);
        avec3[7] = blockAccess.getWorldVec3Pool().getVecFromPool(-f7, f9, f8);

        for (int k = 0; k < 8; k++)
        {
            avec3[k].zCoord += 0.0625D;

            if (flag1)
            {
                avec3[k].rotateAroundX(0.5235988F);
                avec3[k].yCoord -= 0.4375D;
            }
            else if (flag)
            {
                avec3[k].rotateAroundX(0.08726647F);
                avec3[k].yCoord -= 0.4375D;
            }
            else
            {
                avec3[k].rotateAroundX(-((float)Math.PI * 2F / 9F));
                avec3[k].yCoord -= 0.375D;
            }

            avec3[k].rotateAroundX(((float)Math.PI / 2F));

            if (j == 2)
            {
                avec3[k].rotateAroundY(0.0F);
            }

            if (j == 0)
            {
                avec3[k].rotateAroundY((float)Math.PI);
            }

            if (j == 1)
            {
                avec3[k].rotateAroundY(((float)Math.PI / 2F));
            }

            if (j == 3)
            {
                avec3[k].rotateAroundY(-((float)Math.PI / 2F));
            }

            avec3[k].xCoord += (double)par2 + 0.5D;
            avec3[k].yCoord += (float)par3 + 0.3125F;
            avec3[k].zCoord += (double)par4 + 0.5D;
        }

        Vec3 vec3 = null;
        Vec3 vec3_1 = null;
        Vec3 vec3_2 = null;
        Vec3 vec3_3 = null;
        int l = 7;
        int i1 = 9;
        int j1 = 9;
        int k1 = 16;

        for (int l1 = 0; l1 < 6; l1++)
        {
            if (l1 == 0)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[1];
                vec3_2 = avec3[2];
                vec3_3 = avec3[3];
                d = icon.getInterpolatedU(l);
                d1 = icon.getInterpolatedV(j1);
                d2 = icon.getInterpolatedU(i1);
                d3 = icon.getInterpolatedV(j1 + 2);
            }
            else if (l1 == 1)
            {
                vec3 = avec3[7];
                vec3_1 = avec3[6];
                vec3_2 = avec3[5];
                vec3_3 = avec3[4];
            }
            else if (l1 == 2)
            {
                vec3 = avec3[1];
                vec3_1 = avec3[0];
                vec3_2 = avec3[4];
                vec3_3 = avec3[5];
                d = icon.getInterpolatedU(l);
                d1 = icon.getInterpolatedV(j1);
                d2 = icon.getInterpolatedU(i1);
                d3 = icon.getInterpolatedV(k1);
            }
            else if (l1 == 3)
            {
                vec3 = avec3[2];
                vec3_1 = avec3[1];
                vec3_2 = avec3[5];
                vec3_3 = avec3[6];
            }
            else if (l1 == 4)
            {
                vec3 = avec3[3];
                vec3_1 = avec3[2];
                vec3_2 = avec3[6];
                vec3_3 = avec3[7];
            }
            else if (l1 == 5)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[3];
                vec3_2 = avec3[7];
                vec3_3 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d, d3);
            tessellator.addVertexWithUV(vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, d2, d3);
            tessellator.addVertexWithUV(vec3_2.xCoord, vec3_2.yCoord, vec3_2.zCoord, d2, d1);
            tessellator.addVertexWithUV(vec3_3.xCoord, vec3_3.yCoord, vec3_3.zCoord, d, d1);
        }

        float f10 = 0.09375F;
        float f11 = 0.09375F;
        float f12 = 0.03125F;
        avec3[0] = blockAccess.getWorldVec3Pool().getVecFromPool(-f10, 0.0D, -f11);
        avec3[1] = blockAccess.getWorldVec3Pool().getVecFromPool(f10, 0.0D, -f11);
        avec3[2] = blockAccess.getWorldVec3Pool().getVecFromPool(f10, 0.0D, f11);
        avec3[3] = blockAccess.getWorldVec3Pool().getVecFromPool(-f10, 0.0D, f11);
        avec3[4] = blockAccess.getWorldVec3Pool().getVecFromPool(-f10, f12, -f11);
        avec3[5] = blockAccess.getWorldVec3Pool().getVecFromPool(f10, f12, -f11);
        avec3[6] = blockAccess.getWorldVec3Pool().getVecFromPool(f10, f12, f11);
        avec3[7] = blockAccess.getWorldVec3Pool().getVecFromPool(-f10, f12, f11);

        for (int i2 = 0; i2 < 8; i2++)
        {
            avec3[i2].zCoord += 0.21875D;

            if (flag1)
            {
                avec3[i2].yCoord -= 0.09375D;
                avec3[i2].zCoord -= 0.16250000000000001D;
                avec3[i2].rotateAroundX(0.0F);
            }
            else if (flag)
            {
                avec3[i2].yCoord += 0.015625D;
                avec3[i2].zCoord -= 0.171875D;
                avec3[i2].rotateAroundX(0.1745329F);
            }
            else
            {
                avec3[i2].rotateAroundX(0.8726646F);
            }

            if (j == 2)
            {
                avec3[i2].rotateAroundY(0.0F);
            }

            if (j == 0)
            {
                avec3[i2].rotateAroundY((float)Math.PI);
            }

            if (j == 1)
            {
                avec3[i2].rotateAroundY(((float)Math.PI / 2F));
            }

            if (j == 3)
            {
                avec3[i2].rotateAroundY(-((float)Math.PI / 2F));
            }

            avec3[i2].xCoord += (double)par2 + 0.5D;
            avec3[i2].yCoord += (float)par3 + 0.3125F;
            avec3[i2].zCoord += (double)par4 + 0.5D;
        }

        int j2 = 5;
        int k2 = 11;
        int l2 = 3;
        int i3 = 9;

        for (int j3 = 0; j3 < 6; j3++)
        {
            if (j3 == 0)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[1];
                vec3_2 = avec3[2];
                vec3_3 = avec3[3];
                d = icon.getInterpolatedU(j2);
                d1 = icon.getInterpolatedV(l2);
                d2 = icon.getInterpolatedU(k2);
                d3 = icon.getInterpolatedV(i3);
            }
            else if (j3 == 1)
            {
                vec3 = avec3[7];
                vec3_1 = avec3[6];
                vec3_2 = avec3[5];
                vec3_3 = avec3[4];
            }
            else if (j3 == 2)
            {
                vec3 = avec3[1];
                vec3_1 = avec3[0];
                vec3_2 = avec3[4];
                vec3_3 = avec3[5];
                d = icon.getInterpolatedU(j2);
                d1 = icon.getInterpolatedV(l2);
                d2 = icon.getInterpolatedU(k2);
                d3 = icon.getInterpolatedV(l2 + 2);
            }
            else if (j3 == 3)
            {
                vec3 = avec3[2];
                vec3_1 = avec3[1];
                vec3_2 = avec3[5];
                vec3_3 = avec3[6];
            }
            else if (j3 == 4)
            {
                vec3 = avec3[3];
                vec3_1 = avec3[2];
                vec3_2 = avec3[6];
                vec3_3 = avec3[7];
            }
            else if (j3 == 5)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[3];
                vec3_2 = avec3[7];
                vec3_3 = avec3[4];
            }

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, d, d3);
            tessellator.addVertexWithUV(vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, d2, d3);
            tessellator.addVertexWithUV(vec3_2.xCoord, vec3_2.yCoord, vec3_2.zCoord, d2, d1);
            tessellator.addVertexWithUV(vec3_3.xCoord, vec3_3.yCoord, vec3_3.zCoord, d, d1);
        }

        if (flag)
        {
            double d4 = avec3[0].yCoord;
            float f13 = 0.03125F;
            float f14 = 0.5F - f13 / 2.0F;
            float f15 = f14 + f13;
            Icon icon1 = getBlockIcon(Block.tripWire);
            double d5 = icon.getMinU();
            double d6 = icon.getInterpolatedV(flag ? 2D : 0.0D);
            double d7 = icon.getMaxU();
            double d8 = icon.getInterpolatedV(flag ? 4D : 2D);
            double d9 = (double)(flag2 ? 3.5F : 1.5F) / 16D;
            float f6 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4) * 0.75F;
            tessellator.setColorOpaque_F(f6, f6, f6);

            if (j == 2)
            {
                tessellator.addVertexWithUV((float)par2 + f14, (double)par3 + d9, (double)par4 + 0.25D, d5, d6);
                tessellator.addVertexWithUV((float)par2 + f15, (double)par3 + d9, (double)par4 + 0.25D, d5, d8);
                tessellator.addVertexWithUV((float)par2 + f15, (double)par3 + d9, par4, d7, d8);
                tessellator.addVertexWithUV((float)par2 + f14, (double)par3 + d9, par4, d7, d6);
                tessellator.addVertexWithUV((float)par2 + f14, d4, (double)par4 + 0.5D, d5, d6);
                tessellator.addVertexWithUV((float)par2 + f15, d4, (double)par4 + 0.5D, d5, d8);
                tessellator.addVertexWithUV((float)par2 + f15, (double)par3 + d9, (double)par4 + 0.25D, d7, d8);
                tessellator.addVertexWithUV((float)par2 + f14, (double)par3 + d9, (double)par4 + 0.25D, d7, d6);
            }
            else if (j == 0)
            {
                tessellator.addVertexWithUV((float)par2 + f14, (double)par3 + d9, (double)par4 + 0.75D, d5, d6);
                tessellator.addVertexWithUV((float)par2 + f15, (double)par3 + d9, (double)par4 + 0.75D, d5, d8);
                tessellator.addVertexWithUV((float)par2 + f15, d4, (double)par4 + 0.5D, d7, d8);
                tessellator.addVertexWithUV((float)par2 + f14, d4, (double)par4 + 0.5D, d7, d6);
                tessellator.addVertexWithUV((float)par2 + f14, (double)par3 + d9, par4 + 1, d5, d6);
                tessellator.addVertexWithUV((float)par2 + f15, (double)par3 + d9, par4 + 1, d5, d8);
                tessellator.addVertexWithUV((float)par2 + f15, (double)par3 + d9, (double)par4 + 0.75D, d7, d8);
                tessellator.addVertexWithUV((float)par2 + f14, (double)par3 + d9, (double)par4 + 0.75D, d7, d6);
            }
            else if (j == 1)
            {
                tessellator.addVertexWithUV(par2, (double)par3 + d9, (float)par4 + f15, d5, d8);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d9, (float)par4 + f15, d7, d8);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d9, (float)par4 + f14, d7, d6);
                tessellator.addVertexWithUV(par2, (double)par3 + d9, (float)par4 + f14, d5, d6);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d9, (float)par4 + f15, d5, d8);
                tessellator.addVertexWithUV((double)par2 + 0.5D, d4, (float)par4 + f15, d7, d8);
                tessellator.addVertexWithUV((double)par2 + 0.5D, d4, (float)par4 + f14, d7, d6);
                tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d9, (float)par4 + f14, d5, d6);
            }
            else
            {
                tessellator.addVertexWithUV((double)par2 + 0.5D, d4, (float)par4 + f15, d5, d8);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d9, (float)par4 + f15, d7, d8);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d9, (float)par4 + f14, d7, d6);
                tessellator.addVertexWithUV((double)par2 + 0.5D, d4, (float)par4 + f14, d5, d6);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d9, (float)par4 + f15, d5, d8);
                tessellator.addVertexWithUV(par2 + 1, (double)par3 + d9, (float)par4 + f15, d7, d8);
                tessellator.addVertexWithUV(par2 + 1, (double)par3 + d9, (float)par4 + f14, d7, d6);
                tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d9, (float)par4 + f14, d5, d6);
            }
        }

        return true;
    }

    /**
     * Renders a trip wire block at the given coordinates
     */
    public boolean renderBlockTripWire(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSide(par1Block, 0);
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = (i & 4) == 4;
        boolean flag1 = (i & 2) == 2;

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = par1Block.getBlockBrightness(blockAccess, par2, par3, par4) * 0.75F;
        tessellator.setColorOpaque_F(f, f, f);
        double d = icon.getMinU();
        double d1 = icon.getInterpolatedV(flag ? 2D : 0.0D);
        double d2 = icon.getMaxU();
        double d3 = icon.getInterpolatedV(flag ? 4D : 2D);
        double d4 = (double)(flag1 ? 3.5F : 1.5F) / 16D;
        boolean flag2 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, i, 1);
        boolean flag3 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, i, 3);
        boolean flag4 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, i, 2);
        boolean flag5 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, i, 0);
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
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d, d1);
        }

        if (flag4 || flag5 && !flag3 && !flag2)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.25D, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.25D, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d, d1);
        }

        if (flag5 || flag4 && !flag3 && !flag2)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.5D, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.5D, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d, d1);
        }

        if (flag5)
        {
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4 + 1, d, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4 + 1, d, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, (double)par4 + 0.75D, d2, d1);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, (double)par4 + 0.75D, d2, d3);
            tessellator.addVertexWithUV((float)par2 + f3, (double)par3 + d4, par4 + 1, d, d3);
            tessellator.addVertexWithUV((float)par2 + f2, (double)par3 + d4, par4 + 1, d, d1);
        }

        if (flag2)
        {
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV(par2, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        if (flag2 || flag3 && !flag4 && !flag5)
        {
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV((double)par2 + 0.25D, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        if (flag3 || flag2 && !flag4 && !flag5)
        {
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV((double)par2 + 0.5D, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        if (flag3)
        {
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d, d3);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f2, d, d1);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f2, d2, d1);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, (float)par4 + f3, d2, d3);
            tessellator.addVertexWithUV((double)par2 + 0.75D, (double)par3 + d4, (float)par4 + f3, d, d3);
        }

        return true;
    }

    /**
     * Renders a fire block at the given coordinates
     */
    public boolean renderBlockFire(BlockFire par1BlockFire, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = par1BlockFire.getFireIcon(0);
        Icon icon1 = par1BlockFire.getFireIcon(1);
        Icon icon2 = icon;

        if (hasOverrideBlockTexture())
        {
            icon2 = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.setBrightness(par1BlockFire.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }else{
            float ff = par1BlockFire.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        double d = icon2.getMinU();
        double d2 = icon2.getMinV();
        double d4 = icon2.getMaxU();
        double d6 = icon2.getMaxV();
        float f = 1.4F;

        if (blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || Block.fire.canBlockCatchFire(blockAccess, par2, par3 - 1, par4))
        {
            double d8 = (double)par2 + 0.5D + 0.20000000000000001D;
            double d9 = ((double)par2 + 0.5D) - 0.20000000000000001D;
            double d12 = (double)par4 + 0.5D + 0.20000000000000001D;
            double d14 = ((double)par4 + 0.5D) - 0.20000000000000001D;
            double d16 = ((double)par2 + 0.5D) - 0.29999999999999999D;
            double d18 = (double)par2 + 0.5D + 0.29999999999999999D;
            double d20 = ((double)par4 + 0.5D) - 0.29999999999999999D;
            double d22 = (double)par4 + 0.5D + 0.29999999999999999D;
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 1, d4, d2);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 1, d4, d6);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 0, d, d6);
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 0, d, d2);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 0, d4, d2);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 0, d4, d6);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 1, d, d6);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 1, d, d2);
            Icon icon3 = icon1;
            d = icon3.getMinU();
            d2 = icon3.getMinV();
            d4 = icon3.getMaxU();
            d6 = icon3.getMaxV();
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d22, d4, d2);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d14, d4, d6);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d14, d, d6);
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d22, d, d2);
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d20, d4, d2);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d12, d4, d6);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d12, d, d6);
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d20, d, d2);
            d8 = ((double)par2 + 0.5D) - 0.5D;
            d9 = (double)par2 + 0.5D + 0.5D;
            d12 = ((double)par4 + 0.5D) - 0.5D;
            d14 = (double)par4 + 0.5D + 0.5D;
            d16 = ((double)par2 + 0.5D) - 0.40000000000000002D;
            d18 = (double)par2 + 0.5D + 0.40000000000000002D;
            d20 = ((double)par4 + 0.5D) - 0.40000000000000002D;
            d22 = (double)par4 + 0.5D + 0.40000000000000002D;
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 0, d, d2);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 0, d, d6);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 1, d4, d6);
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 1, d4, d2);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 1, d, d2);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 1, d, d6);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 0, d4, d6);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 0, d4, d2);
            icon3 = icon;
            d = icon3.getMinU();
            d2 = icon3.getMinV();
            d4 = icon3.getMaxU();
            d6 = icon3.getMaxV();
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d22, d, d2);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d14, d, d6);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d14, d4, d6);
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d22, d4, d2);
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d20, d, d2);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d12, d, d6);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d12, d4, d6);
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d20, d4, d2);
        }
        else
        {
            float f2 = 0.2F;
            float f3 = 0.0625F;

            if ((par2 + par3 + par4 & 1) == 1)
            {
                Icon icon4 = icon1;
                d = icon4.getMinU();
                d2 = icon4.getMinV();
                d4 = icon4.getMaxU();
                d6 = icon4.getMaxV();
            }

            if ((par2 / 2 + par3 / 2 + par4 / 2 & 1) == 1)
            {
                double d10 = d4;
                d4 = d;
                d = d10;
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2 - 1, par3, par4))
            {
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 1, d4, d2);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 1, d4, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 0, d, d2);
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 0, d, d2);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 1, d4, d6);
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 1, d4, d2);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2 + 1, par3, par4))
            {
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 0, d, d2);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 1, d4, d6);
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 1, d4, d2);
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 1, d4, d2);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 1, d4, d6);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 0, d, d2);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2, par3, par4 - 1))
            {
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)par4 + f2, d4, d2);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d4, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)par4 + f2, d, d2);
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)par4 + f2, d, d2);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d4, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)par4 + f2, d4, d2);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2, par3, par4 + 1))
            {
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)(par4 + 1) - f2, d, d2);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, (par4 + 1) - 0, d, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, (par4 + 1) - 0, d4, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)(par4 + 1) - f2, d4, d2);
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)(par4 + 1) - f2, d4, d2);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, (par4 + 1) - 0, d4, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, (par4 + 1) - 0, d, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)(par4 + 1) - f2, d, d2);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2, par3 + 1, par4))
            {
                double d11 = (double)par2 + 0.5D + 0.5D;
                double d13 = ((double)par2 + 0.5D) - 0.5D;
                double d15 = (double)par4 + 0.5D + 0.5D;
                double d17 = ((double)par4 + 0.5D) - 0.5D;
                double d19 = ((double)par2 + 0.5D) - 0.5D;
                double d21 = (double)par2 + 0.5D + 0.5D;
                double d23 = ((double)par4 + 0.5D) - 0.5D;
                double d24 = (double)par4 + 0.5D + 0.5D;
                Icon icon5 = icon;
                double d1 = icon5.getMinU();
                double d3 = icon5.getMinV();
                double d5 = icon5.getMaxU();
                double d7 = icon5.getMaxV();
                par3++;
                float f1 = -0.2F;

                if ((par2 + par3 + par4 & 1) == 0)
                {
                    tessellator.addVertexWithUV(d19, (float)par3 + f1, par4 + 0, d5, d3);
                    tessellator.addVertexWithUV(d11, par3 + 0, par4 + 0, d5, d7);
                    tessellator.addVertexWithUV(d11, par3 + 0, par4 + 1, d1, d7);
                    tessellator.addVertexWithUV(d19, (float)par3 + f1, par4 + 1, d1, d3);
                    Icon icon6 = icon1;
                    d1 = icon6.getMinU();
                    d3 = icon6.getMinV();
                    d5 = icon6.getMaxU();
                    d7 = icon6.getMaxV();
                    tessellator.addVertexWithUV(d21, (float)par3 + f1, par4 + 1, d5, d3);
                    tessellator.addVertexWithUV(d13, par3 + 0, par4 + 1, d5, d7);
                    tessellator.addVertexWithUV(d13, par3 + 0, par4 + 0, d1, d7);
                    tessellator.addVertexWithUV(d21, (float)par3 + f1, par4 + 0, d1, d3);
                }
                else
                {
                    tessellator.addVertexWithUV(par2 + 0, (float)par3 + f1, d24, d5, d3);
                    tessellator.addVertexWithUV(par2 + 0, par3 + 0, d17, d5, d7);
                    tessellator.addVertexWithUV(par2 + 1, par3 + 0, d17, d1, d7);
                    tessellator.addVertexWithUV(par2 + 1, (float)par3 + f1, d24, d1, d3);
                    Icon icon7 = icon1;
                    d1 = icon7.getMinU();
                    d3 = icon7.getMinV();
                    d5 = icon7.getMaxU();
                    d7 = icon7.getMaxV();
                    tessellator.addVertexWithUV(par2 + 1, (float)par3 + f1, d23, d5, d3);
                    tessellator.addVertexWithUV(par2 + 1, par3 + 0, d15, d5, d7);
                    tessellator.addVertexWithUV(par2 + 0, par3 + 0, d15, d1, d7);
                    tessellator.addVertexWithUV(par2 + 0, (float)par3 + f1, d23, d1, d3);
                }
            }
        }

        return true;
    }

    /**
     * Renders a redstone wire block at the given coordinates
     */
    public boolean renderBlockRedstoneWire(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        Icon icon = BlockRedstoneWire.getRedstoneWireIcon("cross");
        Icon icon1 = BlockRedstoneWire.getRedstoneWireIcon("line");
        Icon icon2 = BlockRedstoneWire.getRedstoneWireIcon("cross_overlay");
        Icon icon3 = BlockRedstoneWire.getRedstoneWireIcon("line_overlay");
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        float f1 = (float)i / 15F;
        float f2 = f1 * 0.6F + 0.4F;

        if (i == 0)
        {
            f2 = 0.3F;
        }

        float f3 = f1 * f1 * 0.7F - 0.5F;
        float f4 = f1 * f1 * 0.6F - 0.7F;

        if (f3 < 0.0F)
        {
            f3 = 0.0F;
        }

        if (f4 < 0.0F)
        {
            f4 = 0.0F;
        }

        tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
        double d = 0.015625D;
        double d1 = 0.015625D;
        boolean flag = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 - 1, par3, par4, 1) || !blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 - 1, par3 - 1, par4, -1);
        boolean flag1 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 + 1, par3, par4, 3) || !blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 + 1, par3 - 1, par4, -1);
        boolean flag2 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3, par4 - 1, 2) || !blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 - 1, -1);
        boolean flag3 = BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3, par4 + 1, 0) || !blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 - 1, par4 + 1, -1);

        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 - 1, par3 + 1, par4, -1))
            {
                flag = true;
            }

            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2 + 1, par3 + 1, par4, -1))
            {
                flag1 = true;
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 - 1, -1))
            {
                flag2 = true;
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && BlockRedstoneWire.isPowerProviderOrWire(blockAccess, par2, par3 + 1, par4 + 1, -1))
            {
                flag3 = true;
            }
        }

        float f5 = par2 + 0;
        float f6 = par2 + 1;
        float f7 = par4 + 0;
        float f8 = par4 + 1;
        byte byte0 = 0;

        if ((flag || flag1) && !flag2 && !flag3)
        {
            byte0 = 1;
        }

        if ((flag2 || flag3) && !flag1 && !flag)
        {
            byte0 = 2;
        }

        if (byte0 == 0)
        {
            int j = 0;
            int k = 0;
            int l = 16;
            int i1 = 16;
            byte byte1 = 5;

            if (!flag)
            {
                f5 += 0.3125F;
            }

            if (!flag)
            {
                j += 5;
            }

            if (!flag1)
            {
                f6 -= 0.3125F;
            }

            if (!flag1)
            {
                l -= 5;
            }

            if (!flag2)
            {
                f7 += 0.3125F;
            }

            if (!flag2)
            {
                k += 5;
            }

            if (!flag3)
            {
                f8 -= 0.3125F;
            }

            if (!flag3)
            {
                i1 -= 5;
            }

            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon.getInterpolatedU(l), icon.getInterpolatedV(i1));
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon.getInterpolatedU(l), icon.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon.getInterpolatedU(j), icon.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon.getInterpolatedU(j), icon.getInterpolatedV(i1));
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon2.getInterpolatedU(l), icon2.getInterpolatedV(i1));
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon2.getInterpolatedU(l), icon2.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon2.getInterpolatedU(j), icon2.getInterpolatedV(k));
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon2.getInterpolatedU(j), icon2.getInterpolatedV(i1));
        }
        else if (byte0 == 1)
        {
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon1.getMaxU(), icon1.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon1.getMaxU(), icon1.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon1.getMinU(), icon1.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon1.getMinU(), icon1.getMaxV());
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon3.getMaxU(), icon3.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon3.getMaxU(), icon3.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon3.getMinU(), icon3.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon3.getMinU(), icon3.getMaxV());
        }
        else
        {
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon1.getMaxU(), icon1.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon1.getMinU(), icon1.getMaxV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon1.getMinU(), icon1.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon1.getMaxU(), icon1.getMinV());
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, icon3.getMaxU(), icon3.getMaxV());
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, icon3.getMinU(), icon3.getMaxV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, icon3.getMinU(), icon3.getMinV());
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, icon3.getMaxU(), icon3.getMinV());
        }

        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            float f9 = 0.021875F;

            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, icon1.getMinU(), icon1.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon1.getMaxU(), icon1.getMaxV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, icon3.getMinU(), icon3.getMinV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon3.getMaxU(), icon3.getMaxV());
            }

            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon1.getMaxU(), icon1.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, icon1.getMinU(), icon1.getMinV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, icon3.getMaxU(), icon3.getMaxV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, icon3.getMinU(), icon3.getMinV());
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon1.getMaxU(), icon1.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, icon1.getMinU(), icon1.getMinV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon3.getMaxU(), icon3.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, icon3.getMinU(), icon3.getMinV());
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon1.getMaxU(), icon1.getMinV());
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, icon1.getMinU(), icon1.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, icon1.getMinU(), icon1.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon1.getMaxU(), icon1.getMaxV());
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon3.getMaxU(), icon3.getMinV());
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, icon3.getMinU(), icon3.getMinV());
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, icon3.getMinU(), icon3.getMaxV());
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, icon3.getMaxU(), icon3.getMaxV());
            }
        }

        return true;
    }

    /**
     * Renders a minecart track block at the given coordinates
     */
    public boolean renderBlockMinecartTrack(BlockRailBase par1BlockRailBase, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        Icon icon = getBlockIconFromSideAndMetadata(par1BlockRailBase, 0, i);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        if (par1BlockRailBase.isPowered())
        {
            i &= 7;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockRailBase.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float f = par1BlockRailBase.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(f, f, f);
        }
        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        double d4 = 0.0625D;
        double d5 = par2 + 1;
        double d6 = par2 + 1;
        double d7 = par2 + 0;
        double d8 = par2 + 0;
        double d9 = par4 + 0;
        double d10 = par4 + 1;
        double d11 = par4 + 1;
        double d12 = par4 + 0;
        double d13 = (double)par3 + d4;
        double d14 = (double)par3 + d4;
        double d15 = (double)par3 + d4;
        double d16 = (double)par3 + d4;

        if (i == 1 || i == 2 || i == 3 || i == 7)
        {
            d5 = d8 = par2 + 1;
            d6 = d7 = par2 + 0;
            d9 = d10 = par4 + 1;
            d11 = d12 = par4 + 0;
        }
        else if (i == 8)
        {
            d5 = d6 = par2 + 0;
            d7 = d8 = par2 + 1;
            d9 = d12 = par4 + 1;
            d10 = d11 = par4 + 0;
        }
        else if (i == 9)
        {
            d5 = d8 = par2 + 0;
            d6 = d7 = par2 + 1;
            d9 = d10 = par4 + 0;
            d11 = d12 = par4 + 1;
        }

        if (i == 2 || i == 4)
        {
            d13++;
            d16++;
        }
        else if (i == 3 || i == 5)
        {
            d14++;
            d15++;
        }

        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d, d1);
        tessellator.addVertexWithUV(d8, d16, d12, d, d1);
        tessellator.addVertexWithUV(d7, d15, d11, d, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d2, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d2, d1);
        return true;
    }

    /**
     * Renders a ladder block at the given coordinates
     */
    public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSide(par1Block, 0);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float d = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        tessellator.setColorOpaque_F(d, d, d);
        d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        double d4 = 0.0D;
        double d5 = 0.05000000074505806D;

        if (i == 5)
        {
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 1) + d4, (double)(par4 + 1) + d4, d, d1);
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 0) - d4, (double)(par4 + 1) + d4, d, d3);
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 0) - d4, (double)(par4 + 0) - d4, d2, d3);
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 1) + d4, (double)(par4 + 0) - d4, d2, d1);
        }

        if (i == 4)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 0) - d4, (double)(par4 + 1) + d4, d2, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 1) + d4, (double)(par4 + 1) + d4, d2, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 1) + d4, (double)(par4 + 0) - d4, d, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 0) - d4, (double)(par4 + 0) - d4, d, d3);
        }

        if (i == 3)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 0) - d4, (double)par4 + d5, d2, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 1) + d4, (double)par4 + d5, d2, d1);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 1) + d4, (double)par4 + d5, d, d1);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 0) - d4, (double)par4 + d5, d, d3);
        }

        if (i == 2)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 1) + d4, (double)(par4 + 1) - d5, d, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 0) - d4, (double)(par4 + 1) - d5, d, d3);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 0) - d4, (double)(par4 + 1) - d5, d2, d3);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 1) + d4, (double)(par4 + 1) - d5, d2, d1);
        }

        return true;
    }

    /**
     * Render block vine
     */
    public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSide(par1Block, 0);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        int ii = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(ii >> 16 & 0xff) / 255F;
        float f2 = (float)(ii >> 8 & 0xff) / 255F;
        float f3 = (float)(ii & 0xff) / 255F;
        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        double d4 = 0.05000000074505806D;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);

        if ((i & 2) != 0)
        {
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 1, d, d1);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 1, d, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 0, d2, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 0, d2, d1);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 0, d2, d1);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 0, d2, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 1, d, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 1, d, d1);
        }

        if ((i & 8) != 0)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 1, d2, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 1, d2, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 0, d, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 0, d, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 0, d, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 0, d, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 1, d2, d1);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 1, d2, d3);
        }

        if ((i & 4) != 0)
        {
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + d4, d2, d3);
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)par4 + d4, d2, d1);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)par4 + d4, d, d1);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + d4, d, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + d4, d, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)par4 + d4, d, d1);
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)par4 + d4, d2, d1);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + d4, d2, d3);
        }

        if ((i & 1) != 0)
        {
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)(par4 + 1) - d4, d, d1);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - d4, d, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - d4, d2, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)(par4 + 1) - d4, d2, d1);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)(par4 + 1) - d4, d2, d1);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - d4, d2, d3);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - d4, d, d3);
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)(par4 + 1) - d4, d, d1);
        }

        if (blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            tessellator.addVertexWithUV(par2 + 1, (double)(par3 + 1) - d4, par4 + 0, d, d1);
            tessellator.addVertexWithUV(par2 + 1, (double)(par3 + 1) - d4, par4 + 1, d, d3);
            tessellator.addVertexWithUV(par2 + 0, (double)(par3 + 1) - d4, par4 + 1, d2, d3);
            tessellator.addVertexWithUV(par2 + 0, (double)(par3 + 1) - d4, par4 + 0, d2, d1);
        }

        return true;
    }

    public boolean renderBlockPane(BlockPane par1BlockPane, int par2, int par3, int par4)
    {
        int i = blockAccess.getHeight();
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockPane.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1BlockPane.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int j = par1BlockPane.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(j >> 16 & 0xff) / 255F;
        float f2 = (float)(j >> 8 & 0xff) / 255F;
        float f3 = (float)(j & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        Icon icon;
        Icon icon1;

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
            icon1 = overrideBlockTexture;
        }
        else
        {
            int k = blockAccess.getBlockMetadata(par2, par3, par4);
            icon = getBlockIconFromSideAndMetadata(par1BlockPane, 0, k);
            icon1 = par1BlockPane.getSideTextureIndex();
        }

        double d = icon.getMinU();
        double d1 = icon.getInterpolatedU(8D);
        double d2 = icon.getMaxU();
        double d3 = icon.getMinV();
        double d4 = icon.getMaxV();
        double d5 = icon1.getInterpolatedU(7D);
        double d6 = icon1.getInterpolatedU(9D);
        double d7 = icon1.getMinV();
        double d8 = icon1.getInterpolatedV(8D);
        double d9 = icon1.getMaxV();
        double d10 = par2;
        double d11 = (double)par2 + 0.5D;
        double d12 = par2 + 1;
        double d13 = par4;
        double d14 = (double)par4 + 0.5D;
        double d15 = par4 + 1;
        double d16 = ((double)par2 + 0.5D) - 0.0625D;
        double d17 = (double)par2 + 0.5D + 0.0625D;
        double d18 = ((double)par4 + 0.5D) - 0.0625D;
        double d19 = (double)par4 + 0.5D + 0.0625D;
        boolean flag = par1BlockPane.canThisPaneConnectToThisBlockID(blockAccess.getBlockId(par2, par3, par4 - 1));
        boolean flag1 = par1BlockPane.canThisPaneConnectToThisBlockID(blockAccess.getBlockId(par2, par3, par4 + 1));
        boolean flag2 = par1BlockPane.canThisPaneConnectToThisBlockID(blockAccess.getBlockId(par2 - 1, par3, par4));
        boolean flag3 = par1BlockPane.canThisPaneConnectToThisBlockID(blockAccess.getBlockId(par2 + 1, par3, par4));
        boolean flag4 = par1BlockPane.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1);
        boolean flag5 = par1BlockPane.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0);
        double d20 = 0.01D;
        double d21 = 0.0050000000000000001D;

        if (flag2 && flag3 || !flag2 && !flag3 && !flag && !flag1)
        {
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d2, d3);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d2, d3);

            if (flag4)
            {
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d9);
            }
            else
            {
                if (par3 < i - 1 && blockAccess.isAirBlock(par2 - 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                }

                if (par3 < i - 1 && blockAccess.isAirBlock(par2 + 1, par3 + 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d9);
            }
            else
            {
                if (par3 > 1 && blockAccess.isAirBlock(par2 - 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                    tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                }

                if (par3 > 1 && blockAccess.isAirBlock(par2 + 1, par3 - 1, par4))
                {
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                    tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                    tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
                }
            }
        }
        else if (flag2 && !flag3)
        {
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d10, par3 + 0, d14, d1, d4);
            tessellator.addVertexWithUV(d10, par3 + 1, d14, d1, d3);

            if (!flag1 && !flag)
            {
                tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
                tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
                tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
                tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
                tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
                tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
                tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
                tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
            }

            if (flag4 || par3 < i - 1 && blockAccess.isAirBlock(par2 - 1, par3 + 1, par4))
            {
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d10, (double)(par3 + 1) + 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
            }

            if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2 - 1, par3 - 1, par4))
            {
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d19, d6, d9);
                tessellator.addVertexWithUV(d10, (double)par3 - 0.01D, d18, d5, d9);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
            }
        }
        else if (!flag2 && flag3)
        {
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d2, d3);
            tessellator.addVertexWithUV(d12, par3 + 1, d14, d1, d3);
            tessellator.addVertexWithUV(d12, par3 + 0, d14, d1, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

            if (!flag1 && !flag)
            {
                tessellator.addVertexWithUV(d11, par3 + 1, d18, d5, d7);
                tessellator.addVertexWithUV(d11, par3 + 0, d18, d5, d9);
                tessellator.addVertexWithUV(d11, par3 + 0, d19, d6, d9);
                tessellator.addVertexWithUV(d11, par3 + 1, d19, d6, d7);
                tessellator.addVertexWithUV(d11, par3 + 1, d19, d5, d7);
                tessellator.addVertexWithUV(d11, par3 + 0, d19, d5, d9);
                tessellator.addVertexWithUV(d11, par3 + 0, d18, d6, d9);
                tessellator.addVertexWithUV(d11, par3 + 1, d18, d6, d7);
            }

            if (flag4 || par3 < i - 1 && blockAccess.isAirBlock(par2 + 1, par3 + 1, par4))
            {
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d11, (double)(par3 + 1) + 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d12, (double)(par3 + 1) + 0.01D, d18, d5, d7);
            }

            if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2 + 1, par3 - 1, par4))
            {
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d7);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d19, d6, d7);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d19, d6, d8);
                tessellator.addVertexWithUV(d11, (double)par3 - 0.01D, d18, d5, d8);
                tessellator.addVertexWithUV(d12, (double)par3 - 0.01D, d18, d5, d7);
            }
        }

        if (flag && flag1 || !flag2 && !flag3 && !flag && !flag1)
        {
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d2, d3);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);

            if (flag4)
            {
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d15, d6, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d13, d6, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d13, d5, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d15, d5, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d13, d6, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d15, d6, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d15, d5, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d13, d5, d9);
            }
            else
            {
                if (par3 < i - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d7);
                }

                if (par3 < i - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d15, d6, d8);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d15, d6, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d13, d6, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d13, d5, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d15, d5, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d13, d6, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d15, d6, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d15, d5, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d13, d5, d9);
            }
            else
            {
                if (par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d5, d7);
                }

                if (par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d15, d6, d8);
                }
            }
        }
        else if (flag && !flag1)
        {
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d13, d1, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d13, d1, d3);

            if (!flag3 && !flag2)
            {
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
                tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
                tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
            }

            if (flag4 || par3 < i - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
            {
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d13, d6, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d8);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d8);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d13, d5, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d7);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d13, d6, d8);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d13, d5, d8);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d7);
            }

            if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
            {
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d13, d6, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d6, d8);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d5, d8);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d13, d5, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d6, d7);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d13, d6, d8);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d13, d5, d8);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d5, d7);
            }
        }
        else if (!flag && flag1)
        {
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d1, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d1, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d2, d3);
            tessellator.addVertexWithUV(d11, par3 + 1, d15, d1, d3);
            tessellator.addVertexWithUV(d11, par3 + 0, d15, d1, d4);
            tessellator.addVertexWithUV(d11, par3 + 0, d14, d2, d4);
            tessellator.addVertexWithUV(d11, par3 + 1, d14, d2, d3);

            if (!flag3 && !flag2)
            {
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                tessellator.addVertexWithUV(d17, par3 + 0, d14, d5, d9);
                tessellator.addVertexWithUV(d16, par3 + 0, d14, d6, d9);
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 0, d14, d5, d9);
                tessellator.addVertexWithUV(d17, par3 + 0, d14, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d7);
            }

            if (flag4 || par3 < i - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
            {
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d8);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d15, d5, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d15, d6, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d8);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d15, d5, d8);
                tessellator.addVertexWithUV(d16, (double)(par3 + 1) + 0.0050000000000000001D, d14, d5, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d14, d6, d9);
                tessellator.addVertexWithUV(d17, (double)(par3 + 1) + 0.0050000000000000001D, d15, d6, d8);
            }

            if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
            {
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d5, d8);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d15, d5, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d15, d6, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d6, d8);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d15, d5, d8);
                tessellator.addVertexWithUV(d16, (double)par3 - 0.0050000000000000001D, d14, d5, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d14, d6, d9);
                tessellator.addVertexWithUV(d17, (double)par3 - 0.0050000000000000001D, d15, d6, d8);
            }
        }

        return true;
    }

    /**
     * Renders any block requiring croseed squares such as reeds, flowers, and mushrooms
     */
    public boolean renderCrossedSquares(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int i = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f2 = (float)(i >> 8 & 0xff) / 255F;
        float f3 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        double d = par2;
        double d1 = par3;
        double d2 = par4;

        if (par1Block == Block.tallGrass)
        {
            long l = (long)(par2 * 0x2fc20f) ^(long)par4 * 0x6ebfff5L ^(long)par3;
            l = l * l * 0x285b825L + l * 11L;
            d += ((double)((float)(l >> 16 & 15L) / 15F) - 0.5D) * 0.5D;
            d1 += ((double)((float)(l >> 20 & 15L) / 15F) - 1.0D) * 0.20000000000000001D;
            d2 += ((double)((float)(l >> 24 & 15L) / 15F) - 0.5D) * 0.5D;
        }

        drawCrossedSquares(par1Block, blockAccess.getBlockMetadata(par2, par3, par4), d, d1, d2, 1.0F);
        return true;
    }

    /**
     * Render block stem
     */
    public boolean renderBlockStem(Block par1Block, int par2, int par3, int par4)
    {
        BlockStem blockstem = (BlockStem)par1Block;
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(blockstem.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int i = blockstem.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f2 = (float)(i >> 8 & 0xff) / 255F;
        float f3 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        blockstem.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);
        int j = blockstem.getState(blockAccess, par2, par3, par4);

        if (j < 0)
        {
            renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(par2, par3, par4), renderMaxY, par2, (float)par3 - 0.0625F, par4);
        }
        else
        {
            renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(par2, par3, par4), 0.5D, par2, (float)par3 - 0.0625F, par4);
            renderBlockStemBig(blockstem, blockAccess.getBlockMetadata(par2, par3, par4), j, renderMaxY, par2, (float)par3 - 0.0625F, par4);
        }

        return true;
    }

    /**
     * Render block crops
     */
    public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        renderBlockCropsImpl(par1Block, blockAccess.getBlockMetadata(par2, par3, par4), par2, (float)par3 - 0.0625F, par4);
        return true;
    }

    /**
     * Renders a torch at the given coordinates, with the base slanting at the given delta
     */
    public void renderTorchAtAngle(Block par1Block, double par2, double par4, double par6, double par8, double par10, int par12)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSideAndMetadata(par1Block, 0, par12);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        double d4 = icon.getInterpolatedU(7D);
        double d5 = icon.getInterpolatedV(6D);
        double d6 = icon.getInterpolatedU(9D);
        double d7 = icon.getInterpolatedV(8D);
        double d8 = icon.getInterpolatedU(7D);
        double d9 = icon.getInterpolatedV(13D);
        double d10 = icon.getInterpolatedU(9D);
        double d11 = icon.getInterpolatedV(15D);
        par2 += 0.5D;
        par6 += 0.5D;
        double d12 = par2 - 0.5D;
        double d13 = par2 + 0.5D;
        double d14 = par6 - 0.5D;
        double d15 = par6 + 0.5D;
        double d16 = 0.0625D;
        double d17 = 0.625D;
        tessellator.addVertexWithUV((par2 + par8 * (1.0D - d17)) - d16, par4 + d17, (par6 + par10 * (1.0D - d17)) - d16, d4, d5);
        tessellator.addVertexWithUV((par2 + par8 * (1.0D - d17)) - d16, par4 + d17, par6 + par10 * (1.0D - d17) + d16, d4, d7);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d17) + d16, par4 + d17, par6 + par10 * (1.0D - d17) + d16, d6, d7);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d17) + d16, par4 + d17, (par6 + par10 * (1.0D - d17)) - d16, d6, d5);
        tessellator.addVertexWithUV(par2 + d16 + par8, par4, (par6 - d16) + par10, d10, d9);
        tessellator.addVertexWithUV(par2 + d16 + par8, par4, par6 + d16 + par10, d10, d11);
        tessellator.addVertexWithUV((par2 - d16) + par8, par4, par6 + d16 + par10, d8, d11);
        tessellator.addVertexWithUV((par2 - d16) + par8, par4, (par6 - d16) + par10, d8, d9);
        tessellator.addVertexWithUV(par2 - d16, par4 + 1.0D, d14, d, d1);
        tessellator.addVertexWithUV((par2 - d16) + par8, par4 + 0.0D, d14 + par10, d, d3);
        tessellator.addVertexWithUV((par2 - d16) + par8, par4 + 0.0D, d15 + par10, d2, d3);
        tessellator.addVertexWithUV(par2 - d16, par4 + 1.0D, d15, d2, d1);
        tessellator.addVertexWithUV(par2 + d16, par4 + 1.0D, d15, d, d1);
        tessellator.addVertexWithUV(par2 + par8 + d16, par4 + 0.0D, d15 + par10, d, d3);
        tessellator.addVertexWithUV(par2 + par8 + d16, par4 + 0.0D, d14 + par10, d2, d3);
        tessellator.addVertexWithUV(par2 + d16, par4 + 1.0D, d14, d2, d1);
        tessellator.addVertexWithUV(d12, par4 + 1.0D, par6 + d16, d, d1);
        tessellator.addVertexWithUV(d12 + par8, par4 + 0.0D, par6 + d16 + par10, d, d3);
        tessellator.addVertexWithUV(d13 + par8, par4 + 0.0D, par6 + d16 + par10, d2, d3);
        tessellator.addVertexWithUV(d13, par4 + 1.0D, par6 + d16, d2, d1);
        tessellator.addVertexWithUV(d13, par4 + 1.0D, par6 - d16, d, d1);
        tessellator.addVertexWithUV(d13 + par8, par4 + 0.0D, (par6 - d16) + par10, d, d3);
        tessellator.addVertexWithUV(d12 + par8, par4 + 0.0D, (par6 - d16) + par10, d2, d3);
        tessellator.addVertexWithUV(d12, par4 + 1.0D, par6 - d16, d2, d1);
    }

    /**
     * Utility function to draw crossed swuares
     */
    public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSideAndMetadata(par1Block, 0, par2);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        double d4 = 0.45000000000000001D * (double)par9;
        double d5 = (par3 + 0.5D) - d4;
        double d6 = par3 + 0.5D + d4;
        double d7 = (par7 + 0.5D) - d4;
        double d8 = par7 + 0.5D + d4;
        tessellator.addVertexWithUV(d5, par5 + (double)par9, d7, d, d1);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d6, par5 + 0.0D, d8, d2, d3);
        tessellator.addVertexWithUV(d6, par5 + (double)par9, d8, d2, d1);
        tessellator.addVertexWithUV(d6, par5 + (double)par9, d8, d, d1);
        tessellator.addVertexWithUV(d6, par5 + 0.0D, d8, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d5, par5 + (double)par9, d7, d2, d1);
        tessellator.addVertexWithUV(d5, par5 + (double)par9, d8, d, d1);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d8, d, d3);
        tessellator.addVertexWithUV(d6, par5 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d6, par5 + (double)par9, d7, d2, d1);
        tessellator.addVertexWithUV(d6, par5 + (double)par9, d7, d, d1);
        tessellator.addVertexWithUV(d6, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d8, d2, d3);
        tessellator.addVertexWithUV(d5, par5 + (double)par9, d8, d2, d1);
    }

    /**
     * Render block stem small
     */
    public void renderBlockStemSmall(Block par1Block, int par2, double par3, double par5, double par7, double par9)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSideAndMetadata(par1Block, 0, par2);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getInterpolatedV(par3 * 16D);
        double d4 = (par5 + 0.5D) - 0.44999998807907104D;
        double d5 = par5 + 0.5D + 0.44999998807907104D;
        double d6 = (par9 + 0.5D) - 0.44999998807907104D;
        double d7 = par9 + 0.5D + 0.44999998807907104D;
        tessellator.addVertexWithUV(d4, par7 + par3, d6, d, d1);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d5, par7 + par3, d7, d2, d1);
        tessellator.addVertexWithUV(d5, par7 + par3, d7, d, d1);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d6, d2, d3);
        tessellator.addVertexWithUV(d4, par7 + par3, d6, d2, d1);
        tessellator.addVertexWithUV(d4, par7 + par3, d7, d, d1);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d6, d2, d3);
        tessellator.addVertexWithUV(d5, par7 + par3, d6, d2, d1);
        tessellator.addVertexWithUV(d5, par7 + par3, d6, d, d1);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d4, par7 + par3, d7, d2, d1);
    }

    /**
     * Render BlockLilyPad
     */
    public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSide(par1Block, 1);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        float f = 0.015625F;
        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        long l = (long)(par2 * 0x2fc20f) ^(long)par4 * 0x6ebfff5L ^(long)par3;
        l = l * l * 0x285b825L + l * 11L;
        int i = (int)(l >> 16 & 3L);
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f1 = (float)par2 + 0.5F;
        float f2 = (float)par4 + 0.5F;
        float f3 = (float)(i & 1) * 0.5F * (float)(1 - ((i / 2) % 2) * 2);
        float f4 = (float)(i + 1 & 1) * 0.5F * (float)(1 - (((i + 1) / 2) % 2) * 2);
        if (!Minecraft.oldlighting){
            tessellator.setColorOpaque_I(par1Block.getBlockColor());
        }else{
            float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
            int c = par1Block.getBlockColor();
            float r = (float)(c >> 16 & 0xff) / 255F;
            float g = (float)(c >> 8 & 0xff) / 255F;
            float b = (float)(c & 0xff) / 255F;
            tessellator.setColorOpaque_F(r * ff, g * ff, b * ff);
        }
        tessellator.addVertexWithUV((f1 + f3) - f4, (float)par3 + f, f2 + f3 + f4, d, d1);
        tessellator.addVertexWithUV(f1 + f3 + f4, (float)par3 + f, (f2 - f3) + f4, d2, d1);
        tessellator.addVertexWithUV((f1 - f3) + f4, (float)par3 + f, f2 - f3 - f4, d2, d3);
        tessellator.addVertexWithUV(f1 - f3 - f4, (float)par3 + f, (f2 + f3) - f4, d, d3);
        if (!Minecraft.oldlighting){
            tessellator.setColorOpaque_I((par1Block.getBlockColor() & 0xfefefe) >> 1);
        }else{
            float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
            int c = (par1Block.getBlockColor() & 0xfefefe) >> 1;
            float r = (float)(c >> 16 & 0xff) / 255F;
            float g = (float)(c >> 8 & 0xff) / 255F;
            float b = (float)(c & 0xff) / 255F;
            tessellator.setColorOpaque_F(r * ff, g * ff, b * ff);
        }
        tessellator.addVertexWithUV(f1 - f3 - f4, (float)par3 + f, (f2 + f3) - f4, d, d3);
        tessellator.addVertexWithUV((f1 - f3) + f4, (float)par3 + f, f2 - f3 - f4, d2, d3);
        tessellator.addVertexWithUV(f1 + f3 + f4, (float)par3 + f, (f2 - f3) + f4, d2, d1);
        tessellator.addVertexWithUV((f1 + f3) - f4, (float)par3 + f, f2 + f3 + f4, d, d1);
        return true;
    }

    /**
     * Render block stem big
     */
    public void renderBlockStemBig(BlockStem par1BlockStem, int par2, int par3, double par4, double par6, double par8, double par10)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = par1BlockStem.getStemIcon();

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        double d4 = (par6 + 0.5D) - 0.5D;
        double d5 = par6 + 0.5D + 0.5D;
        double d6 = (par10 + 0.5D) - 0.5D;
        double d7 = par10 + 0.5D + 0.5D;
        double d8 = par6 + 0.5D;
        double d9 = par10 + 0.5D;

        if (((par3 + 1) / 2) % 2 == 1)
        {
            double d10 = d2;
            d2 = d;
            d = d10;
        }

        if (par3 < 2)
        {
            tessellator.addVertexWithUV(d4, par8 + par4, d9, d, d1);
            tessellator.addVertexWithUV(d4, par8 + 0.0D, d9, d, d3);
            tessellator.addVertexWithUV(d5, par8 + 0.0D, d9, d2, d3);
            tessellator.addVertexWithUV(d5, par8 + par4, d9, d2, d1);
            tessellator.addVertexWithUV(d5, par8 + par4, d9, d2, d1);
            tessellator.addVertexWithUV(d5, par8 + 0.0D, d9, d2, d3);
            tessellator.addVertexWithUV(d4, par8 + 0.0D, d9, d, d3);
            tessellator.addVertexWithUV(d4, par8 + par4, d9, d, d1);
        }
        else
        {
            tessellator.addVertexWithUV(d8, par8 + par4, d7, d, d1);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d7, d, d3);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d6, d2, d3);
            tessellator.addVertexWithUV(d8, par8 + par4, d6, d2, d1);
            tessellator.addVertexWithUV(d8, par8 + par4, d6, d2, d1);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d6, d2, d3);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d7, d, d3);
            tessellator.addVertexWithUV(d8, par8 + par4, d7, d, d1);
        }
    }

    /**
     * Render block crops implementation
     */
    public void renderBlockCropsImpl(Block par1Block, int par2, double par3, double par5, double par7)
    {
        Tessellator tessellator = Tessellator.instance;
        Icon icon = getBlockIconFromSideAndMetadata(par1Block, 0, par2);

        if (hasOverrideBlockTexture())
        {
            icon = overrideBlockTexture;
        }

        double d = icon.getMinU();
        double d1 = icon.getMinV();
        double d2 = icon.getMaxU();
        double d3 = icon.getMaxV();
        double d4 = (par3 + 0.5D) - 0.25D;
        double d5 = par3 + 0.5D + 0.25D;
        double d6 = (par7 + 0.5D) - 0.5D;
        double d7 = par7 + 0.5D + 0.5D;
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d1);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d2, d1);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d1);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d2, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d2, d1);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d1);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d2, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d2, d1);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d1);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d2, d1);
        d4 = (par3 + 0.5D) - 0.5D;
        d5 = par3 + 0.5D + 0.5D;
        d6 = (par7 + 0.5D) - 0.25D;
        d7 = par7 + 0.5D + 0.25D;
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d1);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d2, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d2, d1);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d1);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d2, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d2, d1);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d1);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d2, d1);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d1);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d2, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d2, d1);
    }

    /**
     * Renders a block based on the BlockFluids class at the given coordinates
     */
    public boolean renderBlockFluids(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
        float f = (float)(i >> 16 & 0xff) / 255F;
        float f1 = (float)(i >> 8 & 0xff) / 255F;
        float f2 = (float)(i & 0xff) / 255F;
        boolean flag = par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1);
        boolean flag1 = par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0);
        boolean aflag[] = new boolean[4];
        aflag[0] = par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2);
        aflag[1] = par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3);
        aflag[2] = par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4);
        aflag[3] = par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5);

        if (!flag && !flag1 && !aflag[0] && !aflag[1] && !aflag[2] && !aflag[3])
        {
            return false;
        }

        boolean flag2 = false;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        double d = 0.0D;
        double d1 = 1.0D;
        Material material = par1Block.blockMaterial;
        int j = blockAccess.getBlockMetadata(par2, par3, par4);
        double d2 = getFluidHeight(par2, par3, par4, material);
        double d3 = getFluidHeight(par2, par3, par4 + 1, material);
        double d4 = getFluidHeight(par2 + 1, par3, par4 + 1, material);
        double d5 = getFluidHeight(par2 + 1, par3, par4, material);
        double d6 = 0.0010000000474974513D;

        if (renderAllFaces || flag)
        {
            flag2 = true;
            Icon icon = getBlockIconFromSideAndMetadata(par1Block, 1, j);
            float f8 = (float)BlockFluid.getFlowDirection(blockAccess, par2, par3, par4, material);

            if (f8 > -999F)
            {
                icon = getBlockIconFromSideAndMetadata(par1Block, 2, j);
            }

            d2 -= d6;
            d3 -= d6;
            d4 -= d6;
            d5 -= d6;
            double d7;
            double d8;
            double d10;
            double d12;
            double d14;
            double d16;
            double d18;
            double d20;

            if (f8 < -999F)
            {
                d7 = icon.getInterpolatedU(0.0D);
                d14 = icon.getInterpolatedV(0.0D);
                d8 = d7;
                d16 = icon.getInterpolatedV(16D);
                d10 = icon.getInterpolatedU(16D);
                d18 = d16;
                d12 = d10;
                d20 = d14;
            }
            else
            {
                float f10 = MathHelper.sin(f8) * 0.25F;
                float f13 = MathHelper.cos(f8) * 0.25F;
                float f15 = 8F;
                d7 = icon.getInterpolatedU(8F + (-f13 - f10) * 16F);
                d14 = icon.getInterpolatedV(8F + (-f13 + f10) * 16F);
                d8 = icon.getInterpolatedU(8F + (-f13 + f10) * 16F);
                d16 = icon.getInterpolatedV(8F + (f13 + f10) * 16F);
                d10 = icon.getInterpolatedU(8F + (f13 + f10) * 16F);
                d18 = icon.getInterpolatedV(8F + (f13 - f10) * 16F);
                d12 = icon.getInterpolatedU(8F + (f13 - f10) * 16F);
                d20 = icon.getInterpolatedV(8F + (-f13 - f10) * 16F);
            }

            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            }
            float f11 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
            tessellator.setColorOpaque_F(f4 * f11 * f, f4 * f11 * f1, f4 * f11 * f2);
            tessellator.addVertexWithUV(par2 + 0, (double)par3 + d2, par4 + 0, d7, d14);
            tessellator.addVertexWithUV(par2 + 0, (double)par3 + d3, par4 + 1, d8, d16);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, par4 + 1, d10, d18);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d5, par4 + 0, d12, d20);
        }

        if (renderAllFaces || flag1)
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4));
            }
            float f7 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4) : 1.0F;
            tessellator.setColorOpaque_F(f3 * f7, f3 * f7, f3 * f7);
            renderFaceYNeg(par1Block, par2, (double)par3 + d6, par4, getBlockIconFromSide(par1Block, 0));
            flag2 = true;
        }

        for (int k = 0; k < 4; k++)
        {
            int l = par2;
            int i1 = par3;
            int j1 = par4;

            if (k == 0)
            {
                j1--;
            }

            if (k == 1)
            {
                j1++;
            }

            if (k == 2)
            {
                l--;
            }

            if (k == 3)
            {
                l++;
            }

            Icon icon1 = getBlockIconFromSideAndMetadata(par1Block, k + 2, j);

            if (!renderAllFaces && !aflag[k])
            {
                continue;
            }

            double d9;
            double d11;
            double d13;
            double d15;
            double d17;
            double d19;

            if (k == 0)
            {
                d9 = d2;
                d11 = d5;
                d13 = par2;
                d17 = par2 + 1;
                d15 = (double)par4 + d6;
                d19 = (double)par4 + d6;
            }
            else if (k == 1)
            {
                d9 = d4;
                d11 = d3;
                d13 = par2 + 1;
                d17 = par2;
                d15 = (double)(par4 + 1) - d6;
                d19 = (double)(par4 + 1) - d6;
            }
            else if (k == 2)
            {
                d9 = d3;
                d11 = d2;
                d13 = (double)par2 + d6;
                d17 = (double)par2 + d6;
                d15 = par4 + 1;
                d19 = par4;
            }
            else
            {
                d9 = d5;
                d11 = d4;
                d13 = (double)(par2 + 1) - d6;
                d17 = (double)(par2 + 1) - d6;
                d15 = par4;
                d19 = par4 + 1;
            }

            flag2 = true;
            float f9 = icon1.getInterpolatedU(0.0D);
            float f12 = icon1.getInterpolatedU(8D);
            float f14 = icon1.getInterpolatedV((1.0D - d9) * 16D * 0.5D);
            float f16 = icon1.getInterpolatedV((1.0D - d11) * 16D * 0.5D);
            float f17 = icon1.getInterpolatedV(8D);
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, l, i1, j1));
            }
            float f18 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, l, i1, j1) : 1.0F;

            if (k < 2)
            {
                f18 *= f5;
            }
            else
            {
                f18 *= f6;
            }

            tessellator.setColorOpaque_F(f4 * f18 * f, f4 * f18 * f1, f4 * f18 * f2);
            tessellator.addVertexWithUV(d13, (double)par3 + d9, d15, f9, f14);
            tessellator.addVertexWithUV(d17, (double)par3 + d11, d19, f12, f16);
            tessellator.addVertexWithUV(d17, par3 + 0, d19, f12, f17);
            tessellator.addVertexWithUV(d13, par3 + 0, d15, f9, f17);
        }

        renderMinY = d;
        renderMaxY = d1;
        return flag2;
    }

    /**
     * Get fluid height
     */
    public float getFluidHeight(int par1, int par2, int par3, Material par4Material)
    {
        int i = 0;
        float f = 0.0F;

        for (int j = 0; j < 4; j++)
        {
            int k = par1 - (j & 1);
            int l = par2;
            int i1 = par3 - (j >> 1 & 1);

            if (blockAccess.getBlockMaterial(k, l + 1, i1) == par4Material)
            {
                return 1.0F;
            }

            Material material = blockAccess.getBlockMaterial(k, l, i1);

            if (material == par4Material)
            {
                int j1 = blockAccess.getBlockMetadata(k, l, i1);

                if (j1 >= 8 || j1 == 0)
                {
                    f += BlockFluid.getFluidHeightPercent(j1) * 10F;
                    i += 10;
                }

                f += BlockFluid.getFluidHeightPercent(j1);
                i++;
                continue;
            }

            if (!material.isSolid())
            {
                f++;
                i++;
            }
        }

        return 1.0F - f / (float)i;
    }

    /**
     * Renders a falling sand block
     */
    public void renderBlockSandFalling(Block par1Block, World par2World, int par3, int par4, int par5, int par6)
    {
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
        }
        float f4 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4, par5) : 1.0F;
        float f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4 - 1, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
        renderFaceYNeg(par1Block, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(par1Block, 0, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4 + 1, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        renderFaceYPos(par1Block, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(par1Block, 1, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4, par5 - 1) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderFaceZNeg(par1Block, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(par1Block, 2, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4, par5 + 1) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderFaceZPos(par1Block, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(par1Block, 3, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3 - 1, par4, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderFaceXNeg(par1Block, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(par1Block, 4, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3 + 1, par4, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderFaceXPos(par1Block, -0.5D, -0.5D, -0.5D, getBlockIconFromSideAndMetadata(par1Block, 5, par6));
        tessellator.draw();
    }

    /**
     * Renders a standard cube block at the given coordinates
     */
    public boolean renderStandardBlock(Block par1Block, int par2, int par3, int par4)
    {
        int i = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
        float f = (float)(i >> 16 & 0xff) / 255F;
        float f1 = (float)(i >> 8 & 0xff) / 255F;
        float f2 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
            float f4 = (f * 30F + f1 * 70F) / 100F;
            float f5 = (f * 30F + f2 * 70F) / 100F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        if (Minecraft.isAmbientOcclusionEnabled() && Block.lightValue[par1Block.blockID] == 0)
        {
            if (partialRenderBounds)
            {
                return renderStandardBlockWithAmbientOcclusionPartial(par1Block, par2, par3, par4, f, f1, f2);
            }
            else
            {
                return renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, f, f1, f2);
            }
        }
        else
        {
            return renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, f, f1, f2);
        }
    }

    /**
     * Renders a log block at the given coordinates
     */
    public boolean renderBlockLog(Block par1Block, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 0xc;

        if (j == 4)
        {
            uvRotateEast = 1;
            uvRotateWest = 1;
            uvRotateTop = 1;
            uvRotateBottom = 1;
        }
        else if (j == 8)
        {
            uvRotateSouth = 1;
            uvRotateNorth = 1;
        }

        boolean flag = renderStandardBlock(par1Block, par2, par3, par4);
        uvRotateSouth = 0;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        return flag;
    }

    public boolean renderBlockQuartz(Block par1Block, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);

        if (i == 3)
        {
            uvRotateEast = 1;
            uvRotateWest = 1;
            uvRotateTop = 1;
            uvRotateBottom = 1;
        }
        else if (i == 4)
        {
            uvRotateSouth = 1;
            uvRotateNorth = 1;
        }

        boolean flag = renderStandardBlock(par1Block, par2, par3, par4);
        uvRotateSouth = 0;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        return flag;
    }

    private float getAmbientOcclusionLightValue(Block b, IBlockAccess blockAccess, int i, int j, int k){
        if (Minecraft.oldlighting){
            return b.getBlockBrightness(blockAccess, i, j, k);
        }
        return b.getAmbientOcclusionLightValue(blockAccess, i, j, k);
    }

    public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        enableAO = true;
        boolean flag = false;
        float f = 0.0F;
        float f7 = 0.0F;
        float f14 = 0.0F;
        float f21 = 0.0F;
        boolean flag1 = true;
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(0xf000f);

        if (getBlockIcon(par1Block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            if (renderMinY <= 0.0D)
            {
                par3--;
            }

            aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            aoLightValueScratchXYNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchYZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchYZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            boolean flag2 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            boolean flag8 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            boolean flag14 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
            boolean flag20 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (flag20 || flag8)
            {
                aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 - 1);
                aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
                aoBrightnessXYZNNN = aoBrightnessXYNN;
            }

            if (flag14 || flag8)
            {
                aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 + 1);
                aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
                aoBrightnessXYZNNP = aoBrightnessXYNN;
            }

            if (flag20 || flag2)
            {
                aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 - 1);
                aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
                aoBrightnessXYZPNN = aoBrightnessXYPN;
            }

            if (flag14 || flag2)
            {
                aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 + 1);
                aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
                aoBrightnessXYZPNP = aoBrightnessXYPN;
            }

            if (renderMinY <= 0.0D)
            {
                par3++;
            }

            int j = i;

            if (renderMinY <= 0.0D || !blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
            {
                j = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            }

            float f28 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            float f1 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + f28) / 4F;
            float f22 = (aoLightValueScratchYZNP + f28 + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4F;
            float f15 = (f28 + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4F;
            float f8 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + f28 + aoLightValueScratchYZNN) / 4F;
            brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, j);
            brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, j);
            brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, j);
            brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, j);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.5F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.5F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.5F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.5F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.5F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.5F;
            }

            colorRedTopLeft *= f1;
            colorGreenTopLeft *= f1;
            colorBlueTopLeft *= f1;
            colorRedBottomLeft *= f8;
            colorGreenBottomLeft *= f8;
            colorBlueBottomLeft *= f8;
            colorRedBottomRight *= f15;
            colorGreenBottomRight *= f15;
            colorBlueBottomRight *= f15;
            colorRedTopRight *= f22;
            colorGreenTopRight *= f22;
            colorBlueTopRight *= f22;
            renderFaceYNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            if (renderMaxY >= 1.0D)
            {
                par3++;
            }

            aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchXYPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            aoLightValueScratchYZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchYZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            boolean flag3 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            boolean flag9 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            boolean flag15 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            boolean flag21 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

            if (flag21 || flag9)
            {
                aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 - 1);
                aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
                aoBrightnessXYZNPN = aoBrightnessXYNP;
            }

            if (flag21 || flag3)
            {
                aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 - 1);
                aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
                aoBrightnessXYZPPN = aoBrightnessXYPP;
            }

            if (flag15 || flag9)
            {
                aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 + 1);
                aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
                aoBrightnessXYZNPP = aoBrightnessXYNP;
            }

            if (flag15 || flag3)
            {
                aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 + 1);
                aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
                aoBrightnessXYZPPP = aoBrightnessXYPP;
            }

            if (renderMaxY >= 1.0D)
            {
                par3--;
            }

            int k = i;

            if (renderMaxY >= 1.0D || !blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
            {
                k = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            }

            float f29 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            float f23 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + f29) / 4F;
            float f2 = (aoLightValueScratchYZPP + f29 + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4F;
            float f9 = (f29 + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4F;
            float f16 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + f29 + aoLightValueScratchYZPN) / 4F;
            brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, k);
            brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, k);
            brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, k);
            brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, k);
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7;
            colorRedTopLeft *= f2;
            colorGreenTopLeft *= f2;
            colorBlueTopLeft *= f2;
            colorRedBottomLeft *= f9;
            colorGreenBottomLeft *= f9;
            colorBlueBottomLeft *= f9;
            colorRedBottomRight *= f16;
            colorGreenBottomRight *= f16;
            colorBlueBottomRight *= f16;
            colorRedTopRight *= f23;
            colorGreenTopRight *= f23;
            colorBlueTopRight *= f23;
            renderFaceYPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            if (renderMinZ <= 0.0D)
            {
                par4--;
            }

            aoLightValueScratchXZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchYZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchYZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoLightValueScratchXZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            boolean flag4 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
            boolean flag10 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            boolean flag16 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
            boolean flag22 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (flag10 || flag22)
            {
                aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 - 1, par4);
                aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                aoBrightnessXYZNNN = aoBrightnessXZNN;
            }

            if (flag10 || flag16)
            {
                aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 + 1, par4);
                aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                aoBrightnessXYZNPN = aoBrightnessXZNN;
            }

            if (flag4 || flag22)
            {
                aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 - 1, par4);
                aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                aoBrightnessXYZPNN = aoBrightnessXZPN;
            }

            if (flag4 || flag16)
            {
                aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 + 1, par4);
                aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                aoBrightnessXYZPPN = aoBrightnessXZPN;
            }

            if (renderMinZ <= 0.0D)
            {
                par4++;
            }

            int l = i;

            if (renderMinZ <= 0.0D || !blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
            {
                l = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            }

            float f30 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            float f3 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + f30 + aoLightValueScratchYZPN) / 4F;
            float f10 = (f30 + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4F;
            float f17 = (aoLightValueScratchYZNN + f30 + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4F;
            float f24 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + f30) / 4F;
            brightnessTopLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, l);
            brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, l);
            brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, l);
            brightnessTopRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, l);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
            }

            colorRedTopLeft *= f3;
            colorGreenTopLeft *= f3;
            colorBlueTopLeft *= f3;
            colorRedBottomLeft *= f10;
            colorGreenBottomLeft *= f10;
            colorBlueBottomLeft *= f10;
            colorRedBottomRight *= f17;
            colorGreenBottomRight *= f17;
            colorBlueBottomRight *= f17;
            colorRedTopRight *= f24;
            colorGreenTopRight *= f24;
            colorBlueTopRight *= f24;
            Icon icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 2);
            renderFaceZNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceZNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            if (renderMaxZ >= 1.0D)
            {
                par4++;
            }

            aoLightValueScratchXZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchXZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            aoLightValueScratchYZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchYZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            boolean flag5 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            boolean flag11 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
            boolean flag17 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            boolean flag23 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

            if (flag11 || flag23)
            {
                aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 - 1, par4);
                aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                aoBrightnessXYZNNP = aoBrightnessXZNP;
            }

            if (flag11 || flag17)
            {
                aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 + 1, par4);
                aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                aoBrightnessXYZNPP = aoBrightnessXZNP;
            }

            if (flag5 || flag23)
            {
                aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 - 1, par4);
                aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                aoBrightnessXYZPNP = aoBrightnessXZPP;
            }

            if (flag5 || flag17)
            {
                aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 + 1, par4);
                aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                aoBrightnessXYZPPP = aoBrightnessXZPP;
            }

            if (renderMaxZ >= 1.0D)
            {
                par4--;
            }

            int i1 = i;

            if (renderMaxZ >= 1.0D || !blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            }

            float f31 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            float f4 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + f31 + aoLightValueScratchYZPP) / 4F;
            float f25 = (f31 + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4F;
            float f18 = (aoLightValueScratchYZNP + f31 + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4F;
            float f11 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + f31) / 4F;
            brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, i1);
            brightnessTopRight = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, i1);
            brightnessBottomRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
            brightnessBottomLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, i1);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
            }

            colorRedTopLeft *= f4;
            colorGreenTopLeft *= f4;
            colorBlueTopLeft *= f4;
            colorRedBottomLeft *= f11;
            colorGreenBottomLeft *= f11;
            colorBlueBottomLeft *= f11;
            colorRedBottomRight *= f18;
            colorGreenBottomRight *= f18;
            colorBlueBottomRight *= f18;
            colorRedTopRight *= f25;
            colorGreenTopRight *= f25;
            colorBlueTopRight *= f25;
            Icon icon1 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3);
            renderFaceZPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3));

            if (fancyGrass && icon1.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceZPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            if (renderMinX <= 0.0D)
            {
                par2--;
            }

            aoLightValueScratchXYNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchXZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchXZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            boolean flag6 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            boolean flag12 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            boolean flag18 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            boolean flag24 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

            if (flag18 || flag12)
            {
                aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 - 1);
                aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                aoBrightnessXYZNNN = aoBrightnessXZNN;
            }

            if (flag24 || flag12)
            {
                aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 + 1);
                aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                aoBrightnessXYZNNP = aoBrightnessXZNP;
            }

            if (flag18 || flag6)
            {
                aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 - 1);
                aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                aoBrightnessXYZNPN = aoBrightnessXZNN;
            }

            if (flag24 || flag6)
            {
                aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 + 1);
                aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                aoBrightnessXYZNPP = aoBrightnessXZNP;
            }

            if (renderMinX <= 0.0D)
            {
                par2++;
            }

            int j1 = i;

            if (renderMinX <= 0.0D || !blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
            {
                j1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            }

            float f32 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            float f26 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + f32 + aoLightValueScratchXZNP) / 4F;
            float f5 = (f32 + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4F;
            float f12 = (aoLightValueScratchXZNN + f32 + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4F;
            float f19 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + f32) / 4F;
            brightnessTopRight = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, j1);
            brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, j1);
            brightnessBottomLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, j1);
            brightnessBottomRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, j1);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
            }

            colorRedTopLeft *= f5;
            colorGreenTopLeft *= f5;
            colorBlueTopLeft *= f5;
            colorRedBottomLeft *= f12;
            colorGreenBottomLeft *= f12;
            colorBlueBottomLeft *= f12;
            colorRedBottomRight *= f19;
            colorGreenBottomRight *= f19;
            colorBlueBottomRight *= f19;
            colorRedTopRight *= f26;
            colorGreenTopRight *= f26;
            colorBlueTopRight *= f26;
            Icon icon2 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 4);
            renderFaceXNeg(par1Block, par2, par3, par4, icon2);

            if (fancyGrass && icon2.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceXNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            if (renderMaxX >= 1.0D)
            {
                par2++;
            }

            aoLightValueScratchXYPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchXZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchXZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            boolean flag7 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            boolean flag13 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            boolean flag19 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            boolean flag25 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

            if (flag13 || flag25)
            {
                aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 - 1);
                aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                aoBrightnessXYZPNN = aoBrightnessXZPN;
            }

            if (flag13 || flag19)
            {
                aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 + 1);
                aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                aoBrightnessXYZPNP = aoBrightnessXZPP;
            }

            if (flag7 || flag25)
            {
                aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 - 1);
                aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                aoBrightnessXYZPPN = aoBrightnessXZPN;
            }

            if (flag7 || flag19)
            {
                aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 + 1);
                aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                aoBrightnessXYZPPP = aoBrightnessXZPP;
            }

            if (renderMaxX >= 1.0D)
            {
                par2--;
            }

            int k1 = i;

            if (renderMaxX >= 1.0D || !blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
            {
                k1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            }

            float f33 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            float f6 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + f33 + aoLightValueScratchXZPP) / 4F;
            float f13 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + f33) / 4F;
            float f20 = (aoLightValueScratchXZPN + f33 + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4F;
            float f27 = (f33 + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4F;
            brightnessTopLeft = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, k1);
            brightnessTopRight = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, k1);
            brightnessBottomRight = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, k1);
            brightnessBottomLeft = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, k1);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
            }

            colorRedTopLeft *= f6;
            colorGreenTopLeft *= f6;
            colorBlueTopLeft *= f6;
            colorRedBottomLeft *= f13;
            colorGreenBottomLeft *= f13;
            colorBlueBottomLeft *= f13;
            colorRedBottomRight *= f20;
            colorGreenBottomRight *= f20;
            colorBlueBottomRight *= f20;
            colorRedTopRight *= f27;
            colorGreenTopRight *= f27;
            colorBlueTopRight *= f27;
            Icon icon3 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 5);
            renderFaceXPos(par1Block, par2, par3, par4, icon3);

            if (fancyGrass && icon3.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceXPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        enableAO = false;
        return flag;
    }

    /**
     * Renders non-full-cube block with ambient occusion.  Args: block, x, y, z, red, green, blue (lighting)
     */
    public boolean renderStandardBlockWithAmbientOcclusionPartial(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        enableAO = true;
        boolean flag = false;
        float f = 0.0F;
        float f7 = 0.0F;
        float f14 = 0.0F;
        float f21 = 0.0F;
        boolean flag1 = true;
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(0xf000f);

        if (getBlockIcon(par1Block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            if (renderMinY <= 0.0D)
            {
                par3--;
            }

            aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            aoLightValueScratchXYNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchYZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchYZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            boolean flag2 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            boolean flag8 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            boolean flag14 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
            boolean flag20 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (flag20 || flag8)
            {
                aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 - 1);
                aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
                aoBrightnessXYZNNN = aoBrightnessXYNN;
            }

            if (flag14 || flag8)
            {
                aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 + 1);
                aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
                aoBrightnessXYZNNP = aoBrightnessXYNN;
            }

            if (flag20 || flag2)
            {
                aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 - 1);
                aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
                aoBrightnessXYZPNN = aoBrightnessXYPN;
            }

            if (flag14 || flag2)
            {
                aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 + 1);
                aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
                aoBrightnessXYZPNP = aoBrightnessXYPN;
            }

            if (renderMinY <= 0.0D)
            {
                par3++;
            }

            int j = i;

            if (renderMinY <= 0.0D || !blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
            {
                j = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            }

            float f28 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            float f1 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + f28) / 4F;
            float f22 = (aoLightValueScratchYZNP + f28 + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4F;
            float f15 = (f28 + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4F;
            float f8 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + f28 + aoLightValueScratchYZNN) / 4F;
            brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, j);
            brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, j);
            brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, j);
            brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, j);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.5F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.5F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.5F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.5F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.5F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.5F;
            }

            colorRedTopLeft *= f1;
            colorGreenTopLeft *= f1;
            colorBlueTopLeft *= f1;
            colorRedBottomLeft *= f8;
            colorGreenBottomLeft *= f8;
            colorBlueBottomLeft *= f8;
            colorRedBottomRight *= f15;
            colorGreenBottomRight *= f15;
            colorBlueBottomRight *= f15;
            colorRedTopRight *= f22;
            colorGreenTopRight *= f22;
            colorBlueTopRight *= f22;
            renderFaceYNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            if (renderMaxY >= 1.0D)
            {
                par3++;
            }

            aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchXYPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            aoLightValueScratchYZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchYZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            boolean flag3 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            boolean flag9 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            boolean flag15 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            boolean flag21 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];

            if (flag21 || flag9)
            {
                aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 - 1);
                aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
                aoBrightnessXYZNPN = aoBrightnessXYNP;
            }

            if (flag21 || flag3)
            {
                aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 - 1);
                aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
                aoBrightnessXYZPPN = aoBrightnessXYPP;
            }

            if (flag15 || flag9)
            {
                aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4 + 1);
                aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
                aoBrightnessXYZNPP = aoBrightnessXYNP;
            }

            if (flag15 || flag3)
            {
                aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4 + 1);
                aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
                aoBrightnessXYZPPP = aoBrightnessXYPP;
            }

            if (renderMaxY >= 1.0D)
            {
                par3--;
            }

            int k = i;

            if (renderMaxY >= 1.0D || !blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
            {
                k = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            }

            float f29 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            float f23 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + f29) / 4F;
            float f2 = (aoLightValueScratchYZPP + f29 + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4F;
            float f9 = (f29 + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4F;
            float f16 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + f29 + aoLightValueScratchYZPN) / 4F;
            brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, k);
            brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, k);
            brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, k);
            brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, k);
            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7;
            colorRedTopLeft *= f2;
            colorGreenTopLeft *= f2;
            colorBlueTopLeft *= f2;
            colorRedBottomLeft *= f9;
            colorGreenBottomLeft *= f9;
            colorBlueBottomLeft *= f9;
            colorRedBottomRight *= f16;
            colorGreenBottomRight *= f16;
            colorBlueBottomRight *= f16;
            colorRedTopRight *= f23;
            colorGreenTopRight *= f23;
            colorBlueTopRight *= f23;
            renderFaceYPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            if (renderMinZ <= 0.0D)
            {
                par4--;
            }

            aoLightValueScratchXZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchYZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchYZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoLightValueScratchXZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            boolean flag4 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
            boolean flag10 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            boolean flag16 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
            boolean flag22 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

            if (flag10 || flag22)
            {
                aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 - 1, par4);
                aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                aoBrightnessXYZNNN = aoBrightnessXZNN;
            }

            if (flag10 || flag16)
            {
                aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 + 1, par4);
                aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                aoBrightnessXYZNPN = aoBrightnessXZNN;
            }

            if (flag4 || flag22)
            {
                aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 - 1, par4);
                aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                aoBrightnessXYZPNN = aoBrightnessXZPN;
            }

            if (flag4 || flag16)
            {
                aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 + 1, par4);
                aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                aoBrightnessXYZPPN = aoBrightnessXZPN;
            }

            if (renderMinZ <= 0.0D)
            {
                par4++;
            }

            int l = i;

            if (renderMinZ <= 0.0D || !blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
            {
                l = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            }

            float f30 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            float f34 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + f30 + aoLightValueScratchYZPN) / 4F;
            float f38 = (f30 + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4F;
            float f42 = (aoLightValueScratchYZNN + f30 + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4F;
            float f46 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + f30) / 4F;
            float f3 = (float)((double)f34 * renderMaxY * (1.0D - renderMinX) + (double)f38 * renderMinY * renderMinX + (double)f42 * (1.0D - renderMaxY) * renderMinX + (double)f46 * (1.0D - renderMaxY) * (1.0D - renderMinX));
            float f10 = (float)((double)f34 * renderMaxY * (1.0D - renderMaxX) + (double)f38 * renderMaxY * renderMaxX + (double)f42 * (1.0D - renderMaxY) * renderMaxX + (double)f46 * (1.0D - renderMaxY) * (1.0D - renderMaxX));
            float f17 = (float)((double)f34 * renderMinY * (1.0D - renderMaxX) + (double)f38 * renderMinY * renderMaxX + (double)f42 * (1.0D - renderMinY) * renderMaxX + (double)f46 * (1.0D - renderMinY) * (1.0D - renderMaxX));
            float f24 = (float)((double)f34 * renderMinY * (1.0D - renderMinX) + (double)f38 * renderMinY * renderMinX + (double)f42 * (1.0D - renderMinY) * renderMinX + (double)f46 * (1.0D - renderMinY) * (1.0D - renderMinX));
            int l1 = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, l);
            int l2 = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, l);
            int l3 = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, l);
            int l4 = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, l);
            brightnessTopLeft = mixAoBrightness(l1, l2, l3, l4, renderMaxY * (1.0D - renderMinX), renderMaxY * renderMinX, (1.0D - renderMaxY) * renderMinX, (1.0D - renderMaxY) * (1.0D - renderMinX));
            brightnessBottomLeft = mixAoBrightness(l1, l2, l3, l4, renderMaxY * (1.0D - renderMaxX), renderMaxY * renderMaxX, (1.0D - renderMaxY) * renderMaxX, (1.0D - renderMaxY) * (1.0D - renderMaxX));
            brightnessBottomRight = mixAoBrightness(l1, l2, l3, l4, renderMinY * (1.0D - renderMaxX), renderMinY * renderMaxX, (1.0D - renderMinY) * renderMaxX, (1.0D - renderMinY) * (1.0D - renderMaxX));
            brightnessTopRight = mixAoBrightness(l1, l2, l3, l4, renderMinY * (1.0D - renderMinX), renderMinY * renderMinX, (1.0D - renderMinY) * renderMinX, (1.0D - renderMinY) * (1.0D - renderMinX));

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
            }

            colorRedTopLeft *= f3;
            colorGreenTopLeft *= f3;
            colorBlueTopLeft *= f3;
            colorRedBottomLeft *= f10;
            colorGreenBottomLeft *= f10;
            colorBlueBottomLeft *= f10;
            colorRedBottomRight *= f17;
            colorGreenBottomRight *= f17;
            colorBlueBottomRight *= f17;
            colorRedTopRight *= f24;
            colorGreenTopRight *= f24;
            colorBlueTopRight *= f24;
            Icon icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 2);
            renderFaceZNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceZNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            if (renderMaxZ >= 1.0D)
            {
                par4++;
            }

            aoLightValueScratchXZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            aoLightValueScratchXZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            aoLightValueScratchYZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchYZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            boolean flag5 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            boolean flag11 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
            boolean flag17 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
            boolean flag23 = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];

            if (flag11 || flag23)
            {
                aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 - 1, par4);
                aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                aoBrightnessXYZNNP = aoBrightnessXZNP;
            }

            if (flag11 || flag17)
            {
                aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3 + 1, par4);
                aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                aoBrightnessXYZNPP = aoBrightnessXZNP;
            }

            if (flag5 || flag23)
            {
                aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 - 1, par4);
                aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                aoBrightnessXYZPNP = aoBrightnessXZPP;
            }

            if (flag5 || flag17)
            {
                aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3 + 1, par4);
                aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
            }
            else
            {
                aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                aoBrightnessXYZPPP = aoBrightnessXZPP;
            }

            if (renderMaxZ >= 1.0D)
            {
                par4--;
            }

            int i1 = i;

            if (renderMaxZ >= 1.0D || !blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
            {
                i1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            }

            float f31 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            float f35 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + f31 + aoLightValueScratchYZPP) / 4F;
            float f39 = (f31 + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4F;
            float f43 = (aoLightValueScratchYZNP + f31 + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4F;
            float f47 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + f31) / 4F;
            float f4 = (float)((double)f35 * renderMaxY * (1.0D - renderMinX) + (double)f39 * renderMaxY * renderMinX + (double)f43 * (1.0D - renderMaxY) * renderMinX + (double)f47 * (1.0D - renderMaxY) * (1.0D - renderMinX));
            float f11 = (float)((double)f35 * renderMinY * (1.0D - renderMinX) + (double)f39 * renderMinY * renderMinX + (double)f43 * (1.0D - renderMinY) * renderMinX + (double)f47 * (1.0D - renderMinY) * (1.0D - renderMinX));
            float f18 = (float)((double)f35 * renderMinY * (1.0D - renderMaxX) + (double)f39 * renderMinY * renderMaxX + (double)f43 * (1.0D - renderMinY) * renderMaxX + (double)f47 * (1.0D - renderMinY) * (1.0D - renderMaxX));
            float f25 = (float)((double)f35 * renderMaxY * (1.0D - renderMaxX) + (double)f39 * renderMaxY * renderMaxX + (double)f43 * (1.0D - renderMaxY) * renderMaxX + (double)f47 * (1.0D - renderMaxY) * (1.0D - renderMaxX));
            int i2 = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, i1);
            int i3 = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, i1);
            int i4 = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
            int i5 = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, i1);
            brightnessTopLeft = mixAoBrightness(i2, i5, i4, i3, renderMaxY * (1.0D - renderMinX), (1.0D - renderMaxY) * (1.0D - renderMinX), (1.0D - renderMaxY) * renderMinX, renderMaxY * renderMinX);
            brightnessBottomLeft = mixAoBrightness(i2, i5, i4, i3, renderMinY * (1.0D - renderMinX), (1.0D - renderMinY) * (1.0D - renderMinX), (1.0D - renderMinY) * renderMinX, renderMinY * renderMinX);
            brightnessBottomRight = mixAoBrightness(i2, i5, i4, i3, renderMinY * (1.0D - renderMaxX), (1.0D - renderMinY) * (1.0D - renderMaxX), (1.0D - renderMinY) * renderMaxX, renderMinY * renderMaxX);
            brightnessTopRight = mixAoBrightness(i2, i5, i4, i3, renderMaxY * (1.0D - renderMaxX), (1.0D - renderMaxY) * (1.0D - renderMaxX), (1.0D - renderMaxY) * renderMaxX, renderMaxY * renderMaxX);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.8F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.8F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.8F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.8F;
            }

            colorRedTopLeft *= f4;
            colorGreenTopLeft *= f4;
            colorBlueTopLeft *= f4;
            colorRedBottomLeft *= f11;
            colorGreenBottomLeft *= f11;
            colorBlueBottomLeft *= f11;
            colorRedBottomRight *= f18;
            colorGreenBottomRight *= f18;
            colorBlueBottomRight *= f18;
            colorRedTopRight *= f25;
            colorGreenTopRight *= f25;
            colorBlueTopRight *= f25;
            Icon icon1 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3);
            renderFaceZPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3));

            if (fancyGrass && icon1.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceZPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            if (renderMinX <= 0.0D)
            {
                par2--;
            }

            aoLightValueScratchXYNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchXZNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchXZNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            boolean flag6 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
            boolean flag12 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
            boolean flag18 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
            boolean flag24 = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];

            if (flag18 || flag12)
            {
                aoLightValueScratchXYZNNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 - 1);
                aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                aoBrightnessXYZNNN = aoBrightnessXZNN;
            }

            if (flag24 || flag12)
            {
                aoLightValueScratchXYZNNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 + 1);
                aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                aoBrightnessXYZNNP = aoBrightnessXZNP;
            }

            if (flag18 || flag6)
            {
                aoLightValueScratchXYZNPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 - 1);
                aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                aoBrightnessXYZNPN = aoBrightnessXZNN;
            }

            if (flag24 || flag6)
            {
                aoLightValueScratchXYZNPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 + 1);
                aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                aoBrightnessXYZNPP = aoBrightnessXZNP;
            }

            if (renderMinX <= 0.0D)
            {
                par2++;
            }

            int j1 = i;

            if (renderMinX <= 0.0D || !blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
            {
                j1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
            }

            float f32 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 - 1, par3, par4);
            float f36 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + f32 + aoLightValueScratchXZNP) / 4F;
            float f40 = (f32 + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4F;
            float f44 = (aoLightValueScratchXZNN + f32 + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4F;
            float f48 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + f32) / 4F;
            float f5 = (float)((double)f40 * renderMaxY * renderMaxZ + (double)f44 * renderMaxY * (1.0D - renderMaxZ) + (double)f48 * (1.0D - renderMaxY) * (1.0D - renderMaxZ) + (double)f36 * (1.0D - renderMaxY) * renderMaxZ);
            float f12 = (float)((double)f40 * renderMaxY * renderMinZ + (double)f44 * renderMaxY * (1.0D - renderMinZ) + (double)f48 * (1.0D - renderMaxY) * (1.0D - renderMinZ) + (double)f36 * (1.0D - renderMaxY) * renderMinZ);
            float f19 = (float)((double)f40 * renderMinY * renderMinZ + (double)f44 * renderMinY * (1.0D - renderMinZ) + (double)f48 * (1.0D - renderMinY) * (1.0D - renderMinZ) + (double)f36 * (1.0D - renderMinY) * renderMinZ);
            float f26 = (float)((double)f40 * renderMinY * renderMaxZ + (double)f44 * renderMinY * (1.0D - renderMaxZ) + (double)f48 * (1.0D - renderMinY) * (1.0D - renderMaxZ) + (double)f36 * (1.0D - renderMinY) * renderMaxZ);
            int j2 = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, j1);
            int j3 = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, j1);
            int j4 = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, j1);
            int j5 = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, j1);
            brightnessTopLeft = mixAoBrightness(j3, j4, j5, j2, renderMaxY * renderMaxZ, renderMaxY * (1.0D - renderMaxZ), (1.0D - renderMaxY) * (1.0D - renderMaxZ), (1.0D - renderMaxY) * renderMaxZ);
            brightnessBottomLeft = mixAoBrightness(j3, j4, j5, j2, renderMaxY * renderMinZ, renderMaxY * (1.0D - renderMinZ), (1.0D - renderMaxY) * (1.0D - renderMinZ), (1.0D - renderMaxY) * renderMinZ);
            brightnessBottomRight = mixAoBrightness(j3, j4, j5, j2, renderMinY * renderMinZ, renderMinY * (1.0D - renderMinZ), (1.0D - renderMinY) * (1.0D - renderMinZ), (1.0D - renderMinY) * renderMinZ);
            brightnessTopRight = mixAoBrightness(j3, j4, j5, j2, renderMinY * renderMaxZ, renderMinY * (1.0D - renderMaxZ), (1.0D - renderMinY) * (1.0D - renderMaxZ), (1.0D - renderMinY) * renderMaxZ);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
            }

            colorRedTopLeft *= f5;
            colorGreenTopLeft *= f5;
            colorBlueTopLeft *= f5;
            colorRedBottomLeft *= f12;
            colorGreenBottomLeft *= f12;
            colorBlueBottomLeft *= f12;
            colorRedBottomRight *= f19;
            colorGreenBottomRight *= f19;
            colorBlueBottomRight *= f19;
            colorRedTopRight *= f26;
            colorGreenTopRight *= f26;
            colorBlueTopRight *= f26;
            Icon icon2 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 4);
            renderFaceXNeg(par1Block, par2, par3, par4, icon2);

            if (fancyGrass && icon2.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceXNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            if (renderMaxX >= 1.0D)
            {
                par2++;
            }

            aoLightValueScratchXYPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4);
            aoLightValueScratchXZPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 - 1);
            aoLightValueScratchXZPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3, par4 + 1);
            aoLightValueScratchXYPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4);
            aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
            aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
            aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
            aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
            boolean flag7 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
            boolean flag13 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
            boolean flag19 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
            boolean flag25 = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];

            if (flag13 || flag25)
            {
                aoLightValueScratchXYZPNN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 - 1);
                aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                aoBrightnessXYZPNN = aoBrightnessXZPN;
            }

            if (flag13 || flag19)
            {
                aoLightValueScratchXYZPNP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 - 1, par4 + 1);
                aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                aoBrightnessXYZPNP = aoBrightnessXZPP;
            }

            if (flag7 || flag25)
            {
                aoLightValueScratchXYZPPN = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 - 1);
                aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
            }
            else
            {
                aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                aoBrightnessXYZPPN = aoBrightnessXZPN;
            }

            if (flag7 || flag19)
            {
                aoLightValueScratchXYZPPP = getAmbientOcclusionLightValue(par1Block, blockAccess, par2, par3 + 1, par4 + 1);
                aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
            }
            else
            {
                aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                aoBrightnessXYZPPP = aoBrightnessXZPP;
            }

            if (renderMaxX >= 1.0D)
            {
                par2--;
            }

            int k1 = i;

            if (renderMaxX >= 1.0D || !blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
            {
                k1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
            }

            float f33 = getAmbientOcclusionLightValue(par1Block, blockAccess, par2 + 1, par3, par4);
            float f37 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + f33 + aoLightValueScratchXZPP) / 4F;
            float f41 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + f33) / 4F;
            float f45 = (aoLightValueScratchXZPN + f33 + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4F;
            float f49 = (f33 + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4F;
            float f6 = (float)((double)f37 * (1.0D - renderMinY) * renderMaxZ + (double)f41 * (1.0D - renderMinY) * (1.0D - renderMaxZ) + (double)f45 * renderMinY * (1.0D - renderMaxZ) + (double)f49 * renderMinY * renderMaxZ);
            float f13 = (float)((double)f37 * (1.0D - renderMinY) * renderMinZ + (double)f41 * (1.0D - renderMinY) * (1.0D - renderMinZ) + (double)f45 * renderMinY * (1.0D - renderMinZ) + (double)f49 * renderMinY * renderMinZ);
            float f20 = (float)((double)f37 * (1.0D - renderMaxY) * renderMinZ + (double)f41 * (1.0D - renderMaxY) * (1.0D - renderMinZ) + (double)f45 * renderMaxY * (1.0D - renderMinZ) + (double)f49 * renderMaxY * renderMinZ);
            float f27 = (float)((double)f37 * (1.0D - renderMaxY) * renderMaxZ + (double)f41 * (1.0D - renderMaxY) * (1.0D - renderMaxZ) + (double)f45 * renderMaxY * (1.0D - renderMaxZ) + (double)f49 * renderMaxY * renderMaxZ);
            int k2 = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, k1);
            int k3 = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, k1);
            int k4 = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, k1);
            int k5 = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, k1);
            brightnessTopLeft = mixAoBrightness(k2, k5, k4, k3, (1.0D - renderMinY) * renderMaxZ, (1.0D - renderMinY) * (1.0D - renderMaxZ), renderMinY * (1.0D - renderMaxZ), renderMinY * renderMaxZ);
            brightnessBottomLeft = mixAoBrightness(k2, k5, k4, k3, (1.0D - renderMinY) * renderMinZ, (1.0D - renderMinY) * (1.0D - renderMinZ), renderMinY * (1.0D - renderMinZ), renderMinY * renderMinZ);
            brightnessBottomRight = mixAoBrightness(k2, k5, k4, k3, (1.0D - renderMaxY) * renderMinZ, (1.0D - renderMaxY) * (1.0D - renderMinZ), renderMaxY * (1.0D - renderMinZ), renderMaxY * renderMinZ);
            brightnessTopRight = mixAoBrightness(k2, k5, k4, k3, (1.0D - renderMaxY) * renderMaxZ, (1.0D - renderMaxY) * (1.0D - renderMaxZ), renderMaxY * (1.0D - renderMaxZ), renderMaxY * renderMaxZ);

            if (flag1)
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = par5 * 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = par6 * 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = par7 * 0.6F;
            }
            else
            {
                colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = 0.6F;
                colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = 0.6F;
                colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = 0.6F;
            }

            colorRedTopLeft *= f6;
            colorGreenTopLeft *= f6;
            colorBlueTopLeft *= f6;
            colorRedBottomLeft *= f13;
            colorGreenBottomLeft *= f13;
            colorBlueBottomLeft *= f13;
            colorRedBottomRight *= f20;
            colorGreenBottomRight *= f20;
            colorBlueBottomRight *= f20;
            colorRedTopRight *= f27;
            colorGreenTopRight *= f27;
            colorBlueTopRight *= f27;
            Icon icon3 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 5);
            renderFaceXPos(par1Block, par2, par3, par4, icon3);

            if (fancyGrass && icon3.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                colorRedTopLeft *= par5;
                colorRedBottomLeft *= par5;
                colorRedBottomRight *= par5;
                colorRedTopRight *= par5;
                colorGreenTopLeft *= par6;
                colorGreenBottomLeft *= par6;
                colorGreenBottomRight *= par6;
                colorGreenTopRight *= par6;
                colorBlueTopLeft *= par7;
                colorBlueBottomLeft *= par7;
                colorBlueBottomRight *= par7;
                colorBlueTopRight *= par7;
                renderFaceXPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        enableAO = false;
        return flag;
    }

    /**
     * Get ambient occlusion brightness
     */
    public int getAoBrightness(int par1, int par2, int par3, int par4)
    {
        if (par1 == 0)
        {
            par1 = par4;
        }

        if (par2 == 0)
        {
            par2 = par4;
        }

        if (par3 == 0)
        {
            par3 = par4;
        }

        return par1 + par2 + par3 + par4 >> 2 & 0xff00ff;
    }

    public int mixAoBrightness(int par1, int par2, int par3, int par4, double par5, double par7, double par9, double par11)
    {
        int i = (int)((double)(par1 >> 16 & 0xff) * par5 + (double)(par2 >> 16 & 0xff) * par7 + (double)(par3 >> 16 & 0xff) * par9 + (double)(par4 >> 16 & 0xff) * par11) & 0xff;
        int j = (int)((double)(par1 & 0xff) * par5 + (double)(par2 & 0xff) * par7 + (double)(par3 & 0xff) * par9 + (double)(par4 & 0xff) * par11) & 0xff;
        return i << 16 | j;
    }

    /**
     * Renders a standard cube block at the given coordinates, with a given color ratio.  Args: block, x, y, z, r, g, b
     */
    public boolean renderStandardBlockWithColorMultiplier(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        float f4 = f1 * par5;
        float f5 = f1 * par6;
        float f6 = f1 * par7;
        float f7 = f;
        float f8 = f2;
        float f9 = f3;
        float f10 = f;
        float f11 = f2;
        float f12 = f3;
        float f13 = f;
        float f14 = f2;
        float f15 = f3;

        if (par1Block != Block.grass)
        {
            f7 *= par5;
            f8 *= par5;
            f9 *= par5;
            f10 *= par6;
            f11 *= par6;
            f12 *= par6;
            f13 *= par7;
            f14 *= par7;
            f15 *= par7;
        }

        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMinY <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4) : i);
                tessellator.setColorOpaque_F(f7, f10, f13);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                tessellator.setColorOpaque_F(ff * f7, ff * f10, ff * f13);
            }
            renderFaceYNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMaxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4) : i);
                tessellator.setColorOpaque_F(f4, f5, f6);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                tessellator.setColorOpaque_F(ff * f4, ff * f5, ff * f6);
            }
            renderFaceYPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMinZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : i);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                tessellator.setColorOpaque_F(ff * f8, ff * f11, ff * f14);
            }
            Icon icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 2);
            renderFaceZNeg(par1Block, par2, par3, par4, icon);

            if (fancyGrass && icon.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1) : 1.0F;
                tessellator.setColorOpaque_F(ff * f8 * par5, ff * f11 * par6, ff * f14 * par7);
                renderFaceZNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMaxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : i);
                tessellator.setColorOpaque_F(f8, f11, f14);;
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                tessellator.setColorOpaque_F(ff * f8, ff * f11, ff * f14);
            }
            Icon icon1 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3);
            renderFaceZPos(par1Block, par2, par3, par4, icon1);

            if (fancyGrass && icon1.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1) : 1.0F;
                tessellator.setColorOpaque_F(ff * f8 * par5, ff * f11 * par6, ff * f14 * par7);
                renderFaceZPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMinX <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : i);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                tessellator.setColorOpaque_F(ff * f9, ff * f12, ff * f15);
            }
            Icon icon2 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 4);
            renderFaceXNeg(par1Block, par2, par3, par4, icon2);

            if (fancyGrass && icon2.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4) : 1.0F;
                tessellator.setColorOpaque_F(ff * f9 * par5, ff * f12 * par6, ff * f15 * par7);
                renderFaceXNeg(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMaxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : i);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                tessellator.setColorOpaque_F(ff * f9, ff * f12, ff * f15);
            }
            Icon icon3 = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 5);
            renderFaceXPos(par1Block, par2, par3, par4, icon3);

            if (fancyGrass && icon3.getIconName().equals("grass_side") && !hasOverrideBlockTexture())
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4) : 1.0F;
                tessellator.setColorOpaque_F(ff * f9 * par5, ff * f12 * par6, ff * f15 * par7);
                renderFaceXPos(par1Block, par2, par3, par4, BlockGrass.getIconSideOverlay());
            }

            flag = true;
        }

        return flag;
    }

    /**
     * Renders a Cocoa block at the given coordinates
     */
    public boolean renderBlockCocoa(BlockCocoa par1BlockCocoa, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockCocoa.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float ff = par1BlockCocoa.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = BlockDirectional.getDirection(i);
        int k = BlockCocoa.func_72219_c(i);
        Icon icon = par1BlockCocoa.getCocoaIcon(k);
        int l = 4 + k * 2;
        int i1 = 5 + k * 2;
        double d = 15D - (double)l;
        double d1 = 15D;
        double d2 = 4D;
        double d3 = 4D + (double)i1;
        double d4 = icon.getInterpolatedU(d);
        double d5 = icon.getInterpolatedU(d1);
        double d6 = icon.getInterpolatedV(d2);
        double d7 = icon.getInterpolatedV(d3);
        double d8 = 0.0D;
        double d9 = 0.0D;

        switch (j)
        {
            case 2:
                d8 = 8D - (double)(l / 2);
                d9 = 1.0D;
                break;
            case 0:
                d8 = 8D - (double)(l / 2);
                d9 = 15D - (double)l;
                break;
            case 3:
                d8 = 15D - (double)l;
                d9 = 8D - (double)(l / 2);
                break;
            case 1:
                d8 = 1.0D;
                d9 = 8D - (double)(l / 2);
                break;
        }

        double d10 = (double)par2 + d8 / 16D;
        double d11 = (double)par2 + (d8 + (double)l) / 16D;
        double d12 = (double)par3 + (12D - (double)i1) / 16D;
        double d13 = (double)par3 + 0.75D;
        double d14 = (double)par4 + d9 / 16D;
        double d15 = (double)par4 + (d9 + (double)l) / 16D;
        tessellator.addVertexWithUV(d10, d12, d14, d4, d7);
        tessellator.addVertexWithUV(d10, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d10, d13, d15, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d12, d14, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
        tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
        tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d15, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
        int j1 = l;

        if (k >= 2)
        {
            j1--;
        }

        d4 = icon.getMinU();
        d5 = icon.getInterpolatedU(j1);
        d6 = icon.getMinV();
        d7 = icon.getInterpolatedV(j1);
        tessellator.addVertexWithUV(d10, d13, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d13, d15, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d10, d12, d14, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
        d4 = icon.getInterpolatedU(12D);
        d5 = icon.getMaxU();
        d6 = icon.getMinV();
        d7 = icon.getInterpolatedV(4D);
        d8 = 8D;
        d9 = 0.0D;

        switch (j)
        {
            case 2:
                d8 = 8D;
                d9 = 0.0D;
                break;
            case 0:
                d8 = 8D;
                d9 = 12D;
                double d16 = d4;
                d4 = d5;
                d5 = d16;
                break;
            case 3:
                d8 = 12D;
                d9 = 8D;
                double d17 = d4;
                d4 = d5;
                d5 = d17;
                break;
            case 1:
                d8 = 0.0D;
                d9 = 8D;
                break;
        }

        d10 = (double)par2 + d8 / 16D;
        d11 = (double)par2 + (d8 + 4D) / 16D;
        d12 = (double)par3 + 0.75D;
        d13 = (double)par3 + 1.0D;
        d14 = (double)par4 + d9 / 16D;
        d15 = (double)par4 + (d9 + 4D) / 16D;

        if (j == 2 || j == 0)
        {
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
            tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
            tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
            tessellator.addVertexWithUV(d10, d13, d15, d4, d6);
        }
        else if (j == 1 || j == 3)
        {
            tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
            tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
            tessellator.addVertexWithUV(d10, d12, d14, d5, d7);
            tessellator.addVertexWithUV(d11, d12, d14, d4, d7);
            tessellator.addVertexWithUV(d11, d13, d14, d4, d6);
            tessellator.addVertexWithUV(d10, d13, d14, d5, d6);
        }

        return true;
    }

    /**
     * Renders beacon block
     */
    public boolean renderBlockBeacon(BlockBeacon par1BlockBeacon, int par2, int par3, int par4)
    {
        float f = 0.1875F;
        setOverrideBlockTexture(getBlockIcon(Block.glass));
        setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        renderStandardBlock(par1BlockBeacon, par2, par3, par4);
        renderAllFaces = true;
        setOverrideBlockTexture(getBlockIcon(Block.obsidian));
        setRenderBounds(0.125D, 0.0062500000931322575D, 0.125D, 0.875D, f, 0.875D);
        renderStandardBlock(par1BlockBeacon, par2, par3, par4);
        setOverrideBlockTexture(getBlockIcon(Block.beacon));
        setRenderBounds(0.1875D, f, 0.1875D, 0.8125D, 0.875D, 0.8125D);
        renderStandardBlock(par1BlockBeacon, par2, par3, par4);
        renderAllFaces = false;
        clearOverrideBlockTexture();
        return true;
    }

    /**
     * Renders a cactus block at the given coordinates
     */
    public boolean renderBlockCactus(Block par1Block, int par2, int par3, int par4)
    {
        int i = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
        float f = (float)(i >> 16 & 0xff) / 255F;
        float f1 = (float)(i >> 8 & 0xff) / 255F;
        float f2 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
            float f4 = (f * 30F + f1 * 70F) / 100F;
            float f5 = (f * 30F + f2 * 70F) / 100F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        return renderBlockCactusImpl(par1Block, par2, par3, par4, f, f1, f2);
    }

    /**
     * Render block cactus implementation
     */
    public boolean renderBlockCactusImpl(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        float f4 = f * par5;
        float f5 = f1 * par5;
        float f6 = f2 * par5;
        float f7 = f3 * par5;
        float f8 = f * par6;
        float f9 = f1 * par6;
        float f10 = f2 * par6;
        float f11 = f3 * par6;
        float f12 = f * par7;
        float f13 = f1 * par7;
        float f14 = f2 * par7;
        float f15 = f3 * par7;
        float f16 = 0.0625F;
        float ff4 = 1.0F;
        float ff5 = 1.0F;
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMinY <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4) : i);
            }else{
                ff4 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
                ff5 = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                if(renderMinY > 0.0D)
                {
                    ff5 = f4;
                }
                if(Block.lightValue[par1Block.blockID] > 0)
                {
                    ff5 = 1.0F;
                }
            }
            tessellator.setColorOpaque_F(f4 * ff5, f8 * ff5, f12 * ff5);
            renderFaceYNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0));
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(renderMaxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4) : i);
            }else{
                ff5 = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                if(renderMaxY < 1.0D)
                {
                    ff5 = ff4;
                }
                if(Block.lightValue[par1Block.blockID] > 0)
                {
                    ff5 = 1.0F;
                }
            }
            tessellator.setColorOpaque_F(f5 * ff5, f9 * ff5, f13 * ff5);
            renderFaceYPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 1));
        }

        float ff6 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(i);
        }
        tessellator.setColorOpaque_F(f6 * ff6, f10 * ff6, f14 * ff6);
        tessellator.addTranslation(0.0F, 0.0F, f16);
        renderFaceZNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 2));
        tessellator.addTranslation(0.0F, 0.0F, -f16);
        tessellator.addTranslation(0.0F, 0.0F, -f16);
        renderFaceZPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3));
        tessellator.addTranslation(0.0F, 0.0F, f16);
        tessellator.setColorOpaque_F(f7 * ff6, f11 * ff6, f15 * ff6);
        tessellator.addTranslation(f16, 0.0F, 0.0F);
        renderFaceXNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 4));
        tessellator.addTranslation(-f16, 0.0F, 0.0F);
        tessellator.addTranslation(-f16, 0.0F, 0.0F);
        renderFaceXPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 5));
        tessellator.addTranslation(f16, 0.0F, 0.0F);
        return true;
    }

    public boolean renderBlockFence(BlockFence par1BlockFence, int par2, int par3, int par4)
    {
        boolean flag = false;
        float f = 0.375F;
        float f1 = 0.625F;
        setRenderBounds(f, 0.0D, f, f1, 1.0D, f1);
        renderStandardBlock(par1BlockFence, par2, par3, par4);
        flag = true;
        boolean flag1 = false;
        boolean flag2 = false;

        if (par1BlockFence.canConnectFenceTo(blockAccess, par2 - 1, par3, par4) || par1BlockFence.canConnectFenceTo(blockAccess, par2 + 1, par3, par4))
        {
            flag1 = true;
        }

        if (par1BlockFence.canConnectFenceTo(blockAccess, par2, par3, par4 - 1) || par1BlockFence.canConnectFenceTo(blockAccess, par2, par3, par4 + 1))
        {
            flag2 = true;
        }

        boolean flag3 = par1BlockFence.canConnectFenceTo(blockAccess, par2 - 1, par3, par4);
        boolean flag4 = par1BlockFence.canConnectFenceTo(blockAccess, par2 + 1, par3, par4);
        boolean flag5 = par1BlockFence.canConnectFenceTo(blockAccess, par2, par3, par4 - 1);
        boolean flag6 = par1BlockFence.canConnectFenceTo(blockAccess, par2, par3, par4 + 1);

        if (!flag1 && !flag2)
        {
            flag1 = true;
        }

        f = 0.4375F;
        f1 = 0.5625F;
        float f2 = 0.75F;
        float f3 = 0.9375F;
        float f4 = flag3 ? 0.0F : f;
        float f5 = flag4 ? 1.0F : f1;
        float f6 = flag5 ? 0.0F : f;
        float f7 = flag6 ? 1.0F : f1;

        if (flag1)
        {
            setRenderBounds(f4, f2, f, f5, f3, f1);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        if (flag2)
        {
            setRenderBounds(f, f2, f6, f1, f3, f7);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        f2 = 0.375F;
        f3 = 0.5625F;

        if (flag1)
        {
            setRenderBounds(f4, f2, f, f5, f3, f1);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        if (flag2)
        {
            setRenderBounds(f, f2, f6, f1, f3, f7);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        par1BlockFence.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);
        return flag;
    }

    /**
     * Renders wall block
     */
    public boolean renderBlockWall(BlockWall par1BlockWall, int par2, int par3, int par4)
    {
        boolean flag = par1BlockWall.canConnectWallTo(blockAccess, par2 - 1, par3, par4);
        boolean flag1 = par1BlockWall.canConnectWallTo(blockAccess, par2 + 1, par3, par4);
        boolean flag2 = par1BlockWall.canConnectWallTo(blockAccess, par2, par3, par4 - 1);
        boolean flag3 = par1BlockWall.canConnectWallTo(blockAccess, par2, par3, par4 + 1);
        boolean flag4 = flag2 && flag3 && !flag && !flag1;
        boolean flag5 = !flag2 && !flag3 && flag && flag1;
        boolean flag6 = blockAccess.isAirBlock(par2, par3 + 1, par4);

        if (!flag4 && !flag5 || !flag6)
        {
            setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
            renderStandardBlock(par1BlockWall, par2, par3, par4);

            if (flag)
            {
                setRenderBounds(0.0D, 0.0D, 0.3125D, 0.25D, 0.8125D, 0.6875D);
                renderStandardBlock(par1BlockWall, par2, par3, par4);
            }

            if (flag1)
            {
                setRenderBounds(0.75D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
                renderStandardBlock(par1BlockWall, par2, par3, par4);
            }

            if (flag2)
            {
                setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 0.25D);
                renderStandardBlock(par1BlockWall, par2, par3, par4);
            }

            if (flag3)
            {
                setRenderBounds(0.3125D, 0.0D, 0.75D, 0.6875D, 0.8125D, 1.0D);
                renderStandardBlock(par1BlockWall, par2, par3, par4);
            }
        }
        else if (flag4)
        {
            setRenderBounds(0.3125D, 0.0D, 0.0D, 0.6875D, 0.8125D, 1.0D);
            renderStandardBlock(par1BlockWall, par2, par3, par4);
        }
        else
        {
            setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
            renderStandardBlock(par1BlockWall, par2, par3, par4);
        }

        par1BlockWall.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);
        return true;
    }

    public boolean renderBlockDragonEgg(BlockDragonEgg par1BlockDragonEgg, int par2, int par3, int par4)
    {
        boolean flag = false;
        int i = 0;

        for (int j = 0; j < 8; j++)
        {
            int k = 0;
            byte byte0 = 1;

            if (j == 0)
            {
                k = 2;
            }

            if (j == 1)
            {
                k = 3;
            }

            if (j == 2)
            {
                k = 4;
            }

            if (j == 3)
            {
                k = 5;
                byte0 = 2;
            }

            if (j == 4)
            {
                k = 6;
                byte0 = 3;
            }

            if (j == 5)
            {
                k = 7;
                byte0 = 5;
            }

            if (j == 6)
            {
                k = 6;
                byte0 = 2;
            }

            if (j == 7)
            {
                k = 3;
            }

            float f = (float)k / 16F;
            float f1 = 1.0F - (float)i / 16F;
            float f2 = 1.0F - (float)(i + byte0) / 16F;
            i += byte0;
            setRenderBounds(0.5F - f, f2, 0.5F - f, 0.5F + f, f1, 0.5F + f);
            renderStandardBlock(par1BlockDragonEgg, par2, par3, par4);
        }

        flag = true;
        setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        return flag;
    }

    /**
     * Render block fence gate
     */
    public boolean renderBlockFenceGate(BlockFenceGate par1BlockFenceGate, int par2, int par3, int par4)
    {
        boolean flag = true;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag1 = BlockFenceGate.isFenceGateOpen(i);
        int j = BlockDirectional.getDirection(i);
        float f = 0.375F;
        float f1 = 0.5625F;
        float f2 = 0.75F;
        float f3 = 0.9375F;
        float f4 = 0.3125F;
        float f5 = 1.0F;

        if ((j == 2 || j == 0) && blockAccess.getBlockId(par2 - 1, par3, par4) == Block.cobblestoneWall.blockID && blockAccess.getBlockId(par2 + 1, par3, par4) == Block.cobblestoneWall.blockID || (j == 3 || j == 1) && blockAccess.getBlockId(par2, par3, par4 - 1) == Block.cobblestoneWall.blockID && blockAccess.getBlockId(par2, par3, par4 + 1) == Block.cobblestoneWall.blockID)
        {
            f -= 0.1875F;
            f1 -= 0.1875F;
            f2 -= 0.1875F;
            f3 -= 0.1875F;
            f4 -= 0.1875F;
            f5 -= 0.1875F;
        }

        renderAllFaces = true;

        if (j == 3 || j == 1)
        {
            uvRotateTop = 1;
            float f6 = 0.4375F;
            float f14 = 0.5625F;
            float f22 = 0.0F;
            float f30 = 0.125F;
            setRenderBounds(f6, f4, f22, f14, f5, f30);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f22 = 0.875F;
            f30 = 1.0F;
            setRenderBounds(f6, f4, f22, f14, f5, f30);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            uvRotateTop = 0;
        }
        else
        {
            float f7 = 0.0F;
            float f15 = 0.125F;
            float f23 = 0.4375F;
            float f31 = 0.5625F;
            setRenderBounds(f7, f4, f23, f15, f5, f31);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f7 = 0.875F;
            f15 = 1.0F;
            setRenderBounds(f7, f4, f23, f15, f5, f31);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }

        if (flag1)
        {
            if (j == 2 || j == 0)
            {
                uvRotateTop = 1;
            }

            if (j == 3)
            {
                float f8 = 0.0F;
                float f16 = 0.125F;
                float f24 = 0.875F;
                float f32 = 1.0F;
                float f38 = 0.5625F;
                float f42 = 0.8125F;
                float f46 = 0.9375F;
                setRenderBounds(0.8125D, f, 0.0D, 0.9375D, f3, 0.125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.8125D, f, 0.875D, 0.9375D, f3, 1.0D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.5625D, f, 0.0D, 0.8125D, f1, 0.125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.5625D, f, 0.875D, 0.8125D, f1, 1.0D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.5625D, f2, 0.0D, 0.8125D, f3, 0.125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.5625D, f2, 0.875D, 0.8125D, f3, 1.0D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
            else if (j == 1)
            {
                float f9 = 0.0F;
                float f17 = 0.125F;
                float f25 = 0.875F;
                float f33 = 1.0F;
                float f39 = 0.0625F;
                float f43 = 0.1875F;
                float f47 = 0.4375F;
                setRenderBounds(0.0625D, f, 0.0D, 0.1875D, f3, 0.125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.0625D, f, 0.875D, 0.1875D, f3, 1.0D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.1875D, f, 0.0D, 0.4375D, f1, 0.125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.1875D, f, 0.875D, 0.4375D, f1, 1.0D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.1875D, f2, 0.0D, 0.4375D, f3, 0.125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.1875D, f2, 0.875D, 0.4375D, f3, 1.0D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
            else if (j == 0)
            {
                float f10 = 0.0F;
                float f18 = 0.125F;
                float f26 = 0.875F;
                float f34 = 1.0F;
                float f40 = 0.5625F;
                float f44 = 0.8125F;
                float f48 = 0.9375F;
                setRenderBounds(0.0D, f, 0.8125D, 0.125D, f3, 0.9375D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.875D, f, 0.8125D, 1.0D, f3, 0.9375D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.0D, f, 0.5625D, 0.125D, f1, 0.8125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.875D, f, 0.5625D, 1.0D, f1, 0.8125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.0D, f2, 0.5625D, 0.125D, f3, 0.8125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.875D, f2, 0.5625D, 1.0D, f3, 0.8125D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
            else if (j == 2)
            {
                float f11 = 0.0F;
                float f19 = 0.125F;
                float f27 = 0.875F;
                float f35 = 1.0F;
                float f41 = 0.0625F;
                float f45 = 0.1875F;
                float f49 = 0.4375F;
                setRenderBounds(0.0D, f, 0.0625D, 0.125D, f3, 0.1875D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.875D, f, 0.0625D, 1.0D, f3, 0.1875D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.0D, f, 0.1875D, 0.125D, f1, 0.4375D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.875D, f, 0.1875D, 1.0D, f1, 0.4375D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.0D, f2, 0.1875D, 0.125D, f3, 0.4375D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                setRenderBounds(0.875D, f2, 0.1875D, 1.0D, f3, 0.4375D);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
        }
        else if (j == 3 || j == 1)
        {
            uvRotateTop = 1;
            float f12 = 0.4375F;
            float f20 = 0.5625F;
            float f28 = 0.375F;
            float f36 = 0.5F;
            setRenderBounds(f12, f, f28, f20, f3, f36);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f28 = 0.5F;
            f36 = 0.625F;
            setRenderBounds(f12, f, f28, f20, f3, f36);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f28 = 0.625F;
            f36 = 0.875F;
            setRenderBounds(f12, f, f28, f20, f1, f36);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            setRenderBounds(f12, f2, f28, f20, f3, f36);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f28 = 0.125F;
            f36 = 0.375F;
            setRenderBounds(f12, f, f28, f20, f1, f36);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            setRenderBounds(f12, f2, f28, f20, f3, f36);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }
        else
        {
            float f13 = 0.375F;
            float f21 = 0.5F;
            float f29 = 0.4375F;
            float f37 = 0.5625F;
            setRenderBounds(f13, f, f29, f21, f3, f37);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f13 = 0.5F;
            f21 = 0.625F;
            setRenderBounds(f13, f, f29, f21, f3, f37);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f13 = 0.625F;
            f21 = 0.875F;
            setRenderBounds(f13, f, f29, f21, f1, f37);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            setRenderBounds(f13, f2, f29, f21, f3, f37);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f13 = 0.125F;
            f21 = 0.375F;
            setRenderBounds(f13, f, f29, f21, f1, f37);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            setRenderBounds(f13, f2, f29, f21, f3, f37);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }

        renderAllFaces = false;
        uvRotateTop = 0;
        setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        return flag;
    }

    public boolean renderBlockHopper(BlockHopper par1BlockHopper, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockHopper.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1BlockHopper.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        int i = par1BlockHopper.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(i >> 16 & 0xff) / 255F;
        float f2 = (float)(i >> 8 & 0xff) / 255F;
        float f3 = (float)(i & 0xff) / 255F;

        if (EntityRenderer.anaglyphEnable)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f5 = (f1 * 30F + f2 * 70F) / 100F;
            float f6 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f5;
            f3 = f6;
        }

        tessellator.setColorOpaque_F(f * f1, f * f2, f * f3);
        return renderBlockHopperMetadata(par1BlockHopper, par2, par3, par4, blockAccess.getBlockMetadata(par2, par3, par4), false);
    }

    public boolean renderBlockHopperMetadata(BlockHopper par1BlockHopper, int par2, int par3, int par4, int par5, boolean par6)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = BlockHopper.getDirectionFromMetadata(par5);
        double d = 0.625D;
        setRenderBounds(0.0D, d, 0.0D, 1.0D, 1.0D, 1.0D);

        if (par6)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderFaceYNeg(par1BlockHopper, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockHopper, 0, par5));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(par1BlockHopper, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockHopper, 1, par5));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderFaceZNeg(par1BlockHopper, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockHopper, 2, par5));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderFaceZPos(par1BlockHopper, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockHopper, 3, par5));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderFaceXNeg(par1BlockHopper, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockHopper, 4, par5));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderFaceXPos(par1BlockHopper, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1BlockHopper, 5, par5));
            tessellator.draw();
        }
        else
        {
            renderStandardBlock(par1BlockHopper, par2, par3, par4);
        }

        if (!par6)
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1BlockHopper.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            }
            float f = Minecraft.oldlighting ? par1BlockHopper.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
            int j = par1BlockHopper.colorMultiplier(blockAccess, par2, par3, par4);
            float f1 = (float)(j >> 16 & 0xff) / 255F;
            float f3 = (float)(j >> 8 & 0xff) / 255F;
            float f4 = (float)(j & 0xff) / 255F;

            if (EntityRenderer.anaglyphEnable)
            {
                float f5 = (f1 * 30F + f3 * 59F + f4 * 11F) / 100F;
                float f6 = (f1 * 30F + f3 * 70F) / 100F;
                float f7 = (f1 * 30F + f4 * 70F) / 100F;
                f1 = f5;
                f3 = f6;
                f4 = f7;
            }

            tessellator.setColorOpaque_F(f * f1, f * f3, f * f4);
        }

        Icon icon = BlockHopper.getHopperIcon("hopper_outside");
        Icon icon1 = BlockHopper.getHopperIcon("hopper_inside");
        float f2 = 0.125F;

        if (par6)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderFaceXPos(par1BlockHopper, -1F + f2, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderFaceXNeg(par1BlockHopper, 1.0F - f2, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderFaceZPos(par1BlockHopper, 0.0D, 0.0D, -1F + f2, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderFaceZNeg(par1BlockHopper, 0.0D, 0.0D, 1.0F - f2, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(par1BlockHopper, 0.0D, -1D + d, 0.0D, icon1);
            tessellator.draw();
        }
        else
        {
            renderFaceXPos(par1BlockHopper, ((float)par2 - 1.0F) + f2, par3, par4, icon);
            renderFaceXNeg(par1BlockHopper, ((float)par2 + 1.0F) - f2, par3, par4, icon);
            renderFaceZPos(par1BlockHopper, par2, par3, ((float)par4 - 1.0F) + f2, icon);
            renderFaceZNeg(par1BlockHopper, par2, par3, ((float)par4 + 1.0F) - f2, icon);
            renderFaceYPos(par1BlockHopper, par2, (double)((float)par3 - 1.0F) + d, par4, icon1);
        }

        setOverrideBlockTexture(icon);
        double d1 = 0.25D;
        double d2 = 0.25D;
        double d3 = d;
        setRenderBounds(d1, d2, d1, 1.0D - d1, d3 - 0.002D, 1.0D - d1);

        if (par6)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderFaceXPos(par1BlockHopper, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderFaceXNeg(par1BlockHopper, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderFaceZPos(par1BlockHopper, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderFaceZNeg(par1BlockHopper, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(par1BlockHopper, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderFaceYNeg(par1BlockHopper, 0.0D, 0.0D, 0.0D, icon);
            tessellator.draw();
        }
        else
        {
            renderStandardBlock(par1BlockHopper, par2, par3, par4);
        }

        if (!par6)
        {
            double d4 = 0.375D;
            double d5 = 0.25D;
            setOverrideBlockTexture(icon);

            if (i == 0)
            {
                setRenderBounds(d4, 0.0D, d4, 1.0D - d4, 0.25D, 1.0D - d4);
                renderStandardBlock(par1BlockHopper, par2, par3, par4);
            }

            if (i == 2)
            {
                setRenderBounds(d4, d2, 0.0D, 1.0D - d4, d2 + d5, d1);
                renderStandardBlock(par1BlockHopper, par2, par3, par4);
            }

            if (i == 3)
            {
                setRenderBounds(d4, d2, 1.0D - d1, 1.0D - d4, d2 + d5, 1.0D);
                renderStandardBlock(par1BlockHopper, par2, par3, par4);
            }

            if (i == 4)
            {
                setRenderBounds(0.0D, d2, d4, d1, d2 + d5, 1.0D - d4);
                renderStandardBlock(par1BlockHopper, par2, par3, par4);
            }

            if (i == 5)
            {
                setRenderBounds(1.0D - d1, d2, d4, 1.0D, d2 + d5, 1.0D - d4);
                renderStandardBlock(par1BlockHopper, par2, par3, par4);
            }
        }

        clearOverrideBlockTexture();
        return true;
    }

    /**
     * Renders a stair block at the given coordinates
     */
    public boolean renderBlockStairs(BlockStairs par1BlockStairs, int par2, int par3, int par4)
    {
        par1BlockStairs.func_82541_d(blockAccess, par2, par3, par4);
        setRenderBoundsFromBlock(par1BlockStairs);
        renderStandardBlock(par1BlockStairs, par2, par3, par4);
        boolean flag = par1BlockStairs.func_82542_g(blockAccess, par2, par3, par4);
        setRenderBoundsFromBlock(par1BlockStairs);
        renderStandardBlock(par1BlockStairs, par2, par3, par4);

        if (flag && par1BlockStairs.func_82544_h(blockAccess, par2, par3, par4))
        {
            setRenderBoundsFromBlock(par1BlockStairs);
            renderStandardBlock(par1BlockStairs, par2, par3, par4);
        }

        return true;
    }

    /**
     * Renders a door block at the given coordinates
     */
    public boolean renderBlockDoor(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);

        if ((i & 8) != 0)
        {
            if (blockAccess.getBlockId(par2, par3 - 1, par4) != par1Block.blockID)
            {
                return false;
            }
        }
        else if (blockAccess.getBlockId(par2, par3 + 1, par4) != par1Block.blockID)
        {
            return false;
        }

        boolean flag = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        int j = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        float ff = 1.0F;
        float ff2 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(renderMinY <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4) : j);
        }else{
            ff = renderMinY <= 0.0D ? par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4) : ff2;
        }
        tessellator.setColorOpaque_F(f * ff, f * ff, f * ff);
        renderFaceYNeg(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 0));
        flag = true;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(renderMaxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4) : j);
        }else{
            ff = renderMaxY >= 1.0D ? par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4) : ff2;
        }
        tessellator.setColorOpaque_F(f1 * ff, f1 * ff, f1 * ff);
        renderFaceYPos(par1Block, par2, par3, par4, getBlockIcon(par1Block, blockAccess, par2, par3, par4, 1));
        flag = true;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(renderMinZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : j);
        }else{
            ff = renderMinZ <= 0.0D ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1) : ff2;
        }
        tessellator.setColorOpaque_F(f2 * ff, f2 * ff, f2 * ff);
        Icon icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 2);
        renderFaceZNeg(par1Block, par2, par3, par4, icon);
        flag = true;
        flipTexture = false;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(renderMaxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : j);
        }else{
            ff = renderMaxZ >= 1.0D ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1) : ff2;
        }
        tessellator.setColorOpaque_F(f2 * ff, f2 * ff, f2 * ff);
        icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 3);
        renderFaceZPos(par1Block, par2, par3, par4, icon);
        flag = true;
        flipTexture = false;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(renderMinX <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : j);
        }else{
            ff = renderMinX <= 0.0D ? par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4) : ff2;
        }
        tessellator.setColorOpaque_F(f3 * ff, f3 * ff, f3 * ff);
        icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 4);
        renderFaceXNeg(par1Block, par2, par3, par4, icon);
        flag = true;
        flipTexture = false;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(renderMaxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : j);
        }else{
            ff = renderMaxX >= 1.0D ? par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4) : ff2;
        }
        tessellator.setColorOpaque_F(f3 * ff, f3 * ff, f3 * ff);
        icon = getBlockIcon(par1Block, blockAccess, par2, par3, par4, 5);
        renderFaceXPos(par1Block, par2, par3, par4, icon);
        flag = true;
        flipTexture = false;
        return flag;
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
     */
    public void renderFaceYNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            par8Icon = overrideBlockTexture;
        }

        double d = par8Icon.getInterpolatedU(renderMinX * 16D);
        double d1 = par8Icon.getInterpolatedU(renderMaxX * 16D);
        double d2 = par8Icon.getInterpolatedV(renderMinZ * 16D);
        double d3 = par8Icon.getInterpolatedV(renderMaxZ * 16D);

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
            d = par8Icon.getMinU();
            d1 = par8Icon.getMaxU();
        }

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
            d2 = par8Icon.getMinV();
            d3 = par8Icon.getMaxV();
        }

        double d4 = d1;
        double d5 = d;
        double d6 = d2;
        double d7 = d3;

        if (uvRotateBottom == 2)
        {
            d = par8Icon.getInterpolatedU(renderMinZ * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMaxX * 16D);
            d1 = par8Icon.getInterpolatedU(renderMaxZ * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMinX * 16D);
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
            d4 = d;
            d5 = d1;
            d2 = d3;
            d3 = d6;
        }
        else if (uvRotateBottom == 1)
        {
            d = par8Icon.getInterpolatedU(16D - renderMaxZ * 16D);
            d2 = par8Icon.getInterpolatedV(renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMinZ * 16D);
            d3 = par8Icon.getInterpolatedV(renderMaxX * 16D);
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
            d = d4;
            d1 = d5;
            d6 = d3;
            d7 = d2;
        }
        else if (uvRotateBottom == 3)
        {
            d = par8Icon.getInterpolatedU(16D - renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMaxX * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMinZ * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMaxZ * 16D);
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
        }

        double d8 = par2 + renderMinX;
        double d9 = par2 + renderMaxX;
        double d10 = par4 + renderMinY;
        double d11 = par6 + renderMinZ;
        double d12 = par6 + renderMaxZ;

        if (enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.setBrightness(brightnessTopLeft);
            tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.setBrightness(brightnessBottomLeft);
            tessellator.addVertexWithUV(d8, d10, d11, d, d2);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.setBrightness(brightnessBottomRight);
            tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.setBrightness(brightnessTopRight);
            tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
        }
        else
        {
            tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
            tessellator.addVertexWithUV(d8, d10, d11, d, d2);
            tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
            tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
        }
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    public void renderFaceYPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            par8Icon = overrideBlockTexture;
        }

        double d = par8Icon.getInterpolatedU(renderMinX * 16D);
        double d1 = par8Icon.getInterpolatedU(renderMaxX * 16D);
        double d2 = par8Icon.getInterpolatedV(renderMinZ * 16D);
        double d3 = par8Icon.getInterpolatedV(renderMaxZ * 16D);

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
            d = par8Icon.getMinU();
            d1 = par8Icon.getMaxU();
        }

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
            d2 = par8Icon.getMinV();
            d3 = par8Icon.getMaxV();
        }

        double d4 = d1;
        double d5 = d;
        double d6 = d2;
        double d7 = d3;

        if (uvRotateTop == 1)
        {
            d = par8Icon.getInterpolatedU(renderMinZ * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMaxX * 16D);
            d1 = par8Icon.getInterpolatedU(renderMaxZ * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMinX * 16D);
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
            d4 = d;
            d5 = d1;
            d2 = d3;
            d3 = d6;
        }
        else if (uvRotateTop == 2)
        {
            d = par8Icon.getInterpolatedU(16D - renderMaxZ * 16D);
            d2 = par8Icon.getInterpolatedV(renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMinZ * 16D);
            d3 = par8Icon.getInterpolatedV(renderMaxX * 16D);
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
            d = d4;
            d1 = d5;
            d6 = d3;
            d7 = d2;
        }
        else if (uvRotateTop == 3)
        {
            d = par8Icon.getInterpolatedU(16D - renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMaxX * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMinZ * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMaxZ * 16D);
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
        }

        double d8 = par2 + renderMinX;
        double d9 = par2 + renderMaxX;
        double d10 = par4 + renderMaxY;
        double d11 = par6 + renderMinZ;
        double d12 = par6 + renderMaxZ;

        if (enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.setBrightness(brightnessTopLeft);
            tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.setBrightness(brightnessBottomLeft);
            tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.setBrightness(brightnessBottomRight);
            tessellator.addVertexWithUV(d8, d10, d11, d, d2);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.setBrightness(brightnessTopRight);
            tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
        }
        else
        {
            tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
            tessellator.addVertexWithUV(d9, d10, d11, d4, d6);
            tessellator.addVertexWithUV(d8, d10, d11, d, d2);
            tessellator.addVertexWithUV(d8, d10, d12, d5, d7);
        }
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceZNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            par8Icon = overrideBlockTexture;
        }

        double d = par8Icon.getInterpolatedU(renderMinX * 16D);
        double d1 = par8Icon.getInterpolatedU(renderMaxX * 16D);
        double d2 = par8Icon.getInterpolatedV(16D - renderMaxY * 16D);
        double d3 = par8Icon.getInterpolatedV(16D - renderMinY * 16D);

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
            d = par8Icon.getMinU();
            d1 = par8Icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
            d2 = par8Icon.getMinV();
            d3 = par8Icon.getMaxV();
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateEast == 2)
        {
            d = par8Icon.getInterpolatedU(renderMinY * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMaxX * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d5 = d;
            d6 = d1;
            d2 = d3;
            d3 = d7;
        }
        else if (uvRotateEast == 1)
        {
            d = par8Icon.getInterpolatedU(16D - renderMaxY * 16D);
            d2 = par8Icon.getInterpolatedV(renderMaxX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMinY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMinX * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d = d5;
            d1 = d6;
            d7 = d3;
            d8 = d2;
        }
        else if (uvRotateEast == 3)
        {
            d = par8Icon.getInterpolatedU(16D - renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMaxX * 16D);
            d2 = par8Icon.getInterpolatedV(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMinY * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + renderMinX;
        double d10 = par2 + renderMaxX;
        double d11 = par4 + renderMinY;
        double d12 = par4 + renderMaxY;
        double d13 = par6 + renderMinZ;

        if (enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.setBrightness(brightnessTopLeft);
            tessellator.addVertexWithUV(d9, d12, d13, d5, d7);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.setBrightness(brightnessBottomLeft);
            tessellator.addVertexWithUV(d10, d12, d13, d, d2);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.setBrightness(brightnessBottomRight);
            tessellator.addVertexWithUV(d10, d11, d13, d6, d8);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.setBrightness(brightnessTopRight);
            tessellator.addVertexWithUV(d9, d11, d13, d1, d3);
        }
        else
        {
            tessellator.addVertexWithUV(d9, d12, d13, d5, d7);
            tessellator.addVertexWithUV(d10, d12, d13, d, d2);
            tessellator.addVertexWithUV(d10, d11, d13, d6, d8);
            tessellator.addVertexWithUV(d9, d11, d13, d1, d3);
        }
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceZPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            par8Icon = overrideBlockTexture;
        }

        double d = par8Icon.getInterpolatedU(renderMinX * 16D);
        double d1 = par8Icon.getInterpolatedU(renderMaxX * 16D);
        double d2 = par8Icon.getInterpolatedV(16D - renderMaxY * 16D);
        double d3 = par8Icon.getInterpolatedV(16D - renderMinY * 16D);

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (renderMinX < 0.0D || renderMaxX > 1.0D)
        {
            d = par8Icon.getMinU();
            d1 = par8Icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
            d2 = par8Icon.getMinV();
            d3 = par8Icon.getMaxV();
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateWest == 1)
        {
            d = par8Icon.getInterpolatedU(renderMinY * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(renderMaxY * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMaxX * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d5 = d;
            d6 = d1;
            d2 = d3;
            d3 = d7;
        }
        else if (uvRotateWest == 2)
        {
            d = par8Icon.getInterpolatedU(16D - renderMaxY * 16D);
            d2 = par8Icon.getInterpolatedV(renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMinY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMaxX * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d = d5;
            d1 = d6;
            d7 = d3;
            d8 = d2;
        }
        else if (uvRotateWest == 3)
        {
            d = par8Icon.getInterpolatedU(16D - renderMinX * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMaxX * 16D);
            d2 = par8Icon.getInterpolatedV(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMinY * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + renderMinX;
        double d10 = par2 + renderMaxX;
        double d11 = par4 + renderMinY;
        double d12 = par4 + renderMaxY;
        double d13 = par6 + renderMaxZ;

        if (enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.setBrightness(brightnessTopLeft);
            tessellator.addVertexWithUV(d9, d12, d13, d, d2);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.setBrightness(brightnessBottomLeft);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.setBrightness(brightnessBottomRight);
            tessellator.addVertexWithUV(d10, d11, d13, d1, d3);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.setBrightness(brightnessTopRight);
            tessellator.addVertexWithUV(d10, d12, d13, d5, d7);
        }
        else
        {
            tessellator.addVertexWithUV(d9, d12, d13, d, d2);
            tessellator.addVertexWithUV(d9, d11, d13, d6, d8);
            tessellator.addVertexWithUV(d10, d11, d13, d1, d3);
            tessellator.addVertexWithUV(d10, d12, d13, d5, d7);
        }
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceXNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            par8Icon = overrideBlockTexture;
        }

        double d = par8Icon.getInterpolatedU(renderMinZ * 16D);
        double d1 = par8Icon.getInterpolatedU(renderMaxZ * 16D);
        double d2 = par8Icon.getInterpolatedV(16D - renderMaxY * 16D);
        double d3 = par8Icon.getInterpolatedV(16D - renderMinY * 16D);

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
            d = par8Icon.getMinU();
            d1 = par8Icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
            d2 = par8Icon.getMinV();
            d3 = par8Icon.getMaxV();
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateNorth == 1)
        {
            d = par8Icon.getInterpolatedU(renderMinY * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMaxZ * 16D);
            d1 = par8Icon.getInterpolatedU(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMinZ * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d5 = d;
            d6 = d1;
            d2 = d3;
            d3 = d7;
        }
        else if (uvRotateNorth == 2)
        {
            d = par8Icon.getInterpolatedU(16D - renderMaxY * 16D);
            d2 = par8Icon.getInterpolatedV(renderMinZ * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMinY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMaxZ * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d = d5;
            d1 = d6;
            d7 = d3;
            d8 = d2;
        }
        else if (uvRotateNorth == 3)
        {
            d = par8Icon.getInterpolatedU(16D - renderMinZ * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMaxZ * 16D);
            d2 = par8Icon.getInterpolatedV(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMinY * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + renderMinX;
        double d10 = par4 + renderMinY;
        double d11 = par4 + renderMaxY;
        double d12 = par6 + renderMinZ;
        double d13 = par6 + renderMaxZ;

        if (enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.setBrightness(brightnessTopLeft);
            tessellator.addVertexWithUV(d9, d11, d13, d5, d7);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.setBrightness(brightnessBottomLeft);
            tessellator.addVertexWithUV(d9, d11, d12, d, d2);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.setBrightness(brightnessBottomRight);
            tessellator.addVertexWithUV(d9, d10, d12, d6, d8);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.setBrightness(brightnessTopRight);
            tessellator.addVertexWithUV(d9, d10, d13, d1, d3);
        }
        else
        {
            tessellator.addVertexWithUV(d9, d11, d13, d5, d7);
            tessellator.addVertexWithUV(d9, d11, d12, d, d2);
            tessellator.addVertexWithUV(d9, d10, d12, d6, d8);
            tessellator.addVertexWithUV(d9, d10, d13, d1, d3);
        }
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceXPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon)
    {
        Tessellator tessellator = Tessellator.instance;

        if (hasOverrideBlockTexture())
        {
            par8Icon = overrideBlockTexture;
        }

        double d = par8Icon.getInterpolatedU(renderMinZ * 16D);
        double d1 = par8Icon.getInterpolatedU(renderMaxZ * 16D);
        double d2 = par8Icon.getInterpolatedV(16D - renderMaxY * 16D);
        double d3 = par8Icon.getInterpolatedV(16D - renderMinY * 16D);

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (renderMinZ < 0.0D || renderMaxZ > 1.0D)
        {
            d = par8Icon.getMinU();
            d1 = par8Icon.getMaxU();
        }

        if (renderMinY < 0.0D || renderMaxY > 1.0D)
        {
            d2 = par8Icon.getMinV();
            d3 = par8Icon.getMaxV();
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateSouth == 2)
        {
            d = par8Icon.getInterpolatedU(renderMinY * 16D);
            d2 = par8Icon.getInterpolatedV(16D - renderMinZ * 16D);
            d1 = par8Icon.getInterpolatedU(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(16D - renderMaxZ * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d5 = d;
            d6 = d1;
            d2 = d3;
            d3 = d7;
        }
        else if (uvRotateSouth == 1)
        {
            d = par8Icon.getInterpolatedU(16D - renderMaxY * 16D);
            d2 = par8Icon.getInterpolatedV(renderMaxZ * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMinY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMinZ * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
            d = d5;
            d1 = d6;
            d7 = d3;
            d8 = d2;
        }
        else if (uvRotateSouth == 3)
        {
            d = par8Icon.getInterpolatedU(16D - renderMinZ * 16D);
            d1 = par8Icon.getInterpolatedU(16D - renderMaxZ * 16D);
            d2 = par8Icon.getInterpolatedV(renderMaxY * 16D);
            d3 = par8Icon.getInterpolatedV(renderMinY * 16D);
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + renderMaxX;
        double d10 = par4 + renderMinY;
        double d11 = par4 + renderMaxY;
        double d12 = par6 + renderMinZ;
        double d13 = par6 + renderMaxZ;

        if (enableAO)
        {
            tessellator.setColorOpaque_F(colorRedTopLeft, colorGreenTopLeft, colorBlueTopLeft);
            tessellator.setBrightness(brightnessTopLeft);
            tessellator.addVertexWithUV(d9, d10, d13, d6, d8);
            tessellator.setColorOpaque_F(colorRedBottomLeft, colorGreenBottomLeft, colorBlueBottomLeft);
            tessellator.setBrightness(brightnessBottomLeft);
            tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
            tessellator.setColorOpaque_F(colorRedBottomRight, colorGreenBottomRight, colorBlueBottomRight);
            tessellator.setBrightness(brightnessBottomRight);
            tessellator.addVertexWithUV(d9, d11, d12, d5, d7);
            tessellator.setColorOpaque_F(colorRedTopRight, colorGreenTopRight, colorBlueTopRight);
            tessellator.setBrightness(brightnessTopRight);
            tessellator.addVertexWithUV(d9, d11, d13, d, d2);
        }
        else
        {
            tessellator.addVertexWithUV(d9, d10, d13, d6, d8);
            tessellator.addVertexWithUV(d9, d10, d12, d1, d3);
            tessellator.addVertexWithUV(d9, d11, d12, d5, d7);
            tessellator.addVertexWithUV(d9, d11, d13, d, d2);
        }
    }

    /**
     * Is called to render the image of a block on an inventory, as a held item, or as a an item on the ground
     */
    public void renderBlockAsItem(Block par1Block, int par2, float par3)
    {
        Tessellator tessellator = Tessellator.instance;
        boolean flag = par1Block.blockID == Block.grass.blockID;

        if (par1Block == Block.dispenser || par1Block == Block.dropper || par1Block == Block.furnaceIdle)
        {
            par2 = 3;
        }

        if (useInventoryTint)
        {
            int i = par1Block.getRenderColor(par2);

            if (flag)
            {
                i = 0xffffff;
            }

            float f = (float)(i >> 16 & 0xff) / 255F;
            float f2 = (float)(i >> 8 & 0xff) / 255F;
            float f6 = (float)(i & 0xff) / 255F;
            GL11.glColor4f(f * par3, f2 * par3, f6 * par3, 1.0F);
        }

        int j = par1Block.getRenderType();
        setRenderBoundsFromBlock(par1Block);

        if (j == 0 || j == 31 || j == 39 || j == 16 || j == 26)
        {
            if (j == 16)
            {
                par2 = 1;
            }

            par1Block.setBlockBoundsForItemRender();
            setRenderBoundsFromBlock(par1Block);
            if (!oldrotation){
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            }
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 0, par2));
            tessellator.draw();

            if (flag && useInventoryTint)
            {
                int k = par1Block.getRenderColor(par2);
                float f3 = (float)(k >> 16 & 0xff) / 255F;
                float f7 = (float)(k >> 8 & 0xff) / 255F;
                float f8 = (float)(k & 0xff) / 255F;
                GL11.glColor4f(f3 * par3, f7 * par3, f8 * par3, 1.0F);
            }

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 1, par2));
            tessellator.draw();

            if (flag && useInventoryTint)
            {
                GL11.glColor4f(par3, par3, par3, 1.0F);
            }

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 2, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 3, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 4, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 5, par2));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        else if (j == 1)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            drawCrossedSquares(par1Block, par2, -0.5D, -0.5D, -0.5D, 1.0F);
            tessellator.draw();
        }
        else if (j == 19)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            par1Block.setBlockBoundsForItemRender();
            renderBlockStemSmall(par1Block, par2, renderMaxY, -0.5D, -0.5D, -0.5D);
            tessellator.draw();
        }
        else if (j == 23)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            par1Block.setBlockBoundsForItemRender();
            tessellator.draw();
        }
        else if (j == 13)
        {
            par1Block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = 0.0625F;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 0));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            tessellator.addTranslation(0.0F, 0.0F, f1);
            renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 2));
            tessellator.addTranslation(0.0F, 0.0F, -f1);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addTranslation(0.0F, 0.0F, -f1);
            renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 3));
            tessellator.addTranslation(0.0F, 0.0F, f1);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            tessellator.addTranslation(f1, 0.0F, 0.0F);
            renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 4));
            tessellator.addTranslation(-f1, 0.0F, 0.0F);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            tessellator.addTranslation(-f1, 0.0F, 0.0F);
            renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 5));
            tessellator.addTranslation(f1, 0.0F, 0.0F);
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        else if (j == 22)
        {
            if (!oldrotation){
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            }
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            ChestItemRenderHelper.instance.renderChest(par1Block, par2, par3);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
        else if (j == 6)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderBlockCropsImpl(par1Block, par2, -0.5D, -0.5D, -0.5D);
            tessellator.draw();
        }
        else if (j == 2)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderTorchAtAngle(par1Block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D, 0);
            tessellator.draw();
        }
        else if (j == 10)
        {
            for (int l = 0; l < 2; l++)
            {
                if (l == 0)
                {
                    setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.5D);
                }

                if (l == 1)
                {
                    setRenderBounds(0.0D, 0.0D, 0.5D, 1.0D, 0.5D, 1.0D);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
        else if (j == 27)
        {
            int i1 = 0;
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();

            for (int j2 = 0; j2 < 8; j2++)
            {
                int k2 = 0;
                byte byte0 = 1;

                if (j2 == 0)
                {
                    k2 = 2;
                }

                if (j2 == 1)
                {
                    k2 = 3;
                }

                if (j2 == 2)
                {
                    k2 = 4;
                }

                if (j2 == 3)
                {
                    k2 = 5;
                    byte0 = 2;
                }

                if (j2 == 4)
                {
                    k2 = 6;
                    byte0 = 3;
                }

                if (j2 == 5)
                {
                    k2 = 7;
                    byte0 = 5;
                }

                if (j2 == 6)
                {
                    k2 = 6;
                    byte0 = 2;
                }

                if (j2 == 7)
                {
                    k2 = 3;
                }

                float f9 = (float)k2 / 16F;
                float f10 = 1.0F - (float)i1 / 16F;
                float f11 = 1.0F - (float)(i1 + byte0) / 16F;
                i1 += byte0;
                setRenderBounds(0.5F - f9, f11, 0.5F - f9, 0.5F + f9, f10, 0.5F + f9);
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 0));
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 1));
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 2));
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 3));
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 4));
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 5));
            }

            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        }
        else if (j == 11)
        {
            for (int j1 = 0; j1 < 4; j1++)
            {
                float f4 = 0.125F;

                if (j1 == 0)
                {
                    setRenderBounds(0.5F - f4, 0.0D, 0.0D, 0.5F + f4, 1.0D, f4 * 2.0F);
                }

                if (j1 == 1)
                {
                    setRenderBounds(0.5F - f4, 0.0D, 1.0F - f4 * 2.0F, 0.5F + f4, 1.0D, 1.0D);
                }

                f4 = 0.0625F;

                if (j1 == 2)
                {
                    setRenderBounds(0.5F - f4, 1.0F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 1.0F - f4, 1.0F + f4 * 2.0F);
                }

                if (j1 == 3)
                {
                    setRenderBounds(0.5F - f4, 0.5F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 0.5F - f4, 1.0F + f4 * 2.0F);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        }
        else if (j == 21)
        {
            for (int k1 = 0; k1 < 3; k1++)
            {
                float f5 = 0.0625F;

                if (k1 == 0)
                {
                    setRenderBounds(0.5F - f5, 0.30000001192092896D, 0.0D, 0.5F + f5, 1.0D, f5 * 2.0F);
                }

                if (k1 == 1)
                {
                    setRenderBounds(0.5F - f5, 0.30000001192092896D, 1.0F - f5 * 2.0F, 0.5F + f5, 1.0D, 1.0D);
                }

                f5 = 0.0625F;

                if (k1 == 2)
                {
                    setRenderBounds(0.5F - f5, 0.5D, 0.0D, 0.5F + f5, 1.0F - f5, 1.0D);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSide(par1Block, 5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
        else if (j == 32)
        {
            for (int l1 = 0; l1 < 2; l1++)
            {
                if (l1 == 0)
                {
                    setRenderBounds(0.0D, 0.0D, 0.3125D, 1.0D, 0.8125D, 0.6875D);
                }

                if (l1 == 1)
                {
                    setRenderBounds(0.25D, 0.0D, 0.25D, 0.75D, 1.0D, 0.75D);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 0, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 1, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 2, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 3, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 4, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 5, par2));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
        }
        else if (j == 35)
        {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderBlockAnvilOrient((BlockAnvil)par1Block, 0, 0, 0, par2 << 2, true);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        else if (j == 34)
        {
            for (int i2 = 0; i2 < 3; i2++)
            {
                if (i2 == 0)
                {
                    setRenderBounds(0.125D, 0.0D, 0.125D, 0.875D, 0.1875D, 0.875D);
                    setOverrideBlockTexture(getBlockIcon(Block.obsidian));
                }
                else if (i2 == 1)
                {
                    setRenderBounds(0.1875D, 0.1875D, 0.1875D, 0.8125D, 0.875D, 0.8125D);
                    setOverrideBlockTexture(getBlockIcon(Block.beacon));
                }
                else if (i2 == 2)
                {
                    setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
                    setOverrideBlockTexture(getBlockIcon(Block.glass));
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderFaceYNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 0, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderFaceYPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 1, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderFaceZNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 2, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderFaceZPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 3, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderFaceXNeg(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 4, par2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderFaceXPos(par1Block, 0.0D, 0.0D, 0.0D, getBlockIconFromSideAndMetadata(par1Block, 5, par2));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            setRenderBounds(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
            clearOverrideBlockTexture();
        }
        else if (j == 38)
        {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            renderBlockHopperMetadata((BlockHopper)par1Block, 0, 0, 0, 0, true);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        else
        {
            Minecraft.invokeModMethod("ModLoader", "renderInvBlock",
                                      new Class[]{RenderBlocks.class, Block.class, Integer.TYPE, Integer.TYPE},
                                      this, par1Block, par2, j);
        }
    }

    /**
     * Checks to see if the item's render type indicates that it should be rendered as a regular block or not.
     */
    public static boolean renderItemIn3d(int par0)
    {
        if (par0 == 0)
        {
            return true;
        }

        if (par0 == 31)
        {
            return true;
        }

        if (par0 == 39)
        {
            return true;
        }

        if (par0 == 13)
        {
            return true;
        }

        if (par0 == 10)
        {
            return true;
        }

        if (par0 == 11)
        {
            return true;
        }

        if (par0 == 27)
        {
            return true;
        }

        if (par0 == 22)
        {
            return true;
        }

        if (par0 == 21)
        {
            return true;
        }

        if (par0 == 16)
        {
            return true;
        }

        if (par0 == 26)
        {
            return true;
        }

        if (par0 == 32)
        {
            return true;
        }

        if (par0 == 34)
        {
            return true;
        }
        else
        {
            Object o = Minecraft.invokeModMethod("ModLoader", "renderBlockIsItemFull3D", new Class[]{Integer.TYPE}, par0);
            if (o != null){
                return (Boolean)o;
            }
        }

        return par0 == 35;
    }

    public Icon getBlockIcon(Block par1Block, IBlockAccess par2IBlockAccess, int par3, int par4, int par5, int par6)
    {
        return getIconSafe(par1Block.getBlockTexture(par2IBlockAccess, par3, par4, par5, par6));
    }

    public Icon getBlockIconFromSideAndMetadata(Block par1Block, int par2, int par3)
    {
        return getIconSafe(par1Block.getIcon(par2, par3));
    }

    public Icon getBlockIconFromSide(Block par1Block, int par2)
    {
        return getIconSafe(par1Block.getBlockTextureFromSide(par2));
    }

    public Icon getBlockIcon(Block par1Block)
    {
        return getIconSafe(par1Block.getBlockTextureFromSide(1));
    }

    public Icon getIconSafe(Icon par1Icon)
    {
        if (par1Icon == null)
        {
            par1Icon = ((TextureMap)Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        }

        return par1Icon;
    }
}
