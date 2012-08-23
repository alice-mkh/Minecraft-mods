package net.minecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import net.minecraft.src.AABBPool;
import net.minecraft.src.Achievement;
import net.minecraft.src.AchievementList;
import net.minecraft.src.AnvilSaveConverter;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.CallableClientProfiler;
import net.minecraft.src.CallableGLInfo;
import net.minecraft.src.CallableLWJGLVersion;
import net.minecraft.src.CallableModded;
import net.minecraft.src.CallableType2;
import net.minecraft.src.ColorizerFoliage;
import net.minecraft.src.ColorizerGrass;
import net.minecraft.src.ColorizerWater;
import net.minecraft.src.Container;
import net.minecraft.src.CrashReport;
import net.minecraft.src.EffectRenderer;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.EnumOS;
import net.minecraft.src.EnumOSHelper;
import net.minecraft.src.EnumOptions;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.GuiAchievement;
import net.minecraft.src.GuiChat;
import net.minecraft.src.GuiConnecting;
import net.minecraft.src.GuiErrorScreen;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiMemoryErrorScreen;
import net.minecraft.src.GuiNewChat;
import net.minecraft.src.GuiParticle;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiSleepMP;
import net.minecraft.src.IPlayerUsage;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.IntegratedServer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemRenderer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.LoadingScreenRenderer;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MemoryConnection;
import net.minecraft.src.MinecraftError;
import net.minecraft.src.MinecraftFakeLauncher;
import net.minecraft.src.MinecraftINNER11;
import net.minecraft.src.MouseHelper;
import net.minecraft.src.MovementInputFromOptions;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Packet3Chat;
import net.minecraft.src.PlayerCapabilities;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.PlayerUsageSnooper;
import net.minecraft.src.Profiler;
import net.minecraft.src.ProfilerResult;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderEngine;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ReportedException;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.ScreenShotHelper;
import net.minecraft.src.ServerData;
import net.minecraft.src.Session;
import net.minecraft.src.SoundManager;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatFileWriter;
import net.minecraft.src.StatList;
import net.minecraft.src.StatStringFormatKeyInv;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TextureCompassFX;
import net.minecraft.src.TextureFlamesFX;
import net.minecraft.src.TextureLavaFX;
import net.minecraft.src.TextureLavaFlowFX;
import net.minecraft.src.TexturePackBase;
import net.minecraft.src.TexturePackList;
import net.minecraft.src.TexturePortalFX;
import net.minecraft.src.TextureWatchFX;
import net.minecraft.src.TextureWaterFX;
import net.minecraft.src.TextureWaterFlowFX;
import net.minecraft.src.ThreadClientSleep;
import net.minecraft.src.ThreadDownloadResources;
import net.minecraft.src.ThreadShutdown;
import net.minecraft.src.Timer;
import net.minecraft.src.Vec3;
import net.minecraft.src.Vec3Pool;
import net.minecraft.src.WorldClient;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldRenderer;
import net.minecraft.src.WorldSettings;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import net.minecraft.src.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.zip.*;

public abstract class Minecraft implements Runnable, IPlayerUsage
{
    public static byte field_71444_a[] = new byte[0xa00000];
    private ServerData field_71422_O;

    /**
     * Set to 'this' in Minecraft constructor; used by some settings get methods
     */
    private static Minecraft theMinecraft;
    public PlayerControllerMP field_71442_b;
    private boolean fullscreen;
    private boolean hasCrashed;
    private CrashReport field_71433_S;
    public int displayWidth;
    public int displayHeight;
    private Timer timer;
    private PlayerUsageSnooper field_71427_U;
    public WorldClient field_71441_e;
    public RenderGlobal renderGlobal;
    public EntityClientPlayerMP field_71439_g;

    /**
     * The Entity from which the renderer determines the render viewpoint. Currently is always the parent Minecraft
     * class's 'thePlayer' instance. Modification of its location, rotation, or other settings at render time will
     * modify the camera likewise, with the caveat of triggering chunk rebuilds as it moves, making it unsuitable for
     * changing the viewpoint mid-render.
     */
    public EntityLiving renderViewEntity;
    public EffectRenderer effectRenderer;
    public Session session;
    public String minecraftUri;
    public Canvas mcCanvas;

    /** a boolean to hide a Quit button from the main menu */
    public boolean hideQuitButton;
    public volatile boolean isGamePaused;

    /** The RenderEngine instance used by Minecraft */
    public RenderEngine renderEngine;

    /** The font renderer used for displaying and measuring text. */
    public FontRenderer fontRenderer;
    public FontRenderer standardGalacticFontRenderer;

    /** The GuiScreen that's being displayed at the moment. */
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;

    /** Reference to the download resources thread. */
    private ThreadDownloadResources downloadResourcesThread;

    /** Mouse left click counter */
    private int leftClickCounter;

    /** Display width */
    private int tempDisplayWidth;

    /** Display height */
    private int tempDisplayHeight;
    private IntegratedServer field_71437_Z;

    /** Gui achievement */
    public GuiAchievement guiAchievement;
    public GuiIngame ingameGUI;

    /** Skip render world */
    public boolean skipRenderWorld;

    /** The ray trace hit that the mouse is over. */
    public MovingObjectPosition objectMouseOver;

    /** The game settings that currently hold effect. */
    public GameSettings gameSettings;
    protected MinecraftApplet mcApplet;
    public SoundManager sndManager;

    /** Mouse helper instance. */
    public MouseHelper mouseHelper;

    /** The TexturePackLister used by this instance of Minecraft... */
    public TexturePackList texturePackList;
    public File mcDataDir;
    private ISaveFormat saveLoader;
    private static int field_71470_ab;

    /**
     * When you place a block, it's set to 6, decremented once per tick, when it's 0, you can place another block.
     */
    private int rightClickDelayTimer;
    private boolean field_71468_ad;

    /** Stat file writer */
    public StatFileWriter statFileWriter;
    private String serverName;
    private int serverPort;
    private TextureWaterFX textureWaterFX;
    private TextureLavaFX textureLavaFX;

    /**
     * Makes sure it doesn't keep taking screenshots when both buttons are down.
     */
    boolean isTakingScreenshot;

    /**
     * Does the actual gameplay have focus. If so then mouse and keys will effect the player instead of menus.
     */
    public boolean inGameHasFocus;
    long systemTime;

    /** Join player counter */
    private int joinPlayerCounter;
    private boolean field_71459_aj;
    private NetworkManager field_71453_ak;
    private boolean field_71455_al;
    public final Profiler field_71424_I = new Profiler();

    /** The working dir (OS specific) for minecraft */
    private static File minecraftDir = null;

    /**
     * Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly.
     */
    public volatile boolean running;

    /** String that shows the debug information */
    public String debug;

    /** Approximate time (in ms) of last update to debug string */
    long debugUpdateTime;

    /** holds the current fps */
    int fpsCounter;
    long prevFrameTime;

    /** Profiler currently displayed in the debug screen pie chart */
    private String debugProfilerName;

    private ICommandManager commandManager;
    public boolean enableSP;
    public boolean useSP;
    private String lastWorld;
    public ArrayList<Mod> mods;
    private HashMap<String, Integer> compat; //0 - disabled; 1 - normal; 2 - mcp
    public Class worldClass;
    public int ticksRan;
    public int mouseTicksRan;
    public static boolean oldswing = false;

    public Minecraft(Canvas par1Canvas, MinecraftApplet par2MinecraftApplet, int par3, int par4, boolean par5)
    {
        fullscreen = false;
        hasCrashed = false;
        timer = new Timer(20F);
        field_71427_U = new PlayerUsageSnooper("client", this);
        session = null;
        hideQuitButton = false;
        isGamePaused = false;
        currentScreen = null;
        leftClickCounter = 0;
        guiAchievement = new GuiAchievement(this);
        skipRenderWorld = false;
        objectMouseOver = null;
        sndManager = new SoundManager();
        rightClickDelayTimer = 0;
        textureWaterFX = new TextureWaterFX();
        textureLavaFX = new TextureLavaFX();
        isTakingScreenshot = false;
        inGameHasFocus = false;
        systemTime = func_71386_F();
        joinPlayerCounter = 0;
        running = true;
        debug = "";
        debugUpdateTime = func_71386_F();
        fpsCounter = 0;
        prevFrameTime = -1L;
        debugProfilerName = "root";
        StatList.func_75919_a();
        tempDisplayHeight = par4;
        fullscreen = par5;
        mcApplet = par2MinecraftApplet;
        Packet3Chat.maxChatLength = 32767;
        func_71389_H();
        mcCanvas = par1Canvas;
        displayWidth = par3;
        displayHeight = par4;
        fullscreen = par5;
        theMinecraft = this;
        useSP = true;
        enableSP = useSP;
        mods = new ArrayList<Mod>();
        compat = new HashMap<String, Integer>();
        worldClass = net.minecraft.src.WorldSSP.class;
        ticksRan = 0;
        mouseTicksRan = 0;
        registerCustomPacket();
        checkCompatibility("ModLoader");
    }

    private void func_71389_H()
    {
        ThreadClientSleep threadclientsleep = new ThreadClientSleep(this, "Timer hack thread");
        threadclientsleep.setDaemon(true);
        threadclientsleep.start();
    }

    public void func_71404_a(CrashReport par1CrashReport)
    {
        hasCrashed = true;
        field_71433_S = par1CrashReport;
    }

    public void func_71377_b(CrashReport par1CrashReport)
    {
        hasCrashed = true;
        func_71406_c(par1CrashReport);
    }

    public abstract void func_71406_c(CrashReport crashreport);

    public void setServer(String par1Str, int par2)
    {
        serverName = par1Str;
        serverPort = par2;
    }

    /**
     * Starts the game: initializes the canvas, the title, the settings, etcetera.
     */
    public void startGame() throws LWJGLException
    {
        if (mcCanvas != null)
        {
            Graphics g = mcCanvas.getGraphics();

            if (g != null)
            {
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, displayWidth, displayHeight);
                g.dispose();
            }

            Display.setParent(mcCanvas);
        }
        else if (fullscreen)
        {
            Display.setFullscreen(true);
            displayWidth = Display.getDisplayMode().getWidth();
            displayHeight = Display.getDisplayMode().getHeight();

            if (displayWidth <= 0)
            {
                displayWidth = 1;
            }

            if (displayHeight <= 0)
            {
                displayHeight = 1;
            }
        }
        else
        {
            Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
        }

        Display.setTitle("Minecraft Minecraft 1.3.1");
        System.out.println((new StringBuilder()).append("LWJGL Version: ").append(Sys.getVersion()).toString());

        try
        {
            Display.create((new PixelFormat()).withDepthBits(24));
        }
        catch (LWJGLException lwjglexception)
        {
            lwjglexception.printStackTrace();

            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedexception) { }

