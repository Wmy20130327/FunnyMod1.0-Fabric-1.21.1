package net.funny.funny_mod.item;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class AntidoteItem extends Item {

    public AntidoteItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        // 检查是否有漂浮效果
        if (player.hasStatusEffect(StatusEffects.LEVITATION)) {

            if (world.isClient()) {
                // 客户端播放声音
                world.playSound(player, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.BLOCK_ANVIL_USE, SoundCategory.PLAYERS, 2.0F, 2.5F);
                return TypedActionResult.success(stack, true);
            }

            // --- 服务端逻辑开始 ---

            // 1. 定义射线检测的起点（玩家脚底）和终点（向下300格，防止掉入虚空太久）
            Vec3d startPos = player.getPos();
            Vec3d endPos = startPos.add(0, -300, 0);

            // 2. 创建射线检测上下文
            // ShapeType.COLLIDER: 检测碰撞箱（实体和方块）
            // FluidHandling.NONE: 忽略液体（如果你想穿透水，保持NONE；如果想停在水面，改用SOME）
            RaycastContext context = new RaycastContext(
                    startPos,
                    endPos,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    player
            );

            // 3. 执行检测
            BlockHitResult hitResult = world.raycast(context);

            // 4. 如果检测到了方块（没有 MISS）
            if (hitResult.getType() != net.minecraft.util.hit.HitResult.Type.MISS) {
                BlockPos hitPos = hitResult.getBlockPos();

                // 5. 传送到方块上方 (Y + 1.0 确保站在方块表面，而不是卡在方块里)
                // 使用 setPos 而不是 updatePosition，setPos 会同步给客户端
                player.setPos(hitPos.getX() + 0.5, hitPos.getY() + 1.0, hitPos.getZ() + 0.5);

                // 注意：这里不需要重置 fallDistance，因为玩家还在飘，并没有真正"落地"结算伤害
            }

            // 6. 消耗物品
            if (!player.isCreative()) {
                stack.decrement(1);
            }

            return TypedActionResult.success(stack, false);
        } else {
            return TypedActionResult.fail(stack);
        }
    }
}