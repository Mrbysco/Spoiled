import mods.spoiled.SpoilingManager;
import crafttweaker.api.item.IItemStack;

#spoilManager.addSpoiling("name", <item:minecraft:>, <item:minecraft:>, x);

var spoilManager = <recipetype:spoiled:spoil_recipe>;

#MinecraftStandard:
#-------------------------------------------------10Days-------------------------------------------------
#To Air
#To Item

#-------------------------------------------------7Days-------------------------------------------------

#To Air
spoilManager.addSpoiling("spider_eye_spoil", 		<item:minecraft:spider_eye>, 		<item:minecraft:air>, 280);

#To Item
spoilManager.addSpoiling("golden_apple_spoil", 					<item:minecraft:golden_apple>, 					<item:minecraft:gold_nugget>, 280);
spoilManager.addSpoiling("glistering_melon_slice_spol", 		<item:minecraft:glistering_melon_slice>, 		<item:minecraft:gold_nugget>, 280);

#-------------------------------------------------6Days-------------------------------------------------

#To Air
#spoilManager.addSpoiling("cookie_spoil", 			<item:minecraft:cookie>, 			<item:minecraft:air>, 240);

#To Item

#-------------------------------------------------5Days-------------------------------------------------

#To Air
spoilManager.addSpoiling("bread_spoil", 			<item:minecraft:bread>, 			<item:minecraft:air>, 200);

#To Item
spoilManager.addSpoiling("cooked_porkchop_spoil", 	<item:minecraft:cooked_porkchop>, 	<item:minecraft:rotten_flesh>, 200);
spoilManager.addSpoiling("cooked_beef_spoil", 		<item:minecraft:cooked_beef>, 		<item:minecraft:rotten_flesh>, 200);
spoilManager.addSpoiling("cooked_rabbit_spoil", 	<item:minecraft:cooked_rabbit>, 	<item:minecraft:rotten_flesh>, 200);
spoilManager.addSpoiling("cooked_mutton_spoil", 	<item:minecraft:cooked_mutton>, 	<item:minecraft:rotten_flesh>, 200);
spoilManager.addSpoiling("beetroot_soup_spoil", 	<item:minecraft:beetroot_soup>, 	<item:minecraft:suspicious_stew>, 200);

#-------------------------------------------------4Days-------------------------------------------------
#To Air
spoilManager.addSpoiling("cooked_cod_spoil", 		<item:minecraft:cooked_cod>, 		<item:minecraft:air>, 160);
spoilManager.addSpoiling("dried_kelp_spoil",		<item:minecraft:dried_kelp>, 		<item:minecraft:air>, 160);
spoilManager.addSpoiling("dried_kelp_block_spoil",	<item:minecraft:dried_kelp_block>, 	<item:minecraft:air>, 160);
spoilManager.addSpoiling("popped_chorus_fruit_spoil",	<item:minecraft:popped_chorus_fruit>, 	<item:minecraft:air>, 160);

#To Item
spoilManager.addSpoiling("melon_spoil", 			<item:minecraft:melon>, 			<item:minecraft:melon_seeds>, 160);
spoilManager.addSpoiling("pumpkin_spoil", 			<item:minecraft:pumpkin>, 			<item:minecraft:pumpkin_seeds>, 160);
spoilManager.addSpoiling("baked_potato_spoil", 		<item:minecraft:baked_potato>, 		<item:minecraft:poisonous_potato>, 160);
spoilManager.addSpoiling("golden_carrot_spoil", 	<item:minecraft:golden_carrot>, 	<item:minecraft:gold_nugget>, 160);

#-------------------------------------------------3Days-------------------------------------------------
#To Air
spoilManager.addSpoiling("appples_spoil", 			<item:minecraft:apple>, 			<item:minecraft:air>, 120);
spoilManager.addSpoiling("carrots_spoil", 			<item:minecraft:carrot>, 			<item:minecraft:air>, 120);
spoilManager.addSpoiling("beetroot_spoil", 			<item:minecraft:beetroot>, 			<item:minecraft:air>, 120);

#To Item
spoilManager.addSpoiling("melon_slice_spoil", 		<item:minecraft:melon_slice>, 		<item:minecraft:melon_seeds>, 120);
spoilManager.addSpoiling("potato_spoil", 			<item:minecraft:potato>, 			<item:minecraft:poisonous_potato>, 120);
spoilManager.addSpoiling("carrot_on_a_stick_spoil",	<item:minecraft:carrot_on_a_stick>, <item:minecraft:fishing_rod>, 120);

