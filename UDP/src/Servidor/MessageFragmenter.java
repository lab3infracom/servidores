package Servidor;

import java.util.ArrayList;
import java.util.List;

public class MessageFragmenter {
    private static final int MAX_MESSAGE_SIZE = 64000; // Maximum UDP datagram size is 64KB
    private byte[] message;
    private int fragmentSize;
    
    public MessageFragmenter(byte[] message, int fragmentSize) {
        this.message = message;
        this.fragmentSize = fragmentSize;
    }
    
    public List<byte[]> getFragments() {
        List<byte[]> fragments = new ArrayList<>();
        
        int offset = 0;
        while (offset < message.length) {
            int remaining = message.length - offset;
            int fragmentLength = Math.min(fragmentSize, remaining);
            
            byte[] fragment = new byte[fragmentLength];
            System.arraycopy(message, offset, fragment, 0, fragmentLength);
            
            fragments.add(fragment);
            offset += fragmentLength;
        }
        
        return fragments;
    }
    
    public static void main(String[] args) {
        // Example usage: fragment a message into 64KB fragments
        byte[] message = new byte[1000000]; // 1MB message
        int fragmentSize = MAX_MESSAGE_SIZE;
        MessageFragmenter fragmenter = new MessageFragmenter(message, fragmentSize);
        List<byte[]> fragments = fragmenter.getFragments();
        System.out.println(fragments.size() + " fragments");
    }
}
