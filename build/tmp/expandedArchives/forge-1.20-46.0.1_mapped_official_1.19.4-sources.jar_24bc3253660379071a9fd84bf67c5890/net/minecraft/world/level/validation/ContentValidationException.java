package net.minecraft.world.level.validation;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class ContentValidationException extends Exception {
   private final Path f_289822_;
   private final List<ForbiddenSymlinkInfo> f_289835_;

   public ContentValidationException(Path p_289932_, List<ForbiddenSymlinkInfo> p_289984_) {
      this.f_289822_ = p_289932_;
      this.f_289835_ = p_289984_;
   }

   public String getMessage() {
      return m_289907_(this.f_289822_, this.f_289835_);
   }

   public static String m_289907_(Path p_289929_, List<ForbiddenSymlinkInfo> p_289979_) {
      return "Failed to validate '" + p_289929_ + "'. Found forbidden symlinks: " + (String)p_289979_.stream().map((p_289919_) -> {
         return p_289919_.f_289826_() + "->" + p_289919_.f_289840_();
      }).collect(Collectors.joining(", "));
   }
}