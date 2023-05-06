# BrainDumpstr Backend

This repository contains the backend for BrainDumpstr, an app designed to help users capture, organize, and retrieve their thoughts and ideas using voice recordings and text notes. The backend handles API requests, manages data storage, and performs any necessary server-side processing, such as transcribing audio to text and searching for relevant content.

## Getting Started

These instructions will help you set up the BrainDumpstr backend server on your local machine for development and testing purposes.

### Prerequisites

Ensure that you have the following software installed on your machine:

- Java (JDK 8 or higher)
- Maven (3.6.0 or higher)
- MySQL (5.7 or higher)

### Installing

1. Clone the repository:

\`\`\`
git clone https://github.com/yourusername/braindumpstr-backend.git
\`\`\`

2. Change into the project directory:

\`\`\`
cd braindumpstr-backend
\`\`\`

3. Install the required dependencies:

\`\`\`
mvn clean install
\`\`\`

### Configuration

1. Create a MySQL database and user for the application:

\`\`\`sql
CREATE DATABASE braindumpstrdb;
CREATE USER 'braindumpstruser'@'localhost' IDENTIFIED BY 'mypassword';
GRANT ALL PRIVILEGES ON braindumpstrdb.* TO 'braindumpstruser'@'localhost';
FLUSH PRIVILEGES;
\`\`\`

Replace `braindumpstrdb`, `braindumpstruser`, and `mypassword` with your desired database name, user, and password, respectively.

2. Update the `application.properties` file located in `src/main/resources` with your database connection details:

\`\`\`
spring.datasource.url=jdbc:mysql://localhost:3306/braindumpstrdb
spring.datasource.username=braindumpstruser
spring.datasource.password=mypassword
\`\`\`

## Running the Application

1. To start the backend server, run the following command:

\`\`\`
mvn spring-boot:run
\`\`\`

The backend server will be accessible at `http://localhost:8080`.

## API Endpoints

The BrainDumpstr backend exposes several API endpoints for managing voice recordings, text notes, and associated metadata. For a complete list of endpoints, consult the API documentation.

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.
