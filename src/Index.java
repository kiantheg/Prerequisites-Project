import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
public class Index {
	HashMap<String, String> pairs;
	File indexFile;
	public Index() {
		pairs = new HashMap<String, String>();
	}
	public void initialize() {
		new File("/tester/objects").mkdirs();
		indexFile = new File("/tester/index");
	}
	public void addBlobs(String fileName) throws NoSuchAlgorithmException, IOException {
		Blob newBlob = new Blob("./tester/" + fileName);
		pairs.put(fileName, newBlob.getSha1());
		PrintWriter fileWriter = new PrintWriter("tester/index");
		for (String key: pairs.keySet()) {
			fileWriter.println (key + " : " + pairs.get(key));
		}
		fileWriter.close();
	}
	
	public void removeBlobs(String fileName) throws IOException {
		File myObj = new File("tester/objects/" + pairs.get(fileName));
		pairs.remove(fileName);
		myObj.delete();
		indexFile.delete();
		indexFile = new File("/tester/index");
		PrintWriter fileWriter = new PrintWriter("tester/index");
		for (String name : pairs.keySet()) {
		    fileWriter.println(name + " : " + pairs.get(name));
		}
	    fileWriter.close();
	}
	
	public void delete(String fileName) throws FileNotFoundException {
		PrintWriter printer = new PrintWriter(indexFile);
		printer.println("*deleted* " + fileName);
	}
	
	public void edit(String fileName) throws FileNotFoundException {
		PrintWriter printer = new PrintWriter(indexFile);
		printer.println("*edited* " + fileName);
	}
	
	public static void main(String [] args) throws NoSuchAlgorithmException, IOException {
		Index i = new Index();
		i.initialize();
		i.addBlobs("something.txt");
	}
}
