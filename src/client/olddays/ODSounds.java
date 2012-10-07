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
        new OldDaysPropertyBool(this, 12,false, "Lava");
        new OldDaysPropertyInt(this,  13,1,     "Enderman", 2).setUseNames();
        new OldDaysPropertyBool(this, 14,true,  "Calm4");
        new OldDaysPropertyBool(this, 15,false, "Creeper");
        Minecraft.getMinecraft().setSoundClass(net.minecraft.src.SoundManager2.class);
        addSound(5, "door_open");
        addSound(5, "door_close");
        addSound(6, "door_open");
        addSound(6, "door_close");
        addSound(7, "bow");
        addMusic(14,"calm4");
        addSound(15,"creeper1");
        addSound(15,"creeper2");
        addSound(15,"creeper3");
        addSound(15,"creeper4");
    }

    public void callback (int i){
        switch(i){
            case 1: set(net.minecraft.src.SoundManager2.class, "explode", Explode); break;
            case 2: set(net.minecraft.src.SoundManager2.class, "xporb", XPOrb); break;
            case 3: set(net.minecraft.src.SoundManager2.class, "hurt", Ooh); break;
            case 4: set(net.minecraft.src.SoundManager2.class, "nofall", !Fall); break;
            case 5: set(net.minecraft.src.SoundManager2.class, "door", Door); break;
            case 6: set(net.minecraft.src.SoundManager2.class, "chest", Chest); break;
            case 7: set(net.minecraft.src.SoundManager2.class, "bow", Bow); break;
            case 8: set(net.minecraft.src.SoundManager2.class, "drr", Drr); break;
            case 9: set(net.minecraft.src.SoundManager2.class, "eat", !Eat); break;
            case 10:set(net.minecraft.src.SoundManager2.class, "drink", !Drink); break;
            case 11:set(net.minecraft.src.SoundManager2.class, "breaking", !Break); break;
            case 12:set(net.minecraft.src.SoundManager2.class, "lava", !Lava); break;
            case 13:set(net.minecraft.src.SoundManager2.class, "enderman", Enderman); break;
            case 14:set(net.minecraft.src.SoundManager2.class, "calm4", Calm4); break;
            case 15:set(net.minecraft.src.SoundManager2.class, "creeper", Creeper); break;
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
    public static boolean Lava;
    public static boolean Calm4;
    public static boolean Creeper;
    public static int Enderman = 1;
}