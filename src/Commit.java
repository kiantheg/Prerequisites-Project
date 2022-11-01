import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
	private String childPointer;
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
		date = getDate();
		treePointer = createTree().sha1;
		sha1 = generateSha1();
		commitFile = new File("tester/objects/" + sha1);
		if (parentPointer!=null) {
			ArrayList <String> content = new ArrayList <String> ();
			BufferedReader br = new BufferedReader(new FileReader("tester/objects/"+parentPointer));
			for (int k=0; k<2; k++) {
				content.add(br.readLine());
			}
			content.add("tester/objects/" + sha1);
			br.readLine();
			for (int k=0; k<3; k++) {
				content.add(br.readLine());
			}
			PrintWriter pw = new PrintWriter("tester/objects/"+parentPointer);
			for (String line: content) {
				pw.println(line);
			}
			pw.close();
			br.close();
		}
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
		boolean deleted = false;
		while(scan.hasNext()) {
			String line = scan.nextLine();
			if(line.contains("*deleted*")) {
				String parentT = getParentTree();
				deleteFile(line.substring(10), parentT, content);
				deleted = true;
			}
			else {
				content.add("blob :" + line.substring(line.indexOf(':')+1) + " " + line.substring(0, line.indexOf(':')-1));
			}
		}
		if(!deleted && parentPointer!=null) {
			content.add(getParentTree());
		}
		File indexFile = new File ("tester/index");
		indexFile.delete();
		PrintWriter pw = new PrintWriter("tester/index");
		return new Tree(content);
	}
	
	private boolean deleteFile(String fileName, String tree, ArrayList<String> content) throws FileNotFoundException {
		File treeFile = new File("tester/objects/"+tree.substring(7));
		Scanner scan = new Scanner(treeFile);
		while(scan.hasNext()) {
			String line = scan.nextLine();
			if(line.contains("blob") && !line.contains(fileName)) {
				content.add(line);
			}
			if(line.contains(fileName)) {
				addRest(tree, content);
				content.remove(line);
				return true;
			}
			if (line.contains("tree")) {
				deleteFile(fileName, line, content);
			}
		}
		scan.close();
		return false;
	}
	
	private void addRest(String tree, ArrayList<String> content) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("tester/objects/" + tree.substring(7)));
		while(scan.hasNext()) {
			String line = scan.nextLine();
			content.add(line);
		}
	}

	private String getParentTree() throws IOException {
		String content = "";
		BufferedReader br = new BufferedReader(new FileReader("tester/objects/"+parentPointer));
		String parentTreePointer = br.readLine();
		content = "tree : " + parentTreePointer.substring(15);
		return content;
	}
	
	public String generateSha1() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String str = "";
		str+=summary+"\n";
		str+=date+"\n";
		str+=author+"\n";
		if(parentPointer==null||parentPointer=="") {
			str+="\n";
		}
		else {
			str+="tester/objects/"+parentPointer+"\n";
		}
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
		if(childPointer==null) {
			str+="\n";
		}
		else {
			str+=childPointer+"\n";
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
