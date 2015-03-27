package org.processing;

import java.util.*;

public class KeyCompression {

    public final static List<Integer> baseCompressionKeys;
    static {
        baseCompressionKeys = new ArrayList<>();
        final List<Character> characters = Arrays.asList('a', 'h', 'o', 'v', '1');
        for (Character c : characters) {
            baseCompressionKeys.add((int) c);
        }
//        for (int i = 0; i < 6; i++) {
//            baseCompressionKeys.add('a' + (7 * i));
//        }
    }

    public static int keyForOffset(int offset) {
        int q = offset / 3;
        int w = offset % 3;
        return baseCompressionKeys.get(q) + w;
    }

    public static Collection<Integer> decompressKeys(Collection<Integer> compressedKeys) {
        Collection<Integer> resultKeys = new ArrayList<>();
        for (Integer key : compressedKeys) {
            if (key == '8' || key == '9' || key == '0' || key == '-' || key == '=' || key ==',') {
                int c = 'p';
                if (key == '9') c = c+1;
                if (key == '0') c = c+2;
                if (key == '-') c = c+3;
                if (key == '=') c = c+4;
                if (key == ',') c = c+5;
                resultKeys.add(c);
                continue;
            }
            for (int tKey : baseCompressionKeys) {
                int baseKey = baseKeyLookup(tKey);
                if (key == tKey) {
                    resultKeys.add(baseKey);
                } else if (key == tKey + 1) {
                    resultKeys.add(baseKey + 1);
                } else if (key == tKey + 2) {
                    resultKeys.add(baseKey + 2);
                }
                else if (key == tKey + 3) {
                    resultKeys.add(baseKey);
                    resultKeys.add(baseKey + 1);
                }
                else if (key == tKey + 4) {
                    resultKeys.add(baseKey);
                    resultKeys.add(baseKey + 2);
                }
                else if (key == tKey + 5) {
                    resultKeys.add(baseKey + 1);
                    resultKeys.add(baseKey + 2);
                }
                else if (key == tKey + 6) {
                    resultKeys.add(baseKey);
                    resultKeys.add(baseKey + 1);
                    resultKeys.add(baseKey + 2);
                }
            }
        }
        return resultKeys;
    }

    private static int baseKeyLookup(int tKey) {
        if (tKey == 'a') {
            return 'a';
        } else if (tKey == 'h') {
            return 'd';
        } else if (tKey == 'o') {
            return 'g';
        } else if (tKey == 'v') {
            return 'j';
        } else if (tKey == '1') {
            return 'm';
        }
//        else if (tKey == '[') {
//            return 'p';
//        }
        return 255;
    }
}
