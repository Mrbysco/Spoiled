package com.mrbysco.spoiled.datagen;

import com.google.gson.JsonObject;
import com.mrbysco.spoiled.Reference;
import com.mrbysco.spoiled.recipe.condition.InitializeSpoilingCondition;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.nio.file.Path;
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

		@Override
		protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
			String folder = "spoiling/";
			String toRotten = "_to_rotten_flesh";
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.APPLE), Items.ROTTEN_FLESH)::build)
						.build(consumer, Reference.MOD_ID , folder + "apple" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.BAKED_POTATO), Items.ROTTEN_FLESH)::build)
						.build(consumer, Reference.MOD_ID , folder + "baked_potato" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.BEEF), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "beef" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.BEETROOT), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "beetroot" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.BEETROOT_SOUP), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "beetroot_soup" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.BREAD), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "bread" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.CARROT), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "carrot" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.CHICKEN), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "chicken" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.CHORUS_FRUIT), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "chorus_fruit" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COD), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cod" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_BEEF), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_beef" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_CHICKEN), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_chicken" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_COD), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_cod" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_MUTTON), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_mutton" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_PORKCHOP), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_porkchop" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_RABBIT), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_rabbit" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKED_SALMON), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cooked_salmon" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.COOKIE), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "cookie" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.DRIED_KELP), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "dried_kelp" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.GOLDEN_APPLE), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "golden_apple" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.GOLDEN_CARROT), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "golden_carrot" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.HONEY_BOTTLE), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "honey_bottle" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.MELON_SLICE), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "melon_slice" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.MUSHROOM_STEW), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "mushroom_stew" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.MUTTON), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "mutton" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.POISONOUS_POTATO), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "poisonous_potato" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.PORKCHOP), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "porkchop" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.POTATO), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "potato" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.PUFFERFISH), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "pufferfish" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.PUMPKIN_PIE), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "pumpkin_pie" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.RABBIT), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "rabbit" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.RABBIT_STEW), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "rabbit_stew" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.SALMON), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "salmon" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.SPIDER_EYE), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "spider_eye" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.SUSPICIOUS_STEW), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "suspicious_stew" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.SWEET_BERRIES), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "sweet_berries" + toRotten);
			ConditionalRecipe.builder().addCondition(new InitializeSpoilingCondition())
					.addRecipe(SpoilRecipeBuilder.spoilRecipe(Ingredient.fromItems(Items.TROPICAL_FISH), Items.ROTTEN_FLESH)::build)
					.build(consumer, Reference.MOD_ID , folder + "tropical_fish" + toRotten);
		}

		@Override
		protected void saveRecipeAdvancement(DirectoryCache cache, JsonObject advancementJson, Path path) {
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
			add("spoiled.spoiling.25", "s");
			add("spoiled.spoiling.50", "Stale");
			add("spoiled.spoiling.75", "Stale");
			add("spoiled.spoiling.100", "Rotten");
		}
	}
}
