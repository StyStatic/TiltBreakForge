package net.minecraft.client.quickplay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class QuickPlayLog {
   private static final QuickPlayLog f_278416_ = new QuickPlayLog("") {
      public void m_278768_(Minecraft p_279484_) {
      }

      public void m_278642_(QuickPlayLog.Type p_279348_, String p_279305_, String p_279177_) {
      }
   };
   private static final Logger f_278438_ = LogUtils.getLogger();
   private static final Gson f_278422_ = (new GsonBuilder()).create();
   private final Path f_278473_;
   @Nullable
   private QuickPlayLog.QuickPlayWorld f_278423_;

   QuickPlayLog(String p_279463_) {
      this.f_278473_ = Minecraft.getInstance().gameDirectory.toPath().resolve(p_279463_);
   }

   public static QuickPlayLog m_278648_(@Nullable String p_279275_) {
      return p_279275_ == null ? f_278416_ : new QuickPlayLog(p_279275_);
   }

   public void m_278642_(QuickPlayLog.Type p_279380_, String p_279427_, String p_279470_) {
      this.f_278423_ = new QuickPlayLog.QuickPlayWorld(p_279380_, p_279427_, p_279470_);
   }

   public void m_278768_(Minecraft p_279258_) {
      if (p_279258_.gameMode != null && this.f_278423_ != null) {
         Util.ioPool().execute(() -> {
            try {
               Files.deleteIfExists(this.f_278473_);
            } catch (IOException ioexception) {
               f_278438_.error("Failed to delete quickplay log file {}", this.f_278473_, ioexception);
            }

            QuickPlayLog.QuickPlayEntry quickplaylog$quickplayentry = new QuickPlayLog.QuickPlayEntry(this.f_278423_, Instant.now(), p_279258_.gameMode.getPlayerMode());
            Codec.list(QuickPlayLog.QuickPlayEntry.f_278431_).encodeStart(JsonOps.INSTANCE, List.of(quickplaylog$quickplayentry)).resultOrPartial(Util.prefix("Quick Play: ", f_278438_::error)).ifPresent((p_279238_) -> {
               try {
                  Files.createDirectories(this.f_278473_.getParent());
                  Files.writeString(this.f_278473_, f_278422_.toJson(p_279238_));
               } catch (IOException ioexception1) {
                  f_278438_.error("Failed to write to quickplay log file {}", this.f_278473_, ioexception1);
               }

            });
         });
      } else {
         f_278438_.error("Failed to log session for quickplay. Missing world data or gamemode");
      }
   }

   @OnlyIn(Dist.CLIENT)
   static record QuickPlayEntry(QuickPlayLog.QuickPlayWorld f_278426_, Instant f_278512_, GameType f_278456_) {
      public static final Codec<QuickPlayLog.QuickPlayEntry> f_278431_ = RecordCodecBuilder.create((p_279196_) -> {
         return p_279196_.group(QuickPlayLog.QuickPlayWorld.f_278511_.forGetter(QuickPlayLog.QuickPlayEntry::f_278426_), ExtraCodecs.INSTANT_ISO8601.fieldOf("lastPlayedTime").forGetter(QuickPlayLog.QuickPlayEntry::f_278512_), GameType.CODEC.fieldOf("gamemode").forGetter(QuickPlayLog.QuickPlayEntry::f_278456_)).apply(p_279196_, QuickPlayLog.QuickPlayEntry::new);
      });
   }

   @OnlyIn(Dist.CLIENT)
   static record QuickPlayWorld(QuickPlayLog.Type f_278464_, String f_278460_, String f_278469_) {
      public static final MapCodec<QuickPlayLog.QuickPlayWorld> f_278511_ = RecordCodecBuilder.mapCodec((p_279181_) -> {
         return p_279181_.group(QuickPlayLog.Type.f_278494_.fieldOf("type").forGetter(QuickPlayLog.QuickPlayWorld::f_278464_), Codec.STRING.fieldOf("id").forGetter(QuickPlayLog.QuickPlayWorld::f_278460_), Codec.STRING.fieldOf("name").forGetter(QuickPlayLog.QuickPlayWorld::f_278469_)).apply(p_279181_, QuickPlayLog.QuickPlayWorld::new);
      });
   }

   @OnlyIn(Dist.CLIENT)
   public static enum Type implements StringRepresentable {
      SINGLEPLAYER("singleplayer"),
      MULTIPLAYER("multiplayer"),
      REALMS("realms");

      static final Codec<QuickPlayLog.Type> f_278494_ = StringRepresentable.fromEnum(QuickPlayLog.Type::values);
      private final String f_278427_;

      private Type(String p_279349_) {
         this.f_278427_ = p_279349_;
      }

      public String getSerializedName() {
         return this.f_278427_;
      }
   }
}