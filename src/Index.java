import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
		FileWriter fileWriter = new FileWriter("tester/index");
	    fileWriter.write(fileName + " : " + newBlob.getSha1());
	    fileWriter.close();
	}
	
	public void removeBlobs(String fileName) throws IOException {
		File myObj = new File("tester/objects/" + pairs.get(fileName));
		pairs.remove(fileName);
		myObj.delete();
		indexFile.delete();
		indexFile = new File("/tester/index");
		FileWriter fileWriter = new FileWriter("tester/index");
		for (String name : pairs.keySet()) {
		    fileWriter.write(name + " : " + pairs.get(name));
		}
	    fileWriter.close();
	}
	
	public static void main(String [] args) throws NoSuchAlgorithmException, IOException {
		Index i = new Index();
		i.initialize();
		i.addBlobs("something.txt");
	}
}
