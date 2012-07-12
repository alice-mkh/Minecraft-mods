package net.minecraft.src;

import java.util.List;
import net.minecraft.src.backport.*;

public class mod_backport extends BaseMod{
    public mod_backport(){
        try{
            for (int i = 168; i <= 173; i++){
                ModLoader.setPrivateValue(net.minecraft.src.ModLoader.class, null, "terrainSpriteIndex", i);
                ModLoader.getUniqueSpriteIndex("/terrain.png");
            }
            ModLoader.setPrivateValue(net.minecraft.src.ModLoader.class, null, "terrainSpriteIndex", 0);
            for (int i = 170; i <= 172; i++){
                ModLoader.setPrivateValue(net.minecraft.src.ModLoader.class, null, "itemSpriteIndex", i);
                ModLoader.getUniqueSpriteIndex("/gui/items.png");
            }
            ModLoader.setPrivateValue(net.minecraft.src.ModLoader.class, null, "itemSpriteIndex", 0);
        }catch(Exception ex){
            System.out.println(ex);
        }

        emeraldBlock = new BlockOreStorage(133, 25);
        emeraldBlock.setHardness(5F);
        emeraldBlock.setResistance(10F);
        emeraldBlock.setStepSound(Block.soundMetalFootstep);
        emeraldBlock.setBlockName("blockEmerald");
        ModLoader.addName(emeraldBlock, "Block of Emerald");
        ModLoader.registerBlock(emeraldBlock);

        emeraldOre = new BlockOre2(129, 171);
        emeraldOre.setHardness(3F);
        emeraldOre.setResistance(5F);
        emeraldOre.setStepSound(Block.soundStoneFootstep);
        emeraldOre.setBlockName("oreEmerald");
        ModLoader.addName(emeraldOre, "Emerald Ore");
        ModLoader.registerBlock(emeraldOre);

        Item.itemsList[256 + 132] = null;
        Item emerald = new Item(132);
        emerald.setIconCoord(10, 11);
        emerald.setItemName("emerald2");
        ModLoader.addName(emerald, "Emerald");
        Item.itemsList[256 + 132] = emerald;

        BlockCocoa cocoa = new BlockCocoa(127);
        cocoa.setHardness(0.2F);
        cocoa.setResistance(5F);
        cocoa.setStepSound(Block.soundWoodFootstep);
        cocoa.setRequiresSelfNotify();
        ModLoader.registerBlock(cocoa);
        cocoaRenderID = ModLoader.getUniqueBlockModelID(this, false);

        Item.itemsList[256 + 131] = null;
        ItemWritableBook bookAndQuill = new ItemWritableBook(130);
        bookAndQuill.setIconCoord(11, 11);
        bookAndQuill.setItemName("writingBook");
        ModLoader.addName(bookAndQuill, "Book and Quill");
        Item.itemsList[256 + 130] = bookAndQuill;

        Item.itemsList[256 + 131] = null;
        ItemEditableBook writtenBook = new ItemEditableBook(131);
        writtenBook.setIconCoord(12, 11);
        writtenBook.setItemName("writtenBook");
        Item.itemsList[256 + 131] = writtenBook;

        BlockStairs2 sandstairs = new BlockStairs2(128, Block.sandStone, 0);
        sandstairs.setBlockName("stairsSandStone");
        sandstairs.setRequiresSelfNotify();
        ModLoader.addName(sandstairs, "Sandstone Stairs");
        ModLoader.registerBlock(sandstairs);
        Block.useNeighborBrightness[128] = true;

        BlockStairs2 plank1Stairs = new BlockStairs2(134, Block.planks, 1);
        plank1Stairs.setBlockName("stairsWoodSpruce");
        plank1Stairs.setRequiresSelfNotify();
        ModLoader.registerBlock(plank1Stairs);
        Block.useNeighborBrightness[134] = true;

        BlockStairs2 plank2Stairs = new BlockStairs2(135, Block.planks, 2);
        plank2Stairs.setBlockName("stairsWoodBirch");
        plank2Stairs.setRequiresSelfNotify();
        ModLoader.registerBlock(plank2Stairs);
        Block.useNeighborBrightness[135] = true;

        BlockStairs2 plank3Stairs = new BlockStairs2(136, Block.planks, 3);
        plank3Stairs.setBlockName("stairsWoodJungle");
        plank3Stairs.setRequiresSelfNotify();
        ModLoader.registerBlock(plank3Stairs);
        Block.useNeighborBrightness[136] = true;

        ModLoader.addName(plank1Stairs, "Wooden Stairs");
        ModLoader.addName(plank2Stairs, "Wooden Stairs");
        ModLoader.addName(plank3Stairs, "Wooden Stairs");

        BlockTripWireSource wireHook = (BlockTripWireSource)(new BlockTripWireSource(131));
        wireHook.setBlockName("tripWireSource");
        wireHook.setRequiresSelfNotify();
        ModLoader.addName(wireHook, "Tripwire Hook");
        ModLoader.registerBlock(wireHook);
        hookRenderID = ModLoader.getUniqueBlockModelID(this, false);

        BlockTripWire wire = new BlockTripWire(132);
        wire.setBlockName("tripWire");
        wire.setRequiresSelfNotify();
        ModLoader.addName(wire, "Tripwire");
        ModLoader.registerBlock(wire);
        wireRenderID = ModLoader.getUniqueBlockModelID(this, false);

        Item.itemsList[256 + 31] = null;
        ItemReed silk = new ItemReed(31, wire);
        silk.setIconCoord(8, 0);
        silk.setItemName("string");

        BlockEnderChest chest = new BlockEnderChest(130);
        chest.setHardness(22.5F);
        chest.setResistance(1000F);
        chest.setStepSound(Block.soundStoneFootstep);
        chest.setBlockName("enderChest");
        chest.setRequiresSelfNotify();
        chest.setLightValue(0.5F);
        ModLoader.addName(chest, "Ender Chest");
        ModLoader.registerBlock(chest);
        ModLoader.registerTileEntity(net.minecraft.src.backport.TileEntityEnderChest.class, "EnderChest", new TileEntityEnderChestRenderer());

        ModLoader.addRecipe(new ItemStack(emeraldBlock, 1), new Object[]{"###", "###", "###", '#', emerald});
        ModLoader.addRecipe(new ItemStack(emerald, 9), new Object[]{"#", '#', emeraldBlock});
        ModLoader.addSmelting(emeraldOre.blockID, new ItemStack(emerald, 1));
        ModLoader.addShapelessRecipe(new ItemStack(bookAndQuill, 1), new Object[]{
            Item.book, new ItemStack(Item.dyePowder, 1, 0), Item.feather});
        ModLoader.addRecipe(new ItemStack(sandstairs, 1), new Object[]{"#  ", "## ", "###", '#', Block.sandStone});
        ModLoader.addRecipe(new ItemStack(sandstairs, 1), new Object[]{"  #", " ##", "###", '#', Block.sandStone});
        removeRecipe("4xtile.stairsWood@0");
        ModLoader.addRecipe(new ItemStack(Block.stairCompactPlanks, 1), new Object[]{"#  ", "## ", "###", '#', new ItemStack(Block.planks, 1, 0)});
        ModLoader.addRecipe(new ItemStack(Block.stairCompactPlanks, 1), new Object[]{"  #", " ##", "###", '#', new ItemStack(Block.planks, 1, 0)});
        ModLoader.addRecipe(new ItemStack(plank1Stairs, 1), new Object[]{"#  ", "## ", "###", '#', new ItemStack(Block.planks, 1, 1)});
        ModLoader.addRecipe(new ItemStack(plank1Stairs, 1), new Object[]{"  #", " ##", "###", '#', new ItemStack(Block.planks, 1, 1)});
        ModLoader.addRecipe(new ItemStack(plank2Stairs, 1), new Object[]{"#  ", "## ", "###", '#', new ItemStack(Block.planks, 1, 2)});
        ModLoader.addRecipe(new ItemStack(plank2Stairs, 1), new Object[]{"  #", " ##", "###", '#', new ItemStack(Block.planks, 1, 2)});
        ModLoader.addRecipe(new ItemStack(plank3Stairs, 1), new Object[]{"#  ", "## ", "###", '#', new ItemStack(Block.planks, 1, 3)});
        ModLoader.addRecipe(new ItemStack(plank3Stairs, 1), new Object[]{"  #", " ##", "###", '#', new ItemStack(Block.planks, 1, 3)});
        ModLoader.addRecipe(new ItemStack(wireHook, 1),new Object[]{"#", "$", "%", '#', Item.ingotIron, '$', Item.stick, '%', Block.planks});
        ModLoader.addRecipe(new ItemStack(chest, 1),new Object[]{"###", "#$#", "###", '#', Block.obsidian, '$', Item.eyeOfEnder});

        ModLoader.addLocalization("book.pageIndicator", "Page %1$s of %2$s");
        ModLoader.addLocalization("book.byAuthor", "by %1$s");
        ModLoader.addLocalization("book.signButton", "Sign");
        ModLoader.addLocalization("book.editTitle", "Enter Book Title");
        ModLoader.addLocalization("book.finalizeButton", "Sign and Close");
        ModLoader.addLocalization("book.finalizeWarning", "Note! When you sign the book, it will no longer be editable.");
        ModLoader.addLocalization("container.enderchest", "Ender Chest");
    }

    public void load(){}

    public String getVersion(){
        return "1.2.5";
    }

    public void removeRecipe(String str1){
        try{
            List list = CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < list.size(); i++){
                String match = ((IRecipe)list.get(i)).getRecipeOutput().toString();
                if (match.equals(str1)){
                    list.remove(i);
                }
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public boolean renderWorldBlock(RenderBlocks r, IBlockAccess i, int x, int y, int z, Block b, int id){
        if (id == cocoaRenderID){
            return BlockCocoa.renderBlockCocoa(r, i, b, x, y, z);
        }
        if (id == wireRenderID){
            return BlockTripWire.renderBlockWire(r, i, b, x, y, z);
        }
        if (id == hookRenderID){
            return BlockTripWireSource.renderBlockWireHook(r, i, b, x, y, z);
        }
        return false;
    }

    public String getPriorities(){
        return "before:*";
    }

    public static int cocoaRenderID;
    public static int wireRenderID;
    public static int hookRenderID;
    public static Block emeraldBlock;
    public static Block emeraldOre;
}