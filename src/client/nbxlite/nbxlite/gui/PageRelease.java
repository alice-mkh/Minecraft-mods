package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageRelease extends Page{
    private GuiButton[] featuresButtons;
    private GuiButton newOresButton;
    private boolean newores;

    public PageRelease(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButton[GeneratorList.feat2length + 1];
        newores = ODNBXlite.DefaultNewOres;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButton(i, (width / 2 - 115) + leftmargin, height / 6 + ((i - 1) * 21), 210, 20, "");
            String name = mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1));
            name += " (" + mod_OldDays.lang.get("nbxlite.releasefeatures" + (i + 1)+".desc") + ")";
            featuresButtons[i].displayString = name;
            buttonList.add(featuresButtons[i]);
        }
        buttonList.add(newOresButton = new GuiButton(l, width / 2 - 85 + leftmargin, height / 6 + 149, 150, 20, ""));
        featuresButtons[GeneratorList.feat2current].enabled=false;
    }

    @Override
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
    }

    @Override
    public void updateButtonVisibility(){
        newOresButton.drawButton = GeneratorList.feat2current<ODNBXlite.FEATURES_15;
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        if (!guibutton.enabled){
            return;
        }
        if (guibutton == newOresButton){
            newores = !newores;
        }else if (guibutton.id < featuresButtons.length){
            featuresButtons[GeneratorList.feat2current].enabled = true;
            GeneratorList.feat2current = guibutton.id;
            guibutton.enabled = false;
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
    }

    @Override
    public void selectSettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_NEWBIOMES;
        ODNBXlite.MapFeatures=GeneratorList.feat2current;
        ODNBXlite.GenerateNewOres=newores;
    }
}