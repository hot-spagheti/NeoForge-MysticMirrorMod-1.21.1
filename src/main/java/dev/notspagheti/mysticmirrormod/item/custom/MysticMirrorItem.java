package dev.notspagheti.mysticmirrormod.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class MysticMirrorItem extends Item {
    public MysticMirrorItem(Properties properties){
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairMaterial) {
        return repairMaterial.getItem() == Items.DIAMOND || super.isValidRepairItem(stack, repairMaterial);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand usedHand) {
        player.startUsingItem(usedHand);
        return InteractionResultHolder.success(player.getItemInHand(usedHand));
    }

    @Override
    public void onUseTick(Level world, LivingEntity entity, ItemStack stack, int count) {
        if (!world.isClientSide()) return;
        if (!(entity instanceof Player player)) return;

        int usedTicks = this.getUseDuration(stack, entity) - count;

        for (int i = 0; i < 5; i++){
            double x = player.getX() + (world.random.nextDouble() - 0.5) * 2;
            double y = player.getY() + world.random.nextDouble() * 2;
            double z = player.getZ() + (world.random.nextDouble() - 0.5) * 2;

            double dx = player.getX() - x;
            double dy = player.getY() + 1 - y;
            double dz = player.getZ() - z;

            double speed = 0.1;
            world.addParticle(ParticleTypes.PORTAL, x, y, z, dx * speed, dy * speed, dz * speed);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 30;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide() && entity instanceof ServerPlayer serverPlayer) {

            stack.hurtAndBreak(1, serverPlayer, EquipmentSlot.MAINHAND);
            serverPlayer.invulnerableTime = 30;

            BlockPos respawnPos = serverPlayer.getRespawnPosition();
            if (respawnPos != null && serverPlayer.level().getBlockState(respawnPos).is(BlockTags.BEDS)){
                serverPlayer.teleportTo(respawnPos.getX() + 0.5, respawnPos.getY() + 0.6, respawnPos.getZ() + 0.5);
            } else {
                BlockPos spawn = serverPlayer.level().getSharedSpawnPos();
                serverPlayer.teleportTo(spawn.getX() + 0.5, spawn.getY(), spawn.getZ() + 0.5);
            }

            serverPlayer.level().playSound(null, serverPlayer.blockPosition(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return stack;
    }
}
