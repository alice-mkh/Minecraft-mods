package net.minecraft.src;

import java.util.*;

public class ODCrafting extends OldDaysModule{
    public ODCrafting(mod_OldDays c){
        super(c, 6, "Crafting");
        new OldDaysPropertyBool(this, 1, false, false, "OldPlanks");
        new OldDaysPropertyInt(this,  2, 1,     1,     "ClothArmor", 2).setUseNames();
        new OldDaysPropertyInt(this,  3, 2,     3,     "OldSlabs", 3).setUseNames();
        new OldDaysPropertyInt(this,  4, 2,     3,     "Ladders", 1, 3);
        new OldDaysPropertyBool(this, 5, false, false, "Cloth");
        new OldDaysPropertyBool(this, 6, false, false, "Glowstone");
        new OldDaysPropertyInt(this,  7, 2,     2,     "Apple", 2).setUseNames();
        new OldDaysPropertyBool(this, 8, false, false, "Stew");
        new OldDaysPropertyBool(this, 9, false, false, "OreBlocks");
        new OldDaysPropertyBool(this, 10,false, false, "Books");
        new OldDaysPropertyBool(this, 11,false, false, "OldSigns");
        new OldDaysPropertyBool(this, 12,false, false, "OldMaps");
        new OldDaysPropertyBool(this, 13,false, false, "OldArrows");
        new OldDaysPropertyBool(this, 14,false, false, "OldBread");
        new OldDaysPropertyBool(this, 15,false, false, "OldButtons");
    }

    @Override
    public void callback (int i){
        switch(i){
            case 1: setPlanks(OldPlanks); break;
            case 2: setArmor(ClothArmor); break;
            case 3: setSlabs(OldSlabs); break;
            case 4: setLadders(Ladders); break;
            case 5: setCloth(Cloth); break;
            case 6: setGlowstone(Glowstone); break;
            case 7: setApple(Apple); break;
            case 8: setStew(Stew); break;
            case 9: setOreBlocks(OreBlocks); break;
            case 10:setBook(Books); break;
            case 11:setSign(OldSigns); break;
            case 12:setMap(OldMaps); break;
            case 13:setArrows(OldArrows); break;
            case 14:setBread(OldBread); break;
            case 15:setButtons(OldButtons); break;
        }
    }

    public static boolean OldPlanks;
    public static int ClothArmor = 1;
    public static int OldSlabs = 2;
    public static int Ladders = 2;
    public static boolean Cloth;
    public static boolean Glowstone;
    public static int Apple = 2;
    public static boolean Stew;
    public static boolean OreBlocks;
    public static boolean Books;
    public static boolean OldSigns;
    public static boolean OldMaps;
    public static boolean OldArrows;
    public static boolean OldBread;
    public static boolean OldButtons;

    private void setPlanks(boolean b){
        String str = "4xtile.wood@";
        Block log = Block.wood;
        Block planks = Block.planks;
        if (b){
            removeRecipe(str+"3");
            removeRecipe(str+"2");
            removeRecipe(str+"1");
            removeRecipe(str+"0");
            addRecipe(new ItemStack(planks, 4, 0), "#", '#', new ItemStack(log, 1, 0));
            addRecipe(new ItemStack(planks, 4, 0), "#", '#', new ItemStack(log, 1, 1));
            addRecipe(new ItemStack(planks, 4, 0), "#", '#', new ItemStack(log, 1, 2));
            addRecipe(new ItemStack(planks, 4, 0), "#", '#', new ItemStack(log, 1, 3));
        }else{
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            addRecipe(new ItemStack(planks, 4, 0), "#", '#', new ItemStack(log, 1, 0));
            addRecipe(new ItemStack(planks, 4, 1), "#", '#', new ItemStack(log, 1, 1));
            addRecipe(new ItemStack(planks, 4, 2), "#", '#', new ItemStack(log, 1, 2));
            addRecipe(new ItemStack(planks, 4, 3), "#", '#', new ItemStack(log, 1, 3));
        }
    }

    private void setArmor(int b){
        Block cloth = Block.cloth;
        Item leather = Item.leather;
        removeRecipe("1xitem.helmetCloth@0");
        removeRecipe("1xitem.chestplateCloth@0");
        removeRecipe("1xitem.leggingsCloth@0");
        removeRecipe("1xitem.bootsCloth@0");
        if (b>0){
            addRecipe(new ItemStack(Item.helmetLeather, 1), "###", "# #", '#', leather);
            addRecipe(new ItemStack(Item.plateLeather, 1), "# #", "###", "###", '#', leather);
            addRecipe(new ItemStack(Item.legsLeather, 1), "###", "# #", "# #", '#', leather);
            addRecipe(new ItemStack(Item.bootsLeather, 1), "# #", "# #", '#', leather);
        }
        if (b==0 || b>1){
            addRecipe(new ItemStack(Item.helmetLeather, 1), "###", "# #", '#', cloth);
            addRecipe(new ItemStack(Item.plateLeather, 1), "# #", "###", "###", '#', cloth);
            addRecipe(new ItemStack(Item.legsLeather, 1), "###", "# #", "# #", '#', cloth);
            addRecipe(new ItemStack(Item.bootsLeather, 1), "# #", "# #", '#', cloth);
        }
    }

