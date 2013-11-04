package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageFinite extends Page{
    private boolean indev;
    private boolean origIndev;
    private GuiButtonNBXlite typeButton;
    private GuiButtonNBXlite themeButton;
    private GuiButtonNBXlite[] widthButtons;
    private GuiButtonNBXlite[] lengthButtons;
    private GuiButtonNBXlite sizeButton;
    private GuiButtonNBXlite shapeButton;
    private GuiSliderCustom heightSlider;
    private GuiButtonNBXlite toggleButton;
    private GuiButtonNBXlite newOresButton;
    private GuiButtonNBXlite weatherButton;
    private boolean newores;
    private boolean weather;
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
        widthButtons = new GuiButtonNBXlite[4];
        lengthButtons = new GuiButtonNBXlite[4];
        origIndev = mc.indevShapeSize;
        newores = ODNBXlite.getDefaultFlag("newores");
        weather = ODNBXlite.getDefaultFlag("weather");
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
        addButton(typeButton = new GuiButtonNBXlite(0, width / 2 - 75 + leftmargin, 150));
        addButton(themeButton = new GuiButtonNBXlite(1, width / 2 - 75 + leftmargin, 150));
        for (int i = 0; i < 4; i++){
            addButton(widthButtons[i]=new GuiButtonNBXlite(2+i, (width / 2 - 82+(41*i)) + leftmargin, 40));
            addButton(lengthButtons[i]=new GuiButtonNBXlite(6+i, (width / 2 - 82+(41*i)) + leftmargin, 40));
        }
        addButton(shapeButton = new GuiButtonNBXlite(10, width / 2 - 75 + leftmargin, 150));
        addButton(sizeButton = new GuiButtonNBXlite(11, width / 2 - 75 + leftmargin, 150));
        addButton(toggleButton = new GuiButtonNBXlite(12, width / 2 + 85 + leftmargin, 20));
        addButton(heightSlider = new GuiSliderCustom(13, (width / 2 - 75) + leftmargin, mod_OldDays.lang.get("depth") + ": ", GuiSliderCustom.setSizeValue(ODNBXlite.IndevHeight)));
        addButton(newOresButton = new GuiButtonNBXlite(14, width / 2 - 75 + leftmargin, 150));
        addButton(weatherButton = new GuiButtonNBXlite(15, width / 2 - 85 + leftmargin, 150));
        widthButtons[xSize].enabled=false;
        lengthButtons[zSize].enabled=false;
        heightSlider.sliderValue = tempSliderValue;
    }

    @Override
    public void scrolled(){
        super.scrolled();
        for (int i = 0; i < 4; i++){
            setY(widthButtons[i], -16);
            setY(lengthButtons[i], 14);
        }
        setY(heightSlider, 44);
        setY(toggleButton, 14);
        updateButtonPosition();
    }

    @Override
    public void updateButtonText(){
        newOresButton.displayString = mod_OldDays.lang.get("flag.newores") + ": " + mod_OldDays.lang.get("gui." + (newores ? "on" : "off"));
        sizeButton.displayString = mod_OldDays.lang.get("indevSize") + ": " + mod_OldDays.lang.get(GeneratorList.sizename[size]);
        shapeButton.displayString = mod_OldDays.lang.get("indevShape") + ": " + mod_OldDays.lang.get(GeneratorList.shapename[shape]);
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1));
        typeButton.displayString = mod_OldDays.lang.get("indevType") + ": " + mod_OldDays.lang.get(GeneratorList.typename[type]);
        for (int i = 0; i < 4; i++){
            widthButtons[i].displayString = Integer.toString(1 << i + 6);
            lengthButtons[i].displayString = widthButtons[i].displayString;
        }
        toggleButton.displayString = origIndev ? "+" : "-";
        weatherButton.displayString = mod_OldDays.lang.get("flag.weather") + ": " + mod_OldDays.lang.get("gui." + (weather && weatherButton.enabled ? "on" : "off"));
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
        weatherButton.enabled = theme == ODNBXlite.THEME_NORMAL || theme == ODNBXlite.THEME_WOODS;
    }

    @Override
    public void updateButtonPosition(){
        int[] ycoords = !indev ? new int[]{0, -16, 14, 69, 44, 101} : new int[]{94, -16, 14, 126, 69, 158};
        if (origIndev){
            ycoords = !indev ? new int[]{0, 0, 4, 28, 68, 90} : new int[]{4, 28, 52, 76, 108, 130};
        }
        setY(typeButton, ycoords[0]);
        setY(shapeButton, ycoords[1]);
        setY(sizeButton, ycoords[2]);
        setY(themeButton, ycoords[3]);
        for (int i = 0; i < 4; i++){
            setY(widthButtons[i], ycoords[1]);
            setY(lengthButtons[i], ycoords[2]);
        }
        setY(newOresButton, ycoords[4]);
        setY(weatherButton, ycoords[5]);
        int xpos = width / 2 - (origIndev ? 85 : 75) + leftmargin;
        typeButton.xPosition = xpos;
        shapeButton.xPosition = xpos;
        sizeButton.xPosition = xpos;
        themeButton.xPosition = xpos;
        newOresButton.xPosition = xpos;
        weatherButton.xPosition = xpos;
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
                drawCenteredString(fontRenderer, mod_OldDays.lang.get(count == 1 ? "1Layer" : count+"Layers"), width / 2 + leftmargin, height / 6 + 116 + scrollingGui.scrolling, 0xa0a0a0);
            }
        }
    }

    @Override
    public void actionPerformedScrolling(GuiButton guibutton){
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
        }else if (guibutton == weatherButton){
            weather = !weather;
        }
        super.actionPerformedScrolling(guibutton);
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
        ODNBXlite.setFlag("weather", weather && weatherButton.enabled);
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
        weather = ODNBXlite.getDefaultFlag("weather");
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
        weather = ODNBXlite.getFlagFromString(w.flags, "weather");
    }

    public static String getString(int x, int y, int z, int type, int theme, boolean indev){
        StringBuilder str = new StringBuilder();
        str.append(mod_OldDays.lang.get("nbxlite.defaultgenerator" + (indev ? 2 : 1)));
        str.append(" (");
        str.append(x);
        str.append("x");
        str.append(z);
        if (indev){
            str.append("x");
            str.append(y - 32);
        }
        str.append("), ");
        if (indev){
            str.append(mod_OldDays.lang.get(GeneratorList.typename[type]));
            str.append(", ");
        }
        str.append(mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1)));
        return str.toString();
    }

    @Override
    public String getString(){
        return getString(ODNBXlite.IndevWidthX, ODNBXlite.IndevHeight, ODNBXlite.IndevWidthZ, type, theme, indev);
    }
}