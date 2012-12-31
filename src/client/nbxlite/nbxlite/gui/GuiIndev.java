package net.minecraft.src.nbxlite.gui;

import java.util.List;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.mod_OldDays;
import net.minecraft.src.ODNBXlite;
import net.minecraft.src.StatCollector;

public final class GuiIndev extends GuiScreen{
    private GuiScreen parent;
    private int mapType = GeneratorList.typedefault;
    private int mapShape = GeneratorList.shapedefault;
    private int mapSize = GeneratorList.sizedefault;
    private int mapTheme = GeneratorList.themedefault;

    public GuiIndev(GuiScreen gui){
        parent = gui;
    }

    public void initGui(){
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4, mod_OldDays.lang.get("indevType")+": "));
        controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 24, mod_OldDays.lang.get("indevShape")+": "));
        controlList.add(new GuiButton(2, width / 2 - 100, height / 4 + 48, mod_OldDays.lang.get("indevSize")+": "));
        controlList.add(new GuiButton(3, width / 2 - 100, height / 4 + 72, mod_OldDays.lang.get("nbxlite.maptheme.name")+": "));
        controlList.add(new GuiButton(4, width / 2 - 100, height / 4 + 96 + 12, mod_OldDays.lang.get("indev.create")));
        controlList.add(new GuiButton(5, width / 2 - 100, height / 4 + 120 + 12, StatCollector.translateToLocal("gui.cancel")));
        refresh();
    }

    private void refresh(){
        ((GuiButton)controlList.get(0)).displayString = (mod_OldDays.lang.get("indevType")+": "+mod_OldDays.lang.get(GeneratorList.typename[mapType]));
        ((GuiButton)controlList.get(1)).displayString = (mod_OldDays.lang.get("indevShape")+": "+mod_OldDays.lang.get(GeneratorList.shapename[mapShape]));
        ((GuiButton)controlList.get(2)).displayString = (mod_OldDays.lang.get("indevSize")+": " +mod_OldDays.lang.get(GeneratorList.sizename[mapSize]));
        ((GuiButton)controlList.get(3)).displayString = (mod_OldDays.lang.get("nbxlite.maptheme.name")+": "+mod_OldDays.lang.get(GeneratorList.themename[mapTheme]));
    }

    protected void actionPerformed(GuiButton guibutton){
        if (guibutton.id == 5){
            mc.displayGuiScreen(parent);
        }else if (guibutton.id == 4){
            ODNBXlite.IndevMapType = mapType;
            ODNBXlite.MapTheme = mapTheme;
            ODNBXlite.IndevWidthX = 128 << mapSize;
            ODNBXlite.IndevWidthZ = 128 << mapSize;
            ODNBXlite.IndevHeight = 64;
            if (mapShape == 1){
                ODNBXlite.IndevWidthX /= 2;
                ODNBXlite.IndevWidthZ <<= 1;
            }else if (mapShape == 2){
                ODNBXlite.IndevWidthZ /= 2;
                ODNBXlite.IndevWidthX /= 2;
                ODNBXlite.IndevHeight = 256;
            }
            ODNBXlite.GenerateNewOres = ODNBXlite.DefaultNewOres;
            ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
            ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
            ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
            ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
            ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).forceCreate();
            }
        }else if (guibutton.id == 0){
            mapType = (mapType + 1) % (GeneratorList.typelength + 1);
        }else if (guibutton.id == 1){
            mapShape = (mapShape + 1) % GeneratorList.shapename.length;
        }else if (guibutton.id == 2){
            mapSize = (mapSize + 1) % GeneratorList.sizename.length;
        }else if (guibutton.id == 3){
            mapTheme = (mapTheme + 1) % (GeneratorList.themelength + 1);
        }
        refresh();
    }

    public void drawScreen(int paramInt1, int paramInt2, float paramFloat){
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("indev.generate"), width / 2, 40, 0xffffff);
        super.drawScreen(paramInt1, paramInt2, paramFloat);
    }
}