package com.mrbysco.spoiled.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import com.mrbysco.spoiled.platform.Services;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SpoilRecipe implements Recipe<Container> {
	protected final ResourceLocation id;
	protected final String group;
	protected final Ingredient ingredient;
	protected final ItemStack result;
	protected final int spoilTime;

	public SpoilRecipe(ResourceLocation id, String group, Ingredient ingredient, ItemStack stack, int spoilTime) {
		this.id = id;
		this.group = group;
		this.ingredient = ingredient;
		this.result = stack;
		this.spoilTime = spoilTime;
	}

	@Override
	public RecipeType<?> getType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE.get();
	}

	@Override
	public boolean matches(Container inv, Level level) {
		return this.getIngredients().get(0).test(inv.getItem(0));
	}

	public ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
		return getResultItem(registryAccess).copy();
	}

	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	public ItemStack getResultItem(RegistryAccess registryAccess) {
		return this.result.copy();
	}

	public String getGroup() {
		return this.group;
	}

	public ResourceLocation getId() {
		return this.id;
	}

	public int getSpoilTime() {
		return spoilTime;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SpoiledRecipes.SPOILING_SERIALIZER.get();
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	public static class SerializerSpoilRecipe implements RecipeSerializer<SpoilRecipe> {
		@Override
		public SpoilRecipe fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
			String s = GsonHelper.getAsString(jsonObject, "group", "");
			JsonElement jsonelement = (JsonElement) (GsonHelper.isArrayNode(jsonObject, "ingredient") ?
					GsonHelper.getAsJsonArray(jsonObject, "ingredient") :
					GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
			Ingredient ingredient = Ingredient.fromJson(jsonelement);
			//Forge: Check if primitive string to keep vanilla or an object which can contain a count field.
			ItemStack itemstack;
			if (jsonObject.has("result")) {
				if (jsonObject.get("result").isJsonObject())
					itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
				else {
					String s1 = GsonHelper.getAsString(jsonObject, "result");
					ResourceLocation resourcelocation = new ResourceLocation(s1);
					itemstack = new ItemStack(BuiltInRegistries.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
						return new IllegalStateException("Item: " + s1 + " does not exist");
					}));
				}
			} else {
				itemstack = SpoiledConfigCache.getDefaultSpoilItem();
			}

			int spoilTime = GsonHelper.getAsInt(jsonObject, "spoiltime", Services.PLATFORM.getDefaultSpoilTime());
			return new SpoilRecipe(recipeId, s, ingredient, itemstack, spoilTime);
		}

		@Nullable
		@Override
		public SpoilRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			String s = buffer.readUtf(32767);
			Ingredient ingredient = Ingredient.fromNetwork(buffer);
			ItemStack itemstack = buffer.readItem();
			int spoilTime = buffer.readVarInt();
			return new SpoilRecipe(recipeId, s, ingredient, itemstack, spoilTime);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, SpoilRecipe recipe) {
			buffer.writeUtf(recipe.group);
			recipe.ingredient.toNetwork(buffer);
			buffer.writeItem(recipe.result);
			buffer.writeVarInt(recipe.spoilTime);
		}
	}
}