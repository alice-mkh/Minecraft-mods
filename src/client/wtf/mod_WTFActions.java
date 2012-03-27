package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFActions extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Punch TNT",           false, "PunchTNT");
        addProperty(this, 2, "Extinguish TNT",      false, "ExtinguishTNT");
        addProperty(this, 3, "Smelt items on fire", false, "SmeltOnFire");
        addProperty(this, 4, "Unnerfed fire",       false, "FastFire");
        addProperty(this, 5, "Punch sheep",         false, "PunchSheep");
        addProperty(this, 6, "Animal panic",        false, "AnimalsFlee");
    }

    public static boolean SmeltOnFire;
    public static boolean PunchTNT;
    public static boolean ExtinguishTNT;
    public static boolean FastFire;
    public static boolean PunchSheep;
    public static boolean AnimalsFlee;
//Old tools
//Old blocks
}