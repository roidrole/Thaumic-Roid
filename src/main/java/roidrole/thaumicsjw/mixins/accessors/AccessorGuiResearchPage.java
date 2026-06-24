package roidrole.thaumicsjw.mixins.accessors;

import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import thaumcraft.client.gui.GuiResearchPage;

@Mixin(GuiResearchPage.class)
public interface AccessorGuiResearchPage {
	@Accessor("shownRecipe")
	static ResourceLocation getShownRecipe(){
		throw new AssertionError();
	}

	@Accessor("showingKnowledge")
	boolean getShowingKnowledge();

	@Accessor("showingAspects")
	boolean getShowingAspects();
}
