package net.minecraft.src.ssp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;

public class GuiOverlay{
    private static List<GuiOverlay> list;
    private Class guiClass;

    public GuiOverlay(Class c){
        guiClass = c;
        list.add(this);
    }

    public void unregisterOverlay(){
        list.remove(this);
    }

    public static void addOverlays(GuiScreen gui){
        for (GuiOverlay overlay : list){
            if (overlay.guiClass.isInstance(gui) && overlay.shouldBeAdded()){
                gui.overlays.add(overlay);
                overlay.onAdded(gui);
            }
        }
    }

    public boolean shouldBeAdded(){
        return true;
    }

    public void onAdded(GuiScreen gui){
    }

    public void initGui(List buttonList, int width, int height){
    }

    public boolean actionPerformed(GuiScreen gui, GuiButton button){
        return true;
    }

    protected void moveButton(List buttonList, int i, int x, int y){
        GuiButton b = getButtonById(buttonList, i);
        b.xPosition = x;
        b.yPosition = y;
    }

    protected void hideButton(List buttonList, int i, boolean show){
        getButtonById(buttonList, i).drawButton = show;
    }

    protected void disableButton(List buttonList, int i, boolean enable){
        getButtonById(buttonList, i).enabled = enable;
    }

    protected void resizeButton(List buttonList, int i, int x, int y){
        GuiButton b = getButtonById(buttonList, i);
        try{
            Field width = (GuiButton.class).getDeclaredFields()[1];
            Field height = (GuiButton.class).getDeclaredFields()[2];
            width.setAccessible(true);
            height.setAccessible(true);
            width.set(b, x);
            height.set(b, y);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    protected void renameButton(List buttonList, int i, String newName){
        getButtonById(buttonList, i).displayString = newName;
    }

    private GuiButton getButtonById(List buttonList, int i){
        for (Object o : buttonList){
            GuiButton b = (GuiButton)o;
            if (b.id == i){
                return b;
            }
        }
        return null;
    }

    public boolean preDrawScreen(GuiScreen gui, int x, int y, float f){
        return true;
    }

    public void postDrawScreen(GuiScreen gui, int x, int y, float f){}

    public boolean preUpdateScreen(GuiScreen gui){
        return true;
    }

    public String applyStringOverrides(String str, int x, int y, int width, int height){
        return str;
    }

    public void postUpdateScreen(GuiScreen gui){}

    static{
        list = new ArrayList<GuiOverlay>();
    }
}