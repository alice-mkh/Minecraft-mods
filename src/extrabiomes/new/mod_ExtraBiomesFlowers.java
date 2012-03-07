package net.minecraft.src;

public class mod_ExtraBiomesFlowers extends BaseMod
{
	public static Block orangeFlower = new BlockOrangeFlower(165, 0).setHardness(0F).setResistance(0F).setBlockName("orangeFlower").setStepSound(Block.soundGrassFootstep);
	public static Item orangeFlowerItem = (new ItemOrangeFlower(2873, mod_ExtraBiomesFlowers.orangeFlower)).setItemName("orangeFlowerItem");
	public static Block purpleFlower = new BlockPurpleFlower(167, 0).setHardness(0F).setResistance(0F).setBlockName("purpleFlower").setStepSound(Block.soundGrassFootstep);
	public static Item purpleFlowerItem = (new ItemPurpleFlower(2872, mod_ExtraBiomesFlowers.purpleFlower)).setItemName("purpleFlower");
	public static Block whiteFlower = new BlockWhiteFlower(166, 0).setHardness(0F).setResistance(0F).setBlockName("whiteFlower").setStepSound(Block.soundGrassFootstep);
	public static Item whiteFlowerItem = (new ItemWhiteFlower(2874, mod_ExtraBiomesFlowers.whiteFlower)).setItemName("whiteFlowerItem");
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(orangeFlower);
      ModLoader.registerBlock(purpleFlower);
      ModLoader.registerBlock(whiteFlower);
      orangeFlower.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/orangeflower.png");
      orangeFlowerItem.iconIndex = ModLoader.addOverride("/gui/items.png", "/extrabiomes/orangeflower.png");
      purpleFlower.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/purpleflower.png");
      purpleFlowerItem.iconIndex = ModLoader.addOverride("/gui/items.png", "/extrabiomes/purpleflower.png");
      whiteFlower.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/whiteflowers.png");
      whiteFlowerItem.iconIndex = ModLoader.addOverride("/gui/items.png", "/extrabiomes/whiteflowers.png");
      ModLoader.addName(orangeFlower, "Orange Flowers");
      ModLoader.addName(orangeFlowerItem, "Orange Flowers");
      ModLoader.addName(purpleFlower, "Purple Flower");
      ModLoader.addName(purpleFlowerItem, "Purple Flower");
      ModLoader.addName(whiteFlower, "White Flowers");
      ModLoader.addName(whiteFlowerItem, "White Flowers");
   }
}