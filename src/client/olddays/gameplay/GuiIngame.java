package net.minecraft.src;

import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngame extends Gui
{
    public static boolean hidexp = false;
    public static boolean hidehunger = false;
    public static boolean nodebug = false;
    public static boolean fallbacktex = false;
    public static String version = "OFF";

    private static final RenderItem itemRenderer = new RenderItem();
    private final Random rand = new Random();
    private final Minecraft mc;
    private final GuiNewChat field_73840_e;
    private int updateCounter;

    /** The string specifying which record music is playing */
    private String recordPlaying;

    /** How many ticks the record playing message will be displayed */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    public float prevVignetteBrightness;

    public GuiIngame(Minecraft par1Minecraft)
    {
        updateCounter = 0;
        recordPlaying = "";
        recordPlayingUpFor = 0;
        recordIsPlaying = false;
        prevVignetteBrightness = 1.0F;
        mc = par1Minecraft;
        field_73840_e = new GuiNewChat(par1Minecraft);
    }

    /**
     * Render the ingame overlay with quick icon bar, ...
     */
    public void renderGameOverlay(float par1, boolean par2, int par3, int par4)
    {
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = mc.fontRenderer;
        mc.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);

        if (Minecraft.isFancyGraphicsEnabled())
        {
            renderVignette(mc.field_71439_g.getBrightness(par1), i, j);
        }
        else
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        ItemStack itemstack = mc.field_71439_g.inventory.armorItemInSlot(3);

        if (mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
        {
            renderPumpkinBlur(i, j);
        }

        if (!mc.field_71439_g.isPotionActive(Potion.confusion))
        {
            float f = mc.field_71439_g.prevTimeInPortal + (mc.field_71439_g.timeInPortal - mc.field_71439_g.prevTimeInPortal) * par1;

            if (f > 0.0F)
            {
                renderPortalOverlay(f, i, j);
            }
        }

        if (!mc.field_71442_b.func_78747_a())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
            InventoryPlayer inventoryplayer = mc.field_71439_g.inventory;
            zLevel = -90F;
            drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
            drawTexturedModalRect((i / 2 - 91 - 1) + inventoryplayer.currentItem * 20, j - 22 - 1, 0, 22, 24, 22);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            boolean flag = (mc.field_71439_g.heartsLife / 3) % 2 == 1;

            if (mc.field_71439_g.heartsLife < 10)
            {
                flag = false;
            }

            int i1 = mc.field_71439_g.getHealth();
            int j2 = mc.field_71439_g.prevHealth;
            rand.setSeed(updateCounter * 0x4c627);
            boolean flag2 = false;
            FoodStats foodstats = mc.field_71439_g.getFoodStats();
            int j4 = foodstats.getFoodLevel();
            int i5 = foodstats.getPrevFoodLevel();
            mc.field_71424_I.startSection("bossHealth");
            renderBossHealth();
            mc.field_71424_I.endSection();

            if (mc.field_71442_b.shouldDrawHUD())
            {
                int k5 = i / 2 - 91;
                int j6 = i / 2 + 91;
                mc.field_71424_I.startSection("expBar");

                int l7 = j - 32;
                if(!hidexp){
                    if (mc.field_71439_g.xpBarCap() > 0)
                    {
                        char c = '\266';
                        int k8 = (int)(mc.field_71439_g.experience * (float)(c + 1));
                        int j9 = (j - 32) + 3;
                        drawTexturedModalRect(k5, j9, 0, 64, c, 5);
                        if (k8 > 0)
                        {
                            drawTexturedModalRect(k5, j9, 0, 69, k8, 5);
                        }
                    }
                    l7 = j - 39;
                }
                int l8 = l7 - 10;
                int k9 = mc.field_71439_g.getTotalArmorValue();
                int j10 = -1;

                if (mc.field_71439_g.isPotionActive(Potion.regeneration))
                {
                    j10 = updateCounter % 25;
                }

                mc.field_71424_I.endStartSection("healthArmor");

                for (int k10 = 0; k10 < 10; k10++)
                {
                    if (k9 > 0)
                    {
                        int j11 = k5 + k10 * 8;
                        int intintintintint = l7 - 10;
                        if(hidehunger){
                            j11 = j6 - k10 * 8 - 9;
                            intintintintint = l7;
                        }

                        if (k10 * 2 + 1 < k9)
                        {
                            drawTexturedModalRect(j11, intintintintint, 34, 9, 9, 9);
                        }

                        if (k10 * 2 + 1 == k9)
                        {
                            if (hidehunger && !fallbacktex){
                                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/olddays/icons.png"));
                                drawTexturedModalRect(j11, intintintintint, 0, 0, 9, 9);
                                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
                            }else{
                                drawTexturedModalRect(j11, intintintintint, 25, 9, 9, 9);
                            }
                        }

                        if (k10 * 2 + 1 > k9)
                        {
                            drawTexturedModalRect(j11, intintintintint, 16, 9, 9, 9);
                        }
                    }

                    int k11 = 16;

                    if (mc.field_71439_g.isPotionActive(Potion.poison))
                    {
                        k11 += 36;
                    }

                    int j12 = 0;

                    if (flag)
                    {
                        j12 = 1;
                    }

                    int i13 = k5 + k10 * 8;
                    int k13 = l7;

                    if (i1 <= 4)
                    {
                        k13 += rand.nextInt(2);
                    }

                    if (k10 == j10)
                    {
                        k13 -= 2;
                    }

                    byte byte3 = 0;

                    if (mc.field_71441_e.getWorldInfo().isHardcoreModeEnabled())
                    {
                        byte3 = 5;
                    }

                    drawTexturedModalRect(i13, k13, 16 + j12 * 9, 9 * byte3, 9, 9);

                    if (flag)
                    {
                        if (k10 * 2 + 1 < j2)
                        {
                            drawTexturedModalRect(i13, k13, k11 + 54, 9 * byte3, 9, 9);
                        }

                        if (k10 * 2 + 1 == j2)
                        {
                            drawTexturedModalRect(i13, k13, k11 + 63, 9 * byte3, 9, 9);
                        }
                    }

                    if (k10 * 2 + 1 < i1)
                    {
                        drawTexturedModalRect(i13, k13, k11 + 36, 9 * byte3, 9, 9);
                    }

                    if (k10 * 2 + 1 == i1)
                    {
                        drawTexturedModalRect(i13, k13, k11 + 45, 9 * byte3, 9, 9);
                    }
                }

                mc.field_71424_I.endStartSection("food");

                if(!hidehunger){
                    for (int l10 = 0; l10 < 10; l10++)
                    {
                        int l11 = l7;
                        int k12 = 16;
                        byte byte2 = 0;

                        if (mc.field_71439_g.isPotionActive(Potion.hunger))
                        {
                            k12 += 36;
                            byte2 = 13;
                        }

                        if (mc.field_71439_g.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (j4 * 3 + 1) == 0)
                        {
                            l11 += rand.nextInt(3) - 1;
                        }

                        if (flag2)
                        {
                            byte2 = 1;
                        }

                        int l13 = j6 - l10 * 8 - 9;
                        drawTexturedModalRect(l13, l11, 16 + byte2 * 9, 27, 9, 9);

                        if (flag2)
                        {
                            if (l10 * 2 + 1 < i5)
                            {
                                drawTexturedModalRect(l13, l11, k12 + 54, 27, 9, 9);
                            }

                            if (l10 * 2 + 1 == i5)
                            {
                                drawTexturedModalRect(l13, l11, k12 + 63, 27, 9, 9);
                            }
                        }

                        if (l10 * 2 + 1 < j4)
                        {
                            drawTexturedModalRect(l13, l11, k12 + 36, 27, 9, 9);
                        }

                        if (l10 * 2 + 1 == j4)
                        {
                            drawTexturedModalRect(l13, l11, k12 + 45, 27, 9, 9);
                        }
                    }
                }

                mc.field_71424_I.endStartSection("air");

                if (mc.field_71439_g.isInsideOfMaterial(Material.water))
                {
                    int i11 = mc.field_71439_g.getAir();
                    int i12 = MathHelper.func_76143_f(((double)(i11 - 2) * 10D) / 300D);
                    int l12 = MathHelper.func_76143_f(((double)i11 * 10D) / 300D) - i12;

                    for (int j13 = 0; j13 < i12 + l12; j13++)
                    {
                        if (j13 < i12)
                        {
                            if(hidehunger){
                                drawTexturedModalRect(k5 + j13 * 8, l8, 16, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(j6 - j13 * 8 - 9, l8, 16, 18, 9, 9);
                            }
                        }
                        else
                        {
                            if(hidehunger){
                                drawTexturedModalRect(k5 + j13 * 8, l8, 25, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(j6 - j13 * 8 - 9, l8, 25, 18, 9, 9);
                            }
                        }
                    }
                }

                mc.field_71424_I.endSection();
            }

            GL11.glDisable(GL11.GL_BLEND);
            mc.field_71424_I.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int i6 = 0; i6 < 9; i6++)
            {
                int k6 = (i / 2 - 90) + i6 * 20 + 2;
                int j7 = j - 16 - 3;
                renderInventorySlot(i6, k6, j7, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            mc.field_71424_I.endSection();
        }

        if (mc.field_71439_g.getSleepTimer() > 0)
        {
            mc.field_71424_I.startSection("sleep");
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int k = mc.field_71439_g.getSleepTimer();
            float f1 = (float)k / 100F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F - (float)(k - 100) / 10F;
            }

            int j1 = (int)(220F * f1) << 24 | 0x101020;
            drawRect(0, 0, i, j, j1);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            mc.field_71424_I.endSection();
        }

        if (mc.field_71442_b.func_78763_f() && mc.field_71439_g.experienceLevel > 0 && !hidexp)
        {
            mc.field_71424_I.startSection("expLevel");
            boolean flag1 = false;
            int k1 = flag1 ? 0xffffff : 0x80ff20;
            String s1 = (new StringBuilder()).append("").append(mc.field_71439_g.experienceLevel).toString();
            int j3 = (i - fontrenderer.getStringWidth(s1)) / 2;
            int l3 = j - 31 - 4;
            fontrenderer.drawString(s1, j3 + 1, l3, 0);
            fontrenderer.drawString(s1, j3 - 1, l3, 0);
            fontrenderer.drawString(s1, j3, l3 + 1, 0);
            fontrenderer.drawString(s1, j3, l3 - 1, 0);
            fontrenderer.drawString(s1, j3, l3, k1);
            mc.field_71424_I.endSection();
        }

        if (mc.func_71355_q())
        {
            mc.field_71424_I.startSection("demo");
            String s = "";

            if (mc.field_71441_e.getWorldTime() >= 0x1d6b4L)
            {
                s = StatCollector.translateToLocal("demo.demoExpired");
            }
            else
            {
                s = String.format(StatCollector.translateToLocal("demo.remainingTime"), new Object[]
                        {
                            StringUtils.func_76337_a((int)(0x1d6b4L - mc.field_71441_e.getWorldTime()))
                        });
            }

            int l1 = fontrenderer.getStringWidth(s);
            fontrenderer.drawStringWithShadow(s, i - l1 - 10, 5, 0xffffff);
            mc.field_71424_I.endSection();
        }

        if (mc.gameSettings.showDebugInfo && !nodebug)
        {
            mc.field_71424_I.startSection("debug");
            GL11.glPushMatrix();
            fontrenderer.drawStringWithShadow((new StringBuilder()).append(version.equals("OFF") ? "Minecraft 1.3.1" : version).append(" (").append(mc.debug).append(")").toString(), 2, 2, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getWorldProviderName(), 2, 42, 0xffffff);
            long l = Runtime.getRuntime().maxMemory();
            long l2 = Runtime.getRuntime().totalMemory();
            long l4 = Runtime.getRuntime().freeMemory();
            long l5 = l2 - l4;
            String s2 = (new StringBuilder()).append("Used memory: ").append((l5 * 100L) / l).append("% (").append(l5 / 1024L / 1024L).append("MB) of ").append(l / 1024L / 1024L).append("MB").toString();
            drawString(fontrenderer, s2, i - fontrenderer.getStringWidth(s2) - 2, 2, 0xe0e0e0);
            s2 = (new StringBuilder()).append("Allocated memory: ").append((l2 * 100L) / l).append("% (").append(l2 / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s2, i - fontrenderer.getStringWidth(s2) - 2, 12, 0xe0e0e0);
            drawString(fontrenderer, String.format("x: %.5f", new Object[]
                    {
                        Double.valueOf(mc.field_71439_g.posX)
                    }), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[]
                    {
                        Double.valueOf(mc.field_71439_g.boundingBox.minY), Double.valueOf(mc.field_71439_g.posY)
                    }), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, String.format("z: %.5f", new Object[]
                    {
                        Double.valueOf(mc.field_71439_g.posZ)
                    }), 2, 80, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(MathHelper.floor_double((double)((mc.field_71439_g.rotationYaw * 4F) / 360F) + 0.5D) & 3).toString(), 2, 88, 0xe0e0e0);
            int i8 = MathHelper.floor_double(mc.field_71439_g.posX);
            int i9 = MathHelper.floor_double(mc.field_71439_g.posY);
            int l9 = MathHelper.floor_double(mc.field_71439_g.posZ);

            if (mc.field_71441_e != null && mc.field_71441_e.blockExists(i8, i9, l9))
            {
                Chunk chunk = mc.field_71441_e.getChunkFromBlockCoords(i8, l9);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.getTopFilledSegment() + 15).append(" b: ").append(chunk.getBiomeGenForWorldCoords(i8 & 0xf, l9 & 0xf, mc.field_71441_e.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, i8 & 0xf, i9, l9 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, i8 & 0xf, i9, l9 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(i8 & 0xf, i9, l9 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            drawString(fontrenderer, String.format("ws: %.3f, fs: %.3f, g: %b", new Object[]
                    {
                        Float.valueOf(mc.field_71439_g.capabilities.func_75094_b()), Float.valueOf(mc.field_71439_g.capabilities.func_75093_a()), Boolean.valueOf(mc.field_71439_g.onGround)
                    }), 2, 104, 0xe0e0e0);

            if (!mc.field_71441_e.isRemote && mc.enableSP)
            {
                drawString(fontrenderer, (new StringBuilder()).append("Seed: ").append(mc.field_71441_e.getSeed()).toString(), 2, 120, 0xe0e0e0);
            }
            GL11.glPopMatrix();
            mc.field_71424_I.endSection();
        }else{
            if (!version.equals("OFF")){
                fontrenderer.drawStringWithShadow(version, 2, 2, 0xffffff);   
            }
        }

        if (recordPlayingUpFor > 0)
        {
            mc.field_71424_I.startSection("overlayMessage");
            float f2 = (float)recordPlayingUpFor - par1;
            int i2 = (int)((f2 * 256F) / 20F);

            if (i2 > 255)
            {
                i2 = 255;
            }

            if (i2 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 48, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int k2 = 0xffffff;

                if (recordIsPlaying)
                {
                    k2 = Color.HSBtoRGB(f2 / 50F, 0.7F, 0.6F) & 0xffffff;
                }

                fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, k2 + (i2 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            mc.field_71424_I.endSection();
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j - 48, 0.0F);
        mc.field_71424_I.startSection("chat");
        field_73840_e.func_73762_a(updateCounter);
        mc.field_71424_I.endSection();
        GL11.glPopMatrix();

        if ((mc.field_71439_g instanceof EntityClientPlayerMP) && mc.gameSettings.keyBindPlayerList.pressed && (!mc.func_71387_A() || mc.field_71439_g.sendQueue.playerInfoList.size() > 1))
        {
            mc.field_71424_I.startSection("playerList");
            int i3 = 0;
            java.util.List list = null;
            if (mc.enableSP){
                NetHandlerSP nethandlersp = mc.field_71439_g.sendQueue;
                list = nethandlersp.playerInfoList;
                i3 = nethandlersp.currentServerMaxPlayers;
            }else{
                NetClientHandler netclienthandler = ((EntityClientPlayerMP)mc.field_71439_g).sendQueue;
                list = netclienthandler.playerInfoList;
                i3 = netclienthandler.currentServerMaxPlayers;
            }
            int k3 = i3;
            int i4 = 1;

            for (; k3 > 20; k3 = ((i3 + i4) - 1) / i4)
            {
                i4++;
            }

            int k4 = 300 / i4;

            if (k4 > 150)
            {
                k4 = 150;
            }

            int j5 = (i - i4 * k4) / 2;
            byte byte0 = 10;
            drawRect(j5 - 1, byte0 - 1, j5 + k4 * i4, byte0 + 9 * k3, 0x80000000);

            for (int l6 = 0; l6 < i3; l6++)
            {
                int k7 = j5 + (l6 % i4) * k4;
                int j8 = byte0 + (l6 / i4) * 9;
                drawRect(k7, j8, (k7 + k4) - 1, j8 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (l6 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(l6);
                fontrenderer.drawStringWithShadow(guiplayerinfo.name, k7, j8, 0xffffff);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/icons.png"));
                int i10 = 0;
                byte byte1 = 0;

                if (guiplayerinfo.responseTime < 0)
                {
                    byte1 = 5;
                }
                else if (guiplayerinfo.responseTime < 150)
                {
                    byte1 = 0;
                }
                else if (guiplayerinfo.responseTime < 300)
                {
                    byte1 = 1;
                }
                else if (guiplayerinfo.responseTime < 600)
                {
                    byte1 = 2;
                }
                else if (guiplayerinfo.responseTime < 1000)
                {
                    byte1 = 3;
                }
                else
                {
                    byte1 = 4;
                }

                zLevel += 100F;
                drawTexturedModalRect((k7 + k4) - 12, j8, 0 + i10 * 10, 176 + byte1 * 8, 10, 8);
                zLevel -= 100F;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    private void renderBossHealth()
    {
        if (RenderDragon.entityDragon == null)
        {
            return;
        }

        EntityDragon entitydragon = RenderDragon.entityDragon;
        RenderDragon.entityDragon = null;
        FontRenderer fontrenderer = mc.fontRenderer;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        char c = '\266';
        int j = i / 2 - c / 2;
        int k = (int)(((float)entitydragon.getDragonHealth() / (float)entitydragon.getMaxHealth()) * (float)(c + 1));
        byte byte0 = 12;
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);

        if (k > 0)
        {
            drawTexturedModalRect(j, byte0, 0, 79, k, 5);
        }

        String s = "Boss health";
        fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, byte0 - 10, 0xff00ff);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
    }

    private void renderPumpkinBlur(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, par2, -90D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(par1, par2, -90D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(par1, 0.0D, -90D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the vignette. Args: vignetteBrightness, width, height
     */
    private void renderVignette(float par1, int par2, int par3)
    {
        par1 = 1.0F - par1;

        if (par1 < 0.0F)
        {
            par1 = 0.0F;
        }

        if (par1 > 1.0F)
        {
            par1 = 1.0F;
        }

        prevVignetteBrightness += (double)(par1 - prevVignetteBrightness) * 0.01D;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glColor4f(prevVignetteBrightness, prevVignetteBrightness, prevVignetteBrightness, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, par3, -90D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(par2, par3, -90D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(par2, 0.0D, -90D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Renders the portal overlay. Args: portalStrength, width, height
     */
    private void renderPortalOverlay(float par1, int par2, int par3)
    {
        if (par1 < 1.0F)
        {
            par1 *= par1;
            par1 *= par1;
            par1 = par1 * 0.8F + 0.2F;
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, par1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
        float f = (float)(Block.portal.blockIndexInTexture % 16) / 16F;
        float f1 = (float)(Block.portal.blockIndexInTexture / 16) / 16F;
        float f2 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16F;
        float f3 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, par3, -90D, f, f3);
        tessellator.addVertexWithUV(par2, par3, -90D, f2, f3);
        tessellator.addVertexWithUV(par2, 0.0D, -90D, f2, f1);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, f, f1);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick
     */
    private void renderInventorySlot(int par1, int par2, int par3, float par4)
    {
        ItemStack itemstack = mc.field_71439_g.inventory.mainInventory[par1];

        if (itemstack == null)
        {
            return;
        }

        float f = (float)itemstack.animationsToGo - par4;

        if (f > 0.0F)
        {
            GL11.glPushMatrix();
            float f1 = 1.0F + f / 5F;
            GL11.glTranslatef(par2 + 8, par3 + 12, 0.0F);
            GL11.glScalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(par2 + 8), -(par3 + 12), 0.0F);
        }

        itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);

        if (f > 0.0F)
        {
            GL11.glPopMatrix();
        }

        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick()
    {
        if (recordPlayingUpFor > 0)
        {
            recordPlayingUpFor--;
        }

        updateCounter++;
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        recordPlaying = (new StringBuilder()).append("Now playing: ").append(par1Str).toString();
        recordPlayingUpFor = 60;
        recordIsPlaying = true;
    }

    public GuiNewChat func_73827_b()
    {
        return field_73840_e;
    }

    public int func_73834_c()
    {
        return updateCounter;
    }
}
