import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BlobTester {
	
	private static Index index;
	
	private static File f, fi, fil, file;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		File f = new File("f.txt");
		FileWriter fw = new FileWriter(f);
		fw.write("content");
		fw.close();
		
		File fi = new File("fi.txt");
		FileWriter fwr = new FileWriter(fi);
		fwr.write("some content");
		fwr.close();
		
		File fil = new File("fil.txt");
		FileWriter fwri = new FileWriter(fil);
		fwri.write("othercontent");
		fwri.close();
		
		file = new File("file.txt");
		index = new Index();
		index.initialize();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		file.delete();
		f.delete();
		fi.delete();
		fil.delete();
	}

	@Test
	void testBlob() throws NoSuchAlgorithmException, IOException {
		Blob blob = new Blob("file.txt");
		String Sha = blob.getSha1();
		assertTrue(new File("tester/objects/" + Sha).exists());
	}
	
	@Test
	void testInit() {
		assertTrue(new File("tester/objects").exists() && new File("tester/index.txt").exists());
	}
	
	@Test
	void testIndex() throws Exception {
		index.addBlobs("f.txt");
		Blob b = new Blob("f.text");
		String s = b.getSha1();
		index.addBlobs("fi.txt");
		Blob bl = new Blob("fi.text");
		String sh = bl.getSha1();
		index.addBlobs("fil.txt");
		Blob blo = new Blob("fil.text");
		String sha = blo.getSha1();
		
		File folder = new File("tester/objects");
		ArrayList<String> fileNames = new ArrayList<String>(Arrays.asList(folder.list()));
		if (!fileNames.contains(s)||!fileNames.contains(sh)||!fileNames.contains(sha)) {
			fail("fail");
		}
		
		Scanner scan = new Scanner("tester/index.txt");
		int counter = 0;
		while (scan.hasNextLine()) {
			counter++;
			scan.nextLine();
		}
		scan.close();
		if (counter!=3) {
			fail("fail");
		}
		
		index.removeBlobs("f.txt");
		index.removeBlobs("fi.txt");
		index.removeBlobs("fil.txt");
		
		File fold = new File("tester/objects");
		ArrayList<String> files = new ArrayList<String>(Arrays.asList(fold.list()));
		if (files.contains(s)||files.contains(sh)||files.contains(sha)) {
			fail("fail");
		}
		
		Scanner scanner = new Scanner("tester/index.txt");
		int count = 0;
		while (scanner.hasNextLine()) {
			count++;
			scanner.nextLine();
		}
		scanner.close();
		assertTrue(count==0);
	}
	void testTree() throws IOException {
		ArrayList<String> list = new ArrayList<String>();
		list.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f");
		list.add("blob : 01d82591292494afd1602d175e165f94992f6f5f");
		list.add("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83");
		list.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
		list.add("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976");
		Tree2 t = new Tree2(list);
		assertTrue(t.encryptThisString(t.generateString(list)).equals("dd4840f48a74c1f97437b515101c66834b59b1be"));
		Scanner scan = new Scanner(t.encryptThisString(t.generateString(list)));
		for (int i = 0; i < list.size(); i++) {
			assertTrue(list.get(i).equals(scan.nextLine()));
		}
	}

}
