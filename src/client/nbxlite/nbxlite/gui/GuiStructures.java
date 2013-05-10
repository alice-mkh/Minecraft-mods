package net.minecraft.src.nbxlite.gui;

import java.util.List;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.mod_OldDays;
import net.minecraft.src.ODNBXlite;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.WorldInfo;

public class GuiStructures extends GuiScreen{
    private GuiScreen parent;
    private boolean[] structures;
    private GuiButton[] buttons;

    public GuiStructures(GuiScreen gui){
        parent = gui;
        structures = ODNBXlite.getDefaultStructures(true, ODNBXlite.Generator, ODNBXlite.MapFeatures);
        buttons = new GuiButton[structures.length];
    }

    @Override
    public void initGui(){
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_OldDays.lang.get("continue")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StringTranslate.getInstance().translateKey("gui.cancel")));
        for (int i = 0; i < buttons.length; i++){
            buttons[i] = new GuiButton(2 + i, width / 2 - 100, height / 6 + ((i + 1) * 21), 200, 20, "");
            buttonList.add(buttons[i]);
        }
        structures = ODNBXlite.Structures;
        refresh();
    }

    private void refresh(){
        boolean[] b = ODNBXlite.getAvailableStructures(ODNBXlite.Generator, ODNBXlite.MapFeatures);
        for (int i = 0; i < buttons.length; i++){
            buttons[i].enabled = b[i];
            buttons[i].displayString = mod_OldDays.lang.get("structures." + ODNBXlite.STRUCTURES[i].toLowerCase()) + ": " + mod_OldDays.lang.get("gui." + (b[i] && structures[i] ? "on" : "off"));
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton){
        if (guibutton.id == 1){
            setDefaultSettings(true);
            applySettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id == 0){
            applySettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else{
            structures[guibutton.id - 2] = !structures[guibutton.id - 2];
            refresh();
        }
    }

    public void setDefaultSettings(boolean b){
        structures = ODNBXlite.getDefaultStructures(b, ODNBXlite.Generator, ODNBXlite.MapFeatures);
    }

    public void applySettings(){
        ODNBXlite.Structures = structures;
    }

    @Override
    public void drawScreen(int paramInt1, int paramInt2, float paramFloat){
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("structures.select"), width / 2, 40, 0xffffff);
        super.drawScreen(paramInt1, paramInt2, paramFloat);
    }
}