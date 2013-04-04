package net.minecraft.src;

import java.util.HashMap;

public class ThreadTextures extends Thread{
    private mod_OldDays core;

    public ThreadTextures(mod_OldDays od){
        core = od;
    }

    private Icon[] getItemIcons(){
        Icon[] icons = new Icon[256];
        try{
        }catch(Exception e){}
        return icons;
    }

    private Icon[] getTerrainIcons(){
        Icon[] icons = new Icon[256];
        try{
            Icon[][] leavesIcons = (Icon[][])(mod_OldDays.getField(BlockLeaves.class, Block.leaves, 3));
            icons[0] = (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 0));
            icons[1] = Block.stone.getBlockTextureFromSide(0);
            icons[2] = Block.dirt.getBlockTextureFromSide(0);
            icons[3] = (Icon)(mod_OldDays.getField(Block.class, Block.grass, 195));
            icons[4] = Block.planks.getBlockTextureFromSide(0);
            icons[5] = Block.stoneSingleSlab.getBlockTextureFromSideAndMetadata(2, 0);
            icons[6] = Block.stoneSingleSlab.getBlockTextureFromSideAndMetadata(0, 0);
            icons[7] = Block.brick.getBlockTextureFromSide(0);
            icons[8] = Block.tnt.getBlockTextureFromSide(2);
            icons[9] = Block.tnt.getBlockTextureFromSide(1);
            icons[10] = Block.tnt.getBlockTextureFromSide(0);
            icons[11] = Block.web.getBlockTextureFromSide(0);
            icons[12] = Block.plantRed.getBlockTextureFromSide(0);
            icons[13] = Block.plantYellow.getBlockTextureFromSide(0);
            //14 is animated
            icons[15] = Block.sapling.getBlockTextureFromSide(0);
            icons[16] = Block.cobblestone.getBlockTextureFromSide(0);
            icons[17] = Block.bedrock.getBlockTextureFromSide(0);
            icons[18] = Block.sand.getBlockTextureFromSide(0);
            icons[19] = Block.gravel.getBlockTextureFromSide(0);
            icons[20] = Block.wood.getBlockTextureFromSideAndMetadata(2, 0);
            icons[21] = Block.wood.getBlockTextureFromSideAndMetadata(0, 0);
            icons[22] = Block.blockSteel.getBlockTextureFromSide(1);
            icons[23] = Block.blockGold.getBlockTextureFromSide(1);
            icons[24] = Block.blockDiamond.getBlockTextureFromSide(1);
            icons[25] = Block.blockEmerald.getBlockTextureFromSide(1);
            icons[28] = Block.mushroomRed.getBlockTextureFromSide(0);
            icons[29] = Block.mushroomBrown.getBlockTextureFromSide(0);
            icons[30] = Block.sapling.getBlockTextureFromSideAndMetadata(0, 3);
            //31 is animated
            icons[32] = Block.oreGold.getBlockTextureFromSide(0);
            icons[33] = Block.oreIron.getBlockTextureFromSide(0);
            icons[34] = Block.oreCoal.getBlockTextureFromSide(0);
            icons[35] = Block.bookShelf.getBlockTextureFromSide(2);
            icons[36] = Block.cobblestoneMossy.getBlockTextureFromSide(0);
            icons[37] = Block.obsidian.getBlockTextureFromSide(0);
            icons[38] = (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 2));
            icons[39] = Block.tallGrass.getBlockTextureFromSideAndMetadata(0, 1);
            //40 is unused
            icons[41] = Block.beacon.func_94446_i();
            icons[43] = Block.workbench.getBlockTextureFromSide(1);
            icons[44] = Block.furnaceIdle.getBlockTextureFromSideAndMetadata(2, 2);
            icons[45] = Block.furnaceIdle.getBlockTextureFromSideAndMetadata(3, 2);
            icons[46] = Block.dispenser.getBlockTextureFromSide(2);
            //47 is animated
            icons[48] = Block.sponge.getBlockTextureFromSide(0);
            icons[49] = Block.glass.getBlockTextureFromSide(0);
            icons[50] = Block.oreDiamond.getBlockTextureFromSide(0);
            icons[51] = Block.oreRedstone.getBlockTextureFromSide(0);
            icons[52] = leavesIcons[0][0];
            icons[53] = leavesIcons[1][0];
            icons[54] = Block.stoneBrick.getBlockTextureFromSide(0);
            icons[55] = Block.tallGrass.getBlockTextureFromSideAndMetadata(0, 0);
            icons[56] = Block.tallGrass.getBlockTextureFromSideAndMetadata(0, 2);
            icons[59] = Block.workbench.getBlockTextureFromSide(3);
            icons[60] = Block.workbench.getBlockTextureFromSide(2);
            icons[61] = Block.furnaceBurning.getBlockTextureFromSideAndMetadata(2, 2);
            icons[62] = Block.furnaceIdle.getBlockTextureFromSide(0);
            icons[63] = Block.sapling.getBlockTextureFromSideAndMetadata(0, 2);
            icons[64] = Block.cloth.getBlockTextureFromSideAndMetadata(0, 0);
            icons[65] = Block.mobSpawner.getBlockTextureFromSide(0);
            icons[66] = Block.blockSnow.getBlockTextureFromSide(0);
            icons[67] = Block.ice.getBlockTextureFromSide(0);
            icons[68] = (Icon)(mod_OldDays.getField(BlockGrass.class, Block.grass, 3));
            icons[69] = Block.cactus.getBlockTextureFromSide(1);
            icons[70] = Block.cactus.getBlockTextureFromSide(2);
            icons[71] = Block.cactus.getBlockTextureFromSide(0);
            icons[72] = Block.blockClay.getBlockTextureFromSide(0);
            icons[73] = Block.reed.getBlockTextureFromSide(0);

