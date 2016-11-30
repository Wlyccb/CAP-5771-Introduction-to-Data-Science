## Lab7 PageRank

### PageRank
One of the biggest changes in our lives in the decade was the availability of efficient and accurate web search. Google is the first search engine which is able to defeat the spammers who had made search almost useless. The technology innovation behind Google is called PageRank. This project is to implement the PageRank to find the most important Wikipedia pages on the provided the adjacency graph extracted from Wikipedia dataset using AWS Elastic MapReduce(EMR).

### Definition of PageRank
￼￼PageRank is a function that assigns a real number to each page in the Web. The intent is that the higher the PageRank of a page, the more important it is. The equation is as follows:  
![alt](https://cloud.githubusercontent.com/assets/24231848/20762364/1982c916-b6f4-11e6-8cdf-4d6e3f360c2c.jpg)  
Where d = 0.85, p​1,​ p​2,​ ...,p​N a​ re the pages under consideration, M(pi​ )​ is the set of pages that link
to p​i,​ L(p​j)​ is the number of outbound links on page pj​ ,​ and N is the total number of pages.
