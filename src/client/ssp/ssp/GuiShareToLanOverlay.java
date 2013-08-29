package net.minecraft.src.ssp;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiShareToLan;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Minecraft;

public class GuiShareToLanOverlay extends GuiOverlay{
    public GuiShareToLanOverlay(){
        super(GuiShareToLan.class);
    }

    @Override
    public boolean shouldBeAdded(){
        return Minecraft.getMinecraft().enableSP;
    }

    @Override
    public boolean actionPerformed(GuiScreen gui, GuiButton button){
        if (button.id == 101){
            Minecraft mc = Minecraft.getMinecraft();
            mc.displayGuiScreen(null);
            mc.quitAndStartServer();
        }
        return true;
    }
}
