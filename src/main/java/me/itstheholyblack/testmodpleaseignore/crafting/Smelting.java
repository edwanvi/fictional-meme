package me.itstheholyblack.testmodpleaseignore.crafting;

import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Smelting {
    public static void initRecipies() {
        GameRegistry.addSmelting(new ItemStack(Items.SKULL, 1, 1), new ItemStack(ModItems.testingItem, 2), 0);
    }
}
