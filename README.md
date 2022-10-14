Open scalac_assignment/Scala-Task-Scalac.pdf to see the task description

**Local setup**:

**1) Clone the repository**
 
**2) cd into repository**

**3) Set environmental variable for extending Github's API rate limit**
    
   - **generate github personal access token**

   - **execute**:
      
      - **vim ~/.bash_profile**
     
      - **export GH_TOKEN=${your_github_token} (save and exit)**
     
      - **source ~/.bash_profile**

**4) Execute the following commands:**

- **sbt** 
- **test** 
- **run**

**6) Pick your favorite client and hit the following endpoint: localhost:8080/org/{organizationName}/contributors**
 
**7) Enjoy!**



P.S The app works relatively slowly on big organizations such as apache, microsoft etc..

**Benchmarks**:

    organization=quantori, contributors=560, avg time=13 seconds
    organization=zio, contributors=749, avg time=23 seconds
    organization=pytorch, contributors=2390, avg time=33 seconds
