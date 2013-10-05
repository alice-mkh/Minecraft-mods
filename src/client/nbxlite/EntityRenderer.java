package net.minecraft.src;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Random;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.Project;
import net.minecraft.src.ssp.ChunkProviderLoadOrGenerate;

public class EntityRenderer
{
    public static boolean sunriseFog = true;
    public static boolean sunriseAtNorth = false;
    public static boolean thirdPersonBobbing = true;
    public static boolean thirdPersonShoulder = false;
    public static boolean oldFaceView = false;
    public static boolean classicLight = false;
    public static boolean voidFog = true;
    public static boolean oldFog = false;
    public static boolean snow = false;
    public static boolean bounds = false;
    public static boolean oldNetherFog = false;
    public static boolean oldSnow = false;
    public static boolean fixClouds = true;

    public float[] lightTable;

    private static final ResourceLocation locationRainPng = new ResourceLocation("textures/environment/rain.png");
    private static final ResourceLocation locationSnowPng = new ResourceLocation("textures/environment/snow.png");
    public static boolean anaglyphEnable;

    /** Anaglyph field (0=R, 1=GB) */
    public static int anaglyphField;

    /** A reference to the Minecraft object. */
    private Minecraft mc;
    private float farPlaneDistance;
    public ItemRenderer itemRenderer;

    /** Entity renderer update count */
    private int rendererUpdateCount;

    /** Pointed entity */
    private Entity pointedEntity;
    private MouseFilter mouseFilterXAxis;
    private MouseFilter mouseFilterYAxis;

    /** Mouse filter dummy 1 */
    private MouseFilter mouseFilterDummy1;

    /** Mouse filter dummy 2 */
    private MouseFilter mouseFilterDummy2;

    /** Mouse filter dummy 3 */
    private MouseFilter mouseFilterDummy3;

    /** Mouse filter dummy 4 */
    private MouseFilter mouseFilterDummy4;
    private float thirdPersonDistance;

    /** Third person distance temp */
    private float thirdPersonDistanceTemp;
    private float debugCamYaw;
    private float prevDebugCamYaw;
    private float debugCamPitch;
    private float prevDebugCamPitch;

    /** Smooth cam yaw */
    private float smoothCamYaw;

    /** Smooth cam pitch */
    private float smoothCamPitch;

    /** Smooth cam filter X */
    private float smoothCamFilterX;

    /** Smooth cam filter Y */
    private float smoothCamFilterY;

    /** Smooth cam partial ticks */
    private float smoothCamPartialTicks;
    private float debugCamFOV;
    private float prevDebugCamFOV;
    private float camRoll;
    private float prevCamRoll;

    /**
     * The texture id of the blocklight/skylight texture used for lighting effects
     */
    private final DynamicTexture lightmapTexture = new DynamicTexture(16, 16);
    private final int lightmapColors[];
    private final ResourceLocation locationLightMap;

    /** FOV modifier hand */
    private float fovModifierHand;

    /** FOV modifier hand prev */
    private float fovModifierHandPrev;

    /** FOV multiplier temp */
    private float fovMultiplierTemp;
    private float field_82831_U;
    private float field_82832_V;

    /** Cloud fog mode */
    private boolean cloudFog;
    private double cameraZoom;
    private double cameraYaw;
    private double cameraPitch;

    /** Previous frame time in milliseconds */
    private long prevFrameTime;

    /** End time of last render (ns) */
    private long renderEndNanoTime;

    /**
     * Is set, updateCameraAndRender() calls updateLightmap(); set by updateTorchFlicker()
     */
    private boolean lightmapUpdateNeeded;

    /** Torch flicker X */
    float torchFlickerX;

    /** Torch flicker DX */
    float torchFlickerDX;

    /** Torch flicker Y */
    float torchFlickerY;

    /** Torch flicker DY */
    float torchFlickerDY;
    private Random random;

    /** Rain sound counter */
    private int rainSoundCounter;
    float rainXCoords[];
    float rainYCoords[];

    /** Fog color buffer */
    FloatBuffer fogColorBuffer;

    /** red component of the fog color */
    float fogColorRed;

    /** green component of the fog color */
    float fogColorGreen;

    /** blue component of the fog color */
    float fogColorBlue;

    /** Fog color 2 */
    private float fogColor2;

    /** Fog color 1 */
    private float fogColor1;

    /**
     * Debug view direction (0=OFF, 1=Front, 2=Right, 3=Back, 4=Left, 5=TiltLeft, 6=TiltRight)
     */
    public int debugViewDirection;

    public EntityRenderer(Minecraft par1Minecraft)
    {
        mouseFilterXAxis = new MouseFilter();
        mouseFilterYAxis = new MouseFilter();
        mouseFilterDummy1 = new MouseFilter();
        mouseFilterDummy2 = new MouseFilter();
        mouseFilterDummy3 = new MouseFilter();
        mouseFilterDummy4 = new MouseFilter();
        thirdPersonDistance = 4F;
        thirdPersonDistanceTemp = 4F;
        cameraZoom = 1.0D;
        prevFrameTime = Minecraft.getSystemTime();
        random = new Random();
        fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        mc = par1Minecraft;
        itemRenderer = new ItemRenderer(par1Minecraft);
        locationLightMap = par1Minecraft.getTextureManager().getDynamicTextureLocation("lightMap", lightmapTexture);
        lightmapColors = lightmapTexture.getTextureData();
        calculateLightTable();
    }

    private void setFog(){
        if (GLContext.getCapabilities().GL_NV_fog_distance){
            if(!oldFog){
                GL11.glFogi(org.lwjgl.opengl.NVFogDistance.GL_FOG_DISTANCE_MODE_NV, org.lwjgl.opengl.NVFogDistance.GL_EYE_RADIAL_NV);
            }else{
                GL11.glFogi(org.lwjgl.opengl.NVFogDistance.GL_FOG_DISTANCE_MODE_NV, org.lwjgl.opengl.NVFogDistance.GL_EYE_PLANE_ABSOLUTE_NV);
            }
        }
    }

    /**
     * Updates the entity renderer
     */
    public void updateRenderer()
    {
        updateFovModifierHand();
        updateTorchFlicker();
        fogColor2 = fogColor1;
        thirdPersonDistanceTemp = thirdPersonDistance;
        prevDebugCamYaw = debugCamYaw;
        prevDebugCamPitch = debugCamPitch;
        prevDebugCamFOV = debugCamFOV;
        prevCamRoll = camRoll;

        if (mc.gameSettings.smoothCamera)
        {
            float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f2 = f * f * f * 8F;
            smoothCamFilterX = mouseFilterXAxis.smooth(smoothCamYaw, 0.05F * f2);
            smoothCamFilterY = mouseFilterYAxis.smooth(smoothCamPitch, 0.05F * f2);
            smoothCamPartialTicks = 0.0F;
            smoothCamYaw = 0.0F;
            smoothCamPitch = 0.0F;
        }

        if (mc.renderViewEntity == null)
        {
            mc.renderViewEntity = mc.thePlayer;
        }

        float f1 = mc.theWorld.getLightBrightness(MathHelper.floor_double(mc.renderViewEntity.posX), MathHelper.floor_double(mc.renderViewEntity.posY), MathHelper.floor_double(mc.renderViewEntity.posZ));
        float f3 = (float)(3 - mc.gameSettings.renderDistance) / 3F;
        float f4 = f1 * (1.0F - f3) + f3;
        fogColor1 += (f4 - fogColor1) * 0.1F;
        rendererUpdateCount++;
        itemRenderer.updateEquippedItem();
        addRainParticles();
        field_82832_V = field_82831_U;

        if (BossStatus.field_82825_d)
        {
            field_82831_U += 0.05F;

            if (field_82831_U > 1.0F)
            {
                field_82831_U = 1.0F;
            }

            BossStatus.field_82825_d = false;
        }
        else if (field_82831_U > 0.0F)
        {
            field_82831_U -= 0.0125F;
        }
    }

    /**
     * Finds what block or object the mouse is over at the specified partial tick time. Args: partialTickTime
     */
    public void getMouseOver(float par1)
    {
        if (mc.renderViewEntity == null)
        {
            return;
        }

        if (mc.theWorld == null)
        {
            return;
        }

        mc.pointedEntityLiving = null;
        double d = mc.playerController.getBlockReachDistance();
        mc.objectMouseOver = mc.renderViewEntity.rayTrace(d, par1);
        double d1 = d;
        Vec3 vec3 = mc.renderViewEntity.getPosition(par1);

        if (mc.playerController.extendedReach())
        {
            d1 = d = 6D;
        }
        else
        {
            if (d1 > 3D)
            {
                d1 = 3D;
            }

            d = d1;
        }

        if (mc.objectMouseOver != null)
        {
            d1 = mc.objectMouseOver.hitVec.distanceTo(vec3);
        }

        Vec3 vec3_1 = mc.renderViewEntity.getLook(par1);
        Vec3 vec3_2 = vec3.addVector(vec3_1.xCoord * d, vec3_1.yCoord * d, vec3_1.zCoord * d);
        pointedEntity = null;
        float f = 1.0F;
        List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(vec3_1.xCoord * d, vec3_1.yCoord * d, vec3_1.zCoord * d).expand(f, f, f));
        double d2 = d1;

