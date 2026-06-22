package roidrole.thaumicsjw.jei.categories;

import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface IHasResearch extends IRecipeWrapper {
    String getResearch();

    @Override
    default List<String> getTooltipStrings(int mouseX, int mouseY) {
        if (ThaumcraftCapabilities.knowsResearch(Minecraft.getMinecraft().player, getResearch())){
            return Collections.emptyList();
        }
        if(mouseX < getBarrierX() || mouseX > getBarrierX() + 16 || mouseY < getBarrierY() || mouseY > getBarrierY() + 16){
            return Collections.emptyList();
        }
        List<String> list = new ArrayList<>();
        list.add(TextFormatting.GOLD + "Missing research:");
        for (String s : getResearch().split("&&")) {
            if (!ThaumcraftCapabilities.knowsResearch(Minecraft.getMinecraft().player, s)) {
                ResearchEntry entry = ResearchCategories.getResearch(s.contains("@") ? s.split("@")[0] : s);
                if (entry != null) list.add("- " + TextFormatting.RED + entry.getLocalizedName());
                else list.add("- " + TextFormatting.RED + s);
            }
        }

        return list;
    }

    @Override
    default void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

        if (ThaumcraftCapabilities.knowsResearch(minecraft.player, this.getResearch())) {
            minecraft.getTextureManager().bindTexture(new ResourceLocation("thaumicjei", "textures/gui/thaumonomicon_green.png"));
        } else {
            //TODO: Check if research can be researched
            minecraft.getTextureManager().bindTexture(new ResourceLocation("thaumicjei", "textures/gui/thaumonomicon_red.png"));
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        Gui.drawScaledCustomSizeModalRect(getBarrierX(), getBarrierY(), 0f, 0f, 16, 16, 16, 16, 16, 16);
        GlStateManager.disableBlend();

    }

    int getBarrierX();
    int getBarrierY();
}
