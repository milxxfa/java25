

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        Stream<SurveyParticipant> sourceStream = Stream.generate(SurveyUtils::generateRandomParticipant);

        int nToSkip = 20;
        int totalLimit = 500;
        String cityCondition = "Kyiv";

        List<SurveyParticipant> participants = sourceStream
                .gather(SurveyUtils.skipAndLimitGatherer(nToSkip, totalLimit, cityCondition))
                .collect(Collectors.toList());

        int minAge = 25;
        int maxAge = 35;

        Map<String, List<SurveyParticipant>> groupedByName = participants.stream()
                .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
                .collect(Collectors.groupingBy(SurveyParticipant::getFirstName));

        groupedByName.forEach((name, list) ->
                System.out.println(name + ": " + list.size())
        );

        IncomeStatistics stats = participants.stream()
                .collect(IncomeStatistics.collector());
        System.out.println(stats);

        analyzeOutliers(participants);
    }

    private static void analyzeOutliers(List<SurveyParticipant> data) {
        List<Double> sortedIncomes = data.stream()
                .map(SurveyParticipant::getMonthlyIncome)
                .sorted()
                .toList();

        if (sortedIncomes.isEmpty()) return;

        double q1 = getPercentile(sortedIncomes, 25);
        double q3 = getPercentile(sortedIncomes, 75);
        double iqr = q3 - q1;
        double lowerFence = q1 - 1.5 * iqr;
        double upperFence = q3 + 1.5 * iqr;

        Map<String, Long> outliersReport = data.stream()
                .collect(Collectors.groupingBy(
                        p -> (p.getMonthlyIncome() < lowerFence || p.getMonthlyIncome() > upperFence)
                                ? "outliers" : "data",
                        Collectors.counting()
                ));

        System.out.println(outliersReport);
    }

    private static double getPercentile(List<Double> sortedData, double percentile) {
        int index = (int) Math.ceil(percentile / 100.0 * sortedData.size()) - 1;
        return sortedData.get(Math.max(0, Math.min(index, sortedData.size() - 1)));
    }
}