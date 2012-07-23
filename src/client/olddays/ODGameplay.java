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
        new OldDaysPropertyInt(this,  9, 2,     1,     "CombatSystem", 4).setUseNames();
        new OldDaysPropertyInt(this,  10,3,     0,     "Armor", 4).setUseNames();
        new OldDaysPropertyBool(this, 11,true,  true,  "AllowDebug");
        new OldDaysPropertyBool(this, 12,true,  true,  "AllowSprint");
        new OldDaysPropertyBool(this, 13,true,  false, "JumpDelay");
        new OldDaysPropertyInt(this,  14,0,     0,     "StartItems", 4).setUseNames();
        new OldDaysPropertyBool(this, 15,false, false, "SignStacking");
    }

    public void callback (int i){
        switch (i){
            case 1: setBool(net.minecraft.src.EntityXPOrb.class, "noxp", !EnableXP);
                    setBool(net.minecraft.src.GuiIngame.class, "hidexp", !EnableXP); break;
            case 2: setBool(net.minecraft.src.FoodStats.class, "disabled", !EnableHunger);
                    setBool(net.minecraft.src.ItemFood.class, "heal", !EnableHunger);
                    setBool(net.minecraft.src.BlockCake.class, "heal", !EnableHunger);
                    setBool(net.minecraft.src.GuiIngame.class, "hidehunger", !EnableHunger); break;
            case 3: setBool(net.minecraft.src.ItemFood.class, "instant", InstantFood); break;
            case 4: setBool(net.minecraft.src.ItemFood.class, "stacks", FoodStacking); break;
            case 5: setBool(net.minecraft.src.EntityLiving.class, "oldloot", OldDrops); break;
            case 6: setBool(net.minecraft.src.EntityLiving.class, "rareloot", RareLoot); break;
            case 7: setBool(net.minecraft.src.EntityArrow.class, "olddamage", InstantBow);
                    setBool(net.minecraft.src.ItemBow.class, "nocharging", InstantBow); break;
            case 8: setBool(net.minecraft.src.ItemBow.class, "nodurability", !FiniteBow); break;
            case 9: setInt(net.minecraft.src.EntityPlayer.class, "combat", CombatSystem);
                    setBool(net.minecraft.src.EntityZombie.class, "defense", CombatSystem>=3);
                    setSwordDamage(CombatSystem<2); break;
            case 10:setInt(net.minecraft.src.EntityPlayer.class, "armor", Armor);
                    setBool(net.minecraft.src.EntityLiving.class, "armorblocksall", Armor<=0);
                    setArmorDamage(Armor<2); break;
            case 11:setBool(net.minecraft.src.GuiIngame.class, "nodebug", !AllowDebug); break;
            case 12:setBool(net.minecraft.src.EntityPlayer.class, "sprint", AllowSprint);
                    setInt(net.minecraft.src.FoodStats.class, "disabledLevel", AllowSprint ? 20 : 5); break;
            case 13:setBool(net.minecraft.src.EntityLiving.class, "jumpdelay", JumpDelay); break;
            case 14:setInt(net.minecraft.src.EntityPlayer.class, "startitems", StartItems); break;
            case 15:Item.sign.maxStackSize = SignStacking ? 16 : 1; break;
        }
    }

    protected void onFallbackChange(boolean fallback){
        setBool(net.minecraft.src.GuiIngame.class, "fallbacktex", fallback);
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

    private void setSwordDamage(boolean b){
        try{
            if (b){
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordDiamond, 0, 10);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordSteel, 0, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordStone, 0, 6);
            }else{
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordDiamond, 0, 7);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordSteel, 0, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordStone, 0, 5);
            }
            ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordWood, 0, 4);
            ModLoader.setPrivateValue(net.minecraft.src.ItemSword.class, Item.swordGold, 0, 4);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    private void setArmorDamage(boolean b){
        try{
            if (b){
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetLeather, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetChain, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetSteel, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetGold, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateLeather, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateChain, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateSteel, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateGold, 2, 8);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsLeather, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsChain, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsSteel, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsGold, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsLeather, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsChain, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsSteel, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsGold, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetLeather, 145, 33 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetChain, 145, 33 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetSteel, 145, 33 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetGold, 145, 33 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetDiamond, 145, 33 << 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateLeather, 145, 48 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateChain, 145, 48 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateSteel, 145, 48 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateGold, 145, 48 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateDiamond, 145, 48 << 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsLeather, 145, 45 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsChain, 145, 45 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsSteel, 145, 45 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsGold, 145, 45 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsDiamond, 145, 45 << 3);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsLeather, 145, 39 << 0);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsChain, 145, 39 << 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsSteel, 145, 39 << 2);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsGold, 145, 39 << 4);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsDiamond, 145, 39 << 3);
            }else{
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetLeather, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetChain, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetSteel, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetGold, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateLeather, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateChain, 2, 5);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateSteel, 2, 6);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateGold, 2, 5);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsLeather, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsChain, 2, 4);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsSteel, 2, 5);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsGold, 2, 3);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsLeather, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsChain, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsSteel, 2, 2);
                ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsGold, 2, 1);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetLeather, 145, 55);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetChain, 145, 165);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetSteel, 145, 165);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetGold, 145, 77);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.helmetDiamond, 145, 363);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateLeather, 145, 80);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateChain, 145, 240);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateSteel, 145, 240);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateGold, 145, 112);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.plateDiamond, 145, 528);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsLeather, 145, 75);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsChain, 145, 225);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsSteel, 145, 225);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsGold, 145, 105);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.legsDiamond, 145, 495);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsLeather, 145, 65);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsChain, 145, 195);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsSteel, 145, 195);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsGold, 145, 91);
                ModLoader.setPrivateValue(net.minecraft.src.Item.class, Item.bootsDiamond, 145, 429);
            }
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.helmetDiamond, 2, 3);
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.plateDiamond, 2, 8);
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.legsDiamond, 2, 6);
            ModLoader.setPrivateValue(net.minecraft.src.ItemArmor.class, Item.bootsDiamond, 2, 3);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public boolean onTick(){
        if (AllowDebug){
            return true;
        }
        minecraft.gameSettings.showDebugInfo = false;
        return true;
    }
}