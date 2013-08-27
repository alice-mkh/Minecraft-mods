#!/bin/bash

DIR=`pwd`/reobf
#~/.minecraft/mcp1.3.2/reobf
ORIG_DIR=$DIR/minecraft
ORIG_DIR_SERVER=$DIR/minecraft_server
ADDITIONAL_DIR=$DIR/additional
RESULT_DIR=$DIR/result
RESULT_DIR_2=$DIR/result2

find()
{
    for F in *.class; do
        EXPR="^.?$1.java"
        if [ -n "`strings $F | grep -E $EXPR`" ]; then
            echo ${F%.class}
            exit 0
        fi
    done
    echo "No_Such_Class!"
    exit 0
}

#MODS_LIST_SERVER="nbxlite old-days old-days-actions old-days-bugs old-days-gameplay old-days-mobs old-days-crafting old-days-allin1"

cd $DIR/..
sh recompile.sh
sh reobfuscate.sh

cd $ORIG_DIR
MODS_LIST="old-days-actions
           old-days-bugs
           old-days-gameplay
           old-days-mobs
           old-days-eyecandy
           old-days-sounds
           old-days-crafting
           old-days-textures
           old-days-nbxlite
           old-days
           old-days-allin1
           spawn-human
           spc
           ssp"
UNUSED[0]="spc_tester
           spc_WorldEditGUI
           spc_aprilfools1
           spc_AprilFools2012
           spc_Elevators
           Group
           Group\$1
           Group\$GroupBlock
           RenderGroup"
CP[1]="`find BlockFire`
       `find BlockFlowing`
       `find BlockMushroom`
       `find BlockLeaves`
       `find BlockSnow`
       `find BlockStairs`
       `find EntityBoat`
       `find EntityItem`
       `find EntitySheep`
       `find ItemDye`
       BlockFarmlandOld
       BlockFence2
       BlockLog2
       BlockTNT2
       EntityAIEatGrass2
       EntityTNTPrimed2
       ItemAxe2
       ItemPickaxe2
       ODActions"
CP[2]="`find BlockPistonBase`
       `find ContainerPlayer`
       `find EntityBoat`
       `find EntityLivingBase`
       `find EntityMinecart`
       ODBugs"
CP[3]="`find EntityArrow`
       `find EntityLivingBase`
       `find EntityPlayer`
       `find EntityXPOrb`
       `find EntityZombie`
       `find FoodStats`
       `find ItemBow`
       `find ItemFood`
       BlockCake2
       MovementInputFromOptionsCustom
       ODGameplay"
ADD[3]="olddays/icons.png"
CP[4]="`find EntityAIPanic`
       `find EntityAITarget`
       `find EntityCreature`
       `find EntityCreeper`
       `find EntityEnderman`
       `find EntityLiving`
       `find EntityLivingBase`
       `find EntitySlime`
       `find EntitySkeleton`
       `find EntitySnowman`
       `find EntitySheep`
       `find EntitySpider`
       `find EntityPig`
       `find EntityWaterMob`
       `find EntityWolf`
       `find EntityZombie`
       `find PathFinder`
       EntityAIArrowAttack2
       EntityAIEatGrass2
       EntityRana
       EntitySteve
       EntitySteve\$RenderMD3Steve
       MD3Frame
       MD3Loader
       MD3Model
       MD3Renderer
       MD3Shader
       MD3Surface
       MD3Tag
       ODMobs
       PathEntityIndev
       PathFinderIndev
       PathIndev
       PathPointIndev
       RenderMD3"
