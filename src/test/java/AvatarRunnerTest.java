import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class AvatarRunnerTest {

	private AvatarRunner avatarRunner;

	@Before
	public void setUp() throws Exception {
	 	avatarRunner = new AvatarRunner();
	}

	@After
	public void tearDown() throws Exception {

	}

	@Test
	public void handlePromisesNpmDependency() throws Throwable {
		String result = avatarRunner.runJs("PromiseExample.js");

		System.out.println(result);

	}

	@Test
	public void handleLodashNpmDependency() throws Throwable {
		String result = avatarRunner.runJs("LodashExample.js");

		System.out.println(result);

	}
}