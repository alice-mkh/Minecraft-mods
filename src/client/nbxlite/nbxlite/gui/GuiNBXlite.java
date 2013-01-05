package net.minecraft.src.nbxlite.gui;

import java.util.List;
import java.util.Random;
import java.util.Collections;
import net.minecraft.src.*;

public class GuiNBXlite extends GuiScreen{
    private boolean origIndev;

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
    private GuiButton indevSizeButton;
    private GuiButton indevShapeButton;
    private GuiSliderCustom indevHeightSlider;
    private GuiButton alphaThemeButton;
    private GuiButton[] betaFeaturesButton;
    private GuiButton[] releaseFeaturesButton;
    private GuiButton newOresButton;
    private GuiButton jungleButton;
    private GuiButton toggleButton;
    private boolean newores;
    private boolean jungle;

    public GuiNBXlite(GuiScreen guiscreen){
        parent = guiscreen;
        newworld = true;
        newores = ODNBXlite.DefaultNewOres;
        jungle = false;
        origIndev = mc.indevShapeSize;
    }

    public GuiNBXlite(GuiScreen guiscreen, String world, int i)
    {
        this(guiscreen);
        selectedWorld = world;
        number = i;
        newworld = false;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_OldDays.lang.get("continue")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StringTranslate.getInstance().translateKey("gui.cancel")));
        genButtons = new GuiButton[GeneratorList.genlength+1];
        indevWidthButton = new GuiButton[4];
        indevLengthButton = new GuiButton[4];
        betaFeaturesButton = new GuiButton[GeneratorList.feat1length+1];
        releaseFeaturesButton = new GuiButton[GeneratorList.feat2length+1];
        for (int i = 0; i<=GeneratorList.genlength; i++){
            controlList.add(genButtons[i] = new GuiButton(10+i, width / 2 - 170, height / 6 + (i * 21), 100, 20, ""));
        }
        controlList.add(indevTypeButton = new GuiButton(39, width / 2 - 75 + leftmargin, height / 6 + 94, 150, 20,""));
        controlList.add(indevThemeButton = new GuiButton(40, width / 2 - 75 + leftmargin, height / 6 + 126, 150, 20, ""));
        for (int i=0; i<4; i++){
            controlList.add(indevWidthButton[i]=new GuiButton(30+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 - 16, 40, 20, ""));
            controlList.add(indevLengthButton[i]=new GuiButton(34+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 + 14, 40, 20, ""));
        }
        controlList.add(indevShapeButton = new GuiButton(42, width / 2 - 75 + leftmargin, height / 6 - 16, 150, 20, ""));
        controlList.add(indevSizeButton = new GuiButton(43, width / 2 - 75 + leftmargin, height / 6 + 14, 150, 20, ""));
        controlList.add(toggleButton = new GuiButton(44, width - 35, height / 6 + 14, 20, 20, ""));
        controlList.add(indevHeightSlider = new GuiSliderCustom(41, (width / 2 - 75) + leftmargin, height / 6 + 44, mod_OldDays.lang.get("depth") + ": ", GuiSliderCustom.setSizeValue(ODNBXlite.IndevHeight)));
        controlList.add(alphaThemeButton = new GuiButton(60, width / 2 - 75 + leftmargin, height / 6 + 44, 150, 20, ""));
        for (int i=0; i<=GeneratorList.feat1length; i++){
            controlList.add(betaFeaturesButton[i]=new GuiButton(70+i, (width / 2 - 115) + leftmargin, height / 6 + ((i - 1) * 21), 210, 20, ""));
        }
        for (int i=0; i<=GeneratorList.feat2length; i++){
            controlList.add(releaseFeaturesButton[i]=new GuiButton(80+i, (width / 2 - 115) + leftmargin, height / 6 + (i * 21), 210, 20, ""));
        }
        controlList.add(newOresButton = new GuiButton(2, width / 2 - 75 + leftmargin, height / 6 + 84, 150, 20, ""));
        controlList.add(jungleButton = new GuiButton(3, width / 2 - 85 + leftmargin, height / 6 + 149, 150, 20, ""));
        genButtons[GeneratorList.gencurrent].enabled = false;
        indevWidthButton[GeneratorList.xcurrent].enabled=false;
        indevLengthButton[GeneratorList.zcurrent].enabled=false;
        betaFeaturesButton[GeneratorList.feat1current].enabled=false;
        releaseFeaturesButton[GeneratorList.feat2current].enabled=false;
        updateButtonText();
        updateButtonVisibility();
        updateButtonPosition();
    }

    public void selectNBXliteSettings(){
        ODNBXlite.Generator=GeneratorList.genfeatures[GeneratorList.gencurrent];
        if(GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            ODNBXlite.MapFeatures=GeneratorList.genfeats[GeneratorList.gencurrent];
        }
        if(GeneratorList.genfeatures[GeneratorList.gencurrent]==1){
            ODNBXlite.MapFeatures=jungle ? ODNBXlite.FEATURES_JUNGLE : GeneratorList.feat1current;
        }
        if(GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
            ODNBXlite.MapFeatures=GeneratorList.feat2current;
        }
        ODNBXlite.MapTheme=GeneratorList.themecurrent;
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
            ODNBXlite.IndevMapType=GeneratorList.typecurrent;
        }
        if (ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV || ODNBXlite.MapFeatures==ODNBXlite.FEATURES_CLASSIC)){
            if (origIndev){
                ODNBXlite.IndevWidthX = 128 << GeneratorList.sizecurrent;
                ODNBXlite.IndevWidthZ = 128 << GeneratorList.sizecurrent;
                ODNBXlite.IndevHeight = 64;
                if (ODNBXlite.MapFeatures==ODNBXlite.FEATURES_INDEV){
                    if (GeneratorList.shapecurrent == 1){
                        ODNBXlite.IndevWidthX /= 2;
                        ODNBXlite.IndevWidthZ <<= 1;
                    }else if (GeneratorList.shapecurrent == 2){
                        ODNBXlite.IndevWidthZ /= 2;
                        ODNBXlite.IndevWidthX /= 2;
                        ODNBXlite.IndevHeight = 256;
                    }
                }
            }else{
                ODNBXlite.IndevWidthX=GeneratorList.sizes[GeneratorList.xcurrent];
                ODNBXlite.IndevWidthZ=GeneratorList.sizes[GeneratorList.zcurrent];
                ODNBXlite.IndevHeight=indevHeightSlider.getSizeValue();
            }
        }
        ODNBXlite.GenerateNewOres=newores;
        ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
        ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
        if(ODNBXlite.Generator==ODNBXlite.GEN_BIOMELESS && (ODNBXlite.MapTheme==ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS) && ODNBXlite.MapFeatures==ODNBXlite.FEATURES_ALPHA11201){
            ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme==ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
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
            mc.setController(((SaveFormatComparator)saveList.get(number)).getEnumGameType());
            mc.startWorldSSP(s, selectedWorld, null);
//             MinecraftHook.startWorldHook(s, selectedWorld, null);
            mc.displayGuiScreen(null);
        }else{
            mc.launchIntegratedServer(s, selectedWorld, null);
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
            ((GuiCreateWorld2)parent).fixHardcoreButtons();
        }else if (guibutton.id == 0){
            if (!newworld){
                selectWorld();
                return;
            }
            selectNBXliteSettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id==2){
            newores=!newores;
            updateButtonText();
        }else if (guibutton.id==3){
            jungle=!jungle;
            updateButtonText();
        }else if (guibutton.id>=10 && guibutton.id<30){
            StringTranslate stringtranslate = StringTranslate.getInstance();
            genButtons[GeneratorList.gencurrent].enabled = true;
            GeneratorList.gencurrent = guibutton.id-10;
            guibutton.enabled = false;
        }else
