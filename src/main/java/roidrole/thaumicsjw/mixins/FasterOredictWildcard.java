package roidrole.thaumicsjw.mixins;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import thaumcraft.api.ThaumcraftApiHelper;

import java.util.List;

@Mixin(ThaumcraftApiHelper.class)
public abstract class FasterOredictWildcard {
	@Unique
	private static String[] thaumicsjw_oreNames;

	/**
	 * @author Rongmario
	 * @reason A little optimization to not retrieve a new array every time a wildcard entry is dealt with + cache trimmed entry
	 */
	@Overwrite(remap = false)
	public static List<ItemStack> getOresWithWildCards(String oreDict) {
		oreDict = oreDict.trim();
		if (oreDict.endsWith("*")) {
			final List<ItemStack> ores = new ObjectArrayList<>();
			if (thaumicsjw_oreNames == null) {
				thaumicsjw_oreNames = OreDictionary.getOreNames();
			}
			String wildcard = oreDict.replaceAll("\\*", "");
			for (String ore : thaumicsjw_oreNames) {
				if (ore.startsWith(wildcard)) {
					ores.addAll(OreDictionary.getOres(ore, false));
				}
			}
			return ores;
		} else {
			return OreDictionary.getOres(oreDict, false);
		}
	}
}