ADD[4]="olddays/md3"
CP[5]="`find EntityDiggingFX`
       `find EntityDropParticleFX`
       `find EntityEnderman`
       `find EntityLivingBase`
       `find EntityPlayer`
       `find EntitySkeleton`
       `find EntitySuspendFX`
       `find EntityZombie`
       `find GuiMainMenu`
       `find ItemRenderer`
       `find LoadingScreenRenderer`
       `find ModelBiped`
       `find RendererLivingEntity`
       `find RenderHelper`
       `find RenderItem`
       `find TileEntityChestRenderer`
       `find TileEntityEnderChestRenderer`
       BlockChestOld
       BlockFence2
       BlockRedstoneWireOld
       EntityAIArrowAttack2
       GuiContainerCreativeOld
       ODEyecandy
       OldContainerCreative
       RenderItemFrame2
       LogoEffectRandomizer
       ModelMobArmor
       RenderEnderman2
       RenderMinecart2
       RenderPlayer2
       RenderSkeleton2
       RenderSnowMan2
       RenderTntMinecart2
       RenderZombie2"
ADD[5]="olddays/allitems.png
        olddays/black.png
        olddays/enderman_eyes.png
        olddays/plate.png
        textures/blocks/olddays_chest_top.png
        textures/blocks/olddays_chest_side.png
        textures/blocks/olddays_chest_back_left.png
        textures/blocks/olddays_chest_back_right.png
        textures/blocks/olddays_chest_front.png
        textures/blocks/olddays_chest_front_left.png
        textures/blocks/olddays_chest_front_right.png
        textures/blocks/olddays_chest_front_left_trap.png
        textures/blocks/olddays_chest_front_right_trap.png
        textures/blocks/olddays_chest_front_trap.png"
CP[6]="ODSounds
       SoundManager2"
ADD[6]="olddays/sounds"
CP[7]="ODCrafting"
CP[8]="BlockOreStorageOld
       ItemCoalOld
       ODTextures
       TextureCompassFX
       TextureFlamesFX
       TextureGearFX
       TextureLavaFX
       TextureLavaFlowFX
       TexturePortalFX
       TextureWatchFX
       TextureWaterFX
       TextureWaterFlowFX"
ADD[8]="olddays/char.png
        olddays/cloth_1.png
        olddays/cloth_2.png
        olddays/cloth_empty.png
        olddays/clock.png
        olddays/compass.png
        olddays/dial.png
        olddays/explosion.png
        olddays/mojang.png
        olddays/mojang2.png
        olddays/moon_phases.png
        olddays/pig.png
        olddays/slime.png
        olddays/textures.png
        textures/blocks/olddays_diamond_bottom.png
        textures/blocks/olddays_diamond_side.png
        textures/blocks/olddays_gold_bottom.png
        textures/blocks/olddays_gold_side.png
        textures/blocks/olddays_iron_bottom.png
        textures/blocks/olddays_iron_side.png"
CP[9]="`find BiomeGenBase`
       `find BlockFluid`
       `find BlockGrass`
       `find BlockSapling`
       `find ChestItemRenderHelper`
       `find Chunk`
       `find ChunkCache`
       `find ChunkProviderHell`
       `find ComponentNetherBridgeCorridor`
       `find ComponentNetherBridgeCorridor2`
       `find ComponentStrongholdStairs2`
       `find ComponentVillage`
       `find ComponentVillageField`
       `find ComponentVillageField2`
       `find EntityAIMate`
       `find EntityAnimal`
       `find EntityBreakingFX`
       `find EntityDiggingFX`
       `find EntityFX`
       `find EntityPickupFX`
       `find EntityWolf`
       `find ItemEnchantedBook`
       `find ItemRenderer`
       `find GenLayer`
       `find RenderBiped`
       `find RenderItem`
       `find RenderManager`
       `find StructureMineshaftPieces`
       `find StructureStrongholdPieces`
       `find TileEntityChestRenderer`
       `find TileEntityEnderChestRenderer`
       `find TileEntityRenderer`
       `find World`
       `find WorldChunkManager`
       `find WorldChunkManagerHell`
       `find WorldProviderSurface`
       `find WorldGenBigTree`
       `find WorldGenTrees`
       `find WorldServer`
       ComponentMineshaftCorridorOld
       ComponentStrongholdCrossingOld
       ComponentStrongholdStairsOld
       GuiButtonNBXlite
       GuiCreateFlatWorld2
       ODNBXlite
       RenderEnderman2
       RenderItemFrame2
       RenderMinecart2
       RenderPlayer2
       RenderTntMinecart2
       WorldSSP2
       nbxlite/"
