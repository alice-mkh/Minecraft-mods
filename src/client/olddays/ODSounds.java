package net.minecraft.src;

public class ODSounds extends OldDaysModule{
    public ODSounds(mod_OldDays c){
        super(c, 5, "Sounds");
        new OldDaysPropertyBool(this, 1, true,  false, "Explode");
        new OldDaysPropertyBool(this, 2, true,  false, "XPOrb");
        new OldDaysPropertyInt(this,  3, 0,     2,     "Ooh", 2).setUseNames();
        new OldDaysPropertyInt(this,  4, 0,     2,     "Fall", 2).setUseNames();
        new OldDaysPropertyBool(this, 5, true,  false, "Door");
        new OldDaysPropertyInt(this,  6, 0,     2,     "Chest", 2).setUseNames();
        new OldDaysPropertyBool(this, 7, true,  false, "Bow");
        new OldDaysPropertyBool(this, 8, true,  false, "Drr");
        new OldDaysPropertyBool(this, 9, false, true,  "Eat");
        new OldDaysPropertyBool(this, 10,true,  true,  "Drink");
        new OldDaysPropertyBool(this, 11,false, true,  "Break");
        new OldDaysPropertyInt(this,  12,0,     2,     "Lava", 2).setUseNames();
        new OldDaysPropertyInt(this,  13,1,     2,     "Enderman", 2).setUseNames();
        new OldDaysPropertyBool(this, 14,true,  false, "Calm4");
        new OldDaysPropertyBool(this, 15,false, false, "Creeper");
        new OldDaysPropertyBool(this, 16,true,  false, "Steps");
        new OldDaysPropertyBool(this, 17,true,  false, "MobSteps");
        new OldDaysPropertyBool(this, 18,true,  false, "Cow");
        new OldDaysPropertyBool(this, 19,true,  false, "Slime");
        new OldDaysPropertyBool(this, 20,true,  false, "Skeleton");
        new OldDaysPropertyBool(this, 21,false, true,  "Levelup");
        new OldDaysPropertyBool(this, 22,true,  false, "Ignite");
        new OldDaysPropertyBool(this, 23,true,  true,  "Shear");
        new OldDaysPropertyBool(this, 24,true,  false, "Splash");
        new OldDaysPropertyBool(this, 25,false, true, "Swimming");
        new OldDaysPropertyBool(this, 26,false, true, "Minecart");
        isLocal = true;
        Minecraft.getMinecraft().setSoundManager(SoundManager2.class);
        addSound(1, "explode");
        addSound(3, "hurtflesh1");
        addSound(3, "hurtflesh2");
        addSound(3, "hurtflesh3");
        addSound(4, "fallbig1");
        addSound(4, "fallbig2");
        addSound(4, "fallsmall");
        addSound(5, "door_open");
        addSound(5, "door_close");
        addSound(6, "door_open");
        addSound(6, "door_close");
        addSound(7, "bow");
        addSound(8, "drr");
        addSound(12,"lava");
        addMusic(14,"calm4");
        addSound(18,"cowhurt1");
        addSound(18,"cowhurt2");
        addSound(18,"cowhurt3");
        for (int i = 1; i <= 4; i++){
            addSound(15,"creeper"+i);
            addSound(17,"cow"+i);
            addSound(20,"skeletonhurt"+i);
        }
        addSound(22,"ignite");
        addSound(24,"splash");
    }

    @Override
    public void callback (int i){
        switch(i){
            case 1: set(SoundManager2.class, "explode", Explode); break;
            case 2: set(SoundManager2.class, "xporb", XPOrb); break;
            case 3: set(SoundManager2.class, "hurt", Ooh); break;
            case 4: set(SoundManager2.class, "fall", Fall); break;
            case 5: set(SoundManager2.class, "door", Door); break;
            case 6: set(SoundManager2.class, "chest", Chest); break;
            case 7: set(SoundManager2.class, "bow", Bow); break;
            case 8: set(SoundManager2.class, "drr", Drr); break;
            case 9: set(SoundManager2.class, "eat", !Eat); break;
            case 10:set(SoundManager2.class, "drink", !Drink); break;
            case 11:set(SoundManager2.class, "breaking", !Break); break;
            case 12:set(SoundManager2.class, "lava", Lava); break;
            case 13:set(SoundManager2.class, "enderman", Enderman); break;
            case 14:set(SoundManager2.class, "calm4", Calm4); break;
            case 15:set(SoundManager2.class, "creeper", Creeper); break;
            //1.4
            case 16:set(SoundManager2.class, "steps", Steps); break;
            case 17:set(Entity.class, "oldstepsound", MobSteps); break;
            case 18:set(SoundManager2.class, "cow", Cow); break;
            case 19:set(SoundManager2.class, "slime", Slime); break;
            case 20:set(SoundManager2.class, "skeleton", Skeleton); break;
            case 21:set(SoundManager2.class, "levelup", Levelup); break;
            case 22:set(SoundManager2.class, "ignite", Ignite); break;
            case 23:set(SoundManager2.class, "shear", Shear); break;
            case 24:set(SoundManager2.class, "splash", Splash); break;
            case 25:set(SoundManager2.class, "swimming", Swimming); break;
            case 26:set(SoundManager2.class, "minecart", Minecart); break;
        }
    }

    public static boolean Explode = true;
    public static boolean XPOrb = true;
    public static int Ooh = 0;
    public static int Fall = 0;
    public static boolean Door = true;
    public static int Chest = 0;
    public static boolean Bow = true;
    public static boolean Drr = true;
    public static boolean Eat;
    public static boolean Drink = true;
    public static boolean Break;
    public static int Lava = 0;
    public static boolean Calm4;
    public static boolean Creeper;
    public static int Enderman = 1;
    public static boolean Steps = true;
    public static boolean MobSteps = true;
    public static boolean Cow = true;
    public static boolean Slime = true;
    public static boolean Skeleton = true;
    public static boolean Levelup = true;
    public static boolean Ignite = true;
    public static boolean Shear = true;
    public static boolean Splash = true;
    public static boolean Swimming = true;
    public static boolean Minecart = true;
}