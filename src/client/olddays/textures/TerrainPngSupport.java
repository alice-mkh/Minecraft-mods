package net.minecraft.src;

import java.util.HashMap;
import net.minecraft.client.Minecraft;

public class TerrainPngSupport{
    public static HashMap<String, Integer> terrainIndexMap;
    public static HashMap<String, Integer> itemsIndexMap;
    private static int fillingMap = 0;
    private static boolean filledTerrain = false;
    private static boolean filledItems = false;

    public static void copyAll(){
        if (!filledTerrain){
            terrainIndexMap = new HashMap<String, Integer>();
            fillingMap = 1;
        }
        Icon[] icons = getTerrainIcons();
        for (Icon i : icons){
            if (i == null){
                continue;
            }
            mod_OldDays.texman.copyIconFromSheet(i, "/terrain.png", terrainIndexMap);
        }
        fillingMap = 0;
        if (!filledItems){
            itemsIndexMap = new HashMap<String, Integer>();
            fillingMap = 2;
        }
        icons = getItemIcons();
        for (Icon i : icons){
            if (i == null){
                continue;
            }
            mod_OldDays.texman.copyIconFromSheet(i, "/gui/items.png", itemsIndexMap);
        }
        fillingMap = 0;
    }

    private static void put(Icon[] icons, int i, String str, Icon icon){
        if (fillingMap == 1 && !terrainIndexMap.containsKey(str)){
            terrainIndexMap.put(str, i);
        }
        if (fillingMap == 2 && !itemsIndexMap.containsKey(str)){
            itemsIndexMap.put(str, i);
        }
        icons[i] = icon;
    }

