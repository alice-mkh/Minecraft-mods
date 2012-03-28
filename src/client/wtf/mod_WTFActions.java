package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFActions extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Punch TNT",           false, "PunchTNT");
        addProperty(this, 2, "Extinguish TNT",      false, "ExtinguishTNT");
        addProperty(this, 3, "Smelt items on fire", false, "SmeltOnFire");
        addProperty(this, 4, "Unnerfed fire",       false, "OldFire");
        addProperty(this, 5, "Punch sheep",         false, "PunchSheep");
        addProperty(this, 6, "Animal panic",        true,  "AnimalsFlee");
        loadModuleProperties(0);
    }

    public void callback (int i){
        switch(i){
            case 1: BlockTNT.punchToActivate =    PunchTNT;      break;
            case 2: EntityTNTPrimed.extinguish =  ExtinguishTNT; break;
            case 3: EntityItem.smeltOnFire =      SmeltOnFire;   break;
            case 4: BlockFire.oldFire =           OldFire;       break;
            case 5: EntitySheep.punchToShear =    PunchSheep;    break;
            case 6: EntityAIPanic.disablePanic = !AnimalsFlee;   break;
        }
    }

    public static boolean SmeltOnFire;
    public static boolean PunchTNT;
    public static boolean ExtinguishTNT;
    public static boolean OldFire;
    public static boolean PunchSheep;
    public static boolean AnimalsFlee = true;
//Old tools
//Old blocks
}