//Indev
        if (guibutton.id == 39){
            if (GeneratorList.typecurrent<GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent=0;
            }
        }else if (guibutton.id == 40 || guibutton.id == 60){
            if (GeneratorList.themecurrent<GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent=0;
            }
        }else if (guibutton.id>=30 && guibutton.id<=33){
            indevWidthButton[GeneratorList.xcurrent].enabled = true;
            GeneratorList.xcurrent = guibutton.id-30;
            guibutton.enabled = false;
        }else if (guibutton.id>=34 && guibutton.id<=38){
            indevLengthButton[GeneratorList.zcurrent].enabled = true;
            GeneratorList.zcurrent = guibutton.id-34;
            guibutton.enabled = false;
        }else if (guibutton.id==42){
            if (GeneratorList.shapecurrent<GeneratorList.shapename.length - 1){
                GeneratorList.shapecurrent++;
            }else{
                GeneratorList.shapecurrent=0;
            }
        }else if (guibutton.id==43){
            if (GeneratorList.sizecurrent<GeneratorList.sizename.length - 1){
                GeneratorList.sizecurrent++;
            }else{
                GeneratorList.sizecurrent=0;
            }
        }else if (guibutton.id==44){
            origIndev = !origIndev;
            mc.indevShapeSize = origIndev;
            mod_OldDays.saveman.saveCoreProperties();
        }else
