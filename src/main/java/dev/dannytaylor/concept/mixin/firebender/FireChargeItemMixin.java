package dev.dannytaylor.concept.mixin.firebender;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.FireChargeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireChargeItem.class)
public abstract class FireChargeItemMixin extends Item {
	public FireChargeItemMixin(Settings settings) {
		super(settings);
	}

	@Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
	private void concept$useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
		cir.setReturnValue(ActionResult.PASS);
	}

	@Inject(method = "createEntity", at = @At("HEAD"), cancellable = true)
	private void concept$createEntity(World world, Position pos, ItemStack stack, Direction direction, CallbackInfoReturnable<ProjectileEntity> cir) {
		Random random = world.getRandom();
		double d = random.nextTriangular(direction.getOffsetX(), 0.11485000000000001);
		double e = random.nextTriangular(direction.getOffsetY(), 0.11485000000000001);
		double f = random.nextTriangular(direction.getOffsetZ(), 0.11485000000000001);
		Vec3d vec3d = new Vec3d(d, e, f);
		SmallFireballEntity fireballEntity = new SmallFireballEntity(world, pos.getX(), pos.getY(), pos.getZ(), vec3d);
		fireballEntity.setVelocity(vec3d);
		cir.setReturnValue(fireballEntity);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		if (world instanceof ServerWorld serverWorld) {
			ProjectileEntity.spawnWithVelocity((world2, shooter, stack) -> new FireballEntity(world, user, user.getEntityPos().add(user.getRotationVec(1.0F).multiply(user.getWidth() / 2.0F + 0.5F)), 1), serverWorld, itemStack, user, 0.0F, 1.5F, 1.0F);
		}

		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_FIRECHARGE_USE, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		itemStack.decrementUnlessCreative(1, user);
		return ActionResult.SUCCESS;
	}
}