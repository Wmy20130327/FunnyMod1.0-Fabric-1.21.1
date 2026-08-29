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

        //是否有Joker漂浮效果
        if (player.hasStatusEffect(StatusEffects.LEVITATION)) {

            //播放音效
            if (world.isClient()) {
                //播放喝药/解除效果的音效
                world.playSound(player, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 2.0F, 2.0F);
                return TypedActionResult.success(stack, true);
            }

            //服务端核心逻辑

            // A. 解除漂浮效果
            player.removeStatusEffect(StatusEffects.LEVITATION);

            // B. 【关键】清除摔落距离！
            // 这样玩家掉下来时，游戏会认为他是从 0 高度掉下来的，不会计算高空摔伤
            player.fallDistance = 0.0F;

            // C. 强制扣除 0.5 点伤害 (1/4 滴血)
            // 使用 fall 伤害源，这样受伤动画和声音都是摔落的效果
            player.damage(player.getDamageSources().fall(), 0.5F);

            // D. 消耗物品
            if (!player.isCreative()) {
                stack.decrement(1);
            }

            return TypedActionResult.success(stack, false);

        } else {
            // 如果玩家没有漂浮效果，使用解药无效（返回 fail）
            // 这样会播放失败音效，且不消耗物品
            return TypedActionResult.fail(stack);
        }
    }
}