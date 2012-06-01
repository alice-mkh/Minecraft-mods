package net.minecraft.src;

import java.util.*;

public class mod_OldDaysCrafting extends mod_OldDays{
    public void load(){
        registerModule(this, 6);
        addProperty(1, "Old planks",            false, false, "OldPlanks",  "");
        addProperty(2, "Leather armor",         2,     2,     "ClothArmor", "", new String[]{"Cloth", "Leather", "Both"});
        addProperty(3, "Slabs",                 3,     3,     "OldSlabs",   "", new String[]{"Alpha", "Beta", "1.2.1"});
        addProperty(4, "Ladders from crafting", 3,     3,     "Ladders",    "", 3);
        addProperty(5, "Old cloth",             false, false, "Cloth",      "");
        addProperty(6, "Old glowstone",         false, false, "Glowstone",  "");
        addProperty(7, "Old golden apple",      false, false, "Apple",      "");
        addProperty(8, "Old mushroom stew",     false, false, "Stew",       "");
        addProperty(9, "12w17a books",          false, false, "Books",      "");
        loadModuleProperties();
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
            case 9: setBook(!Books); break;
        }
    }

    public static boolean OldPlanks;
    public static int ClothArmor = 2;
    public static int OldSlabs = 3;
    public static int Ladders = 3;
    public static boolean Cloth;
    public static boolean Glowstone;
    public static boolean Apple;
    public static boolean Stew;
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
        String helmet = "1xitem.helmetCloth@0";
        String plate = "1xitem.chestplateCloth@0";
        String legs = "1xitem.leggingsCloth@0";
        String boots = "1xitem.bootsCloth@0";
        Block cloth = Block.cloth;
        Item leather = Item.leather;
        removeRecipe(helmet);
        removeRecipe(plate);
        removeRecipe(legs);
        removeRecipe(boots);
        if (b>1){
            ModLoader.addRecipe(new ItemStack(Item.helmetLeather, 1), new Object[] {"###", "# #", '#', leather});
            ModLoader.addRecipe(new ItemStack(Item.plateLeather, 1), new Object[] {"# #", "###", "###", '#', leather});
            ModLoader.addRecipe(new ItemStack(Item.legsLeather, 1), new Object[] {"###", "# #", "# #", '#', leather});
            ModLoader.addRecipe(new ItemStack(Item.bootsLeather, 1), new Object[] {"# #", "# #", '#', leather});
        }
        if (b==1 || b>2){
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
        if (b==1){
            ModLoader.addRecipe(new ItemStack(slab, 3, 0), new Object[] {"###", '#', Block.cobblestone});
            ModLoader.addRecipe(new ItemStack(platestone, 1), new Object[] {"###", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(plateplanks, 1), new Object[] {"###", '#', Block.planks});
        }
        if (b==2){
            ModLoader.addRecipe(new ItemStack(slab, 3, 0), new Object[] {"###", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(slab, 3, 2), new Object[] {"###", '#', Block.planks});
            ModLoader.addRecipe(new ItemStack(slab, 3, 3), new Object[] {"###", '#', Block.cobblestone});
            ModLoader.addRecipe(new ItemStack(slab, 3, 1), new Object[] {"###", '#', Block.sandStone});
            ModLoader.addRecipe(new ItemStack(slab, 3, 4), new Object[] {"###", '#', Block.brick});
            ModLoader.addRecipe(new ItemStack(slab, 3, 5), new Object[] {"###", '#', Block.stoneBrick});
            ModLoader.addRecipe(new ItemStack(platestone, 1), new Object[] {"##", '#', Block.stone});
            ModLoader.addRecipe(new ItemStack(plateplanks, 1), new Object[] {"##", '#', Block.planks});
        }
        if (b==3){
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
        if (b<=0){
            b = 1;
        }
        String str = "xtile.ladder@0";
        Block ladder = Block.ladder;
        removeRecipe("1"+str);
        removeRecipe("2"+str);
        removeRecipe("3"+str);
        ModLoader.addRecipe(new ItemStack(ladder, b), new Object[]{"# #", "###", "# #", '#', Item.stick});
    }

    private void setGlowstone(boolean b){
        String str = "1xtile.lightgem@0";
        Block glowstone = Block.glowStone;
        removeRecipe(str);
        if (b){
            ModLoader.addRecipe(new ItemStack(glowstone, 1), new Object[]{"###", "###", "###", '#', Item.lightStoneDust});
        }else{
            ModLoader.addRecipe(new ItemStack(glowstone, 1), new Object[]{"##", "##", '#', Item.lightStoneDust});
        }
    }

    private void setCloth(boolean b){
        String str = "1xtile.cloth@0";
        Block cloth = Block.cloth;
        removeRecipe(str);
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
        String str = "1xitem.appleGold@0";
        removeRecipe(str);
        if (b){
            ModLoader.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Block.blockGold, 'X', Item.appleRed});
        }else{
            ModLoader.addRecipe(new ItemStack(Item.appleGold, 1), new Object[]{"###", "#X#", "###", '#', Item.goldNugget, 'X', Item.appleRed});
        }
    }

    private void setStew(boolean b){
        String str = "1xitem.mushroomStew@0";
        removeRecipe(str);
        if (b){
            ModLoader.addRecipe(new ItemStack(Item.bowlSoup), new Object[]{
                "Y", "X", "#", Character.valueOf('X'), Block.mushroomBrown, Character.valueOf('Y'), Block.mushroomRed, Character.valueOf('#'), Item.bowlEmpty
            });
        }else{
            ModLoader.addShapelessRecipe(new ItemStack(Item.bowlSoup), new Object[]{Block.mushroomBrown, Block.mushroomRed, Item.bowlEmpty});
        }
    }

    private void setBook(boolean b){
        String str = "1xitem.book@0";
        removeRecipe(str);
        if (b){
            ModLoader.addRecipe(new ItemStack(Item.book, 1), new Object[]{"#", "#", "#", Character.valueOf('#'), Item.paper});
        }else{
            ModLoader.addShapelessRecipe(new ItemStack(Item.book, 1), new Object[]{Item.paper, Item.paper, Item.paper, Item.leather});
        }
    }
}