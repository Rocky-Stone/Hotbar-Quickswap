package net.rockystone;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class HotbarQuickswap implements ClientModInitializer {

	private static KeyBinding quickswapKeyBind;

	public static final String MOD_ID = "hotbar-quickswap";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		LOGGER.info("onInitializeClient() was called");
		setKeyBindings();
		registerEvents();
	}



	public void setKeyBindings() {
		LOGGER.info("setKeyBindings() was called");
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("hotbar-quickswap", "keybindings"));
		quickswapKeyBind = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.hotbar-quickswap.config", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, category));
	}

	public void registerEvents() {
		LOGGER.info("registerEvents() was called");
		registerEndClientTickEvent();
	}

	public void hotbarQuickswap(MinecraftClient client) {
		if (client.player == null) {
			return;
		}
		int syncID = client.player.playerScreenHandler.syncId;
		int currentlySelectedSlot = client.player.getInventory().getSelectedSlot();
		int aboveSlot = currentlySelectedSlot + 27;
		client.interactionManager.clickSlot(syncID, aboveSlot, currentlySelectedSlot, SlotActionType.SWAP, client.player);
	}

	public void registerEndClientTickEvent() {

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			while (quickswapKeyBind.wasPressed()) {
				hotbarQuickswap(client);
			}

		});
	}
}