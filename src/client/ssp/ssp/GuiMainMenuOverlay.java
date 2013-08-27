package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.DemoWorldServer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Minecraft;

public class GuiMainMenuOverlay extends GuiOverlay{
    public GuiMainMenuOverlay(){
        super(GuiMainMenu.class);
    }

    @Override
    public boolean actionPerformed(GuiScreen gui, GuiButton par1GuiButton){
        if (par1GuiButton.id == 11){
            Minecraft mc = Minecraft.getMinecraft();
            mc.enableSP = mc.useSP;
            if (mc.enableSP){
                mc.playerController = new PlayerControllerDemo(mc);
                mc.startWorldSSP("Demo_World", "Demo_World", DemoWorldServer.demoWorldSettings);
                mc.displayGuiScreen(null);
            }
            return !mc.enableSP;
        }
        return true;
    }
}
