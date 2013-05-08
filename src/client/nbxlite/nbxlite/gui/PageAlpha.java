package net.minecraft.src.nbxlite.gui;

import java.util.Random;
import net.minecraft.src.*;

public class PageAlpha extends Page{
    private GuiButton[] featuresButtons;
    private GuiButton themeButton;
    private GuiButton newOresButton;
    private GuiButton snowButton;
    private GuiButton fixBeachesButton;
    private GuiButton weatherButton;
    private boolean newores;
    private boolean fixbeaches;
    private boolean weather;
    private int theme;
    private int features;
    private int snow;

    public PageAlpha(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButton[ODNBXlite.BIOMELESS_FEATURES.length];
        newores = ODNBXlite.getDefaultFlag("newores");
        fixbeaches = ODNBXlite.getDefaultFlag("fixbeaches");
        weather = ODNBXlite.getDefaultFlag("weather");
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
            addButton(featuresButtons[i]);
        }
        addButton(themeButton = new GuiButton(l, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        addButton(newOresButton = new GuiButton(l + 1, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        addButton(snowButton = new GuiButton(l + 2, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        addButton(fixBeachesButton = new GuiButton(l + 3, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        addButton(weatherButton = new GuiButton(l + 4, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        featuresButtons[features].enabled=false;
    }

    @Override
    public void scrolled(){
        super.scrolled();
        for (int i = 0; i < featuresButtons.length; i++){
            setY(featuresButtons[i], (i - 1) * 21);
        }
        setY(themeButton, 85);
        setY(newOresButton, 120);
        setY(weatherButton, newOresButton.drawButton ? 142 : 120);
        setY(snowButton, 164);
        setY(fixBeachesButton, 185);
        updateButtonPosition();
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("flag.newores") + ": " + mod_OldDays.lang.get("gui." + (newores ? "on" : "off"));
        themeButton.displayString = mod_OldDays.lang.get("nbxlite.maptheme.name") + ": " + mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1));
        snowButton.displayString = stringtranslate.translateKey("tile.snow.name") + ": ";
        if (snow == 1 && !weather){
            snowButton.displayString += mod_OldDays.lang.get("gui.random") + (theme == ODNBXlite.THEME_WOODS ? " (50%)" : " (25%)");
        }else{
            snowButton.displayString += mod_OldDays.lang.get("gui." + (snow > 0 && !weather ? "on" : "off"));
        }
        fixBeachesButton.displayString = mod_OldDays.lang.get("flag.fixbeaches") + ": " + mod_OldDays.lang.get("gui." + (fixbeaches ? "on" : "off"));
        weatherButton.displayString = mod_OldDays.lang.get("flag.weather") + ": " + mod_OldDays.lang.get("gui." + (weather && weatherButton.enabled ? "on" : "off"));
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = features > 0;
        snowButton.drawButton = canSnow();
        snowButton.enabled = !weather;
        weatherButton.enabled = theme == ODNBXlite.THEME_NORMAL || theme == ODNBXlite.THEME_WOODS;
        fixBeachesButton.drawButton = ODNBXlite.BIOMELESS_FEATURES[features] == ODNBXlite.FEATURES_ALPHA11201;
    }

    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1) + ".desc"), width / 2 + - 10 + leftmargin, height / 6 + 108 + scrollingGui.scrolling, 0xa0a0a0);
    }

    @Override
    public void actionPerformedScrolling(GuiButton guibutton){
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == fixBeachesButton){
            fixbeaches = !fixbeaches;
        }else if (guibutton == weatherButton){
            weather = !weather;
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
        super.actionPerformedScrolling(guibutton);
    }

    @Override
    public void applySettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_BIOMELESS;
        ODNBXlite.MapFeatures = ODNBXlite.BIOMELESS_FEATURES[features];
        ODNBXlite.MapTheme = theme;
        if (canSnow() && !weather){
            if (snow == 1){
                ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
            }else{
                ODNBXlite.SnowCovered = snow > 0;
            }
        }else{
            ODNBXlite.SnowCovered = false;
        }
        ODNBXlite.setFlag("newores", newores);
        ODNBXlite.setFlag("fixbeaches", fixbeaches);
        ODNBXlite.setFlag("weather", weather && weatherButton.enabled);
    }

    @Override
    public void setDefaultSettings(){
        features = ODNBXlite.DefaultFeaturesBiomeless;
        theme = ODNBXlite.DefaultTheme;
        newores = ODNBXlite.getDefaultFlag("newores");
        fixbeaches = ODNBXlite.getDefaultFlag("fixbeaches");
        weather = ODNBXlite.getDefaultFlag("weather");
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        theme = w.mapTheme;
        snow = w.snowCovered ? 2 : 0;
        ODNBXlite.SnowCovered = w.snowCovered;
        ODNBXlite.MapTheme = theme;
        newores = ODNBXlite.getFlagFromString(w.flags, "newores");
        weather = ODNBXlite.getFlagFromString(w.flags, "weather");
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