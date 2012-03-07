package net.minecraft.src.nbxlite;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import java.util.Collections;
import net.minecraft.src.mod_noBiomesX;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.PlayerControllerCreative;
import net.minecraft.src.PlayerControllerSP;
import net.minecraft.src.SaveFormatComparator;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.nbxlite.MinecraftHook;

public class GuiNBXliteSettings extends GuiScreen
{
    private GuiScreen parentGuiScreen;
    private String folderName;
    private boolean field_35365_g;
    private boolean field_40232_h;
    private boolean createClicked;
    private GuiButton generateStructuresButton;
    private String seed;
    private String localizedNewWorldText;
    private GuiButton generatorButton;
    private GuiButton generatorExtraButton;
    private String generatorDescription;
    private String generatorExtraDescription;
    private String generator;
    private String generatorExtra;
    private String selectedWorld;
    private int number;

    public GuiNBXliteSettings(GuiScreen guiscreen, String world, int i)
    {
        selectedWorld = world;
        StringTranslate stringtranslate = StringTranslate.getInstance();
        generator = "beta2";
        generatorExtra = "11";
        generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures12");
        generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenRelease");
        field_35365_g = true;
        field_40232_h = false;
        parentGuiScreen = guiscreen;
        seed = "";
        localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
        number = i;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, stringtranslate.translateKey("nbxlite.continue")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        controlList.add(generateStructuresButton = new GuiButton(4, width / 2 - 75, 65, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        controlList.add(generatorButton = new GuiButton(6, width / 2 - 75, 110, 150, 20, stringtranslate.translateKey("nbxlite.genRelease")));
        controlList.add(generatorExtraButton = new GuiButton(7, width / 2 - 75, 155, 150, 20, stringtranslate.translateKey("nbxlite.features12")));
        func_35363_g();
        if (mc.getSaveLoader().getSaveLoader(selectedWorld, false).loadWorldInfo().getMapGen() != 0){
            selectWorld();
        }
    }

    private void func_35363_g()
    {
        StringTranslate stringtranslate;
        stringtranslate = StringTranslate.getInstance();
        generateStructuresButton.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.mapFeatures")).append(" ").toString();
        if (field_35365_g)
        {
            generateStructuresButton.displayString += stringtranslate.translateKey("options.on");
        }
        else
        {
            generateStructuresButton.displayString += stringtranslate.translateKey("options.off");
        }
        return;
    }

    public void selectWorld()
    {
        ISaveFormat isaveformat = mc.getSaveLoader();
        List saveList = isaveformat.getSaveList();
        Collections.sort(saveList);
        mc.displayGuiScreen(null);
        int j = ((SaveFormatComparator)saveList.get(number)).getGameType();
        if (j == 0)
        {
            mc.playerController = new PlayerControllerSP(mc);
        }
        else
        {
            mc.playerController = new PlayerControllerCreative(mc);
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
        String s = selectedWorld;
        if (s == null)
        {
            s = (new StringBuilder()).append("World").append(selectedWorld).toString();
        }
//         MinecraftHook.startWorldHook(s, selectedWorld, null);
        mc.startWorld(s, selectedWorld, null);
        mc.displayGuiScreen(null);
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
            selectWorld();
        }
        else if (guibutton.id == 4)
        {
            field_35365_g = !field_35365_g;
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
                func_35363_g();
            } else if(generator.equals("infdev0420"))
            {
                generator = "alpha";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genAlpha");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenAlpha");
                field_35365_g = false;
                func_35363_g();
            } else if(generator.equals("infdev0227"))
            {
                generator = "infdev0420";
                generatorButton.displayString = stringtranslate.translateKey("nbxlite.genInfdev0420");
                generatorDescription = stringtranslate.translateKey("nbxlite.descriptionGenInfdev0420");
                field_35365_g = false;
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
                func_35363_g();
            } else
            if(generatorExtra.equals("11"))
            {
                generatorExtra = "12";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.features12");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures12");
                func_35363_g();
            } else
            if(generatorExtra.equals("100"))
            {
                generatorExtra = "11";
                generatorExtraButton.displayString = stringtranslate.translateKey("nbxlite.features11");
                generatorExtraDescription = stringtranslate.translateKey("nbxlite.descriptionFeatures11");
            }
        }
    }

    protected void mouseClicked(int i, int j, int k)
    {
        super.mouseClicked(i, j, k);
    }

    public void drawScreen(int i, int j, float f)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        drawDefaultBackground();
        drawCenteredString(fontRenderer, stringtranslate.translateKey("nbxlite.convert"), width / 2, 20, 0xffffff);
        drawString(fontRenderer, stringtranslate.translateKey("selectWorld.mapFeatures.info"), width / 2 - 75, 87, 0xa0a0a0);
        drawString(fontRenderer, generatorDescription, width / 2 - 75, 132, 0xa0a0a0);
        drawString(fontRenderer, generatorExtraDescription, width / 2 - 75, 177, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }
}
