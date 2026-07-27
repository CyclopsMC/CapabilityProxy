package org.cyclops.capabilityproxy.gametest;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.cyclops.capabilityproxy.Reference;
import org.cyclops.capabilityproxy.RegistryEntries;
import org.cyclops.capabilityproxy.block.BlockCapabilityProxy;
import org.cyclops.capabilityproxy.block.BlockEntityCapabilityProxy;
import org.cyclops.capabilityproxy.block.BlockItemCapabilityProxy;
import org.cyclops.capabilityproxy.block.BlockRangedCapabilityProxy;
import org.cyclops.capabilityproxy.blockentity.BlockEntityItemCapabilityProxyCommon;
import org.cyclops.cyclopscore.gametest.GameTest;

/**
 * @author rubensworks
 */
public class GameTestsCommon {

    public static final String TEMPLATE_EMPTY = Reference.MOD_ID + ":empty10";
    public static final BlockPos POS = BlockPos.ZERO;

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyPlacementDirection(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS, Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_CAPABILITY_PROXY.value(), POS.south());
            helper.assertBlockProperty(POS.south(), BlockCapabilityProxy.FACING, Direction.NORTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyPlacementDirectionOpposite(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.south(), Direction.NORTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_CAPABILITY_PROXY.value(), POS);
            helper.assertBlockProperty(POS, BlockCapabilityProxy.FACING, Direction.SOUTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyPlacementInactive(GameTestHelper helper) {
        // Let proxy target air
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.offset(2, 2, 2), Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_CAPABILITY_PROXY.value(), POS.offset(2, 2, 3));
            helper.assertBlockProperty(POS.offset(2, 2, 3), BlockCapabilityProxy.INACTIVE, true);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyPlacementActive(GameTestHelper helper) {
        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.offset(2, 2, 2), Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_CAPABILITY_PROXY.value(), POS.offset(2, 2, 3));
            helper.assertBlockProperty(POS.offset(2, 2, 3), BlockCapabilityProxy.INACTIVE, false);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyForwardItemHopper(GameTestHelper helper) {
        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));

        // Add hopper that targets proxy
        helper.setBlock(POS.offset(2, 2, 4), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));

        // Throw apple in hopper
        helper.spawnItem(Items.APPLE, POS.offset(2, 3, 4));

