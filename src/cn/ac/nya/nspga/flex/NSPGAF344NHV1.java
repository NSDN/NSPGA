package cn.ac.nya.nspga.flex;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by drzzm32 on 2020.4.6.
 */
public class NSPGAF344NHV1 implements INSPGAFlex {

    protected ScriptEngine engine;
    protected String code = "";

    protected static final int REG_CNT = 32;
    protected int[] regs = new int[REG_CNT];
    protected int prevInput = 0;

    public NSPGAF344NHV1(String code) {
        this.code = code;
    }

    @Override
    public final String getDefaultCode() {
        return  "/**\n" +
                " * Part: NSPGAF344NHV1\n" +
                " * Vendor: NSDN\n" +
                " */\n" +
                "\n" +
                "// NOTE: The unit is tick\n" +
                "function timebase() { return 1; }\n" +
                "\n" +
                "/**\n" +
                " * DO NOT USE INF LOOP\n" +
                " * while (true) ...\n" +
                " */\n" +
                "// NOTE: use regs like [rX], X = 0~31\n" +
                "// NOTE: trigger's bit is 1 when input changed\n" +
                "//   int logic(int,   int    )\n" +
                "function logic(input, trigger) {\n" +
                "    // TODO: Add code\n" +
                "    return input;\n" +
                "}\n";
    }

    @Override
    public String getCode() {
        if (code == null || code.isEmpty())
            code = getDefaultCode();
        return code;
    }

    @Override
    public final void initialize() {
        for (int i = 0; i < REG_CNT; i++)
            regs[i] = 0;
        engine = INSPGAFlex.getEngine();
        for (int i = 0; i < REG_CNT; i++)
            engine.put("r" + i, regs[i]);
        INSPGAFlex.run(() -> engine.eval(getCode()));
    }

    @Override
    public final int timebase() {
        AtomicReference<Integer> result = new AtomicReference<>(1);
        INSPGAFlex.run(() -> {
            Invocable invocable = (Invocable) engine;
            Object obj = invocable.invokeFunction("timebase");
            if (obj instanceof Number)
                result.set((int) obj);
        });
        if (result.get() <= 0) result.set(1);
        return result.get();
    }

    @Override
    public final Number output(Number input) {
        AtomicReference<Integer> result = new AtomicReference<>(0);
        INSPGAFlex.run(() -> {
            Invocable invocable = (Invocable) engine;
            Object obj = invocable.invokeFunction("logic", input, prevInput ^ input.intValue());
            for (int i = 0; i < REG_CNT; i++) {
                Object o = engine.get("r" + i);
                if (o instanceof Number)
                    regs[i] = ((Number) o).intValue();
            }
            prevInput = input.intValue();
            if (obj instanceof Number)
                result.set(((Number) obj).intValue());
        });
        return result.get();
    }

}
