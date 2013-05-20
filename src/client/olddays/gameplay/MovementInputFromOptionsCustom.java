package net.minecraft.src;

public class MovementInputFromOptionsCustom extends MovementInputFromOptions{
    public static boolean allowSneak = true;

    private GameSettings gameSettings;
    private EntityClientPlayerMP player;

    public MovementInputFromOptionsCustom(EntityClientPlayerMP p, GameSettings par1GameSettings){
        super(par1GameSettings);
        gameSettings = par1GameSettings;
        player = p;
    }

    @Override
    public void updatePlayerMoveState(){
        super.updatePlayerMoveState();
        if (allowSneak || player.capabilities.isFlying){
            return;
        }
        if (sneak){
            moveStrafe /= 0.29999999999999999D;
            moveForward /= 0.29999999999999999D;
        }
        sneak = false;
    }
}
