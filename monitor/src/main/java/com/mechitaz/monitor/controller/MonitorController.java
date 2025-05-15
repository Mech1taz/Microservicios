
import org.springframework.web.bind.annotation.*;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

@RestController
@RequestMapping("/monitor")
public class MonitorController {

    @GetMapping("/status")
    public String getSystemStatus() {
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        double cpuLoad = osBean.getSystemCpuLoad(); // 0.0 a 1.0
        long freeMemory = Runtime.getRuntime().freeMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long usedMemory = totalMemory - freeMemory;

        boolean overload = cpuLoad > 0.8 || usedMemory > totalMemory * 0.8;

        return overload
                ? "Sistema en SOBRECARGA :C (CPU: " + String.format("%.2f", cpuLoad * 100) + "%, RAM: " + usedMemory / 1024 / 1024 + "MB)"
                : "Sistema esta OK :3 (CPU: " + String.format("%.2f", cpuLoad * 100) + "%, RAM: " + usedMemory / 1024 / 1024 + "MB)";
    }
}