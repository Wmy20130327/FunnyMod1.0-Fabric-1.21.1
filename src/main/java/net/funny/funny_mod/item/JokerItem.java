package net.funny.funny_mod.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class JokerItem extends Item {

    public JokerItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        //模仿原版击中时的反馈
        if (world.isClient()) {
            //播放潜影贝子弹击中声音
            world.playSound(player, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.PLAYERS, 2.0F, 1.0F);
            return TypedActionResult.success(stack, true);
        }

        //完全模仿 ShulkerBulletEntity.onHit

        //StatusEffects.LEVITATION -> 漂浮效果
        //12000 -> 持续时间 (10分钟 * 60秒 * 20 ticks = 12000)
        //0 -> 效果等级 (0: 表示1级，和原版潜影贝子弹一致)
        //player -> 效果来源（这里填玩家自己，原版填的是子弹的发射者）
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, 12000, 0), player);

        //消耗物品 (模仿原版子弹击中后消失)
        if (!player.isCreative()) {
            stack.decrement(1);
        }

        return TypedActionResult.success(stack, false);
    }
}