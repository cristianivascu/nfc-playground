package com.thoughtworks.nfc;


import javax.smartcardio.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NfcApp {

    public void run() throws Exception {
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();
        CardTerminal terminal = terminals.get(0);

        if (terminal.waitForCardPresent(60000)) {
            Card card = terminal.connect("*");

            CardChannel channel = card.getBasicChannel();

//            byte[] loadKey = {(byte)0xFF, (byte)0x82, 0x00, 0x00, 0x06, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
//            byte[] authentication = {(byte)0xFF, (byte)0x86, 0x00, 0x00, 0x05, 0x01, 0x00, 0x04, 0x60, 0x00};

            for (int i = 4; i < 39; i++) {
                ResponseAPDU responseReadData = readNfcTag((byte) i, (byte) 4, channel);
                StringBuilder sb = new StringBuilder();
                for (byte b : responseReadData.getBytes())
                    sb.append(String.format("%02X ", b));
                System.out.println(sb.toString());
            }

            writeNfcTagUrl("learn.adafruit.com/adafruit-pn532-rfid-nfc/ndef", channel);


//            CommandAPDU readDataCommand = new CommandAPDU(writeData);

//            CommandAPDU loadKeyCommand = new CommandAPDU(loadKey);
//            CommandAPDU authenticationCommand = new CommandAPDU(authentication);
            //           CommandAPDU readDataCommand = new CommandAPDU(readData);

//            ResponseAPDU responseLoadKey = channel.transmit(loadKeyCommand);
//            ResponseAPDU responseAuthentication = channel.transmit(authenticationCommand);
//            ResponseAPDU responseReadData = channel.transmit(readDataCommand);


//            System.out.println(responseLoadKey.getSW1());
//            System.out.println(responseLoadKey.getSW1());
//
//            System.out.println(responseAuthentication.getSW1());
//            System.out.println(responseAuthentication.getSW2());

//            System.out.println(responseReadData.getSW1());
//            System.out.println(responseReadData.getSW2());
//
//            StringBuilder sb = new StringBuilder();
//            for(byte b : responseReadData.getData())
//                sb.append(String.format("%02X ", b));
//            System.out.println(sb.toString());
        }
    }

    public void writeNfcTagUrl(String url, CardChannel channel) throws Exception {

        byte[] urlAsBytes = url.getBytes(Charset.forName("UTF-8"));
        int numberOfBytes = urlAsBytes.length;
        byte[] urlAsBytesPadded = new byte[numberOfBytes + (4 - (numberOfBytes % 4))];

        for (int i = 0; i < numberOfBytes; i++) {
            urlAsBytesPadded[i] = urlAsBytes[i];
        }
        initialiseUrlData(numberOfBytes, channel);

        System.out.println(urlAsBytesPadded.length);
        for (int byteIndex = 0, pageAddress = 7; byteIndex <= urlAsBytesPadded.length; byteIndex += 4, pageAddress += 1) {
            byte[] writeData = {
                    (byte) 0xFF,
                    (byte) 0xD6,
                    0x00,
                    (byte) pageAddress,
                    0x04,
                    urlAsBytesPadded[byteIndex],
                    urlAsBytesPadded[byteIndex + 1],
                    urlAsBytesPadded[byteIndex + 2],
                    urlAsBytesPadded[byteIndex + 3]
            };
            CommandAPDU writeDataCommand = new CommandAPDU(writeData);
            ResponseAPDU responseAPDU = channel.transmit(writeDataCommand);
            System.out.println(responseAPDU.getSW1() + " " + responseAPDU.getSW2());
        }
    }

    private void initialiseUrlData(int numberOfBytes, CardChannel channel) throws Exception {
//        byte[] writeData1 = {(byte) 0xFF, (byte) 0xD6, 0x00, (byte) 0x04, 0x04, (byte) 0x01, (byte) 0x03, (byte) 0xA0, (byte) 0x10};
//        byte[] writeData2 = {(byte) 0xFF, (byte) 0xD6, 0x00, (byte) 0x05, 0x04, (byte) 0x44, (byte) 0x03, (byte) 0x12, (byte) 0xD1};
//        byte[] writeData3 = {(byte) 0xFF, (byte) 0xD6, 0x00, (byte) 0x06, 0x04, (byte) 0x01, (byte) 0x0E, (byte) 0x55, (byte) 0x02};
        byte[] writeData1 = {(byte) 0xFF, (byte) 0xD6, 0x00, (byte) 0x04, 0x04, (byte) 0x01, (byte) 0x03, (byte) 0xA0, (byte) 0x0C};
        byte[] writeData2 = {(byte) 0xFF, (byte) 0xD6, 0x00, (byte) 0x05, 0x04, (byte) 0x34, (byte) 0x03, (byte) (numberOfBytes + 4), (byte) 0xD1};
        byte[] writeData3 = {(byte) 0xFF, (byte) 0xD6, 0x00, (byte) 0x06, 0x04, (byte) 0x01, (byte) numberOfBytes, (byte) 0x55, (byte) 0x02};

        CommandAPDU writeDataCommand = new CommandAPDU(writeData1);
        channel.transmit(writeDataCommand);
        writeDataCommand = new CommandAPDU(writeData2);
        channel.transmit(writeDataCommand);
        writeDataCommand = new CommandAPDU(writeData3);
        channel.transmit(writeDataCommand);
    }

    public ResponseAPDU readNfcTag(byte block, byte numberOfBytes, CardChannel channel) throws Exception {
        byte[] readData = {(byte) 0xFF, (byte) 0xB0, 0x00, block, numberOfBytes};
        CommandAPDU readDataCommand = new CommandAPDU(readData);
        ResponseAPDU responseReadData = channel.transmit(readDataCommand);

        return responseReadData;
    }
}
