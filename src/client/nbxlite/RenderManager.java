package net.minecraft.src;

import java.util.*;
import org.lwjgl.opengl.GL11;

public class RenderManager
{
    /** A map of entity classes and the associated renderer. */
    private Map entityRenderMap;

    /** The static instance of RenderManager. */
    public static RenderManager instance = new RenderManager();

    /** Renders fonts */
    private FontRenderer fontRenderer;
    public static double renderPosX;
    public static double renderPosY;
    public static double renderPosZ;
    public TextureManager renderEngine;
    public ItemRenderer itemRenderer;

    /** Reference to the World object. */
    public World worldObj;

    /** Rendermanager's variable for the player */
    public EntityLivingBase livingPlayer;
    public EntityLivingBase field_96451_i;
    public float playerViewY;
    public float playerViewX;

    /** Reference to the GameSettings object. */
    public GameSettings options;
    public double viewerPosX;
    public double viewerPosY;
    public double viewerPosZ;
    public static boolean field_85095_o;

    private RenderManager()
    {
        entityRenderMap = new HashMap();
        entityRenderMap.put(net.minecraft.src.EntityCaveSpider.class, new RenderCaveSpider());
        entityRenderMap.put(net.minecraft.src.EntitySpider.class, new RenderSpider());
        entityRenderMap.put(net.minecraft.src.EntityPig.class, new RenderPig(new ModelPig(), new ModelPig(0.5F), 0.7F));
        entityRenderMap.put(net.minecraft.src.EntitySheep.class, new RenderSheep(new ModelSheep2(), new ModelSheep1(), 0.7F));
        entityRenderMap.put(net.minecraft.src.EntityCow.class, new RenderCow(new ModelCow(), 0.7F));
        entityRenderMap.put(net.minecraft.src.EntityMooshroom.class, new RenderMooshroom(new ModelCow(), 0.7F));
        entityRenderMap.put(net.minecraft.src.EntityWolf.class, new RenderWolf(new ModelWolf(), new ModelWolf(), 0.5F));
        entityRenderMap.put(net.minecraft.src.EntityChicken.class, new RenderChicken(new ModelChicken(), 0.3F));
        entityRenderMap.put(net.minecraft.src.EntityOcelot.class, new RenderOcelot(new ModelOcelot(), 0.4F));
        entityRenderMap.put(net.minecraft.src.EntitySilverfish.class, new RenderSilverfish());
        entityRenderMap.put(net.minecraft.src.EntityCreeper.class, new RenderCreeper());
        entityRenderMap.put(net.minecraft.src.EntityEnderman.class, new RenderEnderman());
        entityRenderMap.put(net.minecraft.src.EntitySnowman.class, new RenderSnowMan());
        entityRenderMap.put(net.minecraft.src.EntitySkeleton.class, new RenderSkeleton());
        entityRenderMap.put(net.minecraft.src.EntityWitch.class, new RenderWitch());
        entityRenderMap.put(net.minecraft.src.EntityBlaze.class, new RenderBlaze());
        entityRenderMap.put(net.minecraft.src.EntityZombie.class, new RenderZombie());
        entityRenderMap.put(net.minecraft.src.EntitySlime.class, new RenderSlime(new ModelSlime(16), new ModelSlime(0), 0.25F));
        entityRenderMap.put(net.minecraft.src.EntityMagmaCube.class, new RenderMagmaCube());
        entityRenderMap.put(net.minecraft.src.EntityPlayer.class, new RenderPlayer());
        entityRenderMap.put(net.minecraft.src.EntityGiantZombie.class, new RenderGiantZombie(new ModelZombie(), 0.5F, 6F));
        entityRenderMap.put(net.minecraft.src.EntityGhast.class, new RenderGhast());
        entityRenderMap.put(net.minecraft.src.EntitySquid.class, new RenderSquid(new ModelSquid(), 0.7F));
        entityRenderMap.put(net.minecraft.src.EntityVillager.class, new RenderVillager());
        entityRenderMap.put(net.minecraft.src.EntityIronGolem.class, new RenderIronGolem());
        entityRenderMap.put(net.minecraft.src.EntityBat.class, new RenderBat());
        entityRenderMap.put(net.minecraft.src.EntityDragon.class, new RenderDragon());
        entityRenderMap.put(net.minecraft.src.EntityEnderCrystal.class, new RenderEnderCrystal());
        entityRenderMap.put(net.minecraft.src.EntityWither.class, new RenderWither());
        entityRenderMap.put(net.minecraft.src.Entity.class, new RenderEntity());
        entityRenderMap.put(net.minecraft.src.EntityPainting.class, new RenderPainting());
        entityRenderMap.put(net.minecraft.src.EntityItemFrame.class, new RenderItemFrame());
        entityRenderMap.put(net.minecraft.src.EntityLeashKnot.class, new RenderLeashKnot());
        entityRenderMap.put(net.minecraft.src.EntityArrow.class, new RenderArrow());
        entityRenderMap.put(net.minecraft.src.EntitySnowball.class, new RenderSnowball(Item.snowball));
        entityRenderMap.put(net.minecraft.src.EntityEnderPearl.class, new RenderSnowball(Item.enderPearl));
        entityRenderMap.put(net.minecraft.src.EntityEnderEye.class, new RenderSnowball(Item.eyeOfEnder));
        entityRenderMap.put(net.minecraft.src.EntityEgg.class, new RenderSnowball(Item.egg));
        entityRenderMap.put(net.minecraft.src.EntityPotion.class, new RenderSnowball(Item.potion, 16384));
        entityRenderMap.put(net.minecraft.src.EntityExpBottle.class, new RenderSnowball(Item.expBottle));
        entityRenderMap.put(net.minecraft.src.EntityFireworkRocket.class, new RenderSnowball(Item.firework));
        entityRenderMap.put(net.minecraft.src.EntityLargeFireball.class, new RenderFireball(2.0F));
        entityRenderMap.put(net.minecraft.src.EntitySmallFireball.class, new RenderFireball(0.5F));
        entityRenderMap.put(net.minecraft.src.EntityWitherSkull.class, new RenderWitherSkull());
        entityRenderMap.put(net.minecraft.src.EntityItem.class, new RenderItem());
        entityRenderMap.put(net.minecraft.src.EntityXPOrb.class, new RenderXPOrb());
        entityRenderMap.put(net.minecraft.src.EntityTNTPrimed.class, new RenderTNTPrimed());
        entityRenderMap.put(net.minecraft.src.EntityFallingSand.class, new RenderFallingSand());
        entityRenderMap.put(net.minecraft.src.EntityMinecartTNT.class, new RenderTntMinecart());
        entityRenderMap.put(net.minecraft.src.EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner());
        entityRenderMap.put(net.minecraft.src.EntityMinecart.class, new RenderMinecart());
        entityRenderMap.put(net.minecraft.src.EntityBoat.class, new RenderBoat());
        entityRenderMap.put(net.minecraft.src.EntityFishHook.class, new RenderFish());
        entityRenderMap.put(net.minecraft.src.EntityHorse.class, new RenderHorse(new ModelHorse(), 0.75F));
        entityRenderMap.put(net.minecraft.src.EntityLightningBolt.class, new RenderLightningBolt());
        Minecraft.invokeModMethod("ModLoader", "addAllRenderers", new Class[]{Map.class}, entityRenderMap);
        Render render;

        for (Iterator iterator = entityRenderMap.values().iterator(); iterator.hasNext(); render.setRenderManager(this))
        {
            render = (Render)iterator.next();
        }
    }

