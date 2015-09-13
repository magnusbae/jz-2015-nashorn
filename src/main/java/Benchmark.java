import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Benchmark {

	public static void main(String[] args) throws ScriptException {
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		engine.eval("load('src/main/javascript/Benchmark.js')");
	}
}

/*
$ jjs src/main/javascript/Benchmark.js
RegExp#test x 5,,503,,565 ops/sec +/-5.94% (61 runs sampled)
String#indexOf x 23,,423,,545 ops/sec +/-0.39% (69 runs sampled)
lodash#sortBySin(n) x 46,,411 ops/sec +/-44.25% (62 runs sampled)
Fastest is String#indexOf
*/