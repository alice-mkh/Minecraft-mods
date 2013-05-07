package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageRelease extends Page{
    private GuiButton[] featuresButtons;
    private GuiButton newOresButton;
    private GuiButton weatherButton;
    private boolean newores;
    private boolean weather;
    private int features;

    public PageRelease(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButton[GeneratorList.feat2length + 1];
        newores = ODNBXlite.getDefaultFlag("newores");
        weather = ODNBXlite.getDefaultFlag("weather");
        features = ODNBXlite.DefaultFeaturesRelease;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButton(i, (width / 2 - 115) + leftmargin, 0, 210, 20, "");
            String name = mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1));
            name += " (" + mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1)+".desc") + ")";
            featuresButtons[i].displayString = name;
            addButton(featuresButtons[i]);
        }
        addButton(newOresButton = new GuiButton(l, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        addButton(weatherButton = new GuiButton(l + 1, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        featuresButtons[features].enabled=false;
    }

    @Override
    public void scrolled(){
        super.scrolled();
        for (int i = 0; i < featuresButtons.length; i++){
            setY(featuresButtons[i], (i - 1) * 21);
        }
        setY(newOresButton, 149);
        setY(weatherButton, newOresButton.drawButton ? 171 : 149);
        updateButtonPosition();
    }

    @Override
    public void updateButtonText(){
        newOresButton.displayString = mod_OldDays.lang.get("flag.newores") + ": " + mod_OldDays.lang.get("gui." + (newores ? "on" : "off"));
        weatherButton.displayString = mod_OldDays.lang.get("flag.weather") + ": " + mod_OldDays.lang.get("gui." + (weather ? "on" : "off"));
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = features<ODNBXlite.FEATURES_15;
    }

    @Override
    public void actionPerformedScrolling(GuiButton guibutton){
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == weatherButton){
            weather = !weather;
        }else if (guibutton.id < featuresButtons.length){
            featuresButtons[features].enabled = true;
            features = guibutton.id;
            guibutton.enabled = false;
        }
        super.actionPerformedScrolling(guibutton);
    }

    @Override
    public void applySettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_NEWBIOMES;
        ODNBXlite.MapFeatures=features;
        ODNBXlite.setFlag("newores", newores);
        ODNBXlite.setFlag("weather", weather);
    }

    @Override
    public void setDefaultSettings(){
        features = ODNBXlite.DefaultFeaturesRelease;
        newores = ODNBXlite.getDefaultFlag("newores");
        weather = ODNBXlite.getDefaultFlag("weather");
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        features = w.mapGenExtra;
        newores = ODNBXlite.getFlagFromString(w.flags, "newores");
        weather = ODNBXlite.getFlagFromString(w.flags, "weather");
    }

    @Override
    public String getString(){
        return mod_OldDays.lang.get("nbxlite.releasefeatures" + (features + 1));
    }
}