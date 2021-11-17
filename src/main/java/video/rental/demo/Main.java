package video.rental.demo;

import video.rental.demo.application.Interactor;
import video.rental.demo.domain.Repository;
import video.rental.demo.infrastructure.RepositoryMemImpl;
import video.rental.demo.presentation.GraphicUI;
import video.rental.demo.presentation.UI;
import video.rental.demo.utils.SampleGenerator;

public class Main {
	private static UI ui;

	public static void main(String[] args) {
		Repository repository = new RepositoryMemImpl();
		//Repository repository = new RepositoryDBImpl();
		new SampleGenerator(repository).generateSamples();
		
		Interactor interactor = new Interactor(repository);
		//ui = new CmdUI(interactor);
		ui = new GraphicUI(interactor);
		ui.start();
	}
}
