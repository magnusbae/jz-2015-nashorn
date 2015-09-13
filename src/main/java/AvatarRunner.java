import com.oracle.avatar.js.Server;
import com.oracle.avatar.js.Loader;
import com.oracle.avatar.js.log.Logging;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringWriter;

public class AvatarRunner {
	public String runJs(final String javaScriptFile) throws Throwable{
		StringWriter scriptWriter = new StringWriter();
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		ScriptContext scriptContext = engine.getContext();
		scriptContext.setWriter(scriptWriter);

		Server server = new Server(engine, new Loader.Core(), new Logging(false), System.getProperty("user.dir"));
		server.run("src/main/javascript/" + javaScriptFile);

		return scriptWriter.toString();
	}
}
