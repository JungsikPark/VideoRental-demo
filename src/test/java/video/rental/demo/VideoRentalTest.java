package video.rental.demo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class VideoRentalTest {
	
	private GoldenMaster goldenMaster = new GoldenMaster();
	
	@Test
	@Disabled
	void generateGoldenMaster() {
		
		goldenMaster.generateGoldenMaster();
	}
	
	@Test
	//@Disabled
	void checkRuntimeResultWithGoldenMaster() {
		// Given (Arrange)
		String actual = goldenMaster.getRunResult();
		
		// When (Act)
		String expected = goldenMaster.getGoldenMaster();

		// Then (Assert)
		assertEquals(expected, actual.replaceAll("\r\n", "\n"));
	}

}
