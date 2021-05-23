package com.mrbysco.spoiled.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mrbysco.spoiled.config.SpoiledConfig;
import com.mrbysco.spoiled.config.SpoiledConfigCache;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class SpoilRecipe implements IRecipe<IInventory> {
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
	public IRecipeType<?> getType() {
		return SpoiledRecipes.SPOIL_RECIPE_TYPE;
	}

	@Override
	public boolean matches(IInventory inv, World worldIn) {
		return this.ingredient.test(inv.getStackInSlot(0));
	}

	public ItemStack getCraftingResult(IInventory inventory) {
		return this.result.copy();
	}

	public boolean canFit(int x, int y) {
		return true;
	}

	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> nonnulllist = NonNullList.create();
		nonnulllist.add(this.ingredient);
		return nonnulllist;
	}

	public ItemStack getRecipeOutput() {
		return this.result;
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
	public IRecipeSerializer<?> getSerializer() {
		return SpoiledRecipes.SPOILING_SERIALIZER.get();
	}

	public static class SerializerSpoilRecipe extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SpoilRecipe> {
		@Override
		public SpoilRecipe read(ResourceLocation recipeId, JsonObject jsonObject) {
			String s = JSONUtils.getString(jsonObject, "group", "");
			JsonElement jsonelement = (JsonElement)(JSONUtils.isJsonArray(jsonObject, "ingredient") ? JSONUtils.getJsonArray(jsonObject, "ingredient") : JSONUtils.getJsonObject(jsonObject, "ingredient"));
			Ingredient ingredient = Ingredient.deserialize(jsonelement);
			//Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
			ItemStack itemstack;
			if(jsonObject.has("result")) {
				if (jsonObject.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(jsonObject, "result"));
				else {
					String s1 = JSONUtils.getString(jsonObject, "result");
					ResourceLocation resourcelocation = new ResourceLocation(s1);
					itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> {
						return new IllegalStateException("Item: " + s1 + " does not exist");
					}));
				}
			} else {
				itemstack = SpoiledConfigCache.getDefaultSpoilItem();
			}

			int spoilTime = JSONUtils.getInt(jsonObject, "spoiltime", SpoiledConfig.COMMON.defaultSpoilTime.get());
			return new SpoilRecipe(recipeId, s, ingredient, itemstack, spoilTime);
		}

		@Nullable
		@Override
		public SpoilRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
			String s = buffer.readString(32767);
			Ingredient ingredient = Ingredient.read(buffer);
			ItemStack itemstack = buffer.readItemStack();
			int spoilTime = buffer.readVarInt();
			return new SpoilRecipe(recipeId, s, ingredient, itemstack, spoilTime);
		}

		@Override
		public void write(PacketBuffer buffer, SpoilRecipe recipe) {
			buffer.writeString(recipe.group);
			recipe.ingredient.write(buffer);
			buffer.writeItemStack(recipe.result);
			buffer.writeVarInt(recipe.spoilTime);
		}
	}
}