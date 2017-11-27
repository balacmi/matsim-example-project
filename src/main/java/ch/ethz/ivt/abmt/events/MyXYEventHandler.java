package ch.ethz.ivt.abmt.events;

import java.io.BufferedWriter;
import java.io.IOException;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.controler.events.ShutdownEvent;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.ShutdownListener;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.core.utils.io.UncheckedIOException;

public class MyXYEventHandler implements ActivityStartEventHandler, ActivityEndEventHandler,
									StartupListener, ShutdownListener {
	private final String filePath;
	private BufferedWriter writer = null;
	private final Network network;
	private int iteration = -1;
	
	public MyXYEventHandler( String filePath , Network network ) {
		this.network = network;
		this.filePath = filePath;
		
	}

	@Override
	public void reset(int iteration) {
		this.iteration = iteration;
	}

	@Override
	public void handleEvent(ActivityEndEvent event) {
		Id<Link> linkId = event.getLinkId();
		Link link = network.getLinks().get( linkId );
		Coord coord = link.getCoord();
		
		try {
			writer.newLine();
			writer.write( iteration+"\t"+
						event.getPersonId()+"\t"+
						event.getActType()+"\t"+
						"end\t"+
						coord.getX()+"\t"+
						coord.getY()+"\t"+
						event.getTime() );
		} catch (IOException e) {
			throw new UncheckedIOException( e );
		}
	}

	@Override
	public void handleEvent(ActivityStartEvent event) {
		Id<Link> linkId = event.getLinkId();
		Link link = network.getLinks().get( linkId );
		Coord coord = link.getCoord();
		
		try {
			writer.newLine();
			writer.write( iteration+"\t"+
						event.getPersonId()+"\t"+
						event.getActType()+"\t"+
						"start\t"+
						coord.getX()+"\t"+
						coord.getY()+"\t"+
						event.getTime() );
		} catch (IOException e) {
			throw new UncheckedIOException( e );
		}
	
	}
	
	public void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new UncheckedIOException( e );
		}
	}

	@Override
	public void notifyShutdown(ShutdownEvent event) {
		close();
	}

	@Override
	public void notifyStartup(StartupEvent event) {
		this.writer = IOUtils.getBufferedWriter(filePath);
		try {
			writer.write( "iteration\tpersonId\tactType\tevent\tx\ty\ttime");
		} catch (IOException e) {
			throw new UncheckedIOException( e );
		}
		
		event.getServices().getEvents().addHandler( this );
	}

}
