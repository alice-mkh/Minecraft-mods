package net.minecraft.src;
import java.util.*;

public class mod_OldDaysEyecandy extends mod_OldDays{
    public void load(){
        registerModule(4);
        addProperty(this, 1, "Old walking",           true,  "OldWalking");
        addProperty(this, 2, "Bobbing",               false, "Bobbing");
        addProperty(this, 3, "Old endermen",          true,  "OldEndermen");
        addProperty(this, 4, "Endermen open mouth",   true,  "EndermenOpenMouth");
        addProperty(this, 5, "Item sway",             true,  "ItemSway");
        addProperty(this, 6, "2D items",              false, "Items2D");
        addProperty(this, 7, "Old chests",            true,  "OldChest");
        addProperty(this, 8, "Show mob IDs in F3",    true,  "MobLabels");
        addProperty(this, 9, "Mob armor",             false, "MobArmor");
        addProperty(this, 10,"Old main menu",         true,  "OldMainMenu");
        addProperty(this, 11,"Old digging particles", true,  "OldDigging");
        addProperty(this, 12,"Old ore blocks",        true,  "OldOreBlocks");
//         addProperty(this, 13,"Old item tooltips",     true,  "OldTooltips");
        loadModuleProperties();
        replaceBlocks();
    }

    public void callback (int i){
        switch (i){
            case 1: ModelBiped.oldwalking =             OldWalking;        break;
            case 2: RenderLiving.bobbing =              Bobbing;           break;
            case 3: EntityEnderman.smoke =              OldEndermen;
                    RenderEnderman2.greeneyes =         OldEndermen;       break;
            case 4: ModelEnderman.openmouth =           EndermenOpenMouth; break;
            case 5: ItemRenderer.sway =                 ItemSway;          break;
            case 6: ItemRenderer.items2d =              Items2D;           break;
            case 7: BlockChestOld.normalblock =         OldChest;
                    TileEntityChestRenderer.hidemodel = OldChest;
                    RenderMinecart2.shiftChest =        OldChest;
                    ModLoader.getMinecraftInstance().renderGlobal.loadRenderers(); break;
            case 8: RenderLiving.labels =               MobLabels;         break;
            case 9: RenderZombie.mobArmor =             MobArmor;
                    RenderSkeleton.mobArmor =           MobArmor;          break;
            case 10:GuiMainMenu.panorama =             !OldMainMenu;
                    GuiMainMenu.oldlogo =               OldMainMenu;       break;
            case 11:EntityDiggingFX.oldparticles =      OldDigging;        break;
            case 12:BlockOreStorageOld.oldtextures =    OldOreBlocks;
                    ModLoader.getMinecraftInstance().renderGlobal.loadRenderers(); break;
//             case 13:GuiContainer.oldtooltips =          OldTooltips;       break;
        }
    }

    public void addRenderer(Map map){
        map.put(net.minecraft.src.EntityEnderman.class, new RenderEnderman2());
        map.put(net.minecraft.src.EntityMinecart.class, new RenderMinecart2());
        map.put(net.minecraft.src.EntityZombie.class, new RenderZombie(new ModelZombie()));
        map.put(net.minecraft.src.EntitySkeleton.class, new RenderSkeleton(new ModelSkeleton()));
    }

    public static boolean ItemSway = true;
    public static boolean Items2D;
    public static boolean Bobbing;
    public static boolean OldWalking = true;
    public static boolean OldEndermen = true;
    public static boolean EndermenOpenMouth = true;
    public static boolean OldChest = true;
    public static boolean MobLabels = true;
    public static boolean MobArmor;
    public static boolean OldMainMenu = true;
    public static boolean OldDigging = true;
//     public static boolean OldTooltips = true;
    public static boolean OldOreBlocks = true;

    private void replaceBlocks(){
        try{
            Block.blocksList[Block.chest.blockID] = null;
            BlockChestOld customchest = (BlockChestOld)(new BlockChestOld(54));
            customchest.setHardness(2.5F);
            customchest.setStepSound(Block.soundWoodFootstep);
            customchest.setBlockName("chest");
            customchest.setRequiresSelfNotify();
            Block.blocksList[Block.chest.blockID] = customchest;
            Block.blocksList[Block.blockSteel.blockID] = null;
            BlockOreStorageOld customsteel = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockSteel.blockID, 22));
            customsteel.setHardness(5F);
            customsteel.setResistance(10F);
            customsteel.setStepSound(Block.soundMetalFootstep);
            customsteel.setBlockName("blockIron");
            customsteel.sidetex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/ironside.png");
            customsteel.bottomtex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/ironbottom.png");
            Block.blocksList[Block.blockSteel.blockID] = customsteel;
            Block.blocksList[Block.blockGold.blockID] = null;
            BlockOreStorageOld customgold = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockGold.blockID, 23));
            customgold.setHardness(3F);
            customgold.setResistance(10F);
            customgold.setStepSound(Block.soundMetalFootstep);
            customgold.setBlockName("blockGold");
            customgold.sidetex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/goldside.png");
            customgold.bottomtex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/goldbottom.png");
            Block.blocksList[Block.blockGold.blockID] = customgold;
            Block.blocksList[Block.blockDiamond .blockID] = null;
            BlockOreStorageOld customdiamond = (BlockOreStorageOld)(new BlockOreStorageOld(Block.blockDiamond .blockID, 24));
            customdiamond.setHardness(5F);
            customdiamond.setResistance(10F);
            customdiamond.setStepSound(Block.soundMetalFootstep);
            customdiamond.setBlockName("blockDiamond");
            customdiamond.sidetex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/diamondside.png");
            customdiamond.bottomtex = ModLoader.addOverride("/terrain.png", "/olddays/oreblocks/diamondbottom.png");
            Block.blocksList[Block.blockDiamond .blockID] = customdiamond;
        }catch (Exception exception){
            System.out.println(exception);
        }
    }
}