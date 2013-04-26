package net.minecraft.src.nbxlite.gui;

import java.util.Random;
import net.minecraft.src.*;

public class PageAlpha extends Page{
    private GuiButton themeButton;
    private GuiButton newOresButton;
    private boolean newores;
    private int mode;

    public PageAlpha(GuiNBXlite parent, int mode){
        super(parent);
        newores = ODNBXlite.GenerateNewOres;
        this.mode = mode;
    }

    @Override
    public void initButtons(){
        buttonList.add(themeButton = new GuiButton(0, width / 2 - 75 + leftmargin, 0, 150, 20, ""));
        buttonList.add(newOresButton = new GuiButton(1, width / 2 - 75 + leftmargin, 0, 150, 20, ""));
    }

    @Override
    public void scrolled(){
        themeButton.yPosition = height / 6 + 44 + scrolling;
        newOresButton.yPosition = height / 6 + 84 + scrolling;
        updateButtonPosition();
    }

    @Override
    public int getContentHeight(){
        return newOresButton.drawButton ? 84 : 67;
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get(GeneratorList.themename[GeneratorList.themecurrent]);
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = mode > 0;
    }

    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.themedesc[GeneratorList.themecurrent]), width / 2 + leftmargin, height / 6 + 67 + scrolling, 0xa0a0a0);
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        super.actionPerformed(guibutton);
        if (!guibutton.enabled){
            return;
        }
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == themeButton){
            if (GeneratorList.themecurrent < GeneratorList.themelength){
                GeneratorList.themecurrent++;
            }else{
                GeneratorList.themecurrent = 0;
            }
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
        calculateMinScrolling();
    }

    @Override
    public void applySettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_BIOMELESS;
        switch(mode){
            case 0: ODNBXlite.MapFeatures = ODNBXlite.FEATURES_INFDEV0227; break;
            case 1: ODNBXlite.MapFeatures = ODNBXlite.FEATURES_INFDEV0420; break;
            case 2: ODNBXlite.MapFeatures = ODNBXlite.FEATURES_ALPHA11201; break;
        }
        ODNBXlite.MapTheme = GeneratorList.themecurrent;
        if(mode == 2 && (ODNBXlite.MapTheme == ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS)){
            ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
        }
        ODNBXlite.GenerateNewOres = newores;
    }

    @Override
    public void setDefaultSettings(){
        GeneratorList.themecurrent = ODNBXlite.DefaultTheme;
        newores = ODNBXlite.DefaultNewOres;
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        GeneratorList.themecurrent = w.mapTheme;
        newores = w.newOres;
        ODNBXlite.SnowCovered = w.snowCovered;
        ODNBXlite.MapTheme = GeneratorList.themecurrent;
        ODNBXlite.GenerateNewOres = newores;
    }
}