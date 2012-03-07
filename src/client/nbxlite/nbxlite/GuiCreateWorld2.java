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
    private String generatorDescription;
    private String generatorExtraDescription;
    private String generator;
    private String generatorExtra;

    public GuiCreateWorld2(GuiScreen guiscreen)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        gameMode = "survival";
        generator = "beta2";
        generatorExtra = "12";
        generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures12");
        generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenRelease");
        field_35365_g = true;
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
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, stringtranslate.translateKey("selectWorld.create")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        controlList.add(gameModeButton = new GuiButton(2, width / 2 - 75, 100, 150, 20, stringtranslate.translateKey("selectWorld.gameMode")));
        controlList.add(moreWorldOptions = new GuiButton(3, width / 2 - 75, 172, 150, 20, stringtranslate.translateKey("selectWorld.moreWorldOptions")));
        controlList.add(generateStructuresButton = new GuiButton(4, width / 2 - 155, 100, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        controlList.add(generatorButton = new GuiButton(6, width / 2 - 155, 135, 150, 20, stringtranslate.translateKey("nbxlite.genRelease")));
        generatorButton.drawButton = false;
        controlList.add(generatorExtraButton = new GuiButton(7, width / 2 + 5, 135, 150, 20, stringtranslate.translateKey("nbxlite.features12")));
        generatorExtraButton.drawButton = false;
        generateStructuresButton.drawButton = false;
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
            if(generator.equals("beta")){
                mod_noBiomesX.Generator=1;
            }else if(generator.equals("beta2")){
                mod_noBiomesX.Generator=2;
            }else if(generator.equals("alpha")){
                mod_noBiomesX.Generator=0;
                mod_noBiomesX.MapFeatures=0;
            }else if (generator.equals("infdev0420")){
                mod_noBiomesX.Generator=0;
                mod_noBiomesX.MapFeatures=1;
            }else{
                mod_noBiomesX.Generator=0;
                mod_noBiomesX.MapFeatures=2;
            }
            if(generatorExtra.equals("hell")){
                mod_noBiomesX.MapTheme=1;
            }else if(generatorExtra.equals("woods")){
                mod_noBiomesX.MapTheme=2;
            }else if(generatorExtra.equals("paradise")){
                mod_noBiomesX.MapTheme=3;
            }else{
                mod_noBiomesX.MapTheme=0;
            }
            if(generatorExtra.equals("173")){
                mod_noBiomesX.MapFeatures=4;
            }else if(generatorExtra.equals("15") || generatorExtra.equals("12")){
                mod_noBiomesX.MapFeatures=3;
            }else if(generatorExtra.equals("14") || generatorExtra.equals("11")){
                mod_noBiomesX.MapFeatures=2;
            }else if(generatorExtra.equals("beta12") || generatorExtra.equals("100")){
                mod_noBiomesX.MapFeatures=1;
            }else if(generatorExtra.equals("120") || generatorExtra.equals("181")){
                mod_noBiomesX.MapFeatures=0;
            }
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
            StringTranslate stringtranslate = StringTranslate.getInstance();
            if(generator.equals("beta"))
            {
                generator = "beta2";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genRelease");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenRelease");
                generatorExtra = "12";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.features12");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures12");
                field_35365_g = true;
                worldTypeButton.enabled = true;
                func_35363_g();
            } else if(generator.equals("beta2"))
            {
                generator = "infdev0227";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genInfdev0227");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenInfdev0227");
                generatorExtra = "normal";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.themeNormal");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionThemeNormal");
                field_35365_g = false;
                worldTypeButton.enabled = false;
                field_46030_z = 0;
                func_35363_g();
            } else if(generator.equals("alpha"))
            {
                generator = "beta";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genBeta");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenBeta");
                generatorExtra = "173";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresBeta173");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesBeta173");
                field_35365_g = false;
                worldTypeButton.enabled = false;
                field_46030_z = 0;
                func_35363_g();
            } else if(generator.equals("infdev0420"))
            {
                generator = "alpha";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genAlpha");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenAlpha");
                field_35365_g = false;
                worldTypeButton.enabled = false;
                field_46030_z = 0;
                func_35363_g();
            } else if(generator.equals("infdev0227"))
            {
                generator = "infdev0420";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genInfdev0420");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenInfdev0420");
                generatorExtra = "normal";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.themeNormal");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionThemeNormal");
                field_35365_g = false;
                worldTypeButton.enabled = false;
                field_46030_z = 0;
                func_35363_g();
            }
        } else
        if(guibutton.id == 7)
        {
            StringTranslate stringtranslate = StringTranslate.getInstance();
            if(generatorExtra.equals("normal"))
            {
                generatorExtra = "hell";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.themeHell");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionThemeHell");
            } else
            if(generatorExtra.equals("hell"))
            {
                generatorExtra = "woods";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.themeWoods");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionThemeWoods");
            } else
            if(generatorExtra.equals("woods"))
            {
                generatorExtra = "paradise";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.themeParadise");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionThemeParadise");
            } else
            if(generatorExtra.equals("paradise"))
            {
                generatorExtra = "normal";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.themeNormal");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionThemeNormal");
            } else
            if(generatorExtra.equals("120"))
            {
                generatorExtra = "beta12";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresBeta12");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesBeta12");
            } else
            if(generatorExtra.equals("beta12"))
            {
                generatorExtra = "14";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresBeta14");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesBeta14");
            } else
            if(generatorExtra.equals("14"))
            {
                generatorExtra = "15";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresBeta15");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesBeta15");
            } else
            if(generatorExtra.equals("15"))
            {
                generatorExtra = "173";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresBeta173");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesBeta173");
            }  else
            if(generatorExtra.equals("173"))
            {
                generatorExtra = "120";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresHalloween");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesHalloween");
            } else
            if(generatorExtra.equals("181"))
            {
                generatorExtra = "100";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.features10");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures10");
            } else
            if(generatorExtra.equals("12"))
            {
                generatorExtra = "181";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.featuresBeta181");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeaturesBeta181");
                worldTypeButton.enabled = false;
                field_46030_z = 0;
                func_35363_g();
            } else
            if(generatorExtra.equals("11"))
            {
                generatorExtra = "12";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.features12");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures12");
                worldTypeButton.enabled = true;
            } else
            if(generatorExtra.equals("100"))
            {
                generatorExtra = "11";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.features11");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures11");
                worldTypeButton.enabled = true;
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
