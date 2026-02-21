package com.example.nospidercleaner;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(NoSpiderCleanerMod.MOD_ID)
public class NoSpiderCleanerMod {
    public static final String MOD_ID = "nospidercleaner";

    public NoSpiderCleanerMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        EntityType<?> type = entity.getType();

        if (type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide()) {
            return;
        }
        if (event.getChunk() instanceof LevelChunk chunk) {
            cleanChunk(chunk, level);
        }
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof Level level) || level.isClientSide()) {
            return;
        }

        Block placed = event.getPlacedBlock().getBlock();
        if (isTargetBlock(placed)) {
            level.setBlockAndUpdate(event.getPos(), Blocks.AIR.defaultBlockState());
        }
    }

    @SubscribeEvent
    public void onServerLevelTick(TickEvent.LevelTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.level.isClientSide()) {
            return;
        }

        Level level = event.level;
        for (Entity entity : level.getEntities((Entity) null, e -> true)) {
            EntityType<?> type = entity.getType();
            if (type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER) {
                entity.discard();
            }
        }
    }

    private void cleanChunk(LevelChunk chunk, Level level) {
        int minY = level.getMinBuildHeight();
        int maxY = level.getMaxBuildHeight();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int startX = chunk.getPos().getMinBlockX();
        int startZ = chunk.getPos().getMinBlockZ();

        for (int x = startX; x < startX + 16; x++) {
            for (int z = startZ; z < startZ + 16; z++) {
                for (int y = minY; y < maxY; y++) {
                    pos.set(x, y, z);
                    Block block = chunk.getBlockState(pos).getBlock();
                    if (isTargetBlock(block)) {
                        level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }

    private boolean isTargetBlock(Block block) {
        return block == Blocks.COBWEB || block == Blocks.SPAWNER;
    }
}
