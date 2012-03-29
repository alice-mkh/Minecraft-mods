package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_WTFBehavior extends mod_WTF{
    public void load(){
        addProperty(this, 1, "Old mob AI",          false, "OldAI");
        addProperty(this, 2, "Animal panic",        true,  "AnimalsFlee");
        addProperty(this, 3, "Sheep eat grass",    true,  "SheepsEatGrass");
        loadModuleProperties(4);
    }

    public void callback (int i){
        switch(i){
            case 1: EntityLiving.newai =           !OldAI;
                    EntityCreeper.fixai =           OldAI;
                    EntitySkeleton.fixai =          OldAI;
                    EntitySnowman.fixai =           OldAI;          break;
            case 2: EntityAIPanic.disablePanic =   !AnimalsFlee;
                    EntityCreature.nopanic =       !AnimalsFlee;    break;
            case 3: EntityAIEatGrass2.caneatgrass = SheepsEatGrass; break;
        }
    }

    public static boolean OldAI;
    public static boolean AnimalsFlee = true;
    public static boolean SheepsEatGrass = true;
}