package com.spiritlight.fishutils.utils.secure;

import com.spiritlight.fishutils.utils.Numbers;
import com.spiritlight.fishutils.utils.collectors.StringCollectors;
import com.spiritlight.fishutils.utils.enums.Secure;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static com.spiritlight.fishutils.utils.enums.Secure.YES;

public class SeededGenerator {
    public static final String ALPHABETS = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    public static final String NUMERICAL = "0123456789";
    public static final String ALPHABET_NUMERICAL = ALPHABETS + NUMERICAL;
    private final String allowedCharacters;
    private final Supplier<Random> randomInst;
    private long genSeed;

    public SeededGenerator(String allowedCharacters) {
        this(allowedCharacters, Secure.NO);
    }

    public SeededGenerator(String allowedCharacters, Secure secure) {
        this.allowedCharacters = allowedCharacters;
        this.genSeed = System.nanoTime();
        this.randomInst = secure == YES ? () -> new SecureRandom(Numbers.getBytes(this.getSeed()))
                : () -> new Random(this.getSeed());
    }

    public String generate(int size) {
        return this.generate()
                .limit(size)
                .boxed()
                .collect(StringCollectors.concat());
    }

    public IntStream generate() {
        nextSeed();
        final Random random = randomInst.get();
        return IntStream.generate(() -> allowedCharacters.charAt(random.nextInt(allowedCharacters.length())));
    }

    /**
     * XOR-s the current seed
     */
    public void addNoise(long noise) {
        this.genSeed ^= noise;
    }

    public void addNoise(Object noise) {
        this.addNoise(genSeed(noise));
    }

    public void setSeed(long seed) {
        this.genSeed = seed;
    }

    public void setSeed(Object seed) {
        this.setSeed(genSeed(seed));
    }

    /**
     * Refreshes the seed of this generator, the process is independent on
     * current seed value, therefore is unable to provide a consistent
     * output value
     */
    public void refreshSeed() {
        this.setSeed(System.nanoTime());
    }

    public long getSeed() {
        return this.genSeed;
    }

    private void nextSeed() {
        Random random = new Random(genSeed);
        this.genSeed = random.nextLong();
    }

    private static long genSeed(Object in) {
        if(in == null) return 0;
        String stringValue = String.valueOf(in);
        long value = 0;
        for(char c : stringValue.toCharArray()) {
            value += c | in.hashCode();
            value ^= c;
        }
        value *= String.valueOf(in).chars().sum();
        return value;
    }
}
