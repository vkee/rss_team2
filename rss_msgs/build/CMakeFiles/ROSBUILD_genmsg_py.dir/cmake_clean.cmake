FILE(REMOVE_RECURSE
  "../src/rss_msgs/msg"
  "../msg_gen"
  "../msg_gen"
  "CMakeFiles/ROSBUILD_genmsg_py"
  "../src/rss_msgs/msg/__init__.py"
  "../src/rss_msgs/msg/_ResetMsg.py"
  "../src/rss_msgs/msg/_BumpMsg.py"
  "../src/rss_msgs/msg/_AnalogStatusMsg.py"
  "../src/rss_msgs/msg/_ArmMsg.py"
  "../src/rss_msgs/msg/_EncoderMsg.py"
  "../src/rss_msgs/msg/_DigitalStatusMsg.py"
  "../src/rss_msgs/msg/_MotionMsg.py"
  "../src/rss_msgs/msg/_BreakBeamMsg.py"
  "../src/rss_msgs/msg/_SonarMsg.py"
  "../src/rss_msgs/msg/_OdometryMsg.py"
)

# Per-language clean rules from dependency scanning.
FOREACH(lang)
  INCLUDE(CMakeFiles/ROSBUILD_genmsg_py.dir/cmake_clean_${lang}.cmake OPTIONAL)
ENDFOREACH(lang)
