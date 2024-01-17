package com.mrbysco.spoiled.compat.rei;

import com.mrbysco.spoiled.Constants;
import com.mrbysco.spoiled.compat.rei.category.SpoilCategoryForge;
import com.mrbysco.spoiled.compat.rei.display.SpoilDisplayForge;
import com.mrbysco.spoiled.recipe.SpoilRecipe;
import com.mrbysco.spoiled.registration.SpoiledRecipes;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

@REIPluginClient
public class REIPluginForge implements REIClientPlugin {
    public static final CategoryIdentifier<SpoilDisplayForge> SPOILING = CategoryIdentifier.of(Constants.MOD_ID, "plugins/spoiling");

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new SpoilCategoryForge());

        registry.addWorkstations(SPOILING, EntryStacks.of(Items.ROTTEN_FLESH));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        }
        RegistryAccess registryAccess = level.registryAccess();

        List<RecipeHolder<SpoilRecipe>> spoilHolders = registry.getRecipeManager().getAllRecipesFor(SpoiledRecipes.SPOIL_RECIPE_TYPE.get());
        spoilHolders.forEach((holder) -> {
            SpoilRecipe recipe = holder.value();
            registry.add(new SpoilDisplayForge(
                            recipe.getIngredients().get(0),
                            recipe.getResultItem(registryAccess)
                    )
            );
        });
    }
}
