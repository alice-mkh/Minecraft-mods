package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_OldDaysBehavior extends mod_OldDays{
    public void load(){
        addProperty(this, 1, "Old mob AI",             false, "OldAI");
        addProperty(this, 2, "Animal panic",           true,  "AnimalsFlee");
        addProperty(this, 3, "Sheep eat grass",        true,  "SheepEatGrass");
        addProperty(this, 4, "Spiders climb walls",    true,  "SpidersCanClimb");
        addProperty(this, 5, "Survival Test creepers", false, "SurvivalTestCreepers");
        addProperty(this, 6, "Survival Test zombies",  false, "SurvivalTestZombies");
        addProperty(this, 7, "Survival Test spiders",  false, "SurvivalTestSpiders");
        loadModuleProperties(4);
    }

    public void callback (int i){
        switch(i){
            case 1: EntityLiving.newai =           !OldAI;
                    EntityCreeper.fixai =           OldAI;
                    EntitySkeleton.fixai =          OldAI;
                    EntitySnowman.fixai =           OldAI;                break;
            case 2: EntityAIPanic.disablePanic =   !AnimalsFlee;
                    EntityCreature.nopanic =       !AnimalsFlee;          break;
            case 3: EntityAIEatGrass2.caneatgrass = SheepEatGrass;        break;
            case 4: EntitySpider.canclimb =         SpidersCanClimb;      break;
            case 5: EntityCreeper.survivaltest =    SurvivalTestCreepers; break;
            case 6: EntityCreature.fastzombies =    SurvivalTestZombies;
                    EntityZombie.burns =           !SurvivalTestZombies;  break;
            case 7: EntitySpider.survivaltest =     SurvivalTestSpiders;  break;
        }
    }

    public static boolean OldAI;
    public static boolean AnimalsFlee = true;
    public static boolean SheepEatGrass = true;
    public static boolean SpidersCanClimb = true;
    public static boolean SurvivalTestCreepers;
    public static boolean SurvivalTestZombies;
    public static boolean SurvivalTestSpiders;
}