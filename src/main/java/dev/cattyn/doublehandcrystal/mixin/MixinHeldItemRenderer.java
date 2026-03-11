package dev.cattyn.doublehandcrystal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {
    @Unique
    private static final ItemStack END_CRYSTAL = new ItemStack(Items.END_CRYSTAL, 64);

    @ModifyArgs(method = "renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V"))
    private void renderItemHook(Args args, @Local(argsOnly = true) ClientPlayerEntity entity) {
        Hand h = args.get(3);
        if (h != Hand.MAIN_HAND) {
            return;
        }
        if (!entity.getOffHandStack().isOf(Items.TOTEM_OF_UNDYING)
                || !entity.getMainHandStack().isOf(Items.TOTEM_OF_UNDYING)) {
            return;
        }

        boolean hasCrystal = false;
        for (int i = 0; i < 9; i++) {
            if (entity.getInventory().getStack(i).isOf(Items.END_CRYSTAL)) {
                hasCrystal = true;
                break;
            }
        }

        if (!hasCrystal) {
            return;
        }

        args.set(5, END_CRYSTAL);
    }
}
