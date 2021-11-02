
# Task

FilesFromYou is transforming the file sharing business by making it super easy to share files
between your friends and family. You share files by installing a small client on your home PC or mobile
device. In one of the latest releases, it has turned out that some clients have very high CPU usage,
making the FilesFromYou service unusable.

In order to find out what is going wrong we want you to design a service that handles CPU data
reported to our backend. The collected data should be processed and aggregated in an intuitive way
such that it helps troubleshooting which clients are having a problem with the CPU usage. Service is
responsible for generating the report on the data.


# Considerations
To help you with your design, please look at these considerations. It isn’t necessary to prepare
full answers for each of the questions but incorporating these points into your solution will help you to
cover some things we are interested in seeing in your implementation.

#### Client
1. How often does the client send data?
2. What happens if the backend is unavailable?
3. What happens if the client fails to send data because it consumed all the CPU?
#### Backend
1. What is "low" or "high" CPU consumption?
2. How does the API look like, which protocol does it use, and why?
3. Which information does the client pass to the service?
4. How does the service handle scaling, restarts, crashes etc.?
5. Which database or database family to use, if any, and what schema would it have?
6. Do you need to store everything the client sends?
7. How to query persisted data to generate the report?
8. What information in the report allows us to pinpoint the faulty clients?
   You are also very welcome to bring up more considerations on top of the list above.