package net.minecraft.src;

import java.util.HashMap;

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

    private static Icon getIcon(int i, String str, Icon icon){
        if (fillingMap == 1 && !terrainIndexMap.containsKey(str)){
            terrainIndexMap.put(str, i);
        }
        if (fillingMap == 2 && !itemsIndexMap.containsKey(str)){
            itemsIndexMap.put(str, i);
        }
        return icon;
    }

    private static Icon[] getTerrainIcons(){
        Icon[] icons = new Icon[256];
        try{
            Icon[][] leavesIcons = (Icon[][])(mod_OldDays.getField(BlockLeaves.class, Block.leaves, 3));
            Icon[] doorIcons = (Icon[])(mod_OldDays.getField(BlockDoor.class, Block.doorWood, 2));

            icons[0] = getIcon(0, "grass_top", (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 0)));
            icons[1] = getIcon(1, "stone", Block.stone.getIcon(0, 0));
            icons[2] = getIcon(2, "dirt", Block.dirt.getIcon(0, 0));
            icons[3] = getIcon(3, "grass_side", (Icon)(mod_OldDays.getField(Block.class, Block.grass, 195)));
            icons[4] = getIcon(4, "wood", Block.planks.getIcon(0, 0));
            icons[5] = getIcon(5, "stoneslab_side", Block.stoneSingleSlab.getIcon(2, 0));
            icons[6] = getIcon(6, "stoneslab_top", Block.stoneSingleSlab.getIcon(0, 0));
            icons[7] = getIcon(7, "brick", Block.brick.getIcon(0, 0));
            icons[8] = getIcon(8, "tnt_side", Block.tnt.getIcon(2, 0));
            icons[9] = getIcon(9, "tnt_top", Block.tnt.getIcon(1, 0));
            icons[10] = getIcon(10, "tnt_bottom", Block.tnt.getIcon(0, 0));
            icons[11] = getIcon(11, "web", Block.web.getIcon(0, 0));
            icons[12] = getIcon(12, "rose", Block.plantRed.getIcon(0, 0));
            icons[13] = getIcon(13, "flower", Block.plantYellow.getIcon(0, 0));
            //14 is animated
            icons[15] = getIcon(15, "sapling", Block.sapling.getIcon(0, 0));
            icons[16] = getIcon(16, "stonebrick", Block.cobblestone.getIcon(0, 0));
            icons[17] = getIcon(17, "bedrock", Block.bedrock.getIcon(0, 0));
            icons[18] = getIcon(18, "sand", Block.sand.getIcon(0, 0));
            icons[19] = getIcon(19, "gravel", Block.gravel.getIcon(0, 0));
            icons[20] = getIcon(20, "tree_side", Block.wood.getIcon(2, 0));
            icons[21] = getIcon(21, "tree_top", Block.wood.getIcon(0, 0));
            icons[22] = getIcon(22, "blockIron", Block.blockIron.getIcon(1, 0));
            icons[23] = getIcon(23, "blockGold", Block.blockGold.getIcon(1, 0));
            icons[24] = getIcon(24, "blockDiamond", Block.blockDiamond.getIcon(1, 0));
            icons[25] = getIcon(25, "blockEmerald", Block.blockEmerald.getIcon(1, 0));
            icons[26] = getIcon(26, "blockRedstone", Block.blockRedstone.getIcon(1, 0));
            icons[28] = getIcon(28, "mushroom_red", Block.mushroomRed.getIcon(0, 0));
            icons[29] = getIcon(29, "mushroom_brown", Block.mushroomBrown.getIcon(0, 0));
            icons[30] = getIcon(30, "sapling_jungle", Block.sapling.getIcon(0, 3));
            //31 is animated
            icons[32] = getIcon(32, "oreGold", Block.oreGold.getIcon(0, 0));
            icons[33] = getIcon(33, "oreIron", Block.oreIron.getIcon(0, 0));
            icons[34] = getIcon(34, "oreCoal", Block.oreCoal.getIcon(0, 0));
            icons[35] = getIcon(35, "bookshelf", Block.bookShelf.getIcon(2, 0));
            icons[36] = getIcon(36, "stoneMoss", Block.cobblestoneMossy.getIcon(0, 0));
            icons[37] = getIcon(37, "obsidian", Block.obsidian.getIcon(0, 0));
            icons[38] = getIcon(38, "grass_side_overlay", (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 2)));
            icons[39] = getIcon(39, "tallgrass", Block.tallGrass.getIcon(0, 1));
            //40 is unused
            icons[41] = getIcon(41, "beacon", Block.beacon.getBeaconIcon());
            icons[43] = getIcon(43, "workbench_top", Block.workbench.getIcon(1, 0));
            icons[44] = getIcon(44, "furnace_front", Block.furnaceIdle.getIcon(2, 2));
            icons[45] = getIcon(45, "furnace_side", Block.furnaceIdle.getIcon(3, 2));
            icons[46] = getIcon(46, "dispenser_front", Block.dispenser.getIcon(2, 0));
            //47 is animated
            icons[48] = getIcon(48, "sponge", Block.sponge.getIcon(0, 0));
            icons[49] = getIcon(49, "glass", Block.glass.getIcon(0, 0));
            icons[50] = getIcon(50, "oreDiamond", Block.oreDiamond.getIcon(0, 0));
            icons[51] = getIcon(51, "oreRedstone", Block.oreRedstone.getIcon(0, 0));
            icons[52] = getIcon(52, "leaves", leavesIcons[0][0]);
            icons[53] = getIcon(53, "leaves_opaque", leavesIcons[1][0]);
            icons[54] = getIcon(54, "stonebricksmooth", Block.stoneBrick.getIcon(0, 0));
            icons[55] = getIcon(55, "deadbush", Block.tallGrass.getIcon(0, 0));
            icons[56] = getIcon(56, "fern", Block.tallGrass.getIcon(0, 2));
            icons[59] = getIcon(59, "workbench_side", Block.workbench.getIcon(3, 0));
            icons[60] = getIcon(60, "workbench_front", Block.workbench.getIcon(2, 0));
            icons[61] = getIcon(61, "furnace_front_lit", Block.furnaceBurning.getIcon(2, 2));
            icons[62] = getIcon(62, "furnace_top", Block.furnaceIdle.getIcon(0, 0));
            icons[63] = getIcon(63, "sapling_spruce", Block.sapling.getIcon(0, 2));
            icons[64] = getIcon(64, "cloth_0", Block.cloth.getIcon(0, 0));
            icons[65] = getIcon(65, "mobSpawner", Block.mobSpawner.getIcon(0, 0));
            icons[66] = getIcon(66, "snow", Block.blockSnow.getIcon(0, 0));
            icons[67] = getIcon(67, "ice", Block.ice.getIcon(0, 0));
            icons[68] = getIcon(68, "snow_side", (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 3)));
            icons[69] = getIcon(69, "cactus_top", Block.cactus.getIcon(1, 0));
            icons[70] = getIcon(70, "cactus_side", Block.cactus.getIcon(2, 0));
            icons[71] = getIcon(71, "cactus_bottom", Block.cactus.getIcon(0, 0));
            icons[72] = getIcon(72, "clay", Block.blockClay.getIcon(0, 0));
            icons[73] = getIcon(73, "reeds", Block.reed.getIcon(0, 0));
            icons[74] = getIcon(74, "musicBlock", Block.jukebox.getIcon(2, 0));
            icons[75] = getIcon(75, "jukebox_top", Block.jukebox.getIcon(1, 0));
            icons[76] = getIcon(76, "waterlily", Block.waterlily.getIcon(0, 0));
            icons[77] = getIcon(77, "mycel_side", Block.mycelium.getIcon(2, 0));
            icons[78] = getIcon(78, "mycel_top", Block.mycelium.getIcon(1, 0));
            icons[79] = getIcon(79, "sapling_birch", Block.sapling.getIcon(0, 1));
            icons[80] = getIcon(80, "torch", Block.torchWood.getIcon(0, 0));
            icons[81] = getIcon(81, "doorWood_upper", doorIcons[1]);
            icons[82] = getIcon(82, "doorIron_upper", doorIcons[3]);
            icons[83] = getIcon(83, "ladder", Block.ladder.getIcon(0, 0));
            icons[84] = getIcon(84, "trapdoor", Block.trapdoor.getIcon(0, 0));
            icons[85] = getIcon(85, "fenceIron", Block.fenceIron.getIcon(0, 0));
            icons[86] = getIcon(86, "farmland_wet", Block.tilledField.getIcon(1, 1));
            icons[87] = getIcon(87, "farmland_dry", Block.tilledField.getIcon(1, 0));
            for (int i = 0; i < 8; i++){
                icons[88 + i] = getIcon(88 + i, "crops_" + i, Block.crops.getIcon(0, i));
            }
            icons[96] = getIcon(96, "lever", Block.lever.getIcon(0, 0));
            icons[97] = getIcon(97, "doorWood_lower", doorIcons[0]);
            icons[98] = getIcon(98, "doorIron_lower", doorIcons[2]);
            icons[99] = getIcon(99, "redtorch_lit", Block.torchRedstoneActive.getIcon(0, 0));
            icons[100] = getIcon(100, "stonebricksmooth_mossy", Block.stoneBrick.getIcon(0, 1));
            icons[101] = getIcon(101, "stonebricksmooth_cracked", Block.stoneBrick.getIcon(0, 2));
            icons[102] = getIcon(102, "pumpkin_top", Block.pumpkin.getIcon(0, 0));
            icons[103] = getIcon(103, "hellrock", Block.netherrack.getIcon(0, 0));
            icons[104] = getIcon(104, "hellsand", Block.slowSand.getIcon(0, 0));
            icons[105] = getIcon(105, "lightgem", Block.glowStone.getIcon(0, 0));
            icons[106] = getIcon(106, "piston_top_sticky", BlockPistonBase.func_94496_b("piston_top_sticky"));
            icons[107] = getIcon(107, "piston_top", BlockPistonBase.func_94496_b("piston_top"));
            icons[108] = getIcon(108, "piston_side", BlockPistonBase.func_94496_b("piston_side"));
