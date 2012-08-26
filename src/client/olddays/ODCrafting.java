package net.minecraft.src;

import java.util.*;

public class ODCrafting extends OldDaysModule{
    public ODCrafting(mod_OldDays c){
        super(c, 6, "Crafting");
        new OldDaysPropertyBool(this, 1, false, false, "OldPlanks");
        new OldDaysPropertyInt(this,  2, 1,     1,     "ClothArmor", 2).setUseNames();
        new OldDaysPropertyInt(this,  3, 2,     2,     "OldSlabs", 2).setUseNames();
        new OldDaysPropertyInt(this,  4, 2,     2,     "Ladders", 1, 3);
        new OldDaysPropertyBool(this, 5, false, false, "Cloth");
        new OldDaysPropertyBool(this, 6, false, false, "Glowstone");
        new OldDaysPropertyBool(this, 7, false, false, "Apple");
        new OldDaysPropertyBool(this, 8, false, false, "Stew");
        new OldDaysPropertyBool(this, 9, false, false, "OreBlocks");
        new OldDaysPropertyBool(this, 10,true,  true,  "Books");
        new OldDaysPropertyBool(this, 11,true,  true,  "OldSigns");
    }

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
        }
    }

    public static boolean OldPlanks;
    public static int ClothArmor = 1;
    public static int OldSlabs = 2;
    public static int Ladders = 2;
    public static boolean Cloth;
    public static boolean Glowstone;
    public static boolean Apple;
    public static boolean Stew;
    public static boolean OreBlocks;
    public static boolean Books;
    public static boolean OldSigns;

    private void setPlanks(boolean b){
        String str = "4xtile.wood@";
        Block log = Block.wood;
        Block planks = Block.planks;
        if (b){
            removeRecipe(str+"3");
            removeRecipe(str+"2");
            removeRecipe(str+"1");
            removeRecipe(str+"0");
            addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 0)});
            addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 1)});
            addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 2)});
            addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 3)});
        }else{
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 0)});
            addRecipe(new ItemStack(planks, 4, 1), new Object[] {"#", '#', new ItemStack(log, 1, 1)});
            addRecipe(new ItemStack(planks, 4, 2), new Object[] {"#", '#', new ItemStack(log, 1, 2)});
            addRecipe(new ItemStack(planks, 4, 3), new Object[] {"#", '#', new ItemStack(log, 1, 3)});
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
            addRecipe(new ItemStack(Item.helmetLeather, 1), new Object[] {"###", "# #", '#', leather});
            addRecipe(new ItemStack(Item.plateLeather, 1), new Object[] {"# #", "###", "###", '#', leather});
            addRecipe(new ItemStack(Item.legsLeather, 1), new Object[] {"###", "# #", "# #", '#', leather});
            addRecipe(new ItemStack(Item.bootsLeather, 1), new Object[] {"# #", "# #", '#', leather});
        }
        if (b==0 || b>1){
            addRecipe(new ItemStack(Item.helmetLeather, 1), new Object[] {"###", "# #", '#', cloth});
            addRecipe(new ItemStack(Item.plateLeather, 1), new Object[] {"# #", "###", "###", '#', cloth});
            addRecipe(new ItemStack(Item.legsLeather, 1), new Object[] {"###", "# #", "# #", '#', cloth});
            addRecipe(new ItemStack(Item.bootsLeather, 1), new Object[] {"# #", "# #", '#', cloth});
        }
    }

    private void setSlabs(int b){
        String slab3 = "3xtile.stoneSlab@";
        String slab6 = "6xtile.stoneSlab@";
        String plate = "1xtile.pressurePlate@0";
        Block slab = Block.stoneSingleSlab;
        Block platestone = Block.pressurePlateStone;
        Block plateplanks = Block.pressurePlatePlanks;
        removeRecipe(slab3+"0");
        removeRecipe(slab3+"1");
        removeRecipe(slab3+"2");
        removeRecipe(slab3+"3");
        removeRecipe(slab3+"4");
        removeRecipe(slab3+"5");
        removeRecipe(slab6+"0");
        removeRecipe(slab6+"1");
        removeRecipe(slab6+"2");
        removeRecipe(slab6+"3");
        removeRecipe(slab6+"4");
        removeRecipe(slab6+"5");
        removeRecipe(plate);
        removeRecipe(plate);
        if (b==0){
            addRecipe(new ItemStack(slab, 3, 0), new Object[] {"###", '#', Block.cobblestone});
            addRecipe(new ItemStack(platestone, 1), new Object[] {"###", '#', Block.stone});
            addRecipe(new ItemStack(plateplanks, 1), new Object[] {"###", '#', Block.planks});
        }
        if (b==1){
            addRecipe(new ItemStack(slab, 3, 0), new Object[] {"###", '#', Block.stone});
            addRecipe(new ItemStack(slab, 3, 2), new Object[] {"###", '#', Block.planks});
            addRecipe(new ItemStack(slab, 3, 3), new Object[] {"###", '#', Block.cobblestone});
            addRecipe(new ItemStack(slab, 3, 1), new Object[] {"###", '#', Block.sandStone});
            addRecipe(new ItemStack(slab, 3, 4), new Object[] {"###", '#', Block.brick});
            addRecipe(new ItemStack(slab, 3, 5), new Object[] {"###", '#', Block.stoneBrick});
            addRecipe(new ItemStack(platestone, 1), new Object[] {"##", '#', Block.stone});
            addRecipe(new ItemStack(plateplanks, 1), new Object[] {"##", '#', Block.planks});
        }
        if (b==2){
            addRecipe(new ItemStack(slab, 6, 0), new Object[] {"###", '#', Block.stone});
            addRecipe(new ItemStack(slab, 6, 2), new Object[] {"###", '#', Block.planks});
            addRecipe(new ItemStack(slab, 6, 3), new Object[] {"###", '#', Block.cobblestone});
            addRecipe(new ItemStack(slab, 6, 1), new Object[] {"###", '#', Block.sandStone});
            addRecipe(new ItemStack(slab, 6, 4), new Object[] {"###", '#', Block.brick});
            addRecipe(new ItemStack(slab, 6, 5), new Object[] {"###", '#', Block.stoneBrick});
            addRecipe(new ItemStack(platestone, 1), new Object[] {"##", '#', Block.stone});
            addRecipe(new ItemStack(plateplanks, 1), new Object[] {"##", '#', Block.planks});
        }
    }

    private void setLadders(int b){
        if (b<0){
            b = 0;
        }
        String str = "xtile.ladder@0";
        Block ladder = Block.ladder;
        removeRecipe("1"+str);
        removeRecipe("2"+str);
        removeRecipe("3"+str);
        addRecipe(new ItemStack(ladder, b + 1), new Object[]{"# #", "###", "# #", '#', Item.stick});
    }

    private void setGlowstone(boolean b){
        Block glowstone = Block.glowStone;
        removeRecipe("1xtile.lightgem@0");
        if (b){
            addRecipe(new ItemStack(glowstone, 1), new Object[]{"###", "###", "###", '#', Item.lightStoneDust});
        }else{
            addRecipe(new ItemStack(glowstone, 1), new Object[]{"##", "##", '#', Item.lightStoneDust});
        }
    }

    private void setCloth(boolean b){
        Block cloth = Block.cloth;
        removeRecipe("1xtile.cloth@0");
        if (b){
            addRecipe(new ItemStack(cloth, 1), new Object[]{"###", "###", "###", '#', Item.silk});
        }else{
            addRecipe(new ItemStack(cloth, 1), new Object[]{"##", "##", '#', Item.silk});
        }
        for (int i = 0; i < 16; i++){
            addShapelessRecipe(new ItemStack(cloth, 1, BlockCloth.getDyeFromBlock(i)), new Object[]{
                new ItemStack(Item.dyePowder, 1, i), new ItemStack(Item.itemsList[cloth.blockID], 1, 0)
            });
        }
    }

    private void setApple(boolean b){
        removeRecipe("1xitem.appleGold@0");
        if (b){
            addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed});
        }else{
            addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Item.goldNugget, 'X', Item.appleRed});
        }
    }

    private void setStew(boolean b){
        removeRecipe("1xitem.mushroomStew@0");
        if (b){
            addRecipe(new ItemStack(Item.bowlSoup), new Object[]{
                "Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty
            });
        }else{
            addShapelessRecipe(new ItemStack(Item.bowlSoup), new Object[]{Block.mushroomBrown, Block.mushroomRed, Item.bowlEmpty});
        }
    }

    private void setBook(boolean b){
        removeRecipe("1xitem.book@0");
        if (b){
            addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", Character.valueOf('#'), Item.paper});
        }else{
            addShapelessRecipe(new ItemStack(Item.book, 1), new Object[]{Item.paper, Item.paper, Item.paper, Item.leather});
        }
    }

    private void setOreBlocks(boolean b){
        removeRecipe("1xtile.blockIron@0");
        removeRecipe("1xtile.blockGold@0");
        removeRecipe("1xtile.blockDiamond0");
        if (b){
            addRecipe(new ItemStack(Block.blockSteel, 1), new Object[]{"##", "##", '#', Item.ingotIron});
            addRecipe(new ItemStack(Block.blockGold, 1), new Object[]{"##", "##", '#', Item.ingotGold});
            addRecipe(new ItemStack(Block.blockDiamond, 1), new Object[]{"##", "##", '#', Item.diamond});
        }else{
            addRecipe(new ItemStack(Block.blockSteel, 1), new Object[]{"###", "###", "###", '#', Item.ingotIron});
            addRecipe(new ItemStack(Block.blockGold, 1), new Object[]{"###", "###", "###", '#', Item.ingotGold});
            addRecipe(new ItemStack(Block.blockDiamond, 1), new Object[]{"###", "###", "###", '#', Item.diamond});
        }
        int n = b ? 9 : 4;
        removeRecipe(n+"xitem.ingotIron@0");
        removeRecipe(n+"xitem.ingotGold@0");
        removeRecipe(n+"xitem.emerald@0");
        n = b ? 4 : 9;
        addRecipe(new ItemStack(Item.ingotIron, n), new Object[]{"#", '#', Block.blockSteel});
        addRecipe(new ItemStack(Item.ingotGold, n), new Object[]{"#", '#', Block.blockGold});
        addRecipe(new ItemStack(Item.diamond, n), new Object[]{"#", '#', Block.blockDiamond});
    }

    private void setSign(boolean b){
        int count = b ? 1 : 3;
        String str = "xitem.sign@0";
        removeRecipe("1"+str);
        removeRecipe("3"+str);
        addRecipe(new ItemStack(Item.sign, count), new Object[]{"###", "###", " X ", '#', Block.planks, 'X', Item.stick});
    }
}