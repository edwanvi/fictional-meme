package me.itstheholyblack.testmodpleaseignore.crafting;

import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ShapedRecipies {
	public static void initRecipies() {
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.witheredWater), new Object[] {ModItems.testingItem, new ItemStack(Items.POTIONITEM, 1, 0)});
	}
}
