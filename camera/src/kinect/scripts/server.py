#!/usr/bin/env python

import socket
import os
import sys
from thread import *

import rospy
from std_msgs.msg import String
from sensor_msgs.msg import Image
from stereo_msgs.msg import DisparityImage

import datetime
import time
import os
import PIL.Image
from StringIO import StringIO
#import struct

then = datetime.datetime.now()
try:
	os.mkdir("/tmp/fast/",0755)
except:
	pass
def forwardImage(data, img_type):

#	print img_type
	global then
	
	if img_type is "color":
#		print (datetime.datetime.now() - then)
		if (datetime.datetime.now() - then) > datetime.timedelta(seconds = .2):
			then = datetime.datetime.now()
#			print (datetime.datetime.now() - then)
#			print "MADE IT HERE"
			#print then
			f = open("/tmp/fast/rgb.bin","w")
#			f = open("/home/rss-student/snaps/rgb.bin","w")	
			f.write(data.data)
			f.close()
		#bgr8
		#	im =  PIL.Image.fromstring("RGB",(data.width,data.height),data.data,"raw","BGR")
		#	im.save("/home/rss-student/snaps/rgb.png")
	

	if img_type is "depth" or img_type is "disparity":
		if (datetime.datetime.now() - then) > datetime.timedelta(seconds = .2):
			then = datetime.datetime.now()
			f = open("/tmp/fast/depth.bin","w")				
			f.write(data.data)
			f.close()
		#32FC1
		#im =  PIL.Image.fromstring("F",(data.width,data.height),data.data)#,"raw","F;32F")
		#im = im.convert('RGB')
		#im.save("/home/rss-student/snaps/rgb.png")

def forwardColorImage(data):
	forwardImage(data,"color")#,datetime.datetime.now())

def forwardDepthImage(data):
	pass	
def forwardDisparityImage(data):
	forwardImage(data.image,"disparity")


if __name__ == "__main__":
	try:
		print "Image start"
		rospy.init_node('server',anonymous=True)
		rospy.Subscriber('/camera/rgb/image_color',Image, forwardColorImage)
		rospy.Subscriber('/camera/depth/disparity', DisparityImage, forwardDisparityImage)
		rospy.spin()
		print "done"
	except rospy.ROSInterruptException:
		print "Image end"
		pass


