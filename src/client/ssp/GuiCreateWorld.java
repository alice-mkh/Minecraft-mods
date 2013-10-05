package net.minecraft.src;

import java.util.List;
import java.util.Random;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen
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

    /** GuiButton in the more world options screen. */
    private GuiButton buttonCustomize;

    /** The first line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine1;

    /** The second line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine2;

    /** The current textboxSeed text */
    private String seed;

    /** E.g. New World, Neue Welt, Nieuwe wereld, Neuvo Mundo */
    private String localizedNewWorldText;
    private int worldTypeId;

    /** Generator options to use when creating the world. */
    public String generatorOptionsToUse;
    private static final String ILLEGAL_WORLD_NAMES[] =
    {
        "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4",
        "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5",
        "LPT6", "LPT7", "LPT8", "LPT9"
    };

    public GuiCreateWorld(GuiScreen par1GuiScreen)
    {
        gameMode = "survival";
        generateStructures = true;
        generatorOptionsToUse = "";
        parentGuiScreen = par1GuiScreen;
        seed = "";
        localizedNewWorldText = I18n.getString("selectWorld.newWorld");
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
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, I18n.getString("selectWorld.create")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, I18n.getString("gui.cancel")));
        buttonList.add(buttonGameMode = new GuiButton(2, width / 2 - 75, 115, 150, 20, I18n.getString("selectWorld.gameMode")));
        buttonList.add(moreWorldOptions = new GuiButton(3, width / 2 - 75, 187, 150, 20, I18n.getString("selectWorld.moreWorldOptions")));
        buttonList.add(buttonGenerateStructures = new GuiButton(4, width / 2 - 155, 100, 150, 20, I18n.getString("selectWorld.mapFeatures")));
        buttonGenerateStructures.drawButton = false;
        buttonList.add(buttonBonusItems = new GuiButton(7, width / 2 + 5, 151, 150, 20, I18n.getString("selectWorld.bonusItems")));
        buttonBonusItems.drawButton = false;
        buttonList.add(buttonWorldType = new GuiButton(5, width / 2 + 5, 100, 150, 20, I18n.getString("selectWorld.mapType")));
        buttonWorldType.drawButton = false;
        buttonList.add(buttonAllowCommands = new GuiButton(6, width / 2 - 155, 151, 150, 20, I18n.getString("selectWorld.allowCommands")));
        buttonAllowCommands.drawButton = false;
        buttonList.add(buttonCustomize = new GuiButton(8, width / 2 + 5, 120, 150, 20, I18n.getString("selectWorld.customizeType")));
        buttonCustomize.drawButton = false;
        textboxWorldName = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxWorldName.setFocused(true);
        textboxWorldName.setText(localizedNewWorldText);
        textboxSeed = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxSeed.setText(seed);
        func_82288_a(moreOptions);
        makeUseableName();
        updateButtonText();
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
        this.buttonGameMode.displayString = I18n.getString("selectWorld.gameMode") + " " + I18n.getString("selectWorld.gameMode." + this.gameMode);
        this.gameModeDescriptionLine1 = I18n.getString("selectWorld.gameMode." + this.gameMode + ".line1");
        this.gameModeDescriptionLine2 = I18n.getString("selectWorld.gameMode." + this.gameMode + ".line2");
        this.buttonGenerateStructures.displayString = I18n.getString("selectWorld.mapFeatures") + " ";

        if (this.generateStructures)
        {
            this.buttonGenerateStructures.displayString = this.buttonGenerateStructures.displayString + I18n.getString("options.on");
        }
        else
        {
            this.buttonGenerateStructures.displayString = this.buttonGenerateStructures.displayString + I18n.getString("options.off");
        }

        this.buttonBonusItems.displayString = I18n.getString("selectWorld.bonusItems") + " ";

        if (this.bonusItems && !this.isHardcore)
        {
            this.buttonBonusItems.displayString = this.buttonBonusItems.displayString + I18n.getString("options.on");
        }
        else
        {
            this.buttonBonusItems.displayString = this.buttonBonusItems.displayString + I18n.getString("options.off");
        }

        this.buttonWorldType.displayString = I18n.getString("selectWorld.mapType") + " " + I18n.getString(WorldType.worldTypes[this.worldTypeId].getTranslateName());
        this.buttonAllowCommands.displayString = I18n.getString("selectWorld.allowCommands") + " ";

        if (this.commandsAllowed && !this.isHardcore)
        {
            this.buttonAllowCommands.displayString = this.buttonAllowCommands.displayString + I18n.getString("options.on");
        }
        else
        {
            this.buttonAllowCommands.displayString = this.buttonAllowCommands.displayString + I18n.getString("options.off");
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
            worldsettings.func_82750_a(generatorOptionsToUse);

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
            mc.statFileWriter.readStat(StatList.createWorldStat, 1);
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
                if (WorldType.worldTypes[worldTypeId] != null && WorldType.worldTypes[worldTypeId].getCanBeCreated())
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

            generatorOptionsToUse = "";
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
            mc.displayGuiScreen(new GuiCreateFlatWorld(this, generatorOptionsToUse));
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
        buttonCustomize.drawButton = moreOptions && WorldType.worldTypes[worldTypeId] == WorldType.FLAT;

        if (moreOptions)
        {
            moreWorldOptions.displayString = I18n.getString("gui.done");
        }
        else
        {
            moreWorldOptions.displayString = I18n.getString("selectWorld.moreWorldOptions");
        }
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

        if (par2 == 28 || par2 == 156)
        {
            actionPerformed((GuiButton)buttonList.get(0));
        }

        ((GuiButton)buttonList.get(0)).enabled = textboxWorldName.getText().length() > 0;
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
        drawDefaultBackground();
        drawCenteredString(fontRenderer, I18n.getString("selectWorld.create"), width / 2, 20, 0xffffff);

        if (moreOptions)
        {
            drawString(fontRenderer, I18n.getString("selectWorld.enterSeed"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, I18n.getString("selectWorld.seedInfo"), width / 2 - 100, 85, 0xa0a0a0);
            drawString(fontRenderer, I18n.getString("selectWorld.mapFeatures.info"), width / 2 - 150, 122, 0xa0a0a0);
            drawString(fontRenderer, I18n.getString("selectWorld.allowCommands.info"), width / 2 - 150, 172, 0xa0a0a0);
            textboxSeed.drawTextBox();
        }
        else
        {
            drawString(fontRenderer, I18n.getString("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, (new StringBuilder()).append(I18n.getString("selectWorld.resultFolder")).append(" ").append(folderName).toString(), width / 2 - 100, 85, 0xa0a0a0);
            textboxWorldName.drawTextBox();
            drawString(fontRenderer, gameModeDescriptionLine1, width / 2 - 100, 137, 0xa0a0a0);
            drawString(fontRenderer, gameModeDescriptionLine2, width / 2 - 100, 149, 0xa0a0a0);
        }

        super.drawScreen(par1, par2, par3);
    }

    public void func_82286_a(WorldInfo par1WorldInfo)
    {
        localizedNewWorldText = I18n.getStringParams("selectWorld.newWorld.copyOf", new Object[]
                {
                    par1WorldInfo.getWorldName()
                });
        seed = (new StringBuilder()).append(par1WorldInfo.getSeed()).append("").toString();
        worldTypeId = par1WorldInfo.getTerrainType().getWorldTypeID();
        generatorOptionsToUse = par1WorldInfo.getGeneratorOptions();
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
    }
}
