package cn.ac.nya.nspga.flex;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.*;

/**
 * Created by drzzm32 on 2020.4.6.
 */
public interface INSPGAFlex {

    ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(8);

    NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();
    static ScriptEngine getEngine() {
        ScriptEngine engine = FACTORY.getScriptEngine((c) -> c.equals(BinUtil.class.getName()));
        engine.getContext().setReader(new StringReader(""));
        engine.getContext().setWriter(new StringWriter());
        engine.getContext().setErrorWriter(new StringWriter());
        run(() -> engine.eval("var Bin = Java.type(\"" + BinUtil.class.getName() + "\");"));
        return engine;
    }

    @FunctionalInterface
    interface Run {
        void run() throws Exception;
    }

    static void run(Run r) {
        try {
            r.run();
        } catch (Exception ignored) { }
    }

    static void schedule(Run r) {
        EXECUTOR.setKeepAliveTime(1000, TimeUnit.MILLISECONDS);
        EXECUTOR.schedule(() -> {
            try {
                r.run();
            } catch (Exception ignored) { }
        }, 0, TimeUnit.MILLISECONDS);
    }

    String getDefaultCode();
    String getCode();

    default int timebase() { return 1; }

    void initialize();
    Number output(Number input);

}
