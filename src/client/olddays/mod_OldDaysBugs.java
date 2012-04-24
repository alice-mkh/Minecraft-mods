package net.minecraft.src;

public class mod_OldDaysBugs extends mod_OldDays{
    public void load(){
        registerModule(this, 1);
        addProperty(1, "Boosters",      0, 1, "Boosters",     "");
        addProperty(2, "Water lifts",   0, 1, "WaterLifts",   "");
        addProperty(3, "Toasters",      0, 1, "LavaToasters", "");
        addProperty(4, "Water ladders", 0, 1, "WaterLadders", "");
        addProperty(5, "Ladder gaps",   0, 1, "LadderGaps",   "");
        addProperty(6, "Piston dupe",   0, 0, "PistonDupe",   "");
        loadModuleProperties();
    }

    public void callback (int i){
        switch(i){
            case 1: setBool(net.minecraft.src.EntityMinecart.class, "boosters", WaterLifts); break;
            case 2: setBool(net.minecraft.src.EntityBoat.class, "waterlift", WaterLifts); break;
            case 3: setBool(net.minecraft.src.Entity.class, "toaster", LavaToasters); break;
            case 4: setBool(net.minecraft.src.Entity.class, "waterladder", WaterLadders); break;
            case 5: setBool(net.minecraft.src.EntityLiving.class, "laddergaps", LadderGaps); break;
            case 6: setBool(net.minecraft.src.BlockPistonBase.class, "dupe", PistonDupe); break;
        }
    }

    public static boolean Boosters = true;
    public static boolean LadderGaps = true;
    public static boolean WaterLadders = true;
    public static boolean LavaToasters = true;
    public static boolean WaterLifts = true;
    public static boolean PistonDupe;
//Sand generator
//Data value change bug
}