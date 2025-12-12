

import java.util.stream.Collector;

public class IncomeStatistics {
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private double sum = 0;
    private double sumSq = 0;
    private long count = 0;

    public void accept(double value) {
        if (value < min) min = value;
        if (value > max) max = value;
        sum += value;
        sumSq += value * value;
        count++;
    }

    public IncomeStatistics combine(IncomeStatistics other) {
        if (other.min < min) min = other.min;
        if (other.max > max) max = other.max;
        sum += other.sum;
        sumSq += other.sumSq;
        count += other.count;
        return this;
    }

    public double getMin() {
        return count == 0 ? 0 : min;
    }

    public double getMax() {
        return count == 0 ? 0 : max;
    }

    public double getAverage() {
        return count == 0 ? 0 : sum / count;
    }

    public double getStandardDeviation() {
        if (count <= 1) return 0.0;
        double variance = (sumSq - (sum * sum) / count) / (count - 1);
        return Math.sqrt(Math.max(0, variance));
    }

    @Override
    public String toString() {
        return String.format("Stats [Min: %.2f, Max: %.2f, Avg: %.2f, StdDev: %.2f]",
                getMin(), getMax(), getAverage(), getStandardDeviation());
    }

    public static Collector<SurveyParticipant, IncomeStatistics, IncomeStatistics> collector() {
        return Collector.of(
                IncomeStatistics::new,
                (stats, p) -> stats.accept(p.getMonthlyIncome()),
                IncomeStatistics::combine,
                Collector.Characteristics.UNORDERED
        );
    }
}