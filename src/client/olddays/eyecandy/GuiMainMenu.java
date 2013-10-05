package net.minecraft.src;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;
import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

public class GuiMainMenu extends GuiScreen
{
    public static String version = "OFF";
    public static boolean panorama = true;
    public static boolean oldlogo = false;
    public static boolean texturepacks = false;

    String minecraftLogo[] = {
        " *   * * *   * *** *** *** *** *** ***",
        " ** ** * **  * *   *   * * * * *    * ",
        " * * * * * * * **  *   **  *** **   * ",
        " *   * * *  ** *   *   * * * * *    * ",
        " *   * * *   * *** *** * * * * *    * "
    };
    private static final ResourceLocation blackResource = new ResourceLocation("olddays/black.png");
    private LogoEffectRandomizer logoEffects[][];
    public static boolean replaceGuiSelectWorld = true;

    public GuiButton onlineButton;
    public GuiButton texturePackButton;

    /** The RNG used by the Main Menu Screen. */
    private static final Random rand = new Random();

    /** Counts the number of screen updates. */
    private float updateCounter;
    private float updateCounter2;

    /** The splash message. */
    private String splashText;
    private GuiButton buttonResetDemo;

    /** Timer used to rotate the panorama, increases every tick. */
    private int panoramaTimer;

    /**
     * Texture allocated for the current viewport of the main menu's panorama background.
     */
    private DynamicTexture viewportTexture;
    private boolean field_96141_q;
    private static boolean field_96140_r;
    private static boolean field_96139_s;
    private final Object field_104025_t = new Object();
    private String field_92025_p;
    private String field_104024_v;
    private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
    private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
    private static final ResourceLocation titlePanoramaPaths[] =
    {
        new ResourceLocation("textures/gui/title/background/panorama_0.png"), new ResourceLocation("textures/gui/title/background/panorama_1.png"), new ResourceLocation("textures/gui/title/background/panorama_2.png"), new ResourceLocation("textures/gui/title/background/panorama_3.png"), new ResourceLocation("textures/gui/title/background/panorama_4.png"), new ResourceLocation("textures/gui/title/background/panorama_5.png")
    };
    public static final String field_96138_a;
    private int field_92024_r;
    private int field_92023_s;
    private int field_92022_t;
    private int field_92021_u;
    private int field_92020_v;
    private int field_92019_w;
    private ResourceLocation field_110351_G;
    private GuiButton minecraftRealmsButton;

    public GuiMainMenu()
    {
        field_96141_q = true;
        splashText = "missingno";
        BufferedReader bufferedreader = null;

        try
        {
            ArrayList arraylist = new ArrayList();
            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));

