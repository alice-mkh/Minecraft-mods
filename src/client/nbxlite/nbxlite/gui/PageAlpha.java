package net.minecraft.src.nbxlite.gui;

import java.util.Random;
import net.minecraft.src.*;

public class PageAlpha extends Page{
    private int[] feats = new int[]{ODNBXlite.FEATURES_INFDEV0227,
                                    ODNBXlite.FEATURES_INFDEV0420,
                                    ODNBXlite.FEATURES_ALPHA11201};

    private GuiButton themeButton;
    private GuiButton newOresButton;
    private boolean newores;
    private int mode;
    private int theme;

    public PageAlpha(GuiNBXlite parent, int mode){
        super(parent);
        newores = ODNBXlite.GenerateNewOres;
        this.mode = mode;
        theme = 0;
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
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1));
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = mode > 0;
    }

    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1) + ".desc"), width / 2 + leftmargin, height / 6 + 67 + scrolling, 0xa0a0a0);
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
            if (theme < 3){
                theme++;
            }else{
                theme = 0;
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
        ODNBXlite.MapFeatures = feats[mode];
        ODNBXlite.MapTheme = theme;
        if(mode == 2 && (ODNBXlite.MapTheme == ODNBXlite.THEME_NORMAL || ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS)){
            ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
        }
        ODNBXlite.GenerateNewOres = newores;
    }

    @Override
    public void setDefaultSettings(){
        theme = ODNBXlite.DefaultTheme;
        newores = ODNBXlite.DefaultNewOres;
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        theme = w.mapTheme;
        newores = w.newOres;
        ODNBXlite.SnowCovered = w.snowCovered;
        ODNBXlite.MapTheme = theme;
        ODNBXlite.GenerateNewOres = newores;
    }

    @Override
    public String getString(){
        StringBuilder str = new StringBuilder();
        str.append(mod_OldDays.lang.get("nbxlite.defaultgenerator" + (mode + 3)));
        str.append(", ");
        str.append(mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1)));
        if (mode == 2 && ODNBXlite.SnowCovered){
            str.append(" (");
            str.append(StringTranslate.getInstance().translateKey("tile.snow.name"));
            str.append(")");
        }
        return str.toString();
    }
}