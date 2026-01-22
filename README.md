# 🎣 Auto & Swap Fish (BTW Addon)

![Fabric](https://img.shields.io/badge/Loader-Fabric-beige?style=flat-square)
![MC](https://img.shields.io/badge/Minecraft-1.6.4-green?style=flat-square)
![BTW](https://img.shields.io/badge/BTW-CE_3.0.x-red?style=flat-square)
[![Patreon](https://img.shields.io/badge/Support-Patreon-F96854?style=flat-square&logo=patreon&logoColor=white)](https://www.patreon.com/cw/u69669520)

**Auto & Swap Fish** is a Quality-of-Life (QoL) client-side mod for **Better Than Wolves (Community Edition)**. It is designed to automate the tedious fishing process, allowing you to catch fish efficiently without staring at the screen for hours.

> *Created by **NaAstronut** with the assistance of AI.* 🤖

---

## ✨ Features

### 1. 🤖 Auto-Reel (Instant Reaction)
The mod detects the exact moment a fish bites (using server sound packet detection) and reels it in instantly.
* **Zero Latency:** No more "Bait Stolen" messages.
* **Precise:** Works perfectly with BTW's difficult fishing mechanics.

### 2. 🔄 Auto-Rod Swapping
When your current fishing rod breaks or loses its bait (turns into an unbaited rod):
* The mod automatically scans your **Hotbar (Slots 1-9)** for another *Baited Fishing Rod*.
* It swaps to the new rod and casts immediately.
* **Note:** You must keep spare rods in your hotbar for this to work.

### 3. 🥩 Auto-Baiting (Smart Crafting)
If you run out of baited rods but have an **Unbaited Rod** + **Bait** in your inventory:
* The mod will simulate inventory clicks to **craft bait onto the rod automatically!**
* **Supported Baits:**
    * **Vanilla:** Raw Pork, Raw Beef, Raw Chicken, Rotten Flesh, Spider Eye.
    * **BTW:** Bat Wing, Creeper Oysters, Witch Wart, etc.

---

## 📥 Installation

1.  Ensure you have **Minecraft 1.6.4** installed with **Fabric Loader (Legacy)**.
2.  Install **Better Than Wolves CE 3.0.0+**.
3.  Download the latest `.jar` file from the [Releases Page](../../releases).
    * *Note: Download the normal `.jar` file (not the `-sources` one).*
4.  Drop the file into your `.minecraft/mods` folder.
5.  Launch the game!

---

## 🛠️ How to Use

1.  **Prepare:** Put multiple **Baited Fishing Rods** in your **Hotbar**.
2.  **Start:** Hold a rod and cast the line into the water (Right Click).
3.  **Relax:** The mod will handle reeling, swapping, and re-casting automatically.
4.  **Stopping:** To stop the auto-fish loop, simply switch to a different item (like a sword) or move away.

---

## ☕ Support Me

If you like this mod and want to support my work (or buy me a coffee), feel free to check out my Patreon!

[![Patreon](https://img.shields.io/badge/Support_on-Patreon-F96854?style=for-the-badge&logo=patreon&logoColor=white)](https://www.patreon.com/cw/u69669520)

---

## ⚠️ Known Issues / Limitations
* **Inventory Full:** If your inventory is full, the mod will stop fishing to prevent item loss.
* **Hotbar Only:** The Auto-Swap feature currently only looks for rods in the Hotbar (Slots 1-9).
* **Multiplayer:** This is a client-side mod. Use responsibly on servers; some server owners may consider this a macro/bot.

---

## 📜 License
This project is licensed under **CC0-1.0** (Public Domain). Feel free to learn from the code or use it however you like!