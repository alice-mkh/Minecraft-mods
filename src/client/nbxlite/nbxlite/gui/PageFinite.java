package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageFinite extends Page{
    private boolean indev;
    private boolean origIndev;
    private GuiButton indevTypeButton;
    private GuiButton indevThemeButton;
    private GuiButton[] indevWidthButton;
    private GuiButton[] indevLengthButton;
    private GuiButton indevSizeButton;
    private GuiButton indevShapeButton;
    private GuiSliderCustom indevHeightSlider;
    private GuiButton toggleButton;
    private GuiButton newOresButton;
    private boolean newores;

    public PageFinite(GuiNBXlite parent, boolean indev){
        super(parent);
        this.indev = indev;
        indevWidthButton = new GuiButton[4];
        indevLengthButton = new GuiButton[4];
        origIndev = mc.indevShapeSize;
        newores = ODNBXlite.DefaultNewOres;
    }

    @Override
    public void initButtons(){
        buttonList.add(indevTypeButton = new GuiButton(0, width / 2 - 75 + leftmargin, height / 6 + 94, 150, 20,""));
        buttonList.add(indevThemeButton = new GuiButton(1, width / 2 - 75 + leftmargin, height / 6 + 126, 150, 20, ""));
        for (int i = 0; i < 4; i++){
            buttonList.add(indevWidthButton[i]=new GuiButton(2+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 - 16, 40, 20, ""));
            buttonList.add(indevLengthButton[i]=new GuiButton(6+i, (width / 2 - 82+(41*i)) + leftmargin, height / 6 + 14, 40, 20, ""));
        }
        buttonList.add(indevShapeButton = new GuiButton(10, width / 2 - 75 + leftmargin, height / 6 - 16, 150, 20, ""));
        buttonList.add(indevSizeButton = new GuiButton(11, width / 2 - 75 + leftmargin, height / 6 + 14, 150, 20, ""));
        buttonList.add(toggleButton = new GuiButton(12, width - 35, height / 6 + 14, 20, 20, ""));
        buttonList.add(indevHeightSlider = new GuiSliderCustom(13, (width / 2 - 75) + leftmargin, height / 6 + 44, mod_OldDays.lang.get("depth") + ": ", GuiSliderCustom.setSizeValue(ODNBXlite.IndevHeight)));
        buttonList.add(newOresButton = new GuiButton(14, width / 2 - 75 + leftmargin, height / 6 + 84, 150, 20, ""));
        indevWidthButton[GeneratorList.xcurrent].enabled=false;
        indevLengthButton[GeneratorList.zcurrent].enabled=false;
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
        indevSizeButton.displayString = mod_OldDays.lang.get("indevSize") + ": " + mod_OldDays.lang.get(GeneratorList.sizename[GeneratorList.sizecurrent]);
        indevShapeButton.displayString = mod_OldDays.lang.get("indevShape") + ": " + mod_OldDays.lang.get(GeneratorList.shapename[GeneratorList.shapecurrent]);
        indevThemeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
        indevTypeButton.displayString = mod_OldDays.lang.get("indevType") + ": " + mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent]);
        for (int i = 0; i < 4; i++){
            indevWidthButton[i].displayString = Integer.toString(GeneratorList.sizes[i]);
            indevLengthButton[i].displayString = indevWidthButton[i].displayString;
        }
        toggleButton.displayString = origIndev ? "+" : "-";
    }

    @Override
    public void updateButtonVisibility(){
        for (int i = 0; i < 4; i++){
            indevWidthButton[i].drawButton = !origIndev;
            indevLengthButton[i].drawButton = !origIndev;
        }
        indevHeightSlider.drawButton = !origIndev && indev;
        indevShapeButton.drawButton = origIndev && indev;
        indevSizeButton.drawButton = origIndev;
        indevTypeButton.drawButton = indev;
    }

    @Override
    public void updateButtonPosition(){
        newOresButton.yPosition=height / 6 + (origIndev ? (!indev ? 68 : 119) : 69);
        newOresButton.xPosition=width / 2 - (origIndev ? 85 : 75) + leftmargin;
        int[] xcoords = new int[]{94, -16, 14, 126};
        if (origIndev){
            xcoords = !indev ? new int[]{0, 0, 44, 92} : new int[]{4, 28, 52, 76};
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

    @Override
    public void drawScreen(int i, int j, float f){
        if (!origIndev){
            drawString(fontRenderer, mod_OldDays.lang.get("width")+": ", width / 2 - 120 + leftmargin, height / 6 - 10, 0xa0a0a0);
            drawString(fontRenderer, mod_OldDays.lang.get("length")+": ", width / 2 - 120 + leftmargin, height / 6 + 20, 0xa0a0a0);
        }
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin - (origIndev ? 10 : 0), indevThemeButton.yPosition + 22, 0xa0a0a0);
        if (indev && !origIndev){
            if (GeneratorList.typecurrent==2){
                int count = (indevHeightSlider.getSizeValue() - 64) / 48 + 1;
                if (count==1){
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get("1Layer"), width / 2 + leftmargin, height / 6 + 114, 0xa0a0a0);
                }else{
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get(count+"Layers"), width / 2 + leftmargin, height / 6 + 114, 0xa0a0a0);
                }
            }
        }
        super.drawScreen(i, j, f);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        if (!guibutton.enabled){
            return;
        }
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == indevTypeButton){
            if (GeneratorList.typecurrent < GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent = 0;
            }
        }else if (guibutton == indevThemeButton){
            if (GeneratorList.themecurrent < GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent = 0;
            }
        }else if (guibutton.id >= 2 && guibutton.id <= 5){
            indevWidthButton[GeneratorList.xcurrent].enabled = true;
            GeneratorList.xcurrent = guibutton.id - 2;
            guibutton.enabled = false;
        }else if (guibutton.id >= 6 && guibutton.id <= 9){
            indevLengthButton[GeneratorList.zcurrent].enabled = true;
            GeneratorList.zcurrent = guibutton.id - 6;
            guibutton.enabled = false;
        }else if (guibutton == indevShapeButton){
            if (GeneratorList.shapecurrent < GeneratorList.shapename.length - 1){
                GeneratorList.shapecurrent++;
            }else{
                GeneratorList.shapecurrent = 0;
            }
        }else if (guibutton == indevSizeButton){
            if (GeneratorList.sizecurrent < GeneratorList.sizename.length - 1){
                GeneratorList.sizecurrent++;
            }else{
                GeneratorList.sizecurrent = 0;
            }
        }else if (guibutton == toggleButton){
            origIndev = !origIndev;
            mc.indevShapeSize = origIndev;
            mod_OldDays.saveman.saveCoreProperties();
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
    }

    @Override
    public void selectSettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_BIOMELESS;
        ODNBXlite.MapFeatures = indev ? 3 : 4;
        ODNBXlite.MapTheme=GeneratorList.themecurrent;
        if (indev){
            ODNBXlite.IndevMapType=GeneratorList.typecurrent;
        }
        if (origIndev){
            ODNBXlite.IndevWidthX = 128 << GeneratorList.sizecurrent;
            ODNBXlite.IndevWidthZ = 128 << GeneratorList.sizecurrent;
            ODNBXlite.IndevHeight = 64;
            if (ODNBXlite.MapFeatures == ODNBXlite.FEATURES_INDEV){
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
        ODNBXlite.GenerateNewOres=newores;
    }
}