            do
            {
                String s1;

                if ((s1 = bufferedreader.readLine()) == null)
                {
                    break;
                }

                s1 = s1.trim();

                if (!s1.isEmpty())
                {
                    arraylist.add(s1);
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
        updateCounter2 = updateCounter;
        field_92025_p = "";
        String s = System.getProperty("os_architecture");
        String s2 = System.getProperty("java_version");

        if ("ppc".equalsIgnoreCase(s))
        {
            field_92025_p = (new StringBuilder()).append("").append(EnumChatFormatting.BOLD).append("Notice!").append(EnumChatFormatting.RESET).append(" PowerPC compatibility will be dropped in Minecraft 1.6").toString();
            field_104024_v = "http://tinyurl.com/javappc";
        }
        else if (s2 != null && s2.startsWith("1.5"))
        {
            field_92025_p = (new StringBuilder()).append("").append(EnumChatFormatting.BOLD).append("Notice!").append(EnumChatFormatting.RESET).append(" Java 1.5 compatibility will be dropped in Minecraft 1.6").toString();
            field_104024_v = "http://tinyurl.com/javappc";
        }
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
        viewportTexture = new DynamicTexture(256, 256);
        field_110351_G = mc.getTextureManager().getDynamicTextureLocation("background", viewportTexture);
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

        byte byte0 = 24;
        int i = height / 4 + 48;

        if (mc.isDemo())
        {
            addDemoButtons(i, 24);
        }
        else
        {
            addSingleplayerMultiplayerButtons(i, 24);
        }
        if (texturepacks && texturePackButton == null){
            buttonList.add(new GuiButton(10, width / 2 - 100, i + 48, I18n.getString("options.resourcepack")));
        }

        func_130020_g();
        buttonList.add(new GuiButton(0, width / 2 - 100, i + 72 + 12, 98, 20, I18n.getString("menu.options")));
        buttonList.add(new GuiButton(4, width / 2 + 2, i + 72 + 12, 98, 20, I18n.getString("menu.quit")));
        buttonList.add(new GuiButtonLanguage(5, width / 2 - 124, i + 72 + 12));

        synchronized (field_104025_t)
        {
            field_92023_s = fontRenderer.getStringWidth(field_92025_p);
            field_92024_r = fontRenderer.getStringWidth(field_96138_a);
            int j = Math.max(field_92023_s, field_92024_r);
            field_92022_t = (width - j) / 2;
            field_92021_u = ((GuiButton)buttonList.get(0)).yPosition - 24;
            field_92020_v = field_92022_t + j;
            field_92019_w = field_92021_u + 24;
        }
    }

    private void func_130020_g()
    {
        if (field_96141_q)
        {
            if (!field_96140_r)
            {
                field_96140_r = true;
                (new RunnableTitleScreen(this)).start();
            }
            else if (field_96139_s)
            {
                func_130022_h();
            }
        }
    }

    private void func_130022_h()
    {
        if (texturepacks){
            buttonList.add(minecraftRealmsButton = new GuiButton(3, width / 2 + 2, height / 4 + 96, 98, 20, I18n.getString("menu.online")));
            if (texturePackButton != null){
                buttonList.remove(texturePackButton);
            }
            buttonList.add(texturePackButton = new GuiButton(10, width / 2 - 100, height / 4 + 96, 98, 20, I18n.getString("options.resourcepack")));
        }else{
            buttonList.add(minecraftRealmsButton = new GuiButton(3, width / 2 - 100, height / 4 + 96, I18n.getString("menu.online")));
        }
    }

    /**
     * Adds Singleplayer and Multiplayer buttons on Main Menu for players who have bought the game.
     */
    private void addSingleplayerMultiplayerButtons(int par1, int par2)
    {
        buttonList.add(new GuiButton(1, width / 2 - 100, par1, I18n.getString("menu.singleplayer")));
        buttonList.add(new GuiButton(2, width / 2 - 100, par1 + par2 * 1, I18n.getString("menu.multiplayer")));
        buttonList.add(minecraftRealmsButton = new GuiButton(14, width / 2 - 100, par1 + par2 * 2, I18n.getString("menu.online")));
        minecraftRealmsButton.drawButton = false;
    }

    /**
     * Adds Demo buttons on Main Menu for players who are playing Demo.
     */
    private void addDemoButtons(int par1, int par2)
    {
        buttonList.add(new GuiButton(11, width / 2 - 100, par1, I18n.getString("menu.playdemo")));
        buttonList.add(buttonResetDemo = new GuiButton(12, width / 2 - 100, par1 + par2 * 1, I18n.getString("menu.resetdemo")));
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
            mc.displayGuiScreen(new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager()));
        }

        if (par1GuiButton.id == 1)
        {
            mc.displayGuiScreen(new GuiSelectWorld(this));
        }

        if (par1GuiButton.id == 2)
        {
            mc.displayGuiScreen(new GuiMultiplayer(this));
        }

        if (par1GuiButton.id == 14 && minecraftRealmsButton.drawButton)
        {
            func_140005_i();
        }

        if (par1GuiButton.id == 4)
        {
            mc.shutdown();
        }

        if (par1GuiButton.id == 10)
        {
            mc.displayGuiScreen(new GuiScreenTemporaryResourcePackSelect(this, mc.gameSettings));
        }

