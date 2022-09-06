import java.io.*;
import java.nio.file.Path;
import java.nio.file.*;
import java.security.*;
import java.math.*;

public class Blob {
	String text = "";
	String sha1 = "";
	File textFile;
	public Blob(Path filePath) throws IOException, NoSuchAlgorithmException {
		text = Files.readString(filePath);
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset();
		digest.update(text.getBytes("utf8"));
		sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
	}
	public String getSha1() {
		return sha1;
	}
	public void createFile() throws FileNotFoundException {
		textFile = new File(sha1);
		new File("/path/object").mkdirs();
		File directoryFile = new File("C:\\Users\\Wyatt\\objects");
		PrintWriter pw = new PrintWriter(textFile);
		for (int i = 0; i < text.length(); i++) {
			pw.print(text.substring(i,i+1));
		}
	}
}
