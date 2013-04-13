package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.*;

public class PageBeta extends Page{
    private GuiButton[] featuresButtons;
    private GuiButton newOresButton;
    private GuiButton jungleButton;
    private boolean newores;
    private boolean jungle;

    public PageBeta(GuiNBXlite parent){
        super(parent);
        featuresButtons = new GuiButton[GeneratorList.feat1length + 1];
        jungle = false;
        newores = ODNBXlite.DefaultNewOres;
    }

    @Override
    public void initButtons(){
        int l = featuresButtons.length;
        for (int i = 0; i < l; i++){
            featuresButtons[i] = new GuiButton(i, (width / 2 - 115) + leftmargin, 0, 210, 20, "");
            String name = mod_OldDays.lang.get(GeneratorList.feat1name[i]);
            if (GeneratorList.feat1desc[i] != ""){
                name += " (" + mod_OldDays.lang.get(GeneratorList.feat1desc[i]) + ")";
            }
            featuresButtons[i].displayString = name;
            buttonList.add(featuresButtons[i]);
        }
        buttonList.add(newOresButton = new GuiButton(l, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        buttonList.add(jungleButton = new GuiButton(l + 1, width / 2 - 85 + leftmargin, 0, 150, 20, ""));
        featuresButtons[GeneratorList.feat1current].enabled=false;
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
    public void updateButtonText(){
        StringTranslate stringtranslate = StringTranslate.getInstance();
        newOresButton.displayString = mod_OldDays.lang.get("nbxlite.generatenewores.name") + ": " + stringtranslate.translateKey("options." + (newores ? "on" : "off"));
        jungleButton.displayString = mod_OldDays.lang.get("betaJungle") + ": " + stringtranslate.translateKey("options." + (jungle ? "on" : "off"));
    }

    @Override
    public void updateButtonVisibility(){
        jungleButton.drawButton = GeneratorList.feat1current == 5;
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
            featuresButtons[GeneratorList.feat1current].enabled = true;
            GeneratorList.feat1current = guibutton.id;
            guibutton.enabled = false;
        }
        updateButtonPosition();
        updateButtonVisibility();
        updateButtonText();
        calculateMinScrolling();
    }

    @Override
    public void selectSettings(){
        ODNBXlite.Generator = ODNBXlite.GEN_OLDBIOMES;
        ODNBXlite.MapFeatures = jungle ? ODNBXlite.FEATURES_JUNGLE : GeneratorList.feat1current;
        ODNBXlite.GenerateNewOres=newores;
    }
}