#--bucketThings--
spoilManager.addSpoiling("tropical_fish_bucket_spoil", 		<item:minecraft:tropical_fish_bucket>, 	<item:minecraft:water_bucket>, 120);
spoilManager.addSpoiling("pufferfish_bucket_spoil", 		<item:minecraft:pufferfish_bucket>, 	<item:minecraft:water_bucket>, 120);
spoilManager.addSpoiling("cod_bucket_spoil", 				<item:minecraft:cod_bucket>, 			<item:minecraft:water_bucket>, 120);
spoilManager.addSpoiling("salmon_bucket_spoil", 			<item:minecraft:salmon_bucket>, 		<item:minecraft:water_bucket>, 120);
spoilManager.addSpoiling("axolotl_bucket_spoil", 			<item:minecraft:axolotl_bucket>, 		<item:minecraft:water_bucket>, 120);

spoilManager.addSpoiling("milk_bucket_spoil", 				<item:minecraft:milk_bucket>, 			<item:minecraft:bucket>, 120);

#-------------------------------------------------2Days-------------------------------------------------
#To Air
spoilManager.addSpoiling("brown_shroom_spoil", 		<item:minecraft:brown_mushroom>, 		<item:minecraft:air>, 80);
spoilManager.addSpoiling("brown_shroom_block_spoil",<item:minecraft:brown_mushroom_block>, 	<item:minecraft:air>, 80);
spoilManager.addSpoiling("red_shroom_spoil", 		<item:minecraft:red_mushroom>, 			<item:minecraft:air>, 80);
spoilManager.addSpoiling("red_shroom_block_spoil", 	<item:minecraft:red_mushroom_block>, 	<item:minecraft:air>, 80);
spoilManager.addSpoiling("mushroom_stem_spoil", 	<item:minecraft:mushroom_stem>, 		<item:minecraft:air>, 80);
spoilManager.addSpoiling("sea_pickle_spoil", 		<item:minecraft:sea_pickle>, 			<item:minecraft:air>, 80);

spoilManager.addSpoiling("sweet_berry_spoil", 		<item:minecraft:sweet_berries>, 	<item:minecraft:air>, 80);
spoilManager.addSpoiling("chorus_fruit_spoil", 		<item:minecraft:chorus_fruit>, 		<item:minecraft:air>, 80);
spoilManager.addSpoiling("glow_berry_spoil", 		<item:minecraft:glow_berries>, 		<item:minecraft:air>, 80);
spoilManager.addSpoiling("cake_spoil", 				<item:minecraft:cake>, 				<item:minecraft:air>, 80);
spoilManager.addSpoiling("pumpkin_pie_spoil", 		<item:minecraft:pumpkin_pie>, 		<item:minecraft:air>, 80);
spoilManager.addSpoiling("kelp_spoil", 				<item:minecraft:kelp>, 				<item:minecraft:air>, 80);

#To Item
spoilManager.addSpoiling("mushroom_stew_spoil", 	<item:minecraft:mushroom_stew>, 	<item:minecraft:suspicious_stew>, 80);
spoilManager.addSpoiling("rabbit_stew_spoil", 		<item:minecraft:rabbit_stew>, 		<item:minecraft:suspicious_stew>, 80);


#-------------------------------------------------1Day-------------------------------------------------
#To Air
spoilManager.addSpoiling("turtle_egg_spoil", 		<item:minecraft:turtle_egg>, 		<item:minecraft:air>, 40);
spoilManager.addSpoiling("egg_spoil", 				<item:minecraft:egg>, 				<item:minecraft:air>, 40);

spoilManager.addSpoiling("fermented_spider_eye_spoil", 				<item:minecraft:fermented_spider_eye>, 				<item:minecraft:air>, 40);

#To Item
spoilManager.addSpoiling("raw_porkchop_spoil", 		<item:minecraft:porkchop>, 			<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_beef_spoil", 			<item:minecraft:beef>, 				<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_chicken_spoil", 		<item:minecraft:chicken>, 			<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_rabbit_spoil", 		<item:minecraft:rabbit>, 			<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_mutton_spoil", 		<item:minecraft:mutton>, 			<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_cod_spoil", 			<item:minecraft:cod>, 				<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_salmon_spoil", 		<item:minecraft:salmon>, 			<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_tropical_fish_spoil", <item:minecraft:tropical_fish>, 	<item:minecraft:rotten_flesh>, 40);
spoilManager.addSpoiling("raw_puffer_fish_spoil", 	<item:minecraft:pufferfish>, 		<item:minecraft:rotten_flesh>, 40);

#-------------------------------------------------1/2Day-------------------------------------------------
#To Air

#To Item
spoilManager.addSpoiling("powder_snow_bucket_melt", 		<item:minecraft:powder_snow_bucket>, 		<item:minecraft:water_bucket>, 20);
spoilManager.addSpoiling("mushroom_sus_stew_spoil", 		<item:minecraft:suspicious_stew>, 			<item:minecraft:bowl>, 20);