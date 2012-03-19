package net.minecraft.src.nbxlite;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.mod_noBiomesX;

public class GuiNBXlite extends GuiScreen{
    private int leftmargin = 90;
//0-9 - generic; 10-29 - generators; 30-49 - indev
    private GuiScreen parent;
    private GuiButton[] genButtons;

    private GuiButton indevTypeButton;
    private GuiButton indevThemeButton;
    private GuiButton[] indevWidthButton;
    private GuiButton[] indevLengthButton;
    private GuiSliderCustom indevHeightSlider;

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
            controlList.add(indevWidthButton[i]=new GuiButton(30+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 -6, 40, 20, Integer.toString(GeneratorList.sizes[i])));
        }
        for (int i=0; i<4; i++){
            controlList.add(indevLengthButton[i]=new GuiButton(34+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 + 24, 40, 20, Integer.toString(GeneratorList.sizes[i])));
        }
        indevWidthButton[GeneratorList.xcurrent].enabled=false;
        indevLengthButton[GeneratorList.zcurrent].enabled=false;
        controlList.add(indevHeightSlider = new GuiSliderCustom(41, (width / 2 - 75) + leftmargin, height / 6 + 54, mod_noBiomesX.lang.get("depth"), GuiSliderCustom.setSizeValue(mod_noBiomesX.IndevHeight)));
        for (int i=30; i<34; i++){
            indevWidthButton[i-30].drawButton = (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0);
        }
        for (int i=34; i<38; i++){
            indevLengthButton[i-34].drawButton = (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0);
        }
        indevHeightSlider.drawButton = (GeneratorList.gencurrent==1);
        indevTypeButton.drawButton = (GeneratorList.gencurrent==1);
        indevThemeButton.drawButton = (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 1){
            mc.displayGuiScreen(parent);
        }else if (guibutton.id == 0){
            mc.displayGuiScreen(parent);
        }else if (guibutton.id>=10 && guibutton.id<30){
            genButtons[GeneratorList.gencurrent].enabled = true;
            GeneratorList.gencurrent = guibutton.id-10;
            guibutton.enabled = false;
            for (int i=30; i<34; i++){
                indevWidthButton[i-30].drawButton = (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0);
            }
            for (int i=34; i<38; i++){
                indevLengthButton[i-34].drawButton = (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0);
            }
            indevHeightSlider.drawButton = (GeneratorList.gencurrent==1);
            indevTypeButton.drawButton = (GeneratorList.gencurrent==1);
            indevThemeButton.drawButton = (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0);
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
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_noBiomesX.lang.get(GeneratorList.gendesc[GeneratorList.gencurrent]), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        if (GeneratorList.gencurrent==1 || GeneratorList.gencurrent==0){
            drawString(fontRenderer, mod_noBiomesX.lang.get("width"), width / 2 - 120 + leftmargin, height / 6, 0xa0a0a0);
            drawString(fontRenderer, mod_noBiomesX.lang.get("length"), width / 2 - 120 + leftmargin, height / 6 + 30, 0xa0a0a0);
            drawCenteredString(fontRenderer, mod_noBiomesX.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin, height / 6 + 148, 0xa0a0a0);
        }
        if (GeneratorList.gencurrent==1){
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