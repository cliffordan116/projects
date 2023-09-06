In this project I created a program that compresses and decompresses text/image files using the Huffman algorithm. The encode method first creates a frequency file and counts all characters. Then, the program creates nodes storing characters and their frequencies and creates a huffman tree where leaf nodes store characters and internal nodes store the frequency of the sum of its two children. Using the tree, the method creates binary representations for each character and creates an encoded file where the data is shortened. My decode method recreates the huffman tree using the frequency file, and for each bit of the decoded file, traverses the tree until reaching a character. Finally, the decode method prints the characters until all binary bits are read and outputs to the output file.


Compile and Run Steps:
javac *.java
java HuffmanSubmit.java

To compress a file, enter the name of the file into the arguments. For example, "java HuffmanSubmit.java README.txt"

Citations:
https://www.geeksforgeeks.org/how-to-sort-an-arraylist-of-objects-by-property-in-java/