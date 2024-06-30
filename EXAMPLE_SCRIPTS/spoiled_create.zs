import mods.spoiled.SpoilingManager;
import crafttweaker.api.item.IItemStack;

#createSpoilManager.addSpoiling("name", <item:culturaldelights:>, <item:minecraft:>, x);

var createSpoilManager = <recipetype:spoiled:spoil_recipe>;

createSpoilManager.addSpoiling("bar_of_chocolate_spoil", <item:create:bar_of_chocolate>, <item:minecraft:air>, 800);
createSpoilManager.addSpoiling("builders_tea_spoil", <item:create:builders_tea>, <item:minecraft:glass_bottle:>, 200);
createSpoilManager.addSpoiling("honeyed_apple_spoil", <item:create:honeyed_apple>, <item:minecraft:air>, 160);
createSpoilManager.addSpoiling("chocolate_glazed_berries_spoil", <item:create:chocolate_glazed_berries>, <item:minecraft:air>, 120);
createSpoilManager.addSpoiling("sweet_roll_spoil", <item:create:sweet_roll>, <item:minecraft:air>, 120);
