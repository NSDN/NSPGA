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

    ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);

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
        EXECUTOR.execute(() -> {
            Thread t = new Thread(() -> run(r));
            t.start();
            run(() -> {t.join(1000); t.stop();});
        });
    }

    String getDefaultCode();
    String getCode();

    default int timebase() { return 1; }

    void initialize();
    Number output(Number input);

}
