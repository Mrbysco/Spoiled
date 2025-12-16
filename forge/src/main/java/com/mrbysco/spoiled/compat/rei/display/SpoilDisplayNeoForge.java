//package com.mrbysco.spoiled.compat.rei.display;
//
//import com.mrbysco.spoiled.compat.rei.REIPluginNeoForge;
//import me.shedaniel.rei.api.common.category.CategoryIdentifier;
//import me.shedaniel.rei.api.common.display.Display;
//import me.shedaniel.rei.api.common.display.DisplaySerializer;
//import me.shedaniel.rei.api.common.entry.EntryIngredient;
//import me.shedaniel.rei.api.common.util.EntryIngredients;
//import net.minecraft.resources.Identifier;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.Ingredient;
//import org.jetbrains.annotations.Nullable;
//
//import java.util.List;
//import java.util.Optional;
//
//public class SpoilDisplayNeoForge implements Display {
//	private final List<EntryIngredient> inputEntries;
//	private final List<EntryIngredient> outputEntries;
//
//	public SpoilDisplayNeoForge(Ingredient ingredient, ItemStack stack) {
//		this.inputEntries = List.of(EntryIngredients.ofIngredient(ingredient));
//		this.outputEntries = List.of(EntryIngredients.of(stack));
//	}
//
//	@Override
//	public List<EntryIngredient> getInputEntries() {
//		return inputEntries;
//	}
//
//	@Override
//	public List<EntryIngredient> getOutputEntries() {
//		return outputEntries;
//	}
//
//	@Override
//	public CategoryIdentifier<?> getCategoryIdentifier() {
//		return REIPluginNeoForge.SPOILING;
//	}
//
//	@Override
//	public Optional<Identifier> getDisplayLocation() {
//		return Optional.empty();
//	}
//
//	@Nullable
//	@Override
//	public DisplaySerializer<? extends Display> getSerializer() {
//		return null;
//	}
//}
