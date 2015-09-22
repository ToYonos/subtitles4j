# subtitles4j

### What is it ?
Subtitles4j is a simple library which allows you to parse and manipulate subtitles files. 

### Which formats are supported ?
For the moment, only SubRip (srt) and Advanced SubStation Alpha (ass).

### How to use it ?

#### Parsing and writting files
You can easily parse, write and convert files thanks to `Subtitles4jFactory`.
```java
// Getting the factory
Subtitles4jFactory factory = Subtitles4jFactory.getInstance();
```

##### Parsing
```java
// From a File
File inputFile = new File("/an/input/file.srt");
// The input type is deducted from the extension
SubtitlesContainer container = factory.fromFile(inputFile);

// From an InputStream
InputStream is = new FileInputStream(inputFile);
// The input type must be provided
container = factory.fromStream(is, SubtitlesType.SRT);
```

##### Writing
```java
// To a File
File outputFile = new File("/an/output/file.ass");
factory.toASS(container, outputFile);

// To an OutputStream
FileOutputStream os = new FileOutputStream(outputFile);
factory.toASS(container, os);
```

##### Direct conversion
```java
// File to File
factory.toASS(inputFile, outputFile);
		
// InputStream to OutputStream
factory.toASS(is, SubtitlesType.SRT, os);
```

#### Content manipulation
The utility class `Subtitles4jUtils` allows you to perform operation on subtitles files.

##### Time shifting
```java
// From a File, add 250 ms to every timestamp
Subtitles4jUtils.shift(inputFile, 250);
		
// From an InputStream, rempve 150 ms to every timestamp
Subtitles4jUtils.shift(is, SubtitlesType.SRT, os, -150);
```
