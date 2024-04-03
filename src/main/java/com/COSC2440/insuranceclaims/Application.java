package com.COSC2440.insuranceclaims;

import com.COSC2440.insuranceclaims.managers.SimpleClaimProcessManager;
import com.COSC2440.insuranceclaims.ui.ConsoleUI;

public class Application {
    public static void main(String[] args) {
        SimpleClaimProcessManager claimManager = new SimpleClaimProcessManager();
        // Ideally, load existing claims here from a file using FileManager

        ConsoleUI ui = new ConsoleUI(claimManager);
        ui.start();
    }
}