            icons[79] = Block.sapling.getBlockTextureFromSideAndMetadata(0, 1);

            icons[116] = Block.wood.getBlockTextureFromSideAndMetadata(2, 1);
            icons[117] = Block.wood.getBlockTextureFromSideAndMetadata(2, 2);

            icons[153] = Block.wood.getBlockTextureFromSideAndMetadata(2, 3);
        }catch(Exception e){}
        return icons;
    }

    @Override
    public void run(){
        Icon[] icons = getTerrainIcons();
        for (Icon i : icons){
            if (i == null){
                continue;
            }
            mod_OldDays.texman.copyIconFromSheet(i, "/terrain.png", terrainIndexMap);
        }
        icons = getItemIcons();
        for (Icon i : icons){
            if (i == null){
                continue;
            }
            mod_OldDays.texman.copyIconFromSheet(i, "/gui/items.png", itemsIndexMap);
        }
    }

    public static final HashMap<String, Integer> terrainIndexMap;
    public static final HashMap<String, Integer> itemsIndexMap;

    static{
        terrainIndexMap = new HashMap<String, Integer>();
        terrainIndexMap.put("grass_top", 0);
        terrainIndexMap.put("stone", 1);
        terrainIndexMap.put("dirt", 2);
        terrainIndexMap.put("grass_side", 3);
        terrainIndexMap.put("wood", 4);
        terrainIndexMap.put("stoneslab_side", 5);
        terrainIndexMap.put("stoneslab_top", 6);
        terrainIndexMap.put("brick", 7);
        terrainIndexMap.put("tnt_side", 8);
        terrainIndexMap.put("tnt_top", 9);
        terrainIndexMap.put("tnt_bottom", 10);
        terrainIndexMap.put("web", 11);
        terrainIndexMap.put("rose", 12);
        terrainIndexMap.put("flower", 13);
        //14 is animated
        terrainIndexMap.put("sapling", 15);
        terrainIndexMap.put("stonebrick", 16);
        terrainIndexMap.put("bedrock", 17);
        terrainIndexMap.put("sand", 18);
        terrainIndexMap.put("gravel", 19);
        terrainIndexMap.put("tree_side", 20);
        terrainIndexMap.put("tree_top", 21);
        terrainIndexMap.put("blockIron", 22);
        terrainIndexMap.put("blockGold", 23);
        terrainIndexMap.put("blockDiamond", 24);
        terrainIndexMap.put("blockEmerald", 25);
        terrainIndexMap.put("mushroom_red", 28);
        terrainIndexMap.put("mushroom_brown", 29);
        terrainIndexMap.put("sapling_jungle", 30);
        //31 is animated
        terrainIndexMap.put("oreGold", 32);
        terrainIndexMap.put("oreIron", 33);
        terrainIndexMap.put("oreCoal", 34);
        terrainIndexMap.put("bookshelf", 35);
        terrainIndexMap.put("stoneMoss", 36);
        terrainIndexMap.put("obsidian", 37);
        terrainIndexMap.put("grass_side_overlay", 38);
        terrainIndexMap.put("tallgrass", 39);
        //40 is unused
        terrainIndexMap.put("beacon", 41);
        terrainIndexMap.put("workbench_top", 43);
        terrainIndexMap.put("furnace_front", 44);
        terrainIndexMap.put("furnace_side", 45);
        terrainIndexMap.put("dispenser_front", 46);
        //47 is animated
        terrainIndexMap.put("sponge", 48);
        terrainIndexMap.put("glass", 49);
        terrainIndexMap.put("oreDiamond", 50);
        terrainIndexMap.put("oreRedstone", 51);
        terrainIndexMap.put("leaves", 52);
        terrainIndexMap.put("leaves_opaque", 53);
        terrainIndexMap.put("stonebricksmooth", 54);
        terrainIndexMap.put("deadbush", 55);
        terrainIndexMap.put("fern", 56);
        terrainIndexMap.put("workbench_side", 59);
        terrainIndexMap.put("workbench_front", 60);
        terrainIndexMap.put("furnace_front_lit", 61);
        terrainIndexMap.put("furnace_top", 62);
        terrainIndexMap.put("sapling_spruce", 63);
        terrainIndexMap.put("cloth_0", 64);
        terrainIndexMap.put("mobSpawner", 65);
        terrainIndexMap.put("snow", 66);
        terrainIndexMap.put("ice", 67);
        terrainIndexMap.put("snow_side", 68);
        terrainIndexMap.put("cactus_top", 69);
        terrainIndexMap.put("cactus_side", 70);
        terrainIndexMap.put("cactus_bottom", 71);
        terrainIndexMap.put("clay", 72);
        terrainIndexMap.put("reeds", 73);

        terrainIndexMap.put("sapling_birch", 79);

        terrainIndexMap.put("tree_spruce", 116);
        terrainIndexMap.put("tree_birch", 117);

        terrainIndexMap.put("tree_jungle", 153);

        itemsIndexMap = new HashMap<String, Integer>();
    }
}