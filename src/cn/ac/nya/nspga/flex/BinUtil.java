package cn.ac.nya.nspga.flex;

/**
 * Created by drzzm32 on 2020.4.7.
 */
public class BinUtil {

    public static byte get(byte val, int bit) {
        return (byte) ((val & (1 << bit)) != 0 ? 1 : 0);
    }

    public static byte set(byte val, int bit) {
        return (byte) (val | (1 << bit));
    }

    public static byte rst(byte val, int bit) {
        return (byte) (val & ~(1 << bit));
    }

    public static int get(int val, int bit) {
        return (val & (1 << bit)) != 0 ? 1 : 0;
    }

    public static int set(int val, int bit) {
        return val | (1 << bit);
    }

    public static int rst(int val, int bit) {
        return val & ~(1 << bit);
    }

}
