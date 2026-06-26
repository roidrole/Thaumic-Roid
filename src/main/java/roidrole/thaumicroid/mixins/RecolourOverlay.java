package roidrole.thaumicroid.mixins;

import hellfall.visualores.database.thaumcraft.AuraFluxPosition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AuraFluxPosition.class)
public abstract class RecolourOverlay {

	@Shadow(remap = false)
	public float flux;

	@ModifyVariable(
		method = "<init>",
		name = "alpha",
		at = @At(
			value = "LOAD",
			target = "alpha"
		),
		remap = false
	)
	int cutoffAlpha(int alpha){
		return Math.max(1, alpha);
	}

	@ModifyVariable(
		method = "<init>",
		name = "midAlpha",
		at = @At(
			value = "LOAD",
			target = "midAlpha"
		),
		remap = false
	)
	int addMidAlpha(int midAlpha){
		return Math.max(1, (int)(midAlpha * 1.99f));
	}


	@ModifyVariable(
		method = "<init>",
		name = "fluxAmount",
		at = @At(
			value = "LOAD",
			target = "fluxAmount"
		),
		remap = false
	)
	float multiplyFlux(float fluxAmount) {
		return 1.0f;
	}

	@ModifyVariable(
		method = "<init>",
		name = "totalAmount",
		at = @At(
			value = "LOAD",
			target = "totalAmount"
		),
		remap = false
	)
	double multiplyFlux(double totalAmount) {
		return this.flux/32.0d;
	}

}