ADD[9]="olddays/classic_water.png
        olddays/classic_lava.png
        olddays/fluff.png
        olddays/gear.png
        olddays/gearmiddle.png
        textures/blocks/olddays_gear_0.png
        textures/blocks/olddays_gear_1.png
        textures/blocks/olddays_grass_top.png
        textures/blocks/olddays_grass_side.png
        textures/blocks/olddays_leaves_fast.png
        textures/blocks/olddays_leaves_fancy.png"
CP[10]="GuiButtonOldDays
        GuiButtonProp
        GuiOldDaysBase
        GuiOldDaysModules
        GuiOldDaysPresets
        GuiOldDaysSearch
        GuiOldDaysSeparator
        GuiOldDaysSettings
        GuiScrolling
        GuiTextFieldSearch
        IScrollingGui
        OldDaysEasyLocalization
        OldDaysModule
        OldDaysProperty
        OldDaysPropertyBool
        OldDaysPropertyCond
        OldDaysPropertyCond2
        OldDaysPropertyFloat
        OldDaysPropertyInt
        OldDaysPropertyRGB
        OldDaysPropertySet
        OldDaysPropertyString
        OldDaysTextureManager
        OldDaysTextureManager\$TextureHook
        SavingManager
        SMPManager
        TextureFX
        mod_OldDays"
ADD[10]="olddays/lang
         olddays/presets"
MV[11]="`find BlockFire` `find BlockFlowing` `find BlockMushroom` `find EntityItem` `find EntitySheep` `find BlockStairs` ItemCoalOld
        BlockFence2 BlockFarmlandOld BlockLog2 BlockTNT2 EntityAIEatGrass2 EntityTNTPrimed2 ItemPickaxe2 ItemAxe2 ODActions
        `find EntityMinecart` `find EntityBoat` `find BlockPistonBase` `find ContainerPlayer` ODBugs `find BlockLeaves` MovementInputFromOptionsCustom
        `find EntityXPOrb` `find FoodStats` `find ItemFood` BlockCake2 `find TileEntityEnderChestRenderer` PathIndev `find BlockSnow`
        `find EntityArrow` `find ItemBow` `find EntityPlayer` `find EntityZombie` ODGameplay `find ItemEnchantedBook` IScrollingGui
        `find EntityCreeper` `find EntitySkeleton` `find EntitySnowman` `find EntityAIPanic` `find EntityAITarget` `find ComponentNetherBridgeCorridor2`
        OldDaysTextureManager\$TextureHook `find ItemDye` EntityAIArrowAttack2 `find PathFinder` TextureFX TextureCompassFX `find ComponentNetherBridgeCorridor`
        TextureWaterFX TextureWaterFlowFX TextureLavaFX TextureLavaFlowFX TextureFlamesFX TexturePortalFX TextureWatchFX GuiScrolling
        `find EntityCreature` `find EntitySpider` `find EntityPig` `find EntityEnderman` PathEntityIndev `find BlockSapling` TextureGearFX
        `find EntityWaterMob` `find EntitySlime` ODMobs `find ComponentVillageField` `find ComponentVillageField2` PathPointIndev
        MD3Tag MD3Frame MD3Surface MD3Renderer MD3Loader MD3Shader MD3Model RenderMD3 EntitySteve EntityRana EntitySteve\$RenderMD3Steve
        `find ModelBiped` `find RendererLivingEntity` `find TileEntityChestRenderer` `find EntitySuspendFX` `find RenderItem` GuiOldDaysSeparator
        `find EntityDiggingFX` `find RenderHelper` `find EntityDropParticleFX` `find LoadingScreenRenderer` `find GuiMainMenu`
        BlockChestOld BlockRedstoneWireOld OldContainerCreative GuiContainerCreativeOld LogoEffectRandomizer PathFinderIndev GuiButtonNBXlite
        ModelMobArmor RenderEnderman2 RenderMinecart2 RenderTntMinecart2 RenderZombie2 RenderSkeleton2 RenderSnowMan2 RenderItemFrame2 ODEyecandy
        SoundManager2 ODSounds ODCrafting BlockOreStorageOld ODTextures GuiCreateFlatWorld2 `find World` `find RenderBiped`
        `find BiomeGenBase` `find BlockFluid` `find ChunkCache` `find ComponentVillage` `find WorldGenBigTree` `find BlockGrass`
        `find ChunkProviderHell` `find StructureMineshaftPieces` `find EntityAIMate` `find EntityAnimal` `find ComponentStrongholdStairs2`
        `find EntityWolf` `find GenLayer` `find WorldChunkManager` `find WorldChunkManagerHell` `find StructureStrongholdPieces`
        ComponentStrongholdStairsOld ComponentStrongholdCrossingOld `find WorldServer` RenderPlayer2 `find ChestItemRenderHelper`
        `find WorldProviderSurface` `find WorldGenTrees` WorldSSP2 ComponentMineshaftCorridorOld ODNBXlite nbxlite/ `find EntityPickupFX`
        `find RenderManager` `find Chunk` `find TileEntityRenderer` `find EntityFX` `find EntityBreakingFX` GuiOldDaysPresets
        GuiButtonOldDays GuiOldDaysModules GuiOldDaysSettings mod_OldDays OldDaysModule OldDaysProperty OldDaysPropertyBool OldDaysPropertySet
        OldDaysPropertyInt OldDaysPropertyString OldDaysPropertyRGB OldDaysPropertyCond OldDaysPropertyFloat OldDaysTextureManager SavingManager
        SMPManager OldDaysEasyLocalization GuiOldDaysBase GuiOldDaysSearch GuiTextFieldSearch GuiButtonProp OldDaysPropertyCond2"
