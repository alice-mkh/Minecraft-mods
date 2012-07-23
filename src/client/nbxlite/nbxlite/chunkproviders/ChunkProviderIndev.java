package net.minecraft.src.nbxlite.chunkproviders;

import net.minecraft.src.*;

public class ChunkProviderIndev extends ChunkProviderBaseFinite{
    public ChunkProviderIndev(World world, long l){
        super(world, l);
    }

    public void generateFiniteLevel(){
        ODNBXlite.generateIndevLevel(seed);
    }
}