    private static Icon[] getTerrainIcons(){
        Icon[] icons = new Icon[256];
        try{
            Icon[][] leavesIcons = (Icon[][])(mod_OldDays.getField(BlockLeaves.class, Block.leaves, 3));
            Icon[] doorIcons = (Icon[])(mod_OldDays.getField(BlockDoor.class, Block.doorWood, 2));
            Icon[] anvilIcons = (Icon[])(mod_OldDays.getField(BlockAnvil.class, Block.anvil, 3));
            Icon[] destroyIcons = (Icon[])(mod_OldDays.getField(RenderGlobal.class, Minecraft.getMinecraft().renderGlobal, 25));
            put(icons, 0, "grass_top", (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 0)));
            put(icons, 1, "stone", Block.stone.getIcon(0, 0));
            put(icons, 2, "dirt", Block.dirt.getIcon(0, 0));
            put(icons, 3, "grass_side", (Icon)(mod_OldDays.getField(Block.class, Block.grass, 195)));
            put(icons, 4, "wood", Block.planks.getIcon(0, 0));
            put(icons, 5, "stoneslab_side", Block.stoneSingleSlab.getIcon(2, 0));
            put(icons, 6, "stoneslab_top", Block.stoneSingleSlab.getIcon(0, 0));
            put(icons, 7, "brick", Block.brick.getIcon(0, 0));
            put(icons, 8, "tnt_side", Block.tnt.getIcon(2, 0));
            put(icons, 9, "tnt_top", Block.tnt.getIcon(1, 0));
            put(icons, 10, "tnt_bottom", Block.tnt.getIcon(0, 0));
            put(icons, 11, "web", Block.web.getIcon(0, 0));
            put(icons, 12, "rose", Block.plantRed.getIcon(0, 0));
            put(icons, 13, "flower", Block.plantYellow.getIcon(0, 0));
            put(icons, 15, "sapling", Block.sapling.getIcon(0, 0));
            put(icons, 16, "stonebrick", Block.cobblestone.getIcon(0, 0));
            put(icons, 17, "bedrock", Block.bedrock.getIcon(0, 0));
            put(icons, 18, "sand", Block.sand.getIcon(0, 0));
            put(icons, 19, "gravel", Block.gravel.getIcon(0, 0));
            put(icons, 20, "tree_side", Block.wood.getIcon(2, 0));
            put(icons, 21, "tree_top", Block.wood.getIcon(0, 0));
            put(icons, 22, "blockIron", Block.blockIron.getIcon(1, 0));
            put(icons, 23, "blockGold", Block.blockGold.getIcon(1, 0));
            put(icons, 24, "blockDiamond", Block.blockDiamond.getIcon(1, 0));
            put(icons, 25, "blockEmerald", Block.blockEmerald.getIcon(1, 0));
            put(icons, 26, "blockRedstone", Block.blockRedstone.getIcon(1, 0));
            put(icons, 28, "mushroom_red", Block.mushroomRed.getIcon(0, 0));
            put(icons, 29, "mushroom_brown", Block.mushroomBrown.getIcon(0, 0));
            put(icons, 30, "sapling_jungle", Block.sapling.getIcon(0, 3));
            put(icons, 32, "oreGold", Block.oreGold.getIcon(0, 0));
            put(icons, 33, "oreIron", Block.oreIron.getIcon(0, 0));
            put(icons, 34, "oreCoal", Block.oreCoal.getIcon(0, 0));
            put(icons, 35, "bookshelf", Block.bookShelf.getIcon(2, 0));
            put(icons, 36, "stoneMoss", Block.cobblestoneMossy.getIcon(0, 0));
            put(icons, 37, "obsidian", Block.obsidian.getIcon(0, 0));
            put(icons, 38, "grass_side_overlay", (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 2)));
            put(icons, 39, "tallgrass", Block.tallGrass.getIcon(0, 1));
            put(icons, 41, "beacon", Block.beacon.getBeaconIcon());
            put(icons, 43, "workbench_top", Block.workbench.getIcon(1, 0));
            put(icons, 44, "furnace_front", Block.furnaceIdle.getIcon(2, 2));
            put(icons, 45, "furnace_side", Block.furnaceIdle.getIcon(3, 2));
            put(icons, 46, "dispenser_front", Block.dispenser.getIcon(2, 0));
            put(icons, 48, "sponge", Block.sponge.getIcon(0, 0));
            put(icons, 49, "glass", Block.glass.getIcon(0, 0));
            put(icons, 50, "oreDiamond", Block.oreDiamond.getIcon(0, 0));
            put(icons, 51, "oreRedstone", Block.oreRedstone.getIcon(0, 0));
            put(icons, 52, "leaves", leavesIcons[0][0]);
            put(icons, 53, "leaves_opaque", leavesIcons[1][0]);
            put(icons, 54, "stonebricksmooth", Block.stoneBrick.getIcon(0, 0));
            put(icons, 55, "deadbush", Block.tallGrass.getIcon(0, 0));
            put(icons, 56, "fern", Block.tallGrass.getIcon(0, 2));
            put(icons, 59, "workbench_side", Block.workbench.getIcon(3, 0));
            put(icons, 60, "workbench_front", Block.workbench.getIcon(2, 0));
            put(icons, 61, "furnace_front_lit", Block.furnaceBurning.getIcon(2, 2));
            put(icons, 62, "furnace_top", Block.furnaceIdle.getIcon(0, 0));
            put(icons, 63, "sapling_spruce", Block.sapling.getIcon(0, 2));
            put(icons, 64, "cloth_0", Block.cloth.getIcon(0, 0));
            put(icons, 65, "mobSpawner", Block.mobSpawner.getIcon(0, 0));
            put(icons, 66, "snow", Block.blockSnow.getIcon(0, 0));
            put(icons, 67, "ice", Block.ice.getIcon(0, 0));
            put(icons, 68, "snow_side", (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 1)));
            put(icons, 69, "cactus_top", Block.cactus.getIcon(1, 0));
            put(icons, 70, "cactus_side", Block.cactus.getIcon(2, 0));
            put(icons, 71, "cactus_bottom", Block.cactus.getIcon(0, 0));
            put(icons, 72, "clay", Block.blockClay.getIcon(0, 0));
            put(icons, 73, "reeds", Block.reed.getIcon(0, 0));
            put(icons, 74, "musicBlock", Block.jukebox.getIcon(2, 0));
            put(icons, 75, "jukebox_top", Block.jukebox.getIcon(1, 0));
            put(icons, 76, "waterlily", Block.waterlily.getIcon(0, 0));
            put(icons, 77, "mycel_side", Block.mycelium.getIcon(2, 0));
            put(icons, 78, "mycel_top", Block.mycelium.getIcon(1, 0));
            put(icons, 79, "sapling_birch", Block.sapling.getIcon(0, 1));
            put(icons, 80, "torch", Block.torchWood.getIcon(0, 0));
            put(icons, 81, "doorWood_upper", doorIcons[1]);
            put(icons, 82, "doorIron_upper", doorIcons[3]);
            put(icons, 83, "ladder", Block.ladder.getIcon(0, 0));
            put(icons, 84, "trapdoor", Block.trapdoor.getIcon(0, 0));
            put(icons, 85, "fenceIron", Block.fenceIron.getIcon(0, 0));
            put(icons, 86, "farmland_wet", Block.tilledField.getIcon(1, 1));
            put(icons, 87, "farmland_dry", Block.tilledField.getIcon(1, 0));
            for (int i = 0; i < 8; i++){
                put(icons, 88 + i, "crops_" + i, Block.crops.getIcon(0, i));
            }
            put(icons, 96, "lever", Block.lever.getIcon(0, 0));
            put(icons, 97, "doorWood_lower", doorIcons[0]);
            put(icons, 98, "doorIron_lower", doorIcons[2]);
            put(icons, 99, "redtorch_lit", Block.torchRedstoneActive.getIcon(0, 0));
            put(icons, 100, "stonebricksmooth_mossy", Block.stoneBrick.getIcon(0, 1));
            put(icons, 101, "stonebricksmooth_cracked", Block.stoneBrick.getIcon(0, 2));
            put(icons, 102, "pumpkin_top", Block.pumpkin.getIcon(0, 0));
            put(icons, 103, "hellrock", Block.netherrack.getIcon(0, 0));
            put(icons, 104, "hellsand", Block.slowSand.getIcon(0, 0));
            put(icons, 105, "lightgem", Block.glowStone.getIcon(0, 0));
            put(icons, 106, "piston_top_sticky", BlockPistonBase.func_94496_b("piston_top_sticky"));
            put(icons, 107, "piston_top", BlockPistonBase.func_94496_b("piston_top"));
            put(icons, 108, "piston_side", BlockPistonBase.func_94496_b("piston_side"));
            put(icons, 109, "piston_bottom", Block.pistonBase.getIcon(1, 0));
            put(icons, 110, "piston_inner_top", BlockPistonBase.func_94496_b("piston_inner_top"));
            put(icons, 111, "stem_straight", Block.pumpkinStem.getIcon(0, 0));
            put(icons, 112, "rail_turn", Block.rail.getIcon(0, 6));
            put(icons, 113, "cloth_15", Block.cloth.getIcon(0, 15));
            put(icons, 114, "cloth_7", Block.cloth.getIcon(0, 7));
            put(icons, 115, "redtorch", Block.torchRedstoneIdle.getIcon(0, 0));
            put(icons, 116, "tree_spruce", Block.wood.getIcon(2, 1));
            put(icons, 117, "tree_birch", Block.wood.getIcon(2, 2));
            put(icons, 118, "pumpkin_side", Block.pumpkin.getIcon(2, 0));
            put(icons, 119, "pumpkin_face", Block.pumpkin.getIcon(3, 0));
            put(icons, 120, "pumpkin_jack", Block.pumpkinLantern.getIcon(3, 0));
            put(icons, 121, "cake_top", Block.cake.getIcon(1, 0));
            put(icons, 122, "cake_side", Block.cake.getIcon(2, 0));
            put(icons, 123, "cake_inner", Block.cake.getIcon(4, 1));
            put(icons, 124, "cake_bottom", Block.cake.getIcon(0, 0));
            put(icons, 125, "mushroom_skin_red", Block.mushroomCapRed.getIcon(1, 1));
            put(icons, 126, "mushroom_skin_brown", Block.mushroomCapBrown.getIcon(1, 1));
            put(icons, 127, "stem_bent", ((BlockStem)Block.pumpkinStem).func_94368_p());
            put(icons, 128, "rail", Block.rail.getIcon(0, 0));
            put(icons, 129, "cloth_14", Block.cloth.getIcon(0, 14));
            put(icons, 130, "cloth_6", Block.cloth.getIcon(0, 6));
            put(icons, 131, "repeater", Block.redstoneRepeaterIdle.getIcon(1, 0));
            put(icons, 132, "leaves_spruce", leavesIcons[0][1]);
            put(icons, 133, "leaves_spruce_opaque", leavesIcons[1][1]);
            put(icons, 134, "bed_feet_top", Block.bed.getIcon(1, 0));
            put(icons, 135, "bed_head_top", Block.bed.getIcon(1, 8));
            put(icons, 136, "melon_side", Block.melon.getIcon(2, 0));
            put(icons, 137, "melon_top", Block.melon.getIcon(1, 0));
            put(icons, 138, "cauldron_top", Block.cauldron.getIcon(1, 0));
            put(icons, 139, "cauldron_inner", BlockCauldron.func_94375_b("cauldron_inner"));
            put(icons, 140, "cake", Item.cake.getIconFromDamage(0));
            put(icons, 141, "mushroom_skin_stem", Block.mushroomCapRed.getIcon(2, 10));
            put(icons, 142, "mushroom_inside", Block.mushroomCapRed.getIcon(0, 0));
            put(icons, 143, "vine", Block.vine.getIcon(0, 0));
            put(icons, 144, "blockLapis", Block.blockLapis.getIcon(0, 0));
            put(icons, 145, "cloth_13", Block.cloth.getIcon(0, 13));
            put(icons, 146, "cloth_5", Block.cloth.getIcon(0, 5));
            put(icons, 147, "repeater_lit", Block.redstoneRepeaterActive.getIcon(1, 0));
            put(icons, 148, "thinglass_top", ((BlockPane)Block.thinGlass).getSideTextureIndex());
            put(icons, 149, "bed_feet_end", Block.bed.getIcon(2, 0));
            put(icons, 150, "bed_feet_side", Block.bed.getIcon(5, 0));
            put(icons, 151, "bed_head_side", Block.bed.getIcon(3, 8));
            put(icons, 152, "bed_head_end", Block.bed.getIcon(4, 8));
            put(icons, 153, "tree_jungle", Block.wood.getIcon(2, 3));
            put(icons, 154, "cauldron_side", Block.cauldron.getIcon(2, 0));
            put(icons, 155, "cauldron_bottom", BlockCauldron.func_94375_b("cauldron_bottom"));
            put(icons, 156, "brewingStand_base", ((BlockBrewingStand)Block.brewingStand).getBrewingStandIcon());
            put(icons, 157, "brewingStand", Block.brewingStand.getIcon(0, 0));
            put(icons, 158, "endframe_top", Block.endPortalFrame.getIcon(1, 0));
            put(icons, 159, "endframe_side", Block.endPortalFrame.getIcon(2, 0));
            put(icons, 160, "oreLapis", Block.oreLapis.getIcon(0, 0));
            put(icons, 161, "cloth_12", Block.cloth.getIcon(0, 12));
            put(icons, 162, "cloth_4", Block.cloth.getIcon(0, 4));
            put(icons, 163, "goldenRail", Block.railPowered.getIcon(0, 0));
            put(icons, 164, "redstoneDust_cross", BlockRedstoneWire.func_94409_b("redstoneDust_cross"));
            put(icons, 165, "redstoneDust_line", BlockRedstoneWire.func_94409_b("redstoneDust_line"));
            put(icons, 166, "enchantment_top", Block.enchantmentTable.getIcon(1, 0));
            put(icons, 167, "dragonEgg", Block.dragonEgg.getIcon(0, 0));
            put(icons, 168, BlockCocoa.cocoaIcons[2], ((BlockCocoa)Block.cocoaPlant).func_94468_i_(2));
            put(icons, 169, BlockCocoa.cocoaIcons[1], ((BlockCocoa)Block.cocoaPlant).func_94468_i_(1));
            put(icons, 170, BlockCocoa.cocoaIcons[0], ((BlockCocoa)Block.cocoaPlant).func_94468_i_(0));
            put(icons, 171, "oreEmerald", Block.oreEmerald.getIcon(0, 0));
            put(icons, 172, "tripWireSource", Block.tripWireSource.getIcon(0, 0));
            put(icons, 173, "tripWire", Block.tripWire.getIcon(0, 0));
            put(icons, 174, "endframe_eye", ((BlockEndPortalFrame)Block.endPortalFrame).func_94398_p());
            put(icons, 175, "whiteStone", Block.whiteStone.getIcon(0, 0));
            put(icons, 176, "sandstone_top", Block.sandStone.getIcon(1, 0));
            put(icons, 177, "cloth_11", Block.cloth.getIcon(0, 11));
            put(icons, 178, "cloth_3", Block.cloth.getIcon(0, 3));
            put(icons, 179, "goldenRail_powered", Block.railPowered.getIcon(0, 8));
            put(icons, 180, "redstoneDust_cross_overlay", BlockRedstoneWire.func_94409_b("redstoneDust_cross_overlay"));
            put(icons, 181, "redstoneDust_line_overlay", BlockRedstoneWire.func_94409_b("redstoneDust_line_overlay"));
            put(icons, 182, "enchantment_side", Block.enchantmentTable.getIcon(2, 0));
            put(icons, 183, "enchantment_bottom", Block.enchantmentTable.getIcon(0, 0));
            put(icons, 184, "commandBlock", Block.commandBlock.getIcon(0, 0));
            //FIXME: Item frame back
            put(icons, 186, "flowerPot", Block.flowerPot.getIcon(0, 0));
            put(icons, 187, "comparator", Block.redstoneComparatorIdle.getIcon(1, 0));
            put(icons, 188, "comparator_lit", Block.redstoneComparatorActive.getIcon(1, 0));
            put(icons, 191, "netherquartz", Block.oreNetherQuartz.getIcon(0, 0));
            put(icons, 192, "sandstone_side", Block.sandStone.getIcon(2, 0));
            put(icons, 193, "cloth_10", Block.cloth.getIcon(0, 10));
            put(icons, 194, "cloth_2", Block.cloth.getIcon(0, 2));
            put(icons, 195, "detectorRail", Block.railDetector.getIcon(0, 0));
            put(icons, 196, "leaves_jungle", leavesIcons[0][3]);
            put(icons, 197, "leaves_jungle_opaque", leavesIcons[1][3]);
            put(icons, 198, "wood_spruce", Block.planks.getIcon(0, 1));
            put(icons, 199, "wood_birch", Block.planks.getIcon(0, 2));
            put(icons, 200, "carrots_0", Block.carrot.getIcon(0, 1));
            put(icons, 201, "carrots_1", Block.carrot.getIcon(0, 3));
            put(icons, 202, "carrots_2", Block.carrot.getIcon(0, 5));
            put(icons, 203, "carrots_3", Block.carrot.getIcon(0, 7));
            put(icons, 200, "potatoes_0", Block.potato.getIcon(0, 1));
            put(icons, 201, "potatoes_1", Block.potato.getIcon(0, 3));
            put(icons, 202, "potatoes_2", Block.potato.getIcon(0, 5));
            put(icons, 204, "potatoes_3", Block.potato.getIcon(0, 7));
            put(icons, 208, "sandstone_bottom", Block.sandStone.getIcon(0, 0));
            put(icons, 209, "cloth_9", Block.cloth.getIcon(0, 9));
            put(icons, 210, "cloth_1", Block.cloth.getIcon(0, 1));
            put(icons, 211, "redstoneLight", Block.redstoneLampIdle.getIcon(0, 0));
            put(icons, 212, "redstoneLight_lit", Block.redstoneLampActive.getIcon(0, 0));
            put(icons, 213, "stonebricksmooth_carved", Block.stoneBrick.getIcon(0, 3));
            put(icons, 214, "wood_jungle", Block.planks.getIcon(0, 3));
            put(icons, 215, "anvil_base", Block.anvil.getIcon(0, 0));
            put(icons, 216, "anvil_top_damaged_1", anvilIcons[1]);
            put(icons, 224, "netherBrick", Block.netherBrick.getIcon(0, 0));
            put(icons, 225, "cloth_8", Block.cloth.getIcon(0, 8));
            put(icons, 226, "netherStalk_0", Block.netherStalk.getIcon(0, 0));
            put(icons, 227, "netherStalk_1", Block.netherStalk.getIcon(0, 2));
            put(icons, 228, "netherStalk_2", Block.netherStalk.getIcon(0, 4));
            put(icons, 229, "sandstone_carved", Block.sandStone.getIcon(2, 1));
            put(icons, 230, "sandstone_smooth", Block.sandStone.getIcon(2, 2));
            put(icons, 231, "anvil_top", anvilIcons[0]);
            put(icons, 232, "anvil_top_damaged_2", anvilIcons[2]);
            for (int i = 0; i < 10; i++){
                put(icons, 240 + i, "destroy_" + i, destroyIcons[i]);
            }
            if (fillingMap > 0){
                filledTerrain = true;
            }
        }catch(Exception e){}
        return icons;
    }

    private static Icon[] getItemIcons(){
        Icon[] icons = new Icon[256];
        try{
            put(icons, 0, "helmetCloth", Item.helmetLeather.getIconFromDamage(0));
            put(icons, 1, "helmetChain", Item.helmetChain.getIconFromDamage(0));
            put(icons, 2, "helmetIron", Item.helmetIron.getIconFromDamage(0));
            put(icons, 3, "helmetDiamond", Item.helmetDiamond.getIconFromDamage(0));
            put(icons, 4, "helmetGold", Item.helmetGold.getIconFromDamage(0));
            put(icons, 5, "flintAndSteel", Item.flintAndSteel.getIconFromDamage(0));
            put(icons, 6, "flint", Item.flint.getIconFromDamage(0));
            put(icons, 7, "coal", Item.coal.getIconFromDamage(0));
            put(icons, 10, "apple", Item.appleRed.getIconFromDamage(0));
            put(icons, 11, "appleGold", Item.appleGold.getIconFromDamage(0));
            if (fillingMap > 0){
                filledItems = true;
            }
        }catch(Exception e){}
        return icons;
    }
}