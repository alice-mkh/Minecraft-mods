package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageFinite extends Page{
    private boolean indev;
    private boolean origIndev;
    private GuiButton typeButton;
    private GuiButton themeButton;
    private GuiButton[] widthButtons;
    private GuiButton[] lengthButtons;
    private GuiButton sizeButton;
    private GuiButton shapeButton;
    private GuiSliderCustom heightSlider;
    private GuiButton toggleButton;
    private GuiButton newOresButton;
    private boolean newores;

    public PageFinite(GuiNBXlite parent, boolean indev){
        super(parent);
        this.indev = indev;
        widthButtons = new GuiButton[4];
        lengthButtons = new GuiButton[4];
        origIndev = mc.indevShapeSize;
        newores = ODNBXlite.GenerateNewOres;
    }

    @Override
    public void initButtons(){
        buttonList.add(typeButton = new GuiButton(0, width / 2 - 75 + leftmargin, 0, 150, 20,""));
        buttonList.add(themeButton = new GuiButton(1, width / 2 - 75 + leftmargin, 0, 150, 20, ""));
        for (int i = 0; i < 4; i++){
            buttonList.add(widthButtons[i]=new GuiButton(2+i, (width / 2 - 82+(41*i)) + leftmargin, 0, 40, 20, ""));
            buttonList.add(lengthButtons[i]=new GuiButton(6+i, (width / 2 - 82+(41*i)) + leftmargin, 0, 40, 20, ""));
        }
        buttonList.add(shapeButton = new GuiButton(10, width / 2 - 75 + leftmargin, 0, 150, 20, ""));
        buttonList.add(sizeButton = new GuiButton(11, width / 2 - 75 + leftmargin, 0, 150, 20, ""));
        buttonList.add(toggleButton = new GuiButton(12, width / 2 + 85 + leftmargin, 0, 20, 20, ""));
        buttonList.add(heightSlider = new GuiSliderCustom(13, (width / 2 - 75) + leftmargin, 0, mod_OldDays.lang.get("depth") + ": ", GuiSliderCustom.setSizeValue(ODNBXlite.IndevHeight)));
        buttonList.add(newOresButton = new GuiButton(14, width / 2 - 75 + leftmargin, 0, 150, 20, ""));
        widthButtons[GeneratorList.xcurrent].enabled=false;
        lengthButtons[GeneratorList.zcurrent].enabled=false;
    }

    @Override
    public void scrolled(){
        for (int i = 0; i < 4; i++){
            widthButtons[i].yPosition = height / 6 - 16 + scrolling;
            lengthButtons[i].yPosition = height / 6 + 14 + scrolling;
        }
        heightSlider.yPosition = height / 6 + 44 + scrolling;
        toggleButton.yPosition = height / 6 + 14 + scrolling;
        updateButtonPosition();
    }

    @Override
    public int getContentHeight(){
        if (origIndev){
            return indev ? 119 : 114;
        }
        return 130;
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
        sizeButton.displayString = mod_OldDays.lang.get("indevSize") + ": " + mod_OldDays.lang.get(GeneratorList.sizename[GeneratorList.sizecurrent]);
        shapeButton.displayString = mod_OldDays.lang.get("indevShape") + ": " + mod_OldDays.lang.get(GeneratorList.shapename[GeneratorList.shapecurrent]);
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
        typeButton.displayString = mod_OldDays.lang.get("indevType") + ": " + mod_OldDays.lang.get(GeneratorList.typename[GeneratorList.typecurrent]);
        for (int i = 0; i < 4; i++){
            widthButtons[i].displayString = Integer.toString(1 << i + 6);
            lengthButtons[i].displayString = widthButtons[i].displayString;
        }
        toggleButton.displayString = origIndev ? "+" : "-";
    }

    @Override
    public void updateButtonVisibility(){
        for (int i = 0; i < 4; i++){
            widthButtons[i].drawButton = !origIndev;
            lengthButtons[i].drawButton = !origIndev;
        }
        heightSlider.drawButton = !origIndev && indev;
        shapeButton.drawButton = origIndev && indev;
        sizeButton.drawButton = origIndev;
        typeButton.drawButton = indev;
    }

    @Override
    public void updateButtonPosition(){
        newOresButton.yPosition=height / 6 + (origIndev ? (!indev ? 68 : 119) : 69) + scrolling;
        newOresButton.xPosition=width / 2 - (origIndev ? 85 : 75) + leftmargin;
        int[] xcoords = new int[]{94, -16, 14, 126};
        if (origIndev){
            xcoords = !indev ? new int[]{0, 0, 44, 92} : new int[]{4, 28, 52, 76};
        }
        typeButton.yPosition = height / 6 + xcoords[0] + scrolling;
        shapeButton.yPosition = height / 6 + xcoords[1] + scrolling;
        sizeButton.yPosition = height / 6 + xcoords[2] + scrolling;
        themeButton.yPosition = height / 6 + xcoords[3] + scrolling;
        for (int i = 0; i < 4; i++){
            widthButtons[i].yPosition = height / 6 + xcoords[1] + scrolling;
            lengthButtons[i].yPosition = height / 6 + xcoords[2] + scrolling;
        }
        int xpos = width / 2 - (origIndev ? 85 : 75) + leftmargin;
        typeButton.xPosition = xpos;
        shapeButton.xPosition = xpos;
        sizeButton.xPosition = xpos;
        themeButton.xPosition = xpos;
    }

    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        if (!origIndev){
            drawString(fontRenderer, mod_OldDays.lang.get("width")+": ", width / 2 - 120 + leftmargin, height / 6 - 10 + scrolling, 0xa0a0a0);
            drawString(fontRenderer, mod_OldDays.lang.get("length")+": ", width / 2 - 120 + leftmargin, height / 6 + 20 + scrolling, 0xa0a0a0);
        }
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin - (origIndev ? 10 : 0), themeButton.yPosition + 22, 0xa0a0a0);
        if (indev && !origIndev){
            if (GeneratorList.typecurrent==2){
                int count = (heightSlider.getSizeValue() - 64) / 48 + 1;
                if (count==1){
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get("1Layer"), width / 2 + leftmargin, height / 6 + 114 + scrolling, 0xa0a0a0);
                }else{
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get(count+"Layers"), width / 2 + leftmargin, height / 6 + 114 + scrolling, 0xa0a0a0);
                }
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        super.actionPerformed(guibutton);
        if (!guibutton.enabled){
            return;
        }
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == typeButton){
            if (GeneratorList.typecurrent < GeneratorList.typelength){
                GeneratorList.typecurrent++;
            }else{
                GeneratorList.typecurrent = 0;
            }
        }else if (guibutton == themeButton){
            if (GeneratorList.themecurrent < GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent = 0;
            }
        }else if (guibutton.id >= 2 && guibutton.id <= 5){
            widthButtons[GeneratorList.xcurrent].enabled = true;
            GeneratorList.xcurrent = guibutton.id - 2;
            guibutton.enabled = false;
        }else if (guibutton.id >= 6 && guibutton.id <= 9){
            lengthButtons[GeneratorList.zcurrent].enabled = true;
            GeneratorList.zcurrent = guibutton.id - 6;
            guibutton.enabled = false;
        }else if (guibutton == shapeButton){
            if (GeneratorList.shapecurrent < GeneratorList.shapename.length - 1){
                GeneratorList.shapecurrent++;
            }else{
                GeneratorList.shapecurrent = 0;
            }
        }else if (guibutton == sizeButton){
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
        calculateMinScrolling();
    }

    @Override
    public void applySettings(){
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
            ODNBXlite.IndevWidthX = 1 << GeneratorList.xcurrent + 6;
            ODNBXlite.IndevWidthZ = 1 << GeneratorList.zcurrent + 6;
            ODNBXlite.IndevHeight = heightSlider.getSizeValue();
        }
        ODNBXlite.GenerateNewOres=newores;
    }

    @Override
    public void setDefaultSettings(){
        GeneratorList.typecurrent = ODNBXlite.DefaultIndevType;
        GeneratorList.themecurrent = ODNBXlite.DefaultTheme;
        GeneratorList.shapecurrent = 0;
        GeneratorList.sizecurrent = 1;
        GeneratorList.xcurrent = ODNBXlite.DefaultFiniteWidth;
        GeneratorList.zcurrent = ODNBXlite.DefaultFiniteLength;
        newores = ODNBXlite.DefaultNewOres;
        heightSlider.sliderValue = heightSlider.setSizeValue(ODNBXlite.DefaultFiniteDepth);
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        GeneratorList.typecurrent = w.mapType;
        GeneratorList.themecurrent = w.mapTheme;
        newores = w.newOres;
        ODNBXlite.IndevHeight = w.indevY;
        ODNBXlite.MapTheme=GeneratorList.themecurrent;
        ODNBXlite.GenerateNewOres=newores;
        ODNBXlite.IndevMapType=GeneratorList.typecurrent;
    }
}