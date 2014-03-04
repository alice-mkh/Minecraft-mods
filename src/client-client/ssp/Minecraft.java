package net.minecraft.src;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Proxy;
import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.server.MinecraftServer;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.*;
import net.minecraft.src.ssp.ChunkProviderLoadOrGenerate;
import net.minecraft.src.ssp.ClientCommandManager;
import net.minecraft.src.ssp.FakeServer;
import net.minecraft.src.ssp.FakeServerPlayerList;
import net.minecraft.src.ssp.EntityPlayerSP2;
import net.minecraft.src.ssp.GuiGameOverOverlay;
import net.minecraft.src.ssp.GuiIngameMenuOverlay;
import net.minecraft.src.ssp.GuiMultiplayerOverlay;
import net.minecraft.src.ssp.GuiMainMenuOverlay;
import net.minecraft.src.ssp.GuiShareToLanOverlay;
import net.minecraft.src.ssp.Mod;
import net.minecraft.src.ssp.Packet300Custom;
import net.minecraft.src.ssp.PlayerController;
import net.minecraft.src.ssp.PlayerControllerAdventure;
import net.minecraft.src.ssp.PlayerControllerCreative;
import net.minecraft.src.ssp.PlayerControllerSP;
import net.minecraft.src.ssp.TeleporterSP;
import net.minecraft.src.ssp.WorldSSP;

public class Minecraft implements IPlayerUsage
{
    private static final ResourceLocation locationMojangPng = new ResourceLocation("textures/gui/title/mojang.png");
    public static final boolean isRunningOnMac;
    public static byte memoryReserve[] = new byte[0xa00000];
    private static final List macDisplayModes = Lists.newArrayList(new DisplayMode[]
            {
                new DisplayMode(2560, 1600), new DisplayMode(2880, 1800)
            });
    private final ILogAgent mcLogAgent;
    private final File fileResourcepacks;
    private ServerData currentServerData;

    /** The RenderEngine instance used by Minecraft */
    private TextureManager renderEngine;

    /**
     * Set to 'this' in Minecraft constructor; used by some settings get methods
     */
    private static Minecraft theMinecraft;
    public PlayerControllerMP playerController;
    private boolean fullscreen;
    private boolean hasCrashed;

    /** Instance of CrashReport. */
    private CrashReport crashReporter;
    public int displayWidth;
    public int displayHeight;
    private Timer timer;

    /** Instance of PlayerUsageSnooper. */
    private PlayerUsageSnooper usageSnooper;
    public WorldClient theWorld;
    public RenderGlobal renderGlobal;
    public EntityClientPlayerMP thePlayer;

    /**
     * The Entity from which the renderer determines the render viewpoint. Currently is always the parent Minecraft
     * class's 'thePlayer' instance. Modification of its location, rotation, or other settings at render time will
     * modify the camera likewise, with the caveat of triggering chunk rebuilds as it moves, making it unsuitable for
     * changing the viewpoint mid-render.
     */
    public EntityLivingBase renderViewEntity;
    public EntityLivingBase pointedEntityLiving;
    public EffectRenderer effectRenderer;
    private final Session session;
    private boolean isGamePaused;

    /** The font renderer used for displaying and measuring text. */
    public FontRenderer fontRenderer;
    public FontRenderer standardGalacticFontRenderer;

    /** The GuiScreen that's being displayed at the moment. */
    public GuiScreen currentScreen;
    public LoadingScreenRenderer loadingScreen;
    public EntityRenderer entityRenderer;

    /** Mouse left click counter */
    private int leftClickCounter;

    /** Display width */
    private int tempDisplayWidth;

    /** Display height */
    private int tempDisplayHeight;

    /** Instance of IntegratedServer. */
    private IntegratedServer theIntegratedServer;

    /** Gui achievement */
    public GuiAchievement guiAchievement;
    public GuiIngame ingameGUI;

    /** Skip render world */
    public boolean skipRenderWorld;

    /** The ray trace hit that the mouse is over. */
    public MovingObjectPosition objectMouseOver;

    /** The game settings that currently hold effect. */
    public GameSettings gameSettings;
    public SoundManager sndManager;

    /** Mouse helper instance. */
    public MouseHelper mouseHelper;
    public final File mcDataDir;
    private final File fileAssets;
    private final String launchedVersion;
    private final Proxy proxy;
    private ISaveFormat saveLoader;

    /**
     * This is set to fpsCounter every debug screen update, and is shown on the debug screen. It's also sent as part of
     * the usage snooping.
     */
    private static int debugFPS;

    /**
     * When you place a block, it's set to 6, decremented once per tick, when it's 0, you can place another block.
     */
    private int rightClickDelayTimer;

    /**
     * Checked in Minecraft's while(running) loop, if true it's set to false and the textures refreshed.
     */
    private boolean refreshTexturePacksScheduled;

    /** Stat file writer */
    public StatFileWriter statFileWriter;
    private String serverName;
    private int serverPort;

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
    private final boolean isDemo;
    private INetworkManager myNetworkManager;
    private boolean integratedServerIsRunning;

    /** The profiler instance */
    public final Profiler mcProfiler = new Profiler();
    private long field_83002_am;
    private ReloadableResourceManager mcResourceManager;
    private final MetadataSerializer metadataSerializer_ = new MetadataSerializer();
    private List defaultResourcePacks;
    private DefaultResourcePack mcDefaultResourcePack;
    private ResourcePackRepository mcResourcePackRepository;
    private LanguageManager mcLanguageManager;

    /**
     * Set to true to keep the game loop running. Set to false by shutdown() to allow the game loop to exit cleanly.
     */
    volatile boolean running;

    /** String that shows the debug information */
    public String debug;

    /** Approximate time (in ms) of last update to debug string */
    long debugUpdateTime;

    /** holds the current fps */
    int fpsCounter;
    long prevFrameTime;

    /** Profiler currently displayed in the debug screen pie chart */
    private String debugProfilerName;

    public boolean enableSP;
    public boolean useSP;
    private String lastWorld;
    public ArrayList<Mod> mods;
    private HashMap<String, Integer> compat; //0 - disabled; 1 - normal; 2 - mcp
    public Class worldClass;
    public Class playerClass;
    public int ticksRan;
    public int mouseTicksRan;
    private boolean startProfiling;
    private boolean profilingEnabled;
    public static boolean oldswing = false;
    public static boolean timecontrol = false;
    public static boolean isom = false;
    public static boolean oldlighting = false;
    public static boolean indevShapeSize = false;
    public static boolean thirdperson = true;
    public static boolean oldthirdperson = false;
    public static boolean oldHideGui = false;
    public int forcedDifficulty;
    public boolean overrideMobSpawning;

    public Minecraft(Session par1Session, int par2, int par3, boolean par4, boolean par5, File par6File, File par7File, File par8File, Proxy par9Proxy, String par10Str)
    {
        timer = new Timer(20F);
        usageSnooper = new PlayerUsageSnooper("client", this, MinecraftServer.getSystemTimeMillis());
        systemTime = getSystemTime();
        field_83002_am = -1L;
        defaultResourcePacks = Lists.newArrayList();
        running = true;
        debug = "";
        debugUpdateTime = getSystemTime();
        prevFrameTime = -1L;
        debugProfilerName = "root";
        theMinecraft = this;
        mcLogAgent = new LogAgent("Minecraft-Client", " [CLIENT]", (new File(par6File, "output-client.log")).getAbsolutePath());
        mcDataDir = par6File;
        fileAssets = par7File;
        fileResourcepacks = par8File;
        launchedVersion = par10Str;
        mcDefaultResourcePack = new DefaultResourcePack(fileAssets);
        addDefaultResourcePack();
        proxy = par9Proxy;
        startTimerHackThread();
        session = par1Session;
        mcLogAgent.logInfo((new StringBuilder()).append("Setting user: ").append(par1Session.getUsername()).toString());
        mcLogAgent.logInfo((new StringBuilder()).append("(Session ID is ").append(par1Session.getSessionID()).append(")").toString());
        isDemo = par5;
        displayWidth = par2;
        displayHeight = par3;
        tempDisplayWidth = par2;
        tempDisplayHeight = par3;
        fullscreen = par4;
        ImageIO.setUseCache(false);
        StatList.nopInit();

        useSP = true;
        enableSP = useSP;
        mods = new ArrayList<Mod>();
        compat = new HashMap<String, Integer>();
        worldClass = WorldSSP.class;
        playerClass = EntityPlayerSP2.class;
        ticksRan = 0;
        mouseTicksRan = 0;
        forcedDifficulty = -1;
        registerCustomPacket();
        startProfiling = false;
        profilingEnabled = false;
        overrideMobSpawning = false;
        checkCompatibility("ModLoader");
        setupOverlays();
    }

    private void startTimerHackThread()
    {
        ThreadClientSleep threadclientsleep = new ThreadClientSleep(this, "Timer hack thread");
        threadclientsleep.setDaemon(true);
        threadclientsleep.start();
    }

    public void crashed(CrashReport par1CrashReport)
    {
        hasCrashed = true;
        crashReporter = par1CrashReport;
    }

