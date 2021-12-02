package com.mrbysco.spoiled.compat.ct;

//import com.blamejared.crafttweaker.api.CraftTweakerAPI;
//import com.blamejared.crafttweaker.api.annotations.ZenRegister;
//import com.blamejared.crafttweaker.api.item.IIngredient;
//import com.blamejared.crafttweaker.api.item.IItemStack;
//import com.blamejared.crafttweaker.api.managers.IRecipeManager;
//import com.blamejared.crafttweaker.impl.actions.recipes.ActionAddRecipe;
//import com.mrbysco.spoiled.recipe.SpoilRecipe;
//import com.mrbysco.spoiled.recipe.SpoiledRecipes;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.crafting.RecipeType;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraftforge.fml.ModList;
//import net.minecraftforge.registries.ForgeRegistries;
//import org.openzen.zencode.java.ZenCodeGlobals;
//import org.openzen.zencode.java.ZenCodeType;

//@ZenRegister
//@ZenCodeType.Name("mods.spoiled.SpoilingManager")
public class SpoilManager { //implements IRecipeManager {

//    @ZenCodeGlobals.Global("spoiling")
    public static final SpoilManager INSTANCE = new SpoilManager();

    private SpoilManager() {
    }

//    @ZenCodeType.Method
//    public void addSpoiling(String name, IIngredient food, IItemStack spoilStack, int spoilTime) {
//        final ResourceLocation id = new ResourceLocation("crafttweaker", name);
//        final Ingredient foodIngredient = food.asVanillaIngredient();
//        final ItemStack resultItemStack = spoilStack.getInternal();
//        final SpoilRecipe recipe = new SpoilRecipe(id, "", foodIngredient, resultItemStack, spoilTime);
//        CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
//    }

//    @ZenCodeType.Method
//    public void addModSpoiling(String modName, IItemStack spoilStack, int spoilTime) {
//        if(ModList.get().isLoaded(modName)) {
//            for(Item foundItem : ForgeRegistries.ITEMS.getValues()) {
//                if(foundItem != spoilStack.getInternal().getItem() && foundItem.getRegistryName().getNamespace().equals(modName) && foundItem.isEdible()) {
//                    String itemLocation = foundItem.getRegistryName().toString().replace(":", "_");
//                    ResourceLocation id = new ResourceLocation("crafttweaker", itemLocation);
//                    SpoilRecipe recipe = new SpoilRecipe(id, "", Ingredient.of(new ItemStack(foundItem)), spoilStack.getInternal(), spoilTime);
//                    CraftTweakerAPI.apply(new ActionAddRecipe(this, recipe));
//                }
//            }
//        }
//    }

//    @Override
//    public RecipeType getRecipeType() {
//        return SpoiledRecipes.SPOIL_RECIPE_TYPE;
//    }
}
