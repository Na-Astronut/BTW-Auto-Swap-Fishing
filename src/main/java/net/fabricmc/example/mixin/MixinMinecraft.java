package net.fabricmc.example.mixin;

import net.fabricmc.example.FishingState;
import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.ArrayList; // <--- อย่าลืมบรรทัดนี้!
import java.util.List;      // <--- และบรรทัดนี้!

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Unique private int castTimer = 0;
    @Unique private Item[] ALLOWED_BAITS;

    // --- ระบบสแกนหาเหยื่อ (Auto-Discovery) ---
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        List<Item> foundBaits = new ArrayList<Item>();

        // 1. ใส่ของ Minecraft ปกติ (อันนี้เรารู้จักแน่นอน)
        foundBaits.add(Item.porkRaw);
        foundBaits.add(Item.beefRaw);
        foundBaits.add(Item.chickenRaw);
        foundBaits.add(Item.rottenFlesh);
        foundBaits.add(Item.spiderEye);
        // foundBaits.add(Item.fishRaw); // ถ้าจะใช้ปลา ก็เปิดบรรทัดนี้

        // 2. สแกนหาของ BTW จากชื่อภายใน (Unlocalized Name)
        // วนลูปเช็คไอเท็มทุกชิ้นในเกม
        System.out.println("[AutoFish] 🔍 Scanning for BTW Baits...");

        for (Item item : Item.itemsList) {
            if (item == null) continue;

            String name = item.getUnlocalizedName();
            if (name == null) continue;

            // เช็คว่าชื่อมีคำสำคัญที่เราต้องการไหม?
            // (ใช้ toLowerCase เพื่อกันเหนียวเรื่องตัวพิมพ์ใหญ่-เล็ก)
            String lowerName = name.toLowerCase();

            if (lowerName.contains("creeperoysters") ||
                    lowerName.contains("batwing") ||
                    lowerName.contains("witchwart")) {

                System.out.println("[AutoFish] Found Bait: " + name + " (ID: " + item.itemID + ")");
                foundBaits.add(item);
            }
        }

        // แปลงกลับเป็น Array เพื่อเอาไปใช้จริง
        ALLOWED_BAITS = foundBaits.toArray(new Item[0]);
    }

    // --- (โค้ดส่วน runTick, swapToRod เหมือนเดิมเป๊ะ ไม่ต้องแก้) ---
    // ...
    @Inject(method = "runTick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        // ... (ใช้โค้ดเดิมจากรอบที่แล้วได้เลยครับ)
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.thePlayer == null || mc.theWorld == null) return;

        if (!FishingState.needRecast) {
            castTimer = 0;
            return;
        }

        if (castTimer < 50) {
            castTimer++;
            return;
        }

        ItemStack currentItem = mc.thePlayer.getCurrentEquippedItem();

        // 1. เช็คถือของผิด / มือเปล่า
        if (currentItem == null || !(currentItem.getItem() instanceof ItemFishingRod)) {
            System.out.println("[AutoFish] Searching for rod...");
            if (swapToRod(mc)) {
                castTimer = 40; return;
            } else {
                mc.thePlayer.addChatMessage("§c[AutoFish] No Fishing Rod found!");
                FishingState.needRecast = false; return;
            }
        }

        // 2. เช็คถือ "เบ็ดเปล่า" -> ต้องใส่เหยื่อ
        if (currentItem.getItem() == Item.fishingRod) {
            System.out.println("[AutoFish] Unbaited rod detected. Trying to bait...");

            if (swapToBaitedRod(mc)) {
                System.out.println("[AutoFish] Swapped to baited rod.");
                castTimer = 40; return;
            }

            // เรียกใช้ autoCraftBait ที่มีระบบ Cleanup (สำคัญมาก!)
            if (autoCraftBait(mc)) {
                System.out.println("[AutoFish] Crafted bait successfully!");
                castTimer = 60; return;
            } else {
                mc.thePlayer.addChatMessage("§c[AutoFish] Out of Bait! Stopping.");
                FishingState.needRecast = false; return;
            }
        }

        // Inventory เต็ม?
        if (mc.thePlayer.inventory.getFirstEmptyStack() == -1) {
            mc.thePlayer.addChatMessage("§c[AutoFish] Inventory Full!");
            FishingState.needRecast = false; return;
        }

        // โยน!
        System.out.println("[AutoFish] Casting...");
        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getCurrentEquippedItem());
        FishingState.needRecast = false;
        castTimer = 0;
    }

    // ... (ฟังก์ชั่น swapToBaitedRod, swapToRod, autoCraftBait อันเดิม) ...
    @Unique
    private boolean swapToBaitedRod(Minecraft mc) {
        InventoryPlayer inv = mc.thePlayer.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.mainInventory[i];
            if (stack != null && stack.getItem() instanceof ItemFishingRod && stack.getItem() != Item.fishingRod) {
                if (stack.getItemDamage() < stack.getMaxDamage()) {
                    inv.currentItem = i; return true;
                }
            }
        }
        return false;
    }

    @Unique
    private boolean swapToRod(Minecraft mc) {
        InventoryPlayer inv = mc.thePlayer.inventory;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = inv.mainInventory[i];
            if (stack != null && stack.getItem() instanceof ItemFishingRod) {
                inv.currentItem = i; return true;
            }
        }
        return false;
    }

    @Unique
    private boolean autoCraftBait(Minecraft mc) {
        int baitSlot = -1;
        // วนหาเหยื่อจาก List ที่เราสแกนเจอ
        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
            if (stack != null) {
                if (ALLOWED_BAITS != null) {
                    for (Item bait : ALLOWED_BAITS) {
                        if (bait != null && stack.getItem() == bait) {
                            baitSlot = i;
                            break;
                        }
                    }
                }
            }
            if (baitSlot != -1) break;
        }

        if (baitSlot == -1) return false;

        PlayerControllerMP controller = mc.playerController;
        int windowId = 0;

        // Step 1: วางเหยื่อ
        controller.windowClick(windowId, baitSlot, 0, 0, mc.thePlayer);
        controller.windowClick(windowId, 1, 1, 0, mc.thePlayer);
        controller.windowClick(windowId, baitSlot, 0, 0, mc.thePlayer);

        // Step 2: วางเบ็ด
        int currentItemSlot = mc.thePlayer.inventory.currentItem + 36;
        controller.windowClick(windowId, currentItemSlot, 0, 0, mc.thePlayer);
        controller.windowClick(windowId, 2, 0, 0, mc.thePlayer);

        // Step 3: กดผสม
        controller.windowClick(windowId, 0, 0, 1, mc.thePlayer);

        // Step 4: Cleanup (ดึงของกลับกันค้าง)
        controller.windowClick(windowId, 1, 0, 1, mc.thePlayer);
        controller.windowClick(windowId, 2, 0, 1, mc.thePlayer);

        return true;
    }
}