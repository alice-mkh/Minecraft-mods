package net.minecraft.src;

public class mod_OldDaysBugs extends mod_OldDays{
    public void load(){
        registerModule(1);
        addProperty(this, 1, "Boosters",      true,  "Boosters");
        addProperty(this, 2, "Water lifts",   true,  "WaterLifts");
        addProperty(this, 3, "Toasters",      true,  "LavaToasters");
        addProperty(this, 4, "Water ladders", true,  "WaterLadders");
        addProperty(this, 5, "Ladder gaps",   true,  "LadderGaps");
        addProperty(this, 6, "Piston dupe",   false, "PistonDupe");
        loadModuleProperties();
    }

    public void callback (int i){
        switch(i){
            case 1: EntityMinecart.boosters = WaterLifts;   break;
            case 2: EntityBoat.waterlift =    WaterLifts;   break;
            case 3: Entity.toaster =          LavaToasters; break;
            case 4: Entity.waterladder =      WaterLadders; break;
            case 5: EntityLiving.laddergaps = LadderGaps;   break;
            case 6: BlockPistonBase.dupe =    PistonDupe;   break;
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