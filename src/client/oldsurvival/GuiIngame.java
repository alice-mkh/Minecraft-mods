package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngame extends Gui
{
    private static RenderItem itemRenderer = new RenderItem();

    /** A list with all the chat messages in. */
    private java.util.List chatMessageList;
    private Random rand;
    private Minecraft mc;
    public String field_933_a;
    private int updateCounter;

    /** The string specifying which record music is playing */
    private String recordPlaying;

    /** How many ticks the record playing message will be displayed */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;

    /** Damage partial time (GUI) */
    public float damageGuiPartialTime;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    float prevVignetteBrightness;

    public GuiIngame(Minecraft par1Minecraft)
    {
        chatMessageList = new ArrayList();
        rand = new Random();
        field_933_a = null;
        updateCounter = 0;
        recordPlaying = "";
        recordPlayingUpFor = 0;
        recordIsPlaying = false;
        prevVignetteBrightness = 1.0F;
        mc = par1Minecraft;
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
            renderVignette(mc.thePlayer.getEntityBrightness(par1), i, j);
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

        if (!mc.playerController.func_35643_e())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
            InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
            zLevel = -90F;
            drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
            drawTexturedModalRect((i / 2 - 91 - 1) + inventoryplayer.currentItem * 20, j - 22 - 1, 0, 22, 24, 22);
            if(mod_OldSurvivalMode.DisableHunger){
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons2.png"));
            }else{
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
            }
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            boolean flag1 = (mc.thePlayer.heartsLife / 3) % 2 == 1;

            if (mc.thePlayer.heartsLife < 10)
            {
                flag1 = false;
            }

            int l1 = mc.thePlayer.getEntityHealth();
            int i3 = mc.thePlayer.prevHealth;
            rand.setSeed(updateCounter * 0x4c627);
            boolean flag3 = false;
            FoodStats foodstats = mc.thePlayer.getFoodStats();
            int l4 = foodstats.getFoodLevel();
            int j5 = foodstats.getPrevFoodLevel();
            renderBossHealth();

            if (mc.playerController.shouldDrawHUD())
            {
                int j6 = i / 2 - 91;
                int i7 = i / 2 + 91;
                int j8 = j - 32;
                if(!mod_OldSurvivalMode.DisableXP){
                    int k7 = mc.thePlayer.xpBarCap();

                    if (k7 > 0)
                    {
                        char c = '\266';
                        int l8 = (int)(mc.thePlayer.experience * (float)(c + 1));
                        int k9 = (j - 32) + 3;
                        drawTexturedModalRect(j6, k9, 0, 64, c, 5);

                        if (l8 > 0)
                        {
                            drawTexturedModalRect(j6, k9, 0, 69, l8, 5);
                        }
                    }
                    j8 = j - 39;
                }
                int i9 = j8 - 10;
                int l9 = mc.thePlayer.getTotalArmorValue();
                int i10 = -1;

                if (mc.thePlayer.isPotionActive(Potion.regeneration))
                {
                    i10 = updateCounter % 25;
                }

                for (int k10 = 0; k10 < 10; k10++)
                {
                    if (l9 > 0)
                    {
                        int j11 = i7 - k10 * 8 - 9;
                        int intintintintint = j8;
                        if(!mod_OldSurvivalMode.DisableHunger){
                            j11 = j6 + k10 * 8;
                            intintintintint = j8 - 10;
                        }

                        if (k10 * 2 + 1 < l9)
                        {
                            drawTexturedModalRect(j11, intintintintint, 34, 9, 9, 9);
                        }

                        if (k10 * 2 + 1 == l9)
                        {
                            drawTexturedModalRect(j11, intintintintint, 25, 9, 9, 9);
                        }

                        if (k10 * 2 + 1 > l9)
                        {
                            drawTexturedModalRect(j11, intintintintint, 16, 9, 9, 9);
                        }
                    }

                    int k11 = 16;

                    if (mc.thePlayer.isPotionActive(Potion.poison))
                    {
                        k11 += 36;
                    }

                    int j12 = 0;

                    if (flag1)
                    {
                        j12 = 1;
                    }

                    int i13 = j6 + k10 * 8;
                    int j13 = j8;

                    if (l1 <= 4)
                    {
                        j13 += rand.nextInt(2);
                    }

                    if (k10 == i10)
                    {
                        j13 -= 2;
                    }

                    byte byte5 = 0;

                    if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    {
                        byte5 = 5;
                    }

                    drawTexturedModalRect(i13, j13, 16 + j12 * 9, 9 * byte5, 9, 9);

                    if (flag1)
                    {
                        if (k10 * 2 + 1 < i3)
                        {
                            drawTexturedModalRect(i13, j13, k11 + 54, 9 * byte5, 9, 9);
                        }

                        if (k10 * 2 + 1 == i3)
                        {
                            drawTexturedModalRect(i13, j13, k11 + 63, 9 * byte5, 9, 9);
                        }
                    }

                    if (k10 * 2 + 1 < l1)
                    {
                        drawTexturedModalRect(i13, j13, k11 + 36, 9 * byte5, 9, 9);
                    }

                    if (k10 * 2 + 1 == l1)
                    {
                        drawTexturedModalRect(i13, j13, k11 + 45, 9 * byte5, 9, 9);
                    }
                }

                if(!mod_OldSurvivalMode.DisableHunger){
                    for (int l10 = 0; l10 < 10; l10++)
                    {
                        int l11 = j8;
                        int k12 = 16;
                        byte byte4 = 0;

                        if (mc.thePlayer.isPotionActive(Potion.hunger))
                        {
                            k12 += 36;
                            byte4 = 13;
                        }

                        if (mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (l4 * 3 + 1) == 0)
                        {
                            l11 += rand.nextInt(3) - 1;
                        }

                        if (flag3)
                        {
                            byte4 = 1;
                        }

                        int k13 = i7 - l10 * 8 - 9;
                        drawTexturedModalRect(k13, l11, 16 + byte4 * 9, 27, 9, 9);

                        if (flag3)
                        {
                            if (l10 * 2 + 1 < j5)
                            {
                                drawTexturedModalRect(k13, l11, k12 + 54, 27, 9, 9);
                            }

                            if (l10 * 2 + 1 == j5)
                            {
                                drawTexturedModalRect(k13, l11, k12 + 63, 27, 9, 9);
                            }
                        }

                        if (l10 * 2 + 1 < l4)
                        {
                            drawTexturedModalRect(k13, l11, k12 + 36, 27, 9, 9);
                        }

                        if (l10 * 2 + 1 == l4)
                        {
                            drawTexturedModalRect(k13, l11, k12 + 45, 27, 9, 9);
                        }
                    }
                }

                if (mc.thePlayer.isInsideOfMaterial(Material.water))
                {
                    int i11 = (int)Math.ceil(((double)(mc.thePlayer.getAir() - 2) * 10D) / 300D);
                    int i12 = (int)Math.ceil(((double)mc.thePlayer.getAir() * 10D) / 300D) - i11;

                    for (int l12 = 0; l12 < i11 + i12; l12++)
                    {
                        if (l12 < i11)
                        {
                            if(mod_OldSurvivalMode.DisableHunger){
                                drawTexturedModalRect(j6 + l12 * 8, i9, 16, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(i7 - l12 * 8 - 9, i9, 16, 18, 9, 9);
                            }
                        }
                        else
                        {
                            if(mod_OldSurvivalMode.DisableHunger){
                                drawTexturedModalRect(j6 + l12 * 8, i9, 25, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(i7 - l12 * 8 - 9, i9, 25, 18, 9, 9);
                            }
                        }
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int k6 = 0; k6 < 9; k6++)
            {
                int j7 = (i / 2 - 90) + k6 * 20 + 2;
                int l7 = j - 16 - 3;
                renderInventorySlot(k6, j7, l7, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        if (mc.thePlayer.getSleepTimer() > 0)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int k = mc.thePlayer.getSleepTimer();
            float f2 = (float)k / 100F;

            if (f2 > 1.0F)
            {
                f2 = 1.0F - (float)(k - 100) / 10F;
            }

            int i2 = (int)(220F * f2) << 24 | 0x101020;
            drawRect(0, 0, i, j, i2);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        if (mc.playerController.func_35642_f() && mc.thePlayer.experienceLevel > 0 && !mod_OldSurvivalMode.DisableXP)
        {
            boolean flag = false;
            int i1 = flag ? 0xffffff : 0x80ff20;
            String s = (new StringBuilder()).append("").append(mc.thePlayer.experienceLevel).toString();
            int j3 = (i - fontrenderer.getStringWidth(s)) / 2;
            int k3 = j - 31 - 4;
            fontrenderer.drawString(s, j3 + 1, k3, 0);
            fontrenderer.drawString(s, j3 - 1, k3, 0);
            fontrenderer.drawString(s, j3, k3 + 1, 0);
            fontrenderer.drawString(s, j3, k3 - 1, 0);
            fontrenderer.drawString(s, j3, k3, i1);
        }

        if (mc.gameSettings.showDebugInfo && mod_OldSurvivalMode.AllowDebug)
        {
            GL11.glPushMatrix();

            if (Minecraft.hasPaidCheckTime > 0L)
            {
                GL11.glTranslatef(0.0F, 32F, 0.0F);
            }

            fontrenderer.drawStringWithShadow((new StringBuilder()).append("Minecraft 1.2.3 (").append(mc.debug).append(")").toString(), 2, 2, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.func_21002_o(), 2, 42, 0xffffff);
            long la = Runtime.getRuntime().maxMemory();
            long l2a = Runtime.getRuntime().totalMemory();
            long l3 = Runtime.getRuntime().freeMemory();
            long l5 = l2a - l3;
            String s1 = (new StringBuilder()).append("Used memory: ").append((l5 * 100L) / la).append("% (").append(l5 / 1024L / 1024L).append("MB) of ").append(la / 1024L / 1024L).append("MB").toString();
            drawString(fontrenderer, s1, i - fontrenderer.getStringWidth(s1) - 2, 2, 0xe0e0e0);
            s1 = (new StringBuilder()).append("Allocated memory: ").append((l2a * 100L) / la).append("% (").append(l2a / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s1, i - fontrenderer.getStringWidth(s1) - 2, 12, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("x: ").append(mc.thePlayer.posX).toString(), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("y: ").append(mc.thePlayer.posY).toString(), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("z: ").append(mc.thePlayer.posZ).toString(), 2, 80, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3).toString(), 2, 88, 0xe0e0e0);
            int l = MathHelper.floor_double(mc.thePlayer.posX);
            int j1 = MathHelper.floor_double(mc.thePlayer.posY);
            int l2 = MathHelper.floor_double(mc.thePlayer.posZ);

            if (mc.theWorld != null && mc.theWorld.blockExists(l, j1, l2))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(l, l2);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.func_48498_h() + 15).append(" b: ").append(chunk.func_48490_a(l & 0xf, l2 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, l & 0xf, j1, l2 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, l & 0xf, j1, l2 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(l & 0xf, j1, l2 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            drawString(fontrenderer, (new StringBuilder()).append("Seed: ").append(mc.theWorld.getSeed()).toString(), 2, 112, 0xe0e0e0);
            GL11.glPopMatrix();
        }

        if (recordPlayingUpFor > 0)
        {
            float f1 = (float)recordPlayingUpFor - par1;
            int k1 = (int)((f1 * 256F) / 20F);

            if (k1 > 255)
            {
                k1 = 255;
            }

            if (k1 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 48, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int j2 = 0xffffff;

                if (recordIsPlaying)
                {
                    j2 = Color.HSBtoRGB(f1 / 50F, 0.7F, 0.6F) & 0xffffff;
                }

                fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, j2 + (k1 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }

        byte byte0 = 10;
        boolean flag2 = false;

        if (mc.currentScreen instanceof GuiChat)
        {
            byte0 = 20;
            flag2 = true;
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j - 48, 0.0F);

        for (int k2 = 0; k2 < chatMessageList.size() && k2 < byte0; k2++)
        {
            if (((ChatLine)chatMessageList.get(k2)).updateCounter >= 200 && !flag2)
            {
                continue;
            }

            double d = (double)((ChatLine)chatMessageList.get(k2)).updateCounter / 200D;
            d = 1.0D - d;
            d *= 10D;

            if (d < 0.0D)
            {
                d = 0.0D;
            }

            if (d > 1.0D)
            {
                d = 1.0D;
            }

            d *= d;
            int j4 = (int)(255D * d);

            if (flag2)
            {
                j4 = 255;
            }

            if (j4 > 0)
            {
                byte byte1 = 2;
                int k5 = -k2 * 9;
                String s2 = ((ChatLine)chatMessageList.get(k2)).message;
                drawRect(byte1, k5 - 1, byte1 + 320, k5 + 8, j4 / 2 << 24);
                GL11.glEnable(GL11.GL_BLEND);
                fontrenderer.drawStringWithShadow(s2, byte1, k5, 0xffffff + (j4 << 24));
            }
        }

        GL11.glPopMatrix();

        if ((mc.thePlayer instanceof EntityClientPlayerMP) && mc.gameSettings.keyBindPlayerList.pressed)
        {
            NetClientHandler netclienthandler = ((EntityClientPlayerMP)mc.thePlayer).sendQueue;
            java.util.List list = netclienthandler.playerNames;
            int i4 = netclienthandler.currentServerMaxPlayers;
            int k4 = i4;
            int i5 = 1;

            for (; k4 > 20; k4 = ((i4 + i5) - 1) / i5)
            {
                i5++;
            }

            int i6 = 300 / i5;

            if (i6 > 150)
            {
                i6 = 150;
            }

            int l6 = (i - i5 * i6) / 2;
            byte byte2 = 10;
            drawRect(l6 - 1, byte2 - 1, l6 + i6 * i5, byte2 + 9 * k4, 0x80000000);

            for (int i8 = 0; i8 < i4; i8++)
            {
                int k8 = l6 + (i8 % i5) * i6;
                int j9 = byte2 + (i8 / i5) * 9;
                drawRect(k8, j9, (k8 + i6) - 1, j9 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (i8 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(i8);
                fontrenderer.drawStringWithShadow(guiplayerinfo.name, k8, j9, 0xffffff);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/icons.png"));
                int j10 = 0;
                byte byte3 = 0;
                j10 = 0;
                byte3 = 0;

                if (guiplayerinfo.responseTime < 0)
                {
                    byte3 = 5;
                }
                else if (guiplayerinfo.responseTime < 150)
                {
                    byte3 = 0;
                }
                else if (guiplayerinfo.responseTime < 300)
                {
                    byte3 = 1;
                }
                else if (guiplayerinfo.responseTime < 600)
                {
                    byte3 = 2;
                }
                else if (guiplayerinfo.responseTime < 1000)
                {
                    byte3 = 3;
                }
                else
                {
                    byte3 = 4;
                }

                zLevel += 100F;
                drawTexturedModalRect((k8 + i6) - 12, j9, 0 + j10 * 10, 176 + byte3 * 8, 10, 8);
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
        int k = (int)(((float)entitydragon.func_41010_ax() / (float)entitydragon.getMaxHealth()) * (float)(c + 1));
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

        for (int i = 0; i < chatMessageList.size(); i++)
        {
            ((ChatLine)chatMessageList.get(i)).updateCounter++;
        }
    }

    /**
     * Clear all chat messages.
     */
    public void clearChatMessages()
    {
        chatMessageList.clear();
    }

    /**
     * Adds a chat message to the list of chat messages. Args: msg
     */
    public void addChatMessage(String par1Str)
    {
        int i;

        for (; mc.fontRenderer.getStringWidth(par1Str) > 320; par1Str = par1Str.substring(i))
        {
            for (i = 1; i < par1Str.length() && mc.fontRenderer.getStringWidth(par1Str.substring(0, i + 1)) <= 320; i++) { }

            addChatMessage(par1Str.substring(0, i));
        }

        chatMessageList.add(0, new ChatLine(par1Str));

        for (; chatMessageList.size() > 50; chatMessageList.remove(chatMessageList.size() - 1)) { }
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        recordPlaying = (new StringBuilder()).append("Now playing: ").append(par1Str).toString();
        recordPlayingUpFor = 60;
        recordIsPlaying = true;
    }

    /**
     * Adds the string to chat message after translate it with the language file.
     */
    public void addChatMessageTranslate(String par1Str)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        String s = stringtranslate.translateKey(par1Str);
        addChatMessage(s);
    }
}