//             icons[109] = getIcon(109, "piston_bottom", Block.pistonBase.getIcon(5, 0));
            icons[110] = getIcon(110, "piston_inner_top", BlockPistonBase.func_94496_b("piston_inner_top"));
            icons[111] = getIcon(111, "stem_straight", Block.pumpkinStem.getIcon(0, 0));
            icons[112] = getIcon(112, "rail_turn", Block.rail.getIcon(0, 6));
            icons[113] = getIcon(113, "cloth_15", Block.cloth.getIcon(0, 15));
            icons[114] = getIcon(114, "cloth_7", Block.cloth.getIcon(0, 7));
            icons[115] = getIcon(115, "redtorch", Block.torchRedstoneIdle.getIcon(0, 0));
            icons[116] = getIcon(116, "tree_spruce", Block.wood.getIcon(2, 1));
            icons[117] = getIcon(117, "tree_birch", Block.wood.getIcon(2, 2));
            icons[118] = getIcon(118, "pumpkin_side", Block.pumpkin.getIcon(2, 0));
            icons[119] = getIcon(119, "pumpkin_face", Block.pumpkin.getIcon(3, 0));
            icons[120] = getIcon(120, "pumpkin_jack", Block.pumpkinLantern.getIcon(3, 0));
            icons[121] = getIcon(121, "cake_top", Block.cake.getIcon(1, 0));
            icons[122] = getIcon(122, "cake_side", Block.cake.getIcon(2, 0));
            icons[123] = getIcon(123, "cake_inner", Block.cake.getIcon(4, 1));
            icons[124] = getIcon(124, "cake_bottom", Block.cake.getIcon(0, 0));
            icons[125] = getIcon(125, "mushroom_skin_red", Block.mushroomCapRed.getIcon(1, 1));
            icons[126] = getIcon(126, "mushroom_skin_brown", Block.mushroomCapBrown.getIcon(1, 1));
            icons[127] = getIcon(127, "stem_bent", ((BlockStem)Block.pumpkinStem).func_94368_p());
            icons[128] = getIcon(128, "rail", Block.rail.getIcon(0, 0));
            icons[129] = getIcon(129, "cloth_14", Block.cloth.getIcon(0, 14));
            icons[130] = getIcon(130, "cloth_6", Block.cloth.getIcon(0, 6));
            icons[131] = getIcon(131, "repeater", Block.redstoneRepeaterIdle.getIcon(1, 0));
            icons[132] = getIcon(132, "leaves_spruce", leavesIcons[0][1]);
            icons[133] = getIcon(133, "leaves_spruce_opaque", leavesIcons[1][1]);
            icons[134] = getIcon(134, "bed_feet_top", Block.bed.getIcon(1, 0));
            icons[135] = getIcon(135, "bed_head_top", Block.bed.getIcon(1, 8));
            icons[136] = getIcon(136, "melon_side", Block.melon.getIcon(2, 0));
            icons[137] = getIcon(137, "melon_top", Block.melon.getIcon(1, 0));
            icons[138] = getIcon(138, "cauldron_top", Block.cauldron.getIcon(1, 0));
            icons[139] = getIcon(139, "cauldron_inner", BlockCauldron.func_94375_b("cauldron_inner"));
            icons[140] = getIcon(140, "cake", Item.cake.getIconFromDamage(0));
            icons[141] = getIcon(141, "mushroom_skin_stem", Block.mushroomCapRed.getIcon(2, 10));
            icons[142] = getIcon(142, "mushroom_inside", Block.mushroomCapRed.getIcon(0, 0));
            icons[143] = getIcon(143, "vine", Block.vine.getIcon(0, 0));
            icons[144] = getIcon(144, "blockLapis", Block.blockLapis.getIcon(0, 0));
            icons[145] = getIcon(145, "cloth_13", Block.cloth.getIcon(0, 13));
            icons[146] = getIcon(146, "cloth_5", Block.cloth.getIcon(0, 5));
            icons[147] = getIcon(147, "repeater_lit", Block.redstoneRepeaterActive.getIcon(1, 0));
            icons[148] = getIcon(148, "thinglass_top", ((BlockPane)Block.thinGlass).getSideTextureIndex());
            icons[149] = getIcon(149, "bed_feet_end", Block.bed.getIcon(2, 0));
            icons[150] = getIcon(150, "bed_feet_side", Block.bed.getIcon(5, 0));
            icons[151] = getIcon(151, "bed_head_side", Block.bed.getIcon(3, 8));
            icons[152] = getIcon(152, "bed_head_end", Block.bed.getIcon(4, 8));
            icons[153] = getIcon(153, "tree_jungle", Block.wood.getIcon(2, 3));
            icons[154] = getIcon(154, "cauldron_side", Block.cauldron.getIcon(2, 0));
            icons[155] = getIcon(155, "cauldron_bottom", BlockCauldron.func_94375_b("cauldron_bottom"));
            icons[156] = getIcon(156, "brewingStand_base", ((BlockBrewingStand)Block.brewingStand).getBrewingStandIcon());
            icons[157] = getIcon(157, "brewingStand", Block.brewingStand.getIcon(0, 0));
            icons[158] = getIcon(158, "endframe_top", Block.endPortalFrame.getIcon(1, 0));
            icons[159] = getIcon(159, "endframe_side", Block.endPortalFrame.getIcon(2, 0));
            icons[160] = getIcon(160, "oreLapis", Block.oreLapis.getIcon(0, 0));
            icons[161] = getIcon(161, "cloth_12", Block.cloth.getIcon(0, 12));
            icons[162] = getIcon(162, "cloth_4", Block.cloth.getIcon(0, 4));
            icons[163] = getIcon(163, "goldenRail", Block.railPowered.getIcon(0, 0));
            icons[164] = getIcon(164, "redstoneDust_cross", BlockRedstoneWire.func_94409_b("redstoneDust_cross"));
            icons[165] = getIcon(165, "redstoneDust_line", BlockRedstoneWire.func_94409_b("redstoneDust_line"));
            icons[166] = getIcon(166, "enchantment_top", Block.enchantmentTable.getIcon(1, 0));
            icons[167] = getIcon(167, "dragonEgg", Block.dragonEgg.getIcon(0, 0));
            icons[168] = getIcon(168, BlockCocoa.cocoaIcons[2], ((BlockCocoa)Block.cocoaPlant).func_94468_i_(2));
            icons[169] = getIcon(169, BlockCocoa.cocoaIcons[1], ((BlockCocoa)Block.cocoaPlant).func_94468_i_(1));
            icons[170] = getIcon(170, BlockCocoa.cocoaIcons[0], ((BlockCocoa)Block.cocoaPlant).func_94468_i_(0));
            icons[171] = getIcon(171, "oreEmerald", Block.oreEmerald.getIcon(0, 0));
            icons[172] = getIcon(172, "tripWireSource", Block.tripWireSource.getIcon(0, 0));
            icons[173] = getIcon(173, "tripWire", Block.tripWire.getIcon(0, 0));
            icons[174] = getIcon(174, "endframe_eye", ((BlockEndPortalFrame)Block.endPortalFrame).func_94398_p());
            icons[175] = getIcon(175, "whiteStone", Block.whiteStone.getIcon(0, 0));

            icons[177] = getIcon(177, "cloth_11", Block.cloth.getIcon(0, 11));
            icons[178] = getIcon(178, "cloth_3", Block.cloth.getIcon(0, 3));
            icons[179] = getIcon(179, "goldenRail_powered", Block.railPowered.getIcon(0, 8));
            icons[180] = getIcon(180, "redstoneDust_cross_overlay", BlockRedstoneWire.func_94409_b("redstoneDust_cross_overlay"));
            icons[181] = getIcon(181, "redstoneDust_line_overlay", BlockRedstoneWire.func_94409_b("redstoneDust_line_overlay"));
            icons[182] = getIcon(182, "enchantment_side", Block.enchantmentTable.getIcon(2, 0));
            icons[183] = getIcon(183, "enchantment_bottom", Block.enchantmentTable.getIcon(0, 0));
            if (fillingMap > 0){
                filledTerrain = true;
            }
        }catch(Exception e){}
        return icons;
    }

    private static Icon[] getItemIcons(){
        Icon[] icons = new Icon[256];
        try{
            if (fillingMap > 0){
                filledItems = true;
            }
        }catch(Exception e){}
        return icons;
    }
}