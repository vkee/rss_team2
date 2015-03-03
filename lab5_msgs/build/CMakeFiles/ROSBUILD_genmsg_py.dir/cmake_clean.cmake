FILE(REMOVE_RECURSE
  "../src/lab5_msgs/msg"
  "../msg_gen"
  "../msg_gen"
  "CMakeFiles/ROSBUILD_genmsg_py"
  "../src/lab5_msgs/msg/__init__.py"
  "../src/lab5_msgs/msg/_GUILineMsg.py"
  "../src/lab5_msgs/msg/_ColorMsg.py"
  "../src/lab5_msgs/msg/_GUIPointMsg.py"
  "../src/lab5_msgs/msg/_GUISegmentMsg.py"
  "../src/lab5_msgs/msg/_GUIEraseMsg.py"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/ROSBUILD_genmsg_py.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
