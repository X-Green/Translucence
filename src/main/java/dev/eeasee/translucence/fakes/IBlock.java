package dev.eeasee.translucence.fakes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateManager;

import java.util.function.Consumer;
import java.util.function.Function;

public interface IBlock {
    void appendNewBlockStates(Consumer<StateManager.Builder<Block, BlockState>> appendMethod, Function<BlockState, BlockState> appendDefaultState);
}
