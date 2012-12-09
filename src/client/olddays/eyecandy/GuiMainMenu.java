package net.minecraft.src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GuiMainMenu extends GuiScreen
{
    public static String version = "OFF";
    public static boolean panorama = true;
    public static boolean oldlogo = false;
    String minecraftLogo[] = {
        " *   * * *   * *** *** *** *** *** ***",
        " ** ** * **  * *   *   * * * * *    * ",
        " * * * * * * * **  *   **  *** **   * ",
        " *   * * *  ** *   *   * * * * *    * ",
        " *   * * *   * *** *** * * * * *    * "
    };
    private LogoEffectRandomizer logoEffects[][];
    public static boolean replaceGuiSelectWorld = true;

    /** The RNG used by the Main Menu Screen. */
    private static final Random rand = new Random();

    /** Counts the number of screen updates. */
    private float updateCounter;

    /** The splash message. */
    private String splashText;
    private GuiButton buttonResetDemo;

    /** Timer used to rotate the panorama, increases every tick. */
    private int panoramaTimer;

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private int viewportTexture;
    private static final String titlePanoramaPaths[] =
    {
        "/title/bg/panorama0.png", "/title/bg/panorama1.png", "/title/bg/panorama2.png", "/title/bg/panorama3.png", "/title/bg/panorama4.png", "/title/bg/panorama5.png"
    };

    public GuiMainMenu()
    {
        updateCounter = 0.0F;
        panoramaTimer = 0;
        splashText = "missingno";
        BufferedReader bufferedreader = null;

        try
        {
            ArrayList arraylist = new ArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader((net.minecraft.src.GuiMainMenu.class).getResourceAsStream("/title/splashes.txt"), Charset.forName("UTF-8")));

            do
            {
                String s;

                if ((s = bufferedreader.readLine()) == null)
                {
                    break;
                }

                s = s.trim();

                if (s.length() > 0)
                {
                    arraylist.add(s);
                }
            }
            while (true);

            do
            {
                splashText = (String)arraylist.get(rand.nextInt(arraylist.size()));
            }
            while (splashText.hashCode() == 0x77f432f);
        }
        catch (IOException ioexception) { }
        finally
        {
            if (bufferedreader != null)
            {
                try
                {
                    bufferedreader.close();
                }
                catch (IOException ioexception1) { }
            }
        }

        updateCounter = rand.nextFloat();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (panorama){
            panoramaTimer++;
        }
        if (oldlogo){
            updateCounter++;
            if(logoEffects != null)
            {
                for(int i = 0; i < logoEffects.length; i++)
                {
                    for(int j = 0; j < logoEffects[i].length; j++)
                    {
                        logoEffects[i][j].func_875_a();
                    }
                }
            }
        }
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        viewportTexture = mc.renderEngine.allocateAndSetupTexture(new java.awt.image.BufferedImage(256, 256, 2));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9)
        {
            splashText = "Happy birthday, ez!";
        }
        else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1)
        {
            splashText = "Happy birthday, Notch!";
        }
        else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24)
        {
            splashText = "Merry X-mas!";
        }
        else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1)
        {
            splashText = "Happy new year!";
        }
        else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31)
        {
            splashText = "OOoooOOOoooo! Spooky!";
        }

        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = height / 4 + 48;

        if (mc.isDemo())
        {
            addDemoButtons(i, 24, stringtranslate);
        }
        else
        {
            addSingleplayerMultiplayerButtons(i, 24, stringtranslate);
        }

        controlList.add(new GuiButton(3, width / 2 - 100, i + 48, stringtranslate.translateKey("menu.mods")));

        if (mc.hideQuitButton)
        {
            controlList.add(new GuiButton(0, width / 2 - 100, i + 72, stringtranslate.translateKey("menu.options")));
        }
        else
        {
            controlList.add(new GuiButton(0, width / 2 - 100, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.options")));
            controlList.add(new GuiButton(4, width / 2 + 2, i + 72 + 12, 98, 20, stringtranslate.translateKey("menu.quit")));
        }

        controlList.add(new GuiButtonLanguage(5, width / 2 - 124, i + 72 + 12));
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int par1, int par2, StringTranslate par3StringTranslate)
    {
        controlList.add(new GuiButton(1, width / 2 - 100, par1, par3StringTranslate.translateKey("menu.singleplayer")));
        controlList.add(new GuiButton(2, width / 2 - 100, par1 + par2 * 1, par3StringTranslate.translateKey("menu.multiplayer")));
    }

    /**
     * Adds Demo buttons on Main Menu for players who are playing Demo.
     */
    private void addDemoButtons(int par1, int par2, StringTranslate par3StringTranslate)
    {
        controlList.add(new GuiButton(11, width / 2 - 100, par1, par3StringTranslate.translateKey("menu.playdemo")));
        controlList.add(buttonResetDemo = new GuiButton(12, width / 2 - 100, par1 + par2 * 1, par3StringTranslate.translateKey("menu.resetdemo")));
        ISaveFormat isaveformat = mc.getSaveLoader();
        WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

        if (worldinfo == null)
        {
            buttonResetDemo.enabled = false;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
        }

        if (par1GuiButton.id == 5)
        {
            mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings));
        }

        if (par1GuiButton.id == 1)
        {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (par1GuiButton.id == 2)
        {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (par1GuiButton.id == 3)
        {
            mc.displayGuiScreen(new GuiTexturePacks(this));
        }

        if (par1GuiButton.id == 4)
        {
            mc.shutdown();
        }

        if (par1GuiButton.id == 11)
        {
            mc.enableSP = mc.useSP;
            if (mc.enableSP){
                mc.playerController = new PlayerControllerDemo(mc);
                mc.startWorldSSP("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
                mc.displayGuiScreen(null);
            }else{
                mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
            }
        }

        if (par1GuiButton.id == 12)
        {
            ISaveFormat isaveformat = mc.getSaveLoader();
            WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

            if (worldinfo != null)
            {
                GuiYesNo guiyesno = GuiSelectWorld.getDeleteWorldScreen(this, worldinfo.getWorldName(), 12);
                mc.displayGuiScreen(guiyesno);
            }
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (par1 && par2 == 12)
        {
            ISaveFormat isaveformat = mc.getSaveLoader();
            isaveformat.flushCache();
            isaveformat.deleteWorldDirectory("Demo_World");
            mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the main menu panorama
     */
    private void drawPanorama(int par1, int par2, float par3)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GLU.gluPerspective(120F, 1.0F, 0.05F, 10F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glRotatef(180F, 1.0F, 0.0F, 0.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        int i = 8;

        for (int j = 0; j < i * i; j++)
        {
            GL11.glPushMatrix();
            float f = ((float)(j % i) / (float)i - 0.5F) / 64F;
            float f1 = ((float)(j / i) / (float)i - 0.5F) / 64F;
            float f2 = 0.0F;
            GL11.glTranslatef(f, f1, f2);
            GL11.glRotatef(MathHelper.sin(((float)panoramaTimer + par3) / 400F) * 25F + 20F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-((float)panoramaTimer + par3) * 0.1F, 0.0F, 1.0F, 0.0F);

            for (int k = 0; k < 6; k++)
            {
                GL11.glPushMatrix();

                if (k == 1)
                {
                    GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 2)
                {
                    GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 3)
                {
                    GL11.glRotatef(-90F, 0.0F, 1.0F, 0.0F);
                }

                if (k == 4)
                {
                    GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
                }

                if (k == 5)
                {
                    GL11.glRotatef(-90F, 1.0F, 0.0F, 0.0F);
                }

                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture(titlePanoramaPaths[k]));
                tessellator.startDrawingQuads();
                tessellator.setColorRGBA_I(0xffffff, 255 / (j + 1));
                float f3 = 0.0F;
                tessellator.addVertexWithUV(-1D, -1D, 1.0D, 0.0F + f3, 0.0F + f3);
                tessellator.addVertexWithUV(1.0D, -1D, 1.0D, 1.0F - f3, 0.0F + f3);
                tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D, 1.0F - f3, 1.0F - f3);
                tessellator.addVertexWithUV(-1D, 1.0D, 1.0D, 0.0F + f3, 1.0F - f3);
                tessellator.draw();
                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
            GL11.glColorMask(true, true, true, false);
        }

        tessellator.setTranslation(0.0D, 0.0D, 0.0D);
        GL11.glColorMask(true, true, true, true);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

    /**
     * Rotate and blurs the skybox view in the main menu
     */
    private void rotateAndBlurSkybox(float par1)
    {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, viewportTexture);
        GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColorMask(true, true, true, false);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        byte byte0 = 3;

        for (int i = 0; i < byte0; i++)
        {
            tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float)(i + 1));
            int j = width;
            int k = height;
            float f = (float)(i - byte0 / 2) / 256F;
            tessellator.addVertexWithUV(j, k, zLevel, 0.0F + f, 0.0D);
            tessellator.addVertexWithUV(j, 0.0D, zLevel, 1.0F + f, 0.0D);
            tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 1.0F + f, 1.0D);
            tessellator.addVertexWithUV(0.0D, k, zLevel, 0.0F + f, 1.0D);
        }

        tessellator.draw();
        GL11.glColorMask(true, true, true, true);
    }

    /**
     * Renders the skybox in the main menu
     */
    private void renderSkybox(int par1, int par2, float par3)
    {
        GL11.glViewport(0, 0, 256, 256);
        drawPanorama(par1, par2, par3);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        rotateAndBlurSkybox(par3);
        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        float f = width <= height ? 120F / (float)height : 120F / (float)width;
        float f1 = ((float)height * f) / 256F;
        float f2 = ((float)width * f) / 256F;
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
        int i = width;
        int j = height;
        tessellator.addVertexWithUV(0.0D, j, zLevel, 0.5F - f1, 0.5F + f2);
        tessellator.addVertexWithUV(i, j, zLevel, 0.5F - f1, 0.5F - f2);
        tessellator.addVertexWithUV(i, 0.0D, zLevel, 0.5F + f1, 0.5F - f2);
        tessellator.addVertexWithUV(0.0D, 0.0D, zLevel, 0.5F + f1, 0.5F + f2);
        tessellator.draw();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        if (panorama){
            renderSkybox(par1, par2, par3);
        }else{
            drawDefaultBackground();
        }
        Tessellator tessellator = Tessellator.instance;
        if (oldlogo){
            drawLogo(par3);
        }
        char c = 274;
        int i = width / 2 - c / 2;
        byte byte0 = 30;
        if (panorama){
            drawGradientRect(0, 0, width, height, 0x80ffffff, 0xffffff);
            drawGradientRect(0, 0, width, height, 0, 0x80000000);
        }
        if (!oldlogo){
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/title/mclogo.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

            if ((double)updateCounter < 0.0001D)
            {
                drawTexturedModalRect(i + 0, byte0 + 0, 0, 0, 99, 44);
                drawTexturedModalRect(i + 99, byte0 + 0, 129, 0, 27, 44);
                drawTexturedModalRect(i + 99 + 26, byte0 + 0, 126, 0, 3, 44);
                drawTexturedModalRect(i + 99 + 26 + 3, byte0 + 0, 99, 0, 26, 44);
                drawTexturedModalRect(i + 155, byte0 + 0, 0, 45, 155, 44);
            }
            else
            {
                drawTexturedModalRect(i + 0, byte0 + 0, 0, 0, 155, 44);
                drawTexturedModalRect(i + 155, byte0 + 0, 0, 45, 155, 44);
            }
        }

        tessellator.setColorOpaque_I(0xffffff);
        GL11.glPushMatrix();
        GL11.glTranslatef(width / 2 + 90, 70F, 0.0F);
        GL11.glRotatef(-20F, 0.0F, 0.0F, 1.0F);
        float f = 1.8F - MathHelper.abs(MathHelper.sin(((float)(Minecraft.getSystemTime() % 1000L) / 1000F) * (float)Math.PI * 2.0F) * 0.1F);
        f = (f * 100F) / (float)(fontRenderer.getStringWidth(splashText) + 32);
        GL11.glScalef(f, f, f);
        drawCenteredString(fontRenderer, splashText, 0, -8, 0xffff00);
        GL11.glPopMatrix();
        String s = version.contains(":") ? version.split(":", 2)[1] : version;
        if (s.equals("OFF")){
            s = "Minecraft 1.4.4";
        }
        if (mc.isDemo())
        {
            s = (new StringBuilder()).append(s).append(" Demo").toString();
        }
        if (panorama){
            drawString(fontRenderer, s, 2, height - 10, 0xffffff);
        }else{
            drawString(fontRenderer, s, 2, 2, 0x505050);
        }
        String s1 = "Copyright Mojang AB. Do not distribute!";
        drawString(fontRenderer, s1, width - fontRenderer.getStringWidth(s1) - 2, height - 10, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }

    private void drawLogo(float f)
    {
        if(logoEffects == null)
        {
            logoEffects = new LogoEffectRandomizer[minecraftLogo[0].length()][minecraftLogo.length];
            for(int i = 0; i < logoEffects.length; i++)
            {
                for(int j = 0; j < logoEffects[i].length; j++)
                {
                    logoEffects[i][j] = new LogoEffectRandomizer(this, i, j);
                }

            }

        }
        if (!panorama){
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int k = 120 * scaledresolution.getScaleFactor();
        GLU.gluPerspective(70F, (float)mc.displayWidth / (float)k, 0.05F, 100F);
        GL11.glViewport(0, mc.displayHeight - k, mc.displayWidth, k);
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glDepthMask(true);
        RenderBlocks renderblocks = new RenderBlocks();
        for(int l = 0; l < 3; l++)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(0.4F, 0.6F, -13F);
            if(l == 0)
            {
                GL11.glClear(256);
                GL11.glTranslatef(0.0F, -0.4F, 0.0F);
                GL11.glScalef(0.98F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(770, 771);
            }
            if(l == 1)
            {
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glClear(256);
            }
            if(l == 2)
            {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(768, 1);
            }
            GL11.glScalef(1.0F, -1F, 1.0F);
            GL11.glRotatef(15F, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(0.89F, 1.0F, 0.4F);
            GL11.glTranslatef((float)(-minecraftLogo[0].length()) * 0.5F, (float)(-minecraftLogo.length) * 0.5F, 0.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
            if(l == 0)
            {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/title/black.png"));
            }
            for(int i1 = 0; i1 < minecraftLogo.length; i1++)
            {
                for(int j1 = 0; j1 < minecraftLogo[i1].length(); j1++)
                {
                    char c = minecraftLogo[i1].charAt(j1);
                    if(c == ' ')
                    {
                        continue;
                    }
                    GL11.glPushMatrix();
                    LogoEffectRandomizer logoeffectrandomizer = logoEffects[j1][i1];
                    float f1 = (float)(logoeffectrandomizer.field_1311_b + (logoeffectrandomizer.field_1312_a - logoeffectrandomizer.field_1311_b) * (double)f);
                    float f2 = 1.0F;
                    float f3 = 1.0F;
                    float f4 = 0.0F;
                    if(l == 0)
                    {
                        f2 = f1 * 0.04F + 1.0F;
                        f3 = 1.0F / f2;
                        f1 = 0.0F;
                    }
                    GL11.glTranslatef(j1, i1, f1);
                    GL11.glScalef(f2, f2, f2);
                    GL11.glRotatef(f4, 0.0F, 1.0F, 0.0F);
                    renderMenuBlock(Block.stone, f3, renderblocks);
                    GL11.glPopMatrix();
                }

            }

            GL11.glPopMatrix();
        }

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888 /*GL_MODELVIEW0_ARB*/);
        GL11.glPopMatrix();
        GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    static Random getRand()
    {
        return rand;
    }

    private void renderMenuBlock(Block block, float f, RenderBlocks renderblocks)
    {
        int i = block.getRenderType();
        renderblocks.func_83018_a(block);
        Tessellator tessellator = Tessellator.instance;
        if(i == 0)
        {
            block.setBlockBoundsForItemRender();
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float f1 = 0.5F;
            float f2 = 1.0F;
            float f3 = 0.8F;
            float f4 = 0.6F;
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f2, f2, f2, f);
            renderblocks.renderBottomFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
            tessellator.setColorRGBA_F(f1, f1, f1, f);
            renderblocks.renderTopFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
            tessellator.setColorRGBA_F(f3, f3, f3, f);
            renderblocks.renderEastFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
            renderblocks.renderWestFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
            tessellator.setColorRGBA_F(f4, f4, f4, f);
            renderblocks.renderNorthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
            renderblocks.renderSouthFace(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }
}
