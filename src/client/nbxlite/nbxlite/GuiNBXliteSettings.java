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
    private GuiButton indevButton;
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
        parentGuiScreen = guiscreen;
        seed = "";
        localizedNewWorldText = StatCollector.translateToLocal("selectWorld.newWorld");
        number = i;
    }

    public void initGui()
    {
//         if (mc.getSaveLoader().getSaveLoader(selectedWorld, false).loadWorldInfo().getMapGen() != 0){
//             selectWorld();
//         }
        GeneratorList.gencurrent=GeneratorList.gendefault;
        GeneratorList.feat1current=GeneratorList.feat1default;
        GeneratorList.feat2current=GeneratorList.feat2default;
        GeneratorList.themecurrent=GeneratorList.themedefault;
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
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, stringtranslate.translateKey("nbxlite.continue")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, stringtranslate.translateKey("gui.cancel")));
        controlList.add(generateStructuresButton = new GuiButton(4, width / 2 - 75, 65, 150, 20, stringtranslate.translateKey("selectWorld.mapFeatures")));
        controlList.add(generatorButton = new GuiButton(6, width / 2 - 75, 110, 150, 20, stringtranslate.translateKey(GeneratorList.genname[GeneratorList.gencurrent])));
        controlList.add(generatorExtraButton = new GuiButton(7, width / 2 - 75, 155, 150, 20, stringtranslate.translateKey(extraname)));
        controlList.add(indevButton = new GuiButton(8, width / 2 + 74, 155, 20, 20, stringtranslate.translateKey("nbxlite.plus")));
        indevButton.drawButton = GeneratorList.genplus[GeneratorList.gencurrent];
        func_35363_g();
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
                func_35363_g();
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
