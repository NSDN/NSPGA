import cn.ac.nya.nspga.*;
import cn.ac.nya.nspga.flex.*;
import java.util.Scanner;

/**
 * Created by drzzm32 on 2018.3.11.
 */
public class Main {

    public static void main(String[] args) {
//        System.out.println("NSPGAT0C0 Test");
//	    NSPGAT0C0Test();
//        System.out.println("NSPGAT4C4 Test");
//	    NSPGAT4C4Test();
//	    System.out.println("getDev Test");
//	    getDevTest();

	    new NSPGAEditor(8, 8, new NSPGAF344NHV1("").getDefaultCode()).setCallback((inputs, outputs, code) -> {
            INSPGAFlex dev = new NSPGAF344NHV1(code);
            dev.initialize();
            System.out.println("Timebase: " + dev.timebase());
            for (int i = 0; i < dev.timebase(); i++)
                System.out.println("Output: " + dev.output((byte) i));
        });
    }

    static INSPGA getDev(Class<? extends INSPGA> dev) {
        try {
            return dev.newInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    static void getDevTest() {
        INSPGA dev = getDev(NSPGAT0C0.class);
        if (dev != null) {
            int[] data = {
                    0x10FF, // Inverter Enable
                    0x0000, 0x0000, 0x00FF, 0x0000 // Main MUX
            };
            dev.configure(data);
            byte out = dev.output((byte) 0xFF);
            System.out.println(String.format("Result: %02X", out));
        }
    }

    static void NSPGAT0C0Test() {
        NSPGAT0C0 dev = new NSPGAT0C0();
        int[] data = {
                0x0000, // Inverter Enable
                0x0000, 0x0000, 0x0000, 0x0000 // Main MUX
        };
        dev.configure(data);
        long start = System.nanoTime();
        byte out = dev.output((byte) 0xFF);
        long end = System.nanoTime();
        System.out.println(String.format("Result: %02X", out));
        System.out.println(String.format("Took %d ns", end - start));
    }

    static void NSPGAT4C4Test() {
        NSPGAT4C4 dev = new NSPGAT4C4();
        int[] data = {
                0x0000, // Inverter Enable
                0x0000, // Trigger Switch & Counter Connection MUX (MSB -> LSB)
                0x0000, 0x0000, // Output MUX
                0x0000, 0x0000, 0x0000, 0x0000, // RS-Latch MUX (R -> LSB, S -> MSB)
                0x0000, 0x0000, // Counter Register
                0x0000, // Counter Input MUX
                0x0000, // Counter RS-Latch MUX
        };
        dev.configure(data);
        long start = System.nanoTime();
        byte out = dev.output((byte) 0xFF);
        long end = System.nanoTime();
        System.out.println(String.format("Result: %02X", out));
        System.out.println(String.format("Took %d ns", end - start));
    }

}
