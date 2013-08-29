package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.Minecraft;

public class GuiMultiplayerOverlay extends GuiOverlay{
    public GuiMultiplayerOverlay(){
        super(GuiMultiplayer.class);
    }

    public void initGui(List buttonList, int width, int height){
        Minecraft.getMinecraft().enableSP = false;
    }
}
