package net.minecraft.src;

public class ODBugs extends OldDaysModule{
    public ODBugs(mod_OldDays c){
        super(c, 1, "Bugs");
        new OldDaysPropertyBool(this, 1,  false, true,  "Boosters");
        new OldDaysPropertyBool(this, 2,  false, true,  "WaterLifts");
        new OldDaysPropertyBool(this, 3,  false, true,  "LavaToasters");
        new OldDaysPropertyBool(this, 4,  false, true,  "WaterLadders");
        new OldDaysPropertyBool(this, 5,  false, true,  "LadderGaps");
        new OldDaysPropertyBool(this, 6,  false, false, "PistonDupe");
        new OldDaysPropertyBool(this, 7,  false, false, "CraftingSlots");
        new OldDaysPropertyBool(this, 8,  false, false, "InvWalking");
    }

    public void callback (int i){
        switch(i){
            case 1: set(net.minecraft.src.EntityMinecart.class, "boosters", Boosters); break;
            case 2: set(net.minecraft.src.EntityBoat.class, "waterlift", WaterLifts); break;
            case 3: set(net.minecraft.src.Entity.class, "toaster", LavaToasters); break;
            case 4: set(net.minecraft.src.Entity.class, "waterladder", WaterLadders); break;
            case 5: set(net.minecraft.src.EntityLiving.class, "laddergaps", LadderGaps); break;
            case 6: set(net.minecraft.src.BlockPistonBase.class, "dupe", PistonDupe); break;
            case 7: set(net.minecraft.src.ContainerPlayer.class, "dropCrafting", !CraftingSlots); break;
            case 8: set(net.minecraft.src.GuiInventory.class, "walking", InvWalking); break;
        }
    }

    public static boolean Boosters = true;
    public static boolean LadderGaps = true;
    public static boolean WaterLadders = true;
    public static boolean LavaToasters = true;
    public static boolean WaterLifts = true;
    public static boolean PistonDupe;
    public static boolean CraftingSlots;
    public static boolean InvWalking;
}