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
        loadModuleProperties(1);
    }

    public void callback (int i){
        switch(i){
            case 1: EntityMinecart.boosters = WaterLifts;   break;
            case 2: EntityBoat.waterlift =    WaterLifts;   break;
            case 3: Entity.toaster =          LavaToasters; break;
            case 4: Entity.waterladder =      WaterLadders; break;
            case 5: EntityLiving.laddergaps = LadderGaps;   break;
            case 6: BlockPistonBase.dupe =    PistonDupe;   break;
        }
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