            Display.create();
        }

        OpenGlHelper.initializeTextures();
        mcDataDir = getMinecraftDir();
        saveLoader = new AnvilSaveConverter(new File(mcDataDir, "saves"));
        gameSettings = new GameSettings(this, mcDataDir);
        texturePackList = new TexturePackList(mcDataDir, this);
        renderEngine = new RenderEngine(texturePackList, gameSettings);
        loadScreen();
        fontRenderer = new FontRenderer(gameSettings, "/font/default.png", renderEngine, false);
        standardGalacticFontRenderer = new FontRenderer(gameSettings, "/font/alternate.png", renderEngine, false);

        if (gameSettings.language != null)
        {
            StringTranslate.getInstance().setLanguage(gameSettings.language);
            fontRenderer.setUnicodeFlag(StringTranslate.getInstance().isUnicode());
            fontRenderer.setBidiFlag(StringTranslate.isBidirectional(gameSettings.language));
        }

        ColorizerWater.setWaterBiomeColorizer(renderEngine.getTextureContents("/misc/watercolor.png"));
        ColorizerGrass.setGrassBiomeColorizer(renderEngine.getTextureContents("/misc/grasscolor.png"));
        ColorizerFoliage.getFoilageBiomeColorizer(renderEngine.getTextureContents("/misc/foliagecolor.png"));
        entityRenderer = new EntityRenderer(this);
        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        statFileWriter = new StatFileWriter(session, mcDataDir);
        AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
        loadMods();
        loadScreen();
        Mouse.create();
        mouseHelper = new MouseHelper(mcCanvas);
        checkGLError("Pre startup");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glClearDepth(1.0D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        GL11.glCullFace(GL11.GL_BACK);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        checkGLError("Startup");
        sndManager.loadSoundSettings(gameSettings);
        renderEngine.registerTextureFX(textureLavaFX);
        renderEngine.registerTextureFX(textureWaterFX);
        renderEngine.registerTextureFX(new TexturePortalFX());
        renderEngine.registerTextureFX(new TextureCompassFX(this));
        renderEngine.registerTextureFX(new TextureWatchFX(this));
        renderEngine.registerTextureFX(new TextureWaterFlowFX());
        renderEngine.registerTextureFX(new TextureLavaFlowFX());
        renderEngine.registerTextureFX(new TextureFlamesFX(0));
        renderEngine.registerTextureFX(new TextureFlamesFX(1));
        renderGlobal = new RenderGlobal(this, renderEngine);
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        effectRenderer = new EffectRenderer(field_71441_e, renderEngine);

        try
        {
            downloadResourcesThread = new ThreadDownloadResources(mcDataDir, this);
            downloadResourcesThread.start();
        }
        catch (Exception exception) { }

        checkGLError("Post startup");
        ingameGUI = new GuiIngame(this);

        if (serverName != null)
        {
            displayGuiScreen(new GuiConnecting(this, serverName, serverPort));
        }
        else
        {
            displayGuiScreen(new GuiMainMenu());
        }

        loadingScreen = new LoadingScreenRenderer(this);

        if (gameSettings.field_74353_u && !fullscreen)
        {
            toggleFullscreen();
        }
    }

    /**
     * Displays a new screen.
     */
    private void loadScreen() throws LWJGLException
    {
        ScaledResolution scaledresolution = new ScaledResolution(gameSettings, displayWidth, displayHeight);
        GL11.glClear(16640);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.func_78327_c(), scaledresolution.func_78324_d(), 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/title/mojang.png"));
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0xffffff);
        tessellator.addVertexWithUV(0.0D, displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(displayWidth, displayHeight, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(displayWidth, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        tessellator.setColorOpaque_I(0xffffff);
        char c = 256;
        char c1 = 256;
        scaledTessellator((scaledresolution.getScaledWidth() - c) / 2, (scaledresolution.getScaledHeight() - c1) / 2, 0, 0, c, c1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
        Display.swapBuffers();
    }

    /**
     * Loads Tessellator with a scaled resolution
     */
    public void scaledTessellator(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(par1 + 0, par2 + par6, 0.0D, (float)(par3 + 0) * f, (float)(par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + par6, 0.0D, (float)(par3 + par5) * f, (float)(par4 + par6) * f1);
        tessellator.addVertexWithUV(par1 + par5, par2 + 0, 0.0D, (float)(par3 + par5) * f, (float)(par4 + 0) * f1);
        tessellator.addVertexWithUV(par1 + 0, par2 + 0, 0.0D, (float)(par3 + 0) * f, (float)(par4 + 0) * f1);
        tessellator.draw();
    }

    /**
     * gets the working dir (OS specific) for minecraft
     */
    public static File getMinecraftDir()
    {
        if (minecraftDir == null)
        {
            minecraftDir = getAppDir("minecraft");
        }

        return minecraftDir;
    }

    /**
     * gets the working dir (OS specific) for the specific application (which is always minecraft)
     */
    public static File getAppDir(String par0Str)
    {
        String s = System.getProperty("user.home", ".");
        File file;

        switch (MinecraftINNER11.field_79003_a[getOs().ordinal()])
        {
            case 1:
            case 2:
                file = new File(s, (new StringBuilder()).append('.').append(par0Str).append('/').toString());
                break;

            case 3:
                String s1 = System.getenv("APPDATA");

                if (s1 != null)
                {
                    file = new File(s1, (new StringBuilder()).append(".").append(par0Str).append('/').toString());
                }
                else
                {
                    file = new File(s, (new StringBuilder()).append('.').append(par0Str).append('/').toString());
                }

                break;

            case 4:
                file = new File(s, (new StringBuilder()).append("Library/Application Support/").append(par0Str).toString());
                break;

            default:
                file = new File(s, (new StringBuilder()).append(par0Str).append('/').toString());
                break;
        }

        if (!file.exists() && !file.mkdirs())
        {
            throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(file).toString());
        }
        else
        {
            return file;
        }
    }

    public static EnumOS getOs()
    {
        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("win"))
        {
            return EnumOS.WINDOWS;
        }

        if (s.contains("mac"))
        {
            return EnumOS.MACOS;
        }

        if (s.contains("solaris"))
        {
            return EnumOS.SOLARIS;
        }

        if (s.contains("sunos"))
        {
            return EnumOS.SOLARIS;
        }

        if (s.contains("linux"))
        {
            return EnumOS.LINUX;
        }

        if (s.contains("unix"))
        {
            return EnumOS.LINUX;
        }
        else
        {
            return EnumOS.UNKNOWN;
        }
    }

    /**
     * Returns the save loader that is currently being used
     */
    public ISaveFormat getSaveLoader()
    {
        return saveLoader;
    }

    /**
     * Sets the argument GuiScreen as the main (topmost visible) screen.
     */
    public void displayGuiScreen(GuiScreen par1GuiScreen)
    {
        if (currentScreen instanceof GuiErrorScreen)
        {
            return;
        }

        if (currentScreen != null)
        {
            currentScreen.onGuiClosed();
        }

        statFileWriter.syncStats();

        if (par1GuiScreen == null && field_71441_e == null)
        {
            par1GuiScreen = new GuiMainMenu();
        }
        else if (par1GuiScreen == null && field_71439_g.getHealth() <= 0)
        {
            par1GuiScreen = new GuiGameOver();
        }

        if (par1GuiScreen instanceof GuiMainMenu)
        {
            gameSettings.showDebugInfo = false;
            ingameGUI.func_73827_b().func_73761_a();
        }

        currentScreen = par1GuiScreen;

        if (par1GuiScreen != null)
        {
            setIngameNotInFocus();
            ScaledResolution scaledresolution = new ScaledResolution(gameSettings, displayWidth, displayHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            par1GuiScreen.setWorldAndResolution(this, i, j);
            skipRenderWorld = false;
        }
        else
        {
            setIngameFocus();
        }
    }

    /**
     * Checks for an OpenGL error. If there is one, prints the error ID and error string.
     */
    private void checkGLError(String par1Str)
    {
        int i = GL11.glGetError();

        if (i != 0)
        {
            String s = GLU.gluErrorString(i);
            System.out.println("########## GL ERROR ##########");
            System.out.println((new StringBuilder()).append("@ ").append(par1Str).toString());
            System.out.println((new StringBuilder()).append(i).append(": ").append(s).toString());
        }
    }

    /**
     * Shuts down the minecraft applet by stopping the resource downloads, and clearing up GL stuff; called when the
     * application (or web page) is exited.
     */
    public void shutdownMinecraftApplet()
    {
        try
        {
            statFileWriter.syncStats();

            try
            {
                if (downloadResourcesThread != null)
                {
                    downloadResourcesThread.closeMinecraft();
                }
            }
            catch (Exception exception) { }

            System.out.println("Stopping!");

            try
            {
                func_71403_a(null);
            }
            catch (Throwable throwable) { }

            try
            {
                GLAllocation.deleteTexturesAndDisplayLists();
            }
            catch (Throwable throwable1) { }

            sndManager.closeMinecraft();
            Mouse.destroy();
            Keyboard.destroy();
        }
        finally
        {
            Display.destroy();

            if (!hasCrashed)
            {
                System.exit(0);
            }
        }

        System.gc();
    }

    public void run()
    {
        running = true;

        try
        {
            startGame();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
            func_71377_b(func_71396_d(new CrashReport("Failed to start game", exception)));
            return;
        }

        try
        {
            while (running)
            {
                if (hasCrashed && field_71433_S != null)
                {
                    func_71377_b(field_71433_S);
                    return;
                }

                if (field_71468_ad)
                {
                    field_71468_ad = false;
                    renderEngine.refreshTextures();
                }

                try
                {
                    runGameLoop();
                }
                catch (OutOfMemoryError outofmemoryerror)
                {
                    freeMemory();
                    displayGuiScreen(new GuiMemoryErrorScreen());
                    System.gc();
                }
            }
        }
        catch (MinecraftError minecrafterror) { }
        catch (ReportedException reportedexception)
        {
            func_71396_d(reportedexception.func_71575_a());
            freeMemory();
            reportedexception.printStackTrace();
            func_71377_b(reportedexception.func_71575_a());
        }
        catch (Throwable throwable)
        {
            CrashReport crashreport = func_71396_d(new CrashReport("Unexpected error", throwable));
            freeMemory();
            throwable.printStackTrace();
            func_71377_b(crashreport);
        }
        finally
        {
            shutdownMinecraftApplet();
        }
    }

    /**
     * Called repeatedly from run()
     */
    private void runGameLoop()
    {
        if (mcApplet != null && !mcApplet.isActive())
        {
            running = false;
            return;
        }

        AxisAlignedBB.func_72332_a().func_72298_a();
        Vec3.func_72437_a().func_72343_a();
        field_71424_I.startSection("root");

        if (mcCanvas == null && Display.isCloseRequested())
        {
            shutdown();
        }

        if (isGamePaused && field_71441_e != null)
        {
            float f = timer.renderPartialTicks;
            timer.updateTimer();
            timer.renderPartialTicks = f;
        }
        else
        {
            timer.updateTimer();
        }

        long l = System.nanoTime();
        field_71424_I.startSection("tick");

        for (int i = 0; i < timer.elapsedTicks; i++)
        {
            ticksRan++;
            runTick();
        }

        field_71424_I.endStartSection("preRenderErrors");
        long l1 = System.nanoTime() - l;
        checkGLError("Pre render");
        RenderBlocks.fancyGrass = gameSettings.fancyGraphics;
        field_71424_I.endStartSection("sound");
        sndManager.setListener(field_71439_g, timer.renderPartialTicks);
        field_71424_I.endStartSection("updatelights");

        if (field_71441_e != null)
        {
            field_71441_e.updatingLighting();
        }

        field_71424_I.endSection();
        field_71424_I.startSection("render");
        field_71424_I.startSection("display");
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (!Keyboard.isKeyDown(65))
        {
            Display.update();
        }

        if (field_71439_g != null && field_71439_g.isEntityInsideOpaqueBlock())
        {
            gameSettings.thirdPersonView = 0;
        }

        field_71424_I.endSection();

        if (!skipRenderWorld)
        {
            field_71424_I.endStartSection("gameRenderer");
            entityRenderer.updateCameraAndRender(timer.renderPartialTicks);
            field_71424_I.endSection();
        }

        GL11.glFlush();
        field_71424_I.endSection();

        if (!Display.isActive() && fullscreen)
        {
            toggleFullscreen();
        }

        if (gameSettings.showDebugInfo && gameSettings.field_74329_Q)
        {
            if (!field_71424_I.profilingEnabled)
            {
                field_71424_I.clearProfiling();
            }

            field_71424_I.profilingEnabled = true;
            displayDebugInfo(l1);
        }
        else
        {
            field_71424_I.profilingEnabled = false;
            prevFrameTime = System.nanoTime();
        }

        guiAchievement.updateAchievementWindow();
        field_71424_I.startSection("root");
        Thread.yield();

        if (Keyboard.isKeyDown(65))
        {
            Display.update();
        }

        screenshotListener();

        if (mcCanvas != null && !fullscreen && (mcCanvas.getWidth() != displayWidth || mcCanvas.getHeight() != displayHeight))
        {
            displayWidth = mcCanvas.getWidth();
            displayHeight = mcCanvas.getHeight();

            if (displayWidth <= 0)
            {
                displayWidth = 1;
            }

            if (displayHeight <= 0)
            {
                displayHeight = 1;
            }

            resize(displayWidth, displayHeight);
        }

        checkGLError("Post render");
        fpsCounter++;
        boolean flag = isGamePaused;
        if (enableSP){
            isGamePaused = !isMultiplayerWorld() && currentScreen != null && currentScreen.doesGuiPauseGame();

            if (func_71387_A() && field_71439_g != null && getSendQueue() != null && isGamePaused != flag)
            {
                ((MemoryConnection)getSendQueue().func_72548_f()).func_74437_a(isGamePaused);
            }
        }else{
            isGamePaused = func_71356_B() && currentScreen != null && currentScreen.doesGuiPauseGame() && !field_71437_Z.func_71344_c();

            if (func_71387_A() && field_71439_g != null && ((EntityClientPlayerMP)field_71439_g).sendQueue != null && isGamePaused != flag)
            {
                ((MemoryConnection)((EntityClientPlayerMP)field_71439_g).sendQueue.func_72548_f()).func_74437_a(isGamePaused);
            }
        }

        do
        {
            if (func_71386_F() < debugUpdateTime + 1000L)
            {
                break;
            }

            field_71470_ab = fpsCounter;
            debug = (new StringBuilder()).append(field_71470_ab).append(" fps, ").append(WorldRenderer.chunksUpdated).append(" chunk updates").toString();
            WorldRenderer.chunksUpdated = 0;
            debugUpdateTime += 1000L;
            fpsCounter = 0;
            field_71427_U.func_76471_b();

            if (!field_71427_U.func_76468_d())
            {
                field_71427_U.func_76463_a();
            }
        }
        while (true);

        field_71424_I.endSection();
        field_71424_I.startSection("mods");
        for (Mod mod : mods){
            if (mod.usesTick && field_71441_e != null){
                mod.onTick();
            }
            if (mod.usesGUITick){
                mod.onGUITick(currentScreen);
            }
        }
        field_71424_I.endSection();

        if (gameSettings.limitFramerate > 0)
        {
            EntityRenderer _tmp = entityRenderer;
            Display.sync(EntityRenderer.func_78465_a(gameSettings.limitFramerate));
        }
    }

    public void freeMemory()
    {
        try
        {
            field_71444_a = new byte[0];
            renderGlobal.func_72728_f();
        }
        catch (Throwable throwable) { }

        try
        {
            System.gc();
            AxisAlignedBB.func_72332_a().func_72300_b();
            Vec3.func_72437_a().func_72344_b();
        }
        catch (Throwable throwable1) { }

        try
        {
            System.gc();
            func_71403_a(null);
        }
        catch (Throwable throwable2) { }

        System.gc();
    }

    /**
     * checks if keys are down
     */
    private void screenshotListener()
    {
        if (Keyboard.isKeyDown(60))
        {
            if (!isTakingScreenshot)
            {
                isTakingScreenshot = true;
                ingameGUI.func_73827_b().func_73765_a(ScreenShotHelper.saveScreenshot(minecraftDir, displayWidth, displayHeight));
            }
        }
        else
        {
            isTakingScreenshot = false;
        }
    }

    /**
     * Update debugProfilerName in response to number keys in debug screen
     */
    private void updateDebugProfilerName(int par1)
    {
        List var2 = this.field_71424_I.getProfilingData(this.debugProfilerName);

        if (var2 != null && !var2.isEmpty())
        {
            ProfilerResult var3 = (ProfilerResult)var2.remove(0);

            if (par1 == 0)
            {
                if (var3.field_76331_c.length() > 0)
                {
                    int var4 = this.debugProfilerName.lastIndexOf(".");

                    if (var4 >= 0)
                    {
                        this.debugProfilerName = this.debugProfilerName.substring(0, var4);
                    }
                }
            }
            else
            {
                --par1;

                if (par1 < var2.size() && !((ProfilerResult)var2.get(par1)).field_76331_c.equals("unspecified"))
                {
                    if (this.debugProfilerName.length() > 0)
                    {
                        this.debugProfilerName = this.debugProfilerName + ".";
                    }

                    this.debugProfilerName = this.debugProfilerName + ((ProfilerResult)var2.get(par1)).field_76331_c;
                }
            }
        }
    }

    private void displayDebugInfo(long par1)
    {
        if (!field_71424_I.profilingEnabled)
        {
            return;
        }

        java.util.List list = field_71424_I.getProfilingData(debugProfilerName);
        ProfilerResult profilerresult = (ProfilerResult)list.remove(0);
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, displayWidth, displayHeight, 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
        GL11.glLineWidth(1.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        Tessellator tessellator = Tessellator.instance;
        int i = 160;
        int j = displayWidth - i - 10;
        int k = displayHeight - i * 2;
        GL11.glEnable(GL11.GL_BLEND);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_I(0, 200);
        tessellator.addVertex((float)j - (float)i * 1.1F, (float)k - (float)i * 0.6F - 16F, 0.0D);
        tessellator.addVertex((float)j - (float)i * 1.1F, k + i * 2, 0.0D);
        tessellator.addVertex((float)j + (float)i * 1.1F, k + i * 2, 0.0D);
        tessellator.addVertex((float)j + (float)i * 1.1F, (float)k - (float)i * 0.6F - 16F, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        double d = 0.0D;

        for (int l = 0; l < list.size(); l++)
        {
            ProfilerResult profilerresult1 = (ProfilerResult)list.get(l);
            int j1 = MathHelper.floor_double(profilerresult1.field_76332_a / 4D) + 1;
            tessellator.startDrawing(6);
            tessellator.setColorOpaque_I(profilerresult1.func_76329_a());
            tessellator.addVertex(j, k, 0.0D);

            for (int l1 = j1; l1 >= 0; l1--)
            {
                float f = (float)(((d + (profilerresult1.field_76332_a * (double)l1) / (double)j1) * Math.PI * 2D) / 100D);
                float f2 = MathHelper.sin(f) * (float)i;
                float f4 = MathHelper.cos(f) * (float)i * 0.5F;
                tessellator.addVertex((float)j + f2, (float)k - f4, 0.0D);
            }

            tessellator.draw();
            tessellator.startDrawing(5);
            tessellator.setColorOpaque_I((profilerresult1.func_76329_a() & 0xfefefe) >> 1);

            for (int i2 = j1; i2 >= 0; i2--)
            {
                float f1 = (float)(((d + (profilerresult1.field_76332_a * (double)i2) / (double)j1) * Math.PI * 2D) / 100D);
                float f3 = MathHelper.sin(f1) * (float)i;
                float f5 = MathHelper.cos(f1) * (float)i * 0.5F;
                tessellator.addVertex((float)j + f3, (float)k - f5, 0.0D);
                tessellator.addVertex((float)j + f3, ((float)k - f5) + 10F, 0.0D);
            }

            tessellator.draw();
            d += profilerresult1.field_76332_a;
        }

        DecimalFormat decimalformat = new DecimalFormat("##0.00");
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        String s = "";

        if (!profilerresult.field_76331_c.equals("unspecified"))
        {
            s = (new StringBuilder()).append(s).append("[0] ").toString();
        }

        if (profilerresult.field_76331_c.length() == 0)
        {
            s = (new StringBuilder()).append(s).append("ROOT ").toString();
        }
        else
        {
            s = (new StringBuilder()).append(s).append(profilerresult.field_76331_c).append(" ").toString();
        }

        int k1 = 0xffffff;
        fontRenderer.drawStringWithShadow(s, j - i, k - i / 2 - 16, k1);
        fontRenderer.drawStringWithShadow(s = (new StringBuilder()).append(decimalformat.format(profilerresult.field_76330_b)).append("%").toString(), (j + i) - fontRenderer.getStringWidth(s), k - i / 2 - 16, k1);

        for (int i1 = 0; i1 < list.size(); i1++)
        {
            ProfilerResult profilerresult2 = (ProfilerResult)list.get(i1);
            String s1 = "";

            if (profilerresult2.field_76331_c.equals("unspecified"))
            {
                s1 = (new StringBuilder()).append(s1).append("[?] ").toString();
            }
            else
            {
                s1 = (new StringBuilder()).append(s1).append("[").append(i1 + 1).append("] ").toString();
            }

            s1 = (new StringBuilder()).append(s1).append(profilerresult2.field_76331_c).toString();
            fontRenderer.drawStringWithShadow(s1, j - i, k + i / 2 + i1 * 8 + 20, profilerresult2.func_76329_a());
            fontRenderer.drawStringWithShadow(s1 = (new StringBuilder()).append(decimalformat.format(profilerresult2.field_76332_a)).append("%").toString(), (j + i) - 50 - fontRenderer.getStringWidth(s1), k + i / 2 + i1 * 8 + 20, profilerresult2.func_76329_a());
            fontRenderer.drawStringWithShadow(s1 = (new StringBuilder()).append(decimalformat.format(profilerresult2.field_76330_b)).append("%").toString(), (j + i) - fontRenderer.getStringWidth(s1), k + i / 2 + i1 * 8 + 20, profilerresult2.func_76329_a());
        }
    }

    /**
     * Called when the window is closing. Sets 'running' to false which allows the game loop to exit cleanly.
     */
    public void shutdown()
    {
        running = false;
    }

    /**
     * Will set the focus to ingame if the Minecraft window is the active with focus. Also clears any GUI screen
     * currently displayed
     */
    public void setIngameFocus()
    {
        if (!Display.isActive())
        {
            return;
        }

        if (inGameHasFocus)
        {
            return;
        }
        else
        {
            inGameHasFocus = true;
            mouseHelper.grabMouseCursor();
            displayGuiScreen(null);
            leftClickCounter = 10000;
            mouseTicksRan = ticksRan + 10000;
            return;
        }
    }

    /**
     * Resets the player keystate, disables the ingame focus, and ungrabs the mouse cursor.
     */
    public void setIngameNotInFocus()
    {
        if (!inGameHasFocus)
        {
            return;
        }
        else
        {
            KeyBinding.unPressAllKeys();
            inGameHasFocus = false;
            mouseHelper.ungrabMouseCursor();
            return;
        }
    }

    /**
     * Displays the ingame menu
     */
    public void displayInGameMenu()
    {
        if (currentScreen != null)
        {
            return;
        }
        else
        {
            if (enableSP){
                displayGuiScreen(new GuiIngameMenuSP());
            }else{
                displayGuiScreen(new GuiIngameMenu());
            }
            return;
        }
    }

    private void sendClickBlockToController(int par1, boolean par2)
    {
        if (!par2)
        {
            leftClickCounter = 0;
        }

        if (par1 == 0 && leftClickCounter > 0)
        {
            return;
        }

        if (par2 && objectMouseOver != null && objectMouseOver.typeOfHit == EnumMovingObjectType.TILE && par1 == 0)
        {
            int i = objectMouseOver.blockX;
            int j = objectMouseOver.blockY;
            int k = objectMouseOver.blockZ;
            field_71442_b.onPlayerDamageBlock(i, j, k, objectMouseOver.sideHit);

            if (field_71439_g.canPlayerEdit(i, j, k))
            {
                effectRenderer.addBlockHitEffects(i, j, k, objectMouseOver.sideHit);
                field_71439_g.swingItem();
            }
        }
        else
        {
            field_71442_b.resetBlockRemoving();
        }
    }

    /**
     * Called whenever the mouse is clicked. Button clicked is 0 for left clicking and 1 for right clicking. Args:
     * buttonClicked
     */
    private void clickMouse(int par1)
    {
        mouseTicksRan = ticksRan;
        if (par1 == 0 && leftClickCounter > 0)
        {
            return;
        }

        if (par1 == 0)
        {
            field_71439_g.swingItem();
        }

        if (par1 == 1)
        {
            rightClickDelayTimer = 4;
        }

        boolean flag = true;
        ItemStack itemstack = field_71439_g.inventory.getCurrentItem();

        if (objectMouseOver == null)
        {
            if (par1 == 0 && field_71442_b.isNotCreative())
            {
                leftClickCounter = 10;
            }
        }
        else if (objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY)
        {
            if (par1 == 0)
            {
                field_71442_b.attackEntity(field_71439_g, objectMouseOver.entityHit);
            }

            if (par1 == 1 && field_71442_b.func_78768_b(field_71439_g, objectMouseOver.entityHit))
            {
                flag = false;
            }
        }
        else if (objectMouseOver.typeOfHit == EnumMovingObjectType.TILE)
        {
            int i = objectMouseOver.blockX;
            int j = objectMouseOver.blockY;
            int k = objectMouseOver.blockZ;
            int l = objectMouseOver.sideHit;

            if (par1 == 0)
            {
                field_71442_b.clickBlock(i, j, k, objectMouseOver.sideHit);
            }
            else
            {
                int i1 = itemstack == null ? 0 : itemstack.stackSize;

                if (field_71442_b.func_78760_a(field_71439_g, field_71441_e, itemstack, i, j, k, l, objectMouseOver.hitVec))
                {
                    flag = false;
                    field_71439_g.swingItem();
                }

                if (itemstack == null)
                {
                    return;
                }

                if (itemstack.stackSize == 0)
                {
                    field_71439_g.inventory.mainInventory[field_71439_g.inventory.currentItem] = null;
                }
                else if (itemstack.stackSize != i1 || field_71442_b.isInCreativeMode())
                {
                    entityRenderer.itemRenderer.func_78444_b();
                }
            }
        }

        if (flag && par1 == 1)
        {
            ItemStack itemstack1 = field_71439_g.inventory.getCurrentItem();

            if (itemstack1 != null && field_71442_b.sendUseItem(field_71439_g, field_71441_e, itemstack1))
            {
                entityRenderer.itemRenderer.func_78445_c();
            }
        }
    }

    /**
     * Toggles fullscreen mode.
     */
    public void toggleFullscreen()
    {
        try
        {
            fullscreen = !fullscreen;

            if (fullscreen)
            {
                Display.setDisplayMode(Display.getDesktopDisplayMode());
                displayWidth = Display.getDisplayMode().getWidth();
                displayHeight = Display.getDisplayMode().getHeight();

                if (displayWidth <= 0)
                {
                    displayWidth = 1;
                }

                if (displayHeight <= 0)
                {
                    displayHeight = 1;
                }
            }
            else
            {
                if (mcCanvas != null)
                {
                    displayWidth = mcCanvas.getWidth();
                    displayHeight = mcCanvas.getHeight();
                }
                else
                {
                    displayWidth = tempDisplayWidth;
                    displayHeight = tempDisplayHeight;
                }

                if (displayWidth <= 0)
                {
                    displayWidth = 1;
                }

                if (displayHeight <= 0)
                {
                    displayHeight = 1;
                }
            }

            if (currentScreen != null)
            {
                resize(displayWidth, displayHeight);
            }

            Display.setFullscreen(fullscreen);
            Display.setVSyncEnabled(gameSettings.field_74352_v);
            Display.update();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Called to resize the current screen.
     */
    private void resize(int par1, int par2)
    {
        displayWidth = par1 > 0 ? par1 : 1;
        displayHeight = par2 > 0 ? par2 : 1;

        if (currentScreen != null)
        {
            ScaledResolution scaledresolution = new ScaledResolution(gameSettings, par1, par2);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            currentScreen.setWorldAndResolution(this, i, j);
        }
    }

    /**
     * Runs the current tick.
     */
    public void runTick()
    {
        if (rightClickDelayTimer > 0)
        {
            rightClickDelayTimer--;
        }

        field_71424_I.startSection("stats");
        statFileWriter.func_77449_e();
        field_71424_I.endStartSection("gui");

        if (!isGamePaused)
        {
            ingameGUI.updateTick();
        }

        field_71424_I.endStartSection("pick");
        entityRenderer.getMouseOver(1.0F);
        field_71424_I.endStartSection("gameMode");

        if (!isGamePaused && field_71441_e != null)
        {
            field_71442_b.updateController();
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, renderEngine.getTexture("/terrain.png"));
        field_71424_I.endStartSection("textures");

        if (!isGamePaused)
        {
            renderEngine.updateDynamicTextures();
        }

        if (currentScreen == null && field_71439_g != null)
        {
            if (field_71439_g.getHealth() <= 0)
            {
                displayGuiScreen(null);
            }
            else if (field_71439_g.isPlayerSleeping() && field_71441_e != null && field_71441_e.isRemote)
            {
                displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (currentScreen != null && (currentScreen instanceof GuiSleepMP) && !field_71439_g.isPlayerSleeping())
        {
            displayGuiScreen(null);
        }

        if (currentScreen != null)
        {
            leftClickCounter = 10000;
            mouseTicksRan = ticksRan + 10000;
        }

        if (currentScreen != null)
        {
            currentScreen.handleInput();

            if (currentScreen != null)
            {
                currentScreen.guiParticles.update();
                currentScreen.updateScreen();
            }
        }

        if (currentScreen == null || currentScreen.allowUserInput)
        {
            field_71424_I.endStartSection("mouse");

            do
            {
                if (!Mouse.next())
                {
                    break;
                }

                KeyBinding.setKeyBindState(Mouse.getEventButton() - 100, Mouse.getEventButtonState());

                if (Mouse.getEventButtonState())
                {
                    KeyBinding.onTick(Mouse.getEventButton() - 100);
                }

                long l = func_71386_F() - systemTime;

                if (l <= 200L)
                {
                    int k = Mouse.getEventDWheel();

                    if (k != 0)
                    {
                        field_71439_g.inventory.changeCurrentItem(k);

                        if (gameSettings.noclip)
                        {
                            if (k > 0)
                            {
                                k = 1;
                            }

                            if (k < 0)
                            {
                                k = -1;
                            }

                            gameSettings.noclipRate += (float)k * 0.25F;
                        }
                    }

                    if (currentScreen == null)
                    {
                        if (!inGameHasFocus && Mouse.getEventButtonState())
                        {
                            setIngameFocus();
                        }
                    }
                    else if (currentScreen != null)
                    {
                        currentScreen.handleMouseInput();
                    }
                }
            }
            while (true);

            if (leftClickCounter > 0)
            {
                leftClickCounter--;
            }

            field_71424_I.endStartSection("keyboard");

            do
            {
                if (!Keyboard.next())
                {
                    break;
                }

                KeyBinding.setKeyBindState(Keyboard.getEventKey(), Keyboard.getEventKeyState());

                if (Keyboard.getEventKeyState())
                {
                    KeyBinding.onTick(Keyboard.getEventKey());
                }

                if (Keyboard.getEventKeyState())
                {
                    if (Keyboard.getEventKey() == 87)
                    {
                        toggleFullscreen();
                    }
                    else
                    {
                        if (currentScreen != null)
                        {
                            currentScreen.handleKeyboardInput();
                        }
                        else
                        {
                            if (Keyboard.getEventKey() == 1)
                            {
                                displayInGameMenu();
                            }

                            if (Keyboard.getEventKey() == 31 && Keyboard.isKeyDown(61))
                            {
                                forceReload();
                            }

                            if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61))
                            {
                                renderEngine.refreshTextures();
                            }

                            if (Keyboard.getEventKey() == 33 && Keyboard.isKeyDown(61))
                            {
                                boolean flag = Keyboard.isKeyDown(42) | Keyboard.isKeyDown(54);
                                gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, flag ? -1 : 1);
                            }

                            if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61))
                            {
                                renderGlobal.loadRenderers();
                            }

                            if (Keyboard.getEventKey() == 59)
                            {
                                gameSettings.hideGUI = !gameSettings.hideGUI;
                            }

                            if (Keyboard.getEventKey() == 61)
                            {
                                gameSettings.showDebugInfo = !gameSettings.showDebugInfo;
                                gameSettings.field_74329_Q = !GuiScreen.isShiftKeyDown();
                            }

                            if (Keyboard.getEventKey() == 63)
                            {
                                gameSettings.thirdPersonView++;

                                if (gameSettings.thirdPersonView > 2)
                                {
                                    gameSettings.thirdPersonView = 0;
                                }
                            }

                            if (Keyboard.getEventKey() == 66)
                            {
                                gameSettings.smoothCamera = !gameSettings.smoothCamera;
                            }
                        }

                        for (int i = 0; i < 9; i++)
                        {
                            if (Keyboard.getEventKey() == 2 + i)
                            {
                                field_71439_g.inventory.currentItem = i;
                            }
                        }

                        if (gameSettings.showDebugInfo && gameSettings.field_74329_Q)
                        {
                            if (Keyboard.getEventKey() == 11)
                            {
                                updateDebugProfilerName(0);
                            }

                            int j = 0;

                            while (j < 9)
                            {
                                if (Keyboard.getEventKey() == 2 + j)
                                {
                                    updateDebugProfilerName(j + 1);
                                }

                                j++;
                            }
                        }
                    }
                }
            }
            while (true);

            boolean flag1 = gameSettings.field_74343_n != 2;

            for (; gameSettings.keyBindInventory.isPressed(); displayGuiScreen(new GuiInventory(field_71439_g))) { }

            for (; gameSettings.keyBindDrop.isPressed(); field_71439_g.dropOneItem()) { }

            for (; gameSettings.keyBindChat.isPressed() && flag1; displayGuiScreen(new GuiChat())) { }

            if (currentScreen == null && gameSettings.field_74323_J.isPressed() && flag1)
            {
                displayGuiScreen(new GuiChat("/"));
            }

            if (field_71439_g.isUsingItem())
            {
                if (!gameSettings.keyBindUseItem.pressed)
                {
                    field_71442_b.onStoppedUsingItem(field_71439_g);
                }

                while (gameSettings.keyBindAttack.isPressed()) ;

                while (gameSettings.keyBindUseItem.isPressed()) ;

                while (gameSettings.keyBindPickBlock.isPressed()) ;
            }
            else
            {
                for (; gameSettings.keyBindAttack.isPressed(); clickMouse(0)) { }

                for (; gameSettings.keyBindUseItem.isPressed(); clickMouse(1)) { }

                for (; gameSettings.keyBindPickBlock.isPressed(); clickMiddleMouseButton()) { }
            }

            if (gameSettings.keyBindAttack.pressed && oldswing && (float)(ticksRan - mouseTicksRan) >= 10F){
                field_71439_g.swingItem();
                mouseTicksRan = ticksRan;
            }
            if (gameSettings.keyBindUseItem.pressed && rightClickDelayTimer == 0 && !field_71439_g.isUsingItem())
            {
                clickMouse(1);
            }

            sendClickBlockToController(0, currentScreen == null && gameSettings.keyBindAttack.pressed && inGameHasFocus);
        }

        if (field_71441_e != null)
        {
            if (field_71439_g != null)
            {
                joinPlayerCounter++;

                if (joinPlayerCounter == 30)
                {
                    joinPlayerCounter = 0;
                    field_71441_e.joinEntityInSurroundings(field_71439_g);
                }
            }

            if (enableSP){
                if (field_71441_e.getWorldInfo().isHardcoreModeEnabled())
                {
                    field_71441_e.difficultySetting = 3;
                }
                else
                {
                    field_71441_e.difficultySetting = gameSettings.difficulty;
                }

                if (field_71441_e.isRemote)
                {
                    field_71441_e.difficultySetting = 1;
                }
            }

            field_71424_I.endStartSection("gameRenderer");

            if (!isGamePaused)
            {
                entityRenderer.updateRenderer();
            }

            field_71424_I.endStartSection("levelRenderer");

            if (!isGamePaused)
            {
                renderGlobal.updateClouds();
            }

            field_71424_I.endStartSection("level");

            if (!isGamePaused)
            {
                if (field_71441_e.lightningFlash > 0)
                {
                    field_71441_e.lightningFlash--;
                }

                field_71441_e.updateEntities();
            }

            if (!isGamePaused)
            {
                field_71441_e.setAllowedSpawnTypes(field_71441_e.difficultySetting > 0, true);
                field_71441_e.tick();
            }

            field_71424_I.endStartSection("animateTick");

            if (!isGamePaused && field_71441_e != null)
            {
                field_71441_e.func_73029_E(MathHelper.floor_double(field_71439_g.posX), MathHelper.floor_double(field_71439_g.posY), MathHelper.floor_double(field_71439_g.posZ));
            }

            field_71424_I.endStartSection("particles");

            if (!isGamePaused)
            {
                effectRenderer.updateEffects();
            }
        }
        else if (field_71453_ak != null)
        {
            field_71424_I.endStartSection("pendingConnection");
            field_71453_ak.processReadPackets();
        }

        field_71424_I.endSection();
        systemTime = func_71386_F();
    }

    /**
     * Forces a reload of the sound manager and all the resources. Called in game by holding 'F3' and pressing 'S'.
     */
    private void forceReload()
    {
        System.out.println("FORCING RELOAD!");
        sndManager = new SoundManager();
        sndManager.loadSoundSettings(gameSettings);
        downloadResourcesThread.reloadResources();
    }

    public void func_71371_a(String par1Str, String par2Str, WorldSettings par3WorldSettings)
    {
        func_71403_a(null);
        System.gc();
        ISaveHandler isavehandler = saveLoader.getSaveLoader(par1Str, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && par3WorldSettings != null)
        {
            statFileWriter.readStat(StatList.createWorldStat, 1);
            worldinfo = new WorldInfo(par3WorldSettings, par1Str);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (par3WorldSettings == null)
        {
            par3WorldSettings = new WorldSettings(worldinfo);
        }

        statFileWriter.readStat(StatList.startGameStat, 1);
        field_71437_Z = new IntegratedServer(this, par1Str, par2Str, par3WorldSettings);
        field_71437_Z.func_71256_s();
        field_71455_al = true;
        loadingScreen.displaySavingString(StatCollector.translateToLocal("menu.loadingLevel"));

        while (!field_71437_Z.func_71200_ad())
        {
            String s = field_71437_Z.func_71195_b_();

            if (s != null)
            {
                loadingScreen.displayLoadingString(StatCollector.translateToLocal(s));
            }
            else
            {
                loadingScreen.displayLoadingString("");
            }

            try
            {
                Thread.sleep(200L);
            }
            catch (InterruptedException interruptedexception) { }
        }

        displayGuiScreen(null);

        try
        {
            NetClientHandler netclienthandler = new NetClientHandler(this, field_71437_Z);
            field_71453_ak = netclienthandler.func_72548_f();
        }
        catch (IOException ioexception)
        {
            func_71377_b(func_71396_d(new CrashReport("Connecting to integrated server", ioexception)));
        }
        lastWorld = par1Str;
    }

    public void func_71403_a(WorldClient par1WorldClient)
    {
        func_71353_a(par1WorldClient, "");
    }

    public void func_71353_a(WorldClient par1WorldClient, String par2Str)
    {
        statFileWriter.syncStats();

        if (par1WorldClient == null)
        {
            NetClientHandler netclienthandler = getSendQueue();
            if (netclienthandler != null)
            {
                netclienthandler.func_72547_c();
            }

            if (field_71453_ak != null)
            {
                field_71453_ak.func_74431_f();
            }

            if (field_71437_Z != null)
            {
                field_71437_Z.func_71263_m();
            }

            field_71437_Z = null;
        }

        renderViewEntity = null;
        field_71453_ak = null;

        if (loadingScreen != null)
        {
            loadingScreen.printText(par2Str);
            loadingScreen.displayLoadingString("");
        }

        if (par1WorldClient == null && field_71441_e != null)
        {
            if (texturePackList.func_77295_a())
            {
                texturePackList.func_77304_b();
            }

            func_71351_a(null);
            field_71455_al = false;
        }

        sndManager.playStreaming(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        field_71441_e = par1WorldClient;

        if (par1WorldClient != null)
        {
            if (renderGlobal != null)
            {
                renderGlobal.func_72732_a(par1WorldClient);
            }

            if (effectRenderer != null)
            {
                effectRenderer.clearEffects(par1WorldClient);
            }

            if (field_71439_g == null)
            {
                field_71439_g = field_71442_b.func_78754_a(par1WorldClient);
                field_71442_b.flipPlayer(field_71439_g);
            }

            field_71439_g.preparePlayerToSpawn();
            par1WorldClient.spawnEntityInWorld(field_71439_g);
            field_71439_g.movementInput = new MovementInputFromOptions(gameSettings);
            field_71442_b.func_78748_a(field_71439_g);
            renderViewEntity = field_71439_g;
        }
        else
        {
            saveLoader.flushCache();
            field_71439_g = null;
        }

        System.gc();
        systemTime = 0L;
    }

    /**
     * Installs a resource. Currently only sounds are download so this method just adds them to the SoundManager.
     */
    public void installResource(String par1Str, File par2File)
    {
        int i = par1Str.indexOf("/");
        String s = par1Str.substring(0, i);
        par1Str = par1Str.substring(i + 1);

        if (s.equalsIgnoreCase("sound") || s.equalsIgnoreCase("newsound"))
        {
            sndManager.addSound(par1Str, par2File);
        }
        else if (s.equalsIgnoreCase("streaming"))
        {
            sndManager.addStreaming(par1Str, par2File);
        }
        else if (s.equalsIgnoreCase("music") || s.equalsIgnoreCase("newmusic"))
        {
            sndManager.addMusic(par1Str, par2File);
        }
    }

    /**
     * A String of renderGlobal.getDebugInfoRenders
     */
    public String debugInfoRenders()
    {
        return renderGlobal.getDebugInfoRenders();
    }

    /**
     * Gets the information in the F3 menu about how many entities are infront/around you
     */
    public String getEntityDebug()
    {
        return renderGlobal.getDebugInfoEntities();
    }

    /**
     * Gets the name of the world's current chunk provider
     */
    public String getWorldProviderName()
    {
        return field_71441_e.getProviderName();
    }

    /**
     * A String of how many entities are in the world
     */
    public String debugInfoEntities()
    {
        return (new StringBuilder()).append("P: ").append(effectRenderer.getStatistics()).append(". T: ").append(field_71441_e.getDebugLoadedEntities()).toString();
    }

    public void func_71354_a(int par1)
    {
        field_71441_e.setSpawnLocation();
        if (!enableSP){
            field_71441_e.func_73022_a();
        }
        int i = 0;

        if (field_71439_g != null)
        {
            i = field_71439_g.entityId;
            field_71441_e.setEntityDead(field_71439_g);
        }

        renderViewEntity = null;
        field_71439_g = field_71442_b.func_78754_a(field_71441_e);
        field_71439_g.dimension = par1;
        renderViewEntity = field_71439_g;
        field_71439_g.preparePlayerToSpawn();
        field_71441_e.spawnEntityInWorld(field_71439_g);
        field_71442_b.flipPlayer(field_71439_g);
        field_71439_g.movementInput = new MovementInputFromOptions(gameSettings);
        field_71439_g.entityId = i;
        field_71442_b.func_78748_a(field_71439_g);

        if (currentScreen instanceof GuiGameOver)
        {
            displayGuiScreen(null);
        }
    }

    void func_71390_a(boolean par1)
    {
        field_71459_aj = par1;
    }

    public final boolean func_71355_q()
    {
        return field_71459_aj;
    }

    /**
     * get the client packet send queue
     */
    public NetClientHandler getSendQueue()
    {
        if (field_71439_g != null)
        {
            return field_71439_g.sendQueue;
        }
        else
        {
            return null;
        }
    }

    public static void main(String par0ArrayOfStr[])
    {
        if (par0ArrayOfStr.length > 0){
            if (par0ArrayOfStr[0].equals("--server") || par0ArrayOfStr[0].equals("-server") || par0ArrayOfStr[0].equals("server")){
                String[] args2 = new String[par0ArrayOfStr.length - 1];
                for (int i = 0; i < args2.length; i++){
                    args2[i] = par0ArrayOfStr[i + 1];
                }
                new net.minecraft.src.MinecraftAppletImpl(null, null, null, 100, 100, false);
                net.minecraft.server.MinecraftServer.main(args2);
                return;
            }
        }
        HashMap hashmap = new HashMap();
        boolean flag = false;
        boolean flag1 = true;
        boolean flag2 = false;
        String s = (new StringBuilder()).append("Player").append(func_71386_F() % 1000L).toString();

        if (par0ArrayOfStr.length > 0)
        {
            s = par0ArrayOfStr[0];
        }

        String s1 = "-";

        if (par0ArrayOfStr.length > 1)
        {
            s1 = par0ArrayOfStr[1];
        }

        for (int i = 2; i < par0ArrayOfStr.length; i++)
        {
            String s2 = par0ArrayOfStr[i];
            String s3 = i != par0ArrayOfStr.length - 1 ? par0ArrayOfStr[i + 1] : null;
            boolean flag3 = false;

            if (s2.equals("-demo") || s2.equals("--demo"))
            {
                flag = true;
            }
            else if (s2.equals("--applet"))
            {
                flag1 = false;
            }

            if (flag3)
            {
                i++;
            }
        }

        hashmap.put("demo", (new StringBuilder()).append("").append(flag).toString());
        hashmap.put("stand-alone", (new StringBuilder()).append("").append(flag1).toString());
        hashmap.put("username", s);
        hashmap.put("fullscreen", (new StringBuilder()).append("").append(flag2).toString());
        hashmap.put("sessionid", s1);
        Frame frame = new Frame();
        frame.setTitle("Minecraft");
        frame.setBackground(Color.BLACK);
        JPanel jpanel = new JPanel();
        frame.setLayout(new BorderLayout());
        jpanel.setPreferredSize(new Dimension(854, 480));
        frame.add(jpanel, "Center");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new GameWindowListener());
        MinecraftFakeLauncher minecraftfakelauncher = new MinecraftFakeLauncher(hashmap);
        MinecraftApplet minecraftapplet = new MinecraftApplet();
        minecraftapplet.setStub(minecraftfakelauncher);
        minecraftfakelauncher.setLayout(new BorderLayout());
        minecraftfakelauncher.add(minecraftapplet, "Center");
        minecraftfakelauncher.validate();
        frame.removeAll();
        frame.setLayout(new BorderLayout());
        frame.add(minecraftfakelauncher, "Center");
        frame.validate();
        minecraftapplet.init();
        minecraftapplet.start();
        Runtime.getRuntime().addShutdownHook(new ThreadShutdown());
    }

    public static boolean isGuiEnabled()
    {
        return theMinecraft == null || !theMinecraft.gameSettings.hideGUI;
    }

    public static boolean isFancyGraphicsEnabled()
    {
        return theMinecraft != null && theMinecraft.gameSettings.fancyGraphics;
    }

    /**
     * Returns if ambient occlusion is enabled
     */
    public static boolean isAmbientOcclusionEnabled()
    {
        return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion;
    }

    public static boolean isDebugInfoEnabled()
    {
        return theMinecraft != null && theMinecraft.gameSettings.showDebugInfo;
    }

    /**
     * Returns true if the message is a client command and should not be sent to the server. However there are no such
     * commands at this point in time.
     */
    public boolean handleClientCommand(String par1Str)
    {
        return par1Str.startsWith("/") ? false : false;
    }

    /**
     * Called when the middle mouse button gets clicked
     */
    private void clickMiddleMouseButton()
    {
        if (objectMouseOver == null)
        {
            return;
        }

        boolean flag = field_71439_g.capabilities.isCreativeMode;
        int j = 0;
        boolean flag1 = false;
        int i;

        if (objectMouseOver.typeOfHit == EnumMovingObjectType.TILE)
        {
            int k = objectMouseOver.blockX;
            int i1 = objectMouseOver.blockY;
            int j1 = objectMouseOver.blockZ;
            Block block = Block.blocksList[field_71441_e.getBlockId(k, i1, j1)];

            if (block == null)
            {
                return;
            }

            i = block.func_71922_a(field_71441_e, k, i1, j1);

            if (i == 0)
            {
                return;
            }

            flag1 = Item.itemsList[i].getHasSubtypes();
            int k1 = i < 256 ? i : block.blockID;
            j = Block.blocksList[k1].func_71873_h(field_71441_e, k, i1, j1);
        }
        else if (objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY && objectMouseOver.entityHit != null && flag)
        {
            if (objectMouseOver.entityHit instanceof EntityPainting)
            {
                i = Item.painting.shiftedIndex;
            }
            else if (objectMouseOver.entityHit instanceof EntityMinecart)
            {
                EntityMinecart entityminecart = (EntityMinecart)objectMouseOver.entityHit;

                if (entityminecart.minecartType == 2)
                {
                    i = Item.minecartPowered.shiftedIndex;
                }
                else if (entityminecart.minecartType == 1)
                {
                    i = Item.minecartCrate.shiftedIndex;
                }
                else
                {
                    i = Item.minecartEmpty.shiftedIndex;
                }
            }
            else if (objectMouseOver.entityHit instanceof EntityBoat)
            {
                i = Item.boat.shiftedIndex;
            }
            else
            {
                i = Item.monsterPlacer.shiftedIndex;
                j = EntityList.getEntityID(objectMouseOver.entityHit);
                flag1 = true;

                if (j <= 0 || !EntityList.entityEggs.containsKey(Integer.valueOf(j)))
                {
                    return;
                }
            }
        }
        else
        {
            return;
        }

        field_71439_g.inventory.setCurrentItem(i, j, flag1, flag);

        if (flag)
        {
            int l = (field_71439_g.inventorySlots.inventorySlots.size() - 9) + field_71439_g.inventory.currentItem;
            field_71442_b.sendSlotPacket(field_71439_g.inventory.getStackInSlot(field_71439_g.inventory.currentItem), l);
        }
    }

    public CrashReport func_71396_d(CrashReport par1CrashReport)
    {
        par1CrashReport.func_71500_a("LWJGL", new CallableLWJGLVersion(this));
        par1CrashReport.func_71500_a("OpenGL", new CallableGLInfo(this));
        par1CrashReport.func_71500_a("Is Modded", new CallableModded(this));
        par1CrashReport.func_71500_a("Type", new CallableType2(this));
        par1CrashReport.func_71500_a("Texture Pack", new CallableClientProfiler(this));
        par1CrashReport.func_71500_a("Profiler Position", new EnumOSHelper(this));

        if (field_71441_e != null)
        {
            field_71441_e.func_72914_a(par1CrashReport);
        }

        return par1CrashReport;
    }

    public static Minecraft func_71410_x()
    {
        return theMinecraft;
    }

    public void func_71395_y()
    {
        field_71468_ad = true;
    }

    public void func_70000_a(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("fps", Integer.valueOf(field_71470_ab));
        par1PlayerUsageSnooper.addData("texpack_name", texturePackList.func_77292_e().func_77538_c());
        par1PlayerUsageSnooper.addData("texpack_resolution", Integer.valueOf(texturePackList.func_77292_e().func_77534_f()));
        par1PlayerUsageSnooper.addData("vsync_enabled", Boolean.valueOf(gameSettings.field_74352_v));
        par1PlayerUsageSnooper.addData("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
        par1PlayerUsageSnooper.addData("display_type", fullscreen ? "fullscreen" : "windowed");
    }

    public void func_70001_b(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("opengl_version", GL11.glGetString(GL11.GL_VERSION));
        par1PlayerUsageSnooper.addData("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
        par1PlayerUsageSnooper.addData("client_brand", ClientBrandRetriever.getClientModName());
        par1PlayerUsageSnooper.addData("applet", Boolean.valueOf(hideQuitButton));
        ContextCapabilities contextcapabilities = GLContext.getCapabilities();
        par1PlayerUsageSnooper.addData("gl_caps[ARB_multitexture]", Boolean.valueOf(contextcapabilities.GL_ARB_multitexture));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_multisample]", Boolean.valueOf(contextcapabilities.GL_ARB_multisample));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_texture_cube_map]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_cube_map));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_blend]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_blend));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_matrix_palette]", Boolean.valueOf(contextcapabilities.GL_ARB_matrix_palette));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_program]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_program));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_shader));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_fragment_program]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_program));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_fragment_shader]", Boolean.valueOf(contextcapabilities.GL_ARB_fragment_shader));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_shader_objects]", Boolean.valueOf(contextcapabilities.GL_ARB_shader_objects));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_vertex_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_vertex_buffer_object));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_framebuffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_framebuffer_object));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_pixel_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_pixel_buffer_object));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_uniform_buffer_object]", Boolean.valueOf(contextcapabilities.GL_ARB_uniform_buffer_object));
        par1PlayerUsageSnooper.addData("gl_caps[ARB_texture_non_power_of_two]", Boolean.valueOf(contextcapabilities.GL_ARB_texture_non_power_of_two));
        par1PlayerUsageSnooper.addData("gl_max_texture_size", Integer.valueOf(func_71369_N()));
    }

    private static int func_71369_N()
    {
        for (int i = 16384; i > 0; i >>= 1)
        {
            GL11.glTexImage2D(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_RGBA, i, i, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)null);
            int j = GL11.glGetTexLevelParameteri(GL11.GL_PROXY_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);

            if (j != 0)
            {
                return i;
            }
        }

        return -1;
    }

    public boolean func_70002_Q()
    {
        return gameSettings.field_74355_t;
    }

    public void func_71351_a(ServerData par1ServerData)
    {
        field_71422_O = par1ServerData;
    }

    public ServerData func_71362_z()
    {
        return field_71422_O;
    }

    public boolean func_71387_A()
    {
        return field_71455_al;
    }

    public boolean func_71356_B()
    {
        return field_71455_al && field_71437_Z != null;
    }

    public IntegratedServer func_71401_C()
    {
        return field_71437_Z;
    }

    public static void func_71363_D()
    {
        if (theMinecraft == null)
        {
            return;
        }

        IntegratedServer integratedserver = theMinecraft.func_71401_C();

        if (integratedserver != null)
        {
            integratedserver.func_71260_j();
        }
    }

    public PlayerUsageSnooper func_71378_E()
    {
        return field_71427_U;
    }

    public static long func_71386_F()
    {
        return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
    }

    public boolean func_71372_G()
    {
        return fullscreen;
    }

    public ICommandManager getCommandManager()
    {
        return commandManager;
    }

    public static Minecraft getMinecraftInstance()
    {
        return theMinecraft;
    }

    /**
     * creates a new world or loads an existing one
     */
    public void startWorldSSP(String par1Str, String par2Str, WorldSettings par3WorldSettings)
    {
        changeWorld1(null);
        System.gc();
        for (Mod mod : mods){
            mod.onLoadingSP(par1Str, par2Str);
        }
        if (saveLoader.isOldMapFormat(par1Str))
        {
            convertMapFormat(par1Str, par2Str);
        }
        else
        {
            if (loadingScreen != null)
            {
                loadingScreen.printText(StatCollector.translateToLocal("menu.switchingLevel"));
                loadingScreen.displayLoadingString("");
            }

            net.minecraft.src.ISaveHandler isavehandler = saveLoader.getSaveLoader(par1Str, false);
            WorldSSP world = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{ISaveHandler.class, String.class, WorldSettings.class, Profiler.class}).
                           newInstance(new Object[]{isavehandler, par2Str, par3WorldSettings, field_71424_I});
                world = (WorldSSP)o;
//                world = new WorldSSP(isavehandler, par2Str, par3WorldSettings, field_71424_I);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            if (world.isNewWorld)
            {
                statFileWriter.readStat(StatList.createWorldStat, 1);
                statFileWriter.readStat(StatList.startGameStat, 1);
                changeWorld2(world, StatCollector.translateToLocal("menu.generatingLevel"));
            }
            else
            {
                statFileWriter.readStat(StatList.loadWorldStat, 1);
                statFileWriter.readStat(StatList.startGameStat, 1);
                changeWorld2(world, StatCollector.translateToLocal("menu.loadingLevel"));
            }
        }
        lastWorld = par1Str;
    }

    /**
     * Unloads the current world, and displays a String while waiting
     */
    public void exitToMainMenu(String par1Str)
    {
        field_71441_e = null;
        changeWorld2(null, par1Str);
    }

    /**
     * Changes the world, no message, no player.
     */
    public void changeWorld1(WorldSSP par1World)
    {
        changeWorld2(par1World, "");
    }

    /**
     * Changes the world with given message, no player.
     */
    public void changeWorld2(WorldSSP par1World, String par2Str)
    {
        changeWorld(par1World, par2Str, null);
        if (!par2Str.equals("")){
            if (par1World == null){
                invokeModMethod("ModLoader", "clientDisconnect", new Class[]{});
            }else{
                invokeModMethod("ModLoader", "clientConnect", new Class[]{NetClientHandler.class, Packet1Login.class}, getSendQueue(), null);
            }
        }
    }

    /**
     * first argument is the world to change to, second one is a loading message and the third the player itself
     */
    public void changeWorld(WorldSSP par1World, String par2Str, EntityPlayer par3EntityPlayer)
    {
//         statFileWriter.func_27175_b();
        statFileWriter.syncStats();
        renderViewEntity = null;

        if (loadingScreen != null)
        {
            loadingScreen.printText(par2Str);
            loadingScreen.displayLoadingString("");
        }

        sndManager.playStreaming(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);

        if (field_71441_e != null)
        {
            ((WorldSSP)field_71441_e).saveWorldIndirectly(loadingScreen);
        }

        field_71441_e = par1World;

        if (par1World != null)
        {
            commandManager = new ClientCommandManager();

            if (!isMultiplayerWorld())
            {
                if (par3EntityPlayer == null)
                {
                    field_71439_g = (EntityPlayerSP2)par1World.func_4085_a(net.minecraft.src.EntityPlayerSP2.class);
                }
            }
            else if (field_71439_g != null)
            {
                field_71439_g.preparePlayerToSpawn();

                if (par1World != null)
                {
                    par1World.spawnEntityInWorld(field_71439_g);
                }
            }

            if (!par1World.isRemote)
            {
                preloadWorld(par2Str);
            }

            if (field_71439_g == null)
            {
                field_71439_g = field_71442_b.func_78754_a(par1World);
                field_71439_g.preparePlayerToSpawn();
                field_71442_b.flipPlayer(field_71439_g);
            }

            field_71439_g.movementInput = new MovementInputFromOptions(gameSettings);

            if (renderGlobal != null)
            {
                renderGlobal.func_72732_a(par1World);
            }

            if (effectRenderer != null)
            {
                effectRenderer.clearEffects(par1World);
            }

            if (par3EntityPlayer != null)
            {
                par1World.func_6464_c();
            }

            net.minecraft.src.IChunkProvider ichunkprovider = par1World.getChunkProvider();

            if (ichunkprovider instanceof ChunkProviderLoadOrGenerate)
            {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
                int i = MathHelper.floor_float((int)field_71439_g.posX) >> 4;
                int j = MathHelper.floor_float((int)field_71439_g.posZ) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
            }

            par1World.spawnPlayerWithLoadedChunks(field_71439_g);
            ((PlayerController)field_71442_b).setGameMode(field_71439_g);

            if (par1World.isNewWorld)
            {
                ((WorldSSP)par1World).saveWorldIndirectly(loadingScreen);
            }

            renderViewEntity = field_71439_g;
        }
        else
        {
            saveLoader.flushCache();
            field_71439_g = null;
        }

        System.gc();
        systemTime = 0L;
    }

    /**
     * Converts from old map format to new map format
     */
    private void convertMapFormat(String par1Str, String par2Str)
    {
        loadingScreen.printText((new StringBuilder()).append("Converting World to Anvil")/*.append(saveLoader.getFormatName())*/.toString());
        loadingScreen.displayLoadingString("This may take a while :)");
        saveLoader.convertMapFormat(par1Str, loadingScreen);
        startWorldSSP(par1Str, par2Str, new WorldSettings(0L, EnumGameType.SURVIVAL, true, false, WorldType.DEFAULT));
    }

    /**
     * Display the preload world loading screen then load SP World.
     */
    private void preloadWorld(String par1Str)
    {
        if (loadingScreen != null)
        {
            loadingScreen.printText(par1Str);
            loadingScreen.displayLoadingString(StatCollector.translateToLocal("menu.generatingTerrain"));
        }

        char c = '\200';

        if (((PlayerController)field_71442_b).func_35643_e())
        {
            c = '@';
        }

        int i = 0;
        int j = (c * 2) / 16 + 1;
        j *= j;
        IChunkProvider ichunkprovider = field_71441_e.getChunkProvider();
        ChunkCoordinates chunkcoordinates = field_71441_e.getSpawnPoint();

        if (field_71439_g != null)
        {
            chunkcoordinates.posX = (int)field_71439_g.posX;
            chunkcoordinates.posZ = (int)field_71439_g.posZ;
        }

        if (ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            chunkproviderloadorgenerate.setCurrentChunkOver(chunkcoordinates.posX >> 4, chunkcoordinates.posZ >> 4);
        }

        for (int k = -c; k <= c; k += 16)
        {
            for (int l = -c; l <= c; l += 16)
            {
                if (loadingScreen != null)
                {
                    loadingScreen.setLoadingProgress((i++ * 100) / j);
                }

                field_71441_e.getBlockId(chunkcoordinates.posX + k, 64, chunkcoordinates.posZ + l);

                if (((PlayerController)field_71442_b).func_35643_e())
                {
                    continue;
                }

                while (field_71441_e.updatingLighting()) ;
            }
        }

        if (!((PlayerController)field_71442_b).func_35643_e())
        {
            if (loadingScreen != null)
            {
                loadingScreen.displayLoadingString(StatCollector.translateToLocal("menu.simulating"));
            }

            char c1 = 2000;
            ((WorldSSP)field_71441_e).dropOldChunks();
        }
    }

    /**
     * Called when the respawn button is pressed after the player dies.
     */
    public void respawn(boolean par1, int par2, boolean par3)
    {
        if (!field_71441_e.isRemote && !field_71441_e.worldProvider.canRespawnHere())
        {
            usePortal(0);
        }

        ChunkCoordinates chunkcoordinates = null;
        ChunkCoordinates chunkcoordinates1 = null;
        boolean flag = true;

        if (field_71439_g != null && !par1)
        {
            chunkcoordinates = field_71439_g.getSpawnChunk();

            if (chunkcoordinates != null)
            {
                chunkcoordinates1 = EntityPlayer.verifyRespawnCoordinates(field_71441_e, chunkcoordinates);

                if (chunkcoordinates1 == null)
                {
                    field_71439_g.addChatMessage("tile.bed.notValid");
                }
            }
        }

        if (chunkcoordinates1 == null)
        {
            chunkcoordinates1 = field_71441_e.getSpawnPoint();
            flag = false;
        }

        net.minecraft.src.IChunkProvider ichunkprovider = field_71441_e.getChunkProvider();

        if (ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            chunkproviderloadorgenerate.setCurrentChunkOver(chunkcoordinates1.posX >> 4, chunkcoordinates1.posZ >> 4);
        }

        field_71441_e.setSpawnLocation();
        ((WorldSSP)field_71441_e).updateEntityList();
        int i = 0;

        if (field_71439_g != null)
        {
            i = field_71439_g.entityId;
            field_71441_e.setEntityDead(field_71439_g);
        }

        EntityPlayerSP entityplayersp = field_71439_g;
        renderViewEntity = null;
        field_71439_g = field_71442_b.func_78754_a(field_71441_e);

        if (par3)
        {
            field_71439_g.func_71049_a(entityplayersp, true);
        }

        field_71439_g.dimension = par2;
        renderViewEntity = field_71439_g;
        field_71439_g.preparePlayerToSpawn();

        if (flag)
        {
            field_71439_g.setSpawnChunk(chunkcoordinates);
            field_71439_g.setLocationAndAngles((float)chunkcoordinates1.posX + 0.5F, (float)chunkcoordinates1.posY + 0.1F, (float)chunkcoordinates1.posZ + 0.5F, 0.0F, 0.0F);
        }

        field_71442_b.flipPlayer(field_71439_g);
        ((WorldSSP)field_71441_e).spawnPlayerWithLoadedChunks(field_71439_g);
        field_71439_g.movementInput = new MovementInputFromOptions(gameSettings);
        field_71439_g.entityId = i;
        ((EntityPlayerSP2)field_71439_g).func_6420_o();
        ((PlayerController)field_71442_b).setGameMode(field_71439_g);
        preloadWorld(StatCollector.translateToLocal("menu.respawning"));

        if (currentScreen instanceof GuiGameOver)
        {
            displayGuiScreen(null);
        }
    }

    /**
     * Will use a portal teleport switching the dimension the player is in.
     */
    public void usePortal(int par1)
    {
        int i = field_71439_g.dimension;
        field_71439_g.dimension = par1;
        field_71441_e.setEntityDead(field_71439_g);
        field_71439_g.isDead = false;
        double d = field_71439_g.posX;
        double d1 = field_71439_g.posZ;
        double d2 = 1.0D;

        if (i > -1 && field_71439_g.dimension == -1)
        {
            d2 = 0.125D;
        }
        else if (i == -1 && field_71439_g.dimension > -1)
        {
            d2 = 8D;
        }

        d *= d2;
        d1 *= d2;

        if (field_71439_g.dimension == -1)
        {
            field_71439_g.setLocationAndAngles(d, field_71439_g.posY, d1, field_71439_g.rotationYaw, field_71439_g.rotationPitch);

            if (field_71439_g.isEntityAlive())
            {
                field_71441_e.updateEntityWithOptionalForce(field_71439_g, false);
            }

            WorldSSP world = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{WorldSSP.class, WorldProvider.class, Profiler.class}).
                           newInstance(new Object[]{(WorldSSP)field_71441_e, WorldProvider.getProviderForDimension(field_71439_g.dimension), field_71424_I});
                world = (WorldSSP)o;
//                world = new WorldSSP((WorldSSP)field_71441_e, WorldProvider.getProviderForDimension(field_71439_g.dimension), field_71424_I);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            changeWorld(world, "Entering the Nether", field_71439_g);
        }
        else if (field_71439_g.dimension == 0)
        {
            if (field_71439_g.isEntityAlive())
            {
                field_71439_g.setLocationAndAngles(d, field_71439_g.posY, d1, field_71439_g.rotationYaw, field_71439_g.rotationPitch);
                field_71441_e.updateEntityWithOptionalForce(field_71439_g, false);
            }

            WorldSSP world1 = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{WorldSSP.class, WorldProvider.class, Profiler.class}).
                           newInstance(new Object[]{(WorldSSP)field_71441_e, WorldProvider.getProviderForDimension(field_71439_g.dimension), field_71424_I});
                world1 = (WorldSSP)o;
