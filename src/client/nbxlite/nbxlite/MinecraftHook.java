package net.minecraft.src.nbxlite;

import java.awt.*;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.ISaveHandler;
import net.minecraft.src.ModLoader;
import net.minecraft.src.PanelCrashReport;
import net.minecraft.src.StatList;
import net.minecraft.src.StatCollector;
import net.minecraft.src.UnexpectedThrowable;
import net.minecraft.src.World;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.WorldSettings;
import net.minecraft.src.WorldType;
import net.minecraft.src.ODNBXlite;
import net.minecraft.src.nbxlite.format.SaveConverterMcRegion;

public final class MinecraftHook extends Minecraft{
    final Frame mcFrame;

    public static ISaveFormat saveLoader = new SaveConverterMcRegion(new File(ModLoader.getMinecraftInstance().getMinecraftDir(), "saves"));

    public MinecraftHook(Component component, Canvas canvas, MinecraftApplet minecraftapplet, int i, int j, boolean flag, Frame frame)
    {
        super(component, canvas, minecraftapplet, i, j, flag);
        mcFrame = frame;
    }

    public static ISaveFormat getSaveLoader2()
    {
        return saveLoader;
    }

    public void displayUnexpectedThrowable(UnexpectedThrowable unexpectedthrowable)
    {
        mcFrame.removeAll();
        mcFrame.add(new PanelCrashReport(unexpectedthrowable), "Center");
        mcFrame.validate();
    }

    public static void startWorldHook(String s, String s1, WorldSettings worldsettings)
    {
        Minecraft mc = ModLoader.getMinecraftInstance();
        mc.changeWorld(null, "", null);
        System.gc();
        if (getSaveLoader2().isOldMapFormat(s) || getSaveLoader2().getWorldInfo(s).getSaveVersion() == 19132)
        {
            if (getSaveLoader2().getWorldInfo(s).getSaveVersion() != 19132){
                convertMapFormatOld(s, s1);
            }
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

    private static void convertMapFormatOld(String s, String s1)
    {
        Minecraft mc = ModLoader.getMinecraftInstance();
        mc.loadingScreen.printText((new StringBuilder()).append("Converting World to ").append(getSaveLoader2().getFormatName()).toString());
        mc.loadingScreen.displayLoadingString("This may take a while :)");
        getSaveLoader2().convertMapFormat(s, mc.loadingScreen);
//         startWorldHook(s, s1, new WorldSettings(0L, 0, true, false, WorldType.DEFAULT));
    }

    private static void convertMapFormat(String s, String s1)
    {
        Minecraft mc = ModLoader.getMinecraftInstance();
        mc.loadingScreen.printText((new StringBuilder()).append("Converting World to ").append(mc.getSaveLoader().getFormatName()).toString());
        mc.loadingScreen.displayLoadingString("This may take a while :)");
        mc.getSaveLoader().convertMapFormat(s, mc.loadingScreen);
        startWorldHook(s, s1, new WorldSettings(0L, 0, true, false, WorldType.DEFAULT));
    }
}