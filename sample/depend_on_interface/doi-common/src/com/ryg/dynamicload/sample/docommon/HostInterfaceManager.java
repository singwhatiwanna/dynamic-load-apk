package com.ryg.dynamicload.sample.docommon;

public class HostInterfaceManager {

    /**
     * this is just a sample, try not to use static field to avoid memory leak
     */
    private static HostInterface hostInterface;
    public static HostInterface getHostInterface() {
        return hostInterface;
    }
    
    public static void setHostInterface(HostInterface hostInterface) {
        HostInterfaceManager.hostInterface = hostInterface;
    }
}
