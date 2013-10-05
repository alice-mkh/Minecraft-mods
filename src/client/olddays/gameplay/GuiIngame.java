package net.minecraft.src;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import net.minecraft.src.ssp.SSPOptions;

public class GuiIngame extends Gui
{
    public static boolean hidexp = false;
    public static boolean hidehunger = false;
    public static boolean nodebug = false;
    public static boolean fallbacktex = false;
    public static boolean score = false;
    public static String version = "OFF";
    private static final ResourceLocation customArmorResource = new ResourceLocation("olddays/icons.png");

    private static final ResourceLocation vignetteTexPath = new ResourceLocation("textures/misc/vignette.png");
    private static final ResourceLocation widgetsTexPath = new ResourceLocation("textures/gui/widgets.png");
    private static final ResourceLocation pumpkinBlurTexPath = new ResourceLocation("textures/misc/pumpkinblur.png");
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

    /** Remaining ticks the item highlight should be visible */
    private int remainingHighlightTicks;

    /** The ItemStack that is currently being highlighted */
    private ItemStack highlightingItemStack;

    public GuiIngame(Minecraft par1Minecraft)
    {
        recordPlaying = "";
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
                func_130015_b(f, i, j);
            }
        }

        if (!mc.playerController.enableEverythingIsScrewedUpMode())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(widgetsTexPath);
            InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
            zLevel = -90F;
            drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
            drawTexturedModalRect((i / 2 - 91 - 1) + inventoryplayer.currentItem * 20, j - 22 - 1, 0, 22, 24, 22);
            mc.getTextureManager().bindTexture(icons);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            mc.mcProfiler.startSection("bossHealth");
            renderBossHealth();
            mc.mcProfiler.endSection();

            if (mc.playerController.shouldDrawHUD())
            {
                func_110327_a(i, j);
            }

            GL11.glDisable(GL11.GL_BLEND);
            mc.mcProfiler.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int i1 = 0; i1 < 9; i1++)
            {
                int k1 = (i / 2 - 90) + i1 * 20 + 2;
                int j2 = j - 16 - 3;
                renderInventorySlot(i1, k1, j2, par1);
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

            int l1 = (int)(220F * f1) << 24 | 0x101020;
            drawRect(0, 0, i, j, l1);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            mc.mcProfiler.endSection();
        }

        int l = 0xffffff;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int j1 = i / 2 - 91;

        if (mc.thePlayer.isRidingHorse())
        {
            mc.mcProfiler.startSection("jumpBar");
            mc.getTextureManager().bindTexture(Gui.icons);
            float f2 = mc.thePlayer.getHorseJumpPower();
            char c = '\266';
            int k3 = (int)(f2 * (float)(c + 1));
            int i5 = (j - 32) + 3;
            drawTexturedModalRect(j1, i5, 0, 84, c, 5);

            if (k3 > 0)
            {
                drawTexturedModalRect(j1, i5, 0, 89, k3, 5);
            }

            mc.mcProfiler.endSection();
        }
        else if (mc.playerController.func_78763_f() && !hidexp)
        {
            mc.mcProfiler.startSection("expBar");
            mc.getTextureManager().bindTexture(Gui.icons);
            int i2 = mc.thePlayer.xpBarCap();

            if (i2 > 0)
            {
                char c1 = '\266';
                int l3 = (int)(mc.thePlayer.experience * (float)(c1 + 1));
                int j5 = (j - 32) + 3;
                drawTexturedModalRect(j1, j5, 0, 64, c1, 5);

                if (l3 > 0)
                {
                    drawTexturedModalRect(j1, j5, 0, 69, l3, 5);
                }
            }

            mc.mcProfiler.endSection();

            if (mc.thePlayer.experienceLevel > 0)
            {
                mc.mcProfiler.startSection("expLevel");
                boolean flag = false;
                int i4 = flag ? 0xffffff : 0x80ff20;
                String s2 = (new StringBuilder()).append("").append(mc.thePlayer.experienceLevel).toString();
                int i6 = (i - fontrenderer.getStringWidth(s2)) / 2;
                int k6 = j - 31 - 4;
                boolean flag1 = false;
                fontrenderer.drawString(s2, i6 + 1, k6, 0);
                fontrenderer.drawString(s2, i6 - 1, k6, 0);
                fontrenderer.drawString(s2, i6, k6 + 1, 0);
                fontrenderer.drawString(s2, i6, k6 - 1, 0);
                fontrenderer.drawString(s2, i6, k6, i4);
                mc.mcProfiler.endSection();
            }
        }

        if (mc.gameSettings.heldItemTooltips)
        {
            mc.mcProfiler.startSection("toolHighlight");

            if (remainingHighlightTicks > 0 && highlightingItemStack != null)
            {
                String s = highlightingItemStack.getDisplayName();
                int k2 = (i - fontrenderer.getStringWidth(s)) / 2;
                int j4 = j - 59;

                if (!mc.playerController.shouldDrawHUD())
                {
                    j4 += 14;
                }

                int k5 = (int)(((float)remainingHighlightTicks * 256F) / 10F);

                if (k5 > 255)
                {
                    k5 = 255;
                }

                if (k5 > 0)
                {
                    GL11.glPushMatrix();
                    GL11.glEnable(GL11.GL_BLEND);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    fontrenderer.drawStringWithShadow(s, k2, j4, 0xffffff + (k5 << 24));
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
                s1 = I18n.getString("demo.demoExpired");
            }
            else
            {
                s1 = I18n.getStringParams("demo.remainingTime", new Object[]
                        {
                            StringUtils.ticksToElapsedTime((int)(0x1d6b4L - mc.theWorld.getTotalWorldTime()))
                        });
            }

            int i3 = fontrenderer.getStringWidth(s1);
            fontrenderer.drawStringWithShadow(s1, i - i3 - 10, 5, 0xffffff);
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
            fontrenderer.drawStringWithShadow(str+" ("+mc.debug+")", 2, 2, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getWorldProviderName(), 2, 42, 0xffffff);
            long l2 = Runtime.getRuntime().maxMemory();
            long l4 = Runtime.getRuntime().totalMemory();
            long l6 = Runtime.getRuntime().freeMemory();
            long l7 = l4 - l6;
            String s3 = (new StringBuilder()).append("Used memory: ").append((l7 * 100L) / l2).append("% (").append(l7 / 1024L / 1024L).append("MB) of ").append(l2 / 1024L / 1024L).append("MB").toString();
            int i8 = 0xe0e0e0;
            drawString(fontrenderer, s3, i - fontrenderer.getStringWidth(s3) - 2, 2, 0xe0e0e0);
            s3 = (new StringBuilder()).append("Allocated memory: ").append((l4 * 100L) / l2).append("% (").append(l4 / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s3, i - fontrenderer.getStringWidth(s3) - 2, 12, 0xe0e0e0);
            int k8 = MathHelper.floor_double(mc.thePlayer.posX);
            int i9 = MathHelper.floor_double(mc.thePlayer.posY);
            int k9 = MathHelper.floor_double(mc.thePlayer.posZ);
            drawString(fontrenderer, String.format("x: %.5f (%d) // c: %d (%d)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.posX), Integer.valueOf(k8), Integer.valueOf(k8 >> 4), Integer.valueOf(k8 & 0xf)
                    }), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.boundingBox.minY), Double.valueOf(mc.thePlayer.posY)
                    }), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, String.format("z: %.5f (%d) // c: %d (%d)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.posZ), Integer.valueOf(k9), Integer.valueOf(k9 >> 4), Integer.valueOf(k9 & 0xf)
                    }), 2, 80, 0xe0e0e0);
            int l9 = MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(l9).append(" (").append(Direction.directions[l9]).append(") / ").append(MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw)).toString(), 2, 88, 0xe0e0e0);

            if (mc.theWorld != null && mc.theWorld.blockExists(k8, i9, k9))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(k8, k9);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.getTopFilledSegment() + 15).append(" b: ").append(chunk.getBiomeGenForWorldCoords(k8 & 0xf, k9 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, k8 & 0xf, i9, k9 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, k8 & 0xf, i9, k9 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(k8 & 0xf, i9, k9 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            drawString(fontrenderer, String.format("ws: %.3f, fs: %.3f, g: %b, fl: %d", new Object[]
                    {
                        Float.valueOf(mc.thePlayer.capabilities.getWalkSpeed()), Float.valueOf(mc.thePlayer.capabilities.getFlySpeed()), Boolean.valueOf(mc.thePlayer.onGround), Integer.valueOf(mc.theWorld.getHeightValue(k8, k9))
                    }), 2, 104, 0xe0e0e0);

            if (!mc.theWorld.isRemote && mc.enableSP && SSPOptions.getDebugSeed())
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
            float f3 = (float)recordPlayingUpFor - par1;
            int j3 = (int)((f3 * 255F) / 20F);

            if (j3 > 255)
            {
                j3 = 255;
            }

            if (j3 > 8)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 68, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int k4 = 0xffffff;

                if (recordIsPlaying)
                {
                    k4 = Color.HSBtoRGB(f3 / 50F, 0.7F, 0.6F) & 0xffffff;
                }

                fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, k4 + (j3 << 24 & 0xff000000));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            mc.mcProfiler.endSection();
        }

        ScoreObjective scoreobjective = mc.theWorld.getScoreboard().func_96539_a(1);

        if (scoreobjective != null)
        {
            func_96136_a(scoreobjective, j, i, fontrenderer);
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
        scoreobjective = mc.theWorld.getScoreboard().func_96539_a(0);

        if (!mc.enableSP && mc.gameSettings.keyBindPlayerList.pressed && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1 || scoreobjective != null))
        {
            mc.mcProfiler.startSection("playerList");
            NetClientHandler netclienthandler = mc.thePlayer.sendQueue;
            java.util.List list = netclienthandler.playerInfoList;
            int l5 = netclienthandler.currentServerMaxPlayers;
            int j6 = l5;
            int i7 = 1;

            for (; j6 > 20; j6 = ((l5 + i7) - 1) / i7)
            {
                i7++;
            }

            int j7 = 300 / i7;

            if (j7 > 150)
            {
                j7 = 150;
            }

            int k7 = (i - i7 * j7) / 2;
            byte byte0 = 10;
            drawRect(k7 - 1, byte0 - 1, k7 + j7 * i7, byte0 + 9 * j6, 0x80000000);

            for (int j8 = 0; j8 < l5; j8++)
            {
                int l8 = k7 + (j8 % i7) * j7;
                int j9 = byte0 + (j8 / i7) * 9;
                drawRect(l8, j9, (l8 + j7) - 1, j9 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (j8 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(j8);
                ScorePlayerTeam scoreplayerteam = mc.theWorld.getScoreboard().getPlayersTeam(guiplayerinfo.name);
                String s4 = ScorePlayerTeam.formatPlayerName(scoreplayerteam, guiplayerinfo.name);
                fontrenderer.drawStringWithShadow(s4, l8, j9, 0xffffff);

                if (scoreobjective != null)
                {
                    int i10 = l8 + fontrenderer.getStringWidth(s4) + 5;
                    int k10 = (l8 + j7) - 12 - 5;

                    if (k10 - i10 > 5)
                    {
                        Score score = scoreobjective.getScoreboard().func_96529_a(guiplayerinfo.name, scoreobjective);
                        String s5 = (new StringBuilder()).append(EnumChatFormatting.YELLOW).append("").append(score.getScorePoints()).toString();
                        fontrenderer.drawStringWithShadow(s5, k10 - fontrenderer.getStringWidth(s5), j9, 0xffffff);
                    }
                }

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(icons);
                int j10 = 0;
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
                drawTexturedModalRect((l8 + j7) - 12, j9, 0 + j10 * 10, 176 + byte1 * 8, 10, 8);
                zLevel -= 100F;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    private void func_96136_a(ScoreObjective par1ScoreObjective, int par2, int par3, FontRenderer par4FontRenderer)
    {
        Scoreboard scoreboard = par1ScoreObjective.getScoreboard();
        Collection collection = scoreboard.func_96534_i(par1ScoreObjective);

        if (collection.size() > 15)
        {
            return;
        }

        int i = par4FontRenderer.getStringWidth(par1ScoreObjective.getDisplayName());

        for (Iterator iterator = collection.iterator(); iterator.hasNext();)
        {
            Score score = (Score)iterator.next();
            ScorePlayerTeam scoreplayerteam = scoreboard.getPlayersTeam(score.getPlayerName());
            String s = (new StringBuilder()).append(ScorePlayerTeam.formatPlayerName(scoreplayerteam, score.getPlayerName())).append(": ").append(EnumChatFormatting.RED).append(score.getScorePoints()).toString();
            i = Math.max(i, par4FontRenderer.getStringWidth(s));
        }

        int j = collection.size() * par4FontRenderer.FONT_HEIGHT;
        int k = par2 / 2 + j / 3;
        byte byte0 = 3;
        int l = par3 - i - byte0;
        int i1 = 0;
        Iterator iterator1 = collection.iterator();

        do
        {
            if (!iterator1.hasNext())
            {
                break;
            }

            Score score1 = (Score)iterator1.next();
            i1++;
            ScorePlayerTeam scoreplayerteam1 = scoreboard.getPlayersTeam(score1.getPlayerName());
            String s1 = ScorePlayerTeam.formatPlayerName(scoreplayerteam1, score1.getPlayerName());
            String s2 = (new StringBuilder()).append(EnumChatFormatting.RED).append("").append(score1.getScorePoints()).toString();
            int j1 = l;
            int k1 = k - i1 * par4FontRenderer.FONT_HEIGHT;
            int l1 = (par3 - byte0) + 2;
            drawRect(j1 - 2, k1, l1, k1 + par4FontRenderer.FONT_HEIGHT, 0x50000000);
            par4FontRenderer.drawString(s1, j1, k1, 0x20ffffff);
            par4FontRenderer.drawString(s2, l1 - par4FontRenderer.getStringWidth(s2), k1, 0x20ffffff);

            if (i1 == collection.size())
            {
                String s3 = par1ScoreObjective.getDisplayName();
                drawRect(j1 - 2, k1 - par4FontRenderer.FONT_HEIGHT - 1, l1, k1 - 1, 0x60000000);
                drawRect(j1 - 2, k1 - 1, l1, k1, 0x50000000);
                par4FontRenderer.drawString(s3, (j1 + i / 2) - par4FontRenderer.getStringWidth(s3) / 2, k1 - par4FontRenderer.FONT_HEIGHT, 0x20ffffff);
            }
        }
        while (true);
    }

    private void func_110327_a(int par1, int par2)
    {
        boolean flag = (mc.thePlayer.hurtResistantTime / 3) % 2 == 1;

        if (mc.thePlayer.hurtResistantTime < 10)
        {
            flag = false;
        }

        int i = MathHelper.ceiling_float_int(mc.thePlayer.getHealth());
        int j = MathHelper.ceiling_float_int(mc.thePlayer.prevHealth);
        rand.setSeed(updateCounter * 0x4c627);
        boolean flag1 = false;
        FoodStats foodstats = mc.thePlayer.getFoodStats();
        int k = foodstats.getFoodLevel();
        int l = foodstats.getPrevFoodLevel();
        AttributeInstance attributeinstance = mc.thePlayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
        int i1 = par1 / 2 - 91;
        int j1 = par1 / 2 + 91;
        int k1 = par2 - (hidexp && !mc.thePlayer.isRidingHorse() ? 32 : 39);
        float f = (float)attributeinstance.getAttributeValue();
        float f1 = mc.thePlayer.getAbsorptionAmount();
        int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10F);
        int i2 = Math.max(10 - (l1 - 2), 3);
        int j2 = k1 - (l1 - 1) * i2 - 10;
        float f2 = f1;
        int k2 = mc.thePlayer.getTotalArmorValue();
        int l2 = -1;

        if (mc.thePlayer.isPotionActive(Potion.regeneration))
        {
            l2 = updateCounter % MathHelper.ceiling_float_int(f + 5F);
        }

        mc.mcProfiler.startSection("armor");

        for (int i3 = 0; i3 < 10; i3++)
        {
            if (k2 <= 0)
            {
                continue;
            }

            int k3 = i1 + i3 * 8;
            int i4 = j2;
            if(hidehunger){
                k3 = j1 - i3 * 8 - 9;
                i4 = j2 + 10;
            }

            if (i3 * 2 + 1 < k2)
            {
                drawTexturedModalRect(k3, i4, 34, 9, 9, 9);
            }

            if (i3 * 2 + 1 == k2)
            {
                if (hidehunger && !fallbacktex){
                    mc.getTextureManager().bindTexture(customArmorResource);
                    drawTexturedModalRect(k3, i4, 0, 0, 9, 9);
                    mc.getTextureManager().bindTexture(icons);
                }else{
                    drawTexturedModalRect(k3, i4, 25, 9, 9, 9);
                }
            }

            if (i3 * 2 + 1 > k2)
            {
                drawTexturedModalRect(k3, i4, 16, 9, 9, 9);
            }
        }

        mc.mcProfiler.endStartSection("health");

        for (int j3 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; j3 >= 0; j3--)
        {
            int l3 = 16;

            if (mc.thePlayer.isPotionActive(Potion.poison))
            {
                l3 += 36;
            }
            else if (mc.thePlayer.isPotionActive(Potion.wither))
            {
                l3 += 72;
            }

            int k4 = 0;

            if (flag)
            {
                k4 = 1;
            }

            int k5 = MathHelper.ceiling_float_int((float)(j3 + 1) / 10F) - 1;
            int j6 = i1 + (j3 % 10) * 8;
            int i7 = k1 - k5 * i2;

            if (i <= 4)
            {
                i7 += rand.nextInt(2);
            }

            if (j3 == l2)
            {
                i7 -= 2;
            }

            byte byte1 = 0;

            if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
            {
                byte1 = 5;
            }

            drawTexturedModalRect(j6, i7, 16 + k4 * 9, 9 * byte1, 9, 9);

            if (flag)
            {
                if (j3 * 2 + 1 < j)
                {
                    drawTexturedModalRect(j6, i7, l3 + 54, 9 * byte1, 9, 9);
                }

                if (j3 * 2 + 1 == j)
                {
                    drawTexturedModalRect(j6, i7, l3 + 63, 9 * byte1, 9, 9);
                }
            }

            if (f2 > 0.0F)
            {
                if (f2 == f1 && f1 % 2.0F == 1.0F)
                {
                    drawTexturedModalRect(j6, i7, l3 + 153, 9 * byte1, 9, 9);
                }
                else
                {
                    drawTexturedModalRect(j6, i7, l3 + 144, 9 * byte1, 9, 9);
                }

                f2 -= 2.0F;
                continue;
            }

            if (j3 * 2 + 1 < i)
            {
                drawTexturedModalRect(j6, i7, l3 + 36, 9 * byte1, 9, 9);
            }

            if (j3 * 2 + 1 == i)
            {
                drawTexturedModalRect(j6, i7, l3 + 45, 9 * byte1, 9, 9);
            }
        }

        Entity entity = mc.thePlayer.ridingEntity;

        if (entity == null && !hidehunger)
        {
            mc.mcProfiler.endStartSection("food");

            for (int i4 = 0; i4 < 10; i4++)
            {
                int l4 = k1;
                int l5 = 16;
                byte byte0 = 0;

                if (mc.thePlayer.isPotionActive(Potion.hunger))
                {
                    l5 += 36;
                    byte0 = 13;
                }

                if (mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (k * 3 + 1) == 0)
                {
                    l4 += rand.nextInt(3) - 1;
                }

                if (flag1)
                {
                    byte0 = 1;
                }

                int j7 = j1 - i4 * 8 - 9;
                drawTexturedModalRect(j7, l4, 16 + byte0 * 9, 27, 9, 9);

                if (flag1)
                {
                    if (i4 * 2 + 1 < l)
                    {
                        drawTexturedModalRect(j7, l4, l5 + 54, 27, 9, 9);
                    }

                    if (i4 * 2 + 1 == l)
                    {
                        drawTexturedModalRect(j7, l4, l5 + 63, 27, 9, 9);
                    }
                }

                if (i4 * 2 + 1 < k)
                {
                    drawTexturedModalRect(j7, l4, l5 + 36, 27, 9, 9);
                }

                if (i4 * 2 + 1 == k)
                {
                    drawTexturedModalRect(j7, l4, l5 + 45, 27, 9, 9);
                }
            }
        }
        else if (entity instanceof EntityLivingBase)
        {
            mc.mcProfiler.endStartSection("mountHealth");
            EntityLivingBase entitylivingbase = (EntityLivingBase)entity;
            int i5 = (int)Math.ceil(entitylivingbase.getHealth());
            float f3 = entitylivingbase.getMaxHealth();
            int k6 = (int)(f3 + 0.5F) / 2;

            if (k6 > 30)
            {
                k6 = 30;
            }

            int k7 = k1 - (hidehunger && k2 > 0 ? 10 : 0);

            for (int l7 = 0; k6 > 0; l7 += 20)
            {
                int i8 = Math.min(k6, 10);
                k6 -= i8;

                for (int j8 = 0; j8 < i8; j8++)
                {
                    byte byte2 = 52;
                    int k8 = 0;

                    if (flag1)
                    {
                        k8 = 1;
                    }

                    int l8 = j1 - j8 * 8 - 9;
                    drawTexturedModalRect(l8, k7, byte2 + k8 * 9, 9, 9, 9);

                    if (j8 * 2 + 1 + l7 < i5)
                    {
                        drawTexturedModalRect(l8, k7, byte2 + 36, 9, 9, 9);
                    }

                    if (j8 * 2 + 1 + l7 == i5)
                    {
                        drawTexturedModalRect(l8, k7, byte2 + 45, 9, 9, 9);
                    }
                }

                k7 -= 10;
            }
        }

        mc.mcProfiler.endStartSection("air");

        if (mc.thePlayer.isInsideOfMaterial(Material.water))
        {
            int j4 = mc.thePlayer.getAir();
            int j5 = MathHelper.ceiling_double_int(((double)(j4 - 2) * 10D) / 300D);
            int i6 = MathHelper.ceiling_double_int(((double)j4 * 10D) / 300D) - j5;

            for (int l6 = 0; l6 < j5 + i6; l6++)
            {
                if (l6 < j5)
                {
                    if(hidehunger){
                        drawTexturedModalRect(i1 + l6 * 8, j2, 16, 18, 9, 9);
                    }else{
                        drawTexturedModalRect(j1 - l6 * 8 - 9, j2, 16, 18, 9, 9);
                    }
                }
                else
                {
                    if(hidehunger){
                        drawTexturedModalRect(i1 + l6 * 8, j2, 25, 18, 9, 9);
                    }else{
                        drawTexturedModalRect(j1 - l6 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }
        }

        mc.mcProfiler.endSection();
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    private void renderBossHealth()
    {
        if (BossStatus.bossName == null || BossStatus.statusBarLength <= 0)
        {
            return;
        }

        BossStatus.statusBarLength--;
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
        mc.getTextureManager().bindTexture(icons);
    }

    private void renderPumpkinBlur(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        mc.getTextureManager().bindTexture(pumpkinBlurTexPath);
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
        mc.getTextureManager().bindTexture(vignetteTexPath);
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

    private void func_130015_b(float par1, int par2, int par3)
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
        Icon icon = Block.portal.getBlockTextureFromSide(1);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        float f = icon.getMinU();
        float f1 = icon.getMinV();
        float f2 = icon.getMaxU();
        float f3 = icon.getMaxV();
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

        itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, par2, par3);

        if (f > 0.0F)
        {
            GL11.glPopMatrix();
        }

        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.getTextureManager(), itemstack, par2, par3);
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
                remainingHighlightTicks = 0;
            }
            else if (highlightingItemStack == null || itemstack.itemID != highlightingItemStack.itemID || !ItemStack.areItemStackTagsEqual(itemstack, highlightingItemStack) || !itemstack.isItemStackDamageable() && itemstack.getItemDamage() != highlightingItemStack.getItemDamage())
            {
                remainingHighlightTicks = 40;
            }
            else if (remainingHighlightTicks > 0)
            {
                remainingHighlightTicks--;
            }

            highlightingItemStack = itemstack;
        }
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        func_110326_a((new StringBuilder()).append("Now playing: ").append(par1Str).toString(), true);
    }

    public void func_110326_a(String par1Str, boolean par2)
    {
        recordPlaying = par1Str;
        recordPlayingUpFor = 60;
        recordIsPlaying = par2;
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
