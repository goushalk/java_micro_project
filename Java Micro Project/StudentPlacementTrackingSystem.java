import java.sql.*;
import java.util.Scanner;

public class StudentPlacementTrackingSystem{
    private static final String URL = "jdbc:mysql://localhost:3306/javadata";
    private static final String USER = "root";
    private static final String PASSWORD = "Greentree#10";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Placement System Initialized");

            do {
                System.out.println("\n1. Add Student Profile");
                System.out.println("2. Add Job Listing");
                System.out.println("3. Apply for Job");
                System.out.println("4. Employer Interaction");
                System.out.println("5. View Placement Status Dashboard");
                System.out.println("6. Reporting and Analytics");
                System.out.println("7. Notification and Alerts");
                System.out.println("8. Add Counseling Notes");
                System.out.println("9. Employer Database Compliance Check");
                System.out.println("10. Document Management");
                System.out.println("0. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1 -> addStudent(conn, scanner);
                    case 2 -> addJob(conn, scanner);
                    case 3 -> applyForJob(conn, scanner);
                    case 4 -> employerInteraction(conn, scanner);
                    case 5 -> placementDashboard(conn);
                    case 6 -> reportingAnalytics(conn);
                    case 7 -> notificationAlerts();
                    case 8 -> addCounselingNotes(conn, scanner);
                    case 9 -> complianceCheck(conn);
                    case 10 -> documentManagement(conn, scanner);
                    case 0 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        scanner.close();
    }

    private static void addStudent(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Student Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Skills: ");
        String skills = scanner.nextLine();
        System.out.print("Enter Resume Link: ");
        String resumeLink = scanner.nextLine();

        String sql = "INSERT INTO students (name, email, skills, resume_link) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, skills);
            stmt.setString(4, resumeLink);
            stmt.executeUpdate();
            System.out.println("Student profile added successfully.");
        }
    }

    private static void addJob(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();
        System.out.print("Enter Company: ");
        String company = scanner.nextLine();
        System.out.print("Enter Location: ");
        String location = scanner.nextLine();
        System.out.print("Enter Salary: ");
        double salary = scanner.nextDouble();

        String sql = "INSERT INTO jobs (title, description, company, location, salary) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, company);
            stmt.setString(4, location);
            stmt.setDouble(5, salary);
            stmt.executeUpdate();
            System.out.println("Job listing added successfully.");
        }
    }

    private static void applyForJob(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        System.out.print("Enter Job ID: ");
        int jobId = scanner.nextInt();

        String sql = "INSERT INTO applications (student_id, job_id, status) VALUES (?, ?, 'Applied')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, jobId);
            stmt.executeUpdate();
            System.out.println("Application submitted.");
        }
    }

    private static void employerInteraction(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Employer Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Industry: ");
        String industry = scanner.nextLine();
        System.out.print("Enter Contact Info: ");
        String contactInfo = scanner.nextLine();

        String sql = "INSERT INTO employers (name, industry, contact_info) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, industry);
            stmt.setString(3, contactInfo);
            stmt.executeUpdate();
            System.out.println("Employer details recorded.");
        }
    }

    private static void placementDashboard(Connection conn) throws SQLException {
        String sql = "SELECT s.name, j.title, a.status " +
                     "FROM applications a " +
                     "JOIN students s ON a.student_id = s.student_id " +
                     "JOIN jobs j ON a.job_id = j.job_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Placement Status Dashboard:");
            while (rs.next()) {
                System.out.println("Student: " + rs.getString("name") +
                                   ", Job: " + rs.getString("title") +
                                   ", Status: " + rs.getString("status"));
            }
        }
    }

    private static void reportingAnalytics(Connection conn) throws SQLException {
        String sql = "SELECT s.name, COUNT(a.application_id) AS applications, " +
                     "SUM(CASE WHEN a.status = 'Accepted' THEN 1 ELSE 0 END) AS placements " +
                     "FROM students s " +
                     "LEFT JOIN applications a ON s.student_id = a.student_id " +
                     "GROUP BY s.student_id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Placement Report:");
            while (rs.next()) {
                System.out.println("Student: " + rs.getString("name") +
                                   ", Applications: " + rs.getInt("applications") +
                                   ", Placements: " + rs.getInt("placements"));
            }
        }
    }

    private static void notificationAlerts() {
        System.out.println("Sending alerts to students for upcoming deadlines.");
    }

    private static void addCounselingNotes(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Counseling Notes: ");
        String notes = scanner.nextLine();

        String sql = "UPDATE students SET counseling_notes = ? WHERE student_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, notes);
            stmt.setInt(2, studentId);
            stmt.executeUpdate();
            System.out.println("Counseling notes updated.");
        }
    }

    private static void complianceCheck(Connection conn) throws SQLException {
        String sql = "SELECT name, database_compliance FROM employers WHERE database_compliance = FALSE";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Non-compliant Employers:");
            while (rs.next()) {
                System.out.println("Employer: " + rs.getString("name"));
            }
        }
    }

    private static void documentManagement(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID: ");
        int studentId = scanner.nextInt();
        scanner.nextLine(); // consume newline
        System.out.print("Enter Document Type: ");
        String docType = scanner.nextLine();
        System.out.print("Enter Document Link: ");
        String docLink = scanner.nextLine();

        String sql = "INSERT INTO documents (student_id, document_type, document_link) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setString(2, docType);
            stmt.setString(3, docLink);
            stmt.executeUpdate();
            System.out.println("Document added to student profile.");
        }
    }
}
