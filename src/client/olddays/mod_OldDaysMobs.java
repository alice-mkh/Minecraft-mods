package net.minecraft.src;

public class mod_OldDaysMobs extends mod_OldDays{
    public void load(){
        registerModule(3);
        addProperty(this, 1, "Old mob AI",          0, 1, "OldAI",            "");
        addProperty(this, 2, "Animal panic",        1, 0, "AnimalsFlee",      "");
        addProperty(this, 3, "Sheep eat grass",     1, 1, "SheepEatGrass",    "");
        addProperty(this, 4, "Spiders climb walls", 1, 1, "SpidersCanClimb",  "");
        addProperty(this, 5, "Survival Test mobs",  0, 0, "SurvivalTestMobs", "");
        addProperty(this, 6, "Jumping mobs",        0, 1, "JumpingMobs",      "");
        addProperty(this, 7, "Old skeleton fire",   0, 0, "FastSkeletons",    "");
        loadModuleProperties();
    }

    public void callback (int i){
        switch(i){
            case 1: setBool(net.minecraft.src.EntityLiving.class, "newai", !OldAI);
                    setBool(net.minecraft.src.EntityCreeper.class, "fixai", OldAI);
                    setBool(net.minecraft.src.EntitySkeleton.class, "fixai", OldAI);
                    setBool(net.minecraft.src.EntitySnowman.class, "fixai", OldAI); break;
            case 2: setBool(net.minecraft.src.EntityAIPanic.class, "disablePanic", !AnimalsFlee);
                    setBool(net.minecraft.src.EntityCreature.class, "nopanic", !AnimalsFlee); break;
            case 3: setBool(net.minecraft.src.EntityAIEatGrass2.class, "caneatgrass", SheepEatGrass); break;
            case 4: setBool(net.minecraft.src.EntitySpider.class, "canclimb", SpidersCanClimb); break;
            case 5: setBool(net.minecraft.src.EntityCreeper.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityCreeper.class, "dark", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityCreature.class, "fastzombies", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityZombie.class, "burns", !SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySkeleton.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySpider.class, "survivaltest", SurvivalTestMobs); break;
            case 6: setBool(net.minecraft.src.EntityCreature.class, "jump", JumpingMobs); break;
            case 7: setBool(net.minecraft.src.EntitySkeleton.class, "fast", FastSkeletons); break;
        }
    }

    public static boolean OldAI = true;
    public static boolean AnimalsFlee = false;
    public static boolean SheepEatGrass = true;
    public static boolean SpidersCanClimb = true;
    public static boolean SurvivalTestMobs;
    public static boolean JumpingMobs = true;
    public static boolean FastSkeletons;
//Creepers see through blocks;
}