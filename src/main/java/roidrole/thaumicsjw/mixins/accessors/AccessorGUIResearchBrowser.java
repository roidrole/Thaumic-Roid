package roidrole.thaumicsjw.mixins.accessors;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import thaumcraft.client.gui.GuiResearchBrowser;

@Mixin(GuiResearchBrowser.class)
public interface AccessorGUIResearchBrowser {
	@Accessor(remap = false)
	static void setSelectedCategory(String category) {
		throw new AssertionError();
	}
}
