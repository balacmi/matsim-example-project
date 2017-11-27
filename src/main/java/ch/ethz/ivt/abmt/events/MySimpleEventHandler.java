package ch.ethz.ivt.abmt.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.utils.misc.Time;

public class MySimpleEventHandler implements ActivityStartEventHandler {

	@Override
	public void reset(int iteration) {
		
	}

	@Override
	public void handleEvent(ActivityStartEvent event) {
		String actType = event.getActType();
		double time = event.getTime();
		Id<Person> personId = event.getPersonId();
		String timeString = Time.writeTime( time );
		
		System.out.println( personId+" started "+actType+" at "+timeString );
	}

}
