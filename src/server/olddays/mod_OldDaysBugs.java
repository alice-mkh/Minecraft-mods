package net.minecraft.src;

public class mod_OldDaysBugs extends mod_OldDays{
    public void load(){
        registerModule(this, 1);
        addProperty(1, "Boosters",       false, true,  "Boosters",      "");
        addProperty(2, "Water lifts",    false, true,  "WaterLifts",    "");
        addProperty(3, "Toasters",       false, true,  "LavaToasters",  "");
        addProperty(4, "Water ladders",  false, true,  "WaterLadders",  "");
        addProperty(5, "Ladder gaps",    false, true,  "LadderGaps",    "");
        addProperty(6, "Piston dupe",    false, false, "PistonDupe",    "");
        addProperty(7, "Crafting slots", false, false, "CraftingSlots", "");
        loadModuleProperties();
    }

    public void callback (int i){
        switch(i){
            case 1: setBool(net.minecraft.src.EntityMinecart.class, "boosters", Boosters); break;
            case 2: setBool(net.minecraft.src.EntityBoat.class, "waterlift", WaterLifts); break;
            case 3: setBool(net.minecraft.src.Entity.class, "toaster", LavaToasters); break;
            case 4: setBool(net.minecraft.src.Entity.class, "waterladder", WaterLadders); break;
            case 5: setBool(net.minecraft.src.EntityLiving.class, "laddergaps", LadderGaps); break;
            case 6: setBool(net.minecraft.src.BlockPistonBase.class, "dupe", PistonDupe); break;
            case 7: setBool(net.minecraft.src.ContainerPlayer.class, "dropCrafting", !CraftingSlots); break;
        }
    }

    public static boolean Boosters = true;
    public static boolean LadderGaps = true;
    public static boolean WaterLadders = true;
    public static boolean LavaToasters = true;
    public static boolean WaterLifts = true;
    public static boolean PistonDupe;
    public static boolean CraftingSlots;
}