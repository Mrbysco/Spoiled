package com.mrbysco.spoiled.compat.rei;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.compat.rei.category.SpoilCategoryNeoForge;
import com.mrbysco.spoiled.compat.rei.display.SpoilDisplayNeoForge;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.world.item.Items;

@REIPluginClient
public class REIPluginNeoForge implements REIClientPlugin {
	public static final CategoryIdentifier<SpoilDisplayNeoForge> SPOILING = CategoryIdentifier.of(Constants.MOD_ID, "plugins/spoiling");

	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new SpoilCategoryNeoForge());

		registry.addWorkstations(SPOILING, EntryStacks.of(Items.ROTTEN_FLESH));
	}

	@Override
	public void registerDisplays(DisplayRegistry registry) {
		Constants.SPOIL_RECIPES.forEach((holder) -> {
			SpoilRecipe recipe = holder.value();
			registry.add(new SpoilDisplayNeoForge(
							recipe.getIngredient(),
							recipe.getResult()
					)
			);
		});
	}
}
