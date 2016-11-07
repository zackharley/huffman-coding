import java.nio.file.Paths;

public class Test {

	public static void main(String[] args) {
		HuffmanManager huff1 = new HuffmanManager(Paths.get("Sam_McGee_input.txt"));
		HuffmanManager huff2 = new HuffmanManager(Paths.get("Mystery_input.txt"), Paths.get("Dictionary_input.txt"));
		huff1.encode();
		System.out.println(huff1.encodedString);
		huff2.decode();
		System.out.println(huff2.decodedString);
	}

}
