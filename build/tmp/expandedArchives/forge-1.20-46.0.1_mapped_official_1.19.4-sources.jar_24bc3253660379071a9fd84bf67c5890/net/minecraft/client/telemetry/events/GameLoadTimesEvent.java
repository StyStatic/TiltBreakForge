package net.minecraft.client.telemetry.events;

import com.google.common.base.Stopwatch;
import com.google.common.base.Ticker;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalLong;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryProperty;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.slf4j.Logger;

@OnlyIn(Dist.CLIENT)
public class GameLoadTimesEvent {
   public static final GameLoadTimesEvent f_285635_ = new GameLoadTimesEvent(Ticker.systemTicker());
   private static final Logger f_285561_ = LogUtils.getLogger();
   private final Ticker f_285636_;
   private final Map<TelemetryProperty<GameLoadTimesEvent.Measurement>, Stopwatch> f_285659_ = new HashMap<>();
   private OptionalLong f_285644_ = OptionalLong.empty();

   protected GameLoadTimesEvent(Ticker p_286506_) {
      this.f_285636_ = p_286506_;
   }

   public synchronized void m_285833_(TelemetryProperty<GameLoadTimesEvent.Measurement> p_286394_) {
      this.m_285937_(p_286394_, (p_286494_) -> {
         return Stopwatch.createStarted(this.f_285636_);
      });
   }

   public synchronized void m_285977_(TelemetryProperty<GameLoadTimesEvent.Measurement> p_286396_, Stopwatch p_286822_) {
      this.m_285937_(p_286396_, (p_286421_) -> {
         return p_286822_;
      });
   }

   private synchronized void m_285937_(TelemetryProperty<GameLoadTimesEvent.Measurement> p_286311_, Function<TelemetryProperty<GameLoadTimesEvent.Measurement>, Stopwatch> p_286454_) {
      this.f_285659_.computeIfAbsent(p_286311_, p_286454_);
   }

   public synchronized void m_285901_(TelemetryProperty<GameLoadTimesEvent.Measurement> p_286634_) {
      Stopwatch stopwatch = this.f_285659_.get(p_286634_);
      if (stopwatch == null) {
         f_285561_.warn("Attempted to end step for {} before starting it", (Object)p_286634_.id());
      } else {
         if (stopwatch.isRunning()) {
            stopwatch.stop();
         }

      }
   }

   public void m_286019_(TelemetryEventSender p_286524_) {
      p_286524_.send(TelemetryEventType.f_285589_, (p_286285_) -> {
         synchronized(this) {
            this.f_285659_.forEach((p_286804_, p_286275_) -> {
               if (!p_286275_.isRunning()) {
                  long i = p_286275_.elapsed(TimeUnit.MILLISECONDS);
                  p_286285_.put(p_286804_, new GameLoadTimesEvent.Measurement((int)i));
               } else {
                  f_285561_.warn("Measurement {} was discarded since it was still ongoing when the event {} was sent.", p_286804_.id(), TelemetryEventType.f_285589_.id());
               }

            });
            this.f_285644_.ifPresent((p_286872_) -> {
               p_286285_.put(TelemetryProperty.f_285586_, new GameLoadTimesEvent.Measurement((int)p_286872_));
            });
            this.f_285659_.clear();
         }
      });
   }

   public synchronized void m_286069_(long p_286847_) {
      this.f_285644_ = OptionalLong.of(p_286847_);
   }

   @OnlyIn(Dist.CLIENT)
   public static record Measurement(int f_285578_) {
      public static final Codec<GameLoadTimesEvent.Measurement> f_285618_ = Codec.INT.xmap(GameLoadTimesEvent.Measurement::new, (p_286736_) -> {
         return p_286736_.f_285578_;
      });
   }
}