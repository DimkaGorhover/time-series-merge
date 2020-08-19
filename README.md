# Time Series Merge

## Problem:

Time series are stored in files with the following format:

* files are multiline plain text files in ASCII encoding
* each line contains exactly one record
* each record contains date and integer value; records are encoded like so: `YYYY-MM-DD:X`
* dates within single file are non-duplicate and sorted in ascending order
* files can be bigger than RAM available on target host

Implement an algorithm accepting names of files as arguments, which merges two input files into one output file. Result file
should follow the same format conventions as described above. Records with the same date value should be merged into
one by summing up `X` values.

### Optional:

* solution implemented in Clojure
* arbitrary number of input files
* duplicate dates within single file, sorted in ascending order

## How to Test

`Makefile` contains one test case, that merges files from `testcases` 
directory to `target/merge_c.txt`

```sh
# files:
#  * testcases/file1.txt
#  * testcases/file2.txt
#  * testcases/file3.txt
#  * testcases/file4.txt
#  * testcases/file5.txt
#
# will be merged to:
#  * target/merge_c.txt

make run
```

## How to Build

```sh
lein clean && lein uberjar
```

or

```sh
make clean build
```

## How to Run

```sh
lein clean
lein uberjar

java -jar target/time-series-merge-0.1.0-standalone.jar \
    -o {output_file} \
    {input_file_1} \
    {input_file_2} \
    {input_file_3} \
    ...
```
