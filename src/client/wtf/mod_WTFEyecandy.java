package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFEyecandy extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Old walking",           false, "OldWalking");
        addProperty(this, 2, "Bobbing",               false, "Bobbing");
        addProperty(this, 3, "Old endermen",          false, "OldEndermen");
        addProperty(this, 4, "Endermen open mouth",   true,  "EndermenOpenMouth");
        addProperty(this, 5, "Item sway",             true,  "ItemSway");
        addProperty(this, 6, "2D items",              false, "Items2D");
        addProperty(this, 7, "Old chests",            false, "OldChest");
        addProperty(this, 8, "Show mob IDs in F3",    false, "MobLabels");
        loadModuleProperties(3);
    }

    public void callback (int i){
        switch (i){
            case 1: ModelBiped.oldwalking =             OldWalking;        break;
            case 2: RenderLiving.bobbing =              Bobbing;           break;
            case 3: EntityEnderman.smoke =              OldEndermen;
                    RenderEnderman2.greeneyes =         OldEndermen;       break;
            case 4: ModelEnderman.openmouth =           EndermenOpenMouth; break;
            case 5: ItemRenderer.sway =                 ItemSway;          break;
            case 6: ItemRenderer.items2d =              Items2D;           break;
            case 7: BlockChest.normalblock =            OldChest;
                    TileEntityChestRenderer.hidemodel = OldChest;
                    RenderMinecart2.shiftChest =        OldChest;
                    ModLoader.getMinecraftInstance().renderGlobal.loadRenderers(); break;
            case 8: RenderLiving.labels =               MobLabels;         break;
        }
    }

    public void addRenderer(Map map){
        map.put(net.minecraft.src.EntityEnderman.class, new RenderEnderman2());
        map.put(net.minecraft.src.EntityMinecart.class, new RenderMinecart2());
    }

    public static boolean ItemSway = true;
    public static boolean Items2D;
    public static boolean Bobbing;
    public static boolean OldWalking;
    public static boolean OldEndermen;
    public static boolean EndermenOpenMouth = true;
    public static boolean OldChest;
    public static boolean MobLabels;
//Zombies with armor
}