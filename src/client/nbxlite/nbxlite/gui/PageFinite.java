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
    private float tempSliderValue;
    private int theme;
    private int type;
    private int size;
    private int shape;
    private int xSize;
    private int zSize;

    public PageFinite(GuiNBXlite parent, boolean indev){
        super(parent);
        this.indev = indev;
        widthButtons = new GuiButton[4];
        lengthButtons = new GuiButton[4];
        origIndev = mc.indevShapeSize;
        newores = ODNBXlite.getDefaultFlag("newores");
        tempSliderValue = 0.0F;
        theme = 0;
        type = 0;
        size = 0;
        shape = 0;
        xSize = 0;
        zSize = 0;
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
        widthButtons[xSize].enabled=false;
        lengthButtons[zSize].enabled=false;
        heightSlider.sliderValue = tempSliderValue;
    }

    @Override
    public void scrolled(){
        for (int i = 0; i < 4; i++){
            widthButtons[i].yPosition = height / 6 - 16 + scrollingGui.scrolling;
            lengthButtons[i].yPosition = height / 6 + 14 + scrollingGui.scrolling;
        }
        heightSlider.yPosition = height / 6 + 44 + scrollingGui.scrolling;
        toggleButton.yPosition = height / 6 + 14 + scrollingGui.scrolling;
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
        newOresButton.displayString = mod_OldDays.lang.get("newOres") + ": " + mod_OldDays.lang.get("gui." + (newores ? "on" : "off"));
        sizeButton.displayString = mod_OldDays.lang.get("indevSize") + ": " + mod_OldDays.lang.get(GeneratorList.sizename[size]);
        shapeButton.displayString = mod_OldDays.lang.get("indevShape") + ": " + mod_OldDays.lang.get(GeneratorList.shapename[shape]);
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1));
        typeButton.displayString = mod_OldDays.lang.get("indevType") + ": " + mod_OldDays.lang.get(GeneratorList.typename[type]);
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
        newOresButton.yPosition=height / 6 + (origIndev ? (!indev ? 68 : 119) : 69) + scrollingGui.scrolling;
        newOresButton.xPosition=width / 2 - (origIndev ? 85 : 75) + leftmargin;
        int[] xcoords = new int[]{94, -16, 14, 126};
        if (origIndev){
            xcoords = !indev ? new int[]{0, 0, 44, 92} : new int[]{4, 28, 52, 76};
        }
        typeButton.yPosition = height / 6 + xcoords[0] + scrollingGui.scrolling;
        shapeButton.yPosition = height / 6 + xcoords[1] + scrollingGui.scrolling;
        sizeButton.yPosition = height / 6 + xcoords[2] + scrollingGui.scrolling;
        themeButton.yPosition = height / 6 + xcoords[3] + scrollingGui.scrolling;
        for (int i = 0; i < 4; i++){
            widthButtons[i].yPosition = height / 6 + xcoords[1] + scrollingGui.scrolling;
            lengthButtons[i].yPosition = height / 6 + xcoords[2] + scrollingGui.scrolling;
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
            drawString(fontRenderer, mod_OldDays.lang.get("width")+": ", width / 2 - 120 + leftmargin, height / 6 - 10 + scrollingGui.scrolling, 0xa0a0a0);
            drawString(fontRenderer, mod_OldDays.lang.get("length")+": ", width / 2 - 120 + leftmargin, height / 6 + 20 + scrollingGui.scrolling, 0xa0a0a0);
        }
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1) + ".desc"), width / 2 + leftmargin - (origIndev ? 10 : 0), themeButton.yPosition + 22, 0xa0a0a0);
        if (indev && !origIndev){
            if (type==2){
                int count = (heightSlider.getSizeValue() - 64) / 48 + 1;
                if (count==1){
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get("1Layer"), width / 2 + leftmargin, height / 6 + 114 + scrollingGui.scrolling, 0xa0a0a0);
                }else{
                    drawCenteredString(fontRenderer, mod_OldDays.lang.get(count+"Layers"), width / 2 + leftmargin, height / 6 + 114 + scrollingGui.scrolling, 0xa0a0a0);
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
            if (type < 3){
                type++;
            }else{
                type = 0;
            }
        }else if (guibutton == themeButton){
            if (theme < 3){
                theme++;
            }else{
                theme = 0;
            }
        }else if (guibutton.id >= 2 && guibutton.id <= 5){
            widthButtons[xSize].enabled = true;
            xSize = guibutton.id - 2;
            guibutton.enabled = false;
        }else if (guibutton.id >= 6 && guibutton.id <= 9){
            lengthButtons[zSize].enabled = true;
            zSize = guibutton.id - 6;
            guibutton.enabled = false;
        }else if (guibutton == shapeButton){
            if (shape < GeneratorList.shapename.length - 1){
                shape++;
            }else{
                shape = 0;
            }
        }else if (guibutton == sizeButton){
            if (size < GeneratorList.sizename.length - 1){
                size++;
            }else{
                size = 0;
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
        ODNBXlite.MapTheme=theme;
        if (indev){
            ODNBXlite.IndevMapType=type;
        }
        if (origIndev){
            ODNBXlite.IndevWidthX = 128 << size;
            ODNBXlite.IndevWidthZ = 128 << size;
            ODNBXlite.IndevHeight = 64;
            if (ODNBXlite.MapFeatures == ODNBXlite.FEATURES_INDEV){
                if (shape == 1){
                    ODNBXlite.IndevWidthX /= 2;
                    ODNBXlite.IndevWidthZ <<= 1;
                }else if (shape == 2){
                    ODNBXlite.IndevWidthZ /= 2;
                    ODNBXlite.IndevWidthX /= 2;
                    ODNBXlite.IndevHeight = 256;
                }
            }
        }else{
            ODNBXlite.IndevWidthX = 1 << xSize + 6;
            ODNBXlite.IndevWidthZ = 1 << zSize + 6;
            ODNBXlite.IndevHeight = heightSlider.getSizeValue();
        }
        ODNBXlite.setIndevBounds(type, theme);
        ODNBXlite.setFlag("newores", newores);
    }

    @Override
    public void setDefaultSettings(){
        type = ODNBXlite.DefaultIndevType;
        theme = ODNBXlite.DefaultTheme;
        shape = 0;
        size = 1;
        xSize = ODNBXlite.DefaultFiniteWidth;
        zSize = ODNBXlite.DefaultFiniteLength;
        newores = ODNBXlite.getDefaultFlag("newores");
        tempSliderValue = GuiSliderCustom.setSizeValue(ODNBXlite.DefaultFiniteDepth + 32);
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        type = w.mapType;
        theme = w.mapTheme;
        ODNBXlite.IndevHeight = w.indevY;
        ODNBXlite.MapTheme=theme;
        ODNBXlite.IndevMapType=type;
        newores = ODNBXlite.getFlagFromString(w.flags, "newores");
    }

    @Override
    public String getString(){
        StringBuilder str = new StringBuilder();
        str.append(mod_OldDays.lang.get("nbxlite.defaultgenerator" + (indev ? 2 : 1)));
        str.append(" (");
        str.append(ODNBXlite.IndevWidthX);
        str.append("x");
        str.append(ODNBXlite.IndevWidthZ);
        if (indev){
            str.append("x");
            str.append(ODNBXlite.IndevHeight-32);
        }
        str.append("), ");
        if (indev){
            str.append(mod_OldDays.lang.get(GeneratorList.typename[type]));
            str.append(", ");
        }
        str.append(mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1)));
        return str.toString();
    }
}