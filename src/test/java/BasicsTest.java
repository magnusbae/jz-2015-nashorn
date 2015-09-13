import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.api.scripting.ScriptUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class BasicsTest {

	private ScriptEngine engine;
	private PrintStream stdout;
	private ByteArrayOutputStream outContent;
	public static final String CONSOLE_LOG_INJECTION_SCRIPT = "var console = { log: print, dir: print };";

	private Object injectConsoleLogMethods() throws ScriptException {
		return engine.eval("var console = { log: print, dir: print };");
	}

	@Before
	public void setUp() throws Exception {
		stdout = System.out;

		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));

		engine = new ScriptEngineManager().getEngineByName("nashorn");
	}

	@After
	public void tearDown() throws Exception {
		System.setOut(stdout);
	}

	@Test(expected = ScriptException.class)
	public void returnWithoutFunctionThrowsException() throws ScriptException {
		engine.eval("return 42;");

//		javax.script.ScriptException: <eval>:1:0 Invalid return statement
//		return 42;
	}

	@Test
	public void scriptReturnsFortyTwo() throws ScriptException {
		String script = " (   function(){ return 42; }   ) (); ";

		Object result = engine.eval(script);

		assertTrue(result instanceof Number);

		assertEquals(42, ((Number) result).intValue());
	}

	@Test
	public void canInvokeNamedFunctionWithParameters() throws ScriptException, NoSuchMethodException {
		String script = "function add(a, b){ return a + b; };";

		engine.eval(script);
		Invocable invocable = (Invocable) engine;

		Object result = invocable.invokeFunction("add", 22, 20);

		assertTrue(result != null);
		assertTrue(result instanceof Number);

		assertEquals(42, ((Number) result).intValue());
	}

	@Test
	public void canInvokeNamedFunctionUsingInterface() throws ScriptException, NoSuchMethodException {
		String script = "function add(a, b){ return a + b; };";

		engine.eval(script);
		Invocable invocable = (Invocable) engine;
		Adder adder = invocable.getInterface(Adder.class);

		int result = adder.add(22, 20);

		assertEquals(42, result);
	}

	@Test(expected = ScriptException.class)
	public void consoleLogFunctionIsNotNative() throws ScriptException {
		engine.eval("console.log('This text should not be output to the console');");

		//javax.script.ScriptException: ReferenceError: "console" is not defined in <eval> at line number 1
	}

	@Test
	public void consoleLogCanBeInjected() throws ScriptException {
		String consolePrintString = "This text should be printed to standard out";
		String script = "console.log('" + consolePrintString + "');";

		injectConsoleLogMethods();
		engine.eval(script);

		assertEquals(consolePrintString, outContent.toString().trim());
	}

	@Test(expected = ScriptException.class)
	public void failsToHandleLodashNpmDependencyUsingJvmNpm() throws ScriptException {
		engine.eval("load('lib/jvm-npm.js');");
		engine.eval("load('src/main/javascript/LodashExample.js');");

	/*
		javax.script.ScriptException: TypeError: Cannot read property "prototype" from undefined in lib/jvm-npm.js at line number 108 at column number 8
		at jdk.nashorn.api.scripting.NashornScriptEngine.throwAsScriptException(NashornScriptEngine.java:455)
		at jdk.nashorn.api.scripting.NashornScriptEngine.evalImpl(NashornScriptEngine.java:439)
		at jdk.nashorn.api.scripting.NashornScriptEngine.evalImpl(NashornScriptEngine.java:401)
		at jdk.nashorn.api.scripting.NashornScriptEngine.evalImpl(NashornScriptEngine.java:397)
		at jdk.nashorn.api.scripting.NashornScriptEngine.eval(NashornScriptEngine.java:152)
		at javax.script.AbstractScriptEngine.eval(AbstractScriptEngine.java:264)
		at BasicsTest.failsToHandleLodashNpmDependencyUsingDynJs(BasicsTest.java:88)
		...
		Caused by: lib/jvm-npm.js:108:8 TypeError: Cannot read property "prototype" from undefined

	*/
	}

	@Test
	public void lodashCanBeLoadedUsingNashorn() throws ScriptException, NoSuchMethodException {
		engine.eval("load('lib/lodash.js');");
		injectConsoleLogMethods();
		engine.eval("load('src/main/javascript/LodashExampleWoRequire.js');");

		Object result = engine.eval("Java.to(returnArray(), 'int[]')");
		assertNotNull(result);
		assertTrue(result instanceof int[]);
		int[] arr = (int[]) result;

		assertEquals(3, arr.length);
		assertEquals(1, arr[0]);
		assertEquals(2, arr[1]);
		assertEquals(3, arr[2]);
	}

	@Test(expected = ClassCastException.class)
	public void cannotConvertJavaScriptArrayFromScriptObjectMirrorToArrayUsingScriptUtils() throws ScriptException, NoSuchMethodException {
		injectConsoleLogMethods();
		engine.eval("load('lib/lodash.js');");
		engine.eval("load('src/main/javascript/LodashExampleWoRequire.js');");

		Invocable invocable = (Invocable) engine;
		Object result = invocable.invokeFunction("returnArray");
		assertNotNull(result);
		assertTrue(result instanceof ScriptObjectMirror);
		ScriptObjectMirror res = (ScriptObjectMirror) result;

		assertTrue(res.isArray());
		Number[] array = (Number[]) ScriptUtils.convert(res, Number[].class);

		/*
			java.lang.ClassCastException: Cannot cast jdk.nashorn.api.scripting.ScriptObjectMirror to [Ljava.lang.Number;
			at java.lang.invoke.MethodHandleImpl.newClassCastException(MethodHandleImpl.java:361)
			at java.lang.invoke.MethodHandleImpl.castReference(MethodHandleImpl.java:356)
			at jdk.nashorn.api.scripting.ScriptUtils.convert(ScriptUtils.java:164)
			at BasicsTest.loadLodashUsingNashornLoad(BasicsTest.java:122)
		*/
	}

	@Test
	public void usingFakeRequireAllowsToLoadLodashUsingRequire() throws ScriptException {
		String script = "function require(name){ " +
					"load('lib/' + name + '.js'); " +
					"return _; " +
				"}";

		injectConsoleLogMethods();
		engine.eval(script);

		engine.eval("load('src/main/javascript/LodashExample.js');");

	}
}