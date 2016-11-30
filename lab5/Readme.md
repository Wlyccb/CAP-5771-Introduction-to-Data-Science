## MapReduce:  Extract Valid wikilinks and Generate Adjacency Graph

### What is MapReduce
MapReduce is a framework for processing parallelizable problems across huge datasets using a large number of computers.
* Map step: The master node takes the input, divides it into smaller subproblems, and distributes them to worker nodes. The worker node processes the smaller problem, and passes the answer back to its master node.
* Reduce step: The master node then collects the answers to all the subproblems and combines them in some way to form the output the answer to the problem it was originally trying to solve.

### Job for this Lab
+ Write a MapReduce job that extracts wikilinks and also remove all the red links. The dataset contains, for each article, a list of links going out of that article, as well as the articles title. The input for this job should be the Wikipedia dataset above, and the job should look at the XML text for each article, extract title and wikilinks.
+ Write another MapReduce job to generate the Adjacency graph.




