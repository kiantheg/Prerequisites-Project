import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Commit {
	private String summary;
	private String author;
	private String date;
	private String pTree;
	private String parentPointer = null;
	private String otherPointer = null;
	String sha1;
	
	public Commit(String s, String a, String p) {
		summary = s;
		author = a;
		pTree = p;
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
	public String getDate(String d) {
		date = d;
		return date;
	}
	public void writeFile() {
		File file = new File("./objects" + sha1);
	}
}
