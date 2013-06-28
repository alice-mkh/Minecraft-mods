package net.minecraft.src.nbxlite.gui;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.*;

public class PageAlpha extends Page{
    private GuiButtonNBXlite[] featuresButtons;
    private GuiButtonNBXlite[] helpButtons;
    private GuiButtonNBXlite themeButton;
    private GuiButtonNBXlite newOresButton;
    private GuiButtonNBXlite snowButton;
    private GuiButtonNBXlite fixBeachesButton;
    private GuiButtonNBXlite weatherButton;
    private int help;
    private boolean newores;
    private boolean fixbeaches;
    private boolean weather;
    private int theme;
    private int features;
    private int snow;

    public PageAlpha(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButtonNBXlite[ODNBXlite.BIOMELESS_FEATURES.length];
        helpButtons = new GuiButtonNBXlite[featuresButtons.length];
        help = -1;
        newores = ODNBXlite.getDefaultFlag("newores");
        fixbeaches = ODNBXlite.getDefaultFlag("fixbeaches");
        weather = ODNBXlite.getDefaultFlag("weather");
        theme = 0;
        snow = parent.newworld ? 1 : (ODNBXlite.SnowCovered ? 2 : 0);
        features = ODNBXlite.DefaultFeaturesBiomeless;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButtonNBXlite(i, (width / 2 - 105) + leftmargin, 170);
            String name = mod_OldDays.lang.get("nbxlite.biomelessfeatures" + (i + 1));
            featuresButtons[i].displayString = name;
            addButton(featuresButtons[i]);
            helpButtons[i] = new GuiButtonNBXlite(i + 100, (width / 2 + 66) + leftmargin, 20);
            helpButtons[i].displayString = "?";
            addButton(helpButtons[i]);
        }
        addButton(themeButton = new GuiButtonNBXlite(l, width / 2 - 85 + leftmargin, 150));
        addButton(newOresButton = new GuiButtonNBXlite(l + 1, width / 2 - 85 + leftmargin, 150));
        addButton(snowButton = new GuiButtonNBXlite(l + 2, width / 2 - 85 + leftmargin, 150));
        addButton(fixBeachesButton = new GuiButtonNBXlite(l + 3, width / 2 - 85 + leftmargin, 150));
        addButton(weatherButton = new GuiButtonNBXlite(l + 4, width / 2 - 85 + leftmargin, 150));
        featuresButtons[features].enabled=false;
    }

    @Override
    public void scrolled(){
        super.scrolled();
        for (int i = 0; i < featuresButtons.length; i++){
            setY(featuresButtons[i], (i - 1) * 21);
            setY(helpButtons[i], (i - 1) * 21);
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
        snowButton.enabled = parent.newworld && !weather;
        weatherButton.enabled = (theme == ODNBXlite.THEME_NORMAL || theme == ODNBXlite.THEME_WOODS) && (!ODNBXlite.SnowCovered || parent.newworld);
        fixBeachesButton.drawButton = ODNBXlite.BIOMELESS_FEATURES[features] == ODNBXlite.FEATURES_ALPHA11201;
    }

    @Override
    public void drawScreen(int i, int j, float f){
        super.drawScreen(i, j, f);
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.maptheme" + (theme + 1) + ".desc"), width / 2 + - 10 + leftmargin, height / 6 + 108 + scrollingGui.scrolling, 0xa0a0a0);
    }

    @Override
    public void postDrawScreen(int i, int j, float f){
        if (help >= 0 && (i <= helpButtons[help].xPosition || i >= helpButtons[help].xPosition+20 || j <= helpButtons[help].yPosition || j >= helpButtons[help].yPosition+20)){
            help = -1;
        }
        if (help >= 0){
            drawTooltip(getTitle(help), getTooltip(help, 0), width / 2, height / 2);
        }
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
        }else if (guibutton.id < featuresButtons.length){
            featuresButtons[features].enabled = true;
            features = guibutton.id;
            guibutton.enabled = false;
        }else if (guibutton.id > 99){
            help = help < 0 ? guibutton.id - 100 : -1;
        }
        super.actionPerformedScrolling(guibutton);
    }

    @Override
    public void applySettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_BIOMELESS;
        ODNBXlite.MapFeatures = ODNBXlite.BIOMELESS_FEATURES[features];
        ODNBXlite.MapTheme = theme;
        if (parent.newworld){
            if (canSnow() && !weather){
                if (snow == 1){
                    ODNBXlite.SnowCovered = (new Random()).nextInt(ODNBXlite.MapTheme == ODNBXlite.THEME_WOODS ? 2 : 4) == 0;
                }else{
                    ODNBXlite.SnowCovered = snow > 0;
                }
            }else{
                ODNBXlite.SnowCovered = false;
            }
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

    private String getTitle(int i){
        String name = "biomeless";
        return mod_OldDays.lang.get("nbxlite."+name+"features"+(i+1));
    }

    private String[] getTooltip(int i, int col){
        String name = "biomeless";
        ArrayList<String> list = new ArrayList<String>();
        boolean[] features = new boolean[12];
        features[0] = i <= 0;
        features[1] = i <= 0;
        features[2] = i >= 1;
        features[3] = i >= 1;
        features[4] = i >= 3;
        features[5] = i >= 3 || i == 0;
        features[6] = i >= 3;
        features[7] = i >= 4;
        features[8] = i >= 4;
        features[9] = i >= 4;
        features[10] = i >= 4;
        features[11] = i >= 4;
        int num = mod_OldDays.getDescriptionNumber(name+".feature");
        for (int j = 0; j < num; j++){
            list.add("<-• §"+(features[j] ? "a" : "c")+mod_OldDays.lang.get(name+".feature"+(j+1)));
        }
        list.add("<-• "+mod_OldDays.lang.get(name+".trees.value"+(i+1)));
        return list.toArray(new String[list.size()]);
    }
}