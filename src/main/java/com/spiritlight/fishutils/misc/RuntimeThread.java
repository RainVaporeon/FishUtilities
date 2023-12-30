package com.spiritlight.fishutils.misc;

import com.spiritlight.fishutils.misc.annotations.MayExplode;

public class RuntimeThread {

    /**
     * Interrupts all active threads
     */
    @MayExplode(MayExplode.Severity.IRRECOVERABLE)
    public static void interruptAll() {
        Thread current = Thread.currentThread();
        Thread.getAllStackTraces().keySet().forEach(thread -> {
            if(thread != current) thread.interrupt();
        });
        current.interrupt();
    }
}
