import motion.EncoderPublisher;
import orc.Orc;

import org.ros.namespace.GraphName;
import org.ros.node.Node;
import org.ros.node.NodeMain;

import sonar.SonarPublisher;
import analogIO.AnalogIOPublisher;
import digitalIO.BreakBeamPublisher;
import digitalIO.BumperPublisher;
import digitalIO.DigitalIOPublisher;

public class Publisher implements NodeMain {
	Orc orc;

	Thread digitalThread;

	Thread breakBeamThread;
	Thread bumpThread;

	Thread analogThread;

	Thread encoderThread;

	Thread frontSonarThread;
	Thread backSonarThread;

	Object lock;

	@Override
	public void onStart(Node node) {
		lock = new Object();

		orc = Orc.makeOrc();

		DigitalIOPublisher digitalPub = new DigitalIOPublisher(node, orc, lock);
		digitalThread = new Thread(digitalPub);

		BreakBeamPublisher breakBeamPub = new BreakBeamPublisher(node, orc, lock);
		breakBeamThread = new Thread(breakBeamPub);

		BumperPublisher bumpPub = new BumperPublisher(node, orc, lock);
		bumpThread = new Thread(bumpPub);

		AnalogIOPublisher analogPub = new AnalogIOPublisher(node, orc, lock);
		analogThread = new Thread(analogPub);

		EncoderPublisher encoderPub = new EncoderPublisher(node, orc, lock);
		encoderThread = new Thread(encoderPub);

		SonarPublisher frontPub = new SonarPublisher(node, orc, true, lock);
		frontSonarThread = new Thread(frontPub);

		SonarPublisher backPub = new SonarPublisher(node, orc, false, lock);
		backSonarThread = new Thread(backPub);

		//constructors are not thread safe wrt to orc, must start all threads
		// after all have been constructed
		digitalThread.start();
		breakBeamThread.start();
		bumpThread.start();
		analogThread.start();
		encoderThread.start();
		frontSonarThread.start();
		backSonarThread.start();
	}

	@Override
	public void onShutdown(Node node) {
		node.shutdown();
		digitalThread.stop();
		breakBeamThread.stop();
		bumpThread.stop();
		analogThread.stop();
		encoderThread.stop();
		frontSonarThread.stop();
		backSonarThread.stop();
	}

	@Override
	public void onShutdownComplete(Node node) {
	}

	@Override
	public GraphName getDefaultNodeName() {
		return new GraphName("rss/uorc_publisher");
	}

}
