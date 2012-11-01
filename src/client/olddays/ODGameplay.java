package net.minecraft.src;

public class ODGameplay extends OldDaysModule{
    public ODGameplay(mod_OldDays c){
        super(c, 2, "Gameplay");
        new OldDaysPropertyBool(this, 1, false, true,  "EnableXP");
        new OldDaysPropertyBool(this, 2, false, true,  "EnableHunger").setRefreshOnFallback();
        new OldDaysPropertyBool(this, 3, true,  false, "InstantFood");
        new OldDaysPropertyBool(this, 4, false, true,  "FoodStacking");
        new OldDaysPropertyBool(this, 5, true,  false, "OldDrops");
        new OldDaysPropertyBool(this, 6, false, true,  "RareLoot");
        new OldDaysPropertyBool(this, 7, true,  false, "InstantBow");
        new OldDaysPropertyBool(this, 8, false, true,  "FiniteBow");
        new OldDaysPropertyInt(this,  9, 1,     2,     "CombatSystem", 3).setUseNames();
        new OldDaysPropertyInt(this,  10,0,     3,     "Armor", 3).setUseNames();
        new OldDaysPropertyBool(this, 11,true,  true,  "AllowDebug");
        new OldDaysPropertyBool(this, 12,true,  true,  "AllowSprint");
        new OldDaysPropertyBool(this, 13,false, true,  "JumpDelay");
        new OldDaysPropertyInt(this,  14,0,     0,     "StartItems", 3).setUseNames();
        new OldDaysPropertyBool(this, 15,false, true,  "SignStacking");
        new OldDaysPropertyBool(this, 16,false, true,  "BucketStacking");
        new OldDaysPropertyInt(this,  17,2,     2,     "Score", 2).setUseNames();
        new OldDaysPropertyBool(this, 18,true,  false, "OldExplosion");
        replaceBlocks();
    }

    public void callback (int i){
        switch (i){
            case 1: set(EntityXPOrb.class, "noxp", !EnableXP);
                    set(GuiIngame.class, "hidexp", !EnableXP); break;
            case 2: set(FoodStats.class, "disabled", !EnableHunger);
                    set(ItemFood.class, "heal", !EnableHunger);
                    set(BlockCake2.class, "heal", !EnableHunger);
                    set(GuiIngame.class, "hidehunger", !EnableHunger);
                    set(GuiIngame.class, "fallbacktex", !hasTextures("olddays/icons.png")); break;
            case 3: set(ItemFood.class, "instant", InstantFood); break;
            case 4: set(ItemFood.class, "stacks", FoodStacking); break;
            case 5: set(EntityLiving.class, "oldloot", OldDrops); break;
            case 6: set(EntityLiving.class, "rareloot", RareLoot); break;
            case 7: set(EntityArrow.class, "olddamage", InstantBow);
                    set(ItemBow.class, "nocharging", InstantBow); break;
            case 8: set(ItemBow.class, "nodurability", !FiniteBow); break;
            case 9: set(EntityPlayer.class, "combat", CombatSystem);
                    set(EntityZombie.class, "defense", CombatSystem>=3);
                    setSwordDamage(CombatSystem<3); break;
            case 10:set(EntityPlayer.class, "armor", Armor);
                    set(EntityLiving.class, "armorblocksall", Armor<=0);
                    setArmorDamage(Armor<2); break;
            case 11:set(GuiIngame.class, "nodebug", !AllowDebug); break;
            case 12:set(EntityPlayer.class, "sprint", AllowSprint); break;
            case 13:set(EntityLiving.class, "jumpdelay", JumpDelay); break;
            case 14:set(EntityPlayer.class, "startitems", StartItems); break;
            case 15:Item.sign.maxStackSize = SignStacking ? 16 : 1; break;
            case 16:Item.bucketEmpty.maxStackSize = BucketStacking ? 16 : 1; break;
            case 17:set(EntityPlayer.class, "oldscore", Score < 2);
                    set(EntityLiving.class, "score", Score == 1);
                    set(GuiIngame.class, "score", Score == 1);
                    set(GuiGameOver.class, "oldScore", Score == 0); break;
            case 18:set(Explosion.class, "oldexplosion", OldExplosion); break;
        }
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
    public static int Score = 2;
    public static boolean OldExplosion = true;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.cake.blockID] = null;
            BlockCake2 customcake = (BlockCake2)(new BlockCake2(92, 121));
            customcake.setHardness(0.5F);
            customcake.setStepSound(Block.soundClothFootstep);
            customcake.setBlockName("cake");
            customcake.disableStats();
            customcake.setRequiresSelfNotify();
            Block.blocksList[Block.cake.blockID] = customcake;
            mod_OldDays.setField(Block.class, null, 111, customcake);//Block: cake
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    private void setSwordDamage(boolean b){
        mod_OldDays.setField(ItemSword.class, Item.swordDiamond, 0, b ? 10 : 7);
        mod_OldDays.setField(ItemSword.class, Item.swordSteel, 0, b ? 8 : 6);
        mod_OldDays.setField(ItemSword.class, Item.swordStone, 0, b ? 6 : 5);
        mod_OldDays.setField(ItemSword.class, Item.swordWood, 0, 4);
        mod_OldDays.setField(ItemSword.class, Item.swordGold, 0, 4);
    }

