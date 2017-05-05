package me.itstheholyblack.testmodpleaseignore.crafting;

import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ShapelessRecipes {
	public static void initRecipes() {
		// Use this to declare shapeless crafting recipes
		GameRegistry.addShapelessRecipe(new ItemStack(ModItems.eArrow), new Object[] {Items.ARROW, Blocks.TNT});
	}
}
