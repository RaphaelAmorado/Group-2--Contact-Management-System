import java.sql.*;
import java.util.Scanner;

class Contact {
    private String name;
    private String email;
    private String phoneNumber;
    private String notes;

    public Contact(String name, String email, String phoneNumber, String notes) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }



    public String getEmail() {
        return email;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public String getNotes() {
        return notes;
    }


}

class ContactManager {
    private Connection connection;

    public ContactManager() {
        try {
            // Establish connection to MySQL database
            String url = "jdbc:mysql://localhost:3306/programming_languages";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addContact(Contact contact) {
        try {
            String query = "INSERT INTO customer (name, email, phone_number, notes) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, contact.getName());
            statement.setString(2, contact.getEmail());
            statement.setString(3, contact.getPhoneNumber());
            statement.setString(4, contact.getNotes());
            statement.executeUpdate();
            System.out.println("Contact added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void displayContacts() {
        try {
            String query = "SELECT * FROM customer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            boolean hasData = false; // Flag to track if any data is found
            while (resultSet.next()) {
                hasData = true; // Set the flag to true if any data is found
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Phone Number: " + resultSet.getString("phone_number"));
                System.out.println("Notes: " + resultSet.getString("notes"));
                System.out.println("--------------------------------------");
            }
            if (!hasData) {
                System.out.println("No contacts found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void updateContact(String name, Contact updatedContact) {
        try {
            // Check if the contact with the specified name exists
            boolean contactExists = checkContactExists(name);

            // If the contact doesn't exist, print an error message and return
            if (!contactExists) {
                System.out.println("Contact not found.");
                return;
            }

            // Update the contact
            String query = "UPDATE customer SET name=?, email=?, phone_number=?, notes=? WHERE name=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, updatedContact.getName());
            statement.setString(2, updatedContact.getEmail());
            statement.setString(3, updatedContact.getPhoneNumber());
            statement.setString(4, updatedContact.getNotes());
            statement.setString(5, name);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Contact updated successfully.");
            } else {
                System.out.println("Failed to update contact.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if the contact exists
    // Method to check if a contact exists by name (public)
    public boolean checkContactExists(String name) {
        try {
            String query = "SELECT * FROM customer WHERE name=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // If there's a result, the contact exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // In case of an exception, return false
        }
    }
    // Method to retrieve contact details by name


    public void deleteContact(String name) {
        try {
            String query = "DELETE FROM customer WHERE name=?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Contact deleted successfully.");
            } else {
                System.out.println("Contact not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

public class ContactManagementSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactManager contactManager = new ContactManager();
        System.out.println("Welcome to your Contact Management System!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Contact");
            System.out.println("2. Display Contacts");
            System.out.println("3. Update Contact");
            System.out.println("4. Delete Contact");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline


            switch (choice) {
                case 1:
                    String name = "";
                    boolean isValidName = false;
                    while (!isValidName) {
                        System.out.println("Enter name:");
                        name = scanner.nextLine().trim();

                        // Check if the input is not empty
                        if (!name.isEmpty()) {
                            // Check if the input does not contain any digits
                            if (!name.matches(".*\\d.*")) {
                                // Check if the name is unique
                                if (!contactManager.checkContactExists(name)) {
                                    isValidName = true;
                                } else {
                                    System.out.println("Contact with the same name already exists. Please enter a different name.");
                                }
                            } else {
                                System.out.println("Invalid name.");
                            }
                        } else {
                            System.out.println("Invalid name. Name cannot be blank.");
                        }
                    }
                    String email = "";
                    boolean isValidEmail = false;
                    while (!isValidEmail) {
                        System.out.println("Enter email:");
                        email = scanner.nextLine().trim();
                        if (email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                            // Check if the input matches the basic email format pattern
                            isValidEmail = true;
                        } else {
                            System.out.println("Invalid email. Please enter a valid email address.");
                        }
                    }

                    // Ask for phone number until a valid integer is provided
                    // Ask for phone number until a valid numeric input is provided
                    boolean isValidPhoneNumber = false;
                    String phoneNumber = "";
                    while (!isValidPhoneNumber) {
                        System.out.println("Enter phone number:");
                        phoneNumber = scanner.nextLine();
                        if (phoneNumber.matches("^09\\d{9}$")) { // Check if the input starts with "09" and consists of 11 digits in total
                            isValidPhoneNumber = true;
                        } else {
                            System.out.println("Invalid phone number.");
                        }
                    }


                    System.out.println("Enter notes:");
                    String notes = scanner.nextLine();
                    Contact newContact = new Contact(name, email, phoneNumber, notes);
                    contactManager.addContact(newContact);
                    break;

                case 2:
                    contactManager.displayContacts();
                    break;
                case 3:
                    String contactToUpdate = "";
                    boolean isValidContactName = false;
                    while (!isValidContactName) {
                        System.out.println("Enter the name of the contact to update:");
                        contactToUpdate = scanner.nextLine().trim();
                        if (!contactToUpdate.isEmpty()) {
                            isValidContactName = true;
                        } else {
                            System.out.println("Invalid contact name. Name cannot be blank.");
                        }
                    }
                     if (contactManager.checkContactExists(contactToUpdate)) {
                    String updatedName = "";
                    boolean isValidUpdatedName = false;
                    while (!isValidUpdatedName) {
                        System.out.println("Enter updated name:");
                        updatedName = scanner.nextLine().trim();

                        // Check if the input is not empty
                        if (!updatedName.isEmpty()) {
                            // Check if the input does not contain any digits
                            if (!updatedName.matches(".*\\d.*")) {
                                // Check if the name is unique
                                if (!contactManager.checkContactExists(updatedName)) {
                                    isValidUpdatedName = true;
                                } else {
                                    System.out.println("Contact with the same name already exists. Please enter a different name.");
                                }
                            } else {
                                System.out.println("Invalid name. Please enter a valid name without numbers.");
                            }
                        } else {
                            System.out.println("Invalid name. Name cannot be blank.");
                        }
                    }


                        String updatedEmail = "";
                        boolean isValidUpdatedEmail = false;
                        while (!isValidUpdatedEmail) {
                            System.out.println("Enter updated email:");
                            updatedEmail = scanner.nextLine().trim();
                            if (updatedEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                                isValidUpdatedEmail = true;

                            } else {
                                System.out.println("Invalid email. Please enter a valid email address.");
                            }
                        }

                        String updatedPhoneNumber = "";
                        boolean isValidUpdatedPhoneNumber = false;
                        while (!isValidUpdatedPhoneNumber) {
                            System.out.println("Enter updated phone number:");
                            updatedPhoneNumber = scanner.nextLine();
                            if (updatedPhoneNumber.matches("^09\\d{9}$")) { // Check if the input consists only of digits
                                isValidUpdatedPhoneNumber = true;
                            } else {
                                System.out.println("Invalid phone number.");
                            }
                        }

                        System.out.println("Enter updated notes:");
                        String updatedNotes = scanner.nextLine();

                        Contact updatedContact = new Contact(updatedName, updatedEmail, updatedPhoneNumber, updatedNotes);
                        contactManager.updateContact(contactToUpdate, updatedContact);
                    } else {
                        System.out.println("Contact not found.");
                    }
                    break;


                case 4:
                    System.out.println("Enter the name of the contact to delete:");
                    String contactToDelete = scanner.nextLine();
                    contactManager.deleteContact(contactToDelete);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
