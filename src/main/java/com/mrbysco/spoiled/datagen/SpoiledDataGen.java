package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.SpoiledRecipes;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import com.mrbysco.spoiled.recipe.condition.MergeRecipeCondition;
import com.mrbysco.spoiled.util.SpoiledTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpoiledDataGen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper helper = event.getExistingFileHelper();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new Recipes(packOutput));
			BlockTagsProvider blockTagsProvider;
			generator.addProvider(event.includeServer(), blockTagsProvider = new BlockTagsProvider(packOutput, lookupProvider, Reference.MOD_ID, helper) {
				@Override
				protected void addTags(HolderLookup.Provider provider) {

				}
			});
			generator.addProvider(event.includeServer(), new SpoiledItemTags(packOutput, lookupProvider, blockTagsProvider.contentsGetter(), helper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeServer(), new Language(packOutput));
		}
	}

	private static class Recipes extends RecipeProvider {
		public Recipes(PackOutput gen) {
			super(gen);
		}

		private final String folder = "spoiling/";
		private final String toRotten = "_to_rotten_flesh";

		@Override
		protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
			makeConditionalRecipe(consumer, "vanilla", Ingredient.of(SpoiledTags.FOODS_VANILLA));

			ConditionalRecipe.builder()
					.addCondition(new MergeRecipeCondition())
					.addRecipe(c -> SpecialRecipeBuilder.special(SpoiledRecipes.STACK_FOOD_SERIALIZER.get())
							.save(c, new ResourceLocation(Reference.MOD_ID, "merge_food").toString()))
					.build(consumer, new ResourceLocation(Reference.MOD_ID, "merge_food"));
		}

		private void makeConditionalRecipe(Consumer<FinishedRecipe> consumer, String name, Ingredient ingredient) {
			ConditionalRecipe.builder()
					.addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(ingredient, Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID, folder + name + toRotten);
		}

		@Override
		protected @Nullable CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe finishedRecipe, JsonObject advancementJson) {
			return null;
		}
	}

	private static class Language extends LanguageProvider {
		public Language(PackOutput packOutput) {
			super(packOutput, Reference.MOD_ID, "en_us");
		}

		@Override
		protected void addTranslations() {
			add("spoiled.spoiling", "Spoiling progress: ");
			add("spoiled.spoiling.0", "Fresh");
			add("spoiled.spoiling.25", "");
			add("spoiled.spoiling.50", "Stale");
			add("spoiled.spoiling.75", "Stale");
			add("spoiled.spoiling.100", "Rotten");

			add("spoiled.gui.jei.category.spoiling", "Spoiling");
			add("spoiled.gui.jei.spoil_time", "Spoil Time: %s");
			add("spoiled.command.blockentity_list.message", "A list of Block Entities has been output into the log");
			add("spoiled.command.food_list.message", "A list of Food has been output into the log");
		}
	}

	public static class SpoiledItemTags extends ItemTagsProvider {
		public SpoiledItemTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
							   CompletableFuture<TagsProvider.TagLookup<Block>> blockTagProvider, ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTagProvider, Reference.MOD_ID, existingFileHelper);
		}

		@Override
		public void addTags(HolderLookup.Provider lookupProvider) {
			addModFood(SpoiledTags.FOODS_VANILLA, "minecraft", List.of(Items.ROTTEN_FLESH, Items.ENCHANTED_GOLDEN_APPLE));
		}

		private void addModFood(TagKey<Item> tag, String modID, List<Item> blacklist) {
			for (Item item : ForgeRegistries.ITEMS) {
				if (!blacklist.contains(item) && item.isEdible() && ForgeRegistries.ITEMS.getKey(item).getNamespace().equals(modID)) {
					this.tag(tag).add(item);
				}
			}
			this.tag(SpoiledTags.FOODS).addTag(tag);
		}
	}
}
