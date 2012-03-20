package net.minecraft.src.nbxlite;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.mod_noBiomesX;

public class GuiNBXlite extends GuiScreen{
    private int leftmargin = 90;
//0-9 - generic; 10-29 - generators; 30-49 - indev; 50-59 - classic; 60-79 - beta; 80+ - release;
    private GuiScreen parent;
    private GuiButton[] genButtons;

    private GuiButton indevTypeButton;
    private GuiButton indevThemeButton;
    private GuiButton[] indevWidthButton;
    private GuiButton[] indevLengthButton;
    private GuiSliderCustom indevHeightSlider;
    private GuiButton[] betaFeaturesButton;
    private GuiButton[] releaseFeaturesButton;

    public GuiNBXlite(GuiScreen guiscreen){
        parent = guiscreen;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_noBiomesX.lang.get("continue")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StringTranslate.getInstance().translateKey("gui.cancel")));
        genButtons = new GuiButton[GeneratorList.genlength+1];
        for (int i = 0; i<=GeneratorList.genlength; i++){
            controlList.add(genButtons[i] = new GuiButton(10+i, width / 2 - 170, height / 6 + (i * 21), 100, 20, mod_noBiomesX.lang.get(GeneratorList.genname[i])));
        }
        genButtons[GeneratorList.gendefault].enabled = false;
        GeneratorList.gencurrent = GeneratorList.gendefault;
//Indev and classic
        controlList.add(indevTypeButton = new GuiButton(39, width / 2 - 75 + leftmargin, height / 6 + 84, 150, 20, mod_noBiomesX.lang.get("indevType")+mod_noBiomesX.lang.get(GeneratorList.typename[GeneratorList.typecurrent])));
        controlList.add(indevThemeButton = new GuiButton(40, width / 2 - 75 + leftmargin, height / 6 + 124, 150, 20, mod_noBiomesX.lang.get("theme")+mod_noBiomesX.lang.get(GeneratorList.themename[GeneratorList.themecurrent])));
        indevWidthButton = new GuiButton[4];
        indevLengthButton = new GuiButton[4];
        for (int i=0; i<4; i++){
            controlList.add(indevWidthButton[i]=new GuiButton(30+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 - 6, 40, 20, Integer.toString(GeneratorList.sizes[i])));
            controlList.add(indevLengthButton[i]=new GuiButton(34+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 + 24, 40, 20, Integer.toString(GeneratorList.sizes[i])));
            indevWidthButton[i].drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
            indevLengthButton[i].drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
        }
        indevWidthButton[GeneratorList.xcurrent].enabled=false;
        indevLengthButton[GeneratorList.zcurrent].enabled=false;
        controlList.add(indevHeightSlider = new GuiSliderCustom(41, (width / 2 - 75) + leftmargin, height / 6 + 54, mod_noBiomesX.lang.get("depth"), GuiSliderCustom.setSizeValue(mod_noBiomesX.IndevHeight)));
        indevHeightSlider.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1);
        indevTypeButton.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1);
        indevThemeButton.drawButton = (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2);
//Beta
        betaFeaturesButton = new GuiButton[GeneratorList.feat1length+1];
        for (int i=0; i<=GeneratorList.feat1length; i++){
            controlList.add(betaFeaturesButton[i]=new GuiButton(60+i, (width / 2 - 75) + leftmargin, height / 6 + 20 + (i * 21), 150, 20, mod_noBiomesX.lang.get(GeneratorList.feat1name[i])));
            betaFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==1);
        }
        betaFeaturesButton[GeneratorList.feat1current].enabled=false;
