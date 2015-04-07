package MotionPlanning;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.ros.message.rss_msgs.*;
import org.ros.message.MessageListener;
import org.ros.node.topic.Publisher;
import org.ros.node.topic.Subscriber;
import org.ros.node.parameter.ParameterTree;
import org.ros.node.NodeMain;
import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.message.lab5_msgs.*;
import org.ros.message.lab6_msgs.*;
import org.ros.message.Challenge_msgs.*;

import Challenge.GrandChallengeMap;

/**
 * Tests the RRT Module.
 *
 */
public class RRTTest implements NodeMain{

    private CSpaceTest cSpaceTest;

    public RRTTest() {
        cSpaceTest = new CSpaceTest();
    }

    @Override
    public void onStart(Node node) {

    }

    @Override
    public void onShutdown(Node node) {
        if (node != null) {
            node.shutdown();
        }
    }

    @Override
    public void onShutdownComplete(Node node) {
    }

    @Override
    public GraphName getDefaultNodeName() {
        return new GraphName("rss/RRTTest");
    }
}