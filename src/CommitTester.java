import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CommitTester {
	//PLEASE DELETE HEAD BEFORE RUNNING TEST AGAIN
	@Test
	void test() throws NoSuchAlgorithmException, IOException {
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
		Index indy5 = new Index();
		indy5.addBlobs("helloworld.txt");
		indy5.addBlobs("kian.txt");
		Commit comm5 = new Commit("fifth", "Kian");
		Index indy6 = new Index();
		indy6.delete("foo.txt");
		indy6.delete("fi.txt");
		indy6.delete("sometxt.txt");
		indy6.delete("fil.txt");
		indy6.addBlobs("cool.txt");
		Commit comm6 = new Commit ("sixth", "Kian");
	}
}