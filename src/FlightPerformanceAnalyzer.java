import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FlightPerformanceAnalyzer {
    private List<Flight> flights; // 传入的航班数据

    // 构造方法
    public FlightPerformanceAnalyzer(List<Flight> flights) {
        this.flights = flights;
    }

    // 平均准点率
    public double calculateOnTimeRate() {
        long onTimeFlights = flights.stream()
                .filter(flight -> flight.getStatus() != FlightStatus.DELAYED) // 检查航班状态为非延误
                .count();
        return (double) onTimeFlights / flights.size() * 100;
    }

    // 客座率趋势分析
    public Map<String, Double> analyzeSeatOccupancyTrend() {
        return flights.stream()
                .collect(Collectors.toMap(
                        Flight::getFlightNumber, // 使用航班号作为键
                        flight -> {
                            int totalPassengers = flight.getPassengers().size(); // 乘客数量
                            int capacity = flight.getCapacity(); // 总容量
                            return (double) totalPassengers / capacity * 100; // 计算客座率
                        }
                ));
    }

    // 航班取消率
    public double calculateCancellationRate() {
        long cancelledFlights = flights.stream()
                .filter(flight -> flight.getStatus() == FlightStatus.CANCELLED) // 筛选状态为已取消的航班
                .count();
        return (double) cancelledFlights / flights.size() * 100;
    }
}