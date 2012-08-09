package net.minecraft.src;

public class ODGameplay extends OldDaysModule{
    public ODGameplay(mod_OldDays c){
        super(c, 2, "Gameplay");
        new OldDaysPropertyBool(this, 1, true,  false, "EnableXP");
        new OldDaysPropertyBool(this, 2, true,  false, "EnableHunger");
        new OldDaysPropertyBool(this, 3, false, true,  "InstantFood");
        new OldDaysPropertyBool(this, 4, true,  false, "FoodStacking");
        new OldDaysPropertyBool(this, 5, false, true,  "OldDrops");
        new OldDaysPropertyBool(this, 6, true,  false, "RareLoot");
        new OldDaysPropertyBool(this, 7, false, true,  "InstantBow");
        new OldDaysPropertyBool(this, 8, true,  false, "FiniteBow");
        new OldDaysPropertyInt(this,  9, 2,     1,     "CombatSystem", 3).setUseNames();
        new OldDaysPropertyInt(this,  10,3,     0,     "Armor", 3).setUseNames();
        new OldDaysPropertyBool(this, 11,true,  true,  "AllowDebug");
        new OldDaysPropertyBool(this, 12,true,  true,  "AllowSprint");
        new OldDaysPropertyBool(this, 13,true,  false, "JumpDelay");
        new OldDaysPropertyInt(this,  14,0,     0,     "StartItems", 3).setUseNames();
        new OldDaysPropertyBool(this, 15,false, false, "SignStacking");
        new OldDaysPropertyBool(this, 16,false, false, "BucketStacking");
    }

    public void callback (int i){
        switch (i){
            case 1: set(net.minecraft.src.EntityXPOrb.class, "noxp", !EnableXP);
                    set(net.minecraft.src.GuiIngame.class, "hidexp", !EnableXP); break;
            case 2: set(net.minecraft.src.FoodStats.class, "disabled", !EnableHunger);
                    set(net.minecraft.src.ItemFood.class, "heal", !EnableHunger);
                    set(net.minecraft.src.BlockCake.class, "heal", !EnableHunger);
                    set(net.minecraft.src.GuiIngame.class, "hidehunger", !EnableHunger); break;
            case 3: set(net.minecraft.src.ItemFood.class, "instant", InstantFood); break;
            case 4: set(net.minecraft.src.ItemFood.class, "stacks", FoodStacking); break;
            case 5: set(net.minecraft.src.EntityLiving.class, "oldloot", OldDrops); break;
            case 6: set(net.minecraft.src.EntityLiving.class, "rareloot", RareLoot); break;
            case 7: set(net.minecraft.src.EntityArrow.class, "olddamage", InstantBow);
                    set(net.minecraft.src.ItemBow.class, "nocharging", InstantBow); break;
            case 8: set(net.minecraft.src.ItemBow.class, "nodurability", !FiniteBow); break;
            case 9: set(net.minecraft.src.EntityPlayer.class, "combat", CombatSystem);
                    set(net.minecraft.src.EntityZombie.class, "defense", CombatSystem>=3);
                    setSwordDamage(CombatSystem<2); break;
            case 10:set(net.minecraft.src.EntityPlayer.class, "armor", Armor);
                    set(net.minecraft.src.EntityLiving.class, "armorblocksall", Armor<=0);
                    setArmorDamage(Armor<2); break;
            case 11:set(net.minecraft.src.GuiIngame.class, "nodebug", !AllowDebug); break;
            case 12:set(net.minecraft.src.EntityPlayer.class, "sprint", AllowSprint);
                    set(net.minecraft.src.FoodStats.class, "disabledLevel", AllowSprint ? 20 : 5); break;
            case 13:set(net.minecraft.src.EntityLiving.class, "jumpdelay", JumpDelay); break;
            case 14:set(net.minecraft.src.EntityPlayer.class, "startitems", StartItems); break;
            case 15:Item.sign.maxStackSize = SignStacking ? 16 : 1; break;
            case 16:Item.bucketEmpty.maxStackSize = BucketStacking ? 16 : 1; break;
        }
    }