        for (int i = 0; i < list.size(); i++)
        {
            Entity entity = (Entity)list.get(i);

            if (!entity.canBeCollidedWith())
            {
                continue;
            }

            float f1 = entity.getCollisionBorderSize();
            AxisAlignedBB axisalignedbb = entity.boundingBox.expand(f1, f1, f1);
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec3_2);

            if (axisalignedbb.isVecInside(vec3))
            {
                if (0.0D < d2 || d2 == 0.0D)
                {
                    pointedEntity = entity;
                    d2 = 0.0D;
                }

                continue;
            }

            if (movingobjectposition == null)
            {
                continue;
            }

            double d3 = vec3.distanceTo(movingobjectposition.hitVec);

            if (d3 >= d2 && d2 != 0.0D)
            {
                continue;
            }

            if (entity == mc.renderViewEntity.ridingEntity)
            {
                if (d2 == 0.0D)
                {
                    pointedEntity = entity;
                }
            }
            else
            {
                pointedEntity = entity;
                d2 = d3;
            }
        }

        if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
        {
            mc.objectMouseOver = new MovingObjectPosition(pointedEntity);

            if (pointedEntity instanceof EntityLivingBase)
            {
                mc.pointedEntityLiving = (EntityLivingBase)pointedEntity;
            }
        }
    }

    /**
     * Update FOV modifier hand
     */
    private void updateFovModifierHand()
    {
        EntityPlayerSP entityplayersp = (EntityPlayerSP)mc.renderViewEntity;
        fovMultiplierTemp = entityplayersp.getFOVMultiplier();
        fovModifierHandPrev = fovModifierHand;
        fovModifierHand += (fovMultiplierTemp - fovModifierHand) * 0.5F;

        if (fovModifierHand > 1.5F)
        {
            fovModifierHand = 1.5F;
        }

        if (fovModifierHand < 0.1F)
        {
            fovModifierHand = 0.1F;
        }
    }

    /**
     * Changes the field of view of the player depending on if they are underwater or not
     */
    private float getFOVModifier(float par1, boolean par2)
    {
        if (debugViewDirection > 0)
        {
            return 90F;
        }

        EntityPlayer entityplayer = (EntityPlayer)mc.renderViewEntity;
        float f = 70F;

        if (par2)
        {
            f += mc.gameSettings.fovSetting * 40F;
            f *= fovModifierHandPrev + (fovModifierHand - fovModifierHandPrev) * par1;
        }

        if (entityplayer.getHealth() <= 0.0F)
        {
            float f1 = (float)entityplayer.deathTime + par1;
            f /= (1.0F - 500F / (f1 + 500F)) * 2.0F + 1.0F;
        }

        int i = ActiveRenderInfo.getBlockIdAtEntityViewpoint(mc.theWorld, entityplayer, par1);

        if (i != 0 && Block.blocksList[i].blockMaterial == Material.water)
        {
            f = (f * 60F) / 70F;
        }

        return f + prevDebugCamFOV + (debugCamFOV - prevDebugCamFOV) * par1;
    }

    private void hurtCameraEffect(float par1)
    {
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        float f = (float)entitylivingbase.hurtTime - par1;

        if (entitylivingbase.getHealth() <= 0.0F)
        {
            float f1 = (float)entitylivingbase.deathTime + par1;
            GL11.glRotatef(40F - 8000F / (f1 + 200F), 0.0F, 0.0F, 1.0F);
        }

        if (f < 0.0F)
        {
            return;
        }
        else
        {
            f /= entitylivingbase.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entitylivingbase.attackedAtYaw;
            GL11.glRotatef(-f2, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f * 14F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(f2, 0.0F, 1.0F, 0.0F);
            return;
        }
    }

    /**
     * Setups all the GL settings for view bobbing. Args: partialTickTime
     */
    private void setupViewBobbing(float par1)
    {
        if (!(mc.renderViewEntity instanceof EntityPlayer) || (!thirdPersonBobbing && mc.gameSettings.thirdPersonView > 0))
        {
            return;
        }
        else
        {
            EntityPlayer entityplayer = (EntityPlayer)mc.renderViewEntity;
            float f = entityplayer.distanceWalkedModified - entityplayer.prevDistanceWalkedModified;
            float f1 = -(entityplayer.distanceWalkedModified + f * par1);
            float f2 = entityplayer.prevCameraYaw + (entityplayer.cameraYaw - entityplayer.prevCameraYaw) * par1;
            float f3 = entityplayer.prevCameraPitch + (entityplayer.cameraPitch - entityplayer.prevCameraPitch) * par1;
            GL11.glTranslatef(MathHelper.sin(f1 * (float)Math.PI) * f2 * 0.5F, -Math.abs(MathHelper.cos(f1 * (float)Math.PI) * f2), 0.0F);
            GL11.glRotatef(MathHelper.sin(f1 * (float)Math.PI) * f2 * 3F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(Math.abs(MathHelper.cos(f1 * (float)Math.PI - 0.2F) * f2) * 5F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
            return;
        }
    }

    /**
     * sets up player's eye (or camera in third person mode)
     */
    private void orientCamera(float par1)
    {
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        float f = entitylivingbase.yOffset - 1.62F;
        double d = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double)par1;
        double d1 = (entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double)par1) - (double)f;
        double d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double)par1;
        GL11.glRotatef(prevCamRoll + (camRoll - prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);

        if (entitylivingbase.isPlayerSleeping())
        {
            f = (float)((double)f + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);

            if (!mc.gameSettings.debugCamEnable)
            {
                int i = mc.theWorld.getBlockId(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ));

                if (i == Block.bed.blockID)
                {
                    int j = mc.theWorld.getBlockMetadata(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posY), MathHelper.floor_double(entitylivingbase.posZ));
                    int k = j & 3;
                    GL11.glRotatef(k * 90, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(entitylivingbase.prevRotationYaw + (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * par1 + 180F, 0.0F, -1F, 0.0F);
                GL11.glRotatef(entitylivingbase.prevRotationPitch + (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * par1, -1F, 0.0F, 0.0F);
            }
        }
        else if (mc.gameSettings.thirdPersonView > 0)
        {
            double d3 = thirdPersonDistanceTemp + (thirdPersonDistance - thirdPersonDistanceTemp) * par1;

            if (mc.gameSettings.debugCamEnable)
            {
                float f1 = prevDebugCamYaw + (debugCamYaw - prevDebugCamYaw) * par1;
                float f3 = prevDebugCamPitch + (debugCamPitch - prevDebugCamPitch) * par1;
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f3, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(f1, 0.0F, 1.0F, 0.0F);
            }
            else
            {
                float f2 = entitylivingbase.rotationYaw - (thirdPersonShoulder ? 10F : 0F);
                float f4 = entitylivingbase.rotationPitch + (thirdPersonShoulder ? 2F : 0F);

                if (mc.gameSettings.thirdPersonView == 2)
                {
                    f4 += 180F;
                    if (oldFaceView){
                        d3 += 2.0D;
                    }
                }

                double d4 = (double)(-MathHelper.sin((f2 / 180F) * (float)Math.PI) * MathHelper.cos((f4 / 180F) * (float)Math.PI)) * d3;
                double d5 = (double)(MathHelper.cos((f2 / 180F) * (float)Math.PI) * MathHelper.cos((f4 / 180F) * (float)Math.PI)) * d3;
                double d6 = (double)(-MathHelper.sin((f4 / 180F) * (float)Math.PI)) * d3;

                for (int l = 0; l < 8; l++)
                {
                    float f5 = (l & 1) * 2 - 1;
                    float f6 = (l >> 1 & 1) * 2 - 1;
                    float f7 = (l >> 2 & 1) * 2 - 1;
                    f5 *= 0.1F;
                    f6 *= 0.1F;
                    f7 *= 0.1F;
                    MovingObjectPosition movingobjectposition = mc.theWorld.clip(mc.theWorld.getWorldVec3Pool().getVecFromPool(d + (double)f5, d1 + (double)f6, d2 + (double)f7), mc.theWorld.getWorldVec3Pool().getVecFromPool((d - d4) + (double)f5 + (double)f7, (d1 - d6) + (double)f6, (d2 - d5) + (double)f7));

                    if (movingobjectposition == null)
                    {
                        continue;
                    }

                    double d7 = movingobjectposition.hitVec.distanceTo(mc.theWorld.getWorldVec3Pool().getVecFromPool(d, d1, d2));

                    if (d7 < d3)
                    {
                        d3 = d7;
                    }
                }

                if (mc.gameSettings.thirdPersonView == 2)
                {
                    GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(entitylivingbase.rotationPitch - f4, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(entitylivingbase.rotationYaw - f2, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f2 - entitylivingbase.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f4 - entitylivingbase.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!mc.gameSettings.debugCamEnable)
        {
            GL11.glRotatef(entitylivingbase.prevRotationPitch + (entitylivingbase.rotationPitch - entitylivingbase.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(entitylivingbase.prevRotationYaw + (entitylivingbase.rotationYaw - entitylivingbase.prevRotationYaw) * par1 + 180F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, f, 0.0F);
        d = entitylivingbase.prevPosX + (entitylivingbase.posX - entitylivingbase.prevPosX) * (double)par1;
        d1 = (entitylivingbase.prevPosY + (entitylivingbase.posY - entitylivingbase.prevPosY) * (double)par1) - (double)f;
        d2 = entitylivingbase.prevPosZ + (entitylivingbase.posZ - entitylivingbase.prevPosZ) * (double)par1;
        cloudFog = mc.renderGlobal.hasCloudFog(d, d1, d2, par1);
    }

    /**
     * sets up projection, view effects, camera position/rotation
     */
    private void setupCameraTransform(float par1, int par2)
    {
        farPlaneDistance = 256 >> mc.gameSettings.renderDistance;
        if (oldFog){
            farPlaneDistance = (512 >> (mc.gameSettings.renderDistance << 1));
        }
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f = 0.07F;

        if (mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(-(par2 * 2 - 1)) * f, 0.0F, 0.0F);
        }

        if (cameraZoom != 1.0D)
        {
            GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
            GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
        }

        Project.gluPerspective(getFOVModifier(par1, true), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);

        if (mc.playerController.enableEverythingIsScrewedUpMode())
        {
            float f1 = 0.6666667F;
            GL11.glScalef(1.0F, f1, 1.0F);
        }

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        if (mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        hurtCameraEffect(par1);

        if (mc.gameSettings.viewBobbing)
        {
            setupViewBobbing(par1);
        }

        float f2 = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * par1;

        if (f2 > 0.0F)
        {
            int i = 20;

            if (mc.thePlayer.isPotionActive(Potion.confusion))
            {
                i = 7;
            }

            float f3 = 5F / (f2 * f2 + 5F) - f2 * 0.04F;
            f3 *= f3;
            GL11.glRotatef(((float)rendererUpdateCount + par1) * (float)i, 0.0F, 1.0F, 1.0F);
            GL11.glScalef(1.0F / f3, 1.0F, 1.0F);
            GL11.glRotatef(-((float)rendererUpdateCount + par1) * (float)i, 0.0F, 1.0F, 1.0F);
        }

        orientCamera(par1);

        if (debugViewDirection > 0)
        {
            int j = debugViewDirection - 1;

            if (j == 1)
            {
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            }

            if (j == 2)
            {
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            }

            if (j == 3)
            {
                GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
            }

            if (j == 4)
            {
                GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            }

            if (j == 5)
            {
                GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
            }
        }
    }

    /**
     * Render player hand
     */
    private void renderHand(float par1, int par2)
    {
        if (debugViewDirection > 0)
        {
            return;
        }

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float f = 0.07F;

        if (mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(-(par2 * 2 - 1)) * f, 0.0F, 0.0F);
        }

        if (cameraZoom != 1.0D)
        {
            GL11.glTranslatef((float)cameraYaw, (float)(-cameraPitch), 0.0F);
            GL11.glScaled(cameraZoom, cameraZoom, 1.0D);
        }

        Project.gluPerspective(getFOVModifier(par1, false), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);

        if (mc.playerController.enableEverythingIsScrewedUpMode())
        {
            float f1 = 0.6666667F;
            GL11.glScalef(1.0F, f1, 1.0F);
        }

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        if (mc.gameSettings.anaglyph)
        {
            GL11.glTranslatef((float)(par2 * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GL11.glPushMatrix();
        hurtCameraEffect(par1);

        if (mc.gameSettings.viewBobbing)
        {
            setupViewBobbing(par1);
        }

        if (mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping() && !mc.gameSettings.hideGUI && !mc.playerController.enableEverythingIsScrewedUpMode())
        {
            enableLightmap(par1);
            itemRenderer.renderItemInFirstPerson(par1);
            disableLightmap(par1);
        }

        GL11.glPopMatrix();

        if (mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping())
        {
            itemRenderer.renderOverlays(par1);
            hurtCameraEffect(par1);
        }

        if (mc.gameSettings.viewBobbing)
        {
            setupViewBobbing(par1);
        }
    }

    /**
     * Disable secondary texture unit used by lightmap
     */
    public void disableLightmap(double par1)
    {
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Enable lightmap in secondary texture unit
     */
    public void enableLightmap(double par1)
    {
        if (Minecraft.oldlighting){
            return;
        }
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        float f = 0.00390625F;
        GL11.glScalef(f, f, f);
        GL11.glTranslatef(8F, 8F, 8F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        mc.getTextureManager().bindTexture(locationLightMap);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    /**
     * Recompute a random value that is applied to block color in updateLightmap()
     */
    private void updateTorchFlicker()
    {
        torchFlickerDX += (Math.random() - Math.random()) * Math.random() * Math.random();
        torchFlickerDY += (Math.random() - Math.random()) * Math.random() * Math.random();
        torchFlickerDX *= 0.90000000000000002D;
        torchFlickerDY *= 0.90000000000000002D;
        torchFlickerX += (torchFlickerDX - torchFlickerX) * 1.0F;
        torchFlickerY += (torchFlickerDY - torchFlickerY) * 1.0F;
        lightmapUpdateNeeded = true;
    }

    public void calculateLightTable(){
        lightTable = new float[16];
        float f = 0.05F;
        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    private void updateLightmap_classicStyle(float par1)
    {

        int lightTintRed = 255;
        int lightTintGreen = 255;
        int lightTintBlue = 255;
        World world = mc.theWorld;
        float[] lightBrightnessTable = null;
        if (world.provider.dimensionId == 0 || world.provider.dimensionId == 1){
            lightBrightnessTable = lightTable;
        }else{
            lightBrightnessTable = world.provider.lightBrightnessTable;
        }
        int i = world.calculateSkylightSubtracted(1.0F);
        int j = 0;
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                float f = lightBrightnessTable[l];
                int i1 = k - i;
                if(i1 < 0)
                {
                    i1 = 0;
                }
                float f1 = lightBrightnessTable[i1];
                if (world.provider.dimensionId == 1)
                {
                    f1 = 0.22F + f1 * 0.75F;
                }
                if (mc.thePlayer.isPotionActive(Potion.nightVision))
                {
                    float brightness = getNightVisionBrightness(mc.thePlayer, par1);
                    float brightnessshift = brightness * 0.7F;
                    float blockadjust = ((1.0F - f - 0.5F) * (brightnessshift * f)) + (0.5F * brightness);
                    float skyadjust = ((1.0F - f1 - 0.5F) * (brightnessshift * f1)) + (0.5F * brightness);
                    f += blockadjust;
                    f1 += skyadjust;
                }
                int j1 = (int)(f * 255F);
                int k1 = (int)(f1 * 255F);
                float f2 = 1.0F - (float)lightTintRed / 255F;
                float f3 = 1.0F - (float)lightTintGreen / 255F;
                float f4 = 1.0F - (float)lightTintBlue / 255F;
                float f5 = (float)(15 - k) / 15F;
                f2 *= f5;
                f3 *= f5;
                f4 *= f5;
                f2 = 1.0F - f2;
                f3 = 1.0F - f3;
                f4 = 1.0F - f4;
                j1 = (int)((float)j1 * (mc.gameSettings.gammaSetting + 1.0F));
                if(j1 > 255)
                {
                    j1 = 255;
                }
                k1 = (int)((float)k1 * (mc.gameSettings.gammaSetting + 1.0F));
                if(k1 > 255)
                {
                    k1 = 255;
                }
                char c = '\377';
                if(f > f1)
                {
                    lightmapColors[j] = c << 24 | (int)((float)j1 * f2) << 16 | (int)((float)j1 * f3) << 8 | (int)((float)j1 * f4);
                } else
                {
                    lightmapColors[j] = c << 24 | (int)((float)k1 * f2) << 16 | (int)((float)k1 * f3) << 8 | (int)((float)k1 * f4);
                }
                j++;
            }

        }

        lightmapTexture.updateDynamicTexture();
        lightmapUpdateNeeded = false;
    }

    private void updateLightmap(float par1)
    {
        if (Minecraft.oldlighting){
            for (int i = 0; i < 256; i++){
                lightmapColors[i] = 0xffffffff;
            }
            lightmapTexture.updateDynamicTexture();
            lightmapUpdateNeeded = false;
            return;
        }
        WorldClient worldclient = mc.theWorld;

        if (worldclient == null)
        {
            return;
        }
        if(classicLight){
            updateLightmap_classicStyle(par1);
            return;
        }

        for (int i = 0; i < 256; i++)
        {
            float f = worldclient.getSunBrightness(1.0F) * 0.95F + 0.05F;
            float f1 = ((World)(worldclient)).provider.lightBrightnessTable[i / 16] * f;
            float f2 = ((World)(worldclient)).provider.lightBrightnessTable[i % 16] * (torchFlickerX * 0.1F + 1.5F);

            if (((World)(worldclient)).lastLightningBolt > 0)
            {
                f1 = ((World)(worldclient)).provider.lightBrightnessTable[i / 16];
            }

            float f3 = f1 * (worldclient.getSunBrightness(1.0F) * 0.65F + 0.35F);
            float f4 = f1 * (worldclient.getSunBrightness(1.0F) * 0.65F + 0.35F);
            float f5 = f1;
            float f6 = f2;
            float f7 = f2 * ((f2 * 0.6F + 0.4F) * 0.6F + 0.4F);
            float f8 = f2 * (f2 * f2 * 0.6F + 0.4F);
            float f9 = f3 + f6;
            float f10 = f4 + f7;
            float f11 = f5 + f8;
            f9 = f9 * 0.96F + 0.03F;
            f10 = f10 * 0.96F + 0.03F;
            f11 = f11 * 0.96F + 0.03F;

            if (field_82831_U > 0.0F)
            {
                float f12 = field_82832_V + (field_82831_U - field_82832_V) * par1;
                f9 = f9 * (1.0F - f12) + f9 * 0.7F * f12;
                f10 = f10 * (1.0F - f12) + f10 * 0.6F * f12;
                f11 = f11 * (1.0F - f12) + f11 * 0.6F * f12;
            }

            if (((World)(worldclient)).provider.dimensionId == 1)
            {
                f9 = 0.22F + f6 * 0.75F;
                f10 = 0.28F + f7 * 0.75F;
                f11 = 0.25F + f8 * 0.75F;
            }

            if (mc.thePlayer.isPotionActive(Potion.nightVision))
            {
                float f13 = getNightVisionBrightness(mc.thePlayer, par1);
                float f15 = 1.0F / f9;

                if (f15 > 1.0F / f10)
                {
                    f15 = 1.0F / f10;
                }

                if (f15 > 1.0F / f11)
                {
                    f15 = 1.0F / f11;
                }

                f9 = f9 * (1.0F - f13) + f9 * f15 * f13;
                f10 = f10 * (1.0F - f13) + f10 * f15 * f13;
                f11 = f11 * (1.0F - f13) + f11 * f15 * f13;
            }

            if (f9 > 1.0F)
            {
                f9 = 1.0F;
            }

            if (f10 > 1.0F)
            {
                f10 = 1.0F;
            }

            if (f11 > 1.0F)
            {
                f11 = 1.0F;
            }

            float f14 = mc.gameSettings.gammaSetting;
            float f16 = 1.0F - f9;
            float f17 = 1.0F - f10;
            float f18 = 1.0F - f11;
            f16 = 1.0F - f16 * f16 * f16 * f16;
            f17 = 1.0F - f17 * f17 * f17 * f17;
            f18 = 1.0F - f18 * f18 * f18 * f18;
            f9 = f9 * (1.0F - f14) + f16 * f14;
            f10 = f10 * (1.0F - f14) + f17 * f14;
            f11 = f11 * (1.0F - f14) + f18 * f14;
            f9 = f9 * 0.96F + 0.03F;
            f10 = f10 * 0.96F + 0.03F;
            f11 = f11 * 0.96F + 0.03F;

            if (f9 > 1.0F)
            {
                f9 = 1.0F;
            }

            if (f10 > 1.0F)
            {
                f10 = 1.0F;
            }

            if (f11 > 1.0F)
            {
                f11 = 1.0F;
            }

            if (f9 < 0.0F)
            {
                f9 = 0.0F;
            }

            if (f10 < 0.0F)
            {
                f10 = 0.0F;
            }

            if (f11 < 0.0F)
            {
                f11 = 0.0F;
            }

            char c = '\377';
            int j = (int)(f9 * 255F);
            int k = (int)(f10 * 255F);
            int l = (int)(f11 * 255F);
            lightmapColors[i] = c << 24 | j << 16 | k << 8 | l;
        }

        lightmapTexture.updateDynamicTexture();
        lightmapUpdateNeeded = false;
    }

    /**
     * Gets the night vision brightness
     */
    private float getNightVisionBrightness(EntityPlayer par1EntityPlayer, float par2)
    {
        int i = par1EntityPlayer.getActivePotionEffect(Potion.nightVision).getDuration();

        if (i > 200)
        {
            return 1.0F;
        }
        else
        {
            return 0.7F + MathHelper.sin(((float)i - par2) * (float)Math.PI * 0.2F) * 0.3F;
        }
    }

    /**
     * Will update any inputs that effect the camera angle (mouse) and then render the world and GUI
     */
    public void updateCameraAndRender(float par1)
    {
        mc.mcProfiler.startSection("lightTex");

        if (lightmapUpdateNeeded)
        {
            updateLightmap(par1);
        }

        mc.mcProfiler.endSection();
        boolean flag = Display.isActive();

        if (flag || !mc.gameSettings.pauseOnLostFocus || mc.gameSettings.touchscreen && Mouse.isButtonDown(1))
        {
            prevFrameTime = Minecraft.getSystemTime();
        }
        else if (Minecraft.getSystemTime() - prevFrameTime > 500L)
        {
            mc.displayInGameMenu();
        }

        mc.mcProfiler.startSection("mouse");

        if (mc.inGameHasFocus && flag)
        {
            mc.mouseHelper.mouseXYChange();
            float f = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
            float f1 = f * f * f * 8F;
            float f2 = (float)mc.mouseHelper.deltaX * f1;
            float f3 = (float)mc.mouseHelper.deltaY * f1;
            int l = 1;

            if (mc.gameSettings.invertMouse)
            {
                l = -1;
            }

            if (mc.gameSettings.smoothCamera)
            {
                smoothCamYaw += f2;
                smoothCamPitch += f3;
                float f4 = par1 - smoothCamPartialTicks;
                smoothCamPartialTicks = par1;
                f2 = smoothCamFilterX * f4;
                f3 = smoothCamFilterY * f4;
                mc.thePlayer.setAngles(f2, f3 * (float)l);
            }
            else
            {
                mc.thePlayer.setAngles(f2, f3 * (float)l);
            }
        }

        mc.mcProfiler.endSection();

        if (mc.skipRenderWorld)
        {
            return;
        }

        anaglyphEnable = mc.gameSettings.anaglyph;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        int k = (Mouse.getX() * i) / mc.displayWidth;
        int i1 = j - (Mouse.getY() * j) / mc.displayHeight - 1;
        int j1 = performanceToFps(mc.gameSettings.limitFramerate);

        if (mc.theWorld != null)
        {
            mc.mcProfiler.startSection("level");

            if (mc.gameSettings.limitFramerate == 0)
            {
                renderWorld(par1, 0L);
            }
            else
            {
                renderWorld(par1, renderEndNanoTime + (long)(0x3b9aca00 / j1));
            }

            renderEndNanoTime = System.nanoTime();
            mc.mcProfiler.endStartSection("gui");

            if (!mc.gameSettings.hideGUI || mc.currentScreen != null)
            {
                mc.ingameGUI.renderGameOverlay(par1, mc.currentScreen != null, k, i1);
            }

            mc.mcProfiler.endSection();
        }
        else
        {
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            setupOverlayRendering();
            renderEndNanoTime = System.nanoTime();
        }

        if (mc.currentScreen != null)
        {
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);

            try
            {
                mc.currentScreen.drawScreen2(k, i1, par1);
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering screen");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Screen render details");
                crashreportcategory.addCrashSectionCallable("Screen name", new CallableScreenName(this));
                crashreportcategory.addCrashSectionCallable("Mouse location", new CallableMouseLocation(this, k, i1));
                crashreportcategory.addCrashSectionCallable("Screen size", new CallableScreenSize(this, scaledresolution));
                throw new ReportedException(crashreport);
            }
        }
    }

    public void renderWorld(float par1, long par2)
    {
        mc.mcProfiler.startSection("lightTex");

        if (lightmapUpdateNeeded)
        {
            updateLightmap(par1);
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        if (mc.renderViewEntity == null)
        {
            mc.renderViewEntity = mc.thePlayer;
        }

        mc.mcProfiler.endStartSection("pick");
        getMouseOver(par1);
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        RenderGlobal renderglobal = mc.renderGlobal;
        EffectRenderer effectrenderer = mc.effectRenderer;
        double d = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double)par1;
        double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)par1;
        double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double)par1;
        mc.mcProfiler.endStartSection("center");
        if (mc.enableSP){
            IChunkProvider ichunkprovider = mc.theWorld.getChunkProvider();
            if (ichunkprovider instanceof ChunkProviderLoadOrGenerate){
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
                int j = MathHelper.floor_float((int)d) >> 4;
                int k = MathHelper.floor_float((int)d2) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(j, k);
            }
        }

        for (int i = 0; i < 2; i++)
        {
            if (mc.gameSettings.anaglyph)
            {
                anaglyphField = i;

                if (anaglyphField == 0)
                {
                    GL11.glColorMask(false, true, true, false);
                }
                else
                {
                    GL11.glColorMask(true, false, false, false);
                }
            }

            mc.mcProfiler.endStartSection("clear");
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            updateFogColor(par1);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnable(GL11.GL_CULL_FACE);
            mc.mcProfiler.endStartSection("camera");
            setupCameraTransform(par1, i);
            ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
            mc.mcProfiler.endStartSection("frustrum");
            ClippingHelperImpl.getInstance();

            if (mc.gameSettings.renderDistance < 2)
            {
                setupFog(-1, par1);
                mc.mcProfiler.endStartSection("sky");
                renderglobal.renderSky(par1);
            }

            GL11.glEnable(GL11.GL_FOG);
            setupFog(1, par1);

            if (mc.gameSettings.ambientOcclusion != 0)
            {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }

            mc.mcProfiler.endStartSection("culling");
            Frustrum frustrum = new Frustrum();
            frustrum.setPosition(d, d1, d2);
            mc.renderGlobal.clipRenderersByFrustum(frustrum, par1);

            if (i == 0)
            {
                mc.mcProfiler.endStartSection("updatechunks");
                long l;

                do
                {
                    if (mc.renderGlobal.updateRenderers(entitylivingbase, false) || par2 == 0L)
                    {
                        break;
                    }

                    l = par2 - System.nanoTime();
                }
                while (l >= 0L && l <= 0x3b9aca00L);
            }

            if (entitylivingbase.posY < 128D && !fixClouds)
            {
                renderCloudsCheck(renderglobal, par1);
            }

            mc.mcProfiler.endStartSection("prepareterrain");
            setupFog(0, par1);
            GL11.glEnable(GL11.GL_FOG);
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            RenderHelper.disableStandardItemLighting();
            mc.mcProfiler.endStartSection("terrain");
            renderglobal.sortAndRender(entitylivingbase, 0, par1);
            GL11.glShadeModel(GL11.GL_FLAT);

            if (debugViewDirection == 0)
            {
                RenderHelper.enableStandardItemLighting();
                mc.mcProfiler.endStartSection("entities");
                renderglobal.renderEntities(entitylivingbase.getPosition(par1), frustrum, par1);
                enableLightmap(par1);
                mc.mcProfiler.endStartSection("litParticles");
                effectrenderer.renderLitParticles(entitylivingbase, par1);
                RenderHelper.disableStandardItemLighting();
                setupFog(0, par1);
                mc.mcProfiler.endStartSection("particles");
                effectrenderer.renderParticles(entitylivingbase, par1);
                disableLightmap(par1);

                if (mc.objectMouseOver != null && entitylivingbase.isInsideOfMaterial(Material.water) && (entitylivingbase instanceof EntityPlayer) && !mc.gameSettings.hideGUI)
                {
                    EntityPlayer entityplayer = (EntityPlayer)entitylivingbase;
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    mc.mcProfiler.endStartSection("outline");
                    renderglobal.drawSelectionBox(entityplayer, mc.objectMouseOver, 0, par1);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }
            if (bounds && mc.theWorld.provider.dimensionId == 0){
                net.minecraft.src.nbxlite.RenderBounds.renderBounds(mc, par1, true);
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(true);
            setupFog(0, par1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

            if (mc.gameSettings.fancyGraphics)
            {
                mc.mcProfiler.endStartSection("water");

                if (mc.gameSettings.ambientOcclusion != 0)
                {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }

                GL11.glColorMask(false, false, false, false);
                int j = renderglobal.sortAndRender(entitylivingbase, 1, par1);

                if (mc.gameSettings.anaglyph)
                {
                    if (anaglyphField == 0)
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

                if (j > 0)
                {
                    renderglobal.renderAllRenderLists(1, par1);
                }

                GL11.glShadeModel(GL11.GL_FLAT);
            }
            else
            {
                mc.mcProfiler.endStartSection("water");
                renderglobal.sortAndRender(entitylivingbase, 1, par1);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);

            if (cameraZoom == 1.0D && (entitylivingbase instanceof EntityPlayer) && !mc.gameSettings.hideGUI && mc.objectMouseOver != null && !entitylivingbase.isInsideOfMaterial(Material.water))
            {
                EntityPlayer entityplayer1 = (EntityPlayer)entitylivingbase;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                mc.mcProfiler.endStartSection("outline");
                renderglobal.drawSelectionBox(entityplayer1, mc.objectMouseOver, 0, par1);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            mc.mcProfiler.endStartSection("destroyProgress");
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            renderglobal.drawBlockDamageTexture(Tessellator.instance, (EntityPlayer)entitylivingbase, par1);
            GL11.glDisable(GL11.GL_BLEND);
            mc.mcProfiler.endStartSection("weather");
            renderRainSnow(par1);
            GL11.glDisable(GL11.GL_FOG);

            if (entitylivingbase.posY >= 128D || fixClouds)
            {
                renderCloudsCheck(renderglobal, par1);
            }

            mc.mcProfiler.endStartSection("hand");

            if (cameraZoom == 1.0D)
            {
                GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
                renderHand(par1, i);
            }

            if (!mc.gameSettings.anaglyph)
            {
                mc.mcProfiler.endSection();
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
        mc.mcProfiler.endSection();
    }

    /**
     * Render clouds if enabled
     */
    private void renderCloudsCheck(RenderGlobal par1RenderGlobal, float par2)
    {
        if (mc.gameSettings.shouldRenderClouds())
        {
            mc.mcProfiler.endStartSection("clouds");
            GL11.glPushMatrix();
            setupFog(0, par2);
            GL11.glEnable(GL11.GL_FOG);
            par1RenderGlobal.renderClouds(par2);
            GL11.glDisable(GL11.GL_FOG);
            setupFog(1, par2);
            GL11.glPopMatrix();
        }
    }

    private void addRainParticles()
    {
        float f = mc.theWorld.getRainStrength(1.0F);

        if (!mc.gameSettings.fancyGraphics)
        {
            f /= 2.0F;
        }

        if (f == 0.0F)
        {
            return;
        }

        random.setSeed((long)rendererUpdateCount * 0x12a7ce5fL);
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        WorldClient worldclient = mc.theWorld;
        int i = MathHelper.floor_double(entitylivingbase.posX);
        int j = MathHelper.floor_double(entitylivingbase.posY);
        int k = MathHelper.floor_double(entitylivingbase.posZ);
        byte byte0 = 10;
        double d = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int l = 0;
        int i1 = (int)(100F * f * f);

        if (mc.gameSettings.particleSetting == 1)
        {
            i1 >>= 1;
        }
        else if (mc.gameSettings.particleSetting == 2)
        {
            i1 = 0;
        }

        for (int j1 = 0; j1 < i1; j1++)
        {
            int k1 = (i + random.nextInt(byte0)) - random.nextInt(byte0);
            int l1 = (k + random.nextInt(byte0)) - random.nextInt(byte0);
            int i2 = worldclient.getPrecipitationHeight(k1, l1);
            int j2 = worldclient.getBlockId(k1, i2 - 1, l1);
            BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(k1, l1);

            if (i2 > j + byte0 || i2 < j - byte0 || !biomegenbase.canSpawnLightningBolt() || biomegenbase.getFloatTemperature() < 0.2F)
            {
                continue;
            }

            float f1 = random.nextFloat();
            float f2 = random.nextFloat();

            if (j2 <= 0)
            {
                continue;
            }

            if (Block.blocksList[j2].blockMaterial == Material.lava)
            {
                mc.effectRenderer.addEffect(new EntitySmokeFX(worldclient, (float)k1 + f1, (double)((float)i2 + 0.1F) - Block.blocksList[j2].getBlockBoundsMinY(), (float)l1 + f2, 0.0D, 0.0D, 0.0D));
                continue;
            }

            if (random.nextInt(++l) == 0)
            {
                d = (float)k1 + f1;
                d1 = (double)((float)i2 + 0.1F) - Block.blocksList[j2].getBlockBoundsMinY();
                d2 = (float)l1 + f2;
            }

            mc.effectRenderer.addEffect(new EntityRainFX(worldclient, (float)k1 + f1, (double)((float)i2 + 0.1F) - Block.blocksList[j2].getBlockBoundsMinY(), (float)l1 + f2));
        }

        if (l > 0 && random.nextInt(3) < rainSoundCounter++)
        {
            rainSoundCounter = 0;

            if (d1 > entitylivingbase.posY + 1.0D && worldclient.getPrecipitationHeight(MathHelper.floor_double(entitylivingbase.posX), MathHelper.floor_double(entitylivingbase.posZ)) > MathHelper.floor_double(entitylivingbase.posY))
            {
                mc.theWorld.playSound(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F, false);
            }
            else
            {
                mc.theWorld.playSound(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F, false);
            }
        }
    }

    protected void renderSnow(float f)
    {
        if(mc.thePlayer.dimension!=0)
        {
            return;
        }
        if (!Minecraft.oldlighting){
            enableLightmap(f);
        }
        if(rainXCoords == null)
        {
            rainXCoords = new float[1024 /*GL_FRONT_LEFT*/];
            rainYCoords = new float[1024 /*GL_FRONT_LEFT*/];
            for(int i = 0; i < 32; i++)
            {
                for(int j = 0; j < 32; j++)
                {
                    float f1 = j - 16;
                    float f2 = i - 16;
                    float f3 = MathHelper.sqrt_float(f1 * f1 + f2 * f2);
                    rainXCoords[i << 5 | j] = -f2 / f3;
                    rainYCoords[i << 5 | j] = f1 / f3;
                }

            }

        }
        EntityLivingBase entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int k = MathHelper.floor_double(entityliving.posX);
        int l = MathHelper.floor_double(entityliving.posY);
        int i1 = MathHelper.floor_double(entityliving.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.01F);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/environment/snow.png"));
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
        int j1 = MathHelper.floor_double(d1);
        int k1 = 5;
        if(mc.gameSettings.fancyGraphics)
        {
            k1 = 10;
        }
        boolean flag = false;
        byte byte0 = -1;
        float f4 = (float)rendererUpdateCount + f;
        if(mc.gameSettings.fancyGraphics)
        {
            k1 = 10;
        }
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        flag = false;
        for(int l1 = i1 - k1; l1 <= i1 + k1; l1++)
        {
            for(int i2 = k - k1; i2 <= k + k1; i2++)
            {
                int j2 = ((l1 - i1) + 16) * 32 + ((i2 - k) + 16);
                float f5 = rainXCoords[j2] * 0.5F;
                float f6 = rainYCoords[j2] * 0.5F;
                int k2 = world.getPrecipitationHeight(i2, l1);
                int l2 = l - k1;
                int i3 = l + k1;
                if(l2 < k2)
                {
                    l2 = k2;
                }
                if(i3 < k2)
                {
                    i3 = k2;
                }
                float f7 = 1.0F;
                int j3 = k2;
                if(j3 < j1)
                {
                    j3 = j1;
                }
                if(l2 == i3)
                {
                    continue;
                }
                random.setSeed(i2 * i2 * 3121 /*GL_RGBA_MODE*/ + i2 * 0x2b24abb ^ l1 * l1 * 0x66397 + l1 * 13761);
                if(byte0 != 1)
                {
                    if(byte0 >= 0)
                    {
                        tessellator.draw();
                    }
                    byte0 = 1;
                    mc.getTextureManager().bindTexture(new ResourceLocation("textures/environment/snow.png"));
                    tessellator.startDrawingQuads();
                }
                float f8 = ((float)(rendererUpdateCount & 0x1ff) + f) / 512F;
                float f9 = random.nextFloat() + f4 * 0.01F * (float)random.nextGaussian();
                float f10 = random.nextFloat() + f4 * (float)random.nextGaussian() * 0.001F;
                double d3 = (double)((float)i2 + 0.5F) - entityliving.posX;
                double d4 = (double)((float)l1 + 0.5F) - entityliving.posZ;
                float f11 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)k1;
                float f12 = Minecraft.oldlighting ? world.getLightBrightness(i2, j3, l1) : 1.0F;
                if (!Minecraft.oldlighting){
                    tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i2, j3, l1, 0) * 3 + 0xf000f0) / 4);
                }
                tessellator.setColorRGBA_F(f12, f12, f12, ((1.0F - f11 * f11) * 0.3F + 0.5F) * 0.7F);
                tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, l2, (double)((float)l1 - f6) + 0.5D, 0.0F * f7 + f9, ((float)l2 * f7) / 4F + f8 * f7 + f10);
                tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, l2, (double)((float)l1 + f6) + 0.5D, 1.0F * f7 + f9, ((float)l2 * f7) / 4F + f8 * f7 + f10);
                tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, i3, (double)((float)l1 + f6) + 0.5D, 1.0F * f7 + f9, ((float)i3 * f7) / 4F + f8 * f7 + f10);
                tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, i3, (double)((float)l1 - f6) + 0.5D, 0.0F * f7 + f9, ((float)i3 * f7) / 4F + f8 * f7 + f10);
                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            }

        }

        if(byte0 >= 0)
        {
            tessellator.draw();
        }
        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glAlphaFunc(516, 0.1F);
        if (!Minecraft.oldlighting){
            disableLightmap(f);
        }
    }

    private void renderSnowOld(float par1)
    {
        if (!Minecraft.oldlighting){
            enableLightmap(par1);
        }
        EntityLivingBase entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int var4 = MathHelper.floor_double(entityliving.posX);
        int var5 = MathHelper.floor_double(entityliving.posY);
        int var6 = MathHelper.floor_double(entityliving.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        mc.getTextureManager().bindTexture(new ResourceLocation("textures/environment/snow.png"));
        double var8 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)par1;
        double var10 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1;
        double var12 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)par1;
        byte var14 = 5;
        if(mc.gameSettings.fancyGraphics)
        {
            var14 = 10;
        }

        for (int var15 = var4 - var14; var15 <= var4 + var14; ++var15)
        {
            for (int var16 = var6 - var14; var16 <= var6 + var14; ++var16)
            {
                int var17 = world.getPrecipitationHeight(var15, var16);

                if (var17 < 0)
                {
                    var17 = 0;
                }

                int var18 = var5 - var14;
                int var19 = var5 + var14;

                if (var18 < var17)
                {
                    var18 = var17;
                }

                if (var19 < var17)
                {
                    var19 = var17;
                }

                float var20 = 2.0F;

                if (var18 != var19)
                {
                    random.setSeed((long)(var15 * var15 * 3121 + var15 * 45238971 + var16 * var16 * 418711 + var16 * 13761));
                    float var21 = (float)rendererUpdateCount + par1;
                    float var22 = ((float)(rendererUpdateCount & 511) + par1) / 512.0F;
                    float var23 = random.nextFloat() + var21 * 0.01F * (float)random.nextGaussian();
                    float var24 = random.nextFloat() + var21 * (float)random.nextGaussian() * 0.001F;
                    double var25 = (double)((float)var15 + 0.5F) - entityliving.posX;
                    double var27 = (double)((float)var16 + 0.5F) - entityliving.posZ;
                    float var29 = MathHelper.sqrt_double(var25 * var25 + var27 * var27) / (float)var14;
                    tessellator.startDrawingQuads();
                    float var30 = Minecraft.oldlighting ? world.getLightBrightness(var15, 256, var16) : 1.0F;
                    tessellator.setColorRGBA_F(var30, var30, var30, (1.0F - var29 * var29) * 0.7F);
                    tessellator.setTranslation(-var8 * 1.0D, -var10 * 1.0D, -var12 * 1.0D);
                    tessellator.addVertexWithUV((double)(var15 + 0), (double)var18, (double)(var16 + 0), (double)(0.0F * var20 + var23), (double)((float)var18 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 1), (double)var18, (double)(var16 + 1), (double)(1.0F * var20 + var23), (double)((float)var18 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 1), (double)var19, (double)(var16 + 1), (double)(1.0F * var20 + var23), (double)((float)var19 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 0), (double)var19, (double)(var16 + 0), (double)(0.0F * var20 + var23), (double)((float)var19 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 0), (double)var18, (double)(var16 + 1), (double)(0.0F * var20 + var23), (double)((float)var18 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 1), (double)var18, (double)(var16 + 0), (double)(1.0F * var20 + var23), (double)((float)var18 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 1), (double)var19, (double)(var16 + 0), (double)(1.0F * var20 + var23), (double)((float)var19 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.addVertexWithUV((double)(var15 + 0), (double)var19, (double)(var16 + 1), (double)(0.0F * var20 + var23), (double)((float)var19 * var20 / 8.0F + var22 * var20 + var24));
                    tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                }
            }
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        if (!Minecraft.oldlighting){
            disableLightmap(par1);
        }
    }

    /**
     * Render rain and snow
     */
    protected void renderRainSnow(float par1)
    {
        if (snow){
            if (oldSnow){
                renderSnowOld(par1);
            }else{
                renderSnow(par1);
            }
        }
        float f = mc.theWorld.getRainStrength(par1);

        if (f <= 0.0F)
        {
            return;
        }

        if (!Minecraft.oldlighting){
            enableLightmap(par1);
        }

        if (rainXCoords == null)
        {
            rainXCoords = new float[1024];
            rainYCoords = new float[1024];

            for (int i = 0; i < 32; i++)
            {
                for (int j = 0; j < 32; j++)
                {
                    float f1 = j - 16;
                    float f2 = i - 16;
                    float f3 = MathHelper.sqrt_float(f1 * f1 + f2 * f2);
                    rainXCoords[i << 5 | j] = -f2 / f3;
                    rainYCoords[i << 5 | j] = f1 / f3;
                }
            }
        }

        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        WorldClient worldclient = mc.theWorld;
        int k = MathHelper.floor_double(entitylivingbase.posX);
        int l = MathHelper.floor_double(entitylivingbase.posY);
        int i1 = MathHelper.floor_double(entitylivingbase.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
        mc.getTextureManager().bindTexture(locationSnowPng);
        double d = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double)par1;
        double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)par1;
        double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double)par1;
        int j1 = MathHelper.floor_double(d1);
        int k1 = 5;

        if (mc.gameSettings.fancyGraphics)
        {
            k1 = 10;
        }

        boolean flag = false;
        byte byte0 = -1;
        float f4 = (float)rendererUpdateCount + par1;

        if (mc.gameSettings.fancyGraphics)
        {
            k1 = 10;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        flag = false;

        for (int l1 = i1 - k1; l1 <= i1 + k1; l1++)
        {
            for (int i2 = k - k1; i2 <= k + k1; i2++)
            {
                int j2 = ((l1 - i1) + 16) * 32 + ((i2 - k) + 16);
                float f5 = rainXCoords[j2] * 0.5F;
                float f6 = rainYCoords[j2] * 0.5F;
                BiomeGenBase biomegenbase = worldclient.getBiomeGenForCoords(i2, l1);

                if (!biomegenbase.canSpawnLightningBolt() && !biomegenbase.getEnableSnow())
                {
                    continue;
                }

                int k2 = worldclient.getPrecipitationHeight(i2, l1);
                int l2 = l - k1;
                int i3 = l + k1;

                if (l2 < k2)
                {
                    l2 = k2;
                }

                if (i3 < k2)
                {
                    i3 = k2;
                }

                float f7 = 1.0F;
                int j3 = k2;

                if (j3 < j1)
                {
                    j3 = j1;
                }

                if (l2 == i3)
                {
                    continue;
                }

                random.setSeed(i2 * i2 * 3121 + i2 * 0x2b24abb ^ l1 * l1 * 0x66397 + l1 * 13761);
                float f8 = biomegenbase.getFloatTemperature();

                if (worldclient.getWorldChunkManager().getTemperatureAtHeight(f8, k2) >= 0.15F)
                {
                    if (byte0 != 0)
                    {
                        if (byte0 >= 0)
                        {
                            tessellator.draw();
                        }

                        byte0 = 0;
                        mc.getTextureManager().bindTexture(locationRainPng);
                        tessellator.startDrawingQuads();
                    }

                    float f9 = (((float)(rendererUpdateCount + i2 * i2 * 3121 + i2 * 0x2b24abb + l1 * l1 * 0x66397 + l1 * 13761 & 0x1f) + par1) / 32F) * (3F + random.nextFloat());
                    double d3 = (double)((float)i2 + 0.5F) - entitylivingbase.posX;
                    double d4 = (double)((float)l1 + 0.5F) - entitylivingbase.posZ;
                    float f13 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)k1;
                    float f14 = Minecraft.oldlighting ? worldclient.getLightBrightness(i2, j3, l1) : 1.0F;
                    if (!Minecraft.oldlighting){
                        tessellator.setBrightness(worldclient.getLightBrightnessForSkyBlocks(i2, j3, l1, 0));
                    }
                    tessellator.setColorRGBA_F(f14, f14, f14, ((1.0F - f13 * f13) * 0.5F + 0.5F) * f);
                    tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                    tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, l2, (double)((float)l1 - f6) + 0.5D, 0.0F * f7, ((float)l2 * f7) / 4F + f9 * f7);
                    tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, l2, (double)((float)l1 + f6) + 0.5D, 1.0F * f7, ((float)l2 * f7) / 4F + f9 * f7);
                    tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, i3, (double)((float)l1 + f6) + 0.5D, 1.0F * f7, ((float)i3 * f7) / 4F + f9 * f7);
                    tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, i3, (double)((float)l1 - f6) + 0.5D, 0.0F * f7, ((float)i3 * f7) / 4F + f9 * f7);
                    tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                    continue;
                }

                if (byte0 != 1)
                {
                    if (byte0 >= 0)
                    {
                        tessellator.draw();
                    }

                    byte0 = 1;
                    mc.getTextureManager().bindTexture(new ResourceLocation("textures/environment/snow.png"));
                    tessellator.startDrawingQuads();
                }

                float f10 = ((float)(rendererUpdateCount & 0x1ff) + par1) / 512F;
                float f11 = random.nextFloat() + f4 * 0.01F * (float)random.nextGaussian();
                float f12 = random.nextFloat() + f4 * (float)random.nextGaussian() * 0.001F;
                double d5 = (double)((float)i2 + 0.5F) - entitylivingbase.posX;
                double d6 = (double)((float)l1 + 0.5F) - entitylivingbase.posZ;
                float f15 = MathHelper.sqrt_double(d5 * d5 + d6 * d6) / (float)k1;
                float f16 = Minecraft.oldlighting ? worldclient.getLightBrightness(i2, j3, l1) : 1.0F;
                if (!Minecraft.oldlighting){
                    tessellator.setBrightness((worldclient.getLightBrightnessForSkyBlocks(i2, j3, l1, 0) * 3 + 0xf000f0) / 4);
                }
                tessellator.setColorRGBA_F(f16, f16, f16, ((1.0F - f15 * f15) * 0.3F + 0.5F) * f);
                tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, l2, (double)((float)l1 - f6) + 0.5D, 0.0F * f7 + f11, ((float)l2 * f7) / 4F + f10 * f7 + f12);
                tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, l2, (double)((float)l1 + f6) + 0.5D, 1.0F * f7 + f11, ((float)l2 * f7) / 4F + f10 * f7 + f12);
                tessellator.addVertexWithUV((double)((float)i2 + f5) + 0.5D, i3, (double)((float)l1 + f6) + 0.5D, 1.0F * f7 + f11, ((float)i3 * f7) / 4F + f10 * f7 + f12);
                tessellator.addVertexWithUV((double)((float)i2 - f5) + 0.5D, i3, (double)((float)l1 - f6) + 0.5D, 0.0F * f7 + f11, ((float)i3 * f7) / 4F + f10 * f7 + f12);
                tessellator.setTranslation(0.0D, 0.0D, 0.0D);
            }
        }

        if (byte0 >= 0)
        {
            tessellator.draw();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        if (!Minecraft.oldlighting){
            disableLightmap(par1);
        }
    }

    /**
     * Setup orthogonal projection for rendering GUI screen overlays
     */
    public void setupOverlayRendering()
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
    }

    /**
     * calculates fog and calls glClearColor
     */
    private void updateFogColor(float par1)
    {
        WorldClient worldclient = mc.theWorld;
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        float f = 1.0F / (float)(4 - mc.gameSettings.renderDistance);
        f = 1.0F - (float)Math.pow(f, 0.25D);
        Vec3 vec3 = worldclient.getSkyColor(mc.renderViewEntity, par1);
        float f1 = (float)vec3.xCoord;
        float f2 = (float)vec3.yCoord;
        float f3 = (float)vec3.zCoord;
        Vec3 vec3_1 = worldclient.getFogColor(par1);
        fogColorRed = (float)vec3_1.xCoord;
        fogColorGreen = (float)vec3_1.yCoord;
        fogColorBlue = (float)vec3_1.zCoord;

        if (mc.gameSettings.renderDistance < 2 && sunriseFog)
        {
            Vec3 vec3_2 = MathHelper.sin(worldclient.getCelestialAngleRadians(par1)) <= 0.0F ? worldclient.getWorldVec3Pool().getVecFromPool(1.0D, 0.0D, 0.0D) : worldclient.getWorldVec3Pool().getVecFromPool(-1D, 0.0D, 0.0D);
            if (sunriseAtNorth){
                vec3_2 = MathHelper.sin(worldclient.getCelestialAngleRadians(par1)) <= 0.0F ? worldclient.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, -1D) : worldclient.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 1.0D);
            }
            float f5 = (float)entitylivingbase.getLook(par1).dotProduct(vec3_2);

            if (f5 < 0.0F)
            {
                f5 = 0.0F;
            }

            if (f5 > 0.0F)
            {
                float af[] = ((World)(worldclient)).provider.calcSunriseSunsetColors(worldclient.getCelestialAngle(par1), par1);

                if (af != null)
                {
                    f5 *= af[3];
                    fogColorRed = fogColorRed * (1.0F - f5) + af[0] * f5;
                    fogColorGreen = fogColorGreen * (1.0F - f5) + af[1] * f5;
                    fogColorBlue = fogColorBlue * (1.0F - f5) + af[2] * f5;
                }
            }
        }

        fogColorRed += (f1 - fogColorRed) * f;
        fogColorGreen += (f2 - fogColorGreen) * f;
        fogColorBlue += (f3 - fogColorBlue) * f;
        float f4 = worldclient.getRainStrength(par1);

        if (f4 > 0.0F)
        {
            float f6 = 1.0F - f4 * 0.5F;
            float f8 = 1.0F - f4 * 0.4F;
            fogColorRed *= f6;
            fogColorGreen *= f6;
            fogColorBlue *= f8;
        }

        float f7 = worldclient.getWeightedThunderStrength(par1);

        if (f7 > 0.0F)
        {
            float f9 = 1.0F - f7 * 0.5F;
            fogColorRed *= f9;
            fogColorGreen *= f9;
            fogColorBlue *= f9;
        }

        int i = ActiveRenderInfo.getBlockIdAtEntityViewpoint(mc.theWorld, entitylivingbase, par1);

        if (cloudFog)
        {
            Vec3 vec3_3 = worldclient.getCloudColour(par1);
            fogColorRed = (float)vec3_3.xCoord;
            fogColorGreen = (float)vec3_3.yCoord;
            fogColorBlue = (float)vec3_3.zCoord;
        }
        else if (i != 0 && Block.blocksList[i].blockMaterial == Material.water)
        {
            float f10 = (float)EnchantmentHelper.getRespiration(entitylivingbase) * 0.2F;
            fogColorRed = 0.02F + f10;
            fogColorGreen = 0.02F + f10;
            fogColorBlue = 0.2F + f10;
        }
        else if (i != 0 && Block.blocksList[i].blockMaterial == Material.lava)
        {
            fogColorRed = 0.6F;
            fogColorGreen = 0.1F;
            fogColorBlue = 0.0F;
        }

        float f11 = fogColor2 + (fogColor1 - fogColor2) * par1;
        fogColorRed *= f11;
        fogColorGreen *= f11;
        fogColorBlue *= f11;
        double d = (entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)par1) * ((World)(worldclient)).provider.getVoidFogYFactor();

        if (entitylivingbase.isPotionActive(Potion.blindness))
        {
            int j = entitylivingbase.getActivePotionEffect(Potion.blindness).getDuration();

            if (j < 20)
            {
                d *= 1.0F - (float)j / 20F;
            }
            else
            {
                d = 0.0D;
            }
        }

        if (d < 1.0D && voidFog)
        {
            if (d < 0.0D)
            {
                d = 0.0D;
            }

            d *= d;
            fogColorRed *= d;
            fogColorGreen *= d;
            fogColorBlue *= d;
        }

        if (field_82831_U > 0.0F)
        {
            float f12 = field_82832_V + (field_82831_U - field_82832_V) * par1;
            fogColorRed = fogColorRed * (1.0F - f12) + fogColorRed * 0.7F * f12;
            fogColorGreen = fogColorGreen * (1.0F - f12) + fogColorGreen * 0.6F * f12;
            fogColorBlue = fogColorBlue * (1.0F - f12) + fogColorBlue * 0.6F * f12;
        }

        if (entitylivingbase.isPotionActive(Potion.nightVision))
        {
            float f13 = getNightVisionBrightness(mc.thePlayer, par1);
            float f15 = 1.0F / fogColorRed;

            if (f15 > 1.0F / fogColorGreen)
            {
                f15 = 1.0F / fogColorGreen;
            }

            if (f15 > 1.0F / fogColorBlue)
            {
                f15 = 1.0F / fogColorBlue;
            }

            fogColorRed = fogColorRed * (1.0F - f13) + fogColorRed * f15 * f13;
            fogColorGreen = fogColorGreen * (1.0F - f13) + fogColorGreen * f15 * f13;
            fogColorBlue = fogColorBlue * (1.0F - f13) + fogColorBlue * f15 * f13;
        }

        if (mc.gameSettings.anaglyph)
        {
            float f14 = (fogColorRed * 30F + fogColorGreen * 59F + fogColorBlue * 11F) / 100F;
            float f16 = (fogColorRed * 30F + fogColorGreen * 70F) / 100F;
            float f17 = (fogColorRed * 30F + fogColorBlue * 70F) / 100F;
            fogColorRed = f14;
            fogColorGreen = f16;
            fogColorBlue = f17;
        }

        GL11.glClearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
    }

    /**
     * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts at 0 and goes to 80% of far plane
     * distance and is used for sky rendering.
     */
    private void setupFog(int par1, float par2)
    {
        EntityLivingBase entitylivingbase = mc.renderViewEntity;
        boolean flag = false;

        if (entitylivingbase instanceof EntityPlayer)
        {
            flag = ((EntityPlayer)entitylivingbase).capabilities.isCreativeMode;
        }

        if (par1 == 999)
        {
            GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, 8F);

            setFog();

            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            return;
        }

        GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(fogColorRed, fogColorGreen, fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = ActiveRenderInfo.getBlockIdAtEntityViewpoint(mc.theWorld, entitylivingbase, par2);

        if (entitylivingbase.isPotionActive(Potion.blindness))
        {
            float f = 5F;
            int j = entitylivingbase.getActivePotionEffect(Potion.blindness).getDuration();

            if (j < 20)
            {
                f = 5F + (farPlaneDistance - 5F) * (1.0F - (float)j / 20F);
            }

            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

            if (par1 < 0)
            {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                GL11.glFogf(GL11.GL_FOG_END, f * 0.8F);
            }
            else
            {
                GL11.glFogf(GL11.GL_FOG_START, f * 0.25F);
                GL11.glFogf(GL11.GL_FOG_END, f);
            }

            setFog();
        }
        else if (cloudFog)
        {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
        }
        else if (i > 0 && Block.blocksList[i].blockMaterial == Material.water)
        {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

            if (entitylivingbase.isPotionActive(Potion.waterBreathing))
            {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.05F);
            }
            else
            {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F - (float)EnchantmentHelper.getRespiration(entitylivingbase) * 0.03F);
            }
        }
        else if (i > 0 && Block.blocksList[i].blockMaterial == Material.lava)
        {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
        }
        else
        {
            float f1 = farPlaneDistance;

            if (mc.theWorld.provider.getWorldHasVoidParticles() && !flag)
            {
                double d = (double)((entitylivingbase.getBrightnessForRender(par2) & 0xf00000) >> 20) / 16D + (entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)par2 + 4D) / 32D;

                if (d < 1.0D && voidFog)
                {
                    if (d < 0.0D)
                    {
                        d = 0.0D;
                    }

                    d *= d;
                    float f2 = 100F * (float)d;

                    if (f2 < 5F)
                    {
                        f2 = 5F;
                    }

                    if (f1 > f2)
                    {
                        f1 = f2;
                    }
                }
            }

            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

            if (par1 < 0)
            {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                GL11.glFogf(GL11.GL_FOG_END, f1 * 0.8F);
            }
            else
            {
                GL11.glFogf(GL11.GL_FOG_START, f1 * 0.25F);
                GL11.glFogf(GL11.GL_FOG_END, f1);
            }

            setFog();

            if (oldNetherFog && mc.theWorld.provider.isHellWorld)
            {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            }else if (mc.theWorld.provider.doesXZShowFog((int)entitylivingbase.posX, (int)entitylivingbase.posZ))
            {
                GL11.glFogf(GL11.GL_FOG_START, f1 * 0.05F);
                GL11.glFogf(GL11.GL_FOG_END, Math.min(f1, 192F) * 0.5F);
            }
        }

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT);
    }

    /**
     * Update and return fogColorBuffer with the RGBA values passed as arguments
     */
    private FloatBuffer setFogColorBuffer(float par1, float par2, float par3, float par4)
    {
        fogColorBuffer.clear();
        fogColorBuffer.put(par1).put(par2).put(par3).put(par4);
        fogColorBuffer.flip();
        return fogColorBuffer;
    }

    /**
     * Converts performance value (0-2) to FPS (35-200)
     */
    public static int performanceToFps(int par0)
    {
        char c = '\310';

        if (par0 == 1)
        {
            c = 'x';
        }

        if (par0 == 2)
        {
            c = '#';
        }

        return c;
    }

    public void updateFogColorPublic(float f){
        updateFogColor(f);
    }

    public void setupFogPublic(){
        setupFog(0, 0.0F);
    }

    public void renderRainSnowPublic(){
        renderRainSnow(0.0F);
    }

    public void setDistancePublic(){
        farPlaneDistance = (512 >> (mc.gameSettings.renderDistance << 1));
    }

    /**
     * Get minecraft reference from the EntityRenderer
     */
    static Minecraft getRendererMinecraft(EntityRenderer par0EntityRenderer)
    {
        return par0EntityRenderer.mc;
    }
}
