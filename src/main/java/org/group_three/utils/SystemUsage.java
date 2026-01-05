package org.group_three.utils;


import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

/**
 * Utility class to read system CPU and physical memory usage.
 *
 * @author Leon
 */
public class SystemUsage {

    /**
     * Provides access to system-level metrics
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    private static final OperatingSystemMXBean osInfo =
            ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    /**
     * Default constant of one GB in bytes.
     *
     * @author Leon
     */
    @SuppressWarnings("JavadocDeclaration")
    private static final double GIGA_BYTE = 1073741824.0;

    /**
     * Calls getProcessCpuLoad function which return a value between 0.0
     * and 1.0. Multipy that number by 100 and round to get final percentage.
     *
     * @return system CPU usage as a percentage (0-100), or 0 if the current CPU load
     * could not be determined.
     * @author Leon
     */
    public static int getSystemCpuPercent() {
            double procLoad = osInfo.getProcessCpuLoad();
            if (procLoad >= 0) {
                return (int) Math.round(procLoad * 100);
            }
        return 0;
    }

    /**
     * Calls the getTotalMemorySize function, which returns a value in bytes.
     * To get the desired result, divide it by one GB.
     *
     * @return total physical system memory in GB.
     * @author Leon
     */
    public static double getTotalPhysicalMemoryGB() {
            long bytes = osInfo.getTotalMemorySize();
            return bytes / GIGA_BYTE;
    }

    /**
     * Calls the getFreeMemorySize function, which returns a value in bytes.
     * To get the desired result, divide it by one GB.
     *
     * @return amount of free physical system memory in GB.
     * @author Leon
     */
    public static double getFreePhysicalMemoryGB() {
            long bytes = osInfo.getFreeMemorySize();
            return bytes / GIGA_BYTE;
    }

    /**
     * Get used physical system memory in GB.
     *
     * @return amount of physical system memory in use, in GB.
     * @author Leon
     */
    public static double getUsedPhysicalMemoryGB() {
        return getTotalPhysicalMemoryGB() - getFreePhysicalMemoryGB();
    }
}
