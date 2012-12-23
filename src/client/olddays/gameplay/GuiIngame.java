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
    public static boolean score = false;
    public static String version = "OFF";

    private static final RenderItem itemRenderer = new RenderItem();
    private final Random rand = new Random();
    private final Minecraft mc;

    /** ChatGUI instance that retains all previous chat data */
    private final GuiNewChat persistantChatGUI;
    private int updateCounter;

    /** The string specifying which record music is playing */
    private String recordPlaying;

    /** How many ticks the record playing message will be displayed */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    public float prevVignetteBrightness;
    private int field_92017_k;
    private ItemStack field_92016_l;

    public GuiIngame(Minecraft par1Minecraft)
    {
        updateCounter = 0;
        recordPlaying = "";
        recordPlayingUpFor = 0;
        recordIsPlaying = false;
        prevVignetteBrightness = 1.0F;
        mc = par1Minecraft;
        persistantChatGUI = new GuiNewChat(par1Minecraft);
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
            renderVignette(mc.thePlayer.getBrightness(par1), i, j);
        }
        else
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(3);

        if (mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
        {
            renderPumpkinBlur(i, j);
        }

        if (!mc.thePlayer.isPotionActive(Potion.confusion))
        {
            float f = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * par1;

            if (f > 0.0F)
            {
                renderPortalOverlay(f, i, j);
            }
        }

        if (!mc.playerController.func_78747_a())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
            InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
            zLevel = -90F;
            drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
            drawTexturedModalRect((i / 2 - 91 - 1) + inventoryplayer.currentItem * 20, j - 22 - 1, 0, 22, 24, 22);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            boolean flag = (mc.thePlayer.hurtResistantTime / 3) % 2 == 1;

            if (mc.thePlayer.hurtResistantTime < 10)
            {
                flag = false;
            }

            int i1 = mc.thePlayer.getHealth();
            int k2 = mc.thePlayer.prevHealth;
            rand.setSeed(updateCounter * 0x4c627);
            boolean flag2 = false;
            FoodStats foodstats = mc.thePlayer.getFoodStats();
            int j5 = foodstats.getFoodLevel();
            int l5 = foodstats.getPrevFoodLevel();
            mc.mcProfiler.startSection("bossHealth");
            renderBossHealth();
            mc.mcProfiler.endSection();

            if (mc.playerController.shouldDrawHUD())
            {
                int j6 = i / 2 - 91;
                int i7 = i / 2 + 91;
                mc.mcProfiler.startSection("expBar");

                int k8 = j - 32;
                if(!hidexp){
                    if (mc.thePlayer.xpBarCap() > 0)
                    {
                        char c = '\266';
                        int j9 = (int)(mc.thePlayer.experience * (float)(c + 1));
                        int i10 = (j - 32) + 3;
                        drawTexturedModalRect(j6, i10, 0, 64, c, 5);
                        if (j9 > 0)
                        {
                            drawTexturedModalRect(j6, i10, 0, 69, j9, 5);
                        }
                    }
                    k8 = j - 39;
                }
                int k9 = k8 - 10;
                int j10 = mc.thePlayer.getTotalArmorValue();
                int i11 = -1;

                if (mc.thePlayer.isPotionActive(Potion.regeneration))
                {
                    i11 = updateCounter % 25;
                }

                mc.mcProfiler.endStartSection("healthArmor");

                for (int k11 = 0; k11 < 10; k11++)
                {
                    if (j10 > 0)
                    {
                        int j12 = j6 + k11 * 8;
                        int intintintintint = k8 - 10;
                        if(hidehunger){
                            j12 = i7 - k11 * 8 - 9;
                            intintintintint = k8;
                        }

                        if (k11 * 2 + 1 < j10)
                        {
                            drawTexturedModalRect(j12, intintintintint, 34, 9, 9, 9);
                        }

                        if (k11 * 2 + 1 == j10)
                        {
                            if (hidehunger && !fallbacktex){
                                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/olddays/icons.png"));
                                drawTexturedModalRect(j12, intintintintint, 0, 0, 9, 9);
                                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
                            }else{
                                drawTexturedModalRect(j12, intintintintint, 25, 9, 9, 9);
                            }
                        }

                        if (k11 * 2 + 1 > j10)
                        {
                            drawTexturedModalRect(j12, intintintintint, 16, 9, 9, 9);
                        }
                    }

                    int k12 = 16;

                    if (mc.thePlayer.isPotionActive(Potion.poison))
                    {
                        k12 += 36;
                    }
                    else if (mc.thePlayer.isPotionActive(Potion.wither))
                    {
                        k12 += 72;
                    }

                    int j13 = 0;

                    if (flag)
                    {
                        j13 = 1;
                    }

                    int i14 = j6 + k11 * 8;
                    int k14 = k8;

                    if (i1 <= 4)
                    {
                        k14 += rand.nextInt(2);
                    }

                    if (k11 == i11)
                    {
                        k14 -= 2;
                    }

                    byte byte3 = 0;

                    if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    {
                        byte3 = 5;
                    }

                    drawTexturedModalRect(i14, k14, 16 + j13 * 9, 9 * byte3, 9, 9);

                    if (flag)
                    {
                        if (k11 * 2 + 1 < k2)
                        {
                            drawTexturedModalRect(i14, k14, k12 + 54, 9 * byte3, 9, 9);
                        }

                        if (k11 * 2 + 1 == k2)
                        {
                            drawTexturedModalRect(i14, k14, k12 + 63, 9 * byte3, 9, 9);
                        }
                    }

                    if (k11 * 2 + 1 < i1)
                    {
                        drawTexturedModalRect(i14, k14, k12 + 36, 9 * byte3, 9, 9);
                    }

                    if (k11 * 2 + 1 == i1)
                    {
                        drawTexturedModalRect(i14, k14, k12 + 45, 9 * byte3, 9, 9);
                    }
                }

                mc.mcProfiler.endStartSection("food");

                for (int l11 = 0; l11 < 10; l11++)
                {
                    if (hidehunger){
                        continue;
                    }
                    int l12 = k8;
                    int k13 = 16;
                    byte byte2 = 0;

                    if (mc.thePlayer.isPotionActive(Potion.hunger))
                    {
                        k13 += 36;
                        byte2 = 13;
                    }

                    if (mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (j5 * 3 + 1) == 0)
                    {
                        l12 += rand.nextInt(3) - 1;
                    }

                    if (flag2)
                    {
                        byte2 = 1;
                    }

                    int l14 = i7 - l11 * 8 - 9;
                    drawTexturedModalRect(l14, l12, 16 + byte2 * 9, 27, 9, 9);

                    if (flag2)
                    {
                        if (l11 * 2 + 1 < l5)
                        {
                            drawTexturedModalRect(l14, l12, k13 + 54, 27, 9, 9);
                        }

                        if (l11 * 2 + 1 == l5)
                        {
                            drawTexturedModalRect(l14, l12, k13 + 63, 27, 9, 9);
                        }
                    }

                    if (l11 * 2 + 1 < j5)
                    {
                        drawTexturedModalRect(l14, l12, k13 + 36, 27, 9, 9);
                    }

                    if (l11 * 2 + 1 == j5)
                    {
                        drawTexturedModalRect(l14, l12, k13 + 45, 27, 9, 9);
                    }
                }

                mc.mcProfiler.endStartSection("air");

                if (mc.thePlayer.isInsideOfMaterial(Material.water))
                {
                    int i12 = mc.thePlayer.getAir();
                    int i13 = MathHelper.ceiling_double_int(((double)(i12 - 2) * 10D) / 300D);
                    int l13 = MathHelper.ceiling_double_int(((double)i12 * 10D) / 300D) - i13;

                    for (int j14 = 0; j14 < i13 + l13; j14++)
                    {
                        if (j14 < i13)
                        {
                            if(hidehunger){
                                drawTexturedModalRect(j6 + j14 * 8, k9, 16, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(i7 - j14 * 8 - 9, k9, 16, 18, 9, 9);
                            }
                        }
                        else
                        {
                            if(hidehunger){
                                drawTexturedModalRect(j6 + j14 * 8, k9, 25, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(i7 - j14 * 8 - 9, k9, 25, 18, 9, 9);
                            }
                        }
                    }
                }

                mc.mcProfiler.endSection();
            }

            GL11.glDisable(GL11.GL_BLEND);
            mc.mcProfiler.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int k6 = 0; k6 < 9; k6++)
            {
                int j7 = (i / 2 - 90) + k6 * 20 + 2;
                int i8 = j - 16 - 3;
                renderInventorySlot(k6, j7, i8, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            mc.mcProfiler.endSection();
        }

        if (mc.thePlayer.getSleepTimer() > 0)
        {
            mc.mcProfiler.startSection("sleep");
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int k = mc.thePlayer.getSleepTimer();
            float f1 = (float)k / 100F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F - (float)(k - 100) / 10F;
            }

            int j1 = (int)(220F * f1) << 24 | 0x101020;
            drawRect(0, 0, i, j, j1);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            mc.mcProfiler.endSection();
        }

        if (mc.playerController.func_78763_f() && mc.thePlayer.experienceLevel > 0 && !hidexp)
        {
            mc.mcProfiler.startSection("expLevel");
            boolean flag1 = false;
            int k1 = flag1 ? 0xffffff : 0x80ff20;
            String s2 = (new StringBuilder()).append("").append(mc.thePlayer.experienceLevel).toString();
            int k3 = (i - fontrenderer.getStringWidth(s2)) / 2;
            int k4 = j - 31 - 4;
            fontrenderer.drawString(s2, k3 + 1, k4, 0);
            fontrenderer.drawString(s2, k3 - 1, k4, 0);
            fontrenderer.drawString(s2, k3, k4 + 1, 0);
            fontrenderer.drawString(s2, k3, k4 - 1, 0);
            fontrenderer.drawString(s2, k3, k4, k1);
            mc.mcProfiler.endSection();
        }

        if (mc.gameSettings.field_92117_D)
        {
            mc.mcProfiler.startSection("toolHighlight");

            if (field_92017_k > 0 && field_92016_l != null)
            {
                String s = field_92016_l.getDisplayName();
                int l1 = (i - fontrenderer.getStringWidth(s)) / 2;
                int l2 = j - 59;

                if (!mc.playerController.shouldDrawHUD())
                {
                    l2 += 14;
                }

                int i4 = (int)(((float)field_92017_k * 256F) / 10F);

                if (i4 > 255)
                {
                    i4 = 255;
                }

                if (i4 > 0)
                {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    fontrenderer.drawStringWithShadow(s, l1, l2, 0xffffff + (i4 << 24));
                    GL11.glDisable(GL11.GL_BLEND);
                    GL11.glPopMatrix();
                }
            }

            mc.mcProfiler.endSection();
        }

        if (mc.isDemo())
        {
            mc.mcProfiler.startSection("demo");
            String s1 = "";

            if (mc.theWorld.getTotalWorldTime() >= 0x1d6b4L)
            {
                s1 = StatCollector.translateToLocal("demo.demoExpired");
            }
            else
            {
                s1 = String.format(StatCollector.translateToLocal("demo.remainingTime"), new Object[]
                        {
                            StringUtils.ticksToElapsedTime((int)(0x1d6b4L - mc.theWorld.getTotalWorldTime()))
                        });
            }

            int i2 = fontrenderer.getStringWidth(s1);
            fontrenderer.drawStringWithShadow(s1, i - i2 - 10, 5, 0xffffff);
            mc.mcProfiler.endSection();
        }

        if (mc.gameSettings.showDebugInfo && !nodebug)
        {
            mc.mcProfiler.startSection("debug");
            GL11.glPushMatrix();
            String str = version.contains(":") ? version.split(":", 2)[1] : version;
            if (str.equals("OFF")){
                str = "Minecraft "+(new CallableMinecraftVersion(null)).minecraftVersion();
            }
            fontrenderer.drawStringWithShadow((new StringBuilder()).append(str).append(" (").append(mc.debug).append(")").toString(), 2, 2, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getWorldProviderName(), 2, 42, 0xffffff);
            long l = Runtime.getRuntime().maxMemory();
            long l3 = Runtime.getRuntime().totalMemory();
            long l4 = Runtime.getRuntime().freeMemory();
            long l6 = l3 - l4;
            String s3 = (new StringBuilder()).append("Used memory: ").append((l6 * 100L) / l).append("% (").append(l6 / 1024L / 1024L).append("MB) of ").append(l / 1024L / 1024L).append("MB").toString();
            drawString(fontrenderer, s3, i - fontrenderer.getStringWidth(s3) - 2, 2, 0xe0e0e0);
            s3 = (new StringBuilder()).append("Allocated memory: ").append((l3 * 100L) / l).append("% (").append(l3 / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s3, i - fontrenderer.getStringWidth(s3) - 2, 12, 0xe0e0e0);
            int l8 = MathHelper.floor_double(mc.thePlayer.posX);
            int l9 = MathHelper.floor_double(mc.thePlayer.posY);
            int k10 = MathHelper.floor_double(mc.thePlayer.posZ);
            drawString(fontrenderer, String.format("x: %.5f (%d) // c: %d (%d)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.posX), Integer.valueOf(l8), Integer.valueOf(l8 >> 4), Integer.valueOf(l8 & 0xf)
                    }), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.boundingBox.minY), Double.valueOf(mc.thePlayer.posY)
                    }), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, String.format("z: %.5f (%d) // c: %d (%d)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.posZ), Integer.valueOf(k10), Integer.valueOf(k10 >> 4), Integer.valueOf(k10 & 0xf)
                    }), 2, 80, 0xe0e0e0);
            int j11 = MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(j11).append(" (").append(Direction.directions[j11]).append(") / ").append(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw)).toString(), 2, 88, 0xe0e0e0);

            if (mc.theWorld != null && mc.theWorld.blockExists(l8, l9, k10))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(l8, k10);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.getTopFilledSegment() + 15).append(" b: ").append(chunk.getBiomeGenForWorldCoords(l8 & 0xf, k10 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, l8 & 0xf, l9, k10 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, l8 & 0xf, l9, k10 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(l8 & 0xf, l9, k10 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            drawString(fontrenderer, String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[]
                    {
                        Float.valueOf(mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(mc.thePlayer.onGround), Integer.valueOf(mc.theWorld.getHeightValue(l8, k10))
                    }), 2, 104, 0xe0e0e0);

            if (!mc.theWorld.isRemote && mc.enableSP && mc.sspoptions.getDebugSeed())
            {
                drawString(fontrenderer, (new StringBuilder()).append("Seed: ").append(mc.theWorld.getSeed()).toString(), 2, 120, 0xe0e0e0);
            }
            GL11.glPopMatrix();
            mc.mcProfiler.endSection();
        }else{
            String str = version.contains(":") ? version.split(":", 2)[0] : version;
            if (str.equals("OFF")){
                str = "";
            }
            fontrenderer.drawStringWithShadow(str, 2, 2, 0xffffff);
            if (score){
                String s1 = StatCollector.translateToLocal("deathScreen.score")+": "+mc.thePlayer.getScore();
                String s2 = StatCollector.translateToLocal("deathScreen.score")+": §e"+mc.thePlayer.getScore();
                fontrenderer.drawStringWithShadow(s2, i - fontrenderer.getStringWidth(s1) - 2, 2, 0xffffff);
            }
        }

        if (recordPlayingUpFor > 0)
        {
            mc.mcProfiler.startSection("overlayMessage");
            float f2 = (float)recordPlayingUpFor - par1;
            int j2 = (int)((f2 * 256F) / 20F);

            if (j2 > 255)
            {
                j2 = 255;
            }

            if (j2 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 48, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int i3 = 0xffffff;

                if (recordIsPlaying)
                {
                    i3 = Color.HSBtoRGB(f2 / 50F, 0.7F, 0.6F) & 0xffffff;
                }

                fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, i3 + (j2 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            mc.mcProfiler.endSection();
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j - 48, 0.0F);
        mc.mcProfiler.startSection("chat");
        persistantChatGUI.drawChat(updateCounter);
        mc.mcProfiler.endSection();
        GL11.glPopMatrix();

        if ((mc.thePlayer instanceof EntityClientPlayerMP) && mc.gameSettings.keyBindPlayerList.pressed && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1))
        {
            mc.mcProfiler.startSection("playerList");
            int j3 = 0;
            java.util.List list = null;
            if (mc.enableSP){
                j3 = 0;
            }else{
                NetClientHandler netclienthandler = mc.thePlayer.sendQueue;
                list = netclienthandler.playerInfoList;
                j3 = netclienthandler.currentServerMaxPlayers;
            }
            int j4 = j3;
            int i5 = 1;

            for (; j4 > 20; j4 = ((j3 + i5) - 1) / i5)
            {
                i5++;
            }

            int k5 = 300 / i5;

            if (k5 > 150)
            {
                k5 = 150;
            }

            int i6 = (i - i5 * k5) / 2;
            byte byte0 = 10;
            drawRect(i6 - 1, byte0 - 1, i6 + k5 * i5, byte0 + 9 * j4, 0x80000000);

            for (int k7 = 0; k7 < j3; k7++)
            {
                int j8 = i6 + (k7 % i5) * k5;
                int i9 = byte0 + (k7 / i5) * 9;
                drawRect(j8, i9, (j8 + k5) - 1, i9 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (k7 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(k7);
                fontrenderer.drawStringWithShadow(guiplayerinfo.name, j8, i9, 0xffffff);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/icons.png"));
                int l10 = 0;
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
                drawTexturedModalRect((j8 + k5) - 12, i9, 0 + l10 * 10, 176 + byte1 * 8, 10, 8);
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
        if (BossStatus.bossName == null || BossStatus.statusBarLenght <= 0)
        {
            return;
        }

        BossStatus.statusBarLenght--;
        FontRenderer fontrenderer = mc.fontRenderer;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        char c = '\266';
        int j = i / 2 - c / 2;
        int k = (int)(BossStatus.healthScale * (float)(c + 1));
        byte byte0 = 12;
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);

        if (k > 0)
        {
            drawTexturedModalRect(j, byte0, 0, 79, k, 5);
        }

        String s = BossStatus.bossName;
        fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, byte0 - 10, 0xffffff);
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
        ItemStack itemstack = mc.thePlayer.inventory.mainInventory[par1];

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

        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);

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

        if (mc.thePlayer != null)
        {
            ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();

            if (itemstack == null)
            {
                field_92017_k = 0;
            }
            else if (field_92016_l == null || itemstack.itemID != field_92016_l.itemID || !ItemStack.areItemStackTagsEqual(itemstack, field_92016_l) || !itemstack.isItemStackDamageable() && itemstack.getItemDamage() != field_92016_l.getItemDamage())
            {
                field_92017_k = 40;
            }
            else if (field_92017_k > 0)
            {
                field_92017_k--;
            }

            field_92016_l = itemstack;
        }
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        recordPlaying = (new StringBuilder()).append("Now playing: ").append(par1Str).toString();
        recordPlayingUpFor = 60;
        recordIsPlaying = true;
    }

    /**
     * returns a pointer to the persistant Chat GUI, containing all previous chat messages and such
     */
    public GuiNewChat getChatGUI()
    {
        return persistantChatGUI;
    }

    public int getUpdateCounter()
    {
        return updateCounter;
    }
}
