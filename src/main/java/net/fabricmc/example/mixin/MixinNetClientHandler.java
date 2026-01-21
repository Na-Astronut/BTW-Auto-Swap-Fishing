package net.fabricmc.example.mixin;

import net.fabricmc.example.FishingState;
import net.minecraft.src.*; // นำเข้า Packet และ NetClientHandler
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
public class MixinNetClientHandler {

	@Inject(method = "handleLevelSound", at = @At("HEAD"))
	private void onSoundPacket(Packet62LevelSound packet, CallbackInfo ci) {
		Minecraft mc = Minecraft.getMinecraft();

		// 1. เช็คว่าเสียงที่ส่งมาคือ "random.splash" (เสียงปลาฮุบเหยื่อ)
		if ("random.splash".equals(packet.getSoundName())) {

			// 2. เช็คว่าเรากำลังตกปลาอยู่
			if (mc.thePlayer != null && mc.thePlayer.fishEntity != null) {

				// 3. เช็คระยะห่าง: เสียงเกิดใกล้ทุ่นเราไหม? (กันดึงตอนคนอื่นตกได้)
				EntityFishHook bobber = mc.thePlayer.fishEntity;
				double dx = bobber.posX - packet.getEffectX();
				double dy = bobber.posY - packet.getEffectY();
				double dz = bobber.posZ - packet.getEffectZ();
				double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

				// ถ้าระยะห่างน้อยกว่า 3 บล็อก แสดงว่าเป็นทุ่นเราแน่ๆ
				if (distance < 3.0) {
					System.out.println("[AutoFish] 🔊 Packet Splash Detected! PULLING NOW!");

					// สั่งดึงทันที! (นี่คือจุดที่ไวที่สุดที่เป็นไปได้)
					mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());

					// สั่งให้ MixinMinecraft เตรียมโยนใหม่
					FishingState.needRecast = true;
				}
			}
		}
	}
}