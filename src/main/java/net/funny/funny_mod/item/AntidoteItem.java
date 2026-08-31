package net.funny.funny_mod.item;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class AntidoteItem extends Item {

    public AntidoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.hasStatusEffect(StatusEffects.LEVITATION)) {

            if (world.isClient()) {
                // 播放铁砧敲击声（解药生效的声音）
                world.playSound(player, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 2.0F, 2.5F); // 音调调高一点，更清脆
                return TypedActionResult.success(stack, true);
            }

            // 1. 解除漂浮
            player.removeStatusEffect(StatusEffects.LEVITATION);

            // 2. 【核心修复】将摔落距离设为极大的负数！
            // Minecraft 最大建筑高度是 320，最低是 -64，总高度差不到 400。
            // 设为 -1000.0F，确保玩家无论掉多高，落地时 fallDistance 依然是负数，不会触发摔伤。
            player.fallDistance = -1000.0F;

            // 3. 强制扣除 0.5 点伤害 (1/4 滴血)
            player.damage(player.getDamageSources().fall(), 0.5F);

            // 4. 消耗物品
            if (!player.isCreative()) {
                stack.decrement(1);
            }
            return TypedActionResult.success(stack, false);
        }
        else {
            return TypedActionResult.fail(stack);
        }
    }
}