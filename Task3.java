import java.util.*;
import java.util.concurrent.*;

class Question {
    String question;
    String[] options;
    int correctAnswer;

    Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }
}

public class QuizApplication {

    private static final int TIME_LIMIT = 20; // seconds
    private static int score = 0;

    public static void main(String[] args) {

        List<Question> questions = new ArrayList<>();

        questions.add(new Question(
                "What is the capital of India?",
                new String[]{"1. Mumbai", "2. Delhi", "3. Chennai", "4. Kolkata"},
                2));

        questions.add(new Question(
                "Which language is platform independent?",
                new String[]{"1. C", "2. C++", "3. Java", "4. Python"},
                3));

        questions.add(new Question(
                "Who invented Java?",
                new String[]{"1. Dennis Ritchie", "2. James Gosling", "3. Bjarne Stroustrup", "4. Guido van Rossum"},
                2));

        Scanner scanner = new Scanner(System.in);

        System.out.println("===== QUIZ APPLICATION =====");

        for (int i = 0; i < questions.size(); i++) {
            askQuestion(questions.get(i), scanner, i + 1);
        }

        System.out.println("\n===== RESULT =====");
        System.out.println("Total Questions: " + questions.size());
        System.out.println("Correct Answers: " + score);
        System.out.println("Final Score: " + score + "/" + questions.size());

        scanner.close();
    }

    private static void askQuestion(Question q, Scanner scanner, int questionNumber) {

        System.out.println("\nQuestion " + questionNumber + ": " + q.question);
        for (String option : q.options) {
            System.out.println(option);
        }
        System.out.println("You have " + TIME_LIMIT + " seconds to answer.");

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> scanner.nextInt());

        try {
            int answer = future.get(TIME_LIMIT, TimeUnit.SECONDS);
            if (answer == q.correctAnswer) {
                System.out.println("✅ Correct!");
                score++;
            } else {
                System.out.println("❌ Wrong!");
            }
        } catch (TimeoutException e) {
            System.out.println("\n⏰ Time's up! Moving to next question.");
            future.cancel(true);
        } catch (Exception e) {
            System.out.println("Invalid input!");
        } finally {
            executor.shutdownNow();
        }
    }
}