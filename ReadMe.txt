How to run the agent.

open the command prompt/terminal, and navigate to the MyCodeCoverage folder
run: mvn clean install This will create the agent.jar in the target folder.
3.you have to run the example project in 4 different cases:

case 1: Creating the total and additional prioritization test cases

case 2: Executing the project with the default test cases

case 3: Executing the project with total prioritization test cases

case 4: Executing the project with aditional prioritization test cases

Case 1:
In the pom.xml of the commons/dbutils, paste the following in the plugin:
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
     <configuration>
     	<argLine>-javaagent:{absolute path of the location of the agent in your system}\agent.jar</argLine> 
		     <additionalClasspathElements>
         <additionalClasspathElement>${project.build.sourceDirectory}</additionalClasspathElement>
         <additionalClasspathElement>${project.build.testSourceDirectory}</additionalClasspathElement>
      </additionalClasspathElements>
       <properties>
		     	<property>
		     		<name>listener</name>
		     		<value>edu.utdallas.JUnitExecListener</value>
		     	</property>
		     </properties>  
    </configuration>
  </plugin>

  Try executing the project using mvn test. If the project runs, go to case 2. 
  If you incur a classDefNotFound error for org/asm/... add the following to the dependencies:

  <dependency>
  <groupId>asm</groupId>
  <artifactId>asm</artifactId>
  <version>5.2</version>
  <scope>system</scope>
  <systemPath>{location of the asm jar on your system}/asm-5.2.jar</systemPath>
  </dependency>

  Execute with mvn test
  
  
  Case 2: After execution, you will see a message that totalTestSuite.java and AdditionalTestSuite.java has been created, which you can check by going into the src/test/java folder.
  add the following to the maven-surefire-plugin confiuration:

     <excludes>
            <exclude>totalTestSuite.java</exclude>
            <exclude>AdditionalTestSuite.java</exclude>
          </excludes>

      
       This will make the project execute with no priorities.
       execute the project with mvn test after introducing a bug anywhere in the source code. If the coverage tool finds the bug, it             writes the time taken to find the first bug in failure.txt found in the root folder of the example project.
       
   Case 3: Delete 
   <excludes>
            <exclude>totalTestSuite.java</exclude>
            <exclude>AdditionalTestSuite.java</exclude>
          </excludes>

          and replace with 
          <includes>
            <include>totalTestSuite.java</include>
          </includes>
          <excludes>
            <exclude>AdditionalTestSuite.java</exclude>
          </excludes>
          
     and run mvn test. This will run the test cases in order of total prioritization. The failure time will be found in Failure.txt
  Case 4: Delete 
     <includes>
            <include>totalTestSuite.java</include>
          </includes>
          <excludes>
            <exclude>AdditionalTestSuite.java</exclude>
          </excludes>
      
      and replace with
      <includes>
            <include>AdditionalTestSuite.java</include>
          </includes>
          <excludes>
            <exclude>totalTestSuite.java</exclude>
          </excludes>
      
 And run mvn test. This will run the test cases according to the additional priority. The failure will be found in Failure.txt
