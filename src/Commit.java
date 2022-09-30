import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Commit {
	private String summary;
	private String author;
	private String date;
	private Tree tree;
	private String parentPointer = null;
	private String otherPointer = null;
	String sha1;
	
	public Commit(String s, String a, String parent) throws IOException {
		summary = s;
		author = a;
		tree = createTree();
		parentPointer = parent;
	}
	
	private Tree createTree() throws IOException {
		Scanner scan = new Scanner("tester/index");
		ArrayList<String> content = new ArrayList<String>();
		while(scan.hasNext()) {
			String line = scan.nextLine();
			content.add("blob : " + line.substring(line.indexOf(':')+1) + " " + line.substring(0, line.indexOf(':')-1));
		}
		if(parentPointer!=null) {
			content.add(getParentTree());
		}
		File indexFile = new File ("tester/index");
		indexFile.delete();
		File newIndex = new File ("tester/index");
		return new Tree(content);
	}
	
	private String getParentTree() {
		String content;
		Scanner scan = new Scanner ("tester/"+parentPointer);
		String line = scan.nextLine();
		content = "tree : " + line;
		return content;
	}
	
	public String generateSha1() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String str = "";
		str += summary;
		str += author;
		str += date;
		str += parentPointer;
		MessageDigest digest = MessageDigest.getInstance("SHA-1");
		digest.reset(); 
		digest.update(str.getBytes("utf8"));
		sha1 = String.format("%040x", new BigInteger(1, digest.digest()));
		return sha1;
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
		writer.write("tester/" + tree.sha1 + "\n");
		writer.write(parentPointer + "\n");
		writer.write(otherPointer + "\n");
		writer.write(author + "\n");
		writer.write(date + "\n");
		writer.write(summary);
		writer.close();
	}
}