        if (par1GuiButton.id == 11)
        {
            mc.launchIntegratedServer("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
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

    private void func_140005_i()
    {
        McoClient mcoclient = new McoClient(mc.getSession());

        try
        {
            if (mcoclient.func_140054_c().booleanValue())
            {
                mc.displayGuiScreen(new GuiScreenClientOutdated(this));
            }
            else
            {
                mc.displayGuiScreen(new GuiScreenOnlineServers(this));
            }
        }
        catch (ExceptionMcoService exceptionmcoservice)
        {
            mc.getLogAgent().logSevere(exceptionmcoservice.toString());
        }
        catch (IOException ioexception)
        {
            mc.getLogAgent().logSevere(ioexception.getLocalizedMessage());
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
        else if (par2 == 13)
        {
            if (par1)
            {
                try
                {
                    Class class1 = Class.forName("java.awt.Desktop");
                    Object obj = class1.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
                    class1.getMethod("browse", new Class[]
                            {
                                java.net.URI.class
                            }).invoke(obj, new Object[]
                                    {
                                        new URI(field_104024_v)
                                    });
                }
                catch (Throwable throwable)
                {
                    throwable.printStackTrace();
                }
            }

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
        Project.gluPerspective(120F, 1.0F, 0.05F, 10F);
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

                mc.getTextureManager().bindTexture(titlePanoramaPaths[k]);
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
        mc.getTextureManager().bindTexture(field_110351_G);
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
            mc.getTextureManager().bindTexture(minecraftTitleTextures);
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
            s = "Minecraft "+(new CallableMinecraftVersion(null)).minecraftVersion();
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

        if (field_92025_p != null && field_92025_p.length() > 0)
        {
            drawRect(field_92022_t - 2, field_92021_u - 2, field_92020_v + 2, field_92019_w - 1, 0x55200000);
            drawString(fontRenderer, field_92025_p, field_92022_t, field_92021_u, 0xffffff);
            drawString(fontRenderer, field_96138_a, (width - field_92024_r) / 2, ((GuiButton)buttonList.get(0)).yPosition - 12, 0xffffff);
        }

        super.drawScreen(par1, par2, par3);
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        synchronized (field_104025_t)
        {
            if (field_92025_p.length() > 0 && par1 >= field_92022_t && par1 <= field_92020_v && par2 >= field_92021_u && par2 <= field_92019_w)
            {
                GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this, field_104024_v, 13, true);
                guiconfirmopenlink.func_92026_h();
                mc.displayGuiScreen(guiconfirmopenlink);
            }
        }
    }

    static Minecraft func_110348_a(GuiMainMenu par0GuiMainMenu)
    {
        return par0GuiMainMenu.mc;
    }

    static void func_130021_b(GuiMainMenu par0GuiMainMenu)
    {
        par0GuiMainMenu.func_130022_h();
    }

    static boolean func_110349_a(boolean par0)
    {
        return field_96139_s = par0;
    }

    static Minecraft func_130018_c(GuiMainMenu par0GuiMainMenu)
    {
        return par0GuiMainMenu.mc;
    }

    static Minecraft func_130019_d(GuiMainMenu par0GuiMainMenu)
    {
        return par0GuiMainMenu.mc;
    }

    static
    {
        field_96138_a = (new StringBuilder()).append("Please click ").append(EnumChatFormatting.UNDERLINE).append("here").append(EnumChatFormatting.RESET).append(" for more information.").toString();
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
        Project.gluPerspective(70F, (float)mc.displayWidth / (float)k, 0.05F, 100F);
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
            mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            if(l == 0)
            {
                mc.getTextureManager().bindTexture(blackResource);
            }
            for(int i1 = 0; i1 < minecraftLogo.length; i1++)
            {
                for(int j1 = 0; j1 < minecraftLogo[i1].length(); j1++)
                {
                    char c = minecraftLogo[i1].charAt(j1);
                    if (i1 == 2 && ((double)updateCounter2 < 0.0001D))
                    {
                        if (i1 == 2){
                            c = minecraftLogo[i1].charAt(j1 == 20 ? j1 - 1 : (j1 == 16 ? j1 + 1 : j1));
                        }
                    }
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
        renderblocks.setRenderBoundsFromBlock(block);
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
            renderblocks.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(0));
            tessellator.setColorRGBA_F(f1, f1, f1, f);
            renderblocks.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(1));
            tessellator.setColorRGBA_F(f3, f3, f3, f);
            renderblocks.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(2));
            renderblocks.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(3));
            tessellator.setColorRGBA_F(f4, f4, f4, f);
            renderblocks.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(4));
            renderblocks.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getBlockTextureFromSide(5));
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }
    }
}
