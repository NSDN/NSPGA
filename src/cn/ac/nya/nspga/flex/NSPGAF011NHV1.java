package cn.ac.nya.nspga.flex;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by drzzm32 on 2020.4.6.
 */
public class NSPGAF011NHV1 implements INSPGAFlex {

    protected ScriptEngine engine;
    protected String code = "";
    protected byte[] LUT = new byte[0xFF];

    public NSPGAF011NHV1(String code) {
        this.code = code;
    }

    @Override
    public final String getDefaultCode() {
        return  "/**\n" +
                " * Part: NSPGAF011NHV1\n" +
                " * Vendor: NSDN\n" +
                " */\n" +
                "\n" +
                "/**\n" +
                " * DO NOT USE INF LOOP\n" +
                " * while (true) ...\n" +
                " */\n" +
                "//  byte logic(byte )\n" +
                "function logic(input) {\n" +
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
        engine = INSPGAFlex.getEngine();
        INSPGAFlex.run(() -> engine.eval(getCode()));
        for (int i = 0; i < 0xFF; i++)
            LUT[i] = testOutput((byte) i);
    }

    protected byte testOutput(byte input) {
        AtomicReference<Byte> result = new AtomicReference<>((byte) 0x00);
        INSPGAFlex.run(() -> {
            Invocable invocable = (Invocable) engine;
            Object obj = invocable.invokeFunction("logic", input);
            if (obj instanceof Number)
                result.set(((Number) obj).byteValue());
        });
        return result.get();
    }

    @Override
    public final Number output(Number input) {
        return LUT[input.byteValue() & 0xFF];
    }

}
