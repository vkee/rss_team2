; Auto-generated. Do not edit!


(cl:in-package rss_msgs-msg)


;//! \htmlinclude BumpMsg.msg.html

(cl:defclass <BumpMsg> (roslisp-msg-protocol:ros-message)
  ((left
    :reader left
    :initarg :left
    :type cl:boolean
    :initform cl:nil)
   (right
    :reader right
    :initarg :right
    :type cl:boolean
    :initform cl:nil))
)

(cl:defclass BumpMsg (<BumpMsg>)
  ())

(cl:defmethod cl:initialize-instance :after ((m <BumpMsg>) cl:&rest args)
  (cl:declare (cl:ignorable args))
  (cl:unless (cl:typep m 'BumpMsg)
    (roslisp-msg-protocol:msg-deprecation-warning "using old message class name rss_msgs-msg:<BumpMsg> is deprecated: use rss_msgs-msg:BumpMsg instead.")))

(cl:ensure-generic-function 'left-val :lambda-list '(m))
(cl:defmethod left-val ((m <BumpMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:left-val is deprecated.  Use rss_msgs-msg:left instead.")
  (left m))

(cl:ensure-generic-function 'right-val :lambda-list '(m))
(cl:defmethod right-val ((m <BumpMsg>))
  (roslisp-msg-protocol:msg-deprecation-warning "Using old-style slot reader rss_msgs-msg:right-val is deprecated.  Use rss_msgs-msg:right instead.")
  (right m))
(cl:defmethod roslisp-msg-protocol:serialize ((msg <BumpMsg>) ostream)
  "Serializes a message object of type '<BumpMsg>"
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'left) 1 0)) ostream)
  (cl:write-byte (cl:ldb (cl:byte 8 0) (cl:if (cl:slot-value msg 'right) 1 0)) ostream)
)
(cl:defmethod roslisp-msg-protocol:deserialize ((msg <BumpMsg>) istream)
  "Deserializes a message object of type '<BumpMsg>"
    (cl:setf (cl:slot-value msg 'left) (cl:not (cl:zerop (cl:read-byte istream))))
    (cl:setf (cl:slot-value msg 'right) (cl:not (cl:zerop (cl:read-byte istream))))
  msg
)
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql '<BumpMsg>)))
  "Returns string type for a message object of type '<BumpMsg>"
  "rss_msgs/BumpMsg")
(cl:defmethod roslisp-msg-protocol:ros-datatype ((msg (cl:eql 'BumpMsg)))
  "Returns string type for a message object of type 'BumpMsg"
  "rss_msgs/BumpMsg")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql '<BumpMsg>)))
  "Returns md5sum for a message object of type '<BumpMsg>"
  "0544cac0b98e92509d14f758d50cf24b")
(cl:defmethod roslisp-msg-protocol:md5sum ((type (cl:eql 'BumpMsg)))
  "Returns md5sum for a message object of type 'BumpMsg"
  "0544cac0b98e92509d14f758d50cf24b")
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql '<BumpMsg>)))
  "Returns full string definition for message of type '<BumpMsg>"
  (cl:format cl:nil "bool left~%bool right~%~%"))
(cl:defmethod roslisp-msg-protocol:message-definition ((type (cl:eql 'BumpMsg)))
  "Returns full string definition for message of type 'BumpMsg"
  (cl:format cl:nil "bool left~%bool right~%~%"))
(cl:defmethod roslisp-msg-protocol:serialization-length ((msg <BumpMsg>))
  (cl:+ 0
     1
     1
))
(cl:defmethod roslisp-msg-protocol:ros-message-to-list ((msg <BumpMsg>))
  "Converts a ROS message object to a list"
  (cl:list 'BumpMsg
    (cl:cons ':left (left msg))
    (cl:cons ':right (right msg))
))
