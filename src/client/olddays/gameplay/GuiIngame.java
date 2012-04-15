package net.minecraft.src;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiIngame extends Gui
{
    public static boolean hidexp = false;
    public static boolean hidehunger = false;
    public static boolean nodebug = false;

    private static RenderItem itemRenderer = new RenderItem();

    /** A list with all the chat messages in. */
    private java.util.List chatMessageList;

    /** A list with all the sent chat messages in it. */
    private java.util.List sentMessageList;
    private Random rand;
    private Minecraft mc;
    private int updateCounter;

    /** The string specifying which record music is playing */
    private String recordPlaying;

    /** How many ticks the record playing message will be displayed */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    private int historyOffset;
    private boolean field_50018_o;

    /** Damage partial time (GUI) */
    public float damageGuiPartialTime;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    float prevVignetteBrightness;

    public GuiIngame(Minecraft par1Minecraft)
    {
        chatMessageList = new ArrayList();
        sentMessageList = new ArrayList();
        rand = new Random();
        updateCounter = 0;
        recordPlaying = "";
        recordPlayingUpFor = 0;
        recordIsPlaying = false;
        historyOffset = 0;
        field_50018_o = false;
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

        if (!mc.playerController.func_35643_e())
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
            boolean flag = (mc.thePlayer.heartsLife / 3) % 2 == 1;

            if (mc.thePlayer.heartsLife < 10)
            {
                flag = false;
            }

            int i1 = mc.thePlayer.getHealth();
            int i2 = mc.thePlayer.prevHealth;
            rand.setSeed(updateCounter * 0x4c627);
            boolean flag2 = false;
            FoodStats foodstats = mc.thePlayer.getFoodStats();
            int j4 = foodstats.getFoodLevel();
            int l4 = foodstats.getPrevFoodLevel();
            renderBossHealth();

            if (mc.playerController.shouldDrawHUD())
            {
                int j5 = i / 2 - 91;
                int i6 = i / 2 + 91;
                int k7 = j - 32;
                if(!hidexp){
                    if (mc.thePlayer.xpBarCap() > 0)
                    {
                        char c = '\266';
                        int j8 = (int)(mc.thePlayer.experience * (float)(c + 1));
                        int i9 = (j - 32) + 3;
                        drawTexturedModalRect(j5, i9, 0, 64, c, 5);
                        if (j8 > 0)
                        {
                            drawTexturedModalRect(j5, i9, 0, 69, j8, 5);
                        }
                    }
                    k7 = j - 39;
                }
                int k8 = k7 - 10;
                int j9 = mc.thePlayer.getTotalArmorValue();
                int i10 = -1;

                if (mc.thePlayer.isPotionActive(Potion.regeneration))
                {
                    i10 = updateCounter % 25;
                }

                for (int j10 = 0; j10 < 10; j10++)
                {
                    if (j9 > 0)
                    {
                        if (hidehunger){
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/olddays/icons.png"));
                        }
                        int i11 = i6 - j10 * 8 - 9;
                        int intintintintint = k7;
                        if(!hidehunger){
                            i11 = j5 + j10 * 8;
                            intintintintint = k7 - 10;
                        }

                        if (j10 * 2 + 1 < j9)
                        {
                            drawTexturedModalRect(i11, intintintintint, 34, 9, 9, 9);
                        }

                        if (j10 * 2 + 1 == j9)
                        {
                            drawTexturedModalRect(i11, intintintintint, 25, 9, 9, 9);
                        }

                        if (j10 * 2 + 1 > j9)
                        {
                            drawTexturedModalRect(i11, intintintintint, 16, 9, 9, 9);
                        }
                        if (hidehunger){
                            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
                        }
                    }

                    int j11 = 16;

                    if (mc.thePlayer.isPotionActive(Potion.poison))
                    {
                        j11 += 36;
                    }

                    int i12 = 0;

                    if (flag)
                    {
                        i12 = 1;
                    }

                    int l12 = j5 + j10 * 8;
                    int j13 = k7;

                    if (i1 <= 4)
                    {
                        j13 += rand.nextInt(2);
                    }

                    if (j10 == i10)
                    {
                        j13 -= 2;
                    }

                    byte byte3 = 0;

                    if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    {
                        byte3 = 5;
                    }

                    drawTexturedModalRect(l12, j13, 16 + i12 * 9, 9 * byte3, 9, 9);

                    if (flag)
                    {
                        if (j10 * 2 + 1 < i2)
                        {
                            drawTexturedModalRect(l12, j13, j11 + 54, 9 * byte3, 9, 9);
                        }

                        if (j10 * 2 + 1 == i2)
                        {
                            drawTexturedModalRect(l12, j13, j11 + 63, 9 * byte3, 9, 9);
                        }
                    }

                    if (j10 * 2 + 1 < i1)
                    {
                        drawTexturedModalRect(l12, j13, j11 + 36, 9 * byte3, 9, 9);
                    }

                    if (j10 * 2 + 1 == i1)
                    {
                        drawTexturedModalRect(l12, j13, j11 + 45, 9 * byte3, 9, 9);
                    }
                }

                if(!hidehunger){
                    for (int k10 = 0; k10 < 10; k10++)
                    {
                        int k11 = k7;
                        int j12 = 16;
                        byte byte2 = 0;

                        if (mc.thePlayer.isPotionActive(Potion.hunger))
                        {
                            j12 += 36;
                            byte2 = 13;
                        }

                        if (mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (j4 * 3 + 1) == 0)
                        {
                            k11 += rand.nextInt(3) - 1;
                        }

                        if (flag2)
                        {
                            byte2 = 1;
                        }

                        int k13 = i6 - k10 * 8 - 9;
                        drawTexturedModalRect(k13, k11, 16 + byte2 * 9, 27, 9, 9);

                        if (flag2)
                        {
                            if (k10 * 2 + 1 < l4)
                            {
                                drawTexturedModalRect(k13, k11, j12 + 54, 27, 9, 9);
                            }

                            if (k10 * 2 + 1 == l4)
                            {
                                drawTexturedModalRect(k13, k11, j12 + 63, 27, 9, 9);
                            }
                        }

                        if (k10 * 2 + 1 < j4)
                        {
                            drawTexturedModalRect(k13, k11, j12 + 36, 27, 9, 9);
                        }

                        if (k10 * 2 + 1 == j4)
                        {
                            drawTexturedModalRect(k13, k11, j12 + 45, 27, 9, 9);
                        }
                    }
                }

                if (mc.thePlayer.isInsideOfMaterial(Material.water))
                {
                    int l10 = mc.thePlayer.getAir();
                    int l11 = (int)Math.ceil(((double)(l10 - 2) * 10D) / 300D);
                    int k12 = (int)Math.ceil(((double)l10 * 10D) / 300D) - l11;

                    for (int i13 = 0; i13 < l11 + k12; i13++)
                    {
                        if (i13 < l11)
                        {
                            if(hidehunger){
                                drawTexturedModalRect(j5 + i13 * 8, k8, 16, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(i6 - i13 * 8 - 9, k8, 16, 18, 9, 9);
                            }
                            
                        }
                        else
                        {
                            if(hidehunger){
                                drawTexturedModalRect(j5 + i13 * 8, k8, 25, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(i6 - i13 * 8 - 9, k8, 25, 18, 9, 9);
                            }
                        }
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int k5 = 0; k5 < 9; k5++)
            {
                int j6 = (i / 2 - 90) + k5 * 20 + 2;
                int i7 = j - 16 - 3;
                renderInventorySlot(k5, j6, i7, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        }

        if (mc.thePlayer.getSleepTimer() > 0)
        {
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
        }

        if (mc.playerController.func_35642_f() && mc.thePlayer.experienceLevel > 0 && !hidexp)
        {
            boolean flag1 = false;
            int k1 = flag1 ? 0xffffff : 0x80ff20;
            String s = (new StringBuilder()).append("").append(mc.thePlayer.experienceLevel).toString();
            int i3 = (i - fontrenderer.getStringWidth(s)) / 2;
            int k3 = j - 31 - 4;
            fontrenderer.drawString(s, i3 + 1, k3, 0);
            fontrenderer.drawString(s, i3 - 1, k3, 0);
            fontrenderer.drawString(s, i3, k3 + 1, 0);
            fontrenderer.drawString(s, i3, k3 - 1, 0);
            fontrenderer.drawString(s, i3, k3, k1);
        }

        if (mc.gameSettings.showDebugInfo && !nodebug)
        {
            GL11.glPushMatrix();

            if (Minecraft.hasPaidCheckTime > 0L)
            {
                GL11.glTranslatef(0.0F, 32F, 0.0F);
            }

            fontrenderer.drawStringWithShadow((new StringBuilder()).append("Minecraft 1.2.5 (").append(mc.debug).append(")").toString(), 2, 2, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getWorldProviderName(), 2, 42, 0xffffff);
            long l = Runtime.getRuntime().maxMemory();
            long l2 = Runtime.getRuntime().totalMemory();
            long l3 = Runtime.getRuntime().freeMemory();
            long l5 = l2 - l3;
            String s1 = (new StringBuilder()).append("Used memory: ").append((l5 * 100L) / l).append("% (").append(l5 / 1024L / 1024L).append("MB) of ").append(l / 1024L / 1024L).append("MB").toString();
            drawString(fontrenderer, s1, i - fontrenderer.getStringWidth(s1) - 2, 2, 0xe0e0e0);
            s1 = (new StringBuilder()).append("Allocated memory: ").append((l2 * 100L) / l).append("% (").append(l2 / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s1, i - fontrenderer.getStringWidth(s1) - 2, 12, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("x: ").append(mc.thePlayer.posX).toString(), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("y: ").append(mc.thePlayer.posY).toString(), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("z: ").append(mc.thePlayer.posZ).toString(), 2, 80, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3).toString(), 2, 88, 0xe0e0e0);
            int l7 = MathHelper.floor_double(mc.thePlayer.posX);
            int l8 = MathHelper.floor_double(mc.thePlayer.posY);
            int k9 = MathHelper.floor_double(mc.thePlayer.posZ);

            if (mc.theWorld != null && mc.theWorld.blockExists(l7, l8, k9))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(l7, k9);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.getTopFilledSegment() + 15).append(" b: ").append(chunk.func_48490_a(l7 & 0xf, k9 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, l7 & 0xf, l8, k9 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, l7 & 0xf, l8, k9 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(l7 & 0xf, l8, k9 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            if (!mc.theWorld.isRemote)
            {
                drawString(fontrenderer, (new StringBuilder()).append("Seed: ").append(mc.theWorld.getSeed()).toString(), 2, 112, 0xe0e0e0);
            }

            GL11.glPopMatrix();
        }

        if (recordPlayingUpFor > 0)
        {
            float f2 = (float)recordPlayingUpFor - par1;
            int l1 = (int)((f2 * 256F) / 20F);

            if (l1 > 255)
            {
                l1 = 255;
            }

            if (l1 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 48, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int j2 = 0xffffff;

                if (recordIsPlaying)
                {
                    j2 = Color.HSBtoRGB(f2 / 50F, 0.7F, 0.6F) & 0xffffff;
                }

                fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, j2 + (l1 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j - 48, 0.0F);
        func_50010_a(fontrenderer);
        GL11.glPopMatrix();

        if ((mc.thePlayer instanceof EntityClientPlayerMP) && mc.gameSettings.keyBindPlayerList.pressed)
        {
            NetClientHandler netclienthandler = ((EntityClientPlayerMP)mc.thePlayer).sendQueue;
            java.util.List list = netclienthandler.playerNames;
            int k2 = netclienthandler.currentServerMaxPlayers;
            int j3 = k2;
            int i4 = 1;

            for (; j3 > 20; j3 = ((k2 + i4) - 1) / i4)
            {
                i4++;
            }

            int k4 = 300 / i4;

            if (k4 > 150)
            {
                k4 = 150;
            }

            int i5 = (i - i4 * k4) / 2;
            byte byte0 = 10;
            drawRect(i5 - 1, byte0 - 1, i5 + k4 * i4, byte0 + 9 * j3, 0x80000000);

            for (int k6 = 0; k6 < k2; k6++)
            {
                int j7 = i5 + (k6 % i4) * k4;
                int i8 = byte0 + (k6 / i4) * 9;
                drawRect(j7, i8, (j7 + k4) - 1, i8 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (k6 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(k6);
                fontrenderer.drawStringWithShadow(guiplayerinfo.name, j7, i8, 0xffffff);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/icons.png"));
                int l9 = 0;
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
                drawTexturedModalRect((j7 + k4) - 12, i8, 0 + l9 * 10, 176 + byte1 * 8, 10, 8);
                zLevel -= 100F;
            }
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    private void func_50010_a(FontRenderer par1FontRenderer)
    {
        byte byte0 = 10;
        boolean flag = false;
        int i = 0;
        int j = chatMessageList.size();

        if (j <= 0)
        {
            return;
        }

        if (isChatOpen())
        {
            byte0 = 20;
            flag = true;
        }

        for (int k = 0; k + historyOffset < chatMessageList.size() && k < byte0; k++)
        {
            if (((ChatLine)chatMessageList.get(k)).updateCounter >= 200 && !flag)
            {
                continue;
            }

            ChatLine chatline = (ChatLine)chatMessageList.get(k + historyOffset);
            double d = (double)chatline.updateCounter / 200D;
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
            int l1 = (int)(255D * d);

            if (flag)
            {
                l1 = 255;
            }

            i++;

            if (l1 > 2)
            {
                byte byte1 = 3;
                int j2 = -k * 9;
                String s = chatline.message;
                drawRect(byte1, j2 - 1, byte1 + 320 + 4, j2 + 8, l1 / 2 << 24);
                GL11.glEnable(GL11.GL_BLEND);
                par1FontRenderer.drawStringWithShadow(s, byte1, j2, 0xffffff + (l1 << 24));
            }
        }

        if (flag)
        {
            GL11.glTranslatef(0.0F, par1FontRenderer.FONT_HEIGHT, 0.0F);
            int l = j * par1FontRenderer.FONT_HEIGHT + j;
            int i1 = i * par1FontRenderer.FONT_HEIGHT + i;
            int j1 = (historyOffset * i1) / j;
            int k1 = (i1 * i1) / l;

            if (l != i1)
            {
                char c = j1 <= 0 ? '`' : '\252';
                int i2 = field_50018_o ? 0xcc3333 : 0x3333aa;
                drawRect(0, -j1, 2, -j1 - k1, i2 + (c << 24));
                drawRect(2, -j1, 1, -j1 - k1, 0xcccccc + (c << 24));
            }
        }
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
        sentMessageList.clear();
    }

    /**
     * Adds a chat message to the list of chat messages. Args: msg
     */
    public void addChatMessage(String par1Str)
    {
        boolean flag = isChatOpen();
        boolean flag1 = true;
        String s;

        for (Iterator iterator = mc.fontRenderer.listFormattedStringToWidth(par1Str, 320).iterator(); iterator.hasNext(); chatMessageList.add(0, new ChatLine(s)))
        {
            s = (String)iterator.next();

            if (flag && historyOffset > 0)
            {
                field_50018_o = true;
                adjustHistoryOffset(1);
            }

            if (!flag1)
            {
                s = (new StringBuilder()).append(" ").append(s).toString();
            }

            flag1 = false;
        }

        for (; chatMessageList.size() > 100; chatMessageList.remove(chatMessageList.size() - 1)) { }
    }

    /**
     * Returns the list with the sent chat messages in it.
     */
    public java.util.List getSentMessageList()
    {
        return sentMessageList;
    }

    public void func_50014_d()
    {
        historyOffset = 0;
        field_50018_o = false;
    }

    /**
     * increment/decrement history scroll offset
     */
    public void adjustHistoryOffset(int par1)
    {
        historyOffset += par1;
        int i = chatMessageList.size();

        if (historyOffset > i - 20)
        {
            historyOffset = i - 20;
        }

        if (historyOffset <= 0)
        {
            historyOffset = 0;
            field_50018_o = false;
        }
    }

    public ChatClickData func_50012_a(int par1, int par2)
    {
        if (!isChatOpen())
        {
            return null;
        }

        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        par2 = par2 / scaledresolution.scaleFactor - 40;
        par1 = par1 / scaledresolution.scaleFactor - 3;

        if (par1 < 0 || par2 < 0)
        {
            return null;
        }

        int i = Math.min(20, chatMessageList.size());

        if (par1 <= 320 && par2 < mc.fontRenderer.FONT_HEIGHT * i + i)
        {
            int j = par2 / (mc.fontRenderer.FONT_HEIGHT + 1) + historyOffset;
            return new ChatClickData(mc.fontRenderer, (ChatLine)chatMessageList.get(j), par1, (par2 - (j - historyOffset) * mc.fontRenderer.FONT_HEIGHT) + j);
        }
        else
        {
            return null;
        }
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        recordPlaying = (new StringBuilder()).append("Now playing: ").append(par1Str).toString();
        recordPlayingUpFor = 60;
        recordIsPlaying = true;
    }

    /**
     * Return true if chat gui is open
     */
    public boolean isChatOpen()
    {
        return mc.currentScreen instanceof GuiChat;
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
