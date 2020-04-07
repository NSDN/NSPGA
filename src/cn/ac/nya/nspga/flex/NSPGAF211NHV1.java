package cn.ac.nya.nspga.flex;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by drzzm32 on 2020.4.6.
 */
public class NSPGAF211NHV1 implements INSPGAFlex {

    protected ScriptEngine engine;
    protected String code = "";

    protected static final int REG_CNT = 16;
    protected byte[] regs = new byte[REG_CNT];
    protected byte prevInput = 0x00;

    public NSPGAF211NHV1(String code) {
        this.code = code;
    }

    @Override
    public final String getDefaultCode() {
        return  "/**\n" +
                " * Part: NSPGAF211NHV1\n" +
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
                "// NOTE: use regs like [rX], X = 0~15\n" +
                "// NOTE: trigger's bit is 1 when input changed\n" +
                "//  byte logic(byte,  byte   )\n" +
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
        AtomicReference<Byte> result = new AtomicReference<>((byte) 0x00);
        INSPGAFlex.run(() -> {
            Invocable invocable = (Invocable) engine;
            Object obj = invocable.invokeFunction("logic", input.byteValue(), prevInput ^ input.byteValue());
            for (int i = 0; i < REG_CNT; i++) {
                Object o = engine.get("r" + i);
                if (o instanceof Number)
                    regs[i] = ((Number) o).byteValue();
            }
            prevInput = input.byteValue();
            if (obj instanceof Number)
                result.set(((Number) obj).byteValue());
        });
        return result.get();
    }

}
