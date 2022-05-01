package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
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
			generator.addProvider(new SpoiledItemTags(generator, new BlockTagsProvider(generator, Reference.MOD_ID, helper), helper));
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
		protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
			makeConditionalRecipe(consumer, "vanilla", Ingredient.of(SpoiledTags.FOODS_VANILLA));
		}

		private void makeConditionalRecipe(Consumer<IFinishedRecipe> consumer, String name, Ingredient ingredient) {
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(ingredient, Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID, folder + name + toRotten);
		}

		@Override
		protected void saveAdvancement(DirectoryCache cache, JsonObject advancementJson, Path path) {
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

			add("spoiled:tileentity.list.message", "A list of Tile Entities has been output into the log");
		}
	}

	public static class SpoiledItemTags extends ItemTagsProvider {
		public SpoiledItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagsProvider, Reference.MOD_ID, existingFileHelper);
		}

		@Override
		protected void addTags() {
			List<Item> blacklist = new ArrayList<>();
			blacklist.add(Items.ROTTEN_FLESH);
			blacklist.add(Items.ENCHANTED_GOLDEN_APPLE);
			addModFood(SpoiledTags.FOODS_VANILLA, "minecraft", blacklist);
		}

		private void addModFood(ITag.INamedTag<Item> tag, String modID, List<Item> blacklist) {
			for (Item item : ForgeRegistries.ITEMS) {
				if (!blacklist.contains(item) && item.isEdible() && item.getRegistryName().getNamespace().equals(modID)) {
					this.tag(tag).add(item);
				}
			}
			this.tag(SpoiledTags.FOODS).addTag(tag);
		}
	}
}
