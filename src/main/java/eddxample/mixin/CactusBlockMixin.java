package eddxample.mixin;

import eddxample.CactusSpeedometer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(CactusBlock.class)
public class CactusBlockMixin extends Block {

    public CactusBlockMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "scheduledTick", at = @At("HEAD"), cancellable = true)
    public void onRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);

            CactusSpeedometer.onGrowAttempt(world, pos, "§4", state.get(Properties.AGE_15));
        } else {
            BlockPos blockPos = pos.up();
            if (world.isAir(blockPos)) {
                int i = 1;
                for(; world.getBlockState(pos.down(i)).getBlock() == this; ++i);

                if (i < 3) {
                    int j = state.get(Properties.AGE_15);
                    if (j == 15) {
                        world.setBlockState(blockPos, this.getDefaultState());
                        BlockState blockState = state.with(Properties.AGE_15, 0);
                        world.setBlockState(pos, blockState, 4);
                        blockState.neighborUpdate(world, blockPos, this, pos, false);

                        CactusSpeedometer.onGrowAttempt(world, pos, "§2", 0);
                    } else {
                        world.setBlockState(pos, state.with(Properties.AGE_15, j + 1), 4);

                        CactusSpeedometer.onGrowAttempt(world, pos, "§7", j + 1);
                    }

                }
            } else CactusSpeedometer.onGrowAttempt(world, pos, "§8", state.get(Properties.AGE_15));
        }
        ci.cancel();
    }
}
