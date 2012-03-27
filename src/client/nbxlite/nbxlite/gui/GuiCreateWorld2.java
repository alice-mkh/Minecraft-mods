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

    /** 'hardcore', 'creative' or 'survival' */
    private String gameMode;
    private boolean field_35365_g;
    private boolean field_40232_h;
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

    /**
     * the GUIButton in the more world options screen. It's currently greyed out and unused in minecraft 1.0.0
     */
    private GuiButton worldTypeButton;

    /** The first line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine1;

    /** The second line of text describing the currently selected game mode. */
    private String gameModeDescriptionLine2;

    /** The current textboxSeed text */
    private String seed;

    /** E.g. New World, Neue Welt, Nieuwe wereld, Neuvo Mundo */
    private String localizedNewWorldText;
    private int field_46030_z;
    private GuiButton nbxliteButton;

    public GuiCreateWorld2(GuiScreen par1GuiScreen)
    {
        gameMode = "survival";
        field_35365_g = true;
        field_40232_h = false;
        field_46030_z = 0;
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
        controlList.add(gameModeButton = new GuiButton(2, width / 2 - 75, 100, 150, 20, stringtranslate.translateKey("selectWorld.gameMode")));
        controlList.add(moreWorldOptions = new GuiButton(3, width / 2 - 75, 172, 150, 20, stringtranslate.translateKey("selectWorld.moreWorldOptions")));
        controlList.add(generateStructuresButton = new GuiButton(4, width / 2 - 155, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        generateStructuresButton.drawButton = false;
        controlList.add(worldTypeButton = new GuiButton(5, width / 2 + 5, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapType")));
        worldTypeButton.drawButton = false;
        if (!(GeneratorList.genfeatures[GeneratorList.gencurrent]==2 && GeneratorList.feat2worldtype[GeneratorList.feat2current]) &&
            !(GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1worldtype[GeneratorList.feat1current])){
            worldTypeButton.enabled = false;
            field_46030_z = 0;
        }
        field_35365_g = GeneratorList.genstructures[GeneratorList.gencurrent];
        textboxWorldName = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxWorldName.func_50033_b(true);
        textboxWorldName.setText(localizedNewWorldText);
        textboxSeed = new GuiTextField(fontRenderer, width / 2 - 100, 60, 200, 20);
        textboxSeed.setText(seed);
        controlList.add(nbxliteButton = new GuiButton(6, width / 2 - 155, 135, 310, 20, genNBXliteButtonName()));
        nbxliteButton.drawButton = false;
        makeUseableName();
        func_35363_g();
    }

    public static void setDefaultNBXliteSettings(){
        GeneratorList.gencurrent = GeneratorList.gendefault;
        GeneratorList.themecurrent = GeneratorList.themedefault;
        GeneratorList.feat1current = GeneratorList.feat1default;
        GeneratorList.feat2current = GeneratorList.feat2default;
        GeneratorList.typecurrent = GeneratorList.typedefault;
        GeneratorList.xcurrent = GeneratorList.xdefault;
        GeneratorList.zcurrent = GeneratorList.zdefault;
        mod_noBiomesX.IndevHeight=96;
    }

    public static String genNBXliteButtonName(){
        StringBuilder str = new StringBuilder();
        str.append(mod_noBiomesX.lang.get("settings"));
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            str.append(mod_noBiomesX.lang.get(GeneratorList.genname[GeneratorList.gencurrent]));
            if (GeneratorList.genplus[GeneratorList.gencurrent]==0){
                str.append(", ");
            }
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2){
            str.append(" (");
            str.append(GeneratorList.sizes[GeneratorList.xcurrent]);
            str.append("x");
            str.append(GeneratorList.sizes[GeneratorList.zcurrent]);
            if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
                str.append("x");
                str.append(mod_noBiomesX.IndevHeight-32);
            }
            str.append("), ");
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
            str.append(mod_noBiomesX.lang.get(GeneratorList.typename[GeneratorList.typecurrent]));
            str.append(", ");
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            str.append(mod_noBiomesX.lang.get(GeneratorList.themename[GeneratorList.themecurrent]));
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
            str.append(mod_noBiomesX.lang.get(GeneratorList.feat1name[GeneratorList.feat1current]));
        }
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
            str.append(mod_noBiomesX.lang.get(GeneratorList.feat2name[GeneratorList.feat2current]));
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

        folderName = func_25097_a(mc.getSaveLoader(), folderName);
    }

    private void func_35363_g()
    {
        StringTranslate stringtranslate;
        stringtranslate = StringTranslate.getInstance();
        gameModeButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.gameMode")).append(" ").append(stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).toString())).toString();
        gameModeDescriptionLine1 = stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).append(".line1").toString());
        gameModeDescriptionLine2 = stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).append(".line2").toString());
        generateStructuresButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.mapFeatures")).append(" ").toString();

        if (!(!field_35365_g))
        {
            generateStructuresButton.displayString += stringtranslate.translateKey("options.on");
        }
        else
        {
            generateStructuresButton.displayString += stringtranslate.translateKey("options.off");
        }

        worldTypeButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.mapType")).append(" ").append(stringtranslate.translateKey(WorldType.worldTypes[field_46030_z].getTranslateName())).toString();
        return;
    }

    public static String func_25097_a(ISaveFormat par0ISaveFormat, String par1Str)
    {
        for (par1Str = par1Str.replaceAll("[\\./\"]|COM", "_"); par0ISaveFormat.getWorldInfo(par1Str) != null; par1Str = (new StringBuilder()).append(par1Str).append("-").toString()) { }

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

            int i = 0;

            if (gameMode.equals("creative"))
            {
                i = 1;
                mc.playerController = new PlayerControllerCreative(mc);
            }
            else
            {
                mc.playerController = new PlayerControllerSP(mc);
            }

            mc.startWorld(folderName, textboxWorldName.getText(), new WorldSettings(l, i, field_35365_g, field_40232_h, WorldType.worldTypes[field_46030_z]));
            mc.displayGuiScreen(null);
        }
        else if (par1GuiButton.id == 3)
        {
            moreOptions = !moreOptions;
            gameModeButton.drawButton = !moreOptions;
            generateStructuresButton.drawButton = moreOptions;
            worldTypeButton.drawButton = moreOptions;
            nbxliteButton.drawButton = moreOptions;

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
        else if (par1GuiButton.id == 6)
        {
            mc.displayGuiScreen(new GuiNBXlite(this));
            moreOptions = false;
        }
        else if (par1GuiButton.id == 2)
        {
            if (gameMode.equals("survival"))
            {
                field_40232_h = false;
                gameMode = "hardcore";
                field_40232_h = true;
                func_35363_g();
            }
            else if (gameMode.equals("hardcore"))
            {
                field_40232_h = false;
                gameMode = "creative";
                func_35363_g();
                field_40232_h = false;
            }
            else
            {
                gameMode = "survival";
                func_35363_g();
                field_40232_h = false;
            }

            func_35363_g();
        }
        else if (par1GuiButton.id == 4)
        {
            field_35365_g = !field_35365_g;
            func_35363_g();
        }
        else if (par1GuiButton.id == 5)
        {
            field_46030_z++;

            if (field_46030_z >= WorldType.worldTypes.length)
            {
                field_46030_z = 0;
            }

            do
            {
                if (WorldType.worldTypes[field_46030_z] != null && WorldType.worldTypes[field_46030_z].getCanBeCreated())
                {
                    break;
                }

                field_46030_z++;

                if (field_46030_z >= WorldType.worldTypes.length)
                {
                    field_46030_z = 0;
                }
            }
            while (true);

            func_35363_g();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char par1, int par2)
    {
        if (textboxWorldName.func_50025_j() && !moreOptions)
        {
            textboxWorldName.func_50037_a(par1, par2);
            localizedNewWorldText = textboxWorldName.getText();
        }
        else if (textboxSeed.func_50025_j() && moreOptions)
        {
            textboxSeed.func_50037_a(par1, par2);
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

        if (!moreOptions)
        {
            textboxWorldName.mouseClicked(par1, par2, par3);
        }
        else
        {
            textboxSeed.mouseClicked(par1, par2, par3);
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

        if (!moreOptions)
        {
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterName"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.resultFolder")).append(" ").append(folderName).toString(), width / 2 - 100, 85, 0xa0a0a0);
            textboxWorldName.drawTextBox();
            drawString(fontRenderer, gameModeDescriptionLine1, width / 2 - 100, 122, 0xa0a0a0);
            drawString(fontRenderer, gameModeDescriptionLine2, width / 2 - 100, 134, 0xa0a0a0);
        }
        else
        {
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.enterSeed"), width / 2 - 100, 47, 0xa0a0a0);
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.seedInfo"), width / 2 - 100, 85, 0xa0a0a0);
            drawString(fontRenderer, stringtranslate.translateKey("selectWorld.mapFeatures.info"), width / 2 - 150, 122, 0xa0a0a0);
            textboxSeed.drawTextBox();
        }

        super.drawScreen(par1, par2, par3);
    }
}
