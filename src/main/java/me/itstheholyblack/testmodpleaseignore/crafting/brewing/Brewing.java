package me.itstheholyblack.testmodpleaseignore.crafting.brewing;

import java.util.List;
import javax.annotation.Nullable;
import com.google.common.base.Predicate;
import me.itstheholyblack.testmodpleaseignore.MethodHandles;
import me.itstheholyblack.testmodpleaseignore.items.ModItems;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class Brewing {
	private static Predicate<ItemStack> potionPredicate(ItemStack input) {
	    return stack -> OreDictionary.itemMatches(input, stack, false);
	}

	private static Predicate<ItemStack> potionPredicate(String input) {
	    List<ItemStack> ores = OreDictionary.getOres(input);
	    return (stack) -> {
	        boolean flag = false;
	        for (ItemStack ore : ores)
	            if (OreDictionary.itemMatches(ore, stack, false)) {
	                flag = true;
	                break;
	            }
	        return flag;
	    };
	}

	private static final Predicate<ItemStack> REDSTONE_PREDICATE = potionPredicate(new ItemStack(Items.REDSTONE));
	private static final Predicate<ItemStack> GLOWSTONE_PREDICATE = potionPredicate(new ItemStack(Items.GLOWSTONE_DUST));
	/**
	 * Method used to make potion recipes since Forge's is borked. 
	 * @author wiresegal
	 * @param predicate The input ingredient, ex. Nether Wart in water -> Awkward.
	 * @param fromType The type of potion this will be brewed from. Ex water in water -> Awkward.
	 * @param normalType The output potion. This is the effect that will be used for a potion that has not had redstone/glowstone/other modifiers applied.
	 * @param longType The potion type to use if the potion has been lengthened with Redstone.
	 * @param strongType The potion type to use if the potion has been strengthened with Glowstone.
	 */
	public static void addCompletePotionRecipes(Predicate<ItemStack> predicate, PotionType fromType, PotionType normalType, @Nullable PotionType longType, @Nullable PotionType strongType) {
	    if (fromType == PotionTypes.AWKWARD) {
	        MethodHandles.registerPotionTypeConversion(PotionTypes.WATER, predicate, PotionTypes.MUNDANE);
	    }
	    MethodHandles.registerPotionTypeConversion(fromType, predicate, normalType);
	    if (longType != null) MethodHandles.registerPotionTypeConversion(normalType, REDSTONE_PREDICATE, longType);
	    if (strongType != null) MethodHandles.registerPotionTypeConversion(normalType, GLOWSTONE_PREDICATE, strongType);
	}
	public static void addPotionConversionRecipes(Predicate<ItemStack> predicate, PotionType fromTypeNormal, @Nullable PotionType fromTypeLong, @Nullable PotionType fromTypeStrong, PotionType normalType, @Nullable PotionType longType, @Nullable PotionType strongType) {
	    addCompletePotionRecipes(predicate, fromTypeNormal, normalType, longType, strongType);
	    if (longType != null && fromTypeLong != null)
	        MethodHandles.registerPotionTypeConversion(fromTypeLong, predicate, longType);
	    if (strongType != null && fromTypeStrong != null)
	        MethodHandles.registerPotionTypeConversion(fromTypeStrong, predicate, strongType);
	}
}
