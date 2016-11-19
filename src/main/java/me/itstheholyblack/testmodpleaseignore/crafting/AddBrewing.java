package me.itstheholyblack.testmodpleaseignore.crafting;

import com.google.common.base.Predicate;
import me.itstheholyblack.testmodpleaseignore.crafting.Brewing;
import me.itstheholyblack.testmodpleaseignore.effect.ModEffects;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AddBrewing {
	private static ItemStack NETHER_WART;
	private static Predicate<ItemStack> WITHER_POWDER;

	public static void initRecipes() {
		NETHER_WART = new ItemStack(Items.NETHER_WART);
		//WITHER_POWDER = new ItemStack(ModItems.testingItem);
		WITHER_POWDER = potionPredicate(new ItemStack(ModItems.testingItem));
		Brewing.addCompletePotionRecipes(WITHER_POWDER, PotionTypes.AWKWARD, ModEffects.wither, ModEffects.witherLong, ModEffects.witherStrong);
	}

	private static Predicate<ItemStack> potionPredicate(ItemStack input) {
	    return stack -> OreDictionary.itemMatches(input, stack, false);
	}
}
