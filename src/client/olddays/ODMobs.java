package net.minecraft.src;

public class ODMobs extends OldDaysModule{
    public ODMobs(mod_OldDays c){
        super(c, 3, "Mobs");
        new OldDaysPropertyInt(this,  1, 1,     3,     "AI", 3).setUseNames();
        new OldDaysPropertyBool(this, 2, false, true,  "AnimalsFlee");
        new OldDaysPropertyBool(this, 3, true,  true,  "SheepEatGrass");
        new OldDaysPropertyBool(this, 4, true,  true,  "SpidersCanClimb");
        new OldDaysPropertyBool(this, 5, false, false, "SurvivalTestMobs");
        new OldDaysPropertyBool(this, 6, true,  false, "FastSkeletons");
        new OldDaysPropertyBool(this, 7, true,  false, "OldHealth");
        new OldDaysPropertyInt(this,  8, 10,    10,    "Mobs", 10).setUseNames();
        new OldDaysPropertyInt(this,  9, 4,     4,     "Slimes", 4).setUseNames();
        new OldDaysPropertyBool(this, 10,false, false, "Endermen");
    }

    public void callback (int i){
        switch(i){
            case 1: set(EntityLiving.class, "newai", AI>2);
                    set(EntityCreeper.class, "fixai", AI<3);
                    set(EntitySkeleton.class, "fixai", AI<3);
                    set(EntitySnowman.class, "fixai", AI<3);
                    set(EntitySheep.class, "fixai", AI<3);
                    set(EntityWolf.class, "fixai", AI<3);
                    set(EntityCreature.class, "jump", AI<2);
                    set(EntityLiving.class, "infdevai", AI<1); break;
            case 2: set(EntityAIPanic.class, "disablePanic", !AnimalsFlee);
                    set(EntityCreature.class, "nopanic", !AnimalsFlee); break;
            case 3: set(EntitySheep.class, "caneatgrass", SheepEatGrass); break;
            case 4: set(EntitySpider.class, "canclimb", SpidersCanClimb); break;
            case 5: set(EntityCreeper.class, "survivaltest", SurvivalTestMobs);
                    set(EntityCreeper.class, "dark", SurvivalTestMobs);
                    set(EntityCreature.class, "fastzombies", SurvivalTestMobs);
                    set(EntityZombie.class, "burns", !SurvivalTestMobs);
                    set(EntitySkeleton.class, "survivaltest", SurvivalTestMobs);
                    set(EntitySpider.class, "survivaltest", SurvivalTestMobs);
                    set(EntitySheep.class, "survivaltest", SurvivalTestMobs);
                    set(EntitySheep.class, "hungry", SurvivalTestMobs);
                    set(EntityPig.class, "survivaltest", SurvivalTestMobs);
                    set(EntityLiving.class, "survivaltest", SurvivalTestMobs); break;
            case 6: set(EntitySkeleton.class, "fast", FastSkeletons); break;
            case 7: set(EntitySheep.class, "oldhealth", OldHealth);
                    set(EntitySpider.class, "oldhealth", OldHealth);
                    set(EntityEnderman.class, "oldhealth", OldHealth); break;
            case 8: set(EntityLiving.class, "nonewmobs", Mobs);
                    int color = 0;
                    if (Mobs>5){
                        color = 1;
                    }
                    if (Mobs>6){
                        color = 2;
                    }
                    set(EntitySheep.class, "color", color);
                    set(EntityOcelot.class, "allow", Mobs>=10);
                    set(EntitySquid.class, "allow", Mobs>=6);
                    set(EntitySlime.class, "allow", Mobs>=3); break;
            case 9: set(EntitySlime.class, "slimeSpawn", Slimes); break;
            case 10:set(EntityEnderman.class, "oldPicking", Endermen); break;
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