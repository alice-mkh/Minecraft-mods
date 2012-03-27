package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFEyecandy extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Old walking",         false, "OldWalking");
        addProperty(this, 2, "Bobbing",             false, "Bobbing");
        addProperty(this, 3, "Old endermen",        false, "OldEndermen");
        addProperty(this, 4, "Endermen open mouth", false, "EndermenOpenMouth");
        addProperty(this, 5, "Item sway",           false, "ItemSway");
        addProperty(this, 6, "2D items",            false, "Items2D");
    }

    public void addRenderer(Map map){
        map.put(net.minecraft.src.EntityEnderman.class, new RenderEnderman2());
    }

    public static boolean ItemSway = true;
    public static boolean Items2D = false;
    public static boolean Bobbing = false;
    public static boolean OldWalking = false;
    public static boolean OldEndermen = false;
    public static boolean EndermenOpenMouth = true;
//Chest
//Zombies with armor
}