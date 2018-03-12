import cn.ac.nya.nspga.NSPGAT0C0;
import cn.ac.nya.nspga.NSPGAT4C4;

/**
 * Created by drzzm32 on 2018.3.11.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("NSPGAT0C0 Test");
	    NSPGAT0C0Test();
        System.out.println("NSPGAT4C4 Test");
	    NSPGAT4C4Test();
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
