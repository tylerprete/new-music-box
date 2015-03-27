package org.processing;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collection;

public class KeyCompressionTest extends TestCase {

    public void testDecompressKeys() throws Exception {

        int c = 'g';
        int d = 'j';
        final Collection<Integer> integers = KeyCompression.decompressKeys(Arrays.asList(c, d));
        System.out.println(integers);

    }
}
