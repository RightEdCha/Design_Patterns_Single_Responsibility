# Design_Patterns_Single_Responsibility
This project is to try and apply design patterns as I learn about them. I will take old projects done in the past and attempt to apply the design pattern to improve the project.

## Java Client / Server Project
Original Project intent was to create a Client and Server using a TCP connection.

The client requests for a command: ADD, SUBTRACT and MULTIPLY.
The server receives the command and sends back the apporopriate value. If there is an error, negative values will be sent back for the client to handle.

## Is My Project Violating the Single Responsibility Principle?
Since the definition of the single responsibility principle boils down to "will my system only need one reason to change?"
