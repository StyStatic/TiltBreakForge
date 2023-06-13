package net.minecraft.world.level.gameevent.vibrations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Optional;
import java.util.function.ToIntFunction;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public interface VibrationSystem {
   GameEvent[] f_279664_ = new GameEvent[]{GameEvent.f_276553_, GameEvent.f_276530_, GameEvent.f_276691_, GameEvent.f_276533_, GameEvent.f_276695_, GameEvent.f_276518_, GameEvent.f_276655_, GameEvent.f_276494_, GameEvent.f_276419_, GameEvent.f_276431_, GameEvent.f_276621_, GameEvent.f_276548_, GameEvent.f_276569_, GameEvent.f_276454_, GameEvent.f_276436_};
   ToIntFunction<GameEvent> f_279561_ = Util.make(new Object2IntOpenHashMap<>(), (p_282267_) -> {
      p_282267_.defaultReturnValue(0);
      p_282267_.put(GameEvent.STEP, 1);
      p_282267_.put(GameEvent.SWIM, 1);
      p_282267_.put(GameEvent.FLAP, 1);
      p_282267_.put(GameEvent.PROJECTILE_LAND, 2);
      p_282267_.put(GameEvent.HIT_GROUND, 2);
      p_282267_.put(GameEvent.SPLASH, 2);
      p_282267_.put(GameEvent.ITEM_INTERACT_FINISH, 3);
      p_282267_.put(GameEvent.PROJECTILE_SHOOT, 3);
      p_282267_.put(GameEvent.INSTRUMENT_PLAY, 3);
      p_282267_.put(GameEvent.ENTITY_ROAR, 4);
      p_282267_.put(GameEvent.ENTITY_SHAKE, 4);
      p_282267_.put(GameEvent.ELYTRA_GLIDE, 4);
      p_282267_.put(GameEvent.ENTITY_DISMOUNT, 5);
      p_282267_.put(GameEvent.EQUIP, 5);
      p_282267_.put(GameEvent.ENTITY_INTERACT, 6);
      p_282267_.put(GameEvent.SHEAR, 6);
      p_282267_.put(GameEvent.ENTITY_MOUNT, 6);
      p_282267_.put(GameEvent.ENTITY_DAMAGE, 7);
      p_282267_.put(GameEvent.DRINK, 8);
      p_282267_.put(GameEvent.EAT, 8);
      p_282267_.put(GameEvent.CONTAINER_CLOSE, 9);
      p_282267_.put(GameEvent.BLOCK_CLOSE, 9);
      p_282267_.put(GameEvent.BLOCK_DEACTIVATE, 9);
      p_282267_.put(GameEvent.BLOCK_DETACH, 9);
      p_282267_.put(GameEvent.CONTAINER_OPEN, 10);
      p_282267_.put(GameEvent.BLOCK_OPEN, 10);
      p_282267_.put(GameEvent.BLOCK_ACTIVATE, 10);
      p_282267_.put(GameEvent.BLOCK_ATTACH, 10);
      p_282267_.put(GameEvent.PRIME_FUSE, 10);
      p_282267_.put(GameEvent.NOTE_BLOCK_PLAY, 10);
      p_282267_.put(GameEvent.BLOCK_CHANGE, 11);
      p_282267_.put(GameEvent.BLOCK_DESTROY, 12);
      p_282267_.put(GameEvent.FLUID_PICKUP, 12);
      p_282267_.put(GameEvent.BLOCK_PLACE, 13);
      p_282267_.put(GameEvent.FLUID_PLACE, 13);
      p_282267_.put(GameEvent.ENTITY_PLACE, 14);
      p_282267_.put(GameEvent.LIGHTNING_STRIKE, 14);
      p_282267_.put(GameEvent.TELEPORT, 14);
      p_282267_.put(GameEvent.ENTITY_DIE, 15);
      p_282267_.put(GameEvent.EXPLODE, 15);

      for(int i = 1; i <= 15; ++i) {
         p_282267_.put(m_280393_(i), i);
      }

   });

   VibrationSystem.Data m_280002_();

   VibrationSystem.User m_280445_();

   static int m_280122_(GameEvent p_281355_) {
      return f_279561_.applyAsInt(p_281355_);
   }

   static GameEvent m_280393_(int p_282105_) {
      return f_279664_[p_282105_ - 1];
   }

   static int m_280007_(float p_282483_, int p_282722_) {
      double d0 = 15.0D / (double)p_282722_;
      return Math.max(1, 15 - Mth.floor(d0 * (double)p_282483_));
   }

   public static final class Data {
      public static Codec<VibrationSystem.Data> f_279637_ = RecordCodecBuilder.create((p_283387_) -> {
         return p_283387_.group(VibrationInfo.CODEC.optionalFieldOf("event").forGetter((p_281665_) -> {
            return Optional.ofNullable(p_281665_.f_279652_);
         }), VibrationSelector.CODEC.fieldOf("selector").forGetter(VibrationSystem.Data::m_280457_), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(0).forGetter(VibrationSystem.Data::m_280274_)).apply(p_283387_, (p_281934_, p_282381_, p_282931_) -> {
            return new VibrationSystem.Data(p_281934_.orElse((VibrationInfo)null), p_282381_, p_282931_, true);
         });
      });
      public static final String f_279525_ = "listener";
      @Nullable
      VibrationInfo f_279652_;
      private int f_279638_;
      final VibrationSelector f_279593_;
      private boolean f_279613_;

      private Data(@Nullable VibrationInfo p_281967_, VibrationSelector p_283036_, int p_283607_, boolean p_282438_) {
         this.f_279652_ = p_281967_;
         this.f_279638_ = p_283607_;
         this.f_279593_ = p_283036_;
         this.f_279613_ = p_282438_;
      }

      public Data() {
         this((VibrationInfo)null, new VibrationSelector(), 0, false);
      }

      public VibrationSelector m_280457_() {
         return this.f_279593_;
      }

      @Nullable
      public VibrationInfo m_280602_() {
         return this.f_279652_;
      }

      public void m_280036_(@Nullable VibrationInfo p_282049_) {
         this.f_279652_ = p_282049_;
      }

      public int m_280274_() {
         return this.f_279638_;
      }

      public void m_280178_(int p_282973_) {
         this.f_279638_ = p_282973_;
      }

      public void m_280502_() {
         this.f_279638_ = Math.max(0, this.f_279638_ - 1);
      }

      public boolean m_280616_() {
         return this.f_279613_;
      }

      public void m_280671_(boolean p_281702_) {
         this.f_279613_ = p_281702_;
      }
   }

   public static class Listener implements GameEventListener {
      private final VibrationSystem f_279547_;

      public Listener(VibrationSystem p_281843_) {
         this.f_279547_ = p_281843_;
      }

      public PositionSource getListenerSource() {
         return this.f_279547_.m_280445_().m_280010_();
      }

      public int getListenerRadius() {
         return this.f_279547_.m_280445_().m_280351_();
      }

      public boolean handleGameEvent(ServerLevel p_282254_, GameEvent p_283599_, GameEvent.Context p_283664_, Vec3 p_282426_) {
         VibrationSystem.Data vibrationsystem$data = this.f_279547_.m_280002_();
         VibrationSystem.User vibrationsystem$user = this.f_279547_.m_280445_();
         if (vibrationsystem$data.m_280602_() != null) {
            return false;
         } else if (!vibrationsystem$user.m_280612_(p_283599_, p_283664_)) {
            return false;
         } else {
            Optional<Vec3> optional = vibrationsystem$user.m_280010_().getPosition(p_282254_);
            if (optional.isEmpty()) {
               return false;
            } else {
               Vec3 vec3 = optional.get();
               if (!vibrationsystem$user.m_280080_(p_282254_, BlockPos.containing(p_282426_), p_283599_, p_283664_)) {
                  return false;
               } else if (m_280258_(p_282254_, p_282426_, vec3)) {
                  return false;
               } else {
                  this.m_280099_(p_282254_, vibrationsystem$data, p_283599_, p_283664_, p_282426_, vec3);
                  return true;
               }
            }
         }
      }

      public void m_280275_(ServerLevel p_282808_, GameEvent p_281875_, GameEvent.Context p_281652_, Vec3 p_281530_) {
         this.f_279547_.m_280445_().m_280010_().getPosition(p_282808_).ifPresent((p_281936_) -> {
            this.m_280099_(p_282808_, this.f_279547_.m_280002_(), p_281875_, p_281652_, p_281530_, p_281936_);
         });
      }

      private void m_280099_(ServerLevel p_282037_, VibrationSystem.Data p_283229_, GameEvent p_281778_, GameEvent.Context p_283344_, Vec3 p_281758_, Vec3 p_282990_) {
         p_283229_.f_279593_.addCandidate(new VibrationInfo(p_281778_, (float)p_281758_.distanceTo(p_282990_), p_281758_, p_283344_.sourceEntity()), p_282037_.getGameTime());
      }

      public static float m_280659_(BlockPos p_282413_, BlockPos p_281960_) {
         return (float)Math.sqrt(p_282413_.distSqr(p_281960_));
      }

      private static boolean m_280258_(Level p_283225_, Vec3 p_283328_, Vec3 p_283163_) {
         Vec3 vec3 = new Vec3((double)Mth.floor(p_283328_.x) + 0.5D, (double)Mth.floor(p_283328_.y) + 0.5D, (double)Mth.floor(p_283328_.z) + 0.5D);
         Vec3 vec31 = new Vec3((double)Mth.floor(p_283163_.x) + 0.5D, (double)Mth.floor(p_283163_.y) + 0.5D, (double)Mth.floor(p_283163_.z) + 0.5D);

         for(Direction direction : Direction.values()) {
            Vec3 vec32 = vec3.relative(direction, (double)1.0E-5F);
            if (p_283225_.isBlockInLine(new ClipBlockStateContext(vec32, vec31, (p_283608_) -> {
               return p_283608_.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS);
            })).getType() != HitResult.Type.BLOCK) {
               return false;
            }
         }

         return true;
      }
   }

   public interface Ticker {
      static void m_280259_(Level p_281704_, VibrationSystem.Data p_282633_, VibrationSystem.User p_281564_) {
         if (p_281704_ instanceof ServerLevel serverlevel) {
            if (p_282633_.f_279652_ == null) {
               m_280634_(serverlevel, p_282633_, p_281564_);
            }

            if (p_282633_.f_279652_ != null) {
               boolean flag = p_282633_.m_280274_() > 0;
               m_280404_(serverlevel, p_282633_, p_281564_);
               p_282633_.m_280502_();
               if (p_282633_.m_280274_() <= 0) {
                  flag = m_280174_(serverlevel, p_282633_, p_281564_, p_282633_.f_279652_);
               }

               if (flag) {
                  p_281564_.m_280022_();
               }

            }
         }
      }

      private static void m_280634_(ServerLevel p_282775_, VibrationSystem.Data p_282792_, VibrationSystem.User p_281845_) {
         p_282792_.m_280457_().chosenCandidate(p_282775_.getGameTime()).ifPresent((p_282059_) -> {
            p_282792_.m_280036_(p_282059_);
            Vec3 vec3 = p_282059_.pos();
            p_282792_.m_280178_(p_281845_.m_280576_(p_282059_.distance()));
            p_282775_.sendParticles(new VibrationParticleOption(p_281845_.m_280010_(), p_282792_.m_280274_()), vec3.x, vec3.y, vec3.z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
            p_281845_.m_280022_();
            p_282792_.m_280457_().startOver();
         });
      }

      private static void m_280404_(ServerLevel p_282010_, VibrationSystem.Data p_282354_, VibrationSystem.User p_282958_) {
         if (p_282354_.m_280616_()) {
            if (p_282354_.f_279652_ == null) {
               p_282354_.m_280671_(false);
            } else {
               Vec3 vec3 = p_282354_.f_279652_.pos();
               PositionSource positionsource = p_282958_.m_280010_();
               Vec3 vec31 = positionsource.getPosition(p_282010_).orElse(vec3);
               int i = p_282354_.m_280274_();
               int j = p_282958_.m_280576_(p_282354_.f_279652_.distance());
               double d0 = 1.0D - (double)i / (double)j;
               double d1 = Mth.lerp(d0, vec3.x, vec31.x);
               double d2 = Mth.lerp(d0, vec3.y, vec31.y);
               double d3 = Mth.lerp(d0, vec3.z, vec31.z);
               boolean flag = p_282010_.sendParticles(new VibrationParticleOption(positionsource, i), d1, d2, d3, 1, 0.0D, 0.0D, 0.0D, 0.0D) > 0;
               if (flag) {
                  p_282354_.m_280671_(false);
               }

            }
         }
      }

      private static boolean m_280174_(ServerLevel p_282967_, VibrationSystem.Data p_283447_, VibrationSystem.User p_282301_, VibrationInfo p_281498_) {
         BlockPos blockpos = BlockPos.containing(p_281498_.pos());
         BlockPos blockpos1 = p_282301_.m_280010_().getPosition(p_282967_).map(BlockPos::containing).orElse(blockpos);
         if (p_282301_.m_280215_() && !m_280446_(p_282967_, blockpos1)) {
            return false;
         } else {
            p_282301_.m_280271_(p_282967_, blockpos, p_281498_.gameEvent(), p_281498_.getEntity(p_282967_).orElse((Entity)null), p_281498_.getProjectileOwner(p_282967_).orElse((Entity)null), VibrationSystem.Listener.m_280659_(blockpos, blockpos1));
            p_283447_.m_280036_((VibrationInfo)null);
            return true;
         }
      }

      private static boolean m_280446_(Level p_282735_, BlockPos p_281722_) {
         ChunkPos chunkpos = new ChunkPos(p_281722_);

         for(int i = chunkpos.x - 1; i < chunkpos.x + 1; ++i) {
            for(int j = chunkpos.z - 1; j < chunkpos.z + 1; ++j) {
               ChunkAccess chunkaccess = p_282735_.getChunkSource().getChunkNow(i, j);
               if (chunkaccess == null || !p_282735_.shouldTickBlocksAt(chunkaccess.getPos().toLong())) {
                  return false;
               }
            }
         }

         return true;
      }
   }

   public interface User {
      int m_280351_();

      PositionSource m_280010_();

      boolean m_280080_(ServerLevel p_282960_, BlockPos p_282488_, GameEvent p_282865_, GameEvent.Context p_283577_);

      void m_280271_(ServerLevel p_282148_, BlockPos p_282090_, GameEvent p_283663_, @Nullable Entity p_281578_, @Nullable Entity p_281308_, float p_281707_);

      default TagKey<GameEvent> m_280028_() {
         return GameEventTags.VIBRATIONS;
      }

      default boolean m_280076_() {
         return false;
      }

      default boolean m_280215_() {
         return false;
      }

      default int m_280576_(float p_281658_) {
         return Mth.floor(p_281658_);
      }

      default boolean m_280612_(GameEvent p_282750_, GameEvent.Context p_283373_) {
         if (!p_282750_.is(this.m_280028_())) {
            return false;
         } else {
            Entity entity = p_283373_.sourceEntity();
            if (entity != null) {
               if (entity.isSpectator()) {
                  return false;
               }

               if (entity.isSteppingCarefully() && p_282750_.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
                  if (this.m_280076_() && entity instanceof ServerPlayer) {
                     ServerPlayer serverplayer = (ServerPlayer)entity;
                     CriteriaTriggers.AVOID_VIBRATION.trigger(serverplayer);
                  }

                  return false;
               }

               if (entity.dampensVibrations()) {
                  return false;
               }
            }

            if (p_283373_.affectedState() != null) {
               return !p_283373_.affectedState().is(BlockTags.DAMPENS_VIBRATIONS);
            } else {
               return true;
            }
         }
      }

      default void m_280022_() {
      }
   }
}