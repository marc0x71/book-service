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
wrk -t5 -d 60s --latency http://localhost:8080/books
```

## Assembly

```sh
sbt assembly
cd target/scala-2.13/
java -jar ./book-service-assembly-0.0.1-SNAPSHOT.jar
```
