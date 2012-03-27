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
    private static RenderItem itemRenderer = new RenderItem();

    /** A list with all the chat messages in. */
    private java.util.List chatMessageList;
    private java.util.List field_50016_f;
    private Random rand;
    private Minecraft mc;
    public String field_933_a;
    private int updateCounter;

    /** The string specifying which record music is playing */
    private String recordPlaying;

    /** How many ticks the record playing message will be displayed */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;
    private int field_50017_n;
    private boolean field_50018_o;

    /** Damage partial time (GUI) */
    public float damageGuiPartialTime;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    float prevVignetteBrightness;

    public GuiIngame(Minecraft par1Minecraft)
    {
        chatMessageList = new ArrayList();
        field_50016_f = new ArrayList();
        rand = new Random();
        field_933_a = null;
        updateCounter = 0;
        recordPlaying = "";
        recordPlayingUpFor = 0;
        recordIsPlaying = false;
        field_50017_n = 0;
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
            if(mod_WTFGameplay.DisableHunger){
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

            int l1 = mc.thePlayer.getHealth();
            int i3 = mc.thePlayer.prevHealth;
            rand.setSeed(updateCounter * 0x4c627);
            boolean flag2 = false;
            FoodStats foodstats = mc.thePlayer.getFoodStats();
            int k4 = foodstats.getFoodLevel();
            int j5 = foodstats.getPrevFoodLevel();
            renderBossHealth();

            if (mc.playerController.shouldDrawHUD())
            {
                int k5 = i / 2 - 91;
                int k6 = i / 2 + 91;
                int i8 = j - 32;
                if(!mod_WTFGameplay.DisableXP){
                    if ( mc.thePlayer.xpBarCap() > 0)
                    {
                        char c = '\266';
                        int j8 = (int)(mc.thePlayer.experience * (float)(c + 1));
                        int i9 = (j - 32) + 3;
                        drawTexturedModalRect(k5, i9, 0, 64, c, 5);
                        if (j8 > 0)
                        {
                            drawTexturedModalRect(k5, i9, 0, 69, j8, 5);
                        }
                    }
                    i8 = j - 39;
                }
                int k8 = i8 - 10;
                int j9 = mc.thePlayer.getTotalArmorValue();
                int k9 = -1;

                if (mc.thePlayer.isPotionActive(Potion.regeneration))
                {
                    k9 = updateCounter % 25;
                }

                for (int l9 = 0; l9 < 10; l9++)
                {
                    if (j9 > 0)
                    {
                        int k10 = k6 - l9 * 8 - 9;
                        int intintintintint = i8;
                        if(!mod_WTFGameplay.DisableHunger){
                            k10 = k5 + l9 * 8;
                            intintintintint = i8 - 10;
                        }

                        if (l9 * 2 + 1 < j9)
                        {
                            drawTexturedModalRect(k10, intintintintint, 34, 9, 9, 9);
                        }

                        if (l9 * 2 + 1 == j9)
                        {
                            drawTexturedModalRect(k10, intintintintint, 25, 9, 9, 9);
                        }

                        if (l9 * 2 + 1 > j9)
                        {
                            drawTexturedModalRect(k10, intintintintint, 16, 9, 9, 9);
                        }
                    }

                    int l10 = 16;

                    if (mc.thePlayer.isPotionActive(Potion.poison))
                    {
                        l10 += 36;
                    }

                    int k11 = 0;

                    if (flag1)
                    {
                        k11 = 1;
                    }

                    int j12 = k5 + l9 * 8;
                    int k12 = i8;

                    if (l1 <= 4)
                    {
                        k12 += rand.nextInt(2);
                    }

                    if (l9 == k9)
                    {
                        k12 -= 2;
                    }

                    byte byte3 = 0;

                    if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    {
                        byte3 = 5;
                    }

                    drawTexturedModalRect(j12, k12, 16 + k11 * 9, 9 * byte3, 9, 9);

                    if (flag1)
                    {
                        if (l9 * 2 + 1 < i3)
                        {
                            drawTexturedModalRect(j12, k12, l10 + 54, 9 * byte3, 9, 9);
                        }

                        if (l9 * 2 + 1 == i3)
                        {
                            drawTexturedModalRect(j12, k12, l10 + 63, 9 * byte3, 9, 9);
                        }
                    }

                    if (l9 * 2 + 1 < l1)
                    {
                        drawTexturedModalRect(j12, k12, l10 + 36, 9 * byte3, 9, 9);
                    }

                    if (l9 * 2 + 1 == l1)
                    {
                        drawTexturedModalRect(j12, k12, l10 + 45, 9 * byte3, 9, 9);
                    }
                }

                if(!mod_WTFGameplay.DisableHunger){
                    for (int i10 = 0; i10 < 10; i10++)
                    {
                        int i11 = i8;
                        int l11 = 16;
                        byte byte2 = 0;

                        if (mc.thePlayer.isPotionActive(Potion.hunger))
                        {
                            l11 += 36;
                            byte2 = 13;
                        }

                        if (mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (k4 * 3 + 1) == 0)
                        {
                            i11 += rand.nextInt(3) - 1;
                        }

                        if (flag2)
                        {
                            byte2 = 1;
                        }

                        int l12 = k6 - i10 * 8 - 9;
                        drawTexturedModalRect(l12, i11, 16 + byte2 * 9, 27, 9, 9);

                        if (flag2)
                        {
                            if (i10 * 2 + 1 < j5)
                            {
                                drawTexturedModalRect(l12, i11, l11 + 54, 27, 9, 9);
                            }

                            if (i10 * 2 + 1 == j5)
                            {
                                drawTexturedModalRect(l12, i11, l11 + 63, 27, 9, 9);
                            }
                        }

                        if (i10 * 2 + 1 < k4)
                        {
                            drawTexturedModalRect(l12, i11, l11 + 36, 27, 9, 9);
                        }

                        if (i10 * 2 + 1 == k4)
                        {
                            drawTexturedModalRect(l12, i11, l11 + 45, 27, 9, 9);
                        }
                    }
                }

                if (mc.thePlayer.isInsideOfMaterial(Material.water))
                {
                    int j10 = (int)Math.ceil(((double)(mc.thePlayer.getAir() - 2) * 10D) / 300D);
                    int j11 = (int)Math.ceil(((double)mc.thePlayer.getAir() * 10D) / 300D) - j10;

                    for (int i12 = 0; i12 < j10 + j11; i12++)
                    {
                        if (i12 < j10)
                        {
                            if(mod_WTFGameplay.DisableHunger){
                                drawTexturedModalRect(k5 + i12 * 8, k8, 16, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(k6 - i12 * 8 - 9, k8, 16, 18, 9, 9);
                            }
                        }
                        else
                        {
                            if(mod_WTFGameplay.DisableHunger){
                                drawTexturedModalRect(k5 + i12 * 8, k8, 25, 18, 9, 9);
                            }else{
                                drawTexturedModalRect(k6 - i12 * 8 - 9, k8, 25, 18, 9, 9);
                            }
                        }
                    }
                }
            }

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int i6 = 0; i6 < 9; i6++)
            {
                int l6 = (i / 2 - 90) + i6 * 20 + 2;
                int k7 = j - 16 - 3;
                renderInventorySlot(i6, l6, k7, par1);
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

        if (mc.playerController.func_35642_f() && mc.thePlayer.experienceLevel > 0 && !mod_WTFGameplay.DisableXP)
        {
            boolean flag = false;
            int i1 = flag ? 0xffffff : 0x80ff20;
            String s = (new StringBuilder()).append("").append(mc.thePlayer.experienceLevel).toString();
            int j3 = (i - fontrenderer.getStringWidth(s)) / 2;
            int l3 = j - 31 - 4;
            fontrenderer.drawString(s, j3 + 1, l3, 0);
            fontrenderer.drawString(s, j3 - 1, l3, 0);
            fontrenderer.drawString(s, j3, l3 + 1, 0);
            fontrenderer.drawString(s, j3, l3 - 1, 0);
            fontrenderer.drawString(s, j3, l3, i1);
        }

        if (mc.gameSettings.showDebugInfo && mod_WTFGameplay.AllowDebug)
        {
            GL11.glPushMatrix();

            if (Minecraft.hasPaidCheckTime > 0L)
            {
                GL11.glTranslatef(0.0F, 32F, 0.0F);
            }

            fontrenderer.func_50103_a((new StringBuilder()).append("Minecraft 1.2.4 (").append(mc.debug).append(")").toString(), 2, 2, 0xffffff);
            fontrenderer.func_50103_a(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.func_50103_a(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.func_50103_a(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.func_50103_a(mc.getWorldProviderName(), 2, 42, 0xffffff);
            long l = Runtime.getRuntime().maxMemory();
            long l2 = Runtime.getRuntime().totalMemory();
            long l4 = Runtime.getRuntime().freeMemory();
            long l5 = l2 - l4;
            String s1 = (new StringBuilder()).append("Used memory: ").append((l5 * 100L) / l).append("% (").append(l5 / 1024L / 1024L).append("MB) of ").append(l / 1024L / 1024L).append("MB").toString();
            drawString(fontrenderer, s1, i - fontrenderer.getStringWidth(s1) - 2, 2, 0xe0e0e0);
            s1 = (new StringBuilder()).append("Allocated memory: ").append((l2 * 100L) / l).append("% (").append(l2 / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s1, i - fontrenderer.getStringWidth(s1) - 2, 12, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("x: ").append(mc.thePlayer.posX).toString(), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("y: ").append(mc.thePlayer.posY).toString(), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("z: ").append(mc.thePlayer.posZ).toString(), 2, 80, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3).toString(), 2, 88, 0xe0e0e0);
            int j0 = MathHelper.floor_double(mc.thePlayer.posX);
            int j1 = MathHelper.floor_double(mc.thePlayer.posY);
            int j2 = MathHelper.floor_double(mc.thePlayer.posZ);

            if (mc.theWorld != null && mc.theWorld.blockExists(j0, j1, j2))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(j0, j2);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.getTopFilledSegment() + 15).append(" b: ").append(chunk.func_48490_a(j0 & 0xf, j2 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, j0 & 0xf, j1, j2 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, j0 & 0xf, j1, j2 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(j0 & 0xf, j1, j2 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            if (!mc.theWorld.isRemote)
            {
                drawString(fontrenderer, (new StringBuilder()).append("Seed: ").append(mc.theWorld.getSeed()).toString(), 2, 112, 0xe0e0e0);
            }

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
            int k3 = k2;
            int i4 = 1;

            for (; k3 > 20; k3 = ((k2 + i4) - 1) / i4)
            {
                i4++;
            }

            int j4 = 300 / i4;

            if (j4 > 150)
            {
                j4 = 150;
            }

            int i5 = (i - i4 * j4) / 2;
            byte byte0 = 10;
            drawRect(i5 - 1, byte0 - 1, i5 + j4 * i4, byte0 + 9 * k3, 0x80000000);

            for (int j6 = 0; j6 < k2; j6++)
            {
                int i7 = i5 + (j6 % i4) * j4;
                int l7 = byte0 + (j6 / i4) * 9;
                drawRect(i7, l7, (i7 + j4) - 1, l7 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (j6 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(j6);
                fontrenderer.func_50103_a(guiplayerinfo.name, i7, l7, 0xffffff);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/icons.png"));
                int l8 = 0;
                byte byte1 = 0;
                l8 = 0;
                byte1 = 0;

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
                drawTexturedModalRect((i7 + j4) - 12, l7, 0 + l8 * 10, 176 + byte1 * 8, 10, 8);
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

        if (func_50015_e())
        {
            byte0 = 20;
            flag = true;
        }

        for (int k = 0; k + field_50017_n < chatMessageList.size() && k < byte0; k++)
        {
            if (((ChatLine)chatMessageList.get(k)).updateCounter >= 200 && !flag)
            {
                continue;
            }

            ChatLine chatline = (ChatLine)chatMessageList.get(k + field_50017_n);
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
                par1FontRenderer.func_50103_a(s, byte1, j2, 0xffffff + (l1 << 24));
            }
        }

        if (flag)
        {
            GL11.glTranslatef(0.0F, par1FontRenderer.FONT_HEIGHT, 0.0F);
            int l = j * par1FontRenderer.FONT_HEIGHT + j;
            int i1 = i * par1FontRenderer.FONT_HEIGHT + i;
            int j1 = (field_50017_n * i1) / j;
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
        int k = (int)(((float)entitydragon.func_41010_ax() / (float)entitydragon.getMaxHealth()) * (float)(c + 1));
        byte byte0 = 12;
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);

        if (k > 0)
        {
            drawTexturedModalRect(j, byte0, 0, 79, k, 5);
        }

        String s = "Boss health";
        fontrenderer.func_50103_a(s, i / 2 - fontrenderer.getStringWidth(s) / 2, byte0 - 10, 0xff00ff);
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
        field_50016_f.clear();
    }

    /**
     * Adds a chat message to the list of chat messages. Args: msg
     */
    public void addChatMessage(String par1Str)
    {
        boolean flag = func_50015_e();
        boolean flag1 = true;
        String s;

        for (Iterator iterator = mc.fontRenderer.func_50108_c(par1Str, 320).iterator(); iterator.hasNext(); chatMessageList.add(0, new ChatLine(s)))
        {
            s = (String)iterator.next();

            if (flag && field_50017_n > 0)
            {
                field_50018_o = true;
                func_50011_a(1);
            }

            if (!flag1)
            {
                s = (new StringBuilder()).append(" ").append(s).toString();
            }

            flag1 = false;
        }

        for (; chatMessageList.size() > 100; chatMessageList.remove(chatMessageList.size() - 1)) { }
    }

    public java.util.List func_50013_c()
    {
        return field_50016_f;
    }

    public void func_50014_d()
    {
        field_50017_n = 0;
        field_50018_o = false;
    }

    public void func_50011_a(int par1)
    {
        field_50017_n += par1;
        int i = chatMessageList.size();

        if (field_50017_n > i - 20)
        {
            field_50017_n = i - 20;
        }

        if (field_50017_n <= 0)
        {
            field_50017_n = 0;
            field_50018_o = false;
        }
    }

    public ChatClickData func_50012_a(int par1, int par2)
    {
        if (!func_50015_e())
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
            int j = par2 / (mc.fontRenderer.FONT_HEIGHT + 1) + field_50017_n;
            return new ChatClickData(mc.fontRenderer, (ChatLine)chatMessageList.get(j), par1, (par2 - (j - field_50017_n) * mc.fontRenderer.FONT_HEIGHT) + j);
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

    public boolean func_50015_e()
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
