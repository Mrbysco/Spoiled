//package com.mrbysco.spoiled.compat.rei;
//
//import com.mrbysco.spoiled.Constants;
//import com.mrbysco.spoiled.compat.rei.category.SpoilCategoryFabric;
//import com.mrbysco.spoiled.compat.rei.display.SpoilDisplayFabric;
//import com.mrbysco.spoiled.recipe.SpoilRecipe;
//import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
//import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
//import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
//import me.shedaniel.rei.api.common.category.CategoryIdentifier;
//import me.shedaniel.rei.api.common.util.EntryStacks;
//import net.minecraft.world.item.Items;
//
//public class REIPluginFabric implements REIClientPlugin {
//	public static final CategoryIdentifier<SpoilDisplayFabric> SPOILING = CategoryIdentifier.of(Constants.MOD_ID, "plugins/spoiling");
//
//	@Override
//	public void registerCategories(CategoryRegistry registry) {
//		registry.add(new SpoilCategoryFabric());
//
//		registry.addWorkstations(SPOILING, EntryStacks.of(Items.ROTTEN_FLESH));
//	}
//
//	@Override
//	public void registerDisplays(DisplayRegistry registry) {
//		Constants.SPOIL_RECIPES.forEach((holder) -> {
//			SpoilRecipe recipe = holder.value();
//			registry.add(new SpoilDisplayFabric(
//							recipe.getIngredient(),
//							recipe.getResult()
//					)
//			);
//		});
//	}
//}
