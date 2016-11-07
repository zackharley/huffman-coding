import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class HuffmanManager {
	
	String decodedString;
	String encodedString;
	List<Character> characters;
	List<Integer> frequencies;
	List<String> code;
	
	final char LINE_FEED = '<';
	
	
	public HuffmanManager(Path filename) {
		this.characters = new ArrayList<Character>();
		this.frequencies = new ArrayList<Integer>();
		this.code = new ArrayList<String>();
		List<String> strings = null;
		this.decodedString = "";
		this.encodedString = "";
		
		try {
			strings = Files.readAllLines(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(strings != null) {
			this.decodedString = String.join("\n", strings);
		}
	}
	
	public HuffmanManager(Path encodedFile, Path dictionaryFile) {
		this.characters = new ArrayList<Character>();
		this.frequencies = new ArrayList<Integer>();
		this.code = new ArrayList<String>();
		List<String> encodedLines = null;
		List<String> dictionaryLines = null;
		this.decodedString = null;
		this.encodedString = "";
		
		try {
			encodedLines = Files.readAllLines(encodedFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(encodedLines != null) {
			this.encodedString = String.join("\n", encodedLines);
		}
		
		try {
			dictionaryLines = Files.readAllLines(dictionaryFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(dictionaryLines != null) {
			dictionaryLines.forEach((line) -> {
				char c = line.charAt(0);
				
				if(c == LINE_FEED) {
					this.characters.add('\n');
					this.code.add(line.substring(5));
				} else {
					this.characters.add(c);
					this.code.add(line.substring(2));
				}
			});
		}
	}
	
	public void decode() {
		if(!encodedString.isEmpty() && !code.isEmpty()) {
			String codeSequence = "";
			char currentCharacter;
			
			for(int i = 0; i < encodedString.length(); i++) {
				currentCharacter = encodedString.charAt(i);
				codeSequence += currentCharacter;
				
				for(int j = 0; j < code.size(); j++) {
					if(codeSequence.equals(code.get(j))) {
						decodedString += characters.get(j);
						codeSequence = "";
						break;
					}
				}
			}
		}
	}
	
	public void encode() {
		if(!decodedString.isEmpty()) {
			List<String> characterStrings;
			List<Integer> characterStringsFrequencies;
			int currentFrequency;
			String currentCharacterString;
			String s1 = "";
			String s2 = "";
			int f1 = Integer.MAX_VALUE;
			int f2 = Integer.MAX_VALUE;
			int i1 = -1;
			int i2 = -1;
			int totalBits = 0;
			boolean characterExists;
			char currentCharacter;
			
			for(int i = 0; i < decodedString.length(); i++) {
				currentCharacter = decodedString.charAt(i);
				characterExists = false;
				for(int j = 0; j < characters.size(); j++) {
					if(currentCharacter == characters.get(j)) {
						frequencies.set(j, frequencies.get(j) + 1);
						characterExists = true;
						break;
					}
				}
				if(!characterExists) {
					characters.add(currentCharacter);
					frequencies.add(1);
				}
			}
		
			code = new ArrayList<String>(Collections.nCopies(frequencies.size(), ""));
			characterStrings = new ArrayList<String>(Collections.nCopies(frequencies.size(), ""));
			
			for(int i = 0; i < characters.size(); i++) {
				characterStrings.add(characters.get(i).toString());
			}
			
			characterStringsFrequencies = new ArrayList<Integer>(frequencies);
			
			while(characterStrings.size() > 1) {
				f1 = Integer.MAX_VALUE;
				f2 = Integer.MAX_VALUE;
				i1 = -1;
				i2 = -1;
				
				for(int i = 0; i < characterStringsFrequencies.size(); i++) {
					currentFrequency = characterStringsFrequencies.get(i);
					currentCharacterString = characterStrings.get(i);
					if(currentFrequency < f1) {
						f2 = f1;
						f1 = currentFrequency;
						s2 = s1;
						s1 = currentCharacterString;
						i2 = i1;
						i1 = i;
					} else if(currentFrequency < f2) {
						f2 = currentFrequency;
						s2 = currentCharacterString;
						i2 = i;
					}
				}
				
				for( int i = 0; i < s1.length(); i++) {
					for(int j = 0; j < characters.size(); j++) {
						if(s1.charAt(i) == characters.get(j)) {
							code.set(j, "0" + code.get(j));
							break;
						}
					}
				}
				
				for(int i = 0; i < s2.length(); i++) {
					for(int j = 0; j < characters.size(); j++) {
						if(s2.charAt(i) == characters.get(j)) {
							code.set(j, "1" + code.get(j));
							break;
						}
					}
				}
				
				characterStrings.remove(i1);
				characterStrings.remove(i2);
				characterStringsFrequencies.remove(i1);
//				characterStringsFrequencies.remove(i2);
				
				characterStrings.add(s1 + s2);
				characterStringsFrequencies.add(f1 + f2);
			}
			
			for(int i = 0; i < decodedString.length(); i++) {
				for(int j = 0; j < characters.size(); j++) {
					if(decodedString.charAt(i) == characters.get(j)) {
						encodedString += code.get(j);
						break;
					}
				}
			}

			for(int i = 0; i < characters.size(); i++) {
				totalBits += frequencies.get(i) * code.get(i).length();
			}
			
			System.out.println("Unencoded file size: " + decodedString.length() * 8 + " bits");
			System.out.println("Encoded file size: " + totalBits + " bits");
		} else {
			System.err.println("Input an unencoded String!");
		}
	}
	
}