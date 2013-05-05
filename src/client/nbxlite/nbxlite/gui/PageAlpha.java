package net.minecraft.src.nbxlite.gui;

import java.util.Random;
import net.minecraft.src.*;

public class PageAlpha extends Page{
    private GuiButton[] featuresButtons;
    private GuiButton themeButton;
    private GuiButton newOresButton;
    private GuiButton snowButton;
    private boolean newores;
    private int theme;
    private int features;
    private int snow;

    public PageAlpha(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButton[ODNBXlite.BIOMELESS_FEATURES.length];
        newores = ODNBXlite.getDefaultFlag("newores");
        theme = 0;
        snow = 1;
        features = ODNBXlite.DefaultFeaturesBiomeless;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButton(i, (width / 2 - 115) + leftmargin, 0, 210, 20, "");
            String name = mod_OldDays.lang.get("nbxlite.biomelessfeatures" + (i + 1));
            name += " (" + mod_OldDays.lang.get("nbxlite.biomelessfeatures" + (i + 1) + ".desc") + ")";
            featuresButtons[i].displayString = name;
            buttonList.add(featuresButtons[i]);
        }
        buttonList.add(themeButton = new GuiButton(l, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        buttonList.add(newOresButton = new GuiButton(l + 1, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        buttonList.add(snowButton = new GuiButton(l + 2, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        featuresButtons[features].enabled=false;
    }

    @Override
    public void scrolled(){
        for (int i = 0; i < featuresButtons.length; i++){
            featuresButtons[i].yPosition = height / 6 + ((i - 1) * 21) + scrollingGui.scrolling;
        }
        themeButton.yPosition = height / 6 + 85 + scrollingGui.scrolling;
        newOresButton.yPosition = height / 6 + 120 + scrollingGui.scrolling;
        snowButton.yPosition = height / 6 + 142 + scrollingGui.scrolling;
        updateButtonPosition();
    }

    @Override
    public int getContentHeight(){
        return snowButton.drawButton ? 142 : (newOresButton.drawButton ? 120 : 108);
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("flag.newores") + ": " + mod_OldDays.lang.get("gui." + (newores ? "on" : "off"));
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1));
        snowButton.displayString = stringtranslate.translateKey("tile.snow.name") + ": ";
        if (snow == 1){
            snowButton.displayString += mod_OldDays.lang.get("gui.random") + (theme == ODNBXlite.THEME_WOODS ? " (50%)" : " (25%)");
        }else{
            snowButton.displayString += mod_OldDays.lang.get("gui." + (snow > 0 ? "on" : "off"));
        }
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = features > 0;
        snowButton.drawButton = canSnow();
    }

    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1) + ".desc"), width / 2 + - 10 + leftmargin, height / 6 + 108 + scrollingGui.scrolling, 0xa0a0a0);
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
        }else if (guibutton == snowButton){
            if (snow < 2){
                snow++;
            }else{
                snow = 0;
            }
        }else if (guibutton.id < featuresButtons.length){
            featuresButtons[features].enabled = true;
            features = guibutton.id;
            guibutton.enabled = false;
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
        calculateMinScrolling();
    }

    @Override
    public void applySettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_BIOMELESS;
        ODNBXlite.MapFeatures = ODNBXlite.BIOMELESS_FEATURES[features];
        ODNBXlite.MapTheme = theme;
        if (canSnow()){
            if (snow == 1){
                ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
            }else{
                ODNBXlite.SnowCovered = snow > 0;
            }
        }else{
            ODNBXlite.SnowCovered = false;
        }
        ODNBXlite.setFlag("newores", newores);
    }

    @Override
    public void setDefaultSettings(){
        features = ODNBXlite.DefaultFeaturesBiomeless;
        theme = ODNBXlite.DefaultTheme;
        newores = ODNBXlite.getDefaultFlag("newores");
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        theme = w.mapTheme;
        snow = w.snowCovered ? 2 : 0;
        ODNBXlite.SnowCovered = w.snowCovered;
        ODNBXlite.MapTheme = theme;
        newores = ODNBXlite.getFlagFromString(w.flags, "newores");
    }

    @Override
    public String getString(){
        StringBuilder str = new StringBuilder();
        str.append(mod_OldDays.lang.get("nbxlite.biomelessfeatures" + (features + 1)));
        str.append(", ");
        str.append(mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1)));
        if (features == 3 && ODNBXlite.SnowCovered){
            str.append(" (");
            str.append(StringTranslate.getInstance().translateKey("tile.snow.name"));
            str.append(")");
        }
        return str.toString();
    }

    private boolean canSnow(){
        return ODNBXlite.BIOMELESS_FEATURES[features] == ODNBXlite.FEATURES_ALPHA11201 && (theme == ODNBXlite.THEME_NORMAL || theme == ODNBXlite.THEME_WOODS);
    }
}