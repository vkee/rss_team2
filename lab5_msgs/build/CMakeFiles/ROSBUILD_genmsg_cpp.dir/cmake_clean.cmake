FILE(REMOVE_RECURSE
  "../src/lab5_msgs/msg"
  "../msg_gen"
  "../msg_gen"
  "CMakeFiles/ROSBUILD_genmsg_cpp"
  "../msg_gen/cpp/include/lab5_msgs/GUIEraseMsg.h"
  "../msg_gen/cpp/include/lab5_msgs/GUILineMsg.h"
  "../msg_gen/cpp/include/lab5_msgs/GUISegmentMsg.h"
  "../msg_gen/cpp/include/lab5_msgs/ColorMsg.h"
  "../msg_gen/cpp/include/lab5_msgs/GUIPointMsg.h"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/ROSBUILD_genmsg_cpp.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
