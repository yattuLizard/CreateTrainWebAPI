package eu.cronmoth.createtrainwebapi;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue SERVER_PORT = BUILDER
            .comment("Webserver Port")
            .defineInRange("serverPort", 8080, 1, 65535);

    public static final ForgeConfigSpec.ConfigValue<String> SERVER_HOST = BUILDER
            .comment("Webserver hostname")
            .define("serverHost", "0.0.0.0");

    public static final ForgeConfigSpec.ConfigValue<String> TRAIN_MODEL_PATH = BUILDER
            .comment("Path of the train models")
            .define("trainModelPath", "bluemap/train_models/");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static boolean validateItemName(final Object obj) {
        if (!(obj instanceof String itemName)) {
            return false;
        }

        try {
            return BuiltInRegistries.ITEM.containsKey(new ResourceLocation(itemName));
        } catch (ResourceLocationException e) {
            return false;
        }
    }

}
