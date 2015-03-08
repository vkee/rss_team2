FILE(REMOVE_RECURSE
  "../src/lab6_msgs/msg"
  "../msg_gen"
  "../msg_gen"
  "CMakeFiles/ROSBUILD_genmsg_py"
  "../src/lab6_msgs/msg/__init__.py"
  "../src/lab6_msgs/msg/_GUIRectMsg.py"
  "../src/lab6_msgs/msg/_GUIPolyMsg.py"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/ROSBUILD_genmsg_py.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
