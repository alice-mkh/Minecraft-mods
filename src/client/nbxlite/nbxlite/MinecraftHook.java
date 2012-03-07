package net.minecraft.src.nbxlite;

import java.awt.*;
import net.minecraft.src.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;

public final class MinecraftHook extends Minecraft{
    final Frame mcFrame;

    public MinecraftHook(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag, Frame frame)
    {
        super(component, canvas, minecraftapplet, i, j, flag);
        mcFrame = frame;
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable unexpectedthrowable)
    {
        mcFrame.removeAll();
        mcFrame.add(new PanelCrashReport(unexpectedthrowable), "Center");
        mcFrame.validate();
    }
/*
    public static void startWorldHook(String s, String s1, WorldSettings worldsettings)
    {
        Minecraft mc = ModLoader.getMinecraftInstance();
        mc.changeWorld(null, "", null);
        System.gc();
//         System.out.println("LOL");
        if (mc.getSaveLoader().isOldMapFormat(s))
        {
            convertMapFormat(s, s1);
        }
        else
        {
            if (mc.loadingScreen != null)
            {
                mc.loadingScreen.printText(StatCollector.translateToLocal("menu.switchingLevel"));
                mc.loadingScreen.displayLoadingString("");
            }
            ISaveHandler isavehandler = mc.getSaveLoader().getSaveLoader(s, false);
            World world = null;
            world = new World(isavehandler, s1, worldsettings);
            if (world.isNewWorld)
            {
                mc.statFileWriter.readStat(StatList.createWorldStat, 1);
                mc.statFileWriter.readStat(StatList.startGameStat, 1);
                mc.changeWorld(world, StatCollector.translateToLocal("menu.generatingLevel"), null);
            }
            else
            {
                mc.statFileWriter.readStat(StatList.loadWorldStat, 1);
                mc.statFileWriter.readStat(StatList.startGameStat, 1);
                mc.changeWorld(world, StatCollector.translateToLocal("menu.loadingLevel"), null);
            }
        }
    }

    public static void convertMapFormat(String s, String s1)
    {
        Minecraft mc = ModLoader.getMinecraftInstance();
        mc.loadingScreen.printText((new StringBuilder()).append("Converting World to ").append(mc.getSaveLoader().getFormatName()).toString());
        mc.loadingScreen.displayLoadingString("This may take a while :)");
        mc.getSaveLoader().convertMapFormat(s, mc.loadingScreen);
        startWorldHook(s, s1, new WorldSettings(0L, 0, true, false, EnumWorldType.DEFAULT));
    }*/
}