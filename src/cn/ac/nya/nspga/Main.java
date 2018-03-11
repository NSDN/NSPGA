package cn.ac.nya.nspga;

/**
 * Created by drzzm32 on 2018.3.11.
 */
public class Main {

    public static void main(String[] args) {
	    NSPGAT4C4 dev = new NSPGAT4C4();
	    int[] data = {
                0x0000, // Inverter Enable
                0x0000, // Trigger Switch & Counter Connection MUX (HSB -> LSB)
                0x0000, 0x0000, // Output MUX
                0x0000, 0x0000, 0x0000, 0x0000, // RS-Latch MUX (R -> LSB, S -> HSB)
                0x0000, 0x0000, // Counter Register
                0x0000, // Counter Input MUX
                0x0000, // Counter RS-Latch MUX
        };
	    dev.configure(data);
	    byte out = dev.output((byte) 0xFF);
	    System.out.println(String.format("Result: %02X", out));
    }

}