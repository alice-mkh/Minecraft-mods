package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderBlocks
{
    /** The IBlockAccess used by this instance of RenderBlocks */
    public IBlockAccess blockAccess;

    /**
     * If set to >=0, all block faces will be rendered using this texture index
     */
    public int overrideBlockTexture;

    /**
     * Set to true if the texture should be flipped horizontally during render*Face
     */
    private boolean flipTexture;

    /**
     * If true, renders all faces on all blocks rather than using the logic in Block.shouldSideBeRendered.  Unused.
     */
    private boolean renderAllFaces;

    /** Fancy grass side matching biome */
    public static boolean fancyGrass = true;
    public static boolean cfgGrassFix = true;
    public boolean useInventoryTint;
    private int uvRotateEast;
    private int uvRotateWest;
    private int uvRotateSouth;
    private int uvRotateNorth;
    private int uvRotateTop;
    private int uvRotateBottom;

    /** Whether ambient occlusion is enabled or not */
    private boolean enableAO;

    /** Light value of the block itself */
    private float lightValueOwn;

    /** Light value one block less in x axis */
    private float aoLightValueXNeg;

    /** Light value one block more in y axis */
    private float aoLightValueYNeg;

    /** Light value one block more in z axis */
    private float aoLightValueZNeg;

    /** Light value one block more in x axis */
    private float aoLightValueXPos;

    /** Light value one block more in y axis */
    private float aoLightValueYPos;

    /** Light value one block more in z axis */
    private float aoLightValueZPos;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/east corner.
     */
    private float aoLightValueScratchXYZNNN;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the north face.
     */
    private float aoLightValueScratchXYNN;

    /**
     * Used as a scratch variable for ambient occlusion on the north/bottom/west corner.
     */
    private float aoLightValueScratchXYZNNP;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the east face.
     */
    private float aoLightValueScratchYZNN;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the west face.
     */
    private float aoLightValueScratchYZNP;

    /**
     * Used as a scratch variable for ambient occlusion on the south/bottom/east corner.
     */
    private float aoLightValueScratchXYZPNN;

    /**
     * Used as a scratch variable for ambient occlusion between the bottom face and the south face.
     */
    private float aoLightValueScratchXYPN;

    /**
     * Used as a scratch variable for ambient occlusion on the south/bottom/west corner.
     */
    private float aoLightValueScratchXYZPNP;

    /**
     * Used as a scratch variable for ambient occlusion on the north/top/east corner.
     */
    private float aoLightValueScratchXYZNPN;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the north face.
     */
    private float aoLightValueScratchXYNP;

    /**
     * Used as a scratch variable for ambient occlusion on the north/top/west corner.
     */
    private float aoLightValueScratchXYZNPP;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the east face.
     */
    private float aoLightValueScratchYZPN;

    /**
     * Used as a scratch variable for ambient occlusion on the south/top/east corner.
     */
    private float aoLightValueScratchXYZPPN;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the south face.
     */
    private float aoLightValueScratchXYPP;

    /**
     * Used as a scratch variable for ambient occlusion between the top face and the west face.
     */
    private float aoLightValueScratchYZPP;

    /**
     * Used as a scratch variable for ambient occlusion on the south/top/west corner.
     */
    private float aoLightValueScratchXYZPPP;

    /**
     * Used as a scratch variable for ambient occlusion between the north face and the east face.
     */
    private float aoLightValueScratchXZNN;

    /**
     * Used as a scratch variable for ambient occlusion between the south face and the east face.
     */
    private float aoLightValueScratchXZPN;

    /**
     * Used as a scratch variable for ambient occlusion between the north face and the west face.
     */
    private float aoLightValueScratchXZNP;

    /**
     * Used as a scratch variable for ambient occlusion between the south face and the west face.
     */
    private float aoLightValueScratchXZPP;

    /** Ambient occlusion brightness XYZNNN */
    private int aoBrightnessXYZNNN;

    /** Ambient occlusion brightness XYNN */
    private int aoBrightnessXYNN;

    /** Ambient occlusion brightness XYZNNP */
    private int aoBrightnessXYZNNP;

    /** Ambient occlusion brightness YZNN */
    private int aoBrightnessYZNN;

    /** Ambient occlusion brightness YZNP */
    private int aoBrightnessYZNP;

    /** Ambient occlusion brightness XYZPNN */
    private int aoBrightnessXYZPNN;

    /** Ambient occlusion brightness XYPN */
    private int aoBrightnessXYPN;

    /** Ambient occlusion brightness XYZPNP */
    private int aoBrightnessXYZPNP;

    /** Ambient occlusion brightness XYZNPN */
    private int aoBrightnessXYZNPN;

    /** Ambient occlusion brightness XYNP */
    private int aoBrightnessXYNP;

    /** Ambient occlusion brightness XYZNPP */
    private int aoBrightnessXYZNPP;

    /** Ambient occlusion brightness YZPN */
    private int aoBrightnessYZPN;

    /** Ambient occlusion brightness XYZPPN */
    private int aoBrightnessXYZPPN;

    /** Ambient occlusion brightness XYPP */
    private int aoBrightnessXYPP;

    /** Ambient occlusion brightness YZPP */
    private int aoBrightnessYZPP;

    /** Ambient occlusion brightness XYZPPP */
    private int aoBrightnessXYZPPP;

    /** Ambient occlusion brightness XZNN */
    private int aoBrightnessXZNN;

    /** Ambient occlusion brightness XZPN */
    private int aoBrightnessXZPN;

    /** Ambient occlusion brightness XZNP */
    private int aoBrightnessXZNP;

    /** Ambient occlusion brightness XZPP */
    private int aoBrightnessXZPP;

    /** Ambient occlusion type (0=simple, 1=complex) */
    private int aoType;

    /** Brightness top left */
    private int brightnessTopLeft;

    /** Brightness bottom left */
    private int brightnessBottomLeft;

    /** Brightness bottom right */
    private int brightnessBottomRight;

    /** Brightness top right */
    private int brightnessTopRight;

    /** Red color value for the top left corner */
    private float colorRedTopLeft;

    /** Red color value for the bottom left corner */
    private float colorRedBottomLeft;

    /** Red color value for the bottom right corner */
    private float colorRedBottomRight;

    /** Red color value for the top right corner */
    private float colorRedTopRight;

    /** Green color value for the top left corner */
    private float colorGreenTopLeft;

    /** Green color value for the bottom left corner */
    private float colorGreenBottomLeft;

    /** Green color value for the bottom right corner */
    private float colorGreenBottomRight;

    /** Green color value for the top right corner */
    private float colorGreenTopRight;

    /** Blue color value for the top left corner */
    private float colorBlueTopLeft;

    /** Blue color value for the bottom left corner */
    private float colorBlueBottomLeft;

    /** Blue color value for the bottom right corner */
    private float colorBlueBottomRight;

    /** Blue color value for the top right corner */
    private float colorBlueTopRight;

    /**
     * Grass flag for ambient occlusion on Center X, Positive Y, and Negative Z
     */
    private boolean aoGrassXYZCPN;

    /**
     * Grass flag for ambient occlusion on Positive X, Positive Y, and Center Z
     */
    private boolean aoGrassXYZPPC;

    /**
     * Grass flag for ambient occlusion on Negative X, Positive Y, and Center Z
     */
    private boolean aoGrassXYZNPC;

    /**
     * Grass flag for ambient occlusion on Center X, Positive Y, and Positive Z
     */
    private boolean aoGrassXYZCPP;

    /**
     * Grass flag for ambient occlusion on Negative X, Center Y, and Negative Z
     */
    private boolean aoGrassXYZNCN;

    /**
     * Grass flag for ambient occlusion on Positive X, Center Y, and Positive Z
     */
    private boolean aoGrassXYZPCP;

    /**
     * Grass flag for ambient occlusion on Negative X, Center Y, and Positive Z
     */
    private boolean aoGrassXYZNCP;

    /**
     * Grass flag for ambient occlusion on Positive X, Center Y, and Negative Z
     */
    private boolean aoGrassXYZPCN;

    /**
     * Grass flag for ambient occlusion on Center X, Negative Y, and Negative Z
     */
    private boolean aoGrassXYZCNN;

    /**
     * Grass flag for ambient occlusion on Positive X, Negative Y, and Center Z
     */
    private boolean aoGrassXYZPNC;

    /**
     * Grass flag for ambient occlusion on Negative X, Negative Y, and center Z
     */
    private boolean aoGrassXYZNNC;

    /**
     * Grass flag for ambient occlusion on Center X, Negative Y, and Positive Z
     */
    private boolean aoGrassXYZCNP;

    public RenderBlocks(IBlockAccess par1IBlockAccess)
    {
        overrideBlockTexture = -1;
        flipTexture = false;
        renderAllFaces = false;
        useInventoryTint = true;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        aoType = 1;
        blockAccess = par1IBlockAccess;
    }

    public RenderBlocks()
    {
        overrideBlockTexture = -1;
        flipTexture = false;
        renderAllFaces = false;
        useInventoryTint = true;
        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        aoType = 1;
    }

    /**
     * Clear override block texture
     */
    public void clearOverrideBlockTexture()
    {
        overrideBlockTexture = -1;
    }

    /**
     * Renders a block using the given texture instead of the block's own default texture
     */
    public void renderBlockUsingTexture(Block par1Block, int par2, int par3, int par4, int par5)
    {
        overrideBlockTexture = par5;
        renderBlockByRenderType(par1Block, par2, par3, par4);
        overrideBlockTexture = -1;
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
        par1Block.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);

        if (i == 0)
        {
            return renderStandardBlock(par1Block, par2, par3, par4);
        }

        if (i == 31)
        {
            return func_78581_r(par1Block, par2, par3, par4);
        }

        if (i == 4)
        {
            return renderBlockFluids(par1Block, par2, par3, par4);
        }

        if (i == 13)
        {
            return renderBlockCactus(par1Block, par2, par3, par4);
        }

        if (i == 1)
        {
            return renderCrossedSquares(par1Block, par2, par3, par4);
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

        if (i == 2)
        {
            return renderBlockTorch(par1Block, par2, par3, par4);
        }

        if (i == 3)
        {
            return renderBlockFire(par1Block, par2, par3, par4);
        }

        if (i == 5)
        {
            return renderBlockRedstoneWire(par1Block, par2, par3, par4);
        }

        if (i == 8)
        {
            return renderBlockLadder(par1Block, par2, par3, par4);
        }

        if (i == 7)
        {
            return renderBlockDoor(par1Block, par2, par3, par4);
        }

        if (i == 9)
        {
            return renderBlockMinecartTrack((BlockRail)par1Block, par2, par3, par4);
        }

        if (i == 10)
        {
            return renderBlockStairs(par1Block, par2, par3, par4);
        }

        if (i == 27)
        {
            return renderBlockDragonEgg((BlockDragonEgg)par1Block, par2, par3, par4);
        }

        if (i == 11)
        {
            return renderBlockFence((BlockFence)par1Block, par2, par3, par4);
        }

        if (i == 12)
        {
            return renderBlockLever(par1Block, par2, par3, par4);
        }

        if (i == 29)
        {
            return func_78577_f(par1Block, par2, par3, par4);
        }

        if (i == 30)
        {
            return func_78619_g(par1Block, par2, par3, par4);
        }

        if (i == 14)
        {
            return renderBlockBed(par1Block, par2, par3, par4);
        }

        if (i == 15)
        {
            return renderBlockRepeater(par1Block, par2, par3, par4);
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

        if (i == 20)
        {
            return renderBlockVine(par1Block, par2, par3, par4);
        }

        if (i == 21)
        {
            return renderBlockFenceGate((BlockFenceGate)par1Block, par2, par3, par4);
        }

        if (i == 24)
        {
            return renderBlockCauldron((BlockCauldron)par1Block, par2, par3, par4);
        }

        if (i == 25)
        {
            return renderBlockBrewingStand((BlockBrewingStand)par1Block, par2, par3, par4);
        }

        if (i == 26)
        {
            return renderBlockEndPortalFrame(par1Block, par2, par3, par4);
        }

        if (i == 28)
        {
            return func_78616_a((BlockCocoa)par1Block, par2, par3, par4);
        }
        else
        {
            return Minecraft.getMinecraft().renderBlocksMod(this, blockAccess, par1Block, par2, par3, par4, i, overrideBlockTexture);
        }
    }

    /**
     * Render BlockEndPortalFrame
     */
    private boolean renderBlockEndPortalFrame(Block par1Block, int par2, int par3, int par4)
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
            par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
            renderStandardBlock(par1Block, par2, par3, par4);
            par1Block.setBlockBoundsForItemRender();
            uvRotateTop = 0;
            return true;
        }
        else
        {
            par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.8125F, 1.0F);
            renderStandardBlock(par1Block, par2, par3, par4);
            overrideBlockTexture = 174;
            par1Block.setBlockBounds(0.25F, 0.8125F, 0.25F, 0.75F, 1.0F, 0.75F);
            renderStandardBlock(par1Block, par2, par3, par4);
            clearOverrideBlockTexture();
            par1Block.setBlockBoundsForItemRender();
            uvRotateTop = 0;
            return true;
        }
    }

    /**
     * render a bed at the given coordinates
     */
    private boolean renderBlockBed(Block par1Block, int par2, int par3, int par4)
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
        int l = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 0);
        int i1 = (l & 0xf) << 4;
        int j1 = l & 0xf0;
        double d = (float)i1 / 256F;
        double d1 = ((double)(i1 + 16) - 0.01D) / 256D;
        double d2 = (float)j1 / 256F;
        double d3 = ((double)(j1 + 16) - 0.01D) / 256D;
        double d4 = (double)par2 + par1Block.minX;
        double d5 = (double)par2 + par1Block.maxX;
        double d6 = (double)par3 + par1Block.minY + 0.1875D;
        double d7 = (double)par4 + par1Block.minZ;
        double d8 = (double)par4 + par1Block.maxZ;
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
        l = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 1);
        i1 = (l & 0xf) << 4;
        j1 = l & 0xf0;
        d = (float)i1 / 256F;
        d1 = ((double)(i1 + 16) - 0.01D) / 256D;
        d2 = (float)j1 / 256F;
        d3 = ((double)(j1 + 16) - 0.01D) / 256D;
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

        double d12 = (double)par2 + par1Block.minX;
        double d13 = (double)par2 + par1Block.maxX;
        double d14 = (double)par3 + par1Block.maxY;
        double d15 = (double)par4 + par1Block.minZ;
        double d16 = (double)par4 + par1Block.maxZ;
        tessellator.addVertexWithUV(d13, d14, d16, d8, d10);
        tessellator.addVertexWithUV(d13, d14, d15, d4, d6);
        tessellator.addVertexWithUV(d12, d14, d15, d5, d7);
        tessellator.addVertexWithUV(d12, d14, d16, d9, d11);
        int k1 = Direction.headInvisibleFace[j];

        if (flag)
        {
            k1 = Direction.headInvisibleFace[Direction.footInvisibleFaceRemap[j]];
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

        if (k1 != 2 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                tessellator.setColorOpaque_F(f8 * ff, f11 * ff, f14 * ff);
            }else{
                tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : k);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }
            flipTexture = byte0 == 2;
            renderEastFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 2));
        }

        if (k1 != 3 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                tessellator.setColorOpaque_F(f8 * ff, f11 * ff, f14 * ff);
            }else{
                tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : k);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }
            flipTexture = byte0 == 3;
            renderWestFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3));
        }

        if (k1 != 4 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                tessellator.setColorOpaque_F(f9 * ff, f12 * ff, f15 * ff);
            }else{
                tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : k);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }
            flipTexture = byte0 == 4;
            renderNorthFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 4));
        }

        if (k1 != 5 && (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5)))
        {
            if (Minecraft.oldlighting){
                float ff = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                tessellator.setColorOpaque_F(f9 * ff, f12 * ff, f15 * ff);
            }else{
                tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : k);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }
            flipTexture = byte0 == 5;
            renderSouthFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 5));
        }

        flipTexture = false;
        return true;
    }

    /**
     * Render BlockBrewingStand
     */
    private boolean renderBlockBrewingStand(BlockBrewingStand par1BlockBrewingStand, int par2, int par3, int par4)
    {
        par1BlockBrewingStand.setBlockBounds(0.4375F, 0.0F, 0.4375F, 0.5625F, 0.875F, 0.5625F);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        overrideBlockTexture = 156;
        par1BlockBrewingStand.setBlockBounds(0.5625F, 0.0F, 0.3125F, 0.9375F, 0.125F, 0.6875F);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        par1BlockBrewingStand.setBlockBounds(0.125F, 0.0F, 0.0625F, 0.5F, 0.125F, 0.4375F);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
        par1BlockBrewingStand.setBlockBounds(0.125F, 0.0F, 0.5625F, 0.5F, 0.125F, 0.9375F);
        renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
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
        int j = par1BlockBrewingStand.getBlockTextureFromSideAndMetadata(0, 0);

        if (overrideBlockTexture >= 0)
        {
            j = overrideBlockTexture;
        }

        int k = (j & 0xf) << 4;
        int l = j & 0xf0;
        double d = (float)l / 256F;
        double d1 = ((float)l + 15.99F) / 256F;
        int i1 = blockAccess.getBlockMetadata(par2, par3, par4);

        for (int j1 = 0; j1 < 3; j1++)
        {
            double d2 = ((double)j1 * Math.PI * 2D) / 3D + (Math.PI / 2D);
            double d3 = ((float)k + 8F) / 256F;
            double d4 = ((float)k + 15.99F) / 256F;

            if ((i1 & 1 << j1) != 0)
            {
                d3 = ((float)k + 7.99F) / 256F;
                d4 = ((float)k + 0.0F) / 256F;
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
    private boolean renderBlockCauldron(BlockCauldron par1BlockCauldron, int par2, int par3, int par4)
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
        char c = '\232';
        float f6 = 0.125F;
        renderSouthFace(par1BlockCauldron, ((float)par2 - 1.0F) + f6, par3, par4, c);
        renderNorthFace(par1BlockCauldron, ((float)par2 + 1.0F) - f6, par3, par4, c);
        renderWestFace(par1BlockCauldron, par2, par3, ((float)par4 - 1.0F) + f6, c);
        renderEastFace(par1BlockCauldron, par2, par3, ((float)par4 + 1.0F) - f6, c);
        char c1 = '\213';
        renderTopFace(par1BlockCauldron, par2, ((float)par3 - 1.0F) + 0.25F, par4, c1);
        renderBottomFace(par1BlockCauldron, par2, ((float)par3 + 1.0F) - 0.75F, par4, c1);
        int j = blockAccess.getBlockMetadata(par2, par3, par4);

        if (j > 0)
        {
            char c2 = '\315';

            if (j > 3)
            {
                j = 3;
            }

            renderTopFace(par1BlockCauldron, par2, ((float)par3 - 1.0F) + (6F + (float)j * 3F) / 16F, par4, c2);
        }

        return true;
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
            renderTorchAtAngle(par1Block, (double)par2 - d1, (double)par3 + d2, par4, -d, 0.0D);
        }
        else if (i == 2)
        {
            renderTorchAtAngle(par1Block, (double)par2 + d1, (double)par3 + d2, par4, d, 0.0D);
        }
        else if (i == 3)
        {
            renderTorchAtAngle(par1Block, par2, (double)par3 + d2, (double)par4 - d1, 0.0D, -d);
        }
        else if (i == 4)
        {
            renderTorchAtAngle(par1Block, par2, (double)par3 + d2, (double)par4 + d1, 0.0D, d);
        }
        else
        {
            renderTorchAtAngle(par1Block, par2, par3, par4, 0.0D, 0.0D);
        }

        return true;
    }

    /**
     * render a redstone repeater at the given coordinates
     */
    private boolean renderBlockRepeater(Block par1Block, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        int k = (i & 0xc) >> 2;
        renderStandardBlock(par1Block, par2, par3, par4);
        Tessellator tessellator = Tessellator.instance;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
            if(par1Block.lightValue[par1Block.blockID] > 0)
            {
                ff = (ff + 1.0F) * 0.5F;
            }
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        double d = -0.1875D;
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

        renderTorchAtAngle(par1Block, (double)par2 + d1, (double)par3 + d, (double)par4 + d2, 0.0D, 0.0D);
        renderTorchAtAngle(par1Block, (double)par2 + d3, (double)par3 + d, (double)par4 + d4, 0.0D, 0.0D);
        int l = par1Block.getBlockTextureFromSide(1);
        int i1 = (l & 0xf) << 4;
        int j1 = l & 0xf0;
        double d5 = (float)i1 / 256F;
        double d6 = ((float)i1 + 15.99F) / 256F;
        double d7 = (float)j1 / 256F;
        double d8 = ((float)j1 + 15.99F) / 256F;
        double d9 = 0.125D;
        double d10 = par2 + 1;
        double d11 = par2 + 1;
        double d12 = par2 + 0;
        double d13 = par2 + 0;
        double d14 = par4 + 0;
        double d15 = par4 + 1;
        double d16 = par4 + 1;
        double d17 = par4 + 0;
        double d18 = (double)par3 + d9;

        if (j == 2)
        {
            d10 = d11 = par2 + 0;
            d12 = d13 = par2 + 1;
            d14 = d17 = par4 + 1;
            d15 = d16 = par4 + 0;
        }
        else if (j == 3)
        {
            d10 = d13 = par2 + 0;
            d11 = d12 = par2 + 1;
            d14 = d15 = par4 + 0;
            d16 = d17 = par4 + 1;
        }
        else if (j == 1)
        {
            d10 = d13 = par2 + 1;
            d11 = d12 = par2 + 0;
            d14 = d15 = par4 + 1;
            d16 = d17 = par4 + 0;
        }

        tessellator.addVertexWithUV(d13, d18, d17, d5, d7);
        tessellator.addVertexWithUV(d12, d18, d16, d5, d8);
        tessellator.addVertexWithUV(d11, d18, d15, d6, d8);
        tessellator.addVertexWithUV(d10, d18, d14, d6, d7);
        return true;
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
    private boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = par5 || (i & 8) != 0;
        int j = BlockPistonBase.getOrientation(i);

        if (flag)
        {
            switch (j)
            {
                case 0:
                    uvRotateEast = 3;
                    uvRotateWest = 3;
                    uvRotateSouth = 3;
                    uvRotateNorth = 3;
                    par1Block.setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 1:
                    par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
                    break;

                case 2:
                    uvRotateSouth = 1;
                    uvRotateNorth = 2;
                    par1Block.setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
                    break;

                case 3:
                    uvRotateSouth = 2;
                    uvRotateNorth = 1;
                    uvRotateTop = 3;
                    uvRotateBottom = 3;
                    par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
                    break;

                case 4:
                    uvRotateEast = 1;
                    uvRotateWest = 2;
                    uvRotateTop = 2;
                    uvRotateBottom = 1;
                    par1Block.setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                    break;

                case 5:
                    uvRotateEast = 2;
                    uvRotateWest = 1;
                    uvRotateTop = 1;
                    uvRotateBottom = 2;
                    par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
                    break;
            }

            renderStandardBlock(par1Block, par2, par3, par4);
            uvRotateEast = 0;
            uvRotateWest = 0;
            uvRotateSouth = 0;
            uvRotateNorth = 0;
            uvRotateTop = 0;
            uvRotateBottom = 0;
            par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
    private void renderPistonRodUD(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14)
    {
        int i = 108;

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        Tessellator tessellator = Tessellator.instance;
        double d = (float)(j + 0) / 256F;
        double d1 = (float)(k + 0) / 256F;
        double d2 = (((double)j + par14) - 0.01D) / 256D;
        double d3 = ((double)((float)k + 4F) - 0.01D) / 256D;
        tessellator.setColorOpaque_F(par13, par13, par13);
        tessellator.addVertexWithUV(par1, par7, par9, d2, d1);
        tessellator.addVertexWithUV(par1, par5, par9, d, d1);
        tessellator.addVertexWithUV(par3, par5, par11, d, d3);
        tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
    }

    /**
     * Render piston rod south/north
     */
    private void renderPistonRodSN(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14)
    {
        int i = 108;

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        Tessellator tessellator = Tessellator.instance;
        double d = (float)(j + 0) / 256F;
        double d1 = (float)(k + 0) / 256F;
        double d2 = (((double)j + par14) - 0.01D) / 256D;
        double d3 = ((double)((float)k + 4F) - 0.01D) / 256D;
        tessellator.setColorOpaque_F(par13, par13, par13);
        tessellator.addVertexWithUV(par1, par5, par11, d2, d1);
        tessellator.addVertexWithUV(par1, par5, par9, d, d1);
        tessellator.addVertexWithUV(par3, par7, par9, d, d3);
        tessellator.addVertexWithUV(par3, par7, par11, d2, d3);
    }

    /**
     * Render piston rod east/west
     */
    private void renderPistonRodEW(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14)
    {
        int i = 108;

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        Tessellator tessellator = Tessellator.instance;
        double d = (float)(j + 0) / 256F;
        double d1 = (float)(k + 0) / 256F;
        double d2 = (((double)j + par14) - 0.01D) / 256D;
        double d3 = ((double)((float)k + 4F) - 0.01D) / 256D;
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
    private boolean renderPistonExtension(Block par1Block, int par2, int par3, int par4, boolean par5)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = BlockPistonExtension.getDirectionMeta(i);
        float f = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
        float f1 = par5 ? 1.0F : 0.5F;
        double d = par5 ? 16D : 8D;

        switch (j)
        {
            case 0:
                uvRotateEast = 3;
                uvRotateWest = 3;
                uvRotateSouth = 3;
                uvRotateNorth = 3;
                par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.25F, 1.0F);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.625F, (float)par3 + 0.25F, (float)par3 + 0.25F + f1, (float)par4 + 0.625F, (float)par4 + 0.625F, f * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.375F, (float)par3 + 0.25F, (float)par3 + 0.25F + f1, (float)par4 + 0.375F, (float)par4 + 0.375F, f * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.375F, (float)par3 + 0.25F, (float)par3 + 0.25F + f1, (float)par4 + 0.375F, (float)par4 + 0.625F, f * 0.6F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.625F, (float)par3 + 0.25F, (float)par3 + 0.25F + f1, (float)par4 + 0.625F, (float)par4 + 0.375F, f * 0.6F, d);
                break;

            case 1:
                par1Block.setBlockBounds(0.0F, 0.75F, 0.0F, 1.0F, 1.0F, 1.0F);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.625F, (((float)par3 - 0.25F) + 1.0F) - f1, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.625F, (float)par4 + 0.625F, f * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.375F, (((float)par3 - 0.25F) + 1.0F) - f1, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.375F, (float)par4 + 0.375F, f * 0.8F, d);
                renderPistonRodUD((float)par2 + 0.375F, (float)par2 + 0.375F, (((float)par3 - 0.25F) + 1.0F) - f1, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.375F, (float)par4 + 0.625F, f * 0.6F, d);
                renderPistonRodUD((float)par2 + 0.625F, (float)par2 + 0.625F, (((float)par3 - 0.25F) + 1.0F) - f1, ((float)par3 - 0.25F) + 1.0F, (float)par4 + 0.625F, (float)par4 + 0.375F, f * 0.6F, d);
                break;

            case 2:
                uvRotateSouth = 1;
                uvRotateNorth = 2;
                par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.25F);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.375F, (float)par4 + 0.25F, (float)par4 + 0.25F + f1, f * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.625F, (float)par4 + 0.25F, (float)par4 + 0.25F + f1, f * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.375F, (float)par4 + 0.25F, (float)par4 + 0.25F + f1, f * 0.5F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.625F, (float)par4 + 0.25F, (float)par4 + 0.25F + f1, f, d);
                break;

            case 3:
                uvRotateSouth = 2;
                uvRotateNorth = 1;
                uvRotateTop = 3;
                uvRotateBottom = 3;
                par1Block.setBlockBounds(0.0F, 0.0F, 0.75F, 1.0F, 1.0F, 1.0F);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.375F, (((float)par4 - 0.25F) + 1.0F) - f1, ((float)par4 - 0.25F) + 1.0F, f * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.625F, (((float)par4 - 0.25F) + 1.0F) - f1, ((float)par4 - 0.25F) + 1.0F, f * 0.6F, d);
                renderPistonRodSN((float)par2 + 0.375F, (float)par2 + 0.625F, (float)par3 + 0.375F, (float)par3 + 0.375F, (((float)par4 - 0.25F) + 1.0F) - f1, ((float)par4 - 0.25F) + 1.0F, f * 0.5F, d);
                renderPistonRodSN((float)par2 + 0.625F, (float)par2 + 0.375F, (float)par3 + 0.625F, (float)par3 + 0.625F, (((float)par4 - 0.25F) + 1.0F) - f1, ((float)par4 - 0.25F) + 1.0F, f, d);
                break;

            case 4:
                uvRotateEast = 1;
                uvRotateWest = 2;
                uvRotateTop = 2;
                uvRotateBottom = 1;
                par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 0.25F, 1.0F, 1.0F);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f1, (float)par3 + 0.375F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.375F, f * 0.5F, d);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f1, (float)par3 + 0.625F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.625F, f, d);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f1, (float)par3 + 0.375F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.375F, f * 0.6F, d);
                renderPistonRodEW((float)par2 + 0.25F, (float)par2 + 0.25F + f1, (float)par3 + 0.625F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.625F, f * 0.6F, d);
                break;

            case 5:
                uvRotateEast = 2;
                uvRotateWest = 1;
                uvRotateTop = 1;
                uvRotateBottom = 2;
                par1Block.setBlockBounds(0.75F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                renderStandardBlock(par1Block, par2, par3, par4);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f1, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.375F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.375F, f * 0.5F, d);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f1, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.625F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.625F, f, d);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f1, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.375F, (float)par3 + 0.625F, (float)par4 + 0.375F, (float)par4 + 0.375F, f * 0.6F, d);
                renderPistonRodEW((((float)par2 - 0.25F) + 1.0F) - f1, ((float)par2 - 0.25F) + 1.0F, (float)par3 + 0.625F, (float)par3 + 0.375F, (float)par4 + 0.625F, (float)par4 + 0.625F, f * 0.6F, d);
                break;
        }

        uvRotateEast = 0;
        uvRotateWest = 0;
        uvRotateSouth = 0;
        uvRotateNorth = 0;
        uvRotateTop = 0;
        uvRotateBottom = 0;
        par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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
        boolean flag1 = overrideBlockTexture >= 0;

        if (!flag1)
        {
            overrideBlockTexture = Block.cobblestone.blockIndexInTexture;
        }

        float f = 0.25F;
        float f1 = 0.1875F;
        float f2 = 0.1875F;

        if (j == 5)
        {
            par1Block.setBlockBounds(0.5F - f1, 0.0F, 0.5F - f, 0.5F + f1, f2, 0.5F + f);
        }
        else if (j == 6)
        {
            par1Block.setBlockBounds(0.5F - f, 0.0F, 0.5F - f1, 0.5F + f, f2, 0.5F + f1);
        }
        else if (j == 4)
        {
            par1Block.setBlockBounds(0.5F - f1, 0.5F - f, 1.0F - f2, 0.5F + f1, 0.5F + f, 1.0F);
        }
        else if (j == 3)
        {
            par1Block.setBlockBounds(0.5F - f1, 0.5F - f, 0.0F, 0.5F + f1, 0.5F + f, f2);
        }
        else if (j == 2)
        {
            par1Block.setBlockBounds(1.0F - f2, 0.5F - f, 0.5F - f1, 1.0F, 0.5F + f, 0.5F + f1);
        }
        else if (j == 1)
        {
            par1Block.setBlockBounds(0.0F, 0.5F - f, 0.5F - f1, f2, 0.5F + f, 0.5F + f1);
        }
        else if (j == 0)
        {
            par1Block.setBlockBounds(0.5F - f, 1.0F - f2, 0.5F - f1, 0.5F + f, 1.0F, 0.5F + f1);
        }
        else if (j == 7)
        {
            par1Block.setBlockBounds(0.5F - f1, 1.0F - f2, 0.5F - f, 0.5F + f1, 1.0F, 0.5F + f);
        }

        renderStandardBlock(par1Block, par2, par3, par4);

        if (!flag1)
        {
            overrideBlockTexture = -1;
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
        int k = par1Block.getBlockTextureFromSide(0);

        if (overrideBlockTexture >= 0)
        {
            k = overrideBlockTexture;
        }

        int l = (k & 0xf) << 4;
        int i1 = k & 0xf0;
        float f4 = (float)l / 256F;
        float f5 = ((float)l + 15.99F) / 256F;
        float f6 = (float)i1 / 256F;
        float f7 = ((float)i1 + 15.99F) / 256F;
        Vec3 avec3[] = new Vec3[8];
        float f8 = 0.0625F;
        float f9 = 0.0625F;
        float f10 = 0.625F;
        avec3[0] = Vec3.getVec3Pool().getVecFromPool(-f8, 0.0D, -f9);
        avec3[1] = Vec3.getVec3Pool().getVecFromPool(f8, 0.0D, -f9);
        avec3[2] = Vec3.getVec3Pool().getVecFromPool(f8, 0.0D, f9);
        avec3[3] = Vec3.getVec3Pool().getVecFromPool(-f8, 0.0D, f9);
        avec3[4] = Vec3.getVec3Pool().getVecFromPool(-f8, f10, -f9);
        avec3[5] = Vec3.getVec3Pool().getVecFromPool(f8, f10, -f9);
        avec3[6] = Vec3.getVec3Pool().getVecFromPool(f8, f10, f9);
        avec3[7] = Vec3.getVec3Pool().getVecFromPool(-f8, f10, f9);

        for (int j1 = 0; j1 < 8; j1++)
        {
            if (flag)
            {
                avec3[j1].zCoord -= 0.0625D;
                avec3[j1].rotateAroundX(((float)Math.PI * 2F / 9F));
            }
            else
            {
                avec3[j1].zCoord += 0.0625D;
                avec3[j1].rotateAroundX(-((float)Math.PI * 2F / 9F));
            }

            if (j == 0 || j == 7)
            {
                avec3[j1].rotateAroundZ((float)Math.PI);
            }

            if (j == 6 || j == 0)
            {
                avec3[j1].rotateAroundY(((float)Math.PI / 2F));
            }

            if (j > 0 && j < 5)
            {
                avec3[j1].yCoord -= 0.375D;
                avec3[j1].rotateAroundX(((float)Math.PI / 2F));

                if (j == 4)
                {
                    avec3[j1].rotateAroundY(0.0F);
                }

                if (j == 3)
                {
                    avec3[j1].rotateAroundY((float)Math.PI);
                }

                if (j == 2)
                {
                    avec3[j1].rotateAroundY(((float)Math.PI / 2F));
                }

                if (j == 1)
                {
                    avec3[j1].rotateAroundY(-((float)Math.PI / 2F));
                }

                avec3[j1].xCoord += (double)par2 + 0.5D;
                avec3[j1].yCoord += (float)par3 + 0.5F;
                avec3[j1].zCoord += (double)par4 + 0.5D;
                continue;
            }

            if (j == 0 || j == 7)
            {
                avec3[j1].xCoord += (double)par2 + 0.5D;
                avec3[j1].yCoord += (float)par3 + 0.875F;
                avec3[j1].zCoord += (double)par4 + 0.5D;
            }
            else
            {
                avec3[j1].xCoord += (double)par2 + 0.5D;
                avec3[j1].yCoord += (float)par3 + 0.125F;
                avec3[j1].zCoord += (double)par4 + 0.5D;
            }
        }

        Vec3 vec3 = null;
        Vec3 vec3_1 = null;
        Vec3 vec3_2 = null;
        Vec3 vec3_3 = null;

        for (int k1 = 0; k1 < 6; k1++)
        {
            if (k1 == 0)
            {
                f4 = (float)(l + 7) / 256F;
                f5 = ((float)(l + 9) - 0.01F) / 256F;
                f6 = (float)(i1 + 6) / 256F;
                f7 = ((float)(i1 + 8) - 0.01F) / 256F;
            }
            else if (k1 == 2)
            {
                f4 = (float)(l + 7) / 256F;
                f5 = ((float)(l + 9) - 0.01F) / 256F;
                f6 = (float)(i1 + 6) / 256F;
                f7 = ((float)(i1 + 16) - 0.01F) / 256F;
            }

            if (k1 == 0)
            {
                vec3 = avec3[0];
                vec3_1 = avec3[1];
                vec3_2 = avec3[2];
                vec3_3 = avec3[3];
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

            tessellator.addVertexWithUV(vec3.xCoord, vec3.yCoord, vec3.zCoord, f4, f7);
            tessellator.addVertexWithUV(vec3_1.xCoord, vec3_1.yCoord, vec3_1.zCoord, f5, f7);
            tessellator.addVertexWithUV(vec3_2.xCoord, vec3_2.yCoord, vec3_2.zCoord, f5, f6);
            tessellator.addVertexWithUV(vec3_3.xCoord, vec3_3.yCoord, vec3_3.zCoord, f4, f6);
        }

        return true;
    }

    public boolean func_78577_f(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = i & 3;
        boolean flag = (i & 4) == 4;
        boolean flag1 = (i & 8) == 8;
        boolean flag2 = !blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
        boolean flag3 = overrideBlockTexture >= 0;

        if (!flag3)
        {
            overrideBlockTexture = Block.planks.blockIndexInTexture;
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

        renderStandardBlock(par1Block, par2, par3, par4);

        if (!flag3)
        {
            overrideBlockTexture = -1;
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
        int k = par1Block.getBlockTextureFromSide(0);

        if (overrideBlockTexture >= 0)
        {
            k = overrideBlockTexture;
        }

        int l = (k & 0xf) << 4;
        int i1 = k & 0xf0;
        float f7 = (float)l / 256F;
        float f8 = ((float)l + 15.99F) / 256F;
        float f9 = (float)i1 / 256F;
        float f10 = ((float)i1 + 15.99F) / 256F;
        Vec3 avec3[] = new Vec3[8];
        float f11 = 0.046875F;
        float f12 = 0.046875F;
        float f13 = 0.3125F;
        avec3[0] = Vec3.getVec3Pool().getVecFromPool(-f11, 0.0D, -f12);
        avec3[1] = Vec3.getVec3Pool().getVecFromPool(f11, 0.0D, -f12);
        avec3[2] = Vec3.getVec3Pool().getVecFromPool(f11, 0.0D, f12);
        avec3[3] = Vec3.getVec3Pool().getVecFromPool(-f11, 0.0D, f12);
        avec3[4] = Vec3.getVec3Pool().getVecFromPool(-f11, f13, -f12);
        avec3[5] = Vec3.getVec3Pool().getVecFromPool(f11, f13, -f12);
        avec3[6] = Vec3.getVec3Pool().getVecFromPool(f11, f13, f12);
        avec3[7] = Vec3.getVec3Pool().getVecFromPool(-f11, f13, f12);

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

        Vec3 vec3 = null;
        Vec3 vec3_1 = null;
        Vec3 vec3_2 = null;
        Vec3 vec3_3 = null;
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
        avec3[0] = Vec3.getVec3Pool().getVecFromPool(-f14, 0.0D, -f15);
        avec3[1] = Vec3.getVec3Pool().getVecFromPool(f14, 0.0D, -f15);
        avec3[2] = Vec3.getVec3Pool().getVecFromPool(f14, 0.0D, f15);
        avec3[3] = Vec3.getVec3Pool().getVecFromPool(-f14, 0.0D, f15);
        avec3[4] = Vec3.getVec3Pool().getVecFromPool(-f14, f16, -f15);
        avec3[5] = Vec3.getVec3Pool().getVecFromPool(f14, f16, -f15);
        avec3[6] = Vec3.getVec3Pool().getVecFromPool(f14, f16, f15);
        avec3[7] = Vec3.getVec3Pool().getVecFromPool(-f14, f16, f15);

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
            int j2 = (Block.tripWire.blockIndexInTexture & 0xf) << 4;
            int k2 = Block.tripWire.blockIndexInTexture & 0xf0;
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

    public boolean func_78619_g(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSide(0);
        int j = blockAccess.getBlockMetadata(par2, par3, par4);
        boolean flag = (j & 4) == 4;
        boolean flag1 = (j & 2) == 2;

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = par1Block.getBlockBrightness(blockAccess, par2, par3, par4) * 0.75F;
        tessellator.setColorOpaque_F(f, f, f);
        int k = (i & 0xf) << 4;
        int l = i & 0xf0;
        double d = (float)k / 256F;
        double d1 = (float)(k + 16) / 256F;
        double d2 = (float)(l + (flag ? 2 : 0)) / 256F;
        double d3 = (float)(l + (flag ? 4 : 2)) / 256F;
        double d4 = (double)(flag1 ? 3.5F : 1.5F) / 16D;
        boolean flag2 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, j, 1);
        boolean flag3 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, j, 3);
        boolean flag4 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, j, 2);
        boolean flag5 = BlockTripWire.func_72148_a(blockAccess, par2, par3, par4, j, 0);
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

    /**
     * Renders a fire block at the given coordinates
     */
    public boolean renderBlockFire(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSide(0);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }else{
            float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(ff, ff, ff);
        }
        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        double d = (float)j / 256F;
        double d2 = ((float)j + 15.99F) / 256F;
        double d4 = (float)k / 256F;
        double d6 = ((float)k + 15.99F) / 256F;
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
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 1, d2, d4);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 1, d2, d6);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 0, d, d6);
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 0, d, d4);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 0, d2, d4);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 0, d2, d6);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 1, d, d6);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 1, d, d4);
            d = (float)j / 256F;
            d2 = ((float)j + 15.99F) / 256F;
            d4 = (float)(k + 16) / 256F;
            d6 = ((float)k + 15.99F + 16F) / 256F;
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d22, d2, d4);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d14, d2, d6);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d14, d, d6);
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d22, d, d4);
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d20, d2, d4);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d12, d2, d6);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d12, d, d6);
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d20, d, d4);
            d8 = ((double)par2 + 0.5D) - 0.5D;
            d9 = (double)par2 + 0.5D + 0.5D;
            d12 = ((double)par4 + 0.5D) - 0.5D;
            d14 = (double)par4 + 0.5D + 0.5D;
            d16 = ((double)par2 + 0.5D) - 0.40000000000000002D;
            d18 = (double)par2 + 0.5D + 0.40000000000000002D;
            d20 = ((double)par4 + 0.5D) - 0.40000000000000002D;
            d22 = (double)par4 + 0.5D + 0.40000000000000002D;
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 0, d, d4);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 0, d, d6);
            tessellator.addVertexWithUV(d8, par3 + 0, par4 + 1, d2, d6);
            tessellator.addVertexWithUV(d16, (float)par3 + f, par4 + 1, d2, d4);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 1, d, d4);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 1, d, d6);
            tessellator.addVertexWithUV(d9, par3 + 0, par4 + 0, d2, d6);
            tessellator.addVertexWithUV(d18, (float)par3 + f, par4 + 0, d2, d4);
            d = (float)j / 256F;
            d2 = ((float)j + 15.99F) / 256F;
            d4 = (float)k / 256F;
            d6 = ((float)k + 15.99F) / 256F;
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d22, d, d4);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d14, d, d6);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d14, d2, d6);
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d22, d2, d4);
            tessellator.addVertexWithUV(par2 + 1, (float)par3 + f, d20, d, d4);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, d12, d, d6);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, d12, d2, d6);
            tessellator.addVertexWithUV(par2 + 0, (float)par3 + f, d20, d2, d4);
        }
        else
        {
            float f2 = 0.2F;
            float f3 = 0.0625F;

            if ((par2 + par3 + par4 & 1) == 1)
            {
                d = (float)j / 256F;
                d2 = ((float)j + 15.99F) / 256F;
                d4 = (float)(k + 16) / 256F;
                d6 = ((float)k + 15.99F + 16F) / 256F;
            }

            if ((par2 / 2 + par3 / 2 + par4 / 2 & 1) == 1)
            {
                double d10 = d2;
                d2 = d;
                d = d10;
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2 - 1, par3, par4))
            {
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 1, d2, d4);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 1, d2, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 0, d, d4);
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 0, d, d4);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 1, d2, d6);
                tessellator.addVertexWithUV((float)par2 + f2, (float)par3 + f + f3, par4 + 1, d2, d4);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2 + 1, par3, par4))
            {
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 0, d, d4);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 1, d2, d6);
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 1, d2, d4);
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 1, d2, d4);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 1, d2, d6);
                tessellator.addVertexWithUV((par2 + 1) - 0, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV((float)(par2 + 1) - f2, (float)par3 + f + f3, par4 + 0, d, d4);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2, par3, par4 - 1))
            {
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)par4 + f2, d2, d4);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d2, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)par4 + f2, d, d4);
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)par4 + f2, d, d4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, par4 + 0, d, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, par4 + 0, d2, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)par4 + f2, d2, d4);
            }

            if (Block.fire.canBlockCatchFire(blockAccess, par2, par3, par4 + 1))
            {
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)(par4 + 1) - f2, d, d4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, (par4 + 1) - 0, d, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, (par4 + 1) - 0, d2, d6);
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)(par4 + 1) - f2, d2, d4);
                tessellator.addVertexWithUV(par2 + 0, (float)par3 + f + f3, (float)(par4 + 1) - f2, d2, d4);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 0) + f3, (par4 + 1) - 0, d2, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 0) + f3, (par4 + 1) - 0, d, d6);
                tessellator.addVertexWithUV(par2 + 1, (float)par3 + f + f3, (float)(par4 + 1) - f2, d, d4);
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
                double d1 = (float)j / 256F;
                double d3 = ((float)j + 15.99F) / 256F;
                double d5 = (float)k / 256F;
                double d7 = ((float)k + 15.99F) / 256F;
                par3++;
                float f1 = -0.2F;

                if ((par2 + par3 + par4 & 1) == 0)
                {
                    tessellator.addVertexWithUV(d19, (float)par3 + f1, par4 + 0, d3, d5);
                    tessellator.addVertexWithUV(d11, par3 + 0, par4 + 0, d3, d7);
                    tessellator.addVertexWithUV(d11, par3 + 0, par4 + 1, d1, d7);
                    tessellator.addVertexWithUV(d19, (float)par3 + f1, par4 + 1, d1, d5);
                    d1 = (float)j / 256F;
                    d3 = ((float)j + 15.99F) / 256F;
                    d5 = (float)(k + 16) / 256F;
                    d7 = ((float)k + 15.99F + 16F) / 256F;
                    tessellator.addVertexWithUV(d21, (float)par3 + f1, par4 + 1, d3, d5);
                    tessellator.addVertexWithUV(d13, par3 + 0, par4 + 1, d3, d7);
                    tessellator.addVertexWithUV(d13, par3 + 0, par4 + 0, d1, d7);
                    tessellator.addVertexWithUV(d21, (float)par3 + f1, par4 + 0, d1, d5);
                }
                else
                {
                    tessellator.addVertexWithUV(par2 + 0, (float)par3 + f1, d24, d3, d5);
                    tessellator.addVertexWithUV(par2 + 0, par3 + 0, d17, d3, d7);
                    tessellator.addVertexWithUV(par2 + 1, par3 + 0, d17, d1, d7);
                    tessellator.addVertexWithUV(par2 + 1, (float)par3 + f1, d24, d1, d5);
                    d1 = (float)j / 256F;
                    d3 = ((float)j + 15.99F) / 256F;
                    d5 = (float)(k + 16) / 256F;
                    d7 = ((float)k + 15.99F + 16F) / 256F;
                    tessellator.addVertexWithUV(par2 + 1, (float)par3 + f1, d23, d3, d5);
                    tessellator.addVertexWithUV(par2 + 1, par3 + 0, d15, d3, d7);
                    tessellator.addVertexWithUV(par2 + 0, par3 + 0, d15, d1, d7);
                    tessellator.addVertexWithUV(par2 + 0, (float)par3 + f1, d23, d1, d5);
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
        int j = par1Block.getBlockTextureFromSideAndMetadata(1, i);

        if (overrideBlockTexture >= 0)
        {
            j = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = 1.0F;
        if (Minecraft.oldlighting){
            f = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
        }
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
        int k = (j & 0xf) << 4;
        int l = j & 0xf0;
        double d = (float)k / 256F;
        double d2 = ((float)k + 15.99F) / 256F;
        double d4 = (float)l / 256F;
        double d6 = ((float)l + 15.99F) / 256F;
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

        if (byte0 != 0)
        {
            d = (float)(k + 16) / 256F;
            d2 = ((float)(k + 16) + 15.99F) / 256F;
            d4 = (float)l / 256F;
            d6 = ((float)l + 15.99F) / 256F;
        }

        if (byte0 == 0)
        {
            if (!flag)
            {
                f5 += 0.3125F;
            }

            if (!flag)
            {
                d += 0.01953125D;
            }

            if (!flag1)
            {
                f6 -= 0.3125F;
            }

            if (!flag1)
            {
                d2 -= 0.01953125D;
            }

            if (!flag2)
            {
                f7 += 0.3125F;
            }

            if (!flag2)
            {
                d4 += 0.01953125D;
            }

            if (!flag3)
            {
                f8 -= 0.3125F;
            }

            if (!flag3)
            {
                d6 -= 0.01953125D;
            }

            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6 + 0.0625D);
        }
        else if (byte0 == 1)
        {
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d2, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d, d6 + 0.0625D);
        }
        else if (byte0 == 2)
        {
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d, d6);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d2, d4);
            tessellator.setColorOpaque_F(f, f, f);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f8, d2, d6 + 0.0625D);
            tessellator.addVertexWithUV(f6, (double)par3 + 0.015625D, f7, d, d6 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f7, d, d4 + 0.0625D);
            tessellator.addVertexWithUV(f5, (double)par3 + 0.015625D, f8, d2, d4 + 0.0625D);
        }

        if (!blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            double d1 = (float)(k + 16) / 256F;
            double d3 = ((float)(k + 16) + 15.99F) / 256F;
            double d5 = (float)l / 256F;
            double d7 = ((float)l + 15.99F) / 256F;

            if (blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d5);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, d1, d5);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, d1, d7);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d7);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 1, d1, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, par3 + 0, par4 + 0, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)par2 + 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d7 + 0.0625D);
            }

            if (blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, d1, d7);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d7);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d5);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, d1, d5);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 1, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 1, d3, d7 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, (float)(par3 + 1) + 0.021875F, par4 + 0, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV((double)(par2 + 1) - 0.015625D, par3 + 0, par4 + 0, d1, d5 + 0.0625D);
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, d1, d7);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d7);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d5);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, d1, d5);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + 0.015625D, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)par4 + 0.015625D, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + 0.015625D, d1, d5 + 0.0625D);
            }

            if (blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID)
            {
                tessellator.setColorOpaque_F(f * f2, f * f3, f * f4);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d5);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d5);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d7);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d7);
                tessellator.setColorOpaque_F(f, f, f);
                tessellator.addVertexWithUV(par2 + 1, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d5 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - 0.015625D, d1, d7 + 0.0625D);
                tessellator.addVertexWithUV(par2 + 0, (float)(par3 + 1) + 0.021875F, (double)(par4 + 1) - 0.015625D, d3, d7 + 0.0625D);
            }
        }

        return true;
    }

    /**
     * Renders a minecart track block at the given coordinates
     */
    public boolean renderBlockMinecartTrack(BlockRail par1BlockRail, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
        int j = par1BlockRail.getBlockTextureFromSideAndMetadata(0, i);

        if (overrideBlockTexture >= 0)
        {
            j = overrideBlockTexture;
        }

        if (par1BlockRail.isPowered())
        {
            i &= 7;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1BlockRail.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        }else{
            float f = par1BlockRail.getBlockBrightness(blockAccess, par2, par3, par4);
            tessellator.setColorOpaque_F(f, f, f);
        }
        int k = (j & 0xf) << 4;
        int l = j & 0xf0;
        double d = (float)k / 256F;
        double d1 = ((float)k + 15.99F) / 256F;
        double d2 = (float)l / 256F;
        double d3 = ((float)l + 15.99F) / 256F;
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

        tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
        tessellator.addVertexWithUV(d6, d14, d10, d1, d3);
        tessellator.addVertexWithUV(d7, d15, d11, d, d3);
        tessellator.addVertexWithUV(d8, d16, d12, d, d2);
        tessellator.addVertexWithUV(d8, d16, d12, d, d2);
        tessellator.addVertexWithUV(d7, d15, d11, d, d3);
        tessellator.addVertexWithUV(d6, d14, d10, d1, d3);
        tessellator.addVertexWithUV(d5, d13, d9, d1, d2);
        return true;
    }

    /**
     * Renders a ladder block at the given coordinates
     */
    public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSide(0);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        tessellator.setColorOpaque_F(f, f, f);
        f = (i & 0xf) << 4;
        int j = i & 0xf0;
        double d = (float)f / 256F;
        double d1 = ((float)f + 15.99F) / 256F;
        double d2 = (float)j / 256F;
        double d3 = ((float)j + 15.99F) / 256F;
        int k = blockAccess.getBlockMetadata(par2, par3, par4);
        double d4 = 0.0D;
        double d5 = 0.05000000074505806D;

        if (k == 5)
        {
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 1) + d4, (double)(par4 + 1) + d4, d, d2);
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 0) - d4, (double)(par4 + 1) + d4, d, d3);
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 0) - d4, (double)(par4 + 0) - d4, d1, d3);
            tessellator.addVertexWithUV((double)par2 + d5, (double)(par3 + 1) + d4, (double)(par4 + 0) - d4, d1, d2);
        }

        if (k == 4)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 0) - d4, (double)(par4 + 1) + d4, d1, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 1) + d4, (double)(par4 + 1) + d4, d1, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 1) + d4, (double)(par4 + 0) - d4, d, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) - d5, (double)(par3 + 0) - d4, (double)(par4 + 0) - d4, d, d3);
        }

        if (k == 3)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 0) - d4, (double)par4 + d5, d1, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 1) + d4, (double)par4 + d5, d1, d2);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 1) + d4, (double)par4 + d5, d, d2);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 0) - d4, (double)par4 + d5, d, d3);
        }

        if (k == 2)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 1) + d4, (double)(par4 + 1) - d5, d, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) + d4, (double)(par3 + 0) - d4, (double)(par4 + 1) - d5, d, d3);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 0) - d4, (double)(par4 + 1) - d5, d1, d3);
            tessellator.addVertexWithUV((double)(par2 + 0) - d4, (double)(par3 + 1) + d4, (double)(par4 + 1) - d5, d1, d2);
        }

        return true;
    }

    /**
     * Render block vine
     */
    public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSide(0);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        float f = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        int j = par1Block.colorMultiplier(blockAccess, par2, par3, par4);
        float f1 = (float)(j >> 16 & 0xff) / 255F;
        float d = (float)(j >> 8 & 0xff) / 255F;
        float f2 = (float)(j & 0xff) / 255F;
        tessellator.setColorOpaque_F(f * f1, f * d, f * f2);
        j = (i & 0xf) << 4;
        f1 = i & 0xf0;
        d = (float)j / 256F;
        double d1 = ((float)j + 15.99F) / 256F;
        double d2 = (float)f1 / 256F;
        double d3 = ((float)f1 + 15.99F) / 256F;
        double d4 = 0.05000000074505806D;
        int k = blockAccess.getBlockMetadata(par2, par3, par4);

        if ((k & 2) != 0)
        {
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 1, d, d2);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 1, d, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 0, d1, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 0, d1, d2);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 0, d1, d2);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 0, d1, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 0, par4 + 1, d, d3);
            tessellator.addVertexWithUV((double)par2 + d4, par3 + 1, par4 + 1, d, d2);
        }

        if ((k & 8) != 0)
        {
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 1, d1, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 1, d1, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 0, d, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 0, d, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 0, d, d3);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 0, d, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 1, par4 + 1, d1, d2);
            tessellator.addVertexWithUV((double)(par2 + 1) - d4, par3 + 0, par4 + 1, d1, d3);
        }

        if ((k & 4) != 0)
        {
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + d4, d1, d3);
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)par4 + d4, d1, d2);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)par4 + d4, d, d2);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + d4, d, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)par4 + d4, d, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)par4 + d4, d, d2);
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)par4 + d4, d1, d2);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)par4 + d4, d1, d3);
        }

        if ((k & 1) != 0)
        {
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)(par4 + 1) - d4, d, d2);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - d4, d, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - d4, d1, d3);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)(par4 + 1) - d4, d1, d2);
            tessellator.addVertexWithUV(par2 + 0, par3 + 1, (double)(par4 + 1) - d4, d1, d2);
            tessellator.addVertexWithUV(par2 + 0, par3 + 0, (double)(par4 + 1) - d4, d1, d3);
            tessellator.addVertexWithUV(par2 + 1, par3 + 0, (double)(par4 + 1) - d4, d, d3);
            tessellator.addVertexWithUV(par2 + 1, par3 + 1, (double)(par4 + 1) - d4, d, d2);
        }

        if (blockAccess.isBlockNormalCube(par2, par3 + 1, par4))
        {
            tessellator.addVertexWithUV(par2 + 1, (double)(par3 + 1) - d4, par4 + 0, d, d2);
            tessellator.addVertexWithUV(par2 + 1, (double)(par3 + 1) - d4, par4 + 1, d, d3);
            tessellator.addVertexWithUV(par2 + 0, (double)(par3 + 1) - d4, par4 + 1, d1, d3);
            tessellator.addVertexWithUV(par2 + 0, (double)(par3 + 1) - d4, par4 + 0, d1, d2);
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
        int k = 0;
        int l = 0;

        if (overrideBlockTexture >= 0)
        {
            k = overrideBlockTexture;
            l = overrideBlockTexture;
        }
        else
        {
            int i1 = blockAccess.getBlockMetadata(par2, par3, par4);
            k = par1BlockPane.getBlockTextureFromSideAndMetadata(0, i1);
            l = par1BlockPane.getSideTextureIndex();
        }

        int j1 = (k & 0xf) << 4;
        int k1 = k & 0xf0;
        double d = (float)j1 / 256F;
        double d1 = ((float)j1 + 7.99F) / 256F;
        double d2 = ((float)j1 + 15.99F) / 256F;
        double d3 = (float)k1 / 256F;
        double d4 = ((float)k1 + 15.99F) / 256F;
        int l1 = (l & 0xf) << 4;
        int i2 = l & 0xf0;
        double d5 = (float)(l1 + 7) / 256F;
        double d6 = ((float)l1 + 8.99F) / 256F;
        double d7 = (float)i2 / 256F;
        double d8 = (float)(i2 + 8) / 256F;
        double d9 = ((float)i2 + 15.99F) / 256F;
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
                tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d13, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d13, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d13, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d13, d5, d9);
            }
            else
            {
                if (par3 < i - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
                }

                if (par3 < i - 1 && blockAccess.isAirBlock(par2, par3 + 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d8);
                }
            }

            if (flag5)
            {
                tessellator.addVertexWithUV(d17, par3, d15, d6, d9);
                tessellator.addVertexWithUV(d17, par3, d13, d6, d7);
                tessellator.addVertexWithUV(d16, par3, d13, d5, d7);
                tessellator.addVertexWithUV(d16, par3, d15, d5, d9);
                tessellator.addVertexWithUV(d17, par3, d13, d6, d9);
                tessellator.addVertexWithUV(d17, par3, d15, d6, d7);
                tessellator.addVertexWithUV(d16, par3, d15, d5, d7);
                tessellator.addVertexWithUV(d16, par3, d13, d5, d9);
            }
            else
            {
                if (par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
                {
                    tessellator.addVertexWithUV(d16, par3, d13, d6, d7);
                    tessellator.addVertexWithUV(d16, par3, d14, d6, d8);
                    tessellator.addVertexWithUV(d17, par3, d14, d5, d8);
                    tessellator.addVertexWithUV(d17, par3, d13, d5, d7);
                    tessellator.addVertexWithUV(d16, par3, d14, d6, d7);
                    tessellator.addVertexWithUV(d16, par3, d13, d6, d8);
                    tessellator.addVertexWithUV(d17, par3, d13, d5, d8);
                    tessellator.addVertexWithUV(d17, par3, d14, d5, d7);
                }

                if (par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
                {
                    tessellator.addVertexWithUV(d16, par3, d14, d5, d8);
                    tessellator.addVertexWithUV(d16, par3, d15, d5, d9);
                    tessellator.addVertexWithUV(d17, par3, d15, d6, d9);
                    tessellator.addVertexWithUV(d17, par3, d14, d6, d8);
                    tessellator.addVertexWithUV(d16, par3, d15, d5, d8);
                    tessellator.addVertexWithUV(d16, par3, d14, d5, d9);
                    tessellator.addVertexWithUV(d17, par3, d14, d6, d9);
                    tessellator.addVertexWithUV(d17, par3, d15, d6, d8);
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
                tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d8);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d8);
                tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d6, d7);
                tessellator.addVertexWithUV(d16, par3 + 1, d13, d6, d8);
                tessellator.addVertexWithUV(d17, par3 + 1, d13, d5, d8);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d5, d7);
            }

            if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 - 1))
            {
                tessellator.addVertexWithUV(d16, par3, d13, d6, d7);
                tessellator.addVertexWithUV(d16, par3, d14, d6, d8);
                tessellator.addVertexWithUV(d17, par3, d14, d5, d8);
                tessellator.addVertexWithUV(d17, par3, d13, d5, d7);
                tessellator.addVertexWithUV(d16, par3, d14, d6, d7);
                tessellator.addVertexWithUV(d16, par3, d13, d6, d8);
                tessellator.addVertexWithUV(d17, par3, d13, d5, d8);
                tessellator.addVertexWithUV(d17, par3, d14, d5, d7);
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
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d8);
                tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d8);
                tessellator.addVertexWithUV(d16, par3 + 1, d15, d5, d8);
                tessellator.addVertexWithUV(d16, par3 + 1, d14, d5, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d14, d6, d9);
                tessellator.addVertexWithUV(d17, par3 + 1, d15, d6, d8);
            }

            if (flag5 || par3 > 1 && blockAccess.isAirBlock(par2, par3 - 1, par4 + 1))
            {
                tessellator.addVertexWithUV(d16, par3, d14, d5, d8);
                tessellator.addVertexWithUV(d16, par3, d15, d5, d9);
                tessellator.addVertexWithUV(d17, par3, d15, d6, d9);
                tessellator.addVertexWithUV(d17, par3, d14, d6, d8);
                tessellator.addVertexWithUV(d16, par3, d15, d5, d8);
                tessellator.addVertexWithUV(d16, par3, d14, d5, d9);
                tessellator.addVertexWithUV(d17, par3, d14, d6, d9);
                tessellator.addVertexWithUV(d17, par3, d15, d6, d8);
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
            long l = (long)(par2 * 0x2fc20f) ^ (long)par4 * 0x6ebfff5L ^ (long)par3;
            l = l * l * 0x285b825L + l * 11L;
            d += ((double)((float)(l >> 16 & 15L) / 15F) - 0.5D) * 0.5D;
            d1 += ((double)((float)(l >> 20 & 15L) / 15F) - 1.0D) * 0.20000000000000001D;
            d2 += ((double)((float)(l >> 24 & 15L) / 15F) - 0.5D) * 0.5D;
        }

        drawCrossedSquares(par1Block, blockAccess.getBlockMetadata(par2, par3, par4), d, d1, d2);
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
            renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(par2, par3, par4), blockstem.maxY, par2, (float)par3 - 0.0625F, par4);
        }
        else
        {
            renderBlockStemSmall(blockstem, blockAccess.getBlockMetadata(par2, par3, par4), 0.5D, par2, (float)par3 - 0.0625F, par4);
            renderBlockStemBig(blockstem, blockAccess.getBlockMetadata(par2, par3, par4), j, blockstem.maxY, par2, (float)par3 - 0.0625F, par4);
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
    public void renderTorchAtAngle(Block par1Block, double par2, double par4, double par6, double par8, double par10)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSide(0);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        float f = (float)j / 256F;
        float f1 = ((float)j + 15.99F) / 256F;
        float f2 = (float)k / 256F;
        float f3 = ((float)k + 15.99F) / 256F;
        double d = (double)f + 0.02734375D;
        double d1 = (double)f2 + 0.0234375D;
        double d2 = (double)f + 0.03515625D;
        double d3 = (double)f2 + 0.03125D;
        par2 += 0.5D;
        par6 += 0.5D;
        double d4 = par2 - 0.5D;
        double d5 = par2 + 0.5D;
        double d6 = par6 - 0.5D;
        double d7 = par6 + 0.5D;
        double d8 = 0.0625D;
        double d9 = 0.625D;
        tessellator.addVertexWithUV((par2 + par8 * (1.0D - d9)) - d8, par4 + d9, (par6 + par10 * (1.0D - d9)) - d8, d, d1);
        tessellator.addVertexWithUV((par2 + par8 * (1.0D - d9)) - d8, par4 + d9, par6 + par10 * (1.0D - d9) + d8, d, d3);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d9) + d8, par4 + d9, par6 + par10 * (1.0D - d9) + d8, d2, d3);
        tessellator.addVertexWithUV(par2 + par8 * (1.0D - d9) + d8, par4 + d9, (par6 + par10 * (1.0D - d9)) - d8, d2, d1);
        tessellator.addVertexWithUV(par2 - d8, par4 + 1.0D, d6, f, f2);
        tessellator.addVertexWithUV((par2 - d8) + par8, par4 + 0.0D, d6 + par10, f, f3);
        tessellator.addVertexWithUV((par2 - d8) + par8, par4 + 0.0D, d7 + par10, f1, f3);
        tessellator.addVertexWithUV(par2 - d8, par4 + 1.0D, d7, f1, f2);
        tessellator.addVertexWithUV(par2 + d8, par4 + 1.0D, d7, f, f2);
        tessellator.addVertexWithUV(par2 + par8 + d8, par4 + 0.0D, d7 + par10, f, f3);
        tessellator.addVertexWithUV(par2 + par8 + d8, par4 + 0.0D, d6 + par10, f1, f3);
        tessellator.addVertexWithUV(par2 + d8, par4 + 1.0D, d6, f1, f2);
        tessellator.addVertexWithUV(d4, par4 + 1.0D, par6 + d8, f, f2);
        tessellator.addVertexWithUV(d4 + par8, par4 + 0.0D, par6 + d8 + par10, f, f3);
        tessellator.addVertexWithUV(d5 + par8, par4 + 0.0D, par6 + d8 + par10, f1, f3);
        tessellator.addVertexWithUV(d5, par4 + 1.0D, par6 + d8, f1, f2);
        tessellator.addVertexWithUV(d5, par4 + 1.0D, par6 - d8, f, f2);
        tessellator.addVertexWithUV(d5 + par8, par4 + 0.0D, (par6 - d8) + par10, f, f3);
        tessellator.addVertexWithUV(d4 + par8, par4 + 0.0D, (par6 - d8) + par10, f1, f3);
        tessellator.addVertexWithUV(d4, par4 + 1.0D, par6 - d8, f1, f2);
    }

    /**
     * Utility function to draw crossed swuares
     */
    public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        double d = (float)j / 256F;
        double d1 = ((float)j + 15.99F) / 256F;
        double d2 = (float)k / 256F;
        double d3 = ((float)k + 15.99F) / 256F;
        double d4 = (par3 + 0.5D) - 0.45000000000000001D;
        double d5 = par3 + 0.5D + 0.45000000000000001D;
        double d6 = (par7 + 0.5D) - 0.45000000000000001D;
        double d7 = par7 + 0.5D + 0.45000000000000001D;
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d2);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d1, d2);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d2);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d1, d2);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d2);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d1, d2);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d2);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d1, d2);
    }

    /**
     * Render block stem small
     */
    public void renderBlockStemSmall(Block par1Block, int par2, double par3, double par5, double par7, double par9)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        double d = (float)j / 256F;
        double d1 = ((float)j + 15.99F) / 256F;
        double d2 = (float)k / 256F;
        double d3 = ((double)k + 15.989999771118164D * par3) / 256D;
        double d4 = (par5 + 0.5D) - 0.44999998807907104D;
        double d5 = par5 + 0.5D + 0.44999998807907104D;
        double d6 = (par9 + 0.5D) - 0.44999998807907104D;
        double d7 = par9 + 0.5D + 0.44999998807907104D;
        tessellator.addVertexWithUV(d4, par7 + par3, d6, d, d2);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d5, par7 + par3, d7, d1, d2);
        tessellator.addVertexWithUV(d5, par7 + par3, d7, d, d2);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d4, par7 + par3, d6, d1, d2);
        tessellator.addVertexWithUV(d4, par7 + par3, d7, d, d2);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d5, par7 + par3, d6, d1, d2);
        tessellator.addVertexWithUV(d5, par7 + par3, d6, d, d2);
        tessellator.addVertexWithUV(d5, par7 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par7 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d4, par7 + par3, d7, d1, d2);
    }

    /**
     * Render BlockLilyPad
     */
    public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.blockIndexInTexture;

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        float f = 0.015625F;
        double d = (float)j / 256F;
        double d1 = ((float)j + 15.99F) / 256F;
        double d2 = (float)k / 256F;
        double d3 = ((float)k + 15.99F) / 256F;
        long l = (long)(par2 * 0x2fc20f) ^ (long)par4 * 0x6ebfff5L ^ (long)par3;
        l = l * l * 0x285b825L + l * 11L;
        int i1 = (int)(l >> 16 & 3L);
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
        }
        float f1 = (float)par2 + 0.5F;
        float f2 = (float)par4 + 0.5F;
        float f3 = (float)(i1 & 1) * 0.5F * (float)(1 - ((i1 / 2) % 2) * 2);
        float f4 = (float)(i1 + 1 & 1) * 0.5F * (float)(1 - (((i1 + 1) / 2) % 2) * 2);
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
        tessellator.addVertexWithUV((f1 + f3) - f4, (float)par3 + f, f2 + f3 + f4, d, d2);
        tessellator.addVertexWithUV(f1 + f3 + f4, (float)par3 + f, (f2 - f3) + f4, d1, d2);
        tessellator.addVertexWithUV((f1 - f3) + f4, (float)par3 + f, f2 - f3 - f4, d1, d3);
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
        tessellator.addVertexWithUV((f1 - f3) + f4, (float)par3 + f, f2 - f3 - f4, d1, d3);
        tessellator.addVertexWithUV(f1 + f3 + f4, (float)par3 + f, (f2 - f3) + f4, d1, d2);
        tessellator.addVertexWithUV((f1 + f3) - f4, (float)par3 + f, f2 + f3 + f4, d, d2);
        return true;
    }

    /**
     * Render block stem big
     */
    public void renderBlockStemBig(Block par1Block, int par2, int par3, double par4, double par6, double par8, double par10)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2) + 16;

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        double d = (float)j / 256F;
        double d1 = ((float)j + 15.99F) / 256F;
        double d2 = (float)k / 256F;
        double d3 = ((double)k + 15.989999771118164D * par4) / 256D;
        double d4 = (par6 + 0.5D) - 0.5D;
        double d5 = par6 + 0.5D + 0.5D;
        double d6 = (par10 + 0.5D) - 0.5D;
        double d7 = par10 + 0.5D + 0.5D;
        double d8 = par6 + 0.5D;
        double d9 = par10 + 0.5D;

        if (((par3 + 1) / 2) % 2 == 1)
        {
            double d10 = d1;
            d1 = d;
            d = d10;
        }

        if (par3 < 2)
        {
            tessellator.addVertexWithUV(d4, par8 + par4, d9, d, d2);
            tessellator.addVertexWithUV(d4, par8 + 0.0D, d9, d, d3);
            tessellator.addVertexWithUV(d5, par8 + 0.0D, d9, d1, d3);
            tessellator.addVertexWithUV(d5, par8 + par4, d9, d1, d2);
            tessellator.addVertexWithUV(d5, par8 + par4, d9, d1, d2);
            tessellator.addVertexWithUV(d5, par8 + 0.0D, d9, d1, d3);
            tessellator.addVertexWithUV(d4, par8 + 0.0D, d9, d, d3);
            tessellator.addVertexWithUV(d4, par8 + par4, d9, d, d2);
        }
        else
        {
            tessellator.addVertexWithUV(d8, par8 + par4, d7, d, d2);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d7, d, d3);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d6, d1, d3);
            tessellator.addVertexWithUV(d8, par8 + par4, d6, d1, d2);
            tessellator.addVertexWithUV(d8, par8 + par4, d6, d1, d2);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d6, d1, d3);
            tessellator.addVertexWithUV(d8, par8 + 0.0D, d7, d, d3);
            tessellator.addVertexWithUV(d8, par8 + par4, d7, d, d2);
        }
    }

    /**
     * Render block crops implementation
     */
    public void renderBlockCropsImpl(Block par1Block, int par2, double par3, double par5, double par7)
    {
        Tessellator tessellator = Tessellator.instance;
        int i = par1Block.getBlockTextureFromSideAndMetadata(0, par2);

        if (overrideBlockTexture >= 0)
        {
            i = overrideBlockTexture;
        }

        int j = (i & 0xf) << 4;
        int k = i & 0xf0;
        double d = (float)j / 256F;
        double d1 = ((float)j + 15.99F) / 256F;
        double d2 = (float)k / 256F;
        double d3 = ((float)k + 15.99F) / 256F;
        double d4 = (par3 + 0.5D) - 0.25D;
        double d5 = par3 + 0.5D + 0.25D;
        double d6 = (par7 + 0.5D) - 0.5D;
        double d7 = par7 + 0.5D + 0.5D;
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d2);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d1, d2);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d2);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d1, d2);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d2);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d1, d2);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d2);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d1, d2);
        d4 = (par3 + 0.5D) - 0.5D;
        d5 = par3 + 0.5D + 0.5D;
        d6 = (par7 + 0.5D) - 0.25D;
        d7 = par7 + 0.5D + 0.25D;
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d, d2);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d1, d2);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d6, d, d2);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d6, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d6, d1, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d6, d1, d2);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d, d2);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d1, d2);
        tessellator.addVertexWithUV(d4, par5 + 1.0D, d7, d, d2);
        tessellator.addVertexWithUV(d4, par5 + 0.0D, d7, d, d3);
        tessellator.addVertexWithUV(d5, par5 + 0.0D, d7, d1, d3);
        tessellator.addVertexWithUV(d5, par5 + 1.0D, d7, d1, d2);
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
            int k = par1Block.getBlockTextureFromSideAndMetadata(1, j);
            float f8 = (float)BlockFluid.getFlowDirection(blockAccess, par2, par3, par4, material);

            if (f8 > -999F)
            {
                k = par1Block.getBlockTextureFromSideAndMetadata(2, j);
            }

            d2 -= d6;
            d3 -= d6;
            d4 -= d6;
            d5 -= d6;
            int j1 = (k & 0xf) << 4;
            int l1 = k & 0xf0;
            double d7 = ((double)j1 + 8D) / 256D;
            double d8 = ((double)l1 + 8D) / 256D;

            if (f8 < -999F)
            {
                f8 = 0.0F;
            }
            else
            {
                d7 = (float)(j1 + 16) / 256F;
                d8 = (float)(l1 + 16) / 256F;
            }

            double d10 = (double)(MathHelper.sin(f8) * 8F) / 256D;
            double d12 = (double)(MathHelper.cos(f8) * 8F) / 256D;
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4));
            }
            float f9 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;
            tessellator.setColorOpaque_F(f4 * f9 * f, f4 * f9 * f1, f4 * f9 * f2);
            tessellator.addVertexWithUV(par2 + 0, (double)par3 + d2, par4 + 0, d7 - d12 - d10, (d8 - d12) + d10);
            tessellator.addVertexWithUV(par2 + 0, (double)par3 + d3, par4 + 1, (d7 - d12) + d10, d8 + d12 + d10);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d4, par4 + 1, d7 + d12 + d10, (d8 + d12) - d10);
            tessellator.addVertexWithUV(par2 + 1, (double)par3 + d5, par4 + 0, (d7 + d12) - d10, d8 - d12 - d10);
        }

        if (renderAllFaces || flag1)
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4));
            }
            float f7 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4) : 1.0F;
            tessellator.setColorOpaque_F(f3 * f7, f3 * f7, f3 * f7);
            renderBottomFace(par1Block, par2, (double)par3 + d6, par4, par1Block.getBlockTextureFromSide(0));
            flag2 = true;
        }

        for (int l = 0; l < 4; l++)
        {
            int i1 = par2;
            int k1 = par3;
            int i2 = par4;

            if (l == 0)
            {
                i2--;
            }

            if (l == 1)
            {
                i2++;
            }

            if (l == 2)
            {
                i1--;
            }

            if (l == 3)
            {
                i1++;
            }

            int j2 = par1Block.getBlockTextureFromSideAndMetadata(l + 2, j);
            int k2 = (j2 & 0xf) << 4;
            int l2 = j2 & 0xf0;

            if (!renderAllFaces && !aflag[l])
            {
                continue;
            }

            double d9;
            double d11;
            double d13;
            double d14;
            double d15;
            double d16;

            if (l == 0)
            {
                d9 = d2;
                d11 = d5;
                d13 = par2;
                d15 = par2 + 1;
                d14 = (double)par4 + d6;
                d16 = (double)par4 + d6;
            }
            else if (l == 1)
            {
                d9 = d4;
                d11 = d3;
                d13 = par2 + 1;
                d15 = par2;
                d14 = (double)(par4 + 1) - d6;
                d16 = (double)(par4 + 1) - d6;
            }
            else if (l == 2)
            {
                d9 = d3;
                d11 = d2;
                d13 = (double)par2 + d6;
                d15 = (double)par2 + d6;
                d14 = par4 + 1;
                d16 = par4;
            }
            else
            {
                d9 = d5;
                d11 = d4;
                d13 = (double)(par2 + 1) - d6;
                d15 = (double)(par2 + 1) - d6;
                d14 = par4;
                d16 = par4 + 1;
            }

            flag2 = true;
            double d17 = (float)(k2 + 0) / 256F;
            double d18 = ((double)(k2 + 16) - 0.01D) / 256D;
            double d19 = ((double)l2 + (1.0D - d9) * 16D) / 256D;
            double d20 = ((double)l2 + (1.0D - d11) * 16D) / 256D;
            double d21 = ((double)(l2 + 16) - 0.01D) / 256D;
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.getMixedBrightnessForBlock(blockAccess, i1, k1, i2));
            }
            float f10 = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4) : 1.0F;

            if (l < 2)
            {
                f10 *= f5;
            }
            else
            {
                f10 *= f6;
            }

            tessellator.setColorOpaque_F(f4 * f10 * f, f4 * f10 * f1, f4 * f10 * f2);
            tessellator.addVertexWithUV(d13, (double)par3 + d9, d14, d17, d19);
            tessellator.addVertexWithUV(d15, (double)par3 + d11, d16, d18, d20);
            tessellator.addVertexWithUV(d15, par3 + 0, d16, d18, d21);
            tessellator.addVertexWithUV(d13, par3 + 0, d14, d17, d21);
        }

        par1Block.minY = d;
        par1Block.maxY = d1;
        return flag2;
    }

    /**
     * Get fluid height
     */
    private float getFluidHeight(int par1, int par2, int par3, Material par4Material)
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

    public void func_78588_a(Block par1Block, World par2World, int par3, int par4, int par5, int par6)
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
        renderBottomFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSideAndMetadata(0, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4 + 1, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        renderTopFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSideAndMetadata(1, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4, par5 - 1) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderEastFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSideAndMetadata(2, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3, par4, par5 + 1) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        renderWestFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSideAndMetadata(3, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3 - 1, par4, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderNorthFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSideAndMetadata(4, par6));
        f5 = Minecraft.oldlighting ? par1Block.getBlockBrightness(par2World, par3 + 1, par4, par5) : 1.0F;

        if (f5 < f4)
        {
            f5 = f4;
        }

        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        renderSouthFace(par1Block, -0.5D, -0.5D, -0.5D, par1Block.getBlockTextureFromSideAndMetadata(5, par6));
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
            if (Minecraft.oldlighting){
                return renderStandardBlockWithAmbientOcclusion_old(par1Block, par2, par3, par4, f, f1, f2);
            }
            return renderStandardBlockWithAmbientOcclusion(par1Block, par2, par3, par4, f, f1, f2);
        }
        else
        {
            return renderStandardBlockWithColorMultiplier(par1Block, par2, par3, par4, f, f1, f2);
        }
    }

    public boolean func_78581_r(Block par1Block, int par2, int par3, int par4)
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

    public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        enableAO = true;
        boolean flag = false;
        float f = lightValueOwn;
        float f7 = lightValueOwn;
        float f14 = lightValueOwn;
        float f21 = lightValueOwn;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        boolean flag5 = true;
        boolean flag6 = true;
        lightValueOwn = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4);
        aoLightValueXNeg = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4);
        aoLightValueYNeg = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4);
        aoLightValueZNeg = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 - 1);
        aoLightValueXPos = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4);
        aoLightValueYPos = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4);
        aoLightValueZPos = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 + 1);
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        int j = i;
        int k = i;
        int l = i;
        int i1 = i;
        int j1 = i;
        int k1 = i;

        if (par1Block.minY <= 0.0D)
        {
            k = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
        }

        if (par1Block.maxY >= 1.0D)
        {
            j1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
        }

        if (par1Block.minX <= 0.0D)
        {
            j = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
        }

        if (par1Block.maxX >= 1.0D)
        {
            i1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
        }

        if (par1Block.minZ <= 0.0D)
        {
            l = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
        }

        if (par1Block.maxZ >= 1.0D)
        {
            k1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(0xf000f);
        aoGrassXYZPPC = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
        aoGrassXYZPNC = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
        aoGrassXYZPCP = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
        aoGrassXYZPCN = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
        aoGrassXYZNPC = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
        aoGrassXYZNNC = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
        aoGrassXYZNCN = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
        aoGrassXYZNCP = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
        aoGrassXYZCPP = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
        aoGrassXYZCPN = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
        aoGrassXYZCNP = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
        aoGrassXYZCNN = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

        if (par1Block.blockIndexInTexture == 3)
        {
            flag1 = flag3 = flag4 = flag5 = flag6 = false;
        }

        if (overrideBlockTexture >= 0)
        {
            flag1 = flag3 = flag4 = flag5 = flag6 = false;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            float f1;
            float f8;
            float f15;
            float f22;

            if (aoType > 0)
            {
                if (par1Block.minY <= 0.0D)
                {
                    par3--;
                }

                aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
                aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4);

                if (aoGrassXYZCNN || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4 - 1);
                    aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
                    aoBrightnessXYZNNN = aoBrightnessXYNN;
                }

                if (aoGrassXYZCNP || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4 + 1);
                    aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
                    aoBrightnessXYZNNP = aoBrightnessXYNN;
                }

                if (aoGrassXYZCNN || aoGrassXYZPNC)
                {
                    aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4 - 1);
                    aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
                    aoBrightnessXYZPNN = aoBrightnessXYPN;
                }

                if (aoGrassXYZCNP || aoGrassXYZPNC)
                {
                    aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4 + 1);
                    aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
                    aoBrightnessXYZPNP = aoBrightnessXYPN;
                }

                if (par1Block.minY <= 0.0D)
                {
                    par3++;
                }

                f1 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + aoLightValueYNeg) / 4F;
                f22 = (aoLightValueScratchYZNP + aoLightValueYNeg + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4F;
                f15 = (aoLightValueYNeg + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4F;
                f8 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + aoLightValueYNeg + aoLightValueScratchYZNN) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, k);
                brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, k);
                brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, k);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, k);
            }
            else
            {
                f1 = f8 = f15 = f22 = aoLightValueYNeg;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = aoBrightnessXYNN;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag1 ? par5 : 1.0F) * 0.5F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag1 ? par6 : 1.0F) * 0.5F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag1 ? par7 : 1.0F) * 0.5F;
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
            renderBottomFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            float f2;
            float f9;
            float f16;
            float f23;

            if (aoType > 0)
            {
                if (par1Block.maxY >= 1.0D)
                {
                    par3++;
                }

                aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
                aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4);
                aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 + 1);

                if (aoGrassXYZCPN || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4 - 1);
                    aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
                    aoBrightnessXYZNPN = aoBrightnessXYNP;
                }

                if (aoGrassXYZCPN || aoGrassXYZPPC)
                {
                    aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4 - 1);
                    aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
                    aoBrightnessXYZPPN = aoBrightnessXYPP;
                }

                if (aoGrassXYZCPP || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4 + 1);
                    aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
                    aoBrightnessXYZNPP = aoBrightnessXYNP;
                }

                if (aoGrassXYZCPP || aoGrassXYZPPC)
                {
                    aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4 + 1);
                    aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
                    aoBrightnessXYZPPP = aoBrightnessXYPP;
                }

                if (par1Block.maxY >= 1.0D)
                {
                    par3--;
                }

                f23 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + aoLightValueYPos) / 4F;
                f2 = (aoLightValueScratchYZPP + aoLightValueYPos + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4F;
                f9 = (aoLightValueYPos + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4F;
                f16 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + aoLightValueYPos + aoLightValueScratchYZPN) / 4F;
                brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, j1);
                brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, j1);
                brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, j1);
                brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, j1);
            }
            else
            {
                f2 = f9 = f16 = f23 = aoLightValueYPos;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = j1;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = flag2 ? par5 : 1.0F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = flag2 ? par6 : 1.0F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = flag2 ? par7 : 1.0F;
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
            renderTopFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            float f3;
            float f10;
            float f17;
            float f24;

            if (aoType > 0)
            {
                if (par1Block.minZ <= 0.0D)
                {
                    par4--;
                }

                aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4);
                aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4);
                aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);

                if (aoGrassXYZNCN || aoGrassXYZCNN)
                {
                    aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3 - 1, par4);
                    aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNNN = aoBrightnessXZNN;
                }

                if (aoGrassXYZNCN || aoGrassXYZCPN)
                {
                    aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3 + 1, par4);
                    aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNPN = aoBrightnessXZNN;
                }

                if (aoGrassXYZPCN || aoGrassXYZCNN)
                {
                    aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3 - 1, par4);
                    aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPNN = aoBrightnessXZPN;
                }

                if (aoGrassXYZPCN || aoGrassXYZCPN)
                {
                    aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3 + 1, par4);
                    aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPPN = aoBrightnessXZPN;
                }

                if (par1Block.minZ <= 0.0D)
                {
                    par4++;
                }

                f3 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + aoLightValueZNeg + aoLightValueScratchYZPN) / 4F;
                f10 = (aoLightValueZNeg + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4F;
                f17 = (aoLightValueScratchYZNN + aoLightValueZNeg + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4F;
                f24 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + aoLightValueZNeg) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, l);
                brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, l);
                brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, l);
                brightnessTopRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, l);
            }
            else
            {
                f3 = f10 = f17 = f24 = aoLightValueZNeg;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = l;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag3 ? par5 : 1.0F) * 0.8F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag3 ? par6 : 1.0F) * 0.8F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag3 ? par7 : 1.0F) * 0.8F;
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
            int l1 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 2);
            renderEastFace(par1Block, par2, par3, par4, l1);

            if (fancyGrass && l1 == 3 && overrideBlockTexture < 0)
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
                renderEastFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            float f4;
            float f11;
            float f18;
            float f25;

            if (aoType > 0)
            {
                if (par1Block.maxZ >= 1.0D)
                {
                    par4++;
                }

                aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3, par4);
                aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
                aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);

                if (aoGrassXYZNCP || aoGrassXYZCNP)
                {
                    aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3 - 1, par4);
                    aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNNP = aoBrightnessXZNP;
                }

                if (aoGrassXYZNCP || aoGrassXYZCPP)
                {
                    aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 - 1, par3 + 1, par4);
                    aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNPP = aoBrightnessXZNP;
                }

                if (aoGrassXYZPCP || aoGrassXYZCNP)
                {
                    aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3 - 1, par4);
                    aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPNP = aoBrightnessXZPP;
                }

                if (aoGrassXYZPCP || aoGrassXYZCPP)
                {
                    aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2 + 1, par3 + 1, par4);
                    aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPPP = aoBrightnessXZPP;
                }

                if (par1Block.maxZ >= 1.0D)
                {
                    par4--;
                }

                f4 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + aoLightValueZPos + aoLightValueScratchYZPP) / 4F;
                f25 = (aoLightValueZPos + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4F;
                f18 = (aoLightValueScratchYZNP + aoLightValueZPos + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4F;
                f11 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + aoLightValueZPos) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, k1);
                brightnessTopRight = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, k1);
                brightnessBottomRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, k1);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, k1);
            }
            else
            {
                f4 = f11 = f18 = f25 = aoLightValueZPos;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = k1;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag4 ? par5 : 1.0F) * 0.8F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag4 ? par6 : 1.0F) * 0.8F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag4 ? par7 : 1.0F) * 0.8F;
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
            int i2 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3);
            renderWestFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3));

            if (fancyGrass && i2 == 3 && overrideBlockTexture < 0)
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
                renderWestFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            float f5;
            float f12;
            float f19;
            float f26;

            if (aoType > 0)
            {
                if (par1Block.minX <= 0.0D)
                {
                    par2--;
                }

                aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);

                if (aoGrassXYZNCN || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4 - 1);
                    aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNNN = aoBrightnessXZNN;
                }

                if (aoGrassXYZNCP || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4 + 1);
                    aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNNP = aoBrightnessXZNP;
                }

                if (aoGrassXYZNCN || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4 - 1);
                    aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNPN = aoBrightnessXZNN;
                }

                if (aoGrassXYZNCP || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4 + 1);
                    aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNPP = aoBrightnessXZNP;
                }

                if (par1Block.minX <= 0.0D)
                {
                    par2++;
                }

                f26 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + aoLightValueXNeg + aoLightValueScratchXZNP) / 4F;
                f5 = (aoLightValueXNeg + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4F;
                f12 = (aoLightValueScratchXZNN + aoLightValueXNeg + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4F;
                f19 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + aoLightValueXNeg) / 4F;
                brightnessTopRight = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, j);
                brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, j);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, j);
                brightnessBottomRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, j);
            }
            else
            {
                f5 = f12 = f19 = f26 = aoLightValueXNeg;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = j;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag5 ? par5 : 1.0F) * 0.6F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag5 ? par6 : 1.0F) * 0.6F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag5 ? par7 : 1.0F) * 0.6F;
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
            int j2 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 4);
            renderNorthFace(par1Block, par2, par3, par4, j2);

            if (fancyGrass && j2 == 3 && overrideBlockTexture < 0)
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
                renderNorthFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            float f6;
            float f13;
            float f20;
            float f27;

            if (aoType > 0)
            {
                if (par1Block.maxX >= 1.0D)
                {
                    par2++;
                }

                aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);

                if (aoGrassXYZPNC || aoGrassXYZPCN)
                {
                    aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4 - 1);
                    aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPNN = aoBrightnessXZPN;
                }

                if (aoGrassXYZPNC || aoGrassXYZPCP)
                {
                    aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 - 1, par4 + 1);
                    aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPNP = aoBrightnessXZPP;
                }

                if (aoGrassXYZPPC || aoGrassXYZPCN)
                {
                    aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4 - 1);
                    aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPPN = aoBrightnessXZPN;
                }

                if (aoGrassXYZPPC || aoGrassXYZPCP)
                {
                    aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(blockAccess, par2, par3 + 1, par4 + 1);
                    aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPPP = aoBrightnessXZPP;
                }

                if (par1Block.maxX >= 1.0D)
                {
                    par2--;
                }

                f6 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + aoLightValueXPos + aoLightValueScratchXZPP) / 4F;
                f27 = (aoLightValueXPos + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4F;
                f20 = (aoLightValueScratchXZPN + aoLightValueXPos + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4F;
                f13 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + aoLightValueXPos) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
                brightnessTopRight = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, i1);
                brightnessBottomRight = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, i1);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, i1);
            }
            else
            {
                f6 = f13 = f20 = f27 = aoLightValueXPos;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = i1;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag6 ? par5 : 1.0F) * 0.6F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag6 ? par6 : 1.0F) * 0.6F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag6 ? par7 : 1.0F) * 0.6F;
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
            int k2 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 5);
            renderSouthFace(par1Block, par2, par3, par4, k2);

            if (fancyGrass && k2 == 3 && overrideBlockTexture < 0)
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
                renderSouthFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        enableAO = false;
        return flag;
    }

    public boolean renderStandardBlockWithAmbientOcclusion_old(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        enableAO = true;
        boolean flag = false;
        float f = lightValueOwn;
        float f7 = lightValueOwn;
        float f14 = lightValueOwn;
        float f21 = lightValueOwn;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        boolean flag5 = true;
        boolean flag6 = true;
        lightValueOwn = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
        aoLightValueXNeg = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
        aoLightValueYNeg = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
        aoLightValueZNeg = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
        aoLightValueXPos = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
        aoLightValueYPos = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
        aoLightValueZPos = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        int j = i;
        int k = i;
        int l = i;
        int i1 = i;
        int j1 = i;
        int k1 = i;

        if (par1Block.minY <= 0.0D)
        {
            k = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
        }

        if (par1Block.maxY >= 1.0D)
        {
            j1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
        }

        if (par1Block.minX <= 0.0D)
        {
            j = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
        }

        if (par1Block.maxX >= 1.0D)
        {
            i1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
        }

        if (par1Block.minZ <= 0.0D)
        {
            l = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
        }

        if (par1Block.maxZ >= 1.0D)
        {
            k1 = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(0xf000f);
        aoGrassXYZPPC = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
        aoGrassXYZPNC = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
        aoGrassXYZPCP = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
        aoGrassXYZPCN = Block.canBlockGrass[blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
        aoGrassXYZNPC = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
        aoGrassXYZNNC = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
        aoGrassXYZNCN = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
        aoGrassXYZNCP = Block.canBlockGrass[blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
        aoGrassXYZCPP = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
        aoGrassXYZCPN = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
        aoGrassXYZCNP = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
        aoGrassXYZCNN = Block.canBlockGrass[blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

        if (par1Block.blockIndexInTexture == 3)
        {
            flag1 = flag3 = flag4 = flag5 = flag6 = false;
        }

        if (overrideBlockTexture >= 0)
        {
            flag1 = flag3 = flag4 = flag5 = flag6 = false;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            float f1;
            float f8;
            float f15;
            float f22;

            if (aoType > 0)
            {
                if (par1Block.minY <= 0.0D)
                {
                    par3--;
                }

                aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
                aoLightValueScratchXYNN = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchYZNN = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchYZNP = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYPN = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);

                if (aoGrassXYZCNN || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNN = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4 - 1);
                    aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNNN = aoLightValueScratchXYNN;
                    aoBrightnessXYZNNN = aoBrightnessXYNN;
                }

                if (aoGrassXYZCNP || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNP = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4 + 1);
                    aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNNP = aoLightValueScratchXYNN;
                    aoBrightnessXYZNNP = aoBrightnessXYNN;
                }

                if (aoGrassXYZCNN || aoGrassXYZPNC)
                {
                    aoLightValueScratchXYZPNN = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4 - 1);
                    aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPNN = aoLightValueScratchXYPN;
                    aoBrightnessXYZPNN = aoBrightnessXYPN;
                }

                if (aoGrassXYZCNP || aoGrassXYZPNC)
                {
                    aoLightValueScratchXYZPNP = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4 + 1);
                    aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPNP = aoLightValueScratchXYPN;
                    aoBrightnessXYZPNP = aoBrightnessXYPN;
                }

                if (par1Block.minY <= 0.0D)
                {
                    par3++;
                }

                f1 = (aoLightValueScratchXYZNNP + aoLightValueScratchXYNN + aoLightValueScratchYZNP + aoLightValueYNeg) / 4F;
                f22 = (aoLightValueScratchYZNP + aoLightValueYNeg + aoLightValueScratchXYZPNP + aoLightValueScratchXYPN) / 4F;
                f15 = (aoLightValueYNeg + aoLightValueScratchYZNN + aoLightValueScratchXYPN + aoLightValueScratchXYZPNN) / 4F;
                f8 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNN + aoLightValueYNeg + aoLightValueScratchYZNN) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXYNN, aoBrightnessYZNP, k);
                brightnessTopRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXYPN, k);
                brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYPN, aoBrightnessXYZPNN, k);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNN, aoBrightnessYZNN, k);
            }
            else
            {
                f1 = f8 = f15 = f22 = aoLightValueYNeg;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = aoBrightnessXYNN;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag1 ? par5 : 1.0F) * 0.5F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag1 ? par6 : 1.0F) * 0.5F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag1 ? par7 : 1.0F) * 0.5F;
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
            renderBottomFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            float f2;
            float f9;
            float f16;
            float f23;

            if (aoType > 0)
            {
                if (par1Block.maxY >= 1.0D)
                {
                    par3++;
                }

                aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
                aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYNP = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchXYPP = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                aoLightValueScratchYZPN = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchYZPP = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);

                if (aoGrassXYZCPN || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPN = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4 - 1);
                    aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNPN = aoLightValueScratchXYNP;
                    aoBrightnessXYZNPN = aoBrightnessXYNP;
                }

                if (aoGrassXYZCPN || aoGrassXYZPPC)
                {
                    aoLightValueScratchXYZPPN = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4 - 1);
                    aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPPN = aoLightValueScratchXYPP;
                    aoBrightnessXYZPPN = aoBrightnessXYPP;
                }

                if (aoGrassXYZCPP || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPP = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4 + 1);
                    aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNPP = aoLightValueScratchXYNP;
                    aoBrightnessXYZNPP = aoBrightnessXYNP;
                }

                if (aoGrassXYZCPP || aoGrassXYZPPC)
                {
                    aoLightValueScratchXYZPPP = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4 + 1);
                    aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPPP = aoLightValueScratchXYPP;
                    aoBrightnessXYZPPP = aoBrightnessXYPP;
                }

                if (par1Block.maxY >= 1.0D)
                {
                    par3--;
                }

                f23 = (aoLightValueScratchXYZNPP + aoLightValueScratchXYNP + aoLightValueScratchYZPP + aoLightValueYPos) / 4F;
                f2 = (aoLightValueScratchYZPP + aoLightValueYPos + aoLightValueScratchXYZPPP + aoLightValueScratchXYPP) / 4F;
                f9 = (aoLightValueYPos + aoLightValueScratchYZPN + aoLightValueScratchXYPP + aoLightValueScratchXYZPPN) / 4F;
                f16 = (aoLightValueScratchXYNP + aoLightValueScratchXYZNPN + aoLightValueYPos + aoLightValueScratchYZPN) / 4F;
                brightnessTopRight = getAoBrightness(aoBrightnessXYZNPP, aoBrightnessXYNP, aoBrightnessYZPP, j1);
                brightnessTopLeft = getAoBrightness(aoBrightnessYZPP, aoBrightnessXYZPPP, aoBrightnessXYPP, j1);
                brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXYPP, aoBrightnessXYZPPN, j1);
                brightnessBottomRight = getAoBrightness(aoBrightnessXYNP, aoBrightnessXYZNPN, aoBrightnessYZPN, j1);
            }
            else
            {
                f2 = f9 = f16 = f23 = aoLightValueYPos;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = j1;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = flag2 ? par5 : 1.0F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = flag2 ? par6 : 1.0F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = flag2 ? par7 : 1.0F;
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
            renderTopFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            float f3;
            float f10;
            float f17;
            float f24;

            if (aoType > 0)
            {
                if (par1Block.minZ <= 0.0D)
                {
                    par4--;
                }

                aoLightValueScratchXZNN = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchYZNN = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchYZPN = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                aoLightValueScratchXZPN = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);

                if (aoGrassXYZNCN || aoGrassXYZCNN)
                {
                    aoLightValueScratchXYZNNN = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3 - 1, par4);
                    aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNNN = aoBrightnessXZNN;
                }

                if (aoGrassXYZNCN || aoGrassXYZCPN)
                {
                    aoLightValueScratchXYZNPN = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3 + 1, par4);
                    aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNPN = aoBrightnessXZNN;
                }

                if (aoGrassXYZPCN || aoGrassXYZCNN)
                {
                    aoLightValueScratchXYZPNN = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3 - 1, par4);
                    aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPNN = aoBrightnessXZPN;
                }

                if (aoGrassXYZPCN || aoGrassXYZCPN)
                {
                    aoLightValueScratchXYZPPN = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3 + 1, par4);
                    aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPPN = aoBrightnessXZPN;
                }

                if (par1Block.minZ <= 0.0D)
                {
                    par4++;
                }

                f3 = (aoLightValueScratchXZNN + aoLightValueScratchXYZNPN + aoLightValueZNeg + aoLightValueScratchYZPN) / 4F;
                f10 = (aoLightValueZNeg + aoLightValueScratchYZPN + aoLightValueScratchXZPN + aoLightValueScratchXYZPPN) / 4F;
                f17 = (aoLightValueScratchYZNN + aoLightValueZNeg + aoLightValueScratchXYZPNN + aoLightValueScratchXZPN) / 4F;
                f24 = (aoLightValueScratchXYZNNN + aoLightValueScratchXZNN + aoLightValueScratchYZNN + aoLightValueZNeg) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessYZPN, l);
                brightnessBottomLeft = getAoBrightness(aoBrightnessYZPN, aoBrightnessXZPN, aoBrightnessXYZPPN, l);
                brightnessBottomRight = getAoBrightness(aoBrightnessYZNN, aoBrightnessXYZPNN, aoBrightnessXZPN, l);
                brightnessTopRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXZNN, aoBrightnessYZNN, l);
            }
            else
            {
                f3 = f10 = f17 = f24 = aoLightValueZNeg;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = l;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag3 ? par5 : 1.0F) * 0.8F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag3 ? par6 : 1.0F) * 0.8F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag3 ? par7 : 1.0F) * 0.8F;
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
            int l1 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 2);
            renderEastFace(par1Block, par2, par3, par4, l1);

            if (fancyGrass && l1 == 3 && overrideBlockTexture < 0)
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
                renderEastFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            float f4;
            float f11;
            float f18;
            float f25;

            if (aoType > 0)
            {
                if (par1Block.maxZ >= 1.0D)
                {
                    par4++;
                }

                aoLightValueScratchXZNP = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                aoLightValueScratchXZPP = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                aoLightValueScratchYZNP = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchYZPP = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4);
                aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4);
                aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);

                if (aoGrassXYZNCP || aoGrassXYZCNP)
                {
                    aoLightValueScratchXYZNNP = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3 - 1, par4);
                    aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNNP = aoBrightnessXZNP;
                }

                if (aoGrassXYZNCP || aoGrassXYZCPP)
                {
                    aoLightValueScratchXYZNPP = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3 + 1, par4);
                    aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNPP = aoBrightnessXZNP;
                }

                if (aoGrassXYZPCP || aoGrassXYZCNP)
                {
                    aoLightValueScratchXYZPNP = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3 - 1, par4);
                    aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 - 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPNP = aoBrightnessXZPP;
                }

                if (aoGrassXYZPCP || aoGrassXYZCPP)
                {
                    aoLightValueScratchXYZPPP = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3 + 1, par4);
                    aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3 + 1, par4);
                }
                else
                {
                    aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPPP = aoBrightnessXZPP;
                }

                if (par1Block.maxZ >= 1.0D)
                {
                    par4--;
                }

                f4 = (aoLightValueScratchXZNP + aoLightValueScratchXYZNPP + aoLightValueZPos + aoLightValueScratchYZPP) / 4F;
                f25 = (aoLightValueZPos + aoLightValueScratchYZPP + aoLightValueScratchXZPP + aoLightValueScratchXYZPPP) / 4F;
                f18 = (aoLightValueScratchYZNP + aoLightValueZPos + aoLightValueScratchXYZPNP + aoLightValueScratchXZPP) / 4F;
                f11 = (aoLightValueScratchXYZNNP + aoLightValueScratchXZNP + aoLightValueScratchYZNP + aoLightValueZPos) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYZNPP, aoBrightnessYZPP, k1);
                brightnessTopRight = getAoBrightness(aoBrightnessYZPP, aoBrightnessXZPP, aoBrightnessXYZPPP, k1);
                brightnessBottomRight = getAoBrightness(aoBrightnessYZNP, aoBrightnessXYZPNP, aoBrightnessXZPP, k1);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXYZNNP, aoBrightnessXZNP, aoBrightnessYZNP, k1);
            }
            else
            {
                f4 = f11 = f18 = f25 = aoLightValueZPos;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = k1;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag4 ? par5 : 1.0F) * 0.8F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag4 ? par6 : 1.0F) * 0.8F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag4 ? par7 : 1.0F) * 0.8F;
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
            int i2 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3);
            renderWestFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3));

            if (fancyGrass && i2 == 3 && overrideBlockTexture < 0)
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
                renderWestFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            float f5;
            float f12;
            float f19;
            float f26;

            if (aoType > 0)
            {
                if (par1Block.minX <= 0.0D)
                {
                    par2--;
                }

                aoLightValueScratchXYNN = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchXZNN = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchXZNP = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYNP = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);

                if (aoGrassXYZNCN || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNN = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4 - 1);
                    aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNNN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNNN = aoBrightnessXZNN;
                }

                if (aoGrassXYZNCP || aoGrassXYZNNC)
                {
                    aoLightValueScratchXYZNNP = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4 + 1);
                    aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNNP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNNP = aoBrightnessXZNP;
                }

                if (aoGrassXYZNCN || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPN = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4 - 1);
                    aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZNPN = aoLightValueScratchXZNN;
                    aoBrightnessXYZNPN = aoBrightnessXZNN;
                }

                if (aoGrassXYZNCP || aoGrassXYZNPC)
                {
                    aoLightValueScratchXYZNPP = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4 + 1);
                    aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZNPP = aoLightValueScratchXZNP;
                    aoBrightnessXYZNPP = aoBrightnessXZNP;
                }

                if (par1Block.minX <= 0.0D)
                {
                    par2++;
                }

                f26 = (aoLightValueScratchXYNN + aoLightValueScratchXYZNNP + aoLightValueXNeg + aoLightValueScratchXZNP) / 4F;
                f5 = (aoLightValueXNeg + aoLightValueScratchXZNP + aoLightValueScratchXYNP + aoLightValueScratchXYZNPP) / 4F;
                f12 = (aoLightValueScratchXZNN + aoLightValueXNeg + aoLightValueScratchXYZNPN + aoLightValueScratchXYNP) / 4F;
                f19 = (aoLightValueScratchXYZNNN + aoLightValueScratchXYNN + aoLightValueScratchXZNN + aoLightValueXNeg) / 4F;
                brightnessTopRight = getAoBrightness(aoBrightnessXYNN, aoBrightnessXYZNNP, aoBrightnessXZNP, j);
                brightnessTopLeft = getAoBrightness(aoBrightnessXZNP, aoBrightnessXYNP, aoBrightnessXYZNPP, j);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXZNN, aoBrightnessXYZNPN, aoBrightnessXYNP, j);
                brightnessBottomRight = getAoBrightness(aoBrightnessXYZNNN, aoBrightnessXYNN, aoBrightnessXZNN, j);
            }
            else
            {
                f5 = f12 = f19 = f26 = aoLightValueXNeg;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = j;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag5 ? par5 : 1.0F) * 0.6F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag5 ? par6 : 1.0F) * 0.6F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag5 ? par7 : 1.0F) * 0.6F;
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
            int j2 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 4);
            renderNorthFace(par1Block, par2, par3, par4, j2);

            if (fancyGrass && j2 == 3 && overrideBlockTexture < 0)
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
                renderNorthFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            float f6;
            float f13;
            float f20;
            float f27;

            if (aoType > 0)
            {
                if (par1Block.maxX >= 1.0D)
                {
                    par2++;
                }

                aoLightValueScratchXYPN = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                aoLightValueScratchXZPN = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                aoLightValueScratchXZPP = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                aoLightValueScratchXYPP = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4);
                aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1);
                aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1);
                aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4);

                if (aoGrassXYZPNC || aoGrassXYZPCN)
                {
                    aoLightValueScratchXYZPNN = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4 - 1);
                    aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPNN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPNN = aoBrightnessXZPN;
                }

                if (aoGrassXYZPNC || aoGrassXYZPCP)
                {
                    aoLightValueScratchXYZPNP = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4 + 1);
                    aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPNP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPNP = aoBrightnessXZPP;
                }

                if (aoGrassXYZPPC || aoGrassXYZPCN)
                {
                    aoLightValueScratchXYZPPN = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4 - 1);
                    aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 - 1);
                }
                else
                {
                    aoLightValueScratchXYZPPN = aoLightValueScratchXZPN;
                    aoBrightnessXYZPPN = aoBrightnessXZPN;
                }

                if (aoGrassXYZPPC || aoGrassXYZPCP)
                {
                    aoLightValueScratchXYZPPP = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4 + 1);
                    aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4 + 1);
                }
                else
                {
                    aoLightValueScratchXYZPPP = aoLightValueScratchXZPP;
                    aoBrightnessXYZPPP = aoBrightnessXZPP;
                }

                if (par1Block.maxX >= 1.0D)
                {
                    par2--;
                }

                f6 = (aoLightValueScratchXYPN + aoLightValueScratchXYZPNP + aoLightValueXPos + aoLightValueScratchXZPP) / 4F;
                f27 = (aoLightValueXPos + aoLightValueScratchXZPP + aoLightValueScratchXYPP + aoLightValueScratchXYZPPP) / 4F;
                f20 = (aoLightValueScratchXZPN + aoLightValueXPos + aoLightValueScratchXYZPPN + aoLightValueScratchXYPP) / 4F;
                f13 = (aoLightValueScratchXYZPNN + aoLightValueScratchXYPN + aoLightValueScratchXZPN + aoLightValueXPos) / 4F;
                brightnessTopLeft = getAoBrightness(aoBrightnessXYPN, aoBrightnessXYZPNP, aoBrightnessXZPP, i1);
                brightnessTopRight = getAoBrightness(aoBrightnessXZPP, aoBrightnessXYPP, aoBrightnessXYZPPP, i1);
                brightnessBottomRight = getAoBrightness(aoBrightnessXZPN, aoBrightnessXYZPPN, aoBrightnessXYPP, i1);
                brightnessBottomLeft = getAoBrightness(aoBrightnessXYZPNN, aoBrightnessXYPN, aoBrightnessXZPN, i1);
            }
            else
            {
                f6 = f13 = f20 = f27 = aoLightValueXPos;
                brightnessTopLeft = brightnessBottomLeft = brightnessBottomRight = brightnessTopRight = i1;
            }

            colorRedTopLeft = colorRedBottomLeft = colorRedBottomRight = colorRedTopRight = (flag6 ? par5 : 1.0F) * 0.6F;
            colorGreenTopLeft = colorGreenBottomLeft = colorGreenBottomRight = colorGreenTopRight = (flag6 ? par6 : 1.0F) * 0.6F;
            colorBlueTopLeft = colorBlueBottomLeft = colorBlueBottomRight = colorBlueTopRight = (flag6 ? par7 : 1.0F) * 0.6F;
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
            int k2 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 5);
            renderSouthFace(par1Block, par2, par3, par4, k2);

            if (fancyGrass && k2 == 3 && overrideBlockTexture < 0)
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
                renderSouthFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        enableAO = false;
        return flag;
    }

    /**
     * Get ambient occlusion brightness
     */
    private int getAoBrightness(int par1, int par2, int par3, int par4)
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
                tessellator.setBrightness(par1Block.minY <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4) : i);
                tessellator.setColorOpaque_F(f7, f10, f13);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                tessellator.setColorOpaque_F(ff * f7, ff * f10, ff * f13);
            }
            renderBottomFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.maxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4) : i);
                tessellator.setColorOpaque_F(f4, f5, f6);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                tessellator.setColorOpaque_F(ff * f4, ff * f5, ff * f6);
            }
            renderTopFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : i);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                tessellator.setColorOpaque_F(ff * f8, ff * f11, ff * f14);
            }
            int j = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 2);
            renderEastFace(par1Block, par2, par3, par4, j);

            if (fancyGrass && j == 3 && overrideBlockTexture < 0)
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1) : 1.0F;
                tessellator.setColorOpaque_F(f8 * par5 * ff, f11 * par6 * ff, f14 * par7 * ff);
                renderEastFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : i);
                tessellator.setColorOpaque_F(f8, f11, f14);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                tessellator.setColorOpaque_F(ff * f8, ff * f11, ff * f14);
            }
            int k = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3);
            renderWestFace(par1Block, par2, par3, par4, k);

            if (fancyGrass && k == 3 && overrideBlockTexture < 0)
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1) : 1.0F;
                tessellator.setColorOpaque_F(f8 * par5 * ff, f11 * par6 * ff, f14 * par7 * ff);
                renderWestFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.minX <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : i);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                tessellator.setColorOpaque_F(ff * f9, ff * f12, ff * f15);
            }
            int l = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 4);
            renderNorthFace(par1Block, par2, par3, par4, l);

            if (fancyGrass && l == 3 && overrideBlockTexture < 0)
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4) : 1.0F;
                tessellator.setColorOpaque_F(f9 * par5 * ff, f12 * par6 * ff, f15 * par7 * ff);
                renderNorthFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.maxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : i);
                tessellator.setColorOpaque_F(f9, f12, f15);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                tessellator.setColorOpaque_F(ff * f9, ff * f12, ff * f15);
            }
            int i1 = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 5);
            renderSouthFace(par1Block, par2, par3, par4, i1);

            if (fancyGrass && i1 == 3 && overrideBlockTexture < 0)
            {
                float ff = Minecraft.oldlighting ? par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4) : 1.0F;
                tessellator.setColorOpaque_F(f9 * par5 * ff, f12 * par6 * ff, f15 * par7 * ff);
                renderSouthFace(par1Block, par2, par3, par4, 38);
            }

            flag = true;
        }

        return flag;
    }

    private boolean func_78616_a(BlockCocoa par1BlockCocoa, int par2, int par3, int par4)
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
        int j = par1BlockCocoa.getBlockTextureFromSide(1);
        int k = BlockDirectional.getDirection(i);
        int l = BlockCocoa.func_72219_c(i);
        j = (j + 2) - l;
        int i1 = 4 + l * 2;
        int j1 = 5 + l * 2;
        int k1 = (j & 0xf) << 4;
        int l1 = j & 0xf0;
        double d = 15D - (double)i1;
        double d1 = 15D;
        double d2 = 4D;
        double d3 = 4D + (double)j1;
        double d4 = ((double)k1 + d) / 256D;
        double d5 = (((double)k1 + d1) - 0.01D) / 256D;
        double d6 = ((double)l1 + d2) / 256D;
        double d7 = (((double)l1 + d3) - 0.01D) / 256D;
        double d8 = 0.0D;
        double d9 = 0.0D;

        switch (k)
        {
            case 2:
                d8 = 8D - (double)(i1 / 2);
                d9 = 1.0D;
                break;

            case 0:
                d8 = 8D - (double)(i1 / 2);
                d9 = 15D - (double)i1;
                break;

            case 3:
                d8 = 15D - (double)i1;
                d9 = 8D - (double)(i1 / 2);
                break;

            case 1:
                d8 = 1.0D;
                d9 = 8D - (double)(i1 / 2);
                break;
        }

        double d10 = (double)par2 + d8 / 16D;
        double d11 = (double)par2 + (d8 + (double)i1) / 16D;
        double d12 = (double)par3 + (12D - (double)j1) / 16D;
        double d13 = (double)par3 + 0.75D;
        double d14 = (double)par4 + d9 / 16D;
        double d15 = (double)par4 + (d9 + (double)i1) / 16D;
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
        int i2 = i1;

        if (l >= 2)
        {
            i2--;
        }

        d4 = (float)(k1 + 0) / 256F;
        d5 = ((double)(k1 + i2) - 0.01D) / 256D;
        d6 = (float)(l1 + 0) / 256F;
        d7 = ((double)(l1 + i2) - 0.01D) / 256D;
        tessellator.addVertexWithUV(d10, d13, d15, d4, d7);
        tessellator.addVertexWithUV(d11, d13, d15, d5, d7);
        tessellator.addVertexWithUV(d11, d13, d14, d5, d6);
        tessellator.addVertexWithUV(d10, d13, d14, d4, d6);
        tessellator.addVertexWithUV(d10, d12, d14, d4, d6);
        tessellator.addVertexWithUV(d11, d12, d14, d5, d6);
        tessellator.addVertexWithUV(d11, d12, d15, d5, d7);
        tessellator.addVertexWithUV(d10, d12, d15, d4, d7);
        d4 = (float)(k1 + 12) / 256F;
        d5 = ((double)(k1 + 16) - 0.01D) / 256D;
        d6 = (float)(l1 + 0) / 256F;
        d7 = ((double)(l1 + 4) - 0.01D) / 256D;
        d8 = 8D;
        d9 = 0.0D;

        switch (k)
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

        if (k == 2 || k == 0)
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
        else if (k == 1 || k == 3)
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
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 - 1, par4, 0))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.minY <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4) : i);
                tessellator.setColorOpaque_F(f4, f8, f12);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
                tessellator.setColorOpaque_F(ff * f4, ff * f8, ff * f12);
            }
            renderBottomFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3 + 1, par4, 1))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.maxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4) : i);
                tessellator.setColorOpaque_F(f5, f9, f13);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
                tessellator.setColorOpaque_F(ff * f5, ff * f9, ff * f13);
            }
            renderTopFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 - 1, 2))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : i);
                tessellator.setColorOpaque_F(f6, f10, f14);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
                tessellator.setColorOpaque_F(ff * f6, ff * f10, ff * f14);
            }
            tessellator.addTranslation(0.0F, 0.0F, f16);
            renderEastFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 2));
            tessellator.addTranslation(0.0F, 0.0F, -f16);
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2, par3, par4 + 1, 3))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : i);
                tessellator.setColorOpaque_F(f6, f10, f14);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
                tessellator.setColorOpaque_F(ff * f6, ff * f10, ff * f14);
            }
            tessellator.addTranslation(0.0F, 0.0F, -f16);
            renderWestFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3));
            tessellator.addTranslation(0.0F, 0.0F, f16);
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 - 1, par3, par4, 4))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.minX <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : i);
                tessellator.setColorOpaque_F(f7, f11, f15);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
                tessellator.setColorOpaque_F(ff * f7, ff * f11, ff * f15);
            }
            tessellator.addTranslation(f16, 0.0F, 0.0F);
            renderNorthFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 4));
            tessellator.addTranslation(-f16, 0.0F, 0.0F);
            flag = true;
        }

        if (renderAllFaces || par1Block.shouldSideBeRendered(blockAccess, par2 + 1, par3, par4, 5))
        {
            if (!Minecraft.oldlighting){
                tessellator.setBrightness(par1Block.maxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : i);
                tessellator.setColorOpaque_F(f7, f11, f15);
            }else{
                float ff = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
                tessellator.setColorOpaque_F(ff * f7, ff * f11, ff * f15);
            }
            tessellator.addTranslation(-f16, 0.0F, 0.0F);
            renderSouthFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 5));
            tessellator.addTranslation(f16, 0.0F, 0.0F);
            flag = true;
        }

        return flag;
    }

    public boolean renderBlockFence(BlockFence par1BlockFence, int par2, int par3, int par4)
    {
        boolean flag = false;
        float f = 0.375F;
        float f1 = 0.625F;
        par1BlockFence.setBlockBounds(f, 0.0F, f, f1, 1.0F, f1);
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
            par1BlockFence.setBlockBounds(f4, f2, f, f5, f3, f1);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        if (flag2)
        {
            par1BlockFence.setBlockBounds(f, f2, f6, f1, f3, f7);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        f2 = 0.375F;
        f3 = 0.5625F;

        if (flag1)
        {
            par1BlockFence.setBlockBounds(f4, f2, f, f5, f3, f1);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        if (flag2)
        {
            par1BlockFence.setBlockBounds(f, f2, f6, f1, f3, f7);
            renderStandardBlock(par1BlockFence, par2, par3, par4);
            flag = true;
        }

        par1BlockFence.setBlockBoundsBasedOnState(blockAccess, par2, par3, par4);
        return flag;
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
            par1BlockDragonEgg.setBlockBounds(0.5F - f, f2, 0.5F - f, 0.5F + f, f1, 0.5F + f);
            renderStandardBlock(par1BlockDragonEgg, par2, par3, par4);
        }

        flag = true;
        par1BlockDragonEgg.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
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

        if (j == 3 || j == 1)
        {
            float f = 0.4375F;
            float f4 = 0.5625F;
            float f8 = 0.0F;
            float f12 = 0.125F;
            par1BlockFenceGate.setBlockBounds(f, 0.3125F, f8, f4, 1.0F, f12);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f8 = 0.875F;
            f12 = 1.0F;
            par1BlockFenceGate.setBlockBounds(f, 0.3125F, f8, f4, 1.0F, f12);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }
        else
        {
            float f1 = 0.0F;
            float f5 = 0.125F;
            float f9 = 0.4375F;
            float f13 = 0.5625F;
            par1BlockFenceGate.setBlockBounds(f1, 0.3125F, f9, f5, 1.0F, f13);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f1 = 0.875F;
            f5 = 1.0F;
            par1BlockFenceGate.setBlockBounds(f1, 0.3125F, f9, f5, 1.0F, f13);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }

        if (flag1)
        {
            if (j == 3)
            {
                par1BlockFenceGate.setBlockBounds(0.8125F, 0.375F, 0.0F, 0.9375F, 0.9375F, 0.125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.8125F, 0.375F, 0.875F, 0.9375F, 0.9375F, 1.0F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.5625F, 0.375F, 0.0F, 0.8125F, 0.5625F, 0.125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.5625F, 0.375F, 0.875F, 0.8125F, 0.5625F, 1.0F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.5625F, 0.75F, 0.0F, 0.8125F, 0.9375F, 0.125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.5625F, 0.75F, 0.875F, 0.8125F, 0.9375F, 1.0F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
            else if (j == 1)
            {
                par1BlockFenceGate.setBlockBounds(0.0625F, 0.375F, 0.0F, 0.1875F, 0.9375F, 0.125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.0625F, 0.375F, 0.875F, 0.1875F, 0.9375F, 1.0F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.1875F, 0.375F, 0.0F, 0.4375F, 0.5625F, 0.125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.1875F, 0.375F, 0.875F, 0.4375F, 0.5625F, 1.0F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.1875F, 0.75F, 0.0F, 0.4375F, 0.9375F, 0.125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.1875F, 0.75F, 0.875F, 0.4375F, 0.9375F, 1.0F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
            else if (j == 0)
            {
                par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.8125F, 0.125F, 0.9375F, 0.9375F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.8125F, 1.0F, 0.9375F, 0.9375F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.5625F, 0.125F, 0.5625F, 0.8125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.5625F, 1.0F, 0.5625F, 0.8125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.0F, 0.75F, 0.5625F, 0.125F, 0.9375F, 0.8125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.875F, 0.75F, 0.5625F, 1.0F, 0.9375F, 0.8125F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
            else if (j == 2)
            {
                par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.0625F, 0.125F, 0.9375F, 0.1875F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.0625F, 1.0F, 0.9375F, 0.1875F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.0F, 0.375F, 0.1875F, 0.125F, 0.5625F, 0.4375F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.875F, 0.375F, 0.1875F, 1.0F, 0.5625F, 0.4375F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.0F, 0.75F, 0.1875F, 0.125F, 0.9375F, 0.4375F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
                par1BlockFenceGate.setBlockBounds(0.875F, 0.75F, 0.1875F, 1.0F, 0.9375F, 0.4375F);
                renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            }
        }
        else if (j == 3 || j == 1)
        {
            float f2 = 0.4375F;
            float f6 = 0.5625F;
            float f10 = 0.375F;
            float f14 = 0.5F;
            par1BlockFenceGate.setBlockBounds(f2, 0.375F, f10, f6, 0.9375F, f14);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f10 = 0.5F;
            f14 = 0.625F;
            par1BlockFenceGate.setBlockBounds(f2, 0.375F, f10, f6, 0.9375F, f14);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f10 = 0.625F;
            f14 = 0.875F;
            par1BlockFenceGate.setBlockBounds(f2, 0.375F, f10, f6, 0.5625F, f14);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            par1BlockFenceGate.setBlockBounds(f2, 0.75F, f10, f6, 0.9375F, f14);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f10 = 0.125F;
            f14 = 0.375F;
            par1BlockFenceGate.setBlockBounds(f2, 0.375F, f10, f6, 0.5625F, f14);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            par1BlockFenceGate.setBlockBounds(f2, 0.75F, f10, f6, 0.9375F, f14);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }
        else
        {
            float f3 = 0.375F;
            float f7 = 0.5F;
            float f11 = 0.4375F;
            float f15 = 0.5625F;
            par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.9375F, f15);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f3 = 0.5F;
            f7 = 0.625F;
            par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.9375F, f15);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f3 = 0.625F;
            f7 = 0.875F;
            par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.5625F, f15);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            par1BlockFenceGate.setBlockBounds(f3, 0.75F, f11, f7, 0.9375F, f15);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            f3 = 0.125F;
            f7 = 0.375F;
            par1BlockFenceGate.setBlockBounds(f3, 0.375F, f11, f7, 0.5625F, f15);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            par1BlockFenceGate.setBlockBounds(f3, 0.75F, f11, f7, 0.9375F, f15);
            renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
        }

        par1BlockFenceGate.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return flag;
    }

    /**
     * Renders a stair block at the given coordinates
     */
    public boolean renderBlockStairs(Block par1Block, int par2, int par3, int par4)
    {
        int i = blockAccess.getBlockMetadata(par2, par3, par4);
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

        par1Block.setBlockBounds(0.0F, f, 0.0F, 1.0F, f1, 1.0F);
        renderStandardBlock(par1Block, par2, par3, par4);

        if (j == 0)
        {
            par1Block.setBlockBounds(0.5F, f2, 0.0F, 1.0F, f3, 1.0F);
            renderStandardBlock(par1Block, par2, par3, par4);
        }
        else if (j == 1)
        {
            par1Block.setBlockBounds(0.0F, f2, 0.0F, 0.5F, f3, 1.0F);
            renderStandardBlock(par1Block, par2, par3, par4);
        }
        else if (j == 2)
        {
            par1Block.setBlockBounds(0.0F, f2, 0.5F, 1.0F, f3, 1.0F);
            renderStandardBlock(par1Block, par2, par3, par4);
        }
        else if (j == 3)
        {
            par1Block.setBlockBounds(0.0F, f2, 0.0F, 1.0F, f3, 0.5F);
            renderStandardBlock(par1Block, par2, par3, par4);
        }

        par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        return true;
    }

    /**
     * Renders a door block at the given coordinates
     */
    public boolean renderBlockDoor(Block par1Block, int par2, int par3, int par4)
    {
        Tessellator tessellator = Tessellator.instance;
        BlockDoor blockdoor = (BlockDoor)par1Block;
        boolean flag = false;
        float f = 0.5F;
        float f1 = 1.0F;
        float f2 = 0.8F;
        float f3 = 0.6F;
        float f4 = 1.0F;
        float f5 = 1.0F;
        int i = par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4);
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.minY <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 - 1, par4) : i);
        }else{
            f4 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4);
            f5 = par1Block.getBlockBrightness(blockAccess, par2, par3 - 1, par4);
            if(blockdoor.minY > 0.0D)
            {
                f5 = f4;
            }
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f5 = 1.0F;
            }
        }
        tessellator.setColorOpaque_F(f * f5, f * f5, f * f5);
        renderBottomFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 0));
        flag = true;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.maxY >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3 + 1, par4) : i);
        }else{
            f5 = par1Block.getBlockBrightness(blockAccess, par2, par3 + 1, par4);
            if(blockdoor.maxY < 1.0D)
            {
                f5 = f4;
            }
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f5 = 1.0F;
            }
        }
        tessellator.setColorOpaque_F(f1 * f5, f1 * f5, f1 * f5);
        renderTopFace(par1Block, par2, par3, par4, par1Block.getBlockTexture(blockAccess, par2, par3, par4, 1));
        flag = true;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.minZ <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 - 1) : i);
        }else{
            f5 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 - 1);
            if(blockdoor.minZ > 0.0D)
            {
                f5 = f4;
            }
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f5 = 1.0F;
            }
        }
        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        int j = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 2);

        if (j < 0)
        {
            flipTexture = true;
            j = -j;
        }

        renderEastFace(par1Block, par2, par3, par4, j);
        flag = true;
        flipTexture = false;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.maxZ >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2, par3, par4 + 1) : i);
        }else{
            f5 = par1Block.getBlockBrightness(blockAccess, par2, par3, par4 + 1);
            if(blockdoor.maxZ < 1.0D)
            {
                f5 = f4;
            }
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f5 = 1.0F;
            }
        }
        tessellator.setColorOpaque_F(f2 * f5, f2 * f5, f2 * f5);
        j = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 3);

        if (j < 0)
        {
            flipTexture = true;
            j = -j;
        }

        renderWestFace(par1Block, par2, par3, par4, j);
        flag = true;
        flipTexture = false;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.minX <= 0.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 - 1, par3, par4) : i);
        }else{
            f5 = par1Block.getBlockBrightness(blockAccess, par2 - 1, par3, par4);
            if(blockdoor.minX > 0.0D)
            {
                f5 = f4;
            }
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f5 = 1.0F;
            }
        }
        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        j = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 4);

        if (j < 0)
        {
            flipTexture = true;
            j = -j;
        }

        renderNorthFace(par1Block, par2, par3, par4, j);
        flag = true;
        flipTexture = false;
        if (!Minecraft.oldlighting){
            tessellator.setBrightness(par1Block.maxX >= 1.0D ? par1Block.getMixedBrightnessForBlock(blockAccess, par2 + 1, par3, par4) : i);
        }else{
            f5 = par1Block.getBlockBrightness(blockAccess, par2 + 1, par3, par4);
            if(blockdoor.maxX < 1.0D)
            {
                f5 = f4;
            }
            if(Block.lightValue[par1Block.blockID] > 0)
            {
                f5 = 1.0F;
            }
        }
        tessellator.setColorOpaque_F(f3 * f5, f3 * f5, f3 * f5);
        j = par1Block.getBlockTexture(blockAccess, par2, par3, par4, 5);

        if (j < 0)
        {
            flipTexture = true;
            j = -j;
        }

        renderSouthFace(par1Block, par2, par3, par4, j);
        flag = true;
        flipTexture = false;
        return flag;
    }

    /**
     * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
     */
    public void renderBottomFace(Block par1Block, double par2, double par4, double par6, int par8)
    {
        Tessellator tessellator = Tessellator.instance;

        if (overrideBlockTexture >= 0)
        {
            par8 = overrideBlockTexture;
        }

        int i = (par8 & 0xf) << 4;
        int j = par8 & 0xf0;
        double d = ((double)i + par1Block.minX * 16D) / 256D;
        double d1 = (((double)i + par1Block.maxX * 16D) - 0.01D) / 256D;
        double d2 = ((double)j + par1Block.minZ * 16D) / 256D;
        double d3 = (((double)j + par1Block.maxZ * 16D) - 0.01D) / 256D;

        if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D)
        {
            d = ((float)i + 0.0F) / 256F;
            d1 = ((float)i + 15.99F) / 256F;
        }

        if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D)
        {
            d2 = ((float)j + 0.0F) / 256F;
            d3 = ((float)j + 15.99F) / 256F;
        }

        double d4 = d1;
        double d5 = d;
        double d6 = d2;
        double d7 = d3;

        if (uvRotateBottom == 2)
        {
            d = ((double)i + par1Block.minZ * 16D) / 256D;
            d2 = ((double)(j + 16) - par1Block.maxX * 16D) / 256D;
            d1 = ((double)i + par1Block.maxZ * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.minX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.maxZ * 16D) / 256D;
            d2 = ((double)j + par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.minZ * 16D) / 256D;
            d3 = ((double)j + par1Block.maxX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.maxX * 16D - 0.01D) / 256D;
            d2 = ((double)(j + 16) - par1Block.minZ * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.maxZ * 16D - 0.01D) / 256D;
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
        }

        double d8 = par2 + par1Block.minX;
        double d9 = par2 + par1Block.maxX;
        double d10 = par4 + par1Block.minY;
        double d11 = par6 + par1Block.minZ;
        double d12 = par6 + par1Block.maxZ;

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
    public void renderTopFace(Block par1Block, double par2, double par4, double par6, int par8)
    {
        Tessellator tessellator = Tessellator.instance;

        if (overrideBlockTexture >= 0)
        {
            par8 = overrideBlockTexture;
        }

        int i = (par8 & 0xf) << 4;
        int j = par8 & 0xf0;
        double d = ((double)i + par1Block.minX * 16D) / 256D;
        double d1 = (((double)i + par1Block.maxX * 16D) - 0.01D) / 256D;
        double d2 = ((double)j + par1Block.minZ * 16D) / 256D;
        double d3 = (((double)j + par1Block.maxZ * 16D) - 0.01D) / 256D;

        if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D)
        {
            d = ((float)i + 0.0F) / 256F;
            d1 = ((float)i + 15.99F) / 256F;
        }

        if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D)
        {
            d2 = ((float)j + 0.0F) / 256F;
            d3 = ((float)j + 15.99F) / 256F;
        }

        double d4 = d1;
        double d5 = d;
        double d6 = d2;
        double d7 = d3;

        if (uvRotateTop == 1)
        {
            d = ((double)i + par1Block.minZ * 16D) / 256D;
            d2 = ((double)(j + 16) - par1Block.maxX * 16D) / 256D;
            d1 = ((double)i + par1Block.maxZ * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.minX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.maxZ * 16D) / 256D;
            d2 = ((double)j + par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.minZ * 16D) / 256D;
            d3 = ((double)j + par1Block.maxX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.maxX * 16D - 0.01D) / 256D;
            d2 = ((double)(j + 16) - par1Block.minZ * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.maxZ * 16D - 0.01D) / 256D;
            d4 = d1;
            d5 = d;
            d6 = d2;
            d7 = d3;
        }

        double d8 = par2 + par1Block.minX;
        double d9 = par2 + par1Block.maxX;
        double d10 = par4 + par1Block.maxY;
        double d11 = par6 + par1Block.minZ;
        double d12 = par6 + par1Block.maxZ;

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
     * Renders the given texture to the east (z-negative) face of the block.  Args: block, x, y, z, texture
     */
    public void renderEastFace(Block par1Block, double par2, double par4, double par6, int par8)
    {
        Tessellator tessellator = Tessellator.instance;

        if (overrideBlockTexture >= 0)
        {
            par8 = overrideBlockTexture;
        }

        int i = (par8 & 0xf) << 4;
        int j = par8 & 0xf0;
        double d = ((double)i + par1Block.minX * 16D) / 256D;
        double d1 = (((double)i + par1Block.maxX * 16D) - 0.01D) / 256D;
        double d2 = ((double)(j + 16) - par1Block.maxY * 16D) / 256D;
        double d3 = ((double)(j + 16) - par1Block.minY * 16D - 0.01D) / 256D;

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D)
        {
            d = ((float)i + 0.0F) / 256F;
            d1 = ((float)i + 15.99F) / 256F;
        }

        if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D)
        {
            d2 = ((float)j + 0.0F) / 256F;
            d3 = ((float)j + 15.99F) / 256F;
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateEast == 2)
        {
            d = ((double)i + par1Block.minY * 16D) / 256D;
            d2 = ((double)(j + 16) - par1Block.minX * 16D) / 256D;
            d1 = ((double)i + par1Block.maxY * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.maxX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.maxY * 16D) / 256D;
            d2 = ((double)j + par1Block.maxX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.minY * 16D) / 256D;
            d3 = ((double)j + par1Block.minX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.maxX * 16D - 0.01D) / 256D;
            d2 = ((double)j + par1Block.maxY * 16D) / 256D;
            d3 = (((double)j + par1Block.minY * 16D) - 0.01D) / 256D;
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + par1Block.minX;
        double d10 = par2 + par1Block.maxX;
        double d11 = par4 + par1Block.minY;
        double d12 = par4 + par1Block.maxY;
        double d13 = par6 + par1Block.minZ;

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
     * Renders the given texture to the west (z-positive) face of the block.  Args: block, x, y, z, texture
     */
    public void renderWestFace(Block par1Block, double par2, double par4, double par6, int par8)
    {
        Tessellator tessellator = Tessellator.instance;

        if (overrideBlockTexture >= 0)
        {
            par8 = overrideBlockTexture;
        }

        int i = (par8 & 0xf) << 4;
        int j = par8 & 0xf0;
        double d = ((double)i + par1Block.minX * 16D) / 256D;
        double d1 = (((double)i + par1Block.maxX * 16D) - 0.01D) / 256D;
        double d2 = ((double)(j + 16) - par1Block.maxY * 16D) / 256D;
        double d3 = ((double)(j + 16) - par1Block.minY * 16D - 0.01D) / 256D;

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (par1Block.minX < 0.0D || par1Block.maxX > 1.0D)
        {
            d = ((float)i + 0.0F) / 256F;
            d1 = ((float)i + 15.99F) / 256F;
        }

        if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D)
        {
            d2 = ((float)j + 0.0F) / 256F;
            d3 = ((float)j + 15.99F) / 256F;
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateWest == 1)
        {
            d = ((double)i + par1Block.minY * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.minX * 16D) / 256D;
            d1 = ((double)i + par1Block.maxY * 16D) / 256D;
            d2 = ((double)(j + 16) - par1Block.maxX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.maxY * 16D) / 256D;
            d2 = ((double)j + par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.minY * 16D) / 256D;
            d3 = ((double)j + par1Block.maxX * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.minX * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.maxX * 16D - 0.01D) / 256D;
            d2 = ((double)j + par1Block.maxY * 16D) / 256D;
            d3 = (((double)j + par1Block.minY * 16D) - 0.01D) / 256D;
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + par1Block.minX;
        double d10 = par2 + par1Block.maxX;
        double d11 = par4 + par1Block.minY;
        double d12 = par4 + par1Block.maxY;
        double d13 = par6 + par1Block.maxZ;

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
     * Renders the given texture to the north (x-negative) face of the block.  Args: block, x, y, z, texture
     */
    public void renderNorthFace(Block par1Block, double par2, double par4, double par6, int par8)
    {
        Tessellator tessellator = Tessellator.instance;

        if (overrideBlockTexture >= 0)
        {
            par8 = overrideBlockTexture;
        }

        int i = (par8 & 0xf) << 4;
        int j = par8 & 0xf0;
        double d = ((double)i + par1Block.minZ * 16D) / 256D;
        double d1 = (((double)i + par1Block.maxZ * 16D) - 0.01D) / 256D;
        double d2 = ((double)(j + 16) - par1Block.maxY * 16D) / 256D;
        double d3 = ((double)(j + 16) - par1Block.minY * 16D - 0.01D) / 256D;

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D)
        {
            d = ((float)i + 0.0F) / 256F;
            d1 = ((float)i + 15.99F) / 256F;
        }

        if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D)
        {
            d2 = ((float)j + 0.0F) / 256F;
            d3 = ((float)j + 15.99F) / 256F;
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateNorth == 1)
        {
            d = ((double)i + par1Block.minY * 16D) / 256D;
            d2 = ((double)(j + 16) - par1Block.maxZ * 16D) / 256D;
            d1 = ((double)i + par1Block.maxY * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.minZ * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.maxY * 16D) / 256D;
            d2 = ((double)j + par1Block.minZ * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.minY * 16D) / 256D;
            d3 = ((double)j + par1Block.maxZ * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.minZ * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.maxZ * 16D - 0.01D) / 256D;
            d2 = ((double)j + par1Block.maxY * 16D) / 256D;
            d3 = (((double)j + par1Block.minY * 16D) - 0.01D) / 256D;
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + par1Block.minX;
        double d10 = par4 + par1Block.minY;
        double d11 = par4 + par1Block.maxY;
        double d12 = par6 + par1Block.minZ;
        double d13 = par6 + par1Block.maxZ;

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
     * Renders the given texture to the south (x-positive) face of the block.  Args: block, x, y, z, texture
     */
    public void renderSouthFace(Block par1Block, double par2, double par4, double par6, int par8)
    {
        Tessellator tessellator = Tessellator.instance;

        if (overrideBlockTexture >= 0)
        {
            par8 = overrideBlockTexture;
        }

        int i = (par8 & 0xf) << 4;
        int j = par8 & 0xf0;
        double d = ((double)i + par1Block.minZ * 16D) / 256D;
        double d1 = (((double)i + par1Block.maxZ * 16D) - 0.01D) / 256D;
        double d2 = ((double)(j + 16) - par1Block.maxY * 16D) / 256D;
        double d3 = ((double)(j + 16) - par1Block.minY * 16D - 0.01D) / 256D;

        if (flipTexture)
        {
            double d4 = d;
            d = d1;
            d1 = d4;
        }

        if (par1Block.minZ < 0.0D || par1Block.maxZ > 1.0D)
        {
            d = ((float)i + 0.0F) / 256F;
            d1 = ((float)i + 15.99F) / 256F;
        }

        if (par1Block.minY < 0.0D || par1Block.maxY > 1.0D)
        {
            d2 = ((float)j + 0.0F) / 256F;
            d3 = ((float)j + 15.99F) / 256F;
        }

        double d5 = d1;
        double d6 = d;
        double d7 = d2;
        double d8 = d3;

        if (uvRotateSouth == 2)
        {
            d = ((double)i + par1Block.minY * 16D) / 256D;
            d2 = ((double)(j + 16) - par1Block.minZ * 16D) / 256D;
            d1 = ((double)i + par1Block.maxY * 16D) / 256D;
            d3 = ((double)(j + 16) - par1Block.maxZ * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.maxY * 16D) / 256D;
            d2 = ((double)j + par1Block.maxZ * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.minY * 16D) / 256D;
            d3 = ((double)j + par1Block.minZ * 16D) / 256D;
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
            d = ((double)(i + 16) - par1Block.minZ * 16D) / 256D;
            d1 = ((double)(i + 16) - par1Block.maxZ * 16D - 0.01D) / 256D;
            d2 = ((double)j + par1Block.maxY * 16D) / 256D;
            d3 = (((double)j + par1Block.minY * 16D) - 0.01D) / 256D;
            d5 = d1;
            d6 = d;
            d7 = d2;
            d8 = d3;
        }

        double d9 = par2 + par1Block.maxX;
        double d10 = par4 + par1Block.minY;
        double d11 = par4 + par1Block.maxY;
        double d12 = par6 + par1Block.minZ;
        double d13 = par6 + par1Block.maxZ;

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

        if (j == 0 || j == 31 || j == 16 || j == 26)
        {
            if (j == 16)
            {
                par2 = 1;
            }

            par1Block.setBlockBoundsForItemRender();
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(0, par2));
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
            renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(1, par2));
            tessellator.draw();

            if (flag && useInventoryTint)
            {
                GL11.glColor4f(par3, par3, par3, 1.0F);
            }

            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(2, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(3, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(4, par2));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSideAndMetadata(5, par2));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        else if (j == 1)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            drawCrossedSquares(par1Block, par2, -0.5D, -0.5D, -0.5D);
            tessellator.draw();
        }
        else if (j == 19)
        {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, -1F, 0.0F);
            par1Block.setBlockBoundsForItemRender();
            renderBlockStemSmall(par1Block, par2, par1Block.maxY, -0.5D, -0.5D, -0.5D);
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
            renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1F);
            tessellator.addTranslation(0.0F, 0.0F, f1);
            renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
            tessellator.addTranslation(0.0F, 0.0F, -f1);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addTranslation(0.0F, 0.0F, -f1);
            renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
            tessellator.addTranslation(0.0F, 0.0F, f1);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0.0F, 0.0F);
            tessellator.addTranslation(f1, 0.0F, 0.0F);
            renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
            tessellator.addTranslation(-f1, 0.0F, 0.0F);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            tessellator.addTranslation(-f1, 0.0F, 0.0F);
            renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
            tessellator.addTranslation(f1, 0.0F, 0.0F);
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
        else if (j == 22)
        {
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
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
            renderTorchAtAngle(par1Block, -0.5D, -0.5D, -0.5D, 0.0D, 0.0D);
            tessellator.draw();
        }
        else if (j == 10)
        {
            for (int l = 0; l < 2; l++)
            {
                if (l == 0)
                {
                    par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
                }

                if (l == 1)
                {
                    par1Block.setBlockBounds(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
        }
        else if (j == 27)
        {
            int i1 = 0;
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            tessellator.startDrawingQuads();

            for (int l1 = 0; l1 < 8; l1++)
            {
                int i2 = 0;
                byte byte0 = 1;

                if (l1 == 0)
                {
                    i2 = 2;
                }

                if (l1 == 1)
                {
                    i2 = 3;
                }

                if (l1 == 2)
                {
                    i2 = 4;
                }

                if (l1 == 3)
                {
                    i2 = 5;
                    byte0 = 2;
                }

                if (l1 == 4)
                {
                    i2 = 6;
                    byte0 = 3;
                }

                if (l1 == 5)
                {
                    i2 = 7;
                    byte0 = 5;
                }

                if (l1 == 6)
                {
                    i2 = 6;
                    byte0 = 2;
                }

                if (l1 == 7)
                {
                    i2 = 3;
                }

                float f9 = (float)i2 / 16F;
                float f10 = 1.0F - (float)i1 / 16F;
                float f11 = 1.0F - (float)(i1 + byte0) / 16F;
                i1 += byte0;
                par1Block.setBlockBounds(0.5F - f9, f11, 0.5F - f9, 0.5F + f9, f10, 0.5F + f9);
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
            }

            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (j == 11)
        {
            for (int j1 = 0; j1 < 4; j1++)
            {
                float f4 = 0.125F;

                if (j1 == 0)
                {
                    par1Block.setBlockBounds(0.5F - f4, 0.0F, 0.0F, 0.5F + f4, 1.0F, f4 * 2.0F);
                }

                if (j1 == 1)
                {
                    par1Block.setBlockBounds(0.5F - f4, 0.0F, 1.0F - f4 * 2.0F, 0.5F + f4, 1.0F, 1.0F);
                }

                f4 = 0.0625F;

                if (j1 == 2)
                {
                    par1Block.setBlockBounds(0.5F - f4, 1.0F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 1.0F - f4, 1.0F + f4 * 2.0F);
                }

                if (j1 == 3)
                {
                    par1Block.setBlockBounds(0.5F - f4, 0.5F - f4 * 3F, -f4 * 2.0F, 0.5F + f4, 0.5F - f4, 1.0F + f4 * 2.0F);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else if (j == 21)
        {
            for (int k1 = 0; k1 < 3; k1++)
            {
                float f5 = 0.0625F;

                if (k1 == 0)
                {
                    par1Block.setBlockBounds(0.5F - f5, 0.3F, 0.0F, 0.5F + f5, 1.0F, f5 * 2.0F);
                }

                if (k1 == 1)
                {
                    par1Block.setBlockBounds(0.5F - f5, 0.3F, 1.0F - f5 * 2.0F, 0.5F + f5, 1.0F, 1.0F);
                }

                f5 = 0.0625F;

                if (k1 == 2)
                {
                    par1Block.setBlockBounds(0.5F - f5, 0.5F, 0.0F, 0.5F + f5, 1.0F - f5, 1.0F);
                }

                GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, -1F, 0.0F);
                renderBottomFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(0));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 1.0F, 0.0F);
                renderTopFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(1));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, -1F);
                renderEastFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(2));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(0.0F, 0.0F, 1.0F);
                renderWestFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(3));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(-1F, 0.0F, 0.0F);
                renderNorthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(4));
                tessellator.draw();
                tessellator.startDrawingQuads();
                tessellator.setNormal(1.0F, 0.0F, 0.0F);
                renderSouthFace(par1Block, 0.0D, 0.0D, 0.0D, par1Block.getBlockTextureFromSide(5));
                tessellator.draw();
                GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            par1Block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else
        {
            net.minecraft.client.Minecraft.invokeModMethod("ModLoader", "renderInvBlock",
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
        else
        {
            net.minecraft.client.Minecraft.invokeModMethod("ModLoader", "renderBlockIsItemFull3D", new Class[]{Integer.TYPE}, par0);
        }

        return par0 == 26;
    }
}
