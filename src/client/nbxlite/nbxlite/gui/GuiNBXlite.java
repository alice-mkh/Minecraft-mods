package net.minecraft.src.nbxlite.gui;

import java.util.List;
import java.util.Collections;
import net.minecraft.src.*;

public class GuiNBXlite extends GuiScreen{
    private String selectedWorld;
    private int number;
    private boolean newworld;
    private int leftmargin = 90;
//0-9 - generic; 10-29 - generators; 30-49 - indev; 50-59 - classic; 60-69 - alpha; 70-79 - beta; 80+ - release;
    private GuiScreen parent;
    private GuiButton[] genButtons;

    private GuiButton indevTypeButton;
    private GuiButton indevThemeButton;
    private GuiButton[] indevWidthButton;
    private GuiButton[] indevLengthButton;
    private GuiSliderCustom indevHeightSlider;
    private GuiButton alphaThemeButton;
    private GuiButton[] betaFeaturesButton;
    private GuiButton[] releaseFeaturesButton;
    private GuiButton newOresButton;
    private GuiButton jungleButton;
    private boolean newores;
    private boolean jungle;
    private boolean olddays = false;

    public GuiNBXlite(GuiScreen guiscreen){
        parent = guiscreen;
        newworld = true;
        newores = ODNBXlite.DefaultNewOres;
        jungle = false;
    }

    public GuiNBXlite(GuiScreen guiscreen, boolean ingame){
        parent = guiscreen;
        newworld = true;
        olddays = true;
        newores = ODNBXlite.DefaultNewOres;
        jungle = false;
    }

    public GuiNBXlite(GuiScreen guiscreen, String world, int i)
    {
        selectedWorld = world;
        parent = guiscreen;
        number = i;
        newworld = false;
        newores = ODNBXlite.DefaultNewOres;
        jungle = false;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        if (!olddays){
            controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_OldDays.lang.get("continue")));
            controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StringTranslate.getInstance().translateKey("gui.cancel")));
        }else{
            controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        }
        genButtons = new GuiButton[GeneratorList.genlength+1];
        for (int i = 0; i<=GeneratorList.genlength; i++){
            controlList.add(genButtons[i] = new GuiButton(10+i, width / 2 - 170, height / 6 + (i * 21), 100, 20, mod_OldDays.lang.get(GeneratorList.genname[i])));
        }
        genButtons[GeneratorList.gencurrent].enabled = false;
//Indev and classic
        controlList.add(indevTypeButton = new GuiButton(39, width / 2 - 75 + leftmargin, height / 6 + 94, 150, 20, mod_OldDays.lang.get("indevType")+": "+mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent])));
        controlList.add(indevThemeButton = new GuiButton(40, width / 2 - 75 + leftmargin, height / 6 + 126, 150, 20, mod_OldDays.lang.get("nbxlite.maptheme.name")+": "+mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent])));
        indevWidthButton = new GuiButton[4];
        indevLengthButton = new GuiButton[4];
        for (int i=0; i<4; i++){
            controlList.add(indevWidthButton[i]=new GuiButton(30+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 - 16, 40, 20, Integer.toString(GeneratorList.sizes[i])));
            controlList.add(indevLengthButton[i]=new GuiButton(34+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 + 14, 40, 20, Integer.toString(GeneratorList.sizes[i])));
            indevWidthButton[i].drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
            indevLengthButton[i].drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
        }
        indevWidthButton[GeneratorList.xcurrent].enabled=false;
        indevLengthButton[GeneratorList.zcurrent].enabled=false;
        controlList.add(indevHeightSlider = new GuiSliderCustom(41, (width / 2 - 75) + leftmargin, height / 6 + 44, mod_OldDays.lang.get("depth")+": ", GuiSliderCustom.setSizeValue(ODNBXlite.IndevHeight)));
        indevHeightSlider.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1);
        indevTypeButton.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1);
        indevThemeButton.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