    /**
     * Wrapper around displayCrashReportInternal
     */
    public void displayCrashReport(CrashReport par1CrashReport)
    {
        File file = new File(getMinecraft().mcDataDir, "crash-reports");
        File file1 = new File(file, (new StringBuilder()).append("crash-").append((new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss")).format(new Date())).append("-client.txt").toString());
        System.out.println(par1CrashReport.getCompleteReport());

        if (par1CrashReport.getFile() != null)
        {
            System.out.println((new StringBuilder()).append("#@!@# Game crashed! Crash report saved to: #@!@# ").append(par1CrashReport.getFile()).toString());
            System.exit(-1);
        }
        else if (par1CrashReport.saveToFile(file1, getLogAgent()))
        {
            System.out.println((new StringBuilder()).append("#@!@# Game crashed! Crash report saved to: #@!@# ").append(file1.getAbsolutePath()).toString());
            System.exit(-1);
        }
        else
        {
            System.out.println("#@?@# Game crashed! Crash report could not be saved. #@?@#");
            System.exit(-2);
        }
    }

    public void setServer(String par1Str, int par2)
    {
        serverName = par1Str;
        serverPort = par2;
    }

    /**
     * Starts the game: initializes the canvas, the title, the settings, etcetera.
     */
    private void startGame() throws LWJGLException
    {
        gameSettings = new GameSettings(this, mcDataDir);

        if (gameSettings.overrideHeight > 0 && gameSettings.overrideWidth > 0)
        {
            displayWidth = gameSettings.overrideWidth;
            displayHeight = gameSettings.overrideHeight;
        }

        if (fullscreen)
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

        Display.setResizable(true);
        Display.setTitle("Minecraft 1.6.4");
        getLogAgent().logInfo((new StringBuilder()).append("LWJGL Version: ").append(Sys.getVersion()).toString());

        if (Util.getOSType() != EnumOS.MACOS)
        {
            try
            {
                Display.setIcon(new ByteBuffer[]
                        {
                            readImage(new File(fileAssets, "/icons/icon_16x16.png")), readImage(new File(fileAssets, "/icons/icon_32x32.png"))
                        });
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
            }
        }

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

            if (fullscreen)
            {
                updateDisplayMode();
            }

            Display.create();
        }

        OpenGlHelper.initializeTextures();
        guiAchievement = new GuiAchievement(this);
        metadataSerializer_.registerMetadataSectionType(new TextureMetadataSectionSerializer(), net.minecraft.src.TextureMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new FontMetadataSectionSerializer(), net.minecraft.src.FontMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new AnimationMetadataSectionSerializer(), net.minecraft.src.AnimationMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new PackMetadataSectionSerializer(), net.minecraft.src.PackMetadataSection.class);
        metadataSerializer_.registerMetadataSectionType(new LanguageMetadataSectionSerializer(), net.minecraft.src.LanguageMetadataSection.class);
        saveLoader = new AnvilSaveConverter(new File(mcDataDir, "saves"));
        mcResourcePackRepository = new ResourcePackRepository(fileResourcepacks, mcDefaultResourcePack, metadataSerializer_, gameSettings);
        mcResourceManager = new SimpleReloadableResourceManager(metadataSerializer_);
        mcLanguageManager = new LanguageManager(metadataSerializer_, gameSettings.language);
        mcResourceManager.registerReloadListener(mcLanguageManager);
        refreshResources();
        renderEngine = new TextureManager(mcResourceManager);
        mcResourceManager.registerReloadListener(renderEngine);
        loadMods();
        if (sndManager == null){
            setSoundManager(SoundManager.class);
        }
        loadScreen();
        fontRenderer = new FontRenderer(gameSettings, new ResourceLocation("textures/font/ascii.png"), renderEngine, false);

        if (gameSettings.language != null)
        {
            fontRenderer.setUnicodeFlag(mcLanguageManager.isCurrentLocaleUnicode());
            fontRenderer.setBidiFlag(mcLanguageManager.isCurrentLanguageBidirectional());
        }

        standardGalacticFontRenderer = new FontRenderer(gameSettings, new ResourceLocation("textures/font/ascii_sga.png"), renderEngine, false);
        mcResourceManager.registerReloadListener(fontRenderer);
        mcResourceManager.registerReloadListener(standardGalacticFontRenderer);
        mcResourceManager.registerReloadListener(new GrassColorReloadListener());
        mcResourceManager.registerReloadListener(new FoliageColorReloadListener());
        RenderManager.instance.itemRenderer = new ItemRenderer(this);
        entityRenderer = new EntityRenderer(this);
        statFileWriter = new StatFileWriter(session, mcDataDir);
        AchievementList.openInventory.setStatStringFormatter(new StatStringFormatKeyInv(this));
        mouseHelper = new MouseHelper();
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
        renderGlobal = new RenderGlobal(this);
        renderEngine.loadTextureMap(TextureMap.locationBlocksTexture, new TextureMap(0, "textures/blocks"));
        renderEngine.loadTextureMap(TextureMap.locationItemsTexture, new TextureMap(1, "textures/items"));
        for (int i = 0; i < mods.size(); i++){
            mods.get(i).refreshTextures();
        }
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        effectRenderer = new EffectRenderer(theWorld, renderEngine);
        checkGLError("Post startup");
        ingameGUI = new GuiIngame(this);

        if (serverName != null)
        {
            displayGuiScreen(new GuiConnecting(new GuiMainMenu(), this, serverName, serverPort));
        }
        else
        {
            displayGuiScreen(new GuiMainMenu());
        }

        loadingScreen = new LoadingScreenRenderer(this);

        if (gameSettings.fullScreen && !fullscreen)
        {
            toggleFullscreen();
        }
    }

    public void refreshResources()
    {
        ArrayList arraylist = Lists.newArrayList(defaultResourcePacks);
        ResourcePackRepositoryEntry resourcepackrepositoryentry;

        for (Iterator iterator = mcResourcePackRepository.getRepositoryEntries().iterator(); iterator.hasNext(); arraylist.add(resourcepackrepositoryentry.getResourcePack()))
        {
            resourcepackrepositoryentry = (ResourcePackRepositoryEntry)iterator.next();
        }

        mcLanguageManager.parseLanguageMetadata(arraylist);
        mcResourceManager.reloadResources(arraylist);

        if (renderGlobal != null)
        {
            renderGlobal.loadRenderers();
        }
    }

    private void addDefaultResourcePack()
    {
        defaultResourcePacks.add(mcDefaultResourcePack);
    }

    private ByteBuffer readImage(File par1File) throws IOException
    {
        java.awt.image.BufferedImage bufferedimage = ImageIO.read(par1File);
        int ai[] = bufferedimage.getRGB(0, 0, bufferedimage.getWidth(), bufferedimage.getHeight(), null, 0, bufferedimage.getWidth());
        ByteBuffer bytebuffer = ByteBuffer.allocate(4 * ai.length);
        int ai1[] = ai;
        int i = ai1.length;

        for (int j = 0; j < i; j++)
        {
            int k = ai1[j];
            bytebuffer.putInt(k << 8 | k >> 24 & 0xff);
        }

        bytebuffer.flip();
        return bytebuffer;
    }

    private void updateDisplayMode() throws LWJGLException
    {
        HashSet hashset = new HashSet();
        Collections.addAll(hashset, Display.getAvailableDisplayModes());
        DisplayMode displaymode = Display.getDesktopDisplayMode();

        if (!hashset.contains(displaymode) && Util.getOSType() == EnumOS.MACOS)
        {
            Iterator iterator = macDisplayModes.iterator();
            label0:

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                DisplayMode displaymode1 = (DisplayMode)iterator.next();
                boolean flag = true;
                Iterator iterator1 = hashset.iterator();

                do
                {
                    if (!iterator1.hasNext())
                    {
                        break;
                    }

                    DisplayMode displaymode2 = (DisplayMode)iterator1.next();

                    if (displaymode2.getBitsPerPixel() != 32 || displaymode2.getWidth() != displaymode1.getWidth() || displaymode2.getHeight() != displaymode1.getHeight())
                    {
                        continue;
                    }

                    flag = false;
                    break;
                }
                while (true);

                if (flag)
                {
                    continue;
                }

                iterator1 = hashset.iterator();
                DisplayMode displaymode3;

                do
                {
                    if (!iterator1.hasNext())
                    {
                        continue label0;
                    }

                    displaymode3 = (DisplayMode)iterator1.next();
                }
                while (displaymode3.getBitsPerPixel() != 32 || displaymode3.getWidth() != displaymode1.getWidth() / 2 || displaymode3.getHeight() != displaymode1.getHeight() / 2);

                displaymode = displaymode3;
            }
            while (true);
        }

        Display.setDisplayMode(displaymode);
        displayWidth = displaymode.getWidth();
        displayHeight = displaymode.getHeight();
    }

    /**
     * Displays a new screen.
     */
    private void loadScreen() throws LWJGLException
    {
        ScaledResolution scaledresolution = new ScaledResolution(gameSettings, displayWidth, displayHeight);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.getScaledWidth_double(), scaledresolution.getScaledHeight_double(), 0.0D, 1000D, 3000D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000F);
        GL11.glViewport(0, 0, displayWidth, displayHeight);
        GL11.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_FOG);
        renderEngine.bindTexture(locationMojangPng);
        Tessellator tessellator = Tessellator.instance;
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
        Display.update();
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
        if (currentScreen != null)
        {
            currentScreen.onGuiClosed();
        }

