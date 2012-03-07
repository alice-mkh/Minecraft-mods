package net.minecraft.src;

public class mod_Hydrangea extends BaseMod
{
   public static Block hydrangea = new BlockHydrangea(162, 0).setHardness(0F).setResistance(0F).setBlockName("hydrangea").setStepSound(Block.soundGrassFootstep);
   public static Item hydrangeaItem = (new ItemHydrangea(2871, mod_Hydrangea.hydrangea)).setItemName("hydrangeaItem");
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(hydrangea);
      hydrangea.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/hydrangea.png");
      hydrangeaItem.iconIndex = ModLoader.addOverride("/gui/items.png", "/extrabiomes/hydrangea.png");
      ModLoader.addName(hydrangea, "Hydrangea");
      ModLoader.addName(hydrangeaItem, "Hydrangea");
   }
}