    private void setSlabs(int b){
        String slab3 = "3xtile.stoneSlab@";
        String slab6 = "6xtile.stoneSlab@";
        String slabNew = "6xtile.woodSlab@";
        String plate = "1xtile.pressurePlate@0";
        Block slab = Block.stoneSingleSlab;
        Block platestone = Block.pressurePlateStone;
        Block plateplanks = Block.pressurePlatePlanks;
        for (int i = 0; i <= 5; i++){
            removeRecipe(slab3+i);
            removeRecipe(slab6+i);
        }
        for (int i = 0; i <= 3; i++){
            removeRecipe(slabNew+i);
        }
        removeRecipe(plate);
        removeRecipe(plate);
        if (b==0){
            addRecipe(new ItemStack(slab, 3, 0), "###", '#', Block.cobblestone);
            addRecipe(new ItemStack(platestone, 1), "###", '#', Block.stone);
            addRecipe(new ItemStack(plateplanks, 1), "###", '#', Block.planks);
        }
        if (b==1){
            addRecipe(new ItemStack(slab, 3, 0), "###", '#', Block.stone);
            addRecipe(new ItemStack(slab, 3, 2), "###", '#', Block.planks);
            addRecipe(new ItemStack(slab, 3, 3), "###", '#', Block.cobblestone);
            addRecipe(new ItemStack(slab, 3, 1), "###", '#', Block.sandStone);
            addRecipe(new ItemStack(slab, 3, 4), "###", '#', Block.brick);
            addRecipe(new ItemStack(slab, 3, 5), "###", '#', Block.stoneBrick);
            addRecipe(new ItemStack(platestone, 1), "##", '#', Block.stone);
            addRecipe(new ItemStack(plateplanks, 1), "##", '#', Block.planks);
        }
        if (b>=2){
            addRecipe(new ItemStack(slab, 6, 0), "###", '#', Block.stone);
            addRecipe(new ItemStack(slab, 6, 3), "###", '#', Block.cobblestone);
            addRecipe(new ItemStack(slab, 6, 1), "###", '#', Block.sandStone);
            addRecipe(new ItemStack(slab, 6, 4), "###", '#', Block.brick);
            addRecipe(new ItemStack(slab, 6, 5), "###", '#', Block.stoneBrick);
            addRecipe(new ItemStack(platestone, 1), "##", '#', Block.stone);
            addRecipe(new ItemStack(plateplanks, 1), "##", '#', Block.planks);
        }
        if (b==2){
            addRecipe(new ItemStack(slab, 6, 2), "###", '#', Block.planks);
        }
        if (b==3){
            addRecipe(new ItemStack(Block.woodSingleSlab, 6, 0), "###", '#', new ItemStack(Block.planks, 1, 0));
            addRecipe(new ItemStack(Block.woodSingleSlab, 6, 2), "###", '#', new ItemStack(Block.planks, 1, 2));
            addRecipe(new ItemStack(Block.woodSingleSlab, 6, 1), "###", '#', new ItemStack(Block.planks, 1, 1));
            addRecipe(new ItemStack(Block.woodSingleSlab, 6, 3), "###", '#', new ItemStack(Block.planks, 1, 3));
        }
    }

    private void setLadders(int b){
        if (b < 0){
            b = 0;
        }
        String str = "xtile.ladder@0";
        Block ladder = Block.ladder;
        removeRecipe("1"+str);
        removeRecipe("2"+str);
        removeRecipe("3"+str);
        addRecipe(new ItemStack(ladder, b), "# #", "###", "# #", '#', Item.stick);
    }

    private void setGlowstone(boolean b){
        Block glowstone = Block.glowStone;
        removeRecipe("1xtile.lightgem@0");
        if (b){
            addRecipe(new ItemStack(glowstone, 1), "###", "###", "###", '#', Item.glowstone);
        }else{
            addRecipe(new ItemStack(glowstone, 1), "##", "##", '#', Item.glowstone);
        }
    }

    private void setCloth(boolean b){
        Block cloth = Block.cloth;
        removeRecipe("1xtile.cloth@0");
        if (b){
            addRecipe(new ItemStack(cloth, 1), "###", "###", "###", '#', Item.silk);
        }else{
            addRecipe(new ItemStack(cloth, 1), "##", "##", '#', Item.silk);
        }
        for (int i = 0; i < 16; i++){
            addShapelessRecipe(new ItemStack(cloth, 1, BlockColored.getDyeFromBlock(i)), new ItemStack(Item.dyePowder, 1, i), new ItemStack(Item.itemsList[cloth.blockID], 1, 0));
        }
    }

