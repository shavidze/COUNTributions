
# [Scalac Assignment](https://github.com/Ghurtchu/COUNTributions/blob/master/Scala-Task-Scalac.pdf)

## Local Setup

1. Clone the repository to your local machine.
   
2. Change into the repository directory.

3. To extend the GitHub API rate limit, you need to set an environmental variable using a GitHub personal access token. Here's how you can do it:
   
   - [x] Generate a GitHub personal access token from your GitHub account.
   - [x] Open a terminal and execute the following command:
  
     ```bash
     vim ~/.bash_profile
     ```
   - [x] Add the following line to the file:
   
     ```bash
     export GH_TOKEN=${your_github_token}
     ```
     Replace `${your_github_token}` with your actual GitHub personal access token.
   - [x] Save and exit the file.
   - [x] Execute the following command to apply the changes:
     ```bash
     source ~/.bash_profile
     ```

4. Execute the following commands in the terminal:
   
   - To start SBT (Scala Build Tool), run:
     ```
     sbt
     ```
   - To run the tests, execute:
     ```bash
     test
     ```
   - To run the application, use:
     ```bash
     run
     ```

6. Once the application is running, open your favorite client (e.g., a web browser or API testing tool) and make a request to the following endpoint:
   ```
   localhost:8080/org/{organizationName}/contributors
   ```
   Replace `{organizationName}` with the name of the GitHub organization for which you want to retrieve the contributors.


**Note:** The application might work relatively slowly for large organizations such as Apache and Microsoft due to the volume of data. Please be patient while the request is being processed.

## Benchmarks

Here are some benchmark results for the application:

- Organization: quantori
  - Contributors: 560
  - Average time: 13 seconds

- Organization: zio
  - Contributors: 749
  - Average time: 23 seconds

- Organization: pytorch
  - Contributors: 2390
  - Average time: 33 seconds

Feel free to adjust the application and test it with different organizations to observe the performance.

Please let me know if there's anything else you'd like to add or if you need further assistance!
