package me.itstheholyblack.testmodpleaseignore.crafting.brewing;

import com.google.common.base.Predicate;
import me.itstheholyblack.testmodpleaseignore.effect.ModEffects;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class AddBrewing {
    private static Predicate<ItemStack> WITHER_POWDER;

    public static void initRecipes() {
        WITHER_POWDER = potionPredicate(new ItemStack(ModItems.testingItem));
        Brewing.addCompletePotionRecipes(WITHER_POWDER, PotionTypes.WATER, ModEffects.wither, ModEffects.witherLong,
                ModEffects.witherStrong);
    }

    private static Predicate<ItemStack> potionPredicate(ItemStack input) {
        return stack -> OreDictionary.itemMatches(input, stack, false);
    }
}
