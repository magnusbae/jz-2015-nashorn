import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class PorcelainTest {

	private Porcelain porcelain;

	@Before
	public void setUp() throws Exception {
		porcelain = new Porcelain();

	}

	@Test
	public void brokenScriptReturnsEmptyOptional() {
		Optional<Object> result = porcelain.eval("return 42;");

		assertFalse(result.isPresent());
	}

	@Test
	public void scriptReturnsFortyTwo() {
		String script = " (   function(){ return 42; }   ) (); ";

		Optional<Object> result = porcelain.eval(script);

		assertTrue(result.isPresent());
		Object res = result.get();
		assertTrue(res instanceof Number);
		assertEquals(42, ((Number) res).intValue());
	}


	@Test
	public void scriptAddsTwoNumbers() {
		String script = "function add(a, b){ return a + b; }; ";

		porcelain.eval(script);

		Optional<Object> result = porcelain.invokeFunction("add", 22, 20);

		assertTrue(result.isPresent());
		Object res = result.get();
		assertTrue(res instanceof Number);
		assertEquals(42, ((Number) res).intValue());
	}

	@Test
	public void scriptEvalAndInvokeAndNewEvalMakesBothFunctionsAvailableForInvocation() {
		String scriptOne = "function add(a, b){ return a + b; }; ";
		String scriptTwo = "function subtract(a, b){ return a - b; };";

		porcelain.eval(scriptOne);

		Optional<Object> result = porcelain.invokeFunction("add", 22, 20);
		assertTrue(result.isPresent());
		Object res = result.get();

		assertTrue(res instanceof Number);
		assertEquals(42, ((Number) res).intValue());

		porcelain.eval(scriptTwo);

		result = porcelain.invokeFunction("add", 22,20);
		assertTrue(result.isPresent());
		res = result.get();
		int d = ((Number) res).intValue();

		result = porcelain.invokeFunction("subtract", d, 20);
		assertTrue(result.isPresent());
		res = result.get();
		assertTrue(res instanceof Number);
		assertEquals(22, ((Number) res).intValue());
	}

	@Test
	public void loadJavaScriptFromResourcesAndInvokeMethod(){
		assertTrue(porcelain.load("SimpleScript.js"));

		Optional<Object> result = porcelain.invokeFunction("returnFive");

		assertTrue(result.isPresent());
		Object res = result.get();
		assertTrue(res instanceof Number);
		assertEquals(5, ((Number) res).intValue());
	}
}