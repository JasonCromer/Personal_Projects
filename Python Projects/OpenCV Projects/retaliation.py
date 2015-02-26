#!/usr/bin/python
#
# Copyright 2011 PaperCut Software Int. Pty. Ltd. http://www.papercut.com/
#
#   Licensed under the Apache License, Version 2.0 (the "License");
#   you may not use this file except in compliance with the License.
#   You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#   Unless required by applicable law or agreed to in writing, software
#   distributed under the License is distributed on an "AS IS" BASIS,
#   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#   See the License for the specific language governing permissions and
#   limitations under the License.
#
#
# Copyright 2014 Jason Cromer
#   
#   This file has been modified and rewritten by Jason Cromer. Any usages of
#   this program as modified or contains any modification by Jason Cromer
#   requires that this header remain here.
#
#
#
#
# 

############################################################################
# 
# RETALIATION - A Jenkins "Extreme Feedback" Contraption
#
#    Lava Lamps are for pussies! Retaliate to a broken build with a barrage 
#    of foam missiles.
#
# Steps to use:
#
#  1.  Mount your Dream Cheeky Thunder USB missile launcher in a central and 
#      fixed location.
#
#  2.  Copy this script onto the system connected to your missile lanucher.
#
#  3.  Modify your `COMMAND_SETS` in the `retaliation.py` script to define 
#      your targeting commands for each one of your build-braking coders 
#      (their user ID as listed in Jenkins).  A command set is an array of 
#      move and fire commands. It is recommend to start each command set 
#      with a "zero" command.  This parks the launcher in a known position 
#      (bottom-left).  You can then use "up" and "right" followed by a 
#      time (in milliseconds) to position your fire.
# 
#      You can test a set by calling retaliation.py with the target name. 
#      e.g.:  
#
#           retaliation.py "[developer's user name]"
#
#      Trial and error is the best approch. Consider doing this secretly 
#      after hours for best results!
#
#
#
#  Requirements:
#   * A Dream Cheeky Thunder USB Missile Launcher
#   * Python 2.6+
#   * Python PyUSB Support and its dependencies 
#      http://sourceforge.net/apps/trac/pyusb/
#      (on Mac use brew to "brew install libusb")
#   * Should work on Windows, Mac and Linux
#
#  Author:  Chris Dance <chris.dance@papercut.com>
#  Author: Jason Cromer <Jasonmcromer@gmail.com>
#  Version: 2.0 : 10-1-2014
#
############################################################################

import sys
import platform
import time
import socket
import re
import json
import urllib2
import base64

import usb.core
import usb.util

import cv2
import sys, getopt
import numpy as np
import cv2
from matplotlib import pyplot as plt



##########################  CONFIG   #########################

#
# Define a dictionary of "command sets" that map usernames to a sequence 
# of commands to target the user (e.g their desk/workstation).  It's 
# suggested that each set start and end with a "zero" command so it's
# always parked in a known reference location. The timing on move commands
# is milli-seconds. The number after "fire" denotes the number of rockets
# to shoot.
#
COMMAND_SETS = {
    "will" : (
        ("zero", 0), # Zero/Park to know point (bottom-left)
        ("led", 1), # Turn the LED on
        ("right", 3250),
        ("up", 540),
        ("fire", 4), # Fire a full barrage of 4 missiles
        ("Detect",0), # Detects a face and fires a missile
        ("led", 0), # Turn the LED back off
        ("zero", 0), # Park after use for next time
    ),
    "tom" : (
        ("zero", 0), 
        ("right", 4400),
        ("up", 200),
        ("fire", 4),
        ("zero", 0),
    ),
    "chris" : (      # That's me - just dance around and missfire!
        ("zero", 0),
        ("right", 5200),
        ("up", 500),
        ("pause", 5000),
        ("left", 2200),
        ("down", 500),
        ("fire", 1),
        ("zero", 0),
    ),
}


#############################  ENG CONFIG  ##################################

# The code...

# Protocol command bytes
DOWN    = 0x01
UP      = 0x02
LEFT    = 0x04
RIGHT   = 0x08
FIRE    = 0x10
STOP    = 0x20

DEVICE = None
DEVICE_TYPE = None
global x, y, w, h
global length, height


########################## CAMERA CONFIG #############################

    
######################### END CAMERA CONFIG ##########################

def usage():
    print "Usage: retaliation.py [command] [value]"
    print ""
    print "   commands:"
    print "     Detect - detects a face and fires a missile"
    print ""
    print "     up    - move up <value> milliseconds"
    print "     down  - move down <value> milliseconds"
    print "     right - move right <value> milliseconds"
    print "     left  - move left <value> milliseconds"
    print "     fire  - fire <value> times (between 1-4)"
    print "     zero  - park at zero position (bottom-left)"
    print "     pause - pause <value> milliseconds"
    print "     led   - turn the led on or of (1 or 0)"
    print ""
    print "     <command_set_name> - run/test a defined COMMAND_SET"
    print "             e.g. run:"
    print "                  retaliation.py 'chris'"
    print "             to test targeting of chris as defined in your command set."
    print ""