//Alpha and Infdev
//Beta
        if (guibutton.id>=70 && guibutton.id<=79){
            betaFeaturesButton[GeneratorList.feat1current].enabled = true;
            GeneratorList.feat1current = guibutton.id-70;
            guibutton.enabled = false;
        }
//Release
        if (guibutton.id>=80){
            releaseFeaturesButton[GeneratorList.feat2current].enabled = true;
            GeneratorList.feat2current = guibutton.id-80;
            guibutton.enabled = false;
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.gendesc[GeneratorList.gencurrent]), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2){
            if (!origIndev){
                drawString(fontRenderer, mod_OldDays.lang.get("width")+": ", width / 2 - 120 + leftmargin, height / 6 - 10, 0xa0a0a0);
                drawString(fontRenderer, mod_OldDays.lang.get("length")+": ", width / 2 - 120 + leftmargin, height / 6 + 20, 0xa0a0a0);
            }
            drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin - (origIndev && GeneratorList.genplus[GeneratorList.gencurrent] > 0 ? 10 : 0), indevThemeButton.yPosition + 22, 0xa0a0a0);
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 && !origIndev){
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

    private void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
        jungleButton.displayString = mod_OldDays.lang.get("betaJungle") + ": " + stringtranslate.translateKey("options." + (jungle ? "on" : "off"));
        indevSizeButton.displayString = mod_OldDays.lang.get("indevSize") + ": " + mod_OldDays.lang.get(GeneratorList.sizename[GeneratorList.sizecurrent]);
        indevShapeButton.displayString = mod_OldDays.lang.get("indevShape") + ": " + mod_OldDays.lang.get(GeneratorList.shapename[GeneratorList.shapecurrent]);
        indevThemeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
        alphaThemeButton.displayString = indevThemeButton.displayString;
        indevTypeButton.displayString = mod_OldDays.lang.get("indevType") + ": " + mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent]);
        for (int i = 0; i <= GeneratorList.genlength; i++){
            genButtons[i].displayString = mod_OldDays.lang.get(GeneratorList.genname[i]);
        }
        for (int i = 0; i < 4; i++){
            indevWidthButton[i].displayString = Integer.toString(GeneratorList.sizes[i]);
            indevLengthButton[i].displayString = indevWidthButton[i].displayString;
        }
        for (int i = 0; i <= GeneratorList.feat1length; i++){
            String name = mod_OldDays.lang.get(GeneratorList.feat1name[i]);
            if (GeneratorList.feat1desc[i]!=""){
                name += " (" + mod_OldDays.lang.get(GeneratorList.feat1desc[i]) + ")";
            }
            betaFeaturesButton[i].displayString = name;
        }
        for (int i = 0; i <= GeneratorList.feat2length; i++){
            String name = mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1)) + " (" + mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1)+".desc") + ")";
            releaseFeaturesButton[i].displayString = name;
        }
        toggleButton.displayString = origIndev ? "+" : "-";
    }

    private void updateButtonVisibility(){
        boolean indev = GeneratorList.genplus[GeneratorList.gencurrent]==1;
        boolean classic = GeneratorList.genplus[GeneratorList.gencurrent]==2;
        boolean alpha = GeneratorList.genplus[GeneratorList.gencurrent]==0 && GeneratorList.genfeatures[GeneratorList.gencurrent]==0;
        boolean beta = GeneratorList.genfeatures[GeneratorList.gencurrent]==1;
        boolean release = GeneratorList.genfeatures[GeneratorList.gencurrent]==2;
        for (int i = 0; i < 4; i++){
            indevWidthButton[i].drawButton = !origIndev && (indev || classic);
            indevLengthButton[i].drawButton = !origIndev && (indev || classic);
        }
        indevHeightSlider.drawButton = !origIndev && indev;
        indevTypeButton.drawButton = indev;
        indevShapeButton.drawButton = origIndev && indev;
        indevSizeButton.drawButton = origIndev && (indev || classic);
        indevThemeButton.drawButton = indev || classic;
        alphaThemeButton.drawButton = alpha;
        for (int i = 0; i <= GeneratorList.feat1length; i++){
            betaFeaturesButton[i].drawButton = beta;
        }
        for (int i = 0; i <= GeneratorList.feat2length; i++){
            releaseFeaturesButton[i].drawButton = release;
        }
        newOresButton.drawButton = beta || GeneratorList.genores[GeneratorList.gencurrent] || (release && GeneratorList.feat2current<6);
        jungleButton.drawButton = beta && GeneratorList.feat1current==5;
        toggleButton.drawButton = indev || classic;
    }

    private void updateButtonPosition(){
        boolean classic = GeneratorList.genplus[GeneratorList.gencurrent]==2;
        if (GeneratorList.genfeatures[GeneratorList.gencurrent]==0){
            newOresButton.yPosition=height / 6 + (GeneratorList.genplus[GeneratorList.gencurrent]==0 ? 84 : (origIndev ? (classic ? 68 : 119) : 69));
            newOresButton.xPosition=width / 2 - (origIndev && GeneratorList.genplus[GeneratorList.gencurrent] > 0 ? 85 : 75) + leftmargin;
        }else{
            if (GeneratorList.genfeatures[GeneratorList.gencurrent]==1 && GeneratorList.feat1current==5){
                newOresButton.yPosition=height / 6 + 127;
            }else if (GeneratorList.genfeatures[GeneratorList.gencurrent]==2){
                newOresButton.yPosition=height / 6 + 149;
            }else{
                newOresButton.yPosition=height / 6 + 135;
            }
            newOresButton.xPosition=width / 2 - 85 + leftmargin;
        }
        int[] xcoords = new int[]{94, -16, 14, 126};
        if (origIndev){
            xcoords = classic ? new int[]{0, 0, 44, 92} : new int[]{4, 28, 52, 76};
        }
        indevTypeButton.yPosition = height / 6 + xcoords[0];
        indevShapeButton.yPosition = height / 6 + xcoords[1];
        indevSizeButton.yPosition = height / 6 + xcoords[2];
        indevThemeButton.yPosition = height / 6 + xcoords[3];
        for (int i = 0; i < 4; i++){
            indevWidthButton[i].yPosition = height / 6 + xcoords[1];
            indevLengthButton[i].yPosition = height / 6 + xcoords[2];
        }
        int xpos = width / 2 - (origIndev ? 85 : 75) + leftmargin;
        indevTypeButton.xPosition = xpos;
        indevShapeButton.xPosition = xpos;
        indevSizeButton.xPosition = xpos;
        indevThemeButton.xPosition = xpos;
    }
}