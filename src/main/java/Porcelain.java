import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Optional;

public class Porcelain {

	private final ScriptEngine engine;
	private final Invocable invocable;

	public Porcelain() {
		engine = new ScriptEngineManager().getEngineByName("nashorn");
		invocable = (Invocable) engine;
	}

	public Optional<Object> eval(String script) {
		Optional<Object> result;
		try {
			result = Optional.of(engine.eval(script));
		} catch (ScriptException e) {
			result = Optional.empty();
		}
		return result;
	}

	public Optional<Object> invokeFunction(String name, Object... args) {
		Optional<Object> ret;
		try {
			Object result = invocable.invokeFunction(name, args);
			ret = Optional.of(result);
		} catch (Exception e) {
			ret = Optional.empty();
		}
		return ret;
	}

	public boolean load(String scriptPath){
		try {
			//Works in IntelliJ for this project. Path depends on which directory is "working directory"
			engine.eval("load('src/main/javascript/" + scriptPath + "')");
			return true;
		} catch (ScriptException e) {
			return false;
		}
	}

	@Deprecated
	//Does not work very well when run without dyn.js
	public boolean initNpm(){
		try {
			engine.eval("load('./jvm-npm.js')");
			return true;
		} catch (ScriptException e) {
			return false;
		}
	}
}
