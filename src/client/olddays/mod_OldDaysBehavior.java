package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_OldDaysBehavior extends mod_OldDays{
    public void load(){
        addProperty(this, 1, "Old mob AI",          false, "OldAI");
        addProperty(this, 2, "Animal panic",        true,  "AnimalsFlee");
        addProperty(this, 3, "Sheep eat grass",     true,  "SheepsEatGrass");
        addProperty(this, 4, "Survival test mobs",  false, "SurvivalTestMobs");
        loadModuleProperties(4);
    }

    public void callback (int i){
        switch(i){
            case 1: EntityLiving.newai =           !OldAI;
                    EntityCreeper.fixai =           OldAI;
                    EntitySkeleton.fixai =          OldAI;
                    EntitySnowman.fixai =           OldAI;            break;
            case 2: EntityAIPanic.disablePanic =   !AnimalsFlee;
                    EntityCreature.nopanic =       !AnimalsFlee;      break;
            case 3: EntityAIEatGrass2.caneatgrass = SheepsEatGrass;   break;
            case 4: EntityCreature.fastzombies =    SurvivalTestMobs;
                    EntityCreeper.survivaltest =    SurvivalTestMobs; break;
        }
    }

    public void addRenderer(Map map){
        map.put(net.minecraft.src.EntityCreeper.class, new RenderDarkCreeper());
    }

    public static boolean OldAI;
    public static boolean AnimalsFlee = true;
    public static boolean SheepsEatGrass = true;
    public static boolean SurvivalTestMobs;
}