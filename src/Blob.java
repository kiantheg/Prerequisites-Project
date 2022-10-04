import java.io.*;
import java.nio.file.Path;
import java.nio.file.*;
import java.security.*;
import java.math.*;

public class Blob {
	String text = "";
	String sha1 = "";
	public Blob(String filePath) throws IOException, NoSuchAlgorithmException {
		Path fPath = Paths.get(filePath);
		text = Files.readString(fPath);
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset(); 
		digest.update(text.getBytes("utf8"));
		sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		createFile();
	}
	public String getSha1() {
		return sha1;
	}
	public void createFile() throws FileNotFoundException {
		PrintWriter printWriter = new PrintWriter("tester/objects/" + sha1);
		for (int i = 0; i < text.length(); i++) {
			printWriter.print(text.substring(i,i+1));
		}
		printWriter.close();
	}
	public static void main(String [] args) throws NoSuchAlgorithmException, IOException {
		Blob b = new Blob("./tester/something.txt");
		b.getSha1();
	}
}
