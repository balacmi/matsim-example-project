package ch.ethz.ivt.abmt.events;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;

public class RunWithEventHandler {

	public static void main(String[] args) {
		Config config = ConfigUtils.loadConfig( args[ 0 ] ) ;
		config.controler().setLastIteration(1);
		config.controler().setOverwriteFileSetting( OverwriteFileSetting.deleteDirectoryIfExists );
		
		Scenario scenario = ScenarioUtils.loadScenario(config) ;
		Population population = scenario.getPopulation();
		
		Controler controler = new Controler( scenario ) ;
		
		controler.getEvents().addHandler( new MySimpleEventHandler() );
		
		String  xyFile = config.controler().getOutputDirectory() + "/activity.xy";
		MyXYEventHandler xyHandler = new MyXYEventHandler( xyFile , scenario.getNetwork() );
		//controler.getEvents().addHandler( xyHandler );
		controler.addControlerListener(xyHandler);

		controler.run();
	}

}
