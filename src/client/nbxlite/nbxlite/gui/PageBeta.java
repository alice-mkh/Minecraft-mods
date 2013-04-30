package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageBeta extends Page{
    private GuiButton[] featuresButtons;
    private GuiButton newOresButton;
    private GuiButton jungleButton;
    private boolean newores;
    private boolean jungle;
    private int features;

    public PageBeta(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButton[GeneratorList.feat1length + 1];
        jungle = ODNBXlite.MapFeatures == ODNBXlite.FEATURES_JUNGLE;
        newores = ODNBXlite.GenerateNewOres;
        features = ODNBXlite.DefaultFeaturesBeta;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButton(i, (width / 2 - 115) + leftmargin, 0, 210, 20, "");
            String name = mod_OldDays.lang.get("nbxlite.betafeatures" + (i + 1));
            if (i != ODNBXlite.FEATURES_SKY){
                name += " (" + mod_OldDays.lang.get("nbxlite.betafeatures" + (i + 1) + ".desc") + ")";
            }
            featuresButtons[i].displayString = name;
            buttonList.add(featuresButtons[i]);
        }
        buttonList.add(newOresButton = new GuiButton(l, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        buttonList.add(jungleButton = new GuiButton(l + 1, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        featuresButtons[features].enabled=false;
    }

    @Override
    public void scrolled(){
        for (int i = 0; i < featuresButtons.length; i++){
            featuresButtons[i].yPosition = height / 6 + ((i - 1) * 21) + scrolling;
        }
        jungleButton.yPosition = height / 6 + 149 + scrolling;
        newOresButton.yPosition = height / 6 + 127 + scrolling;
        updateButtonPosition();
    }

    @Override
    public int getContentHeight(){
        return jungleButton.drawButton ? 149 : 127;
    }

    @Override
    public void updateButtonText(){
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + mod_OldDays.lang.get("gui." + (newores ? "on" : "off"));
        jungleButton.displayString = mod_OldDays.lang.get("betaJungle") + ": " + mod_OldDays.lang.get("gui." + (jungle ? "on" : "off"));
    }

    @Override
    public void updateButtonVisibility(){
        jungleButton.drawButton = features == 5;
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        super.actionPerformed(guibutton);
        if (!guibutton.enabled){
            return;
        }
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton == jungleButton){
            jungle = !jungle;
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
        ODNBXlite.Generator = ODNBXlite.GEN_OLDBIOMES;
        ODNBXlite.MapFeatures = jungle ? ODNBXlite.FEATURES_JUNGLE : features;
        ODNBXlite.GenerateNewOres=newores;
    }

    @Override
    public void setDefaultSettings(){
        features = ODNBXlite.DefaultFeaturesBeta;
        newores = ODNBXlite.DefaultNewOres;
        jungle = ODNBXlite.DefaultFeaturesBeta == ODNBXlite.FEATURES_JUNGLE;
    }

    @Override
    public void loadFromWorldInfo(WorldInfo w){
        newores = w.newOres;
        features = w.mapGenExtra;
        ODNBXlite.GenerateNewOres=newores;
    }

    @Override
    public String getString(){
        return mod_OldDays.lang.get("nbxlite.betafeatures" + (features + 1));
    }
}