    public Render getEntityClassRenderObject(Class par1Class)
    {
        Render render = (Render)entityRenderMap.get(par1Class);

        if (render == null && par1Class != (net.minecraft.src.Entity.class))
        {
            render = getEntityClassRenderObject(par1Class.getSuperclass());
            entityRenderMap.put(par1Class, render);
        }

        return render;
    }

    public Render getEntityRenderObject(Entity par1Entity)
    {
        return getEntityClassRenderObject(par1Entity.getClass());
    }

    /**
     * Caches the current frame's active render info, including the current World, RenderEngine, GameSettings and
     * FontRenderer settings, as well as interpolated player position, pitch and yaw.
     */
    public void cacheActiveRenderInfo(World par1World, TextureManager par2TextureManager, FontRenderer par3FontRenderer, EntityLivingBase par4EntityLivingBase, EntityLivingBase par5EntityLivingBase, GameSettings par6GameSettings, float par7)
    {
        worldObj = par1World;
        renderEngine = par2TextureManager;
        options = par6GameSettings;
        livingPlayer = par4EntityLivingBase;
        field_96451_i = par5EntityLivingBase;
        fontRenderer = par3FontRenderer;

        if (par4EntityLivingBase.isPlayerSleeping())
        {
            int i = par1World.getBlockId(MathHelper.floor_double(par4EntityLivingBase.posX), MathHelper.floor_double(par4EntityLivingBase.posY), MathHelper.floor_double(par4EntityLivingBase.posZ));

            if (i == Block.bed.blockID)
            {
                int j = par1World.getBlockMetadata(MathHelper.floor_double(par4EntityLivingBase.posX), MathHelper.floor_double(par4EntityLivingBase.posY), MathHelper.floor_double(par4EntityLivingBase.posZ));
                int k = j & 3;
                playerViewY = k * 90 + 180;
                playerViewX = 0.0F;
            }
        }
        else
        {
            playerViewY = par4EntityLivingBase.prevRotationYaw + (par4EntityLivingBase.rotationYaw - par4EntityLivingBase.prevRotationYaw) * par7;
            playerViewX = par4EntityLivingBase.prevRotationPitch + (par4EntityLivingBase.rotationPitch - par4EntityLivingBase.prevRotationPitch) * par7;
        }

        if (par6GameSettings.thirdPersonView == 2)
        {
            playerViewY += 180F;
        }

        viewerPosX = par4EntityLivingBase.lastTickPosX + (par4EntityLivingBase.posX - par4EntityLivingBase.lastTickPosX) * (double)par7;
        viewerPosY = par4EntityLivingBase.lastTickPosY + (par4EntityLivingBase.posY - par4EntityLivingBase.lastTickPosY) * (double)par7;
        viewerPosZ = par4EntityLivingBase.lastTickPosZ + (par4EntityLivingBase.posZ - par4EntityLivingBase.lastTickPosZ) * (double)par7;
    }

