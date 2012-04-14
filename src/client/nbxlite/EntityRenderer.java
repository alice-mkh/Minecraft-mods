package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import net.minecraft.src.nbxlite.oldbiomes.*;

public class EntityRenderer
{
    public static boolean anaglyphEnable = false;

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
    public int lightmapTexture;
    private int lightmapColors[];

    /** FOV modifier hand */
    private float fovModifierHand;

    /** FOV modifier hand prev */
    private float fovModifierHandPrev;

    /** FOV multiplier temp */
    private float fovMultiplierTemp;

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
    volatile int field_1394_b;
    volatile int field_1393_c;

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
        farPlaneDistance = 0.0F;
        pointedEntity = null;
        mouseFilterXAxis = new MouseFilter();
        mouseFilterYAxis = new MouseFilter();
        mouseFilterDummy1 = new MouseFilter();
        mouseFilterDummy2 = new MouseFilter();
        mouseFilterDummy3 = new MouseFilter();
        mouseFilterDummy4 = new MouseFilter();
        thirdPersonDistance = 4F;
        thirdPersonDistanceTemp = 4F;
        debugCamYaw = 0.0F;
        prevDebugCamYaw = 0.0F;
        debugCamPitch = 0.0F;
        prevDebugCamPitch = 0.0F;
        debugCamFOV = 0.0F;
        prevDebugCamFOV = 0.0F;
        camRoll = 0.0F;
        prevCamRoll = 0.0F;
        cloudFog = false;
        cameraZoom = 1.0D;
        cameraYaw = 0.0D;
        cameraPitch = 0.0D;
        prevFrameTime = System.currentTimeMillis();
        renderEndNanoTime = 0L;
        lightmapUpdateNeeded = false;
        torchFlickerX = 0.0F;
        torchFlickerDX = 0.0F;
        torchFlickerY = 0.0F;
        torchFlickerDY = 0.0F;
        random = new Random();
        rainSoundCounter = 0;
        field_1394_b = 0;
        field_1393_c = 0;
        fogColorBuffer = GLAllocation.createDirectFloatBuffer(16);
        mc = par1Minecraft;
        itemRenderer = new ItemRenderer(par1Minecraft);
        lightmapTexture = par1Minecraft.renderEngine.allocateAndSetupTexture(new BufferedImage(16, 16, 1));
        lightmapColors = new int[256];
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
            smoothCamFilterX = mouseFilterXAxis.func_22386_a(smoothCamYaw, 0.05F * f2);
            smoothCamFilterY = mouseFilterYAxis.func_22386_a(smoothCamPitch, 0.05F * f2);
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
        if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_NEWBIOMES){
            addRainParticles();
        }else if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES){
            addRainParticlesOld();
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

        double d = mc.playerController.getBlockReachDistance();
        mc.objectMouseOver = mc.renderViewEntity.rayTrace(d, par1);
        double d1 = d;
        Vec3D vec3d = mc.renderViewEntity.getPosition(par1);

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
            d1 = mc.objectMouseOver.hitVec.distanceTo(vec3d);
        }

        Vec3D vec3d1 = mc.renderViewEntity.getLook(par1);
        Vec3D vec3d2 = vec3d.addVector(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d);
        pointedEntity = null;
        float f = 1.0F;
        java.util.List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(vec3d1.xCoord * d, vec3d1.yCoord * d, vec3d1.zCoord * d).expand(f, f, f));
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
            MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3d, vec3d2);

            if (axisalignedbb.isVecInside(vec3d))
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

            double d3 = vec3d.distanceTo(movingobjectposition.hitVec);

            if (d3 < d2 || d2 == 0.0D)
            {
                pointedEntity = entity;
                d2 = d3;
            }
        }

        if (pointedEntity != null && (d2 < d1 || mc.objectMouseOver == null))
        {
            mc.objectMouseOver = new MovingObjectPosition(pointedEntity);
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

        if (entityplayer.getHealth() <= 0)
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
        EntityLiving entityliving = mc.renderViewEntity;
        float f = (float)entityliving.hurtTime - par1;

        if (entityliving.getHealth() <= 0)
        {
            float f1 = (float)entityliving.deathTime + par1;
            GL11.glRotatef(40F - 8000F / (f1 + 200F), 0.0F, 0.0F, 1.0F);
        }

        if (f < 0.0F)
        {
            return;
        }
        else
        {
            f /= entityliving.maxHurtTime;
            f = MathHelper.sin(f * f * f * f * (float)Math.PI);
            float f2 = entityliving.attackedAtYaw;
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
        if (!(mc.renderViewEntity instanceof EntityPlayer))
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
        EntityLiving entityliving = mc.renderViewEntity;
        float f = entityliving.yOffset - 1.62F;
        double d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)par1;
        double d1 = (entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)par1) - (double)f;
        double d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)par1;
        GL11.glRotatef(prevCamRoll + (camRoll - prevCamRoll) * par1, 0.0F, 0.0F, 1.0F);

        if (entityliving.isPlayerSleeping())
        {
            f = (float)((double)f + 1.0D);
            GL11.glTranslatef(0.0F, 0.3F, 0.0F);

            if (!mc.gameSettings.debugCamEnable)
            {
                int i = mc.theWorld.getBlockId(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));

                if (i == Block.bed.blockID)
                {
                    int j = mc.theWorld.getBlockMetadata(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posY), MathHelper.floor_double(entityliving.posZ));
                    int k = j & 3;
                    GL11.glRotatef(k * 90, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * par1 + 180F, 0.0F, -1F, 0.0F);
                GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * par1, -1F, 0.0F, 0.0F);
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
                float f2 = entityliving.rotationYaw;
                float f4 = entityliving.rotationPitch;

                if (mc.gameSettings.thirdPersonView == 2)
                {
                    f4 += 180F;
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
                    MovingObjectPosition movingobjectposition = mc.theWorld.rayTraceBlocks(Vec3D.createVector(d + (double)f5, d1 + (double)f6, d2 + (double)f7), Vec3D.createVector((d - d4) + (double)f5 + (double)f7, (d1 - d6) + (double)f6, (d2 - d5) + (double)f7));

                    if (movingobjectposition == null)
                    {
                        continue;
                    }

                    double d7 = movingobjectposition.hitVec.distanceTo(Vec3D.createVector(d, d1, d2));

                    if (d7 < d3)
                    {
                        d3 = d7;
                    }
                }

                if (mc.gameSettings.thirdPersonView == 2)
                {
                    GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                }

                GL11.glRotatef(entityliving.rotationPitch - f4, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(entityliving.rotationYaw - f2, 0.0F, 1.0F, 0.0F);
                GL11.glTranslatef(0.0F, 0.0F, (float)(-d3));
                GL11.glRotatef(f2 - entityliving.rotationYaw, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(f4 - entityliving.rotationPitch, 1.0F, 0.0F, 0.0F);
            }
        }
        else
        {
            GL11.glTranslatef(0.0F, 0.0F, -0.1F);
        }

        if (!mc.gameSettings.debugCamEnable)
        {
            GL11.glRotatef(entityliving.prevRotationPitch + (entityliving.rotationPitch - entityliving.prevRotationPitch) * par1, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(entityliving.prevRotationYaw + (entityliving.rotationYaw - entityliving.prevRotationYaw) * par1 + 180F, 0.0F, 1.0F, 0.0F);
        }

        GL11.glTranslatef(0.0F, f, 0.0F);
        d = entityliving.prevPosX + (entityliving.posX - entityliving.prevPosX) * (double)par1;
        d1 = (entityliving.prevPosY + (entityliving.posY - entityliving.prevPosY) * (double)par1) - (double)f;
        d2 = entityliving.prevPosZ + (entityliving.posZ - entityliving.prevPosZ) * (double)par1;
        cloudFog = mc.renderGlobal.func_27307_a(d, d1, d2, par1);
    }

    /**
     * sets up projection, view effects, camera position/rotation
     */
    private void setupCameraTransform(float par1, int par2)
    {
        farPlaneDistance = 256 >> mc.gameSettings.renderDistance;
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

        GLU.gluPerspective(getFOVModifier(par1, true), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);

        if (mc.playerController.func_35643_e())
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

        GLU.gluPerspective(getFOVModifier(par1, false), (float)mc.displayWidth / (float)mc.displayHeight, 0.05F, farPlaneDistance * 2.0F);

        if (mc.playerController.func_35643_e())
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

        if (mc.gameSettings.thirdPersonView == 0 && !mc.renderViewEntity.isPlayerSleeping() && !mc.gameSettings.hideGUI && !mc.playerController.func_35643_e())
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
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glMatrixMode(GL11.GL_TEXTURE);
        GL11.glLoadIdentity();
        float f = 0.00390625F;
        GL11.glScalef(f, f, f);
        GL11.glTranslatef(8F, 8F, 8F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        mc.renderEngine.bindTexture(lightmapTexture);
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

    private void updateLightmap()
    {
        World world = mc.theWorld;

        if (world == null)
        {
            return;
        }
        if(mod_noBiomesX.ClassicLight){
            updateLightmap_classicStyle();
            return;
        }

        for (int i = 0; i < 256; i++)
        {
            float f = world.func_35464_b(1.0F) * 0.95F + 0.05F;
            float f1 = world.worldProvider.lightBrightnessTable[i / 16] * f;
            float f2 = world.worldProvider.lightBrightnessTable[i % 16] * (torchFlickerX * 0.1F + 1.5F);

            if (world.lightningFlash > 0)
            {
                f1 = world.worldProvider.lightBrightnessTable[i / 16];
            }

            float f3 = f1 * (world.func_35464_b(1.0F) * 0.65F + 0.35F);
            float f4 = f1 * (world.func_35464_b(1.0F) * 0.65F + 0.35F);
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

            if (world.worldProvider.worldType == 1)
            {
                f9 = 0.22F + f6 * 0.75F;
                f10 = 0.28F + f7 * 0.75F;
                f11 = 0.25F + f8 * 0.75F;
            }

            float f12 = mc.gameSettings.gammaSetting;

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

            float f13 = 1.0F - f9;
            float f14 = 1.0F - f10;
            float f15 = 1.0F - f11;
            f13 = 1.0F - f13 * f13 * f13 * f13;
            f14 = 1.0F - f14 * f14 * f14 * f14;
            f15 = 1.0F - f15 * f15 * f15 * f15;
            f9 = f9 * (1.0F - f12) + f13 * f12;
            f10 = f10 * (1.0F - f12) + f14 * f12;
            f11 = f11 * (1.0F - f12) + f15 * f12;
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

        mc.renderEngine.createTextureFromBytes(lightmapColors, 16, 16, lightmapTexture);
    }

    private void updateLightmap_classicStyle()
    {
        World world = mc.theWorld;
        int i = world.calculateSkylightSubtracted(1.0F);
        int j = 0;
        for(int k = 0; k < 16; k++)
        {
            for(int l = 0; l < 16; l++)
            {
                float f = world.worldProvider.lightBrightnessTable[l];
                int i1 = k - i;
                if(i1 < 0)
                {
                    i1 = 0;
                }
                float f1 = world.worldProvider.lightBrightnessTable[i1];
                int j1 = (int)(f * 255F);
                int k1 = (int)(f1 * 255F);
                float f2 = 1.0F - (float)mod_noBiomesX.LightTintRed / 255F;
                float f3 = 1.0F - (float)mod_noBiomesX.LightTintGreen / 255F;
                float f4 = 1.0F - (float)mod_noBiomesX.LightTintBlue / 255F;
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

        mc.renderEngine.createTextureFromBytes(lightmapColors, 16, 16, lightmapTexture);
    }

    /**
     * Will update any inputs that effect the camera angle (mouse) and then render the world and GUI
     */
    public void updateCameraAndRender(float par1)
    {
        Profiler.startSection("lightTex");

        if (lightmapUpdateNeeded)
        {
            updateLightmap();
        }

        Profiler.endSection();

        if (!Display.isActive())
        {
            if (System.currentTimeMillis() - prevFrameTime > 500L)
            {
                mc.displayInGameMenu();
            }
        }
        else
        {
            prevFrameTime = System.currentTimeMillis();
        }

        Profiler.startSection("mouse");

        if (mc.inGameHasFocus)
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

        Profiler.endSection();

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
        char c = '\310';

        if (mc.gameSettings.limitFramerate == 1)
        {
            c = 'x';
        }

        if (mc.gameSettings.limitFramerate == 2)
        {
            c = '(';
        }

        if (mc.theWorld != null)
        {
            Profiler.startSection("level");

            if (mc.gameSettings.limitFramerate == 0)
            {
                renderWorld(par1, 0L);
            }
            else
            {
                renderWorld(par1, renderEndNanoTime + (long)(0x3b9aca00 / c));
            }

            Profiler.endStartSection("sleep");

            if (mc.gameSettings.limitFramerate == 2)
            {
                long l1 = ((renderEndNanoTime + (long)(0x3b9aca00 / c)) - System.nanoTime()) / 0xf4240L;

                if (l1 > 0L && l1 < 500L)
                {
                    try
                    {
                        Thread.sleep(l1);
                    }
                    catch (InterruptedException interruptedexception)
                    {
                        interruptedexception.printStackTrace();
                    }
                }
            }

            renderEndNanoTime = System.nanoTime();
            Profiler.endStartSection("gui");

            if (!mc.gameSettings.hideGUI || mc.currentScreen != null)
            {
                mc.ingameGUI.renderGameOverlay(par1, mc.currentScreen != null, k, i1);
            }

            Profiler.endSection();
        }
        else
        {
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            setupOverlayRendering();
            long l2 = ((renderEndNanoTime + (long)(0x3b9aca00 / c)) - System.nanoTime()) / 0xf4240L;

            if (l2 < 0L)
            {
                l2 += 10L;
            }

            if (l2 > 0L && l2 < 500L)
            {
                try
                {
                    Thread.sleep(l2);
                }
                catch (InterruptedException interruptedexception1)
                {
                    interruptedexception1.printStackTrace();
                }
            }

            renderEndNanoTime = System.nanoTime();
        }

        if (mc.currentScreen != null)
        {
            GL11.glClear(256);
            mc.currentScreen.drawScreen(k, i1, par1);

            if (mc.currentScreen != null && mc.currentScreen.guiParticles != null)
            {
                mc.currentScreen.guiParticles.draw(par1);
            }
        }
    }

    public void renderWorld(float par1, long par2)
    {
        Profiler.startSection("lightTex");

        if (lightmapUpdateNeeded)
        {
            updateLightmap();
        }

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        if (mc.renderViewEntity == null)
        {
            mc.renderViewEntity = mc.thePlayer;
        }

        Profiler.endStartSection("pick");
        getMouseOver(par1);
        EntityLiving entityliving = mc.renderViewEntity;
        RenderGlobal renderglobal = mc.renderGlobal;
        EffectRenderer effectrenderer = mc.effectRenderer;
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)par1;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)par1;
        Profiler.endStartSection("center");
        IChunkProvider ichunkprovider = mc.theWorld.getChunkProvider();

        if (ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            int j = MathHelper.floor_float((int)d) >> 4;
            int k = MathHelper.floor_float((int)d2) >> 4;
            chunkproviderloadorgenerate.setCurrentChunkOver(j, k);
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

            Profiler.endStartSection("clear");
            GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
            updateFogColor(par1);
            GL11.glClear(16640);
            GL11.glEnable(GL11.GL_CULL_FACE);
            Profiler.endStartSection("camera");
            setupCameraTransform(par1, i);
            ActiveRenderInfo.updateRenderInfo(mc.thePlayer, mc.gameSettings.thirdPersonView == 2);
            Profiler.endStartSection("frustrum");
            ClippingHelperImpl.getInstance();

            if (mc.gameSettings.renderDistance < 2)
            {
                setupFog(-1, par1);
                Profiler.endStartSection("sky");
                renderglobal.renderSky(par1);
            }

            GL11.glEnable(GL11.GL_FOG);
            setupFog(1, par1);

            if (mc.gameSettings.ambientOcclusion)
            {
                GL11.glShadeModel(GL11.GL_SMOOTH);
            }

            Profiler.endStartSection("culling");
            Frustrum frustrum = new Frustrum();
            frustrum.setPosition(d, d1, d2);
            mc.renderGlobal.clipRenderersByFrustum(frustrum, par1);

            if (i == 0)
            {
                Profiler.endStartSection("updatechunks");
                long l;

                do
                {
                    if (mc.renderGlobal.updateRenderers(entityliving, false) || par2 == 0L)
                    {
                        break;
                    }

                    l = par2 - System.nanoTime();
                }
                while (l >= 0L && l <= 0x3b9aca00L);
            }

            setupFog(0, par1);
            GL11.glEnable(GL11.GL_FOG);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
            RenderHelper.disableStandardItemLighting();
            Profiler.endStartSection("terrain");
            renderglobal.sortAndRender(entityliving, 0, par1);
            GL11.glShadeModel(GL11.GL_FLAT);

            if (debugViewDirection == 0)
            {
                RenderHelper.enableStandardItemLighting();
                Profiler.endStartSection("entities");
                renderglobal.renderEntities(entityliving.getPosition(par1), frustrum, par1);
                enableLightmap(par1);
                Profiler.endStartSection("litParticles");
                effectrenderer.func_1187_b(entityliving, par1);
                RenderHelper.disableStandardItemLighting();
                setupFog(0, par1);
                Profiler.endStartSection("particles");
                effectrenderer.renderParticles(entityliving, par1);
                disableLightmap(par1);

                if (mc.objectMouseOver != null && entityliving.isInsideOfMaterial(Material.water) && (entityliving instanceof EntityPlayer) && !mc.gameSettings.hideGUI)
                {
                    EntityPlayer entityplayer = (EntityPlayer)entityliving;
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    Profiler.endStartSection("outline");
                    renderglobal.drawBlockBreaking(entityplayer, mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), par1);
                    renderglobal.drawSelectionBox(entityplayer, mc.objectMouseOver, 0, entityplayer.inventory.getCurrentItem(), par1);
                    GL11.glEnable(GL11.GL_ALPHA_TEST);
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(true);
            setupFog(0, par1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));

            if (mc.gameSettings.fancyGraphics)
            {
                Profiler.endStartSection("water");

                if (mc.gameSettings.ambientOcclusion)
                {
                    GL11.glShadeModel(GL11.GL_SMOOTH);
                }

                GL11.glColorMask(false, false, false, false);
                int i1 = renderglobal.sortAndRender(entityliving, 1, par1);

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

                if (i1 > 0)
                {
                    renderglobal.renderAllRenderLists(1, par1);
                }

                GL11.glShadeModel(GL11.GL_FLAT);
            }
            else
            {
                Profiler.endStartSection("water");
                renderglobal.sortAndRender(entityliving, 1, par1);
            }

            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);

            if (cameraZoom == 1.0D && (entityliving instanceof EntityPlayer) && !mc.gameSettings.hideGUI && mc.objectMouseOver != null && !entityliving.isInsideOfMaterial(Material.water))
            {
                EntityPlayer entityplayer1 = (EntityPlayer)entityliving;
                GL11.glDisable(GL11.GL_ALPHA_TEST);
                Profiler.endStartSection("outline");
                renderglobal.drawBlockBreaking(entityplayer1, mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1);
                renderglobal.drawSelectionBox(entityplayer1, mc.objectMouseOver, 0, entityplayer1.inventory.getCurrentItem(), par1);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
            }

            Profiler.endStartSection("weather");
            if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_BIOMELESS){
                renderSnow(par1);
            }
            if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_OLDBIOMES){
                renderOldRainSnow(par1);
            }
            if (mod_noBiomesX.Generator==mod_noBiomesX.GEN_NEWBIOMES){
                renderRainSnow(par1);
            }
            GL11.glDisable(GL11.GL_FOG);

            if (pointedEntity == null);

            if (mc.gameSettings.shouldRenderClouds())
            {
                Profiler.endStartSection("clouds");
                GL11.glPushMatrix();
                setupFog(0, par1);
                GL11.glEnable(GL11.GL_FOG);
                renderglobal.renderClouds(par1);
                GL11.glDisable(GL11.GL_FOG);
                setupFog(1, par1);
                GL11.glPopMatrix();
            }

            Profiler.endStartSection("hand");

            if (cameraZoom == 1.0D)
            {
                GL11.glClear(256);
                renderHand(par1, i);
            }

            if (!mc.gameSettings.anaglyph)
            {
                Profiler.endSection();
                return;
            }
        }

        GL11.glColorMask(true, true, true, false);
        Profiler.endSection();
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
        EntityLiving entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int i = MathHelper.floor_double(entityliving.posX);
        int j = MathHelper.floor_double(entityliving.posY);
        int k = MathHelper.floor_double(entityliving.posZ);
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
            int i2 = world.getPrecipitationHeight(k1, l1);
            int j2 = world.getBlockId(k1, i2 - 1, l1);
            BiomeGenBase biomegenbase = world.getBiomeGenForCoords(k1, l1);

            if (i2 > j + byte0 || i2 < j - byte0 || !biomegenbase.canSpawnLightningBolt() || biomegenbase.getFloatTemperature() <= 0.2F)
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
                mc.effectRenderer.addEffect(new EntitySmokeFX(world, (float)k1 + f1, (double)((float)i2 + 0.1F) - Block.blocksList[j2].minY, (float)l1 + f2, 0.0D, 0.0D, 0.0D));
                continue;
            }

            if (random.nextInt(++l) == 0)
            {
                d = (float)k1 + f1;
                d1 = (double)((float)i2 + 0.1F) - Block.blocksList[j2].minY;
                d2 = (float)l1 + f2;
            }

            mc.effectRenderer.addEffect(new EntityRainFX(world, (float)k1 + f1, (double)((float)i2 + 0.1F) - Block.blocksList[j2].minY, (float)l1 + f2));
        }

        if (l > 0 && random.nextInt(3) < rainSoundCounter++)
        {
            rainSoundCounter = 0;

            if (d1 > entityliving.posY + 1.0D && world.getPrecipitationHeight(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posZ)) > MathHelper.floor_double(entityliving.posY))
            {
                mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F);
            }
            else
            {
                mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F);
            }
        }
    }

    private void addRainParticlesOld()
    {
        float f = mc.theWorld.getRainStrength(1.0F);
        if(!mc.gameSettings.fancyGraphics)
        {
            f /= 2.0F;
        }
        if(f == 0.0F)
        {
            return;
        }
        random.setSeed((long)rendererUpdateCount * 0x12a7ce5fL);
        EntityLiving entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int i = MathHelper.floor_double(entityliving.posX);
        int j = MathHelper.floor_double(entityliving.posY);
        int k = MathHelper.floor_double(entityliving.posZ);
        byte byte0 = 10;
        double d = 0.0D;
        double d1 = 0.0D;
        double d2 = 0.0D;
        int l = 0;
        for(int i1 = 0; i1 < (int)(100F * f * f); i1++)
        {
            int j1 = (i + random.nextInt(byte0)) - random.nextInt(byte0);
            int k1 = (k + random.nextInt(byte0)) - random.nextInt(byte0);
            int l1 = world.getPrecipitationHeight(j1, k1);
            int i2 = world.getBlockId(j1, l1 - 1, k1);
            if(l1 > j + byte0 || l1 < j - byte0 || !world.getWorldChunkManager().oldGetBiomeGenAt(j1, k1).canSpawnLightningBolt())
            {
                continue;
            }
            float f1 = random.nextFloat();
            float f2 = random.nextFloat();
            if(i2 <= 0)
            {
                continue;
            }
            if(Block.blocksList[i2].blockMaterial == Material.lava)
            {
                mc.effectRenderer.addEffect(new EntitySmokeFX(world, (float)j1 + f1, (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (float)k1 + f2, 0.0D, 0.0D, 0.0D));
                continue;
            }
            if(random.nextInt(++l) == 0)
            {
                d = (float)j1 + f1;
                d1 = (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY;
                d2 = (float)k1 + f2;
            }
            mc.effectRenderer.addEffect(new EntityRainFX(world, (float)j1 + f1, (double)((float)l1 + 0.1F) - Block.blocksList[i2].minY, (float)k1 + f2));
        }

        if(l > 0 && random.nextInt(3) < rainSoundCounter++)
        {
            rainSoundCounter = 0;
            if(d1 > entityliving.posY + 1.0D && world.getPrecipitationHeight(MathHelper.floor_double(entityliving.posX), MathHelper.floor_double(entityliving.posZ)) > MathHelper.floor_double(entityliving.posY))
            {
                mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.1F, 0.5F);
            } else
            {
                mc.theWorld.playSoundEffect(d, d1, d2, "ambient.weather.rain", 0.2F, 1.0F);
            }
        }
    }

    protected void renderSnow(float f)
    {
        if(!mod_noBiomesX.SnowCovered || mc.thePlayer.dimension!=0)
        {
            return;
        }
        enableLightmap(f);
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
        EntityLiving entityliving = mc.renderViewEntity;
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
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/environment/snow.png"));
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
                    GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/environment/snow.png"));
                    tessellator.startDrawingQuads();
                }
                float f8 = ((float)(rendererUpdateCount & 0x1ff) + f) / 512F;
                float f9 = random.nextFloat() + f4 * 0.01F * (float)random.nextGaussian();
                float f10 = random.nextFloat() + f4 * (float)random.nextGaussian() * 0.001F;
                double d3 = (double)((float)i2 + 0.5F) - entityliving.posX;
                double d4 = (double)((float)l1 + 0.5F) - entityliving.posZ;
                float f11 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)k1;
                float f12 = 1.0F;
                tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i2, j3, l1, 0) * 3 + 0xf000f0) / 4);
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
        disableLightmap(f);
    }

    protected void renderOldRainSnow(float f)
    {
        float f1 = mc.theWorld.getRainStrength(f);
        if(f1 <= 0.0F)
        {
            return;
        }
        EntityLiving entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int i = MathHelper.floor_double(entityliving.posX);
        int j = MathHelper.floor_double(entityliving.posY);
        int k = MathHelper.floor_double(entityliving.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(2884 /*GL_CULL_FACE*/);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(3042 /*GL_BLEND*/);
        GL11.glBlendFunc(770, 771);
        GL11.glAlphaFunc(516, 0.01F);
        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/environment/snow.png"));
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)f;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)f;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)f;
        int l = MathHelper.floor_double(d1);
        int i1 = 5;
        if(mc.gameSettings.fancyGraphics)
        {
            i1 = 10;
        }
        OldBiomeGenBase abiomegenbase[] = world.getWorldChunkManager().oldFunc_4069_a(i - i1, k - i1, i1 * 2 + 1, i1 * 2 + 1);
        int j1 = 0;
        for(int k1 = i - i1; k1 <= i + i1; k1++)
        {
            for(int i2 = k - i1; i2 <= k + i1; i2++)
            {
                OldBiomeGenBase biomegenbase = abiomegenbase[j1++];
                if(!biomegenbase.getEnableSnow())
                {
                    continue;
                }
                int k2 = world.getPrecipitationHeight(k1, i2);
                if(k2 < 0)
                {
                    k2 = 0;
                }
                int i3 = k2;
                if(i3 < l)
                {
                    i3 = l;
                }
                int k3 = j - i1;
                int i4 = j + i1;
                if(k3 < k2)
                {
                    k3 = k2;
                }
                if(i4 < k2)
                {
                    i4 = k2;
                }
                float f3 = 1.0F;
                if(k3 != i4)
                {
                    random.setSeed(k1 * k1 * 3121 /*GL_RGBA_MODE*/ + k1 * 0x2b24abb + i2 * i2 * 0x66397 + i2 * 13761);
                    float f5 = (float)rendererUpdateCount + f;
                    float f6 = ((float)(rendererUpdateCount & 0x1ff) + f) / 512F;
                    float f7 = random.nextFloat() + f5 * 0.01F * (float)random.nextGaussian();
                    float f8 = random.nextFloat() + f5 * (float)random.nextGaussian() * 0.001F;
                    double d5 = (double)((float)k1 + 0.5F) - entityliving.posX;
                    double d6 = (double)((float)i2 + 0.5F) - entityliving.posZ;
                    float f11 = MathHelper.sqrt_double(d5 * d5 + d6 * d6) / (float)i1;
                    tessellator.startDrawingQuads();
                    float f12 = world.getLightBrightness(k1, i3, i2);
                    GL11.glColor4f(f12, f12, f12, ((1.0F - f11 * f11) * 0.3F + 0.5F) * f1);
                    tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                    tessellator.addVertexWithUV(k1 + 0, k3, (double)i2 + 0.5D, 0.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV(k1 + 1, k3, (double)i2 + 0.5D, 1.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV(k1 + 1, i4, (double)i2 + 0.5D, 1.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV(k1 + 0, i4, (double)i2 + 0.5D, 0.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, k3, i2 + 0, 0.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, k3, i2 + 1, 1.0F * f3 + f7, ((float)k3 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, i4, i2 + 1, 1.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.addVertexWithUV((double)k1 + 0.5D, i4, i2 + 0, 0.0F * f3 + f7, ((float)i4 * f3) / 4F + f6 * f3 + f8);
                    tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                }
            }

        }

        GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTexture("/environment/rain.png"));
        if(mc.gameSettings.fancyGraphics)
        {
            i1 = 10;
        }
        j1 = 0;
        for(int l1 = i - i1; l1 <= i + i1; l1++)
        {
            for(int j2 = k - i1; j2 <= k + i1; j2++)
            {
                OldBiomeGenBase biomegenbase1 = abiomegenbase[j1++];
                if(!biomegenbase1.canSpawnLightningBolt())
                {
                    continue;
                }
                int l2 = world.getPrecipitationHeight(l1, j2);
                int j3 = j - i1;
                int l3 = j + i1;
                if(j3 < l2)
                {
                    j3 = l2;
                }
                if(l3 < l2)
                {
                    l3 = l2;
                }
                float f2 = 1.0F;
                if(j3 != l3)
                {
                    random.setSeed(l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 0x2b24abb + j2 * j2 * 0x66397 + j2 * 13761);
                    float f4 = (((float)(rendererUpdateCount + l1 * l1 * 3121 /*GL_RGBA_MODE*/ + l1 * 0x2b24abb + j2 * j2 * 0x66397 + j2 * 13761 & 0x1f) + f) / 32F) * (3F + random.nextFloat());
                    double d3 = (double)((float)l1 + 0.5F) - entityliving.posX;
                    double d4 = (double)((float)j2 + 0.5F) - entityliving.posZ;
                    float f9 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)i1;
                    tessellator.startDrawingQuads();
                    float f10 = world.getLightBrightness(l1, 128, j2) * 0.85F + 0.15F;
                    GL11.glColor4f(f10, f10, f10, ((1.0F - f9 * f9) * 0.5F + 0.5F) * f1);
                    tessellator.setTranslation(-d * 1.0D, -d1 * 1.0D, -d2 * 1.0D);
                    tessellator.addVertexWithUV(l1 + 0, j3, (double)j2 + 0.5D, 0.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV(l1 + 1, j3, (double)j2 + 0.5D, 1.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV(l1 + 1, l3, (double)j2 + 0.5D, 1.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV(l1 + 0, l3, (double)j2 + 0.5D, 0.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, j3, j2 + 0, 0.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, j3, j2 + 1, 1.0F * f2, ((float)j3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, l3, j2 + 1, 1.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.addVertexWithUV((double)l1 + 0.5D, l3, j2 + 0, 0.0F * f2, ((float)l3 * f2) / 4F + f4 * f2);
                    tessellator.setTranslation(0.0D, 0.0D, 0.0D);
                    tessellator.draw();
                }
            }

        }

        GL11.glEnable(2884 /*GL_CULL_FACE*/);
        GL11.glDisable(3042 /*GL_BLEND*/);
        GL11.glAlphaFunc(516, 0.1F);
    }

    /**
     * Render rain and snow
     */
    protected void renderRainSnow(float par1)
    {
        float f = mc.theWorld.getRainStrength(par1);

        if (f <= 0.0F)
        {
            return;
        }

        enableLightmap(par1);

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

        EntityLiving entityliving = mc.renderViewEntity;
        World world = mc.theWorld;
        int k = MathHelper.floor_double(entityliving.posX);
        int l = MathHelper.floor_double(entityliving.posY);
        int i1 = MathHelper.floor_double(entityliving.posZ);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/environment/snow.png"));
        double d = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)par1;
        double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1;
        double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)par1;
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
                BiomeGenBase biomegenbase = world.getBiomeGenForCoords(i2, l1);

                if (!biomegenbase.canSpawnLightningBolt() && !biomegenbase.getEnableSnow())
                {
                    continue;
                }

                int k2 = world.getPrecipitationHeight(i2, l1);
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

                if (world.getWorldChunkManager().getTemperatureAtHeight(f8, k2) >= 0.15F)
                {
                    if (byte0 != 0)
                    {
                        if (byte0 >= 0)
                        {
                            tessellator.draw();
                        }

                        byte0 = 0;
                        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/environment/rain.png"));
                        tessellator.startDrawingQuads();
                    }

                    float f9 = (((float)(rendererUpdateCount + i2 * i2 * 3121 + i2 * 0x2b24abb + l1 * l1 * 0x66397 + l1 * 13761 & 0x1f) + par1) / 32F) * (3F + random.nextFloat());
                    double d3 = (double)((float)i2 + 0.5F) - entityliving.posX;
                    double d4 = (double)((float)l1 + 0.5F) - entityliving.posZ;
                    float f13 = MathHelper.sqrt_double(d3 * d3 + d4 * d4) / (float)k1;
                    float f14 = 1.0F;
                    tessellator.setBrightness(world.getLightBrightnessForSkyBlocks(i2, j3, l1, 0));
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
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/environment/snow.png"));
                    tessellator.startDrawingQuads();
                }

                float f10 = ((float)(rendererUpdateCount & 0x1ff) + par1) / 512F;
                float f11 = random.nextFloat() + f4 * 0.01F * (float)random.nextGaussian();
                float f12 = random.nextFloat() + f4 * (float)random.nextGaussian() * 0.001F;
                double d5 = (double)((float)i2 + 0.5F) - entityliving.posX;
                double d6 = (double)((float)l1 + 0.5F) - entityliving.posZ;
                float f15 = MathHelper.sqrt_double(d5 * d5 + d6 * d6) / (float)k1;
                float f16 = 1.0F;
                tessellator.setBrightness((world.getLightBrightnessForSkyBlocks(i2, j3, l1, 0) * 3 + 0xf000f0) / 4);
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
        disableLightmap(par1);
    }

    /**
     * Setup orthogonal projection for rendering GUI screen overlays
     */
    public void setupOverlayRendering()
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.scaledWidthD, scaledresolution.scaledHeightD, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
    }

    /**
     * calculates fog and calls glClearColor
     */
    private void updateFogColor(float par1)
    {
        World world = mc.theWorld;
        EntityLiving entityliving = mc.renderViewEntity;
        float f = 1.0F / (float)(4 - mc.gameSettings.renderDistance);
        f = 1.0F - (float)Math.pow(f, 0.25D);
        Vec3D vec3d = world.getSkyColor(mc.renderViewEntity, par1);
        float f1 = (float)vec3d.xCoord;
        float f2 = (float)vec3d.yCoord;
        float f3 = (float)vec3d.zCoord;
        Vec3D vec3d1 = world.getFogColor(par1);
        fogColorRed = (float)vec3d1.xCoord;
        fogColorGreen = (float)vec3d1.yCoord;
        fogColorBlue = (float)vec3d1.zCoord;

        if (mc.gameSettings.renderDistance < 2)
        {
            Vec3D vec3d2 = MathHelper.sin(world.getCelestialAngleRadians(par1)) <= 0.0F ? Vec3D.createVector(1.0D, 0.0D, 0.0D) : Vec3D.createVector(-1D, 0.0D, 0.0D);
            if (mod_noBiomesX.SunriseAtNorth){
                vec3d2 = MathHelper.sin(world.getCelestialAngleRadians(f)) <= 0.0F ? Vec3D.createVector(0.0D, 0.0D, -1D) : Vec3D.createVector(0.0D, 0.0D, 1.0D);
            }
            float f5 = (float)entityliving.getLook(par1).dotProduct(vec3d2);

            if (f5 < 0.0F)
            {
                f5 = 0.0F;
            }

            if (f5 > 0.0F)
            {
                float af[] = world.worldProvider.calcSunriseSunsetColors(world.getCelestialAngle(par1), par1);

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
        float f4 = world.getRainStrength(par1);

        if (f4 > 0.0F)
        {
            float f6 = 1.0F - f4 * 0.5F;
            float f8 = 1.0F - f4 * 0.4F;
            fogColorRed *= f6;
            fogColorGreen *= f6;
            fogColorBlue *= f8;
        }

        float f7 = world.getWeightedThunderStrength(par1);

        if (f7 > 0.0F)
        {
            float f9 = 1.0F - f7 * 0.5F;
            fogColorRed *= f9;
            fogColorGreen *= f9;
            fogColorBlue *= f9;
        }

        int i = ActiveRenderInfo.getBlockIdAtEntityViewpoint(mc.theWorld, entityliving, par1);

        if (cloudFog)
        {
            Vec3D vec3d3 = world.drawClouds(par1);
            fogColorRed = (float)vec3d3.xCoord;
            fogColorGreen = (float)vec3d3.yCoord;
            fogColorBlue = (float)vec3d3.zCoord;
        }
        else if (i != 0 && Block.blocksList[i].blockMaterial == Material.water)
        {
            fogColorRed = 0.02F;
            fogColorGreen = 0.02F;
            fogColorBlue = 0.2F;
        }
        else if (i != 0 && Block.blocksList[i].blockMaterial == Material.lava)
        {
            fogColorRed = 0.6F;
            fogColorGreen = 0.1F;
            fogColorBlue = 0.0F;
        }

        float f10 = fogColor2 + (fogColor1 - fogColor2) * par1;
        fogColorRed *= f10;
        fogColorGreen *= f10;
        fogColorBlue *= f10;
        double d = (entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1) * world.worldProvider.getVoidFogYFactor();

        if (entityliving.isPotionActive(Potion.blindness))
        {
            int j = entityliving.getActivePotionEffect(Potion.blindness).getDuration();

            if (j < 20)
            {
                d *= 1.0F - (float)j / 20F;
            }
            else
            {
                d = 0.0D;
            }
        }

        if (d < 1.0D && mod_noBiomesX.VoidFog)
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

        if (mc.gameSettings.anaglyph)
        {
            float f11 = (fogColorRed * 30F + fogColorGreen * 59F + fogColorBlue * 11F) / 100F;
            float f12 = (fogColorRed * 30F + fogColorGreen * 70F) / 100F;
            float f13 = (fogColorRed * 30F + fogColorBlue * 70F) / 100F;
            fogColorRed = f11;
            fogColorGreen = f12;
            fogColorBlue = f13;
        }

        GL11.glClearColor(fogColorRed, fogColorGreen, fogColorBlue, 0.0F);
    }

    /**
     * Sets up the fog to be rendered. If the arg passed in is -1 the fog starts at 0 and goes to 80% of far plane
     * distance and is used for sky rendering.
     */
    private void setupFog(int par1, float par2)
    {
        EntityLiving entityliving = mc.renderViewEntity;
        boolean flag = false;

        if (entityliving instanceof EntityPlayer)
        {
            flag = ((EntityPlayer)entityliving).capabilities.isCreativeMode;
        }

        if (par1 == 999)
        {
            GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);
            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            GL11.glFogf(GL11.GL_FOG_END, 8F);

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GL11.glFogi(34138, 34139);
            }

            GL11.glFogf(GL11.GL_FOG_START, 0.0F);
            return;
        }

        GL11.glFog(GL11.GL_FOG_COLOR, setFogColorBuffer(fogColorRed, fogColorGreen, fogColorBlue, 1.0F));
        GL11.glNormal3f(0.0F, -1F, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int i = ActiveRenderInfo.getBlockIdAtEntityViewpoint(mc.theWorld, entityliving, par2);

        if (entityliving.isPotionActive(Potion.blindness))
        {
            float f = 5F;
            int j = entityliving.getActivePotionEffect(Potion.blindness).getDuration();

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

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GL11.glFogi(34138, 34139);
            }
        }
        else if (cloudFog)
        {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            float f1 = 1.0F;
            float f5 = 1.0F;
            float f8 = 1.0F;

            if (mc.gameSettings.anaglyph)
            {
                float f11 = (f1 * 30F + f5 * 59F + f8 * 11F) / 100F;
                float f15 = (f1 * 30F + f5 * 70F) / 100F;
                float f18 = (f1 * 30F + f8 * 70F) / 100F;
                f1 = f11;
                f5 = f15;
                f8 = f18;
            }
        }
        else if (i > 0 && Block.blocksList[i].blockMaterial == Material.water)
        {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);

            if (!entityliving.isPotionActive(Potion.waterBreathing))
            {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.1F);
            }
            else
            {
                GL11.glFogf(GL11.GL_FOG_DENSITY, 0.05F);
            }

            float f2 = 0.4F;
            float f6 = 0.4F;
            float f9 = 0.9F;

            if (mc.gameSettings.anaglyph)
            {
                float f12 = (f2 * 30F + f6 * 59F + f9 * 11F) / 100F;
                float f16 = (f2 * 30F + f6 * 70F) / 100F;
                float f19 = (f2 * 30F + f9 * 70F) / 100F;
                f2 = f12;
                f6 = f16;
                f9 = f19;
            }
        }
        else if (i > 0 && Block.blocksList[i].blockMaterial == Material.lava)
        {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            GL11.glFogf(GL11.GL_FOG_DENSITY, 2.0F);
            float f3 = 0.4F;
            float f7 = 0.3F;
            float f10 = 0.3F;

            if (mc.gameSettings.anaglyph)
            {
                float f13 = (f3 * 30F + f7 * 59F + f10 * 11F) / 100F;
                float f17 = (f3 * 30F + f7 * 70F) / 100F;
                float f20 = (f3 * 30F + f10 * 70F) / 100F;
                f3 = f13;
                f7 = f17;
                f10 = f20;
            }
        }
        else
        {
            float f4 = farPlaneDistance;

            if (mc.theWorld.worldProvider.getWorldHasNoSky() && !flag)
            {
                double d = (double)((entityliving.getBrightnessForRender(par2) & 0xf00000) >> 20) / 16D + (entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par2 + 4D) / 32D;

                if (d < 1.0D && mod_noBiomesX.VoidFog)
                {
                    if (d < 0.0D)
                    {
                        d = 0.0D;
                    }

                    d *= d;
                    float f14 = 100F * (float)d;

                    if (f14 < 5F)
                    {
                        f14 = 5F;
                    }

                    if (f4 > f14)
                    {
                        f4 = f14;
                    }
                }
            }

            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_LINEAR);

            if (par1 < 0)
            {
                GL11.glFogf(GL11.GL_FOG_START, 0.0F);
                GL11.glFogf(GL11.GL_FOG_END, f4 * 0.8F);
            }
            else
            {
                GL11.glFogf(GL11.GL_FOG_START, f4 * 0.25F);
                GL11.glFogf(GL11.GL_FOG_END, f4);
            }

            if (GLContext.getCapabilities().GL_NV_fog_distance)
            {
                GL11.glFogi(34138, 34139);
            }

            if (mc.theWorld.worldProvider.func_48218_b((int)entityliving.posX, (int)entityliving.posZ))
            {
                GL11.glFogf(GL11.GL_FOG_START, f4 * 0.05F);
                GL11.glFogf(GL11.GL_FOG_END, Math.min(f4, 192F) * 0.5F);
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
}
