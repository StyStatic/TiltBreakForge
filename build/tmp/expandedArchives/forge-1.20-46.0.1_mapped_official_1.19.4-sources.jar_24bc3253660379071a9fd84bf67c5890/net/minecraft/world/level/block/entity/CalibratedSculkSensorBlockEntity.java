package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;

public class CalibratedSculkSensorBlockEntity extends SculkSensorBlockEntity {
   public CalibratedSculkSensorBlockEntity(BlockPos p_277459_, BlockState p_278100_) {
      super(BlockEntityType.f_276581_, p_277459_, p_278100_);
   }

   public VibrationSystem.User m_280175_() {
      return new CalibratedSculkSensorBlockEntity.VibrationUser(this.getBlockPos());
   }

   protected class VibrationUser extends SculkSensorBlockEntity.VibrationUser {
      public VibrationUser(BlockPos p_281602_) {
         super(p_281602_);
      }

      public int m_280351_() {
         return 16;
      }

      public boolean m_280080_(ServerLevel p_282061_, BlockPos p_282550_, GameEvent p_281789_, @Nullable GameEvent.Context p_281456_) {
         int i = this.m_280214_(p_282061_, this.f_279654_, CalibratedSculkSensorBlockEntity.this.getBlockState());
         return i != 0 && VibrationSystem.m_280122_(p_281789_) != i ? false : super.m_280080_(p_282061_, p_282550_, p_281789_, p_281456_);
      }

      private int m_280214_(Level p_282204_, BlockPos p_282397_, BlockState p_282240_) {
         Direction direction = p_282240_.getValue(CalibratedSculkSensorBlock.f_276692_).getOpposite();
         return p_282204_.m_277185_(p_282397_.relative(direction), direction);
      }
   }
}