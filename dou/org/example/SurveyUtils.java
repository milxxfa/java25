

import java.util.Random;
import java.util.stream.Gatherer;

public class SurveyUtils {

    private static final String[] CITIES = {"Kyiv", "Lviv", "Kharkiv", "Odesa", "Dnipro", "Vinnytsia"};
    private static final String[] NAMES = {"Ivan", "Maria", "Petro", "Olena", "Andriy", "Yulia", "Serhiy", "Svitlana"};
    private static final Random RANDOM = new Random();

    public static SurveyParticipant generateRandomParticipant() {
        String name = NAMES[RANDOM.nextInt(NAMES.length)];
        String city = CITIES[RANDOM.nextInt(CITIES.length)];
        int age = 18 + RANDOM.nextInt(43);

        double baseIncome = 15000 + RANDOM.nextDouble() * 85000;
        if (RANDOM.nextDouble() < 0.05) baseIncome *= 5;

        return new SurveyParticipant(name, city, age, baseIncome);
    }

    public static Gatherer<SurveyParticipant, ?, SurveyParticipant> skipAndLimitGatherer(
            int skipCount, int targetSize, String skipCity) {

        class State {
            int skipped = 0;
            int collected = 0;
        }

        return Gatherer.ofSequential(
                State::new,
                (state, element, downstream) -> {
                    if (state.skipped < skipCount && element.getCity().equals(skipCity)) {
                        state.skipped++;
                        return true;
                    }

                    if (state.collected >= targetSize) {
                        return false;
                    }

                    downstream.push(element);
                    state.collected++;

                    return state.collected < targetSize;
                }
        );
    }
}
