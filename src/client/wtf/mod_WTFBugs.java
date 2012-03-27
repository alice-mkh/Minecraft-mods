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

    public static boolean Boosters;
    public static boolean LadderGaps;
    public static boolean WaterLadders;
    public static boolean LavaToasters;
    public static boolean WaterLifts;
    public static boolean PistonDupe;
//Sand generator
//Data value change bug
//Infinite log burning
}