//Alpha and Infdev
        controlList.add(alphaThemeButton = new GuiButton(60, width / 2 - 75 + leftmargin, height / 6 + 44, 150, 20, mod_OldDays.lang.get("nbxlite.maptheme.name")+": "+mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent])));
        alphaThemeButton.drawButton = GeneratorList.genplus[GeneratorList.gencurrent]==0 && GeneratorList.genfeatures[GeneratorList.gencurrent]==0;
//Beta
        String name;
        betaFeaturesButton = new GuiButton[GeneratorList.feat1length+1];
        for (int i=0; i<=GeneratorList.feat1length; i++){
            name = mod_OldDays.lang.get(GeneratorList.feat1name[i]);
            if (GeneratorList.feat1desc[i]!=""){
                name += " ("+mod_OldDays.lang.get(GeneratorList.feat1desc[i])+")";
            }
            controlList.add(betaFeaturesButton[i]=new GuiButton(70+i, (width / 2 - 115) + leftmargin, height / 6 + (i * 21), 210, 20, name));
            betaFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==1);
        }
        betaFeaturesButton[GeneratorList.feat1current].enabled=false;
//Release
        releaseFeaturesButton = new GuiButton[GeneratorList.feat2length+1];
        for (int i=0; i<=GeneratorList.feat2length; i++){
            name = mod_OldDays.lang.get("nbxlite.releasefeatures"+(i+1));
            name += " ("+mod_OldDays.lang.get("nbxlite.releasefeatures"+(i+1)+".desc")+")";
            controlList.add(releaseFeaturesButton[i]=new GuiButton(80+i, (width / 2 - 115) + leftmargin, height / 6 + ((i + 1) * 21), 210, 20, name));
            releaseFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==2);
        }
        releaseFeaturesButton[GeneratorList.feat2current].enabled=false;
        controlList.add(newOresButton = new GuiButton(2, width / 2 - 75 + leftmargin, height / 6 + 84, 150, 20, (mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + (newores?stringtranslate.translateKey("options.on"):stringtranslate.translateKey("options.off")))));
        newOresButton.drawButton = GeneratorList.genfeatures[GeneratorList.gencurrent]==1 || GeneratorList.genores[GeneratorList.gencurrent] || (GeneratorList.genfeatures[GeneratorList.gencurrent]==2 && GeneratorList.feat2current<4);
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]!=0 || GeneratorList.genores[GeneratorList.gencurrent]){
            if (GeneratorList.genfeatures[GeneratorList.gencurrent]!=0){
                if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1current==4){
                    newOresButton.yPosition=height / 6 + 127;
                }else{
                    newOresButton.yPosition=height / 6 + 135;
                }
                newOresButton.xPosition=width / 2 - 85 + leftmargin;
            }else if (GeneratorList.genplus[GeneratorList.gencurrent]==0){
                newOresButton.yPosition=height / 6 + 84;
                newOresButton.xPosition=width / 2 - 75 + leftmargin;
            }else{
                newOresButton.yPosition=height / 6 + 69;
                newOresButton.xPosition=width / 2 - 75 + leftmargin;
            }
        }
        controlList.add(jungleButton = new GuiButton(3, width / 2 - 85 + leftmargin, height / 6 + 149, 150, 20, (mod_OldDays.lang.get("betaJungle") + ": " + (jungle?stringtranslate.translateKey("options.on"):stringtranslate.translateKey("options.off")))));
        jungleButton.drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1current==4);
    }

    public void selectNBXliteSettings(){
        if (olddays){
            int gen = GeneratorList.genfeatures[GeneratorList.gencurrent];
            int feats = 0;
            int type = 0;
            if(gen==0){
                feats=GeneratorList.genfeats[GeneratorList.gencurrent];
            }
            if(gen==1){
                feats=jungle ? 6 : GeneratorList.feat1current;
            }
            if(gen==2){
                feats=GeneratorList.feat2current;
            }
            if (gen==0 && feats==3){
                type=GeneratorList.typecurrent;
            }
            ODNBXlite.SetGenerator(mod_OldDays.getMinecraftInstance().field_71441_e, gen, feats, GeneratorList.themecurrent, type, ODNBXlite.SnowCovered, newores);
            mod_OldDays.refreshConditionProperties();
            if (gen==0 && feats>=3){
                ODNBXlite.IndevWidthX=GeneratorList.sizes[GeneratorList.xcurrent];
                ODNBXlite.IndevWidthZ=GeneratorList.sizes[GeneratorList.zcurrent];
                ODNBXlite.IndevHeight=indevHeightSlider.getSizeValue();
            }
            mod_OldDays.getMinecraftInstance().renderGlobal.loadRenderers();
        }else{
            ODNBXlite.Generator=GeneratorList.genfeatures[GeneratorList.gencurrent];
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
                ODNBXlite.MapFeatures=GeneratorList.genfeats[GeneratorList.gencurrent];
            }
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
                ODNBXlite.MapFeatures=jungle ? 6 : GeneratorList.feat1current;
            }
            if(GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
                ODNBXlite.MapFeatures=GeneratorList.feat2current;
            }
            ODNBXlite.MapTheme=GeneratorList.themecurrent;
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
                ODNBXlite.IndevMapType=GeneratorList.typecurrent;
            }
            if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV || ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC)){
                ODNBXlite.IndevWidthX=GeneratorList.sizes[GeneratorList.xcurrent];
                ODNBXlite.IndevWidthZ=GeneratorList.sizes[GeneratorList.zcurrent];
                ODNBXlite.IndevHeight=indevHeightSlider.getSizeValue();
            }
            ODNBXlite.GenerateNewOres=newores;
        }
    }

    public void selectWorld()
    {
        ISaveFormat isaveformat = ODNBXlite.saveLoader;
        List saveList = isaveformat.getSaveList();
        Collections.sort(saveList);
        mc.displayGuiScreen(null);
        selectNBXliteSettings();
        String s = selectedWorld;
        if (s == null)
        {
            s = (new StringBuilder()).append("World").append(selectedWorld).toString();
        }
        if (mc.enableSP){
            mc.setController(((SaveFormatComparator)saveList.get(number)).func_75790_f());
            mc.startWorldSSP(s, selectedWorld, null);
//             MinecraftHook.startWorldHook(s, selectedWorld, null);
            mc.displayGuiScreen(null);
        }else{
            mc.func_71371_a(s, selectedWorld, null);
        }
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 1){
            GuiCreateWorld2.setDefaultNBXliteSettings();
            mc.displayGuiScreen(parent);
        }else if (guibutton.id == 0){
            if (!newworld){
                selectWorld();
                return;
            }
            selectNBXliteSettings();
            mc.displayGuiScreen(parent);
        }else if (guibutton.id==2){
            StringTranslate stringtranslate = StringTranslate.getInstance();
            newores=!newores;
            newOresButton.displayString=mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + (newores?stringtranslate.translateKey("options.on"):stringtranslate.translateKey("options.off"));
        }else if (guibutton.id==3){
            StringTranslate stringtranslate = StringTranslate.getInstance();
            jungle=!jungle;
            jungleButton.displayString=mod_OldDays.lang.get("betaJungle") + ": " + (jungle?stringtranslate.translateKey("options.on"):stringtranslate.translateKey("options.off"));
        }else if (guibutton.id>=10 && guibutton.id<30){
            StringTranslate stringtranslate = StringTranslate.getInstance();
            genButtons[GeneratorList.gencurrent].enabled = true;
            GeneratorList.gencurrent = guibutton.id-10;
            guibutton.enabled = false;
            for (int i=0; i<4; i++){
                indevWidthButton[i].drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
                indevLengthButton[i].drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
            }
            indevHeightSlider.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1);
            indevTypeButton.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1);
            indevThemeButton.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
            alphaThemeButton.drawButton = GeneratorList.genplus[GeneratorList.gencurrent]==0 && GeneratorList.genfeatures[GeneratorList.gencurrent]==0;
            newOresButton.drawButton = GeneratorList.genfeatures[GeneratorList.gencurrent]==1 || GeneratorList.genores[GeneratorList.gencurrent] || (GeneratorList.genfeatures[GeneratorList.gencurrent]==2 && GeneratorList.feat2current<3);
            for (int i=0; i<=GeneratorList.feat1length; i++){
                betaFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==1);
            }
            for (int i=0; i<=GeneratorList.feat2length; i++){
                releaseFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==2);
            }
            if (GeneratorList.genfeatures[GeneratorList.gencurrent]!=0 || GeneratorList.genores[GeneratorList.gencurrent]){
                newOresButton.displayString=mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + (newores?stringtranslate.translateKey("options.on"):stringtranslate.translateKey("options.off"));
                if (GeneratorList.genfeatures[GeneratorList.gencurrent]!=0){
                    if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1current==4){
                        newOresButton.yPosition=height / 6 + 127;
                    }else{
                        newOresButton.yPosition=height / 6 + 135;
                    }
                    newOresButton.xPosition=width / 2 - 85 + leftmargin;
                }else if (GeneratorList.genplus[GeneratorList.gencurrent]==0){
                    newOresButton.yPosition=height / 6 + 84;
                    newOresButton.xPosition=width / 2 - 75 + leftmargin;
                }else{
                    newOresButton.yPosition=height / 6 + 69;
                    newOresButton.xPosition=width / 2 - 75 + leftmargin;
                }
            }
            jungleButton.drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1current==4);
        }else
