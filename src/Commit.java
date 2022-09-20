import java.nio.file.Path;

public class Commit {
	private String summary;
	private String author;
	private String date;
	private String pTree;
	
	public Commit(String s, String a, String p) {
		summary = s;
		author = a;
		pTree = p;
	}
	public String generateSha1() {
		String str = "";
	}
}
