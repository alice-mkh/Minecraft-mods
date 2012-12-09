package net.minecraft.src.nbxlite.gui;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.*;

public class GuiCreateWorld2 extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;

    /** hardcore', 'creative' or 'survival */
    private String gameMode;
    private boolean generateStructures;
    private boolean commandsAllowed;

    /** True iif player has clicked buttonAllowCommands at least once */
    private boolean commandsToggled;

    /** toggles when GUIButton 7 is pressed */
    private boolean bonusItems;

    /** True if and only if gameMode.equals("hardcore") */
    private boolean isHardcore;
    private boolean createClicked;

    /**
     * True if the extra options (Seed box, structure toggle button, world type button, etc.) are being shown
     */
    private boolean moreOptions;

    /** The GUIButton that you click to change game modes. */
    private GuiButton buttonGameMode;

    /**
     * The GUIButton that you click to get to options like the seed when creating a world.
     */
    private GuiButton moreWorldOptions;

    /** The GuiButton in the 'More World Options' screen. Toggles ON/OFF */
    private GuiButton buttonGenerateStructures;
    private GuiButton buttonBonusItems;

    /** The GuiButton in the more world options screen. */
    private GuiButton buttonWorldType;
    private GuiButton buttonAllowCommands;
    private GuiButton field_82289_B;

    /** The first line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine1;

    /** The second line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine2;

    /** The current textboxSeed text */
    private String seed;

    /** E.g. New World, Neue Welt, Nieuwe wereld, Neuvo Mundo */
    private String localizedNewWorldText;
    private int worldTypeId;
    public String field_82290_a;
    private static final String ILLEGAL_WORLD_NAMES[] =
    {
        "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4",
        "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5",
        "LPT6", "LPT7", "LPT8", "LPT9"
    };
    private GuiButton nbxliteButton;
    private GuiButton nbxliteButtonShort;

    public GuiCreateWorld2(GuiScreen par1GuiScreen)
    {
        gameMode = "survival";
        generateStructures = true;
        commandsAllowed = false;
        commandsToggled = false;
        bonusItems = false;
        isHardcore = false;
        worldTypeId = 0;
        field_82290_a = "";
        parentGuiScreen = par1GuiScreen;
        seed = "";
        localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
        setDefaultNBXliteSettings();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        textboxWorldName.updateCursorCounter();
        textboxSeed.updateCursorCounter();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, stringtranslate.translateKey("selectWorld.create")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        controlList.add(buttonGameMode = new GuiButton(2, width / 2 - 75, 115, 150, 20, stringtranslate.translateKey("selectWorld.gameMode")));
        controlList.add(moreWorldOptions = new GuiButton(3, width / 2 - 75, 187, 150, 20, stringtranslate.translateKey("selectWorld.moreWorldOptions")));
        controlList.add(buttonGenerateStructures = new GuiButton(4, width / 2 - 155, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        buttonGenerateStructures.drawButton = false;
        controlList.add(buttonBonusItems = new GuiButton(7, width / 2 + 5, 151, 150, 20, stringtranslate.translateKey("selectWorld.bonusItems")));
        buttonBonusItems.drawButton = false;
        controlList.add(buttonWorldType = new GuiButton(5, width / 2 + 5, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapType")));
        buttonWorldType.drawButton = false;
        controlList.add(buttonAllowCommands = new GuiButton(6, width / 2 - 155, 151, 150, 20, stringtranslate.translateKey("selectWorld.allowCommands")));
        buttonAllowCommands.drawButton = false;
        controlList.add(field_82289_B = new GuiButton(8, width / 2 + 5, 120, 150, 20, stringtranslate.translateKey("selectWorld.customizeType")));
        field_82289_B.drawButton = false;

        if (GeneratorList.genfeatures[GeneratorList.gencurrent] != 2 || GeneratorList.feat2worldtype[GeneratorList.feat2current] < 1){
            buttonWorldType.enabled = false;
            worldTypeId = 0;
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent] != 2 || GeneratorList.feat2worldtype[GeneratorList.feat2current] < 2){
            if (WorldType.worldTypes[worldTypeId] == WorldType.LARGE_BIOMES){
                worldTypeId = 0;
            }
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent] != 2 || GeneratorList.feat2worldtype[GeneratorList.feat2current] < 3){
            field_82290_a = "";
        }
        generateStructures = GeneratorList.genstructures[GeneratorList.gencurrent];

        textboxWorldName = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxWorldName.setFocused(true);
        textboxWorldName.setText(localizedNewWorldText);
        textboxSeed = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxSeed.setText(seed);
        controlList.add(nbxliteButton = new GuiButton(9, width / 2 - 155, 130, 310, 20, genNBXliteButtonName()));
        controlList.add(nbxliteButtonShort = new GuiButton(10, width / 2 - 155, 130, 150, 20, genNBXliteButtonName()));
        nbxliteButton.drawButton = false;
        nbxliteButtonShort.drawButton = false;
        func_82288_a(moreOptions);
        makeUseableName();
        updateButtonText();
    }

    public static void setDefaultNBXliteSettings(){
        GeneratorList.gendefault=ODNBXlite.DefaultGenerator;
        GeneratorList.feat1default=ODNBXlite.DefaultFeaturesBeta;
        GeneratorList.feat2default=ODNBXlite.DefaultFeaturesRelease;
        GeneratorList.themedefault=ODNBXlite.DefaultTheme;
        GeneratorList.typedefault=ODNBXlite.DefaultIndevType;
        GeneratorList.gencurrent = GeneratorList.gendefault;
        GeneratorList.themecurrent = GeneratorList.themedefault;
        GeneratorList.feat1current = GeneratorList.feat1default;
        GeneratorList.feat2current = GeneratorList.feat2default;
        GeneratorList.typecurrent = GeneratorList.typedefault;
        GeneratorList.xcurrent = GeneratorList.xdefault;
        GeneratorList.zcurrent = GeneratorList.zdefault;
        ODNBXlite.Generator = GeneratorList.genfeatures[GeneratorList.gendefault];
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS){
            ODNBXlite.MapFeatures = GeneratorList.genfeats[GeneratorList.gendefault];
        }else if (ODNBXlite.Generator==ODNBXlite.GEN_OLDBIOMES){
            ODNBXlite.MapFeatures = GeneratorList.feat1default;
        }else if (ODNBXlite.Generator==ODNBXlite.GEN_NEWBIOMES){
            ODNBXlite.MapFeatures = GeneratorList.feat2default;
        }
        ODNBXlite.MapTheme = GeneratorList.themedefault;
        ODNBXlite.IndevMapType = GeneratorList.typedefault;
        ODNBXlite.IndevWidthX = GeneratorList.sizes[GeneratorList.xdefault];
        ODNBXlite.IndevWidthZ = GeneratorList.sizes[GeneratorList.zdefault];
        ODNBXlite.IndevHeight = ODNBXlite.DefaultFiniteDepth+32;
        ODNBXlite.GenerateNewOres = ODNBXlite.DefaultNewOres;
        ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
        ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
        if(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (ODNBXlite.MapTheme==ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS) && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_ALPHA11201){
            ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
        }
    }

    public static String genNBXliteButtonName(){
        StringBuilder str = new StringBuilder();
        str.append(mod_OldDays.lang.get("settings"));
        str.append(": ");
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            str.append(mod_OldDays.lang.get(GeneratorList.genname[GeneratorList.gencurrent]));
            if (GeneratorList.genplus[GeneratorList.gencurrent]==0){
                str.append(", ");
            }
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2){
            str.append(" (");
            str.append(ODNBXlite.IndevWidthX);
            str.append("x");
            str.append(ODNBXlite.IndevWidthZ);
            if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
                str.append("x");
                str.append(ODNBXlite.IndevHeight-32);
            }
            str.append("), ");
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
            str.append(mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent]));
            str.append(", ");
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            str.append(mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]));
            if (GeneratorList.gencurrent == 4 && ODNBXlite.SnowCovered){
                str.append(" (");
                str.append(StringTranslate.getInstance().translateKey("tile.snow.name"));
                str.append(")");
            }
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
            str.append(mod_OldDays.lang.get(GeneratorList.feat1name[GeneratorList.feat1current]));
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
            str.append(mod_OldDays.lang.get("nbxlite.releasefeatures"+(GeneratorList.feat2current+1)));
        }
        return str.toString();
    }

    /**
     * Makes a the name for a world save folder based on your world name, replacing specific characters for _s and
     * appending -s to the end until a free name is available.
     */
    private void makeUseableName()
    {
        folderName = textboxWorldName.getText().trim();
        char ac[] = ChatAllowedCharacters.allowedCharactersArray;
        int i = ac.length;

        for (int j = 0; j < i; j++)
        {
            char c = ac[j];
            folderName = folderName.replace(c, '_');
        }

        if (MathHelper.stringNullOrLengthZero(folderName))
        {
            folderName = "World";
        }

        folderName = func_73913_a(mc.getSaveLoader(), folderName);
    }

    private void updateButtonText()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        this.buttonGameMode.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
        this.gameModeDescriptionLine1 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line1");
        this.gameModeDescriptionLine2 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line2");
        this.buttonGenerateStructures.displayString = var1.translateKey("selectWorld.mapFeatures") + " ";

        if (this.generateStructures)
        {
            this.buttonGenerateStructures.displayString = this.buttonGenerateStructures.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.buttonGenerateStructures.displayString = this.buttonGenerateStructures.displayString + var1.translateKey("options.off");
        }

        this.buttonBonusItems.displayString = var1.translateKey("selectWorld.bonusItems") + " ";

        if (this.bonusItems && !this.isHardcore)
        {
            this.buttonBonusItems.displayString = this.buttonBonusItems.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.buttonBonusItems.displayString = this.buttonBonusItems.displayString + var1.translateKey("options.off");
        }

        this.buttonWorldType.displayString = var1.translateKey("selectWorld.mapType") + " " + var1.translateKey(WorldType.worldTypes[this.worldTypeId].getTranslateName());
        this.buttonAllowCommands.displayString = var1.translateKey("selectWorld.allowCommands") + " ";

        if (this.commandsAllowed && !this.isHardcore)
        {
            this.buttonAllowCommands.displayString = this.buttonAllowCommands.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.buttonAllowCommands.displayString = this.buttonAllowCommands.displayString + var1.translateKey("options.off");
        }
    }

    public static String func_73913_a(ISaveFormat par0ISaveFormat, String par1Str)
    {
        par1Str = par1Str.replaceAll("[\\./\"]", "_");
        String as[] = ILLEGAL_WORLD_NAMES;
        int i = as.length;

        for (int j = 0; j < i; j++)
        {
            String s = as[j];

            if (par1Str.equalsIgnoreCase(s))
            {
                par1Str = (new StringBuilder()).append("_").append(par1Str).append("_").toString();
            }
        }

        for (; par0ISaveFormat.getWorldInfo(par1Str) != null; par1Str = (new StringBuilder()).append(par1Str).append("-").toString()) { }

        return par1Str;
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }

        if (par1GuiButton.id == 1)
        {
            mc.displayGuiScreen(parentGuiScreen);
        }
        else if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(null);

            if (createClicked)
            {
                return;
            }

            createClicked = true;
            long l = (new Random()).nextLong();
            String s = textboxSeed.getText();

            if (!MathHelper.stringNullOrLengthZero(s))
            {
                try
                {
                    long l1 = Long.parseLong(s);

                    if (l1 != 0L)
                    {
                        l = l1;
                    }
                }
                catch (NumberFormatException numberformatexception)
                {
                    l = s.hashCode();
                }
            }

            EnumGameType enumgametype = EnumGameType.getByName(gameMode);
            WorldSettings worldsettings = new WorldSettings(l, enumgametype, generateStructures, isHardcore, WorldType.worldTypes[worldTypeId]);
            worldsettings.func_82750_a(field_82290_a);

            if (bonusItems && !isHardcore)
            {
                worldsettings.enableBonusChest();
            }

            if (commandsAllowed && !isHardcore)
            {
                worldsettings.enableCommands();
            }

            mc.enableSP = mc.useSP;
            if (mc.enableSP){
                mc.setController(enumgametype);
                mc.startWorldSSP(folderName, textboxWorldName.getText().trim(), worldsettings);
                mc.displayGuiScreen(null);
            }else{
                mc.launchIntegratedServer(folderName, textboxWorldName.getText().trim(), worldsettings);
            }
        }
        else if (par1GuiButton.id == 3)
        {
            func_82287_i();
        }
        else if (par1GuiButton.id == 2)
        {
            if (gameMode.equals("survival"))
            {
                if (!commandsToggled)
                {
                    commandsAllowed = false;
                }

                isHardcore = false;
                gameMode = "hardcore";
                isHardcore = true;
                buttonAllowCommands.enabled = false;
                buttonBonusItems.enabled = false;
                updateButtonText();
            }
            else if (gameMode.equals("hardcore"))
            {
                if (!commandsToggled)
                {
                    commandsAllowed = true;
                }

                isHardcore = false;
                gameMode = "creative";
                updateButtonText();
                isHardcore = false;
                buttonAllowCommands.enabled = true;
                buttonBonusItems.enabled = true;
            }
            else
            {
                if (!commandsToggled)
                {
                    commandsAllowed = false;
                }

                gameMode = "survival";
                updateButtonText();
                buttonAllowCommands.enabled = true;
                buttonBonusItems.enabled = true;
                isHardcore = false;
            }

            updateButtonText();
        }
        else if (par1GuiButton.id == 4)
        {
            generateStructures = !generateStructures;
            updateButtonText();
        }
        else if (par1GuiButton.id == 7)
        {
            bonusItems = !bonusItems;
            updateButtonText();
        }
        else if (par1GuiButton.id == 5)
        {
            worldTypeId++;

            if (worldTypeId >= WorldType.worldTypes.length)
            {
                worldTypeId = 0;
            }

            do
            {
                if (WorldType.worldTypes[worldTypeId] != null && WorldType.worldTypes[worldTypeId].getCanBeCreated() && (GeneratorList.feat2worldtype[GeneratorList.feat2current] > 1 || WorldType.worldTypes[worldTypeId] != WorldType.LARGE_BIOMES))
                {
                    break;
                }

                worldTypeId++;

                if (worldTypeId >= WorldType.worldTypes.length)
                {
                    worldTypeId = 0;
                }
            }
            while (true);

            field_82290_a = "";
            updateButtonText();
            func_82288_a(moreOptions);
        }
        else if (par1GuiButton.id == 6)
        {
            commandsToggled = true;
            commandsAllowed = !commandsAllowed;
            updateButtonText();
        }
        else if (par1GuiButton.id == 8)
        {
             mc.displayGuiScreen(new GuiCreateFlatWorld2(this, field_82290_a));
        }
        else if (par1GuiButton.id == 9 || par1GuiButton.id == 10)
        {
             mc.displayGuiScreen(new GuiNBXlite(this));
             moreOptions = false;
        }
    }

    private void func_82287_i()
    {
        func_82288_a(!moreOptions);
    }

    private void func_82288_a(boolean par1)
    {
        moreOptions = par1;
        buttonGameMode.drawButton = !moreOptions;
        buttonGenerateStructures.drawButton = moreOptions;
        buttonBonusItems.drawButton = moreOptions;
        buttonWorldType.drawButton = moreOptions;
        buttonAllowCommands.drawButton = moreOptions;
        field_82289_B.drawButton = moreOptions && WorldType.worldTypes[worldTypeId] == WorldType.FLAT && GeneratorList.genfeatures[GeneratorList.gencurrent] == 2 && GeneratorList.feat2worldtype[GeneratorList.feat2current] >= 3;

        if (moreOptions)
        {
            StringTranslate stringtranslate = StringTranslate.getInstance();
            moreWorldOptions.displayString = stringtranslate.translateKey("gui.done");
        }
        else
        {
            StringTranslate stringtranslate1 = StringTranslate.getInstance();
            moreWorldOptions.displayString = stringtranslate1.translateKey("selectWorld.moreWorldOptions");
        }

        nbxliteButton.drawButton = moreOptions && ODNBXlite.ShowGUI && !field_82289_B.drawButton;
        nbxliteButtonShort.drawButton = moreOptions && ODNBXlite.ShowGUI && field_82289_B.drawButton;
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (textboxWorldName.isFocused() && !moreOptions)
        {
            textboxWorldName.textboxKeyTyped(par1, par2);
            localizedNewWorldText = textboxWorldName.getText();
        }
        else if (textboxSeed.isFocused() && moreOptions)
        {
            textboxSeed.textboxKeyTyped(par1, par2);
            seed = textboxSeed.getText();
        }

        if (par1 == '\r')
        {
            actionPerformed((GuiButton)controlList.get(0));
        }

        ((GuiButton)controlList.get(0)).enabled = textboxWorldName.getText().length() > 0;
        makeUseableName();
    }

    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int par1, int par2, int par3)
    {
        super.mouseClicked(par1, par2, par3);

        if (moreOptions)
        {
            textboxSeed.mouseClicked(par1, par2, par3);
        }
        else
        {
            textboxWorldName.mouseClicked(par1, par2, par3);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, stringtranslate.translateKey("selectWorld.create"), width / 2, 20, 0xffffff);

        if (moreOptions)
        {
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterSeed"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.seedInfo"), width / 2 - 100, 85, 0xa0a0a0);
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.mapFeatures.info"), width / 2 - 150, 122, 0xa0a0a0);
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.allowCommands.info"), width / 2 - 150, 172, 0xa0a0a0);
            textboxSeed.drawTextBox();
        }
        else
        {
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.resultFolder")).append(" ").append(folderName).toString(), width / 2 - 100, 85, 0xa0a0a0);
            textboxWorldName.drawTextBox();
            drawString(fontRenderer, gameModeDescriptionLine1, width / 2 - 100, 137, 0xa0a0a0);
            drawString(fontRenderer, gameModeDescriptionLine2, width / 2 - 100, 149, 0xa0a0a0);
        }

        super.drawScreen(par1, par2, par3);
    }

    public void func_82286_a(WorldInfo par1WorldInfo)
    {
        localizedNewWorldText = StatCollector.translateToLocalFormatted("selectWorld.newWorld.copyOf", new Object[]
                {
                    par1WorldInfo.getWorldName()
                });
        seed = (new StringBuilder()).append(par1WorldInfo.getSeed()).append("").toString();
        worldTypeId = par1WorldInfo.getTerrainType().func_82747_f();
        field_82290_a = par1WorldInfo.func_82571_y();
        generateStructures = par1WorldInfo.isMapFeaturesEnabled();
        commandsAllowed = par1WorldInfo.areCommandsAllowed();

        if (par1WorldInfo.isHardcoreModeEnabled())
        {
            gameMode = "hardcore";
        }
        else if (par1WorldInfo.getGameType().isSurvivalOrAdventure())
        {
            gameMode = "survival";
        }
        else if (par1WorldInfo.getGameType().isCreative())
        {
            gameMode = "creative";
        }
        if (par1WorldInfo.nbxlite){
            ODNBXlite.IndevWidthX = par1WorldInfo.indevX;
            ODNBXlite.IndevWidthZ = par1WorldInfo.indevZ;
            ODNBXlite.IndevHeight = par1WorldInfo.indevY;
            ODNBXlite.SurrWaterType = par1WorldInfo.surrwatertype;
            ODNBXlite.SurrWaterHeight = par1WorldInfo.surrwaterheight;
            ODNBXlite.SurrGroundType = par1WorldInfo.surrgroundtype;
            ODNBXlite.SurrGroundHeight = par1WorldInfo.surrgroundheight;
            ODNBXlite.CloudHeight = par1WorldInfo.cloudheight;
            ODNBXlite.SkyBrightness = par1WorldInfo.skybrightness;
            ODNBXlite.SkyColor = par1WorldInfo.skycolor;
            ODNBXlite.FogColor = par1WorldInfo.fogcolor;
            ODNBXlite.CloudColor = par1WorldInfo.cloudcolor;
            ODNBXlite.Generator = par1WorldInfo.mapGen;
            ODNBXlite.MapFeatures = par1WorldInfo.mapGenExtra;
            ODNBXlite.MapTheme = par1WorldInfo.mapTheme;
            ODNBXlite.IndevMapType = par1WorldInfo.mapType;
            ODNBXlite.SnowCovered = par1WorldInfo.snowCovered;
            ODNBXlite.GenerateNewOres = par1WorldInfo.newOres;
            if (ODNBXlite.Generator == ODNBXlite.GEN_OLDBIOMES){
                GeneratorList.gencurrent = 5;
                GeneratorList.feat1current = ODNBXlite.MapFeatures;
            }else if (ODNBXlite.Generator == ODNBXlite.GEN_NEWBIOMES){
                GeneratorList.gencurrent = 6;
                GeneratorList.feat2current = ODNBXlite.MapFeatures;
            }else{
                switch (ODNBXlite.MapFeatures){
                    case ODNBXlite.FEATURES_ALPHA11201: GeneratorList.gencurrent = 4; break;
                    case ODNBXlite.FEATURES_INFDEV0420: GeneratorList.gencurrent = 3; break;
                    case ODNBXlite.FEATURES_INFDEV0608: GeneratorList.gencurrent = 3; break;
                    case ODNBXlite.FEATURES_INFDEV0227: GeneratorList.gencurrent = 2; break;
                    case ODNBXlite.FEATURES_INDEV: GeneratorList.gencurrent = 1; break;
                    case ODNBXlite.FEATURES_CLASSIC: GeneratorList.gencurrent = 0; break;
                }
                GeneratorList.typecurrent = ODNBXlite.IndevMapType;
                GeneratorList.themecurrent = ODNBXlite.MapTheme;
            }
        }
    }
}
