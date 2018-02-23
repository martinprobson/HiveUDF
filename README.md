# An Example HiveUDF (Scala)

## Summary 
 This is an exmaple Apache Hive UDF (User Defined Function) written in Scala. It is a function that attempts to match names using a set of (simplistic) transform and match functions.

## Usage
 To use the UDF in Hive session: -

```
hive> add jar HiveUDF-assembly-0.1-SNAPSHOT.jar;
hive> create temporary function match as 'net.martinprobson.hiveudf.Matcher';
hive> select match("JOE SMITH","Joe Smith") ;
OK
EXACT
Time taken: 2.645 seconds, Fetched: 1 row(s)
hive> select match("J SMITH","Joe Smith") ;
OK
SURNAME_FIRST_INITIAL
Time taken: 0.196 seconds, Fetched: 1 row(s)
hive> select match("M SMITH","Joe Smith") ;
OK
SURNAME
Time taken: 0.132 seconds, Fetched: 1 row(s)
hive> select match("M Jones","Joe Smith") ;
OK
NONE
Time taken: 0.161 seconds, Fetched: 1 row(s)
```

or in a spark sesssion

```
spark-shell --master local[*] --jars HiveUDF-assembly-0.1-SNAPSHOT.jar
scala> sql("create temporary function match as 'net.martinprobson.hiveudf.Matcher'")
res1: org.apache.spark.sql.DataFrame = []

scala> sql("""select match("JOE SMITH","Joe Smith") """).show
+---------------------------+
|match(JOE SMITH, Joe Smith)|
+---------------------------+
|                      EXACT|
+---------------------------+


scala> sql("""select match("M SMITH","Joe Smith") """).show
+-------------------------+
|match(M SMITH, Joe Smith)|
+-------------------------+
|                  SURNAME|
+-------------------------+
```


## Details
The matcher implements a Hive UDF [org.apache.hadoop.hive.ql.exec.UDF](https://hive.apache.org/javadocs/r1.2.2/api/org/apache/hadoop/hive/ql/exec/UDF.html).
The match function is passed two strings on which the match is to be run. The UDF will call a series of functions to transform and attempt to match the names strings.
An enumeration is returned that contains the result of the match: -

- EXACT Exact match on names.
- SURNAME_FIRST_INITIAL  Surname and first initial match.
- SURNAME Surname match.
- COMPANY_NAME Company Name
- EXECUTOR Executor account
- JOINT_ACCOUNT
- NONE No match found.

Execute the match process by calling each match function in sequence.
The idea is that the match will continue to call the chained match functions
(passing the results along the chain) until a MatchResult is obtained.
ReturnType from each match function is an Either of MatchResult or a tuple2 of the (possibly) transformed
strings from the function that will be passed to the next.
We are folding from the left on a Either[MatchResult,(String,String)] object,
e.g.
```
Right("Fred Smith "," Fred Smith") --> 
               Trim ->  Right("Fred Smith","Fred Smith") ->
              Upper -> Right("FRED SMITH","FRED SMITH") ->
              Exact -> Left(MatchResult.EXACT)
```

## Build Instructions

sbt is used as the build tool with the following goals: -

```bash
sbt clean compile test package install
```

