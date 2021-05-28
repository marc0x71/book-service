# book-service

Basic REST service using http4s

## Initialise repository

```sh
./init_book_store.sh
```

## Operations

#### List of books

```sh
http http://localhost:8080/books
```

#### Add a new book

```sh
http POST http://localhost:8080/book title="Odissea" author="Omero"
```

#### Remove a book

```sh
http DELETE http://localhost:8080/book/Odissea
```

#### Get book details

```sh
http http://localhost:8080/book/Odissea
```

## Performance test

```sh
wrk -t5 -c100 -d 60s --latency http://localhost:8080/books
```

#### Classic JVM

```
Running 1m test @ http://localhost:8080/books
  5 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     5.19ms    5.00ms 131.10ms   94.19%
    Req/Sec     4.45k     1.18k    5.74k    84.64%
  Latency Distribution
     50%    4.13ms
     75%    5.27ms
     90%    7.32ms
     99%   29.18ms
  1329209 requests in 1.00m, 6.91GB read
  Socket errors: connect 0, read 28, write 0, timeout 0
Requests/sec:  22143.69
Transfer/sec:    117.84MB
```

#### GraalVM CE 21.1.0

```
Running 1m test @ http://localhost:8080/books
  5 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     4.39ms    3.77ms 139.34ms   94.35%
    Req/Sec     5.01k     1.15k   10.04k    85.19%
  Latency Distribution
     50%    3.75ms
     75%    4.71ms
     90%    6.20ms
     99%   17.75ms
  1494952 requests in 1.00m, 7.77GB read
  Socket errors: connect 0, read 35, write 1, timeout 0
Requests/sec:  24879.70
Transfer/sec:    132.40MB
```

#### Native image

```
Running 1m test @ http://localhost:8080/books
  5 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     8.21ms    3.02ms  72.97ms   80.45%
    Req/Sec     2.46k   195.48     4.44k    73.79%
  Latency Distribution
     50%    7.60ms
     75%    9.03ms
     90%   11.57ms
     99%   18.51ms
  736295 requests in 1.00m, 3.83GB read
  Socket errors: connect 0, read 26, write 0, timeout 0
Requests/sec:  12259.17
Transfer/sec:     65.24MB
```

removed options:

* --static
* --initialize-at-build-time

## Assembly

```sh
sbt assembly
cd target/scala-2.13/
java -jar ./book-service-assembly-0.0.1-SNAPSHOT.jar
```

#### Native image

```
export JAVA_HOME=$HOME/apps/graalvm-ce-java11-21.1.0/Contents/Home
sbt clean assembly
$JAVA_HOME/bin/native-image --static -H:+AddAllCharsets -H:+ReportExceptionStackTraces -H:UseLibC="$HOME/apps/musl/bundle" --allow-incomplete-classpath --no-fallback --initialize-at-build-time --enable-http --enable-https --enable-all-security-services --verbose -jar ./target/scala-2.13/book-service-assembly-0.0.1-SNAPSHOT.jar book-service-binary-image

$JAVA_HOME/bin/native-image -H:+AddAllCharsets -H:+ReportExceptionStackTraces -H:UseLibC="$HOME/apps/musl/bundle" --allow-incomplete-classpath --no-fallback --enable-http --enable-https --enable-all-security-services --verbose -jar ./target/scala-2.13/book-service-assembly-0.0.1-SNAPSHOT.jar book-service-binary-image
```
