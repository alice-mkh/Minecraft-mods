package net.minecraft.src.nbxlite;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import net.minecraft.src.nbxlite.MinecraftHook;
import net.minecraft.src.*;

public class GuiCreateWorld2 extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private GuiTextField textboxWorldName;
    private GuiTextField textboxSeed;
    private String folderName;
    private String gameMode;
    private boolean field_35365_g;
    private boolean field_40232_h;
    private boolean createClicked;
    private boolean moreOptions;
    private GuiButton gameModeButton;
    private GuiButton moreWorldOptions;
    private GuiButton generateStructuresButton;
    private GuiButton worldTypeButton;
    private String gameModeDescriptionLine1;
    private String gameModeDescriptionLine2;
    private String seed;
    private String localizedNewWorldText;
    private int field_46030_z;
    
    private GuiButton generatorButton;
    private GuiButton generatorExtraButton;
    private GuiButton indevButton;
    private String generatorDescription;
    private String generatorExtraDescription;
    private String generator;
    private String generatorExtra;

    public GuiCreateWorld2(GuiScreen guiscreen)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        gameMode = "survival";
        GeneratorList.gencurrent=GeneratorList.gendefault;
        GeneratorList.feat1current=GeneratorList.feat1default;
        GeneratorList.feat2current=GeneratorList.feat2default;
        GeneratorList.themecurrent=GeneratorList.themedefault;
        generator = GeneratorList.genid[GeneratorList.gencurrent];
        generatorDescription = stringtranslate.translateKey(GeneratorList.gendesc[GeneratorList.gencurrent]);
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
            generatorExtra = GeneratorList.feat1id[GeneratorList.feat1default];
            generatorExtraDescription = stringtranslate.translateKey(GeneratorList.feat1desc[GeneratorList.feat1default]);
        }else if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
            generatorExtra = GeneratorList.feat2id[GeneratorList.feat2default];
            generatorExtraDescription = stringtranslate.translateKey(GeneratorList.feat2desc[GeneratorList.feat2default]);
        }else{
            generatorExtra = GeneratorList.themeid[GeneratorList.themedefault];
            generatorExtraDescription = stringtranslate.translateKey(GeneratorList.themedesc[GeneratorList.themedefault]);
        }
        field_35365_g = GeneratorList.genstructures[GeneratorList.gencurrent];
        field_40232_h = false;
        field_46030_z = 0;
        parentGuiScreen = guiscreen;
        seed = "";
        localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
    }

    public void updateScreen()
    {
        textboxWorldName.updateCursorCounter();
        textboxSeed.updateCursorCounter();
    }

    public void initGui()
    {
        String extraname;
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
            extraname = GeneratorList.feat1name[GeneratorList.feat1current];
        }else if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
            extraname = GeneratorList.feat2name[GeneratorList.feat2current];
        }else{
            extraname = GeneratorList.themename[GeneratorList.themecurrent];
        }
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, stringtranslate.translateKey("selectWorld.create")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        controlList.add(gameModeButton = new GuiButton(2, width / 2 - 75, 100, 150, 20, stringtranslate.translateKey("selectWorld.gameMode")));
        controlList.add(moreWorldOptions = new GuiButton(3, width / 2 - 75, 172, 150, 20, stringtranslate.translateKey("selectWorld.moreWorldOptions")));
        controlList.add(generateStructuresButton = new GuiButton(4, width / 2 - 155, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        controlList.add(generatorButton = new GuiButton(6, width / 2 - 155, 135, 150, 20, stringtranslate.translateKey(GeneratorList.genname[GeneratorList.gencurrent])));
        generatorButton.drawButton = false;
        controlList.add(generatorExtraButton = new GuiButton(7, width / 2 + 5, 135, 150, 20, stringtranslate.translateKey(extraname)));
        generatorExtraButton.drawButton = false;
        generateStructuresButton.drawButton = false;
        controlList.add(indevButton = new GuiButton(8, width / 2 + 154, 135, 20, 20, stringtranslate.translateKey("nbxlite.plus")));
        indevButton.drawButton = false;
        controlList.add(worldTypeButton = new GuiButton(5, width / 2 + 5, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapType")));
        worldTypeButton.drawButton = false;
        textboxWorldName = new GuiTextField(this, fontRenderer, width / 2 - 100, 60, 200, 20, localizedNewWorldText);
        textboxWorldName.isFocused = true;
        textboxWorldName.setMaxStringLength(32);
        textboxSeed = new GuiTextField(this, fontRenderer, width / 2 - 100, 60, 200, 20, seed);
        makeUseableName();
        func_35363_g();
    }

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
        folderName = generateUnusedFolderName(mc.getSaveLoader(), folderName);
    }

    private void func_35363_g()
    {
        StringTranslate stringtranslate;
        stringtranslate = StringTranslate.getInstance();
        gameModeButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.gameMode")).append(" ").append(stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).toString())).toString();
        gameModeDescriptionLine1 = stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).append(".line1").toString());
        gameModeDescriptionLine2 = stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).append(".line2").toString());
        generateStructuresButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.mapFeatures")).append(" ").toString();
        if (field_35365_g)
        {
            generateStructuresButton.displayString += stringtranslate.translateKey("options.on");
        }
        else
        {
            generateStructuresButton.displayString += stringtranslate.translateKey("options.off");
        }
        worldTypeButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.mapType")).append(" ").append(stringtranslate.translateKey(WorldType.field_48637_a[field_46030_z].func_46136_a())).toString();
        return;
    }

    public static String generateUnusedFolderName(ISaveFormat isaveformat, String s)
    {
        for (; isaveformat.getWorldInfo(s) != null; s = (new StringBuilder()).append(s).append("-").toString()) { }
        return s;
    }

    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled)
        {
            return;
        }
        if (guibutton.id == 1)
        {
            mc.displayGuiScreen(parentGuiScreen);
        }
        else if (guibutton.id == 0)
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
            mod_noBiomesX.Generator=GeneratorList.genfeatures[GeneratorList.gencurrent];
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
                mod_noBiomesX.MapFeatures=GeneratorList.genfeats[GeneratorList.gencurrent];
            }
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
                mod_noBiomesX.MapFeatures=GeneratorList.feat1current;
            }
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
                mod_noBiomesX.MapFeatures=GeneratorList.feat2current;
            }
            mod_noBiomesX.MapTheme=GeneratorList.themecurrent;