//Indev
        if (guibutton.id == 39){
            if (GeneratorList.typecurrent<GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent=0;
            }
            indevTypeButton.displayString = mod_OldDays.lang.get("indevType")+": "+mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent]);
        }else if (guibutton.id == 40 || guibutton.id == 60){
            if (GeneratorList.themecurrent<GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent=0;
            }
            guibutton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name")+": "+mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
        }else if (guibutton.id>=30 && guibutton.id<=33){
            indevWidthButton[GeneratorList.xcurrent].enabled = true;
            GeneratorList.xcurrent = guibutton.id-30;
            guibutton.enabled = false;
        }else if (guibutton.id>=34 && guibutton.id<=38){
            indevLengthButton[GeneratorList.zcurrent].enabled = true;
            GeneratorList.zcurrent = guibutton.id-34;
            guibutton.enabled = false;
        }else
//Alpha and Infdev
//Beta
        if (guibutton.id>=70 && guibutton.id<=79){
            betaFeaturesButton[GeneratorList.feat1current].enabled = true;
            GeneratorList.feat1current = guibutton.id-70;
            guibutton.enabled = false;
            newOresButton.drawButton = true;
            if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1current==4){
                newOresButton.yPosition=height / 6 + 127;
            }else{
                newOresButton.yPosition=height / 6 + 135;
            }
            jungleButton.drawButton = GeneratorList.feat1current==4;
        }
//Release
        if (guibutton.id>=80){
            releaseFeaturesButton[GeneratorList.feat2current].enabled = true;
            GeneratorList.feat2current = guibutton.id-80;
            guibutton.enabled = false;
            newOresButton.drawButton = GeneratorList.feat2current<4;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.gendesc[GeneratorList.gencurrent]), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2){
            drawString(fontRenderer, mod_OldDays.lang.get("width")+": ", width / 2 - 120 + leftmargin, height / 6 - 10, 0xa0a0a0);
            drawString(fontRenderer, mod_OldDays.lang.get("length")+": ", width / 2 - 120 + leftmargin, height / 6 + 20, 0xa0a0a0);
            drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin, height / 6 + 148, 0xa0a0a0);
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
            if (GeneratorList.typecurrent==2){
                int count = (indevHeightSlider.getSizeValue() - 64) / 48 + 1;
                if (count==1){
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get("1Layer"), width / 2 + leftmargin, height / 6 + 114, 0xa0a0a0);
                }else{
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get(count+"Layers"), width / 2 + leftmargin, height / 6 + 114, 0xa0a0a0);
                }
            }
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==0 && GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin, height / 6 + 67, 0xa0a0a0);
        }
        super.drawScreen(i, j, f); 
    }
}