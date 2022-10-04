import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
	public String treePointer;
	private String parentPointer;
	private String otherPointer = null;
	private File commitFile;
	String sha1;
	
	public Commit(String s, String a) throws IOException, NoSuchAlgorithmException {
		summary = s;
		author = a;
		File HEAD = new File ("tester/HEAD");
		if(HEAD.exists()) {
			Scanner scan = new Scanner(new File("tester/HEAD"));
			parentPointer = scan.nextLine();
		}
		else {
			parentPointer=null;
		}
		System.out.println(parentPointer);
		date = getDate();
		treePointer = createTree().sha1;
		sha1 = generateSha1();
		commitFile = new File("tester/objects/" + sha1);
		writeFile();
		HEAD = new File("tester/HEAD");
		HEAD.delete();
		PrintWriter printer = new PrintWriter("tester/HEAD");
		printer.println(sha1);
		printer.close();
	}
	
	private Tree createTree() throws IOException, NoSuchAlgorithmException {
		Scanner scan = new Scanner(new File("tester/index"));
		ArrayList<String> content = new ArrayList<String>();
		while(scan.hasNext()) {
			String line = scan.nextLine();
			if(line.contains("*deleted*")) {
				Scanner scanny = new Scanner("tester/objects/"+parentPointer);
				String parentTreePointer = scanny.nextLine();
				traverse(parentTreePointer, line.substring(10));
			}
			else if (line.contains("*edited*")) {
				Blob newBlob = new Blob("tester/"+line.substring(9));
				content.add("blob : " + newBlob.getSha1() + line.substring(9));
				Scanner scanny = new Scanner("tester/objects/"+parentPointer);
				String parentTreePointer = scanny.nextLine();
				traverse(parentTreePointer, line.substring(9));			
			}
			else {
				content.add("blob : " + line.substring(line.indexOf(':')+1) + " " + line.substring(0, line.indexOf(':')-1));
			}
		}
		if(parentPointer!=null) {
			content.add(getParentTree());
		}
		File indexFile = new File ("tester/index");
		indexFile.delete();
		PrintWriter pw = new PrintWriter("tester/index");
		return new Tree(content);
	}
	
	private void traverse(String parentTree, String fileName) {
		Scanner scan = new Scanner("tester/objects/" + treePointer);
		while(scan.hasNext()) {
			String line = scan.next();
			if(line.contains(fileName)) {
				
			}
		}
	}

	private String getParentTree() throws FileNotFoundException {
		String content = "";
		Scanner scanny = new Scanner("tester/objects/"+parentPointer);
		String parentTreePointer = scanny.nextLine();
		content = "tree : " + parentTreePointer;
		return content;
	}
	
	public String generateSha1() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String str = "";
		str+="tester/objects/"+treePointer+"\n";
		if(parentPointer==null||parentPointer=="") {
			str+="\n";
		}
		else {
			str+="tester/objects/"+parentPointer+"\n";
		}
		if(otherPointer==null) {
			str+="\n";
		}
		else {
			str+=otherPointer+"\n";
		}
		str+=author+"\n";
		str+=date+"\n";
		str+=summary+"\n";
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
	
	public void writeFile() throws IOException, NoSuchAlgorithmException {
		PrintWriter printer = new PrintWriter(commitFile);
		String str = "";
		str+="tester/objects/"+treePointer+"\n";
		if(parentPointer==null||parentPointer=="") {
			str+="\n";
		}
		else {
			str+="tester/objects/"+parentPointer+"\n";
		}
		if(otherPointer==null) {
			str+="\n";
		}
		else {
			str+=otherPointer+"\n";
		}
		str+=author+"\n";
		str+=date+"\n";
		str+=summary+"\n";
		printer.print(str);
		printer.close();
	}
	
	public static void main (String [] args) throws NoSuchAlgorithmException, IOException {
		Index indy = new Index();
		indy.initialize();
		indy.addBlobs("something.txt");
		indy.addBlobs("f.txt");
		Commit comm = new Commit("first", "Kian");
		Index indy2 = new Index();
		indy2.addBlobs("fi.txt");
		indy2.addBlobs("fil.txt");
		Commit comm2 = new Commit("second", "Kian");
		Index indy3 = new Index();
		indy3.addBlobs("foo.txt");
		indy3.addBlobs("bar.txt");
		Commit comm3 = new Commit("third", "Kian");
		Index indy4 = new Index();
		indy4.addBlobs("foobar.txt");
		indy4.addBlobs("sometxt.txt");
		Commit comm4 = new Commit("fourth", "Kian");
		
	}
}