def setup_usb():
    # Tested only with the Cheeky Dream Thunder
    # and original USB Launcher
    global DEVICE 
    global DEVICE_TYPE

    DEVICE = usb.core.find(idVendor=0x2123, idProduct=0x1010)

    if DEVICE is None:
        DEVICE = usb.core.find(idVendor=0x0a81, idProduct=0x0701)
        if DEVICE is None:
            raise ValueError('Missile device not found')
        else:
            DEVICE_TYPE = "Original"
    else:
        DEVICE_TYPE = "Thunder"

    

    # On Linux we need to detach usb HID first
    if "Linux" == platform.system():
        try:
            DEVICE.detach_kernel_driver(0)
        except Exception, e:
            pass # already unregistered    

    DEVICE.set_configuration()


def send_cmd(cmd):
    if "Thunder" == DEVICE_TYPE:
        DEVICE.ctrl_transfer(0x21, 0x09, 0, 0, [0x02, cmd, 0x00,0x00,0x00,0x00,0x00,0x00])
    elif "Original" == DEVICE_TYPE:
        DEVICE.ctrl_transfer(0x21, 0x09, 0x0200, 0, [cmd])


def led(cmd):
    if "Thunder" == DEVICE_TYPE:
        DEVICE.ctrl_transfer(0x21, 0x09, 0, 0, [0x03, cmd, 0x00,0x00,0x00,0x00,0x00,0x00])
    elif "Original" == DEVICE_TYPE:
        print("There is no LED on this device")


def send_move(cmd, duration_ms):
    send_cmd(cmd)
    time.sleep(duration_ms / 1000.0)
    send_cmd(STOP)


def run_command(command, value):
    command = command.lower()
    if command == "right":
        send_move(RIGHT, value)
    elif command == "left":
        send_move(LEFT, value)
    elif command == "up":
        send_move(UP, value)
    elif command == "down":
        send_move(DOWN, value)
    elif command == "zero" or command == "park" or command == "reset":
        # Move to bottom-left
        send_move(DOWN, 2000)
        send_move(LEFT, 8000)
    elif command == "pause" or command == "sleep":
        time.sleep(value / 1000.0)
    elif command == "led":
        if value == 0:
            led(0x00)
        else:
            led(0x01)
    elif command == "fire" or command == "shoot":
        if value < 1 or value > 4:
            value = 1
        # Stabilize prior to the shot, then allow for reload time after.
        time.sleep(0.5)
        for i in range(value):
            send_cmd(FIRE)
            time.sleep(4.5)
    elif command == "detect":
        detect_destroy()
        if value < 1 or value > 4:
            value = 1
        # Stabilize prior to the shot, then allow for reload time after.
        time.sleep(0.5)
        led(0x01)
        '''for i in range(value):
            if(x <250):
                send_move(LEFT, length)
                send_cmd(FIRE)
                time.sleep(4.5)
                send_move(RIGHT,length)
            if(x >250):
                send_move(RIGHT, length - 250)
                send_cmd(FIRE)
                time.sleep(4.5)
                send_move(LEFT, length - 250)
        led(0x00)
    else:
        print "Error: Unknown command: '%s'" % command'''


def run_command_set(commands):
    for cmd, value in commands:
        run_command(cmd, value)



def main(args):
    
    if len(args) < 2:
        usage()
        sys.exit(1)
    setup_usb()
    if args[1] == "stalk":
        return

    # Process any passed commands or command_sets
    command = args[1]
    value = 0
    if len(args) > 2:
        value = int(args[2])
    if command in COMMAND_SETS:
        run_command_set(COMMAND_SETS[command])
    else:
        run_command(command, value)



def target_the_target(x,y,w,h):
    print((x,y), (x+w, y+h))
    length = (x + (x+w))/2
    height = (y + (y+h))/2
    return length,height




def detect_destroy():
    args, video_src = getopt.getopt(sys.argv[1:], '', ['cascade=', 'nested-cascade='])
    args = dict(args)
    cascPath = args.get('--cascade', "haarcascade_frontalface_alt.xml")
    faceCascade = cv2.CascadeClassifier(cascPath)
    video_src = cv2.VideoCapture(0)
    
    try:
        # Capture frame-by-frame
        plt.ion()
        ret, frame = video_src.read()
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        faces = faceCascade.detectMultiScale(
            gray,
            scaleFactor=1.1,
            minNeighbors=5,
            minSize=(30, 30),
            flags=cv2.CASCADE_SCALE_IMAGE
            )
        
        if faces == 0 :
            print('no face found')
        # Display the resulting frame
        cv2.imshow('Video', frame)

    except Exception as e:
        print('DEBUG: %s' % e)

    # Draw a rectangle around the faces
    for ( x, y, w, h) in faces:
        print(x,y)
        if (x or y) >= 0:
            print('working')
            cv2.rectangle(frame, (x, y), (x+w, y+h), (0, 255, 0), 2)
            target_the_target(x,y,w,h)
        if (x or y) < 0:
            print('x or y value is negative, check math code')
        elif (x or y) is None:
            print('No face detected')

        

    if cv2.waitKey(1) == ord('q'):
            sys.exit()
    # When everything is done, release the capture
    video_src.release()
    cv2.destroyAllWindows()



if __name__ == '__main__':
    main(sys.argv)
