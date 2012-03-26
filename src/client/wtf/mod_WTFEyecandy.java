package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFEyecandy extends BaseMod{
    public String getVersion(){
        return "1.2.4";
    }

    public mod_WTFEyecandy(){}

    public void load(){};

    public void addRenderer(Map map){
        map.put(net.minecraft.src.EntityEnderman.class, new RenderEnderman2());
    }

    public static boolean ItemSway = true;
    public static boolean Items2D = false;
    public static boolean Bobbing = false;
    public static boolean OldWalking = true;
    public static boolean OldEndermen = true;
}