package net.minecraft.src;

public class mod_OldDaysMobs extends mod_OldDays{
    public void load(){
        registerModule(3);
        addProperty(this, 1, "Old mob AI",          true,  "OldAI");
        addProperty(this, 2, "Animal panic",        false, "AnimalsFlee");
        addProperty(this, 3, "Sheep eat grass",     true,  "SheepEatGrass");
        addProperty(this, 4, "Spiders climb walls", true,  "SpidersCanClimb");
        addProperty(this, 5, "Survival Test mobs",  false, "SurvivalTestMobs");
        addProperty(this, 6, "Jumping mobs",        true,  "JumpingMobs");
        addProperty(this, 7, "Old skeleton fire",   true,  "FastSkeletons");
        loadModuleProperties();
    }

    public void callback (int i){
        switch(i){
            case 1: EntityLiving.newai =           !OldAI;
                    EntityCreeper.fixai =           OldAI;
                    EntitySkeleton.fixai =          OldAI;
                    EntitySnowman.fixai =           OldAI;            break;
            case 2: EntityAIPanic.disablePanic =   !AnimalsFlee;
                    EntityCreature.nopanic =       !AnimalsFlee;      break;
            case 3: EntityAIEatGrass2.caneatgrass = SheepEatGrass;    break;
            case 4: EntitySpider.canclimb =         SpidersCanClimb;  break;
            case 5: EntityCreeper.survivaltest =    SurvivalTestMobs;
                    EntityCreeper.dark =            SurvivalTestMobs;
                    EntityCreature.fastzombies =    SurvivalTestMobs;
                    EntityZombie.burns =           !SurvivalTestMobs;
                    EntitySkeleton.survivaltest =   SurvivalTestMobs;
                    EntitySpider.survivaltest =     SurvivalTestMobs; break;
            case 6: EntityCreature.jump =           JumpingMobs;      break;
            case 7: EntitySkeleton.fast =           FastSkeletons;    break;
        }
    }

    public static boolean OldAI = true;
    public static boolean AnimalsFlee = false;
    public static boolean SheepEatGrass = true;
    public static boolean SpidersCanClimb = true;
    public static boolean SurvivalTestMobs;
    public static boolean JumpingMobs = true;
    public static boolean FastSkeletons = true;
}