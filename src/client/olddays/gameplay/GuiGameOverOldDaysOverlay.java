package net.minecraft.src;

import net.minecraft.src.ssp.GuiOverlay;

public class GuiGameOverOldDaysOverlay extends GuiOverlay{
    public static boolean oldScore = false;

    public GuiGameOverOldDaysOverlay(){
        super(GuiGameOver.class);
    }

    @Override
    public boolean shouldBeAdded(){
        return oldScore;
    }

    @Override
    public String applyStringOverrides(String str, int x, int y, int width, int height){
        if (x == width / 2 && y == 100){
            return str.replaceAll("§", "&");
        }
        return super.applyStringOverrides(str, x, y, width, height);
    }
}