//             MinecraftHook.startWorldHook(folderName, textboxWorldName.getText(), new WorldSettings(l, i, field_35365_g, field_40232_h, EnumWorldType.values()[field_46030_z]));
            mc.startWorld(folderName, textboxWorldName.getText(), new WorldSettings(l, i, field_35365_g, field_40232_h, WorldType.field_48637_a[field_46030_z]));
            mc.displayGuiScreen(null);
        }
        else if (guibutton.id == 3)
        {
            moreOptions = !moreOptions;
            gameModeButton.drawButton = !moreOptions;
            generateStructuresButton.drawButton = moreOptions;
            worldTypeButton.drawButton = moreOptions;
            generatorButton.drawButton = moreOptions;
            generatorExtraButton.drawButton = moreOptions;
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
            indevButton.drawButton = moreOptions && GeneratorList.genplus[GeneratorList.gencurrent];
        }
        else if (guibutton.id == 2)
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
        else if (guibutton.id == 4)
        {
            field_35365_g = !field_35365_g;
            func_35363_g();
        }
        else if (guibutton.id == 5)
        {
            field_46030_z++;

            if (field_46030_z >= WorldType.field_48637_a.length)
            {
                field_46030_z = 0;
            }

            do
            {
                if (WorldType.field_48637_a[field_46030_z] != null && WorldType.field_48637_a[field_46030_z].func_48627_d())
                {
                    break;
                }

                field_46030_z++;

                if (field_46030_z >= WorldType.field_48637_a.length)
                {
                    field_46030_z = 0;
                }
            }
            while (true);

            func_35363_g();
        }
        else if(guibutton.id == 6)
        {
            if (GeneratorList.gencurrent<GeneratorList.genlength){
                GeneratorList.gencurrent++;
            }else{
                GeneratorList.gencurrent=0;
            }
            StringTranslate stringtranslate = StringTranslate.getInstance();
            generator=GeneratorList.genid[GeneratorList.gencurrent];
            generatorButton.displayString = stringtranslate.translateKey(GeneratorList.genname[GeneratorList.gencurrent]);
            generatorDescription = stringtranslate.translateKey(GeneratorList.gendesc[GeneratorList.gencurrent]);
            field_35365_g = GeneratorList.genstructures[GeneratorList.gencurrent];
            if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
                worldTypeButton.enabled = (GeneratorList.feat1worldtype[GeneratorList.feat1default]);
            }else if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
                worldTypeButton.enabled = (GeneratorList.feat2worldtype[GeneratorList.feat2default]);
            }else{
                worldTypeButton.enabled = false;
            }
            if (!worldTypeButton.enabled){
                field_46030_z = 0;
            }
            func_35363_g();
            indevButton.drawButton = GeneratorList.genplus[GeneratorList.gencurrent];
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
                GeneratorList.themecurrent = GeneratorList.themedefault;
                generatorExtra = GeneratorList.themeid[GeneratorList.themecurrent];
                generatorExtraButton.displayString = stringtranslate.translateKey(GeneratorList.themename[GeneratorList.themecurrent]);
                generatorExtraDescription = stringtranslate.translateKey(GeneratorList.themedesc[GeneratorList.themecurrent]);
            }else if(GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
                GeneratorList.feat1current = GeneratorList.feat1default;
                generatorExtra = GeneratorList.feat1id[GeneratorList.feat1current];
                generatorExtraButton.displayString = stringtranslate.translateKey(GeneratorList.feat1name[GeneratorList.feat1current]);
                generatorExtraDescription = stringtranslate.translateKey(GeneratorList.feat1desc[GeneratorList.feat1current]);
            }else{
                GeneratorList.feat2current = GeneratorList.feat2default;
                generatorExtra = GeneratorList.feat2id[GeneratorList.feat2current];
                generatorExtraButton.displayString = stringtranslate.translateKey(GeneratorList.feat2name[GeneratorList.feat2current]);
                generatorExtraDescription = stringtranslate.translateKey(GeneratorList.feat2desc[GeneratorList.feat2current]);
            }
        } else if(guibutton.id == 8) {
            mc.displayGuiScreen(new GuiIndevSettings(this));
            moreOptions = false;
        } else if(guibutton.id == 7) {
            StringTranslate stringtranslate = StringTranslate.getInstance();
            if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
                if (GeneratorList.feat1current<GeneratorList.feat1length){
                    GeneratorList.feat1current++;
                }else{
                    GeneratorList.feat1current=0;
                }
                generatorExtra = GeneratorList.feat1id[GeneratorList.feat1current];
                generatorExtraButton.displayString = stringtranslate.translateKey(GeneratorList.feat1name[GeneratorList.feat1current]);
                generatorExtraDescription = stringtranslate.translateKey(GeneratorList.feat1desc[GeneratorList.feat1current]);
                worldTypeButton.enabled = GeneratorList.feat1worldtype[GeneratorList.feat1current];
                if (!worldTypeButton.enabled){
                    field_46030_z = 0;
                }
                func_35363_g();
            }else if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
                if (GeneratorList.feat2current<GeneratorList.feat2length){
                    GeneratorList.feat2current++;
                }else{
                    GeneratorList.feat2current=0;
                }
                generatorExtra = GeneratorList.feat2id[GeneratorList.feat2current];
                generatorExtraButton.displayString = stringtranslate.translateKey(GeneratorList.feat2name[GeneratorList.feat2current]);
                generatorExtraDescription = stringtranslate.translateKey(GeneratorList.feat2desc[GeneratorList.feat2current]);
                worldTypeButton.enabled = (GeneratorList.feat2worldtype[GeneratorList.feat2current]);
                if (!worldTypeButton.enabled){
                    field_46030_z = 0;
                }
                func_35363_g();
            }else{
                if (GeneratorList.themecurrent<GeneratorList.themelength){
                    GeneratorList.themecurrent++;
                }else{
                    GeneratorList.themecurrent=0;
                }
                generatorExtra = GeneratorList.themeid[GeneratorList.themecurrent];
                generatorExtraButton.displayString = stringtranslate.translateKey(GeneratorList.themename[GeneratorList.themecurrent]);
                generatorExtraDescription = stringtranslate.translateKey(GeneratorList.themedesc[GeneratorList.themecurrent]);
                worldTypeButton.enabled = false;
                field_46030_z = 0;
                func_35363_g();
            }
        }
    }

    protected void keyTyped(char c, int i)
    {
        if (textboxWorldName.isFocused && !moreOptions)
        {
            textboxWorldName.textboxKeyTyped(c, i);
            localizedNewWorldText = textboxWorldName.getText();
        }
        else if (textboxSeed.isFocused && moreOptions)
        {
            textboxSeed.textboxKeyTyped(c, i);
            seed = textboxSeed.getText();
        }
        if (c == '\r')
        {
            actionPerformed((GuiButton)controlList.get(0));
        }
        ((GuiButton)controlList.get(0)).enabled = textboxWorldName.getText().length() > 0;
        makeUseableName();
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
        if (!moreOptions)
        {
            textboxWorldName.mouseClicked(i, j, k);
        }
        else
        {
            textboxSeed.mouseClicked(i, j, k);
        }
    }

    public void drawScreen(int i, int j, float f)
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
            drawString(fontRenderer, generatorDescription, width / 2 - 150, 156, 0xa0a0a0);
            drawString(fontRenderer, generatorExtraDescription, width / 2 + 10, 156, 0xa0a0a0);
        }
        super.drawScreen(i, j, f);
    }

    public void selectNextField()
    {
        if (textboxWorldName.isFocused)
        {
            textboxWorldName.setFocused(false);
            textboxSeed.setFocused(true);
        }
        else
        {
            textboxWorldName.setFocused(true);
            textboxSeed.setFocused(false);
        }
    }
}
