package com.thoughtworks.nfc;

import javax.smartcardio.*;
import java.util.List;

public class NfcDemo {

    public static void main(String[] args) throws Exception {
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();
        CardTerminal terminal = terminals.get(0);

        if (terminal.waitForCardPresent(60000)) {
            Card card = terminal.connect("*");

            CardChannel channel = card.getBasicChannel();

            byte[] loadKey = {(byte)0xFF, (byte)0x82, 0x00, 0x00, 0x06, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
            byte[] authentication = {(byte)0xFF, (byte)0x86, 0x00, 0x00, 0x05, 0x01, 0x00, 0x04, 0x60, 0x00};
            byte[] readData = {(byte)0xFF, (byte)0xB0, 0x00, 0x04, 0x10};

            CommandAPDU loadKeyCommand = new CommandAPDU(loadKey);
            CommandAPDU authenticationCommand = new CommandAPDU(authentication);
            CommandAPDU readDataCommand = new CommandAPDU(readData);

            ResponseAPDU responseLoadKey = channel.transmit(loadKeyCommand);
            ResponseAPDU responseAuthentication = channel.transmit(authenticationCommand);
            ResponseAPDU responseReadData = channel.transmit(readDataCommand);


            System.out.println(responseLoadKey.getSW1());
            System.out.println(responseLoadKey.getSW1());

            System.out.println(responseAuthentication.getSW1());
            System.out.println(responseAuthentication.getSW2());

            System.out.println(responseReadData.getSW1());
            System.out.println(responseReadData.getSW2());

            for(byte b : responseReadData.getData())
                System.out.print(b);
        }
    }
}