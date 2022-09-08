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
		indexFile = new File("/tester/index.txt");
	}
	public void addBlobs(String fileName) throws NoSuchAlgorithmException, IOException {
		Blob newBlob = new Blob("./tester/" + fileName);
		pairs.put(fileName, newBlob.getSha1());
		FileWriter fileWriter = new FileWriter("index.txt");
	    fileWriter.write(fileName + " : " + newBlob.getSha1());
	    fileWriter.close();
	}
	
	public void removeBlobs(String fileName) throws IOException {
		pairs.remove(fileName);
		indexFile.delete();
		indexFile = new File("/tester/index.txt");
		FileWriter fileWriter = new FileWriter("index.txt");
		for (String name : pairs.keySet()) {
		    fileWriter.write(name + " : " + pairs.get(name));
		    fileWriter.close();
		}
	}
	
	public static void main(String [] args) throws NoSuchAlgorithmException, IOException {
		Index i = new Index();
		i.initialize();
		i.addBlobs("something.txt");
		i.removeBlobs("something.txt");
	}
}