    private void setArmorDamage(boolean b){
        mod_OldDays.setField(ItemArmor.class, Item.helmetLeather, 2, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.helmetChain, 2, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.helmetSteel, 2, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.helmetGold, 2, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.plateLeather, 2, b ? 8 : 3);
        mod_OldDays.setField(ItemArmor.class, Item.plateChain, 2, b ? 8 : 5);
        mod_OldDays.setField(ItemArmor.class, Item.plateSteel, 2, b ? 8 : 6);
        mod_OldDays.setField(ItemArmor.class, Item.plateGold, 2, b ? 8 : 5);
        mod_OldDays.setField(ItemArmor.class, Item.legsLeather, 2, b ? 6 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.legsChain, 2, b ? 6 : 4);
        mod_OldDays.setField(ItemArmor.class, Item.legsSteel, 2, b ? 6 : 5);
        mod_OldDays.setField(ItemArmor.class, Item.legsGold, 2, b ? 6 : 3);
        mod_OldDays.setField(ItemArmor.class, Item.bootsLeather, 2, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.bootsChain, 2, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.bootsSteel, 2, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.bootsGold, 2, b ? 3 : 1);
        int maxDamage = 161;
        mod_OldDays.setField(Item.class, Item.helmetLeather, maxDamage, b ? 33 << 0 : 55);
        mod_OldDays.setField(Item.class, Item.helmetChain, maxDamage, b ? 33 << 1 : 165);
        mod_OldDays.setField(Item.class, Item.helmetSteel, maxDamage, b ? 33 << 2 : 165);
        mod_OldDays.setField(Item.class, Item.helmetGold, maxDamage, b ? 33 << 4 : 77);
        mod_OldDays.setField(Item.class, Item.helmetDiamond, maxDamage, b ? 33 << 3 : 363);
        mod_OldDays.setField(Item.class, Item.plateLeather, maxDamage, b ? 48 << 0 : 80);
        mod_OldDays.setField(Item.class, Item.plateChain, maxDamage, b ? 48 << 1 : 240);
        mod_OldDays.setField(Item.class, Item.plateSteel, maxDamage, b ? 48 << 2 : 240);
        mod_OldDays.setField(Item.class, Item.plateGold, maxDamage, b ? 48 << 4 : 112);
        mod_OldDays.setField(Item.class, Item.plateDiamond, maxDamage, b ? 48 << 3 : 528);
        mod_OldDays.setField(Item.class, Item.legsLeather, maxDamage, b ? 45 << 0 : 75);
        mod_OldDays.setField(Item.class, Item.legsChain, maxDamage, b ? 45 << 1 : 225);
        mod_OldDays.setField(Item.class, Item.legsSteel, maxDamage, b ? 45 << 2 : 225);
        mod_OldDays.setField(Item.class, Item.legsGold, maxDamage, b ? 45 << 4 : 105);
        mod_OldDays.setField(Item.class, Item.legsDiamond, maxDamage, b ? 45 << 3 : 495);
        mod_OldDays.setField(Item.class, Item.bootsLeather, maxDamage, b ? 39 << 0 : 65);
        mod_OldDays.setField(Item.class, Item.bootsChain, maxDamage, b ? 39 << 1 : 195);
        mod_OldDays.setField(Item.class, Item.bootsSteel, maxDamage, b ? 39 << 2 : 195);
        mod_OldDays.setField(Item.class, Item.bootsGold, maxDamage, b ? 39 << 4 : 91);
        mod_OldDays.setField(Item.class, Item.bootsDiamond, maxDamage, b ? 39 << 3 : 429);
        mod_OldDays.setField(ItemArmor.class, Item.helmetDiamond, 2, 3);
        mod_OldDays.setField(ItemArmor.class, Item.plateDiamond, 2, 8);
        mod_OldDays.setField(ItemArmor.class, Item.legsDiamond, 2, 6);
        mod_OldDays.setField(ItemArmor.class, Item.bootsDiamond, 2, 3);
    }

    public boolean onTick(){
        if (AllowDebug){
            return true;
        }
        minecraft.gameSettings.showDebugInfo = false;
        return true;
    }
}