package net.minecraft.src;

public class ODMobs extends OldDaysModule{
    public ODMobs(mod_OldDays c){
        super(c, 3, "Mobs");
        new OldDaysPropertyInt(this,  1, 1,     4,     "AI", 4).setUseNames();
        new OldDaysPropertyBool(this, 2, false, true,  "AnimalsFlee");
        new OldDaysPropertyBool(this, 3, true,  true,  "SheepEatGrass");
        new OldDaysPropertyBool(this, 4, true,  true,  "SpidersCanClimb");
        new OldDaysPropertyBool(this, 5, false, false, "SurvivalTestMobs");
        new OldDaysPropertyBool(this, 6, true,  false, "FastSkeletons");
        new OldDaysPropertyBool(this, 7, true,  false, "OldHealth");
        new OldDaysPropertyInt(this,  8, 10,    11,    "Mobs", 11).setUseNames();
        new OldDaysPropertyInt(this,  9, 5,     5,     "Slimes", 5).setUseNames();
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
                    set(EntityPig.class, "fixai", AI<3);
                    set(EntityCreature.class, "jump", AI<2);
                    set(EntityCreature.class, "indevai", AI<1);
                    set(EntityLiving.class, "indevai", AI<1);
                    set(Entity.class, "oldrange", AI<2);
                    set(EntityLiving.class, "oldrange", AI<4);
                    set(EntityCreeper.class, "oldrange", AI<4);
                    set(EntityAITarget.class, "oldai", AI<4); break;
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
                    set(EntitySpider.class, "jockeys", Mobs>=5);
                    set(EntityZombie.class, "custom", Mobs>=11);
                    set(EntitySkeleton.class, "custom", Mobs>=11);
                    set(EntitySlime.class, "allow", Mobs>=3); break;
            case 9: set(EntitySlime.class, "slimeSpawn", Slimes); break;
            case 10:set(EntityEnderman.class, "oldPicking", Endermen); break;
        }
        if (!renderersAdded && RenderManager.instance!=null){
            addEntity(EntitySteve.class, "Steve", 201);
            addEntity(EntityRana.class, "Rana", 202);
            String str = "/olddays/md3/";
            addRenderer(EntitySteve.class, new EntitySteve.RenderMD3Steve(false, str + "mcexport01.MD3", str + "mcexport"));
            addRenderer(EntityRana.class, new RenderMD3(false, str + "rana.MD3", str + "cube-nes.png"));
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