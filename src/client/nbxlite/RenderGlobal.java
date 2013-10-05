package net.minecraft.src;

import java.nio.IntBuffer;
import java.util.*;
import org.lwjgl.opengl.ARBOcclusionQuery;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.ssp.WorldSSP;

public class RenderGlobal implements IWorldAccess
{
    private static final ResourceLocation locationMoonPhasesPng = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation locationSunPng = new ResourceLocation("textures/environment/sun.png");
    private static final ResourceLocation locationCloudsPng = new ResourceLocation("textures/environment/clouds.png");
    private static final ResourceLocation locationEndSkyPng = new ResourceLocation("textures/environment/end_sky.png");
    public List tileEntities;
    private WorldClient theWorld;

    /** The RenderEngine instance used by RenderGlobal */
    private final TextureManager renderEngine;
    private List worldRenderersToUpdate;
    private WorldRenderer sortedWorldRenderers[];
    private WorldRenderer worldRenderers[];
    private int renderChunksWide;
    private int renderChunksTall;
    private int renderChunksDeep;

    /** OpenGL render lists base */
    private int glRenderListBase;

    /** A reference to the Minecraft object. */
    private Minecraft mc;

    /** Global render blocks */
    private RenderBlocks globalRenderBlocks;

    /** OpenGL occlusion query base */
    private IntBuffer glOcclusionQueryBase;

    /** Is occlusion testing enabled */
    private boolean occlusionEnabled;

    /**
     * counts the cloud render updates. Used with mod to stagger some updates
     */
    private int cloudTickCounter;

    /** The star GL Call list */
    private int starGLCallList;

    /** OpenGL sky list */
    private int glSkyList;

    /** OpenGL sky list 2 */
    private int glSkyList2;

    /** Minimum block X */
    private int minBlockX;

    /** Minimum block Y */
    private int minBlockY;

    /** Minimum block Z */
    private int minBlockZ;

    /** Maximum block X */
    private int maxBlockX;

    /** Maximum block Y */
    private int maxBlockY;

    /** Maximum block Z */
    private int maxBlockZ;

    /**
     * Stores blocks currently being broken. Key is entity ID of the thing doing the breaking. Value is a
     * DestroyBlockProgress
     */
    private Map damagedBlocks;
    private Icon destroyBlockIcons[];
    private int renderDistance;

    /** Render entities startup counter (init value=2) */
    private int renderEntitiesStartupCounter;

    /** Count entities total */
    private int countEntitiesTotal;

    /** Count entities rendered */
    private int countEntitiesRendered;

    /** Count entities hidden */
    private int countEntitiesHidden;

    /** Occlusion query result */
    IntBuffer occlusionResult;

    /** How many renderers are loaded this frame that try to be rendered */
    private int renderersLoaded;

    /** How many renderers are being clipped by the frustrum this frame */
    private int renderersBeingClipped;

    /** How many renderers are being occluded this frame */
    private int renderersBeingOccluded;

    /** How many renderers are actually being rendered this frame */
    private int renderersBeingRendered;

    /**
     * How many renderers are skipping rendering due to not having a render pass this frame
     */
    private int renderersSkippingRenderPass;

    /** Dummy render int */
    private int dummyRenderInt;

    /** World renderers check index */
    private int worldRenderersCheckIndex;

    /** List of OpenGL lists for the current render pass */
    private List glRenderLists;
    private RenderList allRenderLists[] =
    {
        new RenderList(), new RenderList(), new RenderList(), new RenderList()
    };

    /**
     * Previous x position when the renderers were sorted. (Once the distance moves more than 4 units they will be
     * resorted)
     */
    double prevSortX;

    /**
     * Previous y position when the renderers were sorted. (Once the distance moves more than 4 units they will be
     * resorted)
     */
    double prevSortY;

    /**
     * Previous Z position when the renderers were sorted. (Once the distance moves more than 4 units they will be
     * resorted)
     */
    double prevSortZ;

    /**
     * The offset used to determine if a renderer is one of the sixteenth that are being updated this frame
     */
    int frustumCheckOffset;

    public static boolean nbxlite = false;
    public static boolean isTakingIsometricScreenshot = false;

    public RenderGlobal(Minecraft par1Minecraft)
    {
        tileEntities = new ArrayList();
        worldRenderersToUpdate = new ArrayList();
        damagedBlocks = new HashMap();
        renderDistance = -1;
        renderEntitiesStartupCounter = 2;
        occlusionResult = GLAllocation.createDirectIntBuffer(64);
        glRenderLists = new ArrayList();
        prevSortX = -9999D;
        prevSortY = -9999D;
        prevSortZ = -9999D;
        mc = par1Minecraft;
        renderEngine = par1Minecraft.getTextureManager();
        byte byte0 = 34;
        byte byte1 = 32;
        glRenderListBase = GLAllocation.generateDisplayLists(byte0 * byte0 * byte1 * 3);
        occlusionEnabled = OpenGlCapsChecker.checkARBOcclusion();

        if (occlusionEnabled)
        {
            occlusionResult.clear();
            glOcclusionQueryBase = GLAllocation.createDirectIntBuffer(byte0 * byte0 * byte1);
            glOcclusionQueryBase.clear();
            glOcclusionQueryBase.position(0);
            glOcclusionQueryBase.limit(byte0 * byte0 * byte1);
            ARBOcclusionQuery.glGenQueriesARB(glOcclusionQueryBase);
        }

        starGLCallList = GLAllocation.generateDisplayLists(3);
        GL11.glPushMatrix();
        GL11.glNewList(starGLCallList, GL11.GL_COMPILE);
        renderStars();
        GL11.glEndList();
        GL11.glPopMatrix();
        Tessellator tessellator = Tessellator.instance;
        glSkyList = starGLCallList + 1;
        GL11.glNewList(glSkyList, GL11.GL_COMPILE);
        byte byte2 = 64;
        int i = 256 / byte2 + 2;
        float f = 16F;

        for (int j = -byte2 * i; j <= byte2 * i; j += byte2)
        {
            for (int l = -byte2 * i; l <= byte2 * i; l += byte2)
            {
                tessellator.startDrawingQuads();
                tessellator.addVertex(j + 0, f, l + 0);
                tessellator.addVertex(j + byte2, f, l + 0);
                tessellator.addVertex(j + byte2, f, l + byte2);
                tessellator.addVertex(j + 0, f, l + byte2);
                tessellator.draw();
            }
        }

        GL11.glEndList();
        glSkyList2 = starGLCallList + 2;
        GL11.glNewList(glSkyList2, GL11.GL_COMPILE);
        f = -16F;
        tessellator.startDrawingQuads();

        for (int k = -byte2 * i; k <= byte2 * i; k += byte2)
        {
            for (int i1 = -byte2 * i; i1 <= byte2 * i; i1 += byte2)
            {
                tessellator.addVertex(k + byte2, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + 0);
                tessellator.addVertex(k + 0, f, i1 + byte2);
                tessellator.addVertex(k + byte2, f, i1 + byte2);
            }
        }

        tessellator.draw();
        GL11.glEndList();
    }

    private void renderStars()
    {
        Random random = new Random(10842L);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for (int i = 0; i < 1500; i++)
        {
            double d = random.nextFloat() * 2.0F - 1.0F;
            double d1 = random.nextFloat() * 2.0F - 1.0F;
            double d2 = random.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + random.nextFloat() * 0.1F;
            double d4 = d * d + d1 * d1 + d2 * d2;

            if (d4 >= 1.0D || d4 <= 0.01D)
            {
                continue;
            }

            d4 = 1.0D / Math.sqrt(d4);
            d *= d4;
            d1 *= d4;
            d2 *= d4;
            double d5 = d * 100D;
            double d6 = d1 * 100D;
            double d7 = d2 * 100D;
            double d8 = Math.atan2(d, d2);
            double d9 = Math.sin(d8);
            double d10 = Math.cos(d8);
            double d11 = Math.atan2(Math.sqrt(d * d + d2 * d2), d1);
            double d12 = Math.sin(d11);
            double d13 = Math.cos(d11);
            double d14 = random.nextDouble() * Math.PI * 2D;
            double d15 = Math.sin(d14);
            double d16 = Math.cos(d14);

            for (int j = 0; j < 4; j++)
            {
                double d17 = 0.0D;
                double d18 = (double)((j & 2) - 1) * d3;
                double d19 = (double)((j + 1 & 2) - 1) * d3;
                double d20 = d17;
                double d21 = d18 * d16 - d19 * d15;
                double d22 = d19 * d16 + d18 * d15;
                double d23 = d22;
                double d24 = d21 * d12 + d20 * d13;
                double d25 = d20 * d12 - d21 * d13;
                double d26 = d25 * d9 - d23 * d10;
                double d27 = d24;
                double d28 = d23 * d9 + d25 * d10;
                tessellator.addVertex(d5 + d26, d6 + d27, d7 + d28);
            }
        }

        tessellator.draw();
    }

    /**
     * set null to clear
     */
    public void setWorldAndLoadRenderers(WorldClient par1WorldClient)
    {
        if (theWorld != null)
        {
            theWorld.removeWorldAccess(this);
        }

        prevSortX = -9999D;
        prevSortY = -9999D;
        prevSortZ = -9999D;
        RenderManager.instance.set(par1WorldClient);
        theWorld = par1WorldClient;
        globalRenderBlocks = new RenderBlocks(par1WorldClient);

        if (par1WorldClient != null)
        {
            par1WorldClient.addWorldAccess(this);
            loadRenderers();
        }
    }

