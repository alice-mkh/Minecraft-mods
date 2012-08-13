package net.minecraft.src;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class GuiCreateWorld extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;

    /** hardcore', 'creative' or 'survival */
    private String gameMode;
    private boolean field_73925_n;
    private boolean field_73926_o;
    private boolean field_73935_p;
    private boolean field_73934_q;
    private boolean field_73933_r;
    private boolean createClicked;

    /**
     * True if the extra options (Seed box, structure toggle button, world type button, etc.) are being shown
     */
    private boolean moreOptions;

    /** The GUIButton that you click to change game modes. */
    private GuiButton gameModeButton;

    /**
     * The GUIButton that you click to get to options like the seed when creating a world.
     */
    private GuiButton moreWorldOptions;

    /** The GuiButton in the 'More World Options' screen. Toggles ON/OFF */
    private GuiButton generateStructuresButton;
    private GuiButton field_73938_x;

    /**
     * the GUIButton in the more world options screen. It's currently greyed out and unused in minecraft 1.0.0
     */
    private GuiButton worldTypeButton;
    private GuiButton field_73936_z;

    /** The first line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine1;

    /** The second line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine2;

    /** The current textboxSeed text */
    private String seed;

    /** E.g. New World, Neue Welt, Nieuwe wereld, Neuvo Mundo */
    private String localizedNewWorldText;
    private int field_73916_E;
    private static final String field_73917_F[] =
    {
        "CON", "COM", "PRN", "AUX", "CLOCK$", "NUL", "COM1", "COM2", "COM3", "COM4",
        "COM5", "COM6", "COM7", "COM8", "COM9", "LPT1", "LPT2", "LPT3", "LPT4", "LPT5",
        "LPT6", "LPT7", "LPT8", "LPT9"
    };

    public GuiCreateWorld(GuiScreen par1GuiScreen)
    {
        gameMode = "survival";
        field_73925_n = true;
        field_73926_o = false;
        field_73935_p = false;
        field_73934_q = false;
        field_73933_r = false;
        field_73916_E = 0;
        parentGuiScreen = par1GuiScreen;
        seed = "";
        localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
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
        controlList.add(gameModeButton = new GuiButton(2, width / 2 - 75, 100, 150, 20, stringtranslate.translateKey("selectWorld.gameMode")));
        controlList.add(moreWorldOptions = new GuiButton(3, width / 2 - 75, 172, 150, 20, stringtranslate.translateKey("selectWorld.moreWorldOptions")));
        controlList.add(generateStructuresButton = new GuiButton(4, width / 2 - 155, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        generateStructuresButton.drawButton = false;
        controlList.add(field_73938_x = new GuiButton(7, width / 2 + 5, 136, 150, 20, stringtranslate.translateKey("selectWorld.bonusItems")));
        field_73938_x.drawButton = false;
        controlList.add(worldTypeButton = new GuiButton(5, width / 2 + 5, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapType")));
        worldTypeButton.drawButton = false;
        controlList.add(field_73936_z = new GuiButton(6, width / 2 - 155, 136, 150, 20, stringtranslate.translateKey("selectWorld.allowCommands")));
        field_73936_z.drawButton = false;
        textboxWorldName = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxWorldName.setFocused(true);
        textboxWorldName.setText(localizedNewWorldText);
        textboxSeed = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxSeed.setText(seed);
        makeUseableName();
        func_73914_h();
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

    private void func_73914_h()
    {
        StringTranslate var1 = StringTranslate.getInstance();
        this.gameModeButton.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
        this.gameModeDescriptionLine1 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line1");
        this.gameModeDescriptionLine2 = var1.translateKey("selectWorld.gameMode." + this.gameMode + ".line2");
        this.generateStructuresButton.displayString = var1.translateKey("selectWorld.mapFeatures") + " ";

        if (this.field_73925_n)
        {
            this.generateStructuresButton.displayString = this.generateStructuresButton.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.generateStructuresButton.displayString = this.generateStructuresButton.displayString + var1.translateKey("options.off");
        }

        this.field_73938_x.displayString = var1.translateKey("selectWorld.bonusItems") + " ";

        if (this.field_73934_q && !this.field_73933_r)
        {
            this.field_73938_x.displayString = this.field_73938_x.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.field_73938_x.displayString = this.field_73938_x.displayString + var1.translateKey("options.off");
        }

        this.worldTypeButton.displayString = var1.translateKey("selectWorld.mapType") + " " + var1.translateKey(WorldType.worldTypes[this.field_73916_E].getTranslateName());
        this.field_73936_z.displayString = var1.translateKey("selectWorld.allowCommands") + " ";

        if (this.field_73926_o && !this.field_73933_r)
        {
            this.field_73936_z.displayString = this.field_73936_z.displayString + var1.translateKey("options.on");
        }
        else
        {
            this.field_73936_z.displayString = this.field_73936_z.displayString + var1.translateKey("options.off");
        }
    }

    public static String func_73913_a(ISaveFormat par0ISaveFormat, String par1Str)
    {
        par1Str = par1Str.replaceAll("[\\./\"]", "_");
        String as[] = field_73917_F;
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

            EnumGameType enumgametype = EnumGameType.func_77142_a(gameMode);
            WorldSettings worldsettings = new WorldSettings(l, enumgametype, field_73925_n, field_73933_r, WorldType.worldTypes[field_73916_E]);

            if (field_73934_q && !field_73933_r)
            {
                worldsettings.func_77159_a();
            }

            if (field_73926_o && !field_73933_r)
            {
                worldsettings.func_77166_b();
            }

            mc.enableSP = mc.useSP;
            if (mc.enableSP){
                mc.setController(enumgametype);
                mc.startWorldSSP(folderName, textboxWorldName.getText().trim(), worldsettings);
                mc.displayGuiScreen(null);
            }else{
                mc.func_71371_a(folderName, textboxWorldName.getText().trim(), worldsettings);
            }
        }
        else if (par1GuiButton.id == 3)
        {
            moreOptions = !moreOptions;
            gameModeButton.drawButton = !moreOptions;
            generateStructuresButton.drawButton = moreOptions;
            field_73938_x.drawButton = moreOptions;
            worldTypeButton.drawButton = moreOptions;
            field_73936_z.drawButton = moreOptions;

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
        }
        else if (par1GuiButton.id == 2)
        {
            if (gameMode.equals("survival"))
            {
                if (!field_73935_p)
                {
                    field_73926_o = false;
                }

                field_73933_r = false;
                gameMode = "hardcore";
                field_73933_r = true;
                field_73936_z.enabled = false;
                field_73938_x.enabled = false;
                func_73914_h();
            }
            else if (gameMode.equals("hardcore"))
            {
                if (!field_73935_p)
                {
                    field_73926_o = true;
                }

                field_73933_r = false;
                gameMode = "creative";
                func_73914_h();
                field_73933_r = false;
                field_73936_z.enabled = true;
                field_73938_x.enabled = true;
            }
            else
            {
                if (!field_73935_p)
                {
                    field_73926_o = false;
                }

                gameMode = "survival";
                func_73914_h();
                field_73936_z.enabled = true;
                field_73938_x.enabled = true;
                field_73933_r = false;
            }

            func_73914_h();
        }
        else if (par1GuiButton.id == 4)
        {
            field_73925_n = !field_73925_n;
            func_73914_h();
        }
        else if (par1GuiButton.id == 7)
        {
            field_73934_q = !field_73934_q;
            func_73914_h();
        }
        else if (par1GuiButton.id == 5)
        {
            field_73916_E++;

            if (field_73916_E >= WorldType.worldTypes.length)
            {
                field_73916_E = 0;
            }

            do
            {
                if (WorldType.worldTypes[field_73916_E] != null && WorldType.worldTypes[field_73916_E].getCanBeCreated())
                {
                    break;
                }

                field_73916_E++;

                if (field_73916_E >= WorldType.worldTypes.length)
                {
                    field_73916_E = 0;
                }
            }
            while (true);

            func_73914_h();
        }
        else if (par1GuiButton.id == 6)
        {
            field_73935_p = true;
            field_73926_o = !field_73926_o;
            func_73914_h();
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
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.allowCommands.info"), width / 2 - 150, 157, 0xa0a0a0);
            textboxSeed.drawTextBox();
        }
        else
        {
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.resultFolder")).append(" ").append(folderName).toString(), width / 2 - 100, 85, 0xa0a0a0);
            textboxWorldName.drawTextBox();
            drawString(fontRenderer, gameModeDescriptionLine1, width / 2 - 100, 122, 0xa0a0a0);
            drawString(fontRenderer, gameModeDescriptionLine2, width / 2 - 100, 134, 0xa0a0a0);
        }

        super.drawScreen(par1, par2, par3);
    }
}
