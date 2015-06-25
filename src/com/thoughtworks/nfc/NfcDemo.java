package com.thoughtworks.nfc;

import javax.smartcardio.*;
import java.util.List;

public class NfcDemo {

    public static void main(String[] args) throws Exception {

        NfcApp nfcApp = new NfcApp();
        nfcApp.run();
    }
}