package net.minecraft.client.gui.font.providers;

import com.mojang.blaze3d.font.GlyphProvider;
import com.mojang.blaze3d.font.TrueTypeGlyphProvider;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryUtil;

@OnlyIn(Dist.CLIENT)
public record TrueTypeGlyphProviderDefinition(ResourceLocation f_285564_, float f_285590_, float f_285576_, TrueTypeGlyphProviderDefinition.Shift f_285584_, String f_285657_) implements GlyphProviderDefinition {
   private static final Codec<String> f_285652_ = Codec.either(Codec.STRING, Codec.STRING.listOf()).xmap((p_286728_) -> {
      return p_286728_.map((p_286306_) -> {
         return p_286306_;
      }, (p_286852_) -> {
         return String.join("", p_286852_);
      });
   }, Either::left);
   public static final MapCodec<TrueTypeGlyphProviderDefinition> f_285645_ = RecordCodecBuilder.mapCodec((p_286284_) -> {
      return p_286284_.group(ResourceLocation.CODEC.fieldOf("file").forGetter(TrueTypeGlyphProviderDefinition::f_285564_), Codec.FLOAT.optionalFieldOf("size", Float.valueOf(11.0F)).forGetter(TrueTypeGlyphProviderDefinition::f_285590_), Codec.FLOAT.optionalFieldOf("oversample", Float.valueOf(1.0F)).forGetter(TrueTypeGlyphProviderDefinition::f_285576_), TrueTypeGlyphProviderDefinition.Shift.f_285647_.optionalFieldOf("shift", TrueTypeGlyphProviderDefinition.Shift.f_285613_).forGetter(TrueTypeGlyphProviderDefinition::f_285584_), f_285652_.optionalFieldOf("skip", "").forGetter(TrueTypeGlyphProviderDefinition::f_285657_)).apply(p_286284_, TrueTypeGlyphProviderDefinition::new);
   });

   public GlyphProviderType m_285843_() {
      return GlyphProviderType.TTF;
   }

   public Either<GlyphProviderDefinition.Loader, GlyphProviderDefinition.Reference> m_285782_() {
      return Either.left(this::m_285764_);
   }

   private GlyphProvider m_285764_(ResourceManager p_286229_) throws IOException {
      STBTTFontinfo stbttfontinfo = null;
      ByteBuffer bytebuffer = null;

      try (InputStream inputstream = p_286229_.open(this.f_285564_.withPrefix("font/"))) {
         stbttfontinfo = STBTTFontinfo.malloc();
         bytebuffer = TextureUtil.readResource(inputstream);
         bytebuffer.flip();
         if (!STBTruetype.stbtt_InitFont(stbttfontinfo, bytebuffer)) {
            throw new IOException("Invalid ttf");
         } else {
            return new TrueTypeGlyphProvider(bytebuffer, stbttfontinfo, this.f_285590_, this.f_285576_, this.f_285584_.f_285596_, this.f_285584_.f_285597_, this.f_285657_);
         }
      } catch (Exception exception) {
         if (stbttfontinfo != null) {
            stbttfontinfo.free();
         }

         MemoryUtil.memFree(bytebuffer);
         throw exception;
      }
   }

   @OnlyIn(Dist.CLIENT)
   public static record Shift(float f_285596_, float f_285597_) {
      public static final TrueTypeGlyphProviderDefinition.Shift f_285613_ = new TrueTypeGlyphProviderDefinition.Shift(0.0F, 0.0F);
      public static final Codec<TrueTypeGlyphProviderDefinition.Shift> f_285647_ = Codec.FLOAT.listOf().comapFlatMap((p_286374_) -> {
         return Util.fixedSize(p_286374_, 2).map((p_286746_) -> {
            return new TrueTypeGlyphProviderDefinition.Shift(p_286746_.get(0), p_286746_.get(1));
         });
      }, (p_286274_) -> {
         return List.of(p_286274_.f_285596_, p_286274_.f_285597_);
      });
   }
}