All of the code written for 6114-course-project is contained in this file.

## Overview of Implementation

The code is organized around the Similarity Ensemble Approach described by Michael J. Keiser and Jérôme Hert. Their paper described a method of grouping ligands with unique features together from a provided Chemical Similarity dataset. We provide a simulation of their implementation in java to better understand their approach and to provide a environment for runtime observations and correctness for the Similarity Ensemble Approach. 

We Setup the process by initially calculating all of the parameters from the Reference Database as described in section 3.1. This provides use with a optimal Time Step, y_micro equation and y_sigma equation to determine grouping. Next with calculate the Setwise Similarity Ensembles to produce a list of sets where the "E-Values" approach zero. This provides us with optimal sets to group with each other. Once we determine our Setwise Similarity Ensembles we build a network of their similarity, store it inside of the "Edges" folder in and build a network map utilizing CytoScape.

## Run Code

To test the code run the test.sh script which includes the parallelization environment configuration. To run the code you must know the path to your `java` directory. A breif example is shown below

```bash
$ ./test.sh /path/to/java/bin
```

To run the code start the `run.sh` script in your terminal

```bash
$ ./run.sh
```

To run the parallel code start the `run-parallel.sh` script in your terminal with a path to your `java` installation directory.

```bash
$ ./run-parallel.sh /path/to/java/bin
```

## Notice

All parallel code currently requires a path to a java1.8 installation. Otherwise the Parallel library will fail, a acceptable restriction. If you face issues contact cmicklis@uncc.edu to receive a complete zip of the library included with the `student-provided_java` code. 

Kind Regards
