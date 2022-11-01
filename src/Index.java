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
		new File("tester/objects").mkdirs();
		indexFile = new File("/tester/index");
	}
	public void addBlobs(String fileName) throws NoSuchAlgorithmException, IOException {
		Blob newBlob = new Blob("./tester/" + fileName);
		pairs.put(fileName, newBlob.getSha1());
		printToFile();
	}
	
	public void removeBlobs(String fileName) throws IOException {
		File myObj = new File("tester/objects/" + pairs.get(fileName));
		pairs.remove(fileName);
		myObj.delete();
		indexFile.delete();
		indexFile = new File("/tester/index");
		printToFile();
	}
	
	public void delete(String fileName) throws FileNotFoundException {
		pairs.put(fileName, "*deleted*");
		printToFile();
	}
	
	private void printToFile() throws FileNotFoundException {
		PrintWriter printer = new PrintWriter("tester/index");
		for (String key: pairs.keySet()) {
			if(pairs.get(key).equals("*deleted*")) {
				printer.println (pairs.get(key) + " " + key);
			}
			else {
				printer.println (key + " : " + pairs.get(key));
			}
		}
		printer.close();
	}
	public static void main(String [] args) throws NoSuchAlgorithmException, IOException {
		Index i = new Index();
		i.initialize();
		i.addBlobs("something.txt");
	}
}
