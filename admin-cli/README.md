## [Documentation for Admin CLI](docs/apidocs/index.html)

# Running With Unit Tests (remember to insert password into uri)
mvn clean; DATABASE_URI="jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.setpdkyicbyderfipqpy&password=[YOUR-PASSWORD]" mvn package

and

DATABASE_URI="jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.setpdkyicbyderfipqpy&password=[YOUR-PASSWORD]" mvn exec:java

# Running Without Unit Tests (remember to insert password into uri)
mvn clean; mvn package -DskipTests

and 

DATABASE_URI="jdbc:postgresql://aws-0-us-east-1.pooler.supabase.com:5432/postgres?user=postgres.setpdkyicbyderfipqpy&password=[YOUR-PASSWORD]" mvn exec:java

# Running Javadoc
mvn javadoc:javadoc