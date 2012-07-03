package net.minecraft.src;

public class ODMobs extends OldDaysModule{
    public ODMobs(mod_OldDays c){
        super(c, 3, "Mobs");
        addProperty(1, "Mob AI",              3,     1,     "AI",            "", new String[]{"Infdev", "Alpha", "Beta", "1.2"});
        addProperty(2, "Animal panic",        true,  false, "AnimalsFlee",      "");
        addProperty(3, "Sheep eat grass",     true,  true,  "SheepEatGrass",    "");
        addProperty(4, "Spiders climb walls", true,  true,  "SpidersCanClimb",  "");
        addProperty(5, "Survival Test mobs",  false, false, "SurvivalTestMobs", "");
        addProperty(6, "Old skeleton fire",   false, false, "FastSkeletons",    "");
        addProperty(7, "Old mob health",      false, false, "OldHealth",        "");
        addProperty(8, "Allowed mobs",        10,    10,    "Mobs",             "", new String[]{"Classic", "Indev", "Alpha 1.0.8", "Alpha 1.0.11", "Alpha 1.0.14", "Alpha 1.2.0",
                                                                                                 "Beta 1.2", "Beta 1.7.3", "Beta 1.8.1", "1.1", "1.2"});
        addProperty(9, "Slime spawning",      3,     3,     "Slimes",           "", new String[]{"OFF", "Alpha 1.0.11", "Beta", "1.0"});
        addProperty(10,"Unnerfed endermen",   false, false, "Endermen",         "");
    }

    public void callback (int i){
        switch(i){
            case 1: setBool(net.minecraft.src.EntityLiving.class, "newai", AI>2);
                    setBool(net.minecraft.src.EntityCreeper.class, "fixai", AI<3);
                    setBool(net.minecraft.src.EntitySkeleton.class, "fixai", AI<3);
                    setBool(net.minecraft.src.EntitySnowman.class, "fixai", AI<3);
                    setBool(net.minecraft.src.EntitySheep.class, "fixai", AI<3);
                    setBool(net.minecraft.src.EntityCreature.class, "jump", AI<2);
                    setBool(net.minecraft.src.EntityLiving.class, "infdevai", AI<1); break;
            case 2: setBool(net.minecraft.src.EntityAIPanic.class, "disablePanic", !AnimalsFlee);
                    setBool(net.minecraft.src.EntityCreature.class, "nopanic", !AnimalsFlee); break;
            case 3: setBool(net.minecraft.src.EntitySheep.class, "caneatgrass", SheepEatGrass); break;
            case 4: setBool(net.minecraft.src.EntitySpider.class, "canclimb", SpidersCanClimb); break;
            case 5: setBool(net.minecraft.src.EntityCreeper.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityCreeper.class, "dark", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityCreature.class, "fastzombies", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityZombie.class, "burns", !SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySkeleton.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySpider.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySheep.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntitySheep.class, "hungry", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityPig.class, "survivaltest", SurvivalTestMobs);
                    setBool(net.minecraft.src.EntityLiving.class, "survivaltest", SurvivalTestMobs); break;
            case 6: setBool(net.minecraft.src.EntitySkeleton.class, "fast", FastSkeletons); break;
            case 7: setBool(net.minecraft.src.EntitySheep.class, "oldhealth", OldHealth);
                    setBool(net.minecraft.src.EntitySpider.class, "oldhealth", OldHealth);
                    setBool(net.minecraft.src.EntityEnderman.class, "oldhealth", OldHealth); break;
            case 8: setInt(net.minecraft.src.EntityLiving.class, "nonewmobs", Mobs);
                    int color = 0;
                    if (Mobs>5){
                        color = 1;
                    }
                    if (Mobs>6){
                        color = 2;
                    }
                    setInt(net.minecraft.src.EntitySheep.class, "color", color);
                    setBool(net.minecraft.src.EntityOcelot.class, "allow", Mobs>=10);
                    setBool(net.minecraft.src.EntitySquid.class, "allow", Mobs>=6);
                    setBool(net.minecraft.src.EntitySlime.class, "allow", Mobs>=3); break;
            case 9: setInt(net.minecraft.src.EntitySlime.class, "slimeSpawn", Slimes); break;
            case 10:setBool(net.minecraft.src.EntityEnderman.class, "oldPicking", Endermen); break;
        }
    }

    public static int AI = 1;
    public static boolean AnimalsFlee;
    public static boolean SheepEatGrass = true;
    public static boolean SpidersCanClimb = true;
    public static boolean SurvivalTestMobs;
    public static boolean JumpingMobs = true;
    public static boolean FastSkeletons;
    public static boolean OldHealth = true;
    public static int Mobs = 10;
    public static int Slimes = 3;
    public static boolean Endermen;
}