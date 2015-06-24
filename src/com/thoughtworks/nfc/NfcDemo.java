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

//            byte[] loadKey = {(byte)0xFF, (byte)0x82, 0x00, 0x00, 0x06, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
//            byte[] authentication = {(byte)0xFF, (byte)0x86, 0x00, 0x00, 0x05, 0x01, 0x00, 0x04, 0x60, 0x00};


            for(int i = 4; i < 39; i ++) {
                byte[] readData = {(byte)0xFF, (byte)0xB0, 0x00, (byte)i, (byte)0x04};
                CommandAPDU readDataCommand = new CommandAPDU(readData);
                ResponseAPDU responseReadData = channel.transmit(readDataCommand);

//                System.out.println(responseReadData.getSW1());
//                System.out.println(responseReadData.getSW2());

                StringBuilder sb = new StringBuilder();
                for(byte b : responseReadData.getBytes())
                    sb.append(String.format("%02X ", b));
                System.out.println(sb.toString());

            }

            byte[] writeData1 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x04,  0x04, (byte)0x01, (byte)0x03, (byte)0xA0, (byte)0x10};
            byte[] writeData2 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x05,  0x04, (byte)0x44, (byte)0x03, (byte)0x12, (byte)0xD1};
            byte[] writeData3 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x06,  0x04, (byte)0x01, (byte)0x0E, (byte)0x55, (byte)0x04};
            byte[] writeData4 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x07,  0x04, (byte)0x67, (byte)0x6F, (byte)0x6F, (byte)0x2E};
            byte[] writeData5 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x08,  0x04, (byte)0x67, (byte)0x6C, (byte)0x2F, (byte)0x6B};
            byte[] writeData6 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x09,  0x04, (byte)0x47, (byte)0x6E, (byte)0x77, (byte)0x78};
            byte[] writeData7 = {(byte)0xFF, (byte)0xD6, 0x00, (byte)0x0A,  0x04, (byte)0x45, (byte)0xFE, (byte)0x00, (byte)0x00};

            CommandAPDU writeDataCommand = new CommandAPDU(writeData1);
            ResponseAPDU responseWriteData = channel.transmit(writeDataCommand);
            writeDataCommand = new CommandAPDU(writeData2);
            responseWriteData = channel.transmit(writeDataCommand);
            writeDataCommand = new CommandAPDU(writeData3);
            responseWriteData = channel.transmit(writeDataCommand);
            writeDataCommand = new CommandAPDU(writeData4);
            responseWriteData = channel.transmit(writeDataCommand);
            writeDataCommand = new CommandAPDU(writeData5);
            responseWriteData = channel.transmit(writeDataCommand);
            writeDataCommand = new CommandAPDU(writeData6);
            responseWriteData = channel.transmit(writeDataCommand);
            writeDataCommand = new CommandAPDU(writeData7);
            responseWriteData = channel.transmit(writeDataCommand);



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
;
        }
    }
}