package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFActions extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Punch TNT",           false, "PunchTNT");
        addProperty(this, 2, "Extinguish TNT",      false, "ExtinguishTNT");
        addProperty(this, 3, "Smelt items on fire", false, "SmeltOnFire");
        addProperty(this, 4, "Unnerfed fire",           false, "FastFire");
    }

    public static boolean SmeltOnFire = false;
    public static boolean PunchTNT = false;
    public static boolean ExtinguishTNT = false;
    public static boolean FastFire = false;
//Old tools
//Old blocks
}