package cn.ac.nya.nspga.flex;

/**
 * Created by drzzm32 on 2020.4.7.
 */
public class BinUtil {

    public static long uint32_t(long val) { return val & 0x0FFFFFFFFL; }

    public static byte get(byte val, int bit) {
        return (byte) ((val & (1 << bit)) != 0 ? 1 : 0);
    }

    public static byte set(byte val, int bit) {
        return (byte) (val | (1 << bit));
    }

    public static byte rst(byte val, int bit) {
        return (byte) (val & ~(1 << bit));
    }

    public static long get(long val, int bit) {
        return (val & (1 << bit)) != 0 ? 1 : 0;
    }

    public static long set(long val, int bit) {
        return uint32_t(val | (1 << bit));
    }

    public static long rst(long val, int bit) {
        return uint32_t(val & ~(1 << bit));
    }

}
