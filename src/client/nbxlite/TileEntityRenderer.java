package net.minecraft.src;

import java.util.*;
import org.lwjgl.opengl.GL11;

public class TileEntityRenderer
{
    /**
     * A mapping of TileEntitySpecialRenderers used for each TileEntity that has one
     */
    private Map specialRendererMap;

    /** The static instance of TileEntityRenderer */
    public static TileEntityRenderer instance = new TileEntityRenderer();

    /** The FontRenderer instance used by the TileEntityRenderer */
    private FontRenderer fontRenderer;

    /** The player's current X position (same as playerX) */
    public static double staticPlayerX;

    /** The player's current Y position (same as playerY) */
    public static double staticPlayerY;

    /** The player's current Z position (same as playerZ) */
    public static double staticPlayerZ;

    /** The RenderEngine instance used by the TileEntityRenderer */
    public RenderEngine renderEngine;

    /** Reference to the World object. */
    public World worldObj;
    public EntityLiving entityLivingPlayer;
    public float playerYaw;
    public float playerPitch;

    /** The player's X position in this rendering context */
    public double playerX;

    /** The player's Y position in this rendering context */
    public double playerY;

    /** The player's Z position in this rendering context */
    public double playerZ;

    private TileEntityRenderer()
    {
        specialRendererMap = new HashMap();
        specialRendererMap.put(net.minecraft.src.TileEntitySign.class, new TileEntitySignRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntityPiston.class, new TileEntityRendererPiston());
        specialRendererMap.put(net.minecraft.src.TileEntityChest.class, new TileEntityChestRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntityEnchantmentTable.class, new RenderEnchantmentTable());
        specialRendererMap.put(net.minecraft.src.TileEntityEndPortal.class, new RenderEndPortal());
        specialRendererMap.put(net.minecraft.src.TileEntityBeacon.class, new TileEntityBeaconRenderer());
        specialRendererMap.put(net.minecraft.src.TileEntitySkull.class, new TileEntitySkullRenderer());
        TileEntitySpecialRenderer tileentityspecialrenderer;

        for (Iterator iterator = specialRendererMap.values().iterator(); iterator.hasNext(); tileentityspecialrenderer.setTileEntityRenderer(this))
        {
            tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();
        }
    }

    /**
     * Returns the TileEntitySpecialRenderer used to render this TileEntity class, or null if it has no special renderer
     */
    public TileEntitySpecialRenderer getSpecialRendererForClass(Class par1Class)
    {
        TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)specialRendererMap.get(par1Class);

        if (tileentityspecialrenderer == null && par1Class != (net.minecraft.src.TileEntity.class))
        {
            tileentityspecialrenderer = getSpecialRendererForClass(par1Class.getSuperclass());
            specialRendererMap.put(par1Class, tileentityspecialrenderer);
        }

        return tileentityspecialrenderer;
    }

    /**
     * Returns true if this TileEntity instance has a TileEntitySpecialRenderer associated with it, false otherwise.
     */
    public boolean hasSpecialRenderer(TileEntity par1TileEntity)
    {
        return getSpecialRendererForEntity(par1TileEntity) != null;
    }

    /**
     * Returns the TileEntitySpecialRenderer used to render this TileEntity instance, or null if it has no special
     * renderer
     */
    public TileEntitySpecialRenderer getSpecialRendererForEntity(TileEntity par1TileEntity)
    {
        if (par1TileEntity == null)
        {
            return null;
        }
        else
        {
            return getSpecialRendererForClass(par1TileEntity.getClass());
        }
    }

    /**
     * Caches several render-related references, including the active World, RenderEngine, FontRenderer, and the camera-
     * bound EntityLiving's interpolated pitch, yaw and position. Args: world, renderengine, fontrenderer, entityliving,
     * partialTickTime
     */
    public void cacheActiveRenderInfo(World par1World, RenderEngine par2RenderEngine, FontRenderer par3FontRenderer, EntityLiving par4EntityLiving, float par5)
    {
        if (worldObj != par1World)
        {
            setWorld(par1World);
        }

        renderEngine = par2RenderEngine;
        entityLivingPlayer = par4EntityLiving;
        fontRenderer = par3FontRenderer;
        playerYaw = par4EntityLiving.prevRotationYaw + (par4EntityLiving.rotationYaw - par4EntityLiving.prevRotationYaw) * par5;
        playerPitch = par4EntityLiving.prevRotationPitch + (par4EntityLiving.rotationPitch - par4EntityLiving.prevRotationPitch) * par5;
        playerX = par4EntityLiving.lastTickPosX + (par4EntityLiving.posX - par4EntityLiving.lastTickPosX) * (double)par5;
        playerY = par4EntityLiving.lastTickPosY + (par4EntityLiving.posY - par4EntityLiving.lastTickPosY) * (double)par5;
        playerZ = par4EntityLiving.lastTickPosZ + (par4EntityLiving.posZ - par4EntityLiving.lastTickPosZ) * (double)par5;
    }

    /**
     * Render this TileEntity at its current position from the player
     */
    public void renderTileEntity(TileEntity par1TileEntity, float par2)
    {
        if (par1TileEntity.getDistanceFrom(playerX, playerY, playerZ) < par1TileEntity.func_82115_m())
        {
            if (net.minecraft.client.Minecraft.oldlighting){
                float f1 = worldObj.getLightBrightness(par1TileEntity.xCoord, par1TileEntity.yCoord, par1TileEntity.zCoord);
                GL11.glColor4f(f1, f1, f1, 1.0F);
            }else{
                int i = worldObj.getLightBrightnessForSkyBlocks(par1TileEntity.xCoord, par1TileEntity.yCoord, par1TileEntity.zCoord, 0);
                int j = i % 0x10000;
                int k = i / 0x10000;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            }
            renderTileEntityAt(par1TileEntity, (double)par1TileEntity.xCoord - staticPlayerX, (double)par1TileEntity.yCoord - staticPlayerY, (double)par1TileEntity.zCoord - staticPlayerZ, par2);
        }
    }

    /**
     * Render this TileEntity at a given set of coordinates
     */
    public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8)
    {
        TileEntitySpecialRenderer tileentityspecialrenderer = getSpecialRendererForEntity(par1TileEntity);

        if (tileentityspecialrenderer != null)
        {
            tileentityspecialrenderer.renderTileEntityAt(par1TileEntity, par2, par4, par6, par8);
        }
    }

    /**
     * Sets the world used by all TileEntitySpecialRender instances and notifies them of this change.
     */
    public void setWorld(World par1World)
    {
        worldObj = par1World;
        Iterator iterator = specialRendererMap.values().iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            TileEntitySpecialRenderer tileentityspecialrenderer = (TileEntitySpecialRenderer)iterator.next();

            if (tileentityspecialrenderer != null)
            {
                tileentityspecialrenderer.onWorldChange(par1World);
            }
        }
        while (true);
    }

    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }
}
