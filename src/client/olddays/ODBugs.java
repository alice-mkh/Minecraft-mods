package net.minecraft.src;

public class ODBugs extends OldDaysModule{
    public ODBugs(mod_OldDays c){
        super(c, 1, "Bugs");
        new OldDaysPropertyBool(this, 1,  true,  false, "Boosters");
        new OldDaysPropertyBool(this, 2,  true,  false, "WaterLifts");
        new OldDaysPropertyBool(this, 3,  true,  false, "LavaToasters");
        new OldDaysPropertyBool(this, 4,  true,  false, "WaterLadders");
        new OldDaysPropertyBool(this, 5,  true,  false, "LadderGaps");
        new OldDaysPropertyBool(this, 6,  false, false, "PistonDupe");
        new OldDaysPropertyBool(this, 7,  false, false, "CraftingSlots");
        new OldDaysPropertyBool(this, 8,  false, false, "InvWalking");
    }

    @Override
    public void callback (int i){
        switch(i){
            case 1: set(EntityMinecart.class, "boosters", Boosters); break;
            case 2: set(EntityBoat.class, "waterlift", WaterLifts); break;
            case 3: set(Entity.class, "toaster", LavaToasters); break;
            case 4: set(Entity.class, "waterladder", WaterLadders); break;
            case 5: set(EntityLivingBase.class, "laddergaps", LadderGaps); break;
            case 6: set(BlockPistonBase.class, "dupe", PistonDupe); break;
            case 7: set(ContainerPlayer.class, "dropCrafting", !CraftingSlots); break;
            case 8: set(GuiInventory.class, "walking", InvWalking); break;
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