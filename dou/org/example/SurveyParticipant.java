
import java.util.Objects;

public class SurveyParticipant {
    private final String firstName;
    private final String city;
    private final int age;
    private final double monthlyIncome;

    public SurveyParticipant(String firstName, String city, int age, double monthlyIncome) {
        this.firstName = firstName;
        this.city = city;
        this.age = age;
        this.monthlyIncome = monthlyIncome;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }

    public int getAge() {
        return age;
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    @Override
    public String toString() {
        return String.format("Participant{city='%s', name='%s', age=%d, income=%.2f}",
                city, firstName, age, monthlyIncome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyParticipant that = (SurveyParticipant) o;
        return age == that.age &&
                Double.compare(that.monthlyIncome, monthlyIncome) == 0 &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, city, age, monthlyIncome);
    }
}