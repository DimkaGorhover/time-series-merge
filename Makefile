project_dir=$(shell pwd)
build_dir=$(project_dir)/target
testcases_dir=$(project_dir)/testcases

clean:
	lein clean

test:
	lein test

build:
	lein uberjar

run: clean test build
	java -jar target/time-series-merge-0.1.0-standalone.jar \
		-o $(build_dir)/merge_c.txt \
		$(testcases_dir)/file1.txt \
		$(testcases_dir)/file2.txt \
		$(testcases_dir)/file3.txt \
		$(testcases_dir)/file4.txt \
		$(testcases_dir)/file5.txt
