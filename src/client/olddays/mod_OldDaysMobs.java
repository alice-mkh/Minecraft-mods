package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_OldDaysMobs extends mod_OldDays{
    public void load(){
        addProperty(this, 1, "Old mob AI",             false, "OldAI");
        addProperty(this, 2, "Animal panic",           true,  "AnimalsFlee");
        addProperty(this, 3, "Sheep eat grass",        true,  "SheepEatGrass");
        addProperty(this, 4, "Spiders climb walls",    true,  "SpidersCanClimb");
        addProperty(this, 5, "Survival Test mobs",     false, "SurvivalTestMobs");
        loadModuleProperties(3);
    }

    public void callback (int i){
        switch(i){
            case 1: EntityLiving.newai =           !OldAI;
                    EntityCreeper.fixai =           OldAI;
                    EntitySkeleton.fixai =          OldAI;
                    EntitySnowman.fixai =           OldAI;           break;
            case 2: EntityAIPanic.disablePanic =   !AnimalsFlee;
                    EntityCreature.nopanic =       !AnimalsFlee;     break;
            case 3: EntityAIEatGrass2.caneatgrass = SheepEatGrass;   break;
            case 4: EntitySpider.canclimb =         SpidersCanClimb; break;
            case 5: EntityCreeper.survivaltest =    SurvivalTestMobs;
                    EntityCreeper.dark =            SurvivalTestMobs;
                    EntityCreature.fastzombies =    SurvivalTestMobs;
                    EntityZombie.burns =           !SurvivalTestMobs;
                    EntitySpider.survivaltest =     SurvivalTestMobs; break;
        }
    }

    public static boolean OldAI;
    public static boolean AnimalsFlee = true;
    public static boolean SheepEatGrass = true;
    public static boolean SpidersCanClimb = true;
    public static boolean SurvivalTestMobs;
}