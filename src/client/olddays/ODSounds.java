package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class ODSounds extends OldDaysModule{
    public ODSounds(mod_OldDays c){
        super(c, 5, "Sounds");
        new OldDaysPropertyBool(this, 1, true,  "Explode");
        new OldDaysPropertyBool(this, 2, true,  "XPOrb");
        new OldDaysPropertyBool(this, 3, true,  "Ooh");
        new OldDaysPropertyBool(this, 4, false, "Fall");
        new OldDaysPropertyBool(this, 5, true,  "Door");
        new OldDaysPropertyInt(this,  6, 0,     "Chest", 2).setUseNames();
        new OldDaysPropertyBool(this, 7, true,  "Bow");
        new OldDaysPropertyBool(this, 8, true,  "Drr");
        new OldDaysPropertyBool(this, 9, false, "Eat");
        new OldDaysPropertyBool(this, 10,true,  "Drink");
        new OldDaysPropertyBool(this, 11,false, "Break");
        new OldDaysPropertyInt(this,  12,0,     "Lava", 2).setUseNames();
        new OldDaysPropertyInt(this,  13,1,     "Enderman", 2).setUseNames();
        new OldDaysPropertyBool(this, 14,true,  "Calm4");
        new OldDaysPropertyBool(this, 15,false, "Creeper");
        new OldDaysPropertyBool(this, 16,true,  "Steps");
        new OldDaysPropertyBool(this, 17,true,  "MobSteps");
        new OldDaysPropertyBool(this, 18,true,  "Cow");
        new OldDaysPropertyBool(this, 19,true,  "Slime");
        new OldDaysPropertyBool(this, 20,true,  "Skeleton");
        new OldDaysPropertyBool(this, 21,false, "Levelup");
        new OldDaysPropertyBool(this, 22,true,  "Ignite");
        new OldDaysPropertyBool(this, 23,true,  "Shear");
        new OldDaysPropertyBool(this, 24,true,  "Splash");
        new OldDaysPropertyBool(this, 25,false, "Swimming");
        new OldDaysPropertyBool(this, 26,false, "Minecart");
        Minecraft.getMinecraft().setSoundClass(SoundManager2.class);
        addSound(1, "explode");
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
    }

    @Override
    public void callback (int i){
        switch(i){
            case 1: set(SoundManager2.class, "explode", Explode); break;
            case 2: set(SoundManager2.class, "xporb", XPOrb); break;
            case 3: set(SoundManager2.class, "hurt", Ooh); break;
            case 4: set(SoundManager2.class, "nofall", !Fall); break;
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
    public static boolean Ooh = true;
    public static boolean Fall;
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