//                world1 = new WorldSSP((WorldSSP)field_71441_e, WorldProvider.getProviderForDimension(field_71439_g.dimension), field_71424_I);
            }catch(Exception ex){
                ex.printStackTrace();
            }

            if (i == -1)
            {
                changeWorld(world1, "Leaving the Nether", field_71439_g);
            }
            else
            {
                changeWorld(world1, "Leaving the End", field_71439_g);
            }
        }
        else
        {
            WorldSSP world2 = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{WorldSSP.class, WorldProvider.class, Profiler.class}).
                           newInstance(new Object[]{(WorldSSP)field_71441_e, WorldProvider.getProviderForDimension(field_71439_g.dimension), field_71424_I});
                world2 = (WorldSSP)o;
//                world2 = new WorldSSP((WorldSSP)field_71441_e, WorldProvider.getProviderForDimension(field_71439_g.dimension), field_71424_I);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            ChunkCoordinates chunkcoordinates = world2.getEntrancePortalLocation();
            d = chunkcoordinates.posX;
            field_71439_g.posY = chunkcoordinates.posY;
            d1 = chunkcoordinates.posZ;
            field_71439_g.setLocationAndAngles(d, field_71439_g.posY, d1, 90F, 0.0F);

            if (field_71439_g.isEntityAlive())
            {
                world2.updateEntityWithOptionalForce(field_71439_g, false);
            }

            changeWorld(world2, "Entering the End", field_71439_g);
        }

        field_71439_g.worldObj = field_71441_e;
        System.out.println((new StringBuilder()).append("Teleported to ").append(field_71441_e.worldProvider.worldType).toString());

        if (field_71439_g.isEntityAlive() && i < 1)
        {
            field_71439_g.setLocationAndAngles(d, field_71439_g.posY, d1, field_71439_g.rotationYaw, field_71439_g.rotationPitch);
            field_71441_e.updateEntityWithOptionalForce(field_71439_g, false);
            (new Teleporter()).placeInPortal(field_71441_e, field_71439_g);
        }
    }

    /**
     * Checks if the current world is a multiplayer world, returns true if it is, false otherwise.
     */
    public boolean isMultiplayerWorld()
    {
        return field_71441_e != null && field_71441_e.isRemote;
    }

    public void setController(EnumGameType enumgametype){
        if (enumgametype == EnumGameType.SURVIVAL){
            field_71442_b = new PlayerControllerSP(this);
            return;
        }
        if (enumgametype == EnumGameType.CREATIVE){
            field_71442_b = new PlayerControllerCreative(this);
            return;
        }
        if (enumgametype == EnumGameType.ADVENTURE){
            field_71442_b = new PlayerControllerAdventure(this);
            return;
        }
    }

    public void setGameMode(EnumGameType enumgametype){
        if (enumgametype == EnumGameType.SURVIVAL){
            PlayerControllerCreative.disableAbilities(field_71439_g);
            PlayerControllerAdventure.enableAbilities(field_71439_g);
            return;
        }
        if (enumgametype == EnumGameType.CREATIVE){
            PlayerControllerCreative.enableAbilities(field_71439_g);
            PlayerControllerAdventure.enableAbilities(field_71439_g);
            return;
        }
        if (enumgametype == EnumGameType.ADVENTURE){
            PlayerControllerCreative.disableAbilities(field_71439_g);
            PlayerControllerAdventure.disableAbilities(field_71439_g);
            return;
        }
    }

    public void quitAndStartServer(){
        if (func_71401_C() != null || enableSP == false){
            return;
        }
        WorldSSP world = (WorldSSP)field_71441_e;
        changeWorld1(null);
        displayGuiScreen(null);
        enableSP = false;
        String s = lastWorld;
        String s1 = world.getWorldInfo().getWorldName();
        func_71371_a(s, s1, null);
    }

    public void loadMods(){
        Class c = net.minecraft.src.Mod.class;
        String p = "";
        try{
            p = c.getPackage().getName()+".";
        }catch(Exception ex){}
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path.replace("%20", " ")+p.replace(".", "/"));        List classes = new ArrayList();
        if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")){
            try{
                ZipFile jar = new ZipFile(file);
                Enumeration entries = jar.entries();
                while (entries.hasMoreElements()){
                    String str = ((ZipEntry)entries.nextElement()).getName();
                    if (str.startsWith("mod_") && str.endsWith(".class")){
                        classes.add(str.replace(".class", ""));
                    }
                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }else{
            String[] str = file.list();
            for (int i = 0; i < str.length; i++){
                if (str[i].startsWith("mod_") && str[i].endsWith(".class")){
                    classes.add(str[i].replace(".class", ""));
                }
            }
        }
        for (int i = 0; i < classes.size(); i++){
            String name = ((String)classes.get(i));
            Class c2 = null;
            try{
                c2 = c.getClassLoader().loadClass(p+name);
            }catch(Exception ex){
                System.out.println("Failed to load mod: "+ex);
                ex.printStackTrace();
                continue;
            }
            if (!c.isAssignableFrom(c2)){
                continue;
            }
            Mod mod = null;
            try{
                mod = ((Mod)c2.getDeclaredConstructor().newInstance());
            }catch(Exception ex){
                System.out.println("Failed to load mod: "+ex);
                ex.printStackTrace();
                continue;
            }
            if (!mod.getMcVersion().equals(getVersion())){
                System.out.println(mod.getModName()+" "+mod.getModVersion()+" is for Minecraft "+mod.getMcVersion()+", not "+getVersion());
                return;
            }
            mods.add(mod);
            mod.load();
            System.out.println("Loaded "+mod.getModName()+" "+mod.getModVersion()+" for Minecraft "+mod.getMcVersion());
        }
    }

    public boolean renderBlocksMod(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        for (Mod mod : mods){
            if (mod.renderBlocks(r, i, b, x, y, z, id, override)){
                return true;
            }
        }
        invokeModMethod("ModLoader", "renderWorldBlock",
                        new Class[]{RenderBlocks.class, IBlockAccess.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Block.class, Integer.TYPE},
                        r, i, x, y, z, b, id);
        return false;
    }

    public String getVersion(){
        return "1.3.1";
    }

    private void registerCustomPacket(){
        try{
            int id = 1;
            Method m = null;
            Method[] methods = (net.minecraft.src.Packet.class).getDeclaredMethods();
            for (int i = 0; i < methods.length; i++){
                if (methods[i].toGenericString().matches("^static void (net.minecraft.src.)?([a-zA-Z]{1,6}).[a-zA-Z]{1,17}.int,boolean,boolean,java.lang.Class.$")){
                    m = methods[i];
                    break;
                }
            }
            if (m == null){
                return;
            }
            m.setAccessible(true);
            m.invoke(null, new Object[]{251, true, true, net.minecraft.src.Packet300Custom.class});
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void checkCompatibility(String mod){
        try{
            Class.forName(mod);
        }catch(ClassNotFoundException e){
            try{
                Class.forName("net.minecraft.src."+mod);
            }catch(ClassNotFoundException ex){
                compat.put(mod, 0);
                return;
            }
            System.out.println("SSP: Enabled "+mod+" compatibility");
            compat.put(mod, 2);
            return;
        }
        System.out.println("SSP: Enabled "+mod+" compatibility");
        compat.put(mod, 1);
    }

    private void invokeModMethod_do(String mod, String method, Class[] pars, Object... args){
        if (compat.get(mod) <= 0){
            return;
        }
        try{
            Class c = Class.forName((compat.get(mod) > 1 ? "net.minecraft.src." : "")+mod);
            Method m = c.getDeclaredMethod(method, pars);
            m.invoke(null, args);
//             System.out.printlsn("SSP: Invoking "+m.toString());
        }catch(Exception ex){
            ex.printStackTrace();
            compat.put(mod, 0);
        }
    }

    public static void invokeModMethod(String mod, String method, Class[] pars, Object... args){
        getMinecraftInstance().invokeModMethod_do(mod, method, pars, args);
    }
}
