@echo off

cd ..
cd ..

:SETUP
set SRC=%cd%\src\minecraft\net\minecraft\src\
set CL=%cd%\src\minecraft\net\minecraft\client\
set CS=%cd%\src\minecraft\net\minecraft\server\
set MODSSRC=%cd%\src-mods\src\client\
set MODSCL=%cd%\src-mods\src\client-client\
set MODSCS=%cd%\src-mods\src\client-server\
cls
Echo.
Echo.Press enter to deploy the repository
pause >NUL
cls

:DEL
Echo.
Echo. * Deleting old files
Echo.
del %CL%\ClientBrandRetriever.java
del %CS%\MinecraftServer.java
del %SRC%\Minecraft.java
del %SRC%\EffectRenderer.java
del %SRC%\EntityAIHurtByTarget.java
del %SRC%\EntityEnderPearl.java
del %SRC%\EntityFireworkRocket.java
del %SRC%\EntityPlayerMP.java
del %SRC%\Explosion.java
del %SRC%\GuiChat.java
del %SRC%\GuiCreateWorld.java
del %SRC%\GuiGameOver.java
del %SRC%\GuiMultiplayer.java
del %SRC%\GuiShareToLan.java
del %SRC%\GuiWorldSlot.java
del %SRC%\ISaveHandler.java
del %SRC%\NetClientHandler.java
del %SRC%\NetServerHandler.java
del %SRC%\RenderBlocks.java
del %SRC%\SaveHandler.java
del %SRC%\SaveHandlerMP.java
del %SRC%\SpawnerAnimals.java
del %SRC%\MobSpawnerBaseLogic.java
del %SRC%\WorldClient.java
del %SRC%\BiomeGenBase.java
del %SRC%\BlockFluid.java
del %SRC%\BlockGrass.java
del %SRC%\BlockSapling.java
del %SRC%\ChestItemRenderHelper.java
del %SRC%\ChunkCache.java
del %SRC%\Chunk.java
del %SRC%\ChunkProviderHell.java
del %SRC%\ComponentStrongholdStairs2.java
del %SRC%\ComponentNetherBridgeCorridor.java
del %SRC%\ComponentNetherBridgeCorridor2.java
del %SRC%\ComponentVillageField2.java
del %SRC%\ComponentVillageField.java
del %SRC%\ComponentVillage.java
del %SRC%\EntityAIMate.java
del %SRC%\EntityAnimal.java
del %SRC%\EntityBreakingFX.java
del %SRC%\EntityFX.java
del %SRC%\EntityOcelot.java
del %SRC%\EntityPickupFX.java
del %SRC%\EntityRenderer.java
del %SRC%\EntityWolf.java
del %SRC%\GenLayer.java
del %SRC%\GuiSelectWorld.java
del %SRC%\ItemEnchantedBook.java
del %SRC%\RenderBiped.java
del %SRC%\RenderGlobal.java
del %SRC%\RenderManager.java
del %SRC%\StructureMineshaftPieces.java
del %SRC%\StructureStrongholdPieces.java
del %SRC%\TileEntityRenderer.java
del %SRC%\WorldChunkManagerHell.java
del %SRC%\WorldChunkManager.java
del %SRC%\WorldGenBigTree.java
del %SRC%\WorldGenTrees.java
del %SRC%\WorldInfo.java
del %SRC%\World.java
del %SRC%\WorldProviderSurface.java
del %SRC%\WorldServer.java
del %SRC%\EntityEnderman.java
del %SRC%\EntityLiving.java
del %SRC%\EntityLivingBase.java
del %SRC%\EntitySheep.java
del %SRC%\EntitySkeleton.java
del %SRC%\EntitySpider.java
del %SRC%\EntityZombie.java
del %SRC%\BlockFire.java
del %SRC%\BlockFlowing.java
del %SRC%\BlockLeaves.java
del %SRC%\BlockMushroom.java
del %SRC%\BlockStairs.java
del %SRC%\EntityItem.java
del %SRC%\BlockSnow.java
del %SRC%\ItemDye.java
del %SRC%\EntityAIPanic.java
del %SRC%\EntityAITarget.java
del %SRC%\EntityCreature.java
del %SRC%\EntityCreeper.java
del %SRC%\EntityPig.java
del %SRC%\EntitySlime.java
del %SRC%\EntitySnowman.java
del %SRC%\EntityWaterMob.java
del %SRC%\PathFinder.java
del %SRC%\BlockPistonBase.java
del %SRC%\ContainerPlayer.java
del %SRC%\EntityBoat.java
del %SRC%\Entity.java
del %SRC%\EntityMinecart.java
del %SRC%\GuiInventory.java
del %SRC%\EntityDiggingFX.java
del %SRC%\EntityDropParticleFX.java
del %SRC%\EntitySuspendFX.java
del %SRC%\GuiContainer.java
del %SRC%\GuiMainMenu.java
del %SRC%\GuiScreen.java
del %SRC%\ItemRenderer.java
del %SRC%\LoadingScreenRenderer.java
del %SRC%\ModelBiped.java
del %SRC%\ModelEnderman.java
del %SRC%\RendererLivingEntity.java
del %SRC%\RenderHelper.java
del %SRC%\RenderItem.java
del %SRC%\TileEntityChestRenderer.java
del %SRC%\TileEntityEnderChestRenderer.java
del %SRC%\EntityArrow.java
del %SRC%\EntityPlayer.java
del %SRC%\EntityXPOrb.java
del %SRC%\FoodStats.java
del %SRC%\GuiIngame.java
del %SRC%\ItemBow.java
del %SRC%\ItemFood.java

:LINK
Echo.
Echo. * Linking src files
Echo.
%cd%\src-mods\build\ln.exe /s %MODSCL%\ssp\ClientBrandRetriever.java %CL%\ClientBrandRetriever.java
%cd%\src-mods\build\ln.exe /s %MODSCS%\ssp\MinecraftServer.java %CS%\MinecraftServer.java
%cd%\src-mods\build\ln.exe /s %MODSCL%\ssp\Minecraft.java %SRC%\Minecraft.java
%cd%\src-mods\build\ln.exe /s %cd%\src-mods\src\client\ %cd%\src\minecraft\net\minecraft\src\mods\
%cd%\src-mods\build\ln.exe /s %cd%\src-mods\resources\olddays\ %cd%\jars\assets\olddays\
%cd%\src-mods\build\ln.exe /s %cd%\src-mods\resources\textures\ %cd%\jars\assets\textures\
%cd%\src-mods\build\ln.exe /s %cd%\src-mods\resources\char.png %cd%\jars\assets\char.png
%cd%\src-mods\build\ln.exe /s %cd%\src-mods\resources %cd%\reobf\additional
%cd%\src-mods\build\wget.exe http://dev.bukkit.org/media/files/715/447/worldedit-5.5.7.jar -cO lib/WorldEdit.jar
pause
cls