        helper.succeedWhen(() -> assertChestContains(helper, POS.offset(2, 2, 2), new ItemStack(Items.APPLE)));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyForwardChainItemHopper(GameTestHelper helper) {
        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));

        // Add chain of hoppers and proxies that targets proxy
        helper.setBlock(POS.offset(2, 2, 4), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(2, 3, 4), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 4), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 5), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 4, 5), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 5, 5), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));

        // Throw apple in final hopper
        helper.spawnItem(Items.APPLE, POS.offset(3, 6, 5));

        helper.succeedWhen(() -> assertChestContains(helper, POS.offset(2, 2, 2), new ItemStack(Items.APPLE)));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyCycleItem(GameTestHelper helper) {
        // Add cycle of proxies
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.EAST));
        helper.setBlock(POS.offset(2, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 2, 2), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.SOUTH));

        // Let hopper target one proxy
        helper.setBlock(POS.offset(2, 3, 2), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));

        // Throw apple in final hopper
        helper.spawnItem(Items.APPLE, POS.offset(2, 4, 2));

        // The hopper should not be able to insert into a cycle
        helper.succeedIf(() -> helper.assertAtTickTimeContainerContains(10, POS.offset(2, 4, 2), Items.APPLE));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyForwardGuiHopper(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));

        helper.succeedIf(() -> {
            // Open screen of target
            BlockState blockState = helper.getBlockState(POS.offset(2, 2, 3));
            InteractionResult result = blockState.useWithoutItem(helper.getLevel(), player, new BlockHitResult(POS.offset(2, 2, 3).getBottomCenter(), Direction.NORTH, helper.absolutePos(POS.offset(2, 2, 3)), false));
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), Component.literal("Interaction failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyForwardChainGuiHopper(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));

        // Add chain of proxies that targets proxy
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(2, 3, 4), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 4), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 5), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 4, 5), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 5, 5), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.DOWN));

        helper.succeedWhen(() -> {
            // Open screen of target
            BlockState blockState = helper.getBlockState(POS.offset(3, 5, 5));
            InteractionResult result = blockState.useWithoutItem(helper.getLevel(), player, new BlockHitResult(POS.offset(3, 5, 5).getBottomCenter(), Direction.NORTH, helper.absolutePos(POS.offset(3, 5, 5)), false));
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), Component.literal("Interaction failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyCycleGui(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Add cycle of proxies
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.EAST));
        helper.setBlock(POS.offset(2, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 2, 3), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 2, 2), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.SOUTH));

        helper.succeedWhen(() -> {
            // Open screen of target
            BlockState blockState = helper.getBlockState(POS.offset(2, 2, 2));
            InteractionResult result = blockState.useWithoutItem(helper.getLevel(), player, new BlockHitResult(POS.offset(2, 2, 2).getBottomCenter(), Direction.NORTH, helper.absolutePos(POS.offset(2, 2, 2)), false));
            helper.assertTrue(result.equals(InteractionResult.FAIL), Component.literal("Interaction did not fail"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedPlacementDirection(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS, Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value(), POS.south());
            helper.assertBlockProperty(POS.south(), BlockRangedCapabilityProxy.FACING, Direction.NORTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedPlacementDirectionOpposite(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_RANGED_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.south(), Direction.NORTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value(), POS);
            helper.assertBlockProperty(POS, BlockRangedCapabilityProxy.FACING, Direction.SOUTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedForwardItemHopper(GameTestHelper helper) {
        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));

        // Add hopper that targets proxy
        helper.setBlock(POS.offset(2, 2, 5), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));

        // Throw apple in hopper
        helper.spawnItem(Items.APPLE, POS.offset(2, 3, 5));

        helper.succeedWhen(() -> assertChestContains(helper, POS.offset(2, 2, 2), new ItemStack(Items.APPLE)));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedForwardChainItemHopper(GameTestHelper helper) {
        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));

        // Add chain of hoppers and proxies that targets proxy
        helper.setBlock(POS.offset(2, 2, 5), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(2, 3, 5), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 5), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 6), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 4, 6), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 5, 6), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));

        // Throw apple in final hopper
        helper.spawnItem(Items.APPLE, POS.offset(3, 6, 6));

        helper.succeedWhen(() -> assertChestContains(helper, POS.offset(2, 2, 2), new ItemStack(Items.APPLE)));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedCycleItem(GameTestHelper helper) {
        // Add cycle of proxies
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.EAST));
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(4, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(4, 2, 2), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.SOUTH));

        // Let hopper target one proxy
        helper.setBlock(POS.offset(2, 3, 2), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));

        // Throw apple in final hopper
        helper.spawnItem(Items.APPLE, POS.offset(2, 4, 2));

        // The hopper should not be able to insert into a cycle
        helper.succeedIf(() -> helper.assertAtTickTimeContainerContains(10, POS.offset(2, 4, 2), Items.APPLE));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedForwardGuiHopper(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));

        helper.succeedIf(() -> {
            // Open screen of target
            BlockState blockState = helper.getBlockState(POS.offset(2, 2, 4));
            InteractionResult result = blockState.useWithoutItem(helper.getLevel(), player, new BlockHitResult(POS.offset(2, 2, 4).getBottomCenter(), Direction.NORTH, helper.absolutePos(POS.offset(2, 2, 4)), false));
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), Component.literal("Interaction failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedForwardChainGuiHopper(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));

        // Add chain of proxies that targets proxy
        helper.setBlock(POS.offset(2, 2, 5), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(2, 3, 5), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 5), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 6), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 4, 6), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 5, 6), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.DOWN));

        helper.succeedWhen(() -> {
            // Open screen of target
            BlockState blockState = helper.getBlockState(POS.offset(3, 5, 6));
            InteractionResult result = blockState.useWithoutItem(helper.getLevel(), player, new BlockHitResult(POS.offset(3, 5, 6).getBottomCenter(), Direction.NORTH, helper.absolutePos(POS.offset(3, 5, 6)), false));
            helper.assertTrue(result.equals(InteractionResult.SUCCESS), Component.literal("Interaction failed"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyRangedCycleGui(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);

        // Add cycle of proxies
        helper.setBlock(POS.offset(2, 2, 2), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.EAST));
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(4, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(4, 2, 2), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.SOUTH));

        helper.succeedWhen(() -> {
            // Open screen of target
            BlockState blockState = helper.getBlockState(POS.offset(2, 2, 2));
            InteractionResult result = blockState.useWithoutItem(helper.getLevel(), player, new BlockHitResult(POS.offset(2, 2, 2).getBottomCenter(), Direction.NORTH, helper.absolutePos(POS.offset(2, 2, 2)), false));
            helper.assertTrue(result.equals(InteractionResult.PASS) || result.equals(InteractionResult.FAIL), Component.literal("Interaction did not pass"));
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyMixedRangedForwardChainItemHopper(GameTestHelper helper) {
        // Set chest
        helper.setBlock(POS.offset(2, 2, 2), Blocks.CHEST);

        // Let proxy target chest
        helper.setBlock(POS.offset(2, 2, 4), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));

        // Add chain of hoppers and proxies that targets proxy
        helper.setBlock(POS.offset(2, 2, 5), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(2, 3, 5), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 5), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));
        helper.setBlock(POS.offset(2, 4, 6), RegistryEntries.BLOCK_RANGED_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockRangedCapabilityProxy.FACING, Direction.NORTH));
        helper.setBlock(POS.offset(3, 4, 6), RegistryEntries.BLOCK_CAPABILITY_PROXY.value()
                .defaultBlockState()
                .setValue(BlockCapabilityProxy.FACING, Direction.WEST));
        helper.setBlock(POS.offset(3, 5, 6), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.DOWN));

        // Throw apple in final hopper
        helper.spawnItem(Items.APPLE, POS.offset(3, 6, 6));

        helper.succeedWhen(() -> assertChestContains(helper, POS.offset(2, 2, 2), new ItemStack(Items.APPLE)));
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyItemPlacementDirection(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS, Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value(), POS.south());
            helper.assertBlockProperty(POS.south(), BlockItemCapabilityProxy.FACING, Direction.NORTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyItemPlacementDirectionOpposite(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.south(), Direction.NORTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value(), POS);
            helper.assertBlockProperty(POS, BlockItemCapabilityProxy.FACING, Direction.SOUTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyItemPlacementInactive(GameTestHelper helper) {
        // Place empty proxy
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.offset(2, 2, 2), Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value(), POS.offset(2, 2, 3));
            helper.assertBlockProperty(POS.offset(2, 2, 3), BlockItemCapabilityProxy.INACTIVE, true);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyItemPlacementActive(GameTestHelper helper) {
        // Place proxy with contained item
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.offset(2, 2, 2), Direction.SOUTH);

        // Place shulker box in proxy inventory
        BlockEntityItemCapabilityProxyCommon blockEntity = helper.getBlockEntity(POS.offset(2, 2, 3), BlockEntityItemCapabilityProxyCommon.class);
        blockEntity.getInventory().setItem(0, new ItemStack(Items.WATER_BUCKET));

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_ITEM_CAPABILITY_PROXY.value(), POS.offset(2, 2, 3));
            helper.assertBlockProperty(POS.offset(2, 2, 3), BlockItemCapabilityProxy.INACTIVE, false);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyItemFillShulkerBoxFromHopper(GameTestHelper helper) {
        // Shulker box capability only exists in NeoForge
        if (!isNeoForge()) {
            helper.succeed();
            return;
        }

        // Place proxy with contained item
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ITEM_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.offset(2, 2, 2), Direction.SOUTH);

        // Place shulker box in proxy inventory
        BlockEntityItemCapabilityProxyCommon blockEntity = helper.getBlockEntity(POS.offset(2, 2, 3), BlockEntityItemCapabilityProxyCommon.class);
        blockEntity.getInventory().setItem(0, new ItemStack(Items.SHULKER_BOX));

        // Make hopper target proxy
        helper.setBlock(POS.offset(2, 2, 4), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));

        // Throw apple in hopper
        helper.spawnItem(Items.APPLE, POS.offset(2, 3, 4));

        helper.succeedWhen(() -> {
            ItemStack itemStackFilled = blockEntity.getInventory().getItem(0);
            ItemContainerContents container = itemStackFilled.get(DataComponents.CONTAINER);
            if (!container.nonEmptyItemCopyStream().anyMatch(i -> i.getItem() == Items.APPLE)) {
                throw new GameTestAssertException((Component) Component.literal("Shulker box in item proxy contains no apple"), (int) helper.getTick());
            }
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyEntityPlacementDirection(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENTITY_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS, Direction.SOUTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY.value(), POS.south());
            helper.assertBlockProperty(POS.south(), BlockEntityCapabilityProxy.FACING, Direction.NORTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyEntityPlacementDirectionOpposite(GameTestHelper helper) {
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENTITY_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.south(), Direction.NORTH);

        helper.succeedIf(() -> {
            helper.assertBlockPresent(RegistryEntries.BLOCK_ENTITY_CAPABILITY_PROXY.value(), POS);
            helper.assertBlockProperty(POS, BlockEntityCapabilityProxy.FACING, Direction.SOUTH);
        });
    }

    @GameTest(template = TEMPLATE_EMPTY)
    public void testBlockProxyEntityFillChestMinecartFromHopper(GameTestHelper helper) {
        // Chest minecart capability only exists in NeoForge and Forge
        if (!isNeoForge() && !isForge()) {
            helper.succeed();
            return;
        }

        // Place proxy with contained item
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack itemStack = new ItemStack(RegistryEntries.ITEM_ENTITY_CAPABILITY_PROXY.value());
        player.setItemInHand(InteractionHand.MAIN_HAND, itemStack);
        helper.placeAt(player, itemStack, POS.offset(2, 0, 2), Direction.SOUTH);

        // Place chest minecart at target
        helper.setBlock(POS.offset(2, 0, 2), Blocks.RAIL.defaultBlockState());
        MinecartChest minecart = helper.spawn(EntityType.CHEST_MINECART, POS.offset(2, 0, 2));

        // Make hopper target proxy
        helper.setBlock(POS.offset(2, 0, 4), Blocks.HOPPER
                .defaultBlockState()
                .setValue(HopperBlock.FACING, Direction.NORTH));

        // Throw apple in hopper
        helper.spawnItem(Items.APPLE, POS.offset(2, 1, 4));

        helper.succeedWhen(() -> {
            if (minecart.getItem(0).getItem() != Items.APPLE) {
                throw new GameTestAssertException((Component) Component.literal("Chest minecart targeted by entity proxy contains no apple"), (int) helper.getTick());
            }
        });
    }

    protected void assertChestContains(GameTestHelper helper, BlockPos pos, ItemStack itemStack) {
        helper.assertBlockEntityData(pos, ChestBlockEntity.class, (ChestBlockEntity chest) -> ItemStack.isSameItemSameComponents(chest.getItem(0), itemStack), () -> Component.literal("Chest is not empty"));
    }

    protected boolean isNeoForge() { // TODO: try to rm in next major
        try {
            Class.forName("net.neoforged.neoforge.common.NeoForge");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    protected boolean isForge() { // TODO: try to rm in next major
        try {
            Class.forName("net.minecraftforge.common.MinecraftForge");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
