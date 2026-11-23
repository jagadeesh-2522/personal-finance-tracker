# Personal Finance Tracker 💰

A Java-based desktop application for managing personal expenses, tracking transactions and analysing spending patterns. Built using Java Swing and JDBC with PostgreSQL database integration.

## Overview

Personal Finance Tracker is a desktop application developed as part of the Object-Oriented Programming with Java (OOPJ) project. It provides a graphical interface to manage expense transactions, view financial records and analyse spending through visual representations.

The application connects to a PostgreSQL database using JDBC, allowing transaction data to be stored and retrieved efficiently.

## Features

- **Add Transactions** – Record expenses with details such as title, amount, category and date.
- **View Transactions** – Display expense records in an organised table.
- **Edit Transactions** – Modify existing expense details.
- **Delete Transactions** – Remove unwanted transactions.
- **Expense Analysis** – Visualise spending patterns through charts.
- **Database Integration** – Store and retrieve expense records from PostgreSQL.
- **Input Validation** – Validate transaction details before processing.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core application development |
| Java Swing | GUI development |
| JDBC | Database connectivity |
| PostgreSQL | Database management |
| Supabase | Hosted PostgreSQL database |

## OOP Concepts

The project applies fundamental Object-Oriented Programming concepts:

- **Classes and Objects** – Structure the application into reusable components.
- **Encapsulation** – Organise data and related functionality within classes.
- **Abstraction** – Separate application functionality and database operations.
- **Event Handling** – Respond to user interactions through GUI components.

## Application Architecture

```text
User
  |
  v
Java Swing GUI
  |
  v
Application Logic
  |
  v
JDBC
  |
  v
PostgreSQL Database
(Supabase)
```

## Database

The application uses PostgreSQL to store expense records.

The database supports operations such as:

- Inserting transactions
- Retrieving transactions
- Updating transaction details
- Deleting transactions

## Getting Started

### Prerequisites

- Java Development Kit (JDK)
- PostgreSQL database
- JDBC driver
- Database connection configuration

## Project Objective

This project was developed to apply Java programming and Object-Oriented Programming concepts in a practical desktop application while integrating database connectivity for persistent transaction management.

## Future Enhancements

- Income management.
- Budget tracking and alerts.
- Monthly financial reports.
- CSV export functionality.
- Advanced financial analytics.

## Author

Developed as part of the Object-Oriented Programming with Java (OOPJ) coursework.
