package eddxample;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

public class CactusSpeedometer {
    public static int lastTimestamp = 0;
    public static String color = "§6";
    public static boolean updateFlag;

    public static void updateTimestamp(int ticks) {
        if (updateFlag) {
            lastTimestamp = ticks;
            color = color.equals("§6") ? "§e" : "§6";
            updateFlag = false;
        }
    }

    public static void onGrowAttempt(ServerWorld world, BlockPos pos, String status, int age) {

        boolean display = world.getGameRules().getBoolean(GameRules.COMMAND_BLOCK_OUTPUT);
        if (!display) return;

        int ticks = world.getServer().getTicks();
        if (ticks != lastTimestamp) {
            updateFlag = true;
        }

        world.getServer().getPlayerManager().sendToAll(new LiteralText(String.format("§f[%s%05d§f]§7 {%d, %d, %d} age: %s%2d§r", color, ticks, pos.getX(), pos.getY(), pos.getZ(), status, age)));
    }
}