CP[11]="`find EntityLiving` `find EntityLivingBase` `find ItemRenderer`"
ADD[11]="${ADD[1]} ${ADD[2]} ${ADD[3]} ${ADD[4]} ${ADD[5]} ${ADD[6]} ${ADD[7]} ${ADD[8]} ${ADD[9]} ${ADD[10]}"
MV[12]="EntityHuman
        ModelHuman
        RenderHuman
        mod_SpawnHuman"
ADD[12]="./char.png"
MV[13]="CommandClientSPC
        EntityPlayerSPSPC
        PlayerHelper
        PlayerHelper\$1
        Settings
        SPCCheckVersion
        SPCCommand
        SPCEntity
        SPCEntityCamera
        SPCEntityInterface
        SPCLocalConfiguration
        SPCLocalPlayer
        SPCLocalWorld
        SPCObjectHit
        SPCPlugin
        SPCPluginManager
        SPCPoint
        SPCServerInterface
        SPCVersion
        SPCVersionInterface
        SPCWorld
        SPCWorldInterface
        WorldEditPlugin
        mod_SSPC
        spc_WorldEdit
        spc_paint
        spc_path"
MV[14]="net/ ssp/ other"
#MV[14]="`find WorldInfo` `find GuiIngame` `find GuiContainer` `find GuiInventory` `find EntityRenderer` `find ItemRenderer`
#        `find RenderBlocks` `find RenderGlobal` `find EntityLiving` `find EntityLivingBase` net/ ssp/ other"

cd $ORIG_DIR_SERVER

rm -rf $RESULT_DIR
rm -rf $RESULT_DIR_2
echo "CLIENT:"
mkdir -p $RESULT_DIR/client/unused
echo " UNUSED:"
for f in $UNUSED; do
    echo "  MV CLASS: $f1.class"
    mv $ORIG_DIR/$f.class "$RESULT_DIR/client/unused/$f.class"
