package net.minecraft.src;

import java.util.*;

public class ODCrafting extends OldDaysModule{
    public ODCrafting(mod_OldDays c){
        super(c, 6, "Crafting");
        addProperty(1, "Old planks",            false, false, "OldPlanks",  "");
        addProperty(2, "Leather armor",         1,     1,     "ClothArmor", "", new String[]{"Cloth", "Leather", "Both"});
        addProperty(3, "Slabs",                 2,     2,     "OldSlabs",   "", new String[]{"Alpha", "Beta", "1.2.1"});
        addProperty(4, "Ladders from crafting", 2,     2,     "Ladders",    "", new String[]{"1", "2", "3"});
        addProperty(5, "Old cloth",             false, false, "Cloth",      "");
        addProperty(6, "Old glowstone",         false, false, "Glowstone",  "");
        addProperty(7, "Old golden apple",      false, false, "Apple",      "");
        addProperty(8, "Old mushroom stew",     false, false, "Stew",       "");
        addProperty(9, "Old ore blocks",        false, false, "OreBlocks",  "");
        addProperty(10,"Old books",             true,  true,  "Books",      "");
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

    private void setPlanks(boolean b){
        String str = "4xtile.wood@";
        Block log = Block.wood;
        Block planks = Block.planks;
        if (b){
            removeRecipe(str+"3");
            removeRecipe(str+"2");
            removeRecipe(str+"1");
            removeRecipe(str+"0");
            ModLoader.addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 0)});
            ModLoader.addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 1)});
            ModLoader.addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 2)});
            ModLoader.addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 3)});
        }else{
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            removeRecipe(str+"0");
            ModLoader.addRecipe(new ItemStack(planks, 4, 0), new Object[] {"#", '#', new ItemStack(log, 1, 0)});
            ModLoader.addRecipe(new ItemStack(planks, 4, 1), new Object[] {"#", '#', new ItemStack(log, 1, 1)});
            ModLoader.addRecipe(new ItemStack(planks, 4, 2), new Object[] {"#", '#', new ItemStack(log, 1, 2)});
            ModLoader.addRecipe(new ItemStack(planks, 4, 3), new Object[] {"#", '#', new ItemStack(log, 1, 3)});
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
            ModLoader.addRecipe(new ItemStack(Item.helmetLeather, 1), new Object[] {"###", "# #", '#', leather});
            ModLoader.addRecipe(new ItemStack(Item.plateLeather, 1), new Object[] {"# #", "###", "###", '#', leather});
            ModLoader.addRecipe(new ItemStack(Item.legsLeather, 1), new Object[] {"###", "# #", "# #", '#', leather});
            ModLoader.addRecipe(new ItemStack(Item.bootsLeather, 1), new Object[] {"# #", "# #", '#', leather});
        }
        if (b==0 || b>1){
            ModLoader.addRecipe(new ItemStack(Item.helmetLeather, 1), new Object[] {"###", "# #", '#', cloth});
            ModLoader.addRecipe(new ItemStack(Item.plateLeather, 1), new Object[] {"# #", "###", "###", '#', cloth});
            ModLoader.addRecipe(new ItemStack(Item.legsLeather, 1), new Object[] {"###", "# #", "# #", '#', cloth});
            ModLoader.addRecipe(new ItemStack(Item.bootsLeather, 1), new Object[] {"# #", "# #", '#', cloth});
        }
    }

    private void setSlabs(int b){
        String slab3 = "3xtile.stoneSlab@";
        String slab6 = "6xtile.stoneSlab@";
        String plate = "1xtile.pressurePlate@0";
        Block slab = Block.stairSingle;
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
            ModLoader.addRecipe(new ItemStack(slab, 3, 0), new Object[] {"###", '#', Block.cobblestone});
            ModLoader.addRecipe(new ItemStack(platestone, 1), new Object[] {"###", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(plateplanks, 1), new Object[] {"###", '#', Block.planks});
        }
        if (b==1){
            ModLoader.addRecipe(new ItemStack(slab, 3, 0), new Object[] {"###", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(slab, 3, 2), new Object[] {"###", '#', Block.planks});
            ModLoader.addRecipe(new ItemStack(slab, 3, 3), new Object[] {"###", '#', Block.cobblestone});
            ModLoader.addRecipe(new ItemStack(slab, 3, 1), new Object[] {"###", '#', Block.sandStone});
            ModLoader.addRecipe(new ItemStack(slab, 3, 4), new Object[] {"###", '#', Block.brick});
            ModLoader.addRecipe(new ItemStack(slab, 3, 5), new Object[] {"###", '#', Block.stoneBrick});
            ModLoader.addRecipe(new ItemStack(platestone, 1), new Object[] {"##", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(plateplanks, 1), new Object[] {"##", '#', Block.planks});
        }
        if (b==2){
            ModLoader.addRecipe(new ItemStack(slab, 6, 0), new Object[] {"###", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(slab, 6, 2), new Object[] {"###", '#', Block.planks});
            ModLoader.addRecipe(new ItemStack(slab, 6, 3), new Object[] {"###", '#', Block.cobblestone});
            ModLoader.addRecipe(new ItemStack(slab, 6, 1), new Object[] {"###", '#', Block.sandStone});
            ModLoader.addRecipe(new ItemStack(slab, 6, 4), new Object[] {"###", '#', Block.brick});
            ModLoader.addRecipe(new ItemStack(slab, 6, 5), new Object[] {"###", '#', Block.stoneBrick});
            ModLoader.addRecipe(new ItemStack(platestone, 1), new Object[] {"##", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(plateplanks, 1), new Object[] {"##", '#', Block.planks});
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
        ModLoader.addRecipe(new ItemStack(ladder, b + 1), new Object[]{"# #", "###", "# #", '#', Item.stick});
    }

    private void setGlowstone(boolean b){
        Block glowstone = Block.glowStone;
        removeRecipe("1xtile.lightgem@0");
        if (b){
            ModLoader.addRecipe(new ItemStack(glowstone, 1), new Object[]{"###", "###", "###", '#', Item.lightStoneDust});
        }else{
            ModLoader.addRecipe(new ItemStack(glowstone, 1), new Object[]{"##", "##", '#', Item.lightStoneDust});
        }
    }

    private void setCloth(boolean b){
        Block cloth = Block.cloth;
        removeRecipe("1xtile.cloth@0");
        if (b){
            ModLoader.addRecipe(new ItemStack(cloth, 1), new Object[]{"###", "###", "###", '#', Item.silk});
        }else{
            ModLoader.addRecipe(new ItemStack(cloth, 1), new Object[]{"##", "##", '#', Item.silk});
        }
        for (int i = 0; i < 16; i++){
            ModLoader.addShapelessRecipe(new ItemStack(cloth, 1, BlockCloth.getDyeFromBlock(i)), new Object[]{
                new ItemStack(Item.dyePowder, 1, i), new ItemStack(Item.itemsList[cloth.blockID], 1, 0)
            });
        }
    }

    private void setApple(boolean b){
        removeRecipe("1xitem.appleGold@0");
        if (b){
            ModLoader.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed});
        }else{
            ModLoader.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Item.goldNugget, 'X', Item.appleRed});
        }
    }

    private void setStew(boolean b){
        removeRecipe("1xitem.mushroomStew@0");
        if (b){
            ModLoader.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{
                "Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty
            });
        }else{
            ModLoader.addShapelessRecipe(new ItemStack(Item.bowlSoup), new Object[]{Block.mushroomBrown, Block.mushroomRed, Item.bowlEmpty});
        }
    }

    private void setBook(boolean b){
        removeRecipe("1xitem.book@0");
        if (b){
            ModLoader.addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", Character.valueOf('#'), Item.paper});
        }else{
            ModLoader.addShapelessRecipe(new ItemStack(Item.book, 1), new Object[]{Item.paper, Item.paper, Item.paper, Item.leather});
        }
    }

    private void setOreBlocks(boolean b){
        removeRecipe("1xtile.blockIron@0");
        removeRecipe("1xtile.blockGold@0");
        removeRecipe("1xtile.blockDiamond@0");
        if (b){
            ModLoader.addRecipe(new ItemStack(Block.blockSteel, 1), new Object[]{"##", "##", '#', Item.ingotIron});
            ModLoader.addRecipe(new ItemStack(Block.blockGold, 1), new Object[]{"##", "##", '#', Item.ingotGold});
            ModLoader.addRecipe(new ItemStack(Block.blockDiamond, 1), new Object[]{"##", "##", '#', Item.diamond});
        }else{
            ModLoader.addRecipe(new ItemStack(Block.blockSteel, 1), new Object[]{"###", "###", "###", '#', Item.ingotIron});
            ModLoader.addRecipe(new ItemStack(Block.blockGold, 1), new Object[]{"###", "###", "###", '#', Item.ingotGold});
            ModLoader.addRecipe(new ItemStack(Block.blockDiamond, 1), new Object[]{"###", "###", "###", '#', Item.diamond});
        }
        int n = b ? 9 : 4;
        removeRecipe(n+"xitem.ingotIron@0");
        removeRecipe(n+"xitem.ingotGold@0");
        removeRecipe(n+"xitem.emerald@0");
        n = b ? 4 : 9;
        ModLoader.addRecipe(new ItemStack(Item.ingotIron, n), new Object[]{"#", '#', Block.blockSteel});
        ModLoader.addRecipe(new ItemStack(Item.ingotGold, n), new Object[]{"#", '#', Block.blockGold});
        ModLoader.addRecipe(new ItemStack(Item.diamond, n), new Object[]{"#", '#', Block.blockDiamond});
    }
}