//Release
        releaseFeaturesButton = new GuiButton[GeneratorList.feat2length+1];
        for (int i=0; i<=GeneratorList.feat2length; i++){
            controlList.add(releaseFeaturesButton[i]=new GuiButton(80+i, (width / 2 - 75) + leftmargin, height / 6 + 20 + (i * 21), 150, 20, mod_noBiomesX.lang.get(GeneratorList.feat2name[i])));
            releaseFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==2);
        }
        releaseFeaturesButton[GeneratorList.feat2current].enabled=false;
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 1){
            mc.displayGuiScreen(parent);
        }else if (guibutton.id == 0){
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
            if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures==3){
                mod_noBiomesX.IndevMapType=GeneratorList.typecurrent;
            }
            if (mod_noBiomesX.Generator==0 && mod_noBiomesX.MapFeatures>=3){
                mod_noBiomesX.IndevWidthX=GeneratorList.sizes[GeneratorList.xcurrent];
                mod_noBiomesX.IndevWidthZ=GeneratorList.sizes[GeneratorList.zcurrent];
                mod_noBiomesX.IndevHeight=indevHeightSlider.getSizeValue();
            }
            mc.displayGuiScreen(parent);
        }else if (guibutton.id>=10 && guibutton.id<30){
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
            for (int i=0; i<=GeneratorList.feat1length; i++){
                betaFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==1);
            }
            for (int i=0; i<=GeneratorList.feat2length; i++){
                releaseFeaturesButton[i].drawButton = (GeneratorList.genfeatures[GeneratorList.gencurrent]==2);
            }
        }else
//Indev
        if (guibutton.id == 39){
            if (GeneratorList.typecurrent<GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent=0;
            }
            indevTypeButton.displayString = mod_noBiomesX.lang.get("indevType")+mod_noBiomesX.lang.get(GeneratorList.typename[GeneratorList.typecurrent]);
        }else if (guibutton.id == 40){
            if (GeneratorList.themecurrent<GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent=0;
            }
            indevThemeButton.displayString = mod_noBiomesX.lang.get("theme")+mod_noBiomesX.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
        }else if (guibutton.id>=30 && guibutton.id<=33){
            indevWidthButton[GeneratorList.xcurrent].enabled = true;
            GeneratorList.xcurrent = guibutton.id-30;
            guibutton.enabled = false;
        }else if (guibutton.id>=34 && guibutton.id<=38){
            indevLengthButton[GeneratorList.zcurrent].enabled = true;
            GeneratorList.zcurrent = guibutton.id-34;
            guibutton.enabled = false;
        }else
//Beta
        if (guibutton.id>=60 && guibutton.id<=79){
            betaFeaturesButton[GeneratorList.feat1current].enabled = true;
            GeneratorList.feat1current = guibutton.id-60;
            guibutton.enabled = false;
        }
//Release
        if (guibutton.id>=80){
            releaseFeaturesButton[GeneratorList.feat2current].enabled = true;
            GeneratorList.feat2current = guibutton.id-80;
            guibutton.enabled = false;
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_noBiomesX.lang.get(GeneratorList.gendesc[GeneratorList.gencurrent]), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1 || GeneratorList.genplus[GeneratorList.gencurrent]==2){
            drawString(fontRenderer, mod_noBiomesX.lang.get("width"), width / 2 - 120 + leftmargin, height / 6, 0xa0a0a0);
            drawString(fontRenderer, mod_noBiomesX.lang.get("length"), width / 2 - 120 + leftmargin, height / 6 + 30, 0xa0a0a0);
            drawCenteredString(fontRenderer, mod_noBiomesX.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin, height / 6 + 148, 0xa0a0a0);
        }
        if (GeneratorList.genplus[GeneratorList.gencurrent]==1){
            if (GeneratorList.typecurrent==2){
                int count = (indevHeightSlider.getSizeValue() - 64) / 48 + 1;
                if (count==1){
                    drawCenteredString(fontRenderer, mod_noBiomesX.lang.get("1Layer"), width / 2 + leftmargin, height / 6 + 108, 0xa0a0a0);
                }else{
                    drawCenteredString(fontRenderer, mod_noBiomesX.lang.get(count+"Layers"), width / 2 + leftmargin, height / 6 + 108, 0xa0a0a0);
                }
            }
        }
        super.drawScreen(i, j, f); 
    }
}