FILE(REMOVE_RECURSE
  "../src/Challenge_msgs/msg"
  "../msg_gen"
  "../msg_gen"
  "CMakeFiles/ROSBUILD_genmsg_py"
  "../src/Challenge_msgs/msg/__init__.py"
  "../src/Challenge_msgs/msg/_GUIEllipseMessage.py"
  "../src/Challenge_msgs/msg/_GUIStringMessage.py"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/ROSBUILD_genmsg_py.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
