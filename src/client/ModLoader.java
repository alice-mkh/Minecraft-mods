import net.minecraft.src.*;

public class ModLoader{
    public static void onItemPickup(EntityPlayer entityplayer, ItemStack itemstack){}

    public static void clientConnect(NetClientHandler netclienthandler, Packet1Login packet1login){}

    public static void clientDisconnect(){}

    public static void clientChat(String s){}

    public static void clientOpenWindow(Packet100OpenWindow packet100openwindow){}

    public static void clientCustomPayload(Packet250CustomPayload packet250custompayload){}

    public static void serverChat(NetServerHandler netserverhandler, String s){}

    public static void serverCustomPayload(NetServerHandler netserverhandler, Packet250CustomPayload packet250custompayload){}

    public static boolean renderWorldBlock(RenderBlocks renderblocks, IBlockAccess iblockaccess, int i, int j, int k, Block block, int l){
        return false;
    }

    public static void renderInvBlock(RenderBlocks renderblocks, Block block, int i, int j){}

    public static boolean renderBlockIsItemFull3D(int i){
        return false;
    }

    public static void populateChunk(IChunkProvider ichunkprovider, int i, int j, World world){}
}