    /**
     * Loads all the renderers and sets up the basic settings usage
     */
    public void loadRenderers()
    {
        if (theWorld == null)
        {
            return;
        }

        Block.leaves.setGraphicsLevel(mc.gameSettings.fancyGraphics);
        renderDistance = mc.gameSettings.renderDistance;

        if (worldRenderers != null)
        {
            for (int i = 0; i < worldRenderers.length; i++)
            {
                worldRenderers[i].stopRendering();
            }
        }

        int j = 64 << 3 - renderDistance;

        if (j > 400)
        {
            j = 400;
        }

        renderChunksWide = j / 16 + 1;
        renderChunksDeep = j / 16 + 1;
        renderChunksTall = 16;
        worldRenderers = new WorldRenderer[renderChunksWide * renderChunksTall * renderChunksDeep];
        sortedWorldRenderers = new WorldRenderer[renderChunksWide * renderChunksTall * renderChunksDeep];
        int k = 0;
        int l = 0;
        minBlockX = 0;
        minBlockY = 0;
        minBlockZ = 0;
        maxBlockX = renderChunksWide;
        maxBlockY = renderChunksTall;
        maxBlockZ = renderChunksDeep;

        for (int i1 = 0; i1 < worldRenderersToUpdate.size(); i1++)
        {
            ((WorldRenderer)worldRenderersToUpdate.get(i1)).needsUpdate = false;
        }

        worldRenderersToUpdate.clear();
        tileEntities.clear();

        for (int j1 = 0; j1 < renderChunksWide; j1++)
        {
            for (int k1 = 0; k1 < renderChunksTall; k1++)
            {
                for (int l1 = 0; l1 < renderChunksDeep; l1++)
                {
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1] = new WorldRenderer(theWorld, tileEntities, j1 * 16, k1 * 16, l1 * 16, glRenderListBase + k);

                    if (occlusionEnabled)
                    {
                        worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].glOcclusionQuery = glOcclusionQueryBase.get(l);
                    }

                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].isWaitingOnOcclusionQuery = false;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].isVisible = true;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].isInFrustum = true;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].chunkIndex = l++;
                    worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1].markDirty();
                    sortedWorldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1] = worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1];
                    worldRenderersToUpdate.add(worldRenderers[(l1 * renderChunksTall + k1) * renderChunksWide + j1]);
                    k += 3;
                }
            }
        }

        if (theWorld != null)
        {
            EntityLivingBase entitylivingbase = mc.renderViewEntity;

            if (entitylivingbase != null)
            {
                markRenderersForNewPosition(MathHelper.floor_double(((Entity)(entitylivingbase)).posX), MathHelper.floor_double(((Entity)(entitylivingbase)).posY), MathHelper.floor_double(((Entity)(entitylivingbase)).posZ));
                Arrays.sort(sortedWorldRenderers, new EntitySorter(entitylivingbase));
            }
        }

        renderEntitiesStartupCounter = 2;
    }

    /**
     * Renders all entities within range and within the frustrum. Args: pos, frustrum, partialTickTime
     */
    public void renderEntities(Vec3 par1Vec3, ICamera par2ICamera, float par3)
    {
        if (renderEntitiesStartupCounter > 0)
        {
            renderEntitiesStartupCounter--;
            return;
        }

        theWorld.theProfiler.startSection("prepare");
        TileEntityRenderer.instance.cacheActiveRenderInfo(theWorld, mc.getTextureManager(), mc.fontRenderer, mc.renderViewEntity, par3);
        RenderManager.instance.cacheActiveRenderInfo(theWorld, mc.getTextureManager(), mc.fontRenderer, mc.renderViewEntity, mc.pointedEntityLiving, mc.gameSettings, par3);
        countEntitiesTotal = 0;
        countEntitiesRendered = 0;
        countEntitiesHidden = 0;
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        RenderManager.renderPosX = ((Entity)(entitylivingbase)).lastTickPosX + (((Entity)(entitylivingbase)).posX - ((Entity)(entitylivingbase)).lastTickPosX) * (double)par3;
        RenderManager.renderPosY = ((Entity)(entitylivingbase)).lastTickPosY + (((Entity)(entitylivingbase)).posY - ((Entity)(entitylivingbase)).lastTickPosY) * (double)par3;
        RenderManager.renderPosZ = ((Entity)(entitylivingbase)).lastTickPosZ + (((Entity)(entitylivingbase)).posZ - ((Entity)(entitylivingbase)).lastTickPosZ) * (double)par3;
        TileEntityRenderer.staticPlayerX = ((Entity)(entitylivingbase)).lastTickPosX + (((Entity)(entitylivingbase)).posX - ((Entity)(entitylivingbase)).lastTickPosX) * (double)par3;
        TileEntityRenderer.staticPlayerY = ((Entity)(entitylivingbase)).lastTickPosY + (((Entity)(entitylivingbase)).posY - ((Entity)(entitylivingbase)).lastTickPosY) * (double)par3;
        TileEntityRenderer.staticPlayerZ = ((Entity)(entitylivingbase)).lastTickPosZ + (((Entity)(entitylivingbase)).posZ - ((Entity)(entitylivingbase)).lastTickPosZ) * (double)par3;
        mc.entityRenderer.enableLightmap(par3);
        theWorld.theProfiler.endStartSection("global");
        List list = theWorld.getLoadedEntityList();
        countEntitiesTotal = list.size();

        for (int i = 0; i < theWorld.weatherEffects.size(); i++)
        {
            Entity entity = (Entity)theWorld.weatherEffects.get(i);
            countEntitiesRendered++;

            if (entity.isInRangeToRenderVec3D(par1Vec3))
            {
                RenderManager.instance.renderEntity(entity, par3);
            }
        }

        theWorld.theProfiler.endStartSection("entities");

        for (int j = 0; j < list.size(); j++)
        {
            Entity entity1 = (Entity)list.get(j);
            boolean flag = entity1.isInRangeToRenderVec3D(par1Vec3) && (entity1.ignoreFrustumCheck || par2ICamera.isBoundingBoxInFrustum(entity1.boundingBox) || entity1.riddenByEntity == mc.thePlayer);

            if (!flag && (entity1 instanceof EntityLiving))
            {
                EntityLiving entityliving = (EntityLiving)entity1;

                if (entityliving.getLeashed() && entityliving.getLeashedToEntity() != null)
                {
                    Entity entity2 = entityliving.getLeashedToEntity();
                    flag = par2ICamera.isBoundingBoxInFrustum(entity2.boundingBox);
                }
            }

            if (flag && (entity1 != mc.renderViewEntity || mc.gameSettings.thirdPersonView != 0 || mc.renderViewEntity.isPlayerSleeping() || isTakingIsometricScreenshot) && theWorld.blockExists(MathHelper.floor_double(entity1.posX), 0, MathHelper.floor_double(entity1.posZ)))
            {
                countEntitiesRendered++;
                RenderManager.instance.renderEntity(entity1, par3);
            }
        }

        theWorld.theProfiler.endStartSection("tileentities");
        RenderHelper.enableStandardItemLighting();

        for (int k = 0; k < tileEntities.size(); k++)
        {
            TileEntityRenderer.instance.renderTileEntity((TileEntity)tileEntities.get(k), par3);
        }

        mc.entityRenderer.disableLightmap(par3);
        theWorld.theProfiler.endSection();
    }

    /**
     * Gets the render info for use on the Debug screen
     */
    public String getDebugInfoRenders()
    {
        return (new StringBuilder()).append("C: ").append(renderersBeingRendered).append("/").append(renderersLoaded).append(". F: ").append(renderersBeingClipped).append(", O: ").append(renderersBeingOccluded).append(", E: ").append(renderersSkippingRenderPass).toString();
    }

    /**
     * Gets the entities info for use on the Debug screen
     */
    public String getDebugInfoEntities()
    {
        return (new StringBuilder()).append("E: ").append(countEntitiesRendered).append("/").append(countEntitiesTotal).append(". B: ").append(countEntitiesHidden).append(", I: ").append(countEntitiesTotal - countEntitiesHidden - countEntitiesRendered).toString();
    }

    /**
     * Goes through all the renderers setting new positions on them and those that have their position changed are
     * adding to be updated
     */
    private void markRenderersForNewPosition(int par1, int par2, int par3)
    {
        par1 -= 8;
        par2 -= 8;
        par3 -= 8;
        minBlockX = 0x7fffffff;
        minBlockY = 0x7fffffff;
        minBlockZ = 0x7fffffff;
        maxBlockX = 0x80000000;
        maxBlockY = 0x80000000;
        maxBlockZ = 0x80000000;
        int i = renderChunksWide * 16;
        int j = i / 2;

        for (int k = 0; k < renderChunksWide; k++)
        {
            int l = k * 16;
            int i1 = (l + j) - par1;

            if (i1 < 0)
            {
                i1 -= i - 1;
            }

            i1 /= i;
            l -= i1 * i;

            if (l < minBlockX)
            {
                minBlockX = l;
            }

            if (l > maxBlockX)
            {
                maxBlockX = l;
            }

            for (int j1 = 0; j1 < renderChunksDeep; j1++)
            {
                int k1 = j1 * 16;
                int l1 = (k1 + j) - par3;

                if (l1 < 0)
                {
                    l1 -= i - 1;
                }

                l1 /= i;
                k1 -= l1 * i;

                if (k1 < minBlockZ)
                {
                    minBlockZ = k1;
                }

                if (k1 > maxBlockZ)
                {
                    maxBlockZ = k1;
                }

                for (int i2 = 0; i2 < renderChunksTall; i2++)
                {
                    int j2 = i2 * 16;

                    if (j2 < minBlockY)
                    {
                        minBlockY = j2;
                    }

                    if (j2 > maxBlockY)
                    {
                        maxBlockY = j2;
                    }

                    WorldRenderer worldrenderer = worldRenderers[(j1 * renderChunksTall + i2) * renderChunksWide + k];
                    boolean flag = worldrenderer.needsUpdate;
                    worldrenderer.setPosition(l, j2, k1);

                    if (!flag && worldrenderer.needsUpdate)
                    {
                        worldRenderersToUpdate.add(worldrenderer);
                    }
                }
            }
        }
    }

    /**
     * Sorts all renderers based on the passed in entity. Args: entityLiving, renderPass, partialTickTime
     */
    public int sortAndRender(EntityLivingBase par1EntityLivingBase, int par2, double par3)
    {
        theWorld.theProfiler.startSection("sortchunks");

        for (int i = 0; i < 10; i++)
        {
            worldRenderersCheckIndex = (worldRenderersCheckIndex + 1) % worldRenderers.length;
            WorldRenderer worldrenderer = worldRenderers[worldRenderersCheckIndex];

            if (worldrenderer.needsUpdate && !worldRenderersToUpdate.contains(worldrenderer))
            {
                worldRenderersToUpdate.add(worldrenderer);
            }
        }

        if (mc.gameSettings.renderDistance != renderDistance)
        {
            loadRenderers();
        }

        if (par2 == 0)
        {
            renderersLoaded = 0;
            dummyRenderInt = 0;
            renderersBeingClipped = 0;
            renderersBeingOccluded = 0;
            renderersBeingRendered = 0;
            renderersSkippingRenderPass = 0;
        }

        double d = par1EntityLivingBase.lastTickPosX + (par1EntityLivingBase.posX - par1EntityLivingBase.lastTickPosX) * par3;
        double d1 = par1EntityLivingBase.lastTickPosY + (par1EntityLivingBase.posY - par1EntityLivingBase.lastTickPosY) * par3;
        double d2 = par1EntityLivingBase.lastTickPosZ + (par1EntityLivingBase.posZ - par1EntityLivingBase.lastTickPosZ) * par3;
        double d3 = par1EntityLivingBase.posX - prevSortX;
        double d4 = par1EntityLivingBase.posY - prevSortY;
        double d5 = par1EntityLivingBase.posZ - prevSortZ;

        if (d3 * d3 + d4 * d4 + d5 * d5 > 16D)
        {
            prevSortX = par1EntityLivingBase.posX;
            prevSortY = par1EntityLivingBase.posY;
            prevSortZ = par1EntityLivingBase.posZ;
            markRenderersForNewPosition(MathHelper.floor_double(par1EntityLivingBase.posX), MathHelper.floor_double(par1EntityLivingBase.posY), MathHelper.floor_double(par1EntityLivingBase.posZ));
            Arrays.sort(sortedWorldRenderers, new EntitySorter(par1EntityLivingBase));
        }

        RenderHelper.disableStandardItemLighting();
        int j = 0;

        if (occlusionEnabled && mc.gameSettings.advancedOpengl && !mc.gameSettings.anaglyph && par2 == 0)
        {
            int k = 0;
            int l = 16;
            checkOcclusionQueryResult(k, l);

            for (int i1 = k; i1 < l; i1++)
            {
                sortedWorldRenderers[i1].isVisible = true;
            }

            theWorld.theProfiler.endStartSection("render");
            j += renderSortedRenderers(k, l, par2, par3);

            do
            {
                theWorld.theProfiler.endStartSection("occ");
                int byte0 = l;
                l *= 2;

                if (l > sortedWorldRenderers.length)
                {
                    l = sortedWorldRenderers.length;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_FOG);
                GL11.glColorMask(false, false, false, false);
                GL11.glDepthMask(false);
                theWorld.theProfiler.startSection("check");
                checkOcclusionQueryResult(byte0, l);
                theWorld.theProfiler.endSection();
                GL11.glPushMatrix();
                float f = 0.0F;
                float f1 = 0.0F;
                float f2 = 0.0F;

                for (int j1 = byte0; j1 < l; j1++)
                {
                    if (sortedWorldRenderers[j1].skipAllRenderPasses())
                    {
                        sortedWorldRenderers[j1].isInFrustum = false;
                        continue;
                    }

                    if (!sortedWorldRenderers[j1].isInFrustum)
                    {
                        sortedWorldRenderers[j1].isVisible = true;
                    }

                    if (!sortedWorldRenderers[j1].isInFrustum || sortedWorldRenderers[j1].isWaitingOnOcclusionQuery)
                    {
                        continue;
                    }

                    float f3 = MathHelper.sqrt_float(sortedWorldRenderers[j1].distanceToEntitySquared(par1EntityLivingBase));
                    int k1 = (int)(1.0F + f3 / 128F);

                    if (cloudTickCounter % k1 != j1 % k1)
                    {
                        continue;
                    }

                    WorldRenderer worldrenderer1 = sortedWorldRenderers[j1];
                    float f4 = (float)((double)worldrenderer1.posXMinus - d);
                    float f5 = (float)((double)worldrenderer1.posYMinus - d1);
                    float f6 = (float)((double)worldrenderer1.posZMinus - d2);
                    float f7 = f4 - f;
                    float f8 = f5 - f1;
                    float f9 = f6 - f2;

                    if (f7 != 0.0F || f8 != 0.0F || f9 != 0.0F)
                    {
                        GL11.glTranslatef(f7, f8, f9);
                        f += f7;
                        f1 += f8;
                        f2 += f9;
                    }

                    theWorld.theProfiler.startSection("bb");
                    ARBOcclusionQuery.glBeginQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB, sortedWorldRenderers[j1].glOcclusionQuery);
                    sortedWorldRenderers[j1].callOcclusionQueryList();
                    ARBOcclusionQuery.glEndQueryARB(ARBOcclusionQuery.GL_SAMPLES_PASSED_ARB);
                    theWorld.theProfiler.endSection();
                    sortedWorldRenderers[j1].isWaitingOnOcclusionQuery = true;
                }

                GL11.glPopMatrix();

                if (mc.gameSettings.anaglyph)
                {
                    if (EntityRenderer.anaglyphField == 0)
                    {
                        GL11.glColorMask(false, true, true, true);
                    }
                    else
                    {
                        GL11.glColorMask(true, false, false, true);
                    }
                }
                else
                {
                    GL11.glColorMask(true, true, true, true);
                }

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_FOG);
                theWorld.theProfiler.endStartSection("render");
                j += renderSortedRenderers(byte0, l, par2, par3);
            }
            while (l < sortedWorldRenderers.length);
        }
        else
        {
            theWorld.theProfiler.endStartSection("render");
            j += renderSortedRenderers(0, sortedWorldRenderers.length, par2, par3);
        }

        theWorld.theProfiler.endSection();
        return j;
    }

    private void checkOcclusionQueryResult(int par1, int par2)
    {
        for (int i = par1; i < par2; i++)
        {
            if (!sortedWorldRenderers[i].isWaitingOnOcclusionQuery)
            {
                continue;
            }

            occlusionResult.clear();
            ARBOcclusionQuery.glGetQueryObjectuARB(sortedWorldRenderers[i].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_AVAILABLE_ARB, occlusionResult);

            if (occlusionResult.get(0) != 0)
            {
                sortedWorldRenderers[i].isWaitingOnOcclusionQuery = false;
                occlusionResult.clear();
                ARBOcclusionQuery.glGetQueryObjectuARB(sortedWorldRenderers[i].glOcclusionQuery, ARBOcclusionQuery.GL_QUERY_RESULT_ARB, occlusionResult);
                sortedWorldRenderers[i].isVisible = occlusionResult.get(0) != 0;
            }
        }
    }

    /**
     * Renders the sorted renders for the specified render pass. Args: startRenderer, numRenderers, renderPass,
     * partialTickTime
     */
    private int renderSortedRenderers(int par1, int par2, int par3, double par4)
    {
        glRenderLists.clear();
        int i = 0;

        for (int j = par1; j < par2; j++)
        {
            if (par3 == 0)
            {
                renderersLoaded++;

                if (sortedWorldRenderers[j].skipRenderPass[par3])
                {
                    renderersSkippingRenderPass++;
                }
                else if (!sortedWorldRenderers[j].isInFrustum)
                {
                    renderersBeingClipped++;
                }
                else if (occlusionEnabled && !sortedWorldRenderers[j].isVisible)
                {
                    renderersBeingOccluded++;
                }
                else
                {
                    renderersBeingRendered++;
                }
            }

            if (sortedWorldRenderers[j].skipRenderPass[par3] || !sortedWorldRenderers[j].isInFrustum || occlusionEnabled && !sortedWorldRenderers[j].isVisible)
            {
                continue;
            }

            int k = sortedWorldRenderers[j].getGLCallListForPass(par3);

            if (k >= 0)
            {
                glRenderLists.add(sortedWorldRenderers[j]);
                i++;
            }
        }

        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        double d = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * par4;
        double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * par4;
        double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * par4;
        int l = 0;

        for (int i1 = 0; i1 < allRenderLists.length; i1++)
        {
            allRenderLists[i1].func_78421_b();
        }

        for (int j1 = 0; j1 < glRenderLists.size(); j1++)
        {
            WorldRenderer worldrenderer = (WorldRenderer)glRenderLists.get(j1);
            int k1 = -1;

            for (int l1 = 0; l1 < l; l1++)
            {
                if (allRenderLists[l1].func_78418_a(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus))
                {
                    k1 = l1;
                }
            }

            if (k1 < 0)
            {
                k1 = l++;
                allRenderLists[k1].func_78422_a(worldrenderer.posXMinus, worldrenderer.posYMinus, worldrenderer.posZMinus, d, d1, d2);
            }

            allRenderLists[k1].func_78420_a(worldrenderer.getGLCallListForPass(par3));
        }

        renderAllRenderLists(par3, par4);
        return i;
    }

    /**
     * Render all render lists
     */
    public void renderAllRenderLists(int par1, double par2)
    {
        mc.entityRenderer.enableLightmap(par2);

        for (int i = 0; i < allRenderLists.length; i++)
        {
            allRenderLists[i].func_78419_a();
        }

        mc.entityRenderer.disableLightmap(par2);
    }

    public void updateClouds()
    {
        cloudTickCounter++;

        if (cloudTickCounter % 20 == 0)
        {
            Iterator iterator = damagedBlocks.values().iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
                int i = destroyblockprogress.getCreationCloudUpdateTick();

                if (cloudTickCounter - i > 400)
                {
                    iterator.remove();
                }
            }
            while (true);
        }
    }

    /**
     * Renders the sky with the partial tick time. Args: partialTickTime
     */
    public void renderSky(float par1)
    {
        if (mc.theWorld.provider.dimensionId == 1)
        {
            GL11.glDisable(GL11.GL_FOG);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.disableStandardItemLighting();
            GL11.glDepthMask(false);
            renderEngine.bindTexture(locationEndSkyPng);
            Tessellator tessellator = Tessellator.instance;

            for (int i = 0; i < 6; i++)
            {
                GL11.glPushMatrix();

                if (i == 1)
                {
                    GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 2)
                {
                    GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 3)
                {
                    GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
                }

                if (i == 4)
                {
                    GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
                }

                if (i == 5)
                {
                    GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
                }

                tessellator.startDrawingQuads();
                tessellator.setColorOpaque_I(0x282828);
                tessellator.addVertexWithUV(-100D, -100D, -100D, 0.0D, 0.0D);
                tessellator.addVertexWithUV(-100D, -100D, 100D, 0.0D, 16D);
                tessellator.addVertexWithUV(100D, -100D, 100D, 16D, 16D);
                tessellator.addVertexWithUV(100D, -100D, -100D, 16D, 0.0D);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            return;
        }

        if (!mc.theWorld.provider.isSurfaceWorld())
        {
            return;
        }

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Vec3 vec3 = theWorld.getSkyColor(mc.renderViewEntity, par1);
        float f = (float)vec3.xCoord;
        float f1 = (float)vec3.yCoord;
        float f2 = (float)vec3.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float f3 = (f * 30F + f1 * 59F + f2 * 11F) / 100F;
            float f4 = (f * 30F + f1 * 70F) / 100F;
            float f5 = (f * 30F + f2 * 70F) / 100F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        GL11.glColor3f(f, f1, f2);
        Tessellator tessellator1 = Tessellator.instance;
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glColor3f(f, f1, f2);
        GL11.glCallList(glSkyList);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderHelper.disableStandardItemLighting();
        float af[] = theWorld.provider.calcSunriseSunsetColors(theWorld.getCelestialAngle(par1), par1);

        if (af != null)
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glPushMatrix();
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(MathHelper.sin(theWorld.getCelestialAngleRadians(par1)) >= 0.0F ? 0.0F : 180F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(90F, 0.0F, 0.0F, 1.0F);
            float f6 = af[0];
            float f7 = af[1];
            float f9 = af[2];

            if (mc.gameSettings.anaglyph)
            {
                float f12 = (f6 * 30F + f7 * 59F + f9 * 11F) / 100F;
                float f15 = (f6 * 30F + f7 * 70F) / 100F;
                float f18 = (f6 * 30F + f9 * 70F) / 100F;
                f6 = f12;
                f7 = f15;
                f9 = f18;
            }

            tessellator1.startDrawing(6);
            tessellator1.setColorRGBA_F(f6, f7, f9, af[3]);
            tessellator1.addVertex(0.0D, 100D, 0.0D);
            int j = 16;
            tessellator1.setColorRGBA_F(af[0], af[1], af[2], 0.0F);

            for (int k = 0; k <= j; k++)
            {
                float f19 = ((float)k * (float)Math.PI * 2.0F) / (float)j;
                float f21 = MathHelper.sin(f19);
                float f22 = MathHelper.cos(f19);
                tessellator1.addVertex(f21 * 120F, f22 * 120F, -f22 * 40F * af[3]);
            }

            tessellator1.draw();
            GL11.glPopMatrix();
            GL11.glShadeModel(GL11.GL_FLAT);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glPushMatrix();
        float dd = 1.0F - theWorld.getRainStrength(par1);
        float f8 = 0.0F;
        float f10 = 0.0F;
        float f13 = 0.0F;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, dd);
        GL11.glTranslatef(f8, f10, f13);
        GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(theWorld.getCelestialAngle(par1) * 360F, 1.0F, 0.0F, 0.0F);
        float f16 = 30F;
        renderEngine.bindTexture(locationSunPng);
        tessellator1.startDrawingQuads();
        tessellator1.addVertexWithUV(-f16, 100D, -f16, 0.0D, 0.0D);
        tessellator1.addVertexWithUV(f16, 100D, -f16, 1.0D, 0.0D);
        tessellator1.addVertexWithUV(f16, 100D, f16, 1.0D, 1.0D);
        tessellator1.addVertexWithUV(-f16, 100D, f16, 0.0D, 1.0D);
        tessellator1.draw();
        f16 = 20F;
        renderEngine.bindTexture(locationMoonPhasesPng);
        int l = theWorld.getMoonPhase();
        int i1 = l % 4;
        int j1 = (l / 4) % 2;
        float f23 = (float)(i1 + 0) / 4F;
        float f24 = (float)(j1 + 0) / 2.0F;
        float f25 = (float)(i1 + 1) / 4F;
        float f26 = (float)(j1 + 1) / 2.0F;
        tessellator1.startDrawingQuads();
        tessellator1.addVertexWithUV(-f16, -100D, f16, f25, f26);
        tessellator1.addVertexWithUV(f16, -100D, f16, f23, f26);
        tessellator1.addVertexWithUV(f16, -100D, -f16, f23, f24);
        tessellator1.addVertexWithUV(-f16, -100D, -f16, f25, f24);
        tessellator1.draw();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float f27 = theWorld.getStarBrightness(par1) * dd;

        if (f27 > 0.0F)
        {
            GL11.glColor4f(f27, f27, f27, f27);
            GL11.glCallList(starGLCallList);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_FOG);
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glColor3f(0.0F, 0.0F, 0.0F);
        double d = mc.thePlayer.getPosition(par1).yCoord - theWorld.getHorizon();

        if (d < 0.0D)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 12F, 0.0F);
            GL11.glCallList(glSkyList2);
            GL11.glPopMatrix();
            float f11 = 1.0F;
            float f14 = -(float)(d + 65D);
            float f17 = -f11;
            float f20 = f14;
            tessellator1.startDrawingQuads();
            tessellator1.setColorRGBA_I(0, 255);
            tessellator1.addVertex(-f11, f20, f11);
            tessellator1.addVertex(f11, f20, f11);
            tessellator1.addVertex(f11, f17, f11);
            tessellator1.addVertex(-f11, f17, f11);
            tessellator1.addVertex(-f11, f17, -f11);
            tessellator1.addVertex(f11, f17, -f11);
            tessellator1.addVertex(f11, f20, -f11);
            tessellator1.addVertex(-f11, f20, -f11);
            tessellator1.addVertex(f11, f17, -f11);
            tessellator1.addVertex(f11, f17, f11);
            tessellator1.addVertex(f11, f20, f11);
            tessellator1.addVertex(f11, f20, -f11);
            tessellator1.addVertex(-f11, f20, -f11);
            tessellator1.addVertex(-f11, f20, f11);
            tessellator1.addVertex(-f11, f17, f11);
            tessellator1.addVertex(-f11, f17, -f11);
            tessellator1.addVertex(-f11, f17, -f11);
            tessellator1.addVertex(-f11, f17, f11);
            tessellator1.addVertex(f11, f17, f11);
            tessellator1.addVertex(f11, f17, -f11);
            tessellator1.draw();
        }

        if (theWorld.provider.isSkyColored())
        {
            GL11.glColor3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        }
        else
        {
            GL11.glColor3f(f, f1, f2);
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -(float)(d - 16D), 0.0F);
        GL11.glCallList(glSkyList2);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(true);
    }

    public void renderClouds(float par1)
    {
        if (!mc.theWorld.provider.isSurfaceWorld())
        {
            return;
        }

        if (mc.gameSettings.fancyGraphics)
        {
            renderCloudsFancy(par1);
            return;
        }

        GL11.glDisable(GL11.GL_CULL_FACE);
        float f = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)par1);
        byte byte0 = 32;
        int i = 256 / byte0;
        Tessellator tessellator = Tessellator.instance;
        renderEngine.bindTexture(locationCloudsPng);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3 vec3 = theWorld.getCloudColour(par1);
        float f1 = (float)vec3.xCoord;
        float f2 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float f4 = (f1 * 30F + f2 * 59F + f3 * 11F) / 100F;
            float f6 = (f1 * 30F + f2 * 70F) / 100F;
            float f7 = (f1 * 30F + f3 * 70F) / 100F;
            f1 = f4;
            f2 = f6;
            f3 = f7;
        }

        float f5 = 0.0004882813F;
        double d = (float)cloudTickCounter + par1;
        if (mc.timecontrol && mc.enableSP){
            d += (((WorldSSP)theWorld).field_35467_J + (((WorldSSP)theWorld).field_35468_K - ((WorldSSP)theWorld).field_35467_J) * (double)par1) * 24000D;
        }
        double d1 = mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * (double)par1 + d * 0.029999999329447746D;
        double d2 = mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * (double)par1;
        int j = MathHelper.floor_double(d1 / 2048D);
        int k = MathHelper.floor_double(d2 / 2048D);
        d1 -= j * 2048;
        d2 -= k * 2048;
        float f8 = (theWorld.provider.getCloudHeight() - f) + 0.33F;
        float f9 = (float)(d1 * (double)f5);
        float f10 = (float)(d2 * (double)f5);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, 0.8F);

        for (int l = -byte0 * i; l < byte0 * i; l += byte0)
        {
            for (int i1 = -byte0 * i; i1 < byte0 * i; i1 += byte0)
            {
                tessellator.addVertexWithUV(l + 0, f8, i1 + byte0, (float)(l + 0) * f5 + f9, (float)(i1 + byte0) * f5 + f10);
                tessellator.addVertexWithUV(l + byte0, f8, i1 + byte0, (float)(l + byte0) * f5 + f9, (float)(i1 + byte0) * f5 + f10);
                tessellator.addVertexWithUV(l + byte0, f8, i1 + 0, (float)(l + byte0) * f5 + f9, (float)(i1 + 0) * f5 + f10);
                tessellator.addVertexWithUV(l + 0, f8, i1 + 0, (float)(l + 0) * f5 + f9, (float)(i1 + 0) * f5 + f10);
            }
        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    /**
     * Checks if the given position is to be rendered with cloud fog
     */
    public boolean hasCloudFog(double par1, double par3, double d, float f)
    {
        return false;
    }

    /**
     * Renders the 3d fancy clouds
     */
    public void renderCloudsFancy(float par1)
    {
        GL11.glDisable(GL11.GL_CULL_FACE);
        float f = (float)(mc.renderViewEntity.lastTickPosY + (mc.renderViewEntity.posY - mc.renderViewEntity.lastTickPosY) * (double)par1);
        Tessellator tessellator = Tessellator.instance;
        float f1 = 12F;
        float f2 = 4F;
        double d = (float)cloudTickCounter + par1;
        if (mc.timecontrol && mc.enableSP){
            d += (((WorldSSP)theWorld).field_35467_J + (((WorldSSP)theWorld).field_35468_K - ((WorldSSP)theWorld).field_35467_J) * (double)par1) * 24000D;
        }
        double d1 = (mc.renderViewEntity.prevPosX + (mc.renderViewEntity.posX - mc.renderViewEntity.prevPosX) * (double)par1 + d * 0.029999999329447746D) / (double)f1;
        double d2 = (mc.renderViewEntity.prevPosZ + (mc.renderViewEntity.posZ - mc.renderViewEntity.prevPosZ) * (double)par1) / (double)f1 + 0.33000001311302185D;
        float f3 = (theWorld.provider.getCloudHeight() - f) + 0.33F;
        int i = MathHelper.floor_double(d1 / 2048D);
        int j = MathHelper.floor_double(d2 / 2048D);
        d1 -= i * 2048;
        d2 -= j * 2048;
        renderEngine.bindTexture(locationCloudsPng);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Vec3 vec3 = theWorld.getCloudColour(par1);
        float f4 = (float)vec3.xCoord;
        float f5 = (float)vec3.yCoord;
        float f6 = (float)vec3.zCoord;

        if (mc.gameSettings.anaglyph)
        {
            float f7 = (f4 * 30F + f5 * 59F + f6 * 11F) / 100F;
            float f9 = (f4 * 30F + f5 * 70F) / 100F;
            float f11 = (f4 * 30F + f6 * 70F) / 100F;
            f4 = f7;
            f5 = f9;
            f6 = f11;
        }

        float f8 = (float)(d1 * 0.0D);
        float f10 = (float)(d2 * 0.0D);
        float f12 = 0.00390625F;
        f8 = (float)MathHelper.floor_double(d1) * f12;
        f10 = (float)MathHelper.floor_double(d2) * f12;
        float f13 = (float)(d1 - (double)MathHelper.floor_double(d1));
        float f14 = (float)(d2 - (double)MathHelper.floor_double(d2));
        int k = 8;
        byte byte0 = 4;
        float f15 = 0.0009765625F;
        GL11.glScalef(f1, 1.0F, f1);

        for (int l = 0; l < 2; l++)
        {
            if (l == 0)
            {
                GL11.glColorMask(false, false, false, false);
            }
            else if (mc.gameSettings.anaglyph)
            {
                if (EntityRenderer.anaglyphField == 0)
                {
                    GL11.glColorMask(false, true, true, true);
                }
                else
                {
                    GL11.glColorMask(true, false, false, true);
                }
            }
            else
            {
                GL11.glColorMask(true, true, true, true);
            }

            for (int i1 = -byte0 + 1; i1 <= byte0; i1++)
            {
                for (int j1 = -byte0 + 1; j1 <= byte0; j1++)
                {
                    tessellator.startDrawingQuads();
                    float f16 = i1 * k;
                    float f17 = j1 * k;
                    float f18 = f16 - f13;
                    float f19 = f17 - f14;

                    if (f3 > -f2 - 1.0F)
                    {
                        tessellator.setColorRGBA_F(f4 * 0.7F, f5 * 0.7F, f6 * 0.7F, 0.8F);
                        tessellator.setNormal(0.0F, -1F, 0.0F);
                        tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, f19 + (float)k, (f16 + 0.0F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, f19 + (float)k, (f16 + (float)k) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, f19 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, f19 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                    }

                    if (f3 <= f2 + 1.0F)
                    {
                        tessellator.setColorRGBA_F(f4, f5, f6, 0.8F);
                        tessellator.setNormal(0.0F, 1.0F, 0.0F);
                        tessellator.addVertexWithUV(f18 + 0.0F, (f3 + f2) - f15, f19 + (float)k, (f16 + 0.0F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, (f3 + f2) - f15, f19 + (float)k, (f16 + (float)k) * f12 + f8, (f17 + (float)k) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + (float)k, (f3 + f2) - f15, f19 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        tessellator.addVertexWithUV(f18 + 0.0F, (f3 + f2) - f15, f19 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                    }

                    tessellator.setColorRGBA_F(f4 * 0.9F, f5 * 0.9F, f6 * 0.9F, 0.8F);

                    if (i1 > -1)
                    {
                        tessellator.setNormal(-1F, 0.0F, 0.0F);

                        for (int k1 = 0; k1 < k; k1++)
                        {
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + 0.0F, f19 + (float)k, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + f2, f19 + (float)k, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + f2, f19 + 0.0F, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k1 + 0.0F, f3 + 0.0F, f19 + 0.0F, (f16 + (float)k1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        }
                    }

                    if (i1 <= 1)
                    {
                        tessellator.setNormal(1.0F, 0.0F, 0.0F);

                        for (int l1 = 0; l1 < k; l1++)
                        {
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + 0.0F, f19 + (float)k, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + f2, f19 + (float)k, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + (float)k) * f12 + f10);
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + f2, f19 + 0.0F, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                            tessellator.addVertexWithUV((f18 + (float)l1 + 1.0F) - f15, f3 + 0.0F, f19 + 0.0F, (f16 + (float)l1 + 0.5F) * f12 + f8, (f17 + 0.0F) * f12 + f10);
                        }
                    }

                    tessellator.setColorRGBA_F(f4 * 0.8F, f5 * 0.8F, f6 * 0.8F, 0.8F);

                    if (j1 > -1)
                    {
                        tessellator.setNormal(0.0F, 0.0F, -1F);

                        for (int i2 = 0; i2 < k; i2++)
                        {
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + f2, f19 + (float)i2 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + f2, f19 + (float)i2 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, f19 + (float)i2 + 0.0F, (f16 + (float)k) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, f19 + (float)i2 + 0.0F, (f16 + 0.0F) * f12 + f8, (f17 + (float)i2 + 0.5F) * f12 + f10);
                        }
                    }

                    if (j1 <= 1)
                    {
                        tessellator.setNormal(0.0F, 0.0F, 1.0F);

                        for (int j2 = 0; j2 < k; j2++)
                        {
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + f2, (f19 + (float)j2 + 1.0F) - f15, (f16 + 0.0F) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + f2, (f19 + (float)j2 + 1.0F) - f15, (f16 + (float)k) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + (float)k, f3 + 0.0F, (f19 + (float)j2 + 1.0F) - f15, (f16 + (float)k) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                            tessellator.addVertexWithUV(f18 + 0.0F, f3 + 0.0F, (f19 + (float)j2 + 1.0F) - f15, (f16 + 0.0F) * f12 + f8, (f17 + (float)j2 + 0.5F) * f12 + f10);
                        }
                    }

                    tessellator.draw();
                }
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    /**
     * Updates some of the renderers sorted by distance from the player
     */
    public boolean updateRenderers(EntityLivingBase par1EntityLivingBase, boolean par2)
    {
        byte byte0 = 2;
        RenderSorter rendersorter = new RenderSorter(par1EntityLivingBase);
        WorldRenderer aworldrenderer[] = new WorldRenderer[byte0];
        ArrayList arraylist = null;
        int i = worldRenderersToUpdate.size();
        int j = 0;
        theWorld.theProfiler.startSection("nearChunksSearch");

        for (int k = 0; k < i; k++)
        {
            WorldRenderer worldrenderer = (WorldRenderer)worldRenderersToUpdate.get(k);
            if (nbxlite){
                if (ODNBXlite.isFinite()){
                    if (worldrenderer.posX<0 || worldrenderer.posZ<0 || worldrenderer.posX>ODNBXlite.IndevWidthX-16 || worldrenderer.posZ>ODNBXlite.IndevWidthZ-16){
                        continue;
                    }
                }
            }
            if (worldrenderer == null)
            {
                continue;
            }

            if (!par2)
            {
                if (worldrenderer.distanceToEntitySquared(par1EntityLivingBase) > 256F)
                {
                    int l1;

                    for (l1 = 0; l1 < byte0 && (aworldrenderer[l1] == null || rendersorter.doCompare(aworldrenderer[l1], worldrenderer) <= 0); l1++) { }

                    if (--l1 <= 0)
                    {
                        continue;
                    }

                    for (int j2 = l1; --j2 != 0;)
                    {
                        aworldrenderer[j2 - 1] = aworldrenderer[j2];
                    }

                    aworldrenderer[l1] = worldrenderer;
                    continue;
                }
            }
            else if (!worldrenderer.isInFrustum)
            {
                continue;
            }

            if (arraylist == null)
            {
                arraylist = new ArrayList();
            }

            j++;
            arraylist.add(worldrenderer);
            worldRenderersToUpdate.set(k, null);
        }

        theWorld.theProfiler.endSection();
        theWorld.theProfiler.startSection("sort");

        if (arraylist != null)
        {
            if (arraylist.size() > 1)
            {
                Collections.sort(arraylist, rendersorter);
            }

            for (int l = arraylist.size() - 1; l >= 0; l--)
            {
                WorldRenderer worldrenderer1 = (WorldRenderer)arraylist.get(l);
                worldrenderer1.updateRenderer();
                worldrenderer1.needsUpdate = false;
            }
        }

        theWorld.theProfiler.endSection();
        int i1 = 0;
        theWorld.theProfiler.startSection("rebuild");

        for (int j1 = byte0 - 1; j1 >= 0; j1--)
        {
            WorldRenderer worldrenderer2 = aworldrenderer[j1];

            if (worldrenderer2 == null)
            {
                continue;
            }

            if (!worldrenderer2.isInFrustum && j1 != byte0 - 1)
            {
                aworldrenderer[j1] = null;
                aworldrenderer[0] = null;
                break;
            }

            aworldrenderer[j1].updateRenderer();
            aworldrenderer[j1].needsUpdate = false;
            i1++;
        }

        theWorld.theProfiler.endSection();
        theWorld.theProfiler.startSection("cleanup");
        int k1 = 0;
        int i2 = 0;

        for (int k2 = worldRenderersToUpdate.size(); k1 != k2; k1++)
        {
            WorldRenderer worldrenderer3 = (WorldRenderer)worldRenderersToUpdate.get(k1);

            if (worldrenderer3 == null)
            {
                continue;
            }

            boolean flag = false;

            for (int l2 = 0; l2 < byte0 && !flag; l2++)
            {
                if (worldrenderer3 == aworldrenderer[l2])
                {
                    flag = true;
                }
            }

            if (flag)
            {
                continue;
            }

            if (i2 != k1)
            {
                worldRenderersToUpdate.set(i2, worldrenderer3);
            }

            i2++;
        }

        theWorld.theProfiler.endSection();
        theWorld.theProfiler.startSection("trim");

        while (--k1 >= i2)
        {
            worldRenderersToUpdate.remove(k1);
        }

        theWorld.theProfiler.endSection();
        return i == j + i1;
    }

    public void drawBlockDamageTexture(Tessellator par1Tessellator, EntityPlayer par2EntityPlayer, float par3)
    {
        double d = par2EntityPlayer.lastTickPosX + (par2EntityPlayer.posX - par2EntityPlayer.lastTickPosX) * (double)par3;
        double d1 = par2EntityPlayer.lastTickPosY + (par2EntityPlayer.posY - par2EntityPlayer.lastTickPosY) * (double)par3;
        double d2 = par2EntityPlayer.lastTickPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.lastTickPosZ) * (double)par3;

        if (!damagedBlocks.isEmpty())
        {
            GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR);
            renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPolygonOffset(-3F, -3F);
            GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            par1Tessellator.startDrawingQuads();
            par1Tessellator.setTranslation(-d, -d1, -d2);
            par1Tessellator.disableColor();

            for (Iterator iterator = damagedBlocks.values().iterator(); iterator.hasNext();)
            {
                DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)iterator.next();
                double d3 = (double)destroyblockprogress.getPartialBlockX() - d;
                double d4 = (double)destroyblockprogress.getPartialBlockY() - d1;
                double d5 = (double)destroyblockprogress.getPartialBlockZ() - d2;

                if (d3 * d3 + d4 * d4 + d5 * d5 > 1024D)
                {
                    iterator.remove();
                }
                else
                {
                    int i = theWorld.getBlockId(destroyblockprogress.getPartialBlockX(), destroyblockprogress.getPartialBlockY(), destroyblockprogress.getPartialBlockZ());
                    Block block = i <= 0 ? null : Block.blocksList[i];

                    if (block == null)
                    {
                        block = Block.stone;
                    }

                    globalRenderBlocks.renderBlockUsingTexture(block, destroyblockprogress.getPartialBlockX(), destroyblockprogress.getPartialBlockY(), destroyblockprogress.getPartialBlockZ(), destroyBlockIcons[destroyblockprogress.getPartialBlockDamage()]);
                }
            }

            par1Tessellator.draw();
            par1Tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glPolygonOffset(0.0F, 0.0F);
            GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDepthMask(true);
            GL11.glPopMatrix();
        }
    }

    /**
     * Draws the selection box for the player. Args: entityPlayer, rayTraceHit, i, itemStack, partialTickTime
     */
    public void drawSelectionBox(EntityPlayer par1EntityPlayer, MovingObjectPosition par2MovingObjectPosition, int par3, float par4)
    {
        if (par3 == 0 && par2MovingObjectPosition.typeOfHit == EnumMovingObjectType.TILE)
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(2.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            float f = 0.002F;
            int i = theWorld.getBlockId(par2MovingObjectPosition.blockX, par2MovingObjectPosition.blockY, par2MovingObjectPosition.blockZ);

            if (i > 0)
            {
                Block.blocksList[i].setBlockBoundsBasedOnState(theWorld, par2MovingObjectPosition.blockX, par2MovingObjectPosition.blockY, par2MovingObjectPosition.blockZ);
                double d = par1EntityPlayer.lastTickPosX + (par1EntityPlayer.posX - par1EntityPlayer.lastTickPosX) * (double)par4;
                double d1 = par1EntityPlayer.lastTickPosY + (par1EntityPlayer.posY - par1EntityPlayer.lastTickPosY) * (double)par4;
                double d2 = par1EntityPlayer.lastTickPosZ + (par1EntityPlayer.posZ - par1EntityPlayer.lastTickPosZ) * (double)par4;
                drawOutlinedBoundingBox(Block.blocksList[i].getSelectedBoundingBoxFromPool(theWorld, par2MovingObjectPosition.blockX, par2MovingObjectPosition.blockY, par2MovingObjectPosition.blockZ).expand(f, f, f).getOffsetBoundingBox(-d, -d1, -d2));
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    /**
     * Draws lines for the edges of the bounding box.
     */
    private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(3);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(3);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.draw();
        tessellator.startDrawing(1);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
        tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
        tessellator.draw();
    }

    /**
     * Marks the blocks in the given range for update
     */
    public void markBlocksForUpdate(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        int i = MathHelper.bucketInt(par1, 16);
        int j = MathHelper.bucketInt(par2, 16);
        int k = MathHelper.bucketInt(par3, 16);
        int l = MathHelper.bucketInt(par4, 16);
        int i1 = MathHelper.bucketInt(par5, 16);
        int j1 = MathHelper.bucketInt(par6, 16);

        for (int k1 = i; k1 <= l; k1++)
        {
            int l1 = k1 % renderChunksWide;

            if (l1 < 0)
            {
                l1 += renderChunksWide;
            }

            for (int i2 = j; i2 <= i1; i2++)
            {
                int j2 = i2 % renderChunksTall;

                if (j2 < 0)
                {
                    j2 += renderChunksTall;
                }

                for (int k2 = k; k2 <= j1; k2++)
                {
                    int l2 = k2 % renderChunksDeep;

                    if (l2 < 0)
                    {
                        l2 += renderChunksDeep;
                    }

                    int i3 = (l2 * renderChunksTall + j2) * renderChunksWide + l1;
                    WorldRenderer worldrenderer = worldRenderers[i3];

                    if (worldrenderer != null && !worldrenderer.needsUpdate)
                    {
                        worldRenderersToUpdate.add(worldrenderer);
                        worldrenderer.markDirty();
                    }
                }
            }
        }
    }

    /**
     * On the client, re-renders the block. On the server, sends the block to the client (which will re-render it),
     * including the tile entity description packet if applicable. Args: x, y, z
     */
    public void markBlockForUpdate(int par1, int par2, int par3)
    {
        markBlocksForUpdate(par1 - 1, par2 - 1, par3 - 1, par1 + 1, par2 + 1, par3 + 1);
    }

    /**
     * On the client, re-renders this block. On the server, does nothing. Used for lighting updates.
     */
    public void markBlockForRenderUpdate(int par1, int par2, int par3)
    {
        markBlocksForUpdate(par1 - 1, par2 - 1, par3 - 1, par1 + 1, par2 + 1, par3 + 1);
    }

    /**
     * On the client, re-renders all blocks in this range, inclusive. On the server, does nothing. Args: min x, min y,
     * min z, max x, max y, max z
     */
    public void markBlockRangeForRenderUpdate(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        markBlocksForUpdate(par1 - 1, par2 - 1, par3 - 1, par4 + 1, par5 + 1, par6 + 1);
    }

    /**
     * Checks all renderers that previously weren't in the frustum and 1/16th of those that previously were in the
     * frustum for frustum clipping Args: frustum, partialTickTime
     */
    public void clipRenderersByFrustum(ICamera par1ICamera, float par2)
    {
        for (int i = 0; i < worldRenderers.length; i++)
        {
            if (!worldRenderers[i].skipAllRenderPasses() && (!worldRenderers[i].isInFrustum || (i + frustumCheckOffset & 0xf) == 0))
            {
                worldRenderers[i].updateInFrustum(par1ICamera);
            }
        }

        frustumCheckOffset++;
    }

    /**
     * Plays the specified record. Arg: recordName, x, y, z
     */
    public void playRecord(String par1Str, int par2, int par3, int par4)
    {
        ItemRecord itemrecord = ItemRecord.getRecord(par1Str);

        if (par1Str != null && itemrecord != null)
        {
            mc.ingameGUI.setRecordPlayingMessage(itemrecord.getRecordTitle());
        }

        mc.sndManager.playStreaming(par1Str, par2, par3, par4);
    }

    /**
     * Plays the specified sound. Arg: soundName, x, y, z, volume, pitch
     */
    public void playSound(String s, double d, double d1, double d2, float f, float f1)
    {
    }

    /**
     * Plays sound to all near players except the player reference given
     */
    public void playSoundToNearExcept(EntityPlayer entityplayer, String s, double d, double d1, double d2, float f, float f1)
    {
    }

    /**
     * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
     */
    public void spawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        try
        {
            doSpawnParticle(par1Str, par2, par4, par6, par8, par10, par12);
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception while adding particle");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being added");
            crashreportcategory.addCrashSection("Name", par1Str);
            crashreportcategory.addCrashSectionCallable("Position", new CallableParticlePositionInfo(this, par2, par4, par6));
            throw new ReportedException(crashreport);
        }
    }

    /**
     * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
     */
    public EntityFX doSpawnParticle(String par1Str, double par2, double par4, double par6, double par8, double par10, double par12)
    {
        if (mc == null || mc.renderViewEntity == null || mc.effectRenderer == null)
        {
            return null;
        }

        int i = mc.gameSettings.particleSetting;

        if (i == 1 && theWorld.rand.nextInt(3) == 0)
        {
            i = 2;
        }

        double d = mc.renderViewEntity.posX - par2;
        double d1 = mc.renderViewEntity.posY - par4;
        double d2 = mc.renderViewEntity.posZ - par6;
        Object obj = null;

        if (par1Str.equals("hugeexplosion"))
        {
            mc.effectRenderer.addEffect(((EntityFX)(obj = new EntityHugeExplodeFX(theWorld, par2, par4, par6, par8, par10, par12))));
        }
        else if (par1Str.equals("largeexplode"))
        {
            mc.effectRenderer.addEffect(((EntityFX)(obj = new EntityLargeExplodeFX(renderEngine, theWorld, par2, par4, par6, par8, par10, par12))));
        }
        else if (par1Str.equals("fireworksSpark"))
        {
            mc.effectRenderer.addEffect(((EntityFX)(obj = new EntityFireworkSparkFX(theWorld, par2, par4, par6, par8, par10, par12, mc.effectRenderer))));
        }

        if (obj != null)
        {
            return ((EntityFX)(obj));
        }

        double d3 = 16D;

        if (d * d + d1 * d1 + d2 * d2 > d3 * d3)
        {
            return null;
        }

        if (i > 1)
        {
            return null;
        }

        if (par1Str.equals("bubble"))
        {
            obj = new EntityBubbleFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("suspended"))
        {
            obj = new EntitySuspendFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("depthsuspend"))
        {
            obj = new EntityAuraFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("townaura"))
        {
            obj = new EntityAuraFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("crit"))
        {
            obj = new EntityCritFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("magicCrit"))
        {
            obj = new EntityCritFX(theWorld, par2, par4, par6, par8, par10, par12);
            ((EntityFX)(obj)).setRBGColorF(((EntityFX)(obj)).getRedColorF() * 0.3F, ((EntityFX)(obj)).getGreenColorF() * 0.8F, ((EntityFX)(obj)).getBlueColorF());
            ((EntityFX)(obj)).nextTextureIndexX();
        }
        else if (par1Str.equals("smoke"))
        {
            obj = new EntitySmokeFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("mobSpell"))
        {
            obj = new EntitySpellParticleFX(theWorld, par2, par4, par6, 0.0D, 0.0D, 0.0D);
            ((EntityFX)(obj)).setRBGColorF((float)par8, (float)par10, (float)par12);
        }
        else if (par1Str.equals("mobSpellAmbient"))
        {
            obj = new EntitySpellParticleFX(theWorld, par2, par4, par6, 0.0D, 0.0D, 0.0D);
            ((EntityFX)(obj)).setAlphaF(0.15F);
            ((EntityFX)(obj)).setRBGColorF((float)par8, (float)par10, (float)par12);
        }
        else if (par1Str.equals("spell"))
        {
            obj = new EntitySpellParticleFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("instantSpell"))
        {
            obj = new EntitySpellParticleFX(theWorld, par2, par4, par6, par8, par10, par12);
            ((EntitySpellParticleFX)obj).setBaseSpellTextureIndex(144);
        }
        else if (par1Str.equals("witchMagic"))
        {
            obj = new EntitySpellParticleFX(theWorld, par2, par4, par6, par8, par10, par12);
            ((EntitySpellParticleFX)obj).setBaseSpellTextureIndex(144);
            float f = theWorld.rand.nextFloat() * 0.5F + 0.35F;
            ((EntityFX)(obj)).setRBGColorF(1.0F * f, 0.0F * f, 1.0F * f);
        }
        else if (par1Str.equals("note"))
        {
            obj = new EntityNoteFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("portal"))
        {
            obj = new EntityPortalFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("enchantmenttable"))
        {
            obj = new EntityEnchantmentTableParticleFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("explode"))
        {
            obj = new EntityExplodeFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("flame"))
        {
            obj = new EntityFlameFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("lava"))
        {
            obj = new EntityLavaFX(theWorld, par2, par4, par6);
        }
        else if (par1Str.equals("footstep"))
        {
            obj = new EntityFootStepFX(renderEngine, theWorld, par2, par4, par6);
        }
        else if (par1Str.equals("splash"))
        {
            obj = new EntitySplashFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("largesmoke"))
        {
            obj = new EntitySmokeFX(theWorld, par2, par4, par6, par8, par10, par12, 2.5F);
        }
        else if (par1Str.equals("cloud"))
        {
            obj = new EntityCloudFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("reddust"))
        {
            obj = new EntityReddustFX(theWorld, par2, par4, par6, (float)par8, (float)par10, (float)par12);
        }
        else if (par1Str.equals("snowballpoof"))
        {
            obj = new EntityBreakingFX(theWorld, par2, par4, par6, Item.snowball);
        }
        else if (par1Str.equals("dripWater"))
        {
            obj = new EntityDropParticleFX(theWorld, par2, par4, par6, Material.water);
        }
        else if (par1Str.equals("dripLava"))
        {
            obj = new EntityDropParticleFX(theWorld, par2, par4, par6, Material.lava);
        }
        else if (par1Str.equals("snowshovel"))
        {
            obj = new EntitySnowShovelFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("slime"))
        {
            obj = new EntityBreakingFX(theWorld, par2, par4, par6, Item.slimeBall);
        }
        else if (par1Str.equals("heart"))
        {
            obj = new EntityHeartFX(theWorld, par2, par4, par6, par8, par10, par12);
        }
        else if (par1Str.equals("angryVillager"))
        {
            obj = new EntityHeartFX(theWorld, par2, par4 + 0.5D, par6, par8, par10, par12);
            ((EntityFX)(obj)).setParticleTextureIndex(81);
            ((EntityFX)(obj)).setRBGColorF(1.0F, 1.0F, 1.0F);
        }
        else if (par1Str.equals("happyVillager"))
        {
            obj = new EntityAuraFX(theWorld, par2, par4, par6, par8, par10, par12);
            ((EntityFX)(obj)).setParticleTextureIndex(82);
            ((EntityFX)(obj)).setRBGColorF(1.0F, 1.0F, 1.0F);
        }
        else if (par1Str.startsWith("iconcrack_"))
        {
            String as[] = par1Str.split("_", 3);
            int j = Integer.parseInt(as[1]);

            if (as.length > 2)
            {
                int l = Integer.parseInt(as[2]);
                obj = new EntityBreakingFX(theWorld, par2, par4, par6, par8, par10, par12, Item.itemsList[j], l);
            }
            else
            {
                obj = new EntityBreakingFX(theWorld, par2, par4, par6, par8, par10, par12, Item.itemsList[j], 0);
            }
        }
        else if (par1Str.startsWith("tilecrack_"))
        {
            String as1[] = par1Str.split("_", 3);
            int k = Integer.parseInt(as1[1]);
            int i1 = Integer.parseInt(as1[2]);
            obj = (new EntityDiggingFX(theWorld, par2, par4, par6, par8, par10, par12, Block.blocksList[k], i1)).applyRenderColor(i1);
        }

        if (obj != null)
        {
            mc.effectRenderer.addEffect(((EntityFX)(obj)));
        }

        return ((EntityFX)(obj));
    }

    /**
     * Called on all IWorldAccesses when an entity is created or loaded. On client worlds, starts downloading any
     * necessary textures. On server worlds, adds the entity to the entity tracker.
     */
    public void onEntityCreate(Entity entity)
    {
    }

    /**
     * Called on all IWorldAccesses when an entity is unloaded or destroyed. On client worlds, releases any downloaded
     * textures. On server worlds, removes the entity from the entity tracker.
     */
    public void onEntityDestroy(Entity entity)
    {
    }

    /**
     * Deletes all display lists
     */
    public void deleteAllDisplayLists()
    {
        GLAllocation.deleteDisplayLists(glRenderListBase);
    }

    public void broadcastSound(int par1, int par2, int par3, int par4, int par5)
    {
        Random random = theWorld.rand;

        switch (par1)
        {
            default:
                break;
            case 1013:
            case 1018:

                if (mc.renderViewEntity != null)
                {
                    double d = (double)par2 - mc.renderViewEntity.posX;
                    double d1 = (double)par3 - mc.renderViewEntity.posY;
                    double d2 = (double)par4 - mc.renderViewEntity.posZ;
                    double d3 = Math.sqrt(d * d + d1 * d1 + d2 * d2);
                    double d4 = mc.renderViewEntity.posX;
                    double d5 = mc.renderViewEntity.posY;
                    double d6 = mc.renderViewEntity.posZ;

                    if (d3 > 0.0D)
                    {
                        d4 += (d / d3) * 2D;
                        d5 += (d1 / d3) * 2D;
                        d6 += (d2 / d3) * 2D;
                    }

                    if (par1 == 1013)
                    {
                        theWorld.playSound(d4, d5, d6, "mob.wither.spawn", 1.0F, 1.0F, false);
                    }
                    else if (par1 == 1018)
                    {
                        theWorld.playSound(d4, d5, d6, "mob.enderdragon.end", 5F, 1.0F, false);
                    }
                }

                break;
        }
    }

    /**
     * Plays a pre-canned sound effect along with potentially auxiliary data-driven one-shot behaviour (particles, etc).
     */
    public void playAuxSFX(EntityPlayer par1EntityPlayer, int par2, int par3, int par4, int par5, int par6)
    {
        Random random = theWorld.rand;

        switch (par2)
        {
            default:
                break;
            case 1001:
                theWorld.playSound(par3, par4, par5, "random.click", 1.0F, 1.2F, false);
                break;
            case 1000:
                theWorld.playSound(par3, par4, par5, "random.click", 1.0F, 1.0F, false);
                break;
            case 1002:
                theWorld.playSound(par3, par4, par5, "random.bow", 1.0F, 1.2F, false);
                break;
            case 2000:
                int i = par6 % 3 - 1;
                int j = (par6 / 3) % 3 - 1;
                double d2 = (double)par3 + (double)i * 0.59999999999999998D + 0.5D;
                double d5 = (double)par4 + 0.5D;
                double d8 = (double)par5 + (double)j * 0.59999999999999998D + 0.5D;

                for (int j1 = 0; j1 < 10; j1++)
                {
                    double d10 = random.nextDouble() * 0.20000000000000001D + 0.01D;
                    double d11 = d2 + (double)i * 0.01D + (random.nextDouble() - 0.5D) * (double)j * 0.5D;
                    double d12 = d5 + (random.nextDouble() - 0.5D) * 0.5D;
                    double d15 = d8 + (double)j * 0.01D + (random.nextDouble() - 0.5D) * (double)i * 0.5D;
                    double d18 = (double)i * d10 + random.nextGaussian() * 0.01D;
                    double d21 = -0.029999999999999999D + random.nextGaussian() * 0.01D;
                    double d23 = (double)j * d10 + random.nextGaussian() * 0.01D;
                    spawnParticle("smoke", d11, d12, d15, d18, d21, d23);
                }

                break;
            case 2003:
                double d = (double)par3 + 0.5D;
                double d3 = par4;
                double d6 = (double)par5 + 0.5D;
                String s = (new StringBuilder()).append("iconcrack_").append(Item.eyeOfEnder.itemID).toString();

                for (int k = 0; k < 8; k++)
                {
                    spawnParticle(s, d, d3, d6, random.nextGaussian() * 0.14999999999999999D, random.nextDouble() * 0.20000000000000001D, random.nextGaussian() * 0.14999999999999999D);
                }

                for (double d9 = 0.0D; d9 < (Math.PI * 2D); d9 += 0.15707963267948966D)
                {
                    spawnParticle("portal", d + Math.cos(d9) * 5D, d3 - 0.40000000000000002D, d6 + Math.sin(d9) * 5D, Math.cos(d9) * -5D, 0.0D, Math.sin(d9) * -5D);
                    spawnParticle("portal", d + Math.cos(d9) * 5D, d3 - 0.40000000000000002D, d6 + Math.sin(d9) * 5D, Math.cos(d9) * -7D, 0.0D, Math.sin(d9) * -7D);
                }

                break;
            case 2002:
                double d1 = par3;
                double d4 = par4;
                double d7 = par5;
                String s1 = (new StringBuilder()).append("iconcrack_").append(Item.potion.itemID).append("_").append(par6).toString();

                for (int l = 0; l < 8; l++)
                {
                    spawnParticle(s1, d1, d4, d7, random.nextGaussian() * 0.14999999999999999D, random.nextDouble() * 0.20000000000000001D, random.nextGaussian() * 0.14999999999999999D);
                }

                int i1 = Item.potion.getColorFromDamage(par6);
                float f = (float)(i1 >> 16 & 0xff) / 255F;
                float f1 = (float)(i1 >> 8 & 0xff) / 255F;
                float f2 = (float)(i1 >> 0 & 0xff) / 255F;
                String s2 = "spell";

                if (Item.potion.isEffectInstant(par6))
                {
                    s2 = "instantSpell";
                }

                for (int k1 = 0; k1 < 100; k1++)
                {
                    double d13 = random.nextDouble() * 4D;
                    double d16 = random.nextDouble() * Math.PI * 2D;
                    double d19 = Math.cos(d16) * d13;
                    double d22 = 0.01D + random.nextDouble() * 0.5D;
                    double d24 = Math.sin(d16) * d13;
                    EntityFX entityfx = doSpawnParticle(s2, d1 + d19 * 0.10000000000000001D, d4 + 0.29999999999999999D, d7 + d24 * 0.10000000000000001D, d19, d22, d24);

                    if (entityfx != null)
                    {
                        float f3 = 0.75F + random.nextFloat() * 0.25F;
                        entityfx.setRBGColorF(f * f3, f1 * f3, f2 * f3);
                        entityfx.multiplyVelocity((float)d13);
                    }
                }

                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "random.glass", 1.0F, theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 2001:
                int l1 = par6 & 0xfff;

                if (l1 > 0)
                {
                    Block block = Block.blocksList[l1];
                    mc.sndManager.playSound(block.stepSound.getBreakSound(), (float)par3 + 0.5F, (float)par4 + 0.5F, (float)par5 + 0.5F, (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                }

                mc.effectRenderer.addBlockDestroyEffects(par3, par4, par5, par6 & 0xfff, par6 >> 12 & 0xff);
                break;
            case 2004:

                for (int i2 = 0; i2 < 20; i2++)
                {
                    double d14 = (double)par3 + 0.5D + ((double)theWorld.rand.nextFloat() - 0.5D) * 2D;
                    double d17 = (double)par4 + 0.5D + ((double)theWorld.rand.nextFloat() - 0.5D) * 2D;
                    double d20 = (double)par5 + 0.5D + ((double)theWorld.rand.nextFloat() - 0.5D) * 2D;
                    theWorld.spawnParticle("smoke", d14, d17, d20, 0.0D, 0.0D, 0.0D);
                    theWorld.spawnParticle("flame", d14, d17, d20, 0.0D, 0.0D, 0.0D);
                }

                break;
            case 2005:
                ItemDye.func_96603_a(theWorld, par3, par4, par5, par6);
                break;
            case 1003:

                if (Math.random() < 0.5D)
                {
                    theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "random.door_open", 1.0F, theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                }
                else
                {
                    theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "random.door_close", 1.0F, theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                }

                break;
            case 1004:
                theWorld.playSound((float)par3 + 0.5F, (float)par4 + 0.5F, (float)par5 + 0.5F, "random.fizz", 0.5F, 2.6F + (random.nextFloat() - random.nextFloat()) * 0.8F, false);
                break;
            case 1020:
                theWorld.playSound((float)par3 + 0.5F, (float)par4 + 0.5F, (float)par5 + 0.5F, "random.anvil_break", 1.0F, theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1021:
                theWorld.playSound((float)par3 + 0.5F, (float)par4 + 0.5F, (float)par5 + 0.5F, "random.anvil_use", 1.0F, theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1022:
                theWorld.playSound((float)par3 + 0.5F, (float)par4 + 0.5F, (float)par5 + 0.5F, "random.anvil_land", 0.3F, theWorld.rand.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1005:

                if (Item.itemsList[par6] instanceof ItemRecord)
                {
                    theWorld.playRecord(((ItemRecord)Item.itemsList[par6]).recordName, par3, par4, par5);
                }
                else
                {
                    theWorld.playRecord(null, par3, par4, par5);
                }

                break;
            case 1007:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.ghast.charge", 10F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1008:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.ghast.fireball", 10F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1010:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.wood", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1012:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.woodbreak", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1011:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.metal", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1009:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.ghast.fireball", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1014:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.wither.shoot", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1016:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.infect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1017:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.zombie.unfect", 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1015:
                theWorld.playSound((double)par3 + 0.5D, (double)par4 + 0.5D, (double)par5 + 0.5D, "mob.bat.takeoff", 0.05F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                break;
        }
    }

    /**
     * Starts (or continues) destroying a block with given ID at the given coordinates for the given partially destroyed
     * value
     */
    public void destroyBlockPartially(int par1, int par2, int par3, int par4, int par5)
    {
        if (par5 < 0 || par5 >= 10)
        {
            damagedBlocks.remove(Integer.valueOf(par1));
        }
        else
        {
            DestroyBlockProgress destroyblockprogress = (DestroyBlockProgress)damagedBlocks.get(Integer.valueOf(par1));

            if (destroyblockprogress == null || destroyblockprogress.getPartialBlockX() != par2 || destroyblockprogress.getPartialBlockY() != par3 || destroyblockprogress.getPartialBlockZ() != par4)
            {
                destroyblockprogress = new DestroyBlockProgress(par1, par2, par3, par4);
                damagedBlocks.put(Integer.valueOf(par1), destroyblockprogress);
            }

            destroyblockprogress.setPartialBlockDamage(par5);
            destroyblockprogress.setCloudUpdateTick(cloudTickCounter);
        }
    }

    public void registerDestroyBlockIcons(IconRegister par1IconRegister)
    {
        destroyBlockIcons = new Icon[10];

        for (int i = 0; i < destroyBlockIcons.length; i++)
        {
            destroyBlockIcons[i] = par1IconRegister.registerIcon((new StringBuilder()).append("destroy_stage_").append(i).toString());
        }
    }

    public void updateAllRenderers(boolean force)
    {
        for(int i = 0; i < worldRenderers.length; i++)
        {
            if((worldRenderers[i].isChunkLit || force) && !worldRenderers[i].needsUpdate)
            {
                worldRenderersToUpdate.add(worldRenderers[i]);
                worldRenderers[i].markDirty();
            }
        }

    }
}