    protected void onFallbackChange(boolean fallback){
        set(net.minecraft.src.GuiIngame.class, "fallbacktex", fallback);
    }

    public static boolean EnableXP;
    public static boolean EnableHunger;
    public static boolean InstantFood = true;
    public static boolean FoodStacking;
    public static boolean OldDrops = true;
    public static boolean RareLoot;
    public static boolean InstantBow = true;
    public static boolean FiniteBow;
    public static int CombatSystem = 0;
    public static int Armor = 0;
    public static boolean AllowDebug = true;
    public static boolean AllowSprint = true;
    public static boolean JumpDelay;
    public static int StartItems = 0;
    public static boolean SignStacking;
    public static boolean BucketStacking;

    private void setSwordDamage(boolean b){
        mod_OldDays.setField(net.minecraft.src.ItemSword.class, Item.swordDiamond, 0, b ? 10 : 7);
        mod_OldDays.setField(net.minecraft.src.ItemSword.class, Item.swordSteel, 0, b ? 8 : 6);
        mod_OldDays.setField(net.minecraft.src.ItemSword.class, Item.swordStone, 0, b ? 6 : 5);
        mod_OldDays.setField(net.minecraft.src.ItemSword.class, Item.swordWood, 0, 4);
        mod_OldDays.setField(net.minecraft.src.ItemSword.class, Item.swordGold, 0, 4);
    }

    private void setArmorDamage(boolean b){
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.helmetLeather, 2, b ? 3 : 1);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.helmetChain, 2, b ? 3 : 2);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.helmetSteel, 2, b ? 3 : 2);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.helmetGold, 2, b ? 3 : 2);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.plateLeather, 2, b ? 8 : 3);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.plateChain, 2, b ? 8 : 5);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.plateSteel, 2, b ? 8 : 6);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.plateGold, 2, b ? 8 : 5);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.legsLeather, 2, b ? 6 : 2);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.legsChain, 2, b ? 6 : 4);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.legsSteel, 2, b ? 6 : 5);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.legsGold, 2, b ? 6 : 3);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.bootsLeather, 2, b ? 3 : 1);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.bootsChain, 2, b ? 3 : 1);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.bootsSteel, 2, b ? 3 : 2);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.bootsGold, 2, b ? 3 : 1);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.helmetLeather, 149, b ? 33 << 0 : 55);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.helmetChain, 149, b ? 33 << 1 : 165);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.helmetSteel, 149, b ? 33 << 2 : 165);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.helmetGold, 149, b ? 33 << 4 : 77);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.helmetDiamond, 149, b ? 33 << 3 : 363);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.plateLeather, 149, b ? 48 << 0 : 80);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.plateChain, 149, b ? 48 << 1 : 240);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.plateSteel, 149, b ? 48 << 2 : 240);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.plateGold, 149, b ? 48 << 4 : 112);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.plateDiamond, 149, b ? 48 << 3 : 528);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.legsLeather, 149, b ? 45 << 0 : 75);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.legsChain, 149, b ? 45 << 1 : 225);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.legsSteel, 149, b ? 45 << 2 : 225);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.legsGold, 149, b ? 45 << 4 : 105);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.legsDiamond, 149, b ? 45 << 3 : 495);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.bootsLeather, 149, b ? 39 << 0 : 65);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.bootsChain, 149, b ? 39 << 1 : 195);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.bootsSteel, 149, b ? 39 << 2 : 195);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.bootsGold, 149, b ? 39 << 4 : 91);
        mod_OldDays.setField(net.minecraft.src.Item.class, Item.bootsDiamond, 149, b ? 39 << 3 : 429);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.helmetDiamond, 2, 3);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.plateDiamond, 2, 8);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.legsDiamond, 2, 6);
        mod_OldDays.setField(net.minecraft.src.ItemArmor.class, Item.bootsDiamond, 2, 3);
    }

    public boolean onTick(){
        if (AllowDebug){
            return true;
        }
        minecraft.gameSettings.showDebugInfo = false;
        return true;
    }
}