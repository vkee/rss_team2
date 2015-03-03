; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude BreakBeamMsg.msg.html

(cl:defclass <BreakBeamMsg> (roslisp-msg-protocol:ros-message)
  ((beamBroken
    :reader beamBroken
    :initarg :beamBroken
    :type cl:boolean
    :initform cl:nil))
)

(cl:defclass BreakBeamMsg (<BreakBeamMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <BreakBeamMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'BreakBeamMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<BreakBeamMsg> is deprecated: use rss_msgs-msg:BreakBeamMsg instead.")))

(cl:ensure-generic-function 'beamBroken-val :lambda-list '(m))
(cl:defmethod beamBroken-val ((m <BreakBeamMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:beamBroken-val is deprecated.  Use rss_msgs-msg:beamBroken instead.")
  (beamBroken m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <BreakBeamMsg>) ostream)
  "Serializes a message object of type '<BreakBeamMsg>"
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'beamBroken) 1 0)) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <BreakBeamMsg>) istream)
  "Deserializes a message object of type '<BreakBeamMsg>"
    (cl:setf (cl:slot-value msg 'beamBroken) (cl:not (cl:zerop (cl:read-byte istream))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<BreakBeamMsg>)))
  "Returns string type for a message object of type '<BreakBeamMsg>"
  "rss_msgs/BreakBeamMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'BreakBeamMsg)))
  "Returns string type for a message object of type 'BreakBeamMsg"
  "rss_msgs/BreakBeamMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<BreakBeamMsg>)))
  "Returns md5sum for a message object of type '<BreakBeamMsg>"
  "5c99d1d4bed9929256313b1c7b10c3bd")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'BreakBeamMsg)))
  "Returns md5sum for a message object of type 'BreakBeamMsg"
  "5c99d1d4bed9929256313b1c7b10c3bd")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<BreakBeamMsg>)))
  "Returns full string definition for message of type '<BreakBeamMsg>"
  (cl:format cl:nil "bool beamBroken~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'BreakBeamMsg)))
  "Returns full string definition for message of type 'BreakBeamMsg"
  (cl:format cl:nil "bool beamBroken~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <BreakBeamMsg>))
  (cl:+ 0
     1
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <BreakBeamMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'BreakBeamMsg
    (cl:cons ':beamBroken (beamBroken msg))
))
