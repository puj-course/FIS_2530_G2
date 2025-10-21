package sis.local;

import java.io.IOException;

public class TestProfileSelector {

    public static boolean isDockerAvailable() {
        try {
            Process process = new ProcessBuilder("docker", "info").start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }
}