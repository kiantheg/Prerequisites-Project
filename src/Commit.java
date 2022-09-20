import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Commit {
	private String summary;
	private String author;
	private String date;
	private String pTree;
	private String parentPointer = null;
	private String otherPointer = null;
	String sha1;
	
	public Commit(String s, String a, String p, String parent) {
		summary = s;
		author = a;
		pTree = p;
		parentPointer = parent;
	}
	public void generateSha1() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String str = "";
		str += summary;
		str += author;
		str += date;
		str += parentPointer;
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset(); 
		digest.update(str.getBytes("utf8"));
		sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
	}
	public String getDate() {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date2 = new Date(System.currentTimeMillis());
		date = formatter.format(date2);
		return date;
	}
	public void writeFile() throws IOException {
		File file = new File("./objects" + sha1);
		FileWriter writer = new FileWriter(file);
		writer.write(pTree + "\n");
		writer.write(parentPointer + "\n");
		writer.write(otherPointer + "\n");
		writer.write(author + "\n");
		writer.write(date + "\n");
		writer.write(summary);
		writer.close();
	}
}
