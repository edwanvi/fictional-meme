package me.itstheholyblack.testmodpleaseignore;

import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

import static java.lang.invoke.MethodHandles.publicLookup;

public class MethodHandles {
    public static final String[] POTIONHELPER_REGISTERPOTIONTYPECONVERSION = new String[]{"a", "func_185204_a",
            "registerPotionTypeConversion"};
    @Nonnull
    private static final MethodHandle registerPotionTypeConversion;

    static {
        try {
            FMLLog.log(Level.INFO, "Attempting to register method handles, hold on to your butts!");
            Method m = ReflectionHelper.findMethod(PotionHelper.class, null, POTIONHELPER_REGISTERPOTIONTYPECONVERSION,
                    PotionType.class, Predicate.class, PotionType.class);
            registerPotionTypeConversion = publicLookup().unreflect(m);
        } catch (Throwable t) {
            FMLLog.log(Level.ERROR, "Couldn't initialize methodhandles! Things will be broken!");
            t.printStackTrace();
            throw Throwables.propagate(t);
        }
    }

    // Register potion recipes
    public static void registerPotionTypeConversion(@Nonnull PotionType input,
                                                    @Nonnull Predicate<ItemStack> reagentPredicate, @Nonnull PotionType output) {
        try {
            registerPotionTypeConversion.invokeExact(input, reagentPredicate, output);
        } catch (Throwable t) {
            throw propagate(t);
        }
    }

    // error handler
    private static RuntimeException propagate(Throwable t) {
        FMLLog.log(Level.ERROR, "Methodhandle failed!");
        t.printStackTrace();
        return Throwables.propagate(t);
    }
}
