package org.example;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Processing process = new Processing();
        process.dataProcess();

        List<BlockHashes> blocksAndHashes = process.getBlockHashesList();
        //Original
        System.out.println("Original");
        authority(blocksAndHashes, process.getH0());
        //Modified the file
        System.out.println("Modified the file");
        blocksAndHashes.get(10).setBlock(STR."\{blocksAndHashes.get(10).getBlock()}af");
        authority(blocksAndHashes, process.getH0());

    }
    public static String hashFunction(String string) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        if (string.length() % 2 != 0) {
            System.err.println("Invalid hex string length.");
            return null;
        }


        byte[] bytes = new byte[string.length() / 2];


        for (int i = 0; i < string.length(); i += 2) {
            String hexByte = string.substring(i, i + 2);
            bytes[i / 2] = (byte) Integer.parseInt(hexByte, 16);
        }
        byte[] h = digest.digest(bytes);
        return bytesToHex(h);
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
    private static void authority(List<BlockHashes> blocksAndHashes, String h) throws NoSuchAlgorithmException {
        for (BlockHashes blockAndHash : blocksAndHashes) {
            if(Objects.equals(h, hashFunction(blockAndHash.getBlock() + blockAndHash.getH()))){
                h = blockAndHash.getH();
            } else {
                System.out.println("Failed");
                return;
            }
        }
        System.out.println("success");
    }
}