package net.fabricmc.example;

import net.fabricmc.api.ClientModInitializer;

public class AutoFishClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		System.out.println("AutoFish for BTW Loaded!");
	}
}