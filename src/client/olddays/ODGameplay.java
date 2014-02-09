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
        new OldDaysPropertyBool(this, 19,true,  true,  "AllowSneak");
        new OldDaysPropertyBool(this, 20,true,  false, "OldReachDistance");
        new GuiGameOverOldDaysOverlay();
    }

    @Override
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
            case 5: set(EntityLivingBase.class, "oldloot", OldDrops); break;
            case 6: set(EntityLivingBase.class, "rareloot", RareLoot); break;
            case 7: set(EntityArrow.class, "olddamage", InstantBow);
                    set(ItemBow.class, "nocharging", InstantBow); break;
            case 8: set(ItemBow.class, "nodurability", !FiniteBow); break;
            case 9: set(EntityPlayer.class, "combat", CombatSystem);
                    set(EntityZombie.class, "defense", CombatSystem>=3);
                    setSwordDamage(CombatSystem<3); break;
            case 10:set(EntityPlayer.class, "armor", Armor);
                    set(EntityLivingBase.class, "armorblocksall", Armor<=0);
                    setArmorDamage(Armor<2); break;
            case 11:set(GuiIngame.class, "nodebug", !AllowDebug); break;
            case 12:set(EntityPlayer.class, "sprint", AllowSprint); break;
            case 13:set(EntityLivingBase.class, "jumpdelay", JumpDelay); break;
            case 14:set(EntityPlayer.class, "startitems", StartItems); break;
            case 15:Item.sign.maxStackSize = SignStacking ? 16 : 1; break;
            case 16:Item.bucketEmpty.maxStackSize = BucketStacking ? 16 : 1; break;
            case 17:set(EntityPlayer.class, "oldscore", Score < 2);
                    set(EntityLivingBase.class, "enablescore", Score == 1);
                    set(GuiIngame.class, "score", Score == 1);
                    set(GuiGameOverOldDaysOverlay.class, "oldScore", Score == 0); break;
            case 18:set(Explosion.class, "oldexplosion", OldExplosion); break;
            case 19:set(MovementInputFromOptionsCustom.class, "allowSneak", AllowSneak); break;
            case 20:set(net.minecraft.src.ssp.PlayerController.class, "oldreach", OldReachDistance); break;
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
    public static boolean AllowSneak = true;
    public static boolean OldReachDistance = true;

    @Override
    public void replaceBlocks(){
        try{
            Block.blocksList[Block.cake.blockID] = null;
            BlockCake2 customcake = (BlockCake2)(new BlockCake2(Block.cake.blockID));
            customcake.setHardness(0.5F);
            customcake.setStepSound(Block.soundClothFootstep);
            customcake.setUnlocalizedName("cake");
            customcake.disableStats();
            customcake.setTextureName("cake");
            Block.blocksList[Block.cake.blockID] = customcake;
            mod_OldDays.setField(Block.class, null, 111, customcake);//Block: cake
        }catch (Exception exception){
            System.out.println(exception);
        }
    }

    @Override
    public void onInitPlayer(EntityClientPlayerMP player, GameSettings settings){
        player.movementInput = new MovementInputFromOptionsCustom(player, settings);
    }

    private void setSwordDamage(boolean b){
        mod_OldDays.setField(ItemSword.class, Item.swordDiamond, 0, b ? 10 : 7);
        mod_OldDays.setField(ItemSword.class, Item.swordIron, 0, b ? 8 : 6);
        mod_OldDays.setField(ItemSword.class, Item.swordStone, 0, b ? 6 : 5);
        mod_OldDays.setField(ItemSword.class, Item.swordWood, 0, 4);
        mod_OldDays.setField(ItemSword.class, Item.swordGold, 0, 4);
    }

    private void setArmorDamage(boolean b){
        int damageReduceAmount = 5;
        mod_OldDays.setField(ItemArmor.class, Item.helmetLeather, damageReduceAmount, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.helmetChain, damageReduceAmount, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.helmetIron, damageReduceAmount, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.helmetGold, damageReduceAmount, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.helmetDiamond, damageReduceAmount, 3);
        mod_OldDays.setField(ItemArmor.class, Item.plateLeather, damageReduceAmount, b ? 8 : 3);
        mod_OldDays.setField(ItemArmor.class, Item.plateChain, damageReduceAmount, b ? 8 : 5);
        mod_OldDays.setField(ItemArmor.class, Item.plateIron, damageReduceAmount, b ? 8 : 6);
        mod_OldDays.setField(ItemArmor.class, Item.plateGold, damageReduceAmount, b ? 8 : 5);
        mod_OldDays.setField(ItemArmor.class, Item.plateDiamond, damageReduceAmount, 8);
        mod_OldDays.setField(ItemArmor.class, Item.legsLeather, damageReduceAmount, b ? 6 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.legsChain, damageReduceAmount, b ? 6 : 4);
        mod_OldDays.setField(ItemArmor.class, Item.legsIron, damageReduceAmount, b ? 6 : 5);
        mod_OldDays.setField(ItemArmor.class, Item.legsGold, damageReduceAmount, b ? 6 : 3);
        mod_OldDays.setField(ItemArmor.class, Item.legsDiamond, damageReduceAmount, 6);
        mod_OldDays.setField(ItemArmor.class, Item.bootsLeather, damageReduceAmount, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.bootsChain, damageReduceAmount, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.bootsIron, damageReduceAmount, b ? 3 : 2);
        mod_OldDays.setField(ItemArmor.class, Item.bootsGold, damageReduceAmount, b ? 3 : 1);
        mod_OldDays.setField(ItemArmor.class, Item.bootsDiamond, damageReduceAmount, 3);
        int maxDamage = 176;
        mod_OldDays.setField(Item.class, Item.helmetLeather, maxDamage, b ? 33 << 0 : 55);
        mod_OldDays.setField(Item.class, Item.helmetChain, maxDamage, b ? 33 << 1 : 165);
        mod_OldDays.setField(Item.class, Item.helmetIron, maxDamage, b ? 33 << 2 : 165);
        mod_OldDays.setField(Item.class, Item.helmetGold, maxDamage, b ? 33 << 4 : 77);
        mod_OldDays.setField(Item.class, Item.helmetDiamond, maxDamage, b ? 33 << 3 : 363);
        mod_OldDays.setField(Item.class, Item.plateLeather, maxDamage, b ? 48 << 0 : 80);
        mod_OldDays.setField(Item.class, Item.plateChain, maxDamage, b ? 48 << 1 : 240);
        mod_OldDays.setField(Item.class, Item.plateIron, maxDamage, b ? 48 << 2 : 240);
        mod_OldDays.setField(Item.class, Item.plateGold, maxDamage, b ? 48 << 4 : 112);
        mod_OldDays.setField(Item.class, Item.plateDiamond, maxDamage, b ? 48 << 3 : 528);
        mod_OldDays.setField(Item.class, Item.legsLeather, maxDamage, b ? 45 << 0 : 75);
        mod_OldDays.setField(Item.class, Item.legsChain, maxDamage, b ? 45 << 1 : 225);
        mod_OldDays.setField(Item.class, Item.legsIron, maxDamage, b ? 45 << 2 : 225);
        mod_OldDays.setField(Item.class, Item.legsGold, maxDamage, b ? 45 << 4 : 105);
        mod_OldDays.setField(Item.class, Item.legsDiamond, maxDamage, b ? 45 << 3 : 495);
        mod_OldDays.setField(Item.class, Item.bootsLeather, maxDamage, b ? 39 << 0 : 65);
        mod_OldDays.setField(Item.class, Item.bootsChain, maxDamage, b ? 39 << 1 : 195);
        mod_OldDays.setField(Item.class, Item.bootsIron, maxDamage, b ? 39 << 2 : 195);
        mod_OldDays.setField(Item.class, Item.bootsGold, maxDamage, b ? 39 << 4 : 91);
        mod_OldDays.setField(Item.class, Item.bootsDiamond, maxDamage, b ? 39 << 3 : 429);
    }

    @Override
    public boolean onTick(){
        if (AllowDebug){
            return true;
        }
        minecraft.gameSettings.showDebugInfo = false;
        return true;
    }
}