        statFileWriter.syncStats();

        if (par1GuiScreen == null && theWorld == null)
        {
            par1GuiScreen = new GuiMainMenu();
        }
        else if (par1GuiScreen == null && thePlayer.getHealth() <= 0.0F)
        {
            par1GuiScreen = new GuiGameOver();
        }

        if (par1GuiScreen instanceof GuiMainMenu)
        {
            gameSettings.showDebugInfo = false;
            ingameGUI.getChatGUI().clearChatMessages();
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
            getLogAgent().logSevere("########## GL ERROR ##########");
            getLogAgent().logSevere((new StringBuilder()).append("@ ").append(par1Str).toString());
            getLogAgent().logSevere((new StringBuilder()).append(i).append(": ").append(s).toString());
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
            getLogAgent().logInfo("Stopping!");

            try
            {
                loadWorld(null);
            }
            catch (Throwable throwable) { }

            try
            {
                GLAllocation.deleteTexturesAndDisplayLists();
            }
            catch (Throwable throwable1) { }

            sndManager.cleanup();
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
        catch (Throwable throwable)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Initializing game");
            crashreport.makeCategory("Initialization");
            displayCrashReport(addGraphicsAndWorldToCrashReport(crashreport));
            return;
        }

        try
        {
            while (running)
            {
                if (hasCrashed && crashReporter != null)
                {
                    displayCrashReport(crashReporter);
                    return;
                }

                if (refreshTexturePacksScheduled)
                {
                    refreshTexturePacksScheduled = false;
                    refreshResources();
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
            addGraphicsAndWorldToCrashReport(reportedexception.getCrashReport());
            freeMemory();
            reportedexception.printStackTrace();
            displayCrashReport(reportedexception.getCrashReport());
        }
        catch (Throwable throwable1)
        {
            CrashReport crashreport1 = addGraphicsAndWorldToCrashReport(new CrashReport("Unexpected error", throwable1));
            freeMemory();
            throwable1.printStackTrace();
            displayCrashReport(crashreport1);
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
        AxisAlignedBB.getAABBPool().cleanPool();

        if (theWorld != null)
        {
            theWorld.getWorldVec3Pool().clear();
        }

        mcProfiler.startSection("root");

        if (Display.isCloseRequested())
        {
            shutdown();
        }

        if (isGamePaused && theWorld != null)
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
        mcProfiler.startSection("tick");

        for (int i = 0; i < timer.elapsedTicks; i++)
        {
            ticksRan++;
            runTick();
        }

        mcProfiler.endStartSection("preRenderErrors");
        long l1 = System.nanoTime() - l;
        checkGLError("Pre render");
        RenderBlocks.fancyGrass = gameSettings.fancyGraphics;
        mcProfiler.endStartSection("sound");
        sndManager.setListener(thePlayer, timer.renderPartialTicks);

        if (!isGamePaused)
        {
            sndManager.func_92071_g();
        }

        mcProfiler.endStartSection("updatelights");

        if (theWorld != null && worldClass != WorldSSP.class)
        {
            theWorld.updatingLighting();
        }
        mcProfiler.endSection();
        mcProfiler.startSection("render");
        mcProfiler.startSection("display");
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        if (!Keyboard.isKeyDown(65))
        {
            Display.update();
        }

        if (thePlayer != null && thePlayer.isEntityInsideOpaqueBlock())
        {
            gameSettings.thirdPersonView = 0;
        }

        mcProfiler.endSection();

        if (!skipRenderWorld)
        {
            mcProfiler.endStartSection("gameRenderer");
            entityRenderer.updateCameraAndRender(timer.renderPartialTicks);
            mcProfiler.endSection();
        }

        GL11.glFlush();
        mcProfiler.endSection();

        if (!Display.isActive() && fullscreen)
        {
            toggleFullscreen();
        }

        if ((gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart) || startProfiling)
        {
            if (!mcProfiler.profilingEnabled)
            {
                mcProfiler.clearProfiling();
            }

            mcProfiler.profilingEnabled = true;
            if (!startProfiling){
                displayDebugInfo(l1);
            }
            startProfiling = false;
        }
        else if (!profilingEnabled)
        {
            mcProfiler.profilingEnabled = false;
            prevFrameTime = System.nanoTime();
        }

        guiAchievement.updateAchievementWindow();
        mcProfiler.startSection("root");
        Thread.yield();

        if (Keyboard.isKeyDown(65))
        {
            Display.update();
        }

        screenshotListener();

        if (!fullscreen && Display.wasResized())
        {
            displayWidth = Display.getWidth();
            displayHeight = Display.getHeight();

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

            if (isIntegratedServerRunning() && thePlayer != null && getNetHandler() != null && isGamePaused != flag)
            {
                ((MemoryConnection)getNetHandler().getNetManager()).setGamePaused(isGamePaused);
            }
        }else{
            isGamePaused = isSingleplayer() && currentScreen != null && currentScreen.doesGuiPauseGame() && !theIntegratedServer.getPublic();

            if (isIntegratedServerRunning() && thePlayer != null && ((EntityClientPlayerMP)thePlayer).sendQueue != null && isGamePaused != flag)
            {
                ((MemoryConnection)((EntityClientPlayerMP)thePlayer).sendQueue.getNetManager()).setGamePaused(isGamePaused);
            }
        }

        do
        {
            if (getSystemTime() < debugUpdateTime + 1000L)
            {
                break;
            }

            debugFPS = fpsCounter;
            debug = (new StringBuilder()).append(debugFPS).append(" fps, ").append(WorldRenderer.chunksUpdated).append(" chunk updates").toString();
            WorldRenderer.chunksUpdated = 0;
            debugUpdateTime += 1000L;
            fpsCounter = 0;
            usageSnooper.addMemoryStatsToSnooper();

            if (!usageSnooper.isSnooperRunning())
            {
                usageSnooper.startSnooper();
            }
        }
        while (true);

        mcProfiler.endSection();
        mcProfiler.startSection("mods");
        for (Mod mod : mods){
            if (mod.usesTick){
                mod.onTick(theWorld != null);
            }
            if (mod.usesGUITick){
                mod.onGUITick(currentScreen);
            }
        }
        mcProfiler.endSection();

        if (getLimitFramerate() > 0)
        {
            Display.sync(EntityRenderer.performanceToFps(getLimitFramerate()));
        }
    }

    private int getLimitFramerate()
    {
        if (currentScreen != null && (currentScreen instanceof GuiMainMenu))
        {
            return 2;
        }
        else
        {
            return gameSettings.limitFramerate;
        }
    }

    public void freeMemory()
    {
        try
        {
            memoryReserve = new byte[0];
            renderGlobal.deleteAllDisplayLists();
        }
        catch (Throwable throwable) { }

        try
        {
            System.gc();
            AxisAlignedBB.getAABBPool().clearPool();
            theWorld.getWorldVec3Pool().clearAndFreeCache();
        }
        catch (Throwable throwable1) { }

        try
        {
            System.gc();
            loadWorld(null);
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
                ingameGUI.getChatGUI().printChatMessage(ScreenShotHelper.saveScreenshot(mcDataDir, displayWidth, displayHeight));
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
        List var2 = this.mcProfiler.getProfilingData(this.debugProfilerName);

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
        if (!mcProfiler.profilingEnabled)
        {
            return;
        }

        List list = mcProfiler.getProfilingData(debugProfilerName);
        ProfilerResult profilerresult = (ProfilerResult)list.remove(0);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
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

        displayGuiScreen(new GuiIngameMenu());

        if (isSingleplayer() && !theIntegratedServer.getPublic())
        {
            sndManager.pauseAllSounds();
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
            playerController.onPlayerDamageBlock(i, j, k, objectMouseOver.sideHit);

            if (thePlayer.isCurrentToolAdventureModeExempt(i, j, k))
            {
                effectRenderer.addBlockHitEffects(i, j, k, objectMouseOver.sideHit);
                thePlayer.swingItem();
            }
        }
        else
        {
            playerController.resetBlockRemoving();
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
            thePlayer.swingItem();
        }

        if (par1 == 1)
        {
            rightClickDelayTimer = 4;
        }

        boolean flag = true;
        ItemStack itemstack = thePlayer.inventory.getCurrentItem();

        if (objectMouseOver == null)
        {
            if (par1 == 0 && playerController.isNotCreative())
            {
                leftClickCounter = 10;
            }
        }
        else if (objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY)
        {
            if (par1 == 0)
            {
                playerController.attackEntity(thePlayer, objectMouseOver.entityHit);
            }

            if (par1 == 1 && playerController.func_78768_b(thePlayer, objectMouseOver.entityHit))
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
                playerController.clickBlock(i, j, k, objectMouseOver.sideHit);
            }
            else
            {
                int i1 = itemstack == null ? 0 : itemstack.stackSize;

                if (playerController.onPlayerRightClick(thePlayer, theWorld, itemstack, i, j, k, l, objectMouseOver.hitVec))
                {
                    flag = false;
                    thePlayer.swingItem();
                }

                if (itemstack == null)
                {
                    return;
                }

                if (itemstack.stackSize == 0)
                {
                    thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
                }
                else if (itemstack.stackSize != i1 || playerController.isInCreativeMode())
                {
                    entityRenderer.itemRenderer.resetEquippedProgress();
                }
            }
        }

        if (flag && par1 == 1)
        {
            ItemStack itemstack1 = thePlayer.inventory.getCurrentItem();

            if (itemstack1 != null && playerController.sendUseItem(thePlayer, theWorld, itemstack1))
            {
                entityRenderer.itemRenderer.resetEquippedProgress2();
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
                updateDisplayMode();
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
                Display.setDisplayMode(new DisplayMode(tempDisplayWidth, tempDisplayHeight));
                displayWidth = tempDisplayWidth;
                displayHeight = tempDisplayHeight;

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
            Display.setVSyncEnabled(gameSettings.enableVsync);
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

        mcProfiler.startSection("stats");
        statFileWriter.func_77449_e();
        mcProfiler.endStartSection("gui");

        if (!isGamePaused)
        {
            ingameGUI.updateTick();
        }

        mcProfiler.endStartSection("pick");
        entityRenderer.getMouseOver(1.0F);
        mcProfiler.endStartSection("gameMode");

        if (!isGamePaused && theWorld != null)
        {
            playerController.updateController();
        }

        mcProfiler.endStartSection("textures");

        if (!isGamePaused)
        {
            renderEngine.tick();
            for (Mod mod : mods){
                mod.updateTextures();
            }
        }

        if (currentScreen == null && thePlayer != null)
        {
            if (thePlayer.getHealth() <= 0.0F)
            {
                displayGuiScreen(null);
            }
            else if (thePlayer.isPlayerSleeping() && theWorld != null && theWorld.isRemote)
            {
                displayGuiScreen(new GuiSleepMP());
            }
        }
        else if (currentScreen != null && (currentScreen instanceof GuiSleepMP) && !thePlayer.isPlayerSleeping())
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
            try
            {
                currentScreen.handleInput();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Updating screen events");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Affected screen");
                crashreportcategory.addCrashSectionCallable("Screen name", new CallableUpdatingScreenName(this));
                throw new ReportedException(crashreport);
            }

            if (currentScreen != null)
            {
                try
                {
                    currentScreen.updateScreen2();
                }
                catch (Throwable throwable1)
                {
                    CrashReport crashreport1 = CrashReport.makeCrashReport(throwable1, "Ticking screen");
                    CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Affected screen");
                    crashreportcategory1.addCrashSectionCallable("Screen name", new CallableParticleScreenName(this));
                    throw new ReportedException(crashreport1);
                }
            }
        }

        if (currentScreen == null || currentScreen.allowUserInput)
        {
            mcProfiler.endStartSection("mouse");

            do
            {
                if (!Mouse.next())
                {
                    break;
                }

                int i = Mouse.getEventButton();

                if (isRunningOnMac && i == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)))
                {
                    i = 1;
                }

                KeyBinding.setKeyBindState(i - 100, Mouse.getEventButtonState());

                if (Mouse.getEventButtonState())
                {
                    KeyBinding.onTick(i - 100);
                }

                long l = getSystemTime() - systemTime;

                if (l <= 200L)
                {
                    int i1 = Mouse.getEventDWheel();

                    if (i1 != 0)
                    {
                        thePlayer.inventory.changeCurrentItem(i1);

                        if (gameSettings.noclip)
                        {
                            if (i1 > 0)
                            {
                                i1 = 1;
                            }

                            if (i1 < 0)
                            {
                                i1 = -1;
                            }

                            gameSettings.noclipRate += (float)i1 * 0.25F;
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

            mcProfiler.endStartSection("keyboard");

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

                if (field_83002_am > 0L)
                {
                    if (getSystemTime() - field_83002_am >= 6000L)
                    {
                        throw new ReportedException(new CrashReport("Manually triggered debug crash", new Throwable()));
                    }

                    if (!Keyboard.isKeyDown(46) || !Keyboard.isKeyDown(61))
                    {
                        field_83002_am = -1L;
                    }
                }
                else if (Keyboard.isKeyDown(46) && Keyboard.isKeyDown(61))
                {
                    field_83002_am = getSystemTime();
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
                                refreshResources();
                            }

                            if (Keyboard.getEventKey() == 20 && Keyboard.isKeyDown(61))
                            {
                                refreshResources();
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

                            if (Keyboard.getEventKey() == 35 && Keyboard.isKeyDown(61))
                            {
                                gameSettings.advancedItemTooltips = !gameSettings.advancedItemTooltips;
                                gameSettings.saveOptions();
                            }

                            if (Keyboard.getEventKey() == 48 && Keyboard.isKeyDown(61))
                            {
                                RenderManager.field_85095_o = !RenderManager.field_85095_o;
                            }

                            if (Keyboard.getEventKey() == 25 && Keyboard.isKeyDown(61))
                            {
                                gameSettings.pauseOnLostFocus = !gameSettings.pauseOnLostFocus;
                                gameSettings.saveOptions();
                            }

                            if (Keyboard.getEventKey() == 59 && !oldHideGui)
                            {
                                gameSettings.hideGUI = !gameSettings.hideGUI;
                            }

                            if (Keyboard.getEventKey() == 61)
                            {
                                gameSettings.showDebugInfo = !gameSettings.showDebugInfo;
                                gameSettings.showDebugProfilerChart = GuiScreen.isShiftKeyDown();
                            }

                            if (Keyboard.getEventKey() == 63 && thirdperson)
                            {
                                if (oldthirdperson){
                                    gameSettings.thirdPersonView = gameSettings.thirdPersonView > 0 ? 0 : 1;
                                }else{
                                    gameSettings.thirdPersonView++;

                                    if (gameSettings.thirdPersonView > 2)
                                    {
                                        gameSettings.thirdPersonView = 0;
                                    }
                                }
                            }

                            if (Keyboard.getEventKey() == 66)
                            {
                                gameSettings.smoothCamera = !gameSettings.smoothCamera;
                            }
                        }

                        for (int j = 0; j < 9; j++)
                        {
                            if (Keyboard.getEventKey() == 2 + j)
                            {
                                thePlayer.inventory.currentItem = j;
                            }
                        }

                        if (gameSettings.showDebugInfo && gameSettings.showDebugProfilerChart)
                        {
                            if (Keyboard.getEventKey() == 11)
                            {
                                updateDebugProfilerName(0);
                            }

                            int k = 0;

                            while (k < 9)
                            {
                                if (Keyboard.getEventKey() == 2 + k)
                                {
                                    updateDebugProfilerName(k + 1);
                                }

                                k++;
                            }
                        }
                    }
                }
            }
            while (true);

            boolean flag1 = gameSettings.chatVisibility != 2;

            while (gameSettings.keyBindInventory.isPressed())
            {
                if (playerController.func_110738_j())
                {
                    thePlayer.func_110322_i();
                }
                else
                {
                    displayGuiScreen(new GuiInventory(thePlayer));
                }
            }

            for (; gameSettings.keyBindDrop.isPressed(); thePlayer.dropOneItem(GuiScreen.isCtrlKeyDown())) { }

            for (; gameSettings.keyBindChat.isPressed() && flag1; displayGuiScreen(new GuiChat())) { }

            if (currentScreen == null && gameSettings.keyBindCommand.isPressed() && flag1)
            {
                displayGuiScreen(new GuiChat("/"));
            }

            if (thePlayer.isUsingItem())
            {
                if (!gameSettings.keyBindUseItem.pressed)
                {
                    playerController.onStoppedUsingItem(thePlayer);
                }

                while (gameSettings.keyBindAttack.isPressed())
                {
                    ;
                }

                while (gameSettings.keyBindUseItem.isPressed())
                {
                    ;
                }

                while (gameSettings.keyBindPickBlock.isPressed())
                {
                    ;
                }
            }
            else
            {
                for (; gameSettings.keyBindAttack.isPressed(); clickMouse(0)) { }

                for (; gameSettings.keyBindUseItem.isPressed(); clickMouse(1)) { }

                for (; gameSettings.keyBindPickBlock.isPressed(); clickMiddleMouseButton()) { }
            }

            if (gameSettings.keyBindAttack.pressed && oldswing && (float)(ticksRan - mouseTicksRan) >= 10F){
                clickMouse(0);
                mouseTicksRan = ticksRan;
            }
            if (gameSettings.keyBindUseItem.pressed && rightClickDelayTimer == 0 && !thePlayer.isUsingItem())
            {
                clickMouse(1);
            }

            sendClickBlockToController(0, currentScreen == null && gameSettings.keyBindAttack.pressed && inGameHasFocus);
        }

        if (theWorld != null)
        {
            if (timecontrol && enableSP && currentScreen == null && (!isom || !GuiScreen.isShiftKeyDown())){
                if(Keyboard.isKeyDown(64)){
                    ((WorldSSP)theWorld).field_35465_L -= 0.001D;
                }
                if(Keyboard.isKeyDown(65)){
                    ((WorldSSP)theWorld).field_35465_L += 0.001D;
                }
            }

            if (oldHideGui && currentScreen == null)
            {
                gameSettings.hideGUI = Keyboard.isKeyDown(59);
                if (gameSettings.thirdPersonView > 0){
                    gameSettings.thirdPersonView = Keyboard.isKeyDown(59) ? 2 : 1;
                }
            }
            if (thePlayer != null)
            {
                joinPlayerCounter++;

                if (joinPlayerCounter == 30)
                {
                    joinPlayerCounter = 0;
                    theWorld.joinEntityInSurroundings(thePlayer);
                }
            }

            if (enableSP){
                if (theWorld.getWorldInfo().isHardcoreModeEnabled())
                {
                    theWorld.difficultySetting = 3;
                }
                else
                {
                    if (forcedDifficulty < 0){
                        theWorld.difficultySetting = gameSettings.difficulty;
                    }else{
                        theWorld.difficultySetting = forcedDifficulty;
                    }
                }

                if (theWorld.isRemote)
                {
                    theWorld.difficultySetting = 1;
                }
            }

            mcProfiler.endStartSection("gameRenderer");

            if (!isGamePaused)
            {
                entityRenderer.updateRenderer();
            }

            mcProfiler.endStartSection("levelRenderer");

            if (!isGamePaused)
            {
                renderGlobal.updateClouds();
            }

            mcProfiler.endStartSection("level");

            if (!isGamePaused)
            {
                if (theWorld.lastLightningBolt > 0)
                {
                    theWorld.lastLightningBolt--;
                }

                theWorld.updateEntities();
            }

            if (!isGamePaused)
            {
                if (!overrideMobSpawning){
                    theWorld.setAllowedSpawnTypes(theWorld.difficultySetting > 0, true);
                }

                try
                {
                    theWorld.tick();
                }
                catch (Throwable throwable2)
                {
                    CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Exception in world tick");

                    if (theWorld == null)
                    {
                        CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Affected level");
                        crashreportcategory2.addCrashSection("Problem", "Level is null!");
                    }
                    else
                    {
                        theWorld.addWorldInfoToCrashReport(crashreport2);
                    }

                    throw new ReportedException(crashreport2);
                }
            }

            mcProfiler.endStartSection("animateTick");

            if (!isGamePaused && theWorld != null)
            {
                theWorld.doVoidFogParticles(MathHelper.floor_double(thePlayer.posX), MathHelper.floor_double(thePlayer.posY), MathHelper.floor_double(thePlayer.posZ));
            }

            mcProfiler.endStartSection("particles");

            if (!isGamePaused)
            {
                effectRenderer.updateEffects();
            }
        }
        else if (myNetworkManager != null)
        {
            mcProfiler.endStartSection("pendingConnection");
            myNetworkManager.processReadPackets();
        }

        mcProfiler.endSection();
        systemTime = getSystemTime();
    }

    /**
     * Arguments: World foldername,  World ingame name, WorldSettings
     */
    public void launchIntegratedServer(String par1Str, String par2Str, WorldSettings par3WorldSettings)
    {
        loadWorld(null);
        System.gc();
        ISaveHandler isavehandler = saveLoader.getSaveLoader(par1Str, false);
        WorldInfo worldinfo = isavehandler.loadWorldInfo();

        if (worldinfo == null && par3WorldSettings != null)
        {
            worldinfo = new WorldInfo(par3WorldSettings, par1Str);
            isavehandler.saveWorldInfo(worldinfo);
        }

        if (par3WorldSettings == null)
        {
            par3WorldSettings = new WorldSettings(worldinfo);
        }

        statFileWriter.readStat(StatList.startGameStat, 1);
        theIntegratedServer = new IntegratedServer(this, par1Str, par2Str, par3WorldSettings);
        theIntegratedServer.startServerThread();
        integratedServerIsRunning = true;
        loadingScreen.displayProgressMessage(I18n.getString("menu.loadingLevel"));

        while (!theIntegratedServer.serverIsInRunLoop())
        {
            String s = theIntegratedServer.getUserMessage();

            if (s != null)
            {
                loadingScreen.resetProgresAndWorkingMessage(I18n.getString(s));
            }
            else
            {
                loadingScreen.resetProgresAndWorkingMessage("");
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
            NetClientHandler netclienthandler = new NetClientHandler(this, theIntegratedServer);
            myNetworkManager = netclienthandler.getNetManager();
        }
        catch (IOException ioexception)
        {
            displayCrashReport(addGraphicsAndWorldToCrashReport(new CrashReport("Connecting to integrated server", ioexception)));
        }
        lastWorld = par1Str;
    }

    /**
     * unloads the current world first
     */
    public void loadWorld(WorldClient par1WorldClient)
    {
        loadWorld(par1WorldClient, "");
    }

    /**
     * par2Str is displayed on the loading screen to the user unloads the current world first
     */
    public void loadWorld(WorldClient par1WorldClient, String par2Str)
    {
        statFileWriter.syncStats();

        if (par1WorldClient == null)
        {
            NetClientHandler netclienthandler = getNetHandler();

            if (netclienthandler != null)
            {
                netclienthandler.cleanup();
            }

            if (myNetworkManager != null)
            {
                myNetworkManager.closeConnections();
            }

            if (theIntegratedServer != null)
            {
                theIntegratedServer.initiateShutdown();
            }

            theIntegratedServer = null;
            for (int i = 0; i < mods.size(); i++){
                Mod mod = mods.get(i);
                mod.canUsePackets = false;
            }
        }

        renderViewEntity = null;
        myNetworkManager = null;

        if (loadingScreen != null)
        {
            loadingScreen.resetProgressAndMessage(par2Str);
            loadingScreen.resetProgresAndWorkingMessage("");
        }

        if (par1WorldClient == null && theWorld != null)
        {
            setServerData(null);
            integratedServerIsRunning = false;
        }

        sndManager.playStreaming(null, 0.0F, 0.0F, 0.0F);
        sndManager.stopAllSounds();
        theWorld = par1WorldClient;

        if (par1WorldClient != null)
        {
            if (renderGlobal != null)
            {
                renderGlobal.setWorldAndLoadRenderers(par1WorldClient);
            }

            if (effectRenderer != null)
            {
                effectRenderer.clearEffects(par1WorldClient);
            }

            if (thePlayer == null)
            {
                thePlayer = playerController.func_78754_a(par1WorldClient);
                playerController.flipPlayer(thePlayer);
            }

            thePlayer.preparePlayerToSpawn();
            par1WorldClient.spawnEntityInWorld(thePlayer);
            thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
            onInitPlayer();
            playerController.setPlayerCapabilities(thePlayer);
            renderViewEntity = thePlayer;
        }
        else
        {
            saveLoader.flushCache();
            thePlayer = null;
        }

        System.gc();
        systemTime = 0L;
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
        return theWorld.getProviderName();
    }

    /**
     * A String of how many entities are in the world
     */
    public String debugInfoEntities()
    {
        return (new StringBuilder()).append("P: ").append(effectRenderer.getStatistics()).append(". T: ").append(theWorld.getDebugLoadedEntities()).toString();
    }

    public void setDimensionAndSpawnPlayer(int par1)
    {
        theWorld.setSpawnLocation();
        if (!enableSP){
            theWorld.removeAllEntities();
        }
        int i = 0;
        String s = null;

        if (thePlayer != null)
        {
            i = thePlayer.entityId;
            theWorld.removeEntity(thePlayer);
            s = thePlayer.func_142021_k();
        }

        renderViewEntity = null;
        thePlayer = playerController.func_78754_a(theWorld);
        thePlayer.dimension = par1;
        renderViewEntity = thePlayer;
        thePlayer.preparePlayerToSpawn();
        thePlayer.func_142020_c(s);
        theWorld.spawnEntityInWorld(thePlayer);
        playerController.flipPlayer(thePlayer);
        thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
        onInitPlayer();
        thePlayer.entityId = i;
        playerController.setPlayerCapabilities(thePlayer);

        if (currentScreen instanceof GuiGameOver)
        {
            displayGuiScreen(null);
        }
    }

    /**
     * Gets whether this is a demo or not.
     */
    public final boolean isDemo()
    {
        return isDemo;
    }

    /**
     * Returns the NetClientHandler.
     */
    public NetClientHandler getNetHandler()
    {
        if (thePlayer != null)
        {
            return thePlayer.sendQueue;
        }
        else
        {
            return null;
        }
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
        return theMinecraft != null && theMinecraft.gameSettings.ambientOcclusion != 0;
    }

    /**
     * Returns true if the message is a client command and should not be sent to the server. However there are no such
     * commands at this point in time.
     */
    public boolean handleClientCommand(String par1Str)
    {
        return false;
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

        boolean flag = thePlayer.capabilities.isCreativeMode;
        int j = 0;
        boolean flag1 = false;
        int i;

        if (objectMouseOver.typeOfHit == EnumMovingObjectType.TILE)
        {
            int k = objectMouseOver.blockX;
            int i1 = objectMouseOver.blockY;
            int j1 = objectMouseOver.blockZ;
            Block block = Block.blocksList[theWorld.getBlockId(k, i1, j1)];

            if (block == null)
            {
                return;
            }

            i = block.idPicked(theWorld, k, i1, j1);

            if (i == 0)
            {
                return;
            }

            flag1 = Item.itemsList[i].getHasSubtypes();
            int k1 = i < 256 && !Block.blocksList[block.blockID].isFlowerPot() ? i : block.blockID;
            j = Block.blocksList[k1].getDamageValue(theWorld, k, i1, j1);
        }
        else if (objectMouseOver.typeOfHit == EnumMovingObjectType.ENTITY && objectMouseOver.entityHit != null && flag)
        {
            if (objectMouseOver.entityHit instanceof EntityPainting)
            {
                i = Item.painting.itemID;
            }
            else if (objectMouseOver.entityHit instanceof EntityLeashKnot)
            {
                i = Item.leash.itemID;
            }
            else if (objectMouseOver.entityHit instanceof EntityItemFrame)
            {
                EntityItemFrame entityitemframe = (EntityItemFrame)objectMouseOver.entityHit;

                if (entityitemframe.getDisplayedItem() == null)
                {
                    i = Item.itemFrame.itemID;
                }
                else
                {
                    i = entityitemframe.getDisplayedItem().itemID;
                    j = entityitemframe.getDisplayedItem().getItemDamage();
                    flag1 = true;
                }
            }
            else if (objectMouseOver.entityHit instanceof EntityMinecart)
            {
                EntityMinecart entityminecart = (EntityMinecart)objectMouseOver.entityHit;

                if (entityminecart.getMinecartType() == 2)
                {
                    i = Item.minecartPowered.itemID;
                }
                else if (entityminecart.getMinecartType() == 1)
                {
                    i = Item.minecartCrate.itemID;
                }
                else if (entityminecart.getMinecartType() == 3)
                {
                    i = Item.minecartTnt.itemID;
                }
                else if (entityminecart.getMinecartType() == 5)
                {
                    i = Item.minecartHopper.itemID;
                }
                else
                {
                    i = Item.minecartEmpty.itemID;
                }
            }
            else if (objectMouseOver.entityHit instanceof EntityBoat)
            {
                i = Item.boat.itemID;
            }
            else
            {
                i = Item.monsterPlacer.itemID;
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

        thePlayer.inventory.setCurrentItem(i, j, flag1, flag);

        if (flag)
        {
            int l = (thePlayer.inventoryContainer.inventorySlots.size() - 9) + thePlayer.inventory.currentItem;
            playerController.sendSlotPacket(thePlayer.inventory.getStackInSlot(thePlayer.inventory.currentItem), l);
        }
    }

    /**
     * adds core server Info (GL version , Texture pack, isModded, type), and the worldInfo to the crash report
     */
    public CrashReport addGraphicsAndWorldToCrashReport(CrashReport par1CrashReport)
    {
        par1CrashReport.getCategory().addCrashSectionCallable("Launched Version", new CallableLaunchedVersion(this));
        par1CrashReport.getCategory().addCrashSectionCallable("LWJGL", new CallableLWJGLVersion(this));
        par1CrashReport.getCategory().addCrashSectionCallable("OpenGL", new CallableGLInfo(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Is Modded", new CallableModded(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Type", new CallableType2(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Resource Pack", new CallableTexturePack(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Current Language", new CallableClientProfiler(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Profiler Position", new CallableClientMemoryStats(this));
        par1CrashReport.getCategory().addCrashSectionCallable("Vec3 Pool Size", new MinecraftINNER13(this));

        if (theWorld != null)
        {
            theWorld.addWorldInfoToCrashReport(par1CrashReport);
        }

        return par1CrashReport;
    }

    /**
     * Return the singleton Minecraft instance for the game
     */
    public static Minecraft getMinecraft()
    {
        return theMinecraft;
    }

    public void addServerStatsToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("fps", Integer.valueOf(debugFPS));
        par1PlayerUsageSnooper.addData("texpack_name", mcResourcePackRepository.getResourcePackName());
        par1PlayerUsageSnooper.addData("vsync_enabled", Boolean.valueOf(gameSettings.enableVsync));
        par1PlayerUsageSnooper.addData("display_frequency", Integer.valueOf(Display.getDisplayMode().getFrequency()));
        par1PlayerUsageSnooper.addData("display_type", fullscreen ? "fullscreen" : "windowed");
        par1PlayerUsageSnooper.addData("run_time", Long.valueOf(((MinecraftServer.getSystemTimeMillis() - par1PlayerUsageSnooper.func_130105_g()) / 60L) * 1000L));

        if (theIntegratedServer != null && theIntegratedServer.getPlayerUsageSnooper() != null)
        {
            par1PlayerUsageSnooper.addData("snooper_partner", theIntegratedServer.getPlayerUsageSnooper().getUniqueID());
        }
    }

    public void addServerTypeToSnooper(PlayerUsageSnooper par1PlayerUsageSnooper)
    {
        par1PlayerUsageSnooper.addData("opengl_version", GL11.glGetString(GL11.GL_VERSION));
        par1PlayerUsageSnooper.addData("opengl_vendor", GL11.glGetString(GL11.GL_VENDOR));
        par1PlayerUsageSnooper.addData("client_brand", ClientBrandRetriever.getClientModName());
        par1PlayerUsageSnooper.addData("launched_version", launchedVersion);
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
        par1PlayerUsageSnooper.addData("gl_caps[gl_max_vertex_uniforms]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_VERTEX_UNIFORM_COMPONENTS)));
        par1PlayerUsageSnooper.addData("gl_caps[gl_max_fragment_uniforms]", Integer.valueOf(GL11.glGetInteger(GL20.GL_MAX_FRAGMENT_UNIFORM_COMPONENTS)));
        par1PlayerUsageSnooper.addData("gl_max_texture_size", Integer.valueOf(getGLMaximumTextureSize()));
    }

    /**
     * Used in the usage snooper.
     */
    public static int getGLMaximumTextureSize()
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

    /**
     * Returns whether snooping is enabled or not.
     */
    public boolean isSnooperEnabled()
    {
        return gameSettings.snooperEnabled;
    }

    /**
     * Set the current ServerData instance.
     */
    public void setServerData(ServerData par1ServerData)
    {
        currentServerData = par1ServerData;
    }

    public boolean isIntegratedServerRunning()
    {
        return integratedServerIsRunning;
    }

    /**
     * Returns true if there is only one player playing, and the current server is the integrated one.
     */
    public boolean isSingleplayer()
    {
        return integratedServerIsRunning && theIntegratedServer != null;
    }

    /**
     * Returns the currently running integrated server
     */
    public IntegratedServer getIntegratedServer()
    {
        return theIntegratedServer;
    }

    public static void stopIntegratedServer()
    {
        if (theMinecraft == null)
        {
            return;
        }

        IntegratedServer integratedserver = theMinecraft.getIntegratedServer();

        if (integratedserver != null)
        {
            integratedserver.stopServer();
        }
    }

    /**
     * Returns the PlayerUsageSnooper instance.
     */
    public PlayerUsageSnooper getPlayerUsageSnooper()
    {
        return usageSnooper;
    }

    /**
     * Gets the system time in milliseconds.
     */
    public static long getSystemTime()
    {
        return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
    }

    /**
     * Returns whether we're in full screen or not.
     */
    public boolean isFullScreen()
    {
        return fullscreen;
    }

    public ILogAgent getLogAgent()
    {
        return mcLogAgent;
    }

    public Session getSession()
    {
        return session;
    }

    public Proxy getProxy()
    {
        return proxy;
    }

    public TextureManager getTextureManager()
    {
        return renderEngine;
    }

    public ResourceManager getResourceManager()
    {
        return mcResourceManager;
    }

    public ResourcePackRepository getResourcePackRepository()
    {
        return mcResourcePackRepository;
    }

    public LanguageManager getLanguageManager()
    {
        return mcLanguageManager;
    }

    static String getLaunchedVersion(Minecraft par0Minecraft)
    {
        return par0Minecraft.launchedVersion;
    }

    static LanguageManager func_142024_b(Minecraft par0Minecraft)
    {
        return par0Minecraft.mcLanguageManager;
    }

    static
    {
        isRunningOnMac = Util.getOSType() == EnumOS.MACOS;
    }

    /**
     * creates a new world or loads an existing one
     */
    public void startWorldSSP(String par1Str, String par2Str, WorldSettings par3WorldSettings)
    {
        changeWorld1(null);
        System.gc();
        WorldSettings par3WorldSettings2 = null;
        if (par3WorldSettings == null){
            ISaveHandler isavehandler = saveLoader.getSaveLoader(par1Str, false);
            par3WorldSettings2 = new WorldSettings(isavehandler.loadWorldInfo());
        }else{
            par3WorldSettings2 = par3WorldSettings;
        }
        theIntegratedServer = new FakeServer(this, par1Str, par2Str, par3WorldSettings2);
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
                loadingScreen.resetProgressAndMessage(StatCollector.translateToLocal("menu.switchingLevel"));
                loadingScreen.resetProgresAndWorkingMessage("");
            }

            ISaveHandler isavehandler = saveLoader.getSaveLoader(par1Str, false);
            WorldSSP world = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{ISaveHandler.class, String.class, WorldSettings.class, Profiler.class, ILogAgent.class}).
                           newInstance(new Object[]{isavehandler, par2Str, par3WorldSettings, mcProfiler, getLogAgent()});
                world = (WorldSSP)o;
//                world = new WorldSSP(isavehandler, par2Str, par3WorldSettings, mcProfiler, getLogAgent());
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
        theIntegratedServer.setConfigurationManager(new FakeServerPlayerList((FakeServer)theIntegratedServer));
        ((FakeServer)theIntegratedServer).setCommandManager(new ClientCommandManager());
    }

    /**
     * Unloads the current world, and displays a String while waiting
     */
    public void exitToMainMenu(String par1Str)
    {
        theWorld = null;
        theIntegratedServer = null;
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
                invokeModMethod("ModLoader", "clientConnect", new Class[]{NetClientHandler.class, Packet1Login.class}, getNetHandler(), null);
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
            loadingScreen.resetProgressAndMessage(par2Str);
            loadingScreen.resetProgresAndWorkingMessage("");
        }

        sndManager.playStreaming(null, 0.0F, 0.0F, 0.0F);

        if (theWorld != null)
        {
            ((WorldSSP)theWorld).saveWorldIndirectly(loadingScreen);
        }

        theWorld = par1World;

        if (par1World != null)
        {
            if (!isMultiplayerWorld())
            {
                if (par3EntityPlayer == null)
                {
                    thePlayer = (EntityPlayerSP2)par1World.func_4085_a(EntityPlayerSP2.class);
                }
            }
            else if (thePlayer != null)
            {
                thePlayer.preparePlayerToSpawn();

                if (par1World != null)
                {
                    par1World.spawnEntityInWorld(thePlayer);
                }
            }

            if (!par1World.isRemote)
            {
                preloadWorld(par2Str);
            }

            if (thePlayer == null)
            {
                thePlayer = playerController.func_78754_a(par1World);
                thePlayer.preparePlayerToSpawn();
                playerController.flipPlayer(thePlayer);
            }

            thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
            onInitPlayer();

            if (renderGlobal != null)
            {
                renderGlobal.setWorldAndLoadRenderers(par1World);
            }

            if (effectRenderer != null)
            {
                effectRenderer.clearEffects(par1World);
            }

            IChunkProvider ichunkprovider = par1World.getChunkProvider();

            if (ichunkprovider instanceof ChunkProviderLoadOrGenerate)
            {
                ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
                int i = MathHelper.floor_float((int)thePlayer.posX) >> 4;
                int j = MathHelper.floor_float((int)thePlayer.posZ) >> 4;
                chunkproviderloadorgenerate.setCurrentChunkOver(i, j);
            }

            par1World.spawnPlayerWithLoadedChunks(thePlayer);
            playerController.setPlayerCapabilities(thePlayer);

            if (par1World.isNewWorld)
            {
                ((WorldSSP)par1World).saveWorldIndirectly(loadingScreen);
            }

            renderViewEntity = thePlayer;
        }
        else
        {
            saveLoader.flushCache();
            thePlayer = null;
            forcedDifficulty = -1;
        }

        System.gc();
        systemTime = 0L;
    }

    /**
     * Converts from old map format to new map format
     */
    private void convertMapFormat(String par1Str, String par2Str)
    {
        loadingScreen.resetProgressAndMessage((new StringBuilder()).append("Converting World to Anvil")/*.append(saveLoader.getFormatName())*/.toString());
        loadingScreen.resetProgresAndWorkingMessage("This may take a while :)");
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
            loadingScreen.resetProgressAndMessage(par1Str);
            loadingScreen.resetProgresAndWorkingMessage(StatCollector.translateToLocal("menu.generatingTerrain"));
        }

        char c = '\200';

        if (((PlayerController)playerController).func_35643_e())
        {
            c = '@';
        }

        int i = 0;
        int j = (c * 2) / 16 + 1;
        j *= j;
        IChunkProvider ichunkprovider = theWorld.getChunkProvider();
        ChunkCoordinates chunkcoordinates = theWorld.getSpawnPoint();

        if (thePlayer != null)
        {
            chunkcoordinates.posX = (int)thePlayer.posX;
            chunkcoordinates.posZ = (int)thePlayer.posZ;
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

                theWorld.getBlockId(chunkcoordinates.posX + k, 64, chunkcoordinates.posZ + l);

                if (((PlayerController)playerController).func_35643_e())
                {
                    continue;
                }

                if (worldClass != WorldSSP.class){
                    while (((WorldSSP)theWorld).updatingLighting()) ;
                }
            }
        }

        if (!((PlayerController)playerController).func_35643_e())
        {
            if (loadingScreen != null)
            {
                loadingScreen.resetProgresAndWorkingMessage(StatCollector.translateToLocal("menu.simulating"));
            }

            char c1 = 2000;
            ((WorldSSP)theWorld).dropOldChunks();
        }
    }

    /**
     * Called when the respawn button is pressed after the player dies.
     */
    public void respawn(boolean par1, int par2, boolean par3)
    {
        if (!theWorld.isRemote && !theWorld.provider.canRespawnHere())
        {
            usePortal(0);
        }

        ChunkCoordinates chunkcoordinates = null;
        ChunkCoordinates chunkcoordinates1 = null;
        boolean flag = true;

        if (thePlayer != null && !par1)
        {
            chunkcoordinates = thePlayer.getBedLocation();

            if (chunkcoordinates != null)
            {
                chunkcoordinates1 = EntityPlayer.verifyRespawnCoordinates(theWorld, chunkcoordinates, thePlayer.isSpawnForced());

                if (chunkcoordinates1 == null)
                {
                    thePlayer.addChatMessage("tile.bed.notValid");
                }
            }
        }

        if (chunkcoordinates1 == null)
        {
            chunkcoordinates1 = theWorld.getSpawnPoint();
            flag = false;
        }

        IChunkProvider ichunkprovider = theWorld.getChunkProvider();

        if (ichunkprovider instanceof ChunkProviderLoadOrGenerate)
        {
            ChunkProviderLoadOrGenerate chunkproviderloadorgenerate = (ChunkProviderLoadOrGenerate)ichunkprovider;
            chunkproviderloadorgenerate.setCurrentChunkOver(chunkcoordinates1.posX >> 4, chunkcoordinates1.posZ >> 4);
        }

        theWorld.setSpawnLocation();
        ((WorldSSP)theWorld).updateEntityList();
        int i = 0;

        if (thePlayer != null)
        {
            i = thePlayer.entityId;
            theWorld.removeEntity(thePlayer);
        }

        EntityPlayerSP entityplayersp = thePlayer;
        renderViewEntity = null;
        thePlayer = playerController.func_78754_a(theWorld);

        thePlayer.clonePlayer(entityplayersp, par3);

        thePlayer.dimension = par2;
        renderViewEntity = thePlayer;
        thePlayer.preparePlayerToSpawn();

        if (flag)
        {
            thePlayer.setSpawnChunk(chunkcoordinates, false);
            thePlayer.setLocationAndAngles((float)chunkcoordinates1.posX + 0.5F, (float)chunkcoordinates1.posY + 0.1F, (float)chunkcoordinates1.posZ + 0.5F, 0.0F, 0.0F);
        }

        playerController.flipPlayer(thePlayer);
        ((WorldSSP)theWorld).spawnPlayerWithLoadedChunks(thePlayer);
        thePlayer.movementInput = new MovementInputFromOptions(gameSettings);
        onInitPlayer();
        thePlayer.entityId = i;
        playerController.setPlayerCapabilities(thePlayer);
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
        int i = thePlayer.dimension;
        thePlayer.dimension = par1;
        theWorld.removeEntity(thePlayer);
        thePlayer.isDead = false;
        double d = thePlayer.posX;
        double d1 = thePlayer.posZ;
        double d2 = 1.0D;

        if (i > -1 && thePlayer.dimension == -1)
        {
            d2 = 0.125D;
        }
        else if (i == -1 && thePlayer.dimension > -1)
        {
            d2 = 8D;
        }

        d *= d2;
        d1 *= d2;

        if (thePlayer.dimension == -1)
        {
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);

            if (thePlayer.isEntityAlive())
            {
                theWorld.updateEntityWithOptionalForce(thePlayer, false);
            }

            WorldSSP world = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{WorldSSP.class, WorldProvider.class, Profiler.class, ILogAgent.class}).
                           newInstance(new Object[]{(WorldSSP)theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension), mcProfiler, getLogAgent()});
                world = (WorldSSP)o;
//                world = new WorldSSP((WorldSSP)theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension), mcProfiler, getLogAgent());
            }catch(Exception ex){
                ex.printStackTrace();
            }
            changeWorld(world, "Entering the Nether", thePlayer);
        }
        else if (thePlayer.dimension == 0)
        {
            if (thePlayer.isEntityAlive())
            {
                thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
                theWorld.updateEntityWithOptionalForce(thePlayer, false);
            }

            WorldSSP world1 = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{WorldSSP.class, WorldProvider.class, Profiler.class, ILogAgent.class}).
                           newInstance(new Object[]{(WorldSSP)theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension), mcProfiler, getLogAgent()});
                world1 = (WorldSSP)o;
