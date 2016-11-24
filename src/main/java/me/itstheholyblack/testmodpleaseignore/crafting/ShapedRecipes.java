package me.itstheholyblack.testmodpleaseignore.crafting;

import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ShapedRecipes {
	public static void initRecipes() {
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.leapingboots), new Object[] {"   ", "# #", "# #", '#', ModItems.endHide});
		GameRegistry.addShapedRecipe(new ItemStack(ModItems.endHide), new Object[] {" . ", ".#.", " . ", '.', Items.ENDER_PEARL, '#', Items.LEATHER});
	}
}
