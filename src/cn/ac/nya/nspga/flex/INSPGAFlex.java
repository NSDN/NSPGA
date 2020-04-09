package cn.ac.nya.nspga.flex;

import jdk.nashorn.api.scripting.NashornScriptEngineFactory;

import javax.script.ScriptEngine;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.concurrent.*;

/**
 * Created by drzzm32 on 2020.4.6.
 */
public interface INSPGAFlex {

    ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors() / 2
    );
    int EXCEED_TIME_MS = 3000;

    NashornScriptEngineFactory FACTORY = new NashornScriptEngineFactory();
    static ScriptEngine getEngine() {
        ScriptEngine engine;
        try {
            engine = FACTORY.getScriptEngine((c) -> c.equals(BinUtil.class.getName()));
        } catch (Exception e) {
            return null;
        }
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

    static void schedule(Run r, Runnable timeoutCallback) {
        EXECUTOR.execute(() -> {
            Thread t = new Thread(() -> run(r));
            t.start();
            long time = System.currentTimeMillis();
            while (t.isAlive()) {
                if (System.currentTimeMillis() - time > EXCEED_TIME_MS) {
                    t.stop();
                    EXECUTOR.getQueue().clear();
                    timeoutCallback.run();
                    break;
                }
            }
        });
    }

    String getDefaultCode();
    String getCode();

    default int timebase() { return 1; }

    void initialize();
    Number output(Number input);

}
