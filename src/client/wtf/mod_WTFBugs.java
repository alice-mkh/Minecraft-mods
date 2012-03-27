package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFBugs extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Boosters",      false, "Boosters");
        addProperty(this, 2, "Water lifts",   false, "WaterLifts");
        addProperty(this, 3, "Toasters",      false, "LavaToasters");
        addProperty(this, 4, "Water ladders", false, "WaterLadders");
        addProperty(this, 5, "Ladder gaps",   false, "LadderGaps");
        addProperty(this, 6, "Piston dupe",   false, "PistonDupe");
    }

    public static boolean Boosters = false;
    public static boolean LadderGaps = false;
    public static boolean WaterLadders = false;
    public static boolean LavaToasters = false;
    public static boolean WaterLifts = false;
    public static boolean PistonDupe = false;
//Sand generator
//Data value change bug
//Infinite log burning
}