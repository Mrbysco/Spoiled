package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpoiledDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(new Recipes(generator));
		}
		if (event.includeClient()) {
			generator.addProvider(new Language(generator));
		}
	}

	private static class Recipes extends RecipeProvider {
		public Recipes(DataGenerator gen) {
			super(gen);
		}

		private final String folder = "spoiling/";
		private final String toRotten = "_to_rotten_flesh";

		@Override
		protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
			List<Item> itemBlacklist = new ArrayList<>();
			itemBlacklist.add(Items.ROTTEN_FLESH);
			itemBlacklist.add(Items.ENCHANTED_GOLDEN_APPLE);

			for (Item item : ForgeRegistries.ITEMS) {
				if (!itemBlacklist.contains(item) && item.isEdible() && item.getRegistryName().getNamespace().equals("minecraft")) {
					makeConditionalRecipe(consumer, item);
				}
			}
		}

		private void makeConditionalRecipe(Consumer<FinishedRecipe> consumer, Item item) {
			ResourceLocation id = item.getRegistryName();
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.of(item), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID, folder + id.getPath() + toRotten);
		}

		@Override
		protected void saveAdvancement(HashCache cache, JsonObject advancementJson, Path path) {
			// Nope
		}
	}

	private static class Language extends LanguageProvider {
		public Language(DataGenerator gen) {
			super(gen, Reference.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("spoiled.spoiling", "Spoiling progress: ");
			add("spoiled.spoiling.0", "Fresh");
			add("spoiled.spoiling.25", "");
			add("spoiled.spoiling.50", "Stale");
			add("spoiled.spoiling.75", "Stale");
			add("spoiled.spoiling.100", "Rotten");
		}
	}
}