done
count=1;
for f in $MODS_LIST; do
    mkdir -p $RESULT_DIR/client/$f
    echo " MOD: $f"
    for f1 in ${MV[$count]}; do
        if [ $f1 == "other" ]; then
            echo "  MV ALL CLASSES"
            for ff in *.class; do
                mv $ORIG_DIR/$ff "$RESULT_DIR/client/$f"
            done
        elif [ ${f1: -1} == "/" ]; then
            echo "  MV DIR: $f1"
            mv "$ORIG_DIR/$f1" "$RESULT_DIR/client/$f"
        else
            echo "  MV CLASS: $f1.class"
            mv "$ORIG_DIR/$f1.class" "$RESULT_DIR/client/$f/$f1.class"
        fi
    done;
    for f1 in ${CP[$count]}; do
        if [ $f1 == "other" ]; then
            echo "  CP ALL CLASSES"
            for ff in *.class; do
                cp $ORIG_DIR/$ff "$RESULT_DIR/client/$f"
            done
        elif [ ${f1: -1} == "/" ]; then
            echo "  CP DIR: $f1"
            cp "$ORIG_DIR/$f1" "$RESULT_DIR/client/$f" -r
        else
            echo "  CP CLASS: $f1.class"
            cp "$ORIG_DIR/$f1.class" "$RESULT_DIR/client/$f/$f1.class"
        fi
    done;
    for f5 in ${ADD[$count]}; do
        echo "  ADDITIONAL: $f5"
        mkdir -p "$RESULT_DIR/client/$f/assets/minecraft/${f5%/*}"
        cp -r "$ADDITIONAL_DIR/$f5" "$RESULT_DIR/client/$f/assets/minecraft/$f5"
    done;
    count=$count+1;
done
echo "ADD: WorldEdit.jar"
cp -r "$DIR/../lib/WorldEdit.jar" "$RESULT_DIR/client/spc/WorldEdit.jar"
echo "SERVER:"
count=1;
for f2 in $MODS_LIST_SERVER; do
    mkdir -p $RESULT_DIR/server/$f2
    echo " MOD: $f2"
    for f3 in ${SERVER_MV[$count]}; do
        if [ $f3 == "other" ]; then
            echo "  MV ALL CLASSES"
            for ff in *.class; do
                mv $ORIG_DIR_SERVER/$ff "$RESULT_DIR/server/$f2"
            done
        elif [ ${f3: -1} == "/" ]; then
            echo "  MV DIR: $f3"
            mv "$ORIG_DIR_SERVER/$f3" "$RESULT_DIR/server/$f2"
        else
            echo "  MV CLASS: $f3.class"
            mv "$ORIG_DIR_SERVER/$f3.class" "$RESULT_DIR/server/$f2/$f3.class"
        fi
    done;
    for f3 in ${SERVER_CP[$count]}; do
        if [ $f3 == "other" ]; then
            echo "  CP ALL CLASSES"
            for ff in *.class; do
                cp $ORIG_DIR_SERVER/$ff "$RESULT_DIR/server/$f2"
            done
        elif [ ${f3: -1} == "/" ]; then
            echo "  CP DIR: $f3"
            cp "$ORIG_DIR_SERVER/$f3" "$RESULT_DIR/server/$f2" -r
        else
            echo "  CP CLASS: $f3.class"
            cp "$ORIG_DIR_SERVER/$f3.class" "$RESULT_DIR/server/$f2/$f3.class"
        fi
    done;
    count=$count+1;
done;
cd $RESULT_DIR/client
mkdir -p $RESULT_DIR_2/client
for F in *; do
    echo "Packaging $F..."
    cd $F
    zip $RESULT_DIR_2/client/$F.zip * -r > /dev/null
    cd ..
done
echo "Packaging source code..."
cd $DIR/../src-mods/src/client/spc
zip $RESULT_DIR_2/client/spc-src.zip * -r > /dev/null
cd $DIR/../src-mods
zip $RESULT_DIR_2/client/src.zip src/client/nbxlite src/client/olddays src/client/spawnhuman src/client/ssp src/client-* src/server build resources README.md -r > /dev/null