    private void setApple(int b){
        removeRecipe("1xitem.appleGold@0");
        removeRecipe("1xitem.appleGold@1");
        if (b == 0){
            addRecipe(new ItemStack(Item.appleGold, 1, 0), "###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed);
        }else if (b == 1){
            addRecipe(new ItemStack(Item.appleGold, 1, 0), "###", "#X#", "###", '#', Item.goldNugget, 'X', Item.appleRed);
        }else{
            addRecipe(new ItemStack(Item.appleGold, 1, 0), "###", "#X#", "###", '#', Item.goldNugget, 'X', Item.appleRed);
            addRecipe(new ItemStack(Item.appleGold, 1, 1), "###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed);
        }
    }

    private void setStew(boolean b){
        removeRecipe("1xitem.mushroomStew@0");
        if (b){
            addRecipe(new ItemStack(Item.bowlSoup), "Y", "X", "#", 'X', Block.mushroomBrown, 'Y', Block.mushroomRed, '#', Item.bowlEmpty);
        }else{
            addShapelessRecipe(new ItemStack(Item.bowlSoup), Block.mushroomBrown, Block.mushroomRed, Item.bowlEmpty);
        }
    }

    private void setBook(boolean b){
        removeRecipe("1xitem.book@0");
        if (b){
            addRecipe(new ItemStack(Item.book, 1), "#", "#", "#", '#', Item.paper);
        }else{
            addShapelessRecipe(new ItemStack(Item.book, 1), Item.paper, Item.paper, Item.paper, Item.leather);
        }
    }

    private void setOreBlocks(boolean b){
        removeRecipe("1xtile.blockIron@0");
        removeRecipe("1xtile.blockGold@0");
        removeRecipe("1xtile.blockDiamond0");
        if (b){
            addRecipe(new ItemStack(Block.blockIron, 1), "##", "##", '#', Item.ingotIron);
            addRecipe(new ItemStack(Block.blockGold, 1), "##", "##", '#', Item.ingotGold);
            addRecipe(new ItemStack(Block.blockDiamond, 1), "##", "##", '#', Item.diamond);
        }else{
            addRecipe(new ItemStack(Block.blockIron, 1), "###", "###", "###", '#', Item.ingotIron);
            addRecipe(new ItemStack(Block.blockGold, 1), "###", "###", "###", '#', Item.ingotGold);
            addRecipe(new ItemStack(Block.blockDiamond, 1), "###", "###", "###", '#', Item.diamond);
        }
        int n = b ? 9 : 4;
        removeRecipe(n+"xitem.ingotIron@0");
        removeRecipe(n+"xitem.ingotGold@0");
        removeRecipe(n+"xitem.diamond@0");
        n = b ? 4 : 9;
        addRecipe(new ItemStack(Item.ingotIron, n), "#", '#', Block.blockIron);
        addRecipe(new ItemStack(Item.ingotGold, n), "#", '#', Block.blockGold);
        addRecipe(new ItemStack(Item.diamond, n), "#", '#', Block.blockDiamond);
    }

    private void setSign(boolean b){
        int count = b ? 1 : 3;
        String str = "xitem.sign@0";
        removeRecipe("1"+str);
        removeRecipe("3"+str);
        addRecipe(new ItemStack(Item.sign, count), "###", "###", " X ", '#', Block.planks, 'X', Item.stick);
    }

    private void setMap(boolean b){
        removeRecipe("1xitem.emptyMap@0");
        removeRecipe("1xitem.map@0");
        if (b){
            addRecipe(new ItemStack(Item.map, 1), "###", "#X#", "###", '#', Item.paper, 'X', Item.compass);
        }else{
            addRecipe(new ItemStack(Item.emptyMap, 1), "###", "#X#", "###", '#', Item.paper, 'X', Item.compass);
        }
    }

    private void setArrows(boolean b){
        removeRecipe("4xitem.arrow@0");
        Item i = b ? Item.ingotIron : Item.flint;
        addRecipe(new ItemStack(Item.arrow, 4),  "X", "#", "Y", 'Y', Item.feather, 'X', i, '#', Item.stick);
    }

    private void setBread(boolean b){
        removeRecipe("1xitem.bread@0");
        if (b){
            addRecipe(new ItemStack(Item.bread, 1), "###", "###", '#', Item.wheat);
        }else{
            addRecipe(new ItemStack(Item.bread, 1), "###", '#', Item.wheat);
        }
    }

    private void setButtons(boolean b){
        removeRecipe("1xtile.button@0");
        removeRecipe("1xtile.button@0");
        addRecipe(new ItemStack(Block.woodenButton, 1), "#", '#', Block.planks);
        if (b){
            addRecipe(new ItemStack(Block.stoneButton, 1), "#", "#", '#', Block.stone);
        }else{
            addRecipe(new ItemStack(Block.stoneButton, 1), "#", '#', Block.stone);
        }
    }
}