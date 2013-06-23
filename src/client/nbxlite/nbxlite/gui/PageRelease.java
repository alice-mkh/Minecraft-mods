package net.minecraft.src.nbxlite.gui;

import java.util.ArrayList;
import net.minecraft.src.*;

public class PageRelease extends Page{
    private GuiButtonNBXlite[] featuresButtons;
    private GuiButtonNBXlite[] helpButtons;
    private GuiButtonNBXlite newOresButton;
    private GuiButtonNBXlite weatherButton;
    private int help;
    private boolean newores;
    private boolean weather;
    private int features;

    public PageRelease(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButtonNBXlite[GeneratorList.feat2length + 1];
        helpButtons = new GuiButtonNBXlite[featuresButtons.length];
        help = -1;
        newores = ODNBXlite.getDefaultFlag("newores");
        weather = true;
        features = ODNBXlite.DefaultFeaturesRelease;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButtonNBXlite(i, (width / 2 - 105) + leftmargin, 170);
            String name = mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1));
//             name += " (" + mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1)+".desc") + ")";
            featuresButtons[i].displayString = name;
            addButton(featuresButtons[i]);
            helpButtons[i] = new GuiButtonNBXlite(i + 100, (width / 2 + 66) + leftmargin, 20);
            helpButtons[i].displayString = "?";
            addButton(helpButtons[i]);
        }
        addButton(newOresButton = new GuiButtonNBXlite(l, width / 2 - 85 + leftmargin, 150));
        addButton(weatherButton = new GuiButtonNBXlite(l + 1, width / 2 - 85 + leftmargin, 150));
        featuresButtons[features].enabled=false;
    }

    @Override
    public void scrolled(){
        super.scrolled();
        for (int i = 0; i < featuresButtons.length; i++){
            setY(featuresButtons[i], (i - 1) * 21);
            setY(helpButtons[i], (i - 1) * 21);
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
    public void postDrawScreen(int i, int j, float f){
        if (help >= 0 && (i <= helpButtons[help].xPosition || i >= helpButtons[help].xPosition+20 || j <= helpButtons[help].yPosition || j >= helpButtons[help].yPosition+20)){
            help = -1;
        }
        if (help >= 0){
            drawTooltip(getTooltip(help), width / 2, height / 2);
        }
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
        }else if (guibutton.id > 99){
            help = help < 0 ? guibutton.id - 100 : -1;
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
        weather = true;
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

    private String[] getTooltip(int i){
        ArrayList<String> list = new ArrayList<String>();
        String name = "nbxlite.releasefeatures"+(i+1);
        list.add(mod_OldDays.lang.get(name));
        list.add("");
        list.add("§7"+mod_OldDays.lang.get(name+".desc"));
        int num2 = mod_OldDays.getDescriptionNumber(name+".desc");
        for (int j = 0; j < num2; j++){
            list.add("§7"+mod_OldDays.lang.get(name+".desc"+(j+1)));
        }
        return list.toArray(new String[list.size()]);
    }
}