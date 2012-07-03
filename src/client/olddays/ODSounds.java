package net.minecraft.src;

import java.io.File;

public class ODSounds extends OldDaysModule{
    public ODSounds(mod_OldDays c){
        super(c, 5, "Sounds");
        addProperty(1, "Old explosion sound", true,  "Explode",    "");
        addProperty(2, "Old XP orb sound",    true,  "XPOrb",      "");
        addProperty(3, "Old hurt sound",      true,  "Ooh",        "");
        addProperty(4, "Falling sound",       false, "Fall",       "");
        addProperty(5, "Old door sounds",     true,  "Door",       "");
        addProperty(6, "Chest sound",         2,     "Chest",      "", new String[]{"1.0", "1.8.1", "OFF"});
        addProperty(7, "Old bow sound",       true,  "Bow",        "");
        addProperty(8, "Old arrow hit sound", true,  "Drr",        "");
        addProperty(9, "Eating sound",        false, "Eat",        "");
        addProperty(10,"Drinking sound",      true,  "Drink",      "");
        addProperty(11,"Tool breaking sound", false, "Break",      "");
        addProperty(12,"Lava sound",          false, "Lava",       "");
        addProperty(13,"Enderman sounds",     1,     "Enderman",   "", new String[]{"1.0", "1.8.1", "OFF"});
        addProperty(14,"Use calm4.ogg music", true,  "Calm4",      "");
        addProperty(15,"Old creeper sound",   false, "Creeper",    "");
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
            case 1: setBool(net.minecraft.src.SoundManager.class, "explode", Explode); break;
            case 2: setBool(net.minecraft.src.SoundManager.class, "xporb", XPOrb); break;
            case 3: setBool(net.minecraft.src.SoundManager.class, "hurt", Ooh); break;
            case 4: setBool(net.minecraft.src.SoundManager.class, "nofall", !Fall); break;
            case 5: setBool(net.minecraft.src.SoundManager.class, "door", Door); break;
            case 6: setInt(net.minecraft.src.SoundManager.class, "chest", Chest); break;
            case 7: setBool(net.minecraft.src.SoundManager.class, "bow", Bow); break;
            case 8: setBool(net.minecraft.src.SoundManager.class, "drr", Drr); break;
            case 9: setBool(net.minecraft.src.SoundManager.class, "eat", !Eat); break;
            case 10:setBool(net.minecraft.src.SoundManager.class, "drink", !Drink); break;
            case 11:setBool(net.minecraft.src.SoundManager.class, "breaking", !Break); break;
            case 12:setBool(net.minecraft.src.SoundManager.class, "lava", !Lava); break;
            case 13:setInt(net.minecraft.src.SoundManager.class, "enderman", Enderman); break;
            case 14:setBool(net.minecraft.src.SoundManager.class, "calm4", Calm4); break;
            case 15:setBool(net.minecraft.src.SoundManager.class, "creeper", Creeper); break;
        }
    }

    public static boolean Explode = true;
    public static boolean XPOrb = true;
    public static boolean Ooh = true;
    public static boolean Fall;
    public static boolean Door = true;
    public static int Chest = 2;
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