    /**
     * Will render the specified entity at the specified partial tick time. Args: entity, partialTickTime
     */
    public void renderEntity(Entity par1Entity, float par2)
    {
        if (par1Entity.ticksExisted == 0)
        {
            par1Entity.lastTickPosX = par1Entity.posX;
            par1Entity.lastTickPosY = par1Entity.posY;
            par1Entity.lastTickPosZ = par1Entity.posZ;
        }

        double d = par1Entity.lastTickPosX + (par1Entity.posX - par1Entity.lastTickPosX) * (double)par2;
        double d1 = par1Entity.lastTickPosY + (par1Entity.posY - par1Entity.lastTickPosY) * (double)par2;
        double d2 = par1Entity.lastTickPosZ + (par1Entity.posZ - par1Entity.lastTickPosZ) * (double)par2;
        float f = par1Entity.prevRotationYaw + (par1Entity.rotationYaw - par1Entity.prevRotationYaw) * par2;
        if (!Minecraft.oldlighting){
            int i = par1Entity.getBrightnessForRender(par2);

            if (par1Entity.isBurning())
            {
                i = 0xf000f0;
            }

            int j = i % 0x10000;
            int k = i / 0x10000;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j / 1.0F, (float)k / 1.0F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }else{
            float ff = par1Entity.getBrightness(par2);
            GL11.glColor3f(ff, ff, ff);
        }
        renderEntityWithPosYaw(par1Entity, d - renderPosX, d1 - renderPosY, d2 - renderPosZ, f, par2);
    }