//                world1 = new WorldSSP((WorldSSP)theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension), mcProfiler, getLogAgent());
            }catch(Exception ex){
                ex.printStackTrace();
            }

            if (i == -1)
            {
                changeWorld(world1, "Leaving the Nether", thePlayer);
            }
            else
            {
                changeWorld(world1, "Leaving the End", thePlayer);
            }
        }
        else
        {
            WorldSSP world2 = null;
            try{
                Object o = worldClass.getDeclaredConstructor(new Class[]{WorldSSP.class, WorldProvider.class, Profiler.class, ILogAgent.class}).
                           newInstance(new Object[]{(WorldSSP)theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension), mcProfiler, getLogAgent()});
                world2 = (WorldSSP)o;
//                world2 = new WorldSSP((WorldSSP)theWorld, WorldProvider.getProviderForDimension(thePlayer.dimension), mcProfiler, getLogAgent());
            }catch(Exception ex){
                ex.printStackTrace();
            }
            ChunkCoordinates chunkcoordinates = world2.getEntrancePortalLocation();
            d = chunkcoordinates.posX;
            thePlayer.posY = chunkcoordinates.posY;
            d1 = chunkcoordinates.posZ;
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, 90F, 0.0F);

            if (thePlayer.isEntityAlive())
            {
                world2.updateEntityWithOptionalForce(thePlayer, false);
            }

            changeWorld(world2, "Entering the End", thePlayer);
        }

        thePlayer.worldObj = theWorld;
        System.out.println((new StringBuilder()).append("Teleported to ").append(theWorld.provider.dimensionId).toString());

        if (thePlayer.isEntityAlive() && i < 1)
        {
            thePlayer.setLocationAndAngles(d, thePlayer.posY, d1, thePlayer.rotationYaw, thePlayer.rotationPitch);
            theWorld.updateEntityWithOptionalForce(thePlayer, false);
            (new TeleporterSP()).placeInPortal(theWorld, thePlayer, d, thePlayer.posY, d1, thePlayer.rotationYaw);
        }
    }

    /**
     * Checks if the current world is a multiplayer world, returns true if it is, false otherwise.
     */
    public boolean isMultiplayerWorld()
    {
        return theWorld != null && theWorld.isRemote;
    }

    public void setController(EnumGameType enumgametype){
        if (enumgametype == EnumGameType.SURVIVAL){
            playerController = new PlayerControllerSP(this);
            return;
        }
        if (enumgametype == EnumGameType.CREATIVE){
            playerController = new PlayerControllerCreative(this);
            return;
        }
        if (enumgametype == EnumGameType.ADVENTURE){
            playerController = new PlayerControllerAdventure(this);
            return;
        }
    }

    public void setGameMode(EnumGameType enumgametype){
        if (enumgametype == EnumGameType.SURVIVAL){
            PlayerControllerCreative.disableAbilities(thePlayer);
            PlayerControllerAdventure.enableAbilities(thePlayer);
            return;
        }
        if (enumgametype == EnumGameType.CREATIVE){
            PlayerControllerCreative.enableAbilities(thePlayer);
            PlayerControllerAdventure.enableAbilities(thePlayer);
            return;
        }
        if (enumgametype == EnumGameType.ADVENTURE){
            PlayerControllerCreative.disableAbilities(thePlayer);
            PlayerControllerAdventure.disableAbilities(thePlayer);
            return;
        }
    }

    public void quitAndStartServer(){
        if ((getIntegratedServer() != null && !(getIntegratedServer() instanceof FakeServer)) || enableSP == false){
            return;
        }
        EnumGameType type = theWorld.getWorldInfo().getGameType();
        boolean commands = theWorld.getWorldInfo().areCommandsAllowed();
        WorldSSP world = (WorldSSP)theWorld;
        world.saveWorld(false, loadingScreen);
        changeWorld1(null);
        displayGuiScreen(null);
        enableSP = false;
        String s = lastWorld;
        String s1 = world.getWorldInfo().getWorldName();
        launchIntegratedServer(s, s1, null);
//         System.out.println(thePlayer.getEntityName());
//         thePlayer.sendGameTypeToPlayer(type);
    }

    public void switchSSP(boolean b){
        if (isMultiplayerWorld() && !isSingleplayer()){
            return;
        }
        WorldClient world = theWorld;
        GuiScreen gui = currentScreen;
        String s1 = "";
        if (enableSP){
            ((WorldSSP)world).saveWorld(false, loadingScreen);
            s1 = world.getWorldInfo().getWorldName();
            changeWorld1(null);
        }else{
            s1 = getIntegratedServer().getWorldName();
            theWorld.sendQuittingDisconnectingPacket();
            for (int i = 0; i < getIntegratedServer().worldServers.length; i++){
                WorldServer worldserver = getIntegratedServer().worldServers[i];
                if (worldserver != null){
                    worldserver.flush();
                    worldserver = null;
                }
            }
            loadWorld(null);
            stopIntegratedServer();
            theWorld = null;
        }
        theIntegratedServer = null;
        enableSP = b;
        String s = lastWorld;
        if (enableSP){
            setController(world.getWorldInfo().getGameType());
            startWorldSSP(s, s1, null);
        }else{
            launchIntegratedServer(s, s1, null);
        }
        displayGuiScreen(gui);
    }

    public void loadMods(){
        Class c = Mod.class;
        String p = "";
        try{
            p = c.getPackage().getName()+".";
        }catch(Exception ex){}
        if (p.endsWith("ssp.")){
            p = p.replace("ssp.", "");
        }
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        try{
            path = URLDecoder.decode(path, "UTF-8");
        }catch(Exception e){}
        File file = new File(path+p.replace(".", "/"));
        List classes = new ArrayList();
        if ((file.getName().endsWith(".zip") || file.getName().endsWith(".jar")) && !file.isDirectory()){
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
            if (mod instanceof ResourceManagerReloadListener){
                mcResourceManager.registerReloadListener((ResourceManagerReloadListener)mod);
            }
            System.out.println("Loaded "+mod.getModName()+" "+mod.getModVersion()+" for Minecraft "+mod.getMcVersion());
        }
    }

    public boolean renderBlocksMod(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
        for (Mod mod : mods){
            if (mod.renderBlocks(r, i, b, x, y, z, id, override)){
                return true;
            }
        }
        Object o = invokeModMethod("ModLoader", "renderWorldBlock",
                                   new Class[]{RenderBlocks.class, IBlockAccess.class, Integer.TYPE, Integer.TYPE, Integer.TYPE, Block.class, Integer.TYPE},
                                   r, i, x, y, z, b, id);
        if (o != null){
            return (Boolean)o;
        }
        return false;
    }

    public String getVersion(){
        return "1.6.4";
    }

    private void registerCustomPacket(){
        try{
            int id = 1;
            Method m = null;
            Method[] methods = (Packet.class).getDeclaredMethods();
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
            m.invoke(null, new Object[]{251, true, true, Packet300Custom.class});
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

    private Object invokeModMethod_do(String mod, String method, Class[] pars, Object... args){
        if (compat.get(mod) <= 0){
            return null;
        }
        try{
            Class c = Class.forName((compat.get(mod) > 1 ? "net.minecraft.src." : "")+mod);
            Method m = c.getDeclaredMethod(method, pars);
            Object o = m.invoke(null, args);
//            System.out.println("SSP: Invoking "+m.toString());
            return o;
        }catch(Exception ex){
            ex.printStackTrace();
            compat.put(mod, 0);
        }
        return null;
    }

    public static Object invokeModMethod(String mod, String method, Class[] pars, Object... args){
        return getMinecraft().invokeModMethod_do(mod, method, pars, args);
    }

    public void enableProfiling()
    {
        startProfiling = true;
        profilingEnabled = true;
    }

    public void disableProfiling()
    {
        mcProfiler.profilingEnabled = false;
        profilingEnabled = false;
    }

    public File getFile(String par1Str)
    {
        return new File(mcDataDir, par1Str);
    }

    public String[] getAllUsernames()
    {
        return new String[]{thePlayer.getEntityName()};
    }

    public void onLoginClient(){
        for (int i = 0; i < mods.size(); i++){
            Mod mod = mods.get(i);
            mod.onLoginClient();
        }
    }

    public void onLoginServer(EntityPlayerMP player){
        player.playerNetServerHandler.sendPacketToPlayer(new Packet300Custom(null, true, 0, ""));
        for (int i = 0; i < mods.size(); i++){
            Mod mod = mods.get(i);
            mod.onLoginServer(player);
        }
    }

    public void onInitClient(){
        for (int i = 0; i < mods.size(); i++){
            Mod mod = mods.get(i);
            mod.canUsePackets = true;
            mod.onInitClient();
        }
    }

    public void addCommandsSP(ClientCommandManager manager){
        for (Mod mod : mods){
            mod.addSPCommands(manager);
        }
    }

    private void onInitPlayer(){
        for (Mod mod : mods){
            mod.onInitPlayer(thePlayer, gameSettings);
        }
    }

    public File getAssetsDir(){
        return fileAssets;
    }

    public void setSoundManager(Class c){
        try{
            Object o = c.getDeclaredConstructor(ResourceManager.class, GameSettings.class, File.class).newInstance(mcResourceManager, gameSettings, fileAssets);
            sndManager = (SoundManager)o;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        mcResourceManager.registerReloadListener(sndManager);
    }

    private void setupOverlays(){
        new GuiGameOverOverlay();
        new GuiIngameMenuOverlay();
        new GuiMainMenuOverlay();
        new GuiMultiplayerOverlay();
        new GuiShareToLanOverlay();
    }
}
