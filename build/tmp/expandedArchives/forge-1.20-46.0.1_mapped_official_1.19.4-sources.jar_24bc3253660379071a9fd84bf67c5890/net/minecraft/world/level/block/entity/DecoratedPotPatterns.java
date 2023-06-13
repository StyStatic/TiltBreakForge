package net.minecraft.world.level.block.entity;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class DecoratedPotPatterns {
   private static final String BASE_NAME = "decorated_pot_base";
   public static final ResourceKey<String> BASE = create("decorated_pot_base");
   private static final String BRICK_NAME = "decorated_pot_side";
   private static final String f_276473_ = "angler_pottery_pattern";
   private static final String ARCHER_NAME = "archer_pottery_pattern";
   private static final String ARMS_UP_NAME = "arms_up_pottery_pattern";
   private static final String f_276417_ = "blade_pottery_pattern";
   private static final String f_276453_ = "brewer_pottery_pattern";
   private static final String f_276515_ = "burn_pottery_pattern";
   private static final String f_276429_ = "danger_pottery_pattern";
   private static final String f_276483_ = "explorer_pottery_pattern";
   private static final String f_276663_ = "friend_pottery_pattern";
   private static final String f_276529_ = "heart_pottery_pattern";
   private static final String f_276623_ = "heartbreak_pottery_pattern";
   private static final String f_276681_ = "howl_pottery_pattern";
   private static final String f_276576_ = "miner_pottery_pattern";
   private static final String f_276456_ = "mourner_pottery_pattern";
   private static final String f_276418_ = "plenty_pottery_pattern";
   private static final String PRIZE_NAME = "prize_pottery_pattern";
   private static final String f_276437_ = "sheaf_pottery_pattern";
   private static final String f_276641_ = "shelter_pottery_pattern";
   private static final String SKULL_NAME = "skull_pottery_pattern";
   private static final String f_276521_ = "snort_pottery_pattern";
   private static final ResourceKey<String> BRICK = create("decorated_pot_side");
   private static final ResourceKey<String> f_276653_ = create("angler_pottery_pattern");
   private static final ResourceKey<String> ARCHER = create("archer_pottery_pattern");
   private static final ResourceKey<String> ARMS_UP = create("arms_up_pottery_pattern");
   private static final ResourceKey<String> f_276511_ = create("blade_pottery_pattern");
   private static final ResourceKey<String> f_276620_ = create("brewer_pottery_pattern");
   private static final ResourceKey<String> f_276550_ = create("burn_pottery_pattern");
   private static final ResourceKey<String> f_276639_ = create("danger_pottery_pattern");
   private static final ResourceKey<String> f_276482_ = create("explorer_pottery_pattern");
   private static final ResourceKey<String> f_276523_ = create("friend_pottery_pattern");
   private static final ResourceKey<String> f_276534_ = create("heart_pottery_pattern");
   private static final ResourceKey<String> f_276578_ = create("heartbreak_pottery_pattern");
   private static final ResourceKey<String> f_276474_ = create("howl_pottery_pattern");
   private static final ResourceKey<String> f_276676_ = create("miner_pottery_pattern");
   private static final ResourceKey<String> f_276687_ = create("mourner_pottery_pattern");
   private static final ResourceKey<String> f_276584_ = create("plenty_pottery_pattern");
   private static final ResourceKey<String> PRIZE = create("prize_pottery_pattern");
   private static final ResourceKey<String> f_276682_ = create("sheaf_pottery_pattern");
   private static final ResourceKey<String> f_276640_ = create("shelter_pottery_pattern");
   private static final ResourceKey<String> SKULL = create("skull_pottery_pattern");
   private static final ResourceKey<String> f_276644_ = create("snort_pottery_pattern");
   private static final Map<Item, ResourceKey<String>> ITEM_TO_POT_TEXTURE = Map.ofEntries(Map.entry(Items.BRICK, BRICK), Map.entry(Items.f_279633_, f_276653_), Map.entry(Items.f_279642_, ARCHER), Map.entry(Items.f_279634_, ARMS_UP), Map.entry(Items.f_279567_, f_276511_), Map.entry(Items.f_279583_, f_276620_), Map.entry(Items.f_279650_, f_276550_), Map.entry(Items.f_279619_, f_276639_), Map.entry(Items.f_279616_, f_276482_), Map.entry(Items.f_279584_, f_276523_), Map.entry(Items.f_279623_, f_276534_), Map.entry(Items.f_279606_, f_276578_), Map.entry(Items.f_279598_, f_276474_), Map.entry(Items.f_279559_, f_276676_), Map.entry(Items.f_279560_, f_276687_), Map.entry(Items.f_279647_, f_276584_), Map.entry(Items.f_279528_, PRIZE), Map.entry(Items.f_279545_, f_276682_), Map.entry(Items.f_279529_, f_276640_), Map.entry(Items.f_279570_, SKULL), Map.entry(Items.f_279636_, f_276644_));

   private static ResourceKey<String> create(String p_272919_) {
      return ResourceKey.create(Registries.DECORATED_POT_PATTERNS, new ResourceLocation(p_272919_));
   }

   public static ResourceLocation location(ResourceKey<String> p_273198_) {
      return p_273198_.location().withPrefix("entity/decorated_pot/");
   }

   @Nullable
   public static ResourceKey<String> getResourceKey(Item p_273094_) {
      return ITEM_TO_POT_TEXTURE.get(p_273094_);
   }

   public static String bootstrap(Registry<String> p_273479_) {
      Registry.register(p_273479_, BRICK, "decorated_pot_side");
      Registry.register(p_273479_, f_276653_, "angler_pottery_pattern");
      Registry.register(p_273479_, ARCHER, "archer_pottery_pattern");
      Registry.register(p_273479_, ARMS_UP, "arms_up_pottery_pattern");
      Registry.register(p_273479_, f_276511_, "blade_pottery_pattern");
      Registry.register(p_273479_, f_276620_, "brewer_pottery_pattern");
      Registry.register(p_273479_, f_276550_, "burn_pottery_pattern");
      Registry.register(p_273479_, f_276639_, "danger_pottery_pattern");
      Registry.register(p_273479_, f_276482_, "explorer_pottery_pattern");
      Registry.register(p_273479_, f_276523_, "friend_pottery_pattern");
      Registry.register(p_273479_, f_276534_, "heart_pottery_pattern");
      Registry.register(p_273479_, f_276578_, "heartbreak_pottery_pattern");
      Registry.register(p_273479_, f_276474_, "howl_pottery_pattern");
      Registry.register(p_273479_, f_276676_, "miner_pottery_pattern");
      Registry.register(p_273479_, f_276687_, "mourner_pottery_pattern");
      Registry.register(p_273479_, f_276584_, "plenty_pottery_pattern");
      Registry.register(p_273479_, PRIZE, "prize_pottery_pattern");
      Registry.register(p_273479_, f_276682_, "sheaf_pottery_pattern");
      Registry.register(p_273479_, f_276640_, "shelter_pottery_pattern");
      Registry.register(p_273479_, SKULL, "skull_pottery_pattern");
      Registry.register(p_273479_, f_276644_, "snort_pottery_pattern");
      return Registry.register(p_273479_, BASE, "decorated_pot_base");
   }
}