package net.minecraft.src;

public class mod_CatTail extends BaseMod
{
   public static Block catTail = new BlockCatTail(161, 0).setHardness(0F).setResistance(0F).setBlockName("catTail").setStepSound(Block.soundGrassFootstep).disableStats();
   public static Item catTailItem = (new ItemCatTail(2870, mod_CatTail.catTail)).setItemName("catTailItem");
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(catTail);
      catTail.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/cattail.png");
      catTailItem.iconIndex = ModLoader.addOverride("/gui/items.png", "/extrabiomes/cattail.png");
      ModLoader.addName(catTail, "Cattail");
      ModLoader.addName(catTailItem, "Cattail");
   }
}