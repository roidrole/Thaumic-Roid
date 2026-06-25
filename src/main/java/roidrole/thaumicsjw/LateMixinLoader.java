package roidrole.thaumicsjw;

import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class LateMixinLoader implements ILateMixinLoader {
	@Override
	public List<String> getMixinConfigs() {
		ArrayList<String> mixinConfigs = new ArrayList<>(8);
		if(ThaumicSJWConfig.general.aspectTooltipInAllGUI){
			mixinConfigs.add("mixins.thaumicsjw.aspect_tooltip_everywhere.json");
		}
		if(ThaumicSJWConfig.performanceConfig.fasterHash){
			mixinConfigs.add("mixins.thaumicsjw.faster_hash.json");
		}
		if(ThaumicSJWConfig.performanceConfig.patternCrafterRecipeCache){
			mixinConfigs.add("mixins.thaumicsjw.patterncrafter_recipe_cache.json");
		}
		if(ThaumicSJWConfig.performanceConfig.fasterOreDictWildcard){
			mixinConfigs.add("mixins.thaumicsjw.faster_oredict_wildcard.json");
		}
		if(ThaumicSJWConfig.performanceConfig.aspectCache){
			mixinConfigs.add("mixins.thaumicsjw.aspect_cache.json");
		}
		if(ThaumicSJWConfig.visualOresConfig.dioptraUpdatesAura && Loader.isModLoaded("visualores")){
			mixinConfigs.add("mixins.thaumicsjw.dioptra_aura.json");
		}
		mixinConfigs.add("mixins.thaumicsjw.accessors.json");
		return mixinConfigs;
	}
}
