package org.example;

import lombok.Data;


import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
@Data

public class Processing {

    private  List<BlockHashes> blockHashesList = new ArrayList<>();
    private String h0;
    public void dataProcess() throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream("/home/quyen/IdeaProjects/demo/src/main/resources/videos/birthday.mp4");

        byte[] videoData = fileInputStream.readAllBytes();
        fileInputStream.close();

        int blockSize = 1024;
        List<byte[]> blocks = new ArrayList<>();
        for (int i = 0; i < videoData.length; i += blockSize) {
            byte[] block = new byte[Math.min(blockSize, videoData.length - i)];
            System.arraycopy(videoData, i, block, 0, block.length);
            blocks.add(block);
        }

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] h = digest.digest(blocks.getLast());

        blockHashesList.add(new BlockHashes(bytesToHex(blocks.getLast()), ""));
        for (int i = blocks.size() - 2; i >= 0; i--) {
            byte[] block = blocks.get(i);
            byte[] blockPlusHash = new byte[block.length + h.length];
            System.arraycopy(block, 0, blockPlusHash, 0, block.length);
            System.arraycopy(h, 0, blockPlusHash, block.length, h.length);
            blockHashesList.add(new BlockHashes(bytesToHex(block), bytesToHex(h)));
            h = digest.digest(blockPlusHash);
        }
        blockHashesList = blockHashesList.reversed();
        h0 = bytesToHex(h);
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
}