    /**
     * Renders the specified entity with the passed in position, yaw, and partialTickTime. Args: entity, x, y, z, yaw,
     * partialTickTime
     */
    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        Render render = null;

        try
        {
            render = getEntityRenderObject(par1Entity);

            if (render != null && renderEngine != null)
            {
                if (field_85095_o && !par1Entity.isInvisible())
                {
                    try
                    {
                        func_85094_b(par1Entity, par2, par4, par6, par8, par9);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }

                try
                {
                    render.doRender(par1Entity, par2, par4, par6, par8, par9);
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Rendering entity in world"));
                }

                try
                {
                    render.doRenderShadowAndFire(par1Entity, par2, par4, par6, par8, par9);
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Post-rendering entity in world"));
                }
            }
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            par1Entity.func_85029_a(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.func_85074_a(par2, par4, par6));
            crashreportcategory1.addCrashSection("Rotation", Float.valueOf(par8));
            crashreportcategory1.addCrashSection("Delta", Float.valueOf(par9));
            throw new ReportedException(crashreport);
        }
    }

    private void func_85094_b(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPushMatrix();
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA(255, 255, 255, 32);
        double d = -par1Entity.width / 2.0F;
        double d1 = -par1Entity.width / 2.0F;
        double d2 = par1Entity.width / 2.0F;
        double d3 = -par1Entity.width / 2.0F;
        double d4 = -par1Entity.width / 2.0F;
        double d5 = par1Entity.width / 2.0F;
        double d6 = par1Entity.width / 2.0F;
        double d7 = par1Entity.width / 2.0F;
        double d8 = par1Entity.height;
        tessellator.addVertex(par2 + d, par4 + d8, par6 + d1);
        tessellator.addVertex(par2 + d, par4, par6 + d1);
        tessellator.addVertex(par2 + d2, par4, par6 + d3);
        tessellator.addVertex(par2 + d2, par4 + d8, par6 + d3);
        tessellator.addVertex(par2 + d6, par4 + d8, par6 + d7);
        tessellator.addVertex(par2 + d6, par4, par6 + d7);
        tessellator.addVertex(par2 + d4, par4, par6 + d5);
        tessellator.addVertex(par2 + d4, par4 + d8, par6 + d5);
        tessellator.addVertex(par2 + d2, par4 + d8, par6 + d3);
        tessellator.addVertex(par2 + d2, par4, par6 + d3);
        tessellator.addVertex(par2 + d6, par4, par6 + d7);
        tessellator.addVertex(par2 + d6, par4 + d8, par6 + d7);
        tessellator.addVertex(par2 + d4, par4 + d8, par6 + d5);
        tessellator.addVertex(par2 + d4, par4, par6 + d5);
        tessellator.addVertex(par2 + d, par4, par6 + d1);
        tessellator.addVertex(par2 + d, par4 + d8, par6 + d1);
        tessellator.draw();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }

    /**
     * World sets this RenderManager's worldObj to the world provided
     */
    public void set(World par1World)
    {
        worldObj = par1World;
    }

    public double getDistanceToCamera(double par1, double par3, double par5)
    {
        double d = par1 - viewerPosX;
        double d1 = par3 - viewerPosY;
        double d2 = par5 - viewerPosZ;
        return d * d + d1 * d1 + d2 * d2;
    }

    /**
     * Returns the font renderer
     */
    public FontRenderer getFontRenderer()
    {
        return fontRenderer;
    }

    public void updateIcons(IconRegister par1IconRegister)
    {
        Render render;

        for (Iterator iterator = entityRenderMap.values().iterator(); iterator.hasNext(); render.updateIcons(par1IconRegister))
        {
            render = (Render)iterator.next();
        }
    }
}
